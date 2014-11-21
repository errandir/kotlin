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

package org.jetbrains.jet.plugin.caches.resolve

import org.jetbrains.jet.lang.psi.JetElement
import org.jetbrains.jet.lang.psi.JetFile
import org.jetbrains.jet.analyzer.AnalysisResult
import org.jetbrains.jet.lang.resolve.BindingContext
import org.jetbrains.jet.lang.psi.JetDeclaration
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor
import org.jetbrains.jet.lang.descriptors.ModuleDescriptor

public fun JetElement.getLazyResolveSession(): ResolutionFacade {
    return KotlinCacheService.getInstance(getProject()).getResolutionFacade(listOf(this))
}

public fun JetElement.getAnalysisResults(vararg extraFiles: JetFile): AnalysisResult {
    return KotlinCacheService.getInstance(getProject()).getAnalysisResults(listOf(this) + extraFiles.toList())
}

public fun JetDeclaration.resolveToDescriptor(): DeclarationDescriptor {
    return getLazyResolveSession().resolveToDescriptor(this)
}

public fun JetElement.resolveToElement(): BindingContext {
    return getLazyResolveSession().resolveToElement(this)
}

public fun JetElement.getModuleDescriptorForElement(): ModuleDescriptor {
    return getLazyResolveSession().getModuleDescriptorForElement(this)
}

public fun JetElement.getBindingContext(): BindingContext {
    return getAnalysisResults().bindingContext
}

public fun getAnalysisResultsForElements(elements: Collection<JetElement>): AnalysisResult {
    if (elements.isEmpty()) return AnalysisResult.EMPTY
    val element = elements.first()
    return KotlinCacheService.getInstance(element.getProject()).getAnalysisResults(elements)
}