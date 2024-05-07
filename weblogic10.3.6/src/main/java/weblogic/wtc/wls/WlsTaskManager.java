package weblogic.wtc.wls;

import com.bea.core.jatmi.intf.TCTask;
import com.bea.core.jatmi.intf.TCTaskManager;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;
import weblogic.wtc.jatmi.TPException;

public final class WlsTaskManager implements TCTaskManager {
   private static WorkManager _wm = null;

   public WlsTaskManager() {
      _wm = WorkManagerFactory.getInstance().getSystem();
   }

   public void initialize() throws TPException {
   }

   public void shutdown(int var1) {
      _wm = null;
   }

   public void schedule(TCTask var1) {
      if (_wm != null) {
         _wm.schedule(new WlsTaskHandler(var1));
      }

   }
}
