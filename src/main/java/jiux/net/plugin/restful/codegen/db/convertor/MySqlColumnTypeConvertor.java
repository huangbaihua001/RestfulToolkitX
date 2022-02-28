package jiux.net.plugin.restful.codegen.db.convertor;

import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import jiux.net.plugin.restful.codegen.db.meta.ColumnType;
import jiux.net.plugin.restful.codegen.db.meta.DbColumnType;
import org.jetbrains.annotations.NotNull;

import static jiux.net.plugin.restful.codegen.db.convertor.ColumnTypeConvertorFactory.contains;
import static jiux.net.plugin.restful.codegen.db.convertor.ColumnTypeConvertorFactory.containsAny;
import static jiux.net.plugin.restful.codegen.db.meta.DbColumnType.*;

public class MySqlColumnTypeConvertor implements ColumnTypeConvertor {

    public static final MySqlColumnTypeConvertor INSTANCE = new MySqlColumnTypeConvertor();

    @Override
    public ColumnType handleTypeConvert(@NotNull GlobalConfig config, @NotNull String fieldType) {
        return ColumnTypeConvertorFactory.use(fieldType)
                .test(containsAny("char", "text", "json", "enum").then(STRING))
                .test(contains("bigint").then(LONG))
                .test(containsAny("tinyint(1)", "bit(1)").then(BOOLEAN))
                .test(contains("bit").then(BYTE))
                .test(contains("int").then(INTEGER))
                .test(contains("decimal").then(BIG_DECIMAL))
                .test(contains("clob").then(CLOB))
                .test(contains("blob").then(BLOB))
                .test(contains("binary").then(BYTE_ARRAY))
                .test(contains("float").then(FLOAT))
                .test(contains("double").then(DOUBLE))
                .test(containsAny("date", "time", "year").then(t -> toDateType(config, t)))
                .or(STRING);
    }


    public static ColumnType toDateType(GlobalConfig config, String type) {
        String dateType = type.replaceAll("\\(\\d+\\)", "");
        switch (config.getDateType()) {
            case ONLY_DATE:
                return DbColumnType.DATE;
            case SQL_PACK:
                switch (dateType) {
                    case "date":
                    case "year":
                        return DbColumnType.DATE_SQL;
                    case "time":
                        return DbColumnType.TIME;
                    default:
                        return DbColumnType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (dateType) {
                    case "date":
                        return DbColumnType.LOCAL_DATE;
                    case "time":
                        return DbColumnType.LOCAL_TIME;
                    case "year":
                        return DbColumnType.YEAR;
                    default:
                        return DbColumnType.LOCAL_DATE_TIME;
                }
            default:
                return STRING;
        }
    }
}
