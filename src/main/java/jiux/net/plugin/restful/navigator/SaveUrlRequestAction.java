package jiux.net.plugin.restful.navigator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.PsiNavigateUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jiux.net.plugin.restful.common.Constants;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;
import jiux.net.plugin.utils.RestServiceDataKeys;
import org.apache.commons.collections.CollectionUtils;

public class SaveUrlRequestAction extends AnAction implements DumbAware {

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Presentation p = e.getPresentation();
        p.setVisible(isVisible(e));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<RestServiceItem> serviceItems = RestServiceDataKeys.SERVICE_ITEMS.getData(e.getDataContext());

        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }

        Project project = e.getProject();
        RestServiceDetail restServiceDetail = project.getComponent(RestServiceDetail.class);
        if (restServiceDetail == null) {
            return;
        }


        String reqHeader = restServiceDetail.requestHeaderTextArea == null ? "" :
                                                   restServiceDetail.requestHeaderTextArea.getText();
        String reqParam =  restServiceDetail.requestParamsTextArea == null ? "" :
                                                   restServiceDetail.requestParamsTextArea.getText();
        String reqBody = restServiceDetail.requestBodyTextArea == null ? "" :
                                                   restServiceDetail.requestBodyTextArea.getText();

        RestServicesRequestManager instance = RestServicesRequestManager.getInstance(project);

        RestServicesRequestState state = instance.getState();
        if (state == null) {
            return;
        }

        Map<String, String> reqMap = new LinkedHashMap<>();
        {
            reqMap.put(Constants.REQ_BODY_NAME, reqBody);
            reqMap.put(Constants.REQ_PARAM_NAME, reqParam);
            reqMap.put(Constants.REQ_HEADER_NAME, reqHeader);
        }

        for (RestServiceItem serviceItem : serviceItems) {
            String key = serviceItem.getKey();
            state.restReqMap.put(key, reqMap);
        }

    }


    protected boolean isVisible(AnActionEvent e) {
        return true;
    }
}
