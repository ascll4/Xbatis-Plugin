package com.github.ansafari.plugin.ibatis.domain.sqlmap;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;


public interface TypeAlias extends DomElement {

    @NameValue
    @Attribute("alias")
    GenericAttributeValue<String> getAlias();

    @Attribute("type")
    GenericAttributeValue<PsiClass> getType();

}
