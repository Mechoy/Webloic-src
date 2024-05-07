package weblogic.messaging.common;

import java.io.StreamCorruptedException;
import java.security.SecureRandom;
import weblogic.messaging.MessagingLogger;

public class MessagingUtilities {
   public static StreamCorruptedException versionIOException(int var0, int var1, int var2) {
      return new StreamCorruptedException(MessagingLogger.logUnsupportedClassVersionLoggable(var0, var1, var2).getMessage());
   }

   public static final int calcObjectSize(Object var0) {
      if (var0 == null) {
         return 2;
      } else if (var0 instanceof Integer) {
         return 6;
      } else if (var0 instanceof String) {
         return 4 + (((String)var0).length() << 2);
      } else if (var0 instanceof Long) {
         return 10;
      } else if (var0 instanceof Boolean) {
         return 3;
      } else if (var0 instanceof Byte) {
         return 3;
      } else if (var0 instanceof Short) {
         return 4;
      } else if (var0 instanceof Float) {
         return 6;
      } else if (var0 instanceof Double) {
         return 10;
      } else {
         return var0 instanceof byte[] ? ((byte[])((byte[])var0)).length + 6 : 0;
      }
   }

   public static final int getSeed() {
      int var0 = 0;
      SecureRandom var1 = new SecureRandom();
      byte[] var2 = new byte[4];
      var1.nextBytes(var2);

      for(int var3 = 0; var3 < 4; ++var3) {
         var0 = var0 << 4 | var2[var3] & 255;
      }

      return var0;
   }
}
