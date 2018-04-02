package com.github.ansafari.plugin.utils;

import com.alibaba.fastjson.JSON;
import com.raycloud.util.daogen.*;
import com.raycloud.util.daogen.util.FileUtil;
import com.raycloud.util.daogen.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

import java.util.*;
import java.util.Map.Entry;

/**
 * 代码生成入口
 */
public class GenUtils {
    /**
     * 日志记录
     */
    private final static Logger logger = Logger.getLogger(GenUtils.class);
    // Source目录，从ClassPath中获取
    private final static String SOURCE_IN_PATH = GenUtils.class.getResource("").getPath().replace("com.github.ansafari.plugin.utils", "");

    // 生成的Maven结构的代码路径
    private final static String PATH_JAVA = "/src/main/java/";
    private final static String PATH_RESOURCES = "/src/main/resources/";

    private GenTable genTable;
    private GlobalBean globalBean;
    private Settings settings;
    public static Map<String, TableConfig> tconfig = new HashMap<String, TableConfig>();

    public GenUtils(DbConn dbConn) {
        genTable = new GenTable(dbConn, tconfig);
        globalBean = new GlobalBean();
        settings = dbConn.getSettings();
    }

    /**
     * 对于dao的生成，暂时特殊定制，硬编码。key 为数据库表名，暂不全量配置
     */
    static {

//        tconfig.put("tb_order", TableConfig.build("tb_order"));
    }

    /**
     * 生成入口
     */
    public static void main(String[] args) {
        // 生成DAO代码
        //language=JSON
        String settingsJson = "{\n" +
                "  \"dbName\": \"kdzs_crm\",\n" +
                "  \"dbPwd\": \"123456\",\n" +
                "  \"dbType\": 1,\n" +
                "  \"dbUser\": \"root\",\n" +
                "  \"driver\": null,\n" +
                "  \"genPath\": \"/Users/xjt2016/Documents/Xbatis-Plugin/gendir/\",\n" +
                "  \"javaPackage\": \"com.kuaidizs.print\",\n" +
                "  \"moduleName\": \"purchase\",\n" +
                "  \"port\": \"3306\",\n" +
                "  \"tables\": [ \"crm_user\"],\n" +
                "  \"tmplPath\": \"dao/ibatisdao\",\n" +
                "  \"url\": \"127.0.0.1\"\n" +
                "}";
        doGenDB(JSON.parseObject(settingsJson, Settings.class));
    }

    public static void doGenDB(Settings settings) {
        logger.info("开始执行DAO代码生成===========================");
        tconfig = new HashMap<>();
        for (String tableName : settings.getTables()) {
            tconfig.put(tableName, TableConfig.build(tableName));
        }
        // 初始化系统环境
//        Settings settings = new Settings();
//        if (!settings.initSystemParam()) {
//            logger.error("系统初始化失败！");
//            return;
//        }
        // 初始化数据库连接
        DbConn dbConn = new DbConn(settings);
        if (!dbConn.isInit()) {
            logger.error("数据库连接创建失败！");
            return;
        }
        GenUtils genUtils = new GenUtils(dbConn);
        // 设置工程的全局变量
        genUtils.globalBean.setNowDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));// 设置系统生成时间
        genUtils.globalBean.setUserName("Jing Longjun");// 设置系统当前用户
        genUtils.globalBean.setPackageName(settings.getJavaPackage());// 设置Java_Package路径
        genUtils.globalBean.setModuleName(settings.getModuleName());
        // 生成指定数据库的指定表或所有表数据访问层代码
        String tabName;
        List<String> tableList = genUtils.genTable.getTableName();
        // 创建系统目录结构
        //FileUtil.copyDirectiory(SOURCE_IN_PATH + settings.getTmplPath(), settings.getGenPath() + settings.getTmplPath());
        //FileUtil.delExtFile(settings.getGenPath() + settings.getTmplPath(), ".vm"); // 删除拷贝过去的VM文件
        // 循环生成所有表数据访问代码，返回类对象并设置类对象列表
        logger.info("共有" + tableList.size() + "个表需要生成数据访问层.");
        List<TableBean> tbList = new ArrayList<TableBean>();
        TableBean tb;
        for (int i = 0; i < tableList.size(); i++) {
            tabName = tableList.get(i);
            logger.info("第" + (i + 1) + "个表[" + tabName + "]数据访问层正在生成中...");
            tb = genUtils.doGenTable(tabName);
            if (tb != null)
                tbList.add(tb);
        }
        logger.info("实际生成" + tbList.size() + "个表的数据访问层！");
        // 根据类对象列表，生成全局配置及基类代码文件
        genUtils.doGenCFG(tbList);

        dbConn.closeConn(); // 关闭数据库链接

        logger.info("所映射的字段有：");
        if (!genUtils.genTable.as.isEmpty()) {
            for (Entry<Integer, String> entry : genUtils.genTable.as.entrySet()) {
                logger.info("sqltype:" + entry.getKey() + " -> " + entry.getValue());
            }
        }
        logger.info("结束执行DAO代码生成===========================");

    }

    /**
     * 生成指定表的数据访问层
     *
     * @param tablename tablename
     * @return 表类名
     */
    private TableBean doGenTable(String tablename) {
        TableConfig conf = GenUtils.tconfig.get(tablename);
        TableBean tableBean = genTable.prepareTableBean(tablename, conf);
//        tableBean.setConf(conf == null ? TableConfig.DEFAULT : conf);
        if (conf.isSplitTable()) {
            // 添加分表的参数
            tableBean.setTableName(tableBean.getTableName().concat("_$tableId$"));
        }

        VelocityContext ctx = new VelocityContext();
        ctx.put("tbb", tableBean); // 设置表对象
        ctx.put("gb", globalBean); // 设置全局信息
        ctx.put("sysInit", settings); // 设置系统信息
        ctx.put("stringUtil", new StringUtil()); // 设置StringUtil
        try {
            // 生成Java代码
            String javaVmDir = SOURCE_IN_PATH + settings.getTmplPath() + PATH_JAVA;
            String javaDir = settings.getGenPath() + PATH_JAVA;

            FileUtil.mkDirs(javaDir);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/DO.java.vm", javaDir  + "/" + tableBean.getClassName() + ".java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/DAO.java.vm", javaDir  + "/" + tableBean.getClassName() + "DAO.java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/Service.java.vm", javaDir  + "/" + tableBean.getClassName() + "Service.java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/Controller.java.vm", javaDir  + "/" + tableBean.getClassName() + "Controller.java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/Result.java.vm", javaDir  + "/" + "Result.java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/BaseQuery.java.vm", javaDir  + "/" +  "BaseQuery.java", ctx);
            VelocityUtil.mergeTemplate("/dao/ibatisdao/src/main/java/Query.java.vm", javaDir  + "/" + tableBean.getClassName() + "Query.java", ctx);

            // 生成SqlMap配置文件
            String sqlmapVm = "/dao/ibatisdao/src/main/resources/sqlmap/-sqlmap.xml.vm";
            String sqlmapDir = settings.getGenPath() + PATH_RESOURCES + "sqlmap/";
            FileUtil.mkDirs(sqlmapDir);
            VelocityUtil.mergeTemplate(sqlmapVm, sqlmapDir + tableBean.getPureTableName() + "-sqlmap.xml", ctx);
        } catch (Exception e) {
            logger.error("表[" + tablename + "]生成出错，异常是:", e);
        }
        return tableBean;
    }

    /**
     * 生成所有表的IbatisDAO配置及基类代码文件
     */
    private void doGenCFG(List<TableBean> tbList) {
        VelocityContext ctxCfg = new VelocityContext();
        ctxCfg.put("tbbList", tbList);
        ctxCfg.put("gb", globalBean); // 设置全局信息
        ctxCfg.put("sysInit", settings); // 设置系统信息
        ctxCfg.put("stringUtil", new StringUtil()); // 设置StringUtil

        try {
            // 生成Java基类代码
            String javaVmDir = SOURCE_IN_PATH + settings.getTmplPath() + PATH_JAVA;
            String javaDir = settings.getGenPath() + PATH_JAVA;
            List<String> javaVmList = FileUtil.getFileListWithExt(javaVmDir, ".vm");
            String createFilename, packageDir = "", packageStr;
            for (String vmFilename : javaVmList) {
                if (!vmFilename.startsWith("Base")) {
                    continue; // 非基类代码跳过
                }
                createFilename = FileUtil.getFilenameWithoutExt(vmFilename);
                packageStr = FileUtil.findLine(javaVmDir + "/" + vmFilename, "package");
                if (StringUtils.isNotBlank(packageStr)) {
                    packageStr = packageStr
                            .substring(packageStr.indexOf("$!{gb.packageName}"), packageStr.indexOf(";"));
                    packageDir = packageStr.replace("$!{gb.packageName}", globalBean.getPackageName())
                            .replace(".", "/");
                }
                FileUtil.mkDirs(javaDir + packageDir);
                VelocityUtil.mergeTemplate(settings.getTmplPath() + PATH_JAVA + "/" + vmFilename, javaDir
                        + packageDir + "/" + createFilename, ctxCfg);
            }
        } catch (Exception e) {
            logger.error("生成所有表的IbatisDAO配置及基类代码文件，异常是:", e);
        }
    }
}
