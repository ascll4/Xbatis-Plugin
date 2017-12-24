package com.github.ansafari.plugin.xbatis.provider;

import com.github.ansafari.plugin.xbatis.icons.XbatisIcons;
import com.github.ansafari.plugin.xbatis.model.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.xbatis.model.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.xbatis.service.DomFileElementsFinder;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Function;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.ElementPresentationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class XBatisProxiesLineMarkerProvider implements LineMarkerProvider {
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiNameIdentifierOwner) {
            DomFileElementsFinder finder = ServiceManager.getService(psiElement.getProject(), DomFileElementsFinder.class);
            CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<>();
            if (psiElement instanceof PsiClass) {
                //匹配类
                finder.processMappers((PsiClass) psiElement, processor);
            } else if (psiElement instanceof PsiMethod) {
                //匹配方法
                finder.processMapperStatements((PsiMethod) psiElement, processor);
            }

            PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
            if (processor.isFound() && nameIdentifier != null) {
                return new LineMarkerInfo<>(
                        (PsiIdentifier) nameIdentifier, //we just know it, ok
                        nameIdentifier.getTextRange(),
                        XbatisIcons.NAVIGATE_TO_STATEMENT,
                        Pass.UPDATE_ALL,
                        getTooltipProvider(processor.getFoundValue()),
                        getNavigationHandler(processor.getFoundValue().getXmlElement()),
                        GutterIconRenderer.Alignment.CENTER
                );
            }
        } else if (psiElement instanceof PsiLiteralExpression) {
            //匹配字面量如：return (MsgPlan) getSqlMapClientTemplate().queryForObject("MsgPlan.getMsgPlanByKey", params);
            //需要优化
            String namespace = "";
            PsiLiteralExpression literalExpression = (PsiLiteralExpression) psiElement;
            String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
            if (value != null && value.length() > 0) {
                List<XmlElement> xmlTagList = new ArrayList<>();

                addSqlMapMatchElements(namespace, value, psiElement, xmlTagList);

                //在这里搓逼地支持了Mapper文件的关联
                addMapperMatchElements(namespace, value, psiElement, xmlTagList);

                if (xmlTagList.size() > 0) {
                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(XbatisIcons.NAVIGATE_TO_STATEMENT).setTargets(xmlTagList).setTooltipText("Navigate to xml");
                    return builder.createLineMarkerInfo(psiElement);
                }
            }
        }
        return null;
    }


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