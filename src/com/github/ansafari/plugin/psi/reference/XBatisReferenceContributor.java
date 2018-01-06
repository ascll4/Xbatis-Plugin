package com.github.ansafari.plugin.psi.reference;

import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMap;
import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.mybatis.dom.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.github.ansafari.plugin.utils.DomUtils;
import com.github.ansafari.plugin.utils.XbatisUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.CommonProcessors;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 字符串 链接到 sql statement in xml.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 21:03
 */
public class XBatisReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        //registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new SqlClientElementFilter()), new StatementIdReferenceProvider());
        //PsiJavaPatterns.psiLiteral()
        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression(), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                //.and(new SqlClientElementFilter())
                if (!(element instanceof PsiLiteralExpression)) return PsiReference.EMPTY_ARRAY;
                if (!XbatisUtils.isWithinScope(element)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{new IdentifiableStatementReference((PsiLiteral) element),};
            }
        });

    }

    /**
     * 字面量 xml tag关联.
     *
     * @author xiongjinteng@raycloud.com
     * @date 2018/1/2 21:01
     */
    static class IdentifiableStatementReference extends PsiPolyVariantReferenceBase<PsiLiteral> {

        private static final Pattern dotPattern = Pattern.compile("\\.");

        IdentifiableStatementReference(PsiLiteral expression) {
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

            DomUtils.addResults(processorResults, results);
            return results;
        }

        private List<ResolveResult> findMapperResults(String namespace, String id) {
            CommonProcessors.CollectUniquesProcessor<MapperIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
            ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processMapperStatements2(namespace, id, processor);

            Collection<MapperIdentifiableStatement> processorResults = processor.getResults();
            final List<ResolveResult> results = new ArrayList<>(processorResults.size());

            DomUtils.addResults(processorResults, results);
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

    /**
     * 字符串与sqlMap的namespace关联
     * 其实可以拆分关联sqlMap，statement，比如MsgPlan.deleteByKey  MsgPlan找sqlMap，找<select id=deleteByKey...
     *
     * @author xiongjinteng@raycloud.com
     * @date 2018/1/2 21:02
     */
    static class SqlMapNamespaceReference extends PsiPolyVariantReferenceBase<PsiLiteral> {

        public SqlMapNamespaceReference(PsiLiteral expression) {
            super(expression);
        }

        @NotNull
        @Override
        public ResolveResult[] multiResolve(boolean b) {

            String rawText = getElement().getText();

            //rawText contains quotes, i.e. only "x" count
            if (rawText.length() < 3) {
                return ResolveResult.EMPTY_ARRAY;
            }

            CommonProcessors.CollectUniquesProcessor<SqlMap> processor = new CommonProcessors.CollectUniquesProcessor<>();
            DomFileElementsFinder elementsFinder = ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class);

            String noQuotesText = rawText.substring(1, rawText.length() - 1);
            elementsFinder.processSqlMaps(noQuotesText, processor);
            if (noQuotesText.length() > 0 && noQuotesText.endsWith(".")) {
                elementsFinder.processSqlMaps(noQuotesText.substring(0, noQuotesText.length() - 1), processor);
            }

            Collection<SqlMap> processorResults = processor.getResults();
            final List<ResolveResult> results = new ArrayList<>(processorResults.size());
            //final SqlMap[] sqlMaps = processorResults.toArray(new SqlMap[processorResults.size()]);
            DomUtils.addResults(processorResults, results);
//        for (SqlMap sqlMap : sqlMaps) {
//            DomTarget target = DomTarget.getTarget(sqlMap);
//            if (target != null) {
//                XmlElement xmlElement = sqlMap.getXmlElement();
//                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
//                results.add(new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
//                    @Override
//                    public String getLocationString() {
//                        return locationString;
//                    }
//                }));
//            }
//        }
            return results.toArray(ResolveResult.EMPTY_ARRAY);

        }

        @NotNull
        public Object[] getVariants() {

            CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<>();
            ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapNamespaceNames(processor);
            return processor.toArray(new String[processor.getResults().size()]);

        }


    }
}

