package com.github.ansafari.plugin.provider;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class AbstractStatementDocumentationProvider implements DocumentationProvider {

    private static final Pattern dotPattern = Pattern.compile("\\.");

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement psiElement1) {
        return null;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement1) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement psiElement, @Nullable PsiElement psiElement1) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String s, PsiElement psiElement) {
        return null;
    }

    protected String getNameSpace(PsiElement psiElement) {
        String rawText = psiElement.getText();
        String[] parts = dotPattern.split(rawText.substring(1, rawText.length() - 1), 2);
        return parts.length == 2 ? parts[0] : "";
    }

    protected String getId(PsiElement psiElement) {
        String rawText = psiElement.getText();
        String[] parts = dotPattern.split(rawText.substring(1, rawText.length() - 1), 2);
        return parts.length == 2 ? parts[1] : parts[0];
    }

    /**
     * 获取XmlElement源代码
     *
     * @param xmlElement xmlElement
     * @return String
     */
    protected String getXmlElementCodeSource(@NotNull XmlElement xmlElement) {
        String text = "";
        if (StringUtils.isNotBlank(xmlElement.getText())) {
            text = xmlElement.getText().replaceAll("\n {4}", "\n");
        }
        return "<pre>" + StringUtil.escapeXml(text) + "</pre>";
    }
}
