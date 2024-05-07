package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSUtilities;

public class UpgradeMoveMessagePendingDeleteRecord implements Externalizable {
   private static final byte EXTVERSION = 1;
   private static final int QUEUESLOT = -1;
   private static final int ALLTOPICSLOT = -2;
   static final long serialVersionUID = 68783746469679355L;
   private JMSMessageId messageId;
   private int durableConsumerSlot;

   boolean isQueueMessage() {
      return this.durableConsumerSlot == -1;
   }

   boolean appliesToAllConsumers() {
      return this.isQueueMessage() || this.durableConsumerSlot == -2;
   }

   int getSlot() {
      return this.durableConsumerSlot;
   }

   JMSMessageId getMessageId() {
      return this.messageId;
   }

   public void writeExternal(ObjectOutput var1) {
      throw new AssertionError("Cannot call writeExternal on an upgrade object");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.durableConsumerSlot = var1.readInt();
         this.messageId = new JMSMessageId();
         this.messageId.readExternal(var1);
      }
   }
}
