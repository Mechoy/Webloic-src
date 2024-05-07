package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ManagedExternalServerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ManagedExternalServerMBean.class;

   public ManagedExternalServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ManagedExternalServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ManagedExternalServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.4.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Used to configure an external server that can be managed by Node Manager. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ManagedExternalServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("AutoRestart")) {
         var3 = "getAutoRestart";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoRestart";
         }

         var2 = new PropertyDescriptor("AutoRestart", ManagedExternalServerMBean.class, var3, var4);
         var1.put("AutoRestart", var2);
         var2.setValue("description", "<p> Specifies whether the Node Manager can automatically restart this server if it crashes or otherwise goes down unexpectedly. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("Machine")) {
         var3 = "getMachine";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMachine";
         }

         var2 = new PropertyDescriptor("Machine", ManagedExternalServerMBean.class, var3, var4);
         var1.put("Machine", var2);
         var2.setValue("description", "<p> The WebLogic Server host computer (machine) on which this server is meant to run. </p>  <p> If you want to use a Node Manager to start this server, you must assign the server to a machine and you must configure the machine for the Node Manager. </p>  <p> You cannot change this value if a server instance is already running. </p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("NMSocketCreateTimeoutInMillis")) {
         var3 = "getNMSocketCreateTimeoutInMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNMSocketCreateTimeoutInMillis";
         }

         var2 = new PropertyDescriptor("NMSocketCreateTimeoutInMillis", ManagedExternalServerMBean.class, var3, var4);
         var1.put("NMSocketCreateTimeoutInMillis", var2);
         var2.setValue("description", "Returns the timeout value to be used by NodeManagerRuntime when creating a socket connection to the agent. Default set high as SSH agent may require a high connection establishment time. ");
         setPropertyDescriptorDefault(var2, new Integer(180000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ManagedExternalServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>An alphanumeric name for this server instance. (Spaces are not valid.)</p>  <p>The name must be unique for all configuration objects in the domain. Within a domain, each server, machine, cluster, JDBC connection pool, virtual host, and any other resource type must be named uniquely and must not use the same name as the domain.</p>  <p>The server name is not used as part of the URL for applications that are deployed on the server. It is for your identification purposes only. The server name displays in the Administration Console, and if you use WebLogic Server command-line utilities or APIs, you use this name to identify the server.</p>  <p>After you have created a server, you cannot change its name. Instead, clone the server and provide a new name for the clone.</p> ");
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("RestartDelaySeconds")) {
         var3 = "getRestartDelaySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartDelaySeconds";
         }

         var2 = new PropertyDescriptor("RestartDelaySeconds", ManagedExternalServerMBean.class, var3, var4);
         var1.put("RestartDelaySeconds", var2);
         var2.setValue("description", "<p> The number of seconds the Node Manager should wait before restarting this server. </p>  <p> After killing a server process, the system might need several seconds to release the TCP port(s) the server was using. If Node Manager attempts to restart the Managed Server while its ports are still active, the startup attempt fails. </p>  <p> If AutoMigration is enabled and RestartDelaySeconds is 0, the RestartDelaySeconds is automatically set to the lease time. This prevents the server from failing to restart after migration when the previous lease is still valid. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("RestartIntervalSeconds")) {
         var3 = "getRestartIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartIntervalSeconds";
         }

         var2 = new PropertyDescriptor("RestartIntervalSeconds", ManagedExternalServerMBean.class, var3, var4);
         var1.put("RestartIntervalSeconds", var2);
         var2.setValue("description", "<p> The number of seconds during which this server can be restarted, up to the number of times specified in RestartMax. </p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getRestartMax")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(3600));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(300));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("RestartMax")) {
         var3 = "getRestartMax";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartMax";
         }

         var2 = new PropertyDescriptor("RestartMax", ManagedExternalServerMBean.class, var3, var4);
         var1.put("RestartMax", var2);
         var2.setValue("description", "<p> The number of times that the Node Manager can restart this server within the interval specified in RestartIntervalSeconds. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
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
