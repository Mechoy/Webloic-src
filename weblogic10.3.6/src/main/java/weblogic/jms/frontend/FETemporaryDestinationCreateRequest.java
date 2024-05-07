package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FETemporaryDestinationCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 8448408102807078690L;
   private int destinationType;
   private boolean temporary;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int TEMPORARY_MASK = 256;

   public FETemporaryDestinationCreateRequest(JMSID var1, int var2, boolean var3) {
      super(var1, 7687);
      this.destinationType = var2;
      this.temporary = var3;
   }

   public final int getDestType() {
      return this.destinationType;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new FETemporaryDestinationCreateResponse();
   }

   public FETemporaryDestinationCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.temporary) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeInt(this.destinationType);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.destinationType = var1.readInt();
         this.temporary = (var2 & 256) != 0;
      }
   }
}
