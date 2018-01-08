package com.github.ansafari.plugin.ibatis.provider;

import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.provider.AbstractStatementDocumentationProvider;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.github.ansafari.plugin.utils.XbatisUtils;
import com.intellij.openapi.components.ServiceManager;
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
public class IbatisStatementDocumentationProvider extends AbstractStatementDocumentationProvider {

    /**
     * Returns the text to show in the Ctrl-hover popup for the specified element.
     *
     * @param element         the element for which the documentation is requested (for example, if the mouse is over
     *                        a method reference, this will be the method to which the reference is resolved).
     * @param originalElement the element under the mouse cursor
     * @return the documentation to show, or null if the provider can't provide any documentation for this element.
     */
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
