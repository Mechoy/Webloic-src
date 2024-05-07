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

public final class FESessionSetRedeliveryDelayRequest extends Request implements Externalizable {
   static final long serialVersionUID = 2214133860017221888L;
   private long redeliveryDelay;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;

   public FESessionSetRedeliveryDelayRequest(JMSID var1, long var2) {
      super(var1, 7176);
      this.redeliveryDelay = var2;
   }

   public final long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FESessionSetRedeliveryDelayRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.redeliveryDelay);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.redeliveryDelay = var1.readLong();
      }
   }
}
