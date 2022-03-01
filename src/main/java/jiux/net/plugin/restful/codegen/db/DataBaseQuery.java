package jiux.net.plugin.restful.codegen.db;


import jiux.net.plugin.restful.codegen.config.DataSourceConfig;
import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import jiux.net.plugin.restful.codegen.db.jdbc.DatabaseMetaDataWrapper;
import jiux.net.plugin.restful.codegen.db.meta.ColumnType;
import jiux.net.plugin.restful.codegen.db.meta.DbType;
import jiux.net.plugin.restful.codegen.db.meta.TableField;
import jiux.net.plugin.restful.codegen.db.meta.TableInfo;
import jiux.net.plugin.restful.codegen.db.query.DbQueryDecorator;
import com.intellij.openapi.diagnostic.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class DataBaseQuery {

    private final static Logger LOGGER = Logger.getInstance(DataBaseQuery.class);

    protected final DataSourceConfig dataSourceConfig;

    private final DbQueryDecorator dbQuery;

    private final GlobalConfig globalConfig;

    public DataBaseQuery(@NotNull GlobalConfig globalConfig, @NotNull DataSourceConfig dataSourceConfig) {
        this.globalConfig = globalConfig;
        this.dataSourceConfig = dataSourceConfig;
        this.dbQuery = new DbQueryDecorator(dataSourceConfig);
    }

    @NotNull
    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public List<TableInfo> queryTables() {
        //所有的表信息
        List<TableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<TableInfo> includeTableList = new ArrayList<>();
        List<TableInfo> excludeTableList = new ArrayList<>();

        try {
            boolean isSkipView = true;
            dbQuery.execute(dbQuery.tablesSql(), result -> {
                String tableName = result.getStringResult(dbQuery.tableName());
                if (StringUtils.isNotBlank(tableName)) {
                    TableInfo tableInfo = new TableInfo(this.globalConfig, tableName);
                    String tableComment = result.getTableComment();
                    // 跳过视图
                    if (!(isSkipView && "VIEW".equals(tableComment))) {
                        tableInfo.setComment(tableComment);
                        //
                        tableList.add(tableInfo);
                    }
                    //TODO 排除逻辑
                }
            });

            // 性能优化，只处理需执行表字段 https://github.com/baomidou/mybatis-plus/issues/219
            tableList.forEach(this::convertTableFields);
            return tableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 数据库操作完成,释放连接对象
            dbQuery.closeConnection();
        }
    }


    private void convertTableFields(@NotNull TableInfo tableInfo) {
        DbType dbType = this.dataSourceConfig.getDbType();
        String tableName = tableInfo.getName();
        try {
            final Map<String, DatabaseMetaDataWrapper.ColumnsInfo> columnsMetaInfoMap = new HashMap<>();
            //TODO 增加元数据信息获取,后面查询表字段要改成这个.
            Map<String, DatabaseMetaDataWrapper.ColumnsInfo> columnsInfo = new DatabaseMetaDataWrapper(
                dbQuery.getConnection()).getColumnsInfo(null, dataSourceConfig.getSchemaName(), tableName);
            if (columnsInfo != null && !columnsInfo.isEmpty()) {
                columnsMetaInfoMap.putAll(columnsInfo);
            }
            String tableFieldsSql = dbQuery.tableFieldsSql(tableName);
            Set<String> h2PkColumns = new HashSet<>();

            dbQuery.execute(tableFieldsSql, result -> {
                String columnName = result.getStringResult(dbQuery.fieldName());
                TableField field = new TableField(columnName);
                // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                boolean isId = DbType.H2 == dbType ? h2PkColumns.contains(columnName) : result.isPrimaryKey();

                //TODO 这里执行表处理
                field.setColumnName(columnName)
                    .setType(result.getStringResult(dbQuery.fieldType()))
                    .setComment(result.getFiledComment())
                    .setCustomMap(dbQuery.getCustomFields(result.getResultSet()));

                ColumnType columnType = dataSourceConfig.getTypeConvert().handleTypeConvert(globalConfig, field);
                field.setMetaInfo(new TableField.MetaInfo(columnsMetaInfoMap.get(columnName.toLowerCase())));
                tableInfo.addField(field);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableInfo.processTable();
    }


}
