package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.utils.NameListerMBeanImplBeanInfo;

public class GroupReaderMBeanImplBeanInfo extends NameListerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = GroupReaderMBean.class;

   public GroupReaderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public GroupReaderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = GroupReaderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("Provides a set of methods for reading data about groups. An Authentication provider MBean can optionally implement this MBean. The WebLogic Server Administration Console detects when an Authentication provider implements this MBean and automatically provides a tab for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.GroupReaderMBean");
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
      Method var3 = GroupReaderMBean.class.getMethod("listGroups", String.class, Integer.TYPE);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("groupNameWildcard", "- <p>The pattern for which this method searches. The pattern can end with an <code>*</code> (asterisk) as a wildcard, which matches any string of characters. The search is not case-sensitive.</p> <p>For example, a pattern of <code>abc</code> matches exactly one group name that contains only <code>abc</code>, a pattern of <code>ab*</code> matches all group names that start with <code>ab</code>, and a pattern of <code>*</code> matches all group names.</p> "), createParameterDescriptor("maximumToReturn", "- The maximum number of group names that this method returns. If there are more matches than this maximum, then the returned results are arbitrary because this method does not sort results. If the parameter is set to 0 there is no maximum and all results are returned. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Searches for a user name that matches a pattern. <p> This method returns a cursor that you can pass to the methods from <code>weblogic.management.utils.NameListerMBean</code> (which this MBean extends) to iterate through the returned list. <p> This method does not sort the results. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupReaderMBean.class.getMethod("groupExists", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name that this method evaluates. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates whether the specified group exists. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupReaderMBean.class.getMethod("getGroupDescription", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("groupName", "- The name of an existing group. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a group's description. ");
         var2.setValue("role", "operation");
      }

      var3 = GroupReaderMBean.class.getMethod("isMember", String.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("parentGroupName", "- The existing group within which this method searches for membership. "), createParameterDescriptor("memberUserOrGroupName", "- The user or group name for which this method searches. "), createParameterDescriptor("recursive", "- If set to <code>true</code>, the criteria for membership extends to any groups within the group that is specified by <code>parentGroupName</code>. <p> If this argument is set to <code>false</code>, then this method checks only for direct membership within the <code>parentGroupName</code>.</p> ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates whether a user or group is a member of the group that you specify. A recursive search returns true if the member belongs to the group that you specify or to any of the groups contained within that group.\" ");
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
