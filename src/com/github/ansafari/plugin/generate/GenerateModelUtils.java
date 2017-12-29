package com.github.ansafari.plugin.generate;

public class GenerateModelUtils {

    public static final GenerateModel START_WITH_MODEL = new StartWithModel();

    public static final GenerateModel END_WITH_MODEL = new EndWithModel();

    public static final GenerateModel CONTAIN_MODEL = new ContainModel();

    public static GenerateModel getInstance(String identifier) {
        try {
            return getInstance(Integer.valueOf(identifier));
        } catch (Exception e) {
            return START_WITH_MODEL;
        }
    }

    public static GenerateModel getInstance(int identifier) {
        switch (identifier) {
            case 0:
                return START_WITH_MODEL;
            case 1:
                return END_WITH_MODEL;
            case 2:
                return CONTAIN_MODEL;
            default:
                throw new AssertionError();
        }
    }

}
