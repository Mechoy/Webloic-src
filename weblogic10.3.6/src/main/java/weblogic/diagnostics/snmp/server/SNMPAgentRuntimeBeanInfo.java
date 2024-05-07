package weblogic.diagnostics.snmp.server;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SNMPAgentRuntimeMBean;

public class SNMPAgentRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPAgentRuntimeMBean.class;

   public SNMPAgentRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPAgentRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPAgentRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPAgentMBean"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPAgentDeploymentMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.diagnostics.snmp.server");
      String var4 = (new String("<p>Runtime information for an SNMP agent that is running in the current WebLogic Server domain.</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SNMPAgentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("AttributeChangeTrapCount")) {
         var3 = "getAttributeChangeTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("AttributeChangeTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("AttributeChangeTrapCount", var2);
         var2.setValue("description", "The number of attribute change notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("CounterMonitorTrapCount")) {
         var3 = "getCounterMonitorTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("CounterMonitorTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("CounterMonitorTrapCount", var2);
         var2.setValue("description", "The number of counter monitor notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("FailedAuthenticationCount")) {
         var3 = "getFailedAuthenticationCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedAuthenticationCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("FailedAuthenticationCount", var2);
         var2.setValue("description", "The number of requests that this agent has rejected because of incorrect user credentials. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("FailedAuthorizationCount")) {
         var3 = "getFailedAuthorizationCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedAuthorizationCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("FailedAuthorizationCount", var2);
         var2.setValue("description", "The number of requests that this agent has rejected because an authenticated user does not have sufficient privileges to view the requested information. You use the WebLogic Server security realm to assign privileges to users. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("FailedEncryptionCount")) {
         var3 = "getFailedEncryptionCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedEncryptionCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("FailedEncryptionCount", var2);
         var2.setValue("description", "<p>The number of requests that this agent has rejected because of incorrect privacy (encryption) credentials </p> ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("GaugeMonitorTrapCount")) {
         var3 = "getGaugeMonitorTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("GaugeMonitorTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("GaugeMonitorTrapCount", var2);
         var2.setValue("description", "The number of gauge monitor notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LogMessageTrapCount")) {
         var3 = "getLogMessageTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("LogMessageTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("LogMessageTrapCount", var2);
         var2.setValue("description", "The number of log message notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MasterAgentXPort")) {
         var3 = "getMasterAgentXPort";
         var4 = null;
         var2 = new PropertyDescriptor("MasterAgentXPort", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MasterAgentXPort", var2);
         var2.setValue("description", "<p>The port that this SNMP agent uses to communicate with subagents.</p> <p>The agent uses subagents to provide access to custom MBeans (MBeans that you create and register) and to other Oracle software components. WebLogic Server SNMP agents do not enable users to register their own subagents.</p> ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MonitorTrapCount")) {
         var3 = "getMonitorTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("MonitorTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MonitorTrapCount", var2);
         var2.setValue("description", "The total number of all notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPAgentName")) {
         var3 = "getSNMPAgentName";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAgentName", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("SNMPAgentName", var2);
         var2.setValue("description", "<pGets the name of the SNMPAgent MBean configuration that is currently active. Returns null if no SNMPAgent configuration is currently active on this Server.</p> ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerStartTrapCount")) {
         var3 = "getServerStartTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("ServerStartTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerStartTrapCount", var2);
         var2.setValue("description", "The number of serverStart notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerStopTrapCount")) {
         var3 = "getServerStopTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("ServerStopTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerStopTrapCount", var2);
         var2.setValue("description", "The number of serverShutdown notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StringMonitorTrapCount")) {
         var3 = "getStringMonitorTrapCount";
         var4 = null;
         var2 = new PropertyDescriptor("StringMonitorTrapCount", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("StringMonitorTrapCount", var2);
         var2.setValue("description", "The number of string monitor notifications that this SNMP agent has sent to all trap destinations since the agent's host server was started. ");
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("UDPListenPort")) {
         var3 = "getUDPListenPort";
         var4 = null;
         var2 = new PropertyDescriptor("UDPListenPort", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("UDPListenPort", var2);
         var2.setValue("description", "<p>The UDP port on which this SNMP agent is listening for incoming requests from SNMP managers.</p> <p>SNMP agents can also communicate through the host server's TCP listen port (7001 by default) or through a TCP port that is configured by a custom network channel.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPAgentMBean#getSNMPPort()")};
         var2.setValue("see", var5);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Running")) {
         var3 = "isRunning";
         var4 = null;
         var2 = new PropertyDescriptor("Running", SNMPAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("Running", var2);
         var2.setValue("description", "Indicates whether this SNMP agent is running. ");
         var2.setValue("since", "10.0.0.0");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SNMPAgentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentRuntimeMBean.class.getMethod("outputCustomMBeansMIBModule");
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            String[] var5 = new String[]{BeanInfoHelper.encodeEntities("ManagementException")};
            var2.setValue("throws", var5);
            var2.setValue("since", "10.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Returns WebLogic Server's MIB module for custom MBeans as a <code>java.lang.String</code>. You can save the <code>String</code> to a file and then load the file into a MIB browser.</p> <p>When you register custom MBeans in the WebLogic Server Runtime MBean Server, WebLogic Server adds entries to a runtime MIB module that it maintains for custom MBeans. For each custom MBean type, WebLogic Server adds a table to the MIB module. For each instance of the custom MBean, it adds a table row. While WebLogic Server does not persist the MIB module as a file or other data structure, the OIDs in the module remain constant across server sessions.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentRuntimeMBean.class.getMethod("invalidateLocalizedKeyCache", String.class);
         ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("username", "Name of the user ")};
         String var8 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var8)) {
            var2 = new MethodDescriptor(var3, var7);
            var2.setValue("since", "10.0.0.0");
            var1.put(var8, var2);
            var2.setValue("description", "<p>Immediately invalidates the cached security keys for the specified WebLogic Server user.</p> <p>An SNMP security key is an encrypted version of an SNMP agent's engine ID and an authentication password or privacy password. WebLogic Server generates one security key for each entry that you create in the SNMP credential map. When a WebLogic Server SNMP agent receives an SNMPv3 request, it compares the key that is in the request with its WebLogic Server keys. If it finds a match, it processes the request. The SNMP agent also encodes these keys in its responses and notifications. (You configure which keys are encoded when you create a trap destination.)</p> <p>Instead of regenerating the keys for each SNMPv3 communication, WebLogic Server caches the keys. To make sure that the cache contains the latest set of SNMP credentials, WebLogic Server periodically invalidates the cache. After the cache is invalidated, the next time an SNMP agent requests credentials, WebLogic Server regenerates the cache.</p> <p>Note that making a change to the credential map does not automatically update the cache. Instead, the cache is updated only after it has been invalidated.</p> <p>Instead of waiting for WebLogic Server to invalidate the cached entry for a key, you can invalidate it immediately.</p> ");
            String[] var6 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPAgentMBean#getLocalizedKeyCacheInvalidationInterval()")};
            var2.setValue("see", var6);
            var2.setValue("role", "operation");
            var2.setValue("since", "10.0.0.0");
         }
      }

   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
