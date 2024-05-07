package weblogic.messaging.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.PeerInfoableObjectOutput;
import weblogic.jms.common.JMSUtilities;

public class Response implements Externalizable {
   static final long serialVersionUID = -4057384450154825617L;
   public static final boolean CHECK = false;
   private static final byte EXTVERSION = 1;
   private static final byte VERSION_MASK = 31;
   private static final byte CORRELATION_ID_MASK = 32;
   private static final byte PAYLOAD_MASK = 64;
   private PeerInfo peerInfo;

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if ((var2 & 31) != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         if ((var2 & 32) != 0) {
            var1.readLong();
         }

         if ((var2 & 64) != 0) {
            var1.readObject();
         }

      }
   }

   public static void instanceOf(Object var0, Class var1) {
      if (!var1.isInstance(var0)) {
         if (var0 != null || weblogic.jms.dispatcher.VoidResponse.class != var1) {
            throw new AssertionError("" + var0 + " is not an instance of " + var1.getName());
         }
      }
   }

   public final void setPeerInfo(PeerInfo var1) {
      this.peerInfo = var1;
   }

   public final ObjectOutput getVersionedStream(ObjectOutput var1) {
      if (var1 instanceof PeerInfoable) {
         return var1;
      } else {
         assert this.peerInfo != null;

         return new PeerInfoableObjectOutput(this.peerInfo, var1);
      }
   }
}
