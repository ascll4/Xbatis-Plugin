package com.github.ansafari.plugin.mybatis.dom.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * TypeAlias.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/5 07:51
 */
public interface TypeAlias extends DomElement {

    @NotNull
    @Attribute("type")
    public GenericAttributeValue<PsiClass> getType();

    @NotNull
    @Attribute("alias")
    public GenericAttributeValue<String> getAlias();

}
