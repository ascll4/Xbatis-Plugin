package com.github.ansafari.plugin.generate;

import org.jetbrains.annotations.NotNull;

/**
 * SelectGenerator.
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:58
 */
public class SelectGenerator extends StatementGenerator {

    public SelectGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    public String getId() {
        return "SelectGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Select Statement";
    }
}
