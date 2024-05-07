package weblogic.xml.crypto.wss;

import weblogic.wsee.security.WssRuntime;

public class SecurityBuilderFactory {
   public static SecurityBuilder newSecurityBuilder(WSSecurityContext var0) {
      return new SecurityBuilderImpl(var0);
   }

   static {
      WssRuntime.init();
   }
}
