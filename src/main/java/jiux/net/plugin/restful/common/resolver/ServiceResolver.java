package jiux.net.plugin.restful.common.resolver;

import java.util.List;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;

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
