package weblogic.connector.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ConnectorServiceRuntimeMBean;

public class ServiceRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ConnectorServiceRuntimeMBean.class;

   public ServiceRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServiceRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServiceRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "6.1.0.0");
      var2.setValue("package", "weblogic.connector.monitoring");
      String var3 = (new String("This interface defines the runtime information that can be accessed at a connector service level. Runtime information can be accessed at a per resource adapter level or at an overall level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ConnectorServiceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveRACount")) {
         var3 = "getActiveRACount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveRACount", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveRACount", var2);
         var2.setValue("description", "<p>Returns the number of resource adapters that are active.</p> ");
      }

      if (!var1.containsKey("ActiveRAs")) {
         var3 = "getActiveRAs";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveRAs", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveRAs", var2);
         var2.setValue("description", "<p>Returns an array of runtime information for all deployed and active resource adapters.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConnectionPoolCurrentCount")) {
         var3 = "getConnectionPoolCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionPoolCurrentCount", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionPoolCurrentCount", var2);
         var2.setValue("description", "<p>Returns the number of connection pools in all active RAs.<p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionPools")) {
         var3 = "getConnectionPools";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionPools", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionPools", var2);
         var2.setValue("description", "Returns connection pool runtimes for all active resource adapters. ");
         var2.setValue("relationship", "containment");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionPoolsTotalCount")) {
         var3 = "getConnectionPoolsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionPoolsTotalCount", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionPoolsTotalCount", var2);
         var2.setValue("description", "<p>Returns the number of resource adapter connection pools that have been created since server startup. This includes re-deployments.</p>  Returns the total number of deployed connection pools instantiated since server startup. ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("InactiveRAs")) {
         var3 = "getInactiveRAs";
         var4 = null;
         var2 = new PropertyDescriptor("InactiveRAs", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("InactiveRAs", var2);
         var2.setValue("description", "<p>Returns an array of runtime information for all deployed and inactive resource adapters.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("RACount")) {
         var3 = "getRACount";
         var4 = null;
         var2 = new PropertyDescriptor("RACount", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("RACount", var2);
         var2.setValue("description", "<p>Returns the number of resource adapters that have been deployed in the server. This count includes active RAs and Non-active RAs ( in the case of versioned RAs that are being replaced by a new version )</p> ");
      }

      if (!var1.containsKey("RAs")) {
         var3 = "getRAs";
         var4 = null;
         var2 = new PropertyDescriptor("RAs", ConnectorServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("RAs", var2);
         var2.setValue("description", "<p>Returns an array of runtime information for all deployed resource adapters.</p> ");
         var2.setValue("relationship", "containment");
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
      Method var3 = ConnectorServiceRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("getRA", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("jndiName", "The JNDI name of the resource adapter. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the runtime information of the resource adapter specified by the given JNDI name. A null is returned if the resource adapter cannot be found. This function returns the active RA if multiple versions of the resource adapters has been deployed</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("getConnectionPool", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("the", "key by which to find the connection pool. this could be the jndi name or the resource-link. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", " ");
         var1.put(var5, var2);
         var2.setValue("description", "Returns a ConnectorConnectionPoolRuntimeMBean that represents the statistics for a Deployed Connection Pool (a deployed connection pool is equivalent to a deployed RAR). ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("getInboundConnections", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("messageListenerType", "Message listener type. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns runtime information for the specified inbound connection. A null is returned if the inbound connection is not found.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("suspendAll", Properties.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("props", "Properties to be passed to the resource adapters or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Suspends all activities of all resource adapters.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("suspend", Integer.TYPE, Properties.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable "), createParameterDescriptor("props", "Properties to pass on to the RA or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Suspends the specified type of activity for all RAs</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("resumeAll", Properties.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("props", "Properties to be passed to the resource adapters or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resumes all activities of all resource adapters.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("resume", Integer.TYPE, Properties.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable "), createParameterDescriptor("props", "Properties to pass on to the RA or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resumes the specified type of activity for all RAs</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("suspendAll");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Suspends all activities of all resource adapters.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("suspend", Integer.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Suspends the specified type of activity for all RAs.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("resumeAll");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes all activities of all resource adapters.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorServiceRuntimeMBean.class.getMethod("resume", Integer.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resumes the specified type of activity for all RAs.</p> ");
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
