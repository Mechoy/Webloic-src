package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

final class HeartbeatMessage implements GroupMessage, Externalizable {
   private static final boolean DEBUG = ClusterDebugLogger.isDebugEnabled();
   private static final long serialVersionUID = -7010984889884629879L;
   private static final long MAX_TIME_DIFF = 60000L;
   private long diff;
   ArrayList items;

   HeartbeatMessage(ArrayList var1) {
      this.items = var1;
   }

   public void execute(HostID var1) {
      MulticastManager.theOne().receiveHeartbeat(var1, this);
      if (this.diff > 60000L) {
         ClusterLogger.logMachineTimesOutOfSync(var1.objectToString(), this.diff / 1000L);
      }

   }

   public String toString() {
      return "Heartbeat with " + this.items.size() + " items";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((WLObjectOutput)var1).writeArrayList(this.items);
      if (DEBUG) {
         var1.writeLong(System.currentTimeMillis());
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.items = ((WLObjectInput)var1).readArrayList();
      if (DEBUG) {
         long var2 = var1.readLong();
         long var4 = System.currentTimeMillis();
         this.diff = Math.abs(var2 - var4);
      }

   }

   public HeartbeatMessage() {
   }
}
