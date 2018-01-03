package com.github.ansafari.plugin.ibatis.dom.sqlmap;

import com.intellij.util.xml.SubTagList;

import java.util.List;

public interface Insert extends SqlMapIdentifiableStatement {

    @SubTagList("selectKey")
    List<SelectKey> getSelectKeys();

}
