package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/18
 * Time: 上午8:23
 */
public class SimpleTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 6439085315997072755L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //设置偶数行颜色
        if (row % 2 == 0) {
            setBackground(Color.white);
        } else {
            setBackground(new Color(206, 231, 255));

        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
