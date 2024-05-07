package weblogic.messaging.saf.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SAFConversationRuntimeMBean;

public class ConversationRuntimeDelegateBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SAFConversationRuntimeMBean.class;

   public ConversationRuntimeDelegateBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConversationRuntimeDelegateBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConversationRuntimeDelegate.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.messaging.saf.internal");
      String var3 = (new String("This class is used for monitoring a WebLogic SAF conversation.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SAFConversationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ConversationName")) {
         var3 = "getConversationName";
         var4 = null;
         var2 = new PropertyDescriptor("ConversationName", SAFConversationRuntimeMBean.class, var3, (String)var4);
         var1.put("ConversationName", var2);
         var2.setValue("description", "<p>The name of the conversation.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("DestinationURL")) {
         var3 = "getDestinationURL";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationURL", SAFConversationRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationURL", var2);
         var2.setValue("description", "<p>The URL of the destination.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("QOS")) {
         var3 = "getQOS";
         var4 = null;
         var2 = new PropertyDescriptor("QOS", SAFConversationRuntimeMBean.class, var3, (String)var4);
         var1.put("QOS", var2);
         var2.setValue("description", "<p>The quality of service (QOS) for the SAF conversation.</p>  <p>The quality-of-service values are:</p>  <ul> <li> <tt>weblogic.management.runtime.SAFConstants.QOS_EXACTLY_ONCE</tt></li>  <li> <tt>weblogic.management.runtime.SAFConstants.QOS_ATLEAST_ONCE</tt></li>  <li> <tt>weblogic.management.runtime.SAFConstants.QOS_ATMOST_ONCE</tt></li> </ul> ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
      Method var3 = SAFConversationRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = SAFConversationRuntimeMBean.class.getMethod("destroy");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Destroys the conversation and purges all the messages.</p> ");
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
