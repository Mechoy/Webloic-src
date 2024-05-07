package weblogic.diagnostics.snmp.server;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.configuration.SNMPStringMonitorMBean;
import weblogic.management.configuration.ServerMBean;

public class StringMonitorLifecycle extends JMXMonitorLifecycle {
   public StringMonitorLifecycle(boolean var1, String var2, SNMPAgent var3, MBeanServerConnection var4) {
      super(var1, var2, var3, var4);
   }

   void initializeMonitorListenerList(SNMPAgentMBean var1) throws Exception {
      this.initializeStringMonitors(var1.getSNMPStringMonitors());
   }

   private void initializeStringMonitors(SNMPStringMonitorMBean[] var1) throws Exception {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            SNMPStringMonitorMBean var3 = var1[var2];
            StringMonitorListener var4 = null;
            ServerMBean[] var5 = var3.getEnabledServers();
            int var6 = var5 == null ? 0 : var5.length;
            boolean var7 = var3.getMonitoredMBeanType().endsWith("Runtime");
            if (var7 && this.adminServer && var6 > 0) {
               for(int var8 = 0; var8 < var6; ++var8) {
                  var4 = new StringMonitorListener(this, this.snmpAgent, var3.getMonitoredMBeanName(), var3.getMonitoredMBeanType(), this.serverName, var5[var8].getName(), var3.getMonitoredAttributeName(), var3.getStringToCompare(), var3.isNotifyDiffer(), var3.isNotifyMatch());
                  this.monitorListenerList.add(var4);
                  var4.setName(var3.getName());
                  var4.setPollingIntervalSeconds(var3.getPollingInterval());
               }
            } else {
               var4 = new StringMonitorListener(this, this.snmpAgent, var3.getMonitoredMBeanName(), var3.getMonitoredMBeanType(), this.serverName, (String)null, var3.getMonitoredAttributeName(), var3.getStringToCompare(), var3.isNotifyDiffer(), var3.isNotifyMatch());
               this.monitorListenerList.add(var4);
               var4.setName(var3.getName());
               var4.setPollingIntervalSeconds(var3.getPollingInterval());
            }
         }

      }
   }

   void registerMonitor(ObjectName var1, JMXMonitorListener var2) {
      StringMonitorListener var3 = (StringMonitorListener)var2;

      try {
         ObjectName var4 = this.getMonitorObjectName(var1, var3, "StringMonitor");
         if (!this.mbeanServerConnection.isRegistered(var4)) {
            ObjectInstance var5 = this.mbeanServerConnection.createMBean("javax.management.monitor.StringMonitor", var4);
            var4 = var5.getObjectName();
         } else {
            this.mbeanServerConnection.invoke(var4, "stop", new Object[0], new String[0]);
         }

         this.mbeanServerConnection.setAttribute(var4, new Attribute("GranularityPeriod", new Long((long)(var3.getPollingIntervalSeconds() * 1000))));
         this.mbeanServerConnection.setAttribute(var4, new Attribute("ObservedAttribute", var3.getAttributeName()));
         this.mbeanServerConnection.invoke(var4, "addObservedObject", new Object[]{var1}, new String[]{"javax.management.ObjectName"});
         this.mbeanServerConnection.setAttribute(var4, new Attribute("StringToCompare", var3.getStringToCompare()));
         this.mbeanServerConnection.setAttribute(var4, new Attribute("NotifyDiffer", new Boolean(var3.isNotifyDiffer())));
         this.mbeanServerConnection.setAttribute(var4, new Attribute("NotifyMatch", new Boolean(var3.isNotifyMatch())));
         this.registerMonitorListener(var4, var3, (Object)null);
         this.mbeanServerConnection.invoke(var4, "start", new Object[0], new String[0]);
         var3.setMonitor(var4);
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Registered " + var4 + " to observe " + var1 + ":" + var3.getAttributeName());
         }
      } catch (Throwable var6) {
         SNMPLogger.logMonitorCreationError(var3.getName(), "SNMPStringMonitorMBean", var1.toString(), var6);
      }

   }
}
