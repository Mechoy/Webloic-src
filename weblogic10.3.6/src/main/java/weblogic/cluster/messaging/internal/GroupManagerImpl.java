package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

public class GroupManagerImpl implements GroupManager {
   private static final boolean DEBUG;
   private static final int MAX_GROUP_SIZE = 10;
   private final GroupMemberImpl localMember;
   private final ServerConfigurationInformation serverConfigForWire;
   private GroupImpl localGroup;
   private GroupImpl anonymousGroup;
   private final Set remoteGroups;
   private MessageListener listener;

   public static GroupManager getInstance() {
      return GroupManagerImpl.Factory.THE_ONE;
   }

   private GroupManagerImpl() {
      this.anonymousGroup = new AnonymousGroupImpl();
      this.remoteGroups = new HashSet();
      this.localMember = new GroupMemberImpl(Environment.getConfiguredServersMonitor().getLocalServerConfiguration(), 1L);
      ServerConfigurationInformation var1 = this.localMember.getConfiguration();
      this.serverConfigForWire = new ServerConfigurationInformationImpl(var1.getAddress(), var1.getPort(), var1.getServerName(), System.currentTimeMillis(), var1.isUsingSSL());
      this.initializeGroups(Environment.getConfiguredServersMonitor().getConfiguredServers());
      this.localGroup.start();
   }

   public ServerConfigurationInformation getServerConfigForWire() {
      return this.serverConfigForWire;
   }

   public void startRemoteGroups() {
      Iterator var1 = this.remoteGroups.iterator();

      while(var1.hasNext()) {
         Group var2 = (Group)var1.next();
         var2.start();
      }

   }

   public void stopRemoteGroups() {
      Iterator var1 = this.remoteGroups.iterator();

      while(var1.hasNext()) {
         Group var2 = (Group)var1.next();
         var2.stop();
      }

   }

   public void sendRemoteGroups(final Message var1) {
      Environment.executeForwardMessage(new Runnable() {
         public void run() {
            Iterator var1x = GroupManagerImpl.this.remoteGroups.iterator();

            while(var1x.hasNext()) {
               Group var2 = (Group)var1x.next();

               try {
                  var2.send(var1);
               } catch (IOException var5) {
               }
            }

            try {
               GroupManagerImpl.this.anonymousGroup.send(var1);
            } catch (IOException var4) {
            }

         }
      });
   }

   public GroupMember getLocalMember() {
      return this.localMember;
   }

   public Group getLocalGroup() {
      return this.localGroup;
   }

   public synchronized Group[] getRemoteGroups() {
      Group[] var1 = new Group[this.remoteGroups.size()];
      this.remoteGroups.toArray(var1);
      return var1;
   }

   private void initializeGroups(SortedSet var1) {
      Iterator var2 = var1.iterator();
      ServerConfigurationInformation var3 = Environment.getConfiguredServersMonitor().getLocalServerConfiguration();
      int var4 = 0;
      HashSet var5 = new HashSet(10);
      boolean var6 = false;

      while(true) {
         do {
            if (!var2.hasNext()) {
               if (DEBUG) {
                  this.debug("local group: " + this.localGroup);
               }

               return;
            }

            ServerConfigurationInformation var7 = (ServerConfigurationInformation)var2.next();
            if (DEBUG) {
               this.debug("initGroups: processing " + var7);
            }

            if (var3.equals(var7)) {
               var6 = true;
            }

            if (DEBUG) {
               this.debug("initGroups: isLocal " + var6);
            }

            var5.add(var7);
            ++var4;
         } while(var4 % 10 != 0 && var4 != var1.size());

         if (var6) {
            this.localGroup = new GroupImpl(var5);
            this.localGroup.addToRunningSet(this.localMember);
            var6 = false;
         } else {
            this.remoteGroups.add(new RemoteGroupImpl(var5));
         }

         var5 = new HashSet(10);
      }
   }

   private void debug(String var1) {
      Environment.getLogService().debug("[GroupManager] " + var1);
   }

   public void handleMessage(Message var1, Connection var2) {
      if (this.localMember.getConfiguration().equals(var1.getSenderConfiguration())) {
         if (DEBUG) {
            this.debug("squelching local message:" + var1);
         }

      } else {
         if (DEBUG) {
            this.debug("received for dispatch:" + var1 + " from " + var2);
         }

         GroupMember var3 = this.findOrCreateGroupMember(var1);
         if (var3 != null) {
            var3.setLastMessageArrivalTime(System.currentTimeMillis());
            if (DEBUG) {
               this.debug("updated LAT for " + var3.getConfiguration().getServerName());
            }
         }

         GroupMember var4 = this.findOrCreateGroupMember(var2);
         if (var4 != null) {
            var4.setLastMessageArrivalTime(System.currentTimeMillis());
            if (DEBUG) {
               this.debug("updated LAT for " + var4.getConfiguration().getServerName());
            }

            var4.addConnection(var2);
         }

         this.localMember.receive(var1, var2);
         this.listener.onMessage(var1);
      }
   }

   public void setMessageListener(MessageListener var1) {
      this.listener = var1;
   }

   private GroupMember findOrCreateGroupMember(Message var1) {
      String var2 = var1.getServerName();
      ServerConfigurationInformation var3 = this.localGroup.getConfigInformation(var2);
      Object var4 = var3 != null ? this.localGroup : null;
      if (var4 == null) {
         Iterator var5 = this.remoteGroups.iterator();

         while(var5.hasNext()) {
            Group var6 = (Group)var5.next();
            var3 = var6.getConfigInformation(var2);
            if (var3 != null) {
               var4 = var6;
               break;
            }
         }
      }

      if (var4 == null) {
         if (DEBUG) {
            this.debug(var1 + " associated with anonymous group");
         }

         var4 = this.anonymousGroup;
         var3 = var1.getSenderConfiguration();
      }

      return ((Group)var4).findOrCreateGroupMember(var3, var1.getServerStartTime());
   }

   private GroupMember findOrCreateGroupMember(Connection var1) {
      String var2 = var1.getConfiguration().getServerName();
      ServerConfigurationInformation var3 = this.localGroup.getConfigInformation(var2);
      Object var4 = var3 != null ? this.localGroup : null;
      if (var4 == null) {
         Iterator var5 = this.remoteGroups.iterator();

         while(var5.hasNext()) {
            Group var6 = (Group)var5.next();
            var3 = var6.getConfigInformation(var2);
            if (var3 != null) {
               var4 = var6;
               break;
            }
         }
      }

      if (var4 == null) {
         if (DEBUG) {
            this.debug(var1 + " associated with anonymous group");
         }

         var4 = this.anonymousGroup;
         var3 = var1.getConfiguration();
      }

      return ((Group)var4).findOrCreateGroupMember(var3, var1.getConfiguration().getCreationTime());
   }

   // $FF: synthetic method
   GroupManagerImpl(Object var1) {
      this();
   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static final class Factory {
      static final GroupManager THE_ONE = new GroupManagerImpl();
   }
}
