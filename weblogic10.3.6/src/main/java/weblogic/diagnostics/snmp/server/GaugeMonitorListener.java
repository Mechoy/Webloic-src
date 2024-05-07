package weblogic.diagnostics.snmp.server;

import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;

public class GaugeMonitorListener extends JMXMonitorListener {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private int lowThreshold;
   private int highThreshold;

   public GaugeMonitorListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String var5, String var6, String var7, int var8, int var9) throws MalformedObjectNameException {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.lowThreshold = var8;
      this.highThreshold = var9;
   }

   public boolean isNotificationEnabled(Notification var1) {
      return true;
   }

   int getHighThreshold() {
      return this.highThreshold;
   }

   int getLowThreshold() {
      return this.lowThreshold;
   }

   void updateMonitorTrapCount() {
      if (this.snmpStats != null) {
         this.snmpStats.incrementGaugeMonitorTrapCount();
      }

   }
}
