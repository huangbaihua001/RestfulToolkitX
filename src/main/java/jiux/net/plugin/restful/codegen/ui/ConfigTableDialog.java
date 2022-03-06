package jiux.net.plugin.restful.codegen.ui;

import jiux.net.plugin.restful.codegen.dict.GlobalDict;
import jiux.net.plugin.restful.codegen.entity.ColumnConfig;
import jiux.net.plugin.restful.codegen.entity.TableInfo;
import jiux.net.plugin.restful.codegen.factory.CellEditorFactory;
import jiux.net.plugin.restful.codegen.service.TableInfoSettingsService;
import jiux.net.plugin.restful.codegen.tool.CacheDataUtils;
import jiux.net.plugin.restful.codegen.tool.CurrGroupUtils;
import jiux.net.plugin.restful.codegen.tool.ProjectUtils;
import jiux.net.plugin.restful.codegen.tool.StringUtils;
import jiux.net.plugin.restful.codegen.ui.base.ConfigTableModel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * 表配置窗口
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class ConfigTableDialog extends DialogWrapper {
    /**
     * 主面板
     */
    private JPanel mainPanel;
    /**
     * 表信息对象
     */
    private TableInfo tableInfo;

    public ConfigTableDialog() {
        super(ProjectUtils.getCurrProject());
        this.mainPanel = new JPanel(new BorderLayout());
        this.initPanel();
    }

    private void initPanel() {
        init();
        this.tableInfo = TableInfoSettingsService.getInstance().getTableInfo(CacheDataUtils.getInstance().getSelectDbTable());
        setTitle("Config Table " + this.tableInfo.getObj().getName());
        ConfigTableModel model = new ConfigTableModel(this.tableInfo);
        JBTable table = new JBTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 配置列编辑器
        table.getColumn("name").setCellEditor(CellEditorFactory.createTextFieldEditor());
        table.getColumn("type").setCellEditor(CellEditorFactory.createComboBoxEditor(true, GlobalDict.DEFAULT_JAVA_TYPE_LIST));
        table.getColumn("type").setMinWidth(120);
        table.getColumn("comment").setCellEditor(CellEditorFactory.createTextFieldEditor());
        // 其他附加列
        for (ColumnConfig columnConfig : CurrGroupUtils.getCurrColumnConfigGroup().getElementList()) {
            TableColumn column = table.getColumn(columnConfig.getTitle());
            switch (columnConfig.getType()) {
                case TEXT:
                    column.setCellEditor(CellEditorFactory.createTextFieldEditor());
                    break;
                case SELECT:
                    if (StringUtils.isEmpty(columnConfig.getSelectValue())) {
                        column.setCellEditor(CellEditorFactory.createTextFieldEditor());
                    } else {
                        column.setCellEditor(CellEditorFactory.createComboBoxEditor(false, columnConfig.getSelectValue().split(",")));
                    }
                    break;
                case BOOLEAN:
                    column.setCellEditor(CellEditorFactory.createBooleanEditor());
                    break;
                default:
                    break;
            }
        }

        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table);
        this.mainPanel.add(decorator.createPanel(), BorderLayout.CENTER);
        this.mainPanel.setMinimumSize(new Dimension(600, 300));
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.mainPanel;
    }

    @Override
    protected void doOKAction() {
        // 保存信息
        TableInfoSettingsService.getInstance().saveTableInfo(tableInfo);
        super.doOKAction();
    }

}
