package com.github.ansafari.plugin.xbatis.model.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

public interface Include extends DomElement {

    @NameValue
    @Attribute("refid")
    GenericAttributeValue<SqlMapIdentifiableStatement> getRefid();

}
