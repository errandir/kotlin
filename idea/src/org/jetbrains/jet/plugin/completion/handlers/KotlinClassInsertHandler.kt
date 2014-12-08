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

package org.jetbrains.jet.plugin.completion.handlers

import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import org.jetbrains.jet.lang.psi.JetFile
import org.jetbrains.jet.plugin.codeInsight.ShortenReferences
import org.jetbrains.jet.plugin.completion.DeclarationDescriptorLookupObject
import org.jetbrains.jet.lang.descriptors.ClassDescriptor
import org.jetbrains.jet.plugin.completion.qualifiedNameForSourceCode
import com.intellij.psi.PsiClass

public object KotlinClassInsertHandler : BaseDeclarationInsertHandler() {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        super.handleInsert(context, item)

        val file = context.getFile()
        if (file is JetFile) {
            val startOffset = context.getStartOffset()
            val document = context.getDocument()
            if (!isAfterDot(document, startOffset)) {
                val qualifiedName = qualifiedNameToInsert(item)

                // we need to insert prefix&suffix otherwise parser can sometimes produce no suitable reference here
                val tempPrefix = "$;val v:"
                val tempSuffix = ".xxx" // we add "xxx" after dot because of some bugs in resolve (see KT-5145)
                document.replaceString(startOffset, context.getTailOffset(), tempPrefix + qualifiedName + tempSuffix)

                val psiDocumentManager = PsiDocumentManager.getInstance(context.getProject())
                psiDocumentManager.commitAllDocuments()

                val classNameStart = startOffset + tempPrefix.length()
                val classNameEnd = classNameStart + qualifiedName.length()
                val rangeMarker = document.createRangeMarker(classNameStart, classNameEnd)
                val wholeRangeMarker = document.createRangeMarker(startOffset, classNameEnd + tempSuffix.length())

                ShortenReferences.process(file, classNameStart, classNameEnd)
                psiDocumentManager.commitAllDocuments()
                psiDocumentManager.doPostponedOperationsAndUnblockDocument(document)

                if (rangeMarker.isValid() && wholeRangeMarker.isValid()) {
                    document.deleteString(wholeRangeMarker.getStartOffset(), rangeMarker.getStartOffset())
                    document.deleteString(rangeMarker.getEndOffset(), wholeRangeMarker.getEndOffset())
                }
            }
        }
    }

    private fun qualifiedNameToInsert(item: LookupElement): String {
        val lookupObject = item.getObject()
        return when (lookupObject) {
            is DeclarationDescriptorLookupObject -> qualifiedNameForSourceCode(lookupObject.descriptor as ClassDescriptor)!!
            is PsiClass -> lookupObject.getQualifiedName()!!
            else -> error("Unknown object in LookupElement with KotlinClassInsertHandler: $lookupObject")
        }
    }

    private fun isAfterDot(document: Document, offset: Int): Boolean {
        var curOffset = offset
        val chars = document.getCharsSequence()
        while (curOffset > 0) {
            curOffset--
            val c = chars.charAt(curOffset)
            if (!Character.isWhitespace(c)) {
                return c == '.'
            }
        }
        return false
    }
}
