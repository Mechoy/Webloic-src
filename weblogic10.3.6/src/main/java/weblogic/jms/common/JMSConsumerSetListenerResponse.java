package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public final class JMSConsumerSetListenerResponse extends Response implements Externalizable {
   static final long serialVersionUID = 1743355884850281257L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private long sequenceNumber;

   public JMSConsumerSetListenerResponse(long var1) {
      this.sequenceNumber = var1;
   }

   public JMSConsumerSetListenerResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeInt(1);
      super.writeExternal(var1);
      var1.writeLong(this.sequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.sequenceNumber = var1.readLong();
      }
   }
}
