package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Response;

public final class FETemporaryDestinationCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = 8807310522019572947L;
   private static final byte EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private DestinationImpl destination;

   public FETemporaryDestinationCreateResponse(DestinationImpl var1) {
      this.destination = var1;
   }

   public DestinationImpl getDestination() {
      return this.destination;
   }

   public FETemporaryDestinationCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeByte(1);
      super.writeExternal(var1);
      this.destination.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         super.readExternal(var1);
         this.destination = new DestinationImpl();
         this.destination.readExternal(var1);
      }
   }
}
