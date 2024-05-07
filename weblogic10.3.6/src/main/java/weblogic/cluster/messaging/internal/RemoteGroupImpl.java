package weblogic.cluster.messaging.internal;

import java.util.Set;

final class RemoteGroupImpl extends GroupImpl {
   public RemoteGroupImpl(Set var1) {
      super(var1);
   }

   public void start() {
      if (DEBUG) {
         this.debug("starting remote group timer for " + this);
      }

      this.handle = Environment.startTimer(new RemoteGroupMonitor(), 0, Environment.getPropertyService().getDiscoveryPeriodMillis());
   }

   public void send(Message var1) {
      GroupMember[] var2 = this.getMembers();
      if (var2.length == 0) {
         if (DEBUG) {
            this.debug("no server in remote group is alive!");
         }

         this.startDiscoveryIfNeeded();
      } else {
         GroupMember var3 = var2[0];
         this.sendToLeader(var3, var1);
      }

   }

   protected void performLeaderActions(Message var1) {
      throw new AssertionError("local server can never perform leader actions on a remote group !");
   }

   public void forward(Message var1, Connection var2) {
      throw new AssertionError("forward cannot be called on remote groups !");
   }

   protected synchronized void startDiscoveryIfNeeded() {
      if (this.handle == null) {
         if (this.members.size() == 0) {
            this.handle = Environment.startTimer(new RemoteGroupMonitor(), 0, Environment.getPropertyService().getDiscoveryPeriodMillis());
         }

      }
   }

   public boolean isLocal() {
      return false;
   }

   public String toString() {
      return "RemoteGroup [" + this.members + "]";
   }

   private class RemoteGroupMonitor implements Runnable {
      private RemoteGroupMonitor() {
      }

      public void run() {
         ServerConfigurationInformation[] var1 = RemoteGroupImpl.this.getConfigurations();
         if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               PingRoutine var3 = Environment.getPingRoutine();
               long var4 = var3.ping(var1[var2]);
               if (var4 > 0L) {
                  if (GroupImpl.DEBUG) {
                     RemoteGroupImpl.this.debug("discovered " + var1[var2]);
                  }

                  GroupMemberImpl var6 = new GroupMemberImpl(var1[var2], var4);
                  RemoteGroupImpl.this.addToRunningSet(var6);
               }
            }

            if (RemoteGroupImpl.this.members.size() > 0) {
               if (RemoteGroupImpl.this.handle != null) {
                  Environment.stopTimer(RemoteGroupImpl.this.handle);
               }

               RemoteGroupImpl.this.handle = null;
               if (GroupImpl.DEBUG) {
                  RemoteGroupImpl.this.debug("stopping timer!");
               }
            }

         }
      }

      // $FF: synthetic method
      RemoteGroupMonitor(Object var2) {
         this();
      }
   }
}
