package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import weblogic.wtc.jatmi.TPException;

public final class GwtUtil {
   public static String getLocalDomId(String var0) throws TPException {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/GwtUtil/getLocalDomId/0");
      }

      if (var0.equals("")) {
         throw new TPException(4);
      } else {
         if (var1) {
            ntrace.doTrace("/GwtUtil/getLocalDomId/10" + var0);
         }

         TDMRemoteTDomain var2 = WTCService.getWTCService().getRemoteTDomain(var0);
         if (null == var2) {
            if (var1) {
               ntrace.doTrace("*]/GwtUtil/getLocalDomId/20");
            }

            throw new TPException(6);
         } else {
            if (var1) {
               ntrace.doTrace("]/GwtUtil/getLocalDomId/30");
            }

            return var2.getLocalAccessPointObject().getAccessPoint();
         }
      }
   }
}
