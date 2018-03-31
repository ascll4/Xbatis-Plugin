package com.github.ansafari.plugin.codeGen.ui.wizard.crud;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.intellij.util.ui.JBUI;
import com.github.ansafari.plugin.codeGen.model.DataSource;
import com.github.ansafari.plugin.codeGen.model.gen.GenTable;
import com.github.ansafari.plugin.codeGen.model.gen.GenTableColumn;
import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.storage.GenTableConfigStorage;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.github.ansafari.plugin.codeGen.util.GenDbUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.Enumeration;

/**
 * 选中表的字段信息.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/16
 * Time: 上午8:12
 */
public class FieldConfigurableWizardStep extends WizardStep<StartWizardModel> {

    private static final Logger logger = Logger.getInstance(FieldConfigurableWizardStep.class);
    private JPanel container;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private JBTable table;

    private GenTable genTable;

    private void initTable() {
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    //行单选
        table.setRowHeight(JBUI.scale(18));
        FitTableColumns(table);
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                String newValue = table.getValueAt(e.getLastRow(), e.getColumn()).toString();
                logger.info("new value: " + newValue);
            }
        });
    }

    private void initTableData() {
        genTable = ServiceManager.getService(Env.project, GenTableConfigStorage.class).getState();

        Object[][] cellData = {};
        String[] headers = {"列名", "说明", "物理类型", "Java类型", "Java属性名称", "主键", "可空", "插入", "编辑", "列表", "查询", "查询匹配方式", "显示表单类型", "字典类型", "排序"};
        DefaultTableModel dtm = new DefaultTableModel(cellData, headers) {
            private static final long serialVersionUID = 7734388618330057094L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && super.isCellEditable(row, column);//第一列字段不可修改
            }
        };
        assert genTable != null;
        DataSource ds = GenDbUtils.getDefaultDataSource();
        genTable.setColumnList(GenDbUtils.getGenTableColumnList(ds, genTable.getName()));

        // }
        if (genTable.getColumnList() != null) {
            for (GenTableColumn column : genTable.getColumnList()) {
                Object[] rowData = {
                        column.getName(),
                        column.getComments(),
                        column.getJdbcType(),
                        column.getJavaType(),
                        column.getJavaFieldName(),
                        column.getIsPk(),
                        column.getIsNull(),
                        column.getIsInsert(),
                        column.getIsEdit(),
                        column.getIsList(),
                        column.getIsQuery(),
                        column.getQueryType(),
                        column.getShowType(),
                        column.getDictType(),
                        column.getSort()
                };
                dtm.addRow(rowData);
            }
        }
        table.setModel(dtm);

        logger.info("FieldConfigurableWizardStep prepare...成功!");
    }

    public FieldConfigurableWizardStep(String title, String explanation) {
        super(title, explanation);
        initTable();
        initTableData();
    }


    @Override
    public JComponent prepare(WizardNavigationState state) {
        return container;
    }

    @Override
    public WizardStep onNext(StartWizardModel model) {
        GenTableConfigStorage storage = ServiceManager.getService(Env.project, GenTableConfigStorage.class);
        storage.loadState(genTable);
        logger.info("FieldConfigurableWizardStep save columnModelList success !");

        return super.onNext(model);
    }

    @Override
    public String getHelpId() {
        return "code.gen.wizard.step.two";
    }

    public void FitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier()
                            , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }
}
