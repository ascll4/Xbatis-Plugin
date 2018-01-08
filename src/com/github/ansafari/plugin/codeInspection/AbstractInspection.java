package com.github.ansafari.plugin.codeInspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInspection extends BaseJavaLocalInspectionTool {

    public static final ProblemDescriptor[] EMPTY_ARRAY = new ProblemDescriptor[0];

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "IBATIS/MyBatis issues";
    }

    @NotNull
    @Override
    public String getShortName() {
        return this.getClass().getName();
    }

}
