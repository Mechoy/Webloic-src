package weblogic.xml.crypto.wss11.internal;

import weblogic.wsee.security.WssRuntime;

public class SecurityValidatorFactory {
   public static SecurityValidator getSecurityValidator(WSS11Context var0) {
      return new SecurityValidatorImpl(var0);
   }

   static {
      WssRuntime.init();
   }
}
