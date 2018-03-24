package com.github.ansafari.plugin.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.spring.java.SpringJavaClassInfo;
import com.intellij.spring.model.utils.SpringCommonUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * XbatisUtils.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/3 18:5
 */
public class XbatisUtils {

    /**
     * 是否在扫描范围内
     * 1. 是SpringBean
     * 2. 继承了{@code com.alibaba.cobarclient.dao.MysdalBaseDao}
     *
     * @param psiElement psiElement
     * @return boolean
     */
    public static boolean isWithinScope(@NotNull PsiElement psiElement) {
        //PsiTreeUtil.getParentOfType(psiElement, PsiClass.class).getQualifiedName()
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        //是 java类，且是spring bean 且 className不为空
        if (psiClass == null || StringUtils.isBlank(psiClass.getName()) || PsiTreeUtil.getParentOfType(psiElement, PsiMethodCallExpression.class) == null) {
            return false;
        }

        //获取类psiClass的父类列表
        PsiClass psiSuperClass = psiClass.getSuperClass();
        if (psiSuperClass != null && StringUtils.equals(psiSuperClass.getQualifiedName(), "com.alibaba.cobarclient.dao.MysdalBaseDao")) {
            return true;
        }
//        PsiReferenceList psiReferenceList = psiClass.getExtendsList();
//        if (psiReferenceList != null) {
//            PsiJavaCodeReferenceElement[] psiJavaCodeReferenceElements = psiReferenceList.getReferenceElements();
//            if (psiJavaCodeReferenceElements.length > 0) {
//                for (PsiJavaCodeReferenceElement psiJavaCodeReferenceElement : psiReferenceList.getReferenceElements()) {
//                    String qualifiedName = psiJavaCodeReferenceElement.getQualifiedName();
//                    if (StringUtils.equals(qualifiedName, "com.alibaba.cobarclient.dao.MysdalBaseDao")) {
//                        return true;
//                    }
//                }
//            }
//        }
        return isCandidateSpringBean(psiClass);
    }

    /**
     * spring bean 检查,只有是spring bean 并被Service，Repository标注的才符合
     *
     * @param psiClass psiClass
     * @return true：是；false：否
     */
    private static boolean isCandidateSpringBean(@NotNull PsiClass psiClass) {
        //是否是spring bean
        if (SpringCommonUtils.isSpringBeanCandidateClass(psiClass)) {
            SpringJavaClassInfo info = SpringJavaClassInfo.getSpringJavaClassInfo(psiClass);
            //如果是StereotypeJavaBean，有@controller注解，则忽略，范围为不符合spring bean，目的只返回service或者dao
            if (info.isStereotypeJavaBean()) {
                return Arrays.stream(psiClass.getAnnotations())
                        .anyMatch((e) -> StringUtils.isNotBlank(e.getQualifiedName())
                                && ("org.springframework.stereotype.Service," +
                                "org.springframework.stereotype.Repository").contains(e.getQualifiedName()));
            }
        }
        return false;
    }
}
