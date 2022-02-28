package jiux.net.plugin.restful.codegen.db.convertor;

import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import jiux.net.plugin.restful.codegen.db.meta.ColumnType;
import jiux.net.plugin.restful.codegen.db.meta.TableField;
import org.jetbrains.annotations.NotNull;

public interface ColumnTypeConvertor {


    default ColumnType handleTypeConvert(@NotNull GlobalConfig globalConfig, @NotNull TableField tableField) {
        return handleTypeConvert(globalConfig, tableField.getType());
    }

    ColumnType handleTypeConvert(@NotNull GlobalConfig globalConfig, @NotNull String fieldType);
}
