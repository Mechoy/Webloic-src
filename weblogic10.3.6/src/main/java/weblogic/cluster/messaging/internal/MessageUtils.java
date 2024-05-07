package weblogic.cluster.messaging.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public final class MessageUtils {
   public static Message getMessage(byte[] var0) throws IOException {
      ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
      ObjectInputStream var2 = new ObjectInputStream(var1);

      Message var3;
      try {
         var3 = (Message)var2.readObject();
      } catch (ClassNotFoundException var7) {
         throw new AssertionError(var7);
      } finally {
         var2.close();
      }

      return var3;
   }

   public static Message createMessage(byte[] var0, String var1, long var2) {
      return new MessageImpl(var0, var1, var2, System.currentTimeMillis());
   }
}
