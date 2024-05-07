package weblogic.diagnostics.snmp.server;

import java.util.Date;
import java.util.LinkedList;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.MonitorNotification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPNotificationManager;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;

public abstract class JMXMonitorListener implements NotificationFilter, NotificationListener {
   private static final String LOCATION_KEY = "Location";
   protected static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   protected static final String TRAP_TIME = "trapTime";
   protected static final String TRAP_SERVER_NAME = "trapServerName";
   protected static final String TRAP_MONITOR_TYPE = "trapMonitorType";
   protected static final String TRAP_MBEAN_NAME = "trapMBeanName";
   protected static final String TRAP_MBEAN_TYPE = "trapMBeanType";
   protected static final String TRAP_MONITOR_THRESHOLD = "trapMonitorThreshold";
   protected static final String TRAP_ATTR_NAME = "trapAttributeName";
   protected static final String TRAP_MONITOR_VALUE = "trapMonitorValue";
   protected static final String WLS_MONITOR_NOTIFICATION = "wlsMonitorNotification";
   protected transient SNMPAgent snmpAgent;
   protected transient ObjectName monitor;
   protected transient JMXMonitorLifecycle monitorLifecycle;
   protected transient SNMPRuntimeStats snmpStats;
   protected String serverName;
   protected String mbeanName;
   protected String typeName;
   protected ObjectName queryExpression;
   protected String attributeName;
   protected int pollingIntervalSeconds;
   protected String name;

   public JMXMonitorListener(JMXMonitorLifecycle var1, SNMPAgent var2) {
      this.monitor = null;
      this.monitorLifecycle = var1;
      this.snmpAgent = var2;
   }

   public JMXMonitorListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String var5, String var6, String var7) throws MalformedObjectNameException {
      this(var1, var2);
      this.serverName = var5;
      this.mbeanName = var3;
      this.typeName = var4;
      this.attributeName = var7;
      String var8 = "com.bea:Type=" + this.typeName;
      if (this.mbeanName != null && this.mbeanName.length() > 0) {
         var8 = var8 + ",Name=" + this.mbeanName;
      }

      if (var6 != null && var6.length() > 0) {
         var8 = var8 + ",Location=" + var6;
      }

      var8 = var8 + ",*";
      this.queryExpression = new ObjectName(var8);
   }

   public ObjectName getQueryExpression() {
      return this.queryExpression;
   }

   int getPollingIntervalSeconds() {
      return this.pollingIntervalSeconds;
   }

   void setPollingIntervalSeconds(int var1) {
      this.pollingIntervalSeconds = var1;
   }

   String getAttributeName() {
      return this.attributeName;
   }

   String getName() {
      return this.name;
   }

   void setName(String var1) {
      this.name = var1;
   }

   ObjectName getMonitor() {
      return this.monitor;
   }

   void setMonitor(ObjectName var1) {
      this.monitor = var1;
   }

   public void handleNotification(Notification var1, Object var2) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Got notification from MonitorMBean " + var1);
      }

      if (var1 instanceof MonitorNotification) {
         MonitorNotification var3 = (MonitorNotification)var1;
         ObjectName var4 = var3.getObservedObject();
         String var5 = this.serverName;
         String var7;
         if (var4 instanceof ObjectName) {
            ObjectName var6 = (ObjectName)var4;
            if (this.mbeanName == null || this.mbeanName.trim().length() == 0) {
               this.mbeanName = var6.toString();
            }

            var7 = var6.getKeyProperty("Location");
            if (var7 != null && var7.length() > 0) {
               var5 = var7;
            }
         }

         SNMPNotificationManager var11 = this.snmpAgent.getSNMPAgentToolkit().getSNMPNotificationManager();
         var7 = this.snmpAgent.getNotifyGroup();
         LinkedList var8 = new LinkedList();
         var8.add(new Object[]{"trapTime", (new Date()).toString()});
         var8.add(new Object[]{"trapServerName", var5});
         var8.add(new Object[]{"trapMonitorType", var3.getType()});
         if (null != var3.getTrigger()) {
            var8.add(new Object[]{"trapMonitorThreshold", var3.getTrigger().toString()});
         } else {
            var8.add(new Object[]{"trapMonitorThreshold", "null"});
         }

         if (null != var3.getDerivedGauge()) {
            var8.add(new Object[]{"trapMonitorValue", var3.getDerivedGauge().toString()});
         } else {
            var8.add(new Object[]{"trapMonitorValue", "null"});
         }

         var8.add(new Object[]{"trapMBeanName", var4 != null ? var4.toString() : this.mbeanName});
         var8.add(new Object[]{"trapMBeanType", this.typeName});
         var8.add(new Object[]{"trapAttributeName", this.attributeName});

         try {
            var11.sendNotification(var7, "wlsMonitorNotification", var8);
            this.updateMonitorTrapCount();
         } catch (SNMPAgentToolkitException var10) {
            SNMPLogger.logMonitorNotificationError(this.serverName, this.typeName, this.mbeanName, var10);
         }
      }

   }

   abstract void updateMonitorTrapCount();

   JMXMonitorLifecycle getMonitorLifecycle() {
      return this.monitorLifecycle;
   }

   SNMPRuntimeStats getSNMPRuntimeStats() {
      return this.snmpStats;
   }

   void setSNMPRuntimeStats(SNMPRuntimeStats var1) {
      this.snmpStats = var1;
   }

   String getTypeName() {
      return this.typeName;
   }

   String getServerName() {
      return this.serverName;
   }
}
