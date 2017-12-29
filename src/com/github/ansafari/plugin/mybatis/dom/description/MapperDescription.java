package com.github.ansafari.plugin.mybatis.dom.description;

import com.github.ansafari.plugin.mybatis.domain.mapper.Mapper;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MapperDescription extends DomFileDescription<Mapper> {

    public MapperDescription() {
        super(Mapper.class, "mapper");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        XmlTag rootTag = file.getRootTag();
        return rootTag != null && rootTag.getName().equals(getRootTagName());
    }

}
