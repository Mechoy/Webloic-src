package weblogic.wsee.jaxws.framework.policy;

import java.util.List;

public interface PolicyReference {
   String getPolicyURI();

   String getCategory();

   boolean getEnabled();

   boolean isEnabled();

   List<OverrideProperty> getOverrideProperties();
}
