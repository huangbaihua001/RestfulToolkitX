package jiux.net.plugin.restful.codegen.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlQuery extends AbstractDbQuery {

    @Override
    public String tablesSql() {
        return "SHOW TABLE STATUS WHERE 1=1 ";
    }


    @Override
    public String tableFieldsSql() {
        return "SHOW FULL FIELDS FROM `%s`";
    }


    @Override
    public String tableName() {
        return "NAME";
    }


    @Override
    public String tableComment() {
        return "COMMENT";
    }


    @Override
    public String fieldName() {
        return "FIELD";
    }


    @Override
    public String fieldType() {
        return "TYPE";
    }


    @Override
    public String fieldComment() {
        return "COMMENT";
    }


    @Override
    public String fieldKey() {
        return "KEY";
    }


    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return "auto_increment".equals(results.getString("Extra"));
    }
}
