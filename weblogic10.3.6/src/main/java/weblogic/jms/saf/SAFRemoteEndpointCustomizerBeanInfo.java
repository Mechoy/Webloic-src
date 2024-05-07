package weblogic.jms.saf;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.messaging.saf.internal.SAFStatisticsCommonMBeanImplBeanInfo;

public class SAFRemoteEndpointCustomizerBeanInfo extends SAFStatisticsCommonMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SAFRemoteEndpointRuntimeMBean.class;

   public SAFRemoteEndpointCustomizerBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SAFRemoteEndpointCustomizerBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SAFRemoteEndpointCustomizer.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.jms.saf");
      String var3 = (new String("This class is used for monitoring a WebLogic SAF remote endpoint  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean");
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
         var2 = new PropertyDescriptor("BytesCurrentCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of bytes. This number does not include the pending bytes.</p> ");
      }

      if (!var1.containsKey("BytesHighCount")) {
         var3 = "getBytesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesHighCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of bytes since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending bytes. Pending bytes are over and above the current number of bytes.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesThresholdTime")) {
         var3 = "getBytesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("BytesThresholdTime", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("DowntimeHigh")) {
         var3 = "getDowntimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("DowntimeHigh", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("DowntimeHigh", var2);
         var2.setValue("description", "<p>Specifies the longest time, in seconds, that the remote endpoint has not been available since the last reset.</p> ");
      }

      if (!var1.containsKey("DowntimeTotal")) {
         var3 = "getDowntimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("DowntimeTotal", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("DowntimeTotal", var2);
         var2.setValue("description", "<p>Specifies the total time, in seconds, that the remote endpoint has not been available since the last reset.</p> ");
      }

      if (!var1.containsKey("EndpointType")) {
         var3 = "getEndpointType";
         var4 = null;
         var2 = new PropertyDescriptor("EndpointType", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("EndpointType", var2);
         var2.setValue("description", "<p>Specifies if the remote endpoint is a JMS or Web Services (WSRM) destination. The possible values are: weblogic.management.runtime.SAFConstants.JMS_ENDPOINT or weblogic.management.runtime.SAFConstants.WS_ENDPOINT.</p> ");
      }

      if (!var1.containsKey("FailedMessagesTotal")) {
         var3 = "getFailedMessagesTotal";
         var4 = null;
         var2 = new PropertyDescriptor("FailedMessagesTotal", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("FailedMessagesTotal", var2);
         var2.setValue("description", "<p>Returns the total number of messages that have failed to be forwarded since the last reset.</p> ");
      }

      if (!var1.containsKey("LastException")) {
         var3 = "getLastException";
         var4 = null;
         var2 = new PropertyDescriptor("LastException", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("LastException", var2);
         var2.setValue("description", "<p>Specifies the exception thrown when message forwarding failed.</p> ");
      }

      if (!var1.containsKey("LastTimeConnected")) {
         var3 = "getLastTimeConnected";
         var4 = null;
         var2 = new PropertyDescriptor("LastTimeConnected", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("LastTimeConnected", var2);
         var2.setValue("description", "<p>Specifies the last time that the remote endpoint was connected.</p> ");
      }

      if (!var1.containsKey("LastTimeFailedToConnect")) {
         var3 = "getLastTimeFailedToConnect";
         var4 = null;
         var2 = new PropertyDescriptor("LastTimeFailedToConnect", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("LastTimeFailedToConnect", var2);
         var2.setValue("description", "<p>Specifies the last time that the remote endpoint failed to be connected.</p> ");
      }

      if (!var1.containsKey("MessagesCurrentCount")) {
         var3 = "getMessagesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesCurrentCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of messages. This number includes the pending messages.</p> ");
      }

      if (!var1.containsKey("MessagesHighCount")) {
         var3 = "getMessagesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesHighCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of messages since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending messages. Pending messages are over and above the current number of messages. A pending message is one that has either been sent in a transaction and not committed, or been forwarded but has not been acknowledged.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesThresholdTime")) {
         var3 = "getMessagesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesThresholdTime", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("OperationState")) {
         var3 = "getOperationState";
         var4 = null;
         var2 = new PropertyDescriptor("OperationState", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("OperationState", var2);
         var2.setValue("description", "<p>Specifies the state of the most recent <code>ExireAll</code> operation.</p> ");
      }

      if (!var1.containsKey("URL")) {
         var3 = "getURL";
         var4 = null;
         var2 = new PropertyDescriptor("URL", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("URL", var2);
         var2.setValue("description", "<p>The URL of the remote endpoint.</p> ");
      }

      if (!var1.containsKey("UptimeHigh")) {
         var3 = "getUptimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("UptimeHigh", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("UptimeHigh", var2);
         var2.setValue("description", "<p>Specifies the longest time, in seconds, that the remote endpoint has been available since the last reset.</p> ");
      }

      if (!var1.containsKey("UptimeTotal")) {
         var3 = "getUptimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("UptimeTotal", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("UptimeTotal", var2);
         var2.setValue("description", "<p>Specifies the total time, in seconds, that the remote endpoint has been available since the last reset.</p> ");
      }

      if (!var1.containsKey("PausedForForwarding")) {
         var3 = "isPausedForForwarding";
         var4 = null;
         var2 = new PropertyDescriptor("PausedForForwarding", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("PausedForForwarding", var2);
         var2.setValue("description", "<p>Indicates if the remote endpoint is currently not forwarding messages.</p> ");
      }

      if (!var1.containsKey("PausedForIncoming")) {
         var3 = "isPausedForIncoming";
         var4 = null;
         var2 = new PropertyDescriptor("PausedForIncoming", SAFRemoteEndpointRuntimeMBean.class, var3, (String)var4);
         var1.put("PausedForIncoming", var2);
         var2.setValue("description", "<p>Indicates if a remote endpoint is currently not accepting new messages.</p> ");
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
      Method var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getCursorStartPosition", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor start position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getMessage", String.class, String.class);
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

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("preDeregister");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("sort", String.class, Long.class, String[].class, Boolean[].class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The handle of the cursor on which to perform the sort operation "), createParameterDescriptor("start", "The location of the message before the sort that will be  the first message returned after the sort.  A value of -1 will place the  cursor start position at the head of the new sort order. "), createParameterDescriptor("fields", "The SAF header attributes on which to sort. "), createParameterDescriptor("ascending", "Determines whether the sort of the corresponding fields  element is in ascending or descending order. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var7;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException Thrown when an internal error occurs while  processing the request.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Sorts the entire message result set managed by the cursor according to the SAF header attributes specified. The cursor position is set to the new position of the message corresponding to the \"start\" location before the sort is performed. The method returns the new cursor position.</p> ");
         var2.setValue("role", "operation");
         var7 = new String[]{BeanInfoHelper.encodeEntities("Operator"), BeanInfoHelper.encodeEntities("Monitor")};
         var2.setValue("rolesAllowed", var7);
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getCursorEndPosition", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor end position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getMessage", String.class, Long.class);
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

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getItems", String.class, Long.class, Integer.class);
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

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getNext", String.class, Integer.class);
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

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("pauseIncoming");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      String[] var9;
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Pauses a remote endpoint so that new messages are not accepted.</p> ");
         var2.setValue("role", "operation");
         var9 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var9);
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getPrevious", String.class, Integer.class);
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

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("resumeIncoming");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Resumes a remote endpoint so that new messages are accepted.</p> ");
         var2.setValue("role", "operation");
         var9 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var9);
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getCursorSize", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the number of items in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("closeCursor", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Releases the server-side resources associated with the cursor and removes the runtime MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("pauseForwarding");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Pauses the forwarding of messages for a remote endpoint. The agent accepts new messages but does not forward them.</p> ");
         var2.setValue("role", "operation");
         var9 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var9);
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("resumeForwarding");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Resumes the forwarding of messages for the remote endpoint.</p> ");
         var2.setValue("role", "operation");
         var9 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var9);
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("expireAll");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>All pending messages for a remote destination are processed according to the policy specified by the associated Error Handling configuration and then removed.</p>  <ul> <li> <p>When selected, <code>expireAll</code> is performed asynchronously by the server.</p> </li>  <li> <p>Oracle recommends that the remote endpoint is paused for incoming messages prior to expiring messages. When all pending messages are processed and removed, the remote endpoint can be set to resume and accept new messages.</p> </li>  <li> <p>The state of the <code>expireAll</code> operation can be determined by the <code>getOperationState</code> method.</p> </li>  </ul ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("purge");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Destroys all conversations and purges all the pending messages for a remote destination.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SAFRemoteEndpointRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A valid JMS message selector or SAF message selector. "), createParameterDescriptor("timeout", "Specifies the amount of time the message cursor is valid.  A value of 0 indicates the cursor does not expire. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Queries messages on the queue according to the message selector and returns a message cursor representing the result set. The timeout parameter specifies the amount of time in seconds for which the cursor is valid. If the cursor expires, the associated resources are released.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("SAFMessageCursorRuntimeMBean")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
         var7 = new String[]{BeanInfoHelper.encodeEntities("Operator"), BeanInfoHelper.encodeEntities("Monitor")};
         var2.setValue("rolesAllowed", var7);
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
