package jiux.net.plugin.restful.codegen.db.meta;

import jiux.net.plugin.restful.codegen.config.GlobalConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

public class TableInfo {

    /**
     * 全局配置信息
     */
    private final GlobalConfig globalConfig;

    private final String name;

    public TableInfo(GlobalConfig globalConfig, String name) {
        this.globalConfig = globalConfig;
        this.name = name;
    }

    /**
     * 包导入信息
     */
    private final Set<String> importPackages = new TreeSet<>();

    /**
     * 是否转换
     */
    private boolean convert;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * mapper名称
     */
    private String mapperName;

    /**
     * xml名称
     */
    private String xmlName;

    /**
     * service名称
     */
    private String serviceName;

    /**
     * serviceImpl名称
     */
    private String serviceImplName;

    /**
     * controller名称
     */
    private String controllerName;

    /**
     * 表字段
     */
    private final List<TableField> fields = new ArrayList<>();

    /**
     * 是否有主键
     */
    private boolean havePrimaryKey;

    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();

    /**
     * 字段名称集
     */
    private String fieldNames;


    public void processTable() {
        //TODO 这里处理表
    }



    public void addField(@NotNull TableField field) {
       //TODO 这里转换 Entity 设置

        this.fields.add(field);
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public boolean isConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public List<TableField> getFields() {
        return fields;
    }

    public boolean isHavePrimaryKey() {
        return havePrimaryKey;
    }

    public void setHavePrimaryKey(boolean havePrimaryKey) {
        this.havePrimaryKey = havePrimaryKey;
    }

    public List<TableField> getCommonFields() {
        return commonFields;
    }

    public String getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
            "name='" + name + '\'' +
            ", comment='" + comment + '\'' +
            ", fields=" + fields +
            ", fieldNames='" + fieldNames + '\'' +
            '}';
    }
}
