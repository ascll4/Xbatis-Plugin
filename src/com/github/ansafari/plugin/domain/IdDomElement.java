package com.github.ansafari.plugin.domain;

import com.intellij.util.xml.*;

/**
 * IdDomElement.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/8 22:32
 */
public interface IdDomElement extends DomElement {

    @Required
    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    //public void setValue(String content);
}
