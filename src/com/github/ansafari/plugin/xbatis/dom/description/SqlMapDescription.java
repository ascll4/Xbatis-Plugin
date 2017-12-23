package com.github.ansafari.plugin.xbatis.dom.description;

import com.github.ansafari.plugin.xbatis.domain.SqlMap;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SqlMapDescription extends DomFileDescription<SqlMap> {

    public SqlMapDescription() {
        super(SqlMap.class, "sqlMap");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        XmlTag rootTag = file.getRootTag();
        return rootTag != null && rootTag.getName().equals(getRootTagName());
    }

}
