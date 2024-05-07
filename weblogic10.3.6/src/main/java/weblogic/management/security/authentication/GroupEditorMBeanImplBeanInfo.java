package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class GroupEditorMBeanImplBeanInfo extends GroupReaderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = GroupEditorMBean.class;

   public GroupEditorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public GroupEditorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = GroupEditorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("Provides a set of methods for creating, editing, and removing groups. An Authentication provider MBean can optionally implement this MBean. The WebLogic Server Administration Console detects when an Authentication provider implements this MBean and automatically provides a tab for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.GroupEditorMBean");
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
      Method var3 = GroupEditorMBean.class.getMethod("createGroup", String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of the new group. The name cannot be the name of an existing user or group. The Authentication provider determines syntax requirements for the group name. "), createParameterDescriptor("description", "- The description of the group. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a group. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupEditorMBean.class.getMethod("removeGroup", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of an existing group. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes a group. If the group contains members, the members are not removed. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupEditorMBean.class.getMethod("setGroupDescription", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of an existing group. "), createParameterDescriptor("description", "- The description of the group. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Sets the description for an existing group. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupEditorMBean.class.getMethod("addMemberToGroup", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of an existing group to which this method adds a member. "), createParameterDescriptor("memberUserOrGroupName", "- The name of the member, which must be an existing user or group. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Adds a user or group (member) to a group. If the member already belongs to the group, this method does nothing. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupEditorMBean.class.getMethod("removeMemberFromGroup", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of an existing group from which this method removes a member. "), createParameterDescriptor("memberUserOrGroupName", "- The name of the member, which must be an existing user or group. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes a user or group (member) from a group. If the member is not in the group, this method does nothing. ");
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
