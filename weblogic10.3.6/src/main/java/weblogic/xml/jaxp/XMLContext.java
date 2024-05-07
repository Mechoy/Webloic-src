package weblogic.xml.jaxp;

import java.security.AccessController;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.xml.registry.EntityCache;
import weblogic.xml.registry.RegistryEntityResolver;
import weblogic.xml.registry.XMLRegistry;
import weblogic.xml.registry.XMLRegistryException;
import weblogic.xml.util.Debug;

public class XMLContext {
   static ServerMBean serverConfigMBean = null;
   static Debug.DebugFacility dbg = null;

   static ServerMBean getServerConfigMBean() throws XMLRegistryException {
      if (serverConfigMBean == null) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         serverConfigMBean = ManagementService.getRuntimeAccess(var0).getServer();
         if (serverConfigMBean == null) {
            dbg.println("Can't get serverConfigMBean");
            throw new XMLRegistryException("ServerConfigMBean can't be null!");
         }
      }

      return serverConfigMBean;
   }

   public static void init() {
      try {
         ServerDebugMBean var0 = null;
         var0 = getServerConfigMBean().getServerDebug();
         if (var0 != null) {
            var0.addPropertyChangeListener(getDebug().new DebugListener());
            getDebug().setMBean(var0);
         } else {
            dbg.pe("Can't get serverDebugMBean. User settings through mbean will be ignored.");
         }
      } catch (Exception var1) {
         dbg.px(var1, "Failure setting serverDebugMBean.", 1, 2);
      }

   }

   public static Debug.DebugFacility getDebug() {
      return dbg;
   }

   public static RegistryEntityResolver getResolver() throws XMLRegistryException {
      return new RegistryEntityResolver();
   }

   public static XMLRegistry[] getRegistries() throws XMLRegistryException {
      return (new RegistryEntityResolver()).getRegistryPath();
   }

   public static EntityCache getCache(XMLRegistry var0) throws XMLRegistryException {
      return var0.getCache();
   }

   static {
      Debug.DebugSpec var0 = Debug.getDebugSpec();
      var0.name = "xml.jaxp";
      var0.prefix = "JAXP";
      dbg = Debug.makeDebugFacility(var0);
   }
}
