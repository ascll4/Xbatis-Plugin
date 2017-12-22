package com.github.ansafari.plugin.xbatis.utils;

import com.github.ansafari.plugin.xbatis.domain.SqlDomElement;
import com.github.ansafari.plugin.xbatis.domain.SqlMap;
import com.github.ansafari.plugin.xbatis.fileTypes.SimpleFileType;
import com.github.ansafari.plugin.xbatis.psi.SimpleFile;
import com.github.ansafari.plugin.xbatis.psi.SimpleProperty;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class SimpleUtil {
    public static List<SimpleProperty> findProperties(Project project, String key) {
        List<SimpleProperty> result = null;
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SimpleFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            SimpleFile simpleFile = (SimpleFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, SimpleProperty.class);
                if (properties != null) {
                    for (SimpleProperty property : properties) {
                        if (key.equals(property.getKey())) {
                            if (result == null) {
                                result = new ArrayList<>();
                            }
                            result.add(property);
                        }
                    }
                }
            }
        }
        return result != null ? result : Collections.emptyList();

    }

    public static List<SimpleProperty> findProperties(Project project) {
        List<SimpleProperty> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SimpleFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            SimpleFile simpleFile = (SimpleFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, SimpleProperty.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

    public static List<XmlTag> findXmlTags(Project project, String key) {
        List<XmlTag> result = new ArrayList<>();
        //VirtualFile src = project.getBaseDir().findChild("src");
        //if (src == null) return null;
        //VirtualFile[] virtualFile = src.getChildren();
        //GlobalSearchScope.filesScope(project, Arrays.asList(virtualFile))
        Module[] modules = ModuleManager.getInstance(project).getModules();
        Collection<VirtualFile> allXmlVirtualFiles = new LinkedHashSet<>();
        for (Module module : modules) {
            Collection<VirtualFile> xmlVirtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, XmlFileType.INSTANCE, GlobalSearchScope.projectScope(project));
            allXmlVirtualFiles.addAll(xmlVirtualFiles);
        }
        for (VirtualFile xmlVirtualFile : allXmlVirtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(xmlVirtualFile);
            if (psiFile != null && psiFile instanceof XmlFile) {
                DomManager manager = DomManager.getDomManager(project);
                //DomFileDescription<SqlMap> description = new DomFileDescription<>(SqlMap.class, "sqlMap", "sqlMap");
                DomFileElement<SqlMap> domFileElement = manager.getFileElement((XmlFile) psiFile, SqlMap.class);
                SqlMap sqlMap = null;
                if (domFileElement != null) {
                    sqlMap = domFileElement.getRootElement();
                }
                if (sqlMap != null) {
                    findXmlTags(sqlMap.getInserts(), result, key);
                    findXmlTags(sqlMap.getDeletes(), result, key);
                    findXmlTags(sqlMap.getSelects(), result, key);
                    findXmlTags(sqlMap.getUpdates(), result, key);
                }
            }
        }
        return result;
    }

    public static void findXmlTags(List<? extends SqlDomElement> elementList, List<XmlTag> result, String key) {
        if (elementList != null && elementList.size() > 0 && result != null) {
            for (SqlDomElement domElement : elementList) {
                String id = domElement.getId().getValue();
                if (key == null) {
                    result.add(domElement.getXmlTag());
                } else if (id != null && id.length() > 0 && StringUtils.equals(id, key)) {
                    result.add(domElement.getXmlTag());
                }
            }
        }
    }

    public static Set<PsiElement> findJavaByPsiLiteralExpression(Project project, String key) {
        Set<PsiElement> psiElementSet = ContainerUtil.newLinkedHashSet();
        //XmlFile xmlFile = DomUtil.getFile(element);
        Module[] modules = ModuleManager.getInstance(project).getModules();
        Collection<VirtualFile> allJavaVirtualFiles = new LinkedHashSet<>();
        if (modules.length > 0) {
            for (Module module : modules) {
                Collection<VirtualFile> javaVirtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.moduleScope(module));
                allJavaVirtualFiles.addAll(javaVirtualFiles);
            }
        }
        //Collection<VirtualFile> javaVirtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        for (VirtualFile javaVirtualFile : allJavaVirtualFiles) {
            PsiJavaFile javaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(javaVirtualFile);
            Collection<PsiLiteralExpression> psiLiteralExpressions = PsiTreeUtil.findChildrenOfAnyType(javaFile, PsiLiteralExpression.class);

            for (PsiLiteralExpression psiLiteralExpression : psiLiteralExpressions) {
                String value = psiLiteralExpression.getValue() instanceof String ? (String) psiLiteralExpression.getValue() : null;
                if (key == null || key.equals(value)) {
                    psiElementSet.add(PsiTreeUtil.getParentOfType(psiLiteralExpression, PsiStatement.class));
                }
            }
        }
        return psiElementSet;
    }

}