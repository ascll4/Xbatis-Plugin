package com.github.ansafari.plugin.xbatis.model.mapper;

import com.intellij.util.xml.*;

import java.util.List;

public interface Mapper extends DomElement {

    @Attribute("namespace")
    GenericAttributeValue<String> getNamespace();

    @SubTagsList({"sql", "select", "insert", "update", "delete"})
    List<MapperIdentifiableStatement> getIdentifiableStatements();

    @SubTagList("resultMap")
    List<ResultMap> getResultMaps();
    
    @SubTagList("sql")
    List<MapperIdentifiableStatement> getSqls();

    @SubTagList("select")
    List<Select> getSelects();

    @SubTagList("insert")
    List<Insert> getInserts();

    @SubTagList("update")
    List<Update> getUpdates();

    @SubTagList("delete")
    List<Delete> getDeletes();


}
