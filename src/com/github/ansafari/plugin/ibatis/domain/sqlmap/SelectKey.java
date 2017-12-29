package com.github.ansafari.plugin.ibatis.domain.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface SelectKey extends DomElement {

    @Attribute("resultClass")
    GenericAttributeValue<TypeAlias> getResultClass();

}
