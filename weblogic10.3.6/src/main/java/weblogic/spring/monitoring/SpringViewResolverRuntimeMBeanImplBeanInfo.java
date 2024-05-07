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
import weblogic.management.runtime.SpringViewResolverRuntimeMBean;

public class SpringViewResolverRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SpringViewResolverRuntimeMBean.class;

   public SpringViewResolverRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SpringViewResolverRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SpringViewResolverRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.1.0");
      var2.setValue("package", "weblogic.spring.monitoring");
      String var3 = (new String("This MBean represents statistics for org.springframework.web.servlet.ViewResolver ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SpringViewResolverRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ApplicationContextDisplayName")) {
         var3 = "getApplicationContextDisplayName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationContextDisplayName", SpringViewResolverRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationContextDisplayName", var2);
         var2.setValue("description", "<p>The display name of the Application Context that this bean is from</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageResolveViewNameTime")) {
         var3 = "getAverageResolveViewNameTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageResolveViewNameTime", SpringViewResolverRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageResolveViewNameTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required to resolve a view name</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("BeanId")) {
         var3 = "getBeanId";
         var4 = null;
         var2 = new PropertyDescriptor("BeanId", SpringViewResolverRuntimeMBean.class, var3, (String)var4);
         var1.put("BeanId", var2);
         var2.setValue("description", "<p>Name of the Spring bean.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("ResolveViewNameCount")) {
         var3 = "getResolveViewNameCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResolveViewNameCount", SpringViewResolverRuntimeMBean.class, var3, (String)var4);
         var1.put("ResolveViewNameCount", var2);
         var2.setValue("description", "<p>The number of times resolveViewName was called</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("ResolveViewNameFailedCount")) {
         var3 = "getResolveViewNameFailedCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResolveViewNameFailedCount", SpringViewResolverRuntimeMBean.class, var3, (String)var4);
         var1.put("ResolveViewNameFailedCount", var2);
         var2.setValue("description", "<p>The number of resolveViewName calls that failed</p> ");
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
      Method var3 = SpringViewResolverRuntimeMBean.class.getMethod("preDeregister");
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
