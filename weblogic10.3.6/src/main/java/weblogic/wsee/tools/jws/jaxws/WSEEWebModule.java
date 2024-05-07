package weblogic.wsee.tools.jws.jaxws;

import java.util.Map;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.wsee.deploy.WSEEBaseModule;

public class WSEEWebModule extends WSEEBaseModule {
   private final Map<String, Class> linkMap;

   public WSEEWebModule(Map<String, Class> var1) {
      this.linkMap = var1;
   }

   protected String getLinkName(ServiceImplBeanBean var1) {
      return var1.getServletLink();
   }

   protected void setLinkName(ServiceImplBeanBean var1, String var2) {
      var1.setServletLink(var2);
   }

   protected Map<String, Class> getLinkMap() {
      return this.linkMap;
   }

   protected EnvEntryBean[] getEnvEntries(ServiceImplBeanBean var1) {
      return null;
   }
}
