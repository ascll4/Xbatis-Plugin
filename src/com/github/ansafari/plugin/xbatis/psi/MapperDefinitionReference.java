package com.github.ansafari.plugin.xbatis.psi;

import com.github.ansafari.plugin.xbatis.icons.SimpleIcons;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MapperDefinitionReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String key;

    public MapperDefinitionReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
        key = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
    }


    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<XmlTag> xmlTagList = SimpleUtil.findXmlTags(project, key);
        List<ResolveResult> results = new ArrayList<>();
        if (xmlTagList != null && xmlTagList.size() > 0) {
            for (XmlTag xmlTag : xmlTagList) {
                results.add(new PsiElementResolveResult(xmlTag));
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
        final List<XmlTag> xmlTagList = SimpleUtil.findXmlTags(project, null);
        List<LookupElement> variants = new ArrayList<>();
        for (final XmlTag xmlTag : xmlTagList) {
            XmlAttribute xmlAttribute = xmlTag.getAttribute("id");
            if (xmlAttribute != null) {
                String id = xmlAttribute.getValue();
                if (id != null && id.length() > 0) {
                    variants.add(LookupElementBuilder.create(id).
                            withIcon(SimpleIcons.NAVIGATE_TO_STATEMENT).
                            withTypeText(id)
                    );
                }
            }
        }
        return variants.toArray();
    }
}
