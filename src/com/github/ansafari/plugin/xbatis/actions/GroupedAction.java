//package com.github.ansafari.plugin.xbatis.actions;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.CommonDataKeys;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.vfs.VirtualFile;
//import org.apache.commons.lang.time.DateFormatUtils;
//
//import java.util.Date;
//
///**
// * @author Anna Bulenkova
// */
//public class GroupedAction extends AnAction {
//    private static final Logger LOG = Logger.getInstance(GroupedAction.class);
//
//    @Override
//    public void update(AnActionEvent event) {
//        final Project project = event.getData(CommonDataKeys.PROJECT);
//        Editor editor = event.getData(CommonDataKeys.EDITOR);
//        event.getPresentation().setVisible(project != null);
//        event.getPresentation().setEnabled(editor != null);
//        //event.getPresentation().setIcon(AllIcons.Actions.);
//    }
//
//    @Override
//    public void actionPerformed(AnActionEvent event) {
//
//        //e.getData(LangDataKeys.PSI_FILE)
//        VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
//
//        LOG.info("actionPerformed" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:SS"));
//    }
//}
