package com.github.ansafari.plugin.generate.setting;

import com.github.ansafari.plugin.generate.GenerateModelUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.intellij.application.options.editor.EditorOptionsProvider;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.github.ansafari.plugin.generate.StatementGeneratorUtils.*;

public class XbatisConfigurable extends BaseConfigurable implements SearchableConfigurable, EditorOptionsProvider {

    private XbatisSettingStorage xbatisSettingStorage;

    private XbatisSettingPanel xbatisSettingPanel;

    private String separator = ";";

    private Splitter splitter = Splitter.on(separator).omitEmptyStrings().trimResults();

    private Joiner joiner = Joiner.on(separator);


    public XbatisConfigurable() {
        this.xbatisSettingStorage = XbatisSettingStorage.getInstance();
    }

    @Override
    public JComponent createComponent() {
        if (this.xbatisSettingPanel == null) {
            this.xbatisSettingPanel = new XbatisSettingPanel();
        }
        return xbatisSettingPanel.mainPanel;
    }

    @Override
    public String getDisplayName() {
        return "Xbatis Plugin";
    }

    @Override
    public void apply() throws ConfigurationException {
        xbatisSettingStorage.setStatementGenerateModel(GenerateModelUtils.getInstance(xbatisSettingPanel.modelComboBox.getSelectedIndex()));
        INSERT_GENERATOR.setPatterns(Sets.newHashSet(splitter.split(xbatisSettingPanel.insertPatternTextField.getText())));
        DELETE_GENERATOR.setPatterns(Sets.newHashSet(splitter.split(xbatisSettingPanel.deletePatternTextField.getText())));
        UPDATE_GENERATOR.setPatterns(Sets.newHashSet(splitter.split(xbatisSettingPanel.updatePatternTextField.getText())));
        SELECT_GENERATOR.setPatterns(Sets.newHashSet(splitter.split(xbatisSettingPanel.selectPatternTextField.getText())));
    }

    @Override
    public void reset() {
        xbatisSettingPanel.modelComboBox.setSelectedIndex(xbatisSettingStorage.getStatementGenerateModel() != null ? xbatisSettingStorage.getStatementGenerateModel().getIdentifier() : 0);
        xbatisSettingPanel.insertPatternTextField.setText(joiner.join(INSERT_GENERATOR.getPatterns()));
        xbatisSettingPanel.deletePatternTextField.setText(joiner.join(DELETE_GENERATOR.getPatterns()));
        xbatisSettingPanel.updatePatternTextField.setText(joiner.join(UPDATE_GENERATOR.getPatterns()));
        xbatisSettingPanel.selectPatternTextField.setText(joiner.join(SELECT_GENERATOR.getPatterns()));
    }

    @Override
    public boolean isModified() {
        return xbatisSettingStorage.getStatementGenerateModel().getIdentifier() != xbatisSettingPanel.modelComboBox.getSelectedIndex()
                || !joiner.join(INSERT_GENERATOR.getPatterns()).equals(xbatisSettingPanel.insertPatternTextField.getText())
                || !joiner.join(DELETE_GENERATOR.getPatterns()).equals(xbatisSettingPanel.deletePatternTextField.getText())
                || !joiner.join(UPDATE_GENERATOR.getPatterns()).equals(xbatisSettingPanel.updatePatternTextField.getText())
                || !joiner.join(SELECT_GENERATOR.getPatterns()).equals(xbatisSettingPanel.selectPatternTextField.getText());
    }

//    @Override
//    public void disposeUIResources() {
//        xbatisSettingPanel.mainPanel = null;
//    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    @NotNull
    public String getId() {
        return "XbatisConfigurable";
    }
}
