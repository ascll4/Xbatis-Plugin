package com.github.ansafari.plugin.xbatis;

import com.github.ansafari.plugin.xbatis.utils.SimpleUtil;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiLiteralExpression) {
            PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
            String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;

            if (value != null && value.length() > 0) {
                Project project = element.getProject();
                List<XmlTag> xmlTagList = SimpleUtil.findXmlTags(project, value);
                if (xmlTagList.size() == 1) {
                    TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset());
                    Annotation annotation = holder.createInfoAnnotation(range, null);
                    annotation.setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
                } else if (xmlTagList.size() == 0) {
                    TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset());
                    holder.createErrorAnnotation(range, "Unresolved property").
                            registerFix(new CreatePropertyQuickFix(value));
                }
            }
        }
    }
}
