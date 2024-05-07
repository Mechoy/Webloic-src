package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SNMPGaugeMonitorMBeanImplBeanInfo extends SNMPJMXMonitorMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPGaugeMonitorMBean.class;

   public SNMPGaugeMonitorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPGaugeMonitorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPGaugeMonitorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class describes the criteria for a Gauge-based Monitor. A notification will be generated when this criteria is satisfied.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPGaugeMonitorMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ThresholdHigh")) {
         var3 = "getThresholdHigh";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThresholdHigh";
         }

         var2 = new PropertyDescriptor("ThresholdHigh", SNMPGaugeMonitorMBean.class, var3, var4);
         var1.put("ThresholdHigh", var2);
         var2.setValue("description", "<p>The high threshold at which a notification should be generated. A notification is generated the first time the monitored value is equal to or greater than this value.</p> <p>Subsequent crossings of the high threshold value do not cause additional notifications unless the attribute value becomes equal to or less than the low threshold value.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ThresholdLow")) {
         var3 = "getThresholdLow";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThresholdLow";
         }

         var2 = new PropertyDescriptor("ThresholdLow", SNMPGaugeMonitorMBean.class, var3, var4);
         var1.put("ThresholdLow", var2);
         var2.setValue("description", "<p>The low threshold at which a notification should be generated. A notification is generated the first time the monitored value is less than or equal to this value.</p> <p> Subsequent crossings of the low threshold value do not cause additional notifications unless the attribute value becomes equal to or greater than the high threshold value. </p> ");
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
