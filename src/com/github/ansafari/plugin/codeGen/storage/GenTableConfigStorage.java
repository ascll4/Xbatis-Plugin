package com.github.ansafari.plugin.codeGen.storage;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.ansafari.plugin.codeGen.model.gen.GenTable;
import org.jetbrains.annotations.Nullable;

/**
 * GenTableConfigStorage.
 *
 * @author xiongjinteng@raycloud.com
 */
@State(
        name = "GenTableConfigStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
//file = "$PROJECT_CONFIG_DIR$/azureSettings.xml"
public class GenTableConfigStorage implements PersistentStateComponent<GenTable> {
    private GenTable genTable = new GenTable();

    @Nullable
    @Override
    public GenTable getState() {
        //ServiceManager.getService(Env.project, CodeGenModelStorage.class).getState();
        return genTable;
    }

    @Override
    public void loadState(GenTable state) {
        XmlSerializerUtil.copyBean(state, genTable);
    }
}
