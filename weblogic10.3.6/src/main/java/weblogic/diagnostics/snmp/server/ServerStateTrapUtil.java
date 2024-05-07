package weblogic.diagnostics.snmp.server;

import java.util.Date;
import java.util.LinkedList;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPNotificationManager;

public class ServerStateTrapUtil {
   static final String WLS_START_TRAP = "wlsServerStart";
   static final String WLS_SHUTDOWN_TRAP = "wlsServerShutDown";

   static void sendServerLifecycleNotification(SNMPAgent var0, String var1, String var2) throws SNMPAgentToolkitException {
      SNMPNotificationManager var3 = var0.getSNMPAgentToolkit().getSNMPNotificationManager();
      LinkedList var4 = new LinkedList();
      String var5 = (new Date()).toString();
      var4.add(new Object[]{"trapTime", var5});
      var4.add(new Object[]{"trapServerName", var1});
      var3.sendNotification(var0.getNotifyGroup(), var2, var4);
   }
}
