package weblogic.cluster.messaging.internal;

import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class HttpPingProbe implements Probe {
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();

   public void invoke(ProbeContext var1) {
      boolean var2 = this.doHttpPing(var1.getSuspectedMemberInfo());
      if (var2) {
         var1.setNextAction(0);
         var1.setResult(1);
      } else {
         var1.setNextAction(1);
         var1.setResult(-1);
      }

   }

   protected boolean doHttpPing(SuspectedMemberInfo var1) {
      HttpPingRoutineImpl var2 = HttpPingRoutineImpl.getInstance();
      return var2.ping(var1.getServerConfigurationInformation()) > 0L;
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }

   private static void debug(String var0) {
      System.out.println("[HttpPingProbe] " + var0);
   }
}
