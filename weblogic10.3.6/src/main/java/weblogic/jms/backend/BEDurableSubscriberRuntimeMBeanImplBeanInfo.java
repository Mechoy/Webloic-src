package weblogic.jms.backend;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import weblogic.management.configuration.JMSConstants;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSDurableSubscriberRuntimeMBean;

public class BEDurableSubscriberRuntimeMBeanImplBeanInfo extends BEMessageManagementRuntimeDelegateBeanInfo {
   public static Class INTERFACE_CLASS = JMSDurableSubscriberRuntimeMBean.class;

   public BEDurableSubscriberRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BEDurableSubscriberRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BEDurableSubscriberRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms.backend");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS durable subscriber.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSDurableSubscriberRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("BytesCurrentCount")) {
         var3 = "getBytesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesCurrentCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesCurrentCount", var2);
         var2.setValue("description", "<p>The number of bytes received by this durable subscriber.</p> ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>The number of bytes pending by this durable subscriber.</p> ");
      }

      if (!var1.containsKey("ClientID")) {
         var3 = "getClientID";
         var4 = null;
         var2 = new PropertyDescriptor("ClientID", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientID", var2);
         var2.setValue("description", "<p>A unique client identifier for this durable subscriber.</p>  <p><b>Note:</b> The client ID is not necessarily equivalent to the WebLogic Server username; that is, a name used to authenticate a user in the WebLogic security realm. You can set the client ID to the WebLogic Server username if it is appropriate for your JMS application.</p> ");
      }

      if (!var1.containsKey("ClientIDPolicy")) {
         var3 = "getClientIDPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("ClientIDPolicy", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientIDPolicy", var2);
         var2.setValue("description", "<p> The policy for the client identifier for this durable subscriber.</p>  <p><b>Note:</b> The client ID policy is either <code>Restricted</code> or <code>Unrestricted</code>. </p> ");
      }

      String[] var5;
      if (!var1.containsKey("CurrentConsumerInfo")) {
         var3 = "getCurrentConsumerInfo";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentConsumerInfo", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentConsumerInfo", var2);
         var2.setValue("description", "<p>Specifies information about the current consumer.  The information is returned in the form of an OpenMBean CompositeData object.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.jms.extensions.ConsumerInfo")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("DestinationInfo")) {
         var3 = "getDestinationInfo";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationInfo", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationInfo", var2);
         var2.setValue("description", "<p> Specifies information about the durable subscriber's internal destination in JMX open data representation. The resulting object is intended for use in the message management APIs for identifying a target destination. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("JMSMessageManagementRuntimeMBean#moveMessages(String, CompositeData)"), BeanInfoHelper.encodeEntities("JMSMessageManagementRuntimeMBean#moveMessages(String, CompositeData, Integer)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("DestinationRuntime")) {
         var3 = "getDestinationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationRuntime", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationRuntime", var2);
         var2.setValue("description", "<p>The runtime MBean of the Topic to which this durable subscriber is associated.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("LastMessagesReceivedTime")) {
         var3 = "getLastMessagesReceivedTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastMessagesReceivedTime", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("LastMessagesReceivedTime", var2);
         var2.setValue("description", "<p> The time when the last time a subscriber received a message from the subscription. The returned value is a standard java absolute time, which is measured in milliseconds since midnight, January 1, 1970 UTC </p>  This returns the JMS durable subscription boot time if there were no messages that were successfully delivered to any subscriber on this subscription since the subscription was booted. We define the boot time of a durable subscription to be the time the subscription is originally created or recovered during a server reboot or jms migration, which ever is latest. ");
      }

      if (!var1.containsKey("MessagesCurrentCount")) {
         var3 = "getMessagesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesCurrentCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesCurrentCount", var2);
         var2.setValue("description", "<p>The number of messages still available by this durable subscriber.</p> ");
      }

      if (!var1.containsKey("MessagesDeletedCurrentCount")) {
         var3 = "getMessagesDeletedCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesDeletedCurrentCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesDeletedCurrentCount", var2);
         var2.setValue("description", "<p>Returns the number of messages that have been deleted from the destination.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#deleteMessages(String)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("MessagesHighCount")) {
         var3 = "getMessagesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesHighCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesHighCount", var2);
         var2.setValue("description", "<p>The peak number of messages for the durable subscriber since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesMovedCurrentCount")) {
         var3 = "getMessagesMovedCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesMovedCurrentCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesMovedCurrentCount", var2);
         var2.setValue("description", "<p>Returns the number of messages that have been moved from the destination.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>The number of messages pending (uncommitted and unacknowledged) by this durable subscriber.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received by the durable subscriber since that reset.</p> ");
      }

      if (!var1.containsKey("Selector")) {
         var3 = "getSelector";
         var4 = null;
         var2 = new PropertyDescriptor("Selector", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("Selector", var2);
         var2.setValue("description", "<p>The message selector defined for this durable subscriber.</p> ");
      }

      if (!var1.containsKey("SubscribersCurrentCount")) {
         var3 = "getSubscribersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("SubscribersCurrentCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscribersCurrentCount", var2);
         var2.setValue("description", "The number of subscribers that currently share this subscription. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
      }

      if (!var1.containsKey("SubscribersHighCount")) {
         var3 = "getSubscribersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("SubscribersHighCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscribersHighCount", var2);
         var2.setValue("description", "The highest number of subscribers that have shared this subscription at the same time since the creation or the last reboot of the subscription, which ever is later. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
      }

      if (!var1.containsKey("SubscribersTotalCount")) {
         var3 = "getSubscribersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SubscribersTotalCount", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscribersTotalCount", var2);
         var2.setValue("description", "The total number of subscribers that have accessed this subscription since the creation or the last reboot of the subscription, whichever is later. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
      }

      if (!var1.containsKey("SubscriptionName")) {
         var3 = "getSubscriptionName";
         var4 = null;
         var2 = new PropertyDescriptor("SubscriptionName", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscriptionName", var2);
         var2.setValue("description", "<p>The subscription name for this durable subscriber. This name must be unique for each client ID.</p>  <p>Valid durable subscription names cannot include the following characters: comma \",\", equals \"=\", colon \":\", asterisk \"*\", percent \"%\", or question mark\"?\".</p> ");
      }

      if (!var1.containsKey("SubscriptionSharingPolicy")) {
         var3 = "getSubscriptionSharingPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("SubscriptionSharingPolicy", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscriptionSharingPolicy", var2);
         var2.setValue("description", "The SubscriptionSharingPolicy on this subscriber. ");
         setPropertyDescriptorDefault(var2, JMSConstants.SUBSCRIPTION_EXCLUSIVE);
      }

      if (!var1.containsKey("Active")) {
         var3 = "isActive";
         var4 = null;
         var2 = new PropertyDescriptor("Active", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("Active", var2);
         var2.setValue("description", "<p>Indicates whether this subscription is being used by a durable subscriber.</p> ");
      }

      if (!var1.containsKey("NoLocal")) {
         var3 = "isNoLocal";
         var4 = null;
         var2 = new PropertyDescriptor("NoLocal", JMSDurableSubscriberRuntimeMBean.class, var3, (String)var4);
         var1.put("NoLocal", var2);
         var2.setValue("description", "<p>Specifies whether this durable subscriber receives local messages that it has published.</p>  <p>To prevent this, set the <code>noLocal</code> parameter to <code>true</code>.</p> ");
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
      Method var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getCursorStartPosition", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor start position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getMessage", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The handle of the cursor. "), createParameterDescriptor("messageID", "The JMS message ID of the requested message. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Given a JMS message ID this method returns the corresponding message from the queue. If no message with the specified message ID exists on the destination, a null value is returned.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("preDeregister");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("sort", String.class, Long.class, String[].class, Boolean[].class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The handle of the cursor. "), createParameterDescriptor("start", "The location of the message before the sort that will be the first message returned after the sort.  A value of -1 will place the  cursor start position at the head of the new sort order. "), createParameterDescriptor("fields", "The JMS header attributes on which to sort. "), createParameterDescriptor("ascending", "Determines whether the sort of the corresponding fields element is in ascending or descending order. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var7;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException Thrown when an internal JMS error occurs while processing the request.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Sorts the entire message result set managed by the cursor according to the JMS header attributes specified. The cursor position is set to the new position of the message corresponding to the \"start\" location before the sort is performed. The method returns the new cursor position.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getCursorEndPosition", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor end position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getMessage", String.class, Long.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The handle of the cursor. "), createParameterDescriptor("messageHandle", "The handle of the message within the cursor. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException Thrown when an error occurs while performing the  operation.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the message associated with the specified cursor handle.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getItems", String.class, Long.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. "), createParameterDescriptor("start", "The new cursor start location. "), createParameterDescriptor("count", "The maximum number of items to return. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns an array of items from the specified cursor location. The new cursor start position will be the location after the old cursor end position. The size of the array returned is determined by the count argument. An array smaller than the \"count\" value is returned if there are fewer items from the specified start position to the end of the result set. A null value is returned if the size of the return array is zero. In this case, the cursor position will not change.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A valid JMS message selector. "), createParameterDescriptor("timeout", "The last access timeout for the cursor.  The cursor  resources will be reclaimed if it is not accessed within the specified  time interval.  A value of 0 indicates no timeout. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Queries messages on the queue according to the provided message selector and returns a message cursor representing the result set. The timeout parameter specifies the amount of time in seconds for which the cursor is valid. Upon timeout expiration the cursor is invalidated and the associated resources released.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("JMSMessageCursorRuntimeMBean")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A valid JMS message selector. "), createParameterDescriptor("timeout", "The last access timeout for the cursor.  The cursor  resources will be reclaimed if it is not accessed within the specified  time interval.  A value of 0 indicates no timeout. "), createParameterDescriptor("state", "A messaging kernel state bitmask.  Refer to the messaging kernel MessageElement interface for a description of the various message states. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Queries messages on the queue according to the provided message selector and state bitmask and returns a message cursor representing the result set. The timeout parameter specifies the amount of time in seconds for which the cursor is valid. Upon timeout expiration the cursor is invalidated and the associated resources released.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("JMSMessageCursorRuntimeMBean")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getNext", String.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. "), createParameterDescriptor("count", "The maximum number of items to return. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns an array of items starting from the current cursor end position.  The new cursor start position is set to be the location of the first item returned to the caller. The new cursor end position is set according to the size of the array returned, which is determined by the count argument. An array smaller than the \"count\" value is returned if there are fewer items from the specified start position to the end of the result set. A null value is returned if the size of the array is zero. In this case, the cursor position will not change.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getMessage", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messageID", "The JMS message ID of the requested message. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Given a JMS message ID this method returns the corresponding message from the queue. If no message with the specified message ID exists on the destination, a null value is returned.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getPrevious", String.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. "), createParameterDescriptor("count", "The maximum number of item to return. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns an array of items up to the current cursor start position. The new start position will be placed at the location of the first item in the set returned to the caller. The new cursor end position will be placed at the location after the last item in the set that is returned.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.management.openmbean.CompositeData")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("getCursorSize", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the number of items in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("moveMessages", String.class, CompositeData.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A JMS message selector that identifies the messages  to move. "), createParameterDescriptor("targetDestination", "A JMS destination that the messages will  be moved to. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Moves the set of messages that match the specified selector to the target destination. The move operation is guaranteed to be atomic for the selected messages.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("closeCursor", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Releases the server-side resources associated with the cursor and removes the runtime MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("moveMessages", String.class, CompositeData.class, Integer.class);
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("deleteMessages", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A JMS message selector to identify which messages to  delete. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the set of messages from the destination that are qualified by the specified JMS message selector.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("importMessages", CompositeData[].class, Boolean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messages", "An array of messages in CompositeData representation to be imported. "), createParameterDescriptor("replaceOnly", "When set to true an excetion will be thrown if the message ID does not exist on the target destination. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Imports an array of messages into the destination. If the message ID of the message being imported matches a message already on the destination, then the existing message will be replaced. If an existing message does not exist, then the message will be produced on the destination. A produced message is subject to quota limitations.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDurableSubscriberRuntimeMBean.class.getMethod("purge");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "As of WLS 9.0.  Use {@link #deleteMessages(String)} ");
         var1.put(var8, var2);
         var2.setValue("description", "<p>Purges all the pending and current messages for this durable subscriber.</p> ");
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
