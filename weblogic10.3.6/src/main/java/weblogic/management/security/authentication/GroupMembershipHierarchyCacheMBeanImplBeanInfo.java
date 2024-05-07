package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;

public class GroupMembershipHierarchyCacheMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = GroupMembershipHierarchyCacheMBean.class;

   public GroupMembershipHierarchyCacheMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public GroupMembershipHierarchyCacheMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = GroupMembershipHierarchyCacheMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("Defines methods used to get/set the configuration attributes that are required to support the Group Membership Hierarchy Cache.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.GroupMembershipHierarchyCacheMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("EnableGroupMembershipLookupHierarchyCaching")) {
         var3 = "getEnableGroupMembershipLookupHierarchyCaching";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnableGroupMembershipLookupHierarchyCaching";
         }

         var2 = new PropertyDescriptor("EnableGroupMembershipLookupHierarchyCaching", GroupMembershipHierarchyCacheMBean.class, var3, var4);
         var1.put("EnableGroupMembershipLookupHierarchyCaching", var2);
         var2.setValue("description", "Returns whether group membership hierarchies found during recursive membership lookup will be cached. If true, each subtree found will be cached. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("GroupHierarchyCacheTTL")) {
         var3 = "getGroupHierarchyCacheTTL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupHierarchyCacheTTL";
         }

         var2 = new PropertyDescriptor("GroupHierarchyCacheTTL", GroupMembershipHierarchyCacheMBean.class, var3, var4);
         var1.put("GroupHierarchyCacheTTL", var2);
         var2.setValue("description", "Returns the maximum number of seconds a group membership hierarchy entry is valid in the LRU cache. ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxGroupHierarchiesInCache")) {
         var3 = "getMaxGroupHierarchiesInCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxGroupHierarchiesInCache";
         }

         var2 = new PropertyDescriptor("MaxGroupHierarchiesInCache", GroupMembershipHierarchyCacheMBean.class, var3, var4);
         var1.put("MaxGroupHierarchiesInCache", var2);
         var2.setValue("description", "Returns the maximum size of the LRU cache for holding group membership hierarchies if caching is enabled. ");
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("dynamic", Boolean.FALSE);
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
