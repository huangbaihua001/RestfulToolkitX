package jiux.net.plugin.restful.codegen.db.meta;

import jiux.net.plugin.restful.codegen.db.jdbc.DatabaseMetaDataWrapper;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class TableField {
    private boolean convert;
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String name;
    private String type;
    private String propertyName;
    private ColumnType columnType;
    private String comment;
    private String fill;
    /**
     * 是否关键字
     *
     * @since 3.3.2
     */
    private boolean keyWords;
    /**
     * 数据库字段（关键字含转义符号）
     *
     * @since 3.3.2
     */
    private String columnName;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    private MetaInfo metaInfo;


    public TableField( @NotNull String name) {
        this.name = name;
        this.columnName = name;
    }

    public boolean isConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return keyIdentityFlag;
    }

    public void setKeyIdentityFlag(boolean keyIdentityFlag) {
        this.keyIdentityFlag = keyIdentityFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public TableField setType(String type) {
        this.type = type;
        return this;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public TableField setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public boolean isKeyWords() {
        return keyWords;
    }

    public void setKeyWords(boolean keyWords) {
        this.keyWords = keyWords;
    }

    public String getColumnName() {
        return columnName;
    }

    public TableField setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public Map<String, Object> getCustomMap() {
        return customMap;
    }

    public void setCustomMap(Map<String, Object> customMap) {
        this.customMap = customMap;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public static class MetaInfo {
        private int length;

        private boolean nullable;

        private String remarks;

        private String defaultValue;

        private int scale;

        private JdbcType jdbcType;

        public MetaInfo(DatabaseMetaDataWrapper.ColumnsInfo columnsInfo) {
            if (columnsInfo != null) {
                this.length = columnsInfo.getLength();
                this.nullable = columnsInfo.isNullable();
                this.remarks = columnsInfo.getRemarks();
                this.defaultValue = columnsInfo.getDefaultValue();
                this.scale = columnsInfo.getScale();
                this.jdbcType = columnsInfo.getJdbcType();
            }
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

        @Override
        public String toString() {
            return "MetaInfo{" +
                "length=" + length +
                ", nullable=" + nullable +
                ", remarks='" + remarks + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", scale=" + scale +
                ", jdbcType=" + jdbcType +
                '}';
        }
    }

    @Override
    public String toString() {
        return "TableField{" +
            "convert=" + convert +
            ", keyFlag=" + keyFlag +
            ", keyIdentityFlag=" + keyIdentityFlag +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", propertyName='" + propertyName + '\'' +
            ", columnType=" + columnType +
            ", comment='" + comment + '\'' +
            ", fill='" + fill + '\'' +
            ", keyWords=" + keyWords +
            ", columnName='" + columnName + '\'' +
            ", customMap=" + customMap +
            ", metaInfo=" + metaInfo +
            '}';
    }
}
