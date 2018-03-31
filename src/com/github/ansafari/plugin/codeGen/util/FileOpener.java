package com.github.ansafari.plugin.codeGen.util;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.github.ansafari.plugin.codeGen.storage.Env;

import java.io.File;

/**
 * User: duxing
 * Date: 2015.08.17 23:25
 */
public class FileOpener {
    public static void openFile(File f) {
        try {
            VirtualFileManager.getInstance().syncRefresh();
            VirtualFile vf = LocalFileSystem.getInstance().findFileByIoFile(f);
            if (vf != null) {
                FileEditorManager.getInstance(Env.project).openFile(vf, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        openFile(new File("/Users/xjt2016"));
    }
}
