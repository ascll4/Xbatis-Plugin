package com.github.ansafari.plugin.xbatis.simple;

import com.github.ansafari.plugin.xbatis.icons.XbatisIcons;
import com.github.ansafari.plugin.xbatis.model.sqlmap.SqlMapIdentifiableStatement;
import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class XmlStatementLineMarkerProvider extends RelatedItemLineMarkerProvider {

    public String getId() {
        return "XmlStatementLineMarkerProvider";
    }

    @Nullable
    @Override
    public String getName() {
        return "Configuration (XML)";
    }

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement psiElement, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (psiElement instanceof XmlTag) {
            DomElement element = DomManager.getDomManager(psiElement.getProject()).getDomElement((XmlTag) psiElement);
            if (element != null && element instanceof SqlMapIdentifiableStatement) {
                XmlTag xmlTag = element.getXmlTag();
                if (xmlTag != null) {
                    Project project = xmlTag.getProject();
                    //NotNullLazyValue
                    Set<PsiElement> psiElementSet = SimpleUtil.findJavaByPsiLiteralExpression(project, ((SqlMapIdentifiableStatement) element).getId().getValue());
                    if (psiElementSet != null && psiElementSet.size() > 0) {
                        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                                .create(XbatisIcons.NAVIGATE_TO_METHOD)
                                .setTargets(psiElementSet)
                                .setEmptyPopupText("Java Statement Not Found")
                                .setTooltipText("Navigate to a Java Statement");
                        result.add(builder.createLineMarkerInfo(xmlTag));
                    }
                }
            }
        }
    }


}
