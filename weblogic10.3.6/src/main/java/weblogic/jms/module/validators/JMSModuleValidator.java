package weblogic.jms.module.validators;

import java.io.File;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ClientParamsBean;
import weblogic.j2ee.descriptor.wl.DefaultDeliveryParamsBean;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.FlowControlParamsBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.SAFDestinationBean;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFQueueBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.j2ee.descriptor.wl.SAFTopicBean;
import weblogic.j2ee.descriptor.wl.TargetableBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.ThresholdParamsBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedDestinationBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.deployer.DeployerConstants;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.jms.extensions.Schedule;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.saf.IDBeanHandler;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.PathServiceMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;

public class JMSModuleValidator {
   private static final String PHY_PROP = "PhysicalDestinationName";
   private static final int DQ_TYPE = 0;
   private static final int DT_TYPE = 1;
   private static final int DD_MAX_TYPE = 2;
   private static final int QUEUE_TYPE = 0;
   private static final int TOPIC_TYPE = 1;
   private static final int MAX_TYPE = 2;
   private static final String INTEROP_FILE_NAME = "interop-jms.xml";

   public static void validateJMSModule(JMSBean var0) throws IllegalArgumentException {
      validateTemplates(var0);
      validateDestinations(var0);
      validateConnectionFactories(var0);
      validateForeignServers(var0);
   }

   public static void validateTemplates(JMSBean var0) throws IllegalArgumentException {
      TemplateBean[] var1 = var0.getTemplates();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         validateAMEPolicy(var0, (DescriptorBean)var1[var2]);
      }

   }

   private static void validateDistributedDestinations(TargetInfoMBean var0, BasicDeploymentMBean var1, JMSBean var2) throws IllegalArgumentException {
      validateDistributedQueues(var0, var1, var2);
      validateDistributedTopics(var0, var1, var2);
   }

   private static SubDeploymentMBean mySub(SubDeploymentMBean[] var0, TargetableBean var1) {
      if (var1.getSubDeploymentName() != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2].getName().equals(var1.getSubDeploymentName())) {
               return var0[var2];
            }
         }
      }

      return null;
   }

   private static void sameCluster(DomainMBean var0, String var1, Collection var2) {
      ClusterMBean var3 = null;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         String var6 = var5;
         TargetMBean[] var7 = null;
         if (var0.lookupSAFAgent(var5) != null) {
            var7 = var0.lookupSAFAgent(var5).getTargets();
            var6 = var5 + "; 134 domain.lookupSAFAgent " + (var7 == null);
         }

         Object var8;
         if (var7 == null) {
            if (var0.lookupJMSServer(var5) != null) {
               var7 = var0.lookupJMSServer(var5).getTargets();
               var6 = var6 + "; 140 did domain.lookupJMSServer " + (var7 == null);
            }

            if (var7 == null) {
               if (IDBeanHandler.hasSafTarget(var5)) {
                  var5 = IDBeanHandler.extractTargetPart(var5);
                  var6 = var6 + "; 146 replace targetName " + var5;
               }

               var8 = var0.lookupServer(var5);
               if (var8 == null) {
                  var8 = var0.lookupMigratableTarget(var5);
               }

               var7 = new TargetMBean[]{(TargetMBean)var8};
               var6 = var6 + "; 151 replace lookupServer " + (var8 == null);
            }
         }

         var8 = null;

         for(int var9 = 0; var9 < var7.length; ++var9) {
            ClusterMBean var10;
            if (var7[var9] instanceof ServerMBean) {
               var10 = ((ServerMBean)var7[var9]).getCluster();
            } else if (var7[var9] instanceof MigratableTargetMBean) {
               var10 = ((MigratableTargetMBean)var7[var9]).getCluster();
            } else {
               if (!(var7[var9] instanceof ClusterMBean)) {
                  throw new AssertionError("Nonsensical target " + var7[var9] + " details " + var6 + (var7[var9] == null ? "; null " + var9 : "; [" + var9 + "] type " + var7[var9].getClass().getName()));
               }

               var10 = (ClusterMBean)var7[var9];
            }

            if (var3 == null) {
               var3 = var10;
            } else if (var3 != var10) {
               throw new IllegalArgumentException(var1 + " may only be targeted to one cluster");
            }
         }
      }

   }

   private static void validateImportedDestinationGroups(SubDeploymentMBean[] var0, BasicDeploymentMBean var1, JMSBean var2) throws IllegalArgumentException {
      SAFImportedDestinationsBean[] var3 = var2.getSAFImportedDestinations();
      DomainMBean var4 = JMSBeanHelper.getDomain(var1);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         validateTargetingOptions(var3[var5]);
         SubDeploymentMBean var6 = mySub(var0, var3[var5]);
         if (var6 != null) {
            TargetMBean[] var7 = var6.getTargets();
            if (var7 != null && var7.length != 0) {
               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (!(var7[var8] instanceof ServerMBean) && !(var7[var8] instanceof ClusterMBean) && !(var7[var8] instanceof SAFAgentMBean)) {
                     throw new IllegalArgumentException(JMSExceptionLogger.logIllegalTargetTypeLoggable(var3[var8].getName(), var7[var8].getName(), var6.getName()).getMessage());
                  }

                  if (var7[var8] instanceof SAFAgentMBean && "Receiving-only".equals(((SAFAgentMBean)var7[var8]).getServiceType())) {
                     throw new IllegalArgumentException(JMSExceptionLogger.logIllegalAgentTypeLoggable(var3[var8].getName(), var7[var8].getName()).getMessage());
                  }
               }
            }

            HashMap var10 = new HashMap();
            String var9 = var3[var5].getName();
            IDBeanHandler.fillWithMyTargets(var9, var10, var4, var6.getTargets());
            sameCluster(var4, var9, var10.values());
            validateImportedDestinations(var3[var5], var6, var2);
         }
      }

   }

   private static void validateUniformDistributedDestinations(SubDeploymentMBean[] var0, BasicDeploymentMBean var1, JMSBean var2) throws IllegalArgumentException {
      DomainMBean var3 = JMSBeanHelper.getDomain(var1);
      UniformDistributedQueueBean[] var4 = var2.getUniformDistributedQueues();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         validateTargetingOptions(var4[var5]);
         SubDeploymentMBean var6 = mySub(var0, var4[var5]);
         if (var6 != null) {
            HashMap var7 = new HashMap();
            JMSModuleHelper.uddFillWithMyTargets(var7, var3, (SubDeploymentMBean)var6);
            sameCluster(var3, var4[var5].getName(), var7.values());
            validateErrorDestination(var4[var5], (TargetInfoMBean)null, true);
         }
      }

      UniformDistributedTopicBean[] var9 = var2.getUniformDistributedTopics();

      for(int var10 = 0; var10 < var9.length; ++var10) {
         validateTargetingOptions(var9[var10]);
         SubDeploymentMBean var11 = mySub(var0, var9[var10]);
         if (var11 != null) {
            HashMap var8 = new HashMap();
            JMSModuleHelper.uddFillWithMyTargets(var8, var3, (SubDeploymentMBean)var11);
            sameCluster(var3, var9[var10].getName(), var8.values());
            validateErrorDestination(var9[var10], (TargetInfoMBean)null, true);
         }
      }

   }

   private static String getDestinationTargetName(DestinationBean var0, TargetInfoMBean var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var0.getSubDeploymentName();
         SubDeploymentMBean var3 = null;
         if (var1 instanceof BasicDeploymentMBean) {
            var3 = ((BasicDeploymentMBean)var1).lookupSubDeployment(var2);
         } else {
            var3 = ((SubDeploymentMBean)var1).lookupSubDeployment(var2);
         }

         if (var3 == null) {
            return null;
         } else {
            TargetMBean[] var4 = var3.getTargets();
            return var4.length != 1 ? null : var4[0].getName();
         }
      }
   }

   private static void validateDistributedQueues(TargetInfoMBean var0, BasicDeploymentMBean var1, JMSBean var2) throws IllegalArgumentException {
      boolean var3 = var1 instanceof JMSInteropModuleMBean;
      boolean var4 = false;
      String var5 = null;
      DistributedQueueBean[] var6 = var2.getDistributedQueues();
      HashMap var7 = new HashMap();

      for(int var8 = 0; var8 < var6.length; ++var8) {
         DistributedQueueBean var9 = var6[var8];
         DistributedDestinationMemberBean[] var10 = var9.getDistributedQueueMembers();
         LinkedList var11 = new LinkedList();

         for(int var12 = 0; var12 < var10.length; ++var12) {
            DistributedDestinationMemberBean var13 = var10[var12];
            if (!var3 && var13.isSet("PhysicalDestinationName")) {
               throw new IllegalArgumentException(JMSExceptionLogger.logUseOfInteropFieldLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
            }

            String var14 = var13.getPhysicalDestinationName();
            QueueBean var15 = var2.lookupQueue(var14);
            if (!var3) {
               if (var15 == null) {
                  throw new IllegalArgumentException(JMSExceptionLogger.logPhysicalDestinationNotPresentLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
               }
            } else if (var13.isSet("PhysicalDestinationName") && var15 == null) {
               throw new IllegalArgumentException(JMSExceptionLogger.logPhysicalDestinationNotPresentLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
            }

            String var16;
            if (var15 != null) {
               var16 = var15.getUnitOfWorkHandlingPolicy();
               if (var4) {
                  if (!var5.equals(var16)) {
                     throw new IllegalArgumentException("DQ Member " + var13.getName() + " has a Unit Of Work Handling Policy which is inconsistent with other members");
                  }
               } else {
                  var5 = var16;
                  var4 = true;
               }
            }

            var16 = (String)var7.put(var13.getName(), var9.getName());
            if (var16 != null) {
               throw new IllegalArgumentException("DQ Member " + var13.getName() + " is part of both " + var16 + " and " + var9.getName());
            }

            String var17 = getDestinationTargetName(var15, var0);
            if (var17 != null) {
               var11.add(var17);
            }
         }

         DomainMBean var18 = JMSBeanHelper.getDomain(var1);
         sameCluster(var18, var9.getName(), var11);
      }

   }

   private static void validateDistributedTopics(TargetInfoMBean var0, BasicDeploymentMBean var1, JMSBean var2) throws IllegalArgumentException {
      boolean var3 = var1 instanceof JMSInteropModuleMBean;
      boolean var4 = false;
      String var5 = null;
      DistributedTopicBean[] var6 = var2.getDistributedTopics();
      HashMap var7 = new HashMap();

      for(int var8 = 0; var8 < var6.length; ++var8) {
         DistributedTopicBean var9 = var6[var8];
         DistributedDestinationMemberBean[] var10 = var9.getDistributedTopicMembers();
         LinkedList var11 = new LinkedList();

         for(int var12 = 0; var12 < var10.length; ++var12) {
            DistributedDestinationMemberBean var13 = var10[var12];
            if (!var3 && var13.isSet("PhysicalDestinationName")) {
               throw new IllegalArgumentException(JMSExceptionLogger.logUseOfInteropFieldLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
            }

            String var14 = var13.getPhysicalDestinationName();
            TopicBean var15 = var2.lookupTopic(var14);
            if (!var3) {
               if (var15 == null) {
                  throw new IllegalArgumentException(JMSExceptionLogger.logPhysicalDestinationNotPresentLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
               }
            } else if (var13.isSet("PhysicalDestinationName") && var15 == null) {
               throw new IllegalArgumentException(JMSExceptionLogger.logPhysicalDestinationNotPresentLoggable(var1.getName(), var9.getName(), var13.getName(), var13.getPhysicalDestinationName()).getMessage());
            }

            String var16;
            if (var15 != null) {
               var16 = var15.getUnitOfWorkHandlingPolicy();
               if (var4) {
                  if (!var5.equals(var16)) {
                     throw new IllegalArgumentException("DQ Member " + var13.getName() + " has a Unit Of Work Handling Policy which is inconsistent with other members");
                  }
               } else {
                  var5 = var16;
                  var4 = true;
               }
            }

            var16 = (String)var7.put(var13.getName(), var9.getName());
            if (var16 != null) {
               throw new IllegalArgumentException("DT Member " + var13.getName() + " is part of both " + var16 + " and " + var9.getName());
            }

            String var17 = getDestinationTargetName(var15, var0);
            if (var17 != null) {
               var11.add(var17);
            }
         }

         DomainMBean var18 = JMSBeanHelper.getDomain(var1);
         sameCluster(var18, var9.getName(), var11);
      }

   }

   private static void validateDestinations(JMSBean var0) throws IllegalArgumentException {
      HashMap var1 = new HashMap();

      int var2;
      for(var2 = 0; var2 < var0.getQueues().length; ++var2) {
         validateQueue(var0, var0.getQueues()[var2], var1);
         if (var0.getQueues()[var2].isSet("DefaultTargetingEnabled")) {
            throw new IllegalArgumentException(JMSExceptionLogger.logDefaultTargetingNotSupportedLoggable(var0.getQueues()[var2].getName()).getMessage());
         }
      }

      for(var2 = 0; var2 < var0.getTopics().length; ++var2) {
         validateTopic(var0, var0.getTopics()[var2], var1);
         if (var0.getTopics()[var2].isSet("DefaultTargetingEnabled")) {
            throw new IllegalArgumentException(JMSExceptionLogger.logDefaultTargetingNotSupportedLoggable(var0.getTopics()[var2].getName()).getMessage());
         }
      }

      for(var2 = 0; var2 < var0.getUniformDistributedQueues().length; ++var2) {
         validateQueue(var0, var0.getUniformDistributedQueues()[var2], var1);
      }

      for(var2 = 0; var2 < var0.getUniformDistributedTopics().length; ++var2) {
         validateTopic(var0, var0.getUniformDistributedTopics()[var2], var1);
      }

      for(var2 = 0; var2 < 2; ++var2) {
         Object var3;
         String var4;
         switch (var2) {
            case 0:
               var3 = var0.getDistributedQueues();
               var4 = "DistributedQueue";
               break;
            case 1:
               var3 = var0.getDistributedTopics();
               var4 = "DistributedTopic";
               break;
            default:
               throw new IllegalArgumentException("Unknown type: " + var2);
         }

         for(int var5 = 0; var5 < ((Object[])var3).length; ++var5) {
            Object var6 = ((Object[])var3)[var5];
            if (var1.containsKey(((NamedEntityBean)var6).getName())) {
               String var7 = ((NamedEntityBean)var6).getName();
               String var8 = (String)var1.get(var7);
               throw new IllegalArgumentException("The destination " + var7 + " has a duplicate name with an entity of type " + var8);
            }

            var1.put(((NamedEntityBean)var6).getName(), var4);
         }
      }

   }

   private static void validateQueue(JMSBean var0, QueueBean var1, HashMap var2) throws IllegalArgumentException {
      if (var2.containsKey(var1.getName())) {
         String var3 = var1.getName();
         String var4 = (String)var2.get(var3);
         throw new IllegalArgumentException("The destination " + var3 + " has a duplicate name with an entity of type " + var4);
      } else {
         var2.put(var1.getName(), "Queue");
         validateDestCommon(var0, var1);
      }
   }

   private static void validateTopic(JMSBean var0, TopicBean var1, HashMap var2) throws IllegalArgumentException {
      if (var2.containsKey(var1.getName())) {
         String var3 = var1.getName();
         String var4 = (String)var2.get(var3);
         throw new IllegalArgumentException("The destination " + var3 + " has a duplicate name with an entity of type " + var4);
      } else {
         var2.put(var1.getName(), "Topic");
         validateDestCommon(var0, var1);
         validateMulticastAddress(var1.getMulticast().getMulticastAddress());
      }
   }

   private static void validateDestCommon(JMSBean var0, DestinationBean var1) throws IllegalArgumentException {
      validateLegalOrderOfDestinationKeys(var0, var1);
      validateThresholds(var1);
      validateQuota(var1);
      validateDeliveryParamsOverrides(var1);
      validateDeliveryFailureParams(var0, var1);
   }

   private static void validateThresholds(DestinationBean var0) throws IllegalArgumentException {
      validBytes(var0);
      validMessages(var0);
   }

   private static void validateQuota(DestinationBean var0) throws IllegalArgumentException {
      validBytesMaximum(var0);
      validMessagesMaximum(var0);
   }

   private static void validBytes(DestinationBean var0) throws IllegalArgumentException {
      ThresholdParamsBean var1 = var0.getThresholds();
      long var2 = var1.getBytesLow();
      long var4 = var1.getBytesHigh();
      long var6 = Long.MAX_VALUE;
      QuotaBean var8 = var0.getQuota();
      if (var8 != null) {
         var6 = var8.getBytesMaximum();
      }

      if (var4 != Long.MAX_VALUE && var2 >= var4 && var6 < var4) {
         throw new IllegalArgumentException("Invalid bytes threshold parameter for destination " + var0.getName() + ", bytes low=" + var2 + ", bytes high=" + var4 + ", bytes maximum=" + var6);
      }
   }

   private static void validMessages(DestinationBean var0) throws IllegalArgumentException {
      ThresholdParamsBean var1 = var0.getThresholds();
      long var2 = var1.getMessagesLow();
      long var4 = var1.getMessagesHigh();
      long var6 = Long.MAX_VALUE;
      QuotaBean var8 = var0.getQuota();
      if (var8 != null) {
         var6 = var8.getMessagesMaximum();
      }

      if (var4 != Long.MAX_VALUE && var2 >= var4 && var6 < var4) {
         throw new IllegalArgumentException("Invalid messages threshold for destination " + var0.getName() + ", messages low=" + var2 + ", messages high=" + var4 + ", messages maximum=" + var6);
      }
   }

   private static void validBytesMaximum(DestinationBean var0) throws IllegalArgumentException {
      long var1 = Long.MAX_VALUE;
      QuotaBean var3 = var0.getQuota();
      if (var3 != null) {
         var1 = var3.getBytesMaximum();
      }

      if (var1 != Long.MAX_VALUE && (var1 < 0L || var1 > Long.MAX_VALUE)) {
         throw new IllegalArgumentException("Invalid bytes maximum for destination " + var0.getName() + ", bytes maximum=" + var1);
      }
   }

   private static void validMessagesMaximum(DestinationBean var0) throws IllegalArgumentException {
      long var1 = Long.MAX_VALUE;
      QuotaBean var3 = var0.getQuota();
      if (var3 != null) {
         var1 = var3.getMessagesMaximum();
      }

      if (var1 != Long.MAX_VALUE && (var1 < 0L || var1 > Long.MAX_VALUE)) {
         throw new IllegalArgumentException("Invalid messages maximum for destination " + var0.getName() + ", messages maximum=" + var1);
      }
   }

   private static void validateLegalOrderOfDestinationKeys(JMSBean var0, DestinationBean var1) {
      String[] var2 = var1.getDestinationKeys();
      DestinationKeyBean[] var3 = new DestinationKeyBean[var2.length];

      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         DestinationKeyBean var5 = var0.lookupDestinationKey(var2[var4]);
         if (var5 == null) {
            throw new IllegalArgumentException("Destination key " + var2[var4] + " used by destination " + var1.getName() + " not found");
         }

         var3[var4] = var5;
      }

      if (var3.length > 1) {
         for(var4 = 0; var4 < var3.length - 1; ++var4) {
            if (var3[var4].getProperty().equals("JMSMessageID")) {
               throw new IllegalArgumentException("Invalid destination key order for destination " + var1.getName() + ", JMSMessageID must be specified as last key ");
            }
         }
      }

   }

   public static void validateDestinationKeyProperty(DestinationKeyBean var0) throws IllegalArgumentException {
      String var1 = var0.getProperty();
      if (var1 == null) {
         throw new IllegalArgumentException("Invalid property value " + var1 + " found for destination key " + var0.getName());
      } else if (var1.trim().length() == 0) {
         throw new IllegalArgumentException("Invalid property value \"" + var1 + "\" found for destination key " + var0.getName());
      } else if (var1.startsWith("JMS") && !var1.equals("JMSMessageID") && !var1.equals("JMSTimestamp") && !var1.equals("JMSCorrelationID") && !var1.equals("JMSPriority") && !var1.equals("JMSExpiration") && !var1.equals("JMSType") && !var1.equals("JMSRedelivered") && !var1.equals("JMSDeliveryTime") && !var1.equals("JMS_BEA_Size") && !var1.equals("JMS_BEA_UnitOfOrder")) {
         throw new IllegalArgumentException("Invalid JMS Header property value " + var1 + " found for destination key " + var0.getName());
      }
   }

   public static void validateDeliveryParamsOverrides(DestinationBean var0) throws IllegalArgumentException {
      validateTimeToDeliverOverride(var0);
   }

   private static boolean isTimeToDeliverOverrideValid(String var0) throws ParseException {
      if (var0 == null) {
         return true;
      } else {
         try {
            long var1 = Long.parseLong(var0);
            return var1 >= -1L;
         } catch (NumberFormatException var3) {
            Schedule.parseSchedule(var0);
            return true;
         }
      }
   }

   public static void validateTimeToDeliverOverride(DestinationBean var0) {
      DeliveryParamsOverridesBean var1 = var0.getDeliveryParamsOverrides();
      String var2 = var1.getTimeToDeliver();

      try {
         if (!isTimeToDeliverOverrideValid(var2)) {
            throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getIllegalTimeToDeliverOverride());
         }
      } catch (ParseException var4) {
         throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getIllegalTimeToDeliverOverrideWithException(var4.toString()));
      }
   }

   public static void validateTimeToDeliverOverride(String var0) {
      try {
         if (!isTimeToDeliverOverrideValid(var0)) {
            throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getIllegalTimeToDeliverOverride());
         }
      } catch (ParseException var2) {
         throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getIllegalTimeToDeliverOverrideWithException(var2.toString()));
      }
   }

   private static void validateDeliveryFailureParams(JMSBean var0, DestinationBean var1) throws IllegalArgumentException {
      validateAMEPolicy(var0, (DescriptorBean)var1);
      DeliveryFailureParamsBean var2 = var1.getDeliveryFailureParams();
      if (var2.getExpirationPolicy().equals("Redirect") && var2.getErrorDestination() == null) {
         throw new IllegalArgumentException("Can not use \"Redirect\" as message expiration policy if error destination is not defined for destination " + var1.getName());
      }
   }

   private static void validateErrorDestination(DestinationBean var0, TargetInfoMBean var1, boolean var2) throws IllegalArgumentException {
      if (var1 != null || var2) {
         String var3 = var0.getSubDeploymentName();
         String var4 = null;
         DestinationBean var5 = var0.getDeliveryFailureParams().getErrorDestination();
         if (var5 != null) {
            if (var5 instanceof UniformDistributedDestinationBean) {
               if (!var2) {
                  throw new IllegalArgumentException("Invalid Error destination for destination " + var0.getName() + ", a normal destination may not have a uniform distributed destination as an error destination");
               }
            } else if (var2) {
               throw new IllegalArgumentException("Invalid Error destination for destination " + var0.getName() + ", a uniform distributed destination must have a uniform distributed destination as an error destination");
            }

            String var6 = var5.getName();
            if (var6.equals(var0.getName())) {
               throw new IllegalArgumentException("Invalid Error destination for destination " + var0.getName() + ", a destination cannot set itself as its error destination");
            } else {
               var4 = var5.getSubDeploymentName();
               if (!var3.equals(var4)) {
                  if (var2) {
                     throw new IllegalArgumentException("Invalid Error destination for destination " + var0.getName() + ", a uniform distributed destination must have an error destination which is targeted identically to itself");
                  } else {
                     SubDeploymentMBean var7 = null;
                     SubDeploymentMBean var8 = null;
                     if (var1 instanceof BasicDeploymentMBean) {
                        var7 = ((BasicDeploymentMBean)var1).lookupSubDeployment(var4);
                        var8 = ((BasicDeploymentMBean)var1).lookupSubDeployment(var3);
                     } else {
                        var7 = ((SubDeploymentMBean)var1).lookupSubDeployment(var4);
                        var8 = ((SubDeploymentMBean)var1).lookupSubDeployment(var3);
                     }

                     if (var7 != null || var8 != null) {
                        if (var7 == null && var8 != null || var7 != null && var8 == null) {
                           throw new IllegalArgumentException("Sub Deploymet does not exist for either error destination " + var6 + " or the destination " + var0.getName());
                        } else {
                           TargetMBean[] var9 = var7.getTargets();
                           TargetMBean[] var10 = var8.getTargets();
                           if (var9.length > 0 || var10.length > 0) {
                              if ((var9.length != 1 || var10.length > 0) && (var9.length > 0 || var10.length != 1)) {
                                 if (!var9[0].getName().equals(var10[0].getName())) {
                                    throw new IllegalArgumentException("Error destination " + var6 + " must be targeted to the same JMSServer as the destination " + var0.getName());
                                 }
                              } else {
                                 throw new IllegalArgumentException("The SubDeployment of either destination " + var0.getName() + " or the error destination " + var0.getName() + " is not currently targeted");
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void validateAMEPolicy(JMSBean var0, DescriptorBean var1) throws IllegalArgumentException {
      DeliveryFailureParamsBean var2 = null;
      if (!(var1 instanceof DestinationBean) && !(var1 instanceof TemplateBean)) {
         if (var1 instanceof DestinationBean) {
            var2 = ((DestinationBean)var1).getDeliveryFailureParams();
         } else if (var1 instanceof TemplateBean) {
            var2 = ((TemplateBean)var1).getDeliveryFailureParams();
         }

         String var3 = var2.getExpirationPolicy();
         if (var3 != null && (!var3.equalsIgnoreCase("Discard") || !var3.equalsIgnoreCase("Redirect") || !var3.equalsIgnoreCase("Log"))) {
            throw new IllegalArgumentException("Invalid active message expiration policy for destination " + ((NamedEntityBean)var1).getName() + ", expiration policy=" + var3);
         } else {
            if (var1 instanceof DestinationBean) {
               if (var3 != null && var3.equalsIgnoreCase("Redirect")) {
                  DestinationBean var4 = ((DestinationBean)var1).getDeliveryFailureParams().getErrorDestination();
                  if (var4 == null) {
                     throw new IllegalArgumentException("Invalid active message expiration policy for destination " + ((NamedEntityBean)var1).getName() + ", expiration policy=" + var3 + ", no error destination found");
                  }
               }
            } else if (var1 instanceof TemplateBean) {
               TemplateBean var8 = (TemplateBean)var1;
               if (var3 != null && var3.equalsIgnoreCase("Redirect")) {
                  DestinationBean var5 = var8.getDeliveryFailureParams().getErrorDestination();
                  if (var5 == null) {
                     DestinationBean[] var6 = JMSBeanHelper.findAllInheritedDestinations(var8.getName(), var0);
                     if (var6 != null) {
                        for(int var7 = 0; var7 < var6.length; ++var7) {
                           var5 = var6[var7].getDeliveryFailureParams().getErrorDestination();
                           if (var5 == null) {
                              throw new IllegalArgumentException("Invalid active message expiration policy for destination " + ((NamedEntityBean)var1).getName() + ", expiration policy=" + var3 + ", no error destination found");
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public static void validateMulticastAddress(String var0) throws IllegalArgumentException {
      if (var0 != null && !var0.equals("")) {
         for(int var2 = 0; var2 < 4; ++var2) {
            int var1 = var0.indexOf(".");
            if (var1 == -1 && var2 < 3) {
               throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().InvalidMulticastAddress(var0));
            }

            if (var2 == 3) {
               var1 = var0.length();
            }

            for(int var3 = 0; var3 < var1; ++var3) {
               if (!Character.isDigit(var0.charAt(var3))) {
                  throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().InvalidMulticastAddress(var0));
               }
            }

            if (var2 < 3) {
               var0 = var0.substring(var1 + 1);
            }
         }
      }

   }

   public static void validateCFJNDIName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         for(int var1 = 0; var1 < DeployerConstants.DEFAULT_FACTORY_NAMES.length; ++var1) {
            if (var0.equalsIgnoreCase(DeployerConstants.DEFAULT_FACTORY_NAMES[var1][1])) {
               throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getJMSCFJNDIConflictWithDefaultsException(var0));
            }
         }

      }
   }

   public static void validateCFName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         for(int var1 = 0; var1 < DeployerConstants.DEFAULT_FACTORY_NAMES.length; ++var1) {
            if (var0.equalsIgnoreCase(DeployerConstants.DEFAULT_FACTORY_NAMES[var1][0])) {
               throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getJMSCFConflictWithDefaultsException(var0));
            }
         }

      }
   }

   private static SubDeploymentMBean[] getSubDeploymentsFromTargetInfo(TargetInfoMBean var0) {
      if (var0 == null) {
         return new SubDeploymentMBean[0];
      } else if (var0 instanceof BasicDeploymentMBean) {
         return ((BasicDeploymentMBean)var0).getSubDeployments();
      } else {
         return var0 instanceof SubDeploymentMBean ? ((SubDeploymentMBean)var0).getSubDeployments() : new SubDeploymentMBean[0];
      }
   }

   private static boolean isTargetASubTarget(TargetMBean var0, TargetMBean var1) {
      boolean var2 = var0 instanceof ClusterMBean;
      ServerMBean var3 = null;
      TargetMBean[] var5;
      if (var1 instanceof SAFAgentMBean) {
         SAFAgentMBean var8 = (SAFAgentMBean)var1;
         var5 = var8.getTargets();
         if (var5.length <= 0) {
            return true;
         } else {
            for(int var10 = 0; var10 < var5.length; ++var10) {
               TargetMBean var11 = var5[var10];
               if (var11 instanceof ServerMBean) {
                  var3 = (ServerMBean)var11;
                  if (!isSingleServerTargetASubTarget(var2, var3, var0, var1)) {
                     return false;
                  }
               } else if (var11 instanceof ClusterMBean) {
                  if (!var2) {
                     return false;
                  }

                  if (!isMatchingCluster(var0, var3)) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         if (var1 instanceof JMSServerMBean) {
            JMSServerMBean var4 = (JMSServerMBean)var1;
            var5 = var4.getTargets();
            if (var5.length <= 0) {
               return true;
            }

            TargetMBean var6 = var5[0];
            if (var6 instanceof MigratableTargetMBean) {
               if (!var2) {
                  return false;
               }

               MigratableTargetMBean var7 = (MigratableTargetMBean)var6;
               ClusterMBean var9 = var7.getCluster();
               if (var9 == null) {
                  return false;
               }

               if (!var0.getName().equals(var9.getName())) {
                  return false;
               }

               return true;
            }

            if (!(var6 instanceof ServerMBean)) {
               return false;
            }

            var3 = (ServerMBean)var6;
         }

         return isSingleServerTargetASubTarget(var2, var3, var0, var1);
      }
   }

   private static boolean isSingleServerTargetASubTarget(boolean var0, ServerMBean var1, TargetMBean var2, TargetMBean var3) {
      if (var1 == null && var3 instanceof ServerMBean) {
         var1 = (ServerMBean)var3;
      }

      if (var1 != null) {
         if (var0) {
            return isMatchingCluster(var2, var1);
         } else {
            return var2.getName().equals(var1.getName());
         }
      } else {
         return !var0 ? false : var2.getName().equals(var3.getName());
      }
   }

   private static boolean isMatchingCluster(TargetMBean var0, ServerMBean var1) {
      ClusterMBean var2 = null;
      if (var1 != null) {
         var2 = var1.getCluster();
      }

      if (var2 == null) {
         return false;
      } else {
         return var0.getName().equals(var2.getName());
      }
   }

   private static void validateTargetingHierarchy(BasicDeploymentMBean var0, TargetInfoMBean var1) throws IllegalArgumentException {
      if (var1 != null) {
         if (!(var0 instanceof JMSInteropModuleMBean)) {
            TargetMBean[] var2 = var0.getTargets();
            if (var2.length > 0) {
               SubDeploymentMBean[] var3 = getSubDeploymentsFromTargetInfo(var1);
               HashSet var4 = new HashSet();
               HashSet var5 = new HashSet();
               HashSet var6 = new HashSet();
               HashSet var7 = new HashSet();

               for(int var8 = 0; var8 < var3.length; ++var8) {
                  SubDeploymentMBean var9 = var3[var8];
                  TargetMBean[] var10 = var9.getTargets();

                  for(int var11 = 0; var11 < var10.length; ++var11) {
                     TargetMBean var12 = var10[var11];
                     if (var12 instanceof JMSServerMBean) {
                        if (var4.contains(var12.getName())) {
                           continue;
                        }

                        var4.add(var12.getName());
                     } else if (var12 instanceof ServerMBean) {
                        if (var5.contains(var12.getName())) {
                           continue;
                        }

                        var5.add(var12.getName());
                     } else if (var12 instanceof ClusterMBean) {
                        if (var6.contains(var12.getName())) {
                           continue;
                        }

                        var6.add(var12.getName());
                     } else {
                        if (!(var12 instanceof SAFAgentMBean)) {
                           throw new IllegalArgumentException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var0.getName(), var12.getName(), var12.getClass().getName(), var9.getName()).getMessage());
                        }

                        if (var7.contains(var12.getName())) {
                           continue;
                        }

                        var7.add(var12.getName());
                     }

                     boolean var13 = false;
                     TargetMBean var14 = null;

                     for(int var15 = 0; var15 < var2.length; ++var15) {
                        TargetMBean var16 = var2[var15];
                        if (isTargetASubTarget(var16, var12)) {
                           var13 = true;
                           break;
                        }

                        if (var14 == null) {
                           var14 = var16;
                        }
                     }

                     if (!var13) {
                        String var17;
                        if (var1 instanceof SubDeploymentMBean) {
                           var17 = JMSBeanHelper.getDecoratedName(var0.getName(), var1.getName());
                        } else {
                           var17 = var0.getName();
                        }

                        String var18 = var14 == null ? "<no-targets>" : var14.getName();
                        throw new IllegalArgumentException(JMSExceptionLogger.logInvalidSubTargetingLoggable(var0.getName(), var17, var9.getName(), var12.getName(), var18).getMessage());
                     }
                  }
               }

            }
         }
      }
   }

   private static void validateDeliveryMode(DestinationBean var0, JMSServerMBean var1) {
      if (var0.getDeliveryParamsOverrides().getDeliveryMode().equals("Persistent")) {
         throw new IllegalArgumentException(JMSExceptionLogger.logDeliveryModeMismatch2Loggable(var0.getName(), var1.getName()).getMessage());
      }
   }

   public static void validateTargeting(JMSBean var0, BasicDeploymentMBean var1, TargetInfoMBean var2) throws IllegalArgumentException {
      validateJMSModuleName(var1);
      validateAppDeploymentFileName(var1);
      int var4;
      if (var2 != null) {
         validateTargetingHierarchy(var1, var2);
         TargetMBean[] var3 = var2.getTargets();

         for(var4 = 0; var4 < var3.length; ++var4) {
            TargetMBean var5 = var3[var4];
            if (!(var5 instanceof ServerMBean) && !(var5 instanceof ClusterMBean)) {
               throw new IllegalArgumentException(JMSExceptionLogger.logInvalidModuleTargetLoggable(var1.getName(), var5.getName(), var5.getClass().getName()).getMessage());
            }
         }
      }

      HashSet var18 = new HashSet();

      Object var7;
      for(var4 = 0; var4 < 2; ++var4) {
         Object var20;
         switch (var4) {
            case 0:
               var20 = var0.getQueues();
               break;
            case 1:
               var20 = var0.getTopics();
               break;
            default:
               throw new AssertionError("Unknown JMSServer type: " + var4);
         }

         if (var20 != null) {
            for(int var6 = 0; var6 < ((Object[])var20).length; ++var6) {
               var7 = ((Object[])var20)[var6];
               String var8 = ((TargetableBean)var7).getSubDeploymentName();
               var18.add(var8);
            }
         }
      }

      DomainMBean var19 = null;
      Map var21 = null;
      DistributedTopicBean[] var22 = var0.getDistributedTopics();
      var7 = var0.getDistributedQueues();

      while(true) {
         int var24;
         for(var24 = 0; var24 < ((Object[])var7).length; ++var24) {
            if (!hashRouting((DistributedDestinationBean)((Object[])var7)[var24])) {
               if (var19 == null) {
                  var19 = JMSBeanHelper.getDomain(var1);
               }

               if (var21 == null) {
                  var21 = validatePathServicesInternal(var19);
               }

               validatePathServiceDistributedDestination(var19, var21, var0, (DistributedDestinationBean)((Object[])var7)[var24], var1);
            }
         }

         if (var7 == var22) {
            if (var2 == null) {
               return;
            }

            SubDeploymentMBean[] var23 = getSubDeploymentsFromTargetInfo(var2);

            for(var24 = 0; var24 < var23.length; ++var24) {
               SubDeploymentMBean var9 = var23[var24];
               String var10 = var9.getName();
               if (var18.contains(var10)) {
                  TargetMBean[] var11 = var9.getTargets();
                  if (var11 != null && var11.length > 0) {
                     if (var11.length > 1) {
                        throw new IllegalArgumentException(JMSExceptionLogger.logInvalidDeploymentTargetLoggable(var10, var1.getName()).getMessage());
                     }

                     TargetMBean var12 = var11[0];
                     if (!(var12 instanceof JMSServerMBean)) {
                        throw new IllegalArgumentException(JMSExceptionLogger.logInvalidDeploymentTargetLoggable(var10, var1.getName()).getMessage());
                     }

                     JMSServerMBean var13 = (JMSServerMBean)var12;
                     if (!var13.getStoreEnabled()) {
                        QueueBean[] var14 = var0.getQueues();

                        for(int var15 = 0; var15 < var14.length; ++var15) {
                           QueueBean var16 = var14[var15];
                           if (var16.getSubDeploymentName().equals(var10)) {
                              validateDeliveryMode(var16, var13);
                           }
                        }

                        TopicBean[] var29 = var0.getTopics();

                        for(int var30 = 0; var30 < var29.length; ++var30) {
                           TopicBean var17 = var29[var30];
                           if (var17.getSubDeploymentName().equals(var10)) {
                              validateDeliveryMode(var17, var13);
                           }
                        }
                     }
                  }
               }
            }

            QueueBean[] var26 = var0.getQueues();
            if (var26 != null) {
               for(int var25 = 0; var25 < var26.length; ++var25) {
                  validateErrorDestination(var26[var25], var2, false);
               }
            }

            TopicBean[] var27 = var0.getTopics();
            if (var22 != null) {
               for(int var28 = 0; var28 < var27.length; ++var28) {
                  validateErrorDestination(var27[var28], var2, false);
               }
            }

            validateDistributedDestinations(var2, var1, var0);
            validateUniformDistributedDestinations(var23, var1, var0);
            validateImportedDestinationGroups(var23, var1, var0);
            return;
         }

         var7 = var22;
      }
   }

   public static void validateJMSSystemResource(JMSSystemResourceMBean var0) throws IllegalArgumentException {
      if (var0 != null) {
         validateJMSModuleName(var0);
         JMSBean var1 = var0.getJMSResource();
         if (var1 != null) {
            validateJMSSystemResourceModuleDescriptorFileName((HashMap)null, var0);
            validateTargeting(var1, var0, var0);
            if (var0 instanceof JMSInteropModuleMBean) {
               UniformDistributedQueueBean[] var2 = var1.getUniformDistributedQueues();
               if (var2.length > 0) {
                  UniformDistributedQueueBean var11 = var2[0];
                  throw new IllegalArgumentException(JMSExceptionLogger.logInteropUDQLoggable(var11.getName()).getMessage());
               }

               UniformDistributedTopicBean[] var4 = var1.getUniformDistributedTopics();
               if (var4.length > 0) {
                  UniformDistributedTopicBean var10 = var4[0];
                  throw new IllegalArgumentException(JMSExceptionLogger.logInteropUDTLoggable(var10.getName()).getMessage());
               }

               SAFImportedDestinationsBean[] var5 = var1.getSAFImportedDestinations();
               if (var5.length > 0) {
                  SAFImportedDestinationsBean var9 = var5[0];
                  throw new IllegalArgumentException(JMSExceptionLogger.logInteropSIDLoggable(var9.getName()).getMessage());
               }

               SAFRemoteContextBean[] var6 = var1.getSAFRemoteContexts();
               if (var6.length > 0) {
                  SAFRemoteContextBean var7 = var6[0];
                  throw new IllegalArgumentException(JMSExceptionLogger.logInteropSRCLoggable(var7.getName()).getMessage());
               }

               SAFErrorHandlingBean[] var8 = var1.getSAFErrorHandlings();
               if (var8.length > 0) {
                  SAFErrorHandlingBean var3 = var8[0];
                  throw new IllegalArgumentException(JMSExceptionLogger.logInteropSEHLoggable(var3.getName()).getMessage());
               }
            }

         }
      }
   }

   public static void validateEntityName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         if (var0.indexOf("!") != -1 || var0.indexOf("#") != -1 || var0.indexOf("$") != -1 || var0.indexOf("%") != -1 || var0.indexOf("^") != -1 || var0.indexOf("&") != -1 || var0.indexOf("*") != -1 || var0.indexOf("(") != -1 || var0.indexOf(")") != -1 || var0.indexOf(",") != -1 || var0.indexOf(":") != -1 || var0.indexOf("?") != -1 || var0.indexOf("+") != -1 || var0.indexOf("=") != -1 || var0.indexOf("\\") != -1) {
            throw new IllegalArgumentException("Invalid Name " + var0 + ", any of these characters !#$%^&*(),:?+=\\ are not allowed to be part of the name value");
         }
      } else {
         throw new IllegalArgumentException("Invalid name: JMS resource name cannot be null or empty");
      }
   }

   private static boolean hashRouting(DistributedDestinationBean var0) {
      return "Hash".equals(var0.getUnitOfOrderRouting());
   }

   private static void validateInteropModule(DomainMBean var0) throws IllegalArgumentException {
      JMSInteropModuleMBean[] var1 = var0.getJMSInteropModules();
      if (var1.length > 1) {
         throw new IllegalArgumentException(JMSExceptionLogger.logMoreThanOneInteropModuleLoggable().getMessage());
      } else {
         if (var1.length > 0) {
            if (!var1[0].getName().equals("interop-jms")) {
               throw new IllegalArgumentException(JMSExceptionLogger.logInvalidInteropModuleLoggable(var1[0].getName()).getMessage());
            }

            String var2 = var1[0].getDescriptorFileName().toLowerCase(Locale.ENGLISH);
            String var3 = var2.replace(File.separatorChar, '/');
            if (!var3.endsWith("jms/interop-jms.xml")) {
               throw new IllegalArgumentException(JMSExceptionLogger.logInvalidInteropModuleLoggable(var1[0].getName()).getMessage());
            }
         }

      }
   }

   private static void conflictsWithAppDeployment(DomainMBean var0, String var1) throws IllegalArgumentException {
      AppDeploymentMBean var2 = var0.lookupAppDeployment(var1);
      if (var2 != null) {
         throw new IllegalArgumentException(JMSExceptionLogger.logDuplicateResourceNameLoggable(var1).getMessage());
      }
   }

   private static void validateJMSSystemResource(DomainMBean var0) throws IllegalArgumentException {
      JMSSystemResourceMBean[] var1 = var0.getJMSSystemResources();
      HashMap var2 = new HashMap();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         conflictsWithAppDeployment(var0, var1[var3].getName());
         validateJMSModuleName(var1[var3]);
         validateJMSSystemResourceModuleDescriptorFileName(var2, var1[var3]);
      }

   }

   private static void validateJMSModuleName(BasicDeploymentMBean var0) throws IllegalArgumentException {
      if (!(var0 instanceof JMSInteropModuleMBean) && var0.getName().equals("interop-jms")) {
         throw new IllegalArgumentException(JMSExceptionLogger.logJMSSystemResourceModuleCannotHaveInteropJmsNameLoggable().getMessage());
      }
   }

   private static void validateAppDeploymentFileName(BasicDeploymentMBean var0) throws IllegalArgumentException {
      if (!(var0 instanceof JMSInteropModuleMBean)) {
         String var1 = var0.getSourcePath();
         File var2 = new File(var1);
         String var3 = var2.getName();
         if ("interop-jms.xml".equalsIgnoreCase(var3)) {
            throw new IllegalArgumentException(JMSExceptionLogger.logJMSDeploymentModuleCannotHaveInteropJmsDescriptorNameLoggable(var0.getName(), var0.getSourcePath()).getMessage());
         }
      }
   }

   private static void validateJMSSystemResourceModuleDescriptorFileName(HashMap var0, JMSSystemResourceMBean var1) throws IllegalArgumentException {
      if (var1 != null) {
         String var2 = null;
         String var3 = var1.getDescriptorFileName();
         File var4 = new File(var3);
         String var5 = var4.getName();
         if (var3 != null) {
            var2 = var3.replace(File.separatorChar, '/');
            if (!var2.toLowerCase(Locale.ENGLISH).endsWith("-jms.xml")) {
               throw new IllegalArgumentException(JMSExceptionLogger.logInvalidJMSSystemResourceModuleDescriptorFileNameLoggable(var1.getName(), var3).getMessage());
            }

            if (!(var1 instanceof JMSInteropModuleMBean) && "interop-jms.xml".equalsIgnoreCase(var5)) {
               throw new IllegalArgumentException(JMSExceptionLogger.logJMSDeploymentModuleCannotHaveInteropJmsDescriptorNameLoggable(var1.getName(), var1.getDescriptorFileName()).getMessage());
            }

            if (var0 != null) {
               if (var2.startsWith("./")) {
                  var2 = var2.substring(2, var2.length());
               }

               if (var0.get(var2) != null) {
                  throw new IllegalArgumentException(JMSExceptionLogger.logInvalidJMSSystemResourceModuleDescriptorFileNameLoggable(var1.getName(), var3).getMessage());
               }

               var0.put(var2, var1.getName());
            }
         }

      }
   }

   public static void validateJMSDomain(DomainMBean var0) throws IllegalArgumentException {
      validateInteropModule(var0);
      validateJMSSystemResource(var0);
      validatePathServicesInternal(var0);
   }

   private static Map validatePathServicesInternal(DomainMBean var0) throws IllegalArgumentException {
      PathServiceMBean[] var1 = var0.getPathServices();
      HashMap var2 = new HashMap();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         TargetMBean[] var4 = var1[var3].getTargets();
         if (var4.length != 1) {
            throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is not targetted to a single server");
         }

         ServerMBean var5 = null;
         ClusterMBean var6 = null;
         boolean var7 = false;
         if (var4[0] instanceof ServerMBean) {
            var5 = (ServerMBean)((ServerMBean)var4[0]);
            var6 = var5.getCluster();
         } else {
            if (!(var4[0] instanceof MigratableTargetMBean)) {
               throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is not targetted to a server, it is targetted to " + var4[0]);
            }

            var7 = true;
            var6 = ((MigratableTargetMBean)var4[0]).getCluster();
         }

         if (var7) {
            PersistentStoreMBean var8 = var1[var3].getPersistentStore();
            if (var8 == null) {
               throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is targetted to Migratable Target " + var4[0].getName() + ", it cannot use the default persistent store." + " Please configure a custom persistent store, that is also targeted" + " to the same migratable target as PathService");
            }

            TargetMBean[] var9 = var8.getTargets();
            if (var9.length != 1 || var9[0] != var4[0]) {
               throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is targetted to Migratable Target " + var4[0].getName() + ", but persistent store it uses is not targeted to the migratable target as PathService");
            }
         }

         if (var6 == null) {
            if (var5 != null) {
               throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is targetted to " + var5.getName() + ", but that sever is not in a cluster");
            }

            throw new IllegalArgumentException("PathService " + var1[var3].getName() + " is not targetted to valid targets");
         }

         PathServiceMBean var10 = (PathServiceMBean)var2.put(var6.getName(), var1[var3]);
         if (var10 != null) {
            throw new IllegalArgumentException("Both " + var10.getName() + " and " + var1[var3].getName() + " are targetted to cluster " + var6.getName());
         }
      }

      return var2;
   }

   private static TargetMBean[] getDDTargets(JMSBean var0, DistributedDestinationBean var1, BasicDeploymentMBean var2) {
      LinkedList var3 = new LinkedList();
      DistributedDestinationMemberBean[] var5;
      int var6;
      DistributedDestinationMemberBean var7;
      String var8;
      if (var1 instanceof DistributedQueueBean) {
         DistributedQueueBean var4 = (DistributedQueueBean)var1;
         var5 = var4.getDistributedQueueMembers();

         for(var6 = 0; var6 < var5.length; ++var6) {
            var7 = var5[var6];
            var8 = var7.getPhysicalDestinationName();
            QueueBean var9 = var0.lookupQueue(var8);
            if (var9 == null) {
               throw new IllegalArgumentException("Could not find the member bean \"" + var8 + "\" for distributed queue \"" + var1.getName() + "\"");
            }

            var3.add(var9);
         }
      } else {
         DistributedTopicBean var10 = (DistributedTopicBean)var1;
         var5 = var10.getDistributedTopicMembers();

         for(var6 = 0; var6 < var5.length; ++var6) {
            var7 = var5[var6];
            var8 = var7.getPhysicalDestinationName();
            TopicBean var17 = var0.lookupTopic(var8);
            if (var17 == null) {
               throw new IllegalArgumentException("Could not find the member bean \"" + var8 + "\" for distributed topic \"" + var1.getName() + "\"");
            }

            var3.add(var17);
         }
      }

      LinkedList var11 = new LinkedList();
      Iterator var12 = var3.iterator();

      while(var12.hasNext()) {
         DestinationBean var13 = (DestinationBean)var12.next();
         TargetMBean[] var15 = JMSBeanHelper.getSubDeploymentTargets(var13.getSubDeploymentName(), var2);

         for(int var16 = 0; var16 < var15.length; ++var16) {
            var11.add(var15[var16]);
         }
      }

      TargetMBean[] var14 = (TargetMBean[])((TargetMBean[])var11.toArray(new TargetMBean[var11.size()]));
      return var14;
   }

   private static Map validatePathServiceDistributedDestination(DomainMBean var0, Map var1, JMSBean var2, DistributedDestinationBean var3, BasicDeploymentMBean var4) throws IllegalArgumentException {
      if (hashRouting(var3)) {
         return var1;
      } else {
         TargetMBean[] var5 = getDDTargets(var2, var3, var4);
         if (var5.length == 0) {
            return var1;
         } else {
            ClusterMBean var6 = null;

            for(int var8 = 0; var8 < var5.length; ++var8) {
               ClusterMBean var7;
               try {
                  var7 = getClusterFromTarget(var5[var8]);
               } catch (IllegalArgumentException var10) {
                  throw new IllegalArgumentException("Could not extract cluster for JMSDistributedDestination " + var3.getName() + " target " + var5[var8].getName() + ".\n" + var10.toString());
               }

               if (var7 == null) {
                  throw new IllegalArgumentException("JMSDistributedDestination " + var3.getName() + " is targetted to " + var5[var8].getName() + ", but is not a cluster member");
               }

               if (var6 == null) {
                  var6 = var7;
               } else if (var6 != var7) {
                  throw new IllegalArgumentException("JMSDistributedDestination " + var3.getName() + " but is targetted to " + var5[var8].getName() + ", and that is not in targetted cluster " + var6.getName());
               }
            }

            if (var1 == null) {
               var1 = validatePathServicesInternal(var0);
            }

            if (var1.get(var6.getName()) == null) {
               throw new IllegalArgumentException("JMSDistributedDestination " + var3.getName() + " is targetted to cluster " + var6.getName() + ", but that cluster does not have a PathService");
            } else {
               return var1;
            }
         }
      }
   }

   private static ClusterMBean getClusterFromTarget(TargetMBean var0) throws IllegalArgumentException {
      if (var0 instanceof ClusterMBean) {
         return (ClusterMBean)var0;
      } else if (var0 instanceof ServerMBean) {
         return ((ServerMBean)((ServerMBean)var0)).getCluster();
      } else {
         ServerMBean var1;
         if (var0 instanceof MigratableTargetMBean) {
            var1 = ((MigratableTargetMBean)var0).getHostingServer();
         } else {
            if (var0 instanceof JMSServerMBean) {
               TargetMBean[] var2 = ((JMSServerMBean)var0).getTargets();
               if (var2.length == 0) {
                  throw new IllegalArgumentException("no targets for JMSServer " + var0.getName());
               }

               ClusterMBean var3 = getClusterFromTarget(var2[0]);

               for(int var4 = 1; var4 < var2.length; ++var4) {
                  ClusterMBean var5 = getClusterFromTarget(var2[var4]);
                  if (var3 != var5) {
                     String var6 = var3 == null ? null : var3.getName();
                     String var7 = var2[var4] == null ? null : var2[var4].getName();
                     String var8 = var5 == null ? null : var5.getName();
                     throw new IllegalArgumentException("First target for " + var0.getName() + " is in cluster " + var6 + " but target " + var7 + " is in cluster " + var8);
                  }
               }

               return var3;
            }

            var1 = null;
         }

         if (var1 == null) {
            throw new IllegalArgumentException("could not find server for " + var0.getName() + " the target class is " + var0.getClass().getName());
         } else {
            return var1.getCluster();
         }
      }
   }

   private static void validateConnectionFactories(JMSBean var0) {
      JMSConnectionFactoryBean[] var1 = var0.getConnectionFactories();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         JMSConnectionFactoryBean var3 = var1[var2];
         validateConnectionFactory(var3);
      }

   }

   private static void validateConnectionFactory(JMSConnectionFactoryBean var0) {
      FlowControlParamsBean var1 = var0.getFlowControlParams();
      validateFlowControl(var0.getName(), var1);
      validateTargetingOptions(var0);
      ClientParamsBean var2 = var0.getClientParams();
      validateClient(var2);
      DefaultDeliveryParamsBean var3 = var0.getDefaultDeliveryParams();
      validateDefaultDeliveryParams(var0.getName(), var3);
   }

   private static void validateDefaultDeliveryParams(String var0, DefaultDeliveryParamsBean var1) {
      validateUOOName(var0, var1.getDefaultUnitOfOrder());
   }

   private static void validateUOOName(String var0, String var1) {
      if (var1 != null) {
         if (var1.startsWith(".")) {
            if (!var1.equals(".System")) {
               if (!var1.equals(".Standard")) {
                  throw new IllegalArgumentException("Illegal defaultUnitOfOrderName " + var1 + " starts with \".\" in JMSConnectionFactory " + var0);
               }
            }
         } else {
            validateCFJNDIName(var1);
         }
      }
   }

   private static void validateFlowControl(String var0, FlowControlParamsBean var1) {
      if (var1.getFlowSteps() > var1.getFlowInterval()) {
         throw new IllegalArgumentException(JMSExceptionLogger.logFlowIntervalLoggable(var0, var1.getFlowSteps(), var1.getFlowInterval()).getMessage());
      } else if (var1.getFlowMinimum() >= var1.getFlowMaximum()) {
         throw new IllegalArgumentException(JMSExceptionLogger.logFlowLimitsLoggable(var0, var1.getFlowMinimum(), var1.getFlowMaximum()).getMessage());
      }
   }

   private static void validateClient(ClientParamsBean var0) {
      int var1 = var0.getMessagesMaximum();
      if (var1 < -1 || var1 == 0) {
         throw new IllegalArgumentException(JMSTextTextFormatter.getInstance().getInvalidJMSMessagesMaximum());
      }
   }

   public static void validateSessionPoolSessionsMaximum(int var0) {
      if (var0 < -1 || var0 == 0) {
         throw new IllegalArgumentException(JMSExceptionLogger.logBadSessionsMaxLoggable(var0).getMessage());
      }
   }

   private static void validateSAFErrorDestinationTargeting(String var0, SAFErrorHandlingBean var1, SubDeploymentMBean var2) {
      if (var1 != null) {
         SAFDestinationBean var3 = var1.getSAFErrorDestination();
         if (var3 != null) {
            SAFImportedDestinationsBean var4 = (SAFImportedDestinationsBean)((DescriptorBean)var3).getParentBean();
            if (!var2.getName().equals(var4.getSubDeploymentName())) {
               throw new IllegalArgumentException(JMSExceptionLogger.logBadErrorDestinationLoggable(var0, var1.getName(), var3.getName()).getMessage());
            }
         }
      }
   }

   private static void validateImportedDestinations(SAFImportedDestinationsBean var0, SubDeploymentMBean var1, JMSBean var2) {
      if (var0.getSAFErrorHandling() != null) {
         validateSAFErrorDestinationTargeting(var0.getName(), getErrorHandlingBean(var2, var0.getName(), var0.getSAFErrorHandling().getName()), var1);
      }

      SAFQueueBean[] var3 = var0.getSAFQueues();

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         SAFQueueBean var5 = var3[var4];
         if (var5.getSAFErrorHandling() != null) {
            validateSAFErrorDestinationTargeting(var5.getName(), getErrorHandlingBean(var2, var5.getName(), var5.getSAFErrorHandling().getName()), var1);
         }
      }

      SAFTopicBean[] var6 = var0.getSAFTopics();

      for(var4 = 0; var4 < var6.length; ++var4) {
         SAFTopicBean var7 = var6[var4];
         if (var7.getSAFErrorHandling() != null) {
            validateSAFErrorDestinationTargeting(var7.getName(), getErrorHandlingBean(var2, var7.getName(), var7.getSAFErrorHandling().getName()), var1);
         }
      }

   }

   private static SAFErrorHandlingBean getErrorHandlingBean(JMSBean var0, String var1, String var2) {
      if (var2 == null) {
         return null;
      } else {
         SAFErrorHandlingBean var3 = var0.lookupSAFErrorHandling(var2);
         if (var3 == null) {
            throw new IllegalArgumentException(JMSExceptionLogger.logErrorHandlingNotFoundLoggable(var1, var2).getMessage());
         } else {
            return var3;
         }
      }
   }

   private static void validateForeignServers(JMSBean var0) {
      ForeignServerBean[] var1 = var0.getForeignServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ForeignServerBean var3 = var1[var2];
         validateForeignServer(var3);
      }

   }

   private static void validateForeignServer(ForeignServerBean var0) {
      boolean var1 = false;
      validateTargetingOptions(var0);
      String var2 = var0.getConnectionURL();
      if (var2 != null && !var2.trim().equals("")) {
         var1 = true;
      }

      ForeignDestinationBean[] var3 = var0.getForeignDestinations();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (!var1 && var3[var4].getLocalJNDIName() != null && var3[var4].getRemoteJNDIName() != null && var3[var4].getLocalJNDIName().equals(var3[var4].getRemoteJNDIName())) {
            throw new IllegalArgumentException(JMSExceptionLogger.logInvalidForeignServerLoggable(var0.getName(), var3[var4].getName(), "Foreign Destination", var3[var4].getLocalJNDIName()).getMessage());
         }
      }

      ForeignConnectionFactoryBean[] var6 = var0.getForeignConnectionFactories();

      for(int var5 = 0; var5 < var6.length; ++var5) {
         if (!var1 && var6[var5].getLocalJNDIName() != null && var6[var5].getRemoteJNDIName() != null && var6[var5].getLocalJNDIName().equals(var6[var5].getRemoteJNDIName())) {
            throw new IllegalArgumentException(JMSExceptionLogger.logInvalidForeignServerLoggable(var0.getName(), var6[var5].getName(), "Foreign Connection Factory", var6[var5].getLocalJNDIName()).getMessage());
         }
      }

   }

   private static void validateTargetingOptions(TargetableBean var0) throws IllegalArgumentException {
      if (var0.isDefaultTargetingEnabled() && var0.isSet("SubDeploymentName")) {
         throw new IllegalArgumentException(JMSExceptionLogger.logConflictingTargetingInformationLoggable(var0.getName()).getMessage());
      }
   }
}
