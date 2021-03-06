package com.github.ansafari.plugin.mybatis.search;

import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

/**
 * MyBatisProxiesDefinitionsSearcher.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/6 14:55
 */
public class MyBatisProxiesDefinitionsSearcher extends QueryExecutorBase<XmlElement, PsiElement> {

    @Override
    public void processQuery(@NotNull PsiElement element, @NotNull final Processor<XmlElement> consumer) {
        Processor<DomElement> processor = domElement -> consumer.process(domElement.getXmlElement());
        if (element instanceof PsiClass) {
            DomFileElementsFinder finder = DomFileElementsFinder.getInstance(element.getProject());
            finder.processMappers((PsiClass) element, processor);
        } else if (element instanceof PsiMethod) {
            DomFileElementsFinder finder = DomFileElementsFinder.getInstance(element.getProject());
            finder.processMapperStatements((PsiMethod) element, processor);
        }
    }
}
