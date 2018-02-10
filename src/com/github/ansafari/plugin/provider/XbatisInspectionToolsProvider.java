package com.github.ansafari.plugin.provider;

import com.github.ansafari.plugin.ibatis.codeInspection.NoSqlMapStatementInspection;
import com.github.ansafari.plugin.mybatis.codeInspection.NoMapperStatementInspection;
import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

public class XbatisInspectionToolsProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{NoMapperStatementInspection.class, NoSqlMapStatementInspection.class};
    }

}
