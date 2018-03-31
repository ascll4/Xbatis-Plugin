package com.github.ansafari.plugin.codeGen.model;

/**
 * yes和no的枚举.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/22
 * Time: 上午11:13
 */
public enum YesNo {
    YES("1", "是"), NO("0", "否");

    YesNo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
