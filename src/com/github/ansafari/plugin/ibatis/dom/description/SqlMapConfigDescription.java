
package com.github.ansafari.plugin.ibatis.dom.description;

import com.github.ansafari.plugin.ibatis.dom.configuration.SqlMapConfig;
import com.intellij.util.xml.DomFileDescription;

/**
 * SqlMapConfigDescription.
 *
 * @author xiongjinteng@raycloud.com
 * @date 2018/1/2 22:52
 */
public class SqlMapConfigDescription extends DomFileDescription<SqlMapConfig> {

    public SqlMapConfigDescription() {
        super(SqlMapConfig.class, "sqlMapConfig");
    }

}