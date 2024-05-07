package weblogic.jms.module;

import java.security.AccessController;
import java.util.HashMap;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.GroupParamsBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public abstract class JMSBeanHelper extends JMSModuleHelper {
   public static final String INTEROP_APPLICATION_NAME = "interop-jms";
   private static final String DECORATION_SEPARATOR = "!";
   public static final HashMap destinationBeanSignatures = new HashMap();
   public static final HashMap destinationKeyBeanSignatures = new HashMap();
   public static final HashMap thresholdBeanSignatures = new HashMap();
   public static final HashMap deliveryOverridesSignatures = new HashMap();
   public static final HashMap deliveryFailureSignatures = new HashMap();
   public static final HashMap messageLoggingSignatures = new HashMap();
   public static final HashMap templateBeanSignatures = new HashMap();
   public static final HashMap multicastBeanSignatures = new HashMap();
   public static final HashMap distributedTopicBeanSignatures = new HashMap();
   public static final HashMap distributedTopicAdditionSignatures = new HashMap();
   public static final HashMap distributedQueueBeanSignatures = new HashMap();
   public static final HashMap distributedQueueAdditionSignatures = new HashMap();
   public static final HashMap uniformDistributedTopicBeanSignatures = new HashMap();
   public static final HashMap uniformDistributedQueueBeanSignatures = new HashMap();
   public static final HashMap localDestinationBeanSignatures = new HashMap();
   public static final HashMap localDeliveryFailureSignatures = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String DEFAULT_APPENDIX = "-jms.xml";

   public static void copyTemplateBean(TemplateBean var0, JMSBean var1, TemplateBean var2) throws ManagementException {
      GenericBeanListener var3 = new GenericBeanListener((DescriptorBean)var2, var0, templateBeanSignatures, false);
      var3.initialize(false);
      var3 = new GenericBeanListener((DescriptorBean)var2.getThresholds(), var0.getThresholds(), thresholdBeanSignatures, false);
      var3.initialize(false);
      var3 = new GenericBeanListener((DescriptorBean)var2.getDeliveryParamsOverrides(), var0.getDeliveryParamsOverrides(), deliveryOverridesSignatures, false);
      var3.initialize(false);
      var3 = new GenericBeanListener((DescriptorBean)var2.getDeliveryFailureParams(), var0.getDeliveryFailureParams(), deliveryFailureSignatures, false);
      var3.initialize(false);
      var3 = new GenericBeanListener((DescriptorBean)var2.getMulticast(), var0.getMulticast(), multicastBeanSignatures, false);
      var3.initialize(false);
      var3 = new GenericBeanListener((DescriptorBean)var2.getMessageLoggingParams(), var0.getMessageLoggingParams(), messageLoggingSignatures, false);
      var3.initialize(false);
      if (var2.isSet("Quota")) {
         QuotaBean var4 = var2.getQuota();
         if (var4 == null) {
            var0.setQuota((QuotaBean)null);
         } else {
            var0.setQuota(var1.lookupQuota(var4.getName()));
         }
      }

      DeliveryFailureParamsBean var9 = var2.getDeliveryFailureParams();
      if (var9.isSet("ErrorDestination")) {
         DeliveryFailureParamsBean var5 = var0.getDeliveryFailureParams();
         DestinationBean var6 = var9.getErrorDestination();
         if (var6 == null) {
            var5.setErrorDestination((DestinationBean)null);
         } else if (var6 instanceof QueueBean) {
            var5.setErrorDestination(var1.lookupQueue(var6.getName()));
         } else {
            var5.setErrorDestination(var1.lookupTopic(var6.getName()));
         }
      }

      GroupParamsBean[] var10 = var2.getGroupParams();
      if (var10 != null) {
         for(int var11 = 0; var11 < var10.length; ++var11) {
            GroupParamsBean var7 = var10[var11];
            GroupParamsBean var8 = var0.createGroupParams(var7.getSubDeploymentName());
            var8.setErrorDestination(var7.getErrorDestination());
         }
      }

   }

   public static void copyDestinationKeyBean(JMSBean var0, JMSBean var1, String var2) throws ManagementException {
      var0.createDestinationKey(var2);
      DestinationKeyBean var3 = var1.lookupDestinationKey(var2);
      DestinationKeyBean var4 = var0.lookupDestinationKey(var2);
      GenericBeanListener var5 = new GenericBeanListener((DescriptorBean)var3, var4, destinationKeyBeanSignatures, false);
      var5.initialize(false);
   }

   public static JMSInteropModuleMBean getJMSInteropModule(DomainMBean var0) {
      if (var0 == null) {
         return null;
      } else {
         JMSInteropModuleMBean[] var1 = var0.getJMSInteropModules();
         return var1.length <= 0 ? null : var1[0];
      }
   }

   public static JMSInteropModuleMBean getJMSInteropModule() {
      return getJMSInteropModule(ManagementService.getRuntimeAccess(kernelId).getDomain());
   }

   public static JMSBean getInteropJMSBean(DomainMBean var0) {
      if (var0 == null) {
         return null;
      } else {
         JMSInteropModuleMBean var1 = getJMSInteropModule(var0);
         return var1 == null ? null : var1.getJMSResource();
      }
   }

   public static JMSBean getInteropJMSBean() {
      return getInteropJMSBean(ManagementService.getRuntimeAccess(kernelId).getDomain());
   }

   public static JMSSystemResourceMBean addInteropApplication(DomainMBean var0) {
      JMSInteropModuleMBean var1 = getJMSInteropModule(var0);
      if (var1 == null) {
         var1 = var0.createJMSInteropModule("interop-jms");
      }

      return var1;
   }

   public static JMSSystemResourceMBean addInteropApplication() {
      return addInteropApplication(ManagementService.getRuntimeAccess(kernelId).getDomain());
   }

   public static SubDeploymentMBean findSubDeployment(String var0, BasicDeploymentMBean var1) {
      if (var1 != null && var0 != null) {
         SubDeploymentMBean[] var2 = var1.getSubDeployments();
         if (var2 == null) {
            return null;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               SubDeploymentMBean var4 = var2[var3];
               if (var0.equals(var4.getName())) {
                  return var4;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static String getDecoratedName(String var0, String var1) {
      return var0 != null && !"interop-jms".equals(var0) ? var0 + "!" + var1 : var1;
   }

   public static TargetMBean[] getSubDeploymentTargets(String var0, BasicDeploymentMBean var1) {
      SubDeploymentMBean[] var2 = var1.getSubDeployments();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var0.equals(var2[var3].getName())) {
            return var2[var3].getTargets();
         }
      }

      return new TargetMBean[0];
   }

   public static DomainMBean getDomain(WebLogicMBean var0) throws IllegalArgumentException {
      DescriptorBean var1 = (DescriptorBean)var0;
      DescriptorBean var2 = var1.getDescriptor().getRootBean();
      if (!(var2 instanceof DomainMBean)) {
         throw new IllegalArgumentException("could not get DomainMbean from " + var0.getName() + ".  My root has type " + var2.getClass().getName());
      } else {
         return (DomainMBean)var2;
      }
   }

   public static String constructDefaultJMSSystemFilename(String var0) {
      String var1 = var0.toLowerCase().trim();
      if (var1.endsWith("-jms")) {
         var1 = var1.substring(0, var1.length() - 4);
      }

      String var2 = "jms/" + FileUtils.mapNameToFileName(var1) + "-jms.xml";
      return var2;
   }

   public static void destroyDestination(JMSBean var0, DestinationBean var1) {
      if (var1 instanceof QueueBean) {
         var0.destroyQueue((QueueBean)var1);
      } else {
         var0.destroyTopic((TopicBean)var1);
      }

   }

   static {
      destinationBeanSignatures.put("DestinationKeys", String[].class);
      destinationBeanSignatures.put("AttachSender", String.class);
      destinationBeanSignatures.put("ProductionPausedAtStartup", Boolean.TYPE);
      destinationBeanSignatures.put("InsertionPausedAtStartup", Boolean.TYPE);
      destinationBeanSignatures.put("ConsumptionPausedAtStartup", Boolean.TYPE);
      destinationBeanSignatures.put("MaximumMessageSize", Integer.TYPE);
      destinationBeanSignatures.put("MessagingPerformancePreference", Integer.TYPE);
      destinationBeanSignatures.put("JNDIName", String.class);
      destinationBeanSignatures.put("LocalJNDIName", String.class);
      destinationBeanSignatures.put("JMSCreateDestinationIdentifier", String.class);
      destinationBeanSignatures.put("DefaultUnitOfOrder", Boolean.TYPE);
      destinationBeanSignatures.put("SAFExportPolicy", String.class);
      destinationBeanSignatures.put("UnitOfWorkHandlingPolicy", String.class);
      destinationBeanSignatures.put("IncompleteWorkExpirationTime", Integer.TYPE);
      destinationBeanSignatures.put("DefaultTargetingEnabled", Boolean.TYPE);
      destinationKeyBeanSignatures.put("Property", String.class);
      destinationKeyBeanSignatures.put("KeyType", String.class);
      destinationKeyBeanSignatures.put("SortOrder", String.class);
      thresholdBeanSignatures.put("BytesHigh", Long.TYPE);
      thresholdBeanSignatures.put("BytesLow", Long.TYPE);
      thresholdBeanSignatures.put("MessagesHigh", Long.TYPE);
      thresholdBeanSignatures.put("MessagesLow", Long.TYPE);
      deliveryOverridesSignatures.put("DeliveryMode", String.class);
      deliveryOverridesSignatures.put("TimeToDeliver", String.class);
      deliveryOverridesSignatures.put("TimeToLive", Long.TYPE);
      deliveryOverridesSignatures.put("Priority", Integer.TYPE);
      deliveryOverridesSignatures.put("RedeliveryDelay", Long.TYPE);
      deliveryFailureSignatures.put("RedeliveryLimit", Integer.TYPE);
      deliveryFailureSignatures.put("ExpirationPolicy", String.class);
      deliveryFailureSignatures.put("ExpirationLoggingPolicy", String.class);
      messageLoggingSignatures.put("MessageLoggingFormat", String.class);
      messageLoggingSignatures.put("MessageLoggingEnabled", Boolean.TYPE);
      templateBeanSignatures.put("DestinationKeys", String[].class);
      templateBeanSignatures.put("AttachSender", String.class);
      templateBeanSignatures.put("ProductionPausedAtStartup", Boolean.TYPE);
      templateBeanSignatures.put("InsertionPausedAtStartup", Boolean.TYPE);
      templateBeanSignatures.put("ConsumptionPausedAtStartup", Boolean.TYPE);
      templateBeanSignatures.put("MaximumMessageSize", Integer.TYPE);
      templateBeanSignatures.put("MessagingPerformancePreference", Integer.TYPE);
      multicastBeanSignatures.put("MulticastAddress", String.class);
      multicastBeanSignatures.put("MulticastPort", Integer.TYPE);
      multicastBeanSignatures.put("MulticastTimeToLive", Integer.TYPE);
      distributedTopicBeanSignatures.put("JNDIName", String.class);
      distributedTopicBeanSignatures.put("LoadBalancingPolicy", String.class);
      distributedTopicBeanSignatures.put("UnitOfOrderRouting", String.class);
      distributedTopicBeanSignatures.put("SAFExportPolicy", String.class);
      distributedTopicAdditionSignatures.put("DistributedTopicMembers", DistributedDestinationMemberBean.class);
      distributedQueueBeanSignatures.put("JNDIName", String.class);
      distributedQueueBeanSignatures.put("LoadBalancingPolicy", String.class);
      distributedQueueBeanSignatures.put("UnitOfOrderRouting", String.class);
      distributedQueueBeanSignatures.put("ForwardDelay", Integer.TYPE);
      distributedQueueBeanSignatures.put("SAFExportPolicy", String.class);
      distributedQueueAdditionSignatures.put("DistributedQueueMembers", DistributedDestinationMemberBean.class);
      uniformDistributedTopicBeanSignatures.put("JNDIName", String.class);
      uniformDistributedTopicBeanSignatures.put("LoadBalancingPolicy", String.class);
      uniformDistributedTopicBeanSignatures.put("ForwardingPolicy", String.class);
      uniformDistributedTopicBeanSignatures.put("UnitOfOrderRouting", String.class);
      uniformDistributedTopicBeanSignatures.put("SAFExportPolicy", String.class);
      uniformDistributedQueueBeanSignatures.put("JNDIName", String.class);
      uniformDistributedQueueBeanSignatures.put("LoadBalancingPolicy", String.class);
      uniformDistributedQueueBeanSignatures.put("UnitOfOrderRouting", String.class);
      uniformDistributedQueueBeanSignatures.put("ForwardDelay", Integer.TYPE);
      uniformDistributedQueueBeanSignatures.put("SAFExportPolicy", String.class);
      localDestinationBeanSignatures.put("Quota", QuotaBean.class);
      localDeliveryFailureSignatures.put("ErrorDestination", DestinationBean.class);
   }
}
