package weblogic.wsee.reliability2.exception;

import java.util.Iterator;
import java.util.List;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.reliability.faults.WsrmFaultException;

public class WsrmExceptionUtil {
   public static boolean isPermanentSendFailure(Throwable var0) {
      if (WsUtil.isPermanentSendFailure(var0)) {
         return true;
      } else {
         List var1 = WsUtil.findNestedThrowableClasses(var0);
         Iterator var2 = var1.iterator();

         Class var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (Class)var2.next();
         } while(!WsrmFaultException.class.isAssignableFrom(var3));

         return true;
      }
   }
}
