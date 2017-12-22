package com.github.ansafari.plugin.xbatis.domain;

import com.intellij.util.xml.GenericAttributeValue;

public interface SqlDomElement extends com.intellij.util.xml.DomElement {
    GenericAttributeValue<String> getId();
}
