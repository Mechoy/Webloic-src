package weblogic.xml.crypto.wss;

import weblogic.wsee.security.WssRuntime;

public class SecurityValidatorFactory {
   public static SecurityValidator newSecurityValidator(WSSecurityContext var0) {
      return new SecurityValidatorImpl(var0);
   }

   static {
      WssRuntime.init();
   }
}
