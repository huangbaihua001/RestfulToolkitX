package jiux.net.plugin.restful.codegen.db.query;


import jiux.net.plugin.restful.codegen.config.DataSourceConfig;
import jiux.net.plugin.restful.codegen.db.meta.DbType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbQueryDecorator extends AbstractDbQuery {

    private final static Logger LOGGER = LoggerFactory.getLogger(DbQueryDecorator.class);

    private final Connection connection;
    private final DbQuery dbQuery;
    private final DbType dbType;
    private final String schema;

    public DbQueryDecorator(@NotNull DataSourceConfig dataSourceConfig) {
        this.dbQuery = dataSourceConfig.getDbQuery();
        this.dbType = dataSourceConfig.getDbType();
        this.schema = dataSourceConfig.getSchemaName();
        this.connection = dataSourceConfig.getConn();
    }

    public void execute(String sql, Consumer<ResultSetWrapper> consumer) throws SQLException {
        LOGGER.debug("执行SQL:{}", sql);
        int count = 0;
        long start = System.nanoTime();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                consumer.accept(new ResultSetWrapper(resultSet, this, this.dbType));
                count++;
            }
            long end = System.nanoTime();
            LOGGER.debug("返回记录数:{},耗时(ms):{}", count, (end - start) / 1000000);
        }
    }


    @Override
    public String tablesSql() {
        String tablesSql = dbQuery.tablesSql();
        if (DbType.POSTGRES_SQL == dbType || DbType.DB2 == dbType
            || DbType.ORACLE == dbType) {
            tablesSql = String.format(tablesSql, this.schema);
        }

        return tablesSql;
    }

    public String tableFieldsSql(String tableName) {
        String tableFieldsSql = this.tableFieldsSql();
        if (DbType.DB2 == dbType) {
            tableFieldsSql = String.format(tableFieldsSql, this.schema, tableName);
        } else if (DbType.ORACLE == dbType) {
            tableFieldsSql = String.format(tableFieldsSql.replace("#schema", this.schema), tableName,
                tableName.toUpperCase());
        } else if (DbType.POSTGRES_SQL == dbType) {
            tableFieldsSql = String.format(tableFieldsSql, tableName, tableName, tableName);
        } else {
            tableFieldsSql = String.format(tableFieldsSql, tableName);
        }
        return tableFieldsSql;
    }


    public void closeConnection() {
        Optional.ofNullable(connection).ifPresent((con) -> {
            try {
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });
    }

    public Map<String, Object> getCustomFields(ResultSet resultSet) {
        String[] fcs = this.customFields();
        if (null != fcs) {
            Map<String, Object> customMap = new HashMap(fcs.length);
            for (String fc : fcs) {
                try {
                    customMap.put(fc, resultSet.getObject(fc));
                } catch (SQLException sqlException) {
                    throw new RuntimeException("获取自定义字段错误:", sqlException);
                }
            }
            return customMap;
        }
        return Collections.emptyMap();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public String tableFieldsSql() {
        return dbQuery.tableFieldsSql();
    }

    @Override
    public String tableName() {
        return dbQuery.tableName();
    }

    @Override
    public String tableComment() {
        return dbQuery.tableComment();
    }

    @Override
    public String fieldName() {
        return dbQuery.fieldName();
    }

    @Override
    public String fieldType() {
        return dbQuery.fieldType();
    }

    @Override
    public String fieldComment() {
        return dbQuery.fieldComment();
    }

    @Override
    public String fieldKey() {
        return dbQuery.fieldKey();
    }


    @Override
    public String[] customFields() {
        return dbQuery.customFields();
    }

    public static class ResultSetWrapper {

        private final DbQuery dbQuery;

        private final ResultSet resultSet;

        private final DbType dbType;

        ResultSetWrapper(ResultSet resultSet, DbQuery dbQuery, DbType dbType) {
            this.resultSet = resultSet;
            this.dbQuery = dbQuery;
            this.dbType = dbType;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public String getStringResult(String columnLabel) {
            try {
                return resultSet.getString(columnLabel);
            } catch (SQLException sqlException) {
                throw new RuntimeException(String.format("读取[%s]字段出错!", columnLabel), sqlException);
            }
        }

        public String getFiledComment() {
            return getComment(dbQuery.fieldComment());

        }

        private String getComment(String columnLabel) {
            return StringUtils.isNotBlank(columnLabel) ? formatComment(getStringResult(columnLabel)) : "";
        }

        public String getTableComment() {
            return getComment(dbQuery.tableComment());
        }

        public String formatComment(String comment) {
            return StringUtils.isBlank(comment) ? "" : comment.replaceAll("\r\n", "\t");
        }

        public boolean isPrimaryKey() {
            String key = this.getStringResult(dbQuery.fieldKey());
            if (DbType.DB2 == dbType || DbType.SQLITE == dbType) {
                return StringUtils.isNotBlank(key) && "1".equals(key);
            } else {
                return StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key);
            }
        }
    }
}
