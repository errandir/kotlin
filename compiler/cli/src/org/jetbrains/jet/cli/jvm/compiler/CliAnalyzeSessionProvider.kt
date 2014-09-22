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

package org.jetbrains.jet.cli.jvm.compiler

import org.jetbrains.jet.lang.resolve.AnalyzeSessionProvider
import org.jetbrains.jet.lang.resolve.lazy.KotlinCodeAnalyzer
import com.intellij.openapi.project.Project
import org.jetbrains.jet.lang.resolve.BindingTrace
import org.jetbrains.jet.lang.descriptors.ModuleDescriptor

public class CliAnalyzeSessionProvider(project: Project) : AnalyzeSessionProvider(project) {
    override fun initialize(trace: BindingTrace, module: ModuleDescriptor, codeAnalyzer: KotlinCodeAnalyzer?) {
        CliLightClassGenerationSupport.getInstanceForCli(project)!!.initialize(trace, module, codeAnalyzer)
    }
}