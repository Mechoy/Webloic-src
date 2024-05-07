package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class DomainLogFilterMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DomainLogFilterMBean.class;

   public DomainLogFilterMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DomainLogFilterMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DomainLogFilterMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link LogFilterMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents a filter to qualify log messages which are logged to the domain logfile. A message must qualify all criteria specified to qualify the filter.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.DomainLogFilterMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", DomainLogFilterMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SeverityLevel")) {
         var3 = "getSeverityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSeverityLevel";
         }

         var2 = new PropertyDescriptor("SeverityLevel", DomainLogFilterMBean.class, var3, var4);
         var1.put("SeverityLevel", var2);
         var2.setValue("description", "<p>The minimum severity of a message that this domain log filter forwards to the domain log. All messages with the specified severity and higher will be sent to the domain log.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.logging.Severities")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(16));
         var2.setValue("legalValues", new Object[]{new Integer(64), new Integer(16), new Integer(8), new Integer(32), new Integer(4), new Integer(2), new Integer(1)});
         var2.setValue("deprecated", "This attibute is deprecated in favor of LogMBean's DomainLogBroadcastSeverity attribute. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SubsystemNames")) {
         var3 = "getSubsystemNames";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSubsystemNames";
         }

         var2 = new PropertyDescriptor("SubsystemNames", DomainLogFilterMBean.class, var3, var4);
         var1.put("SubsystemNames", var2);
         var2.setValue("description", "<p>The subsystems for which this filter can accept messages. If no subsystems are chosen, the filter accepts messages from all subsystems.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("UserIds")) {
         var3 = "getUserIds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserIds";
         }

         var2 = new PropertyDescriptor("UserIds", DomainLogFilterMBean.class, var3, var4);
         var1.put("UserIds", var2);
         var2.setValue("description", "<p>The user ids for which associated messages are accepted for publishing. If no ids are specified, messages from all user ids can be sent to the log.</p>  <p>Every message includes the user id under which the associated event was executed. To execute some pieces of internal code, WebLogic Server authenticates the id of the user who initiates the execution and then runs the code under a special Kernel Identity user id. J2EE modules such as EJBs that are deployed onto a server instance report the user id that the module passes to the server.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
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
      Method var3 = DomainLogFilterMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = DomainLogFilterMBean.class.getMethod("restoreDefaultValue", String.class);
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
