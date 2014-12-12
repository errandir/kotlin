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

package org.jetbrains.k2js.resolve.diagnostics

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.jet.lang.diagnostics.Diagnostic
import org.jetbrains.jet.lang.diagnostics.DiagnosticWithParameters1
import org.jetbrains.jet.lang.diagnostics.PositioningStrategy
import org.jetbrains.jet.lang.psi.JetExpression
import org.jetbrains.jet.renderer.Renderer
import org.jetbrains.jet.lang.diagnostics.DiagnosticWithParameters2
import org.jetbrains.jet.lang.diagnostics.ParametrizedDiagnostic

public object JsCodePositioningStrategy : PositioningStrategy<PsiElement>() {
    override fun markDiagnostic(diagnostic: ParametrizedDiagnostic<out PsiElement>): List<TextRange> {
        [suppress("UNCHECKED_CAST")]
        val diagnosticWithParameters = diagnostic as DiagnosticWithParameters2<JetExpression, String, List<TextRange>>
        return diagnosticWithParameters.getB()
    }
}