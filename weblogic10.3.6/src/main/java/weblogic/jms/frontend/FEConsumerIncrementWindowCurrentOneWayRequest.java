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

public final class FEConsumerIncrementWindowCurrentOneWayRequest extends Request implements Externalizable {
   static final long serialVersionUID = 1485628795862457005L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int CLIENTRESPONSIBLEFORACKNOWLEDGE = 256;
   private int windowIncrement;
   private transient boolean clientResponsibleForAcknowledge;

   public FEConsumerIncrementWindowCurrentOneWayRequest(JMSID var1, int var2, boolean var3) {
      super(var1, 17418);
      this.windowIncrement = var2;
      this.clientResponsibleForAcknowledge = var3;
   }

   public final int getWindowIncrement() {
      return this.windowIncrement;
   }

   public final boolean getClientResponsibleForAcknowledge() {
      return this.clientResponsibleForAcknowledge;
   }

   public int remoteSignature() {
      return 64;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FEConsumerIncrementWindowCurrentOneWayRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.clientResponsibleForAcknowledge) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeInt(this.windowIncrement);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         if ((var2 & 256) != 0) {
            this.clientResponsibleForAcknowledge = true;
         } else {
            this.clientResponsibleForAcknowledge = false;
         }

         super.readExternal(var1);
         this.windowIncrement = var1.readInt();
      }
   }
}
