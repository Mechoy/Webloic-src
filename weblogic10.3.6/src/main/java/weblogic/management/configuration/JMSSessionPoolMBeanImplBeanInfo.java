package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSSessionPoolMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSSessionPoolMBean.class;

   public JMSSessionPoolMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSSessionPoolMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSSessionPoolMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 Replaced with message-dirven beans.  This functionality will be removed in a future release. ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS session pool, a server-managed pool of server sessions that enables an application to process messages concurrently.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSSessionPoolMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AcknowledgeMode")) {
         var3 = "getAcknowledgeMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcknowledgeMode";
         }

         var2 = new PropertyDescriptor("AcknowledgeMode", JMSSessionPoolMBean.class, var3, var4);
         var1.put("AcknowledgeMode", var2);
         var2.setValue("description", "<p>The acknowledge mode used by non-transacted sessions within this JMS session pool.</p>  <p>For transacted sessions, messages are acknowledged automatically when the session is committed and this value is ignored.</p> ");
         setPropertyDescriptorDefault(var2, "Auto");
         var2.setValue("legalValues", new Object[]{"Auto", "Client", "Dups-Ok", "None"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionConsumers")) {
         var3 = "getConnectionConsumers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionConsumers";
         }

         var2 = new PropertyDescriptor("ConnectionConsumers", JMSSessionPoolMBean.class, var3, var4);
         var1.put("ConnectionConsumers", var2);
         var2.setValue("description", "<p>The connection consumers for this JMS session pool. JMS connection consumers are queues (Point-To-Point) or topics (Pub/Sub) that retrieve server sessions and process messages. Once you have defined a session pool, you can configure one or more connection consumers for each session pool. This method is provided for backward compatibility.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addConnectionConsumer");
         var2.setValue("remover", "removeConnectionConsumer");
         var2.setValue("deprecated", "9.0.0.0 Replaced with message-driven beans. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionFactory")) {
         var3 = "getConnectionFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFactory";
         }

         var2 = new PropertyDescriptor("ConnectionFactory", JMSSessionPoolMBean.class, var3, var4);
         var1.put("ConnectionFactory", var2);
         var2.setValue("description", "<p>The JNDI name of the connection factory for this JMS session pool. Connection factories are used to create connections with predefined attributes.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JMSConnectionConsumers")) {
         var3 = "getJMSConnectionConsumers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSConnectionConsumers", JMSSessionPoolMBean.class, var3, var4);
         var1.put("JMSConnectionConsumers", var2);
         var2.setValue("description", "<p>The connection consumers for this JMS session pool. JMS connection consumers are queues (Point-To-Point) or topics (Pub/Sub) that retrieve server sessions and process messages. Once you have defined a session pool, you can configure one or more connection consumers for each session pool.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSConnectionConsumer");
         var2.setValue("destroyer", "destroyJMSConnectionConsumer");
         var2.setValue("creator", "createJMSConnectionConsumer");
      }

      if (!var1.containsKey("ListenerClass")) {
         var3 = "getListenerClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenerClass";
         }

         var2 = new PropertyDescriptor("ListenerClass", JMSSessionPoolMBean.class, var3, var4);
         var1.put("ListenerClass", var2);
         var2.setValue("description", "<p>The name of the server-side listener class for this JMS session pool, which is used to receive and process messages concurrently.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSSessionPoolMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionsMaximum")) {
         var3 = "getSessionsMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionsMaximum";
         }

         var2 = new PropertyDescriptor("SessionsMaximum", JMSSessionPoolMBean.class, var3, var4);
         var1.put("SessionsMaximum", var2);
         var2.setValue("description", "<p>The maximum number of sessions allowed for this JMS session pool. A value of <code>-1</code> indicates that there is no maximum.</p>  <p>This attribute is dynamically configurable; however, it does not take effect until the session pool is restarted.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Transacted")) {
         var3 = "isTransacted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransacted";
         }

         var2 = new PropertyDescriptor("Transacted", JMSSessionPoolMBean.class, var3, var4);
         var1.put("Transacted", var2);
         var2.setValue("description", "<p>Indicates whether this JMS session pool creates transacted sessions.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSSessionPoolMBean.class.getMethod("createJMSConnectionConsumer", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the connection consumer to create ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The connection consumers for this JMS session pool. JMS connection consumers are queues (Point-To-Point) or topics (Pub/Sub) that retrieve server sessions and process messages. Once you have defined a session pool, you can configure one or more connection consumers for each session pool.</p>  <p>Create a connection consumer for the session pool.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSConnectionConsumers");
      }

      var3 = JMSSessionPoolMBean.class.getMethod("destroyJMSConnectionConsumer", JMSConnectionConsumerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("connectionConsumer", "a reference to  JMSConnectionConsumerMBean ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Remove a connection consumer from the session pool.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSConnectionConsumers");
      }

      var3 = JMSSessionPoolMBean.class.getMethod("createJMSConnectionConsumer", String.class, JMSConnectionConsumerMBean.class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSConnectionConsumers");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSSessionPoolMBean.class.getMethod("addConnectionConsumer", JMSConnectionConsumerMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("connectionConsumer", "the connection consumer to add ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with message-driven beans. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Add a connection consumer to the session pool. This method is provided for backward compatibility.</p> ");
            var2.setValue("transient", Boolean.TRUE);
            var2.setValue("role", "collection");
            var2.setValue("property", "ConnectionConsumers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSSessionPoolMBean.class.getMethod("removeConnectionConsumer", JMSConnectionConsumerMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("connectionConsumer", "a reference to  JMSConnectionConsumerMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with message-driven beans. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Remove a connection consumer from the session pool. This method is provided for backward compatibility.</p> ");
            var2.setValue("transient", Boolean.TRUE);
            var2.setValue("role", "collection");
            var2.setValue("property", "ConnectionConsumers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSSessionPoolMBean.class.getMethod("lookupJMSConnectionConsumer", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Get a connection consumer from the session pool by name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSConnectionConsumers");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSSessionPoolMBean.class.getMethod("destroyJMSConnectionConsumer", String.class, JMSConnectionConsumerMBean.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = JMSSessionPoolMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSSessionPoolMBean.class.getMethod("restoreDefaultValue", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
