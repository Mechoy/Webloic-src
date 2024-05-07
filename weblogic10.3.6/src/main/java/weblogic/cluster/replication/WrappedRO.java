package weblogic.cluster.replication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.rmi.spi.HostID;

final class WrappedRO {
   public static final byte PRIMARY_STATUS = 0;
   public static final byte SECONDARY_STATUS = 1;
   public static final int INITIAL_VERSION_NUMBER = 0;
   static final int RO_NOT_FOUND = -1;
   private final Map map = Collections.synchronizedMap(new HashMap(3));
   private final Map<Object, Integer> versionMap = Collections.synchronizedMap(new HashMap(3));
   private byte status;
   private HostID otherHost;
   private int version;
   private final ROInfo roInfo;
   private final ROID id;
   final int channelIndex;

   WrappedRO(Replicatable var1, ROID var2, byte var3, int var4) {
      this.roInfo = new ROInfo(var2);
      this.status = var3;
      this.version = var4;
      this.otherHost = null;
      this.id = var2;
      if (var3 == 0) {
         var1.becomePrimary(var2);
      } else {
         this.roInfo.setSecondaryROInfo(var1.becomeSecondary(var2));
      }

      this.addMapEntry(var1, var4);
      this.channelIndex = ReplicationManager.replicationChannels != null ? (var2.getValueAsInt() & Integer.MAX_VALUE) % ReplicationManager.replicationChannels.length : -1;
   }

   private void addMapEntry(Replicatable var1, int var2) {
      this.map.put(var1.getKey(), var1);
      this.versionMap.put(var1.getKey(), var2);
   }

   void addRO(Replicatable var1) {
      this.addMapEntry(var1, 1);
      if (this.status == 0) {
         var1.becomePrimary(this.id);
      } else {
         this.roInfo.setSecondaryROInfo(var1.becomeSecondary(this.id));
      }

   }

   void addRO(Replicatable var1, int var2) {
      this.addMapEntry(var1, var2);
      if (this.status == 0) {
         var1.becomePrimary(this.id);
      } else {
         this.roInfo.setSecondaryROInfo(var1.becomeSecondary(this.id));
      }

   }

   void removeAll() {
      this.map.clear();
      this.versionMap.clear();
   }

   boolean removeRO(Object var1) {
      this.map.remove(var1);
      this.versionMap.remove(var1);
      return this.map.size() == 0;
   }

   Replicatable getRO(Object var1) {
      return (Replicatable)this.map.get(var1);
   }

   ROID getID() {
      return this.roInfo.getROID();
   }

   ROInfo getROInfo() {
      return this.roInfo;
   }

   HostID getOtherHost() {
      return this.otherHost;
   }

   byte getStatus() {
      return this.status;
   }

   int getVersion() {
      return this.version;
   }

   HostID setOtherHost(HostID var1) {
      return this.otherHost = var1;
   }

   int getVersion(Object var1) {
      Integer var2 = (Integer)this.versionMap.get(var1);
      return var2 != null ? var2 : -1;
   }

   Object getSecondaryROInfo() {
      return this.roInfo.getSecondaryROInfo();
   }

   void setOtherHostInfo(Object var1) {
      this.roInfo.setSecondaryROInfo(var1);
   }

   final int incrementVersion(Object var1) {
      ++this.version;
      int var2 = this.getVersion(var1) + 1;
      this.versionMap.put(var1, var2);
      return var2;
   }

   final void decrementVersion(Object var1) {
      --this.version;
      this.versionMap.put(var1, this.getVersion(var1) - 1);
   }

   final int getNumROS() {
      return this.map.size();
   }

   final void ensureStatus(byte var1) {
      if (this.status != var1) {
         this.status = var1;
         if (var1 == 0) {
            this.roInfo.setSecondaryROInfo((Object)null);
            this.otherHost = null;
            this.changeStatus(true);
         } else {
            this.changeStatus(false);
         }
      }

   }

   private void changeStatus(boolean var1) {
      Iterator var2 = this.map.values().iterator();

      while(var2.hasNext()) {
         Replicatable var3 = (Replicatable)var2.next();
         if (var1) {
            var3.becomePrimary(this.roInfo.getROID());
         } else {
            this.roInfo.setSecondaryROInfo(var3.becomeSecondary(this.roInfo.getROID()));
         }
      }

   }

   Iterator keys() {
      return this.map.keySet().iterator();
   }

   Map getMap() {
      return this.map;
   }

   Map getVersionMap() {
      return this.versionMap;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof WrappedRO) {
         WrappedRO var2 = (WrappedRO)var1;
         return var2.id == this.id;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }
}
