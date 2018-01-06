package com.github.ansafari.plugin.mybatis.dom.description;

import com.github.ansafari.plugin.mybatis.dom.configuration.Configuration;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ConfigurationDescription.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/5 07:52
 */
public class ConfigurationDescription extends DomFileDescription<Configuration> {

    public ConfigurationDescription() {
        super(Configuration.class, "configuration");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        XmlTag rootTag = file.getRootTag();
        return null != rootTag && rootTag.getName().equals("configuration");
    }

}
