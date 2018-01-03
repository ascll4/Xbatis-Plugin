package com.github.ansafari.plugin.converter;

import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.psi.PsiFile;

public class PropertiesFileConverter extends PsiFileConverterBase {

    /**
     * validate file can be accepted
     *
     * @param psiFile psi file
     * @return accept mark
     */
    @Override
    protected boolean isFileAccepted(final PsiFile psiFile) {
        return psiFile instanceof PropertiesFile;
    }

}