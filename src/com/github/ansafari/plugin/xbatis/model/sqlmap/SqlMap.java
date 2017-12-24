package com.github.ansafari.plugin.xbatis.model.sqlmap;

import com.intellij.util.xml.*;

import java.util.List;

public interface SqlMap extends DomElement {

    @NameValue
    @Attribute("namespace")
    GenericAttributeValue<String> getNamespace();

    @SubTagsList({"sql", "statement", "select", "insert", "update", "delete", "procedure"})
    List<SqlMapIdentifiableStatement> getIdentifiableStatements();
    
    @SubTagList("sql")
    List<SqlMapIdentifiableStatement> getSqls();

    @SubTagList("statement")
    List<Statement> getStatements();

    @SubTagList("select")
    List<Select> getSelects();

    @SubTagList("insert")
    List<Insert> getInserts();

    @SubTagList("update")
    List<Update> getUpdates();

    @SubTagList("delete")
    List<Delete> getDeletes();

    @SubTagList("procedure")
    List<Procedure> getProcedures();

    @SubTagList("typeAlias")
    List<TypeAlias> getTypeAliases();

    @SubTagList("resultMap")
    List<ResultMap> getResultMaps();

    @SubTagList("parameterMap")
    List<ParameterMap> getParameterMap();

}
