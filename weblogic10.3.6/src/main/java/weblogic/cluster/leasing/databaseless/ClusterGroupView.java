package weblogic.cluster.leasing.databaseless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkManagerFactory;

public final class ClusterGroupView implements Serializable {
   private ServerInformation leaderInformation;
   private TreeSet members;
   private transient GroupViewListener listener;
   private long versionNumber = 0L;
   private static final DebugCategory debugClusterGroupView;
   private static final boolean DEBUG;
   private static final long serialVersionUID = 8079240922100673698L;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   ClusterGroupView(ServerInformation var1, ServerInformation[] var2) {
      this.leaderInformation = var1;
      this.members = new TreeSet();
      this.members.add(var1);
      if (var2 != null && var2.length > 0) {
         this.members.addAll(Arrays.asList(var2));
      }

   }

   ClusterGroupView(TreeSet var1) {
      if (!$assertionsDisabled && var1.size() <= 0) {
         throw new AssertionError();
      } else {
         this.leaderInformation = (ServerInformation)var1.first();
         this.members = var1;
      }
   }

   synchronized ServerInformation getLeaderInformation() {
      return this.leaderInformation;
   }

   Set getMembers() {
      return this.members;
   }

   public String toString() {
      return "[GroupView with leader " + this.leaderInformation + " version " + this.versionNumber + " and members " + this.members + "]";
   }

   public synchronized boolean isSeniorMost(ServerInformation var1) {
      return var1.equals(this.members.first());
   }

   public synchronized ServerInformation getSeniorMost() {
      return (ServerInformation)this.members.first();
   }

   public synchronized void removeLeader() {
      this.removeMember(this.leaderInformation);
      this.leaderInformation = (ServerInformation)this.members.first();
   }

   public synchronized ServerInformation[] getRemoteMembers(ServerInformation var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.members.iterator();

      while(var3.hasNext()) {
         ServerInformation var4 = (ServerInformation)var3.next();
         if (!var1.equals(var4)) {
            var2.add(var4);
         }
      }

      ServerInformation[] var5 = new ServerInformation[var2.size()];
      return (ServerInformation[])((ServerInformation[])var2.toArray(var5));
   }

   public synchronized void addMember(final ServerInformation var1) {
      this.members.add(var1);
      if (DEBUG) {
         debug("Added Member - " + var1 + " to group " + this);
      }

      if (this.listener != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               ClusterGroupView.this.listener.memberAdded(var1);
            }
         });
      }

   }

   public synchronized void removeMember(final ServerInformation var1) {
      if (this.members.remove(var1)) {
         if (this.leaderInformation.equals(var1)) {
            this.leaderInformation = (ServerInformation)this.members.first();
         }

         if (DEBUG) {
            debug("Removed Member - " + var1 + " from group " + this);
         }

         if (this.listener != null) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  ClusterGroupView.this.listener.memberRemoved(var1);
               }
            });
         }

      }
   }

   void setGroupViewListener(GroupViewListener var1) {
      this.listener = var1;
   }

   public synchronized long getVersionNumber() {
      return this.versionNumber;
   }

   synchronized long incrementVersionNumber() {
      return ++this.versionNumber;
   }

   public synchronized void processStateDump(ClusterGroupView var1) {
      if (var1 != null && this.versionNumber < var1.getVersionNumber()) {
         TreeSet var2 = (TreeSet)var1.getMembers();
         Iterator var3 = this.members.iterator();

         while(var3.hasNext()) {
            ServerInformation var4 = (ServerInformation)var3.next();
            if (!var2.contains(var4)) {
               this.listener.memberRemoved(var4);
            }
         }

         this.members = var2;
         this.versionNumber = var1.getVersionNumber();
      }
   }

   synchronized ServerInformation getServerInformation(String var1) {
      Iterator var2 = this.members.iterator();

      ServerInformation var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ServerInformation)var2.next();
      } while(!var3.getServerName().equals(var1));

      return var3;
   }

   private static void debug(String var0) {
      DebugLogger.debug("[ClusterGroupView] " + var0);
   }

   private static boolean debugEnabled() {
      return debugClusterGroupView.isEnabled() || DebugLogger.isDebugEnabled();
   }

   static {
      $assertionsDisabled = !ClusterGroupView.class.desiredAssertionStatus();
      debugClusterGroupView = Debug.getCategory("weblogic.cluster.leasing.ClusterGroupView");
      DEBUG = debugEnabled();
   }
}
