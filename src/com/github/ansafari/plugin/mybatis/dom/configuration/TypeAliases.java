package com.github.ansafari.plugin.mybatis.dom.configuration;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TypeAliases.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/5 07:51
 */
public interface TypeAliases extends DomElement {

    @NotNull
    @SubTagList("typeAlias")
    List<TypeAlias> getTypeAlias();

    @NotNull
    @SubTagList("package")
    List<Package> getPackages();

}
