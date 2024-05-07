package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CoherenceServerMBeanImplBeanInfo extends ManagedExternalServerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CoherenceServerMBean.class;

   public CoherenceServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CoherenceServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CoherenceServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This class represents a Coherence stand-alone server.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CoherenceServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CoherenceClusterSystemResource")) {
         var3 = "getCoherenceClusterSystemResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCoherenceClusterSystemResource";
         }

         var2 = new PropertyDescriptor("CoherenceClusterSystemResource", CoherenceServerMBean.class, var3, var4);
         var1.put("CoherenceClusterSystemResource", var2);
         var2.setValue("description", "The system-level Coherence cluster resource associated with this server. ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("CoherenceServerStart")) {
         var3 = "getCoherenceServerStart";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceServerStart", CoherenceServerMBean.class, var3, var4);
         var1.put("CoherenceServerStart", var2);
         var2.setValue("description", "<p> Returns the CoherenceServerStartMBean that can be used to start up this server remotely. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", CoherenceServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>An alphanumeric name for this server instance. (Spaces are not valid.)</p>  <p>The name must be unique for all configuration objects in the domain. Within a domain, each server, machine, cluster, JDBC connection pool, virtual host, and any other resource type must be named uniquely and must not use the same name as the domain.</p>  <p>The server name is not used as part of the URL for applications that are deployed on the server. It is for your identification purposes only. The server name displays in the Administration Console, and if you use WebLogic Server command-line utilities or APIs, you use this name to identify the server.</p>  <p>After you have created a server, you cannot change its name. Instead, clone the server and provide a new name for the clone.</p> ");
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      String[] var5;
      if (!var1.containsKey("UnicastListenAddress")) {
         var3 = "getUnicastListenAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUnicastListenAddress";
         }

         var2 = new PropertyDescriptor("UnicastListenAddress", CoherenceServerMBean.class, var3, var4);
         var1.put("UnicastListenAddress", var2);
         var2.setValue("description", "<p>The IP address for the cluster unicast listener.</p> <p>This attribute may be used to override the value of the referenced Coherence cluster.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("CoherenceClusterSystemResourceMBean#getCoherenceClusterResource()")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UnicastListenPort")) {
         var3 = "getUnicastListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUnicastListenPort";
         }

         var2 = new PropertyDescriptor("UnicastListenPort", CoherenceServerMBean.class, var3, var4);
         var1.put("UnicastListenPort", var2);
         var2.setValue("description", "<p>The port for the cluster unicast listener.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("CoherenceClusterSystemResourceMBean#getCoherenceClusterResource()")};
         var2.setValue("see", var5);
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UnicastPortAutoAdjust")) {
         var3 = "isUnicastPortAutoAdjust";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUnicastPortAutoAdjust";
         }

         var2 = new PropertyDescriptor("UnicastPortAutoAdjust", CoherenceServerMBean.class, var3, var4);
         var1.put("UnicastPortAutoAdjust", var2);
         var2.setValue("description", "<p> Specifies whether the unicast port will be automatically incremented if the port cannot be bound because it is already in use. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
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
      Method var3 = CoherenceServerMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
