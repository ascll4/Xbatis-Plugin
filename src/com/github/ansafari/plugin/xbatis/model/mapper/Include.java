package com.github.ansafari.plugin.xbatis.model.mapper;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface Include extends DomElement {

    @Attribute("refid")
    GenericAttributeValue<MapperIdentifiableStatement> getRefid();

}
