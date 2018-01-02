package com.github.ansafari.plugin.provider;

import com.github.ansafari.plugin.ibatis.domain.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.mybatis.domain.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

/**
 * control+J|F1 根据statement id查看XML元素源码.
 * 如果不知道快捷键，可以通过Find Action（shift+command+a）搜索 Quick Documentation查看快捷方式
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 20:36
 */
public class StatementDocumentationProvider implements DocumentationProvider {

    private static final Pattern dotPattern = Pattern.compile("\\.");

    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Override
    public String generateDoc(PsiElement element, PsiElement originalElement) {

        if (originalElement instanceof PsiJavaToken) {

            String rawText = originalElement.getText();
            String[] parts = dotPattern.split(rawText.substring(1, rawText.length() - 1), 2);

            String namespace;
            String id;

            if (parts.length == 2) {
                namespace = parts[0];
                id = parts[1];
            } else {
                namespace = "";
                id = parts[0];
            }

            //先看Ibatis能不能找到，否则找Mybatis，最优是能够识别Dao是Ibatis还是Mybatis

            DomFileElementsFinder finder = ServiceManager.getService(originalElement.getProject(), DomFileElementsFinder.class);
            CommonProcessors.FindFirstProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.FindFirstProcessor<>();
            finder.processSqlMapStatements(namespace, id, processor);
            if (processor.isFound()) {
                SqlMapIdentifiableStatement statement = processor.getFoundValue();
                XmlElement xmlElement = statement != null ? statement.getXmlElement() : null;
                if (xmlElement != null) {
                    return getXmlElementCodeSource(xmlElement);
                }
            }

            CommonProcessors.FindFirstProcessor<MapperIdentifiableStatement> processorMapper = new CommonProcessors.FindFirstProcessor<>();
            finder.processMapperStatements2(namespace, id, processorMapper);
            if (processorMapper.isFound()) {
                MapperIdentifiableStatement statement = processorMapper.getFoundValue();
                XmlElement xmlElement = statement != null ? statement.getXmlElement() : null;
                if (xmlElement != null) {
                    return getXmlElementCodeSource(xmlElement);
                }
            }
            return null;
        }
        return null;
    }

    /**
     * 获取XmlElement源代码
     *
     * @param xmlElement xmlElement
     * @return String
     */
    private String getXmlElementCodeSource(@NotNull XmlElement xmlElement) {
        String text = "";
        if (StringUtils.isNotBlank(xmlElement.getText())) {
            text = xmlElement.getText().replaceAll("\n {4}", "\n");
        }
        return "<pre>" + StringUtil.escapeXml(text) + "</pre>";
    }

    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

}
