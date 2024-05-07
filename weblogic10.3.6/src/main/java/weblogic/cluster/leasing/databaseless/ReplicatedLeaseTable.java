package weblogic.cluster.leasing.databaseless;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ReplicatedLeaseTable implements Map {
   private ClusterLeader leader;
   private LeaseView leaseView;
   private Map localMap;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   ReplicatedLeaseTable(ClusterLeader var1, LeaseView var2) {
      this.leader = var1;
      this.leaseView = var2;
      this.localMap = var2.getLeaseTableReplica();
   }

   public Object put(Object var1, Object var2) {
      if (!$assertionsDisabled && this.leader == null) {
         throw new AssertionError();
      } else if (var2 != null && var2.equals(this.localMap.get(var1))) {
         return this.localMap.put(var1, var2);
      } else {
         LeaseTableUpdateMessage var3 = LeaseTableUpdateMessage.createPutMessage(this.leader.getLeaderInformation(), this.leaseView.getVersionNumber() + 1L, (Serializable)var1, (Serializable)var2);
         if (this.leader.sendGroupMessage(var3)) {
            this.leaseView.incrementVersionNumber();
            return this.localMap.put(var1, var2);
         } else {
            throw new LeaseTableUpdateException("Unable to send lease table PUT to remote servers");
         }
      }
   }

   public Object remove(Object var1) {
      if (!$assertionsDisabled && this.leader == null) {
         throw new AssertionError();
      } else {
         LeaseTableUpdateMessage var2 = LeaseTableUpdateMessage.createRemoveMessage(this.leader.getLeaderInformation(), this.leaseView.getVersionNumber() + 1L, (Serializable)var1);
         if (this.leader.sendGroupMessage(var2)) {
            this.leaseView.incrementVersionNumber();
            return this.localMap.remove(var1);
         } else {
            throw new LeaseTableUpdateException("Unable to send lease table REMOVE to remote servers");
         }
      }
   }

   public void putAll(Map var1) {
      this.localMap.putAll(var1);
   }

   public void clear() {
      this.localMap.clear();
   }

   public Set keySet() {
      return this.localMap.keySet();
   }

   public Collection values() {
      return this.localMap.values();
   }

   public Set entrySet() {
      return this.localMap.entrySet();
   }

   public int size() {
      return this.localMap.size();
   }

   public boolean isEmpty() {
      return this.localMap.isEmpty();
   }

   public boolean containsKey(Object var1) {
      return this.localMap.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.localMap.containsValue(var1);
   }

   public Object get(Object var1) {
      return this.localMap.get(var1);
   }

   static {
      $assertionsDisabled = !ReplicatedLeaseTable.class.desiredAssertionStatus();
   }
}
