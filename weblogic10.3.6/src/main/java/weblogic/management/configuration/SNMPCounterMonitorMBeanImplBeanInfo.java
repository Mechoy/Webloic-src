package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPCounterMonitorMBeanImplBeanInfo extends SNMPJMXMonitorMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPCounterMonitorMBean.class;

   public SNMPCounterMonitorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPCounterMonitorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPCounterMonitorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class describes the criteria for a Counter-based Monitor. A notification will be generated when this criteria is satisfied.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPCounterMonitorMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Modulus")) {
         var3 = "getModulus";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setModulus";
         }

         var2 = new PropertyDescriptor("Modulus", SNMPCounterMonitorMBean.class, var3, var4);
         var1.put("Modulus", var2);
         var2.setValue("description", "<p>An integer value to be subtracted from the threshold value when the threshold value is crossed.</p>  <p>If Modulus is 0, a notification is generated each time the agent polls the monitored attribute and its value still exceeds or equals the threshold value.</p>  <p>If Modulus is larger than 0, the value of the modulus is subtracted from the threshold each time the threshold is crossed.</p> ");
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Offset")) {
         var3 = "getOffset";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOffset";
         }

         var2 = new PropertyDescriptor("Offset", SNMPCounterMonitorMBean.class, var3, var4);
         var1.put("Offset", var2);
         var2.setValue("description", "<p>An integer value to be added to the threshold value each time the observed value equals or exceeds the threshold.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getThreshold()")};
         var2.setValue("see", var5);
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Threshold")) {
         var3 = "getThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreshold";
         }

         var2 = new PropertyDescriptor("Threshold", SNMPCounterMonitorMBean.class, var3, var4);
         var1.put("Threshold", var2);
         var2.setValue("description", "<p>Specifies a value that triggers the Counter Monitor to generate a notification.</p>  <p>The monitor generates a notification the first time the observed value transitions from below the threshold to at or above the threshold. While the observed value remains at or above the threshold, the Counter Monitor does not generate additional notifications. If the observed value falls below the threshold and then later equals or exceeds the threshold, the Counter Monitor does not generate an additional notification.</p>  <p>You can specify an offset value to cause this threshold value to increase each time the observed value equals or exceeds the threshold. The first time the observed value equals or exceeds the new threshold value, this monitor generates a notification and adds the offset value to the new threshold value.</p>  <p>For example, if you set Threshold to 1000 and Offset to 2000, when the observed attribute equals or exceeds 1000, the Counter Monitor sends a notification and increases the threshold to 3000. When the observed attribute equals or exceeds 3000, the Counter Monitor sends a notification and increases the threshold again to 5000.</p> ");
         var2.setValue("legalMin", new Integer(0));
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
