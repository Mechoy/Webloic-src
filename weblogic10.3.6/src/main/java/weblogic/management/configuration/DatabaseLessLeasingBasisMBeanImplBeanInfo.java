package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class DatabaseLessLeasingBasisMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DatabaseLessLeasingBasisMBean.class;

   public DatabaseLessLeasingBasisMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DatabaseLessLeasingBasisMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DatabaseLessLeasingBasisMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("DatabaseLessLeasingBasisMBean defines attributes related to the functioning of singleton services and server migration without the use of a HA database. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.DatabaseLessLeasingBasisMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("FenceTimeout")) {
         var3 = "getFenceTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFenceTimeout";
         }

         var2 = new PropertyDescriptor("FenceTimeout", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("FenceTimeout", var2);
         var2.setValue("description", "The timeout to wait and retry getting the server state when the NodeManager is unreachable. We try once more after waiting for the fence timeout period to make sure that the machine is really unavailable as opposed to heavy disk swapping. ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LeaderHeartbeatPeriod")) {
         var3 = "getLeaderHeartbeatPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLeaderHeartbeatPeriod";
         }

         var2 = new PropertyDescriptor("LeaderHeartbeatPeriod", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("LeaderHeartbeatPeriod", var2);
         var2.setValue("description", "Gets the LeaderHeartbeatPeriod value. The cluster leader heartbeats a special leader heartbeat every period seconds to publish group view version and other cluster information. Members look at this heartbeat and perform any sync up operations if required. ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MemberDiscoveryTimeout")) {
         var3 = "getMemberDiscoveryTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemberDiscoveryTimeout";
         }

         var2 = new PropertyDescriptor("MemberDiscoveryTimeout", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("MemberDiscoveryTimeout", var2);
         var2.setValue("description", "Gets the MemberDiscoveryTimeout value. This value defines the amount of time a server waits during or after startup to discover members that belong to the same cluster. This information is used to join or form a new cluster. ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setMemberDiscoveryTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MessageDeliveryTimeout")) {
         var3 = "getMessageDeliveryTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageDeliveryTimeout";
         }

         var2 = new PropertyDescriptor("MessageDeliveryTimeout", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("MessageDeliveryTimeout", var2);
         var2.setValue("description", "Gets the message delivery timeout value. This is the amount of time a server waits to get a response from the remote peer before taking recovery actions. ");
         setPropertyDescriptorDefault(var2, new Integer(5000));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("NodeManagerTimeoutMillis")) {
         var3 = "getNodeManagerTimeoutMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNodeManagerTimeoutMillis";
         }

         var2 = new PropertyDescriptor("NodeManagerTimeoutMillis", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("NodeManagerTimeoutMillis", var2);
         var2.setValue("description", "<p> NodeManager timeout. Amount of time to wait for a response from NodeManager. NodeManager is used to get server state and decide if a server is really dead. Note that the server automatically calculates a timeout value and this mbean attribute is used as a upper bound. </p> <p> The NodeManager timeout is dependent on the type of the NodeManager used. For SSH nodemanager, the timeout is on the larger side due to the nature of the SSH connection establishment. The default timeout value might appear very large for some installations. Please set the timeout to a value that is representative of SSH performance in your environment. </p> <p> Note that if the NodeManager does not respond back within the timeout period, the machine is considered 'dead'. One retry attempt is provided by the server if the NodeManager timesout. On timeout, the server will wait for a FenceTimeout period and retry connecting to the NodeManager. If that call times out as well, the machine is deemed unavailable and taken out of the cluster view. </p> <p> A value of 0 means that the timeout will not be applied at all. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(180000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PeriodicSRMCheckEnabled")) {
         var3 = "isPeriodicSRMCheckEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPeriodicSRMCheckEnabled";
         }

         var2 = new PropertyDescriptor("PeriodicSRMCheckEnabled", DatabaseLessLeasingBasisMBean.class, var3, var4);
         var1.put("PeriodicSRMCheckEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the cluster leader needs to periodically check if it is still in the right network partition using NodeManager state query. By default the cluster leader or the seniormost member ensures that it is in the right partition by periodically checking with all NodeManagers. This is used to cover a case where the cluster leader was elected with most of the servers shutdown but subsequently there is a network partition with the leader in the minority partition. This is just done on the cluster leader instance and not on other instances.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
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
