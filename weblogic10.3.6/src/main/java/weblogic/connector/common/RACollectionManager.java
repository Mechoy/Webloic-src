package weblogic.connector.common;

import com.bea.connector.diagnostic.AdapterType;
import com.bea.connector.diagnostic.ConnectorDiagnosticImageDocument;
import com.bea.connector.diagnostic.ConnectorDiagnosticImageType;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.resource.spi.ResourceAdapter;
import weblogic.connector.exception.RAException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class RACollectionManager {
   private static final String CLASS_NAME = "weblogic.connector.common.RACollectionManager";
   private static List raList = new Vector(10);
   private static int connectionPoolsTotalCount = 0;

   public static void register(RAInstanceManager var0) {
      raList.add(var0);
      ConnectorService.getConnectorServiceRuntimeMBean().addConnectorRuntime(var0.getConnectorComponentRuntimeMBean());
   }

   public static void unregister(RAInstanceManager var0) {
      raList.remove(var0);
      ConnectorService.getConnectorServiceRuntimeMBean().removeConnectorRuntime(var0.getConnectorComponentRuntimeMBean());
   }

   public static void stop() throws RAException {
      Debug.service("stop() called on RACollectionManager; will stop each RA instance.");
      Iterator var0 = raList.iterator();

      while(var0.hasNext()) {
         RAInstanceManager var1 = (RAInstanceManager)var0.next();
         var1.stop();
      }

   }

   public static void halt() throws RAException {
      Debug.service("halt() called on RACollectionManager; will halt each RA instance.");
      Iterator var0 = raList.iterator();

      while(var0.hasNext()) {
         RAInstanceManager var1 = (RAInstanceManager)var0.next();
         var1.halt();
      }

   }

   public static void start() throws RAException {
      Debug.service("start() called on RACollectionManager; no actions to perform.");
   }

   public static void updateCounts(RAInstanceManager var0) {
      connectionPoolsTotalCount += var0.getAvailableConnectionPoolsCount();
   }

   public static Iterator getRAs() {
      return raList.iterator();
   }

   public static int getConnectionPoolsTotalCount() {
      return connectionPoolsTotalCount;
   }

   public static RAInstanceManager getRAInstanceManager(ResourceAdapter var0) {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      Iterator var4 = raList.iterator();

      RAInstanceManager var2;
      ResourceAdapter var3;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         var2 = (RAInstanceManager)var4.next();
         var3 = var2.getResourceAdapter();
      } while(var3 == null || !var2.getAdapterLayer().equals(var3, var0, var1));

      return var2;
   }

   public static RAInstanceManager getRAInstanceManager(String var0) {
      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManager( jndiName = '" + var0 + "' )");
      if (var0 == null || var0.trim().equals("")) {
         Debug.throwAssertionError("jndiName is null or empty");
      }

      RAInstanceManager var1 = null;
      Iterator var2 = raList.iterator();

      do {
         if (!var2.hasNext()) {
            Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManager() found no deployed RA with JNDI name = '" + var0 + "'");
            return null;
         }

         var1 = (RAInstanceManager)var2.next();
      } while(!var0.equals(var1.getJndiName()));

      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManager( " + var0 + " ) returning RA with moduleName = " + var1.getModuleName());
      return var1;
   }

   public static ConnectorDiagnosticImageDocument getXMLBean(ConnectorDiagnosticImageSource var0) {
      ConnectorDiagnosticImageDocument var1 = ConnectorDiagnosticImageDocument.Factory.newInstance();
      ConnectorDiagnosticImageType var2 = ConnectorDiagnosticImageType.Factory.newInstance();
      boolean var3 = var0 != null ? var0.timedout() : false;
      if (var3) {
         return var1;
      } else {
         AdapterType[] var4 = new AdapterType[raList.size()];
         Iterator var5 = raList.iterator();

         for(int var7 = 0; var5.hasNext(); ++var7) {
            RAInstanceManager var6 = (RAInstanceManager)var5.next();
            var4[var7] = var6.getXMLBean(var0);
         }

         var2.setAdapterArray(var4);
         var1.setConnectorDiagnosticImage(var2);
         return var1;
      }
   }

   public static RAInstanceManager getRAInstanceManagerByAppName(String var0, String var1) {
      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManagerByAppName( appName = '" + var0 + "' )");
      if (var0 == null || var0.trim().equals("")) {
         Debug.throwAssertionError("jndiName is null or empty");
      }

      RAInstanceManager var2 = null;
      Iterator var3 = raList.iterator();
      if (var1 != null && var1.length() > 0) {
         while(var3.hasNext()) {
            var2 = (RAInstanceManager)var3.next();
            if (var0.equals(var2.getAppContext().getApplicationName()) && var1.equals(var2.getVersionId())) {
               Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManager( " + var0 + " ) returning RA with moduleName = " + var2.getModuleName());
               return var2;
            }
         }
      }

      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManagerByAppName found no deployed RA with appName name = '" + var0 + "'");
      return null;
   }

   public static RAInstanceManager getRAInstanceManagerByAppIdAndModuleName(String var0, String var1) {
      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManagerByAppIdAndModuleName( appId = '" + var0 + "', moduleName = '" + var1 + "' )");
      if (var0 == null || var0.trim().equals("")) {
         Debug.throwAssertionError("appId is null or empty");
      }

      if (var1 == null || var1.trim().equals("")) {
         Debug.throwAssertionError("moduleName is null or empty");
      }

      RAInstanceManager var2 = null;
      Iterator var3 = raList.iterator();

      do {
         if (!var3.hasNext()) {
            Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManagerByAppIdAndModuleName found no deployed RA with appId = '" + var0 + "', and moduleName = '" + var1 + "'");
            return null;
         }

         var2 = (RAInstanceManager)var3.next();
      } while(!var0.equals(var2.getAppContext().getApplicationId()) || !var1.equals(var2.getModuleName()));

      Debug.println("weblogic.connector.common.RACollectionManager", ".getRAInstanceManagerByAppIdAndModuleName( " + var0 + ", " + var1 + " ) returning RA with moduleName = " + var2.getModuleName());
      return var2;
   }
}
