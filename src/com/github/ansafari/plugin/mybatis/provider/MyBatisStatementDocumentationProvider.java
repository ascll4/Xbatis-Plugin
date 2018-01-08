package com.github.ansafari.plugin.mybatis.provider;

import com.github.ansafari.plugin.mybatis.dom.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.provider.AbstractStatementDocumentationProvider;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.github.ansafari.plugin.utils.XbatisUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;

import java.util.List;

/**
 * control+J|F1 根据statement id查看XML元素源码.
 * 如果不知道快捷键，可以通过Find Action（shift+command+a）搜索 Quick Documentation查看快捷方式
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 20:36
 */
public class MyBatisStatementDocumentationProvider extends AbstractStatementDocumentationProvider {


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
        if (originalElement instanceof PsiJavaToken && XbatisUtils.isWithinScope(originalElement)) {
            String namespace = getNameSpace(originalElement);
            String id = getId(originalElement);

            DomFileElementsFinder finder = DomFileElementsFinder.getInstance(originalElement.getProject());
            CommonProcessors.FindFirstProcessor<MapperIdentifiableStatement> processorMapper = new CommonProcessors.FindFirstProcessor<>();
            finder.processMapperStatements2(namespace, id, processorMapper);
            if (processorMapper.isFound()) {
                MapperIdentifiableStatement statement = processorMapper.getFoundValue();
                XmlElement xmlElement = statement != null ? statement.getXmlElement() : null;
                if (xmlElement != null) {
                    return getXmlElementCodeSource(xmlElement);
                }
            }
        }
        return null;
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
