package com.github.ansafari.plugin.psi.reference;

import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;


/**
 * 字符串 链接到 sql statement in xml
 */
public class XBatisReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        //registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new SqlClientElementFilter()), new StatementIdReferenceProvider());
        //PsiJavaPatterns.psiLiteral()
        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression(), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                //.and(new SqlClientElementFilter())
                if (!(element instanceof PsiLiteralExpression)) return PsiReference.EMPTY_ARRAY;
                if (PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class) == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
//                //method name validation simply, filter for detailed validation
//                String[] path = ((PsiMethodCallExpression) parent).getMethodExpression().getText().split("\\.");
//                String methodName = path[path.length - 1].trim().toLowerCase();
//                if (!methodName.matches(SqlClientElementFilter.operationPattern)) return PsiReference.EMPTY_ARRAY;
                //new SqlMapNamespaceReference((PsiLiteral) element)
                return new PsiReference[]{new IdentifiableStatementReference((PsiLiteral) element),};
            }
        });

    }

}

