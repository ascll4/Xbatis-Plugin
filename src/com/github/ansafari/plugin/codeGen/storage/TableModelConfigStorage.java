package com.github.ansafari.plugin.codeGen.storage;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.ansafari.plugin.codeGen.model.TableModel;
import org.jetbrains.annotations.Nullable;

/**
 * TableModelConfigStorage.
 *
 * @author xiongjinteng@raycloud.com
 */
@State(
        name = "TableModelConfigStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
public class TableModelConfigStorage implements PersistentStateComponent<TableModel> {
    private TableModel tableModel = new TableModel();

    @Nullable
    @Override
    public TableModel getState() {
        return tableModel;
    }

    @Override
    public void loadState(TableModel state) {
        XmlSerializerUtil.copyBean(state, tableModel);
    }

    @Nullable
    public static TableModel getTableModelConfig() {
        if (Env.project == null) return null;
        try {
            return ServiceManager.getService(Env.project, TableModelConfigStorage.class).getState();
        } catch (Exception e) {
            return null;
        }
    }
}
