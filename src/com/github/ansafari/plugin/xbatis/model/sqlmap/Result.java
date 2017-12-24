package com.github.ansafari.plugin.xbatis.model.sqlmap;

import com.github.ansafari.plugin.xbatis.psi.reference.ResultMapReference;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

public interface Result extends DomElement {

    @NameValue
    @Attribute("property")
    GenericAttributeValue<String> getProperty();

    @Attribute("typeHandler")
    GenericAttributeValue<TypeAlias> getTypeHandler();

    @Attribute("resultMap")
    @Referencing(ResultMapReferenceConverter.class)
    GenericAttributeValue<ResultMap> getResultMap();

    class ResultMapReferenceConverter implements CustomReferenceConverter {

        @NotNull
        @Override
        public PsiReference[] createReferences(GenericDomValue genericDomValue, PsiElement element, ConvertContext context) {
            return new PsiReference[]{new ResultMapReference(element)};
        }

    }

}
