package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * type handler element.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 22:42
 */
public interface TypeHandler extends DomElement {
    @Attribute("javaType")
    @NotNull
    GenericAttributeValue<String> getJavaType();

    @Attribute("jdbcType")
    @NotNull
    GenericAttributeValue<String> getJdbcType();

    @Attribute("callback")
    @NotNull
    GenericAttributeValue<PsiClass> getCallback();
}
