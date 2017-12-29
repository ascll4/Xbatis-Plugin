package com.github.ansafari.plugin.xbatis.generate;

public class EndWithModel extends GenerateModel {

    @Override
    protected boolean apply(String pattern, String target) {
        return target.endsWith(pattern);
    }

    @Override
    public int getIdentifier() {
        return 1;
    }
}
