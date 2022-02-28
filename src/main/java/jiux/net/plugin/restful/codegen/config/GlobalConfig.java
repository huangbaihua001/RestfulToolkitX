package jiux.net.plugin.restful.codegen.config;

import jiux.net.plugin.restful.codegen.db.meta.DateType;

public class GlobalConfig {

    private DateType dateType = DateType.TIME_PACK;


    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }
}
