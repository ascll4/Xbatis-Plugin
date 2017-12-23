package com.github.ansafari.plugin.xbatis.provider;

import com.github.ansafari.plugin.xbatis.icons.XbatisIcons;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class MapperDefinitionLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof PsiLiteralExpression) {
            PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
            String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
            if (value != null) {
                Project project = element.getProject();
                List<XmlTag> xmlTagList = SimpleUtil.findXmlTags(project, value);
                if (xmlTagList != null && xmlTagList.size() > 0) {
                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                            .create(XbatisIcons.NAVIGATE_TO_STATEMENT)
                            .setTargets(xmlTagList).setTooltipText("Navigate to xml");
                    result.add(builder.createLineMarkerInfo(element));
                }
            }
        }
    }
}
