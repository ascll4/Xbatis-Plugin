package com.github.ansafari.plugin.xbatis.psi;

import com.github.ansafari.plugin.xbatis.icons.SimpleIcons;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class XmlStatementReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private String key;

    public XmlStatementReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
        key = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final Set<PsiElement> psiElementSet = SimpleUtil.findJavaByPsiLiteralExpression(project, key);
        List<ResolveResult> results = new ArrayList<>();
        if (psiElementSet != null && psiElementSet.size() > 0) {
            for (PsiElement psiElement : psiElementSet) {
                results.add(new PsiElementResolveResult(psiElement));
            }
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        Project project = myElement.getProject();
        final Set<PsiElement> psiElementSet = SimpleUtil.findJavaByPsiLiteralExpression(project, key);
        List<LookupElement> variants = new ArrayList<>();
        for (final PsiElement psiElement : psiElementSet) {
            Collection<PsiLiteralExpression> psiLiteralExpressions = PsiTreeUtil.findChildrenOfAnyType(psiElement, PsiLiteralExpression.class);
            if (psiLiteralExpressions.size() > 0) {
                for (PsiLiteralExpression psiLiteralExpression : psiLiteralExpressions) {
                    String value = psiLiteralExpression.getValue() instanceof String ? (String) psiLiteralExpression.getValue() : null;
                    if (value != null && value.length() > 0) {
                        variants.add(LookupElementBuilder.create(value).
                                withIcon(SimpleIcons.NAVIGATE_TO_METHOD).
                                withTypeText(value)
                        );
                    }
                }
            }
        }
        return variants.toArray();
    }
}
