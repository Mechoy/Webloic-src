package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WSReliableDeliveryPolicyMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WSReliableDeliveryPolicyMBean.class;

   public WSReliableDeliveryPolicyMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WSReliableDeliveryPolicyMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WSReliableDeliveryPolicyMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents the reliable messaging delivery policy for the current WebLogic Server as both a sender and a receiver of a reliable SOAP message to and from a Web service running on a different WebLogic Server. <p> Reliable messaging is a framework for applications running in WebLogic Server to asynchronously and reliably invoke a Web service running in a different WebLogic Server.  Reliable messaging works only between WebLogic Servers.  This class encapsulates the default reliable messaging parameters for an application running in this WebLogic Server instance that both sends and receives a SOAP message reliably. These parameters include the number of times to retry sending the message, the time to wait between retries, the persistent store for the reliable message, and so on. </p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WSReliableDeliveryPolicyMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DefaultRetryCount")) {
         var3 = "getDefaultRetryCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRetryCount";
         }

         var2 = new PropertyDescriptor("DefaultRetryCount", WSReliableDeliveryPolicyMBean.class, var3, var4);
         var1.put("DefaultRetryCount", var2);
         var2.setValue("description", "<p>The default maximum number of times that the sender runtime should attempt to redeliver a message that the receiver WebLogic Server has not yet acknowledged.</p>  <p>The default maximum number of times that the sender should attempt to redeliver a message that the receiver WebLogic Web service has not yet acknowledged.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
      }

      if (!var1.containsKey("DefaultRetryInterval")) {
         var3 = "getDefaultRetryInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRetryInterval";
         }

         var2 = new PropertyDescriptor("DefaultRetryInterval", WSReliableDeliveryPolicyMBean.class, var3, var4);
         var1.put("DefaultRetryInterval", var2);
         var2.setValue("description", "<p>The default minimum number of seconds that the sender runtime should wait between retries if the receiver does not send an acknowledgement of receiving the message, or if the sender runtime detects a communications error while attempting to send a message.</p>  <p>The default minimum number of seconds that the sender should wait between retries if the receiver does not send an acknowledgement of receiving the message, or if the sender detects a communications error while attempting to send a message.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(6));
      }

      if (!var1.containsKey("DefaultTimeToLive")) {
         var3 = "getDefaultTimeToLive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTimeToLive";
         }

         var2 = new PropertyDescriptor("DefaultTimeToLive", WSReliableDeliveryPolicyMBean.class, var3, var4);
         var1.put("DefaultTimeToLive", var2);
         var2.setValue("description", "<p>The default number of seconds that the receiver of the reliable message should persist the history of the reliable message in its store.</p>  <p>The default minimum number of seconds that the receiver of the reliably sent message should persist the message in its storage.</p>  <p>If the DefaultTimeToLive number of message have passed since the message was first sent, the sender should not resent a message with the same message id.</p>  <p>If a sender cannot send a message successfully before the DefaultTimeToLive has passed, the sender should report a delivery failure. The receiver, after recovering from a crash, will not dispatch saved messages that have expired.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(360));
      }

      if (!var1.containsKey("JMSServer")) {
         var3 = "getJMSServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSServer";
         }

         var2 = new PropertyDescriptor("JMSServer", WSReliableDeliveryPolicyMBean.class, var3, var4);
         var1.put("JMSServer", var2);
         var2.setValue("description", "<p>The JMS server used by WebLogic Server, in its role as a sender, to persist the reliable messages that it sends, or the JMS server used by the receiver WebLogic Server to persist the history of a reliable message sent by a sender.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("Store")) {
         var3 = "getStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStore";
         }

         var2 = new PropertyDescriptor("Store", WSReliableDeliveryPolicyMBean.class, var3, var4);
         var1.put("Store", var2);
         var2.setValue("description", "<p>The persistent JMS store used by WebLogic Server, in its role as a sender, to persist the reliable messages that it sends, or the persistent JMS store used by the receiver WebLogic Server to persist the history of a reliable message sent by a sender.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 use the JMSServer attribute instead ");
         var2.setValue("obsolete", "9.0.0.0");
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
