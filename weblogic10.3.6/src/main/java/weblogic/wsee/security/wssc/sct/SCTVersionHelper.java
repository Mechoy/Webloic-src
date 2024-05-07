package weblogic.wsee.security.wssc.sct;

import weblogic.wsee.security.wssc.base.sct.SCTokenHandlerBase;
import weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler;

public class SCTVersionHelper {
   private static String default_wssc_xmlns = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";

   public static SCTokenHandlerBase newSCTokenHandler(String var0) {
      if (var0 == null) {
         var0 = default_wssc_xmlns;
      }

      return (SCTokenHandlerBase)(var0.equals("http://schemas.xmlsoap.org/ws/2005/02/sc") ? new SCTokenHandler() : new weblogic.wsee.security.wssc.v13.sct.SCTokenHandler());
   }
}
