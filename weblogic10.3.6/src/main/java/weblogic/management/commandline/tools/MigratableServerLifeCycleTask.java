package weblogic.management.commandline.tools;

import java.io.IOException;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.ServerStartMBean;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.nodemanager.mbean.NodeManagerTask;
import weblogic.utils.Debug;

public final class MigratableServerLifeCycleTask {
   private static final boolean DEBUG = true;
   private final int healthCheckInterval;
   private final MachineMBean machine;
   private final ServerStartMBean server;
   private final ServerMBean migServer;
   private final String migIPAddress;
   private final String serverName;
   private final String domainName;

   MigratableServerLifeCycleTask(ServerMBean var1, String var2) {
      this.migServer = var1;
      this.serverName = this.migServer.getName();
      this.healthCheckInterval = var1.getCluster().getHealthCheckIntervalMillis();
      this.migIPAddress = this.migServer.getListenAddress();
      this.server = this.migServer.getServerStart();
      this.machine = this.migServer.getMachine();
      this.domainName = var2;
   }

   void startServer(MachineMBean var1) throws IOException {
      NodeManagerTask var2 = null;
      NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(var1);
      var2 = var3.start(this.migServer);

      while(!var2.isFinished()) {
         try {
            var2.waitForFinish();
         } catch (InterruptedException var5) {
         }
      }

      String var4 = var3.getState(this.migServer);
      Debug.say(this.serverName + " is now in " + var4 + " state");
   }

   void isMachineReachable(MachineMBean var1) throws IOException {
      NodeManagerRuntime var2 = NodeManagerRuntime.getInstance(var1);
      var2.getState(this.migServer);
      Debug.say(this.machine.getName() + " is reachable");
   }

   void stopServer() throws IOException {
      Object var1 = null;
      NodeManagerRuntime var2 = NodeManagerRuntime.getInstance(this.machine);
      var2.kill(this.migServer);
      Debug.say(this.serverName + " is shutdown now");
   }
}
