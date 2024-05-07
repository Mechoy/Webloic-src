package weblogic.nodemanager.server;

import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.StartupConfig;

public class CoherenceServerMonitor extends AbstractServerMonitor {
   protected CoherenceServerMonitor(CoherenceServerManager var1, StartupConfig var2) {
      super(var1, var2);
   }

   protected WLSProcessBuilder createWLSProcessBuilder(ServerManagerI var1, StartupConfig var2) {
      return new CoherenceProcessBuilder(var1, var2);
   }

   protected String getStartString(NodeManagerTextTextFormatter var1, StringBuilder var2) {
      return var1.msgStartingType("Coherence", var2.toString());
   }
}
