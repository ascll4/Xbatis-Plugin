package com.github.ansafari.plugin.codeGen.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.github.ansafari.plugin.codeGen.model.CodeGenModel;
import com.github.ansafari.plugin.codeGen.model.ColumnModel;
import com.github.ansafari.plugin.codeGen.model.DataSource;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FreeMakerUtils {

    public static Configuration getFreeMarkerCfg(String ftlPath) {
        Configuration freemarkerCfg = new Configuration();
        freemarkerCfg.setBooleanFormat("true,false");
        freemarkerCfg.setNumberFormat("#");
        freemarkerCfg.setDefaultEncoding("UTF-8");
        try {
            freemarkerCfg.setDirectoryForTemplateLoading(new File(ftlPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freemarkerCfg;
    }

    public static Configuration getFreeMarkerCfg(Class cls, String templatePath) {
        Configuration freemarkerCfg = new Configuration();
        freemarkerCfg.setBooleanFormat("true,false");
        freemarkerCfg.setDefaultEncoding("UTF-8");
        freemarkerCfg.setClassForTemplateLoading(cls, templatePath);
        return freemarkerCfg;
    }

    public static boolean generateFile(Configuration cfg, String templateFileName, Map propMap, String relPath, String fileName, String rootDir) {
        try {
            Template t = cfg.getTemplate(templateFileName);

            createDirs(rootDir, relPath);

            File dir = new File(rootDir + File.separator + relPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(rootDir + File.separator + relPath + File.separator + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            t.process(propMap, out);
            out.close();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean createDirs(String aParentDir, String aSubDir) {
        File aFile = new File(aParentDir);
        if (aFile.exists()) {
            File aSubFile = new File(aParentDir + File.separator + aSubDir);
            if (!aSubFile.exists()) {
                return aSubFile.mkdirs();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String renderString(String templateString, Map<String, ?> model) {
        try {
            StringWriter result = new StringWriter();
            Template t = new Template("name", new StringReader(templateString), new Configuration());
            t.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }


    public static void main(String[] args) {
        DataSource ds = GenDbUtils.getDefaultDataSource();
        List<ColumnModel> columnModelList = GenDbUtils.getColumnList(ds, "sys_user");
        CodeGenModel tableconfig = new CodeGenModel();
        tableconfig.setTopPackage("xjt");
        tableconfig.setTableName("sys_user");
        tableconfig.setFilePath("i");

        Map data = new HashMap();
        data.put("columnList", columnModelList);
        data.put("table", tableconfig);

        Configuration freemakerCfg = FreeMakerUtils.getFreeMarkerCfg("templates");
        FreeMakerUtils.generateFile(freemakerCfg, "entity.ftl", data
                , "src/" + StringUtils.replace(tableconfig.getTopPackage(), "."
                        , "/") + "/entity/"
                , StringUtils.capitalize(tableconfig.getTableJavaName()) + ".java", tableconfig.getFilePath());
    }
}
