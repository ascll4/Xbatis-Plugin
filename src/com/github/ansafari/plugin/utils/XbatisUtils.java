package com.github.ansafari.plugin.utils;

import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.java.SpringJavaClassInfo;
import com.intellij.spring.model.utils.SpringCommonUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class XbatisUtils {

    public static boolean isWithinScope(@NotNull PsiElement psiElement) {
        //PsiTreeUtil.getParentOfType(psiElement, PsiClass.class).getQualifiedName()
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        //是 java类，且是spring bean 且 className不为空

        return psiClass != null && StringUtils.isNotBlank(psiClass.getName()) && isSpringBean(psiClass)
                && PsiTreeUtil.getParentOfType(psiElement, PsiMethodCallExpression.class) != null;
    }

    /**
     * spring bean 检查
     *
     * @param psiClass psiClass
     * @return true：是；false：否
     */
    public static boolean isSpringBean(@NotNull PsiClass psiClass) {
        if (SpringCommonUtils.isSpringBeanCandidateClass(psiClass)) {
            if (SpringCommonUtils.isSpringConfigured(ModuleUtilCore.findModuleForPsiElement(psiClass))) {
                SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(psiClass);
                //annotatePsiClassSpringPropertyValues(result, psiClass, SpringModelUtils.getInstance().getSpringModel(psiElement).getConfigFiles());
                //spring bean defined in xml and annotation
                if (info.isMappedDomBean() || info.isStereotypeJavaBean()) {
                    return true;
                }
            }
        }
        return false;
    }
}
