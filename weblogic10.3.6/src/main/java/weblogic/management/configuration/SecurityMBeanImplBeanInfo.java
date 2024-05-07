package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SecurityMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SecurityMBean.class;

   public SecurityMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SecurityMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SecurityMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0  Replaced by {@link SecurityConfigurationMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Specifies the security properties of a WebLogic domain.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SecurityMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AuditProviderClassName")) {
         var3 = "getAuditProviderClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuditProviderClassName";
         }

         var2 = new PropertyDescriptor("AuditProviderClassName", SecurityMBean.class, var3, var4);
         var1.put("AuditProviderClassName", var2);
         var2.setValue("description", "The name of the Java class that implements a 6.x Auditing provider. The Auditing provider must be an implementation of the <tt>weblogic.security.audit.AuditProvider</tt> interface. This interface is only available in Compatibility mode. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionFilter")) {
         var3 = "getConnectionFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFilter";
         }

         var2 = new PropertyDescriptor("ConnectionFilter", SecurityMBean.class, var3, var4);
         var1.put("ConnectionFilter", var2);
         var2.setValue("description", "<p>The name of the Java class that implements a connection filter (that is, the <tt>weblogic.security.net.ConnectionFilter</tt> interface). If no class name is specified, no connection filter will be used.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link SecurityConfigurationMBean#getConnectionFilter()} ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionFilterRules")) {
         var3 = "getConnectionFilterRules";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFilterRules";
         }

         var2 = new PropertyDescriptor("ConnectionFilterRules", SecurityMBean.class, var3, var4);
         var1.put("ConnectionFilterRules", var2);
         var2.setValue("description", "<p>The rules used by any connection filter that implements the <tt>ConnectionFilterRulesListener</tt> interface. When using the default implementation and when no rules are specified, all connections are accepted. The default implementation rules are in the format: <tt>target localAddress localPort action protocols</tt>.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link SecurityConfigurationMBean#getConnectionFilterRules()} ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionLoggerEnabled")) {
         var3 = "getConnectionLoggerEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionLoggerEnabled";
         }

         var2 = new PropertyDescriptor("ConnectionLoggerEnabled", SecurityMBean.class, var3, var4);
         var1.put("ConnectionLoggerEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this WebLogic Server domain should log accepted connections.</p>  <p>This attribute can be used by a system administrator to dynamically check the incoming connections in the log file to determine if filtering needs to be performed.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link SecurityConfigurationMBean#getConnectionLoggerEnabled()} ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LogAllChecksEnabled")) {
         var3 = "getLogAllChecksEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogAllChecksEnabled";
         }

         var2 = new PropertyDescriptor("LogAllChecksEnabled", SecurityMBean.class, var3, var4);
         var1.put("LogAllChecksEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this WebLogic Server domain should log all security checks.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SecurityMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordPolicy")) {
         var3 = "getPasswordPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordPolicy";
         }

         var2 = new PropertyDescriptor("PasswordPolicy", SecurityMBean.class, var3, var4);
         var1.put("PasswordPolicy", var2);
         var2.setValue("description", "<p>Sets the password policy.</p>  <p>This interface is used in Compatibility Security.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Realm")) {
         var3 = "getRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRealm";
         }

         var2 = new PropertyDescriptor("Realm", SecurityMBean.class, var3, var4);
         var1.put("Realm", var2);
         var2.setValue("description", "<p>Sets the realm policies.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerSecurityRuntime")) {
         var3 = "getServerSecurityRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ServerSecurityRuntime", SecurityMBean.class, var3, var4);
         var1.put("ServerSecurityRuntime", var2);
         var2.setValue("description", "<p>Returns the state of security on the specified WebLogic Server. Returns null if the specified server is not running.</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemUser")) {
         var3 = "getSystemUser";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemUser";
         }

         var2 = new PropertyDescriptor("SystemUser", SecurityMBean.class, var3, var4);
         var1.put("SystemUser", var2);
         var2.setValue("description", "<p>The name of the <tt>system</tt> user. This attribute must be specified.</p> ");
         setPropertyDescriptorDefault(var2, "system");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CompatibilityMode")) {
         var3 = "isCompatibilityMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompatibilityMode";
         }

         var2 = new PropertyDescriptor("CompatibilityMode", SecurityMBean.class, var3, var4);
         var1.put("CompatibilityMode", var2);
         var2.setValue("description", "<p>Indicates whether migration from a WebLogic Server 6.x security configuration is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GuestDisabled")) {
         var3 = "isGuestDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGuestDisabled";
         }

         var2 = new PropertyDescriptor("GuestDisabled", SecurityMBean.class, var3, var4);
         var1.put("GuestDisabled", var2);
         var2.setValue("description", "<p>Indicates whether guest logins can be used to access WebLogic resources in this WebLogic Server domain.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
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
      Method var3 = SecurityMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SecurityMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
