package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebserviceTimestampMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebserviceTimestampMBean.class;

   public WebserviceTimestampMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebserviceTimestampMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebserviceTimestampMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Encapsulates the timestamp information that is associated with a Web Service security configuration.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebserviceTimestampMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ClockPrecision")) {
         var3 = "getClockPrecision";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClockPrecision";
         }

         var2 = new PropertyDescriptor("ClockPrecision", WebserviceTimestampMBean.class, var3, var4);
         var1.put("ClockPrecision", var2);
         var2.setValue("description", "DEPRECATED -- Use set/getClockSkew() -- If both ClockSkew and ClockPrecision are defined, then ClockSkew takes precedence and is used. <p>If clocks are synchronized, this attribute describes the accuracy of the synchronization.</p> ");
         setPropertyDescriptorDefault(var2, new Long(60000L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClockSkew")) {
         var3 = "getClockSkew";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClockSkew";
         }

         var2 = new PropertyDescriptor("ClockSkew", WebserviceTimestampMBean.class, var3, var4);
         var1.put("ClockSkew", var2);
         var2.setValue("description", "<p> ClockSkew takes precedence over ClockPrecision if both are defined, as ClockPrecision has been DEPRECATED. <p>If clocks are synchronized, this attribute describes the accuracy of the synchronization between two clocks: the client and the server.</p>  <p>ClockSkew is expressed in milliseconds. Clock skew is enforced by rendering all times into milliseconds since a common time 0 and using these times for comparisons. For example, if you're clocks are accurate to within 1 minute of each other, you would set your skew to 1 minute * 60 seconds * 1000 milliseconds or 60000. </p> ");
         setPropertyDescriptorDefault(var2, new Long(60000L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxProcessingDelay")) {
         var3 = "getMaxProcessingDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxProcessingDelay";
         }

         var2 = new PropertyDescriptor("MaxProcessingDelay", WebserviceTimestampMBean.class, var3, var4);
         var1.put("MaxProcessingDelay", var2);
         var2.setValue("description", "<p>Specifies the freshness policy for received messages: the Web Service observes the processing delay by subtracting the Created time in the Timestamp from the current time.</p>  <p>If the observed processing delay is greater than maxProcessingDelay plus clockSkew, then the message is  rejected as stale.</p>  <p>This attribute is specified in milliseconds.</p>  <p>Setting maxProcessingDelay to NO_MAX_PROCESSING_DELAY disables to enforcement of the freshness policy.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ValidityPeriod")) {
         var3 = "getValidityPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setValidityPeriod";
         }

         var2 = new PropertyDescriptor("ValidityPeriod", WebserviceTimestampMBean.class, var3, var4);
         var1.put("ValidityPeriod", var2);
         var2.setValue("description", "<p>Represents the length of time the sender wants the outbound message to be valid. </p>  <p>When the validityPeriod is positive, the TimestampHandler inserts an Expires element into the Timestamp header.   The validityPeriod is expressed in seconds:  the Expires time will be that many seconds ahead of the Timestamp's Created time.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClockSynchronized")) {
         var3 = "isClockSynchronized";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClockSynchronized";
         }

         var2 = new PropertyDescriptor("ClockSynchronized", WebserviceTimestampMBean.class, var3, var4);
         var1.put("ClockSynchronized", var2);
         var2.setValue("description", "<p>Specifies whether the Web Service assumes synchronized clocks.</p>  <p>If the clockSynchronized attribute is false, the Web Service rejects all inbound messages with that contain expirations, because this is the only safe way to ensure that the message hasn't already expired. In this case, the Web Service also does not enforce a freshness policy. <p>If this attribute is set to true, then the Web Service enforces expirations on inbound messages to the best of its ability and enforces an optional freshness policy (via maxProcessingDelay).  </p>  <p>The default value of this attribute is true.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LaxPrecision")) {
         var3 = "isLaxPrecision";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLaxPrecision";
         }

         var2 = new PropertyDescriptor("LaxPrecision", WebserviceTimestampMBean.class, var3, var4);
         var1.put("LaxPrecision", var2);
         var2.setValue("description", "<p>DEPRECATED. </p> ");
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
