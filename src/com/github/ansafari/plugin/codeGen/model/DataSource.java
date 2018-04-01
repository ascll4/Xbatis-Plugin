package com.github.ansafari.plugin.codeGen.model;

public class DataSource extends AbstractModelObject {
    private String name;
    private String driver;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        firePropertyChange("name", oldValue, name);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        String oldValue = this.driver;
        this.driver = driver;
        firePropertyChange("driver", oldValue, driver);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        String oldValue = this.host;
        this.host = host;
        firePropertyChange("host", oldValue, host);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        String oldValue = this.database;
        this.database = database;
        firePropertyChange("host", oldValue, database);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        String oldValue = this.user;
        this.user = user;
        firePropertyChange("user", oldValue, user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldValue = this.password;
        this.password = password;
        firePropertyChange("password", oldValue, password);
    }

    /**
     * 获取数据库类型
     *
     * @return String
     */
    public String getDataBaseType() {
        if ("oracle.jdbc.driver.OracleDriver".equals(this.driver)) {
            return "ORACLE";
        } else if ("com.mysql.jdbc.Driver".equals(this.driver)) {
            return "MYSQL";
        } else {
            return "";
        }
    }
}
