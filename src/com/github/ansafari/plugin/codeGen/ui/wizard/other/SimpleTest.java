package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleTest extends JFrame {
    private JTable table = new JTable(10, 10);

    private JButton button = new JButton("AddRow");

    private Rectangle rect = new Rectangle();

    public SimpleTest() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPane.add(button, BorderLayout.SOUTH);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel) table.getModel()).insertRow(
                        table.getRowCount() - 1,
                        new Object[]{"", "", "", "", "",});
                rect = new Rectangle(0, table.getHeight(), 20, 20);
                table.scrollRectToVisible(rect);
                table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                table.grabFocus();
                table.changeSelection(table.getRowCount() - 1, 0, false, true);
            }
        });
    }

    public static void main(String[] args) {
        SimpleTest st = new SimpleTest();
        st.setVisible(true);
        st.setSize(300, 300);
        st.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}