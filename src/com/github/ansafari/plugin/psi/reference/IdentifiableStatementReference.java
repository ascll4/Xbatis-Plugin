package com.github.ansafari.plugin.psi.reference;

import com.github.ansafari.plugin.mybatis.domain.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.ibatis.domain.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.utils.SimpleUtil;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.CommonProcessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字面量 xml tag关联
 */
public class IdentifiableStatementReference extends PsiPolyVariantReferenceBase<PsiLiteral> {

    private static final Pattern dotPattern = Pattern.compile("\\.");

    public IdentifiableStatementReference(PsiLiteral expression) {
        super(expression);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {
        String value = tryComputeConcatenatedValue();
        if (value.length() == 0) {
            return ResolveResult.EMPTY_ARRAY;
        }

        String[] parts = dotPattern.split(value);

        List<ResolveResult> resolveResultList = new ArrayList<>();

        if (parts.length == 1) {
            //.toArray(ResolveResult.EMPTY_ARRAY)
            resolveResultList.addAll(findSqlMapResults("", parts[0]));
            resolveResultList.addAll(findMapperResults("", parts[0]));
        } else {
            for (int i = 0; i < parts.length - 1; i++) {
                resolveResultList.addAll(findSqlMapResults(concatBefore(parts, i), concatAfter(parts, i + 1)));
                resolveResultList.addAll(findMapperResults(concatBefore(parts, i), concatAfter(parts, i + 1)));
            }
            //return results.toArray(new ResolveResult[results.size()]);
        }
        return resolveResultList.toArray(new ResolveResult[resolveResultList.size()]);
    }

    @NotNull
    public Object[] getVariants() {
        CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatementNames(processor);
        return processor.toArray(new String[processor.getResults().size()]);

    }

    private String tryComputeConcatenatedValue() {
        PsiPolyadicExpression parentExpression = PsiTreeUtil.getParentOfType(getElement(), PsiPolyadicExpression.class);

        if (parentExpression != null) {
            StringBuilder computedValue = new StringBuilder();
            for (PsiExpression operand : parentExpression.getOperands()) {
                if (operand instanceof PsiReference) {
                    PsiElement probableDefinition = ((PsiReference) operand).resolve();
                    if (probableDefinition instanceof PsiVariable) {
                        PsiExpression initializer = ((PsiVariable) probableDefinition).getInitializer();
                        if (initializer != null) {
                            Object value = JavaConstantExpressionEvaluator.computeConstantExpression(initializer, true);
                            if (value instanceof String) {
                                computedValue.append(value);
                            }
                        }
                    }
                } else {
                    Object value = JavaConstantExpressionEvaluator.computeConstantExpression(operand, true);
                    if (value instanceof String) {
                        computedValue.append(value);
                    }
                }
            }
            return computedValue.toString();
        } else {
            String rawText = getElement().getText();
            //with quotes, i.e. at least "x" count
            if (rawText.length() < 3) {
                return "";
            }
            //clean up quotes
            return rawText.substring(1, rawText.length() - 1);
        }

    }

    private List<ResolveResult> findSqlMapResults(String namespace, String id) {
        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);

        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        final List<ResolveResult> results = new ArrayList<>(processorResults.size());

        SimpleUtil.addResults(processorResults, results);
        return results;
    }

    private List<ResolveResult> findMapperResults(String namespace, String id) {
        CommonProcessors.CollectUniquesProcessor<MapperIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processMapperStatements2(namespace, id, processor);

        Collection<MapperIdentifiableStatement> processorResults = processor.getResults();
        final List<ResolveResult> results = new ArrayList<>(processorResults.size());

        SimpleUtil.addResults(processorResults, results);
        return results;
    }

    private String concatBefore(String[] parts, int before) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= before; i++) {
            sb.append(parts[i]);
            if (i + 1 <= before) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    private String concatAfter(String[] parts, int after) {
        StringBuilder sb = new StringBuilder();
        for (int i = after; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i + 1 < parts.length) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

}
