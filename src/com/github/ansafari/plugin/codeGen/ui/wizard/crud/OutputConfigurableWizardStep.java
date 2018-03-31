package com.github.ansafari.plugin.codeGen.ui.wizard.crud;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import freemarker.template.Configuration;
import com.github.ansafari.plugin.codeGen.model.CodeGenModel;
import com.github.ansafari.plugin.codeGen.storage.CodeGenModelStorage;
import com.github.ansafari.plugin.codeGen.storage.ColumnModelConfigStorage;
import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.github.ansafari.plugin.codeGen.util.Constants;
import com.github.ansafari.plugin.codeGen.util.FreeMakerUtils;
import com.github.ansafari.plugin.codeGen.util.StringUtils;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成输出配置.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/15
 * Time: 上午1:40
 */
public class OutputConfigurableWizardStep extends WizardStep<StartWizardModel> {
    private static final Logger logger = Logger.getInstance(OutputConfigurableWizardStep.class);

    private JPanel container;
    private JTextField tableName_txt;
    private JLabel tableName_label;
    private JLabel tableComment_label;
    private JTextField tableComment_txt;
    private JLabel functionNameEn_label;
    private JTextField functionNameEn_txt;
    private JLabel functionNameCn_label;
    private JTextField functionNameCn_txt;
    private JLabel urlPrefix_label;
    private JTextField urlPrefix_txt;
    private JLabel jspLocation_label;
    private JTextField jspLocation_txt;
    private JTextField permission_txt;
    private JLabel author_label;
    private JTextField author_txt;
    private JTextField version_txt;
    private JLabel version_label;
    private JLabel filePath_label;
    private JTextField filePath_txt;
    private JButton select_btn;
    private JPanel jPanel1;
    private JLabel topPackage_label;
    private JTextField topPackage_txt;
    private CodeGenModel codeGenModel;

    public OutputConfigurableWizardStep(String title, String explanation) {
        super(title, explanation);
        select_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("请选择保存代码的文件夹");
                chooser.showOpenDialog(container);
                File file = chooser.getSelectedFile() == null ? chooser.getCurrentDirectory() : chooser.getSelectedFile();
                if (file == null) return;
                if (file.isDirectory()) {
                    logger.info("文件夹:" + file.getAbsolutePath());
                    filePath_txt.setText(file.getAbsolutePath());
                }
                logger.info(file.getName());
            }
        });

        topPackage_txt.addFocusListener(focusAdapter);
        functionNameCn_txt.addFocusListener(focusAdapter);
        functionNameEn_txt.addFocusListener(focusAdapter);
        urlPrefix_txt.addFocusListener(focusAdapter);
        jspLocation_txt.addFocusListener(focusAdapter);
        author_txt.addFocusListener(focusAdapter);
        filePath_txt.addFocusListener(focusAdapter);
        version_txt.addFocusListener(focusAdapter);
    }

    private FocusAdapter focusAdapter = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            logger.info("focusGained：" + e.getComponent().getClass().getSimpleName());
            super.focusGained(e);
        }

        @Override
        public void focusLost(FocusEvent e) {
            getData(codeGenModel);
            logger.info("focusLost：" + e.getComponent().getClass().getSimpleName());
            super.focusLost(e);
        }
    };

    @Override
    public JComponent prepare(WizardNavigationState state) {
        CodeGenModelStorage codeGenModelStorage = ServiceManager.getService(Env.project, CodeGenModelStorage.class);
        codeGenModel = codeGenModelStorage.getState();
        if (codeGenModel != null) {
            setData(codeGenModel);
        }
        return container;
    }


    public boolean checkInput() {
        if (StringUtils.isBlank(topPackage_txt.getText())) {
            Messages.showErrorDialog("请输入顶层包名", Constants.MESSAGE_TITLE);
            return false;
        }

        if (StringUtils.isBlank(functionNameEn_txt.getText())) {
            Messages.showErrorDialog("请输入功能名(EN)", Constants.MESSAGE_TITLE);
            return false;
        }

        if (StringUtils.isBlank(functionNameCn_txt.getText())) {
            Messages.showErrorDialog("请输入功能名(CN)", Constants.MESSAGE_TITLE);
            return false;
        }

        if (StringUtils.isBlank(urlPrefix_txt.getText())) {
            Messages.showErrorDialog("请输入URL前缀", Constants.MESSAGE_TITLE);
            return false;
        }

        if (StringUtils.isBlank(jspLocation_txt.getText())) {
            Messages.showErrorDialog("请输入JSP路径", Constants.MESSAGE_TITLE);
            return false;
        }

        if (StringUtils.isBlank(filePath_txt.getText())) {
            Messages.showErrorDialog("请选择代码生成路径", Constants.MESSAGE_TITLE);
            return false;
        }

        return true;
    }

    // TODO: 2017/4/16 返回值不起作用
    @Override
    public boolean onFinish() {
        if (!checkInput()) return false;
        CodeGenModelStorage codeGenModelStorage = ServiceManager.getService(Env.project, CodeGenModelStorage.class);
        if (codeGenModel != null && checkInput()) {
            codeGenModelStorage.loadState(codeGenModel);

            Map data = new HashMap();
            data.put("columnList", ServiceManager.getService(Env.project, ColumnModelConfigStorage.class).getState());
            data.put("table", codeGenModel);

            //执行代码生成
            genCode(data);

            Messages.showInfoMessage("代码生成成功", "提示");
            logger.info("OutputConfigurableWizardStep.onFinish---codeGenModel loadState success");
            return super.onFinish();
        }
        return false;

    }


    public boolean genCode(Map data) {
        // TODO: 2017/4/16 data需要进行key校验；
        //生成文件
        Configuration freemakerCfg = FreeMakerUtils.getFreeMarkerCfg(this.getClass(), "template");
        String basePath = "";
        //Configuration freemakerCfg = FreeMakerUtils.getFreeMarkerCfg(XmlObjectCrud.getFtlPath());
        FreeMakerUtils.generateFile(freemakerCfg, "entity.ftl", data, "src" + File.separator + StringUtils.replace(codeGenModel.getTopPackage(), ".", File.separator) + File.separator + "entity" + File.separator, StringUtils.capitalize(codeGenModel.getTableJavaName()) + ".java", basePath);
        return false;
    }

    public void setData(CodeGenModel data) {
        tableName_txt.setText(data.getTableName());
        tableComment_txt.setText(data.getTableComment());
        functionNameEn_txt.setText(data.getFunctionNameEn());
        functionNameCn_txt.setText(data.getFunctionNameCn());
        urlPrefix_txt.setText(data.getUrlPrefix());
        jspLocation_txt.setText(data.getJspLocation());
        author_txt.setText(data.getAuthor());
        version_txt.setText(data.getVersion());
        filePath_txt.setText(data.getFilePath());
        topPackage_txt.setText(data.getTopPackage());
    }

    public void getData(CodeGenModel data) {
        data.setTableName(tableName_txt.getText());
        data.setTableComment(tableComment_txt.getText());
        data.setFunctionNameEn(functionNameEn_txt.getText());
        data.setFunctionNameCn(functionNameCn_txt.getText());
        data.setUrlPrefix(urlPrefix_txt.getText());
        data.setJspLocation(jspLocation_txt.getText());
        data.setAuthor(author_txt.getText());
        data.setVersion(version_txt.getText());
        data.setFilePath(filePath_txt.getText());
        data.setTopPackage(topPackage_txt.getText());
    }

    public boolean isModified(CodeGenModel data) {
        if (tableName_txt.getText() != null ? !tableName_txt.getText().equals(data.getTableName()) : data.getTableName() != null)
            return true;
        if (tableComment_txt.getText() != null ? !tableComment_txt.getText().equals(data.getTableComment()) : data.getTableComment() != null)
            return true;
        if (functionNameEn_txt.getText() != null ? !functionNameEn_txt.getText().equals(data.getFunctionNameEn()) : data.getFunctionNameEn() != null)
            return true;
        if (functionNameCn_txt.getText() != null ? !functionNameCn_txt.getText().equals(data.getFunctionNameCn()) : data.getFunctionNameCn() != null)
            return true;
        if (urlPrefix_txt.getText() != null ? !urlPrefix_txt.getText().equals(data.getUrlPrefix()) : data.getUrlPrefix() != null)
            return true;
        if (jspLocation_txt.getText() != null ? !jspLocation_txt.getText().equals(data.getJspLocation()) : data.getJspLocation() != null)
            return true;
        if (author_txt.getText() != null ? !author_txt.getText().equals(data.getAuthor()) : data.getAuthor() != null)
            return true;
        if (version_txt.getText() != null ? !version_txt.getText().equals(data.getVersion()) : data.getVersion() != null)
            return true;
        if (filePath_txt.getText() != null ? !filePath_txt.getText().equals(data.getFilePath()) : data.getFilePath() != null)
            return true;
        if (topPackage_txt.getText() != null ? !topPackage_txt.getText().equals(data.getTopPackage()) : data.getTopPackage() != null)
            return true;
        return false;
    }

    @Override
    public String getHelpId() {
        return "code.gen.wizard.step.three";
    }
}
