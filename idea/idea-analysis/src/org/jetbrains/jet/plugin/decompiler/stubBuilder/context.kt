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

import org.jetbrains.jet.descriptors.serialization.NameResolver
import org.jetbrains.jet.lang.resolve.name.Name
import org.jetbrains.jet.lang.resolve.name.FqName

//TODO: naming all over the place
public class MemberFqNameProvider(private val parentFqName: FqName) {
    fun getFqNameForMember(name: Name) = parentFqName.child(name)
}

//TODO_R: this should store a map from id to name, otherwise some inconsistencies may arise
//TODO: share lists
class TypeParameterContext(val typeParameters: List<Name>) {
    fun inner(names: List<Name>) = TypeParameterContext(typeParameters + names)

    internal class object {
        val EMPTY = TypeParameterContext(listOf())
    }
}

class ClsStubBuilderContext(
        val nameResolver: NameResolver,
        val memberFqNameProvider: MemberFqNameProvider,
        val typeParameters: TypeParameterContext
)

fun ClsStubBuilderContext.withTypeParameters(newTypeParameters: List<Name>) = ClsStubBuilderContext(
        this.nameResolver,
        this.memberFqNameProvider,
        this.typeParameters.inner(newTypeParameters)
)

