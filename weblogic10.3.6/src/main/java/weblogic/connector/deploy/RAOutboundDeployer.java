package weblogic.connector.deploy;

import java.security.AccessController;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ValidatingManagedConnectionFactory;
import weblogic.common.ResourceException;
import weblogic.connector.common.Debug;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.outbound.RAOutboundManager;
import weblogic.management.DeploymentException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;

public final class RAOutboundDeployer {
   private static final String CLASS_NAME = "weblogic.connector.deploy.RAOutboundDeployer";

   public static ConnectionPool prepare(ManagedConnectionFactory var0, OutboundInfo var1, String var2, String var3, RAOutboundManager var4) throws DeploymentException {
      Debug.enter("weblogic.connector.deploy.RAOutboundDeployer", "prepare(...) ");
      String var5 = var1.getKey();
      String var6 = var1.getRAInfo().getModuleName();
      ConnectionPool var7 = null;
      checkValidTestParameters(var0, var1);

      try {
         Debug.pooling("Constructing the connection pool for module '" + var6 + "' with key:  '" + var5 + "'");
         var7 = new ConnectionPool(var0, var1, var2, var3, var4);
         Debug.pooling("Starting the connection pool for module '" + var6 + "' with key:  '" + var5 + "'");
         var7.start(getPoolProperties(var1, var7.getName()));
      } catch (ResourceException var14) {
         Debug.pooling("While preparing the connection pool for module '" + var6 + "' with key:  '" + var5 + "' an exception was thrown.");
         String var9 = Debug.getExceptionStartPoolFailed(var14.toString());
         throw new DeploymentException(var9, var14);
      } finally {
         Debug.pooling("Done preparing the connection pool for module '" + var6 + "' with key:  '" + var5 + "'.");
         Debug.exit("weblogic.connector.deploy.RAOutboundDeployer", "prepare(...) ");
      }

      return var7;
   }

   private static void checkValidTestParameters(ManagedConnectionFactory var0, OutboundInfo var1) throws DeploymentException {
      if (!(var0 instanceof ValidatingManagedConnectionFactory)) {
         Vector var2 = new Vector();
         String var3;
         if (var1.getTestFrequencySeconds() != 0) {
            var3 = Debug.getExceptionTestFrequencyNonZero();
            var2.add(var3);
         }

         if (var1.isTestConnectionsOnCreate()) {
            var3 = Debug.getTestConnectionsOnCreateTrue();
            var2.add(var3);
         }

         if (var1.isTestConnectionsOnRelease()) {
            var3 = Debug.getTestConnectionsOnReleaseTrue();
            var2.add(var3);
         }

         if (var1.isTestConnectionsOnReserve()) {
            var3 = Debug.getTestConnectionsOnReserveTrue();
            var2.add(var3);
         }

         if (var2.size() > 0) {
            var3 = Debug.getExceptionInvalidTestingConfig();
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               var3 = var3 + (String)var4.next();
               if (var4.hasNext()) {
                  var3 = var3 + PlatformConstants.EOL + "  ";
               }
            }

            throw new DeploymentException(var3);
         }
      }

   }

   public static void updateInitialCapacity(ConnectionPool var0, OutboundInfo var1) {
      try {
         var0.setInitialCapacity(var1.getInitialCapacity());
      } catch (ResourceException var6) {
         Object var3;
         for(var3 = var6; var3 instanceof ResourceException && ((ResourceException)((ResourceException)var3)).getNested() != null; var3 = ((ResourceException)((ResourceException)var3)).getNested()) {
         }

         AuthenticatedSubject var4;
         for(var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()); var3 instanceof javax.resource.ResourceException && var0.getRAInstanceManager().getAdapterLayer().getLinkedException((javax.resource.ResourceException)var3, var4) != null; var3 = var0.getRAInstanceManager().getAdapterLayer().getLinkedException((javax.resource.ResourceException)var3, var4)) {
         }

         String var5 = var0.getRAInstanceManager().getAdapterLayer().toString(var3, var4);
         Debug.logCreateInitialConnectionsError(var0.getName(), var5);
         Debug.pooling("Failed to open initial connections for " + var0.getName() + " root cause " + var5 + " stacktrace:\n" + var0.getRAInstanceManager().getAdapterLayer().throwable2StackTrace((Throwable)var3, var4));
      }

   }

   public static Properties getPoolProperties(OutboundInfo var0, String var1) {
      Properties var2 = new Properties();
      var2.setProperty("name", var1);
      var2.setProperty("maxCapacity", String.valueOf(var0.getMaxCapacity()));
      var2.setProperty("capacityIncrement", String.valueOf(var0.getCapacityIncrement()));
      var2.setProperty("testOnReserve", String.valueOf(var0.isTestConnectionsOnReserve()));
      var2.setProperty("testOnRelease", String.valueOf(var0.isTestConnectionsOnRelease()));
      var2.setProperty("testOnCreate", String.valueOf(var0.isTestConnectionsOnCreate()));
      var2.setProperty("testFrequencySeconds", String.valueOf(var0.getTestFrequencySeconds()));
      var2.setProperty("shrinkEnabled", String.valueOf(var0.isShrinkingEnabled()));
      var2.setProperty("resvTimeoutSeconds", String.valueOf(var0.getConnectionReserveTimeoutSeconds()));
      var2.setProperty("shrinkFrequencySeconds", String.valueOf(var0.getShrinkFrequencySeconds()));
      var2.setProperty("resCreationRetrySeconds", String.valueOf(var0.getConnectionCreationRetryFrequencySeconds()));
      var2.setProperty("maxWaiters", String.valueOf(var0.getHighestNumWaiters()));
      var2.setProperty("maxUnavl", String.valueOf(var0.getHighestNumUnavailable()));
      var2.setProperty("inactiveResTimeoutSeconds", String.valueOf(var0.getInactiveConnectionTimeoutSeconds()));
      var2.setProperty("harvestFreqSecsonds", String.valueOf(var0.getProfileHarvestFrequencySeconds()));
      var2.setProperty("ignoreInUseResources", String.valueOf(var0.isIgnoreInUseConnectionsEnabled()));
      var2.setProperty("matchConnectionsSupported", String.valueOf(var0.isMatchConnectionsSupported()));
      var2.setProperty("initialCapacity", "0");
      return var2;
   }
}
