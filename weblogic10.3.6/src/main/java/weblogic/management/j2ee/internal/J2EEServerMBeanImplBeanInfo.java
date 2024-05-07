package weblogic.management.j2ee.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.j2ee.J2EEServerMBean;

public class J2EEServerMBeanImplBeanInfo extends J2EEManagedObjectMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = J2EEServerMBean.class;

   public J2EEServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public J2EEServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = J2EEServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.j2ee.internal");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.j2ee.J2EEServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("deployedObjects")) {
         var3 = "getdeployedObjects";
         var4 = null;
         var2 = new PropertyDescriptor("deployedObjects", J2EEServerMBean.class, var3, (String)var4);
         var1.put("deployedObjects", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("javaVMs")) {
         var3 = "getjavaVMs";
         var4 = null;
         var2 = new PropertyDescriptor("javaVMs", J2EEServerMBean.class, var3, (String)var4);
         var1.put("javaVMs", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("resources")) {
         var3 = "getresources";
         var4 = null;
         var2 = new PropertyDescriptor("resources", J2EEServerMBean.class, var3, (String)var4);
         var1.put("resources", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("serverVendor")) {
         var3 = "getserverVendor";
         var4 = null;
         var2 = new PropertyDescriptor("serverVendor", J2EEServerMBean.class, var3, (String)var4);
         var1.put("serverVendor", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("serverVersion")) {
         var3 = "getserverVersion";
         var4 = null;
         var2 = new PropertyDescriptor("serverVersion", J2EEServerMBean.class, var3, (String)var4);
         var1.put("serverVersion", var2);
         var2.setValue("description", " ");
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
