package jiux.net.plugin.restful.codegen.service;

import jiux.net.plugin.restful.codegen.dto.SettingsStorageDTO;
import jiux.net.plugin.restful.codegen.service.impl.SettingsStorageServiceImpl;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;

/**
 * @author makejava
 * @version 1.0.0
 * @date 2021/08/07 11:55
 */
public interface SettingsStorageService extends PersistentStateComponent<SettingsStorageDTO> {
    /**
     * 获取实例
     *
     * @return {@link SettingsStorageService}
     */
    static SettingsStorageService getInstance() {
        return ServiceManager.getService(SettingsStorageServiceImpl.class);
    }

    /**
     * 获取设置存储
     *
     * @return {@link SettingsStorageDTO}
     */
    static SettingsStorageDTO getSettingsStorage() {
        return getInstance().getState();
    }
}
