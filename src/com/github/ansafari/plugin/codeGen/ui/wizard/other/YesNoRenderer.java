package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class YesNoRenderer extends JComboBox implements TableCellRenderer {
    private static final long serialVersionUID = -8624401777277852691L;

    public YesNoRenderer() {
        super();
        addItem("是");
        addItem("否");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        boolean isTrue = "1".equals(value);
        setSelectedIndex(isTrue ? 0 : 1);
        return this;
    }

} 