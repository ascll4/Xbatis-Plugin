package com.github.ansafari.plugin.mybatis.provider;

import com.github.ansafari.plugin.icons.Icons;
import com.github.ansafari.plugin.mybatis.dom.mapper.Mapper;
import com.github.ansafari.plugin.mybatis.dom.mapper.MapperIdentifiableStatement;
import com.github.ansafari.plugin.service.DomFileElementsFinder;
import com.github.ansafari.plugin.utils.CollectionUtils;
import com.github.ansafari.plugin.utils.XbatisUtils;
import com.google.common.collect.Collections2;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.ElementPresentationManager;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Java 类名与namespace的映射，method与id的映射，字面量与id的映射.
 * 原则上扫描范围需要限定：
 * 1.spring bean dao才进行扫描
 * 2.字面量符合配置的规则才查找XML映射 statement
 * 参考类：com.intellij.spring.gutter.SpringClassAnnotator
 *
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/29 15:16
 */
public class MybatisProxiesLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiNameIdentifierOwner) {
            DomFileElementsFinder finder = DomFileElementsFinder.getInstance(psiElement.getProject());

            if (psiElement instanceof PsiClass) {
                //匹配类
                CommonProcessors.CollectUniquesProcessor<Mapper> processor = new CommonProcessors.CollectUniquesProcessor<>();
                finder.processMappers((PsiClass) psiElement, processor);
                return createLineMarkerInfo(psiElement, processor.getResults());
            } else if (psiElement instanceof PsiMethod) {
                //匹配方法
                CommonProcessors.CollectUniquesProcessor<MapperIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<>();
                finder.processMapperStatements((PsiMethod) psiElement, processor);
                return createLineMarkerInfo(psiElement, processor.getResults());
            }

        } else if (psiElement instanceof PsiLiteralExpression) {
            //PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
            //匹配字面量如：return (MsgPlan) getSqlMapClientTemplate().queryForObject("MsgPlan.getMsgPlanByKey", params);
            //需要验证一个问题：就是.queryForObject("MsgPlan.getMsgPlanByKey", params)，同时<select id="MsgPlan.getMsgPlanByKey">居然可以映射到
            //优化方案：如果发现字面量包含1个点，则认为.前面为namespace，后面为statement id；同时statement id如果也是带点的，则进行拆分出来
            //原来的处理方法是
            String targetNamespace = "";

            if (!XbatisUtils.isWithinScope(psiElement)) {
                return null;
            }

            PsiLiteralExpression literalExpression = (PsiLiteralExpression) psiElement;
            String targetId = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
            if (StringUtils.isNotBlank(targetId)) {
                String[] values = targetId.split("\\.");
                if (values.length == 2) {
                    targetNamespace = values[0];
                    targetId = values[values.length - 1];
                }

                List<XmlElement> xmlTagList = new ArrayList<>();
                addMapperMatchElements(targetNamespace, targetId, psiElement, xmlTagList);

                if (CollectionUtils.isNotEmpty(xmlTagList)) {
                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(Icons.NAVIGATE_TO_STATEMENT).setTargets(xmlTagList).setTooltipText("Navigate to xml");
                    return builder.createLineMarkerInfo(psiElement);
                }
            }
        }
        return null;
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

    private RelatedItemLineMarkerInfo<PsiElement> createLineMarkerInfo(@NotNull PsiElement psiElement, Collection<? extends DomElement> domElements) {
        PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
        if (!domElements.isEmpty() && nameIdentifier != null) {
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(Icons.NAVIGATE_TO_STATEMENT)
                            .setAlignment(GutterIconRenderer.Alignment.CENTER)
                            .setTargets(Collections2.transform(domElements, FUN::apply))
                            .setTooltipTitle("Navigation to target in mapper xml");
            return (builder.createLineMarkerInfo(nameIdentifier));
        }
        return null;
    }

    private static final Function<DomElement, XmlTag> FUN = DomElement::getXmlTag;

//    private Function<PsiIdentifier, String> getTooltipProvider(final DomElement element) {
//        return new NullableFunction<PsiIdentifier, String>() {
//            @Override
//            public String fun(PsiIdentifier psiIdentifier) {
//                XmlElement xmlElement = element.getXmlElement();
//                if (xmlElement != null) {
//                    return element.getXmlElementName() + " in " + xmlElement.getContainingFile().getName();
//                } else {
//                    return null;
//                }
//            }
//        };
//    }
//
//    private GutterIconNavigationHandler<PsiIdentifier> getNavigationHandler(final XmlElement statement) {
//        return new GutterIconNavigationHandler<PsiIdentifier>() {
//            @Override
//            public void navigate(MouseEvent e, PsiIdentifier elt) {
//                if (statement instanceof Navigatable) {
//                    ((Navigatable) statement).navigate(true);
//                } else {
//                    throw new AssertionError("Could not navigate statement " + statement);
//                }
//            }
//        };
//    }




//            PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
//            if (processor.isFound() && nameIdentifier != null && processor.getFoundValue() != null) {
//                return new LineMarkerInfo<>(
//                        (PsiIdentifier) nameIdentifier, //we just know it, ok
//                        nameIdentifier.getTextRange(),
//                        XbatisIcons.NAVIGATE_TO_STATEMENT,
//                        Pass.UPDATE_ALL,
//                        getTooltipProvider(processor.getFoundValue()),
//                        getNavigationHandler(processor.getFoundValue().getXmlElement()),
//                        GutterIconRenderer.Alignment.CENTER
//                );
//            }


//CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<>();
//            PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
//            if (processor.isFound() && nameIdentifier != null && processor.getFoundValue() != null) {
//                return new LineMarkerInfo<>(
//                        (PsiIdentifier) nameIdentifier, //we just know it, ok
//                        nameIdentifier.getTextRange(),
//                        Icons.NAVIGATE_TO_STATEMENT,
//                        Pass.UPDATE_ALL,
//                        getTooltipProvider(processor.getFoundValue()),
//                        getNavigationHandler(processor.getFoundValue().getXmlElement()),
//                        GutterIconRenderer.Alignment.CENTER
//                );
//            }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }
}
