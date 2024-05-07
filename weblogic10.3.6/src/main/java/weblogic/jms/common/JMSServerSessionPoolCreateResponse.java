package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.ServerSessionPool;
import weblogic.jms.client.JMSServerSessionPool;
import weblogic.jms.dispatcher.Response;

public final class JMSServerSessionPoolCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = -1327407705330866950L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private ServerSessionPool serverSessionPool;

   public JMSServerSessionPoolCreateResponse(ServerSessionPool var1) {
      this.serverSessionPool = var1;
   }

   public final ServerSessionPool getServerSessionPool() {
      return this.serverSessionPool;
   }

   public JMSServerSessionPoolCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(1);
      super.writeExternal(var1);
      ((JMSServerSessionPool)this.serverSessionPool).writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.serverSessionPool = new JMSServerSessionPool();
         ((JMSServerSessionPool)this.serverSessionPool).readExternal(var1);
      }
   }
}
