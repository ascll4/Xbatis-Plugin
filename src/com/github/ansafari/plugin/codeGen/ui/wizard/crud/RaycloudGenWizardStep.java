package com.github.ansafari.plugin.codeGen.ui.wizard.crud;

import com.github.ansafari.plugin.codeGen.model.DataSource;
import com.github.ansafari.plugin.codeGen.storage.DataSourceStorage;
import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.storage.RaycloudGenStorage;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.github.ansafari.plugin.codeGen.util.Constants;
import com.github.ansafari.plugin.utils.CollectionUtils;
import com.github.ansafari.plugin.utils.GenUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.raycloud.util.daogen.Settings;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

public class RaycloudGenWizardStep extends WizardStep<StartWizardModel> {

    private JPanel container;
    private JPanel jPanel;
    private JButton raycloudButton;
    private JLabel labelTableName;
    private JTextField tableName_text;
    private JTextField moduleName_text;
    private JLabel moduleName_label;
    private JTextField javaPackage_text;
    private JLabel javaPackage_label;
    private JTextField genPath_text;
    private JLabel genPath_label;
    private JComboBox genScheme_text;
    private JLabel genScheme_label;
    private JLabel message_label;

    private Settings settings;

    public RaycloudGenWizardStep(String title, String explanation) {
        super(title, explanation);
        initData();

        //执行raycloud风格代码生成方案
        raycloudButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String errors = checkSettings();
                if (StringUtils.isNotBlank(errors)) {
                    message_label.setForeground(JBColor.RED);
                    message_label.setText(errors);
                    return;
                }

                if (StringUtils.isBlank(tableName_text.getText())) {
                    message_label.setForeground(JBColor.RED);
                    message_label.setText("请选择一张表");
                    return;
                }

                DataSource dataSource = ServiceManager.getService(Env.project, DataSourceStorage.class).getState();
                if (dataSource == null) {
                    Messages.showErrorDialog("数据库连接失败，请重新配置连接参数", Constants.MESSAGE_TITLE);
                } else {
                    if (settings == null) {
                        Messages.showErrorDialog("配置信息为空", Constants.MESSAGE_TITLE);
                        return;
                    }
                    Settings settings = new Settings();
                    settings.setDbName(dataSource.getDatabase());
                    settings.setDriver(dataSource.getDriver());
                    settings.setUrl(dataSource.getHost());
                    settings.setPort(dataSource.getPort());
                    settings.setDbUser(dataSource.getUser());
                    settings.setDbPwd(dataSource.getPassword());
                    settings.setDbType(1);

                    settings.setTables(Collections.singletonList(tableName_text.getText()));
                    settings.setModuleName(moduleName_text.getText());
                    settings.setJavaPackage(javaPackage_text.getText());
                    settings.setGenPath(genPath_text.getText());
                    settings.setTmplPath("dao/ibatisdao");
                    GenUtils.doGenDB(settings);

                    message_label.setForeground(new java.awt.Color(83, 128, 77));
                    message_label.setText("生成成功");

                    setData();
                }
            }
        });
    }

    private String checkSettings() {
        if (StringUtils.isBlank(tableName_text.getText())) {
            return "表名不能为空";
        }
        if (StringUtils.isBlank(moduleName_text.getText())) {
            return "模块名不能为空";
        }
        if (StringUtils.isBlank(javaPackage_text.getText())) {
            return "包名不能为空";
        }
        if (StringUtils.isBlank(genPath_text.getText())) {
            return "生成路径不能为空";
        }
        return "";
    }

    private void initData() {
        message_label.setText("");

        settings = ServiceManager.getService(Env.project, RaycloudGenStorage.class).getState();
        DataSource dataSource = ServiceManager.getService(Env.project, DataSourceStorage.class).getState();
        if (dataSource != null) {
            if (settings != null) {
                if (CollectionUtils.isNotEmpty(settings.getTables())) {
                    this.tableName_text.setText(settings.getTables().get(0));
                }
                this.moduleName_text.setText(settings.getModuleName());
                this.genPath_text.setText(StringUtils.isBlank(settings.getGenPath()) ? Env.project.getBasePath() : settings.getGenPath());
                this.javaPackage_text.setText(settings.getJavaPackage());
            }
        }
    }

    private void setData() {
        if (settings == null) {
            settings = new Settings();
        }
        settings.setTables(Collections.singletonList(tableName_text.getText()));
        settings.setModuleName(moduleName_text.getText());
        settings.setJavaPackage(javaPackage_text.getText());
        settings.setGenPath(genPath_text.getText());
        settings.setTmplPath("dao/ibatisdao");
        ServiceManager.getService(Env.project, RaycloudGenStorage.class).loadState(settings);
    }

    @Override
    public String getHelpId() {
        return "code.gen.wizard.step.RaycloudGenWizardStep";
    }

    @Override
    public JComponent prepare(WizardNavigationState wizardNavigationState) {
        return container;
    }
}
