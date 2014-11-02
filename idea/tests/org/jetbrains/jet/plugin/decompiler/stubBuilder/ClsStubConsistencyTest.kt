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

import org.jetbrains.jet.plugin.JetLightCodeInsightFixtureTestCase
import org.jetbrains.jet.lang.resolve.name.FqName
import org.jetbrains.jet.lang.resolve.java.PackageClassUtils
import org.jetbrains.jet.lang.resolve.kotlin.VirtualFileFinderFactory
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.jet.plugin.decompiler.textBuilder.buildDecompiledText
import org.jetbrains.jet.plugin.JetWithJdkAndRuntimeLightProjectDescriptor
import org.jetbrains.jet.plugin.PluginTestCaseBase
import com.intellij.util.indexing.FileContentImpl
import org.jetbrains.jet.lang.psi.JetPsiFactory
import org.jetbrains.jet.lang.psi.stubs.elements.JetFileStubBuilder
import org.junit.Assert
import org.jetbrains.jet.plugin.JetLightProjectDescriptor

public class ClsStubConsistencyTest : JetLightCodeInsightFixtureTestCase() {

    private val STANDARD_LIBRARY_FQNAME = FqName("kotlin")

    public fun testConsistencyForKotlinPackage() {
        //TODO_R: test kotlin package
        val project = getProject()
        val packageClassFqName = FqName("kotlin.KotlinPackage")
        val virtualFileFinder = VirtualFileFinderFactory.SERVICE.getInstance(project).create(GlobalSearchScope.allScope(project))
        val kotlinPackageFile = virtualFileFinder.findVirtualFileWithHeader(packageClassFqName)!!
        val decompiledText = buildDecompiledText(kotlinPackageFile).text

        //TODO_R: sout
        println(decompiledText)
        val clsStub = KotlinClsStubBuilder().buildFileStub(FileContentImpl.createByFile(kotlinPackageFile))!!
        val fileWithDecompiledText = JetPsiFactory(project).createFile(decompiledText)

        val stubTreeFromDecompiledText = JetFileStubBuilder().buildStubTree(fileWithDecompiledText)

        val expectedText = stubTreeFromDecompiledText.serializeToString()
        Assert.assertEquals(expectedText, clsStub.serializeToString())
    }

    override fun getProjectDescriptor() = JetWithJdkAndRuntimeLightProjectDescriptor.INSTANCE
}