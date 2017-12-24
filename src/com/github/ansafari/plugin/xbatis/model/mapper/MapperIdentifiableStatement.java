package com.github.ansafari.plugin.xbatis.model.mapper;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;


public interface MapperIdentifiableStatement extends DomElement, WithIncludes {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

}
