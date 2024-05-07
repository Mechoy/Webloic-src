package weblogic.wsee.security;

import weblogic.xml.crypto.wss.STRTransform;

public class WssRuntime {
   public static void init() {
   }

   private static void initInternal() {
      STRTransform.init();
   }

   static {
      initInternal();
   }
}
