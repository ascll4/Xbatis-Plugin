package com.github.ansafari.plugin.mybatis.dom.configuration;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * Package.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/5 07:51
 */
public interface Package extends DomElement {

    @NotNull
    @Attribute("name")
    public GenericAttributeValue<String> getName();

}
