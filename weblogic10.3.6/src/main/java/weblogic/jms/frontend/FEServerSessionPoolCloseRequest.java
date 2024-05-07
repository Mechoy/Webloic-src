package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FEServerSessionPoolCloseRequest extends Request implements Externalizable {
   static final long serialVersionUID = 4982031104004704411L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSServerId backEndId;
   private JMSID serverSessionPoolId;

   public FEServerSessionPoolCloseRequest(JMSServerId var1, JMSID var2) {
      super((JMSID)null, 5633);
      this.backEndId = var1;
      this.serverSessionPoolId = var2;
   }

   public final JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public final JMSID getServerSessionPoolId() {
      return this.serverSessionPoolId;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FEServerSessionPoolCloseRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.backEndId.writeExternal(var1);
      this.serverSessionPoolId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
         this.serverSessionPoolId = new JMSID();
         this.serverSessionPoolId.readExternal(var1);
      }
   }
}
