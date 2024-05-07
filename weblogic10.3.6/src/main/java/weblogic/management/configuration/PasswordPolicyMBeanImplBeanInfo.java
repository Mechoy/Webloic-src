package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class PasswordPolicyMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PasswordPolicyMBean.class;

   public PasswordPolicyMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PasswordPolicyMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PasswordPolicyMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.PasswordPolicyMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("LockoutCacheSize")) {
         var3 = "getLockoutCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutCacheSize";
         }

         var2 = new PropertyDescriptor("LockoutCacheSize", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutCacheSize", var2);
         var2.setValue("description", "<p>The size of the cache (between 1 and 99999 kilobytes) used for invalid login attempts.</p>  <p>Size of cache of invalid login attempts.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("secureValue", new Integer(50));
         var2.setValue("legalMax", new Integer(99999));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutDuration")) {
         var3 = "getLockoutDuration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutDuration";
         }

         var2 = new PropertyDescriptor("LockoutDuration", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutDuration", var2);
         var2.setValue("description", "<p>The number of minutes (between 0 and 999999) that a user's account remains inaccessible after being locked.</p>  <p>Number of minutes that a user's account remains inaccessible after being locked in response to several invalid login attempts within the amount of time specified in the <tt>LockoutResetDuration</tt> attribute. In order to unlock a user account, you must have the unlockuser permission for the Password Policy MBean.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("secureValue", new Integer(30));
         var2.setValue("legalMax", new Integer(999999));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutGCThreshold")) {
         var3 = "getLockoutGCThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutGCThreshold";
         }

         var2 = new PropertyDescriptor("LockoutGCThreshold", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutGCThreshold", var2);
         var2.setValue("description", "<p>If the number of current invalid login attempts is equal to or great than this attribute, WebLogic Server deletes any expired records from the cache. The lower the value of this attribute, the more often WebLogic Server clears the cache. This may impact the performance of WebLogic Server. Set the attribute accordingly.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(400));
         var2.setValue("legalMax", new Integer(999999));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutResetDuration")) {
         var3 = "getLockoutResetDuration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutResetDuration";
         }

         var2 = new PropertyDescriptor("LockoutResetDuration", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutResetDuration", var2);
         var2.setValue("description", "<p>The number of minutes (between 0 and 999999) within which invalid login attempts must happen in order for the user's account to be locked.</p>  <p>Number of minutes within which the invalid login attempts must happen in order for the user's account to be locked.</p>  <p>An account is locked if the number of invalid login attempts defined in the <tt>Lockout Threshold</tt> attribute happens within the amount of time defined by this field.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("secureValue", new Integer(5));
         var2.setValue("legalMax", new Integer(99999));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutThreshold")) {
         var3 = "getLockoutThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutThreshold";
         }

         var2 = new PropertyDescriptor("LockoutThreshold", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutThreshold", var2);
         var2.setValue("description", "<p>The number of failed logins (between 1 and 99999) that can be tried for a user before their account is locked.</p>  <p>Number of failed logins for a user that can be tried before that account is locked. Any subsequent attempts to access the account (even if the username/password combination is correct) cause a security exception. If a security exception occurs, the account remains locked until it is explicitly unlocked by the system administrator or another login attempt is made after the lockout duration period ends. Note that invalid login attempts must be made within a span defined by the <tt>Lockout Reset Duration</tt> attribute to count toward the value of the <tt>Lockout Threshold</tt> attribute.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("secureValue", new Integer(5));
         var2.setValue("legalMax", new Integer(99999));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MinimumPasswordLength")) {
         var3 = "getMinimumPasswordLength";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinimumPasswordLength";
         }

         var2 = new PropertyDescriptor("MinimumPasswordLength", PasswordPolicyMBean.class, var3, var4);
         var1.put("MinimumPasswordLength", var2);
         var2.setValue("description", "<p>The minimum number of characters required for any password in this WebLogic Server domain.</p>  <p>The minimum number of characters required in any domain password.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(8));
         var2.setValue("secureValue", new Integer(20));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LockoutEnabled")) {
         var3 = "isLockoutEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLockoutEnabled";
         }

         var2 = new PropertyDescriptor("LockoutEnabled", PasswordPolicyMBean.class, var3, var4);
         var1.put("LockoutEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this WebLogic Server domain tracks invalid login attempts and takes appropriate action. (The remaining fields on this page are relevant only if you check this box.)</p>  <p>Controls whether or not WebLogic Server tracks invalid login attempts and takes appropriate action.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
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
