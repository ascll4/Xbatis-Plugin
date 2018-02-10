package com.github.ansafari.plugin.ibatis.codeInspection;

import com.github.ansafari.plugin.codeInspection.AbstractInspection;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.github.ansafari.plugin.utils.XbatisUtils;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.siyeh.ig.BaseInspectionVisitor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * NoSqlMapStatementInspection.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/8 21:32
 */
public class NoSqlMapStatementInspection extends AbstractInspection {


    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new BaseInspectionVisitor() {
            @Override
            public void visitLiteralExpression(PsiLiteralExpression expression) {
                String rawText = ((PsiLiteralExpressionImpl) expression).getInnerText();
                if (StringUtils.isBlank(rawText) && !XbatisUtils.isWithinScope(expression)) {
                    return;
                }
                //org.springframework.orm.ibatis.SqlMapClientTemplate
                String methodName = "";
                PsiMethodCallExpression psiMethodCallExpression = PsiTreeUtil.getParentOfType(expression, PsiMethodCallExpression.class);
                if (psiMethodCallExpression != null) {
                    PsiExpression psiExpression = psiMethodCallExpression.getMethodExpression().getQualifierExpression();
                    if (psiExpression != null) {
                        PsiType psiType = psiExpression.getType();
                        if (psiType != null) {
                            methodName = psiType.getCanonicalText();
                        }
                    }
                }
                //如何定位调用了哪个类的哪个函数，可以再探究一下
                if (!StringUtils.equals("org.springframework.orm.ibatis.SqlMapClientTemplate", methodName)) {
                    return;
                }
                DomFileElementsFinder finder = DomFileElementsFinder.getInstance(expression.getProject());

                String[] parts = dotPattern.split(rawText, 2);
                if (parts != null) {
                    String targetNamespace = parts.length == 2 ? parts[0] : "";
                    String targetId = parts.length == 2 ? parts[1] : parts[0];
                    boolean existsMapperStatement = finder.existsSqlMapStatement(targetNamespace, targetId);
                    if (!existsMapperStatement) {
                        holder.registerProblem(expression, "Not defined in mapper xml");
                    }
                }
            }
        };
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "SqlMap statement not defined in xml";
    }

    @NotNull
    @Override
    public String getShortName() {
        return "NoSqlMapStatement";
    }
}
