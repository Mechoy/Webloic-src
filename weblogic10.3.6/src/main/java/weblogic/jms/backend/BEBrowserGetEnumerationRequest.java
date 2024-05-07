package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSBrowserGetEnumerationResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEBrowserGetEnumerationRequest extends Request implements Externalizable {
   static final long serialVersionUID = -5460900869537242019L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;

   public BEBrowserGetEnumerationRequest(JMSID var1) {
      super(var1, 8722);
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSBrowserGetEnumerationResponse();
   }

   public BEBrowserGetEnumerationRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
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
