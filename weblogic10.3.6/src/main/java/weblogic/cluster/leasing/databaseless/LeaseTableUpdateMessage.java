package weblogic.cluster.leasing.databaseless;

import java.io.Serializable;
import java.util.Map;
import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class LeaseTableUpdateMessage extends BaseClusterMessage {
   static final int PUT = 1;
   static final int REMOVE = 2;
   static final int PUT_ALL = 3;
   private final long version;
   private final int operation;
   private final Serializable key;
   private final Serializable value;
   private final Serializable map;
   private static final long serialVersionUID = -4700234497899619454L;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public LeaseTableUpdateMessage(ServerInformation var1, long var2, int var4, Serializable var5, Serializable var6) {
      super(var1, 5);
      this.version = var2;
      this.operation = var4;
      this.key = var5;
      this.value = var6;
      this.map = null;
   }

   public LeaseTableUpdateMessage(ServerInformation var1, long var2, int var4, Serializable var5) {
      super(var1, 5);
      this.version = var2;
      this.operation = var4;
      if (!$assertionsDisabled && !(var5 instanceof Serializable)) {
         throw new AssertionError();
      } else {
         this.map = var5;
         this.key = null;
         this.value = null;
      }
   }

   public static LeaseTableUpdateMessage createPutMessage(ServerInformation var0, long var1, Serializable var3, Serializable var4) {
      return new LeaseTableUpdateMessage(var0, var1, 1, var3, var4);
   }

   public static LeaseTableUpdateMessage createRemoveMessage(ServerInformation var0, long var1, Serializable var3) {
      return new LeaseTableUpdateMessage(var0, var1, 2, var3, (Serializable)null);
   }

   public static LeaseTableUpdateMessage createPutAllMessage(ServerInformation var0, long var1, Serializable var3) {
      return new LeaseTableUpdateMessage(var0, var1, 3, var3);
   }

   public long getVersion() {
      return this.version;
   }

   public int getOperation() {
      return this.operation;
   }

   public Serializable getKey() {
      return this.key;
   }

   public Serializable getValue() {
      return this.value;
   }

   public Map getMap() {
      return (Map)this.map;
   }

   public String toString() {
      return "[LeaseTableUpdateMessage] operation " + this.operation + ", version " + this.version + ", key " + this.key + ", value " + this.value + ", map " + this.map + "]";
   }

   static {
      $assertionsDisabled = !LeaseTableUpdateMessage.class.desiredAssertionStatus();
   }
}
