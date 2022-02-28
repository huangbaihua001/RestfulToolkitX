package jiux.net.plugin.restful.codegen.db.convertor;

import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import jiux.net.plugin.restful.codegen.db.meta.ColumnType;
import org.jetbrains.annotations.NotNull;

public class MySqlColumnTypeConvertor implements ColumnTypeConvertor {

    public static final MySqlColumnTypeConvertor INSTANCE = new MySqlColumnTypeConvertor();

    @Override
    public ColumnType handleTypeConvert(@NotNull GlobalConfig globalConfig, @NotNull String fieldType) {
        return null;
    }
}
