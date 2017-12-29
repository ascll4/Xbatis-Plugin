package com.github.ansafari.plugin.setting;

import javax.swing.*;

public class XbatisSettingPanel {


    JPanel mainPanel;
    public JComboBox modelComboBox;
    public JTextField insertPatternTextField;
    public JTextField deletePatternTextField;
    public JTextField updatePatternTextField;
    public JTextField selectPatternTextField;

    boolean isModified() {
        return false;
    }

    void reset() {
    }

    void apply() {

    }
}
