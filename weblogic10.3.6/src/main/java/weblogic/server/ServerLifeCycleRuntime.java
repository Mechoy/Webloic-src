package weblogic.server;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.ManagementRuntimeException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.ServerStartMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.ServerStates;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.nodemanager.mbean.NodeManagerTask;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.ServerIdentityManager;
import weblogic.protocol.URLManager;
import weblogic.protocol.UnknownProtocolException;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentMapAsyncTX;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.ContextWrap;
import weblogic.work.WorkManagerFactory;

public final class ServerLifeCycleRuntime extends DomainRuntimeMBeanDelegate implements ServerLifeCycleRuntimeMBean, ServerStates {
   private static final int TASK_AFTERLIFE_TIME_MILLIS = 1800000;
   private static final String SERVER_LIFECYCLE_STORE_NAME = "weblogic.server.lifecycle.store";
   private static final DebugCategory debug = Debug.getCategory("weblogic.slcruntime");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Set tasks = Collections.synchronizedSet(new HashSet());
   private final String serverName;
   private final ServerMBean serverMBean;
   private String oldState;
   private String currentState;
   private int startCount;
   private boolean stateShouldBeAvailable;
   private transient String cachedBulkQueryState;
   private static final PersistentMap pMap = initStore();
   private ServerProperties mCachedValue;
   private MachineMBean lastKnownMachine;

   ServerLifeCycleRuntime(ServerMBean var1) throws ManagementException {
      super(var1.getName());
      this.serverMBean = var1;
      this.serverName = var1.getName();
   }

   public ServerMBean getServerMBean() {
      return this.serverMBean;
   }

   static void cleanupStore(ServerMBean[] var0) {
      if (pMap != null) {
         try {
            Set var1 = pMap.keySet();
            ArrayList var2 = new ArrayList();
            var2.addAll(var1);
            if (var0 != null && var0.length > 0) {
               for(int var3 = 0; var3 < var0.length; ++var3) {
                  if (var0[var3] != null) {
                     var2.remove(var0[var3].getName());
                  }
               }
            }

            String var4;
            for(Iterator var6 = var2.iterator(); var6.hasNext(); pMap.remove(var4)) {
               var4 = (String)var6.next();
               if (debug.isEnabled()) {
                  Debug.say("ServerLifeCycleRuntime.cleanupStore() removing entry for " + var4);
               }
            }
         } catch (PersistentStoreException var5) {
            if (debug.isEnabled()) {
               Debug.say(var5.getMessage());
            }
         }

      }
   }

   private static PersistentMap initStore() {
      PersistentMapAsyncTX var0 = null;
      PersistentStore var1 = PersistentStoreManager.getManager().getDefaultStore();
      if (var1 == null) {
         if (debug.isEnabled()) {
            Debug.say("The default persistent store cannot be found.");
         }
      } else {
         try {
            var0 = var1.createPersistentMap("weblogic.server.lifecycle.store");
            if (debug.isEnabled()) {
               Set var2 = var0.keySet();
               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  Object var4 = var3.next();
                  Debug.say("--  pMap contains (" + var4 + ", " + var0.get(var4) + ")");
               }
            }
         } catch (PersistentStoreException var5) {
            if (debug.isEnabled()) {
               Debug.say(var5.getMessage());
            }
         }
      }

      return var0;
   }

   void cleanup() {
      if (pMap != null) {
         try {
            pMap.remove(this.getName());
         } catch (PersistentStoreException var2) {
            if (debug.isEnabled()) {
               Debug.say(var2.getMessage());
            }
         }
      }

   }

   private void saveStore(ServerProperties var1) {
      if (pMap != null) {
         try {
            pMap.put(this.getName(), var1);
            if (debug.isEnabled()) {
               Debug.say("saveStore() adding to pMap(" + this.getName() + ", " + var1 + ") pMap size is " + pMap.size());
               Set var2 = pMap.keySet();
               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  Object var4 = var3.next();
                  Debug.say("--  pMap contains (" + var4 + ", " + pMap.get(var4) + ")");
               }
            }

            this.mCachedValue = var1;
         } catch (PersistentStoreException var5) {
            if (debug.isEnabled()) {
               Debug.say(var5.getMessage());
            }
         }
      }

   }

   private ServerProperties getFromStore() {
      if (this.mCachedValue == null && pMap != null) {
         try {
            this.mCachedValue = (ServerProperties)pMap.get(this.getName());
            if (debug.isEnabled()) {
               Debug.say("getFromStore() got from pMap(" + this.getName() + ", " + this.mCachedValue + ")");
            }
         } catch (PersistentStoreException var2) {
            if (debug.isEnabled()) {
               Debug.say(var2.getMessage());
            }
         }
      }

      return this.mCachedValue;
   }

   public ServerLifeCycleTaskRuntimeMBean start() throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var2;
      try {
         ServerLifeCycleTaskRuntime var1 = new ServerLifeCycleTaskRuntime(this, "Starting " + this.serverName + " server ...", "start");
         this.tasks.add(var1);
         this.currentState = null;
         this.startServer(var1, (String)null);
         updateTaskMBeanOnCompletion(var1);
         var2 = var1;
      } catch (ManagementException var7) {
         throw new ServerLifecycleException(var7);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var2;
   }

   public ServerLifeCycleTaskRuntimeMBean start(String var1) throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var14;
      try {
         boolean var3 = false;
         MachineMBean var4 = this.serverMBean.getMachine();
         if (var4 != null && var4.getName().equals(var1)) {
            var3 = true;
         }

         if (!var3) {
            MachineMBean[] var5 = this.serverMBean.getCandidateMachines();
            if (var5 != null && var5.length > 0) {
               var3 = this.isMachineListed(var1, var5);
            } else {
               ClusterMBean var6 = this.serverMBean.getCluster();
               if (var6 != null) {
                  MachineMBean[] var7 = var6.getCandidateMachinesForMigratableServers();
                  if (var7 != null && var7.length > 0) {
                     var3 = this.isMachineListed(var1, var7);
                  }
               }
            }
         }

         if (!var3) {
            throw new ServerLifecycleException("Invalid machine name or server not configured to run on this machine");
         }

         ServerLifeCycleTaskRuntime var2 = new ServerLifeCycleTaskRuntime(this, "Starting " + this.serverName + " server ...", "start");
         this.tasks.add(var2);
         this.currentState = null;
         this.startServer(var2, var1);
         updateTaskMBeanOnCompletion(var2);
         var14 = var2;
      } catch (ManagementException var12) {
         throw new ServerLifecycleException(var12);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var14;
   }

   private boolean isMachineListed(String var1, MachineMBean[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public ServerLifeCycleTaskRuntimeMBean startInStandby() throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var2;
      try {
         ServerLifeCycleTaskRuntime var1 = new ServerLifeCycleTaskRuntime(this, "Starting " + this.serverName + " server in standby mode ...", "startInStandby");
         this.tasks.add(var1);
         this.currentState = null;
         this.startInStandbyServer(var1);
         updateTaskMBeanOnCompletion(var1);
         var2 = var1;
      } catch (ManagementException var7) {
         throw new ServerLifecycleException(var7);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var2;
   }

   public ServerLifeCycleTaskRuntimeMBean resume() throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var3;
      try {
         ServerLifeCycleTaskRuntime var1 = new ServerLifeCycleTaskRuntime(this, "Resuming " + this.serverName + " server ...", "resume");
         this.tasks.add(var1);
         ResumeRequest var2 = new ResumeRequest(var1);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var2));
         var3 = var1;
      } catch (ManagementException var8) {
         throw new ServerLifecycleException(var8);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var3;
   }

   public ServerLifeCycleTaskRuntimeMBean shutdown() throws ServerLifecycleException {
      return this.shutdown(0, false);
   }

   public ServerLifeCycleTaskRuntimeMBean shutdown(int var1, boolean var2) throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var5;
      try {
         this.logAdministratorAddress("Graceful shutdown");
         if (this.getState() == "SHUTDOWN") {
            throw new ServerLifecycleException("Can not get to the relevant ServerRuntimeMBean for server " + this.serverName + ". Server is in SHUTDOWN state and cannot be reached.");
         }

         ServerLifeCycleTaskRuntime var3 = new ServerLifeCycleTaskRuntime(this, "Shutting down " + this.serverName + " server ...", "shutdown");
         this.tasks.add(var3);
         ShutdownRequest var4 = new ShutdownRequest(var1, var2, var3);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var4));
         var5 = var3;
      } catch (ManagementException var10) {
         throw new ServerLifecycleException(var10);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var5;
   }

   public ServerLifeCycleTaskRuntimeMBean forceShutdown() throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var3;
      try {
         this.logAdministratorAddress("Force shutdown");
         if (this.getState() == "SHUTDOWN") {
            throw new ServerLifecycleException("Can not get to the relevant ServerRuntimeMBean for server " + this.serverName + ". Server is in SHUTDOWN state and cannot be reached.");
         }

         ServerLifeCycleTaskRuntime var1 = new ServerLifeCycleTaskRuntime(this, "Forcefully shutting down " + this.serverName + " server ...", "forceShutdown");
         this.tasks.add(var1);
         ShutdownRequest var2 = new ShutdownRequest(var1);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var2));
         var3 = var1;
      } catch (ManagementException var8) {
         throw new ServerLifecycleException(var8);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var3;
   }

   public ServerLifeCycleTaskRuntimeMBean[] getTasks() {
      return (ServerLifeCycleTaskRuntimeMBean[])((ServerLifeCycleTaskRuntimeMBean[])this.tasks.toArray(new ServerLifeCycleTaskRuntimeMBean[this.tasks.size()]));
   }

   public ServerLifeCycleTaskRuntimeMBean suspend() throws ServerLifecycleException {
      return this.suspend(0, false);
   }

   public ServerLifeCycleTaskRuntimeMBean suspend(int var1, boolean var2) throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var5;
      try {
         this.logAdministratorAddress("Graceful suspend");
         ServerLifeCycleTaskRuntime var3 = new ServerLifeCycleTaskRuntime(this, "suspending " + this.serverName + " server ...", "suspendWithTimeout");
         this.tasks.add(var3);
         SuspendRequest var4 = new SuspendRequest(var1, var2, var3);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var4));
         var5 = var3;
      } catch (ManagementException var10) {
         throw new ServerLifecycleException(var10);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var5;
   }

   public ServerLifeCycleTaskRuntimeMBean forceSuspend() throws ServerLifecycleException {
      ServerLifeCycleTaskRuntime var3;
      try {
         this.logAdministratorAddress("Force suspend");
         ServerLifeCycleTaskRuntime var1 = new ServerLifeCycleTaskRuntime(this, "Forcefully suspending " + this.serverName + " server ...", "forceSuspend");
         this.tasks.add(var1);
         SuspendRequest var2 = new SuspendRequest(var1);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var2));
         var3 = var1;
      } catch (ManagementException var8) {
         throw new ServerLifecycleException(var8);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var3;
   }

   public void setState(String var1) {
      synchronized(this) {
         if (var1 == null || var1.equalsIgnoreCase(this.currentState)) {
            return;
         }

         this.oldState = this.currentState;
         this.currentState = var1;
         if ("RUNNING".equals(var1)) {
            this.saveStore(new ServerProperties(this));
         }

         if (!"SHUTTING_DOWN".equals(var1) && !"FORCE_SHUTTING_DOWN".equals(var1)) {
            if ("STARTING".equals(var1) || "RESUMING".equals(var1) || "RUNNING".equals(var1)) {
               this.stateShouldBeAvailable = true;
               this.saveLastKnownMachine();
            }
         } else {
            this.stateShouldBeAvailable = false;
         }
      }

      this._postSet("State", this.oldState, this.currentState);
   }

   public String getState() {
      String var1 = this.getStateRemote();
      if (var1 == null) {
         var1 = this.getStateNodeManager();
         if (var1 == null || "UNKNOWN".equalsIgnoreCase(var1)) {
            var1 = this.stateShouldBeAvailable ? "UNKNOWN" : "SHUTDOWN";
         }
      }

      this.clearOldServerLifeCycleTaskRuntimes();
      return var1;
   }

   public int getNodeManagerRestartCount() {
      return this.startCount > 0 ? this.startCount - 1 : 0;
   }

   public String getWeblogicHome() {
      RemoteLifeCycleOperations var1 = this.getLifeCycleOperationsRemote();
      if (var1 == null) {
         ServerProperties var2 = this.getFromStore();
         return var2 != null ? var2.getWeblogicHome() : null;
      } else {
         try {
            return var1.getWeblogicHome();
         } catch (RemoteException var3) {
            return null;
         }
      }
   }

   public String getMiddlewareHome() {
      RemoteLifeCycleOperations var1 = this.getLifeCycleOperationsRemote();
      if (var1 == null) {
         ServerProperties var2 = this.getFromStore();
         return var2 != null ? var2.getMiddlewareHome() : null;
      } else {
         try {
            return var1.getMiddlewareHome();
         } catch (RemoteException var3) {
            return null;
         }
      }
   }

   public String getIPv4URL(String var1) {
      ServerIdentity var2 = ServerIdentityManager.findServerIdentity(ManagementService.getRuntimeAccess(kernelId).getDomainName(), this.getName());
      String var3 = null;

      try {
         if (var2 != null) {
            var3 = URLManager.findIPv4URL(var2, ProtocolManager.findProtocol(var1));
         }

         if (var3 == null) {
            ServerProperties var4 = this.getFromStore();
            if (var4 != null) {
               var3 = var4.getIPv4URL(var1);
            }
         }
      } catch (UnknownProtocolException var5) {
      }

      return var3;
   }

   public String getIPv6URL(String var1) {
      ServerIdentity var2 = ServerIdentityManager.findServerIdentity(ManagementService.getRuntimeAccess(kernelId).getDomainName(), this.getName());
      String var3 = null;

      try {
         if (var2 != null) {
            var3 = URLManager.findIPv6URL(var2, ProtocolManager.findProtocol(var1));
         }

         if (var3 == null) {
            ServerProperties var4 = this.getFromStore();
            if (var4 != null) {
               var3 = var4.getIPv6URL(var1);
            }
         }
      } catch (UnknownProtocolException var5) {
      }

      return var3;
   }

   private String getStateRemote() {
      RemoteLifeCycleOperations var1 = this.getLifeCycleOperationsRemote();
      if (var1 == null) {
         return null;
      } else {
         try {
            return var1.getState();
         } catch (NullPointerException var3) {
         } catch (PeerGoneException var4) {
         } catch (ManagementRuntimeException var5) {
            if (!isNestedDueToShutdown(var5.getCause())) {
               throw new AssertionError(var5);
            }
         } catch (RemoteRuntimeException var6) {
            if (!isNestedDueToShutdown(var6.getNested())) {
               throw new AssertionError(var6);
            }
         } catch (RuntimeException var7) {
            throw new AssertionError(var7);
         } catch (RemoteException var8) {
            if (!isNestedDueToShutdown(var8.getCause())) {
               throw new AssertionError(var8);
            }
         }

         return null;
      }
   }

   private String getStateNodeManager() {
      MachineMBean var1 = this.getLastKnownMachine();
      if (var1 == null) {
         return null;
      } else {
         try {
            return NodeManagerRuntime.getInstance(var1).getState(this.serverMBean);
         } catch (IOException var3) {
            return null;
         }
      }
   }

   private static boolean isNestedDueToShutdown(Throwable var0) {
      return var0 instanceof IOException;
   }

   public int getStateVal() {
      String var1 = this.getState().intern();
      if (var1 != "STARTING" && var1 != "FAILED_RESTARTING") {
         if (var1 == "SHUTTING_DOWN") {
            return 7;
         } else if (var1 == "FORCE_SHUTTING_DOWN") {
            return 18;
         } else if (var1 == "STANDBY") {
            return 3;
         } else if (var1 == "ADMIN") {
            return 17;
         } else if (var1 == "SUSPENDING") {
            return 4;
         } else if (var1 == "RESUMING") {
            return 6;
         } else if (var1 == "RUNNING") {
            return 2;
         } else if (var1 == "SHUTDOWN") {
            return 0;
         } else if (var1 == "FAILED") {
            return 8;
         } else if (var1 == "ACTIVATE_LATER") {
            return 13;
         } else if (var1 == "FAILED_NOT_RESTARTABLE") {
            return 14;
         } else {
            return var1 == "FAILED_MIGRATABLE" ? 15 : 9;
         }
      } else {
         return 1;
      }
   }

   private void startServer(ServerLifeCycleTaskRuntime var1, String var2) {
      try {
         NodeManagerRuntime.checkStartPrivileges(this.serverName, SecurityServiceManager.getCurrentSubject(kernelId));
         NodeManagerRuntime var3 = null;
         if (var2 != null) {
            MachineMBean var4 = this.getMachine(var2);
            if (var2 == null) {
               var1.setError(new IOException("Unknown machine name"));
               return;
            }

            var3 = NodeManagerRuntime.getInstance(var4);
         } else {
            var3 = NodeManagerRuntime.getInstance(this.serverMBean);
         }

         NodeManagerTask var7 = var3.start(this.serverMBean);
         ++this.startCount;
         var1.setNMTask(var7);
      } catch (SecurityException var5) {
         var1.setError(var5);
      } catch (IOException var6) {
         var1.setError(var6);
      }

   }

   private MachineMBean getMachine(String var1) {
      MachineMBean[] var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getMachines();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   private void startInStandbyServer(ServerLifeCycleTaskRuntime var1) {
      this.startServer(var1, (String)null);
   }

   private static boolean isSecurityException(Exception var0) {
      Object var1;
      for(var1 = var0; ((Throwable)var1).getCause() != null; var1 = ((Throwable)var1).getCause()) {
      }

      return var1 instanceof SecurityException;
   }

   private boolean useNodeManagerToShutdown() throws IOException {
      ServerStartMBean var1 = this.serverMBean.getServerStart();
      if (var1 == null) {
         return false;
      } else {
         MachineMBean var2 = this.serverMBean.getMachine();
         if (var2 == null) {
            return false;
         } else {
            NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(var2);
            if (var3 == null) {
               return false;
            } else {
               var3.kill(this.serverMBean);
               return true;
            }
         }
      }
   }

   private static void updateTaskMBeanOnCompletion(ServerLifeCycleTaskRuntime var0) {
      if (var0.getError() != null) {
         var0.setStatus("FAILED");
      } else {
         var0.setStatus("TASK COMPLETED");
      }

      var0.setEndTime(System.currentTimeMillis());
      var0.setIsRunning(false);
   }

   public String getBulkQueryState() {
      return this.cachedBulkQueryState;
   }

   public void setBulkQueryState(String var1) {
      this.cachedBulkQueryState = var1;
   }

   public void clearOldServerLifeCycleTaskRuntimes() {
      synchronized(this.tasks) {
         Iterator var2 = this.tasks.iterator();

         while(var2.hasNext()) {
            ServerLifeCycleTaskRuntime var3 = (ServerLifeCycleTaskRuntime)var2.next();
            if (var3.getEndTime() > 0L && System.currentTimeMillis() - var3.getEndTime() > 1800000L) {
               try {
                  var3.unregister();
               } catch (ManagementException var6) {
               }

               var2.remove();
            }
         }

      }
   }

   private RemoteLifeCycleOperations getLifeCycleOperationsRemote() {
      return getLifeCycleOperationsRemote(this.serverName);
   }

   public static RemoteLifeCycleOperations getLifeCycleOperationsRemote(String var0) {
      try {
         Environment var1 = new Environment();
         String var2 = URLManager.findAdministrationURL(var0);
         var1.setProviderUrl(var2);
         if (debug.isEnabled()) {
            Debug.say("Looking up RemoteLifeCycleOperations with URL " + var2);
         }

         RemoteLifeCycleOperations var3 = (RemoteLifeCycleOperations)PortableRemoteObject.narrow(var1.getInitialReference(RemoteLifeCycleOperationsImpl.class), RemoteLifeCycleOperations.class);
         return var3;
      } catch (UnknownHostException var4) {
         ServerLogger.logLookupSLCOperations(var0, var4);
         return null;
      } catch (NamingException var5) {
         ServerLogger.logLookupSLCOperations(var0, var5);
         return null;
      }
   }

   private void logAdministratorAddress(String var1) {
      EndPoint var2 = ServerHelper.getClientEndPointInternal();
      if (var2 != null) {
         Channel var3 = var2.getRemoteChannel();
         if (var3 != null) {
            ServerLogger.logAdminAddress(var1 + " of " + this.serverName, var3.getInetAddress().getHostAddress());
         }
      }
   }

   private ServerRuntimeMBean getServerRuntimeMBean() {
      DomainRuntimeServiceMBean var1 = ManagementService.getDomainAccess(kernelId).getDomainRuntimeService();
      return var1.lookupServerRuntime(this.serverMBean.getName());
   }

   private void saveLastKnownMachine() {
      ServerRuntimeMBean var1 = this.getServerRuntimeMBean();
      if (var1 != null) {
         this.lastKnownMachine = this.getMachine(var1.getCurrentMachine());
      }

   }

   private MachineMBean getLastKnownMachine() {
      return this.lastKnownMachine != null ? this.lastKnownMachine : this.serverMBean.getMachine();
   }

   static class ServerProperties implements Serializable {
      private String mWeblogicHome;
      private String mMiddlewareHome;
      private HashMap<String, String> mIPv4URLs;
      private HashMap<String, String> mIPv6URLs;

      ServerProperties(ServerLifeCycleRuntime var1) {
         this.mWeblogicHome = var1.getWeblogicHome();
         this.mMiddlewareHome = var1.getMiddlewareHome();
         String[] var2 = ProtocolManager.getProtocols();
         if (var2 != null && var2.length > 0) {
            this.mIPv4URLs = new HashMap();
            this.mIPv6URLs = new HashMap();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = var2[var3];
               String var5 = var1.getIPv4URL(var4);
               String var6 = var1.getIPv6URL(var4);
               if (var5 != null) {
                  this.mIPv4URLs.put(var4, var5);
               }

               if (var6 != null) {
                  this.mIPv6URLs.put(var4, var6);
               }
            }
         }

      }

      public String getWeblogicHome() {
         return this.mWeblogicHome;
      }

      public String getMiddlewareHome() {
         return this.mMiddlewareHome;
      }

      public String getIPv4URL(String var1) {
         String var2 = null;
         if (this.mIPv4URLs != null) {
            var2 = (String)this.mIPv4URLs.get(var1);
         }

         return var2;
      }

      public String getIPv6URL(String var1) {
         String var2 = null;
         if (this.mIPv6URLs != null) {
            var2 = (String)this.mIPv6URLs.get(var1);
         }

         return var2;
      }

      public String toString() {
         return "ServerProperties(weblogicHome: " + this.mWeblogicHome + ", middlewareHome: " + this.mMiddlewareHome + ")";
      }
   }

   private final class ResumeRequest implements Runnable {
      private final ServerLifeCycleTaskRuntime taskMBean;

      ResumeRequest(ServerLifeCycleTaskRuntime var2) {
         this.taskMBean = var2;
      }

      public void run() {
         try {
            RemoteLifeCycleOperations var1 = ServerLifeCycleRuntime.this.getLifeCycleOperationsRemote();
            if (var1 == null) {
               throw new ServerLifecycleException("Can not get to the relevant ServerRuntimeMBean");
            }

            var1.resume();
         } catch (Exception var6) {
            this.taskMBean.setError(var6);
         } finally {
            ServerLifeCycleRuntime.updateTaskMBeanOnCompletion(this.taskMBean);
         }

      }
   }

   private final class SuspendRequest implements Runnable {
      private int timeout;
      private final boolean forceSuspend;
      private boolean ignoreSessions;
      private final ServerLifeCycleTaskRuntime taskMBean;

      SuspendRequest(int var2, boolean var3, ServerLifeCycleTaskRuntime var4) {
         this.timeout = var2;
         this.ignoreSessions = var3;
         this.forceSuspend = false;
         this.taskMBean = var4;
      }

      SuspendRequest(ServerLifeCycleTaskRuntime var2) {
         this.forceSuspend = true;
         this.taskMBean = var2;
      }

      public void run() {
         try {
            RemoteLifeCycleOperations var1 = ServerLifeCycleRuntime.this.getLifeCycleOperationsRemote();
            if (var1 == null) {
               throw new ServerLifecycleException("Can not get to the relevant ServerRuntimeMBean");
            }

            if (this.forceSuspend) {
               var1.forceSuspend();
            } else {
               var1.suspend(this.timeout, this.ignoreSessions);
            }
         } catch (Exception var6) {
            this.taskMBean.setError(var6);
         } finally {
            ServerLifeCycleRuntime.updateTaskMBeanOnCompletion(this.taskMBean);
         }

      }
   }

   private final class ShutdownRequest implements Runnable {
      private int timeout;
      private final boolean forceShutdown;
      private boolean ignoreSessions;
      private final ServerLifeCycleTaskRuntime taskMBean;

      ShutdownRequest(int var2, boolean var3, ServerLifeCycleTaskRuntime var4) {
         this.timeout = var2;
         this.ignoreSessions = var3;
         this.forceShutdown = false;
         this.taskMBean = var4;
      }

      ShutdownRequest(ServerLifeCycleTaskRuntime var2) {
         this.forceShutdown = true;
         this.taskMBean = var2;
      }

      public void run() {
         try {
            RemoteLifeCycleOperations var13 = ServerLifeCycleRuntime.this.getLifeCycleOperationsRemote();
            if (var13 == null) {
               Loggable var14 = ServerLogger.logRemoteServerLifeCycleRuntimeNotFoundLoggable(this.taskMBean.getServerName());
               throw new ServerLifecycleException(var14.getMessageText());
            }

            if (this.forceShutdown) {
               var13.forceShutdown();
            } else {
               var13.shutdown(this.timeout, this.ignoreSessions);
            }

            return;
         } catch (RemoteRuntimeException var10) {
            Throwable var2 = var10.getNestedException();
            if (ServerLifeCycleRuntime.debug.isEnabled()) {
               Debug.say("Got a RemoteRuntimeException shutting down with nested exception:" + var2);
            }

            if (!(var2 instanceof PeerGoneException)) {
               this.taskMBean.setError(var10);
            }

            return;
         } catch (Exception var11) {
            Exception var1 = var11;
            if (ServerLifeCycleRuntime.debug.isEnabled()) {
               Debug.say("Got an Exception shutting down with exeption " + var11);
            }

            if (!(var11 instanceof PeerGoneException)) {
               try {
                  if (this.forceShutdown && !ServerLifeCycleRuntime.isSecurityException(var1) && ServerLifeCycleRuntime.this.useNodeManagerToShutdown()) {
                     return;
                  }

                  this.taskMBean.setError(var1);
               } catch (IOException var9) {
                  var9.initCause(var11);
                  this.taskMBean.setError(var9);
               }

               return;
            }
         } finally {
            ServerLifeCycleRuntime.updateTaskMBeanOnCompletion(this.taskMBean);
         }

      }
   }
}
