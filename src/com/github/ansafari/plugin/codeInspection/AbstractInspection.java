package com.github.ansafari.plugin.codeInspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public abstract class AbstractInspection extends BaseJavaLocalInspectionTool {

    public static final ProblemDescriptor[] EMPTY_ARRAY = new ProblemDescriptor[0];

    public static final Pattern dotPattern = Pattern.compile("\\.");

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "Ibatis/Mybatis issues";
    }

    @NotNull
    @Override
    public String getShortName() {
        return this.getClass().getName();
    }

}
