package com.github.ansafari.plugin.xbatis.generate;

import com.github.ansafari.plugin.xbatis.setting.XbatisSettingStorage;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class StatementGeneratorUtils {

    public static final StatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

    public static final StatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count", "query");

    public static final StatementGenerator DELETE_GENERATOR = new DeleteGenerator("del", "cancel");

    public static final StatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

    private static final Set<StatementGenerator> ALL = ImmutableSet.of(UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR);

    public static boolean matchesAny(String target) {
        GenerateModel model = XbatisSettingStorage.getInstance().getStatementGenerateModel();
        for (StatementGenerator generator : ALL) {
            if (model.matchesAny(generator.getPatterns(), target)) {
                return true;
            }
        }
        return false;
    }
}
