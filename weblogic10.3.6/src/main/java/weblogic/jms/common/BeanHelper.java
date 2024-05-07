package weblogic.jms.common;

import java.util.HashMap;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.management.ManagementException;
import weblogic.management.utils.GenericBeanListener;

public abstract class BeanHelper {
   public static final HashMap connectionFactoryBeanSignatures = new HashMap();
   public static final HashMap defaultDeliveryParamsBeanSignatures = new HashMap();
   public static final HashMap clientParamsBeanSignatures = new HashMap();
   public static final HashMap transactionParamsBeanSignatures = new HashMap();
   public static final HashMap flowControlParamsBeanSignatures = new HashMap();
   public static final HashMap loadBalancingParamsBeanSignatures = new HashMap();
   public static final HashMap securityParamsBeanSignatures = new HashMap();

   public static void copyConnectionFactory(JMSConnectionFactoryBean var0, JMSConnectionFactoryBean var1) throws ManagementException {
      GenericBeanListener var2 = new GenericBeanListener((DescriptorBean)var1, var0, connectionFactoryBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getDefaultDeliveryParams(), var0.getDefaultDeliveryParams(), defaultDeliveryParamsBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getClientParams(), var0.getClientParams(), clientParamsBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getTransactionParams(), var0.getTransactionParams(), transactionParamsBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getFlowControlParams(), var0.getFlowControlParams(), flowControlParamsBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getLoadBalancingParams(), var0.getLoadBalancingParams(), loadBalancingParamsBeanSignatures);
      var2.initialize(false);
      var2 = new GenericBeanListener((DescriptorBean)var1.getSecurityParams(), var0.getSecurityParams(), securityParamsBeanSignatures);
      var2.initialize(false);
   }

   static {
      connectionFactoryBeanSignatures.put("JNDIName", String.class);
      connectionFactoryBeanSignatures.put("LocalJNDIName", String.class);
      connectionFactoryBeanSignatures.put("DefaultTargetingEnabled", Boolean.TYPE);
      defaultDeliveryParamsBeanSignatures.put("DefaultDeliveryMode", String.class);
      defaultDeliveryParamsBeanSignatures.put("DefaultTimeToDeliver", String.class);
      defaultDeliveryParamsBeanSignatures.put("DefaultTimeToLive", Long.TYPE);
      defaultDeliveryParamsBeanSignatures.put("DefaultPriority", Integer.TYPE);
      defaultDeliveryParamsBeanSignatures.put("DefaultRedeliveryDelay", Long.TYPE);
      defaultDeliveryParamsBeanSignatures.put("SendTimeout", Long.TYPE);
      defaultDeliveryParamsBeanSignatures.put("DefaultCompressionThreshold", Integer.TYPE);
      defaultDeliveryParamsBeanSignatures.put("DefaultUnitOfOrder", String.class);
      clientParamsBeanSignatures.put("ClientId", String.class);
      clientParamsBeanSignatures.put("ClientIdPolicy", String.class);
      clientParamsBeanSignatures.put("SubscriptionSharingPolicy", String.class);
      clientParamsBeanSignatures.put("AcknowledgePolicy", String.class);
      clientParamsBeanSignatures.put("AllowCloseInOnMessage", Boolean.TYPE);
      clientParamsBeanSignatures.put("MessagesMaximum", Integer.TYPE);
      clientParamsBeanSignatures.put("MulticastOverrunPolicy", String.class);
      clientParamsBeanSignatures.put("SynchronousPrefetchMode", String.class);
      clientParamsBeanSignatures.put("ReconnectPolicy", String.class);
      clientParamsBeanSignatures.put("ReconnectBlockingMillis", Long.TYPE);
      clientParamsBeanSignatures.put("TotalReconnectPeriodMillis", Long.TYPE);
      transactionParamsBeanSignatures.put("TransactionTimeout", Long.TYPE);
      transactionParamsBeanSignatures.put("XAConnectionFactoryEnabled", Boolean.TYPE);
      flowControlParamsBeanSignatures.put("FlowMinimum", Integer.TYPE);
      flowControlParamsBeanSignatures.put("FlowMaximum", Integer.TYPE);
      flowControlParamsBeanSignatures.put("FlowInterval", Integer.TYPE);
      flowControlParamsBeanSignatures.put("FlowSteps", Integer.TYPE);
      flowControlParamsBeanSignatures.put("FlowControlEnabled", Boolean.TYPE);
      flowControlParamsBeanSignatures.put("OneWaySendMode", String.class);
      flowControlParamsBeanSignatures.put("OneWaySendWindowSize", Integer.TYPE);
      loadBalancingParamsBeanSignatures.put("LoadBalancingEnabled", Boolean.TYPE);
      loadBalancingParamsBeanSignatures.put("ServerAffinityEnabled", Boolean.TYPE);
      securityParamsBeanSignatures.put("AttachJMSXUserId", Boolean.TYPE);
   }
}
