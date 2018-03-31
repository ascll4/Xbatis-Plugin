/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.github.ansafari.plugin.codeGen.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanye
 * @version v 1.0
 * @date Dec 14, 2008
 * @description 得到当前应用的系统路径
 */
public class SystemPath {
    public static String getSystemPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static String getClassPath(Class<?> clazz) {
        return clazz.getResource("").getPath();
    }

    public static String getSeparator() {
        return System.getProperty("file.separator");
    }

    public static void main(String[] args) throws IOException, TemplateException {
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(getSeparator());
        System.out.println(getSystemPath());
        System.out.println(getClassPath(SystemPath.class));

        Configuration configuration = FreeMakerUtils.getFreeMarkerCfg(SystemPath.getSystemPath() + File.separator + "templates");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("username", "xjt2016");
        StringWriter result = new StringWriter();
        template.process(data, result);
        System.out.println(result.toString());
    }
}
