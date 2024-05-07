package weblogic.diagnostics.snmp.server;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;

public class ServerStateLifecycle extends JMXMonitorLifecycle {
   private LogFilterLifecycle logFilterLifecycle;

   public ServerStateLifecycle(boolean var1, String var2, SNMPAgent var3, MBeanServerConnection var4) {
      super(var1, var2, var3, var4);
      this.deregisterMonitorListener = true;
   }

   void initializeMonitorListenerList(SNMPAgentMBean var1) throws Exception {
      if (this.adminServer) {
         this.initializeAdminServerLifecycleListeners();
      } else {
         this.initializeManagedServerRuntimeListener();
      }

   }

   void registerMonitor(ObjectName var1, JMXMonitorListener var2) {
   }

   private void initializeAdminServerLifecycleListeners() throws Exception {
      ObjectName var1 = (ObjectName)this.mbeanServerConnection.getAttribute(new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME), "DomainRuntime");
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Found " + var1);
      }

      ObjectName[] var2 = (ObjectName[])((ObjectName[])this.mbeanServerConnection.getAttribute(var1, "ServerLifeCycleRuntimes"));

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ObjectName var4 = var2[var3];
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Found " + var4);
         }

         String var5 = (String)this.mbeanServerConnection.getAttribute(var4, "Name");
         ServerStateListener var6 = new ServerStateListener(this, var5, this.snmpAgent);
         this.monitorListenerList.add(var6);
         this.registerMonitorListener(var4, var6, (Object)null);
      }

   }

   private void initializeManagedServerRuntimeListener() throws Exception {
      ObjectName var1 = (ObjectName)this.mbeanServerConnection.getAttribute(new ObjectName(RuntimeServiceMBean.OBJECT_NAME), "ServerRuntime");
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Found " + var1);
      }

      String var2 = (String)this.mbeanServerConnection.getAttribute(var1, "Name");
      ServerStateListener var3 = new ServerStateListener(this, var2, this.snmpAgent);
      this.monitorListenerList.add(var3);
      this.registerMonitorListener(var1, var3, (Object)null);
   }

   public void setLogFilterLifecycle(LogFilterLifecycle var1) {
      this.logFilterLifecycle = var1;
   }

   void serverStarted(String var1) {
      if (this.logFilterLifecycle != null) {
         this.logFilterLifecycle.serverStarted(var1);
      }

   }

   void serverStopped(String var1) {
      if (this.logFilterLifecycle != null) {
         this.logFilterLifecycle.serverStopped(var1);
      }

   }
}
