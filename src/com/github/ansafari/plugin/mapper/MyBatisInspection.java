package com.github.ansafari.plugin.mapper;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class MyBatisInspection extends AbstractBaseJavaLocalInspectionTool {

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "iBATIS/MyBatis issues";
    }

    @NotNull
    @Override
    public String getShortName() {
        return this.getClass().getName();
    }

}
