package weblogic.wsee.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseeWsrmRuntimeMBean;

public class WseeWsrmRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeWsrmRuntimeMBean.class;

   public WseeWsrmRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeWsrmRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeWsrmRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Encapsulates runtime information about WS-RM functionality. If this MBean is parented by a WseePortRuntimeMBean instance, this MBean represents WS-RM resources contained in a particular Web Service or web service client. If this MBean is parented by ServerRuntimeMBean, this MBean represents WS-RM resources for the entire server/VM (spanning across applications, web services, clients, etc.)</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeWsrmRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("SequenceIds")) {
         String var3 = "getSequenceIds";
         Object var4 = null;
         var2 = new PropertyDescriptor("SequenceIds", WseeWsrmRuntimeMBean.class, var3, (String)var4);
         var1.put("SequenceIds", var2);
         var2.setValue("description", "A list of sequence IDs representing active sequences being managed on the WS-RM destination (receiving side). ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
      Method var3 = WseeWsrmRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WseeWsrmRuntimeMBean.class.getMethod("getSequenceInfo", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Given a sequence ID from the sequence ID list, get a data object representing current information for that sequence. The returned CompositeData item is patterned after the WsrmSequenceInfo interface. <br> We give a summary of the structure of WsrmSequenceInfo here. For more details consult the JavaDoc for WsrmSequenceInfo and WsrmRequestInfo <pre> <ul> <li>String id</li> <li>boolean source</li> <li>String destinationId</li> <li>boolean offer</li> <li>mainSequenceId</li> <li>String state <ul> <li>values are: <ul> <li>\"NEW\"</li> <li>\"CREATING\"</li> <li>\"CREATED\"</li> <li>\"LAST_MESSAGE_PENDING\"</li> <li>\"LAST_MESSAGE\"</li> <li>\"CLOSING\"</li> <li>\"CLOSED\"</li> <li>\"TERMINATING\"</li> <li>\"TERMINATED\"</li> </ul> </li> </ul> <li>long creationTime</li> <li>long lastActivityTime</li> <li>long maxAge</li> <li>long lastAckdMessageNum</li> <li>long unackdCount</li> <li>WsrmRequestInfo[] requests <ul> <li>where WsrmRequestInfo is <ul> <li>String messageId</li> <li>long seqNum</li> <li>String soapAction</li> <li>long timestamp</li> <li>boolean ackFlag</li> <li>String responseMessageId</li> <li>long responseTimestamp</li> </ul> </li> </ul> </li> </ul> </pre> ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
