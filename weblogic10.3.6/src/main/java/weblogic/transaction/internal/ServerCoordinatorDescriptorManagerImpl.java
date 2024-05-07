package weblogic.transaction.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.Remote;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TransactionLogger;
import weblogic.utils.collections.ArraySet;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class ServerCoordinatorDescriptorManagerImpl implements ServerCoordinatorDescriptorManager {
   private static final int SERVER_REFRESH_INTERVAL_MILLIS = 5000;
   private static final CacheLock cacheLock = new CacheLock();
   private static final HashMap xaResourceToServers = new HashMap();
   private static final HashMap nonXAResourceToServers = new HashMap();
   private static final HashMap activeServers = new HashMap();
   private static final HashMap lostServers = new HashMap();
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int purgeFromCheckpointIntervalSeconds;
   private boolean serverCheckpointNeeded = false;
   private ServerCheckpoint latestServerCheckpoint = null;

   public ServerCoordinatorDescriptor getLocalCoordinatorDescriptor() {
      return ServerCoordinatorDescriptorManagerImpl.SCDMaker.SELF;
   }

   public void setLocalCoordinatorDescriptor(CoordinatorDescriptor var1) {
   }

   public String getLocalCoordinatorURL() {
      return this.getLocalCoordinatorDescriptor() == null ? null : this.getLocalCoordinatorDescriptor().getCoordinatorURL();
   }

   public ServerCoordinatorDescriptor getOrCreate(String var1) {
      return var1 == null ? null : this.getOrCreate(var1, (ServerMBean)null);
   }

   public ServerCoordinatorDescriptor getOrCreateForMigration(String var1) {
      try {
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelID).getDomain().lookupServer(var1);
         String var3 = getCoordinatorURL(var2);
         return this.getOrCreate(var3);
      } catch (Exception var4) {
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("ServerCoordinatorDescriptor.getOrCreateForMigration(server=" + var1 + ") failed", var4);
         }

         return null;
      }
   }

   public List getAllCheckpointServers() {
      ArrayList var1 = new ArrayList();
      synchronized(cacheLock) {
         Collection var3 = activeServers.values();
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               ServerCoordinatorDescriptor var5 = (ServerCoordinatorDescriptor)var4.next();
               if (var5.includeInCheckpoint()) {
                  var1.add(var5.getCoordinatorURL());
               }
            }
         }

         Collection var10 = lostServers.values();
         if (var10 != null) {
            Iterator var9 = var10.iterator();

            while(var9.hasNext()) {
               ServerCoordinatorDescriptor var6 = (ServerCoordinatorDescriptor)var9.next();
               if (var6.includeInCheckpoint()) {
                  var1.add(var6.getCoordinatorURL());
               }
            }
         }

         return var1;
      }
   }

   public void setLatestServerCheckpoint(TransactionLogger var1, ServerCheckpoint var2) {
      long var4 = getTM().getRuntime().getTransactionTotalCount();
      ServerCheckpoint var3;
      synchronized(cacheLock) {
         var3 = this.latestServerCheckpoint;
         this.latestServerCheckpoint = var2;
      }

      if (var3 != null) {
         var1.release(var3);
      }

   }

   public void checkpointIfNecessary() {
      synchronized(cacheLock) {
         if (!this.serverCheckpointNeeded) {
            return;
         }

         this.serverCheckpointNeeded = false;
      }

      this.checkpointServers();
   }

   public ServerCoordinatorDescriptor[] getServers(String var1) {
      ServerCoordinatorDescriptor[] var2 = getServersHostingXAResource(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = getServersHostingNonXAResource(var1);
         return var2 != null ? var2 : null;
      }
   }

   public void checkpointServers() {
      ServerCheckpoint var1 = new ServerCheckpoint();
      var1.blockingStore(getTM().getTransactionLogger());
   }

   public void setServerCheckpointNeeded(boolean var1) {
      this.serverCheckpointNeeded = var1;
   }

   public void setPurgeFromCheckpointIntervalSeconds(int var1) {
      this.purgeFromCheckpointIntervalSeconds = var1;
      if (TxDebug.JTARecovery.isDebugEnabled()) {
         TxDebug.JTARecovery.debug("ServerCoordinatorDescriptor.setPurgeFromCheckpointIntervalSecs:" + var1);
      }

   }

   public int getPurgeFromCheckpointIntervalSeconds() {
      return this.purgeFromCheckpointIntervalSeconds;
   }

   public void updateXAResources(String var1, String[] var2) {
      ServerCoordinatorDescriptor var3 = this.getOrCreate(var1);
      if (var3 != null) {
         updateSCDXAResources(var3, nameArray2XAResourceDescriptorSet(var2));
      }

   }

   public void updateNonXAResources(String var1, String[] var2) {
      ServerCoordinatorDescriptor var3 = this.getOrCreate(var1);
      if (var3 != null) {
         updateSCDNonXAResources(var3, nameArray2NonXAResourceDescriptorSet(var2));
      }

   }

   private static void updateSCDXAResources(ServerCoordinatorDescriptor var0, Set var1) {
      Set var2 = var0.getXAResources();
      var0.updateXAResources(var1);
      updateCache(var2, var1, var0);
   }

   private static void updateSCDNonXAResources(ServerCoordinatorDescriptor var0, Set var1) {
      Set var2 = var0.getNonXAResources();
      var0.updateNonXAResources(var1);
      updateCache(var2, var1, var0);
   }

   private static final void updateCache(Set var0, Set var1, ServerCoordinatorDescriptor var2) {
      Set var3 = complementOfXInY(var0, var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         ResourceDescriptor var5 = (ResourceDescriptor)var4.next();
         var5.addSC(var2);
         String var6 = var5.getName();
         Object var7;
         if (var5 instanceof XAResourceDescriptor) {
            var7 = (Set)xaResourceToServers.get(var6);
            if (var7 == null) {
               var7 = new ArraySet();
               xaResourceToServers.put(var6, var7);
            }

            ((Set)var7).add(var2);
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.updateCache() XA resource " + var6 + " now registered with " + var2);
            }
         } else {
            var7 = (Set)nonXAResourceToServers.get(var6);
            if (var7 == null) {
               var7 = new ArraySet();
               nonXAResourceToServers.put(var6, var7);
            }

            ((Set)var7).add(var2);
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.updateCache() non-XA resource " + var6 + " now registered with " + var2);
            }
         }
      }

      Set var10 = complementOfXInY(var1, var0);
      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         ResourceDescriptor var12 = (ResourceDescriptor)var11.next();
         var12.removeSC(var2);
         String var8 = var12.getName();
         Set var9;
         if (var12 instanceof XAResourceDescriptor) {
            var9 = (Set)xaResourceToServers.get(var8);
            if (var9 != null) {
               var9.remove(var2);
               if (var9.size() == 0 && TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.updateCache() XA resource " + var8 + " no longer registered with " + var2);
               }
            }
         } else {
            var9 = (Set)nonXAResourceToServers.get(var8);
            if (var9 != null) {
               var9.remove(var2);
               if (var9.size() == 0 && TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.updateCache() non-XA resource " + var8 + " no longer registered with " + var2);
               }
            }
         }
      }

   }

   private static final Set complementOfXInY(Set var0, Set var1) {
      ArraySet var2 = new ArraySet();
      if (var1 == null) {
         return var2;
      } else if (var0 == null) {
         return var1;
      } else {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (!var0.contains(var4)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   private final ServerCoordinatorDescriptor getOrCreate(String var1, ServerMBean var2) {
      if (var1 == null) {
         return null;
      } else {
         String var3 = CoordinatorDescriptor.getServerID(var1);
         if (var3 == null) {
            return null;
         } else if (this.isLocalServer(var3)) {
            return this.getLocalCoordinatorDescriptor();
         } else {
            synchronized(cacheLock) {
               ServerCoordinatorDescriptor var5;
               if (!this.isKnownServer(var3)) {
                  if (TxDebug.JTANaming.isDebugEnabled()) {
                     TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getOrCreate(" + var1 + ")");
                  }

                  if (TxDebug.JTANamingStackTrace.isDebugEnabled()) {
                     TxDebug.debugStack(TxDebug.JTANaming, "ServerCoordinatorDescriptor.getOrCreate(" + var1 + ")");
                  }

                  var5 = new ServerCoordinatorDescriptor(var1);
                  synchronized(cacheLock) {
                     lostServers.put(var3, var5);
                  }

                  this.scheduleRefresh(var5);
                  return var5;
               }

               var5 = (ServerCoordinatorDescriptor)activeServers.get(var3);
               if (var5 != null) {
                  return var5;
               }

               var5 = (ServerCoordinatorDescriptor)lostServers.get(var3);
               if (var5 != null) {
                  this.scheduleRefresh(var5);
                  return var5;
               }
            }

            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getOrCreate(): known server but not active or lost");
            }

            return null;
         }
      }
   }

   private boolean isKnownServer(String var1) {
      synchronized(cacheLock) {
         if (activeServers.get(var1) != null) {
            return true;
         } else {
            return lostServers.get(var1) != null;
         }
      }
   }

   static final ServerCoordinatorDescriptor[] getServersHostingXAResource(String var0) {
      synchronized(cacheLock) {
         Set var2 = (Set)xaResourceToServers.get(var0);
         return var2 != null && var2.size() != 0 ? (ServerCoordinatorDescriptor[])((ServerCoordinatorDescriptor[])var2.toArray(new ServerCoordinatorDescriptor[var2.size()])) : null;
      }
   }

   private static ServerCoordinatorDescriptor[] getServersHostingNonXAResource(String var0) {
      synchronized(cacheLock) {
         Set var2 = (Set)nonXAResourceToServers.get(var0);
         return var2 != null && var2.size() != 0 ? (ServerCoordinatorDescriptor[])((ServerCoordinatorDescriptor[])var2.toArray(new ServerCoordinatorDescriptor[var2.size()])) : null;
      }
   }

   private static final String getHost(ServerMBean var0) throws Exception {
      String var1 = var0.getListenAddress();
      if (var1 != null && !var1.equalsIgnoreCase("localhost") && !var1.equals("127.0.0.1")) {
         return var1;
      } else {
         throw new Exception("Unable to get runtime listen address.Server " + var0.getName() + " configured with localhost");
      }
   }

   private static String getCoordinatorURL(String var0, String var1) throws URISyntaxException {
      String var2 = null;

      try {
         ProtocolService var3 = (new ProtocolServiceImpl()).getProtocolService();
         if (((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode() != 1 && !PlatformHelper.getPlatformHelper().isCDSEnabled()) {
            var2 = var3.findAdministrationURL(var1);
         } else {
            var2 = var3.findURL(var1, var3.getDefaultProtocol());
         }

         if (var2 == null) {
            var2 = var3.findURL(var1, var3.getDefaultSecureProtocol());
         }
      } catch (UnknownHostException var7) {
         return null;
      }

      URI var8 = new URI(var2);
      String var4 = var8.getScheme();
      int var5 = var8.getPort();
      String var6 = var8.getHost();
      return CoordinatorDescriptor.getCoordinatorURL(var6 + ":" + var5, var0, var1, var4);
   }

   private static String getCoordinatorURL(ServerMBean var0) throws Exception {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getName();
         if (var1 == null) {
            throw new Exception("Unable to obtain the server name");
         } else {
            int var2;
            String var3;
            String var4;
            try {
               ProtocolService var6 = (new ProtocolServiceImpl()).getProtocolService();
               String var5;
               if (((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode() != 1 && !PlatformHelper.getPlatformHelper().isCDSEnabled()) {
                  var5 = var6.findAdministrationURL(var1);
               } else {
                  var5 = var6.findURL(var1, var6.getDefaultProtocol());
               }

               if (var5 == null) {
                  var5 = var6.findURL(var1, var6.getDefaultSecureProtocol());
               }

               URI var10 = new URI(var5);
               var3 = var10.getScheme();
               var2 = var10.getPort();
               var4 = var10.getHost();
            } catch (UnknownHostException var8) {
               var4 = getHost(var0);
               if (var0.isListenPortEnabled()) {
                  var2 = var0.getListenPort();
                  var3 = var0.getDefaultProtocol();
               } else {
                  SSLMBean var7 = var0.getSSL();
                  if (var7 == null) {
                     throw new Exception("Unable to obtain the SSL listen port of the server: no sslMBean");
                  }

                  if (!var7.isEnabled()) {
                     throw new Exception("SSL listen port is not configured on the server");
                  }

                  var2 = var7.getListenPort();
                  var3 = var0.getDefaultSecureProtocol();
               }
            }

            String var9 = ManagementService.getRuntimeAccess(kernelID).getDomainName();
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getCoordinatorURL() " + var4 + ":" + var2 + "+" + var9 + "+" + var1 + "+" + var3);
            }

            return CoordinatorDescriptor.getCoordinatorURL(var4 + ":" + var2, var9, var1, var3);
         }
      }
   }

   private final boolean isLocalServer(String var1) {
      String var2;
      try {
         var2 = this.getLocalCoordinatorURL();
      } catch (Exception var4) {
         return false;
      }

      String var3 = CoordinatorDescriptor.getServerID(var2);
      return var3 != null && var3.equals(var1);
   }

   private final void scheduleRefresh(final ServerCoordinatorDescriptor var1) {
      if (TxDebug.JTANaming.isDebugEnabled()) {
         TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.scheduleRefresh(" + var1.getCoordinatorURL() + ")");
      }

      synchronized(this) {
         if (var1.isRefreshScheduled() || var1.isRefreshInProgress()) {
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.scheduleRefresh(" + var1.getCoordinatorURL() + "): lookup in progress");
            }

            return;
         }

         long var3 = System.currentTimeMillis() - var1.getLastRefreshTime();
         if (var3 < 5000L) {
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.scheduleRefresh(): timeSinceLastRefresh = " + var3 + " is less than server refresh interval of " + 5000);
            }

            return;
         }

         var1.setRefreshScheduled(true);
      }

      WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
         public void run() {
            try {
               String var1x = var1.getCoordinatorURL();
               AuthenticatedSubject var10000 = ServerCoordinatorDescriptorManagerImpl.kernelID;
               RefreshServerAction var10001 = ServerCoordinatorDescriptorManagerImpl.this.new RefreshServerAction(var1);
               ServerCoordinatorDescriptor var10002 = var1;
               SecureAction.runAction(var10000, var10001, ServerCoordinatorDescriptor.getServerURL(var1x), "refreshServer");
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         }
      });
   }

   private static final Set nameArray2XAResourceDescriptorSet(Object[] var0) {
      ArraySet var1 = new ArraySet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(XAResourceDescriptor.getOrCreate((String)var0[var2]));
      }

      return var1;
   }

   private static final Set nameArray2NonXAResourceDescriptorSet(Object[] var0) {
      ArraySet var1 = new ArraySet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(NonXAResourceDescriptor.getOrCreate((String)var0[var2]));
      }

      return var1;
   }

   private final boolean inLocalDomain(CoordinatorDescriptor var1) {
      return getLocalDomain().equals(var1.getDomainName());
   }

   private static final String getLocalDomain() {
      return ManagementService.getRuntimeAccess(kernelID).getDomain().getName();
   }

   private static final void lostServer(String var0) {
      try {
         synchronized(cacheLock) {
            ServerCoordinatorDescriptor var2 = (ServerCoordinatorDescriptor)activeServers.remove(var0);
            if (var2 == null) {
               return;
            }

            lostServers.put(var0, var2);
            updateSCDXAResources(var2, new ArraySet());
            updateSCDNonXAResources(var2, new ArraySet());
         }
      } catch (Exception var5) {
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.lostServer() ", var5);
         }
      }

   }

   private static final ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager();
   }

   public ServerCoordinatorDescriptor[] getActiveServers() {
      synchronized(cacheLock) {
         return (ServerCoordinatorDescriptor[])((ServerCoordinatorDescriptor[])activeServers.values().toArray(new ServerCoordinatorDescriptor[activeServers.size()]));
      }
   }

   private class GetSubCoordinatorInfoAction implements PrivilegedExceptionAction {
      private String coUrl;
      private Map infoMap;
      private SubCoordinator3 sc;

      GetSubCoordinatorInfoAction(SubCoordinator3 var2, String var3) {
         this.sc = var2;
         this.infoMap = null;
         this.coUrl = var3;
      }

      public Object run() throws Exception {
         this.infoMap = this.sc.getSubCoordinatorInfo(this.coUrl);
         return null;
      }

      public Map getMap() {
         return this.infoMap;
      }
   }

   private final class RefreshServerAction implements PrivilegedExceptionAction {
      private final ServerCoordinatorDescriptor coordinatorDescriptor;

      RefreshServerAction(ServerCoordinatorDescriptor var2) {
         this.coordinatorDescriptor = var2;
      }

      public Object run() throws Exception {
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ")");
         }

         TransactionImpl var2 = null;
         synchronized(this) {
            if (this.coordinatorDescriptor.isRefreshInProgress()) {
               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") refresh in progress");
               }

               return null;
            }

            this.coordinatorDescriptor.setRefreshInProgress(true);
         }

         boolean var21 = false;

         Object var4;
         label343: {
            label344: {
               try {
                  label308: {
                     try {
                        var21 = true;
                        var2 = ServerCoordinatorDescriptorManagerImpl.getTM().internalSuspend();
                        SubCoordinator3 var3 = this.getSubCoordinator3();
                        if (var3 == null) {
                           break label308;
                        }

                        this.doPeerExchange(var3);
                        var4 = null;
                     } finally {
                        ServerCoordinatorDescriptorManagerImpl.getTM().internalResume(var2);
                     }

                     var21 = false;
                     break label343;
                  }

                  var21 = false;
               } catch (Exception var35) {
                  if (TxDebug.JTANaming.isDebugEnabled()) {
                     TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") Exception: ", var35);
                     var21 = false;
                  } else {
                     var21 = false;
                  }
                  break label344;
               } finally {
                  if (var21) {
                     synchronized(this) {
                        this.coordinatorDescriptor.setRefreshInProgress(false);
                        this.coordinatorDescriptor.setRefreshScheduled(false);
                        this.coordinatorDescriptor.setLastRefreshTime(System.currentTimeMillis());
                        this.notifyAll();
                     }

                     if (TxDebug.JTANaming.isDebugEnabled()) {
                        TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") completed lookup");
                     }

                  }
               }

               synchronized(this) {
                  this.coordinatorDescriptor.setRefreshInProgress(false);
                  this.coordinatorDescriptor.setRefreshScheduled(false);
                  this.coordinatorDescriptor.setLastRefreshTime(System.currentTimeMillis());
                  this.notifyAll();
               }

               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") completed lookup");
               }

               return null;
            }

            synchronized(this) {
               this.coordinatorDescriptor.setRefreshInProgress(false);
               this.coordinatorDescriptor.setRefreshScheduled(false);
               this.coordinatorDescriptor.setLastRefreshTime(System.currentTimeMillis());
               this.notifyAll();
            }

            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") completed lookup");
            }

            return null;
         }

         synchronized(this) {
            this.coordinatorDescriptor.setRefreshInProgress(false);
            this.coordinatorDescriptor.setRefreshScheduled(false);
            this.coordinatorDescriptor.setLastRefreshTime(System.currentTimeMillis());
            this.notifyAll();
         }

         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.refreshServer(" + this.coordinatorDescriptor.getCoordinatorURL() + ") completed lookup");
         }

         return var4;
      }

      private SubCoordinator3 getSubCoordinator3() {
         Object var1 = JNDIAdvertiser.getCachedCoordinator(this.coordinatorDescriptor, (TransactionImpl)null);
         if (var1 == null && ServerCoordinatorDescriptorManagerImpl.this.inLocalDomain(this.coordinatorDescriptor)) {
            String var2;
            try {
               var2 = ServerCoordinatorDescriptorManagerImpl.getCoordinatorURL(this.coordinatorDescriptor.getDomainName(), this.coordinatorDescriptor.getServerName());
            } catch (URISyntaxException var4) {
               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getSubCoordinator3() Invalid URI: " + var4);
               }

               return null;
            }

            if (var2 == null) {
               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getSubCoordinator3() no URL found for " + this.coordinatorDescriptor.getServerID());
               }

               return null;
            }

            if (!CoordinatorDescriptor.getServerURL(var2).equals(this.coordinatorDescriptor.serverURL)) {
               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.getSubCoordinator3() server " + this.coordinatorDescriptor.getServerID() + " has moved to " + var2);
               }

               this.coordinatorDescriptor.init(var2);
            }
         }

         var1 = JNDIAdvertiser.getCachedCoordinator(this.coordinatorDescriptor, (TransactionImpl)null);
         return var1 != null && var1 instanceof SubCoordinator3 && var1 instanceof NotificationBroadcaster ? (SubCoordinator3)var1 : null;
      }

      private void doPeerExchange(SubCoordinator3 var1) throws Exception {
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.doPeerExchange()");
         }

         String var2 = ServerCoordinatorDescriptorManagerImpl.this.getLocalCoordinatorURL();
         String var3 = this.coordinatorDescriptor.getCoordinatorURL();
         ServerCoordinatorDescriptor var10000 = this.coordinatorDescriptor;
         String var4 = ServerCoordinatorDescriptor.getServerURL(var3);
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.doPeerExchange()serverURL= " + var4);
         }

         GetSubCoordinatorInfoAction var5 = ServerCoordinatorDescriptorManagerImpl.this.new GetSubCoordinatorInfoAction(var1, var2);
         SecureAction.runAction(ServerCoordinatorDescriptorManagerImpl.kernelID, var5, var4, "sc3.getSubCoordinatorInfo");
         Map var6 = var5.getMap();
         if (var6 == null) {
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.doPeerExchange()  peer info null");
            }

         } else {
            SubCoordinatorInfo var7 = new SubCoordinatorInfo(var6);
            this.coordinatorDescriptor.init(var7.getCoordinatorURL());
            this.coordinatorDescriptor.setSSLOnly(var7.isSslOnly());
            NotificationBroadcaster var8 = (NotificationBroadcaster)var1;
            var8.addNotificationListener((CoordinatorImpl)ServerCoordinatorDescriptorManagerImpl.getTM().getLocalCoordinator(), this.coordinatorDescriptor.getCoordinatorURL());
            if (var1 instanceof Remote) {
               DisconnectMonitor var9 = DisconnectMonitorListImpl.getDisconnectMonitor();

               try {
                  var9.addDisconnectListener(var1, new SubCoordinatorDisconnectListener(this.coordinatorDescriptor.getCoordinatorURL(), this.coordinatorDescriptor.getServerID()));
               } catch (DisconnectMonitorUnavailableException var13) {
               }
            }

            synchronized(ServerCoordinatorDescriptorManagerImpl.cacheLock) {
               ServerCoordinatorDescriptorManagerImpl.updateSCDXAResources(this.coordinatorDescriptor, ServerCoordinatorDescriptorManagerImpl.nameArray2XAResourceDescriptorSet(var7.getRegisteredXAResources()));
               ServerCoordinatorDescriptorManagerImpl.updateSCDNonXAResources(this.coordinatorDescriptor, ServerCoordinatorDescriptorManagerImpl.nameArray2NonXAResourceDescriptorSet(var7.getRegisteredNonXAResources()));
               ServerCoordinatorDescriptorManagerImpl.activeServers.put(this.coordinatorDescriptor.getServerID(), this.coordinatorDescriptor);
               ServerCoordinatorDescriptorManagerImpl.lostServers.remove(this.coordinatorDescriptor.getServerID());
            }

            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.process90Exchange() completed lookup for " + this.coordinatorDescriptor.getCoordinatorURL());
            }

            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.doPeerExchange() initialized");
            }

         }
      }

      class SubCoordinatorDisconnectListener implements DisconnectListener {
         String url;
         String id;

         SubCoordinatorDisconnectListener(String var2, String var3) {
            this.url = var2;
            this.id = var3;
         }

         public void onDisconnect(DisconnectEvent var1) {
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("ServerCoordinatorDescriptor.RefreshServerAction.DisconnectListenerImpl.onDisconnect(" + this.url + ")");
            }

            ServerCoordinatorDescriptorManagerImpl.lostServer(this.id);
         }
      }
   }

   private static final class SCDMaker {
      private static ServerCoordinatorDescriptor SELF;

      static {
         try {
            if (Kernel.isServer()) {
               String var0 = ServerCoordinatorDescriptorManagerImpl.getCoordinatorURL(ManagementService.getRuntimeAccess(ServerCoordinatorDescriptorManagerImpl.kernelID).getServer());
               if (TxDebug.JTANaming.isDebugEnabled()) {
                  TxDebug.JTANaming.debug("ServerCoordinatorDescriptor(): local server " + var0);
               }

               if (PlatformHelper.getPlatformHelper().extendCoordinatorURL(var0) && !PlatformHelper.getPlatformHelper().isCDSEnabled()) {
                  SELF = new ServerCoordinatorDescriptor(var0, CoordinatorDescriptor.getPort(var0));
               } else {
                  if (((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode() != 2) {
                     ((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).setInteropModeToVal(1);
                  }

                  SELF = new ServerCoordinatorDescriptor(var0);
               }
            }
         } catch (Exception var1) {
            TXLogger.logLocalCoordinatorDescriptorError(var1);
         }

      }
   }

   private static final class CacheLock {
      private CacheLock() {
      }

      // $FF: synthetic method
      CacheLock(Object var1) {
         this();
      }
   }
}
