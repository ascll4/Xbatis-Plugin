package com.github.ansafari.plugin.generate;

import org.jetbrains.annotations.NotNull;

/**
 * UpdateGenerator.
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:59
 */
public class UpdateGenerator extends StatementGenerator {

    public UpdateGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    public String getId() {
        return "UpdateGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Update Statement";
    }

}
