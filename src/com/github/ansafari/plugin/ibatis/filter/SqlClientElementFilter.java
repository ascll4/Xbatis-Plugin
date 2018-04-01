package com.github.ansafari.plugin.ibatis.filter;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.patterns.InitialPatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Nullable;

/**
 * sql map client method call filter
 */
public class SqlClientElementFilter implements ElementPattern<PsiLiteralExpression> {
    public static String operationPattern = "(execute)?((query[(for)|(with)]\\w*)|insert|update|delete)";

    public boolean accepts(@Nullable Object o) {
        return false;
    }

    public boolean accepts(@Nullable Object o, ProcessingContext processingContext) {
        return false;
    }

    public ElementPatternCondition<PsiLiteralExpression> getCondition() {
        return new ElementPatternCondition<>(new InitialPatternCondition<PsiLiteralExpression>(PsiLiteralExpression.class) {
            @Override
            public boolean accepts(@Nullable Object o, ProcessingContext processingContext) {
                PsiLiteralExpression literalExpression = (PsiLiteralExpression) o;
                PsiElement parent = PsiTreeUtil.getParentOfType(literalExpression, PsiMethodCallExpression.class);
                if (parent != null) {
                    //first parameter validate
                    PsiElement previousElement = literalExpression.getPrevSibling();
                    if (previousElement != null) {
                        String text1 = previousElement.getText();
                        String text2 = "";
                        if (!text1.equals("(") && previousElement.getPrevSibling() != null) {
                            text2 = previousElement.getPrevSibling().getText();
                        }
                        if (!(text1.equals("(") || (text1.concat(text2).trim().equals("(")))) {
                            return false;
                        }
                    }
                    //method validation
                    final PsiMethodCallExpression callExpression = (PsiMethodCallExpression) parent;
                    String[] path = callExpression.getMethodExpression().getText().split("\\.");
                    String methodName = path[path.length - 1].trim().toLowerCase();
                    //&& IbatisUtil.getConfig(literalExpression) != null
                    System.out.println("methodName: " + methodName);
                    if (methodName.matches(operationPattern)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
