package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEServerSessionGetRequest extends Request implements Externalizable {
   static final long serialVersionUID = -5838190690108762415L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSID serverSessionPoolId;

   public BEServerSessionGetRequest(JMSID var1, JMSID var2) {
      super(var1, 12302);
      this.serverSessionPoolId = var2;
   }

   public final JMSID getServerSessionPoolId() {
      return this.serverSessionPoolId;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new BEServerSessionGetResponse();
   }

   public BEServerSessionGetRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.serverSessionPoolId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.serverSessionPoolId = new JMSID();
         this.serverSessionPoolId.readExternal(var1);
      }
   }
}
