package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WLDFDataRetirementMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFDataRetirementMBean.class;

   public WLDFDataRetirementMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFDataRetirementMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFDataRetirementMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean specifies how data retirement for a WLDF archive will be performed. This base interface is extended by the interfaces which define specific retirement policies, eg. WLDFDataRetirementByAgeMBean </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WLDFDataRetirementMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ArchiveName")) {
         var3 = "getArchiveName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setArchiveName";
         }

         var2 = new PropertyDescriptor("ArchiveName", WLDFDataRetirementMBean.class, var3, var4);
         var1.put("ArchiveName", var2);
         var2.setValue("description", "<p>Name of the archive for which data retirement is configured</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetirementPeriod")) {
         var3 = "getRetirementPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetirementPeriod";
         }

         var2 = new PropertyDescriptor("RetirementPeriod", WLDFDataRetirementMBean.class, var3, var4);
         var1.put("RetirementPeriod", var2);
         var2.setValue("description", "<p>This attribute specifies the period in hours at which the data retirement task will be periodically performed for the archive during the day after it is first executed. The value of this attribute must be positive </p> ");
         setPropertyDescriptorDefault(var2, new Integer(24));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetirementTime")) {
         var3 = "getRetirementTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetirementTime";
         }

         var2 = new PropertyDescriptor("RetirementTime", WLDFDataRetirementMBean.class, var3, var4);
         var1.put("RetirementTime", var2);
         var2.setValue("description", "<p>This attribute specifies the hour of day at which the data retirement task will first run during the day.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFDataRetirementMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Enable data retirement</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
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
