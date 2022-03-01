package jiux.net.plugin.restful.codegen.config;



import jiux.net.plugin.restful.codegen.db.convertor.ColumnTypeConvertor;
import jiux.net.plugin.restful.codegen.db.convertor.ColumnTypeConvertorFactory;
import jiux.net.plugin.restful.codegen.db.convertor.MySqlColumnTypeConvertor;
import jiux.net.plugin.restful.codegen.db.meta.DbType;
import jiux.net.plugin.restful.codegen.db.query.DbQuery;
import jiux.net.plugin.restful.codegen.db.query.DbQueryRegistry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库配置
 */
public class DataSourceConfig {
    protected final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    private String url;

    private String username;

    private String password;

    private String schemaName;

    private DataSource dataSource;

    private Connection connection;

    private DbQuery dbQuery;

    @NotNull
    public Connection getConn() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                synchronized (this) {
                    if (dataSource != null) {
                        connection = dataSource.getConnection();
                    } else {
                        this.connection = DriverManager.getConnection(url, username, password);
                    }
                }
            }
            String schema = StringUtils.isNotBlank(schemaName) ? schemaName : getDefaultSchema();
            if (StringUtils.isNotBlank(schema)) {
                schemaName = schema;
                try {
                    connection.setSchema(schemaName);
                } catch (Throwable t) {
                    LOGGER.error(
                        "There may be exceptions in the driver and version of the database, " + t.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    protected String getDefaultSchema() {
        DbType dbType = getDbType();
        String schema = null;
        if (DbType.POSTGRES_SQL == dbType) {
            //pg 默认 schema=public
            schema = "public";
        } else if (DbType.DB2 == dbType) {
            //db2 默认 schema=current schema
            schema = "current schema";
        } else if (DbType.ORACLE == dbType) {
            //oracle 默认 schema=username
            schema = this.username.toUpperCase();
        }
        return schema;
    }

    @NotNull
    public DbType getDbType() {
        return this.getDbType(this.url.toLowerCase());
    }

    public DbQuery getDbQuery() {
        if (null == dbQuery) {
            DbType dbType = getDbType();
            DbQueryRegistry dbQueryRegistry = new DbQueryRegistry();
            // 默认 MYSQL
            dbQuery = Optional.ofNullable(dbQueryRegistry.getDbQuery(dbType))
                .orElseGet(() -> dbQueryRegistry.getDbQuery(DbType.MYSQL));
        }
        return dbQuery;
    }


    @NotNull
    private DbType getDbType(@NotNull String str) {
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DbType.POSTGRES_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DbType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DbType.H2;
        } else if (str.contains(":sybase:")) {
            return DbType.SYBASE;
        } else {
            return DbType.OTHER;
        }
    }


    private ColumnTypeConvertor typeConvert;

    public ColumnTypeConvertor getTypeConvert() {
        if (null == typeConvert) {
            DbType dbType = getDbType();
            // 默认 MYSQL
            typeConvert = ColumnTypeConvertorFactory.getTypeConvert(dbType);
            if (null == typeConvert) {
                typeConvert = MySqlColumnTypeConvertor.INSTANCE;
            }
        }
        return typeConvert;
    }


    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
