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

public final class BEConnectionConsumerCloseRequest extends Request implements Externalizable {
   static final long serialVersionUID = -284970159934936686L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSID connectionConsumerId;

   public BEConnectionConsumerCloseRequest(JMSID var1, JMSID var2) {
      super(var1, 8975);
      this.connectionConsumerId = var2;
   }

   public final JMSID getConnectionConsumerId() {
      return this.connectionConsumerId;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BEConnectionConsumerCloseRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.connectionConsumerId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.connectionConsumerId = new JMSID();
         this.connectionConsumerId.readExternal(var1);
      }
   }
}
