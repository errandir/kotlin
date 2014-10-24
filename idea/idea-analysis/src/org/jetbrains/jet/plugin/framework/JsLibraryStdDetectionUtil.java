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

package org.jetbrains.jet.plugin.framework;

import com.intellij.openapi.util.io.JarUtil;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.utils.LibraryUtils;

import java.io.File;
import java.util.List;
import java.util.jar.Attributes;

public class JsLibraryStdDetectionUtil {
    public static String getJsLibraryStdVersion(@NotNull List<VirtualFile> classesRoots) {
        if (JavaRuntimeDetectionUtil.getJavaRuntimeVersion(classesRoots) != null) {
            // Prevent clashing with java runtime, in case when library collects all roots.
            return null;
        }

        VirtualFile jar = getJsStdLibJar(classesRoots);
        if (jar == null) return null;

        assert JsHeaderLibraryDetectionUtil.isJsHeaderLibraryDetected(classesRoots) : "StdLib should also be detected as headers library";

        return JarUtil.getJarAttribute(VfsUtilCore.virtualToIoFile(jar), Attributes.Name.IMPLEMENTATION_VERSION);
    }

    @Nullable
    public static VirtualFile getJsStdLibJar(@NotNull List<VirtualFile> classesRoots) {
        for (VirtualFile root : classesRoots) {
            if (root.getFileSystem().getProtocol() != StandardFileSystems.JAR_PROTOCOL) continue;

            VirtualFile jar = VfsUtilCore.getVirtualFileForJar(root);
            assert jar != null;

            if (LibraryUtils.isKotlinJavascriptStdLibrary(new File(jar.getPath()))) {
                return jar;
            }
        }

        return null;
    }
}
