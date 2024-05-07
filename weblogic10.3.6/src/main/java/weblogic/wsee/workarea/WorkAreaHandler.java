package weblogic.wsee.workarea;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.wsee.util.Verbose;

public class WorkAreaHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(WorkAreaHandler.class);
   private static final boolean skipWorkAreaHeader = Boolean.getBoolean("weblogic.wsee.workarea.skipWorkAreaHeader");

   public QName[] getHeaders() {
      return WorkAreaConstants.WORK_HEADERS;
   }

   protected static final WorkContextMapInterceptor getContext() {
      return WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
   }

   protected boolean hasContext(int var1) {
      return this.hasContext(var1, false);
   }

   protected boolean hasContext(int var1, boolean var2) {
      if (skipWorkAreaHeader) {
         return false;
      } else {
         WorkContextMap var3 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
         if (var3 == null) {
            return false;
         } else {
            Iterator var4 = var3.keys();

            String var5;
            do {
               do {
                  if (!var4.hasNext()) {
                     return false;
                  }

                  var5 = (String)var4.next();
               } while((var3.getPropagationMode(var5) & var1) == 0);
            } while(var2 && (var3.getPropagationMode(var5) & 256) != 0);

            return true;
         }
      }
   }
}
