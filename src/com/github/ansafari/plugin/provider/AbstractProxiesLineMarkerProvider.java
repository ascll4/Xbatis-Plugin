package com.github.ansafari.plugin.provider;

import com.github.ansafari.plugin.icons.Icons;
import com.google.common.collect.Collections2;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.presentation.java.SymbolPresentationUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractProxiesLineMarkerProvider implements LineMarkerProvider {

    private static final Function<DomElement, XmlTag> FUN = DomElement::getXmlTag;

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }

    protected RelatedItemLineMarkerInfo<PsiElement> createLineMarkerInfo(@NotNull PsiElement psiElement, Collection<? extends DomElement> domElements) {
        PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
        if (!domElements.isEmpty() && nameIdentifier != null) {
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(Icons.NAVIGATE_TO_STATEMENT)
                            .setCellRenderer(new PsiElementListCellRenderer() {
                                @Override
                                public String getElementText(PsiElement psiElement) {
                                    if (psiElement instanceof XmlTag) {
                                        XmlAttribute attribute = ((XmlTag) psiElement).getAttribute("id");
                                        if (attribute != null) {
                                            return attribute.getValue();
                                        }
                                    }
                                    return SymbolPresentationUtil.getSymbolPresentableText(psiElement);
                                }

                                @Nullable
                                @Override
                                protected String getContainerText(PsiElement psiElement, String s) {
                                    if (psiElement instanceof XmlTag) {
                                        return psiElement.getContainingFile().getName();
                                    }
                                    return SymbolPresentationUtil.getSymbolContainerText(psiElement);
                                }

                                @Override
                                protected int getIconFlags() {
                                    return 0;
                                }
                            })
                            .setAlignment(GutterIconRenderer.Alignment.CENTER)
                            .setTargets(Collections2.transform(domElements, FUN::apply))
                            .setTooltipTitle("Navigation to target in mapper xml");
            return (builder.createLineMarkerInfo(nameIdentifier));
        }
        return null;
    }


}
