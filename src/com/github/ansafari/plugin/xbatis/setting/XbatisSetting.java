package com.github.ansafari.plugin.xbatis.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

public class XbatisSetting implements PersistentStateComponent<Element> {
    @Nullable
    @Override
    public Element getState() {
        return null;
    }

    @Override
    public void loadState(Element element) {

    }
}
