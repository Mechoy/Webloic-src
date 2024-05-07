package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Response;

public final class BEServerSessionGetResponse extends Response implements Externalizable {
   static final long serialVersionUID = 4398629545912724093L;
   private static final byte EXTVERSION = 1;
   private BEServerSession serverSession;

   public BEServerSessionGetResponse(BEServerSession var1) {
      this.serverSession = var1;
   }

   public final BEServerSession getServerSession() {
      return this.serverSession;
   }

   public BEServerSessionGetResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      super.writeExternal(var1);
      this.serverSession.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         super.readExternal(var1);
         this.serverSession = new BEServerSession();
         this.serverSession.readExternal(var1);
      }
   }
}
