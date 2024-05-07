package weblogic.diagnostics.snmp.server;

import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;

public class StringMonitorListener extends JMXMonitorListener {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private String stringToCompare;
   private boolean notifyDiffer;
   private boolean notifyMatch;

   public StringMonitorListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, boolean var10) throws MalformedObjectNameException {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.stringToCompare = var8;
      this.notifyDiffer = var9;
      this.notifyMatch = var10;
   }

   public boolean isNotificationEnabled(Notification var1) {
      return true;
   }

   boolean isNotifyDiffer() {
      return this.notifyDiffer;
   }

   boolean isNotifyMatch() {
      return this.notifyMatch;
   }

   String getStringToCompare() {
      return this.stringToCompare;
   }

   void updateMonitorTrapCount() {
      if (this.snmpStats != null) {
         this.snmpStats.incrementStringMonitorTrapCount();
      }

   }
}
