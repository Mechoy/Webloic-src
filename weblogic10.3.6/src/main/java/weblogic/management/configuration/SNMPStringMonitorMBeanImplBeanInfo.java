package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SNMPStringMonitorMBeanImplBeanInfo extends SNMPJMXMonitorMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPStringMonitorMBean.class;

   public SNMPStringMonitorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPStringMonitorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPStringMonitorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class describes the criteria for a String-based Monitor. A notification is generated when this criteria is satisfied.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPStringMonitorMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("StringToCompare")) {
         var3 = "getStringToCompare";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStringToCompare";
         }

         var2 = new PropertyDescriptor("StringToCompare", SNMPStringMonitorMBean.class, var3, var4);
         var1.put("StringToCompare", var2);
         var2.setValue("description", "<p>The string against which the value of the monitored attribute will be compared.</p>  <p>A notification is generated when the criteria specified by Notify Match or Notify Differ is satisfied.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NotifyDiffer")) {
         var3 = "isNotifyDiffer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotifyDiffer";
         }

         var2 = new PropertyDescriptor("NotifyDiffer", SNMPStringMonitorMBean.class, var3, var4);
         var1.put("NotifyDiffer", var2);
         var2.setValue("description", "<p>Generates a notification if the value of the monitored attribute and the value of String to Compare are different.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NotifyMatch")) {
         var3 = "isNotifyMatch";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotifyMatch";
         }

         var2 = new PropertyDescriptor("NotifyMatch", SNMPStringMonitorMBean.class, var3, var4);
         var1.put("NotifyMatch", var2);
         var2.setValue("description", "<p>Generates a notification if the value of the monitored attribute and the value of String to Compare are the same.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
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
