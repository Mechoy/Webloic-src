package weblogic.management.j2ee.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.util.Map;
import weblogic.management.j2ee.StatefulSessionBeanMBean;

public class StatefulSessionBeanMBeanImplBeanInfo extends SessionBeanMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = StatefulSessionBeanMBean.class;

   public StatefulSessionBeanMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public StatefulSessionBeanMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = StatefulSessionBeanMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.j2ee.internal");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.j2ee.StatefulSessionBeanMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
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
