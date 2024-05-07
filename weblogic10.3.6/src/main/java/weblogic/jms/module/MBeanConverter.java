package weblogic.jms.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.ClientParamsBean;
import weblogic.j2ee.descriptor.wl.DefaultDeliveryParamsBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.FlowControlParamsBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.LoadBalancingParamsBean;
import weblogic.j2ee.descriptor.wl.PropertyBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.TransactionParamsBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.management.DistributedManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSConnectionFactoryMBean;
import weblogic.management.configuration.ForeignJMSDestinationMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.ForeignJNDIObjectMBean;
import weblogic.management.configuration.JMSConnectionFactoryMBean;
import weblogic.management.configuration.JMSDestinationKeyMBean;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.configuration.JMSDistributedDestinationMBean;
import weblogic.management.configuration.JMSDistributedDestinationMemberMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedQueueMemberMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.UpdateException;

public abstract class MBeanConverter {
   private static final int STORE_DEFAULT = 0;
   private static final int STORE_TRUE = 1;
   private static final int STORE_FALSE = 2;
   private static final int DMO_UNSET = 0;
   private static final int DMO_PERSISTENT = 1;
   private static final int DMO_NONPERSISTENT = 2;

   public static JMSConnectionFactoryBean addJMSConnectionFactory(JMSBean var0, JMSSystemResourceMBean var1, JMSConnectionFactoryMBean var2, String var3, TargetMBean[] var4) {
      JMSConnectionFactoryBean var5;
      if ((var5 = var0.lookupConnectionFactory(var3)) != null) {
         return var5;
      } else {
         var5 = var0.createConnectionFactory(var3);
         fillInJMSConnectionFactory(var1, var5, var2, var4);
         return var5;
      }
   }

   private static void fillInJMSConnectionFactory(JMSSystemResourceMBean var0, JMSConnectionFactoryBean var1, JMSConnectionFactoryMBean var2, TargetMBean[] var3) {
      if (var2.getNotes() != null) {
         var1.setNotes(var2.getNotes());
      }

      String var4 = var2.getJNDIName();
      if (var4 != null) {
         var1.setJNDIName(var4);
      }

      if (var0 != null) {
         SubDeploymentMBean var5;
         if ((var5 = var0.lookupSubDeployment(var1.getName())) == null) {
            var5 = var0.createSubDeployment(var1.getName());
         }

         try {
            var5.setTargets(var3);
         } catch (InvalidAttributeValueException var7) {
            throw new AssertionError("ERROR: Could not set the targets of JMS connection factory " + var1.getName() + " due to " + var7);
         } catch (DistributedManagementException var8) {
            throw new AssertionError("WARN: Could not set the targets of JMS connection factory " + var1.getName() + " due to " + var8);
         }
      }

      fillInDefaultDeliveryParams(var1, var2);
      fillInClientParams(var1, var2);
      fillInTransactionParams(var1, var2);
      fillInFlowControlParams(var1, var2);
      fillInLoadBalancingParams(var1, var2);
   }

   private static void fillInDefaultDeliveryParams(JMSConnectionFactoryBean var0, JMSConnectionFactoryMBean var1) {
      DefaultDeliveryParamsBean var2 = var0.getDefaultDeliveryParams();
      String var3 = var1.getDefaultDeliveryMode();
      if (var3 != null && !var3.equals("Persistent")) {
         var2.setDefaultDeliveryMode(var3);
      }

      int var4 = var1.getDefaultPriority();
      if (var4 != 4) {
         var2.setDefaultPriority(var4);
      }

      long var5 = var1.getDefaultTimeToDeliver();
      if (var5 != 0L) {
         var2.setDefaultTimeToDeliver((new Long(var5)).toString());
      }

      if ((var5 = var1.getDefaultTimeToLive()) != 0L) {
         var2.setDefaultTimeToLive(var5);
      }

      if ((var5 = var1.getSendTimeout()) != 10L) {
         var2.setSendTimeout(var5);
      }

      if ((var5 = var1.getDefaultRedeliveryDelay()) != 0L) {
         var2.setDefaultRedeliveryDelay(var5);
      }

   }

   private static void fillInClientParams(JMSConnectionFactoryBean var0, JMSConnectionFactoryMBean var1) {
      ClientParamsBean var2 = var0.getClientParams();
      String var3 = var1.getClientId();
      if (var3 != null) {
         var2.setClientId(var3);
      }

      int var4 = var1.getMessagesMaximum();
      if (var4 != 10) {
         var2.setMessagesMaximum(var4);
      }

      var3 = var1.getOverrunPolicy();
      if (var3 != null && !var3.equals("KeepOld")) {
         var2.setMulticastOverrunPolicy(var3);
      }

      var3 = var1.getAcknowledgePolicy();
      if (var3 != null && !var3.equals("All")) {
         var2.setAcknowledgePolicy(var3);
      }

      boolean var5 = var1.getAllowCloseInOnMessage();
      if (var5) {
         var2.setAllowCloseInOnMessage(var5);
      }

   }

   private static void fillInTransactionParams(JMSConnectionFactoryBean var0, JMSConnectionFactoryMBean var1) {
      TransactionParamsBean var2 = var0.getTransactionParams();
      long var3 = var1.getTransactionTimeout();
      if (var3 != 3600L) {
         var2.setTransactionTimeout(var3);
      }

      boolean var5 = var1.isXAConnectionFactoryEnabled();
      if (var5) {
         var2.setXAConnectionFactoryEnabled(var5);
      }

      var5 = var1.isUserTransactionsEnabled();
      var2.setXAConnectionFactoryEnabled(var2.isXAConnectionFactoryEnabled() || var5);
      var5 = var1.isXAServerEnabled();
      var2.setXAConnectionFactoryEnabled(var2.isXAConnectionFactoryEnabled() || var5);
   }

   private static void fillInFlowControlParams(JMSConnectionFactoryBean var0, JMSConnectionFactoryMBean var1) {
      FlowControlParamsBean var2 = var0.getFlowControlParams();
      boolean var3 = var1.isFlowControlEnabled();
      if (!var3) {
         var2.setFlowControlEnabled(var3);
      }

      int var4 = var1.getFlowMinimum();
      if (var4 != 50) {
         var2.setFlowMinimum(var4);
      }

      if ((var4 = var1.getFlowMaximum()) != 500) {
         var2.setFlowMaximum(var4);
      }

      if ((var4 = var1.getFlowInterval()) != 60) {
         var2.setFlowInterval(var4);
      }

      if ((var4 = var1.getFlowSteps()) != 10) {
         var2.setFlowSteps(var4);
      }

   }

   private static void fillInLoadBalancingParams(JMSConnectionFactoryBean var0, JMSConnectionFactoryMBean var1) {
      LoadBalancingParamsBean var2 = var0.getLoadBalancingParams();
      boolean var3 = var1.isLoadBalancingEnabled();
      if (!var3) {
         var2.setLoadBalancingEnabled(var3);
      }

      if (!(var3 = var1.isServerAffinityEnabled())) {
         var2.setServerAffinityEnabled(var3);
      }

   }

   public static ForeignServerBean addForeignJMSServer(JMSBean var0, ForeignJMSServerMBean var1) {
      return addForeignJMSServer(var0, (JMSSystemResourceMBean)null, var1, var1.getName(), var1.getTargets());
   }

   public static ForeignServerBean addForeignJMSServer(JMSBean var0, JMSSystemResourceMBean var1, ForeignJMSServerMBean var2, String var3, TargetMBean[] var4) {
      ForeignServerBean var5;
      if ((var5 = var0.lookupForeignServer(var3)) != null) {
         return var5;
      } else {
         var5 = var0.createForeignServer(var3);
         fillInForeignServer(var1, var5, var2, var4);
         return var5;
      }
   }

   private static void fillInForeignServer(JMSSystemResourceMBean var0, ForeignServerBean var1, ForeignJMSServerMBean var2, TargetMBean[] var3) {
      if (var2.getNotes() != null) {
         var1.setNotes(var2.getNotes());
      }

      if (var0 != null) {
         SubDeploymentMBean var4;
         if ((var4 = var0.lookupSubDeployment(var1.getName())) == null) {
            var4 = var0.createSubDeployment(var1.getName());
         }

         try {
            var4.setTargets(var3);
         } catch (InvalidAttributeValueException var9) {
            throw new AssertionError("ERROR: Could not set the targets of JMS foreign server " + var1.getName() + " due to " + var9);
         } catch (DistributedManagementException var10) {
            throw new AssertionError("WARN: Could not set the targets of JMS foreign server " + var1.getName() + " due to " + var10);
         }
      }

      String var11 = var2.getInitialContextFactory();
      if (var11 != null && !var11.equals("weblogic.jndi.WLInitialContextFactory")) {
         var1.setInitialContextFactory(var11);
      }

      if ((var11 = var2.getConnectionURL()) != null) {
         var1.setConnectionURL(var11);
      }

      Properties var5 = var2.getJNDIProperties();
      if (var5 != null) {
         Iterator var6 = var5.keySet().iterator();

         while(var6.hasNext()) {
            PropertyBean var7 = null;
            String var8 = (String)var6.next();
            var7 = var1.createJNDIProperty(var8);
            var7.setValue(var5.getProperty(var8));
         }
      }

      ForeignJMSDestinationMBean[] var12 = var2.getForeignJMSDestinations();

      for(int var13 = 0; var13 < var12.length; ++var13) {
         addForeignDestination(var1, var12[var13]);
      }

      ForeignJMSConnectionFactoryMBean[] var14 = var2.getForeignJMSConnectionFactories();

      for(int var15 = 0; var15 < var14.length; ++var15) {
         addForeignConnectionFactory(var1, var14[var15]);
      }

   }

   public static ForeignDestinationBean addForeignDestination(ForeignServerBean var0, ForeignJMSDestinationMBean var1) {
      ForeignDestinationBean var2;
      if ((var2 = var0.lookupForeignDestination(var1.getName())) != null) {
         return var2;
      } else {
         var2 = var0.createForeignDestination(var1.getName());
         fillInForeignDestination(var2, var1);
         return var2;
      }
   }

   private static void fillInForeignDestination(ForeignDestinationBean var0, ForeignJMSDestinationMBean var1) {
      fillInForeignJNDIObject(var0, var1);
   }

   public static ForeignConnectionFactoryBean addForeignConnectionFactory(ForeignServerBean var0, ForeignJMSConnectionFactoryMBean var1) {
      ForeignConnectionFactoryBean var2;
      if ((var2 = var0.lookupForeignConnectionFactory(var1.getName())) != null) {
         return var2;
      } else {
         var2 = var0.createForeignConnectionFactory(var1.getName());
         fillInForeignConnectionFactory(var2, var1);
         return var2;
      }
   }

   private static void fillInForeignConnectionFactory(ForeignConnectionFactoryBean var0, ForeignJMSConnectionFactoryMBean var1) {
      fillInForeignJNDIObject(var0, var1);
      String var2 = var1.getUsername();
      if (var2 != null) {
         var0.setUsername(var2);
      }

      if ((var2 = var1.getPassword()) != null) {
         var0.setPassword(var2);
      }

   }

   private static void fillInForeignJNDIObject(ForeignJNDIObjectBean var0, ForeignJNDIObjectMBean var1) {
      if (var1.getNotes() != null) {
         var0.setNotes(var1.getNotes());
      }

      var0.setLocalJNDIName(var1.getLocalJNDIName());
      var0.setRemoteJNDIName(var1.getRemoteJNDIName());
   }

   public static DistributedQueueBean addDistributedQueue(JMSBean var0, JMSSystemResourceMBean var1, JMSDistributedQueueMBean var2) throws UpdateException {
      DistributedQueueBean var3;
      if ((var3 = var0.lookupDistributedQueue(var2.getName())) != null) {
         return var3;
      } else {
         var3 = var0.createDistributedQueue(var2.getName());
         fillInDistributedDestination(var1, var3, var2);
         fillInDistributedQueue(var0, var3, var2);
         return var3;
      }
   }

   public static DistributedTopicBean addDistributedTopic(JMSBean var0, JMSSystemResourceMBean var1, JMSDistributedTopicMBean var2) throws UpdateException {
      DistributedTopicBean var3;
      if ((var3 = var0.lookupDistributedTopic(var2.getName())) != null) {
         return var3;
      } else {
         var3 = var0.createDistributedTopic(var2.getName());
         fillInDistributedDestination(var1, var3, var2);
         fillInDistributedTopic(var0, var3, var2);
         return var3;
      }
   }

   public static DistributedDestinationMemberBean addDistributedQueueMember(JMSBean var0, DistributedQueueBean var1, JMSDistributedQueueMemberMBean var2) throws UpdateException {
      DistributedDestinationMemberBean var3;
      if ((var3 = var1.lookupDistributedQueueMember(var2.getName())) != null) {
         return var3;
      } else {
         var3 = var1.createDistributedQueueMember(var2.getName());
         fillInDistributedDestinationMember(var3, var2);
         fillInDistributedQueueMember(var0, var3, var2);
         return var3;
      }
   }

   public static DistributedDestinationMemberBean addDistributedTopicMember(JMSBean var0, DistributedTopicBean var1, JMSDistributedTopicMemberMBean var2) throws UpdateException {
      DistributedDestinationMemberBean var3;
      if ((var3 = var1.lookupDistributedTopicMember(var2.getName())) != null) {
         return var3;
      } else {
         var3 = var1.createDistributedTopicMember(var2.getName());
         fillInDistributedDestinationMember(var3, var2);
         fillInDistributedTopicMember(var0, var3, var2);
         return var3;
      }
   }

   private static void fillInDistributedDestination(JMSSystemResourceMBean var0, DistributedDestinationBean var1, JMSDistributedDestinationMBean var2) {
      if (var2.getNotes() != null) {
         var1.setNotes(var2.getNotes());
      }

      String var3 = var2.getJNDIName();
      if (var3 != null && var3.length() > 0) {
         var1.setJNDIName(var3);
      }

      String var4 = var2.getLoadBalancingPolicy();
      if (var4 != null && !var4.equals("Round-Robin")) {
         var1.setLoadBalancingPolicy(var4);
      }

      JMSTemplateMBean var5 = var2.getJMSTemplate();
      if (var5 != null) {
         JMSLogger.logTemplateOnDDNotSupported(var2.getName(), var5.getName());
      }

      var5 = var2.getTemplate();
      if (var5 != null) {
         JMSLogger.logTemplateOnDDNotSupported(var2.getName(), var5.getName());
      }

      if (var0 != null) {
         String var6 = var1.getName();
         SubDeploymentMBean var7;
         if ((var7 = var0.lookupSubDeployment(var6)) == null) {
            var7 = var0.createSubDeployment(var6);
         }

         try {
            var7.setTargets(var2.getTargets());
         } catch (InvalidAttributeValueException var9) {
            throw new AssertionError("ERROR: Could not set the targets of JMS distributed destination " + var1.getName() + " due to " + var9);
         } catch (DistributedManagementException var10) {
            throw new AssertionError("ERROR: Could not set the targets of JMS distributed destination " + var1.getName() + " due to" + var10);
         }
      }

   }

   private static void fillInDistributedQueue(JMSBean var0, DistributedQueueBean var1, JMSDistributedQueueMBean var2) throws UpdateException {
      JMSDistributedQueueMemberMBean[] var3 = var2.getJMSDistributedQueueMembers();
      int var4;
      if (var3 != null) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            addDistributedQueueMember(var0, var1, var3[var4]);
         }
      }

      var4 = var2.getForwardDelay();
      if (var4 != -1) {
         var1.setForwardDelay(var4);
      }

   }

   private static void fillInDistributedTopic(JMSBean var0, DistributedTopicBean var1, JMSDistributedTopicMBean var2) throws UpdateException {
      JMSDistributedTopicMemberMBean[] var3 = var2.getJMSDistributedTopicMembers();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            addDistributedTopicMember(var0, var1, var3[var4]);
         }
      }

   }

   private static void fillInDistributedDestinationMember(DistributedDestinationMemberBean var0, JMSDistributedDestinationMemberMBean var1) {
      if (var1.getNotes() != null) {
         var0.setNotes(var1.getNotes());
      }

      int var2 = var1.getWeight();
      if (var2 != 1) {
         var0.setWeight(var2);
      }

   }

   private static void fillInDistributedQueueMember(JMSBean var0, DistributedDestinationMemberBean var1, JMSDistributedQueueMemberMBean var2) throws UpdateException {
      JMSQueueMBean var3 = var2.getJMSQueue();
      if (var3 != null) {
         String var4 = var3.getName();
         if (JMSBeanHelper.findDestinationBean(var4, var0) == null) {
            addQueue(var0, var3);
         }

         var1.setPhysicalDestinationName(var4);
      }

   }

   private static void fillInDistributedTopicMember(JMSBean var0, DistributedDestinationMemberBean var1, JMSDistributedTopicMemberMBean var2) throws UpdateException {
      JMSTopicMBean var3 = var2.getJMSTopic();
      if (var3 != null) {
         String var4 = var3.getName();
         if (JMSBeanHelper.findDestinationBean(var4, var0) == null) {
            addTopic(var0, var3);
         }

         var1.setPhysicalDestinationName(var4);
      }

   }

   public static DestinationKeyBean addDestinationKey(JMSBean var0, JMSDestinationKeyMBean var1) {
      DestinationKeyBean var2 = var0.createDestinationKey(var1.getName());
      if (var1.getNotes() != null) {
         var2.setNotes(var1.getNotes());
      }

      String var3 = var1.getProperty();
      if (var3 != null && !"JMSMessageID".equals(var3)) {
         var2.setProperty(var3);
      }

      String var4 = var1.getKeyType();
      if (var4 != null && !"String".equals(var4)) {
         var2.setKeyType(var4);
      }

      String var5 = var1.getDirection();
      if (var5 != null && !"Ascending".equals(var5)) {
         var2.setSortOrder(var5);
      }

      return var2;
   }

   public static QueueBean addQueue(JMSBean var0, JMSQueueMBean var1) throws UpdateException {
      return addQueue(var0, (JMSSystemResourceMBean)null, var1);
   }

   public static QueueBean addQueue(JMSBean var0, JMSSystemResourceMBean var1, JMSQueueMBean var2) throws UpdateException {
      boolean var4 = false;
      String var5 = null;
      QueueBean var3;
      if ((var3 = var0.lookupQueue(var2.getName())) != null) {
         TargetMBean[] var6 = getTargetsFromDestination(var2);
         JMSServerMBean var7 = null;
         if (var6.length > 0) {
            var7 = (JMSServerMBean)var6[0];
            var5 = var7.getName();
         }

         String var8 = var3.getSubDeploymentName();
         if (var8 != null && var8.equals(var5)) {
            return var3;
         }

         JMSLogger.logDestinationNameConflict("JMSQueue", var2.getName());
         var4 = true;
      }

      if (var4) {
         var3 = var0.createQueue(var2.getName() + "_" + var5);
      } else {
         var3 = var0.createQueue(var2.getName());
      }

      fillInDestination(var0, var1, var3, var2, var4);
      return var3;
   }

   public static TopicBean addTopic(JMSBean var0, JMSTopicMBean var1) throws UpdateException {
      return addTopic(var0, (JMSSystemResourceMBean)null, var1);
   }

   public static TopicBean addTopic(JMSBean var0, JMSSystemResourceMBean var1, JMSTopicMBean var2) throws UpdateException {
      boolean var4 = false;
      String var5 = null;
      TopicBean var3;
      if ((var3 = var0.lookupTopic(var2.getName())) != null) {
         TargetMBean[] var6 = getTargetsFromDestination(var2);
         JMSServerMBean var7 = null;
         if (var6.length > 0) {
            var7 = (JMSServerMBean)var6[0];
            var5 = var7.getName();
         }

         String var8 = var3.getSubDeploymentName();
         if (var8 != null && var8.equals(var5)) {
            return var3;
         }

         JMSLogger.logDestinationNameConflict("JMSTopic", var2.getName());
         var4 = true;
      }

      if (var4) {
         var3 = var0.createTopic(var2.getName() + "_" + var5);
      } else {
         var3 = var0.createTopic(var2.getName());
      }

      fillInDestination(var0, var1, var3, var2, var4);
      String var9 = var2.getMulticastAddress();
      if (var9 != null) {
         var3.getMulticast().setMulticastAddress(var9);
      }

      int var10;
      if ((var10 = var2.getMulticastPort()) != 6001) {
         var3.getMulticast().setMulticastPort(var10);
      }

      if ((var10 = var2.getMulticastTTL()) != 1) {
         var3.getMulticast().setMulticastTimeToLive(var10);
      }

      return var3;
   }

   public static String constructQuotaNameFromDestinationName(String var0) {
      return var0 + ".Quota";
   }

   private static TargetMBean[] getTargetsFromDestination(JMSDestinationMBean var0) {
      WebLogicMBean var1 = var0.getParent();
      if (var1 instanceof JMSServerMBean) {
         TargetMBean[] var2 = new TargetMBean[]{(TargetMBean)var1};
         return var2;
      } else {
         return new TargetMBean[0];
      }
   }

   private static void warnDeliveryModeOverrideChange(String var0, JMSServerMBean var1, String var2) {
      String var3 = var1 == null ? "<none>" : var1.getName();
      JMSLogger.logChangingDeliveryModeOverride(var0, var3, var2, "Non-Persistent");
   }

   private static String errorDeliveryModeOverride(String var0, JMSServerMBean var1) {
      String var2 = var1 == null ? "<none>" : var1.getName();
      return JMSExceptionLogger.logDeliveryModeMismatchLoggable(var0, var2).getMessage();
   }

   private static void fillInDestination(JMSBean var0, JMSSystemResourceMBean var1, DestinationBean var2, JMSDestinationMBean var3, boolean var4) throws UpdateException {
      if (var3.getNotes() != null) {
         var2.setNotes(var3.getNotes());
      }

      JMSDestinationKeyMBean[] var5 = var3.getDestinationKeys();
      if (var5 != null) {
         String[] var6 = new String[var5.length];

         for(int var7 = 0; var7 < var5.length; ++var7) {
            String var8 = var5[var7].getName();
            var6[var7] = var8;
            if (var0.lookupDestinationKey(var8) == null) {
               addDestinationKey(var0, var5[var7]);
            }
         }

         var2.setDestinationKeys(var6);
      }

      long var21;
      if ((var21 = var3.getBytesThresholdHigh()) >= 0L) {
         var2.getThresholds().setBytesHigh(var21);
      }

      if ((var21 = var3.getBytesThresholdLow()) >= 0L) {
         var2.getThresholds().setBytesLow(var21);
      }

      if ((var21 = var3.getMessagesThresholdHigh()) >= 0L) {
         var2.getThresholds().setMessagesHigh(var21);
      }

      if ((var21 = var3.getMessagesThresholdLow()) >= 0L) {
         var2.getThresholds().setMessagesLow(var21);
      }

      int var22;
      if ((var22 = var3.getPriorityOverride()) >= 0) {
         var2.getDeliveryParamsOverrides().setPriority(var22);
      }

      String var9 = var3.getTimeToDeliverOverride();
      if (var9 != null && !var9.equals("-1")) {
         var2.getDeliveryParamsOverrides().setTimeToDeliver(var9);
      }

      if ((var21 = var3.getRedeliveryDelayOverride()) >= 0L) {
         var2.getDeliveryParamsOverrides().setRedeliveryDelay(var21);
      }

      JMSDestinationMBean var10 = var3.getErrorDestination();
      String var11;
      if (var10 != null) {
         var11 = var10.getName();
         Object var12;
         if ((var12 = JMSBeanHelper.findDestinationBean(var11, var0)) == null) {
            if (var10 instanceof JMSQueueMBean) {
               var12 = addQueue(var0, (JMSQueueMBean)var10);
            } else {
               if (!(var10 instanceof JMSTopicMBean)) {
                  throw new AssertionError("ERROR: Error destination " + var11 + " for destination " + var3.getName() + " is neither queue nor topic");
               }

               var12 = addTopic(var0, (JMSTopicMBean)var10);
            }
         }

         var2.getDeliveryFailureParams().setErrorDestination((DestinationBean)var12);
      }

      if ((var22 = var3.getRedeliveryLimit()) >= 0) {
         var2.getDeliveryFailureParams().setRedeliveryLimit(var22);
      }

      if ((var21 = var3.getTimeToLiveOverride()) >= 0L) {
         var2.getDeliveryParamsOverrides().setTimeToLive(var21);
      }

      var9 = var3.getExpirationPolicy();
      if (var9 != null && !var9.equals("Discard")) {
         var2.getDeliveryFailureParams().setExpirationPolicy(var9);
      }

      if ((var9 = var3.getExpirationLoggingPolicy()) != null) {
         var2.getDeliveryFailureParams().setExpirationLoggingPolicy(var9);
      }

      if ((var22 = var3.getMaximumMessageSize()) != Integer.MAX_VALUE) {
         var2.setMaximumMessageSize(var22);
      }

      var11 = var3.getJNDIName();
      if (var11 != null && var11.length() > 0) {
         boolean var23 = var3.isJNDINameReplicated();
         if (var23) {
            var2.setJNDIName(var11);
         } else {
            var2.setLocalJNDIName(var11);
         }
      }

      TargetMBean[] var24 = getTargetsFromDestination(var3);
      JMSServerMBean var13 = null;
      if (var24.length > 0) {
         var13 = (JMSServerMBean)var24[0];
         String var14 = var24[0].getName();
         var2.setSubDeploymentName(var14);
         if (var4) {
            var2.setJMSCreateDestinationIdentifier(var3.getName());
         }

         if (var1 != null) {
            SubDeploymentMBean var15;
            if ((var15 = var1.lookupSubDeployment(var14)) == null) {
               var15 = var1.createSubDeployment(var14);
            }

            try {
               var15.setTargets(var24);
            } catch (InvalidAttributeValueException var19) {
               throw new AssertionError("ERROR: Could not set the targets of JMS destination " + var2.getName() + " due to " + var19);
            } catch (DistributedManagementException var20) {
               throw new AssertionError("ERROR: Could not set the targets of JMS destination " + var2.getName() + " due to" + var20);
            }
         }
      }

      var9 = var3.getStoreEnabled();
      byte var25;
      if (var9 != null && !var9.equalsIgnoreCase("default")) {
         if (var9.equalsIgnoreCase("true")) {
            var25 = 1;
         } else {
            var25 = 2;
         }
      } else {
         var25 = 0;
      }

      var9 = var3.getDeliveryModeOverride();
      byte var26;
      if (var9 != null && !var9.equalsIgnoreCase("No-Delivery")) {
         if (var9.equalsIgnoreCase("Persistent")) {
            var26 = 1;
         } else {
            var26 = 2;
         }
      } else {
         var26 = 0;
      }

      boolean var16;
      if (var13 != null && var13.getStore() == null && var13.getPersistentStore() == null) {
         var16 = false;
      } else {
         var16 = true;
      }

      label124:
      switch (var25) {
         case 0:
            switch (var26) {
               case 0:
                  if (!var16) {
                     warnDeliveryModeOverrideChange(var2.getName(), var13, "No-Delivery");
                     var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  }
                  break label124;
               case 1:
                  if (var16) {
                     var2.getDeliveryParamsOverrides().setDeliveryMode("Persistent");
                  } else {
                     warnDeliveryModeOverrideChange(var2.getName(), var13, "Persistent");
                     var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  }
                  break label124;
               case 2:
                  var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  break label124;
               default:
                  throw new AssertionError("Unknown deliveryModeOverride=" + var26 + " storeValue=" + var25);
            }
         case 1:
            switch (var26) {
               case 0:
                  if (!var16) {
                     throw new UpdateException(errorDeliveryModeOverride(var2.getName(), var13));
                  }
                  break label124;
               case 1:
                  if (!var16) {
                     throw new UpdateException(errorDeliveryModeOverride(var2.getName(), var13));
                  }

                  var2.getDeliveryParamsOverrides().setDeliveryMode("Persistent");
                  break label124;
               case 2:
                  if (!var16) {
                     throw new UpdateException(errorDeliveryModeOverride(var2.getName(), var13));
                  }

                  var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  break label124;
               default:
                  throw new AssertionError("Unknown deliveryModeOverride=" + var26 + " storeValue=" + var25);
            }
         case 2:
            switch (var26) {
               case 0:
                  warnDeliveryModeOverrideChange(var2.getName(), var13, "No-Delivery");
                  var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  break label124;
               case 1:
                  warnDeliveryModeOverrideChange(var2.getName(), var13, "Persistent");
                  var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  break label124;
               case 2:
                  var2.getDeliveryParamsOverrides().setDeliveryMode("Non-Persistent");
                  break label124;
               default:
                  throw new AssertionError("Unknown deliveryModeOverride=" + var26 + " storeValue=" + var25);
            }
         default:
            throw new AssertionError("Unknown storeValue=" + var25);
      }

      JMSTemplateMBean var17 = var3.getTemplate();
      if (var17 != null) {
         TemplateBean var18 = addTemplate(var0, var17);
         var2.setTemplate(var18);
      }

      if (var3.getBytesMaximum() != -1L || var3.getMessagesMaximum() != -1L) {
         QuotaBean var27 = addQuota(var0, constructQuotaNameFromDestinationName(var3.getName()), var3.getBytesMaximum(), var3.getMessagesMaximum());
         var2.setQuota(var27);
      }

   }

   private static QuotaBean addQuota(JMSBean var0, String var1, long var2, long var4) {
      QuotaBean var6;
      if ((var6 = var0.lookupQuota(var1)) != null) {
         return var6;
      } else {
         var6 = var0.createQuota(var1);
         if (var2 >= 0L) {
            var6.setBytesMaximum(var2);
         }

         if (var4 >= 0L) {
            var6.setMessagesMaximum(var4);
         }

         var6.setShared(false);
         return var6;
      }
   }

   public static TemplateBean addTemplate(JMSBean var0, JMSTemplateMBean var1) throws UpdateException {
      TemplateBean var2;
      if ((var2 = var0.lookupTemplate(var1.getName())) != null) {
         return var2;
      } else {
         var2 = var0.createTemplate(var1.getName());
         if (var1.getNotes() != null) {
            var2.setNotes(var1.getNotes());
         }

         JMSDestinationKeyMBean[] var3 = var1.getDestinationKeys();
         if (var3 != null) {
            String[] var4 = new String[var3.length];

            for(int var5 = 0; var5 < var3.length; ++var5) {
               String var6 = var3[var5].getName();
               var4[var5] = var6;
               if (var0.lookupDestinationKey(var6) == null) {
                  addDestinationKey(var0, var3[var5]);
               }
            }

            var2.setDestinationKeys(var4);
         }

         long var11;
         if ((var11 = var1.getBytesThresholdHigh()) >= 0L) {
            var2.getThresholds().setBytesHigh(var11);
         }

         if ((var11 = var1.getBytesThresholdLow()) >= 0L) {
            var2.getThresholds().setBytesLow(var11);
         }

         if ((var11 = var1.getMessagesThresholdHigh()) >= 0L) {
            var2.getThresholds().setMessagesHigh(var11);
         }

         if ((var11 = var1.getMessagesThresholdLow()) >= 0L) {
            var2.getThresholds().setMessagesLow(var11);
         }

         int var12;
         if ((var12 = var1.getPriorityOverride()) >= 0) {
            var2.getDeliveryParamsOverrides().setPriority(var12);
         }

         String var7 = var1.getTimeToDeliverOverride();
         if (var7 != null && !var7.equals("-1")) {
            var2.getDeliveryParamsOverrides().setTimeToDeliver(var7);
         }

         if ((var11 = var1.getRedeliveryDelayOverride()) >= 0L) {
            var2.getDeliveryParamsOverrides().setRedeliveryDelay(var11);
         }

         JMSDestinationMBean var8 = var1.getErrorDestination();
         if (var8 != null) {
            String var9 = var8.getName();
            Object var10;
            if ((var10 = JMSBeanHelper.findDestinationBean(var9, var0)) == null) {
               if (var8 instanceof JMSQueueMBean) {
                  var10 = addQueue(var0, (JMSQueueMBean)var8);
               } else {
                  if (!(var8 instanceof JMSTopicMBean)) {
                     throw new AssertionError("ERROR: Error destination " + var9 + " of template " + var1.getName() + " is neither queue nor topic");
                  }

                  var10 = addTopic(var0, (JMSTopicMBean)var8);
               }
            }

            var2.getDeliveryFailureParams().setErrorDestination((DestinationBean)var10);
         }

         if ((var12 = var1.getRedeliveryLimit()) >= 0) {
            var2.getDeliveryFailureParams().setRedeliveryLimit(var12);
         }

         if ((var11 = var1.getTimeToLiveOverride()) >= 0L) {
            var2.getDeliveryParamsOverrides().setTimeToLive(var11);
         }

         var7 = var1.getDeliveryModeOverride();
         if (var7 != null && !var7.equals("No-Delivery")) {
            var2.getDeliveryParamsOverrides().setDeliveryMode(var7);
         }

         if ((var7 = var1.getExpirationPolicy()) != null) {
            var2.getDeliveryFailureParams().setExpirationPolicy(var7);
         }

         if ((var7 = var1.getExpirationLoggingPolicy()) != null) {
            var2.getDeliveryFailureParams().setExpirationLoggingPolicy(var7);
         }

         if ((var12 = var1.getMaximumMessageSize()) != Integer.MAX_VALUE) {
            var2.setMaximumMessageSize(var12);
         }

         QuotaBean var13 = addQuota(var0, constructQuotaNameFromDestinationName(var1.getName()), var1.getBytesMaximum(), var1.getMessagesMaximum());
         if (var1.getBytesMaximum() >= 0L || var1.getMessagesMaximum() >= 0L) {
            var2.setQuota(var13);
         }

         return var2;
      }
   }

   public static JMSDestinationMBean findErrorQueue(DomainMBean var0, String var1) {
      if (var0 != null && var1 != null) {
         JMSServerMBean[] var2 = var0.getJMSServers();
         if (var2 == null) {
            return null;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               JMSServerMBean var4 = var2[var3];
               JMSQueueMBean[] var5 = var4.getJMSQueues();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  JMSQueueMBean var7 = var5[var6];
                  if (var1.equals(var7.getName())) {
                     return var7;
                  }
               }

               JMSTopicMBean[] var9 = var4.getJMSTopics();

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  JMSTopicMBean var8 = var9[var10];
                  if (var1.equals(var8.getName())) {
                     return var8;
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static HashMap splitDeployment(DeploymentMBean var0) {
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      TargetMBean[] var6 = var0.getTargets();
      if (var6 != null && var6.length > 1) {
         int var7;
         ArrayList var9;
         for(var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7] instanceof ClusterMBean && !var4.containsValue((ClusterMBean)var6[var7])) {
               var4.put(var6[var7].getName(), (ClusterMBean)var6[var7]);
               String var8 = var0.getName() + "_" + var6[var7].getName();
               var9 = new ArrayList();
               var9.add(var6[var7]);
               var1.put(var8, var9);
            }
         }

         for(var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7] instanceof ServerMBean) {
               ClusterMBean var16 = ((ServerMBean)var6[var7]).getCluster();
               if (var16 != null) {
                  if (!var4.containsValue(var16)) {
                     var2.put(var6[var7].getName(), var16);
                     var3.put(var6[var7].getName(), var6[var7]);
                  }
               } else if (!var5.containsValue((ServerMBean)var6[var7])) {
                  var5.put(var6[var7].getName(), (ServerMBean)var6[var7]);
                  String var18 = var0.getName() + "_" + var6[var7].getName();
                  ArrayList var10 = new ArrayList();
                  var10.add(var6[var7]);
                  var1.put(var18, var10);
               }
            }
         }

         Iterator var15 = var2.values().iterator();
         Iterator var17 = var2.keySet().iterator();

         while(var15.hasNext()) {
            var9 = new ArrayList();
            String var19 = null;
            ClusterMBean var11 = (ClusterMBean)var15.next();

            while(var17.hasNext()) {
               String var12 = (String)var17.next();
               ClusterMBean var13 = (ClusterMBean)var2.get(var12);
               if (var13.getName().equals(var11.getName())) {
                  TargetMBean var14 = (TargetMBean)var3.get(var12);
                  var9.add(var14);
                  if (var19 != null) {
                     var19 = var19 + "_" + var12;
                  } else {
                     var19 = var0.getName() + "_" + var12;
                  }
               }
            }

            if (var19 != null) {
               var1.put(var19, var9);
            }
         }
      }

      return var1;
   }
}
