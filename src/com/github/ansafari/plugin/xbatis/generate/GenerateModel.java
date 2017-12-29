package com.github.ansafari.plugin.xbatis.generate;

import java.util.Collection;

/**
 * GenerateModel.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2017/12/28 20:37
 */
public abstract class GenerateModel {

    public boolean matchesAny(String[] patterns, String target) {
        for (String pattern : patterns) {
            if (apply(pattern, target)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesAny(Collection<String> patterns, String target) {
        return matchesAny(patterns.toArray(new String[patterns.size()]), target);
    }

    protected abstract boolean apply(String pattern, String target);

    public abstract int getIdentifier();

}
