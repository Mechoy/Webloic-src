package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class CachingRealmMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CachingRealmMBean.class;

   public CachingRealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CachingRealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CachingRealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Displays the active security realm as defined in the Administration Console. This attribute can not be changed.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CachingRealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ACLCacheEnable")) {
         var3 = "getACLCacheEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACLCacheEnable";
         }

         var2 = new PropertyDescriptor("ACLCacheEnable", CachingRealmMBean.class, var3, var4);
         var1.put("ACLCacheEnable", var2);
         var2.setValue("description", "<p>Specifies whether the ACL cache should be enabled. By default, the ACL cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ACLCacheSize")) {
         var3 = "getACLCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACLCacheSize";
         }

         var2 = new PropertyDescriptor("ACLCacheSize", CachingRealmMBean.class, var3, var4);
         var1.put("ACLCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of ACL lookups to cache. This value should be a prime number for best lookup performance.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(211));
         var2.setValue("legalMax", new Integer(65537));
         var2.setValue("legalMin", new Integer(17));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ACLCacheTTLNegative")) {
         var3 = "getACLCacheTTLNegative";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACLCacheTTLNegative";
         }

         var2 = new PropertyDescriptor("ACLCacheTTLNegative", CachingRealmMBean.class, var3, var4);
         var1.put("ACLCacheTTLNegative", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of an unsuccessful ACL lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ACLCacheTTLPositive")) {
         var3 = "getACLCacheTTLPositive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACLCacheTTLPositive";
         }

         var2 = new PropertyDescriptor("ACLCacheTTLPositive", CachingRealmMBean.class, var3, var4);
         var1.put("ACLCacheTTLPositive", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of a successful ACL lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthenticationCacheEnable")) {
         var3 = "getAuthenticationCacheEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationCacheEnable";
         }

         var2 = new PropertyDescriptor("AuthenticationCacheEnable", CachingRealmMBean.class, var3, var4);
         var1.put("AuthenticationCacheEnable", var2);
         var2.setValue("description", "<p>Specifies whether the Authentication cache should be enabled. By default, the Authentication cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthenticationCacheSize")) {
         var3 = "getAuthenticationCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationCacheSize";
         }

         var2 = new PropertyDescriptor("AuthenticationCacheSize", CachingRealmMBean.class, var3, var4);
         var1.put("AuthenticationCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of Authentication requests to cache. This value should be a prime number for best lookup performance.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(211));
         var2.setValue("legalMax", new Integer(65537));
         var2.setValue("legalMin", new Integer(17));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthenticationCacheTTLNegative")) {
         var3 = "getAuthenticationCacheTTLNegative";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationCacheTTLNegative";
         }

         var2 = new PropertyDescriptor("AuthenticationCacheTTLNegative", CachingRealmMBean.class, var3, var4);
         var1.put("AuthenticationCacheTTLNegative", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of an unsuccessful Authentication lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthenticationCacheTTLPositive")) {
         var3 = "getAuthenticationCacheTTLPositive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationCacheTTLPositive";
         }

         var2 = new PropertyDescriptor("AuthenticationCacheTTLPositive", CachingRealmMBean.class, var3, var4);
         var1.put("AuthenticationCacheTTLPositive", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of a successful Authentication lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("BasicRealm")) {
         var3 = "getBasicRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBasicRealm";
         }

         var2 = new PropertyDescriptor("BasicRealm", CachingRealmMBean.class, var3, var4);
         var1.put("BasicRealm", var2);
         var2.setValue("description", "<p>The name of the class for the alterate security realm or Custom security realm to be used with this Caching Realm.</p>  <p>This attribute is required.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CacheCaseSensitive")) {
         var3 = "getCacheCaseSensitive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheCaseSensitive";
         }

         var2 = new PropertyDescriptor("CacheCaseSensitive", CachingRealmMBean.class, var3, var4);
         var1.put("CacheCaseSensitive", var2);
         var2.setValue("description", "<p>Specifies whether the specified security realm is case-sensitive. To use a realm that is not case-sensitive (such as the Windows NT and LDAP security realms), disable this attribute.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupCacheEnable")) {
         var3 = "getGroupCacheEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupCacheEnable";
         }

         var2 = new PropertyDescriptor("GroupCacheEnable", CachingRealmMBean.class, var3, var4);
         var1.put("GroupCacheEnable", var2);
         var2.setValue("description", "<p>Specifies whether the Group cache should be enabled. By default, the Group cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupCacheSize")) {
         var3 = "getGroupCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupCacheSize";
         }

         var2 = new PropertyDescriptor("GroupCacheSize", CachingRealmMBean.class, var3, var4);
         var1.put("GroupCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of group lookups to cache. This value should be a prime number for best performance.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(211));
         var2.setValue("legalMax", new Integer(65537));
         var2.setValue("legalMin", new Integer(17));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupCacheTTLNegative")) {
         var3 = "getGroupCacheTTLNegative";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupCacheTTLNegative";
         }

         var2 = new PropertyDescriptor("GroupCacheTTLNegative", CachingRealmMBean.class, var3, var4);
         var1.put("GroupCacheTTLNegative", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of an unsuccessful group lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupCacheTTLPositive")) {
         var3 = "getGroupCacheTTLPositive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupCacheTTLPositive";
         }

         var2 = new PropertyDescriptor("GroupCacheTTLPositive", CachingRealmMBean.class, var3, var4);
         var1.put("GroupCacheTTLPositive", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of a successful group lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupMembershipCacheTTL")) {
         var3 = "getGroupMembershipCacheTTL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupMembershipCacheTTL";
         }

         var2 = new PropertyDescriptor("GroupMembershipCacheTTL", CachingRealmMBean.class, var3, var4);
         var1.put("GroupMembershipCacheTTL", var2);
         var2.setValue("description", "<p>The number of seconds to store the members of a group before updating it.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PermissionCacheEnable")) {
         var3 = "getPermissionCacheEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPermissionCacheEnable";
         }

         var2 = new PropertyDescriptor("PermissionCacheEnable", CachingRealmMBean.class, var3, var4);
         var1.put("PermissionCacheEnable", var2);
         var2.setValue("description", "<p>Specifies whether the Permission cache should be enabled. By default, the Permission cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PermissionCacheSize")) {
         var3 = "getPermissionCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPermissionCacheSize";
         }

         var2 = new PropertyDescriptor("PermissionCacheSize", CachingRealmMBean.class, var3, var4);
         var1.put("PermissionCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of permission lookups to cache. This value should be a prime number for best performance.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(211));
         var2.setValue("legalMax", new Integer(65537));
         var2.setValue("legalMin", new Integer(17));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PermissionCacheTTLNegative")) {
         var3 = "getPermissionCacheTTLNegative";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPermissionCacheTTLNegative";
         }

         var2 = new PropertyDescriptor("PermissionCacheTTLNegative", CachingRealmMBean.class, var3, var4);
         var1.put("PermissionCacheTTLNegative", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of an unsuccessful permission lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PermissionCacheTTLPositive")) {
         var3 = "getPermissionCacheTTLPositive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPermissionCacheTTLPositive";
         }

         var2 = new PropertyDescriptor("PermissionCacheTTLPositive", CachingRealmMBean.class, var3, var4);
         var1.put("PermissionCacheTTLPositive", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of a successful permission lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserCacheEnable")) {
         var3 = "getUserCacheEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserCacheEnable";
         }

         var2 = new PropertyDescriptor("UserCacheEnable", CachingRealmMBean.class, var3, var4);
         var1.put("UserCacheEnable", var2);
         var2.setValue("description", "<p>Specifies whether the User cache should be enabled. By default, the User cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserCacheSize")) {
         var3 = "getUserCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserCacheSize";
         }

         var2 = new PropertyDescriptor("UserCacheSize", CachingRealmMBean.class, var3, var4);
         var1.put("UserCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of user lookups to cache. This value should be a prime number for best performance.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(211));
         var2.setValue("legalMax", new Integer(65537));
         var2.setValue("legalMin", new Integer(17));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserCacheTTLNegative")) {
         var3 = "getUserCacheTTLNegative";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserCacheTTLNegative";
         }

         var2 = new PropertyDescriptor("UserCacheTTLNegative", CachingRealmMBean.class, var3, var4);
         var1.put("UserCacheTTLNegative", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of an unsuccessful user lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserCacheTTLPositive")) {
         var3 = "getUserCacheTTLPositive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserCacheTTLPositive";
         }

         var2 = new PropertyDescriptor("UserCacheTTLPositive", CachingRealmMBean.class, var3, var4);
         var1.put("UserCacheTTLPositive", var2);
         var2.setValue("description", "<p>The number of seconds to retain the results of a successful user lookup.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
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
