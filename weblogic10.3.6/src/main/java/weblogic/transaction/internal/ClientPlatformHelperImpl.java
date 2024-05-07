package weblogic.transaction.internal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.management.runtime.JTATransaction;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.cluster.ThreadPreferredHost;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.Interceptor;
import weblogic.rmi.spi.InterceptorManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.transaction.TransactionHelper;

public class ClientPlatformHelperImpl extends PlatformHelper {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   Channel findAdminChannel(ServerIdentity var1) {
      return null;
   }

   boolean isJNDIEnabled() {
      return true;
   }

   String getRootName() {
      return "weblogic.transaction";
   }

   Context getInitialContext(String var1) throws NamingException {
      return this.getInitialContext(var1, true);
   }

   Context getInitialContext(String var1, boolean var2) throws NamingException {
      Hashtable var3 = new Hashtable();
      if (var1 != null) {
         var3.put("java.naming.provider.url", var1);
      }

      var3.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      return new InitialContext(var3);
   }

   boolean extendCoordinatorURL(String var1) {
      return false;
   }

   private static final boolean ignoreProtocol(String var0, String var1) {
      String var2 = getExtendedProtocol(var1);
      return var0.equalsIgnoreCase("iiop") || var0.equalsIgnoreCase("tgiop") || var2 != null;
   }

   private static final String getServerName(String var0) {
      int var1 = var0.indexOf(43);
      return var1 == -1 ? null : var0.substring(0, var1);
   }

   String getDomainName(String var1) {
      int var2 = var1.indexOf(43);
      if (var2 == -1) {
         return null;
      } else {
         int var3 = var1.indexOf(43, var2 + 1);
         if (var3 == -1) {
            return null;
         } else {
            int var4 = var1.indexOf(43, var3 + 1);
            return var4 == -1 ? null : var1.substring(var3 + 1, var4);
         }
      }
   }

   private static final String getProtocol(String var0) {
      int var1 = var0.indexOf(43);
      if (var1 == -1) {
         return null;
      } else {
         int var2 = var0.indexOf(43, var1 + 1);
         if (var2 == -1) {
            return null;
         } else {
            int var3 = var0.indexOf(43, var2 + 1);
            if (var3 == -1) {
               return null;
            } else {
               int var4 = var0.indexOf(43, var3 + 1);
               return var4 == -1 ? null : var0.substring(var3 + 1, var4);
            }
         }
      }
   }

   private static final String getExtendedProtocol(String var0) {
      int var1 = var0.indexOf(43);
      if (var1 == -1) {
         return null;
      } else {
         int var2 = var0.indexOf(43, var1 + 1);
         if (var2 == -1) {
            return null;
         } else {
            int var3 = var0.indexOf(43, var2 + 1);
            if (var3 == -1) {
               return null;
            } else {
               int var4 = var0.indexOf(43, var3 + 1);
               if (var4 == -1) {
                  return null;
               } else {
                  int var5 = var0.indexOf(43, var4 + 1);
                  return var5 == -1 ? null : var0.substring(var4 + 1, var5);
               }
            }
         }
      }
   }

   private static final String getHostPort(String var0) {
      int var1 = var0.indexOf(43);
      int var2 = var0.indexOf(43, var1 + 1);
      return var2 > var1 ? var0.substring(var1 + 1, var2) : var0.substring(var1 + 1);
   }

   private static final String getHost(String var0) {
      String var1 = getHostPort(var0);
      int var2 = var1.indexOf(58);
      return var2 == -1 ? null : var1.substring(0, var2);
   }

   private static final String getAdminProtocol(String var0) {
      int var1 = var0.indexOf(58);
      return var1 == -1 ? null : var0.substring(0, var1);
   }

   String getAdminPort(String var1) {
      int var2 = var1.indexOf(58);
      if (var2 == -1) {
         return null;
      } else {
         int var3 = var1.indexOf(58, var2 + 1);
         return var1.substring(var3 + 1);
      }
   }

   boolean openPrimaryStore(boolean var1) {
      return false;
   }

   public void setPrimaryStore(PersistentStore var1) {
      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      var2.setDefaultStore(var1);
   }

   PersistentStore getPrimaryStore() {
      PersistentStoreManager var1 = PersistentStoreManager.getManager();
      PersistentStore var2 = var1.getDefaultStore();
      return var2;
   }

   public PersistentStore getStore(String var1, String var2) throws PersistentStoreException {
      return null;
   }

   void closeStore(PersistentStore var1) throws PersistentStoreException {
      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      var2.closeFileStore(var1.getName());
   }

   int getQOSAdmin() {
      return 0;
   }

   boolean isLocalAdminChannelEnabled() {
      return false;
   }

   String findLocalAdminChannelURL(String var1) {
      return null;
   }

   String findLocalSSLURL(String var1) {
      return null;
   }

   public boolean isSSLURL(String var1) {
      return getProtocol(var1).equalsIgnoreCase("t3s") || getProtocol(var1).equalsIgnoreCase("https");
   }

   boolean isCheckpointLLR() {
      return false;
   }

   void dumpTLOG(String var1, String var2, boolean var3) throws PersistentStoreException {
   }

   void dumpJDBCTLOG(String var1, String var2, String var3, String var4) throws PersistentStoreException {
   }

   boolean isTransactionServiceRunning() {
      return false;
   }

   boolean isServerRunning() {
      return false;
   }

   String getDomainName() {
      return null;
   }

   void doTimerLifecycleHousekeeping() {
   }

   JTARecoveryRuntime getJTARecoveryRuntime(String var1) {
      return null;
   }

   void scheduleFailBack(String var1) {
   }

   JTATransaction createJTATransaction(TransactionImpl var1) {
      return null;
   }

   TransactionHelper getTransactionHelper() {
      return new TransactionHelperImpl();
   }

   boolean sendRequest(Object var1) {
      EndPoint var2 = (EndPoint)var1;
      TransactionImpl var3 = (TransactionImpl)getTM().getTransaction();
      if (var2 != null && var2.getHostID() instanceof ServerIdentity) {
         ServerIdentity var4 = (ServerIdentity)var2.getHostID();
         var3.setPreferredHost(var4);
         return var3.setCoordinatorDescriptor(var4, var2.getRemoteChannel());
      } else {
         return false;
      }
   }

   void sendRequestServer(Object var1) {
   }

   private static TransactionManagerImpl getTM() {
      return TransactionManagerImpl.getTransactionManager();
   }

   public CoordinatorDescriptorManager getCoordinatorDescriptorManager() {
      return null;
   }

   public void associateThreadPreferredHost(TransactionImpl var1, TransactionManagerImpl.TxThreadProperty var2) {
      if (var1 != null) {
         if (!var2.preferredHostSetForTx) {
            var2.preTxPreferredHost = ThreadPreferredHost.get();
            var2.preferredHostSetForTx = true;
         }

         var1.setActiveThread(Thread.currentThread());
         ThreadPreferredHost.set(var1.getPreferredHost());
      } else if (var2.preferredHostSetForTx) {
         ThreadPreferredHost.set(var2.preTxPreferredHost);
         var2.preferredHostSetForTx = false;
         var2.preTxPreferredHost = null;
      }

   }

   DisconnectMonitor getDisconnectMonitor() {
      return DisconnectMonitorListImpl.getDisconnectMonitor();
   }

   AuthenticatedSubject getRemoteSubject(String var1) throws IOException, RemoteException {
      return null;
   }

   final boolean isInCluster() {
      return false;
   }

   Collection<String> getActiveServersInCluster() {
      return null;
   }

   CoordinatorDescriptor findServerInClusterByLocalJNDI(String var1, Collection var2) {
      Collection var3 = this.getActiveServersInCluster();
      return null;
   }

   boolean isServer() {
      return false;
   }

   void registerRMITransactionInterceptor(Interceptor var1) {
      InterceptorManager.getManager().setTransactionInterceptor(var1);
   }

   boolean isCDSEnabled() {
      return false;
   }

   boolean isDomainExcluded(String var1) {
      return false;
   }

   int getInteropMode() {
      return 0;
   }

   public ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public AuthenticatedSubject getCurrentSubject() {
      return SecurityServiceManager.getCurrentSubject(this.getKernelID());
   }

   private AuthenticatedSubject getKernelID() {
      return kernelID;
   }
}
