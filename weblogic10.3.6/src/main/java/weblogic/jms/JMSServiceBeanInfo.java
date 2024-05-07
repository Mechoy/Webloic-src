package weblogic.jms;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSRuntimeMBean;

public class JMSServiceBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSRuntimeMBean.class;

   public JMSServiceBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSServiceBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSService.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS service.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Connections")) {
         var3 = "getConnections";
         var4 = null;
         var2 = new PropertyDescriptor("Connections", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("Connections", var2);
         var2.setValue("description", "<p>The connections to this WebLogic server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConnectionsCurrentCount")) {
         var3 = "getConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsCurrentCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of connections to WebLogic Server server.</p> ");
      }

      if (!var1.containsKey("ConnectionsHighCount")) {
         var3 = "getConnectionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsHighCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsHighCount", var2);
         var2.setValue("description", "<p>The highest number of connections to this WebLogic Server since the last reset.</p> ");
      }

      if (!var1.containsKey("ConnectionsTotalCount")) {
         var3 = "getConnectionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsTotalCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsTotalCount", var2);
         var2.setValue("description", "<p>The total number of connections made to this WebLogic Server since the last reset.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of this JMS service.</p> ");
      }

      if (!var1.containsKey("JMSPooledConnections")) {
         var3 = "getJMSPooledConnections";
         var4 = null;
         var2 = new PropertyDescriptor("JMSPooledConnections", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSPooledConnections", var2);
         var2.setValue("description", "<p>The JMS pooled connections to this WebLogic server.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("JMSServers")) {
         var3 = "getJMSServers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSServers", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSServers", var2);
         var2.setValue("description", "<p>The JMS servers that are currently deployed on this WebLogic Server instance.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JMSServersCurrentCount")) {
         var3 = "getJMSServersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("JMSServersCurrentCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSServersCurrentCount", var2);
         var2.setValue("description", "<p>The current number of JMS servers that are deployed on this WebLogic Server instance.</p> ");
      }

      if (!var1.containsKey("JMSServersHighCount")) {
         var3 = "getJMSServersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("JMSServersHighCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSServersHighCount", var2);
         var2.setValue("description", "<p>The highest number of JMS servers that were deployed on this WebLogic Server instance since this server was started.</p> ");
      }

      if (!var1.containsKey("JMSServersTotalCount")) {
         var3 = "getJMSServersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("JMSServersTotalCount", JMSRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSServersTotalCount", var2);
         var2.setValue("description", "<p>The total number of JMS servers that were deployed on this WebLogic Server instance since this server was started.</p> ");
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
      Method var3 = JMSRuntimeMBean.class.getMethod("preDeregister");
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
