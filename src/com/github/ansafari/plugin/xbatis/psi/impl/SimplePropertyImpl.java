// This is a generated file. Not intended for manual editing.
package com.github.ansafari.plugin.xbatis.psi.impl;

import com.github.ansafari.plugin.xbatis.psi.SimpleProperty;
import com.github.ansafari.plugin.xbatis.psi.SimpleVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;


public class SimplePropertyImpl extends SimpleNamedElementImpl implements SimpleProperty {

    public SimplePropertyImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull SimpleVisitor visitor) {
        visitor.visitProperty(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SimpleVisitor) accept((SimpleVisitor) visitor);
        else super.accept(visitor);
    }

    public String getKey() {
        return SimplePsiImplUtil.getKey(this);
    }

    public String getValue() {
        return SimplePsiImplUtil.getValue(this);
    }

    public String getName() {
        return SimplePsiImplUtil.getName(this);
    }

    public PsiElement setName(String newName) {
        return SimplePsiImplUtil.setName(this, newName);
    }

    public PsiElement getNameIdentifier() {
        return SimplePsiImplUtil.getNameIdentifier(this);
    }

    public ItemPresentation getPresentation() {
        return SimplePsiImplUtil.getPresentation(this);
    }

}
