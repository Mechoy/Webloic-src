package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FEDestinationCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = -4660550797390037314L;
   private String destinationName;
   private int destinationType;
   private boolean temporary;
   private JMSServerId backEndId;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int TEMPORARY_MASK = 256;
   static final int START = 0;
   static final int CONTINUE = 1;

   public FEDestinationCreateRequest(String var1, int var2, boolean var3) {
      super((JMSID)null, 3841);
      this.destinationName = var1;
      this.destinationType = var2;
      this.temporary = var3;
   }

   public final String getDestinationName() {
      return this.destinationName;
   }

   public final void setDestinationName(String var1) {
      this.destinationName = var1;
   }

   public final int getDestType() {
      return this.destinationType;
   }

   public final JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public final void setBackEndId(JMSServerId var1) {
      this.backEndId = var1;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSDestinationCreateResponse();
   }

   public FEDestinationCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.temporary) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeUTF(this.destinationName);
      var1.writeInt(this.destinationType);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.destinationName = var1.readUTF();
         this.destinationType = var1.readInt();
         this.temporary = (var2 & 256) != 0;
      }
   }
}
