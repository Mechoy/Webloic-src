package weblogic.servlet.internal.session;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ServletSessionRuntimeMBean;

public class ServletSessionRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServletSessionRuntimeMBean.class;

   public ServletSessionRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServletSessionRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServletSessionRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "As of WebLogic 9.0, use WebAppComponentRuntimeMBean instead. ");
      var2.setValue("package", "weblogic.servlet.internal.session");
      String var3 = (new String("This class is used for monitoring a WebLogic servlet session within a WebLogic server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServletSessionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("MainAttribute")) {
         var3 = "getMainAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("MainAttribute", ServletSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("MainAttribute", var2);
         var2.setValue("description", "<p>Provides a copy (as a string) of an attribute specified by the user, such as user-name, associated with this session.</p>  <p>Gets a Stringified copy of an attribute specified by the user (like user-name) associated with this session. Where should this attribute be specified? In ServletDeploymentRuntimeMBean?</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("MaxInactiveInterval")) {
         var3 = "getMaxInactiveInterval";
         var4 = null;
         var2 = new PropertyDescriptor("MaxInactiveInterval", ServletSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxInactiveInterval", var2);
         var2.setValue("description", "<p> Returns the timeout (seconds) for the session </p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("TimeLastAccessed")) {
         var3 = "getTimeLastAccessed";
         var4 = null;
         var2 = new PropertyDescriptor("TimeLastAccessed", ServletSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("TimeLastAccessed", var2);
         var2.setValue("description", "<p>Provides a record of the last time this session was accessed.</p> ");
         var2.setValue("deprecated", " ");
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
      Method var3 = ServletSessionRuntimeMBean.class.getMethod("invalidate");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", " ");
         var1.put(var4, var2);
         var2.setValue("description", "<p>Invalidates this session.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServletSessionRuntimeMBean.class.getMethod("preDeregister");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
