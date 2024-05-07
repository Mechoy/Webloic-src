package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class KernelDebugMBeanImplBeanInfo extends DebugMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = KernelDebugMBean.class;

   public KernelDebugMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public KernelDebugMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = KernelDebugMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Defines the debug attributes that are common to WebLogic Server and Client.</p> <p>While all attributes will be supported in adherence with the standard WebLogic Server deprecation policies, the resultant debug content is free to change in both form and content across releases.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.KernelDebugMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DebugAbbreviation")) {
         var3 = "getDebugAbbreviation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugAbbreviation";
         }

         var2 = new PropertyDescriptor("DebugAbbreviation", KernelDebugMBean.class, var3, var4);
         var1.put("DebugAbbreviation", var2);
         var2.setValue("description", "<p>Debug abbreviations over JVM to JVM connections</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugConnection")) {
         var3 = "getDebugConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugConnection";
         }

         var2 = new PropertyDescriptor("DebugConnection", KernelDebugMBean.class, var3, var4);
         var1.put("DebugConnection", var2);
         var2.setValue("description", "<p>Debug JVM to JVM connections</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDGCEnrollment")) {
         var3 = "getDebugDGCEnrollment";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDGCEnrollment";
         }

         var2 = new PropertyDescriptor("DebugDGCEnrollment", KernelDebugMBean.class, var3, var4);
         var1.put("DebugDGCEnrollment", var2);
         var2.setValue("description", "<p>Debug each DGC enrollment.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugFailOver")) {
         var3 = "getDebugFailOver";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugFailOver";
         }

         var2 = new PropertyDescriptor("DebugFailOver", KernelDebugMBean.class, var3, var4);
         var1.put("DebugFailOver", var2);
         var2.setValue("description", "<p>Debug stub-level fail-over processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugIIOP")) {
         var3 = "getDebugIIOP";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOP";
         }

         var2 = new PropertyDescriptor("DebugIIOP", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOP", var2);
         var2.setValue("description", "<p>Debug IIOP processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPConnection")) {
         var3 = "getDebugIIOPConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPConnection";
         }

         var2 = new PropertyDescriptor("DebugIIOPConnection", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPConnection", var2);
         var2.setValue("description", "<p>Debug IIOP connection management processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPMarshal")) {
         var3 = "getDebugIIOPMarshal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPMarshal";
         }

         var2 = new PropertyDescriptor("DebugIIOPMarshal", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPMarshal", var2);
         var2.setValue("description", "<p>Debug buffer-level IIOP processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPOTS")) {
         var3 = "getDebugIIOPOTS";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPOTS";
         }

         var2 = new PropertyDescriptor("DebugIIOPOTS", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPOTS", var2);
         var2.setValue("description", "<p>Debug IIOP Object Transaction Service (OTS) processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPReplacer")) {
         var3 = "getDebugIIOPReplacer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPReplacer";
         }

         var2 = new PropertyDescriptor("DebugIIOPReplacer", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPReplacer", var2);
         var2.setValue("description", "<p>Debug IIOP object replacement processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPSecurity")) {
         var3 = "getDebugIIOPSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPSecurity";
         }

         var2 = new PropertyDescriptor("DebugIIOPSecurity", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPSecurity", var2);
         var2.setValue("description", "<p>Debug IIOP security processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPStartup")) {
         var3 = "getDebugIIOPStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPStartup";
         }

         var2 = new PropertyDescriptor("DebugIIOPStartup", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPStartup", var2);
         var2.setValue("description", "<p>Debug IIOP startup processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPTransport")) {
         var3 = "getDebugIIOPTransport";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPTransport";
         }

         var2 = new PropertyDescriptor("DebugIIOPTransport", KernelDebugMBean.class, var3, var4);
         var1.put("DebugIIOPTransport", var2);
         var2.setValue("description", "<p>Debug IIOP message processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugLoadBalancing")) {
         var3 = "getDebugLoadBalancing";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugLoadBalancing";
         }

         var2 = new PropertyDescriptor("DebugLoadBalancing", KernelDebugMBean.class, var3, var4);
         var1.put("DebugLoadBalancing", var2);
         var2.setValue("description", "<p>Debug stub-level load-balancing processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugMessaging")) {
         var3 = "getDebugMessaging";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessaging";
         }

         var2 = new PropertyDescriptor("DebugMessaging", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMessaging", var2);
         var2.setValue("description", "<p>Debug messages sent over JVM to JVM connections</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugMuxer")) {
         var3 = "getDebugMuxer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMuxer";
         }

         var2 = new PropertyDescriptor("DebugMuxer", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMuxer", var2);
         var2.setValue("description", "<p>Debug Muxer processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("DebugMuxerConnection")) {
         var3 = "getDebugMuxerConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMuxerConnection";
         }

         var2 = new PropertyDescriptor("DebugMuxerConnection", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMuxerConnection", var2);
         var2.setValue("description", "<p>Debug Muxer connection processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("DebugMuxerDetail")) {
         var3 = "getDebugMuxerDetail";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMuxerDetail";
         }

         var2 = new PropertyDescriptor("DebugMuxerDetail", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMuxerDetail", var2);
         var2.setValue("description", "<p>Detailed debug for Muxer processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("DebugMuxerException")) {
         var3 = "getDebugMuxerException";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMuxerException";
         }

         var2 = new PropertyDescriptor("DebugMuxerException", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMuxerException", var2);
         var2.setValue("description", "<p>Debug Muxer exception processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("DebugMuxerTimeout")) {
         var3 = "getDebugMuxerTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMuxerTimeout";
         }

         var2 = new PropertyDescriptor("DebugMuxerTimeout", KernelDebugMBean.class, var3, var4);
         var1.put("DebugMuxerTimeout", var2);
         var2.setValue("description", "<p>Debug Muxer timeout processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("DebugRC4")) {
         var3 = "getDebugRC4";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRC4";
         }

         var2 = new PropertyDescriptor("DebugRC4", KernelDebugMBean.class, var3, var4);
         var1.put("DebugRC4", var2);
         var2.setValue("description", "<p>Debug RC4 cipher processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugRSA")) {
         var3 = "getDebugRSA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRSA";
         }

         var2 = new PropertyDescriptor("DebugRSA", KernelDebugMBean.class, var3, var4);
         var1.put("DebugRSA", var2);
         var2.setValue("description", "<p>Debug RSA security processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugRouting")) {
         var3 = "getDebugRouting";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRouting";
         }

         var2 = new PropertyDescriptor("DebugRouting", KernelDebugMBean.class, var3, var4);
         var1.put("DebugRouting", var2);
         var2.setValue("description", "<p>Debug routing of messages over JVM to JVM connections</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugSSL")) {
         var3 = "getDebugSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSSL";
         }

         var2 = new PropertyDescriptor("DebugSSL", KernelDebugMBean.class, var3, var4);
         var1.put("DebugSSL", var2);
         var2.setValue("description", "<p>Debug SSL processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugSelfTuning")) {
         var3 = "getDebugSelfTuning";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSelfTuning";
         }

         var2 = new PropertyDescriptor("DebugSelfTuning", KernelDebugMBean.class, var3, var4);
         var1.put("DebugSelfTuning", var2);
         var2.setValue("description", "<p>Debug WorkManager self-tuning processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWorkContext")) {
         var3 = "getDebugWorkContext";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWorkContext";
         }

         var2 = new PropertyDescriptor("DebugWorkContext", KernelDebugMBean.class, var3, var4);
         var1.put("DebugWorkContext", var2);
         var2.setValue("description", "<p>Debug Work context (out of band data propagation)</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("ForceGCEachDGCPeriod")) {
         var3 = "getForceGCEachDGCPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setForceGCEachDGCPeriod";
         }

         var2 = new PropertyDescriptor("ForceGCEachDGCPeriod", KernelDebugMBean.class, var3, var4);
         var1.put("ForceGCEachDGCPeriod", var2);
         var2.setValue("description", "<p>Force VM garbage collection on each DGC interval</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("LogDGCStatistics")) {
         var3 = "getLogDGCStatistics";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogDGCStatistics";
         }

         var2 = new PropertyDescriptor("LogDGCStatistics", KernelDebugMBean.class, var3, var4);
         var1.put("LogDGCStatistics", var2);
         var2.setValue("description", "<p>Debug DGC with Statistics</p> ");
         var2.setValue("secureValue", new Boolean(false));
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
