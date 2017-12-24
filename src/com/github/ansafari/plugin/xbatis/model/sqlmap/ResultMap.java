package com.github.ansafari.plugin.xbatis.model.sqlmap;

import com.intellij.util.xml.*;

import java.util.List;

public interface ResultMap extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("class")
    GenericAttributeValue<TypeAlias> getClazz();

    @SubTagList("result")
    List<Result> getResults();

}
