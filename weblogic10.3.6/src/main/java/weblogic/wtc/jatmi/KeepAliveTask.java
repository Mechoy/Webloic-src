package weblogic.wtc.jatmi;

import java.util.TimerTask;

class KeepAliveTask extends TimerTask {
   private dsession myDSession;

   KeepAliveTask(dsession var1) {
      this.myDSession = var1;
   }

   public void run() {
      this.myDSession.sendKeepAliveRequest();
   }
}
