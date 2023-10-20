package jiux.net.plugin.utils;

import com.intellij.openapi.actionSystem.DataKey;
import java.util.List;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;

public class RestServiceDataKeys {

  public static final DataKey<List<RestServiceItem>> SERVICE_ITEMS = DataKey.create(
    "SERVICE_ITEMS"
  );

  private RestServiceDataKeys() {}
}
