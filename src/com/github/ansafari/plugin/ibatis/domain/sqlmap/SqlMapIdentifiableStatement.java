package com.github.ansafari.plugin.ibatis.domain.sqlmap;

import com.intellij.util.xml.*;

import java.util.List;

public interface SqlMapIdentifiableStatement extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("parameterClass")
    GenericAttributeValue<TypeAlias> getParameterClass();

    @Attribute("parameterMap")
    GenericAttributeValue<ParameterMap> getParameterMap();

    @SubTagList("include")
    List<Include> getIncludes();

}
