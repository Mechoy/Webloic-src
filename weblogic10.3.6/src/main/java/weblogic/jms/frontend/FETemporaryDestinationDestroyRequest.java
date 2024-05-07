package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FETemporaryDestinationDestroyRequest extends Request implements Externalizable {
   static final long serialVersionUID = 1839581631272219969L;
   private JMSID destinationId;
   private transient JMSDispatcher dispatcher;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   static final int START = 0;
   static final int CONTINUE = 1;

   public FETemporaryDestinationDestroyRequest(JMSID var1, JMSID var2) {
      super(var1, 7431);
      this.destinationId = var2;
   }

   public final JMSID getDestinationId() {
      return this.destinationId;
   }

   public final JMSDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public final void setDispatcher(JMSDispatcher var1) {
      this.dispatcher = var1;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FETemporaryDestinationDestroyRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeInt(1);
      super.writeExternal(var1);
      this.destinationId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.destinationId = new JMSID();
         this.destinationId.readExternal(var1);
      }
   }
}
