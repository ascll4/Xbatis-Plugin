
package com.github.ansafari.plugin.codeGen.model.gen;

import java.io.Serializable;
import java.util.List;


/**
 * 生成方案Entity
 *
 * @author ThinkGem
 * @version 2013-10-15
 */
public class GenConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<GenCategory> categoryList;    // 代码模板分类
    private List<Dict> javaTypeList;        // Java类型
    private List<Dict> queryTypeList;        // 查询类型
    private List<Dict> showTypeList;        // 显示类型

    public GenConfig() {
        super();
    }

    public List<GenCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<GenCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Dict> getJavaTypeList() {
        return javaTypeList;
    }

    public void setJavaTypeList(List<Dict> javaTypeList) {
        this.javaTypeList = javaTypeList;
    }

    public List<Dict> getQueryTypeList() {
        return queryTypeList;
    }

    public void setQueryTypeList(List<Dict> queryTypeList) {
        this.queryTypeList = queryTypeList;
    }

    public List<Dict> getShowTypeList() {
        return showTypeList;
    }

    public void setShowTypeList(List<Dict> showTypeList) {
        this.showTypeList = showTypeList;
    }

}