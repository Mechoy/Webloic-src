package weblogic.wtc.gwt;

import com.bea.core.jatmi.internal.TCTaskHelper;
import java.util.TimerTask;

class OatmialCommitterTimer extends TimerTask {
   private OatmialCommitter myCommitter;

   public OatmialCommitterTimer(OatmialCommitter var1) {
      this.myCommitter = var1;
   }

   public void run() {
      TCTaskHelper.schedule(this.myCommitter);
   }
}
