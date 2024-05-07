package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEBrowserCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 7172028677958484592L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int SELECTOR_MASK = 256;
   private JMSID destinationId;
   private String selector;

   public BEBrowserCreateRequest(JMSID var1, JMSID var2, String var3) {
      super(var1, 8464);
      this.destinationId = var2;
      this.selector = var3;
   }

   public final JMSID getDestinationId() {
      return this.destinationId;
   }

   public final String getSelector() {
      return this.selector;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSBrowserCreateResponse();
   }

   public BEBrowserCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.selector != null) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      this.destinationId.writeExternal(var1);
      if (this.selector != null) {
         var1.writeUTF(this.selector);
      }

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
         if ((var2 & 256) != 0) {
            this.selector = var1.readUTF();
         }

      }
   }
}
