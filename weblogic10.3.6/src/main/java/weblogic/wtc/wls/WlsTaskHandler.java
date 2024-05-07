package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCTask;
import weblogic.work.WorkAdapter;

public final class WlsTaskHandler extends WorkAdapter {
   private TCTask task;
   private static boolean debug = false;

   public WlsTaskHandler(TCTask var1) {
      this.task = var1;
   }

   public void run() {
      if (debug) {
         ntrace.doTrace("WlsTaskHandler: start execution");
      }

      while(this.task.execute() == 2) {
         if (debug) {
            ntrace.doTrace("WlsTaskHandler: continue processing");
         }
      }

      if (debug) {
         ntrace.doTrace("WlsTaskHandler: complete processing");
      }

   }
}
