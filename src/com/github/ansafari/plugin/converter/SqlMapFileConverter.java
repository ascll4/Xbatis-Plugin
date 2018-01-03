package com.github.ansafari.plugin.converter;

import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMap;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;

/**
 * sqlmap configuration file converter.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 22:49
 */
public class SqlMapFileConverter extends PsiFileConverterBase {

    protected boolean isFileAccepted(final PsiFile file) {
        if (file instanceof XmlFile) {
            final DomFileElement fileElement = DomManager.getDomManager(file.getProject()).getFileElement((XmlFile) file, DomElement.class);
            return fileElement != null && fileElement.getRootElement() instanceof SqlMap;
        }
        return false;
    }
}