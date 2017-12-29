package com.github.ansafari.plugin.utils;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.PomTargetPsiElementImpl;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomTarget;

import java.util.Collection;
import java.util.List;

public class SimpleUtil {

    public static <T> void addResults(Collection<? extends DomElement> statements, List<ResolveResult> results) {
        if (statements == null || statements.size() <= 0 || results == null) {
            return;
        }
        for (DomElement statement : statements) {
            DomTarget target = DomTarget.getTarget(statement);
            if (target != null) {
                XmlElement xmlElement = statement.getXmlElement();
                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                results.add(new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
                    @Override
                    public String getLocationString() {
                        return locationString;
                    }
                }));
            }
        }
    }
}