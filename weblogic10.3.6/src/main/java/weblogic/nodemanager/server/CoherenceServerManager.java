package weblogic.nodemanager.server;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import weblogic.nodemanager.common.CoherenceStartupConfig;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.common.StartupConfig;

public class CoherenceServerManager extends AbstractServerManager {
   protected CoherenceServerManager(DomainManager var1, String var2) throws IOException, ConfigException {
      super(var1, var2, ServerType.Coherence);
   }

   protected ServerMonitorI createServerMonitor(StartupConfig var1) {
      return new CoherenceServerMonitor(this, var1);
   }

   protected boolean canPingServer(ServerDir var1) {
      return true;
   }

   protected boolean isCrashRecoveryNeeded(StartupConfig var1) throws IOException {
      return true;
   }

   protected List<WLSProcess.ExecuteCallbackHook> getStopCallbacks(StartupConfig var1) throws IOException {
      return null;
   }

   protected List<WLSProcess.ExecuteCallbackHook> getStartCallbacks(StartupConfig var1) throws IOException {
      return null;
   }

   protected StartupConfig createStartupConfig(Properties var1) throws ConfigException {
      return new CoherenceStartupConfig(var1);
   }
}
