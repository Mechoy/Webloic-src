package weblogic.security;

import com.bea.security.utils.random.SecureRandomData;

public final class Salt {
   public static byte[] getRandomBytes(int var0) {
      return SecureRandomData.getInstance().getRandomBytes(var0);
   }
}
