package com.github.ansafari.plugin.ibatis.search;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

public class IbatisProxiesDefinitionsSearcher extends QueryExecutorBase<PsiElement, PsiElement> {
    @Override
    public void processQuery(@NotNull PsiElement psiElement, @NotNull Processor<PsiElement> processor) {
        //DomService.getInstance().
        //getImplementationElements
        //fileBasedIndex
        //definitionsSearch
        DumbService.getInstance(psiElement.getProject()).runReadActionInSmartMode(new Runnable() {
            @Override
            public void run() {
                System.out.println(psiElement.getText());
                //DomFileElementsFinder.getInstance(psiElement.getProject()).processSqlMapStatements();
            }
        });
    }
}
