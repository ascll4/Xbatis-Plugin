package com.github.ansafari.plugin.codeGen.ui.wizard.crud;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.github.ansafari.plugin.codeGen.model.DataSource;
import com.github.ansafari.plugin.codeGen.storage.DataSourceStorage;
import com.github.ansafari.plugin.codeGen.storage.Env;
import com.github.ansafari.plugin.codeGen.ui.wizard.other.StartWizardModel;
import com.github.ansafari.plugin.codeGen.util.GenDbUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接配置.
 *
 * @author xiongjinteng@raycloud.com
 */
public class DataSourceConfigurableWizardStep extends WizardStep<StartWizardModel> {
    private static final Logger logger = Logger.getInstance(OutputConfigurableWizardStep.class);
    private JPanel container;
    private JTextField name_txt;
    private JLabel name_label;
    private JTextField driver_txt;
    private JLabel driver_label;
    private JTextField user_txt;
    private JLabel user_label;
    private JLabel password_label;
    private JTextField password_txt;
    private JLabel host_label;
    private JTextField host_text;
    private JButton btn_test;
    private JPanel jPanel;
    private JLabel conn_result_label;
    private JTextField port_text;
    private JLabel jLabelPort;
    private JTextField database_text;
    private JLabel database_label;


    private DataSource dataSource = new DataSource();

    public DataSourceConfigurableWizardStep(String title, String explanation) {
        super(title, explanation);

        DataSourceStorage dataSourceStorage = ServiceManager.getService(Env.project, DataSourceStorage.class);
        DataSource dataSource = dataSourceStorage.getState();
        if (dataSource != null) {
            this.setData(dataSource);
        }

        btn_test.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //super.mouseReleased(e);
                conn_result_label.setText("");
                checkConn(dataSource);
            }
        });
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        return container;
    }

    @Override
    public WizardStep onNext(StartWizardModel model) {
        if (checkConn(dataSource)) {
            DataSourceStorage dataSourceStorage = ServiceManager.getService(Env.project, DataSourceStorage.class);
            dataSourceStorage.loadState(dataSource);
            logger.info("DataSourceConfigurableWizardStep dataSourceStorage.loadState" + dataSource.toString());
            return super.onNext(model);
        }
        return this;
    }

    public void setData(DataSource dataSource) {
        name_txt.setText(dataSource.getName());
        driver_txt.setText(dataSource.getDriver());
        host_text.setText(dataSource.getHost());
        database_text.setText(dataSource.getDatabase());
        user_txt.setText(dataSource.getUser());
        password_txt.setText(dataSource.getPassword());
        port_text.setText(dataSource.getPort());
    }

    public void getData(DataSource dataSource) {
        dataSource.setName(name_txt.getText());
        dataSource.setDriver(driver_txt.getText());
        dataSource.setHost(host_text.getText());
        dataSource.setPort(port_text.getText());
        dataSource.setDatabase(database_text.getText());
        dataSource.setUser(user_txt.getText());
        dataSource.setPassword(password_txt.getText());
    }

    public boolean isModified(DataSource data) {
        if (name_txt.getText() != null ? !name_txt.getText().equals(data.getName()) : data.getName() != null)
            return true;
        if (driver_txt.getText() != null ? !driver_txt.getText().equals(data.getDriver()) : data.getDriver() != null)
            return true;
        if (host_text.getText() != null ? !host_text.getText().equals(data.getHost()) : data.getHost() != null)
            return true;
        if (user_txt.getText() != null ? !user_txt.getText().equals(data.getUser()) : data.getUser() != null)
            return true;
        if (password_txt.getText() != null ? !password_txt.getText().equals(data.getPassword()) : data.getPassword() != null)
            return true;
        if (StringUtils.isNotBlank(port_text.getText()) ? !StringUtils.equals(port_text.getText(), data.getPort()) : data.getPort() != null) {
            return true;
        }
        return false;
    }

    /***
     * check最后的连接数据源配置是否正确，不正确不进行下一步操作
     * @param dataSource dataSource
     * @return boolean
     */
    private boolean checkConn(DataSource dataSource) {
        getData(dataSource);
        Connection connection = GenDbUtils.getConnection(dataSource);
        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("datasource connect success");
                // TODO: 2017/4/17  Color.rgb(83,128, 77)
                conn_result_label.setForeground(new java.awt.Color(83, 128, 77));
                conn_result_label.setText("Successful!");
                return true;
            } else {
                conn_result_label.setForeground(JBColor.RED);
                conn_result_label.setText("Failed!");
                return false;
            }

        } catch (SQLException e1) {
            //e1.printStackTrace();
            logger.error(e1.getMessage());
            conn_result_label.setText(e1.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;

    }

    @Override
    public String getHelpId() {
        return "code.gen.wizard.step.zero";
    }
}
