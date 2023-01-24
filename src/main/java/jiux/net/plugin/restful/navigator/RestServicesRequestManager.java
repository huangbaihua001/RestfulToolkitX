package jiux.net.plugin.restful.navigator;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "restServicesRequestManager")
public class RestServicesRequestManager implements PersistentStateComponent<RestServicesRequestState> {

    private final RestServicesRequestState restServicesRequestState = new RestServicesRequestState();

    public static RestServicesRequestManager getInstance(Project p) {
        return p.getComponent(RestServicesRequestManager.class);
    }

    @Override
    public @Nullable RestServicesRequestState getState() {
        return restServicesRequestState;
    }

    @Override
    public void loadState(@NotNull RestServicesRequestState state) {
        XmlSerializerUtil.copyBean(state, restServicesRequestState);
    }
}
