package com.github.ansafari.plugin.xbatis.setting;

import com.intellij.application.options.editor.EditorOptionsProvider;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class XbatisConfigurable extends BaseConfigurable implements SearchableConfigurable, EditorOptionsProvider {
    private XbatisSettingPanel myPanel;

    @Override
    public boolean isModified() {
        return myPanel != null && myPanel.isModified();
    }

    @Override
    public JComponent createComponent() {
        myPanel = new XbatisSettingPanel();
        return myPanel.mainPanel;
    }

    @Override
    public String getDisplayName() {
        return ApplicationBundle.message("title.code.myCompletion");
    }

    @Override
    public void reset() {
        myPanel.reset();
    }

    @Override
    public void apply() {
        myPanel.apply();
    }

    @Override
    public void disposeUIResources() {
        myPanel = null;
    }

    @Override
    public String getHelpTopic() {
        return "reference.settingsdialog.IDE.editor.code.myCompletion";
    }

    @Override
    @NotNull
    public String getId() {
        return "editor.preferences.myCompletion";
    }
}
