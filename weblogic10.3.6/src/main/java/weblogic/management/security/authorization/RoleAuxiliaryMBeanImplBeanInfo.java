package weblogic.management.security.authorization;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class RoleAuxiliaryMBeanImplBeanInfo extends RoleEditorMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RoleAuxiliaryMBean.class;

   public RoleAuxiliaryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RoleAuxiliaryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RoleAuxiliaryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authorization");
      String var3 = (new String("Provides a set of auxiliary methods for creating, editing, and removing role assignment policies. An RoleMapping-provider MBean can optionally extend this MBean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authorization.RoleAuxiliaryMBean");
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
      Method var3 = RoleAuxiliaryMBean.class.getMethod("createRole", String.class, String.class, String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource that scopes the new role. Each resource has its predefined 'hierachy'. This new role is applicable to all of the given resouce's descendants if any. A null indicates a global role, no scoping resource, which applies to all resources within the container. "), createParameterDescriptor("roleName", "- The name of the role that this method creates. A null value will trigger NullPointerException. "), createParameterDescriptor("expression", "- The expression policy designates which user or group having this named 'role'. A null value indicates this role is not granted to anyone. "), createParameterDescriptor("auxiliary", "- Auxiliary data to support WLP. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates role for a resource. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RoleAuxiliaryMBean.class.getMethod("setRoleAuxiliary", String.class, String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource that scopes the role. "), createParameterDescriptor("roleName", "- The name of the role that this method sets the auxiliary on. A null value will trigger NullPointerException. "), createParameterDescriptor("auxiliary", "- Auxiliary data to support WLP. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Set auxiliary role for a resource. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RoleAuxiliaryMBean.class.getMethod("getRoleAuxiliary", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource that scopes the role. "), createParameterDescriptor("roleName", "- The name of the role that this method sets the auxiliary on. A null value will trigger NullPointerException. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Get auxiliary role for a resource. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RoleAuxiliaryMBean.class.getMethod("exportResource", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("filename", "- The full path to the filename used to write data. "), createParameterDescriptor("cn", "- The LDAP CN to be used when exporting data. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Exports provider specific role data using an LDAP CN search filter to specify the resources for export. When errors occur, the MBean throws an ErrorCollectionException containing a list of &lt;code&gt;java.lang.Exceptions&lt;/code;&gt;. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RoleAuxiliaryMBean.class.getMethod("listAllRolesAndURIs", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("application", "- The name of the deployed application. "), createParameterDescriptor("contextPath", "- The context path for the application. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "List all Role and URI pairings that have policy defined for the specified application and context path. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RoleAuxiliaryMBean.class.getMethod("getRoleNames", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceType", "- The resource type, or null to get global roles ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Returns array of resource/role name tuples of roles defined in the scope of resources of the given type or global roles, when the type is null. ");
         var2.setValue("exclude", Boolean.TRUE);
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
