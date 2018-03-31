package com.github.ansafari.plugin.codeGen.storage;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.ansafari.plugin.codeGen.model.DataSource;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/16
 * Time: 上午1:21
 */
@State(
        name = "DataSourceStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
public class DataSourceStorage implements PersistentStateComponent<DataSource> {
    private DataSource dataSource = new DataSource();

    @Nullable
    @Override
    public DataSource getState() {
        return dataSource;
    }

    @Override
    public void loadState(DataSource state) {
        XmlSerializerUtil.copyBean(state, dataSource);
    }

    @Nullable
    public static DataSource getDataSourceStorage() {
        if (Env.project == null) return null;
        try {
            return ServiceManager.getService(Env.project, DataSourceStorage.class).getState();
        } catch (Exception e) {
            return null;
        }
    }
}
