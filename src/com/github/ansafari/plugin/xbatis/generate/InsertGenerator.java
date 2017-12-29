package com.github.ansafari.plugin.xbatis.generate;

import org.jetbrains.annotations.NotNull;

/**
 * InsertGenerator.
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:58
 */
public class InsertGenerator extends StatementGenerator {

    public InsertGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    public String getId() {
        return "InsertGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Insert Statement";
    }
}
