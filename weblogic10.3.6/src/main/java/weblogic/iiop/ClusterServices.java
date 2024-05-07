package weblogic.iiop;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterServiceActivator;
import weblogic.rjvm.JVMID;

public final class ClusterServices implements ClusterMembersChangeListener {
   private static ClusterServices singleton;
   private ClusterMemberInfo[] members;
   private final Object memberslock = new Object();
   private int current = 0;
   private JVMID secondary;
   private final Object secondarylock = new Object();
   private int lcpolicy = 1;
   private final Set serverSet = Collections.synchronizedSet(new TreeSet());
   public static final int LOCATION_FORWARD_OFF = 0;
   public static final int LOCATION_FORWARD_FAILOVER = 1;
   public static final int LOCATION_FORWARD_ROUND_ROBIN = 2;
   public static final int LOCATION_FORWARD_RANDOM = 3;

   static ClusterServices getServices() {
      return singleton;
   }

   static void initialize() {
      if (singleton == null) {
         createSingleton();
      }

   }

   private static synchronized ClusterServices createSingleton() {
      if (singleton == null) {
         singleton = new ClusterServices(IIOPClientService.locationForwardPolicy);
         weblogic.cluster.ClusterServices var0 = ClusterServiceActivator.INSTANCE.getClusterService();
         if (var0 != null) {
            var0.addClusterMembersListener(singleton);
         }
      }

      return singleton;
   }

   private ClusterServices(String var1) {
      if (var1.equals("off")) {
         this.lcpolicy = 0;
      } else if (var1.equals("failover")) {
         this.lcpolicy = 1;
      } else if (var1.equals("round-robin")) {
         this.lcpolicy = 2;
      } else if (var1.equals("random")) {
         this.lcpolicy = 3;
      }

      this.getMembers();
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      this.serverSet.add(var1.getClusterMemberInfo().identity());
      this.getMembers();
   }

   public final int getLocationForwardPolicy() {
      return this.lcpolicy;
   }

   public JVMID getNextMember() {
      if (this.members != null && this.lcpolicy != 0) {
         switch (this.lcpolicy) {
            case 1:
               return this.getNextMemberPrimarySecondary();
            case 2:
            default:
               ClusterMemberInfo var1 = null;
               synchronized(this.memberslock) {
                  var1 = this.members[this.current];
                  this.current = (this.current + 1) % this.members.length;
               }

               return (JVMID)var1.identity();
            case 3:
               return this.getNextMemberRandom();
         }
      } else {
         return JVMID.localID();
      }
   }

   private void getMembers() {
      weblogic.cluster.ClusterServices var1 = ClusterServiceActivator.INSTANCE.getClusterService();
      if (var1 != null) {
         synchronized(this.memberslock) {
            Collection var3 = var1.getRemoteMembers();
            if (var3 != null) {
               this.members = (ClusterMemberInfo[])((ClusterMemberInfo[])var3.toArray(new ClusterMemberInfo[0]));
               this.current = 0;
            }

            if (this.serverSet.size() > 0) {
               this.secondary = (JVMID)this.serverSet.toArray()[0];
            }
         }
      }

   }

   protected JVMID getNextMemberRandom() {
      JVMID var1 = null;
      synchronized(this.memberslock) {
         int var3 = this.members.length;
         double var4 = Math.random() * (double)var3 + 0.5;
         int var6 = (int)Math.round(var4) - 1;
         var1 = (JVMID)this.members[var6].identity();
         return var1;
      }
   }

   protected JVMID getNextMemberPrimarySecondary() {
      synchronized(this.secondarylock) {
         return this.secondary != null ? this.secondary : JVMID.localID();
      }
   }
}
