package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class GroupImpl implements Group {
   protected static final boolean DEBUG;
   protected final Set configurationSet;
   protected final TreeSet members = new TreeSet();
   protected final Set unreliableSet = new HashSet();
   protected Object handle;

   private static boolean initProperty(String var0) {
      try {
         return Boolean.getBoolean(var0);
      } catch (SecurityException var2) {
         return false;
      }
   }

   GroupImpl() {
      this.configurationSet = new HashSet();
   }

   public GroupImpl(Set var1) {
      this.configurationSet = var1;
   }

   public synchronized GroupMember[] getMembers() {
      GroupMember[] var1 = new GroupMember[this.members.size()];
      this.members.toArray(var1);
      return var1;
   }

   public synchronized GroupMember[] getUnReliableMembers() {
      GroupMember[] var1 = new GroupMember[this.unreliableSet.size()];
      this.unreliableSet.toArray(var1);
      return var1;
   }

   protected synchronized ServerConfigurationInformation[] getConfigurations() {
      ServerConfigurationInformation[] var1 = new ServerConfigurationInformation[this.configurationSet.size()];
      this.configurationSet.toArray(var1);
      return var1;
   }

   public synchronized void addToConfigurationSet(ServerConfigurationInformation var1) {
      this.configurationSet.add(var1);
   }

   public synchronized void removeFromConfigurationSet(ServerConfigurationInformation var1) {
      this.configurationSet.remove(var1);
   }

   synchronized void addToRunningSet(GroupMember var1) {
      this.members.add(var1);
      this.configurationSet.remove(var1.getConfiguration());
      if (DEBUG) {
         this.debug("addToRunningSet " + var1);
      }

   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[Group] [" + this.toString() + "] " + var1);
   }

   public synchronized GroupMember removeFromRunningSet(GroupMember var1) {
      if (DEBUG) {
         this.debug("removeFromRunningSet " + var1);
      }

      this.members.remove(var1);
      this.configurationSet.add(var1.getConfiguration());
      this.startDiscoveryIfNeeded();
      return this.members.size() == 0 ? null : (GroupMember)this.members.first();
   }

   public synchronized void removeFromUnReliableSet(GroupMember var1) {
      if (DEBUG) {
         this.debug("removeFromUnReliableSet " + var1);
      }

      this.unreliableSet.remove(var1);
      this.configurationSet.add(var1.getConfiguration());
      this.startDiscoveryIfNeeded();
   }

   private synchronized boolean isLocalServerSeniormost() {
      GroupMember var1 = GroupManagerImpl.getInstance().getLocalMember();
      return var1.equals(this.members.first());
   }

   protected synchronized void startDiscoveryIfNeeded() {
      if (this.handle == null) {
         if (this.members.size() < 1) {
            throw new AssertionError("local group should have atleast one member!");
         } else {
            if (this.members.size() == 1 || this.isLocalServerSeniormost()) {
               this.handle = this.startTimer();
            }

         }
      }
   }

   public void send(Message var1) throws IOException {
      GroupMember var2 = GroupManagerImpl.getInstance().getLocalMember();
      GroupMember[] var3 = this.getMembers();
      if (var3.length == 0) {
         throw new AssertionError("LocalGroup should atleast have the local server!");
      } else {
         if (var2.equals(var3[0])) {
            if (DEBUG) {
               this.debug("we are the seniormost. Send message to group");
            }

            this.performLeaderActions(var1);
         } else {
            GroupMember var4 = var3[0];
            this.sendToLeader(var4, var1);
         }

      }
   }

   protected void sendToLeader(GroupMember var1, Message var2) {
      GroupMember var3 = GroupManagerImpl.getInstance().getLocalMember();

      while(true) {
         try {
            if (var3.equals(var1)) {
               this.performLeaderActions(var2);
               return;
            }

            if (DEBUG) {
               this.debug("Send [" + var2 + "] to leader -> " + var1);
            }

            if (!var2.getServerName().equals(var1.getConfiguration().getServerName())) {
               var1.send(var2);
               if (var1.getLastArrivalTime() > 0L && System.currentTimeMillis() - var1.getLastArrivalTime() > Environment.getPropertyService().getHeartbeatTimeoutMillis()) {
                  if (DEBUG) {
                     this.debug("add Leader " + var1 + " to unreliableSet");
                  }

                  this.unreliableSet.add(var1);
                  throw new UnReliableServerException(var1.getConfiguration().getServerName() + " missed heartbeats !");
               }
            }

            if (DEBUG) {
               this.debug("send ok to " + var1);
            }

            return;
         } catch (IOException var5) {
            var1 = this.removeFromRunningSet(var1);
            if (var1 == null) {
               return;
            }

            if (DEBUG) {
               this.debug("send failed to " + var1);
            }
         } catch (UnReliableServerException var6) {
            var1 = this.removeFromRunningSet(var1);
            if (var1 == null) {
               return;
            }

            var1.setLastMessageArrivalTime(System.currentTimeMillis() + Environment.getPropertyService().getHeartbeatTimeoutMillis());
            if (DEBUG) {
               this.debug("send failed to " + var1);
            }
         }
      }
   }

   protected void performLeaderActions(Message var1) {
      if (DEBUG) {
         this.debug("we are the seniormost. Send message to group");
      }

      GroupMember var2 = GroupManagerImpl.getInstance().getLocalMember();
      GroupMember[] var3 = this.getMembers();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         GroupMember var5 = var3[var4];
         if (!var2.equals(var5)) {
            try {
               var5.send(var1);
               if (DEBUG) {
                  this.debug(var1 + " send ok to " + var5);
               }
            } catch (IOException var9) {
               this.removeFromRunningSet(var5);
               if (DEBUG) {
                  this.debug(var1 + " send failed to " + var5);
               }
            }
         }
      }

      GroupMember[] var10 = this.getUnReliableMembers();

      for(int var11 = 0; var11 < var10.length; ++var11) {
         GroupMember var6 = var10[var11];

         try {
            var6.send(var1);
            if (DEBUG) {
               this.debug(var1 + " send ok to unreliable member " + var6);
            }
         } catch (IOException var8) {
            this.removeFromUnReliableSet(var6);
            if (DEBUG) {
               this.debug(var1 + " send failed to " + var6);
            }
         }
      }

      GroupManagerImpl.getInstance().sendRemoteGroups(var1);
   }

   public boolean isLocal() {
      return true;
   }

   public void start() {
      this.handle = this.startTimer();
   }

   public void stop() {
      if (this.handle != null) {
         Environment.stopTimer(this.handle);
      }
   }

   private Object startTimer() {
      int var1 = Environment.getPropertyService().getDiscoveryPeriodMillis();
      return Environment.startTimer(new LocalGroupMonitor(), 0, var1);
   }

   public synchronized ServerConfigurationInformation getConfigInformation(String var1) {
      Iterator var2 = this.configurationSet.iterator();

      ServerConfigurationInformation var3;
      do {
         if (!var2.hasNext()) {
            var2 = this.members.iterator();

            ServerConfigurationInformation var4;
            do {
               GroupMember var5;
               if (!var2.hasNext()) {
                  var2 = this.unreliableSet.iterator();

                  do {
                     if (!var2.hasNext()) {
                        return null;
                     }

                     var5 = (GroupMember)var2.next();
                     var4 = var5.getConfiguration();
                  } while(!var4.getServerName().equals(var1));

                  return var4;
               }

               var5 = (GroupMember)var2.next();
               var4 = var5.getConfiguration();
            } while(!var4.getServerName().equals(var1));

            return var4;
         }

         var3 = (ServerConfigurationInformation)var2.next();
      } while(!var3.getServerName().equals(var1));

      return var3;
   }

   public synchronized GroupMember findOrCreateGroupMember(ServerConfigurationInformation var1, long var2) {
      Iterator var4 = this.members.iterator();

      GroupMember var5;
      do {
         if (!var4.hasNext()) {
            GroupMemberImpl var6 = new GroupMemberImpl(var1, var2);
            if (this.unreliableSet.contains(var6)) {
               this.removeFromUnReliableSet(var6);
            }

            this.addToRunningSet(var6);
            return var6;
         }

         var5 = (GroupMember)var4.next();
      } while(!var5.getConfiguration().equals(var1));

      return var5;
   }

   private boolean isMessageFromRemoteGroup(Connection var1) {
      return this.getConfigInformation(var1.getConfiguration().getServerName()) == null;
   }

   public void forward(Message var1, Connection var2) {
      GroupMember var3 = GroupManagerImpl.getInstance().getLocalMember();
      GroupMember[] var4 = this.getMembers();
      if (var4.length == 0) {
         throw new AssertionError("LocalGroup should atleast have the local server!");
      } else {
         if (var3.equals(var4[0]) || this.isMessageFromRemoteGroup(var2)) {
            if (DEBUG) {
               this.debug("we are the seniormost. Send message to group. connection.getConfiguration()=" + var2.getConfiguration().getServerName());
            }

            for(int var5 = 0; var5 < var4.length; ++var5) {
               GroupMember var6 = var4[var5];
               if (!var3.equals(var6) && !var6.getConfiguration().equals(var2.getConfiguration())) {
                  try {
                     var6.send(var1);
                     if (DEBUG) {
                        this.debug(var1 + " forward ok to " + var6);
                     }
                  } catch (IOException var10) {
                     this.removeFromRunningSet(var6);
                     if (DEBUG) {
                        this.debug(var1 + " forward failed to " + var6);
                     }
                  }
               }
            }

            GroupMember[] var11 = this.getUnReliableMembers();

            for(int var13 = 0; var13 < var11.length; ++var13) {
               GroupMember var7 = var11[var13];
               if (!var3.equals(var7) && !var7.getConfiguration().equals(var2.getConfiguration())) {
                  try {
                     var7.send(var1);
                     if (DEBUG) {
                        this.debug(var1 + " forward ok to unreliable member " + var7);
                     }
                  } catch (IOException var9) {
                     this.removeFromUnReliableSet(var7);
                     if (DEBUG) {
                        this.debug(var1 + " send failed to " + var7);
                     }
                  }
               }
            }
         }

         boolean var12 = this.getConfigInformation(var1.getServerName()) != null;
         boolean var14 = this.getConfigInformation(var2.getConfiguration().getServerName()) != null;
         if (var3.equals(var4[0]) && var12 && var14) {
            GroupManagerImpl.getInstance().sendRemoteGroups(var1);
         }

      }
   }

   public String toString() {
      return "LocalGroup [" + this.members + "]";
   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static class UnReliableServerException extends Exception {
      UnReliableServerException(String var1) {
         super(var1);
      }
   }

   private class LocalGroupMonitor implements Runnable {
      private int discoveryAttempts;
      private boolean remoteGroupMonitoring;

      private LocalGroupMonitor() {
      }

      public void run() {
         if (GroupImpl.this.isLocalServerSeniormost()) {
            if (this.discoveryAttempts > 2 && !this.remoteGroupMonitoring) {
               GroupManagerImpl.getInstance().startRemoteGroups();
               this.remoteGroupMonitoring = true;
               if (GroupImpl.DEBUG) {
                  GroupImpl.this.debug("starting remote group discovery ...");
               }
            }

            ServerConfigurationInformation[] var1 = GroupImpl.this.getConfigurations();
            if (var1 == null || var1.length == 0) {
               return;
            }

            for(int var2 = 0; var2 < var1.length; ++var2) {
               PingRoutine var3 = Environment.getPingRoutine();
               long var4 = var3.ping(var1[var2]);
               if (var4 > 0L) {
                  if (GroupImpl.DEBUG) {
                     GroupImpl.this.debug("discovered " + var1[var2]);
                  }

                  GroupMemberImpl var6 = new GroupMemberImpl(var1[var2], var4);
                  if (!GroupImpl.this.unreliableSet.contains(var6)) {
                     GroupImpl.this.addToRunningSet(var6);
                  }
               }
            }

            ++this.discoveryAttempts;
         } else {
            if (GroupImpl.DEBUG) {
               GroupImpl.this.debug("we are not senior anymore ! senior is " + GroupImpl.this.getMembers()[0]);
            }

            Environment.stopTimer(GroupImpl.this.handle);
            GroupImpl.this.handle = null;
            if (this.remoteGroupMonitoring) {
               if (GroupImpl.DEBUG) {
                  GroupImpl.this.debug("stopping remote group discovery ...");
               }

               Environment.getGroupManager().stopRemoteGroups();
               this.remoteGroupMonitoring = false;
            }
         }

      }

      // $FF: synthetic method
      LocalGroupMonitor(Object var2) {
         this();
      }
   }
}
