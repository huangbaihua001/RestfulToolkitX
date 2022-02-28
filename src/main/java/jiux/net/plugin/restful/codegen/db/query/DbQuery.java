package jiux.net.plugin.restful.codegen.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface DbQuery {
    /**
     * 表信息查询 SQL
     *
     * @return SQL
     */
    String tablesSql();

    /**
     * 表字段信息查询 SQL
     *
     * @return SQL
     */
    String tableFieldsSql();

    /**
     * 表名称
     *
     * @return 表名
     */
    String tableName();

    /**
     * 表注释
     *
     * @return 表的注释
     */
    String tableComment();

    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String fieldName();

    /**
     * 字段类型
     *
     * @return 字段类型
     */
    String fieldType();

    /**
     * 字段注释
     *
     * @return 字段注释
     */
    String fieldComment();

    /**
     * 主键字段
     *
     * @return 主键字段
     */
    String fieldKey();

    /**
     * 判断主键是否为 Identity
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException ignore
     */
    boolean isKeyIdentity(ResultSet results) throws SQLException;

    /**
     * 自定义字段名称
     *
     * @return 自定义字段数组
     */
    String[] customFields();
}
