package com.github.ansafari.plugin.provider;

import com.github.ansafari.plugin.setting.XbatisSettingStorage;
import com.github.ansafari.plugin.generate.GenerateModel;
import com.github.ansafari.plugin.icons.XbatisIcons;
import com.github.ansafari.plugin.mybatis.domain.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.ibatis.domain.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Function;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.ElementPresentationManager;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Java 类名与namespace的映射，method与id的映射，字面量与id的映射.
 * 原则上扫描范围需要限定：
 * 1.spring bean dao才进行扫描
 * 2.字面量符合配置的规则才查找XML映射 statement
 *
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/29 15:16
 */
public class ProxiesLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiLiteralExpression) {
            //PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
            //匹配字面量如：return (MsgPlan) getSqlMapClientTemplate().queryForObject("MsgPlan.getMsgPlanByKey", params);
            //需要验证一个问题：就是.queryForObject("MsgPlan.getMsgPlanByKey", params)，同时<select id="MsgPlan.getMsgPlanByKey">居然可以映射到
            //优化方案：如果发现字面量包含1个点，则认为.前面为namespace，后面为statement id；同时statement id如果也是带点的，则进行拆分出来
            //原来的处理方法是
            String targetNamespace = "";
            PsiLiteralExpression literalExpression = (PsiLiteralExpression) psiElement;
            String targetId = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
            if (StringUtils.isNotBlank(targetId)) {
                XbatisSettingStorage storage = XbatisSettingStorage.getInstance();
                GenerateModel generateModel = storage.getStatementGenerateModel();

                String[] values = targetId.split("\\.");
                if (values.length == 2) {
                    targetNamespace = values[0];
                    targetId = values[values.length - 1];
                }

                //查询是否符合关联的字符规则，不符合规则跳过
//                if (!StatementGeneratorUtils.matchesAny(targetId)) {
//                    return null;
//                }

                List<XmlElement> xmlTagList = new ArrayList<>();

                addSqlMapMatchElements(targetNamespace, targetId, psiElement, xmlTagList);

                //在这里搓逼地支持了Mapper文件的关联
                addMapperMatchElements(targetNamespace, targetId, psiElement, xmlTagList);

                if (xmlTagList.size() > 0) {
                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(XbatisIcons.NAVIGATE_TO_STATEMENT).setTargets(xmlTagList).setTooltipText("Navigate to xml");
                    return builder.createLineMarkerInfo(psiElement);
                }
            }
        }
        return null;
    }


//    if (psiElement instanceof PsiNameIdentifierOwner) {
//        DomFileElementsFinder finder = ServiceManager.getService(psiElement.getProject(), DomFileElementsFinder.class);
//        CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<>();
//        if (psiElement instanceof PsiClass) {
//            //匹配类
//            finder.processMappers((PsiClass) psiElement, processor);
//        } else if (psiElement instanceof PsiMethod) {
//            //匹配方法
//            finder.processMapperStatements((PsiMethod) psiElement, processor);
//        }
//
//        PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
//        if (processor.isFound() && nameIdentifier != null) {
//            return new LineMarkerInfo<>(
//                    (PsiIdentifier) nameIdentifier, //we just know it, ok
//                    nameIdentifier.getTextRange(),
//                    XbatisIcons.NAVIGATE_TO_STATEMENT,
//                    Pass.UPDATE_ALL,
//                    getTooltipProvider(processor.getFoundValue()),
//                    getNavigationHandler(processor.getFoundValue().getXmlElement()),
//                    GutterIconRenderer.Alignment.CENTER
//            );
//        }
//    } else if


    private void addSqlMapMatchElements(String namespace, @NotNull String value, @NotNull PsiElement psiElement, List<XmlElement> xmlTagList) {
        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
        ServiceManager.getService(psiElement.getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, value, processor);
        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        if (processorResults.size() > 0) {
            for (SqlMapIdentifiableStatement statement : processorResults) {
                XmlElement xmlElement = statement.getId().getXmlElement();
                String idValue = statement.getId().getStringValue();
                if (idValue != null) {
                    ElementPresentationManager.getIcon(statement);
                    if (namespace.length() > 0) {
                        // results.add(new BaseNavigationItem(psiElement, "" + "." + value, icon));
                        xmlTagList.add(xmlElement);
                    } else {
                        // results.add(new BaseNavigationItem(psiElement, value, icon));
                        xmlTagList.add(xmlElement);
                    }
                }
            }
        }
    }

    private void addMapperMatchElements(String namespace, @NotNull String value, @NotNull PsiElement psiElement, List<XmlElement> xmlTagList) {
        CommonProcessors.CollectUniquesProcessor<MapperIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
        ServiceManager.getService(psiElement.getProject(), DomFileElementsFinder.class).processMapperStatements2(namespace, value, processor);
        Collection<MapperIdentifiableStatement> processorResults = processor.getResults();
        if (processorResults.size() > 0) {
            for (MapperIdentifiableStatement statement : processorResults) {
                XmlElement xmlElement = statement.getId().getXmlElement();
                String idValue = statement.getId().getStringValue();
                if (idValue != null) {
                    ElementPresentationManager.getIcon(statement);
                    if (namespace.length() > 0) {
                        // results.add(new BaseNavigationItem(psiElement, "" + "." + value, icon));
                        xmlTagList.add(xmlElement);
                    } else {
                        // results.add(new BaseNavigationItem(psiElement, value, icon));
                        xmlTagList.add(xmlElement);
                    }
                }
            }
        }
    }


    private Function<PsiIdentifier, String> getTooltipProvider(final DomElement element) {
        return new NullableFunction<PsiIdentifier, String>() {
            @Override
            public String fun(PsiIdentifier psiIdentifier) {
                XmlElement xmlElement = element.getXmlElement();
                if (xmlElement != null) {
                    return element.getXmlElementName() + " in " + xmlElement.getContainingFile().getName();
                } else {
                    return null;
                }
            }
        };
    }

    private GutterIconNavigationHandler<PsiIdentifier> getNavigationHandler(final XmlElement statement) {
        return new GutterIconNavigationHandler<PsiIdentifier>() {
            @Override
            public void navigate(MouseEvent e, PsiIdentifier elt) {
                if (statement instanceof Navigatable) {
                    ((Navigatable) statement).navigate(true);
                } else {
                    throw new AssertionError("Could not navigate statement " + statement);
                }
            }
        };
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }
}
