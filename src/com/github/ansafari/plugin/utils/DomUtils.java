package com.github.ansafari.plugin.utils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.PomTargetPsiElementImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.DomTarget;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public final class DomUtils {

    private DomUtils() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @NonNls
    public static <T extends DomElement> Collection<T> findDomElements(@NotNull Project project, Class<T> clazz) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        List<DomFileElement<T>> elements = DomService.getInstance().getFileElements(clazz, project, scope);
        return Collections2.transform(elements, new Function<DomFileElement<T>, T>() {
            @Override
            public T apply(DomFileElement<T> input) {
                return input.getRootElement();
            }
        });
    }

    public static boolean isMybatisFile(@Nullable PsiFile file) {
        assert file != null;
        if (!isXmlFile(file)) {
            return false;
        }
        XmlTag rootTag = ((XmlFile) file).getRootTag();
        return null != rootTag && rootTag.getName().equals("mapper");
    }

    public static boolean isIbatisFile(@Nullable PsiFile file) {
        assert file != null;
        if (!isXmlFile(file)) {
            return false;
        }
        XmlTag rootTag = ((XmlFile) file).getRootTag();
        return null != rootTag && rootTag.getName().equals("sqlMap");
    }

    public static boolean isBeansFile(@NotNull PsiFile file) {
        if (!isXmlFile(file)) {
            return false;
        }
        XmlTag rootTag = ((XmlFile) file).getRootTag();
        return null != rootTag && rootTag.getName().equals("beans");
    }

    private static boolean isXmlFile(@NotNull PsiFile file) {
        return file instanceof XmlFile;
    }

    public static void addResults(Collection<? extends DomElement> statements, List<ResolveResult> results) {
        if (statements == null || statements.size() <= 0 || results == null) {
            return;
        }
        for (DomElement statement : statements) {
            DomTarget target = DomTarget.getTarget(statement);
            if (target != null) {
                XmlElement xmlElement = statement.getXmlElement();
                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                results.add(new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
                    @Override
                    public String getLocationString() {
                        return locationString;
                    }
                }));
            }
        }
    }
}