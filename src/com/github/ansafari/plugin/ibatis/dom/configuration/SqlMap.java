package com.github.ansafari.plugin.ibatis.dom.configuration;

import com.github.ansafari.plugin.converter.SqlMapFileConverter;
import com.intellij.psi.PsiFile;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * properties element in iBATIS configuration xml file
 */
public interface SqlMap extends DomElement {

    @Convert(SqlMapFileConverter.class)
    @NotNull
    public GenericAttributeValue<PsiFile> getResource();

}