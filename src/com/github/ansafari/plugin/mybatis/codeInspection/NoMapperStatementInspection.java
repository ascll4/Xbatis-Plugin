package com.github.ansafari.plugin.mybatis.codeInspection;

import com.github.ansafari.plugin.codeInspection.AbstractInspection;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class NoMapperStatementInspection extends AbstractInspection {

    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        PsiClass containingClass = method.getContainingClass();
        if (containingClass != null && containingClass.isInterface()) {

            PsiIdentifier methodName = method.getNameIdentifier();
            DomFileElementsFinder finder = DomFileElementsFinder.getInstance(containingClass.getProject());
            boolean existsMapperStatement = finder.existsMapperStatement(method);

            boolean containsIbatisAnnotation = false;
            for (PsiAnnotation annotation : method.getModifierList().getAnnotations()) {
                String annotationName = annotation.getQualifiedName();
                if (annotationName != null && annotationName.startsWith("org.apache.ibatis.annotations")) {
                    containsIbatisAnnotation = true;
                    break;
                }
            }

            if (!existsMapperStatement && !containsIbatisAnnotation && methodName != null) {
                return new ProblemDescriptor[]{
                        manager.createProblemDescriptor(methodName, "Statement with id=\"#ref\" not defined in mapper xml", (LocalQuickFix) null, ProblemHighlightType.GENERIC_ERROR, isOnTheFly)
                };
            }
        }
        return null;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Mapper interface method not defined in xml";
    }

}
