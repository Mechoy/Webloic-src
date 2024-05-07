package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.CompletionRequest;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class BEOrderUpdateParentRequest extends Request implements Externalizable {
   static final long serialVersionUID = 4890857082204074715L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private BEOrderUpdateRequest orderUpdateRequest;
   private CompletionRequest completionRequest;

   BEOrderUpdateParentRequest(JMSID var1, BEOrderUpdateRequest var2, CompletionRequest var3) {
      super(var1, 18178);
      this.orderUpdateRequest = var2;
      this.completionRequest = var3;
   }

   CompletionRequest getCompletionRequest() {
      return this.completionRequest;
   }

   BEOrderUpdateRequest getOrderUpdate() {
      return this.orderUpdateRequest;
   }

   public int remoteSignature() {
      return 34;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BEOrderUpdateParentRequest() {
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
