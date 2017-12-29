package com.github.ansafari.plugin.generate;

public class StartWithModel extends GenerateModel {

    @Override
    protected boolean apply(String pattern, String target) {
        return target.startsWith(pattern);
    }

    @Override
    public int getIdentifier() {
        return 0;
    }
}
