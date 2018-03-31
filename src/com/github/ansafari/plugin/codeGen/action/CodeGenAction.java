package com.github.ansafari.plugin.codeGen.action;

import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.ui.wizard.crud.DataSourceConfigurableWizardStep;
import com.github.ansafari.plugin.codeGen.ui.wizard.crud.FieldConfigurableWizardStep;
import com.github.ansafari.plugin.codeGen.ui.wizard.crud.OutputConfigurableWizardStep;
import com.github.ansafari.plugin.codeGen.ui.wizard.crud.TableSelectionWizardStep;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardDialog;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/10
 * Time: 下午5:24
 */
public class CodeGenAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
        Project project = anActionEvent.getProject();


        Env.project = project;
//        if (navigatable != null) {
//            Messages.showDialog(navigatable.toString(), "Selected Element:", new String[]{"OK"}, -1, null);
//        }
//        Application application = ApplicationManager.getApplication();

        StartWizardModel wizardModel = new StartWizardModel("StartWizardModel");
        DataSourceConfigurableWizardStep zero = new DataSourceConfigurableWizardStep("连接数据源", "请输入目标数据源信息");
        TableSelectionWizardStep one = new TableSelectionWizardStep("数据库表", "请选择表进行代码生成...");
        FieldConfigurableWizardStep two = new FieldConfigurableWizardStep("设置字段信息", "可编辑选中表的字段信息");
        OutputConfigurableWizardStep three = new OutputConfigurableWizardStep("配置生成选项", "配置全局生成信息");
        wizardModel.add(zero);
        wizardModel.add(one);
        wizardModel.add(two);
        wizardModel.add(three);
//        wizardModel.addAfter(three, four);

        StartWizardDialog dialog = new StartWizardDialog(anActionEvent.getProject(), true, wizardModel);
        dialog.setTitle("Auto Code");
        dialog.setModal(true);
        dialog.show();
    }


}
