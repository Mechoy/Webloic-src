package weblogic.management.mbeanservers.domainruntime.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.domainruntime.MBeanServerConnectionManagerMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;

public class MBeanServerConnectionManagerBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = MBeanServerConnectionManagerMBean.class;

   public MBeanServerConnectionManagerBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MBeanServerConnectionManagerBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MBeanServerConnectionManager.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.domainruntime.internal");
      String var3 = (new String("Provides information about the federerated MBeanServers. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.domainruntime.MBeanServerConnectionManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", MBeanServerConnectionManagerMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", MBeanServerConnectionManagerMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", MBeanServerConnectionManagerMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", MBeanServerConnectionManagerMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", MBeanServerConnectionManagerMBean.class, var3, (String)var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
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
      Method var3 = MBeanServerConnectionManagerMBean.class.getMethod("notifyNewMBeanServer", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Called to trigger the MBeanServer to recognize the presence of a new managed server. ");
         var2.setValue("role", "operation");
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
