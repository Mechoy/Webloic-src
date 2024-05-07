package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ServerFailureTriggerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerFailureTriggerMBean.class;

   public ServerFailureTriggerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerFailureTriggerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerFailureTriggerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configuration to mark the server as failed when threads are stuck. A failed server in turn can be configured to shutdown or go into admin state. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ServerFailureTriggerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MaxStuckThreadTime")) {
         var3 = "getMaxStuckThreadTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxStuckThreadTime";
         }

         var2 = new PropertyDescriptor("MaxStuckThreadTime", ServerFailureTriggerMBean.class, var3, var4);
         var1.put("MaxStuckThreadTime", var2);
         var2.setValue("description", "<p>The number of seconds that a thread must be continually working before this server diagnoses the thread as being stuck.</p>  <p>For example, if you set this to 600 seconds, WebLogic Server considers a thread to be \"stuck\" after 600 seconds of continuous use.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(600));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("StuckThreadCount")) {
         var3 = "getStuckThreadCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStuckThreadCount";
         }

         var2 = new PropertyDescriptor("StuckThreadCount", ServerFailureTriggerMBean.class, var3, var4);
         var1.put("StuckThreadCount", var2);
         var2.setValue("description", "<p>The number of stuck threads after which the server is transitioned into FAILED state. There are options in OverloadProtectionMBean to suspend and shutdown a FAILED server. By default, the server continues to run in FAILED state.</p>  <p>If the StuckThreadCount value is set to zero then the server never transitions into FAILED server irrespective of the number of stuck threads. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
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
