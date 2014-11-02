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

package org.jetbrains.jet.plugin.decompiler.stubBuilder

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.stubs.StubElement
import org.jetbrains.jet.descriptors.serialization.ClassData
import org.jetbrains.jet.descriptors.serialization.Flags
import org.jetbrains.jet.descriptors.serialization.ProtoBuf
import org.jetbrains.jet.lang.psi.stubs.KotlinStubWithFqName
import org.jetbrains.jet.lang.psi.stubs.elements.JetClassElementType
import org.jetbrains.jet.lang.psi.stubs.impl.KotlinClassStubImpl
import org.jetbrains.jet.lang.psi.stubs.impl.KotlinObjectStubImpl
import org.jetbrains.jet.lang.resolve.java.JvmAbi
import org.jetbrains.jet.lang.resolve.name.FqName
import org.jetbrains.jet.lang.resolve.name.Name
import com.intellij.psi.PsiElement
import org.jetbrains.jet.lang.psi.JetClassBody
import org.jetbrains.jet.lang.psi.stubs.impl.KotlinPlaceHolderStubImpl
import org.jetbrains.jet.lang.psi.stubs.elements.JetStubElementTypes
import com.intellij.util.io.StringRef
import org.jetbrains.jet.lang.resolve.kotlin.KotlinBinaryClassCache
import org.jetbrains.jet.descriptors.serialization.JavaProtoBufUtil

public class CompiledClassStubBuilder(
        classData: ClassData,
        private val classFqName: FqName,
        packageFqName: FqName,
        private val parent: StubElement<out PsiElement>,
        private val file: VirtualFile
) : CompiledStubBuilderBase(classData.getNameResolver(), packageFqName) {
    private val classProto = classData.getClassProto()

    override fun getInternalFqName(name: String) = null

    public fun createStub() {
        val name = nameResolver.getName(classProto.getFqName())
        val flags = classProto.getFlags()
        val kind = Flags.CLASS_KIND.get(flags)
        val isEnumEntry = kind == ProtoBuf.Class.Kind.ENUM_ENTRY
        //TODO: inner classes
        val classOrObjectStub: KotlinStubWithFqName<*>
        val superList = getSuperList()
        if (kind == ProtoBuf.Class.Kind.OBJECT) {
            classOrObjectStub = KotlinObjectStubImpl(parent, name.asString().ref(), classFqName, superList, true, false, false, false)
        }
        else {
            classOrObjectStub =
                    KotlinClassStubImpl(
                            JetClassElementType.getStubType(isEnumEntry),
                            parent,
                            classFqName.asString().ref(),
                            name.asString().ref(),
                            superList,
                            kind == ProtoBuf.Class.Kind.TRAIT,
                            kind == ProtoBuf.Class.Kind.ENUM_CLASS,
                            false,
                            true
                    )
        }
        val classBody = KotlinPlaceHolderStubImpl<JetClassBody>(classOrObjectStub, JetStubElementTypes.CLASS_BODY)

        for (nestedNameIndex in classProto.getNestedClassNameList()) {
            val nestedName = nameResolver.getName(nestedNameIndex)
            val nestedFile = findNestedClassFile(file, nestedName)
            val kotlinBinaryClass = KotlinBinaryClassCache.getKotlinBinaryClass(nestedFile)
            val classFqName = kotlinBinaryClass.getClassId().asSingleFqName().toSafe()
            val classData = JavaProtoBufUtil.readClassDataFrom(kotlinBinaryClass.getClassHeader().annotationData)
            // TODO package name is not needed
            CompiledClassStubBuilder(classData, classFqName, classFqName, classBody, nestedFile).createStub()
        }

//        if (classProto.hasClassObject() && kind != ProtoBuf.Class.Kind.ENUM_CLASS) {
//            // TODO enum
//            val nestedFile = findNestedClassFile(file, Name.identifier(JvmAbi.CLASS_OBJECT_CLASS_NAME))
//            val kotlinBinaryClass = KotlinBinaryClassCache.getKotlinBinaryClass(nestedFile)
//            val classFqName = kotlinBinaryClass.getClassId().asSingleFqName().toSafe()
//            val classData = JavaProtoBufUtil.readClassDataFrom(kotlinBinaryClass.getClassHeader().annotationData)
//            // TODO package name is not needed
//            CompiledClassStubBuilder(classData, classFqName, classFqName.parent(), classBody, nestedFile).createStub()
//        }

        //TODO: primary constructor
        for (callableProto in classProto.getMemberList()) {
            createCallableStub(classBody, callableProto)
        }
    }

    private fun getSuperList() = classProto.getSupertypeList().map {
        type ->
        assert(type.getConstructor().getKind() == ProtoBuf.Type.Constructor.Kind.CLASS)
        val superFqName = nameResolver.getFqName(`type`.getConstructor().getId())
        superFqName.asString().ref()
    }.copyToArray()

    private fun findNestedClassFile(file: VirtualFile, innerName: Name): VirtualFile {
        val baseName = file.getNameWithoutExtension()
        val dir = file.getParent()
        assert(dir != null)

        return dir!!.findChild(baseName + "$" + innerName.asString() + ".class")
    }
}

fun String.ref() = StringRef.fromString(this)