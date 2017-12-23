package com.github.ansafari.plugin.xbatis.psi.reference;

import com.github.ansafari.plugin.xbatis.domain.SqlDomElement;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.ProcessingContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class XmlStatementReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns
                        .xmlAttributeValue().withLocalName("id")
                        .inFile(PlatformPatterns.psiFile(XmlFile.class))
                        .with(new PatternCondition<XmlAttributeValue>("isXbatisXml") {
                            @Override
                            public boolean accepts(@NotNull XmlAttributeValue xmlAttributeValue, ProcessingContext context) {
                                return DomUtil.findDomElement(xmlAttributeValue, DomElement.class) instanceof SqlDomElement;
                            }
                        }),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        String value = ((XmlAttributeValueImpl) element).getValue();
                        if (value == null || value.length() <= 0) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        final Set<PsiElement> psiElementSet = SimpleUtil.findJavaByPsiLiteralExpression(element.getProject(), value);
                        if (psiElementSet != null && psiElementSet.size() > 0) {
                            return new PsiReference[]{new XmlStatementReference(element, new TextRange(1, value.length() + 1))};
                        } else {
                            return PsiReference.EMPTY_ARRAY;
                        }
                    }
                });
    }
}
