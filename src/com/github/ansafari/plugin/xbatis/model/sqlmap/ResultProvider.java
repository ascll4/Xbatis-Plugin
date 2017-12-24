package com.github.ansafari.plugin.xbatis.model.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface ResultProvider extends DomElement {

    @Attribute("resultClass")
    GenericAttributeValue<TypeAlias> getResultClass();

    @Attribute("resultMap")
    GenericAttributeValue<ResultMap> getResultMap();

}
