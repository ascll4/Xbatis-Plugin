package com.github.ansafari.plugin.service;

import com.github.ansafari.plugin.ibatis.dom.sqlmap.ResultMap;
import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMap;
import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.mybatis.dom.mapper.Mapper;
import com.github.ansafari.plugin.mybatis.dom.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.utils.CollectionUtils;
import com.intellij.codeInsight.navigation.ClassImplementationsSearch;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public class DomFileElementsFinder {

    private final Project project;
    private final DomService domService;
    private final Application application;

    public DomFileElementsFinder(Project project, DomService domService, Application application) {
        this.project = project;
        this.domService = domService;
        this.application = application;
    }

    public static DomFileElementsFinder getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, DomFileElementsFinder.class);
    }

    public void processSqlMapStatements(@NotNull String targetNamespace, @NotNull String targetId, @NotNull Processor<? super SqlMapIdentifiableStatement> processor) {

        nsloop:
        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap sqlMap = fileElement.getRootElement();
            String namespace = sqlMap.getNamespace().getRawText();
            if (targetNamespace.equals(namespace) || targetNamespace.length() == 0) {
                for (SqlMapIdentifiableStatement statement : sqlMap.getIdentifiableStatements()) {
                    // id匹配或者namespace.id 匹配
                    if (isMatch(targetNamespace, targetId, statement.getId().getRawText())) {
                        if (!processor.process(statement)) {
                            return;
                        }
                        continue nsloop;
                    }
                }
            }
        }
    }

    public void processMapperStatements2(@NotNull String targetNamespace, @NotNull String targetId, @NotNull Processor<? super MapperIdentifiableStatement> processor) {

        nsloop:
        for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
            Mapper mapper = fileElement.getRootElement();
            String namespace = mapper.getNamespace().getRawText();
            if (targetNamespace.equals(namespace) || targetNamespace.length() == 0) {
                for (MapperIdentifiableStatement statement : mapper.getIdentifiableStatements()) {
                    if (isMatch(targetNamespace, targetId, statement.getId().getRawText())) {
                        if (!processor.process(statement)) {
                            return;
                        }
                        continue nsloop;
                    }
                }
            }
        }
    }

    /**
     * id匹配或者namespace.id 匹配
     *
     * @param targetNamespace targetNamespace
     * @param targetId        targetId
     * @param statementId     statementId
     * @return boolean
     */
    private static boolean isMatch(String targetNamespace, String targetId, String statementId) {
        String[] values = statementId.split("\\.");
        if (values.length == 1) {
            return StringUtils.equals(targetId, values[0]);
        } else if (values.length == 2) {
            return (StringUtils.isBlank(targetNamespace) || StringUtils.equals(targetNamespace, values[0]))
                    && StringUtils.equals(targetId, values[values.length - 1]);
        }
        return false;
    }

    public void processSqlMapStatementNames(@NotNull Processor<String> processor) {

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            for (SqlMapIdentifiableStatement statement : rootElement.getIdentifiableStatements()) {
                String id = statement.getId().getRawText();
                if (id != null && (namespace != null && !processor.process(namespace + "." + id) || namespace == null && !processor.process(id))) {
                    return;
                }
            }
        }
    }

    public void processSqlMaps(@NotNull String targetNamespace, @NotNull Processor<? super SqlMap> processor) {

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap sqlMap = fileElement.getRootElement();
            String namespace = sqlMap.getNamespace().getRawText();
            if (targetNamespace.equals(namespace)) {
                if (!processor.process(sqlMap)) {
                    return;
                }
            }
        }
    }

    public void processSqlMapNamespaceNames(CommonProcessors.CollectProcessor<String> processor) {

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap sqlMap = fileElement.getRootElement();
            if (sqlMap.getNamespace().getRawText() != null && !processor.process(sqlMap.getNamespace().getRawText())) {
                return;
            }
        }
    }

    public void processResultMaps(@NotNull String targetNamespace, @NotNull String targetId, @NotNull Processor<? super ResultMap> processor) {
        nsloop:
        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            if (targetNamespace.equals(namespace) || targetNamespace.length() == 0 && namespace == null) {
                for (ResultMap resultMap : rootElement.getResultMaps()) {
                    if (targetId.equals(resultMap.getId().getRawText())) {
                        if (!processor.process(resultMap)) {
                            return;
                        }
                        continue nsloop;
                    }
                }
            }
        }
    }

    public void processResultMapNames(@NotNull Processor<String> processor) {
        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            for (ResultMap resultMap : rootElement.getResultMaps()) {
                String id = resultMap.getId().getRawText();
                if (id != null && (namespace != null && !processor.process(namespace + "." + id) || namespace == null && !processor.process(id))) {
                    return;
                }
            }
        }
    }

    public void processMappers(@NotNull final PsiClass clazz, @NotNull final Processor<? super Mapper> processor) {
        application.runReadAction(() -> {
            if (clazz.isInterface()) {
                PsiIdentifier nameIdentifier = clazz.getNameIdentifier();
                String qualifiedName = clazz.getQualifiedName();
                if (nameIdentifier != null && qualifiedName != null) {
                    processMappers(qualifiedName, processor);
                }
            }
        });
    }

    public void processMapperStatements(@NotNull final PsiMethod method, @NotNull final Processor<? super MapperIdentifiableStatement> processor) {
        application.runReadAction(() -> {
            PsiClass clazz = method.getContainingClass();
            if (clazz != null && clazz.isInterface()) {
                String qualifiedName = clazz.getQualifiedName();
                String methodName = method.getName();
                if (qualifiedName != null) {
                    processMapperStatements(qualifiedName, methodName, processor);
                }
                processImplementationStatements(clazz, method, processor);
            }
        });
    }

    private void processImplementationStatements(@NotNull PsiClass clazz, @NotNull final PsiMethod method, @NotNull final Processor<? super MapperIdentifiableStatement> processor) {
        CommonProcessors.CollectProcessor<PsiElement> processor1 = new CommonProcessors.CollectProcessor<>();
        ClassImplementationsSearch.processImplementations(clazz, processor1, clazz.getUseScope());
        Collection<PsiElement> collection = processor1.getResults();
        if (CollectionUtils.isNotEmpty(collection)) {
            for (PsiElement psiElement : collection) {
                if (psiElement instanceof PsiClass) {
                    String qualifiedName = ((PsiClass) psiElement).getQualifiedName();
                    String methodName = method.getName();
                    if (qualifiedName != null) {
                        processMapperStatements(qualifiedName, methodName, processor);
                    }
                }
            }
        }
    }

    public boolean existsMapperStatement(PsiMethod method) {
        CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<>();
        processMapperStatements(method, processor);
        return processor.isFound();
    }

    private void processMappers(String className, Processor<? super Mapper> processor) {
        for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
            Mapper mapper = fileElement.getRootElement();
            if (className.equals(mapper.getNamespace().getRawText())) {
                if (!processor.process(mapper)) {
                    return;
                }
            }
        }
    }

    private void processMapperStatements(String className, String methodName, Processor<? super MapperIdentifiableStatement> processor) {
        for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
            Mapper mapper = fileElement.getRootElement();
            if (className.equals(mapper.getNamespace().getRawText())) {
                for (MapperIdentifiableStatement statement : mapper.getIdentifiableStatements()) {
                    if (methodName.equals(statement.getId().getRawText())) {
                        if (!processor.process(statement)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private List<DomFileElement<SqlMap>> findSqlMapFileElements() {
        return domService.getFileElements(SqlMap.class, project, GlobalSearchScope.allScope(project));
    }

    private List<DomFileElement<Mapper>> findMapperFileElements() {
        return domService.getFileElements(Mapper.class, project, GlobalSearchScope.allScope(project));
    }
}


