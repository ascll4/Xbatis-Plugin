package com.github.ansafari.plugin.xbatis.model.mapper;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;


public interface WithResultMap extends DomElement {

    @Attribute("resultMap")
    GenericAttributeValue<ResultMap> getResultMap();

}
