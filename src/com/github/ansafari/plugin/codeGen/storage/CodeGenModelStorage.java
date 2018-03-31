package com.github.ansafari.plugin.codeGen.storage;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.ansafari.plugin.codeGen.model.CodeGenModel;
import org.jetbrains.annotations.Nullable;

/**
 * CodeGenModelStorage.
 *
 * @author xiongjinteng@raycloud.com
 */
@State(
        name = "CodeGenModelStorage",
        storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/autoCode/config.xml")}
)
public class CodeGenModelStorage implements PersistentStateComponent<CodeGenModel> {
    private CodeGenModel codeGenModel = new CodeGenModel();

    @Nullable
    @Override
    public CodeGenModel getState() {
        return codeGenModel;
    }

    @Override
    public void loadState(CodeGenModel state) {
        XmlSerializerUtil.copyBean(state, codeGenModel);
    }

    @Nullable
    public static CodeGenModel getCodeGenModelStorage() {
        if (Env.project == null) return null;
        try {
            return ServiceManager.getService(Env.project, CodeGenModelStorage.class).getState();
        } catch (Exception e) {
            return null;
        }
    }
}
