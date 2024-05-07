package weblogic.jms.frontend;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSConnectionRuntimeMBean;

public class FEConnectionRuntimeDelegateBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSConnectionRuntimeMBean.class;

   public FEConnectionRuntimeDelegateBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public FEConnectionRuntimeDelegateBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = FEConnectionRuntimeDelegate.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms.frontend");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS connection.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSConnectionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ClientID")) {
         var3 = "getClientID";
         var4 = null;
         var2 = new PropertyDescriptor("ClientID", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientID", var2);
         var2.setValue("description", "<p>The client ID for this connection.</p> ");
      }

      if (!var1.containsKey("ClientIDPolicy")) {
         var3 = "getClientIDPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("ClientIDPolicy", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientIDPolicy", var2);
         var2.setValue("description", "<p>The ClientIDPolicy on this connection or durable subscriber.</p> <p>Values are: <ul><li><code>weblogic.management.configuration.JMSConstants.CLIENT_ID_POLICY_RESTRICTED</code>: Only one connection that uses this policy exists in a cluster at any given time for a particular <code>ClientID</code>.</li> <li><code>weblogic.management.configuration.JMSConstants.CLIENT_ID_POLICY_UNRESTRICTED</code>: Connections created using this policy can specify any <code>ClientID</code>, even when other restricted or unrestricted connections already use the same <code>ClientID</code>.</li></ul></p> ");
      }

      if (!var1.containsKey("HostAddress")) {
         var3 = "getHostAddress";
         var4 = null;
         var2 = new PropertyDescriptor("HostAddress", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HostAddress", var2);
         var2.setValue("description", "<p>The host address of the client JVM as a string.</p> ");
      }

      if (!var1.containsKey("Sessions")) {
         var3 = "getSessions";
         var4 = null;
         var2 = new PropertyDescriptor("Sessions", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("Sessions", var2);
         var2.setValue("description", "<p>An array of sessions for this connection.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SessionsCurrentCount")) {
         var3 = "getSessionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionsCurrentCount", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of sessions for this connection.</p> ");
      }

      if (!var1.containsKey("SessionsHighCount")) {
         var3 = "getSessionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionsHighCount", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionsHighCount", var2);
         var2.setValue("description", "<p>The peak number of sessions for this connection since the last reset.</p> ");
      }

      if (!var1.containsKey("SessionsTotalCount")) {
         var3 = "getSessionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionsTotalCount", JMSConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionsTotalCount", var2);
         var2.setValue("description", "<p>The number of sessions on this connection since the last reset.</p> ");
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
      Method var3 = JMSConnectionRuntimeMBean.class.getMethod("destroy");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Destroys server side context for the connection.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSConnectionRuntimeMBean.class.getMethod("preDeregister");
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
