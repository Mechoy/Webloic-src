package weblogic.jms.backend;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSServerRuntimeMBean;

public class BackEndBeanInfo extends JMSMessageCursorRuntimeImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSServerRuntimeMBean.class;

   public BackEndBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BackEndBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BackEnd.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms.backend");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSServerRuntimeMBean");
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
         var2 = new PropertyDescriptor("BytesCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesCurrentCount", var2);
         var2.setValue("description", "<p>The current number of bytes stored on this JMS server.</p>  <p>This number does not include the pending bytes.</p> ");
      }

      if (!var1.containsKey("BytesHighCount")) {
         var3 = "getBytesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesHighCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesHighCount", var2);
         var2.setValue("description", "<p>The peak number of bytes stored in the JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesPageableCurrentCount")) {
         var3 = "getBytesPageableCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPageableCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPageableCurrentCount", var2);
         var2.setValue("description", "Return the total number of bytes in all the messages that are currently available to be paged out, but which have not yet been paged out. The JMS server attempts to keep this number smaller than the \"MessageBufferSize\" parameter. ");
      }

      if (!var1.containsKey("BytesPagedInTotalCount")) {
         var3 = "getBytesPagedInTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPagedInTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPagedInTotalCount", var2);
         var2.setValue("description", "Return the total number of bytes that were read from the paging directory since the JMS server was started. ");
      }

      if (!var1.containsKey("BytesPagedOutTotalCount")) {
         var3 = "getBytesPagedOutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPagedOutTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPagedOutTotalCount", var2);
         var2.setValue("description", "Return the total number of bytes that were written to the paging directory since the JMS server was started. ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>The current number of bytes pending (unacknowledged or uncommitted) stored on this JMS server.</p>  <p>Pending bytes are over and above the current number of bytes.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received on this JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesThresholdTime")) {
         var3 = "getBytesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("BytesThresholdTime", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesThresholdTime", var2);
         var2.setValue("description", "<p>The amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("ConsumptionPausedState")) {
         var3 = "getConsumptionPausedState";
         var4 = null;
         var2 = new PropertyDescriptor("ConsumptionPausedState", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("ConsumptionPausedState", var2);
         var2.setValue("description", "<p>Returns the current consumption paused state of the JMSServer as string value. ");
      }

      if (!var1.containsKey("Destinations")) {
         var3 = "getDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("Destinations", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("Destinations", var2);
         var2.setValue("description", "<p>An array of destinations on this JMS server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DestinationsCurrentCount")) {
         var3 = "getDestinationsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationsCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of destinations for this JMS server.</p> ");
      }

      if (!var1.containsKey("DestinationsHighCount")) {
         var3 = "getDestinationsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationsHighCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationsHighCount", var2);
         var2.setValue("description", "<p>The peak number of destinations on this JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("DestinationsTotalCount")) {
         var3 = "getDestinationsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationsTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationsTotalCount", var2);
         var2.setValue("description", "<p>The number of destinations instantiated on this JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of this JMS server.</p> ");
      }

      if (!var1.containsKey("InsertionPausedState")) {
         var3 = "getInsertionPausedState";
         var4 = null;
         var2 = new PropertyDescriptor("InsertionPausedState", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("InsertionPausedState", var2);
         var2.setValue("description", "<p>Returns the current insertion paused state of the JMSServer as string value. ");
      }

      if (!var1.containsKey("MessagesCurrentCount")) {
         var3 = "getMessagesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesCurrentCount", var2);
         var2.setValue("description", "<p>The current number of messages stored on this JMS server. This number does not include the pending messages.</p> ");
      }

      if (!var1.containsKey("MessagesHighCount")) {
         var3 = "getMessagesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesHighCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesHighCount", var2);
         var2.setValue("description", "<p>The peak number of messages stored in the JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesPageableCurrentCount")) {
         var3 = "getMessagesPageableCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPageableCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPageableCurrentCount", var2);
         var2.setValue("description", "Return the number of messages that are currently available for paging in this JMS server but have not yet been paged out. Note that due to internal implementation details, this count may be zero even if \"PageableByteCurrentCount\" is zero. ");
      }

      if (!var1.containsKey("MessagesPagedInTotalCount")) {
         var3 = "getMessagesPagedInTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPagedInTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPagedInTotalCount", var2);
         var2.setValue("description", "Return the total number of messages that were read from the paging directory since the JMS server was started. ");
      }

      if (!var1.containsKey("MessagesPagedOutTotalCount")) {
         var3 = "getMessagesPagedOutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPagedOutTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPagedOutTotalCount", var2);
         var2.setValue("description", "Return the total number of messages that were written to the paging directory since the JMS server was started. ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>The current number of messages pending (unacknowledged or uncommitted) stored on this JMS server.</p>  <p>Pending messages are over and above the current number of messages.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received on this destination since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesThresholdTime")) {
         var3 = "getMessagesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesThresholdTime", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesThresholdTime", var2);
         var2.setValue("description", "<p>The amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("PagingAllocatedIoBufferBytes")) {
         var3 = "getPagingAllocatedIoBufferBytes";
         var4 = null;
         var2 = new PropertyDescriptor("PagingAllocatedIoBufferBytes", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("PagingAllocatedIoBufferBytes", var2);
         var2.setValue("description", "See <a href=\"PersistentStoreRuntimeMBean.html#getAllocatedIoBufferBytes\">PersistentStoreRuntimeMBean.AllocatedIoBufferBytes</a> ");
      }

      if (!var1.containsKey("PagingAllocatedWindowBufferBytes")) {
         var3 = "getPagingAllocatedWindowBufferBytes";
         var4 = null;
         var2 = new PropertyDescriptor("PagingAllocatedWindowBufferBytes", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("PagingAllocatedWindowBufferBytes", var2);
         var2.setValue("description", "See <a href=\"PersistentStoreRuntimeMBean.html#getAllocatedWindowBufferBytes\">PersistentStoreRuntimeMBean.AllocatedWindowBufferBytes</a> ");
      }

      if (!var1.containsKey("PagingPhysicalWriteCount")) {
         var3 = "getPagingPhysicalWriteCount";
         var4 = null;
         var2 = new PropertyDescriptor("PagingPhysicalWriteCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("PagingPhysicalWriteCount", var2);
         var2.setValue("description", "See <a href=\"PersistentStoreRuntimeMBean.html#getPhysicalWriteCount\">PersistentStoreRuntimeMBean.PhysicalWriteCount</a> ");
      }

      String[] var5;
      if (!var1.containsKey("PendingTransactions")) {
         var3 = "getPendingTransactions";
         var4 = null;
         var2 = new PropertyDescriptor("PendingTransactions", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingTransactions", var2);
         var2.setValue("description", "Returns an array of Xids representing transaction branches that exist onthis JMS server in the pending state, i.e. branches that have been prepared by the transaction manager but not yet committed or rolled back. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("javax.transaction.xa.Xid")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("ProductionPausedState")) {
         var3 = "getProductionPausedState";
         var4 = null;
         var2 = new PropertyDescriptor("ProductionPausedState", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("ProductionPausedState", var2);
         var2.setValue("description", "<p>Returns the current production paused state of the JMSServer as string value. ");
      }

      if (!var1.containsKey("SessionPoolRuntimes")) {
         var3 = "getSessionPoolRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("SessionPoolRuntimes", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionPoolRuntimes", var2);
         var2.setValue("description", "<p>The session pools running on this JMS server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SessionPoolsCurrentCount")) {
         var3 = "getSessionPoolsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionPoolsCurrentCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionPoolsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of session pools instantiated on this JMS server.</p> ");
      }

      if (!var1.containsKey("SessionPoolsHighCount")) {
         var3 = "getSessionPoolsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionPoolsHighCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionPoolsHighCount", var2);
         var2.setValue("description", "<p>The peak number of session pools instantiated on this JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("SessionPoolsTotalCount")) {
         var3 = "getSessionPoolsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionPoolsTotalCount", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionPoolsTotalCount", var2);
         var2.setValue("description", "<p>The number of session pools instantiated on this JMS server since the last reset.</p> ");
      }

      if (!var1.containsKey("Transactions")) {
         var3 = "getTransactions";
         var4 = null;
         var2 = new PropertyDescriptor("Transactions", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("Transactions", var2);
         var2.setValue("description", "Returns an array of Xids representing transaction branches that exist on this JMS server in any state. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("javax.transaction.xa.Xid")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("ConsumptionPaused")) {
         var3 = "isConsumptionPaused";
         var4 = null;
         var2 = new PropertyDescriptor("ConsumptionPaused", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("ConsumptionPaused", var2);
         var2.setValue("description", "<p>Returns the current consumption paused state of the JMSServer as boolean value. ");
      }

      if (!var1.containsKey("InsertionPaused")) {
         var3 = "isInsertionPaused";
         var4 = null;
         var2 = new PropertyDescriptor("InsertionPaused", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("InsertionPaused", var2);
         var2.setValue("description", "<p>Returns the current insertion paused state of the JMSServer as boolean value. ");
      }

      if (!var1.containsKey("ProductionPaused")) {
         var3 = "isProductionPaused";
         var4 = null;
         var2 = new PropertyDescriptor("ProductionPaused", JMSServerRuntimeMBean.class, var3, (String)var4);
         var1.put("ProductionPaused", var2);
         var2.setValue("description", "<p>Returns the current production paused state of the JMSServer as boolean value. ");
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
      Method var3 = JMSServerRuntimeMBean.class.getMethod("getCursorStartPosition", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor start position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("getMessage", String.class, String.class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("preDeregister");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("sort", String.class, Long.class, String[].class, Boolean[].class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("getCursorEndPosition", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor end position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("getMessage", String.class, Long.class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("getItems", String.class, Long.class, Integer.class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("getNext", String.class, Integer.class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("getPrevious", String.class, Integer.class);
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

      var3 = JMSServerRuntimeMBean.class.getMethod("getCursorSize", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the number of items in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("closeCursor", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Releases the server-side resources associated with the cursor and removes the runtime MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("pauseProduction");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      String[] var9;
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMSException Thrown when an internal JMS error occurs while pausing the production.")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Pauses the new message production on all the destinations hosted by the JMSServer. </p> When the production paused, it would prevent any new produce operations from both new and existing producers attached to the destinations. When the destination is &quot;resumed from production pause&quot;, all the new message production is allowed from both new and existing producers attached to that destination.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#resumeProduction"), BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#pauseInsertion"), BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#pauseConsumption")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("resumeProduction");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMSException Thrown when an internal JMS error occurs while resuming the production.")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Resumes the new message production operation on all the destinations hosted by the JMSServer. The state of the JMSServer shall be marked as &quot;production enabled&quot; thus allowing all the new &quot;producing&quot; activity to continue normally. Invoking this API on a JMSServer that is currently not in &quot;production paused&quot; state has no effect. ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("pauseInsertion");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMSException Thrown when an internal JMS error occurs while pausing the insertion.")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Pauses the appearance of any messages on all the destinations of the JMSServer, that are result of the in-flight work completion on all the destinations hosted by this JMSServer.  <p><b> Definition of In-Flight work</b></p> <p> The definitions below are based on the current implementation of WebLogic JMS subsystem.  <ul> <ul> <p>In-flight messages associated with Producers  <li> UN-BORN MESSAGES <p>Messages that are produced by the producer, with &quot;birth time&quot; (TimeToDeliver) set in future are called un-born messages and are counted as &quot;pending&quot; messages in the destination statistics and are not available for consumers yet.</li>  <li> UN-COMMITTED MESSAGES <p>These are the messages that are produced by the producer as part of the transaction (using either user transaction or transacted session) and the transaction is still not committed or rolled back. These messages are also counted as &quot;pending&quot; messages in the destination statistics and are not available for consumption.</li>  <li> QUOTA BLOCKING SEND <p>These are the messages that are produced by the producers but are not able reach the destination because of (either message or byte or both) quota limit on the destination and the producers are willing to block for a specific period of time for the quota to be available. These messages are invisible to the system and are not counted against any of the destination statistics.</li> </ul>  <ul> <p>In-flight messages associated with Consumers  <li> UN-ACKNOWLEDGED (CLIENT ACK PENDING) MESSAGES <p>These are the messages that are successfully consumed by the clients using a &quot;client acknowledge&quot; session, and are awaiting acknowledgements from the clients. These are &quot;pending messages&quot; which will be removed from the destination/system when the acknowledgement is received.</li>  <li> UN-COMMITTED MESSAGES <p>These are the messages that are consumed (received) by the clients within a transaction (using either user transaction or transacted session) and the transaction is still not committed or rolled back. When the clients successfully commit the transaction the messages get removed from the system.  <li> ROLLED BACK MESSAGES <p>These are the messages that are put back on the destination because of a successful rollback of transactional receive by the consumers. These messages might or might not be ready for consumption (re-delivered) to the clients immediately, depending on the redelivery parameters, RedeliveryDelay and/or RedeliveryDelayOverride and RedeliveryLimit configured on the associated JMSConnectionFactory and JMSDestination respectively.  <p>If there is a redelivery delay configured, then for that &quot;delay&quot; duration, the messages are not available for consumption and are counted as &quot;pending&quot; in the destination statistics and after the &quot;delay&quot; period, if the redelivery limit is not exceeded, then they are delivered (made available for consumption) on that destination and are counted as &quot;current&quot; messages in the destination statistics. If the redelivery limit exceeds, then those messages will be moved to the ErrorDestination, if one configured.  <p>Another parameter that controls the availability of the rolled back messages is RedeliveryLimit.</li>  <li> RECOVERED MESSAGES <p>These messages are similar to <b>ROLLED BACK MESSAGES<b> discussed above. Except that these messages appear on the queue because of an explicit call to session &quot;recover&quot; by the client.  <li> REDELIVERED MESSAGES <p>These are again similar to <b>ROLLED BACK MESSAGES<b> except that these messages may re-appear on the destination because of an un-successful delivery attempt to the client (consumer crash, close etc.).</li> </ul> </ul> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#resumeInsertion"), BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#pauseProduction"), BeanInfoHelper.encodeEntities("JMSServerRuntimeMBean#pauseConsumption")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("resumeInsertion");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMSException Thrown when an internal JMS error occurs while resuming the insertion.")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Resumes the in-flight message production operation on all the destinations hosted by the JMSServer. The state of the JMSServer shall be marked as &quot;insertion enabled&quot; thus allowing all the messages from in-flight work completion are alloed to appear on the destinations.  Invoking this API on a JMSServer that is currently not in &quot;insertion paused&quot; state has no effect. ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("pauseConsumption");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Pauses the consumption operation on all the destinations hosted by the JMSServer.  </p> When the JMSServer is paused for consumption, all of its destination's state is marked as &quot;consumption paused&quot; and all the new synchronous receive operations will block until the destination is resumed and there are messages available for consumption. All the synchronous receive with blocking timeout will block until the timeout happens during the consumption paused state.  <p>All the asynchronous consumers attached to that destination will not get any messages delivered to them while the destination in &quot;consumption paused&quot; state.  <p> After a successful consumption &quot;pause&quot; operation, the user has to explicitly &quot;resume&quot; the destination to allow for any further consume operations on that destination ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("resumeConsumption");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMSException Thrown when an internal JMS error occurs while resuming the consumption.")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Resumes the consumption operation on all the destinations hosted by the JMSSever.</p> <p> The state of the destinations shall be marked as &quot;consumption enabled&quot; thus allowing all the &quot;consuming&quot; activity to continue normally.  <p>Invoking this API on a JMSServer that is currently not in \"consumption paused\" state has no effect. ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("getTransactionStatus", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("xid", "An Xid in string representation for a JMS transaction branch. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Given an Xid this method returns the JTA status of the associated JMS transaction branch. ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.transaction.xa.Xid"), BeanInfoHelper.encodeEntities("javax.transaction.Status")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("xid", "An Xid in string representation for a JMS transaction branch. "), createParameterDescriptor("timeoutSeconds", "The last access timeout for the cursor.  The cursor resources will be reclaimed if it is not accessed within the specified time interval.  A value of 0 indicates no timeout. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Returns a set of messages that are associated with a JMS transaction branch.  Note that the result set is returned to the caller in the form of a message cursor that may contain messages from several destinations on this JMS server. The timeout parameter specifies the amount of time in seconds for which the cursor is valid.  Upon timeout expiration the cursor is invalidated and the associated resources released. ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("javax.transaction.xa.Xid"), BeanInfoHelper.encodeEntities("JMSMessageCursorRuntimeMBean")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("forceCommit", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("xid", "An xid in string representation for a JMS transaction branch. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Causes the work associated with the specified transaction branch to be committed. ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerRuntimeMBean.class.getMethod("forceRollback", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("xid", "An xid in string representation for a JMS transaction branch. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Causes the work associated with the specified transaction branch to be rolled back. ");
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
