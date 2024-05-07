package weblogic.diagnostics.snmp.server;

import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.configuration.SNMPAttributeChangeMBean;
import weblogic.management.configuration.ServerMBean;

public class MBeanAttributeChangeLifecycle extends JMXMonitorLifecycle {
   public MBeanAttributeChangeLifecycle(boolean var1, String var2, SNMPAgent var3, MBeanServerConnection var4) {
      super(var1, var2, var3, var4);
      this.deregisterMonitorListener = true;
   }

   void initializeMonitorListenerList(SNMPAgentMBean var1) throws Exception {
      this.initializeMBeanAttributeChangeListeners(var1.getSNMPAttributeChanges());
   }

   private void initializeMBeanAttributeChangeListeners(SNMPAttributeChangeMBean[] var1) throws Exception {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            SNMPAttributeChangeMBean var3 = var1[var2];
            MBeanAttributeChangeListener var4 = null;
            ServerMBean[] var5 = var3.getEnabledServers();
            int var6 = var5 == null ? 0 : var5.length;
            boolean var7 = var3.getAttributeMBeanType().endsWith("Runtime");
            if (var7 & this.adminServer && var6 > 0) {
               for(int var8 = 0; var8 < var6; ++var8) {
                  var4 = new MBeanAttributeChangeListener(this, this.snmpAgent, var3.getAttributeMBeanName(), var3.getAttributeMBeanType(), this.serverName, var5[var8].getName(), var3.getAttributeName());
                  this.monitorListenerList.add(var4);
                  var4.setName(var3.getName());
               }
            } else {
               var4 = new MBeanAttributeChangeListener(this, this.snmpAgent, var3.getAttributeMBeanName(), var3.getAttributeMBeanType(), this.serverName, this.adminServer ? this.serverName : null, var3.getAttributeName());
               this.monitorListenerList.add(var4);
               var4.setName(var3.getName());
            }
         }

      }
   }

   void registerMonitor(ObjectName var1, JMXMonitorListener var2) {
      MBeanAttributeChangeListener var3 = (MBeanAttributeChangeListener)var2;
      Object var4 = null;
      synchronized(this.monitorListenerRegistry) {
         var4 = (List)this.monitorListenerRegistry.get(var1);
         if (var4 == null) {
            var4 = new LinkedList();
            this.monitorListenerRegistry.put(var1, var4);
         }
      }

      try {
         this.mbeanServerConnection.addNotificationListener(var1, var3, var3, (Object)null);
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Registered attribute change listener " + var3 + " for " + var1 + ":" + var3.getAttributeName());
         }

         synchronized(var4) {
            ((List)var4).add(var3);
         }
      } catch (Throwable var9) {
         SNMPLogger.logAttrChangeCreationError(var3.getName(), var3.getTypeName(), var3.getServerName(), var9);
      }

   }
}
