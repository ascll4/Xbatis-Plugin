package com.github.ansafari.plugin.psi.reference;

import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class XBatisReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        //字面量匹配 reference to xml sql statement
        registrar.registerReferenceProvider(PsiJavaPatterns.psiLiteral(), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new IdentifiableStatementReference((PsiLiteral) element), new SqlMapReference((PsiLiteral) element)};
            }
        });

    }

}

