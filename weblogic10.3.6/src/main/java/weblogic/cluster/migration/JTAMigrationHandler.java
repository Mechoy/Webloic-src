package weblogic.cluster.migration;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.migration.management.MigratableServiceCoordinatorRuntime;
import weblogic.cluster.singleton.LeasingException;
import weblogic.cluster.singleton.MigratableServerService;
import weblogic.cluster.singleton.SingletonMonitorRemote;
import weblogic.jndi.Environment;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreException;

public class JTAMigrationHandler {
   private static int SINGLETON_MASTER_RETRY_COUNT;
   private static RemoteMigratableServiceCoordinator remoteMigratableServiceCoordinator;
   private static final boolean DEBUG = true;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void deactivateJTA(MigratableTargetMBean var0, String var1) throws MigrationException {
      if (isTargetAutoMigratable(var0)) {
         try {
            SingletonMonitorRemote var2 = MigratableServerService.theOne().getSingletonMasterRemote(SINGLETON_MASTER_RETRY_COUNT);
            if (var2 == null) {
               throw new MigrationException("Could not deactivate JTA, the singleton monitor is unreachable.");
            } else {
               var2.deactivateJTA(var1, var1);
            }
         } catch (Exception var3) {
            throw new MigrationException("Could not deactivate JTA: " + var3, var3);
         }
      } else {
         if (remoteMigratableServiceCoordinator == null) {
            initializeRemoteMigratableServiceCoordinator();
         }

         try {
            remoteMigratableServiceCoordinator.deactivateJTA(var1, var1);
         } catch (RemoteException var4) {
            throw new MigrationException("Could not deactivate JTA: " + var4, var4);
         }
      }
   }

   public static void deactivateJTA(String var0, String var1) throws MigrationException {
      deactivateJTA(getJTAMigratableTarget(var0), var1);
   }

   public static void migrateJTA(String var0, String var1, boolean var2, boolean var3) throws MigrationException {
      MigratableTargetMBean var4 = getJTAMigratableTarget(var0);
      if (isTargetAutoMigratable(var4)) {
         if (!var3) {
            deactivateJTA(var0, var1);
         }

         try {
            SingletonMonitorRemote var5 = MigratableServerService.theOne().getSingletonMasterRemote();
            if (!var5.migrate(var0, var1)) {
               throw new MigrationException("Migration of " + var0 + " to " + var1 + " failed.");
            }
         } catch (LeasingException var6) {
            throw new MigrationException("Could not migrate JTA: " + var6, var6);
         } catch (RemoteException var7) {
            throw new MigrationException("Could not migrate JTA: " + var7, var7);
         }
      } else {
         if (remoteMigratableServiceCoordinator == null) {
            initializeRemoteMigratableServiceCoordinator();
         }

         remoteMigratableServiceCoordinator.migrateJTA(var0, var1, var2, var3);
      }

   }

   public static String findJTA(String var0) throws RemoteException, PersistentStoreException {
      MigratableTargetMBean var1 = getJTAMigratableTarget(var0);
      if (isTargetAutoMigratable(var1)) {
         try {
            SingletonMonitorRemote var2 = MigratableServerService.theOne().getSingletonMasterRemote();
            return var2.findServiceLocation(var0);
         } catch (LeasingException var3) {
            throw new RemoteException("Could not find JTA: " + var3);
         }
      } else {
         if (remoteMigratableServiceCoordinator == null) {
            initializeRemoteMigratableServiceCoordinator();
         }

         return remoteMigratableServiceCoordinator.getCurrentLocationOfJTA(var0);
      }
   }

   public static boolean isAvailable(String var0) {
      MigratableTargetMBean var1 = getJTAMigratableTarget(var0);
      if (isTargetAutoMigratable(var1)) {
         try {
            SingletonMonitorRemote var2 = MigratableServerService.theOne().getSingletonMasterRemote();
            return var2 != null;
         } catch (LeasingException var3) {
            return false;
         }
      } else {
         try {
            if (remoteMigratableServiceCoordinator == null) {
               initializeRemoteMigratableServiceCoordinator();
            }

            return true;
         } catch (MigrationException var4) {
            return false;
         }
      }
   }

   private static MigratableTargetMBean getJTAMigratableTarget(String var0) {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var0);
      if (var1 == null) {
         throw new MigrationException("No server found named " + var0);
      } else {
         JTAMigratableTargetMBean var2 = var1.getJTAMigratableTarget();
         if (var2 == null) {
            throw new MigrationException("No JTA migratable target found on " + var0);
         } else {
            return var2;
         }
      }
   }

   private static boolean isTargetAutoMigratable(MigratableTargetMBean var0) throws MigrationException {
      return !var0.getMigrationPolicy().equals("manual");
   }

   private static void initializeRemoteMigratableServiceCoordinator() {
      DomainMBean var0 = ManagementService.getRuntimeAccess(kernelId).getDomain();

      String var1;
      try {
         var1 = URLManager.findAdministrationURL(var0.getAdminServerName());
      } catch (UnknownHostException var9) {
         throw new MigrationException("Cannot contact the administration server to deactivate JTA.", var9);
      }

      Environment var2 = new Environment();
      var2.setProviderUrl(var1);
      if (var0.getAdminServerName().equals(ManagementService.getRuntimeAccess(kernelId).getServer().getName())) {
         ServerRuntimeMBean var10 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         if (2 != var10.getStateVal()) {
            throw new MigrationException("Admin server is not available");
         } else {
            remoteMigratableServiceCoordinator = MigratableServiceCoordinatorRuntime.remoteCoordinator;
         }
      } else {
         try {
            Context var3 = var2.getInitialContext();
            remoteMigratableServiceCoordinator = (RemoteMigratableServiceCoordinator)var3.lookup("weblogic.cluster.migration.migratableServiceCoordinator");
            if (remoteMigratableServiceCoordinator != null) {
               DisconnectMonitor var4 = DisconnectMonitorListImpl.getDisconnectMonitor();

               try {
                  var4.addDisconnectListener(remoteMigratableServiceCoordinator, new MigratableServiceCoordinatorDisconnectListener());
                  return;
               } catch (DisconnectMonitorUnavailableException var6) {
               } catch (Exception var7) {
                  p("Unexpected exception while getting RemoteMigratableServiceCoordinator", var7);
               }
            }
         } catch (NamingException var8) {
            p("Unexpected exception while getting RemoteMigratableServiceCoordinator", var8);
         }

         throw new MigrationException("Cannot contact the administration server to deactivate JTA.");
      }
   }

   private static void p(String var0, Exception var1) {
      System.out.println("<JTAMigrationHandler> " + var0 + " : " + var1);
   }

   static {
      try {
         String var0 = System.getProperty("weblogic.cluster.jta.SingletonMasterRetryCount", "20");
         SINGLETON_MASTER_RETRY_COUNT = Integer.valueOf(var0);
      } catch (Exception var1) {
         SINGLETON_MASTER_RETRY_COUNT = 20;
      }

   }

   private static final class MigratableServiceCoordinatorDisconnectListener implements DisconnectListener {
      private MigratableServiceCoordinatorDisconnectListener() {
      }

      public void onDisconnect(DisconnectEvent var1) {
         JTAMigrationHandler.remoteMigratableServiceCoordinator = null;
      }

      // $FF: synthetic method
      MigratableServiceCoordinatorDisconnectListener(Object var1) {
         this();
      }
   }
}
