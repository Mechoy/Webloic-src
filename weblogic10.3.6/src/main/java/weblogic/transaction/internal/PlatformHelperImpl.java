package weblogic.transaction.internal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.jdbc.utils.BasicDataSource;
import weblogic.jndi.Environment;
import weblogic.kernel.KernelStatus;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TransactionLogJDBCStoreMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.management.runtime.JTATransaction;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.cluster.ThreadPreferredHost;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.Interceptor;
import weblogic.rmi.spi.InterceptorManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.RuntimeHandler;
import weblogic.store.StoreWritePolicy;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.admin.JDBCAdminHandler;
import weblogic.store.admin.RuntimeHandlerImpl;
import weblogic.store.internal.PersistentStoreImpl;
import weblogic.store.io.file.FileStoreIO;
import weblogic.store.io.jdbc.JDBCStoreIO;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.t3.srvr.T3Srvr;
import weblogic.transaction.TransactionHelper;

public class PlatformHelperImpl extends PlatformHelper {
   private static ServerCoordinatorDescriptorManager scdm = new ServerCoordinatorDescriptorManagerImpl();
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private PersistentStore persistentStore;
   JDBCTLogServerMBeanIF m_JDBCTLogServerMBeanIF;

   Channel findAdminChannel(ServerIdentity var1) {
      ProtocolService var2 = (new ProtocolServiceImpl()).getProtocolService();
      return var2.findServerChannel(var1, ProtocolService.PROTOCOL_ADMIN);
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
      Environment var3 = new Environment();
      if (var1 != null) {
         var3.setProviderUrl(var1);
      }

      var3.setCreateIntermediateContexts(true);
      var3.setReplicateBindings(var2);
      var3.setInitialContextFactory("weblogic.jndi.WLInitialContextFactory");
      return var3.getInitialContext();
   }

   boolean extendCoordinatorURL(String var1) {
      String var2 = getProtocol(var1);
      if (var2 == null) {
         var2 = "t3";
      }

      int var4 = ((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode();
      return !ignoreProtocol(var2, var1) && var4 == 0 && this.isLocalAdminChannelEnabled();
   }

   private static final boolean ignoreProtocol(String var0, String var1) {
      String var2 = getExtendedProtocol(var1);
      return var0.equalsIgnoreCase("iiop") || var0.equalsIgnoreCase("tgiop") || var2 != null;
   }

   private static final SecurityConfigurationMBean getSecurityConfigurationMBean() {
      return ManagementService.getRuntimeAccess(kernelID).getDomain().getSecurityConfiguration();
   }

   boolean isCDSEnabled() {
      return getSecurityConfigurationMBean().isCrossDomainSecurityEnabled();
   }

   boolean isDomainExcluded(String var1) {
      if (var1 == null) {
         return false;
      } else {
         String[] var2 = getSecurityConfigurationMBean().getExcludedDomainNames();
         if (var2 == null) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var1.equals(var2[var3])) {
                  return true;
               }
            }

            return false;
         }
      }
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

   public boolean isSSLURL(String var1) {
      String var2 = getProtocol(var1);
      return var2 != null && (var2.equalsIgnoreCase("t3s") || var2.equalsIgnoreCase("https"));
   }

   public void setPrimaryStore(PersistentStore var1) {
      this.persistentStore = var1;
   }

   PersistentStore getPrimaryStore() {
      if (this.persistentStore == null) {
         this.openPrimaryStore(false);
      }

      return this.persistentStore;
   }

   boolean openPrimaryStore(boolean var1) {
      if (var1) {
         try {
            this.closeStore(this.persistentStore);
         } catch (Exception var6) {
            TXLogger.logFailedSetPrimaryStoreRetry(var6);
         }
      }

      TransactionManagerImpl var2 = getTM();
      if (var2.getJdbcTLogEnabled()) {
         JDBCTLogServerMBeanIF var3 = this.getJDBCTLogServerMBeanIF();

         try {
            this.persistentStore = this.getPrimaryStoreProvidedServerMBean(var3.getName(), var3, var2.getJdbcTLogMaxRetrySecondsBeforeTXException());
         } catch (PersistentStoreException var5) {
            TXLogger.logFailedSetPrimaryStore(var5);
            return false;
         }
      } else {
         this.persistentStore = PersistentStoreManager.getManager().getDefaultStore();
      }

      return this.persistentStore == null ? false : var1;
   }

   void setJDBCTLogServerMBeanIF(JDBCTLogServerMBeanIF var1) {
      this.m_JDBCTLogServerMBeanIF = var1;
   }

   JDBCTLogServerMBeanIF getJDBCTLogServerMBeanIF() {
      return this.m_JDBCTLogServerMBeanIF != null ? this.m_JDBCTLogServerMBeanIF : new JDBCTLogServerMBeanIF() {
         ServerMBean server;

         {
            this.server = ManagementService.getRuntimeAccess(PlatformHelperImpl.kernelID).getServer();
         }

         public TransactionLogJDBCStoreMBean getTransactionLogJDBCStore() {
            return this.server.getTransactionLogJDBCStore();
         }

         public String getName() {
            return this.server.getName();
         }
      };
   }

   private PersistentStore getPrimaryStoreProvidedServerMBean(String var1, JDBCTLogServerMBeanIF var2, int var3) throws PersistentStoreException {
      TransactionLogJDBCStoreMBean var4 = var2.getTransactionLogJDBCStore();
      RuntimeHandlerImpl var5 = null;
      if (this.isServer()) {
         var5 = new RuntimeHandlerImpl();
      }

      PersistentStoreXA var6 = JDBCAdminHandler.makeStore(var1 + "JTA_JDBCTLOGStore", (String)null, var4, (ClearOrEncryptedService)null, var5);
      HashMap var7 = new HashMap();
      var7.put("MaxRetrySeconds", var3);
      var6.open(var7);
      return var6;
   }

   public PersistentStore getStore(String var1, String var2) throws PersistentStoreException {
      final ServerMBean var3 = ManagementService.getRuntimeAccess(kernelID).getDomain().lookupServer(var1);
      boolean var4 = var3.getTransactionLogJDBCStore().isEnabled();
      if (var4) {
         JDBCTLogServerMBeanIF var8 = new JDBCTLogServerMBeanIF() {
            public TransactionLogJDBCStoreMBean getTransactionLogJDBCStore() {
               return var3.getTransactionLogJDBCStore();
            }

            public String getName() {
               return var3.getName();
            }
         };
         return this.getPrimaryStoreProvidedServerMBean(var1, var8, var3.getTransactionLogJDBCStore().getMaxRetrySecondsBeforeTXException());
      } else {
         FileAdminHandler var5 = new FileAdminHandler();

         try {
            var5.createMigratedDefaultStore(var3, false);
         } catch (DeploymentException var7) {
            throw new PersistentStoreException(var7);
         }

         PersistentStoreXA var6 = var5.getStore();
         return var6;
      }
   }

   void closeStore(PersistentStore var1) throws PersistentStoreException {
      PersistentStoreManager var2 = PersistentStoreManager.getManager();
      var2.closeFileStore(var1.getName());

      try {
         var1.close();
      } catch (Exception var4) {
      }

   }

   int getQOSAdmin() {
      return 103;
   }

   boolean isLocalAdminChannelEnabled() {
      ProtocolService var1 = (new ProtocolServiceImpl()).getProtocolService();
      return var1.isLocalAdminChannelEnabled();
   }

   String findLocalAdminChannelURL(String var1) {
      String var2 = getServerName(var1);

      try {
         return (new ProtocolServiceImpl()).getProtocolService().findAdministrationURL(var2);
      } catch (Exception var4) {
         TXLogger.logDowngradeAdminURL(var4);
         return null;
      }
   }

   String findLocalSSLURL(String var1) {
      String var2 = getServerName(var1);
      String var3 = this.getDomainName(var1);
      String var4 = getHost(var1);

      try {
         ProtocolService var5 = (new ProtocolServiceImpl()).getProtocolService();
         String var6 = var5.findURL(var2, var5.getDefaultSecureProtocol());
         if (var6 == null) {
            TXLogger.logDowngradeSSLURL(new Exception("SSL Port can not be obtained"));
            return null;
         } else {
            String var7 = this.getAdminPort(var6);
            String var8 = getAdminProtocol(var6);
            return var2 + "+" + var4 + ":" + var7 + "+" + var3 + "+" + var8 + "+";
         }
      } catch (Exception var9) {
         TXLogger.logDowngradeSSLURL(var9);
         return null;
      }
   }

   boolean isCheckpointLLR() {
      return false;
   }

   void dumpTLOG(String var1, String var2, boolean var3) throws PersistentStoreException {
      PersistentStoreManager var4 = PersistentStoreManager.getManager();
      String var5 = "_WLS_" + var2;
      FileStoreIO var6 = new FileStoreIO(var5, var1);
      PersistentStoreImpl var7 = new PersistentStoreImpl(var2, var6, (RuntimeHandler)null);
      HashMap var8 = new HashMap();
      var8.put("SynchronousWritePolicy", StoreWritePolicy.CACHE_FLUSH);
      var7.open(var8);
      var4.setDefaultStore(var7);
      StoreTransactionLoggerImpl var9 = new StoreTransactionLoggerImpl(System.out, !var3);
      if (var3) {
         long var10 = var9.delete();
         System.out.println("Deleted " + var10 + " tlog entries");
      }

   }

   void dumpJDBCTLOG(String var1, String var2, String var3, String var4) throws PersistentStoreException {
      PersistentStoreManager var5 = PersistentStoreManager.getManager();
      PersistentStoreImpl var6 = null;
      String var7 = null;
      String var8 = var1 + "JTA_JDBCTLOGStore";
      String var9 = var2 + "/config/config.xml";
      DomainMBean var10 = null;
      System.setProperty("weblogic.RootDirectory", var2);

      try {
         var10 = ManagementServiceClient.getClientAccess().getDomain(var9, false);
      } catch (Exception var22) {
         var22.printStackTrace();
      }

      JDBCSystemResourceMBean var11 = var10.lookupJDBCSystemResource(var3);
      if (var11 == null) {
         throw new PersistentStoreException("DataSourceName " + var3 + " NotFound");
      } else {
         JDBCDataSourceBean var12 = var11.getJDBCResource();
         JDBCDriverParamsBean var13 = var12.getJDBCDriverParams();
         var7 = var13.getPassword();
         BasicDataSource var14 = null;

         try {
            var14 = new BasicDataSource(var12, var7);
         } catch (Exception var21) {
            var21.printStackTrace();
         }

         byte var15 = 20;
         byte var16 = 20;
         byte var17 = 20;
         JDBCStoreIO var18 = new JDBCStoreIO(var8, var14, var4, "", var15, var16, var17);
         var6 = new PersistentStoreImpl(var8, var18, (RuntimeHandler)null);
         HashMap var19 = new HashMap();
         var6.open(var19);
         var19.put("SynchronousWritePolicy", StoreWritePolicy.CACHE_FLUSH);
         var5.setDefaultStore(var6);
         new StoreTransactionLoggerImpl(System.out, true);
      }
   }

   boolean isTransactionServiceRunning() {
      return TransactionService.isRunning();
   }

   boolean isServerRunning() {
      return T3Srvr.getT3Srvr().getRunState() == 2;
   }

   String getDomainName() {
      return ManagementService.getRuntimeAccess(kernelID).getDomainName();
   }

   void doTimerLifecycleHousekeeping() {
      if (!this.isTransactionServiceRunning() && getTM().getNumTransactions() == 0) {
         if (ClientInitiatedTxShutdownService.isSuspending()) {
            ClientInitiatedTxShutdownService.suspendDone();
         }

         if (TransactionService.isSuspending()) {
            ((ServerTransactionManagerImpl)getTM()).checkpoint();
            TransactionService.suspendDone();
         }
      }

   }

   JTARecoveryRuntime getJTARecoveryRuntime(String var1) {
      return TransactionRecoveryService.getRuntimeMBean(var1);
   }

   void scheduleFailBack(String var1) {
      TransactionRecoveryService.scheduleFailBack(var1);
   }

   JTATransaction createJTATransaction(TransactionImpl var1) {
      return new JTATransactionImpl((ServerTransactionImpl)var1);
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
      return scdm;
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
      AuthenticatedSubject var2 = null;

      try {
         var2 = RemoteDomainSecurityHelper.getSubject(var1);
         return var2;
      } catch (IOException var4) {
         throw var4;
      }
   }

   final boolean isInCluster() {
      return ManagementService.getRuntimeAccess(kernelID).getServer().getCluster() != null;
   }

   Collection<String> getActiveServersInCluster() {
      if (!this.isInCluster()) {
         return null;
      } else {
         ClusterService var1 = ClusterService.getClusterService();
         Collection var2 = var1.getRemoteMembers();
         Iterator var3 = var2.iterator();
         ArrayList var4 = new ArrayList();

         while(var3.hasNext()) {
            Object var5 = var3.next();
            if (var5 instanceof ClusterMemberInfo) {
               ClusterMemberInfo var6 = (ClusterMemberInfo)var5;
               String var7 = var6.serverName();
               var4.add(var7);
            }
         }

         return var4;
      }
   }

   ServerCoordinatorDescriptor findServerInClusterByLocalJNDI(String var1, Collection var2) {
      Collection var3 = this.getActiveServersInCluster();
      if (var3 == null) {
         return null;
      } else {
         var3.removeAll(var2);
         if (var3.isEmpty()) {
            return null;
         } else {
            InitialContext var4 = null;

            label164: {
               String var6;
               try {
                  var4 = new InitialContext();
                  Iterator var5 = var3.iterator();

                  while(true) {
                     if (!var5.hasNext()) {
                        break label164;
                     }

                     var6 = (String)var5.next();
                     Object var7 = null;

                     try {
                        try {
                           var4.lookup("weblogic.transaction.resources." + var6 + "." + var1);
                           if (TxDebug.JTA2PC.isDebugEnabled()) {
                              TxDebug.txdebug(TxDebug.JTA2PC, (TransactionImpl)TransactionHelper.getTransactionHelper().getTransaction(), "findResourceInCluster: check local JNDI: found XA resource " + var1 + " on  " + var6);
                           }
                        } catch (NamingException var26) {
                           try {
                              var4.lookup("weblogic.transaction.nonxaresources." + var6 + "." + var1);
                              if (TxDebug.JTA2PC.isDebugEnabled()) {
                                 TxDebug.txdebug(TxDebug.JTA2PC, (TransactionImpl)TransactionHelper.getTransactionHelper().getTransaction(), "findResourceInCluster: check local JNDI: found non-XA resource " + var1 + " on  " + var6);
                              }
                           } catch (NamingException var25) {
                              continue;
                           }
                        }

                        ClientTransactionManagerImpl var8 = (ClientTransactionManagerImpl)var4.lookup("weblogic.transaction.coordinators." + var6);
                        String var9 = var8.getCoordinatorURL();
                        ServerCoordinatorDescriptor var10 = ((ServerCoordinatorDescriptorManager)this.getCoordinatorDescriptorManager()).getOrCreate(var9);
                        ServerCoordinatorDescriptor var11 = var10;
                        return var11;
                     } catch (Exception var27) {
                     }
                  }
               } catch (NamingException var28) {
                  var6 = null;
               } finally {
                  if (var4 != null) {
                     try {
                        var4.close();
                     } catch (NamingException var24) {
                     }
                  }

               }

               return var6;
            }

            if (TxDebug.JTA2PC.isDebugEnabled()) {
               TxDebug.txdebug(TxDebug.JTA2PC, (TransactionImpl)TransactionHelper.getTransactionHelper().getTransaction(), "findResourceInCluster: check local JNDI: cannot find resource " + var1 + " on any server in cluster");
            }

            return null;
         }
      }
   }

   boolean isServer() {
      return KernelStatus.isServer();
   }

   void registerRMITransactionInterceptor(Interceptor var1) {
      InterceptorManager.getManager().setTransactionInterceptor(var1);
   }

   int getInteropMode() {
      return ((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode();
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

   public interface JDBCTLogServerMBeanIF {
      TransactionLogJDBCStoreMBean getTransactionLogJDBCStore();

      String getName();
   }
}
