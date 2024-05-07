package weblogic.management.provider;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public interface RuntimeAccess extends RegistrationManager {
   DomainMBean getDomain();

   ServerMBean getServer();

   String getServerName();

   ServerRuntimeMBean getServerRuntime();

   void addAccessCallbackClass(String var1);

   AccessCallback[] initializeCallbacks(DomainMBean var1);

   boolean isAdminServer();

   boolean isAdminServerAvailable();

   String getDomainName();

   String getAdminServerName();
}
