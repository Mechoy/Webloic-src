package weblogic.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.UserLockoutManagerRuntimeMBean;

public class UserLockoutManagerRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserLockoutManagerRuntimeMBean.class;

   public UserLockoutManagerRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserLockoutManagerRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserLockoutManagerRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.security");
      String var3 = (new String("<p>This class is used to monitor and manage per security realm user lockout information.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.UserLockoutManagerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("InvalidLoginAttemptsTotalCount")) {
         var3 = "getInvalidLoginAttemptsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginAttemptsTotalCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("InvalidLoginAttemptsTotalCount", var2);
         var2.setValue("description", "Returns the number of invalid logins attempted since this server has been started and lockouts have been enabled.  In a cluster, this method returns the number of invalid logins attempted that have occured since the cluster has been started because all servers share login failure information. ");
      }

      if (!var1.containsKey("InvalidLoginUsersHighCount")) {
         var3 = "getInvalidLoginUsersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginUsersHighCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("InvalidLoginUsersHighCount", var2);
         var2.setValue("description", "Returns the highest number of users with concurrent unexpired or uncleared invalid login attempts.  Invalid login attempts expire as specified by <code>LockoutResetDuration</code>. This count is useful in determining whether the <code>LockoutCacheSize</code> needs to be modified. ");
      }

      if (!var1.containsKey("LockedUsersCurrentCount")) {
         var3 = "getLockedUsersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("LockedUsersCurrentCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("LockedUsersCurrentCount", var2);
         var2.setValue("description", "Returns the number of users that are currently locked out of this server. ");
      }

      if (!var1.containsKey("LoginAttemptsWhileLockedTotalCount")) {
         var3 = "getLoginAttemptsWhileLockedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("LoginAttemptsWhileLockedTotalCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("LoginAttemptsWhileLockedTotalCount", var2);
         var2.setValue("description", "Returns the number of invalid logins attempted since this server has been started and lockouts have been enabled. ");
      }

      if (!var1.containsKey("UnlockedUsersTotalCount")) {
         var3 = "getUnlockedUsersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UnlockedUsersTotalCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("UnlockedUsersTotalCount", var2);
         var2.setValue("description", "Returns the number times users have been unlocked since this server has been started. ");
      }

      if (!var1.containsKey("UserLockoutTotalCount")) {
         var3 = "getUserLockoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UserLockoutTotalCount", UserLockoutManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("UserLockoutTotalCount", var2);
         var2.setValue("description", "Returns the number of user lockouts that have occured since this server has been started.  In a cluster, this method returns the number of user lockouts that have occured since the cluster has been started because all servers share login failure information. ");
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
      Method var3 = UserLockoutManagerRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerRuntimeMBean.class.getMethod("isLockedOut", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates whether a user is locked out. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerRuntimeMBean.class.getMethod("clearLockout", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Unlocks a user account. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerRuntimeMBean.class.getMethod("getLastLoginFailure", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Returns a string that indicates the time of the last invalid login for this user. ");
         var2.setValue("role", "operation");
      }

      var3 = UserLockoutManagerRuntimeMBean.class.getMethod("getLoginFailureCount", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- A user name. If the user does not exist, this method returns <code>false</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
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
