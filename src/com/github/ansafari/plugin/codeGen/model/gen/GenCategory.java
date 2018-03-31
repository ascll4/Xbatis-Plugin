package com.github.ansafari.plugin.codeGen.model.gen;

import java.util.List;


/**
 * 生成方案Entity
 * @author ThinkGem
 * @version 2013-10-15
 */
public class GenCategory extends Dict {
	
	private static final long serialVersionUID = 1L;
	private List<String> template;			// 主表模板
	private List<String> childTableTemplate;// 子表模板
	
	public static String CATEGORY_REF = "category-ref:";

	public GenCategory() {
		super();
	}

	public List<String> getTemplate() {
		return template;
	}

	public void setTemplate(List<String> template) {
		this.template = template;
	}
	
	public List<String> getChildTableTemplate() {
		return childTableTemplate;
	}

	public void setChildTableTemplate(List<String> childTableTemplate) {
		this.childTableTemplate = childTableTemplate;
	}
	
}


