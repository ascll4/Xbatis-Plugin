package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.github.ansafari.plugin.converter.PropertiesFileConverter;
import com.intellij.psi.PsiFile;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.Nullable;

/**
 * properties element in iBATIS configuration xml file
 */
public interface Properties extends DomElement {
    @Nullable
    @Convert(PropertiesFileConverter.class)
    GenericAttributeValue<PsiFile> getResource();

    @Nullable
    GenericAttributeValue<String> getUrl();
}
