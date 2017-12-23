package com.github.ansafari.plugin.xbatis.psi.reference;

import com.github.ansafari.plugin.xbatis.utils.IbatisConstants;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.filters.*;
import com.intellij.psi.filters.position.NamespaceFilter;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class IbatisReferenceProvider extends PsiReferenceContributor {

    private PsiReferenceRegistrar registrar;

    private NamespaceFilter ibatisSqlMapNamespaceFilter;

    public IbatisReferenceProvider() {
        ibatisSqlMapNamespaceFilter = new NamespaceFilter(IbatisConstants.SQLMAP_DTDS);
    }

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        this.registrar = registrar;
        registerProvider();
    }

    public void registerProvider() {
        JavaClassReferenceProvider classReferenceProvider = new JavaClassReferenceProvider();
        //跳转到Java文件
        // registerXmlAttributeValueReferenceProvider(ibatisSqlMapNamespaceFilter, "typeAlias", new String[]{"type"}, classReferenceProvider);


        PsiReferenceProvider psiReferenceProvider = new PsiReferenceProvider() {
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
        };
        registerXmlAttributeValueReferenceProvider(ibatisSqlMapNamespaceFilter, new String[]{"insert", "update", "delete", "select"}, new String[]{"id"}, psiReferenceProvider);
    }

    /**
     * Register the given provider on the given XmlAttribute/Namespace/XmlTag(s) combination.
     *
     * @param provider        Provider to install.
     * @param attributeNames  Attribute names.
     * @param namespaceFilter Namespace for tag(s).
     * @param tagName         tag name
     */
    private void registerXmlAttributeValueReferenceProvider(final NamespaceFilter namespaceFilter, String[] tagName, final @NonNls String[] attributeNames, final PsiReferenceProvider provider) {
        XmlUtil.registerXmlAttributeValueReferenceProvider(registrar, attributeNames, andTagNames(namespaceFilter, tagName), provider);
    }

    private void registerXmlAttributeValueReferenceProvider(final NamespaceFilter namespaceFilter, String tagName, final @NonNls String[] attributeNames, final PsiReferenceProvider provider) {
        XmlUtil.registerXmlAttributeValueReferenceProvider(registrar, attributeNames, andTagNames(namespaceFilter, tagName), provider);
    }


    public static ScopeFilter andTagNames(final ElementFilter namespace, final String... tagNames) {
        return new ScopeFilter(new ParentElementFilter(new AndFilter(namespace, TAG_CLASS_FILTER, new TextFilter(tagNames)), 2));
    }

    public final static ClassFilter TAG_CLASS_FILTER = new ClassFilter(XmlTag.class);

//    registrar.registerReferenceProvider(XmlPatterns
//            .xmlAttributeValue().withLocalName("id")
//                .inFile(PlatformPatterns.psiFile(XmlFile.class))
//            .with(new PatternCondition<XmlAttributeValue>("isXbatisXml") {
//        @Override
//        public boolean accepts(@NotNull XmlAttributeValue xmlAttributeValue, ProcessingContext context) {
//            return DomUtil.findDomElement(xmlAttributeValue, DomElement.class) instanceof SqlDomElement;
//        }
//    }), psiReferenceProvider
//        );

}
