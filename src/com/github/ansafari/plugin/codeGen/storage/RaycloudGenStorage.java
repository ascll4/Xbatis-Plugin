package com.github.ansafari.plugin.codeGen.storage;


import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.raycloud.util.daogen.Settings;
import org.jetbrains.annotations.Nullable;

@State(
        name = "RaycloudGenStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
public class RaycloudGenStorage implements PersistentStateComponent<Settings> {
    private Settings settings = new Settings();

    @Nullable
    @Override
    public Settings getState() {
        return settings;
    }

    @Override
    public void loadState(Settings state) {
        XmlSerializerUtil.copyBean(state, settings);
    }

    @Nullable
    public static Settings getSettings() {
        if (Env.project == null) return null;
        try {
            return ServiceManager.getService(Env.project, RaycloudGenStorage.class).getState();
        } catch (Exception e) {
            return null;
        }
    }
}
