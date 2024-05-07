package weblogic.spring.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SpringRuntimeMBean;

public class SpringRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SpringRuntimeMBean.class;

   public SpringRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SpringRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SpringRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.0.0");
      var2.setValue("package", "weblogic.spring.monitoring");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SpringRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SpringApplicationContextRuntimeMBeans")) {
         var3 = "getSpringApplicationContextRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringApplicationContextRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringApplicationContextRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring ApplicationContext runtime bean definitions.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("SpringBeanDefinitionRuntimeMBeans")) {
         var3 = "getSpringBeanDefinitionRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringBeanDefinitionRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringBeanDefinitionRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring bean definitions.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SpringTransactionManagerRuntimeMBeans")) {
         var3 = "getSpringTransactionManagerRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringTransactionManagerRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringTransactionManagerRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring TransactionManager runtime bean definitions.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SpringTransactionTemplateRuntimeMBeans")) {
         var3 = "getSpringTransactionTemplateRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringTransactionTemplateRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringTransactionTemplateRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring TransactionTemplate runtime bean definitions.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("SpringVersion")) {
         var3 = "getSpringVersion";
         var4 = null;
         var2 = new PropertyDescriptor("SpringVersion", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringVersion", var2);
         var2.setValue("description", "<p>Get the version number of the Spring Framework.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SpringViewResolverRuntimeMBeans")) {
         var3 = "getSpringViewResolverRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringViewResolverRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringViewResolverRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring ViewResolver runtime bean definitions.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SpringViewRuntimeMBeans")) {
         var3 = "getSpringViewRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("SpringViewRuntimeMBeans", SpringRuntimeMBean.class, var3, (String)var4);
         var1.put("SpringViewRuntimeMBeans", var2);
         var2.setValue("description", "<p>The Spring View runtime bean definitions.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.1.0");
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
      Method var3 = SpringRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

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
