package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public final class JMSEnumerationNextElementResponse extends Response implements Externalizable {
   static final long serialVersionUID = -6840179225324539871L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int MESSAGE_MASK = 65280;
   private static final int MESSAGE_SHIFT = 8;
   private MessageImpl message;
   private int compressionThreshold = Integer.MAX_VALUE;

   public JMSEnumerationNextElementResponse(MessageImpl var1) {
      this.message = var1;
   }

   public final void setCompressionThreshold(int var1) {
      this.compressionThreshold = var1;
   }

   public final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   public final MessageImpl getMessage() {
      return this.message;
   }

   public JMSEnumerationNextElementResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
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

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         byte var4 = (byte)((var2 & '\uff00') >> 8);
         if (var4 != 0) {
            this.message = MessageImpl.createMessageImpl(var4);
            this.message.readExternal(var1);
         }

      }
   }
}
