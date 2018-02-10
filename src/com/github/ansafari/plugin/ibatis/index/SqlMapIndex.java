package com.github.ansafari.plugin.ibatis.index;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.uiDesigner.compiler.Utils;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public class SqlMapIndex extends ScalarIndexExtension {
    @NonNls
    public static final ID<String, Void> NAME = ID.create("SqlMapIndex");

    private final MyDataIndexer myDataIndexer = new MyDataIndexer();

    @NotNull
    @Override
    public ID getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer getIndexer() {
        return myDataIndexer;
    }

    @NotNull
    @Override
    public KeyDescriptor getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return new DefaultFileTypeSpecificInputFilter(StdFileTypes.XML);
    }

    @Override
    public boolean dependsOnFileContent() {
        return false;
    }

    private static class MyDataIndexer implements DataIndexer<String, Void, FileContent> {
        @Override
        @NotNull
        public Map<String, Void> map(@NotNull final FileContent inputData) {
            String className = null;
            try {
                className = Utils.getBoundClassName(inputData.getContentAsText().toString());
            } catch (Exception e) {
                // ignore
            }
            if (className != null) {
                return Collections.singletonMap(className, null);
            }
            return Collections.emptyMap();
        }
    }
}
