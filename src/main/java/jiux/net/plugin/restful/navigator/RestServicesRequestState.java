package jiux.net.plugin.restful.navigator;

import com.intellij.psi.PsiElement;
import com.intellij.util.xmlb.annotations.Tag;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jdom.Element;

public class RestServicesRequestState {

  public Map<String, Map<String, String>> restReqMap = new LinkedHashMap<>();
}
