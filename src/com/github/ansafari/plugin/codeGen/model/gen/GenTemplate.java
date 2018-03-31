package com.github.ansafari.plugin.codeGen.model.gen;

import com.google.common.collect.Lists;
import com.github.ansafari.plugin.codeGen.util.StringUtils;

import java.util.List;

/**
 * 生成方案Entity
 *
 * @author ThinkGem
 * @version 2013-10-15
 */
public class GenTemplate extends DataEntity<GenTemplate> {

    private static final long serialVersionUID = 1L;
    private String name;    // 名称
    private String category;        // 分类
    private String filePath;        // 生成文件路径
    private String fileName;        // 文件名
    private String content;        // 内容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCategoryList() {
        if (category == null) {
            return Lists.newArrayList();
        } else {
            return Lists.newArrayList(StringUtils.split(category, ","));
        }
    }

    public void setCategoryList(List<String> categoryList) {
        if (categoryList == null) {
            this.category = "";
        } else {
            this.category = "," + StringUtils.join(categoryList, ",") + ",";
        }
    }

}


