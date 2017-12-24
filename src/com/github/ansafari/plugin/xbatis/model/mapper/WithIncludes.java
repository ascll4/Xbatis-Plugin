package com.github.ansafari.plugin.xbatis.model.mapper;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;


public interface WithIncludes extends DomElement  {

    @SubTagList("include")
    List<Include> getIncludes();

}
