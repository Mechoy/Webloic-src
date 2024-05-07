package weblogic.messaging.saf.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.server.SequenceData;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;

public class SAFSequenceData extends SequenceData {
   static final long serialVersionUID = -2611753600731762875L;
   private static final int EXTVERSION1 = 1;
   private static final int VERSION_MASK = 255;
   private static final int _HASINFO = 256;
   private static final int _HASLASTMSGSEQUENCENUMBER = 512;
   private SAFConversationInfo info;
   private long lastMsgSequenceNumber = Long.MAX_VALUE;

   public SAFSequenceData(SAFConversationInfo var1) {
      this.info = var1;
      this.setUnitOfOrder(var1.getConversationName());
   }

   public SAFSequenceData() {
   }

   public SAFConversationInfo getConversationInfo() {
      return this.info;
   }

   public void setConversationInfo(SAFConversationInfo var1) {
      this.info = var1;
   }

   public synchronized long getLastMsgSequenceNumber() {
      return this.lastMsgSequenceNumber;
   }

   public synchronized void setLastMsgSequenceNumber(long var1) {
      this.lastMsgSequenceNumber = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof SAFSequenceData)) {
         return false;
      } else {
         SequenceData var2 = (SequenceData)var1;
         if (this.info != null && !this.info.equals(((SAFSequenceData)var1).getConversationInfo())) {
            return false;
         } else if (((SAFSequenceData)var1).getConversationInfo() != null && !((SAFSequenceData)var1).getConversationInfo().equals(this.info)) {
            return false;
         } else {
            return this.lastMsgSequenceNumber == ((SAFSequenceData)var1).getLastMsgSequenceNumber();
         }
      }
   }

   public int hashCode() {
      int var1 = super.hashCode();
      if (this.info != null) {
         var1 |= this.info.hashCode();
      }

      if (this.lastMsgSequenceNumber != Long.MAX_VALUE) {
         var1 = (int)((long)var1 | this.lastMsgSequenceNumber);
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      super.writeExternal(var1);
      if (this.info != null) {
         var2 |= 256;
      }

      if (this.lastMsgSequenceNumber != Long.MAX_VALUE) {
         var2 |= 512;
      }

      var1.writeInt(var2);
      if (this.info != null) {
         this.info.writeExternal(var1);
      }

      if (this.lastMsgSequenceNumber != 0L) {
         var1.writeLong(this.lastMsgSequenceNumber);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         if ((var2 & 256) != 0) {
            this.info = new SAFConversationInfoImpl();
            this.info.readExternal(var1);
         }

         if ((var2 & 512) != 0) {
            this.lastMsgSequenceNumber = var1.readLong();
         }

      }
   }
}
