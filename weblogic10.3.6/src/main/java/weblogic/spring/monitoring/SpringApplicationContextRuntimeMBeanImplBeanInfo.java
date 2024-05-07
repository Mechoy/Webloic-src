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
import weblogic.management.runtime.SpringApplicationContextRuntimeMBean;

public class SpringApplicationContextRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SpringApplicationContextRuntimeMBean.class;

   public SpringApplicationContextRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SpringApplicationContextRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SpringApplicationContextRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.1.0");
      var2.setValue("package", "weblogic.spring.monitoring");
      String var3 = (new String("This MBean represents instances of class org.springframework.context.support.AbstractApplicationContext and org.springframework.beans.factory.support.AbstractBeanFactory There is a SpringApplicationContextRuntimeMBean for each application context in a deployment. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SpringApplicationContextRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageGetBeanNamesForTypeTime")) {
         var3 = "getAverageGetBeanNamesForTypeTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageGetBeanNamesForTypeTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageGetBeanNamesForTypeTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required for getBeanNamesForType()</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageGetBeanTime")) {
         var3 = "getAverageGetBeanTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageGetBeanTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageGetBeanTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required for getBean()</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageGetBeansOfTypeTime")) {
         var3 = "getAverageGetBeansOfTypeTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageGetBeansOfTypeTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageGetBeansOfTypeTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required for getBeansOfType()</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AveragePrototypeBeanCreationTime")) {
         var3 = "getAveragePrototypeBeanCreationTime";
         var4 = null;
         var2 = new PropertyDescriptor("AveragePrototypeBeanCreationTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AveragePrototypeBeanCreationTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required to create prototype beans</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageRefreshTime")) {
         var3 = "getAverageRefreshTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageRefreshTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageRefreshTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required to perform a refresh</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("AverageSingletonBeanCreationTime")) {
         var3 = "getAverageSingletonBeanCreationTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageSingletonBeanCreationTime", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageSingletonBeanCreationTime", var2);
         var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required to create singleton beans</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("CustomScopeNames")) {
         var3 = "getCustomScopeNames";
         var4 = null;
         var2 = new PropertyDescriptor("CustomScopeNames", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("CustomScopeNames", var2);
         var2.setValue("description", "<p>The names of customer scopes that were registered.</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("DisplayName")) {
         var3 = "getDisplayName";
         var4 = null;
         var2 = new PropertyDescriptor("DisplayName", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("DisplayName", var2);
         var2.setValue("description", "<p>Display name of the application context</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("GetBeanCount")) {
         var3 = "getGetBeanCount";
         var4 = null;
         var2 = new PropertyDescriptor("GetBeanCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("GetBeanCount", var2);
         var2.setValue("description", "<p>The number of times getBean() was called</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("GetBeanNamesForTypeCount")) {
         var3 = "getGetBeanNamesForTypeCount";
         var4 = null;
         var2 = new PropertyDescriptor("GetBeanNamesForTypeCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("GetBeanNamesForTypeCount", var2);
         var2.setValue("description", "<p>The number of times getBeanNamesForType() was called</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("GetBeansOfTypeCount")) {
         var3 = "getGetBeansOfTypeCount";
         var4 = null;
         var2 = new PropertyDescriptor("GetBeansOfTypeCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("GetBeansOfTypeCount", var2);
         var2.setValue("description", "<p>The number of times getBeansOfType() was called</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("ParentContext")) {
         var3 = "getParentContext";
         var4 = null;
         var2 = new PropertyDescriptor("ParentContext", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("ParentContext", var2);
         var2.setValue("description", "<p>The name of the parent context</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("PrototypeBeansCreatedCount")) {
         var3 = "getPrototypeBeansCreatedCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrototypeBeansCreatedCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("PrototypeBeansCreatedCount", var2);
         var2.setValue("description", "<p>The number of Prototype beans created.</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("RefreshCount")) {
         var3 = "getRefreshCount";
         var4 = null;
         var2 = new PropertyDescriptor("RefreshCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("RefreshCount", var2);
         var2.setValue("description", "<p>The number of refreshes performed</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("SingletonBeansCreatedCount")) {
         var3 = "getSingletonBeansCreatedCount";
         var4 = null;
         var2 = new PropertyDescriptor("SingletonBeansCreatedCount", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("SingletonBeansCreatedCount", var2);
         var2.setValue("description", "<p>The number of singleton beans created.</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("StartupDate")) {
         var3 = "getStartupDate";
         var4 = null;
         var2 = new PropertyDescriptor("StartupDate", SpringApplicationContextRuntimeMBean.class, var3, (String)var4);
         var1.put("StartupDate", var2);
         var2.setValue("description", "<p>Return the timestamp in milliseconds when this context was first loaded</p> ");
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
      Method var3 = SpringApplicationContextRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      String var5;
      ParameterDescriptor[] var6;
      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = SpringApplicationContextRuntimeMBean.class.getMethod("getAverageCustomScopeBeanCreationTime", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("scopeName", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "10.3.1.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This returns the average elapsed time in milliseconds required to create custom scope beans</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.1.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = SpringApplicationContextRuntimeMBean.class.getMethod("getCustomScopeBeansCreatedCount", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("scopeName", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "10.3.1.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>The number of custom scope beans created.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.1.0");
         }
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
