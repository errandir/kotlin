/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.lang.resolve.lazy.descriptors

import com.google.common.collect.Lists
import org.jetbrains.jet.lang.descriptors.*
import org.jetbrains.jet.lang.descriptors.impl.ConstructorDescriptorImpl
import org.jetbrains.jet.lang.diagnostics.Errors
import org.jetbrains.jet.lang.psi.*
import org.jetbrains.jet.lang.resolve.*
import org.jetbrains.jet.lang.resolve.dataClassUtils.*
import org.jetbrains.jet.lang.resolve.lazy.ResolveSession
import org.jetbrains.jet.lang.resolve.lazy.declarations.ClassMemberDeclarationProvider
import org.jetbrains.jet.lang.resolve.name.Name
import org.jetbrains.jet.lang.resolve.scopes.JetScope
import org.jetbrains.jet.lang.types.DeferredType
import org.jetbrains.jet.lang.types.JetType
import org.jetbrains.jet.lang.types.lang.KotlinBuiltIns
import org.jetbrains.jet.storage.NullableLazyValue

import java.util.*

import org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor.Kind.DELEGATION
import org.jetbrains.jet.lang.descriptors.CallableMemberDescriptor.Kind.FAKE_OVERRIDE
import org.jetbrains.jet.lang.resolve.DelegationResolver.generateDelegatedMembers
import org.jetbrains.jet.storage.NotNullLazyValue
import org.jetbrains.jet.lang.resolve.scopes.DescriptorKindFilter

public open class LazyClassMemberScope(resolveSession: ResolveSession,
                                  declarationProvider: ClassMemberDeclarationProvider,
                                  thisClass: LazyClassDescriptor,
                                  trace: BindingTrace)
: AbstractLazyMemberScope<LazyClassDescriptor, ClassMemberDeclarationProvider>(resolveSession, declarationProvider, thisClass, trace) {

    private val descriptorsFromDeclaredElements = storageManager.createLazyValue {
        computeDescriptorsFromDeclaredElements(DescriptorKindFilter.ALL, JetScope.ALL_NAME_FILTER)
    }
    private val extraDescriptors: NotNullLazyValue<Collection<DeclarationDescriptor>> = storageManager.createLazyValue {
        computeExtraDescriptors()
    }

    override fun getDescriptors(kindFilter: DescriptorKindFilter,
                                nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> {
        val result = LinkedHashSet(descriptorsFromDeclaredElements())
        result.addAll(extraDescriptors())
        return result
    }

    protected open fun computeExtraDescriptors(): Collection<DeclarationDescriptor> {
        val result = ArrayList<DeclarationDescriptor>()
        for (supertype in thisDescriptor.getTypeConstructor().getSupertypes()) {
            for (descriptor in supertype.getMemberScope().getDescriptors()) {
                if (descriptor is FunctionDescriptor) {
                    result.addAll(getFunctions(descriptor.getName()))
                }
                else if (descriptor is PropertyDescriptor) {
                    result.addAll(getProperties(descriptor.getName()))
                }
                // Nothing else is inherited
            }
        }

        addDataClassMethods(result)

        result.trimToSize()
        return result
    }

    private trait MemberExtractor<T : CallableMemberDescriptor> {
        public fun extract(extractFrom: JetType, name: Name): Collection<T>
    }

    private val primaryConstructor: NullableLazyValue<ConstructorDescriptor>
            = resolveSession.getStorageManager().createNullableLazyValue { resolvePrimaryConstructor() }

    override fun getScopeForMemberDeclarationResolution(declaration: JetDeclaration): JetScope {
        if (declaration is JetProperty) {
            return thisDescriptor.getScopeForInitializerResolution()
        }
        return thisDescriptor.getScopeForMemberDeclarationResolution()
    }

    private fun <D : CallableMemberDescriptor> generateFakeOverrides(name: Name, fromSupertypes: Collection<D>, result: MutableCollection<D>, exactDescriptorClass: Class<out D>) {
        OverridingUtil.generateOverridesInFunctionGroup(name, fromSupertypes, ArrayList(result), thisDescriptor, object : OverridingUtil.DescriptorSink {
            override fun addToScope(fakeOverride: CallableMemberDescriptor) {
                assert(exactDescriptorClass.isInstance(fakeOverride)) { "Wrong descriptor type in an override: " + fakeOverride + " while expecting " + exactDescriptorClass.getSimpleName() }
                [suppress("UNCHECKED_CAST")]
                result.add(fakeOverride as D)
            }

            override fun conflict(fromSuper: CallableMemberDescriptor, fromCurrent: CallableMemberDescriptor) {
                val declaration = DescriptorToSourceUtils.descriptorToDeclaration(fromCurrent) as JetDeclaration?
                assert(declaration != null, "fromCurrent can not be a fake override")
                trace.report(Errors.CONFLICTING_OVERLOADS.on(declaration, fromCurrent, fromCurrent.getContainingDeclaration().getName().asString()))
            }
        })
        OverrideResolver.resolveUnknownVisibilities(result, trace)
    }

    override fun getFunctions(name: Name): Collection<FunctionDescriptor> {
        // TODO: this should be handled by lazy function descriptors
        val functions = super.getFunctions(name)
        resolveUnknownVisibilitiesForMembers(functions)
        return functions
    }

    protected override fun getNonDeclaredFunctions(name: Name, result: MutableSet<FunctionDescriptor>) {
        val fromSupertypes = Lists.newArrayList<FunctionDescriptor>()
        for (supertype in thisDescriptor.getTypeConstructor().getSupertypes()) {
            fromSupertypes.addAll(supertype.getMemberScope().getFunctions(name))
        }
        result.addAll(generateDelegatingDescriptors(name, EXTRACT_FUNCTIONS, result))
        generateDataClassMethods(result, name)
        generateFakeOverrides(name, fromSupertypes, result, javaClass<FunctionDescriptor>())
    }

    private fun generateDataClassMethods(result: MutableCollection<FunctionDescriptor>, name: Name) {
        if (!KotlinBuiltIns.getInstance().isData(thisDescriptor)) return

        val constructor = getPrimaryConstructor()
        if (constructor == null) return

        val primaryConstructorParameters = declarationProvider.getOwnerInfo().getPrimaryConstructorParameters()
        assert(constructor.getValueParameters().size() == primaryConstructorParameters.size()) { "From descriptor: " + constructor.getValueParameters().size() + " but from PSI: " + primaryConstructorParameters.size() }

        if (isComponentLike(name)) {
            var componentIndex = 0

            for (parameter in constructor.getValueParameters()) {
                if (parameter.getType().isError()) continue
                if (!primaryConstructorParameters.get(parameter.getIndex()).hasValOrVarNode()) continue

                val properties = getProperties(parameter.getName())
                if (properties.isEmpty()) continue

                assert(properties.size() == 1) { "A constructor parameter is resolved to more than one (" + properties.size() + ") property: " + parameter }

                val property = properties.iterator().next() as PropertyDescriptor

                ++componentIndex

                if (name == createComponentName(componentIndex)) {
                    val functionDescriptor = DescriptorResolver.createComponentFunctionDescriptor(componentIndex, property, parameter, thisDescriptor, trace)
                    result.add(functionDescriptor)
                    break
                }
            }
        }

        if (name == DescriptorResolver.COPY_METHOD_NAME) {
            for (parameter in constructor.getValueParameters()) {
                // force properties resolution to fill BindingContext.VALUE_PARAMETER_AS_PROPERTY slice
                getProperties(parameter.getName())
            }

            val copyFunctionDescriptor = DescriptorResolver.createCopyFunctionDescriptor(constructor.getValueParameters(), thisDescriptor, trace)
            result.add(copyFunctionDescriptor)
        }
    }

    override fun getProperties(name: Name): Collection<VariableDescriptor> {
        // TODO: this should be handled by lazy property descriptors
        val properties = super.getProperties(name)
        resolveUnknownVisibilitiesForMembers(properties as Collection<CallableMemberDescriptor>)
        return properties
    }

    private fun resolveUnknownVisibilitiesForMembers(descriptors: Collection<CallableMemberDescriptor>) {
        for (descriptor in descriptors) {
            if (descriptor.getKind() != FAKE_OVERRIDE && descriptor.getKind() != DELEGATION) {
                OverridingUtil.resolveUnknownVisibilityForMember(descriptor, OverrideResolver.createCannotInferVisibilityReporter(trace))
            }
        }
    }

    [suppress("UNCHECKED_CAST")]
    protected override fun getNonDeclaredProperties(name: Name, result: MutableSet<VariableDescriptor>) {
        createPropertiesFromPrimaryConstructorParameters(name, result)

        // Members from supertypes
        val fromSupertypes = ArrayList<PropertyDescriptor>()
        for (supertype in thisDescriptor.getTypeConstructor().getSupertypes()) {
            fromSupertypes.addAll(supertype.getMemberScope().getProperties(name) as Collection<PropertyDescriptor>)
        }
        result.addAll(generateDelegatingDescriptors(name, EXTRACT_PROPERTIES, result))
        generateFakeOverrides(name, fromSupertypes, result as MutableCollection<PropertyDescriptor>, javaClass<PropertyDescriptor>())
    }

    protected open fun createPropertiesFromPrimaryConstructorParameters(name: Name, result: MutableSet<VariableDescriptor>) {
        val classInfo = declarationProvider.getOwnerInfo()

        // From primary constructor parameters
        val primaryConstructor = getPrimaryConstructor() ?: return

        val valueParameterDescriptors = primaryConstructor.getValueParameters()
        val primaryConstructorParameters = classInfo.getPrimaryConstructorParameters()
        assert(valueParameterDescriptors.size() == primaryConstructorParameters.size()) {
            "From descriptor: ${valueParameterDescriptors.size()} but from PSI: ${primaryConstructorParameters.size()}"
        }

        for (valueParameterDescriptor in valueParameterDescriptors) {
            if (name != valueParameterDescriptor.getName()) continue

            val parameter = primaryConstructorParameters.get(valueParameterDescriptor.getIndex())
            if (parameter.hasValOrVarNode()) {
                val propertyDescriptor = resolveSession.getDescriptorResolver().resolvePrimaryConstructorParameterToAProperty(
                        thisDescriptor, valueParameterDescriptor, thisDescriptor.getScopeForClassHeaderResolution(), parameter, trace)
                result.add(propertyDescriptor)
            }
        }
    }

    private fun <T : CallableMemberDescriptor> generateDelegatingDescriptors(name: Name, extractor: MemberExtractor<T>, existingDescriptors: Collection<CallableDescriptor>): Collection<T> {
        val classOrObject = declarationProvider.getOwnerInfo().getCorrespondingClassOrObject()
            ?: return setOf() // Enum class objects do not have delegated members

        val lazyTypeResolver = DelegationResolver.TypeResolver { reference ->
            resolveSession.getTypeResolver().resolveType(thisDescriptor.getScopeForClassHeaderResolution(), reference, trace, false)
        }
        val lazyMemberExtractor = DelegationResolver.MemberExtractor<T> {
            type -> extractor.extract(type, name)
        }
        return generateDelegatedMembers(classOrObject, thisDescriptor, existingDescriptors, trace, lazyMemberExtractor, lazyTypeResolver)
    }

    private fun addDataClassMethods(result: MutableCollection<DeclarationDescriptor>) {
        if (!KotlinBuiltIns.getInstance().isData(thisDescriptor)) return

        if (getPrimaryConstructor() == null) return

        // Generate componentN functions until there's no such function for some n
        var n = 1
        while (true) {
            val componentName = createComponentName(n)
            val functions = getFunctions(componentName)
            if (functions.isEmpty()) break

            result.addAll(functions)

            n++
        }
        result.addAll(getFunctions(Name.identifier("copy")))
    }

    override fun getPackage(name: Name): PackageViewDescriptor? = null

    public fun getConstructors(): Set<ConstructorDescriptor> {
        val constructor = getPrimaryConstructor()
        return if (constructor != null) setOf(constructor) else setOf()
    }

    public fun getPrimaryConstructor(): ConstructorDescriptor? = primaryConstructor()

    protected open fun resolvePrimaryConstructor(): ConstructorDescriptor? {
        if (GENERATE_CONSTRUCTORS_FOR.contains(thisDescriptor.getKind())) {
            val ownerInfo = declarationProvider.getOwnerInfo()
            val classOrObject = ownerInfo.getCorrespondingClassOrObject()
            if (!thisDescriptor.getKind().isSingleton()) {
                assert(classOrObject is JetClass) { "No JetClass for $thisDescriptor" }
                classOrObject as JetClass
                val constructor = resolveSession.getDescriptorResolver().resolvePrimaryConstructorDescriptor(
                        thisDescriptor.getScopeForClassHeaderResolution(), thisDescriptor, classOrObject, trace)
                assert(constructor != null) { "No constructor created for $thisDescriptor" }
                setDeferredReturnType(constructor)
                return constructor
            }
            else {
                val constructor = DescriptorResolver.createAndRecordPrimaryConstructorForObject(classOrObject, thisDescriptor, trace)
                setDeferredReturnType(constructor)
                return constructor
            }
        }
        return null
    }

    protected fun setDeferredReturnType(descriptor: ConstructorDescriptorImpl) {
        descriptor.setReturnType(DeferredType.create(resolveSession.getStorageManager(), trace, { thisDescriptor.getDefaultType() }))
    }

    // Do not add details here, they may compromise the laziness during debugging
    override fun toString() = "lazy scope for class ${thisDescriptor.getName()}"

    class object {
        private val GENERATE_CONSTRUCTORS_FOR = setOf(ClassKind.CLASS,
                                                      ClassKind.ANNOTATION_CLASS,
                                                      ClassKind.OBJECT,
                                                      ClassKind.ENUM_CLASS,
                                                      ClassKind.ENUM_ENTRY,
                                                      ClassKind.CLASS_OBJECT)

        private val EXTRACT_FUNCTIONS: MemberExtractor<FunctionDescriptor> = object : MemberExtractor<FunctionDescriptor> {
            override fun extract(extractFrom: JetType, name: Name): Collection<FunctionDescriptor> {
                return extractFrom.getMemberScope().getFunctions(name)
            }
        }

        private val EXTRACT_PROPERTIES: MemberExtractor<PropertyDescriptor> = object : MemberExtractor<PropertyDescriptor> {
            override fun extract(extractFrom: JetType, name: Name): Collection<PropertyDescriptor> {
                [suppress("UNCHECKED_CAST")]
                return extractFrom.getMemberScope().getProperties(name) as Collection<PropertyDescriptor>
            }
        }
    }
}