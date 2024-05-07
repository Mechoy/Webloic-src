package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.ConnectionConsumer;
import weblogic.jms.client.JMSConnectionConsumer;
import weblogic.jms.dispatcher.Response;

public final class JMSConnectionConsumerCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = -3526422355578670715L;
   private static final byte EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private ConnectionConsumer connectionConsumer;

   public JMSConnectionConsumerCreateResponse(ConnectionConsumer var1) {
      this.connectionConsumer = var1;
   }

   public final ConnectionConsumer getConnectionConsumer() {
      return this.connectionConsumer;
   }

   public final void setConnectionConsumer(ConnectionConsumer var1) {
      this.connectionConsumer = var1;
   }

   public JMSConnectionConsumerCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeInt(1);
      super.writeExternal(var1);
      ((JMSConnectionConsumer)this.connectionConsumer).writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.connectionConsumer = new JMSConnectionConsumer();
         ((JMSConnectionConsumer)this.connectionConsumer).readExternal(var1);
      }
   }
}
