package weblogic.management.security.authorization;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.utils.PropertiesListerMBeanImplBeanInfo;

public class RoleListerMBeanImplBeanInfo extends PropertiesListerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RoleListerMBean.class;

   public RoleListerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RoleListerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RoleListerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authorization");
      String var3 = (new String("Provides a set of methods for listing data about roles. An Authorization-provider MBean can optionally extend this MBean. The WebLogic Server Administration Console detects when an Authorization provider extends this MBean and automatically provides a GUI for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authorization.RoleListerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = RoleListerMBean.class.getMethod("listAllRoles", Integer.TYPE);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain an unsorted list of role definitions. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listRolesByResourceType", String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceType", "- The name of the resource type specified by a <code>weblogic.security.spi.Resource</code> object. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return.  Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain an list of role definitions by resource type. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listRolesByApplication", String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("applicationName", "- The name of the application. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain an list of role definitions by application name. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listRolesByComponent", String.class, String.class, String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("componentName", "- The name of the component. "), createParameterDescriptor("componentType", "- The component type. "), createParameterDescriptor("applicationName", "- The name of the application. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain an list of role definitions for a specific J2EE component. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listRoles", String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- a security resource identifier. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain a list of role definitions for a specific resource. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listChildRoles", String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- a security resource identifier. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain a list of role definitions for the children of a resource. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("listRepeatingActionsRoles", String.class, Integer.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- a security resource identifier. "), createParameterDescriptor("maximumToReturn", "- The maximum number of entires to return. Use 0 to return all role definitions. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain a list of role definitions for the actions that are repeating on a resource. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.PropertiesListerMBean</code> (which this MBean extends) to iterate through the returned list. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("getRole", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- a security resource identifier. "), createParameterDescriptor("roleName", "- a role name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain a role definition. A null is returned when no role is found. <p> The <code>Properties</code> is the same as those returned from the <code>PropertiesListerMBean</code>. ");
         var2.setValue("role", "operation");
      }

      var3 = RoleListerMBean.class.getMethod("getRoleScopedByResource", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- a security resource identifier. "), createParameterDescriptor("roleName", "- a role name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Obtain a role definition for a role scoped by a specified resource. This may result in a global role definition. <p> If the role is defined at the specified resource then a call to <code>getRole</code> would return the same result. <p> A null is returned when no role is found. <p> The <code>Properties</code> object is the same as those returned from the <code>PropertiesListerMBean</code>. ");
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
