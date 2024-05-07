package weblogic.diagnostics.snmp.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.configuration.SNMPLogFilterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;

public class LogFilterLifecycle extends JMXMonitorLifecycle {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private Map logFilters = new HashMap();

   public LogFilterLifecycle(boolean var1, String var2, SNMPAgent var3, MBeanServerConnection var4) {
      super(var1, var2, var3, var4);
   }

   void initializeMonitorListenerList(SNMPAgentMBean var1) throws Exception {
      this.initializeLogFilterListeners(var1.getSNMPLogFilters());
   }

   void registerMonitor(ObjectName var1, JMXMonitorListener var2) {
   }

   void serverStarted(String var1) {
      synchronized(this.logFilters) {
         List var3 = (List)this.logFilters.get(var1);
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               SNMPLogFilterMBean var5 = (SNMPLogFilterMBean)var4.next();
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Adding filter " + var5.getName() + " for server " + var1);
               }

               try {
                  this.addLogBroadcasterRuntimeListener(var1, var5);
               } catch (Throwable var8) {
                  SNMPLogger.logTrapLogAddNotifError(var5.getName(), var8);
               }
            }

         }
      }
   }

   private void initializeLogFilterListeners(SNMPLogFilterMBean[] var1) throws Exception {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            SNMPLogFilterMBean var3 = var1[var2];
            if (this.adminServer) {
               this.initializeAdminServerLogFilterListeners(var3);
            } else {
               this.initializeManagedServerLogFilterListeners(var3);
            }
         }

      }
   }

   private void initializeManagedServerLogFilterListeners(SNMPLogFilterMBean var1) throws Exception {
      ObjectName var2 = (ObjectName)this.mbeanServerConnection.getAttribute(new ObjectName(RuntimeServiceMBean.OBJECT_NAME), "ServerRuntime");
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Found " + var2);
      }

      String var3 = (String)this.mbeanServerConnection.getAttribute(var2, "Name");
      this.addLogFilter(var3, var1);
      this.addLogBroadcasterRuntimeListener(var2, var1);
   }

   private void initializeAdminServerLogFilterListeners(SNMPLogFilterMBean var1) throws Exception {
      ServerMBean[] var2 = var1.getEnabledServers();
      if (var2 == null || var2.length == 0) {
         SNMPAgentMBean var3 = (SNMPAgentMBean)var1.getParent();
         if (var3 != null) {
            DomainMBean var4 = null;
            var4 = (DomainMBean)var3.getParent();
            if (var4 != null) {
               var2 = var4.getServers();
            }
         }
      }

      if (var2 != null) {
         for(int var6 = 0; var6 < var2.length; ++var6) {
            ServerMBean var7 = var2[var6];
            String var5 = var7.getName();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Looking up ServerRuntime for " + var5);
            }

            this.addLogFilter(var5, var1);
            this.addLogBroadcasterRuntimeListener(var5, var1);
         }

      }
   }

   private void addLogFilter(String var1, SNMPLogFilterMBean var2) {
      synchronized(this.logFilters) {
         Object var4 = (List)this.logFilters.get(var1);
         if (var4 == null) {
            var4 = new LinkedList();
            this.logFilters.put(var1, var4);
         }

         ((List)var4).add(var2);
      }
   }

   private void addLogBroadcasterRuntimeListener(String var1, SNMPLogFilterMBean var2) throws Exception {
      ObjectName var3 = (ObjectName)this.mbeanServerConnection.invoke(new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME), "lookupServerRuntime", new Object[]{var1}, new String[]{"java.lang.String"});
      if (var3 != null) {
         this.addLogBroadcasterRuntimeListener(var3, var2);
      }

   }

   private void addLogBroadcasterRuntimeListener(ObjectName var1, SNMPLogFilterMBean var2) throws Exception {
      ObjectName var3 = (ObjectName)this.mbeanServerConnection.getAttribute(var1, "LogBroadcasterRuntime");
      if (this.monitorListenerRegistry.containsKey(var3)) {
         List var4 = (List)this.monitorListenerRegistry.get(var3);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            if (var6 != null && var6 instanceof LogFilterListener) {
               LogFilterListener var7 = (LogFilterListener)var6;
               if (var7.getName().equals(var2.getName())) {
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug("Not adding log broadcaster listener as it is already registered for filter " + var2.getName());
                  }

                  return;
               }
            }
         }
      }

      LogFilterListener var8 = new LogFilterListener(this, this.snmpAgent, var2.getName(), var2.getSeverityLevel(), var2.getSubsystemNames(), var2.getUserIds(), var2.getMessageIds(), var2.getMessageSubstring());
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Registering log filter listener " + var2.getName() + "for " + var3);
      }

      this.monitorListenerList.add(var8);
      this.registerMonitorListener(var3, var8, (Object)null);
   }

   protected void deregisterMonitorListener(ObjectName var1, JMXMonitorListener var2) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Deregistering log filter listener " + var2);
      }

      try {
         this.mbeanServerConnection.removeNotificationListener(var1, var2);
      } catch (Throwable var4) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception deregistering listener " + var2 + " from " + var1);
         }
      }

   }
}
