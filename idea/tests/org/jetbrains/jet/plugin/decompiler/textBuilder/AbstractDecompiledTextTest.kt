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

package org.jetbrains.jet.plugin.decompiler.textBuilder

import com.intellij.psi.PsiManager
import com.intellij.psi.impl.compiled.ClsFileImpl
import com.intellij.testFramework.LightProjectDescriptor
import org.jetbrains.jet.plugin.JdkAndMockLibraryProjectDescriptor
import org.jetbrains.jet.plugin.PluginTestCaseBase
import com.intellij.testFramework.UsefulTestCase.*
import org.junit.Assert.*
import org.jetbrains.jet.plugin.JetLightCodeInsightFixtureTestCase
import org.jetbrains.jet.plugin.JetLightProjectDescriptor
import org.jetbrains.jet.plugin.decompiler.navigation.NavigateToDecompiledLibraryTest
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.PsiErrorElement
import org.jetbrains.jet.lang.psi.JetPsiUtil
import org.jetbrains.jet.lang.psi.JetFile
import com.intellij.psi.PsiFile

public abstract class AbstractDecompiledTextTest() : JetLightCodeInsightFixtureTestCase() {

    private val TEST_DATA_PATH = PluginTestCaseBase.getTestDataPathBase() + "/decompiler/decompiledText"

    public fun doTest(path: String) {
        val classFile = NavigateToDecompiledLibraryTest.getClassFile("test", getTestName(false), myModule!!)
        val clsFile = PsiManager.getInstance(getProject()!!).findFile(classFile)
        assertTrue("Expecting decompiled kotlin file, was: " + clsFile!!.javaClass, clsFile is JetFile)
        assertSameLinesWithFile(path.substring(0, path.length - 1) + ".expected.kt", clsFile.getText())
        checkThatFileWasParsedCorrectly(clsFile)
    }

    override fun getProjectDescriptor(): LightProjectDescriptor {
        if (isAllFilesPresentInTest()) {
            return JetLightProjectDescriptor.INSTANCE
        }
        return JdkAndMockLibraryProjectDescriptor(TEST_DATA_PATH + "/" + getTestName(false), false)
    }

    private fun checkThatFileWasParsedCorrectly(clsFile: PsiFile) {
        clsFile.accept(object : PsiRecursiveElementVisitor() {
            override fun visitErrorElement(element: PsiErrorElement) {
                fail("Decompiled file should not contain error elements!\n${JetPsiUtil.getElementTextWithContext(element)}")
            }
        })
    }
}
