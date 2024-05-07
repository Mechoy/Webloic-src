package weblogic.wsee.security.wssc.faults;

import weblogic.wsee.security.wssc.base.faults.WSCFaultException;
import weblogic.wsee.security.wssc.v200502.faults.RenewNeededException;
import weblogic.wsee.security.wssc.v200502.faults.UnableToRenewException;

public class FaultVersionHelper {
   private static String default_wssc_xmlns = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";

   public static WSCFaultException newRenewNeededException(String var0, String var1) {
      if (var0 == null) {
         var0 = default_wssc_xmlns;
      }

      return (WSCFaultException)(var0.equals("http://schemas.xmlsoap.org/ws/2005/02/sc") ? new RenewNeededException(var1) : new weblogic.wsee.security.wssc.v13.faults.RenewNeededException(var1));
   }

   public static WSCFaultException newUnableToRenewException(String var0, String var1) {
      if (var0 == null) {
         var0 = default_wssc_xmlns;
      }

      return (WSCFaultException)(var0.equals("http://schemas.xmlsoap.org/ws/2005/02/sc") ? new UnableToRenewException(var1) : new weblogic.wsee.security.wssc.v13.faults.UnableToRenewException(var1));
   }
}
