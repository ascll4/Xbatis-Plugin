package com.github.ansafari.plugin.mybatis.dom.mapper;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;


public interface WithIncludes extends DomElement  {

    @SubTagList("include")
    List<Include> getIncludes();

}
