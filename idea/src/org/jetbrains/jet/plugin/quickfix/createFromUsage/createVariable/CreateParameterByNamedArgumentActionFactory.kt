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

package org.jetbrains.jet.plugin.quickfix.createFromUsage.createVariable

import org.jetbrains.jet.plugin.quickfix.JetSingleIntentionActionFactory
import org.jetbrains.jet.lang.diagnostics.Diagnostic
import com.intellij.codeInsight.intention.IntentionAction
import org.jetbrains.jet.lang.psi.JetFile
import org.jetbrains.jet.plugin.caches.resolve.analyzeFullyAndGetResult
import org.jetbrains.jet.plugin.quickfix.QuickFixUtil
import org.jetbrains.jet.plugin.quickfix.createFromUsage.callableBuilder.guessTypes
import org.jetbrains.jet.lang.types.lang.KotlinBuiltIns
import org.jetbrains.jet.plugin.refactoring.changeSignature.JetParameterInfo
import org.jetbrains.jet.lang.psi.JetValueArgument
import org.jetbrains.jet.lang.psi.JetCallElement
import org.jetbrains.jet.lang.psi.psiUtil.getStrictParentOfType
import org.jetbrains.jet.lang.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.jet.plugin.codeInsight.DescriptorToDeclarationUtil
import org.jetbrains.jet.plugin.refactoring.canRefactor
import org.jetbrains.jet.lang.psi.JetFunction
import org.jetbrains.jet.lang.psi.JetClass
import org.jetbrains.jet.lang.descriptors.FunctionDescriptor

public object CreateParameterByNamedArgumentActionFactory: JetSingleIntentionActionFactory() {
    override fun createAction(diagnostic: Diagnostic): IntentionAction? {
        val result = (diagnostic.getPsiFile() as? JetFile)?.analyzeFullyAndGetResult() ?: return null
        val context = result.bindingContext

        val argument = QuickFixUtil.getParentElementOfType(diagnostic, javaClass<JetValueArgument>()) ?: return null
        val name = argument.getArgumentName()?.getText() ?: return null
        val argumentExpression = argument.getArgumentExpression()

        val callElement = argument.getStrictParentOfType<JetCallElement>() ?: return null
        val functionDescriptor = callElement.getResolvedCall(context)?.getResultingDescriptor() as? FunctionDescriptor ?: return null
        val callable = DescriptorToDeclarationUtil.getDeclaration(callElement.getProject(), functionDescriptor) ?: return null
        if (!((callable is JetFunction || callable is JetClass) && callable.canRefactor())) return null

        val anyType = KotlinBuiltIns.getInstance().getAnyType()
        val paramType = argumentExpression?.guessTypes(context, result.moduleDescriptor)?.let {
            when (it.size()) {
                0 -> anyType
                1 -> it.first()
                else -> return null
            }
        } ?: anyType
        if (paramType.hasTypeParametersToAdd(functionDescriptor, context)) return null

        val parameterInfo = JetParameterInfo(name, paramType)
        parameterInfo.setDefaultValueText(argumentExpression.getText() ?: "")
        return CreateParameterFromUsageFix(functionDescriptor, context, parameterInfo, argument)
    }
}