package com.github.ansafari.plugin.ibatis.domain.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

public interface ParameterMap extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("class")
    GenericAttributeValue<TypeAlias> getClazz();

}
