package weblogic.nodemanager.server;

import weblogic.nodemanager.common.StartupConfig;

public class ServerMonitor extends AbstractServerMonitor {
   public ServerMonitor(ServerManagerI var1, StartupConfig var2) {
      super(var1, var2);
   }

   protected WLSProcessBuilder createWLSProcessBuilder(ServerManagerI var1, StartupConfig var2) {
      return new WLSProcessBuilder(var1, var2);
   }
}
