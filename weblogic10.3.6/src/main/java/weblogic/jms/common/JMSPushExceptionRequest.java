package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class JMSPushExceptionRequest extends Request implements Externalizable {
   static final long serialVersionUID = -8924769504929515114L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSException exception;

   public JMSPushExceptionRequest(int var1, JMSID var2, JMSException var3) {
      super(var2, 15360 | var1);
      this.exception = var3;
   }

   public void setInvocableType(int var1) {
      this.methodId = this.methodId & 16776960 & var1;
   }

   public JMSException getException() {
      return this.exception;
   }

   public int remoteSignature() {
      return 64;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public JMSPushExceptionRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeObject(this.exception);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.exception = (JMSException)var1.readObject();
      }
   }
}
