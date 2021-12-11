package jiux.net.plugin.restful.common.resolver;

import jiux.net.plugin.restful.navigation.action.RestServiceItem;

import java.util.List;

public interface ServiceResolver {
    /**
     *
     * @return
     */
    List<RestServiceItem> findAllSupportedServiceItemsInModule();

    /**
     *
     * @return
     */
    List<RestServiceItem> findAllSupportedServiceItemsInProject();
}
