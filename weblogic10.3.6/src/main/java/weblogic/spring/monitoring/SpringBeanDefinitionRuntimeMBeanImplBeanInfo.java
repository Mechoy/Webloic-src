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
import weblogic.management.runtime.SpringBeanDefinitionRuntimeMBean;

public class SpringBeanDefinitionRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SpringBeanDefinitionRuntimeMBean.class;

   public SpringBeanDefinitionRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SpringBeanDefinitionRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SpringBeanDefinitionRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.0.0");
      var2.setValue("package", "weblogic.spring.monitoring");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SpringBeanDefinitionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Aliases")) {
         var3 = "getAliases";
         var4 = null;
         var2 = new PropertyDescriptor("Aliases", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Aliases", var2);
         var2.setValue("description", "<p>Get the aliases for this bean definition. Aliases are other names this bean definition is known by.</p> ");
      }

      if (!var1.containsKey("ApplicationContextDisplayName")) {
         var3 = "getApplicationContextDisplayName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationContextDisplayName", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationContextDisplayName", var2);
         var2.setValue("description", "<p>Display name of the application context that this Spring bean is defined in. The application context is the Spring Inversion of Control (IoC) container.</p> ");
      }

      if (!var1.containsKey("BeanClassname")) {
         var3 = "getBeanClassname";
         var4 = null;
         var2 = new PropertyDescriptor("BeanClassname", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("BeanClassname", var2);
         var2.setValue("description", "<p>Class name of this Spring bean, as defined in the application context of the Spring application.</p> ");
      }

      if (!var1.containsKey("BeanId")) {
         var3 = "getBeanId";
         var4 = null;
         var2 = new PropertyDescriptor("BeanId", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("BeanId", var2);
         var2.setValue("description", "<p>Name of the Spring bean.</p> ");
      }

      if (!var1.containsKey("Dependencies")) {
         var3 = "getDependencies";
         var4 = null;
         var2 = new PropertyDescriptor("Dependencies", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Dependencies", var2);
         var2.setValue("description", "<p>Get the names (ids) of other bean definitions that this bean definition depends on.</p> ");
      }

      if (!var1.containsKey("DependencyValues")) {
         var3 = "getDependencyValues";
         var4 = null;
         var2 = new PropertyDescriptor("DependencyValues", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("DependencyValues", var2);
         var2.setValue("description", "<p>Dependency values to be injected into this bean.</p> ");
      }

      if (!var1.containsKey("ParentId")) {
         var3 = "getParentId";
         var4 = null;
         var2 = new PropertyDescriptor("ParentId", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("ParentId", var2);
         var2.setValue("description", "<p>Name (Id) of parent bean definition.</p> ");
      }

      if (!var1.containsKey("ResourceDescription")) {
         var3 = "getResourceDescription";
         var4 = null;
         var2 = new PropertyDescriptor("ResourceDescription", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("ResourceDescription", var2);
         var2.setValue("description", "<p>The name of the resource that this bean definition comes from. May be empty if the bean is implicitly registered.</p> ");
      }

      if (!var1.containsKey("Role")) {
         var3 = "getRole";
         var4 = null;
         var2 = new PropertyDescriptor("Role", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Role", var2);
         var2.setValue("description", "<p>Role hint of this bean definition. The role is one of ROLE_APPLICATION, ROLE_SUPPORT, or ROLE_INFRASTRUCTURE.</p> ");
      }

      if (!var1.containsKey("Scope")) {
         var3 = "getScope";
         var4 = null;
         var2 = new PropertyDescriptor("Scope", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Scope", var2);
         var2.setValue("description", "<p>Scope of this bean. The scope is \"singleton\", \"prototype\", or other web specific or user defined values.</p> ");
      }

      if (!var1.containsKey("Abstract")) {
         var3 = "isAbstract";
         var4 = null;
         var2 = new PropertyDescriptor("Abstract", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Abstract", var2);
         var2.setValue("description", "<p>Whether this Spring bean is \"abstract\". An abstract bean definition can be used as a base for other definitions but cannot be instantiated.</p> ");
      }

      if (!var1.containsKey("AutowireCandidate")) {
         var3 = "isAutowireCandidate";
         var4 = null;
         var2 = new PropertyDescriptor("AutowireCandidate", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("AutowireCandidate", var2);
         var2.setValue("description", "<p>Whether this bean is a candidate to be autowired to other beans.</p> ");
      }

      if (!var1.containsKey("LazyInit")) {
         var3 = "isLazyInit";
         var4 = null;
         var2 = new PropertyDescriptor("LazyInit", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("LazyInit", var2);
         var2.setValue("description", "<p>Whether this bean should be lazily initialized. A lazy initialized bean is not created until it is needed.</p> ");
      }

      if (!var1.containsKey("Singleton")) {
         var3 = "isSingleton";
         var4 = null;
         var2 = new PropertyDescriptor("Singleton", SpringBeanDefinitionRuntimeMBean.class, var3, (String)var4);
         var1.put("Singleton", var2);
         var2.setValue("description", "<p>Whether this is a singleton Spring bean. There is just one instance of a singleton bean per bean definition per application context.</p> ");
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
      Method var3 = SpringBeanDefinitionRuntimeMBean.class.getMethod("preDeregister");
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
