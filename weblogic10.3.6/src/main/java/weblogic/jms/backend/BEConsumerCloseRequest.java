package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class BEConsumerCloseRequest extends Request implements Externalizable {
   static final long serialVersionUID = -785311104852028751L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private long lastSequenceNumber;

   public BEConsumerCloseRequest(JMSID var1, long var2) {
      super(var1, 10001);
      this.lastSequenceNumber = var2;
   }

   public final long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BEConsumerCloseRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.lastSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.lastSequenceNumber = var1.readLong();
      }
   }
}
