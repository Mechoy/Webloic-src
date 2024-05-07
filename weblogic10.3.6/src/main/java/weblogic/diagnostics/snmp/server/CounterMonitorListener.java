package weblogic.diagnostics.snmp.server;

import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;

public class CounterMonitorListener extends JMXMonitorListener {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private int threshold;
   private int offset;
   private int modulus;

   public CounterMonitorListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String var5, String var6, String var7, int var8, int var9, int var10) throws MalformedObjectNameException {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.threshold = var8;
      this.offset = var9;
      this.modulus = var10;
   }

   public boolean isNotificationEnabled(Notification var1) {
      return true;
   }

   int getModulus() {
      return this.modulus;
   }

   int getOffset() {
      return this.offset;
   }

   int getThreshold() {
      return this.threshold;
   }

   void updateMonitorTrapCount() {
      if (this.snmpStats != null) {
         this.snmpStats.incrementCounterMonitorTrapCount();
      }

   }
}
