package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WorkManagerShutdownTriggerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WorkManagerShutdownTriggerMBean.class;

   public WorkManagerShutdownTriggerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WorkManagerShutdownTriggerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WorkManagerShutdownTriggerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean is used to configure the trigger that shuts down the WorkManager. The trigger specifies the number of threads that need to be stuck for a certain amount of time for the trigger to shutdown the WorkManager automatically. A shutdown WorkManager refuses new work and completes pending work. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WorkManagerShutdownTriggerMBean");
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

         var2 = new PropertyDescriptor("MaxStuckThreadTime", WorkManagerShutdownTriggerMBean.class, var3, var4);
         var1.put("MaxStuckThreadTime", var2);
         var2.setValue("description", "Time after which a executing thread is declared as stuck. ");
      }

      if (!var1.containsKey("StuckThreadCount")) {
         var3 = "getStuckThreadCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStuckThreadCount";
         }

         var2 = new PropertyDescriptor("StuckThreadCount", WorkManagerShutdownTriggerMBean.class, var3, var4);
         var1.put("StuckThreadCount", var2);
         var2.setValue("description", "Number of stuck threads after which the WorkManager is shutdown ");
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
