package com.github.ansafari.plugin.codeGen.storage;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.ansafari.plugin.codeGen.model.ColumnModel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: duxing
 * Date: 2015.8.11
 */

@State(
        name = "ColumnModelConfigStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
public class ColumnModelConfigStorage implements PersistentStateComponent<List<ColumnModel>> {
    public List<ColumnModel> columnModelList = new ArrayList<>();


    @Nullable
    @Override
    public List<ColumnModel> getState() {
        return columnModelList;
    }

    @Override
    public void loadState(List<ColumnModel> state) {
        XmlSerializerUtil.copyBean(state, columnModelList);
    }


    @Nullable
    public static List<ColumnModel> getColumnModelConfig() {
        if (Env.project == null) return null;
        try {
            return ServiceManager.getService(Env.project, ColumnModelConfigStorage.class).getState();
        } catch (Exception e) {
            return null;
        }
    }

}
