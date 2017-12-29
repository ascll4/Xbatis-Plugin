package com.github.ansafari.plugin.xbatis.generate;

import org.jetbrains.annotations.NotNull;

/**
 * DeleteGenerator.
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:58
 */
public class DeleteGenerator extends StatementGenerator {

    public DeleteGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    public String getId() {
        return "DeleteGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Delete Statement";
    }

}
