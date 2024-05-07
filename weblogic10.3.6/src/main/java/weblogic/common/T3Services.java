package weblogic.common;

import weblogic.rjvm.JVMID;
import weblogic.t3.srvr.T3Srvr;

/** @deprecated */
public final class T3Services {
   /** @deprecated */
   public static final T3ServicesDef getT3Services() {
      T3ServicesDef var0 = null;
      if (JVMID.localID().isServer()) {
         var0 = getServerServices();
         return var0;
      } else {
         throw new AssertionError("getT3Services() not available in a client");
      }
   }

   private static T3ServicesDef getServerServices() {
      return T3Srvr.getT3Srvr().getT3Services();
   }
}
