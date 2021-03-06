package com.github.ansafari.plugin.mybatis.dom.mapper;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;


public interface MapperIdentifiableStatement extends DomElement, WithIncludes {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

}
