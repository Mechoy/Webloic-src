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
import weblogic.management.runtime.ServerSecurityRuntimeMBean;

public class SecurityRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerSecurityRuntimeMBean.class;

   public SecurityRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SecurityRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SecurityRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.security");
      String var3 = (new String("<p>This class is used for monitoring WebLogic Security Info.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServerSecurityRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DefaultRealmRuntime")) {
         var3 = "getDefaultRealmRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("DefaultRealmRuntime", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("DefaultRealmRuntime", var2);
         var2.setValue("description", "<p> Returns the realm runtime mbean for the default realm from when this server was booted. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("InvalidLoginAttemptsTotalCount")) {
         var3 = "getInvalidLoginAttemptsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginAttemptsTotalCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("InvalidLoginAttemptsTotalCount", var2);
         var2.setValue("description", "<p>The total number of invalid logins that have been attempted on this server instance.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("InvalidLoginUsersHighCount")) {
         var3 = "getInvalidLoginUsersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvalidLoginUsersHighCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("InvalidLoginUsersHighCount", var2);
         var2.setValue("description", "<p>The highest number of users with outstanding invalid login attempts on this server instance.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockedUsersCurrentCount")) {
         var3 = "getLockedUsersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("LockedUsersCurrentCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("LockedUsersCurrentCount", var2);
         var2.setValue("description", "<p>The current number of locked users on this server instance.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LoginAttemptsWhileLockedTotalCount")) {
         var3 = "getLoginAttemptsWhileLockedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("LoginAttemptsWhileLockedTotalCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("LoginAttemptsWhileLockedTotalCount", var2);
         var2.setValue("description", "<p>The total number of invalid logins that have been attempted on this server instance while a user was locked.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UnlockedUsersTotalCount")) {
         var3 = "getUnlockedUsersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UnlockedUsersTotalCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("UnlockedUsersTotalCount", var2);
         var2.setValue("description", "<p>The total number of times a user of this server instance has been unlocked.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserLockoutTotalCount")) {
         var3 = "getUserLockoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("UserLockoutTotalCount", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("UserLockoutTotalCount", var2);
         var2.setValue("description", "<p>The total number of user lockouts that have occurred on this server instance.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JACCEnabled")) {
         var3 = "isJACCEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("JACCEnabled", ServerSecurityRuntimeMBean.class, var3, (String)var4);
         var1.put("JACCEnabled", var2);
         var2.setValue("description", "<p>Indicates whether JACC (Java Authorization Contract for Containers) was enabled on the commandline for the jvm hosting this server</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("since", "9.0.0.0");
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
      Method var3 = ServerSecurityRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ServerSecurityRuntimeMBean.class.getMethod("isLockedOut", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("userName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether a user is locked out of her or his account.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ServerSecurityRuntimeMBean.class.getMethod("clearLockout", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("userName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Unlocks a locked user account.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ServerSecurityRuntimeMBean.class.getMethod("getLastLoginFailure", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("userName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The time of the last login failure for the specified user.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ServerSecurityRuntimeMBean.class.getMethod("getLoginFailureCount", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("userName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The current count of login failures for the specified user.</p>  <p>This value returns to <code>0</code> after a user successfully logs in or any lockout is cleared.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#clearLockout")};
         var2.setValue("see", var6);
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
