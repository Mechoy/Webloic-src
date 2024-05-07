package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SCAContainerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SCAContainerMBean.class;

   public SCAContainerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SCAContainerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SCAContainerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SCAContainerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MaxAge")) {
         var3 = "getMaxAge";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxAge";
         }

         var2 = new PropertyDescriptor("MaxAge", SCAContainerMBean.class, var3, var4);
         var1.put("MaxAge", var2);
         var2.setValue("description", "Get max of a stateful service ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxIdleTime")) {
         var3 = "getMaxIdleTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxIdleTime";
         }

         var2 = new PropertyDescriptor("MaxIdleTime", SCAContainerMBean.class, var3, var4);
         var1.put("MaxIdleTime", var2);
         var2.setValue("description", "Get max idle time of the a stateful service ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Timeout")) {
         var3 = "getTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeout";
         }

         var2 = new PropertyDescriptor("Timeout", SCAContainerMBean.class, var3, var4);
         var1.put("Timeout", var2);
         var2.setValue("description", " ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AllowsPassByReference")) {
         var3 = "isAllowsPassByReference";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowsPassByReference";
         }

         var2 = new PropertyDescriptor("AllowsPassByReference", SCAContainerMBean.class, var3, var4);
         var1.put("AllowsPassByReference", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Autowire")) {
         var3 = "isAutowire";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutowire";
         }

         var2 = new PropertyDescriptor("Autowire", SCAContainerMBean.class, var3, var4);
         var1.put("Autowire", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Remotable")) {
         var3 = "isRemotable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemotable";
         }

         var2 = new PropertyDescriptor("Remotable", SCAContainerMBean.class, var3, var4);
         var1.put("Remotable", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SinglePrincipal")) {
         var3 = "isSinglePrincipal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSinglePrincipal";
         }

         var2 = new PropertyDescriptor("SinglePrincipal", SCAContainerMBean.class, var3, var4);
         var1.put("SinglePrincipal", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
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
