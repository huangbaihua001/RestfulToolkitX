package jiux.net.plugin.restful.codegen.entity;

import lombok.Data;

import java.util.List;

/**
 * 列配置分组
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/18 09:33
 */
@Data
public class ColumnConfigGroup implements AbstractGroup<ColumnConfigGroup, ColumnConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<ColumnConfig> elementList;
}
