package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.intellij.lang.properties.psi.Property;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * iBATIS configuration xml file model
 */
public interface SqlMapConfig extends DomElement {
    Properties getProperties();

    Settings getSettings();

    ResultObjectFactory getResultObjectFactory();

    @SubTagList("typeAlias")
    List<TypeAlias> getTypeAlias();

    @SubTagList("typeHandler")
    List<TypeHandler> getTypeHandlers();

    @SubTagList("sqlMap")
    List<SqlMap> getSqlMaps();

    @SubTagList("sqlMap")
    SqlMap addSqlMap();

    /**
     * get properties in file
     *
     * @return properties
     */
    List<Property> getPropertiesInFile();
}
