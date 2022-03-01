package jiux.net.plugin.restful.codegen.db.query;

import jiux.net.plugin.restful.codegen.db.meta.DbType;
import java.util.EnumMap;
import java.util.Map;

public class DbQueryRegistry {

    private final Map<DbType, DbQuery> db_query_enum_map = new EnumMap<>(DbType.class);

    public DbQueryRegistry() {
        db_query_enum_map.put(DbType.MYSQL, new MySqlQuery());
    }

    public DbQuery getDbQuery(DbType dbType) {
        return db_query_enum_map.get(dbType);
    }
}
