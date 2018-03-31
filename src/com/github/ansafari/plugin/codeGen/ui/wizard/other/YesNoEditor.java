package com.github.ansafari.plugin.codeGen.ui.wizard.other;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class YesNoEditor extends JComboBox implements TableCellEditor {

    private static final long serialVersionUID = 5860619160549087886L;
    //EventListenerList:保存EventListener 列表的类。
    private EventListenerList listenerList = new EventListenerList();
    //ChangeEvent用于通知感兴趣的参与者事件源中的状态已发生更改。
    private ChangeEvent changeEvent = new ChangeEvent(this);

    public YesNoEditor() {
        super();
        addItem("是");
        addItem("否");
        //请求终止编辑操作可以包含单元格的JTable收到，也可以从编辑器组件本身（如这里的JComboBox）获得
                   /*addActionListener(newActionListener(){
                            publicvoid actionPerformed(ActionEvent e) {
                                     System.out.println("ActionListener");
                                     //如同stopCellEditing，都是调用fireEditingStopped()方法
                                     fireEditingStopped();
                            }
                           
                   });*/
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    private void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                //之所以是i+1，是因为一个为CellEditorListener.class（Class对象），
                //接着的是一个CellEditorListener的实例
                listener = (CellEditorListener) listeners[i + 1];
                //让changeEvent去通知编辑器已经结束编辑
                //在editingStopped方法中，JTable调用getCellEditorValue()取回单元格的值，
                //并且把这个值传递给TableValues(TableModel)的setValueAt()
                listener.editingStopped(changeEvent);
            }
        }
    }

    public void cancelCellEditing() {
    }

    /**
     * 编辑其中一个单元格，再点击另一个单元格时，调用。-------------！！！！！
     */
    public boolean stopCellEditing() {
        //可以注释掉下面的fireEditingStopped();，然后在GenderEditor的构造函数中把
        //addActionListener()的注释去掉（这时请求终止编辑操作从JComboBox获得），
        System.out.println("编辑其中一个单元格，再点击另一个单元格时，调用。");
        fireEditingStopped();//请求终止编辑操作从JTable获得
        return true;
    }

    /**
     * 为一个单元格初始化编辑时，getTableCellEditorComponent被调用
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        boolean isTrue = "1".equals(value);
        setSelectedIndex(isTrue ? 0 : 1);
        return this;
    }

    /**
     * 询问编辑器它是否可以使用 anEvent 开始进行编辑。
     */
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    /**
     * 如果应该选择正编辑的单元格，则返回true，否则返回 false。
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
     * 返回值传递给TableValue（TableModel）中的setValueAt()方法
     */
    public Object getCellEditorValue() {
        return getSelectedIndex() == 0;
    }
} 