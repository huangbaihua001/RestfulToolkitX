package jiux.net.plugin.restful.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jiux.net.plugin.restful.codegen.config.DataSourceConfig;
import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import jiux.net.plugin.restful.codegen.db.DataBaseQuery;
import jiux.net.plugin.restful.codegen.db.meta.TableInfo;
import org.junit.Test;

import java.util.List;

public class DataBaseQueryTest {
    ObjectMapper om = new ObjectMapper();

    @Test
    public void testQueryTables() throws JsonProcessingException {
        GlobalConfig globalConfig = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/world");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        DataBaseQuery dataBaseQuery = new DataBaseQuery(globalConfig, dataSourceConfig);
        List<TableInfo> tableInfoList = dataBaseQuery.queryTables();
        System.out.println(om.writeValueAsString(tableInfoList));
    }

}
