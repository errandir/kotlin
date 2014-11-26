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

package org.jetbrains.jet.lang.resolve.kotlin

import org.jetbrains.jet.descriptors.serialization.JavaProtoBuf.*;
import org.jetbrains.jet.descriptors.serialization.NameResolver;
import org.jetbrains.jet.descriptors.serialization.ProtoBuf;
import org.jetbrains.jet.descriptors.serialization.descriptors.AnnotatedCallableKind;
import org.jetbrains.jet.lang.descriptors.ClassDescriptor;
import org.jetbrains.jet.lang.descriptors.ClassOrPackageFragmentDescriptor;
import org.jetbrains.jet.lang.resolve.java.resolver.ErrorReporter;
import org.jetbrains.jet.lang.resolve.name.ClassId;

import org.jetbrains.jet.lang.resolve.DescriptorUtils.isClassObject
import org.jetbrains.jet.lang.resolve.DescriptorUtils.isTrait
import org.jetbrains.jet.lang.resolve.kotlin.DescriptorLoadersStorage.MemberSignature
import org.jetbrains.jet.lang.resolve.kotlin.DeserializedResolverUtils.getClassId
import org.jetbrains.jet.lang.resolve.kotlin.DeserializedResolverUtils.kotlinClassIdToJavaClassId
import kotlin.platform.platformStatic
import org.jetbrains.jet.descriptors.serialization.descriptors.PackageProtoContainer
import org.jetbrains.jet.descriptors.serialization.descriptors.ProtoContainer
import org.jetbrains.jet.descriptors.serialization.descriptors.ClassProtoContainer
import org.jetbrains.jet.descriptors.serialization.Flags

public abstract class BaseDescriptorLoader protected(
        private val kotlinClassFinder: KotlinClassFinder,
        protected val errorReporter: ErrorReporter,
        protected val storage: DescriptorLoadersStorage
) {

    protected fun findClassWithAnnotationsAndInitializers(
            container: ProtoContainer,
            proto: ProtoBuf.Callable,
            nameResolver: NameResolver,
            annotatedCallableKind: AnnotatedCallableKind
    ): KotlinJvmBinaryClass? {
        if (container is PackageProtoContainer) {
            return findPackagePartClass(container, proto, nameResolver)
        }
        val classProto = (container as ClassProtoContainer).classProto
        val classKind = Flags.CLASS_KIND[classProto.getFlags()]
        val classId = nameResolver.getClassId(classProto.getFqName())
        if (classKind == ProtoBuf.Class.Kind.CLASS_OBJECT && isStaticFieldInOuter(proto)) {
            // Backing fields of properties of a class object are generated in the outer class
            return kotlinClassFinder.findKotlinClass(kotlinClassIdToJavaClassId(classId.getOuterClassId()))
        }
        else if (classKind == ProtoBuf.Class.Kind.TRAIT && annotatedCallableKind == AnnotatedCallableKind.PROPERTY) {
            if (proto.hasExtension(implClassName)) {
                val packageFqName = classId.getPackageFqName()
                val tImplName = nameResolver.getName(proto.getExtension(implClassName))
                // TODO: store accurate name for nested traits
                return kotlinClassFinder.findKotlinClass(ClassId(packageFqName, tImplName))
            }
            return null
        }

        return findKotlinClassByProto(classProto, nameResolver)
    }

    private fun findPackagePartClass(
            container: PackageProtoContainer,
            proto: ProtoBuf.Callable,
            nameResolver: NameResolver
    ): KotlinJvmBinaryClass? {
        if (proto.hasExtension(implClassName)) {
            return kotlinClassFinder.findKotlinClass(ClassId(container.fqName, nameResolver.getName(proto.getExtension(implClassName))))
        }
        return null
    }

    protected fun findKotlinClassByProto(classProto: ProtoBuf.Class, nameResolver: NameResolver): KotlinJvmBinaryClass? {
        val classId = nameResolver.getClassId(classProto.getFqName())
        return kotlinClassFinder.findKotlinClass(kotlinClassIdToJavaClassId(classId))
    }

    class object {
        platformStatic fun getCallableSignature(
                proto: ProtoBuf.Callable,
                nameResolver: NameResolver,
                kind: AnnotatedCallableKind
        ): MemberSignature? {
            val deserializer = SignatureDeserializer(nameResolver)
            when (kind) {
                AnnotatedCallableKind.FUNCTION -> if (proto.hasExtension(methodSignature)) {
                    return deserializer.methodSignature(proto.getExtension(methodSignature))
                }
                AnnotatedCallableKind.PROPERTY_GETTER -> if (proto.hasExtension(propertySignature)) {
                    return deserializer.methodSignature(proto.getExtension(propertySignature).getGetter())
                }
                AnnotatedCallableKind.PROPERTY_SETTER -> if (proto.hasExtension(propertySignature)) {
                    return deserializer.methodSignature(proto.getExtension(propertySignature).getSetter())
                }
                AnnotatedCallableKind.PROPERTY -> if (proto.hasExtension(propertySignature)) {
                    val propertySignature = proto.getExtension(propertySignature)

                    if (propertySignature.hasField()) {
                        val field = propertySignature.getField()
                        val type = deserializer.typeDescriptor(field.getType())
                        val name = nameResolver.getName(field.getName())
                        return MemberSignature.fromFieldNameAndDesc(name, type)
                    }
                    else if (propertySignature.hasSyntheticMethod()) {
                        return deserializer.methodSignature(propertySignature.getSyntheticMethod())
                    }
                }
            }
            return null
        }

        private fun isStaticFieldInOuter(proto: ProtoBuf.Callable): Boolean {
            if (!proto.hasExtension(propertySignature)) return false
            val propertySignature = proto.getExtension(propertySignature)
            return propertySignature.hasField() && propertySignature.getField().getIsStaticInOuter()
        }
    }
}
