package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class UserLockoutManagerMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserLockoutManagerMBean.class;

   public UserLockoutManagerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserLockoutManagerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserLockoutManagerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("Lists and manages lockouts on user accounts.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.UserLockoutManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("InvalidLoginAttemptsTotalCount")) {
         var3 = "getInvalidLoginAttemptsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginAttemptsTotalCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("InvalidLoginAttemptsTotalCount", var2);
         var2.setValue("description", "Returns the number of invalid logins attempted since this server has been started and lockouts have been enabled.  In a cluster, this method returns the number of invalid logins attempted that have occured since the cluster has been started because all servers share login failure information. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getInvalidLoginAttemptsTotalCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("InvalidLoginUsersHighCount")) {
         var3 = "getInvalidLoginUsersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginUsersHighCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("InvalidLoginUsersHighCount", var2);
         var2.setValue("description", "Returns the highest number of users with concurrent unexpired or uncleared invalid login attempts.  Invalid login attempts expire as specified by <code>LockoutResetDuration</code>. This count is useful in determining whether the <code>LockoutCacheSize</code> needs to be modified. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getInvalidLoginUsersHighCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("LockedUsersCurrentCount")) {
         var3 = "getLockedUsersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("LockedUsersCurrentCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockedUsersCurrentCount", var2);
         var2.setValue("description", "Returns the number of users that are currently locked out of this server. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getLockedUsersCurrentCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutCacheSize")) {
         var3 = "getLockoutCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutCacheSize";
         }

         var2 = new PropertyDescriptor("LockoutCacheSize", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutCacheSize", var2);
         var2.setValue("description", "Returns the number of invalid login records that the server places in a cache. The server creates one record for each invalid login. ");
         setPropertyDescriptorDefault(var2, new Long(5L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LockoutDuration")) {
         var3 = "getLockoutDuration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutDuration";
         }

         var2 = new PropertyDescriptor("LockoutDuration", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutDuration", var2);
         var2.setValue("description", "Returns the number of minutes that a user account is locked out. ");
         setPropertyDescriptorDefault(var2, new Long(30L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LockoutGCThreshold")) {
         var3 = "getLockoutGCThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutGCThreshold";
         }

         var2 = new PropertyDescriptor("LockoutGCThreshold", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutGCThreshold", var2);
         var2.setValue("description", "Returns the maximum number of invalid login records that the server keeps in memory.  If the number of invalid login records is equal to or greater than this value, the server's garbage collection purges the records that have expired. A record expires when the user associated with the record has been locked out. <p> The lower the threshold, the more often the server uses its resources to collect garbage. ");
         setPropertyDescriptorDefault(var2, new Long(400L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LockoutResetDuration")) {
         var3 = "getLockoutResetDuration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutResetDuration";
         }

         var2 = new PropertyDescriptor("LockoutResetDuration", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutResetDuration", var2);
         var2.setValue("description", "The number of minutes within which consecutive invalid login attempts cause the user account to be locked out. ");
         setPropertyDescriptorDefault(var2, new Long(5L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LockoutThreshold")) {
         var3 = "getLockoutThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutThreshold";
         }

         var2 = new PropertyDescriptor("LockoutThreshold", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutThreshold", var2);
         var2.setValue("description", "Returns the maximum number of consecutive invalid login attempts before account is locked out.  When the number of invalid logins within a specified period of time is greater than <code>LockoutThreshold</code>value, the user is locked out. For example, with the default setting of <code>1</code>, the user is locked out on the second consecutive invalid login. With a setting of <code>2</code>, the user is locked out on the third consecutive invalid login. ");
         setPropertyDescriptorDefault(var2, new Long(5L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LoginAttemptsWhileLockedTotalCount")) {
         var3 = "getLoginAttemptsWhileLockedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("LoginAttemptsWhileLockedTotalCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LoginAttemptsWhileLockedTotalCount", var2);
         var2.setValue("description", "Returns the number of invalid logins attempted since this server has been started and lockouts have been enabled. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getLoginAttemptsWhileLockedTotalCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", UserLockoutManagerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "The name of this configuration. WebLogic Server uses an MBean to implement and persist the configuration. ");
         setPropertyDescriptorDefault(var2, "UserLockoutManager");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("legal", "");
      }

      if (!var1.containsKey("Realm")) {
         var3 = "getRealm";
         var4 = null;
         var2 = new PropertyDescriptor("Realm", UserLockoutManagerMBean.class, var3, var4);
         var1.put("Realm", var2);
         var2.setValue("description", "Returns the realm that contains this user lockout manager. Returns null if this security provider is not contained by a realm. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UnlockedUsersTotalCount")) {
         var3 = "getUnlockedUsersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UnlockedUsersTotalCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("UnlockedUsersTotalCount", var2);
         var2.setValue("description", "Returns the number times users have been unlocked since this server has been started. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getUnlockedUsersTotalCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("UserLockoutTotalCount")) {
         var3 = "getUserLockoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UserLockoutTotalCount", UserLockoutManagerMBean.class, var3, var4);
         var1.put("UserLockoutTotalCount", var2);
         var2.setValue("description", "Returns the number of user lockouts that have occured since this server has been started.  In a cluster, this method returns the number of user lockouts that have occured since the cluster has been started because all servers share login failure information. ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getUserLockoutTotalCount()} ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutEnabled")) {
         var3 = "isLockoutEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutEnabled";
         }

         var2 = new PropertyDescriptor("LockoutEnabled", UserLockoutManagerMBean.class, var3, var4);
         var1.put("LockoutEnabled", var2);
         var2.setValue("description", "Returns whether the server locks out users when there are invalid login attempts.  A <code>true</code> value for this attribute causes the server to consider the other attributes of this MBean. A <code>false</code> value causes the server to ignore the other attributes of this MBean.\" ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
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
      Method var3 = UserLockoutManagerMBean.class.getMethod("isLockedOut", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#isLockedOut(String)} ");
         var1.put(var5, var2);
         var2.setValue("description", "Indicates whether a user is locked out. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerMBean.class.getMethod("clearLockout", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#clearLockout(String)} ");
         var1.put(var5, var2);
         var2.setValue("description", "Unlocks a user account. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerMBean.class.getMethod("getLastLoginFailure", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getLastLoginFailure(String)} ");
         var1.put(var5, var2);
         var2.setValue("description", "Returns a string that indicates the time of the last invalid login for this user. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerMBean.class.getMethod("getLoginFailureCount", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.UserLockoutManagerRuntimeMBean#getLoginFailureCount(String)} ");
         var1.put(var5, var2);
         var2.setValue("description", "Returns the current count of login failures for a specific user. This value returns to ");
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
