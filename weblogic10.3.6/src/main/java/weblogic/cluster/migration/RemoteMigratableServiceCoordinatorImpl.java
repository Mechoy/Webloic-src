package weblogic.cluster.migration;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.migration.management.MigratableServiceCoordinatorRuntime;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.DefaultObjectHandler;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.utils.Debug;

public class RemoteMigratableServiceCoordinatorImpl implements RemoteMigratableServiceCoordinator {
   private MigratableServiceCoordinatorRuntime runtimeMBeanDelegate;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final DebugLogger JTAMigration = DebugLogger.getDebugLogger("DebugJTAMigration");

   public RemoteMigratableServiceCoordinatorImpl(MigratableServiceCoordinatorRuntime var1) throws NamingException {
      this.runtimeMBeanDelegate = var1;
      Context var2 = this.getInitialContext();
      var2.bind("weblogic.cluster.migration.migratableServiceCoordinator", this);
   }

   public void migrateJTA(String var1, String var2, boolean var3, boolean var4) throws MigrationException {
      DomainMBean var5 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      JTAMigratableTargetMBean var6 = var5.lookupServer(var1).getJTAMigratableTarget();
      ServerMBean var7 = var5.lookupServer(var2);
      if (JTAMigration.isDebugEnabled()) {
         JTAMigration.debug("RemoteMigratableServiceCoordinatorImpl.migrateJTA() migratableTarget=" + var6 + ",destination=" + var7 + ",sourceUp=" + var3 + ",destinationUp=" + var4);
      }

      try {
         this.runtimeMBeanDelegate.migrateJTA((MigratableTargetMBean)var6, (ServerMBean)var7, var3, var4);
      } catch (weblogic.management.runtime.MigrationException var9) {
         throw new MigrationException(var9);
      }
   }

   public void deactivateJTA(String var1, String var2) throws RemoteException, MigrationException {
      String var3 = null;

      try {
         var3 = this.getCurrentLocationOfJTA(var1);
      } catch (PersistentStoreException var7) {
         throw new MigrationException("Unexpected exception accessing store", var7);
      }

      if (var3 != null && !var3.equals(var2)) {
         Debug.say("Current " + var3 + " destination " + var2);
         RemoteMigrationControl var4 = this.getRemoteMigrationControl(var3);
         if (var4 != null) {
            var4.deactivateTarget(var1, var2);

            try {
               this.getPersistentStoreMap().put(var1, var2);
            } catch (PersistentStoreException var6) {
               throw new MigrationException("Failed to contact server " + var3 + " hosting '" + var1 + "' to deactivate");
            }
         } else {
            throw new MigrationException("Failed to contact server " + var3 + " hosting '" + var1 + "' to deactivate");
         }
      }
   }

   private RemoteMigrationControl getRemoteMigrationControl(String var1) {
      Environment var2 = new Environment();
      Context var3 = null;

      RemoteMigrationControl var5;
      try {
         String var4 = URLManager.findAdministrationURL(var1);
         if (var4 == null) {
            var5 = null;
            return var5;
         }

         var2.setProviderUrl(var4);
         var3 = var2.getInitialContext();
         var5 = (RemoteMigrationControl)var3.lookup("weblogic.cluster.migrationControl");
      } catch (NamingException var18) {
         var5 = null;
         return var5;
      } catch (UnknownHostException var19) {
         var5 = null;
         return var5;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (NamingException var17) {
            }
         }

      }

      return var5;
   }

   private PersistentMap getPersistentStoreMap() throws PersistentStoreException {
      PersistentStore var1 = PersistentStoreManager.getManager().getDefaultStore();
      String var2 = "weblogic_migratable_services_store";
      if (var1 == null) {
         throw new PersistentStoreException("No store found");
      } else {
         return var1.createPersistentMap(var2, DefaultObjectHandler.THE_ONE);
      }
   }

   public String getCurrentLocationOfJTA(String var1) throws PersistentStoreException {
      return (String)this.getPersistentStoreMap().get(var1);
   }

   public void setCurrentLocation(String var1, String var2) throws PersistentStoreException {
      this.getPersistentStoreMap().put(var1, var2);
   }

   private Context getInitialContext() throws NamingException {
      Environment var1 = new Environment();
      var1.setCreateIntermediateContexts(true);
      return var1.getInitialContext();
   }
}
