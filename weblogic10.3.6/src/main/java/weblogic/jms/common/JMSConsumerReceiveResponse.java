package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public class JMSConsumerReceiveResponse extends Response implements Externalizable {
   static final long serialVersionUID = -6762955361476059878L;
   private static final byte EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int MESSAGE_TYPE_MASK = 65280;
   private static final int MESSAGE_TYPE_SHIFT = 8;
   private static final int TRANSACTIONAL_FLAG = 65536;
   private static final int SEQUENCE_FLAG = 131072;
   private MessageImpl message;
   private long sequenceNumber;
   private boolean isTransactional;
   private int compressionThreshold = Integer.MAX_VALUE;

   public JMSConsumerReceiveResponse(MessageImpl var1, long var2, boolean var4) {
      this.message = var1;
      this.sequenceNumber = var2;
      this.isTransactional = var4;
   }

   public final void setCompressionThreshold(int var1) {
      this.compressionThreshold = var1;
   }

   public final boolean isTransactional() {
      return this.isTransactional;
   }

   public final MessageImpl getMessage() {
      return this.message;
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public final void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public JMSConsumerReceiveResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.isTransactional) {
         var2 |= 65536;
      }

      if (this.sequenceNumber != 0L) {
         var2 |= 131072;
      }

      if (this.message != null) {
         var2 |= this.message.getType() << 8;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if (this.message != null) {
         if (this.compressionThreshold == Integer.MAX_VALUE) {
            this.message.writeExternal(var1);
         } else {
            this.message.writeExternal(MessageImpl.createJMSObjectOutputWrapper(var1, this.compressionThreshold, true));
         }
      }

      if (this.sequenceNumber != 0L) {
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 65536) != 0) {
            this.isTransactional = true;
         }

         int var4 = (var2 & '\uff00') >> 8;
         if (var4 != 0) {
            this.message = MessageImpl.createMessageImpl((byte)var4);
            this.message.readExternal(var1);
         }

         if ((var2 & 131072) != 0) {
            this.sequenceNumber = var1.readLong();
         }

      }
   }
}
