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

public final class BESessionStartRequest extends Request implements Externalizable {
   static final long serialVersionUID = 7937742897977941049L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;

   public BESessionStartRequest(JMSID var1) {
      super(var1, 14352);
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BESessionStartRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(1);
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
      }
   }
}
