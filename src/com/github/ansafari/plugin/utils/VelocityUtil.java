package com.github.ansafari.plugin.utils;

import com.github.ansafari.plugin.codeGen.util.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.jetbrains.java.generate.velocity.VelocityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
            if (StringUtils.isNotBlank(templateName)) {
                if (!templateName.startsWith("\\/")) {
                    templateName = VelocityUtil.class.getResource("/").getPath() + templateName;
                }
            }
            Reader reader = new FileReader(new File(templateName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfileName));
            VELOCITY_ENGINE.evaluate(context, writer, "", reader);
            writer.flush();
            writer.close();
            logger.info("文件生成成功：" + outfileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}