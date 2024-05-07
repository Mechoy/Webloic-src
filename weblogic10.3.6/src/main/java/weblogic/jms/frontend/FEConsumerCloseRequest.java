package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FEConsumerCloseRequest extends Request implements Externalizable {
   static final long serialVersionUID = -1696547268688497887L;
   private long lastSequenceNumber;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int SEQUENCE_NUMBER_MASK = 256;

   public FEConsumerCloseRequest(JMSID var1, long var2) {
      super(var1, 2570);
      this.lastSequenceNumber = var2;
   }

   public long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FEConsumerCloseRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.lastSequenceNumber != 0L) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if (this.lastSequenceNumber != 0L) {
         var1.writeLong(this.lastSequenceNumber);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 256) != 0) {
            this.lastSequenceNumber = var1.readLong();
         }

      }
   }
}
