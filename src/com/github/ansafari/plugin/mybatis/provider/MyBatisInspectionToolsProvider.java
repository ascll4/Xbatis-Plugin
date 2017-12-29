package com.github.ansafari.plugin.mybatis.provider;

import com.github.ansafari.plugin.mapper.NoMapperStatementInspection;
import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

public class MyBatisInspectionToolsProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{NoMapperStatementInspection.class};
    }

}
