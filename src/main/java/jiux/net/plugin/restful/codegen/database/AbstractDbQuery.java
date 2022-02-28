package jiux.net.plugin.restful.codegen.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDbQuery implements DbQuery {

    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }


    @Override
    public String[] customFields() {
        return new String[0];
    }
}
