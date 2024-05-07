package weblogic.jms.backend;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSMessageManagementRuntimeMBean;

public class BEMessageManagementRuntimeDelegateBeanInfo extends JMSMessageCursorRuntimeImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSMessageManagementRuntimeMBean.class;

   public BEMessageManagementRuntimeDelegateBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BEMessageManagementRuntimeDelegateBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BEMessageManagementRuntimeDelegate.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.jms.backend");
      String var3 = (new String("This interface defines the common management functionality for both Queues and Topics  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSMessageManagementRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("MessagesDeletedCurrentCount")) {
         var3 = "getMessagesDeletedCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesDeletedCurrentCount", JMSMessageManagementRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesDeletedCurrentCount", var2);
         var2.setValue("description", "<p>Returns the number of messages that have been deleted from the destination.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#deleteMessages(String)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("MessagesMovedCurrentCount")) {
         var3 = "getMessagesMovedCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesMovedCurrentCount", JMSMessageManagementRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesMovedCurrentCount", var2);
         var2.setValue("description", "<p>Returns the number of messages that have been moved from the destination.</p> ");
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
      Method var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getCursorStartPosition", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor start position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getMessage", String.class, String.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("preDeregister");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("sort", String.class, Long.class, String[].class, Boolean[].class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getCursorEndPosition", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the cursor end position in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getMessage", String.class, Long.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getItems", String.class, Long.class, Integer.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getMessages", String.class, Integer.class, Integer.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getNext", String.class, Integer.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getMessage", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messageID", "The JMS message ID of the requested message. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Given a JMS message ID this method returns the corresponding message from the queue. If no message with the specified message ID exists on the destination, a null value is returned.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getPrevious", String.class, Integer.class);
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

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("getCursorSize", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the number of items in the result set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("moveMessages", String.class, CompositeData.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A JMS message selector that identifies the messages  to move. "), createParameterDescriptor("targetDestination", "A JMS destination that the messages will  be moved to. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Moves the set of messages that match the specified selector to the target destination. The move operation is guaranteed to be atomic for the selected messages.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("closeCursor", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursorHandle", "The cursor handle. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Releases the server-side resources associated with the cursor and removes the runtime MBean instance.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("moveMessages", String.class, CompositeData.class, Integer.class);
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("deleteMessages", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("selector", "A JMS message selector to identify which messages to  delete. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the set of messages from the destination that are qualified by the specified JMS message selector.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSMessageManagementRuntimeMBean.class.getMethod("importMessages", CompositeData[].class, Boolean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messages", "An array of messages in CompositeData representation to be imported. "), createParameterDescriptor("replaceOnly", "When set to true an excetion will be thrown if the message ID does not exist on the target destination. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Imports an array of messages into the destination. If the message ID of the message being imported matches a message already on the destination, then the existing message will be replaced. If an existing message does not exist, then the message will be produced on the destination. A produced message is subject to quota limitations.</p> ");
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
