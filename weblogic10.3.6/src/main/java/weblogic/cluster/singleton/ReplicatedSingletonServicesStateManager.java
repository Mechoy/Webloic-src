package weblogic.cluster.singleton;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.cluster.GroupMessage;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ReplicatedSingletonServicesStateManager implements SingletonServicesStateManager, SingletonServicesStateManagerRemote, RecoverListener {
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   private static final int DEFAULT_SIZE = 32;
   private static final String VERSION_STRING = "ReplicatedSingletonServicesStateManager.Version";
   private static final Integer VERSION_NONE = new Integer(Integer.MAX_VALUE);
   private static final int EXECUTE_MESSAGE = 1;
   private static final int GET_ALL_SERVICES_STATE = 2;
   private static final int STORE_MESSAGE_FOR_NEXT_STATE_MANAGER = 3;
   private static final int GET_PENDING_MESSAGES = 4;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ConcurrentHashMap vmVideMap = new ConcurrentHashMap();
   private static final ConcurrentHashMap vmVidePendingMessagesMap = new ConcurrentHashMap();
   private static String localServerName;
   private static ClusterMBean clusterMBean;
   private static int unicast_servers_to_send;
   private static int unicast_servers_to_recv;
   private final String instanceName;
   private final LeaseManager leaseManager;
   private final MulticastSession multicastSession;
   private boolean isActive = false;
   private Integer lastVersionSent = null;

   public ReplicatedSingletonServicesStateManager(String var1, LeaseManager var2) {
      this.leaseManager = var2;
      this.instanceName = var1;
      ConcurrentHashMap var3 = new ConcurrentHashMap(32);
      vmVideMap.put(this.instanceName, var3);
      ArrayList var4 = new ArrayList();
      vmVidePendingMessagesMap.put(this.instanceName, var4);
      ClusterService var5 = ClusterService.getClusterService();
      if (var5 == null) {
         throw new RuntimeException("This server is not in a cluster.");
      } else {
         this.multicastSession = var5.createMulticastSession(this, 1, false, true);
         DomainMBean var6 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         localServerName = LocalServerIdentity.getIdentity().getServerName();
         clusterMBean = var6.lookupServer(localServerName).getCluster();
         int var7 = clusterMBean.getServers().length;

         String var8;
         try {
            var8 = System.getProperty("weblogic.cluster.singleton.SendPendingMessagesToServers", "3");
            unicast_servers_to_send = Integer.valueOf(var8);
         } catch (Exception var10) {
            unicast_servers_to_send = 3;
         }

         unicast_servers_to_send = var7 > unicast_servers_to_send ? unicast_servers_to_send : var7;

         try {
            var8 = System.getProperty("weblogic.cluster.singleton.ReceivePendingMessagesFromServers", String.valueOf(var7));
            unicast_servers_to_recv = Integer.valueOf(var8);
         } catch (Exception var9) {
            unicast_servers_to_recv = var7;
         }

         unicast_servers_to_recv = var7 > unicast_servers_to_recv ? unicast_servers_to_recv : var7;
         syncStateFromActiveStateManager(this.instanceName);
         if (getVersion(this.instanceName) == null) {
            incrementVersion(this.instanceName);
         }

      }
   }

   public Serializable invoke(int var1, Serializable var2) {
      Object var3 = null;
      Message var11;
      switch (var1) {
         case 1:
            var11 = (Message)var2;
            var11.executeOnActiveStateManager(this);
            var3 = new Boolean(true);
            break;
         case 2:
            var3 = (Serializable)this.getAllServicesState();
            break;
         case 3:
            var11 = (Message)var2;
            if (DEBUG) {
               p("Adding pending message: " + var11);
            }

            addLocalPendingMessages(this.instanceName, var11);
            var3 = new Boolean(true);
            break;
         case 4:
            var3 = (Serializable)getLocalPendingMessages(this.instanceName);
            break;
         case 1001:
            Map var4 = (Map)var2;
            String var5 = (String)var4.get("Sender");
            String var6 = (String)var4.get("SvcName");
            SingletonServicesState var7 = (SingletonServicesState)var4.get("SvcState");
            String var8 = null;

            try {
               var8 = this.leaseManager.findOwner(var6);
               if (var8 != null) {
                  var8 = LeaseManager.getServerNameFromOwnerIdentity(var8);
               }
            } catch (LeasingException var10) {
            }

            boolean var9 = false;
            if (var8 != null && (var8 == null || !var5.equals(var8))) {
               if (DEBUG) {
                  p("Ignoring the call for update state - " + var7 + " for service " + var6 + "as the sender - " + var5 + " does not match the current owner - " + var8);
               }
            } else {
               var9 = this.storeServiceState(var6, var7);
            }

            var3 = new Boolean(var9);
      }

      return (Serializable)var3;
   }

   public boolean checkServiceState(String var1, int var2) {
      Map var3 = getLocalMap(this.instanceName);
      synchronized(var3) {
         SingletonServicesState var5 = (SingletonServicesState)var3.get(var1);
         return var5 != null && var5.getState() == var2;
      }
   }

   public SingletonServicesState getServiceState(String var1) {
      Map var2 = getLocalMap(this.instanceName);
      synchronized(var2) {
         return (SingletonServicesState)var2.get(var1);
      }
   }

   public Map getAllServicesState() {
      Map var1 = getLocalMap(this.instanceName);
      synchronized(var1) {
         HashMap var3 = new HashMap(var1);
         return var3;
      }
   }

   public boolean storeServiceState(String var1, SingletonServicesState var2) {
      boolean var3 = false;
      if (this.isActive) {
         Map var4 = getLocalMap(this.instanceName);
         Integer var5 = null;
         synchronized(var4) {
            SingletonServicesState var7 = (SingletonServicesState)var4.put(var1, var2);
            var5 = incrementVersion(this.instanceName);
         }

         var3 = this.sendUpdateMessage(var5, var1, var2, false, true);
      } else {
         var3 = this.sendUpdateMessage(VERSION_NONE, var1, var2, false, false);
      }

      return var3;
   }

   public boolean removeServiceState(String var1) {
      boolean var2 = false;

      try {
         Map var3 = getLocalMap(this.instanceName);
         Integer var4 = null;
         synchronized(var3) {
            SingletonServicesState var6 = (SingletonServicesState)var3.remove(var1);
            var4 = incrementVersion(this.instanceName);
         }

         var2 = this.sendUpdateMessage(var4, var1, (SingletonServicesState)null, true, true);
      } catch (Exception var9) {
      }

      return var2;
   }

   public synchronized void syncState() {
      Integer var1 = getVersion(this.instanceName);
      if (this.lastVersionSent == null || this.lastVersionSent != var1) {
         HeartbeatMessage var2 = new HeartbeatMessage(this.instanceName, var1);
         boolean var3 = false;
         if (!ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown()) {
            this.sendGroupMessage(var2);
            this.lastVersionSent = var1;
         }
      }
   }

   public void leaseAcquired() {
      this.isActive = true;
      TreeSet var1 = new TreeSet();
      ArrayList var2 = new ArrayList();
      List var3 = getLocalPendingMessages(this.instanceName);
      if (DEBUG) {
         p("Got " + var3.size() + " local pending messages");
      }

      this.sortPendingMessages(var3, var1, var2);
      Collection var4 = ClusterService.getClusterService().getAllRemoteMembers();
      int var5 = var4.size() > unicast_servers_to_recv ? unicast_servers_to_recv : var4.size();
      if (DEBUG) {
         p("Fetching pending messages from " + var5 + " remote members");
      }

      Set var6 = this.sortServersBasedonSeniority(var4);
      int var7 = 0;
      Iterator var8 = var6.iterator();

      while(var8.hasNext()) {
         ClusterMemberInfo var9 = (ClusterMemberInfo)var8.next();
         String var10 = var9.serverName();
         SingletonServicesStateManagerRemote var11 = getSingletonServicesStateManagerRemote(var10, (long)clusterMBean.getHealthCheckIntervalMillis());
         if (var11 != null) {
            try {
               List var12 = (List)var11.invoke(4, (Serializable)null);
               if (DEBUG) {
                  p("Got " + var12.size() + " pending messages from " + var10);
               }

               this.sortPendingMessages(var12, var1, var2);
            } catch (Exception var13) {
               continue;
            }

            ++var7;
            if (var7 == var5) {
               break;
            }
         }
      }

      Iterator var14 = var1.iterator();

      while(var14.hasNext()) {
         Message var15 = (Message)var14.next();
         if (var15.isPendingModeExecutionAllowed()) {
            var15.executeOnActiveStateManager(this);
         }
      }

      Iterator var16 = var2.iterator();

      while(var16.hasNext()) {
         Message var17 = (Message)var16.next();
         if (var17.isPendingModeExecutionAllowed()) {
            var17.executeOnActiveStateManager(this);
         }
      }

   }

   private void sortPendingMessages(List var1, Set var2, List var3) {
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Message var5 = (Message)var4.next();
         if (var5.messageID.equals(VERSION_NONE)) {
            if (!var3.contains(var5)) {
               var3.add(var5);
               if (DEBUG) {
                  p("Added pending message for current state manager - " + var5);
               }
            }
         } else if (var2.add(var5) && DEBUG) {
            p("Added message from previous state manager - " + var5);
         }
      }

   }

   public synchronized void lostLease() {
      this.isActive = false;
      this.lastVersionSent = null;
   }

   public synchronized GroupMessage createRecoverMessage() {
      return this.isActive ? new StateDumpMessage(this.instanceName, (Serializable)getLocalMap(this.instanceName), getVersion(this.instanceName)) : new StateDumpMessage(this.instanceName, (Serializable)null, getVersion(this.instanceName));
   }

   private static Integer getVersion(String var0) {
      Integer var1 = null;
      Map var2 = getLocalMap(var0);
      synchronized(var2) {
         SingletonServicesState var4 = (SingletonServicesState)var2.get("ReplicatedSingletonServicesStateManager.Version");
         if (var4 != null) {
            var1 = (Integer)var4.getStateData();
         }

         return var1;
      }
   }

   private static void setVersion(String var0, Integer var1) {
      SingletonServicesState var2 = new SingletonServicesState(1);
      var2.setStateData(var1);
      Map var3 = getLocalMap(var0);
      synchronized(var3) {
         var3.put("ReplicatedSingletonServicesStateManager.Version", var2);
      }
   }

   private static Integer incrementVersion(String var0) {
      Map var1 = getLocalMap(var0);
      Integer var2 = null;
      synchronized(var1) {
         Integer var4 = getVersion(var0);
         if (var4 == null) {
            var4 = new Integer(0);
         }

         int var5 = var4 % 2147483646 + 1;
         var2 = new Integer(var5);
         setVersion(var0, var2);
         return var2;
      }
   }

   private static List getLocalPendingMessages(String var0) {
      ArrayList var1 = (ArrayList)vmVidePendingMessagesMap.get(var0);
      synchronized(var1) {
         if (var1 == null) {
            return null;
         } else {
            ArrayList var3 = (ArrayList)var1.clone();
            var1.clear();
            return var3;
         }
      }
   }

   private static void addLocalPendingMessages(String var0, Message var1) {
      ArrayList var2 = (ArrayList)vmVidePendingMessagesMap.get(var0);
      synchronized(var2) {
         if (var2 != null) {
            var2.add(var1);
         }
      }
   }

   private boolean sendUpdateMessage(Integer var1, Serializable var2, SingletonServicesState var3, boolean var4, boolean var5) {
      boolean var6 = false;
      UpdateMessage var7 = new UpdateMessage(this.instanceName, var1, var2, var3, var4);
      if (var5) {
         if (!ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown()) {
            var6 = this.sendGroupMessage(var7);
         } else {
            this.sendPendingMessagesForNextStateManager(var7, (long)clusterMBean.getHealthCheckIntervalMillis());
            var6 = true;
         }
      } else {
         SingletonServicesStateManagerRemote var8 = null;

         try {
            String var9 = MigratableServerService.theOne().findSingletonMaster();
            if (var9 != null) {
               var8 = getSingletonServicesStateManagerRemote(var9, (long)clusterMBean.getHealthCheckIntervalMillis());
            }
         } catch (LeasingException var11) {
         }

         if (var8 != null) {
            try {
               Boolean var12 = (Boolean)var8.invoke(1, var7);
               var6 = var12;
            } catch (RemoteException var10) {
            }
         } else {
            this.sendPendingMessagesForNextStateManager(var7, (long)clusterMBean.getHealthCheckIntervalMillis());
            var6 = true;
         }
      }

      return var6;
   }

   private boolean sendGroupMessage(Message var1) {
      boolean var2 = false;
      if (!this.isActive) {
         if (DEBUG) {
            p("Inactive state manager cannot send group message: " + var1);
         }

         return false;
      } else {
         try {
            this.multicastSession.send(var1);
            var2 = true;
         } catch (IOException var4) {
            ClusterLogger.logMulticastSendErrorMsg("Error in sending message: " + var4);
         }

         return var2;
      }
   }

   private void sendPendingMessagesForNextStateManager(Message var1, long var2) {
      if (!var1.isPendingModeExecutionAllowed()) {
         if (DEBUG) {
            p("Not sendPendingMessagesForNextStateManager - " + var1);
         }

      } else {
         Collection var4 = ClusterService.getClusterService().getAllRemoteMembers();
         int var5 = var4.size() > unicast_servers_to_send ? unicast_servers_to_send : var4.size();
         if (DEBUG) {
            p("Total number of servers to push pending message = " + var5);
         }

         Set var6 = this.sortServersBasedonSeniority(var4);
         int var7 = 0;
         Iterator var8 = var6.iterator();

         while(var8.hasNext()) {
            ClusterMemberInfo var9 = (ClusterMemberInfo)var8.next();
            String var10 = var9.serverName();

            try {
               SingletonServicesStateManagerRemote var11 = getSingletonServicesStateManagerRemote(var10, var2);
               if (var11 == null) {
                  continue;
               }

               var11.invoke(3, var1);
               if (DEBUG) {
                  p("Sent pending message to " + var10);
               }
            } catch (Exception var12) {
               continue;
            }

            ++var7;
            if (var7 == var5) {
               break;
            }
         }

      }
   }

   private Set sortServersBasedonSeniority(Collection var1) {
      TreeSet var2 = new TreeSet(new Comparator() {
         public int compare(Object var1, Object var2) {
            if (var1 instanceof ClusterMemberInfo && var2 instanceof ClusterMemberInfo) {
               Long var3 = new Long(((ClusterMemberInfo)var1).joinTime());
               Long var4 = new Long(((ClusterMemberInfo)var2).joinTime());
               return var3.compareTo(var4);
            } else {
               throw new IllegalArgumentException("Objects not instanceof ClusterMemberInfo");
            }
         }
      });
      var2.addAll(var1);
      return var2;
   }

   private static SingletonServicesStateManagerRemote getSingletonServicesStateManagerRemote(String var0, long var1) {
      SingletonServicesStateManagerRemote var3 = null;

      try {
         String var4 = null;

         try {
            var4 = URLManager.findAdministrationURL(var0);
         } catch (UnknownHostException var8) {
            MigratableServerService.theOne();
            var4 = MigratableServerService.findURLOfUnconnectedServer(var0);
         }

         if (var4 == null) {
            if (DEBUG) {
               p("Unable to find the admin url for active state manager - " + var0);
            }

            return null;
         }

         if (DEBUG) {
            p("Looking up SingletonServicesStateManagerRemote on " + var0 + " with url " + var4);
         }

         Hashtable var5 = new Hashtable();
         var5.put("java.naming.provider.url", var4);
         var5.put("weblogic.jndi.requestTimeout", new Long(var1));
         Environment var6 = new Environment(var5);
         Context var7 = var6.getInitialContext();
         var3 = (SingletonServicesStateManagerRemote)var7.lookup("weblogic.cluster.singleton.SingletonServicesStateManager");
      } catch (Exception var9) {
      }

      return var3;
   }

   private static void syncStateFromActiveStateManager(String var0) {
      try {
         String var1 = MigratableServerService.theOne().findSingletonMaster();
         if (var1 == null || var1.equals(localServerName)) {
            if (DEBUG) {
               p("Not able to sync state as active state manager - " + var1 + " not yet available or the local state manager is the active one.");
            }

            return;
         }

         SingletonServicesStateManagerRemote var2 = getSingletonServicesStateManagerRemote(var1, (long)clusterMBean.getHealthCheckIntervalMillis());
         if (var2 != null) {
            Map var3 = (Map)var2.invoke(2, var0);
            if (DEBUG) {
               p("New Services State Map:" + var3);
            }

            replaceLocalMap(var0, var3);
         }
      } catch (Exception var4) {
         if (DEBUG) {
            p("Failed to get a state dump because of :" + var4);
         }
      }

   }

   private static Map getLocalMap(String var0) {
      return (Map)vmVideMap.get(var0);
   }

   private static void replaceLocalMap(String var0, Map var1) {
      Map var2 = getLocalMap(var0);
      if (var2 == null) {
         if (DEBUG) {
            p("Local State Manager not yet initialized. Not replacing local map.");
         }

      } else {
         synchronized(var2) {
            if (var2 != null) {
               var2.clear();
               var2.putAll(var1);
            }
         }
      }
   }

   private static void p(Object var0) {
      SingletonServicesDebugLogger.debug("ReplicatedSingletonServicesStateManager: " + var0);
   }

   protected static class StateDumpMessage extends Message {
      private static final long serialVersionUID = -3290218502688112771L;
      private Serializable stateDump;

      StateDumpMessage(String var1, Serializable var2, Integer var3) {
         super(var1, var3);
         this.stateDump = var2;
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Sending :" + this);
         }

      }

      public void execute(HostID var1) {
         if (this.stateDump != null) {
            if (ReplicatedSingletonServicesStateManager.DEBUG) {
               ReplicatedSingletonServicesStateManager.p("Executing: " + this);
            }

            ReplicatedSingletonServicesStateManager.replaceLocalMap(this.name, (Map)this.stateDump);
         }

      }

      public boolean isPendingModeExecutionAllowed() {
         return false;
      }

      public String toString() {
         return new String("StateDumpMessage for state manager:" + this.name + " MessageID:" + this.messageID);
      }
   }

   public static class HeartbeatMessage extends Message {
      private static final long serialVersionUID = -7435880529011603250L;
      private Integer version;

      public HeartbeatMessage(String var1, Integer var2) {
         super(var1, var2);
         this.version = var2;
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Sending :" + this);
         }

      }

      public void execute(HostID var1) {
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Executing :" + this);
         }

         if (ManagementService.getRuntimeAccess(ReplicatedSingletonServicesStateManager.kernelId).getServerRuntime().isShuttingDown()) {
            if (ReplicatedSingletonServicesStateManager.DEBUG) {
               ReplicatedSingletonServicesStateManager.p("Server is shutting down. Not executing " + this);
            }

         } else {
            Map var2 = ReplicatedSingletonServicesStateManager.getLocalMap(this.name);
            if (var2 == null) {
               if (ReplicatedSingletonServicesStateManager.DEBUG) {
                  ReplicatedSingletonServicesStateManager.p("Local State Manager not yet initialized. Not executing " + this);
               }

            } else {
               Integer var3 = ReplicatedSingletonServicesStateManager.getVersion(this.name);
               if (var3 == null) {
                  if (ReplicatedSingletonServicesStateManager.DEBUG) {
                     ReplicatedSingletonServicesStateManager.p("Local State Manager not yet initialized. Not executing " + this);
                  }

               } else {
                  if (!var3.equals(this.version)) {
                     if (ReplicatedSingletonServicesStateManager.DEBUG) {
                        ReplicatedSingletonServicesStateManager.p("Version mismatch for state manager:" + this.name + " Local Version#:" + var3 + " Master Version#:" + this.version);
                     }

                     ReplicatedSingletonServicesStateManager.syncStateFromActiveStateManager(this.name);
                  }

                  List var4 = ReplicatedSingletonServicesStateManager.getLocalPendingMessages(this.name);
                  if (var4 != null && var4.size() > 0) {
                     if (ReplicatedSingletonServicesStateManager.DEBUG) {
                        ReplicatedSingletonServicesStateManager.p("Discarding pending messages that are still present even after starting to get heartbeat messages");
                     }

                     var4.clear();
                  }

               }
            }
         }
      }

      public boolean isPendingModeExecutionAllowed() {
         return false;
      }

      public String toString() {
         return new String("HeartbeatMessage for state manager:" + this.name + " MessageID:" + this.messageID + " Value:" + this.version);
      }
   }

   public static class UpdateMessage extends Message {
      private static final long serialVersionUID = 5942110178350613494L;
      private Serializable key;
      private SingletonServicesState newValue;
      private Integer version;
      private boolean removeState = false;

      public UpdateMessage(String var1, Integer var2, Serializable var3, SingletonServicesState var4, boolean var5) {
         super(var1, var2);
         this.key = var3;
         this.version = var2;
         this.newValue = var4;
         this.removeState = var5;
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Sending :" + this);
         }

      }

      public boolean equals(Object var1) {
         if (this.isVersionValid()) {
            return super.equals(var1);
         } else {
            boolean var2 = false;
            if (var1 instanceof UpdateMessage) {
               UpdateMessage var3 = (UpdateMessage)var1;
               if (var3.key.equals(this.key) && var3.newValue.equals(this.newValue) && var3.removeState == this.removeState) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public void executeOnActiveStateManager(ReplicatedSingletonServicesStateManager var1) {
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Executing :" + this);
         }

         if (!this.removeState) {
            var1.storeServiceState((String)this.key, this.newValue);
         } else {
            var1.removeServiceState((String)this.key);
         }

      }

      public void execute(HostID var1) {
         if (ReplicatedSingletonServicesStateManager.DEBUG) {
            ReplicatedSingletonServicesStateManager.p("Executing :" + this);
         }

         Map var2 = ReplicatedSingletonServicesStateManager.getLocalMap(this.name);
         if (var2 == null) {
            if (ReplicatedSingletonServicesStateManager.DEBUG) {
               ReplicatedSingletonServicesStateManager.p("Local State Manager not yet initialized. Not executing " + this);
            }

         } else {
            synchronized(var2) {
               Integer var4 = ReplicatedSingletonServicesStateManager.getVersion(this.name);
               int var5 = var4 == null ? 0 : var4;
               Integer var6 = new Integer(var5 + 1);
               if (!var6.equals(this.version)) {
                  if (ReplicatedSingletonServicesStateManager.DEBUG) {
                     ReplicatedSingletonServicesStateManager.p("Missed Update Message for state manager:" + this.name + " Expected Version#:" + var6 + " Master Version#:" + this.version);
                  }

                  ReplicatedSingletonServicesStateManager.syncStateFromActiveStateManager(this.name);
               } else {
                  if (this.removeState) {
                     var2.remove(this.key);
                  } else {
                     var2.put(this.key, this.newValue);
                  }

                  ReplicatedSingletonServicesStateManager.setVersion(this.name, this.version);
               }

            }
         }
      }

      public boolean isPendingModeExecutionAllowed() {
         return true;
      }

      public String toString() {
         String var1 = this.version.equals(ReplicatedSingletonServicesStateManager.VERSION_NONE) ? "None" : String.valueOf(this.version);
         return new String("UpdateMessage for state manager:" + this.name + " MessageID:" + var1 + " Key:" + this.key + " New Value:" + this.newValue + " DeleteState:" + this.removeState);
      }

      private boolean isVersionValid() {
         return !this.version.equals(ReplicatedSingletonServicesStateManager.VERSION_NONE);
      }
   }

   protected abstract static class Message implements Serializable, Comparable, GroupMessage {
      protected final String name;
      protected final Integer messageID;

      protected Message(String var1, Integer var2) {
         this.name = var1;
         this.messageID = var2;
      }

      public boolean equals(Object var1) {
         if (!var1.getClass().getName().equals(this.getClass().getName())) {
            return false;
         } else {
            Message var2 = (Message)var1;
            return this.messageID == var2.messageID;
         }
      }

      public int hashCode() {
         return this.messageID;
      }

      public int compareTo(Object var1) {
         if (!var1.getClass().getName().equals(this.getClass().getName())) {
            throw new ClassCastException();
         } else {
            Message var2 = (Message)var1;
            return this.messageID.compareTo(var2.messageID);
         }
      }

      public abstract boolean isPendingModeExecutionAllowed();

      public void executeOnActiveStateManager(ReplicatedSingletonServicesStateManager var1) {
      }

      public abstract void execute(HostID var1);
   }
}
