package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Response;

public final class BEConsumerIsActiveResponse extends Response implements Externalizable {
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int ISACTIVE_MASK = 256;
   static final long serialVersionUID = 7162255911562909284L;
   public boolean consumerIsActive;

   public BEConsumerIsActiveResponse(boolean var1) {
      this.consumerIsActive = var1;
   }

   public BEConsumerIsActiveResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.consumerIsActive) {
         var2 |= 256;
      }

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
         this.consumerIsActive = (var2 & 256) != 0;
      }
   }
}
