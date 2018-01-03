package com.github.ansafari.plugin.provider;

import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMap;
import com.github.ansafari.plugin.ibatis.dom.sqlmap.SqlMapIdentifiableStatement;
import com.intellij.ide.actions.QualifiedNameProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.Nullable;


public class IdentifiableStatementQualifiedNameProvider implements QualifiedNameProvider {

    @Nullable
    @Override
    public PsiElement adjustElementToCopy(PsiElement element) {
        DomElement domElement = DomUtil.getDomElement(element);
        if (domElement != null && "id".equals(domElement.getXmlElementName()) && domElement.getParent() instanceof SqlMapIdentifiableStatement) {
            return domElement.getParent().getXmlElement();
        }
        return null;
    }

    @Nullable
    @Override
    public String getQualifiedName(PsiElement element) {
        DomElement domElement = DomUtil.getDomElement(element);
        if (domElement instanceof SqlMapIdentifiableStatement) {
            SqlMapIdentifiableStatement statement = (SqlMapIdentifiableStatement) domElement;
            SqlMap sqlMap = statement.getParentOfType(SqlMap.class, true);
            if (sqlMap != null) {
                String namespace = sqlMap.getNamespace().getRawText();
                return (namespace != null ? namespace + "." : "") + statement.getId();
            }
        }
        return null;
    }

    @Override
    public PsiElement qualifiedNameToElement(String fqn, Project project) {
        return null;
    }

    @Override
    public void insertQualifiedName(String fqn, PsiElement element, Editor editor, Project project) {

    }
}
