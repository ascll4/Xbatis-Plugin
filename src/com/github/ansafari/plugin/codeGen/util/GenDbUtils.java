package com.github.ansafari.plugin.codeGen.util;

import com.intellij.openapi.diagnostic.Logger;
import com.github.ansafari.plugin.codeGen.model.ColumnModel;
import com.github.ansafari.plugin.codeGen.model.DataSource;
import com.github.ansafari.plugin.codeGen.model.TableModel;
import com.github.ansafari.plugin.codeGen.model.YesNo;
import com.github.ansafari.plugin.codeGen.model.gen.GenTable;
import com.github.ansafari.plugin.codeGen.model.gen.GenTableColumn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class GenDbUtils {
    /**
     * 数据库连接 MySql,SqlServer,Oracle driver-驱动名,url-连接地址及数据库,user-用户名,pwd-密码
     */
    private static final Logger logger = Logger.getInstance(GenDbUtils.class);

    public static Connection getConntion(DataSource ds) {
        try {
            Class.forName(ds.getDriver()).newInstance();
            Properties prop = new Properties();
            prop.put("user", ds.getUser());
            prop.put("password", ds.getPassword());
            if ("ORACLE".equals(ds.getDataBaseType())) {
                prop.put("remarksReporting", "true");
            }
            Connection conn = DriverManager.getConnection(ds.getUrl(), prop);
            return conn;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取所选数据库的表集合
     * @param ds
     * @return
     */
    @Deprecated
    public static List<TableModel> getTableList(DataSource ds) {
        List<TableModel> list = new ArrayList<TableModel>();
        String sql = "";
        if ("ORACLE".equals(ds.getDataBaseType())) {
            sql = "SELECT table_name AS name,comments AS comments FROM user_tab_comments";
        } else if ("MYSQL".equals(ds.getDataBaseType())) {
            //sql = "SHOW TABLE STATUS FROM `" + ds.getDbName() + "`";
            sql = "SELECT t.table_name AS name,t.TABLE_COMMENT AS comments FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = ('" + ds.getDbName() + "') ORDER BY t.TABLE_NAME";
        }

        Connection conn = null;
        try {
            conn = getConntion(ds);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                TableModel table = new TableModel();
                table.setName(rs.getString("NAME").toLowerCase());
                if ("ORACLE".equals(ds.getDataBaseType())) {
                    table.setComment(rs.getString("comments"));
                } else if ("MYSQL".equals(ds.getDataBaseType())) {
                    table.setComment(rs.getString("comments"));
                }
                list.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return list;
    }


    public static List<GenTable> getGenTableList(DataSource ds) {
        List<GenTable> list = new ArrayList<GenTable>();
        String sql = "";
        if ("ORACLE".equals(ds.getDataBaseType())) {
            sql = "SELECT table_name AS name,comments AS comments FROM user_tab_comments";
        } else if ("MYSQL".equals(ds.getDataBaseType())) {
            //sql = "SHOW TABLE STATUS FROM `" + ds.getDbName() + "`";
            sql = "SELECT t.table_name AS name,t.TABLE_COMMENT AS comments FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = ('" + ds.getDbName() + "') ORDER BY t.TABLE_NAME";
        }

        Connection conn = null;
        try {
            conn = getConntion(ds);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GenTable table = new GenTable();
                table.setName(rs.getString("NAME").toLowerCase());
                if ("ORACLE".equals(ds.getDataBaseType())) {
                    table.setComments(rs.getString("comments"));
                } else if ("MYSQL".equals(ds.getDataBaseType())) {
                    table.setComments(rs.getString("comments"));
                }
                list.add(table);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return new ArrayList<>();
    }

    @Deprecated
    public static List<ColumnModel> getColumnList(DataSource ds, String tableName) {
        Connection conn = null;

        List<ColumnModel> list = new ArrayList<ColumnModel>();

        String sql = "show full fields from `" + tableName + "`";

        logger.info("sql: " + sql);

        try {
            conn = getConntion(ds);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ColumnModel column = new ColumnModel();
                column.setColumnName(rs.getString("FIELD").toLowerCase());

                if ("MYSQL".equals(ds.getDataBaseType())) {
                    column.setIsPrimaryKey(rs.getString("KEY"));
                    column.setComment(rs.getString("COMMENT"));
                    column.setJavaType(rs.getString("TYPE"));
                }

                list.add(column);
            }
            rs.close();

            for (ColumnModel column : list) {
                if ("MYSQL".equals(ds.getDataBaseType())) {
                    column.init();
                } else if ("ORACLE".equals(ds.getDataBaseType())) {
                    column.initOra();//针对ORACLE进行初始化
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public static List<GenTableColumn> getGenTableColumnList(DataSource ds, String tableName) {
        Connection conn = null;

        List<GenTableColumn> list = new ArrayList<>();

        String sql = "show full fields from `" + tableName + "`";

        logger.info("sql: " + sql);

        try {
            conn = getConntion(ds);
            assert conn != null;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GenTableColumn column = new GenTableColumn();
                column.setName(rs.getString("FIELD").toLowerCase());
                if ("MYSQL".equals(ds.getDataBaseType())) {
                    column.setIsPk(rs.getString("KEY"));
                    column.setComments(rs.getString("COMMENT"));
                    column.setJdbcType(rs.getString("TYPE"));
                    column.setIsNull(YesNo.YES.getValue().equals(rs.getString("Null")
                            .toLowerCase()) ? YesNo.YES.getKey() : YesNo.NO.getKey());
                }
                list.add(column);
            }
            rs.close();

            GenTable genTable = new GenTable();
            if ("MYSQL".equals(ds.getDataBaseType())) {
                genTable.setColumnList(list);
                GenDbUtils.findTablePK(ds, genTable);
                GenUtils.initColumnField(genTable);
            }
            return genTable.getColumnList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public static void findTablePK(DataSource ds, GenTable genTable) {
        Connection conn = null;
        List<String> pkList = new ArrayList<>();
        String sql = "SELECT lower(au.COLUMN_NAME) AS columnName FROM information_schema.`COLUMNS` au WHERE au.TABLE_SCHEMA =(select database()) AND au.COLUMN_KEY = 'PRI' AND au.TABLE_NAME =('" + genTable.getName() + "')";
        try {
            conn = getConntion(ds);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pkList.add(rs.getString("columnName"));
            }
            rs.close();
            genTable.setPkList(pkList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }

    }

    public static List<ColumnModel> getColumnListBySql(DataSource ds, final String sql) {
        //SQL增加条件过滤，只选择一条记录
        Connection conn = null;

        List<ColumnModel> list = new ArrayList<ColumnModel>();

        String newsql = sql;

        if ("ORACLE".equals(ds.getDataBaseType())) {

        } else if ("MYSQL".equals(ds.getDataBaseType())) {
            newsql += " limit 0,1";
        }

        try {
            conn = getConntion(ds);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            //spring dao 里面有具体的实现，回头翻阅下spring jdbc 代码
            List<ColumnModel> colList = new ArrayList();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                ColumnModel column = new ColumnModel();
                column.setColumnName(rsmd.getColumnLabel(i));
                column.setComment("");
                column.setDigits(rsmd.getScale(i));
                column.setJavaType(rsmd.getColumnTypeName(i));
                column.init();
                list.add(column);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    /***
     * 获取默认数据源，需要移除
     * @return
     */
    @Deprecated
    public static DataSource getDefaultDataSource() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/melon_primary";
        String user = "root";
        String password = "123456";
        DataSource ds = new DataSource();
        ds.setUrl(url);
        ds.setDriver(driver);
        ds.setDbName("melon_primary");
        ds.setUser(user);
        ds.setPassword(password);
        ds.setName("datasource_name");
        return ds;
    }


    public static void main(String[] args) {
        DataSource ds = getDefaultDataSource();

    }
}
