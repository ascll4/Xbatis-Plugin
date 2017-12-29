package com.github.ansafari.plugin.generate;

public class ContainModel extends GenerateModel {

    @Override
    protected boolean apply(String pattern, String target) {
        return target.contains(pattern);
    }

    @Override
    public int getIdentifier() {
        return 2;
    }
}
