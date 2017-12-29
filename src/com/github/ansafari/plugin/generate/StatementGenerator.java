package com.github.ansafari.plugin.generate;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class StatementGenerator {

    private Set<String> patterns;

    public StatementGenerator(@NotNull String... patterns) {
        this.patterns = Sets.newHashSet(patterns);
    }

    @Override
    public String toString() {
        return this.getDisplayText();
    }


    @NotNull
    public abstract String getId();

    @NotNull
    public abstract String getDisplayText();

    public Set<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    private static final Function<DomElement, String> FUN = new Function<DomElement, String>() {
        @Override
        public String apply(DomElement mapper) {
            VirtualFile vf = mapper.getXmlTag().getContainingFile().getVirtualFile();
            if (null == vf) return "";
            return vf.getCanonicalPath();
        }
    };


}
