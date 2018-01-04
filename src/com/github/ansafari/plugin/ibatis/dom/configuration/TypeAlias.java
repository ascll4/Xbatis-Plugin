package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * properties element in iBATIS configuration xml file
 */
public interface TypeAlias extends DomElement {
    @NotNull
    GenericAttributeValue<PsiClass> getType();

    @NotNull
    GenericAttributeValue<String> getAlias();

}