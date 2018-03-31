package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class TableSort {
    final static Icon upIcon = new UpDownArrow(0);
    final static Icon downIcon = new UpDownArrow(1);

    private boolean ascending = false;
    private TableModel model;

    private int sortColumn = -1;

    public static void main(String[] args) {
        new TableSort().init();
    }

    private void init() {
        List<Object[]> list = new Vector<Object[]>();
        Object[] obj = null;
        for (int i = 0; i < 50; i++) {
            obj = new Object[3];
            String s = "";
            if (i % 2 == 0) {
                s = "aw";
            } else {
                s = "3e";
            }
            obj[0] = "name" + s;
            obj[1] = i + (123 / (i + 1)) + 2;
            obj[2] = i + 17.0169 / (i + 1) * 1.1347;
            list.add(obj);
        }
        String[] names = {"姓名", "学号", "成绩"};
        model = new TableModel(names, 0);
        model.addData(list);
        final JTable table = new JTable(model);
// TableRowSorter<TableModel> sort = new TableRowSorter<TableModel>(model);   
// CompareTo to = new CompareTo();   
// for(int i=0;i<names.length;i++)   
// {   
// sort.setComparator(i,to);   
// }   
//SortManager sortManager = new SortManager(table);   
// table.setRowSorter(sort);   
        DefaultTableCellRenderer defaultHeaderRenderer = new SortHeaderRenderer();
        defaultHeaderRenderer.setHorizontalAlignment(0);
        defaultHeaderRenderer.setHorizontalTextPosition(JLabel.LEFT);
        JTableHeader jtableheader = table.getTableHeader();
        jtableheader.setDefaultRenderer(defaultHeaderRenderer);
        addListener(table);
        JScrollPane js = new JScrollPane(table);
        JFrame frame = new JFrame();
        frame.getContentPane().add(js);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void addListener(final JTable table) {
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseevent) {
                int i = table.columnAtPoint(mouseevent.getPoint());
                int j = table.convertColumnIndexToModel(i);
                //转换出用户想排序的列和底层数据的列，然后判断   
                if (j == sortColumn) {
                    ascending = !ascending;
                } else {
                    ascending = true;
                    sortColumn = j;
                }
                model.sort(ascending, sortColumn);
                table.revalidate();
                table.repaint();

            }

        });
    }

    //jdk1.5表头渲染器
    class SortHeaderRenderer extends DefaultTableCellRenderer {

        public SortHeaderRenderer() {

        }

        public Component getTableCellRendererComponent(JTable jtable,
                                                       Object obj, boolean flag, boolean flag1, int i, int j) {
            if (jtable != null) {
                JTableHeader jtableheader = jtable.getTableHeader();
                if (jtableheader != null) {
                    setForeground(jtableheader.getForeground());
                    setBackground(jtableheader.getBackground());
                    setFont(jtableheader.getFont());
                }
            }
            setText(obj != null ? obj.toString() : "");
            int col = jtable.convertColumnIndexToModel(j);
            if (col == sortColumn) {

                setIcon(ascending ? TableSort.upIcon : TableSort.downIcon);
            } else {
                setIcon(null);
            }
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }
    }

}

//表格模型   
class TableModel extends DefaultTableModel {
    List<Object[]> dataLst = new ArrayList<Object[]>();
    private Compare compare = new Compare();

    public TableModel(String[] tableHeaders, int count) {
        super(tableHeaders, count);
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (dataLst.size() > 0) {
            Object[] obj = dataLst.get(row);
            return obj[col];
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getRowCount() {
        if (null != dataLst) {
            return dataLst.size();
        }
        return 0;
    }

    public void sort(boolean desc, int col) {
        compare.setDes(desc);
        compare.setCol(col);
        java.util.Collections.sort(this.dataLst, compare);
    }

    public void addData(List<Object[]> objLst) {
        dataLst.addAll(objLst);
        this.fireTableDataChanged();
    }
}

//jdk1.5排序比较器   
class Compare implements Comparator {
    private boolean des;

    private int col;

    public Compare() {
        this(true, 0);
    }

    public Compare(boolean des, int col) {
        this.des = des;
        this.col = col;
    }

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        if (!(o1 instanceof Object[]) && !(o2 instanceof Object[])) {
            return -1;
        } else {
            Object[] l1 = (Object[]) o1;
            Object[] l2 = (Object[]) o2;
            Object oo1 = l1[col];
            Object oo2 = l2[col];
            if (oo1.getClass() == String.class && oo2.getClass() == String.class) {
                String obj1 = (String) oo1;
                String obj2 = (String) oo2;
                result = obj1.compareTo(obj2);
            } else if (oo1.getClass() == Double.class && oo2.getClass() == Double.class) {
                Double obj1 = (Double) oo1;
                Double obj2 = (Double) oo2;
                double t = obj1 - obj2;
                if (t > 0.00000) {
                    result = 1;
                } else if (t < 0.00000) {
                    result = -1;
                }
            } else {
                Integer obj1 = (Integer) oo1;
                Integer obj2 = (Integer) oo2;
                result = obj1 - obj2;
            }
        }
        if (!des) {
            result = -result;
        }
        return result;
    }

    public boolean isDes() {
        return des;
    }

    public void setDes(boolean des) {
        this.des = des;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}

//jdk1.6排序比较器   
class CompareTo implements Comparator {
    public CompareTo() {
    }

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        if (o1.getClass() != o2.getClass()) {
            return -1;
        } else {
            if (o1.getClass() == String.class && o2.getClass() == String.class) {
                String obj1 = (String) o1;
                String obj2 = (String) o2;
                result = obj1.compareTo(obj2);
            } else if (o1.getClass() == Double.class && o2.getClass() == Double.class) {
                Double obj1 = (Double) o1;
                Double obj2 = (Double) o2;
                double t = obj1 - obj2;
                if (t > 0.00000) {
                    result = 1;
                } else if (t < 0.00000) {
                    result = -1;
                }
            } else {
                Integer obj1 = (Integer) o1;
                Integer obj2 = (Integer) o2;
                result = obj1 - obj2;
            }
        }
        return result;
    }

}

//绘制排序图标   
class UpDownArrow implements Icon {
    private int size = 12;

    public static final int UP = 0;
    public static final int DOWN = 1;
    private int direction;

    public UpDownArrow(int i) {
        direction = i;
    }

    public int getIconHeight() {
        return size;
    }

    public int getIconWidth() {
        return size;
    }

    public void paintIcon(Component component, Graphics g, int i, int j) {
        int x1 = i + size / 2;
        int x = i + 1;
        int z = (i + size) - 2;
        int y = j + 1;
        int y1 = (j + size) - 2;
        Color color = (Color) UIManager.get("controlDkShadow");
        //画三角形，方向向上   
        if (direction == 0) {
            g.setColor(Color.white);
            g.drawLine(x, y1, z, y1);
            g.drawLine(z, y1, x1, y);
            g.setColor(color);
            g.drawLine(x, y1, x1, y);
        } else {
            g.setColor(color);
            g.drawLine(x, y, z, y);
            g.drawLine(x, y, x1, y1);
            g.setColor(Color.white);
            g.drawLine(z, y, x1, y1);
        }
        //g.dispose();   
    }
}

