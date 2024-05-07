package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSUtilities;

public class UpgradeDurableTopicMessageInfo implements Externalizable {
   private static final byte EXTVERSION = 4;
   static final long serialVersionUID = -17931833902731539L;
   private JMSMessageId messageId;
   private HashMap receiveTrans;
   private byte[] hasMessageSlots;
   private byte[] redeliveredSlots;
   private int consumerCount;
   private int pageCount;
   private boolean isMessageExpired;
   private boolean isMoveMessageOperation;
   private long generation;

   private static final int roundUp8(int var0) {
      return var0 + 8 - 1 & -8;
   }

   void setMessageId(JMSMessageId var1) {
      this.messageId = var1;
   }

   public JMSMessageId getMessageId() {
      return this.messageId;
   }

   long getGeneration() {
      return this.generation;
   }

   boolean isOlderThanConsumer(UpgradeConsumer var1) {
      return var1.getDurableSlot() >= this.hasMessageSlots.length << 3 || this.messageId.compareTime(var1.getTimestampId()) < 0;
   }

   int getNextOccupiedSlot(int var1) {
      int var2 = this.hasMessageSlots.length << 3;

      do {
         ++var1;
         if (var1 >= var2) {
            return -1;
         }
      } while((this.hasMessageSlots[var1 >> 3] & 1 << (var1 & 7)) == 0);

      return var1;
   }

   boolean isRedelivered(UpgradeConsumer var1) {
      if (this.redeliveredSlots == null) {
         return false;
      } else {
         int var2 = var1.getDurableSlot();
         return (this.redeliveredSlots[var2 >> 3] & 1 << (var2 & 7)) != 0;
      }
   }

   UpgradeXAXid getReceiveTran(UpgradeConsumer var1) {
      return this.receiveTrans == null ? null : (UpgradeXAXid)this.receiveTrans.get(var1.getDurableSlot());
   }

   public void migrateConsumer(int var1, boolean var2) {
      ++this.consumerCount;
      byte[] var10000 = this.hasMessageSlots;
      var10000[var1 >> 3] = (byte)(var10000[var1 >> 3] | 1 << (var1 & 7));
      if (this.redeliveredSlots == null && var2) {
         this.redeliveredSlots = new byte[this.hasMessageSlots.length];
      }

      if (this.redeliveredSlots != null) {
         if (var2) {
            var10000 = this.redeliveredSlots;
            var10000[var1 >> 3] = (byte)(var10000[var1 >> 3] | 1 << (var1 & 7));
         } else {
            var10000 = this.redeliveredSlots;
            var10000[var1 >> 3] = (byte)(var10000[var1 >> 3] & ~(1 << (var1 & 7)));
         }
      }

   }

   public synchronized void writeExternal(ObjectOutput var1) {
      throw new AssertionError("Can't call writeExternal on an upgrade object");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 4) {
         throw JMSUtilities.versionIOException(var2, 4, 4);
      } else {
         this.generation = var1.readLong();
         this.messageId = new JMSMessageId();
         this.messageId.readExternal(var1);
         this.consumerCount = var1.readInt();
         int var3 = var1.readInt();
         this.hasMessageSlots = new byte[var3];
         var1.readFully(this.hasMessageSlots);
         if (var1.readBoolean()) {
            this.redeliveredSlots = new byte[var3];
            var1.readFully(this.redeliveredSlots);
         }

         int var4 = var1.readInt();
         if (var4 > 0) {
            this.receiveTrans = new HashMap();
         }

         while(var4-- > 0) {
            Integer var5 = new Integer(var1.readInt());
            UpgradeXAXid var6 = new UpgradeXAXid();
            var6.readExternal(var1);
            this.receiveTrans.put(var5, var6);
         }

      }
   }

   public String toString() {
      synchronized(this) {
         return "(dtmi messageId=" + this.messageId + " generation=" + this.generation + " consumerCount=" + this.consumerCount + "pageCount=" + this.pageCount + ")";
      }
   }
}
