package weblogic.server.channels;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ServerChannelRuntimeMBean;

public class ServerChannelRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerChannelRuntimeMBean.class;

   public ServerChannelRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerChannelRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerChannelRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.server.channels");
      String var3 = (new String("Runtime information for NetworkAccessPoints or \"Channels\".  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServerChannelRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AcceptCount")) {
         var3 = "getAcceptCount";
         var4 = null;
         var2 = new PropertyDescriptor("AcceptCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("AcceptCount", var2);
         var2.setValue("description", "<p>The number of sockets that have been accepted on this channel. This includes sockets both past and present so gives a good idea of the connection rate to the server.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The total number of bytes received on this channel.</p> ");
      }

      if (!var1.containsKey("BytesSentCount")) {
         var3 = "getBytesSentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesSentCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesSentCount", var2);
         var2.setValue("description", "<p>The total number of bytes sent on this channel.</p> ");
      }

      if (!var1.containsKey("ChannelName")) {
         var3 = "getChannelName";
         var4 = null;
         var2 = new PropertyDescriptor("ChannelName", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("ChannelName", var2);
         var2.setValue("description", "<p>The channel name of this channel.</p> ");
      }

      if (!var1.containsKey("ConnectionsCount")) {
         var3 = "getConnectionsCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsCount", var2);
         var2.setValue("description", "<p>The number of active connections and sockets associated with this channel.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received on this channel.</p> ");
      }

      if (!var1.containsKey("MessagesSentCount")) {
         var3 = "getMessagesSentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesSentCount", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesSentCount", var2);
         var2.setValue("description", "<p>The number of messages sent on this channel.</p> ");
      }

      if (!var1.containsKey("PublicURL")) {
         var3 = "getPublicURL";
         var4 = null;
         var2 = new PropertyDescriptor("PublicURL", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("PublicURL", var2);
         var2.setValue("description", "<p>The physical URL that this channel is listening on.</p> ");
      }

      if (!var1.containsKey("ServerConnectionRuntimes")) {
         var3 = "getServerConnectionRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ServerConnectionRuntimes", ServerChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerConnectionRuntimes", var2);
         var2.setValue("description", "<p>The active connections and sockets associated with this channel.</p> ");
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
      Method var3 = ServerChannelRuntimeMBean.class.getMethod("preDeregister");
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
