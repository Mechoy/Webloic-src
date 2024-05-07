package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServiceReliabilityMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceReliabilityMBean.class;

   public WebServiceReliabilityMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServiceReliabilityMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServiceReliabilityMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents reliability configuration for web services.</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServiceReliabilityMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AcknowledgementInterval")) {
         var3 = "getAcknowledgementInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcknowledgementInterval";
         }

         var2 = new PropertyDescriptor("AcknowledgementInterval", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("AcknowledgementInterval", var2);
         var2.setValue("description", "The maximum time a pending acknowledgement (set after the destination accepts a message) can wait before being delivered back to the RM source. String value in ‘Duration’ format. Defaults to ‘P0DT0.2S (200 milliseconds). Set at sequence creation time, and cannot be reset. ");
         setPropertyDescriptorDefault(var2, "P0DT0.2S");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BaseRetransmissionInterval")) {
         var3 = "getBaseRetransmissionInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBaseRetransmissionInterval";
         }

         var2 = new PropertyDescriptor("BaseRetransmissionInterval", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("BaseRetransmissionInterval", var2);
         var2.setValue("description", "The interval of time that must pass before a message will be retransmitted to the RM destination (in the event a prior transmission failed). String value in ‘Duration’ format. Defaults to ‘P0DT3S’ (3 seconds). Set at sequence creation time, and cannot be reset. ");
         setPropertyDescriptorDefault(var2, "P0DT3S");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InactivityTimeout")) {
         var3 = "getInactivityTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInactivityTimeout";
         }

         var2 = new PropertyDescriptor("InactivityTimeout", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("InactivityTimeout", var2);
         var2.setValue("description", "If during this duration, an endpoint (RM source or RM destination) has received no application or control messages, the endpoint MAY consider the RM Sequence to have been terminated due to inactivity. String value in ‘Duration’ format. Defaults to ‘P0DT600S’ (600 seconds). Implementations of RM source and RM destination are free to manage resources associated with the sequence as they please, but in general, there are no guarantees that the sequence will be useable by either party after the inactivity timeout expires. Set at sequence creation time, and cannot be reset. ");
         setPropertyDescriptorDefault(var2, "P0DT600S");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SequenceExpiration")) {
         var3 = "getSequenceExpiration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSequenceExpiration";
         }

         var2 = new PropertyDescriptor("SequenceExpiration", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("SequenceExpiration", var2);
         var2.setValue("description", "This is the maximum lifetime of a sequence. If this limit is reached before the sequence naturally completes, it will be forcibly terminated. String value in ‘Duration’ format. Defaults to ‘P1D’ (1 day). Set at sequence creation time, and cannot be reset. ");
         setPropertyDescriptorDefault(var2, "P1D");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NonBufferedDestination")) {
         var3 = "isNonBufferedDestination";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNonBufferedDestination";
         }

         var2 = new PropertyDescriptor("NonBufferedDestination", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("NonBufferedDestination", var2);
         var2.setValue("description", "A boolean flag indicating that RM destinations, by default, will receive non-buffered. Defaults to false. Note, changes to this default will only be picked up by new reliable sequences. Existing reliable sequences have their persistence handling set at creation time and these values will not change. ");
         setPropertyDescriptorDefault(var2, false);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NonBufferedSource")) {
         var3 = "isNonBufferedSource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNonBufferedSource";
         }

         var2 = new PropertyDescriptor("NonBufferedSource", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("NonBufferedSource", var2);
         var2.setValue("description", "A boolean flag indicating that RM sources, by default, will send non-buffered. Defaults to false. Note, changes to this default will only be picked up by new reliable sequences. Existing reliable sequences have their persistence handling set at creation time and these values will not change. ");
         setPropertyDescriptorDefault(var2, false);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetransmissionExponentialBackoff")) {
         var3 = "isRetransmissionExponentialBackoff";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetransmissionExponentialBackoff";
         }

         var2 = new PropertyDescriptor("RetransmissionExponentialBackoff", WebServiceReliabilityMBean.class, var3, var4);
         var1.put("RetransmissionExponentialBackoff", var2);
         var2.setValue("description", "A boolean flag indicating that the retransmission interval will be adjusted using the exponential backoff algorithm ([Tanenbaum]). Defaults to false. Set at sequence creation time, and cannot be reset. ");
         setPropertyDescriptorDefault(var2, false);
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
