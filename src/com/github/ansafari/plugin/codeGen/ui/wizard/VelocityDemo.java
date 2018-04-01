package com.github.ansafari.plugin.codeGen.ui.wizard;

import com.github.ansafari.plugin.utils.VelocityUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class VelocityDemo {

    public static void main(String[] args) throws IOException {
        VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();

        // 设置变量
        VelocityContext ctx = new VelocityContext();
        ctx.put("username", "xjt");

        StringWriter stringWriter = new StringWriter();
        String out = VelocityUtil.evaluate(ctx, "<html>\n" +
                "<body>\n" +
                "<pre>\n" +
                " Hello Lily Velocity\n" +
                "\n" +
                "##这里是注解\n" +
                "#*\n" +
                "* 这里还是注解\n" +
                "* *#\n" +
                "\n" +
                "    username: $!{username}\n" +
                "\n" +
                "  </pre>\n" +
                "</body>\n" +
                "</html>");


        String outfileName = "/Users/xjt2016/Documents/Xbatis-Plugin/resources/news.vm";
        BufferedWriter writer = new BufferedWriter(new FileWriter(outfileName));
        VelocityUtil.evaluateWriter(ctx, "<html>\n" +
                "<body>\n" +
                "<pre>\n" +
                " Hello Lily Velocity\n" +
                "\n" +
                "##这里是注解\n" +
                "#*\n" +
                "* 这里还是注解\n" +
                "* *#\n" +
                "\n" +
                "    username: $!{username}\n" +
                "\n" +
                "  </pre>\n" +
                "</body>\n" +
                "</html>", writer);
        writer.flush();
        writer.close();
    }
}
