package com.github.ansafari.plugin.mybatis.search;

import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

public class MyBatis3ProxiesDefinitionsSearcher extends QueryExecutorBase<XmlElement, PsiElement> {

    @Override
    public void processQuery(@NotNull PsiElement element, @NotNull final Processor<XmlElement> consumer) {

        DomFileElementsFinder finder = ServiceManager.getService(element.getProject(), DomFileElementsFinder.class);
        Processor<DomElement> processor = domElement -> consumer.process(domElement.getXmlElement());

        if (element instanceof PsiClass) {
            finder.processMappers((PsiClass) element, processor);
        } else if (element instanceof PsiMethod) {
            finder.processMapperStatements((PsiMethod) element, processor);
        }

    }

}
