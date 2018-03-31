package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import com.intellij.openapi.project.Project;
import com.intellij.ui.wizard.WizardDialog;
import com.intellij.ui.wizard.WizardModel;

/**
 * Created with IntelliJ IDEA.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/10
 * Time: 下午11:13
 */
public class StartWizardDialog extends WizardDialog {

    public StartWizardDialog(Project project, boolean b, WizardModel wizardModel) {
        super(project, b, wizardModel);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
