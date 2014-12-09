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

package org.jetbrains.jet.plugin.caches.resolve;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.compiled.ClsFileImpl;
import com.intellij.psi.impl.java.stubs.PsiJavaFileStub;
import com.intellij.psi.impl.java.stubs.impl.PsiJavaFileStubImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.PsiClassHolderFileStub;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.cls.ClsFormatException;
import kotlin.Function1;
import kotlin.KotlinPackage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.asJava.*;
import org.jetbrains.jet.lang.descriptors.ClassDescriptor;
import org.jetbrains.jet.lang.descriptors.FunctionDescriptor;
import org.jetbrains.jet.lang.descriptors.PackageViewDescriptor;
import org.jetbrains.jet.lang.descriptors.VariableDescriptor;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.resolve.java.JvmAbi;
import org.jetbrains.jet.lang.resolve.kotlin.PackagePartClassUtils;
import org.jetbrains.jet.lang.resolve.lazy.ForceResolveUtil;
import org.jetbrains.jet.lang.resolve.lazy.KotlinCodeAnalyzer;
import org.jetbrains.jet.lang.resolve.name.FqName;
import org.jetbrains.jet.lang.resolve.name.Name;
import org.jetbrains.jet.plugin.decompiler.navigation.JetSourceNavigationHelper;
import org.jetbrains.jet.plugin.project.ResolveSessionForBodies;
import org.jetbrains.jet.plugin.stubindex.JetTopLevelClassByPackageIndex;
import org.jetbrains.jet.plugin.stubindex.JetFullClassNameIndex;
import org.jetbrains.jet.plugin.stubindex.PackageIndexUtil;
import org.jetbrains.jet.plugin.util.ProjectRootsUtil;

import java.io.IOException;
import java.util.*;

import static org.jetbrains.jet.plugin.stubindex.JetSourceFilterScope.kotlinSourceAndClassFiles;

public class IDELightClassGenerationSupport extends LightClassGenerationSupport {

    private static final Logger LOG = Logger.getInstance(IDELightClassGenerationSupport.class);

    private final Project project;

    private final Comparator<JetFile> jetFileComparator;
    private final PsiManager psiManager;

    public IDELightClassGenerationSupport(@NotNull Project project) {
        this.project = project;
        this.jetFileComparator = byScopeComparator(GlobalSearchScope.allScope(project));
        this.psiManager = PsiManager.getInstance(project);
    }


    @NotNull
    @Override
    public LightClassConstructionContext getContextForPackage(@NotNull Collection<JetFile> files) {
        assert !files.isEmpty() : "No files in package";

        List<JetFile> sortedFiles = new ArrayList<JetFile>(files);
        Collections.sort(sortedFiles, jetFileComparator);

        JetFile file = sortedFiles.get(0);
        ResolveSessionForBodies session = KotlinCacheService.OBJECT$.getInstance(file.getProject()).getLazyResolveSession(file);
        forceResolvePackageDeclarations(files, session);
        return new LightClassConstructionContext(session.getBindingContext(), session.getModuleDescriptor());
    }

    @NotNull
    @Override
    public LightClassConstructionContext getContextForClassOrObject(@NotNull JetClassOrObject classOrObject) {
        ResolveSessionForBodies session =
                KotlinCacheService.OBJECT$.getInstance(classOrObject.getProject()).getLazyResolveSession(classOrObject);

        if (classOrObject.isLocal()) {
            BindingContext bindingContext = session.resolveToElement(classOrObject);
            ClassDescriptor descriptor = bindingContext.get(BindingContext.CLASS, classOrObject);

            if (descriptor == null) {
                LOG.warn("No class descriptor in context for class: " + JetPsiUtil.getElementTextWithContext(classOrObject));
                return new LightClassConstructionContext(bindingContext, session.getModuleDescriptor());
            }

            ForceResolveUtil.forceResolveAllContents(descriptor);

            return new LightClassConstructionContext(bindingContext, session.getModuleDescriptor());
        }

        ForceResolveUtil.forceResolveAllContents(session.getClassDescriptor(classOrObject));
        return new LightClassConstructionContext(session.getBindingContext(), session.getModuleDescriptor());
    }

    private static void forceResolvePackageDeclarations(@NotNull Collection<JetFile> files, @NotNull KotlinCodeAnalyzer session) {
        for (JetFile file : files) {
            // SCRIPT: not supported
            if (file.isScript()) continue;

            FqName packageFqName = file.getPackageFqName();

            // make sure we create a package descriptor
            PackageViewDescriptor packageDescriptor = session.getModuleDescriptor().getPackage(packageFqName);
            if (packageDescriptor == null) {
                LOG.warn("No descriptor found for package " + packageFqName + " in file " + file.getName() + "\n" + file.getText());
                session.forceResolveAll();
                continue;
            }

            for (JetDeclaration declaration : file.getDeclarations()) {
                if (declaration instanceof JetFunction) {
                    JetFunction jetFunction = (JetFunction) declaration;
                    Name name = jetFunction.getNameAsSafeName();
                    Collection<FunctionDescriptor> functions = packageDescriptor.getMemberScope().getFunctions(name);
                    for (FunctionDescriptor descriptor : functions) {
                        ForceResolveUtil.forceResolveAllContents(descriptor);
                    }
                }
                else if (declaration instanceof JetProperty) {
                    JetProperty jetProperty = (JetProperty) declaration;
                    Name name = jetProperty.getNameAsSafeName();
                    Collection<VariableDescriptor> properties = packageDescriptor.getMemberScope().getProperties(name);
                    for (VariableDescriptor descriptor : properties) {
                        ForceResolveUtil.forceResolveAllContents(descriptor);
                    }
                }
                else if (declaration instanceof JetClassOrObject) {
                    // Do nothing: we are not interested in classes
                }
                else {
                    LOG.error("Unsupported declaration kind: " + declaration + " in file " + file.getName() + "\n" + file.getText());
                }
            }
        }
    }

    @NotNull
    @Override
    public Collection<JetClassOrObject> findClassOrObjectDeclarations(@NotNull FqName fqName, @NotNull GlobalSearchScope searchScope) {
        return JetFullClassNameIndex.getInstance().get(fqName.asString(), project, kotlinSourceAndClassFiles(searchScope, project));
    }

    @NotNull
    @Override
    public Collection<JetFile> findFilesForPackage(@NotNull FqName fqName, @NotNull GlobalSearchScope searchScope) {
        return PackageIndexUtil.findFilesWithExactPackage(fqName, kotlinSourceAndClassFiles(searchScope, project), project);
    }

    @NotNull
    private static Map<IdeaModuleInfo, List<JetFile>> groupByModuleInfo(@NotNull Collection<JetFile> allFiles) {
        return KotlinPackage.groupByTo(
                allFiles,
                new LinkedHashMap<IdeaModuleInfo, List<JetFile>>(),
                new Function1<JetFile, IdeaModuleInfo>() {
                    @Override
                    public IdeaModuleInfo invoke(JetFile file) {
                        return ResolvePackage.getModuleInfo(file);
                    }
                });
    }

    @NotNull
    @Override
    public Collection<JetClassOrObject> findClassOrObjectDeclarationsInPackage(
            @NotNull FqName packageFqName, @NotNull GlobalSearchScope searchScope
    ) {
        return JetTopLevelClassByPackageIndex.getInstance().get(
                packageFqName.asString(), project, kotlinSourceAndClassFiles(searchScope, project)
        );
    }

    @Override
    public boolean packageExists(@NotNull FqName fqName, @NotNull GlobalSearchScope scope) {
        return PackageIndexUtil.packageExists(fqName, kotlinSourceAndClassFiles(scope, project), project);
    }

    @NotNull
    @Override
    public Collection<FqName> getSubPackages(@NotNull FqName fqn, @NotNull GlobalSearchScope scope) {
        return PackageIndexUtil.getSubPackageFqNames(fqn, kotlinSourceAndClassFiles(scope, project), project);
    }

    @Nullable
    @Override
    public PsiClass getPsiClass(@NotNull JetClassOrObject classOrObject) {
        VirtualFile virtualFile = classOrObject.getContainingFile().getVirtualFile();
        if (virtualFile != null) {
            if (ProjectRootsUtil.isLibraryClassFile(project, virtualFile)) {
                //TODO_R: extract
                JetFile containingJetFile = classOrObject.getContainingJetFile();
                PsiClass baseClass = getClsClassForKotlinCompiledFiles(containingJetFile);
                if (baseClass == null) return null;

                FqName packageFqName = containingJetFile.getPackageFqName();
                FqName classFqName = getClassFqName(classOrObject);
                List<Name> relativeSegments =
                        classFqName.pathSegments().subList(packageFqName.pathSegments().size(), classFqName.pathSegments().size());
                Iterator<Name> iterator = relativeSegments.iterator();
                Name base = iterator.next();
                assert baseClass.getName().equals(base.asString());
                while (iterator.hasNext()) {
                    Name name = iterator.next();
                    PsiClass innerClass = baseClass.findInnerClassByName(name.asString(), false);
                    assert innerClass != null : "Inner class should be found";
                    baseClass = innerClass;
                }
                return baseClass;
            }
            else if (ProjectRootsUtil.isLibraryFile(project, virtualFile)) {
                return JetSourceNavigationHelper.getOriginalClass(classOrObject);
            }
        }

        //TODO_R: really create in all other cases?
        return KotlinLightClassForExplicitDeclaration.create(psiManager, classOrObject);
    }

    //TODO: object in class object / class / class object / class stuff
    private static FqName getClassFqName(@NotNull JetClassOrObject classOrObject) {
        Name name = classOrObject.getNameAsName();
        if (name == null) {
            assert classOrObject instanceof JetObjectDeclaration && ((JetObjectDeclaration) classOrObject).isClassObject();
            name = Name.identifier(JvmAbi.CLASS_OBJECT_CLASS_NAME);
        }
        JetClassOrObject parent = PsiTreeUtil.getParentOfType(classOrObject, JetClassOrObject.class, true);
        if (parent == null) {
            assert classOrObject.isTopLevel();
            return classOrObject.getContainingJetFile().getPackageFqName().child(name);
        }
        return getClassFqName(parent).child(name);
    }

    @NotNull
    @Override
    public Collection<PsiClass> getPackageClasses(@NotNull FqName packageFqName, @NotNull GlobalSearchScope scope) {
        List<PsiClass> result = new ArrayList<PsiClass>();
        List<KotlinLightPackageClassInfo> packageClassesInfos = findPackageClassesInfos(packageFqName, scope);
        for (KotlinLightPackageClassInfo info : packageClassesInfos) {
            Collection<JetFile> files = info.getFiles();
            List<JetFile> filesWithCallables = PackagePartClassUtils.getPackageFilesWithCallables(files);
            if (filesWithCallables.isEmpty()) continue;

            //TODO: several callable files
            JetFile fileWithCallables = filesWithCallables.iterator().next();
            if (fileWithCallables.isCompiled()) {
                PsiClass clsClass = getClsClassForKotlinCompiledFiles(fileWithCallables);
                if (clsClass != null) {
                    result.add(clsClass);
                }
                continue;
            }

            KotlinLightClassForPackage lightClass = KotlinLightClassForPackage.create(psiManager, packageFqName, info.getScope(), files);
            if (lightClass == null) continue;

            result.add(lightClass);

            if (files.size() > 1) {
                for (JetFile file : files) {
                    result.add(new FakeLightClassForFileOfPackage(psiManager, lightClass, file));
                }
            }
        }
        return result;
    }

    @NotNull
    private List<KotlinLightPackageClassInfo> findPackageClassesInfos(
            @NotNull FqName fqName, @NotNull GlobalSearchScope wholeScope
    ) {
        Collection<JetFile> allFiles = findFilesForPackage(fqName, wholeScope);
        Map<IdeaModuleInfo, List<JetFile>> filesByInfo = groupByModuleInfo(allFiles);
        List<KotlinLightPackageClassInfo> result = new ArrayList<KotlinLightPackageClassInfo>();
        for (Map.Entry<IdeaModuleInfo, List<JetFile>> entry : filesByInfo.entrySet()) {
            result.add(new KotlinLightPackageClassInfo(entry.getValue(), entry.getKey().contentScope()));
        }
        sortByClasspath(wholeScope, result);
        return result;
    }

    @NotNull
    private static Comparator<JetFile> byScopeComparator(@NotNull final GlobalSearchScope searchScope) {
        return new Comparator<JetFile>() {
            @Override
            public int compare(@NotNull JetFile o1, @NotNull JetFile o2) {
                VirtualFile f1 = o1.getVirtualFile();
                VirtualFile f2 = o2.getVirtualFile();
                if (f1 == f2) return 0;
                if (f1 == null) return -1;
                if (f2 == null) return 1;
                return searchScope.compare(f1, f2);
            }
        };
    }

    private static void sortByClasspath(@NotNull GlobalSearchScope wholeScope, @NotNull List<KotlinLightPackageClassInfo> result) {
        final Comparator<JetFile> byScopeComparator = byScopeComparator(wholeScope);
        Collections.sort(result, new Comparator<KotlinLightPackageClassInfo>() {
            @Override
            public int compare(@NotNull KotlinLightPackageClassInfo info1, @NotNull KotlinLightPackageClassInfo info2) {
                JetFile file1 = info1.getFiles().iterator().next();
                JetFile file2 = info2.getFiles().iterator().next();
                //classes earlier that would appear earlier on classpath should go first
                return -byScopeComparator.compare(file1, file2);
            }
        });
    }

    private static final class KotlinLightPackageClassInfo {
        private final Collection<JetFile> files;
        private final GlobalSearchScope scope;

        public KotlinLightPackageClassInfo(@NotNull Collection<JetFile> files, @NotNull GlobalSearchScope scope) {
            this.files = files;
            this.scope = scope;
        }

        @NotNull
        public Collection<JetFile> getFiles() {
            return files;
        }

        @NotNull
        public GlobalSearchScope getScope() {
            return scope;
        }
    }

    @Nullable
    private static PsiClass getClsClassForKotlinCompiledFiles(@NotNull final JetFile file) {
        VirtualFile vFile = file.getVirtualFile();
        if (vFile == null) {
            return null;
        }

        final PsiJavaFileStubImpl javaFileStub = (PsiJavaFileStubImpl) createStub(vFile);

        Project project = file.getProject();

        PsiManager manager = PsiManager.getInstance(project);
        ClsFileImpl fakeFile = new ClsFileImpl((PsiManagerImpl) manager, new ClassFileViewProvider(manager, vFile)) {
            @NotNull
            @Override
            public PsiClassHolderFileStub getStub() {
                return javaFileStub;
            }

            //TODO: these override are probably rubbish
            @Override
            public PsiElement getMirror() {
                return file;
            }

            @Override
            public PsiFile getDecompiledPsiFile() {
                return file;
            }
        };
        fakeFile.setPhysical(false);
        javaFileStub.setPsi(fakeFile);
        PsiClass[] classes = fakeFile.getClasses();
        assert classes.length == 1;
        return classes[0];
    }

    @NotNull
    private static PsiJavaFileStub createStub(@NotNull VirtualFile vFile) {
        try {
            return ClsFileImpl.buildFileStub(vFile, vFile.contentsToByteArray());
        }
        //TODO:
        catch (ClsFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
