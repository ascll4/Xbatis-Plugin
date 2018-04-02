package com.github.ansafari.plugin.utils;

import com.intellij.openapi.util.io.FileUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.jetbrains.java.generate.velocity.VelocityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;

public class VelocityUtil {

    public static final Logger logger = LoggerFactory.getLogger(VelocityUtil.class);

    private final static VelocityEngine VELOCITY_ENGINE = VelocityFactory.getVelocityEngine();

    static {
        /*
          IDEA 的 URLClassLoader 无法获取当前插件的 path
          @see org.apache.velocity.util.ClassUtils
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(VelocityUtil.class.getClassLoader());
        VELOCITY_ENGINE.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public static String evaluate(Context context, String inputStr) {
        StringWriter writer = new StringWriter();
        VELOCITY_ENGINE.evaluate(context, writer, "", inputStr);
        return writer.toString();
    }

    public static void evaluateWriter(Context context, String inputStr, BufferedWriter writer) {
        VELOCITY_ENGINE.evaluate(context, writer, "", inputStr);
    }

    public static void mergeTemplate(String templateName, String outfileName, VelocityContext context) {
        try {
            InputStream inputStream = VelocityUtil.class.getResourceAsStream(templateName);
            if (inputStream != null) {
                String templateContent = FileUtil.loadTextAndClose(inputStream);
                BufferedWriter writer = new BufferedWriter(new FileWriter(outfileName));
                VELOCITY_ENGINE.evaluate(context, writer, "", templateContent);
                writer.flush();
                writer.close();
                logger.info("文件生成成功：" + outfileName);
            } else {
                logger.error("模板文件不存在：" + templateName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}