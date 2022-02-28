package jiux.net.plugin.restful.codegen.db.convertor;

import jiux.net.plugin.restful.codegen.db.convertor.selector.BranchBuilder;
import jiux.net.plugin.restful.codegen.db.convertor.selector.Selector;
import jiux.net.plugin.restful.codegen.db.meta.ColumnType;
import jiux.net.plugin.restful.codegen.db.meta.DbType;

public final class ColumnTypeConvertorFactory {

    public static ColumnTypeConvertor getTypeConvert(DbType dbType) {
        switch (dbType) {
            case MYSQL:
            case MARIADB:
                return MySqlColumnTypeConvertor.INSTANCE;
            default:
                return null;
        }
    }


    static Selector<String, ColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }


    static BranchBuilder<String, ColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    static BranchBuilder<String, ColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) {
                    return true;
                }
            }
            return false;
        });
    }
}
