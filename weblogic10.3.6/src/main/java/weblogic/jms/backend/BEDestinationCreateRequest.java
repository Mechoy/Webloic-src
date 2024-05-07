package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEDestinationCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 2406860847787652474L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int JMS_CREATE_IDENTIFIER = 256;
   private String destinationName;
   private int destType;
   private boolean forCreateDestination;

   public BEDestinationCreateRequest(JMSID var1, String var2, int var3, boolean var4) {
      super(var1, 11534);
      this.destinationName = var2;
      this.destType = var3;
      this.forCreateDestination = var4;
   }

   public final String getDestinationName() {
      return this.destinationName;
   }

   public final int getDestType() {
      return this.destType;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new JMSDestinationCreateResponse();
   }

   public BEDestinationCreateRequest() {
   }

   boolean isForCreateDestination() {
      return this.forCreateDestination;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.forCreateDestination) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeUTF(this.destinationName);
      var1.writeInt(this.destType);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         this.forCreateDestination = (var2 & 256) != 0;
         super.readExternal(var1);
         this.destinationName = var1.readUTF();
         this.destType = var1.readInt();
      }
   }
}
