package com.github.ansafari.plugin.xbatis.dom.fileDescription;

import com.github.ansafari.plugin.xbatis.domain.SqlMap;
import com.intellij.util.xml.DomFileDescription;

public class SimpleDomFileDescription extends DomFileDescription<SqlMap> {

    public SimpleDomFileDescription() {
        super(SqlMap.class, "sqlMap");
    }
}
