package jiux.net.plugin.restful.codegen.db.jdbc;

import jiux.net.plugin.restful.codegen.db.meta.JdbcType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class DatabaseMetaDataWrapper {
    private final DatabaseMetaData databaseMetaData;

    public DatabaseMetaDataWrapper(Connection connection) throws SQLException {
        this.databaseMetaData = connection.getMetaData();
    }


    public Map<String, ColumnsInfo> getColumnsInfo(String catalog, String schemaPattern, String tableNamePattern)
        throws SQLException {
        ResultSet resultSet = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, "%");
        Map<String, ColumnsInfo> columnsInfoMap = new HashMap<>();
        while (resultSet.next()) {
            ColumnsInfo columnsInfo = new ColumnsInfo();
            String name = resultSet.getString("COLUMN_NAME");
            columnsInfo.name = name;
            columnsInfo.jdbcType = JdbcType.forCode(resultSet.getInt("DATA_TYPE"));
            columnsInfo.length = resultSet.getInt("COLUMN_SIZE");
            columnsInfo.scale = resultSet.getInt("DECIMAL_DIGITS");
            columnsInfo.remarks = resultSet.getString("REMARKS");
            columnsInfo.defaultValue = resultSet.getString("COLUMN_DEF");
            columnsInfo.nullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
            columnsInfoMap.put(name.toLowerCase(), columnsInfo);
        }
        return Collections.unmodifiableMap(columnsInfoMap);
    }

    public static class ColumnsInfo {
        private String name;

        private int length;

        private boolean nullable;

        private String remarks;

        private String defaultValue;

        private int scale;

        private JdbcType jdbcType;

        public String getName() {
            return name;
        }

        public int getLength() {
            return length;
        }

        public boolean isNullable() {
            return nullable;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public int getScale() {
            return scale;
        }

        public JdbcType getJdbcType() {
            return jdbcType;
        }
    }

}
