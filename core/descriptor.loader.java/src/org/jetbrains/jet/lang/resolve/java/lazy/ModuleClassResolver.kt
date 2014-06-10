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

package org.jetbrains.jet.lang.resolve.java.lazy

import org.jetbrains.jet.lang.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.jet.lang.resolve.java.structure.JavaClass
import org.jetbrains.jet.lang.resolve.java.JavaDescriptorResolver
import org.jetbrains.jet.lang.descriptors.ClassDescriptor

trait ModuleClassResolver {
    fun resolveClass(javaClass: JavaClass): ClassDescriptor?
}

public class DefaultModuleClassResolver(): ModuleClassResolver {
    override fun resolveClass(javaClass: JavaClass): ClassDescriptor? {
        //TODO:
        throw UnsupportedOperationException()
    }
}

public class ModuleClassResolverImpl(val analyzerByJavaClass: (JavaClass) -> JavaDescriptorResolver): ModuleClassResolver {
    override fun resolveClass(javaClass: JavaClass): ClassDescriptor? = analyzerByJavaClass(javaClass).resolveClass(javaClass)
}