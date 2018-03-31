package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.ui.table.TableView;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/19
 * Time: 下午8:59
 */
public class WizardStepFive extends WizardStep<StartWizardModel> {
    private JPanel container;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private JTable table1;
    private final TableView myTableVeiw;

    public WizardStepFive(String title, String explanation) {
        super(title, explanation);

        myTableVeiw = new TableView(new ListTableModel((new ColumnInfo[]{NAME, VALUE})));
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        return null;
    }

    private ColumnInfo NAME = new ColumnInfo<EnvironmentVariable, String>("Name") {
        public String valueOf(EnvironmentVariable environmentVariable) {
            return environmentVariable.getName();
        }

        public Class getColumnClass() {
            return String.class;
        }

        public boolean isCellEditable(EnvironmentVariable environmentVariable) {
            return environmentVariable.getNameIsWriteable();
        }

        public void setValue(EnvironmentVariable environmentVariable, String s) {
            if (s.equals(valueOf(environmentVariable))) {
                return;
            }
            environmentVariable.setName(s);
            //setModified();
        }
    };

    private ColumnInfo VALUE = new ColumnInfo<EnvironmentVariable, String>("Value") {
        public String valueOf(EnvironmentVariable environmentVariable) {
            return environmentVariable.getValue();
        }

        public Class getColumnClass() {
            return String.class;
        }

        public boolean isCellEditable(EnvironmentVariable environmentVariable) {
            return !environmentVariable.getIsPredefined();
        }

        public void setValue(EnvironmentVariable environmentVariable, String s) {
            if (s.equals(valueOf(environmentVariable))) {
                return;
            }
            environmentVariable.setValue(s);
            //setModified();
        }

    };


}
