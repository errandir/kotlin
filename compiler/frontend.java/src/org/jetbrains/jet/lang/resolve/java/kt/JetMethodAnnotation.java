/*
 * Copyright 2010-2012 JetBrains s.r.o.
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

package org.jetbrains.jet.lang.resolve.java.kt;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.resolve.java.JavaDescriptorResolver;
import org.jetbrains.jet.lang.resolve.java.JvmStdlibNames;

/**
 * @author Stepan Koltsov
 * @author alex.tkachman
 */
public class JetMethodAnnotation extends PsiAnnotationWithFlags {

    private String typeParameters;
    private String returnType;
    private String propertyType;

    public JetMethodAnnotation(@Nullable PsiAnnotation psiAnnotation) {
        super(psiAnnotation);
    }

    @Override
    protected void initialize() {
        super.initialize();
        typeParameters = getStringAttribute(JvmStdlibNames.JET_METHOD_TYPE_PARAMETERS_FIELD, "");
        returnType = getStringAttribute(JvmStdlibNames.JET_METHOD_RETURN_TYPE_FIELD, "");
        propertyType = getStringAttribute(JvmStdlibNames.JET_METHOD_PROPERTY_TYPE_FIELD, "");
    }

    public int kind() {
        return flags() & JvmStdlibNames.FLAG_KIND_MASK;
    }

    @NotNull
    public String typeParameters() {
        checkInitialized();
        return typeParameters;
    }

    @NotNull
    public String returnType() {
        checkInitialized();
        return returnType;
    }

    public boolean returnTypeNullable() {
        return (flags() & JvmStdlibNames.FLAG_NULLABLE_RETURN_TYPE_BIT) != 0;
    }

    @NotNull
    public String propertyType() {
        checkInitialized();
        return propertyType;
    }

    public static JetMethodAnnotation get(PsiMethod psiMethod) {
        return new JetMethodAnnotation(JavaDescriptorResolver.findAnnotation(psiMethod, JvmStdlibNames.JET_METHOD.getFqName().getFqName()));
    }
}
