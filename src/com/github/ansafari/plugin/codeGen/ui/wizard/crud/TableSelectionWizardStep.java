package com.github.ansafari.plugin.codeGen.ui.wizard.crud;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.github.ansafari.plugin.codeGen.model.CodeGenModel;
import com.github.ansafari.plugin.codeGen.model.DataSource;
import com.github.ansafari.plugin.codeGen.model.gen.GenTable;
import com.github.ansafari.plugin.codeGen.storage.CodeGenModelStorage;
import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.storage.GenTableConfigStorage;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.SimpleTableCellRenderer;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.github.ansafari.plugin.codeGen.util.GenDbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 数据库表集合，选择一张表进行操作，初始化数据需要选择storage的表配置.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/15
 * Time: 下午11:12
 */
public class TableSelectionWizardStep extends WizardStep<StartWizardModel> {
    private static final Logger logger = Logger.getInstance(FieldConfigurableWizardStep.class);
    private static final int WIDTH = 400;
    private static final int PREFERRED_SCROLLABLE_VIEWPORT_HEIGHT_IN_ROWS = 7;
    private JPanel container;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private JBTable table;

    private java.util.List<GenTable> genTables;

    // TODO: 2017/4/14 conn List

    /***
     * 初始化
     * @param title title
     * @param explanation explanation
     */
    public TableSelectionWizardStep(String title, String explanation) {
        super(title, explanation);
        initTable();
    }

    private void initTable() {
        DataSource dataSource = GenDbUtils.getDefaultDataSource();

        GenTable genTableSelected = ServiceManager.getService(Env.project, GenTableConfigStorage.class).getState();

        genTables = GenDbUtils.getGenTableList(dataSource);

        //不知道能不能获取到Step实例中的数据，todo 急需验证
        Object[][] cellData = {};
        String[] headers = {"表名", "描述", ""};
        DefaultTableModel dtm = new DefaultTableModel(cellData, headers) {
            private static final long serialVersionUID = -5138875262567667823L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int selectedIndex = 0;
        for (GenTable genTable : genTables) {
            logger.info(genTable.getName() + " " + genTable.getComments());
            Object[] rowData = {genTable.getName(), genTable.getComments(), ""};
            dtm.addRow(rowData);
            if (genTableSelected != null && genTable.getName().equals(genTableSelected.getName())) {
                selectedIndex = genTables.indexOf(genTable);
            }
        }
        //GenTable genTable = ServiceManager.getService(Env.project, GenTableConfigStorage.class).getState();
        //genTable.getName();
        table.setModel(dtm);
        table.setPreferredScrollableViewportSize(new Dimension(WIDTH, table.getRowHeight() * PREFERRED_SCROLLABLE_VIEWPORT_HEIGHT_IN_ROWS));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    //行单选
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;    //如果该值在进行一系列更改，则返回 true。
            if (table.getSelectedRow() >= 0) {
                GenTableConfigStorage genTableConfigStorage = ServiceManager.getService(Env.project, GenTableConfigStorage.class);
                GenTable genTable = genTables.get(table.getSelectedRow());

                CodeGenModelStorage codeGenModelStorage = ServiceManager.getService(Env.project, CodeGenModelStorage.class);
                CodeGenModel codeGenModel = codeGenModelStorage.getState();
                if (genTable != null) {
                    genTableConfigStorage.loadState(genTable);
                    logger.info("TableSelectionWizardStep tableModelConfigStorage.loadState: " + genTable.toString());
                    if (codeGenModel == null) codeGenModel = new CodeGenModel();
                    codeGenModel.setTableName(genTable.getName());
                    codeGenModel.setTableComment(genTable.getComments());
                    codeGenModelStorage.loadState(codeGenModel);
                    logger.info("TableSelectionWizardStep codeGenModelStorage.loadState: " + codeGenModel.toString());
                }
            }
        });
       // table.setRowSelectionInterval(selectedIndex, selectedIndex);    //初始化选中配置文件中保存的表，的行
        logger.info("table初始化数据...成功!");


        /**
         *  rect = new Rectangle(0, table.getHeight(), 20, 20);
                         table.scrollRectToVisible(rect);
                         table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                         table.grabFocus();
                         table.changeSelection(table.getRowCount() - 1, 0, false, true);
         */
        Rectangle rect = new Rectangle(0, table.getHeight(), 20, 20);
        table.scrollRectToVisible(rect);
        table.setRowSelectionInterval(selectedIndex, selectedIndex);
        table.grabFocus();
      // table.changeSelection(table.getRowCount() - 1, 0, false, true);

    }

    private void validateTextFields() {

    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        return container;
    }


    private SimpleTableCellRenderer cellRenderer = new SimpleTableCellRenderer();

    /**
     * 间隔色
     *
     * @param table
     */
    private void setRenderColor(JBTable table) {
        if (table == null) return;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        }

    }


    @Override
    public String getHelpId() {
        return "code.gen.wizard.step.one";
    }
}
