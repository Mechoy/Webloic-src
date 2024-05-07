package weblogic.jms.extensions;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFQueueBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.j2ee.descriptor.wl.SAFTopicBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.common.JMSEditHelper;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditTimedOutException;
import weblogic.management.mbeanservers.edit.NotEditorException;
import weblogic.management.mbeanservers.edit.ValidationException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JMSModuleHelper extends JMSRuntimeHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static DestinationBean findDestinationBean(String var0, JMSBean var1) {
      Object var2 = null;
      if ((var2 = var1.lookupQueue(var0)) == null) {
         var2 = var1.lookupTopic(var0);
      }

      return (DestinationBean)var2;
   }

   public static DestinationBean[] findAllInheritedDestinations(String var0, JMSBean var1) {
      QueueBean[] var2 = findAllInheritedQueueBeans(var0, var1);
      TopicBean[] var3 = findAllInheritedTopicBeans(var0, var1);
      ArrayList var4 = new ArrayList();
      int var5;
      if (var2 != null) {
         for(var5 = 0; var5 < var2.length; ++var5) {
            var4.add(var2[var5]);
         }
      }

      if (var3 != null) {
         for(var5 = 0; var5 < var3.length; ++var5) {
            var4.add(var3[var5]);
         }
      }

      return (DestinationBean[])((DestinationBean[])var4.toArray(new DestinationBean[0]));
   }

   public static QueueBean[] findAllInheritedQueueBeans(String var0, JMSBean var1) {
      ArrayList var2 = new ArrayList();
      QueueBean[] var3 = var1.getQueues();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         TemplateBean var5 = var3[var4].getTemplate();
         String var6 = var5 == null ? null : var5.getName();
         if (var6 != null && var6.equals(var0)) {
            var2.add(var3[var4]);
         }
      }

      return (QueueBean[])((QueueBean[])var2.toArray(new QueueBean[0]));
   }

   public static TopicBean[] findAllInheritedTopicBeans(String var0, JMSBean var1) {
      ArrayList var2 = new ArrayList();
      TopicBean[] var3 = var1.getTopics();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         TemplateBean var5 = var3[var4].getTemplate();
         String var6 = var5 == null ? null : var5.getName();
         if (var6 != null && var6.equals(var0)) {
            var2.add(var3[var4]);
         }
      }

      return (TopicBean[])((TopicBean[])var2.toArray(new TopicBean[0]));
   }

   public static JMSSystemResourceMBean findJMSSystemResource(Context var0, String var1) throws JMSException {
      if (var1 == null) {
         return null;
      } else {
         JMSSystemResourceMBean var2 = null;
         ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
         DomainMBean var4 = beginEditSession(var3);
         boolean var5 = false;
         var2 = findJMSSystemResource(var4, var1);
         endEditSession(var3, var1, var5);
         return var2;
      }
   }

   public static JMSSystemResourceMBean findJMSSystemResource(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      return var1.lookupJMSSystemResource(var0);
   }

   public static void createJMSSystemResource(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         createJMSSystemResource(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var1, var5);
      }

   }

   public static void createJMSSystemResource(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var1 != null && !var1.trim().equals("")) {
         try {
            TargetMBean[] var3 = targetNames2TargetMBeans(var0, var2);
            if (!(var3[0] instanceof MigratableTargetMBean) && !(var3[0] instanceof JMSServerMBean)) {
               JMSSystemResourceMBean var4 = var0.createJMSSystemResource(var1);
               var4.setTargets(var3);
            } else {
               throw new JMSException("ERROR: MigratableTarget and JMSServer cannot be set as target for JMSSystemResource");
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInDomainLoggable(var0.getName(), "JMSSystemResource", var1).getMessage(), var5);
         }
      } else {
         throw new JMSException("ERROR: resourceName cannot be null or empty");
      }
   }

   public static void deleteJMSSystemResource(Context var0, String var1) throws JMSException {
      ConfigurationManagerMBean var2 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var3 = beginEditSession(var2);
      boolean var4 = false;

      try {
         deleteJMSSystemResource(var3, var1);
         var4 = true;
      } finally {
         endEditSession(var2, var1, var4);
      }

   }

   public static void deleteJMSSystemResource(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSSystemResourceMBean var2 = findJMSSystemResource(var0, var1);
            var0.destroyJMSSystemResource(var2);
         } catch (Exception var3) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromDomainLoggable(var0.getName(), "JMSSystemResource", var1).getMessage(), var3);
         }
      }
   }

   public static void createTemplate(Context var0, String var1, String var2) throws JMSException {
      createTemplate((Context)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createTemplate(DomainMBean var0, String var1, String var2) throws JMSException {
      createTemplate((DomainMBean)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createTemplate(Context var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         createTemplate(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var1, var6);
      }

   }

   public static void createTemplate(DomainMBean var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            TemplateBean var5 = var4.createTemplate(var2);
            if (var3 != null) {
               var3.modify(var5);
            }

         } catch (Exception var6) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "Template", var2).getMessage(), var6);
         }
      } else {
         throw new JMSException("ERROR: templateName cannot be null or empty");
      }
   }

   public static void deleteTemplate(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteTemplate(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteTemplate(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            TemplateBean var4 = var3.lookupTemplate(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "Template", var2).getMessage());
            } else {
               var3.destroyTemplate(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "Template", var2).getMessage(), var5);
         }
      }
   }

   public static void createQuota(Context var0, String var1, String var2, String var3) throws JMSException {
      createQuota((Context)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createQuota(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      createQuota((DomainMBean)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createQuota(Context var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      ConfigurationManagerMBean var5 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var6 = beginEditSession(var5);
      boolean var7 = false;

      try {
         createQuota(var6, var1, var2, var3, var4);
         var7 = true;
      } finally {
         endEditSession(var5, var2, var7);
      }

   }

   public static void createQuota(DomainMBean var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSServerMBean var5 = var0.lookupJMSServer(var3);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "JMSServer", var3).getMessage());
            } else {
               findJMSSystemResource(var0, var1);
               JMSBean var7 = getJMSBean(var0, var1);
               QuotaBean var8 = var7.createQuota(var2);
               if (var4 != null) {
                  var4.modify(var8);
               }

            }
         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "Quota", var2).getMessage(), var9);
         }
      } else {
         throw new JMSException("ERROR: quotaName cannot be null or empty");
      }
   }

   public static void deleteQuota(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteQuota(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteQuota(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            QuotaBean var4 = var3.lookupQuota(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "Quota", var2).getMessage());
            } else {
               var3.destroyQuota(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "Quota", var2).getMessage(), var5);
         }
      }
   }

   public static void createDestinationKey(Context var0, String var1, String var2, String var3, String var4, String var5) throws JMSException {
      createDestinationKey((Context)var0, var1, var2, var3, var4, var5, (JMSNamedEntityModifier)null);
   }

   public static void createDestinationKey(DomainMBean var0, String var1, String var2, String var3, String var4, String var5) throws JMSException {
      createDestinationKey((DomainMBean)var0, var1, var2, var3, var4, var5, (JMSNamedEntityModifier)null);
   }

   public static void createDestinationKey(Context var0, String var1, String var2, String var3, String var4, String var5, JMSNamedEntityModifier var6) throws JMSException {
      ConfigurationManagerMBean var7 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var8 = beginEditSession(var7);
      boolean var9 = false;

      try {
         createDestinationKey(var8, var1, var2, var3, var4, var5, var6);
         var9 = true;
      } finally {
         endEditSession(var7, var2, var9);
      }

   }

   public static void createDestinationKey(DomainMBean var0, String var1, String var2, String var3, String var4, String var5, JMSNamedEntityModifier var6) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSBean var7 = getJMSBean(var0, var1);
            DestinationKeyBean var8 = var7.createDestinationKey(var2);
            var8.setProperty(var3);
            var8.setKeyType(var4);
            var8.setSortOrder(var5);
            if (var6 != null) {
               var6.modify(var8);
            }

         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "DestinationKey", var2).getMessage(), var9);
         }
      } else {
         throw new JMSException("ERROR: destinationKeyName cannot be null or empty");
      }
   }

   public static void deleteDestinationKey(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteDestinationKey(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteDestinationKey(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            DestinationKeyBean var4 = var3.lookupDestinationKey(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DestinationKey", var2).getMessage());
            } else {
               var3.destroyDestinationKey(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "DestinationKey", var2).getMessage(), var5);
         }
      }
   }

   public static void createConnectionFactory(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createConnectionFactory((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createConnectionFactory(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createConnectionFactory((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createConnectionFactory(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createConnectionFactory(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var1, var8);
      }

   }

   public static void createConnectionFactory(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            TargetMBean[] var6 = targetNames2TargetMBeans(var0, var4);
            if (var6[0] instanceof MigratableTargetMBean) {
               throw new JMSException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var1, var4, "MigratableTargetMBean", var2).getMessage());
            } else {
               JMSSystemResourceMBean var7 = findJMSSystemResource(var0, var1);
               JMSBean var8 = var7.getJMSResource();
               JMSConnectionFactoryBean var9 = var8.createConnectionFactory(var2);
               var9.setJNDIName(var3);
               if (var5 != null) {
                  var5.modify(var9);
               }

               String var10 = var9.getSubDeploymentName();
               if (var10 == null) {
                  var10 = var2;
               }

               SubDeploymentMBean var11 = findOrCreateSubDeployment(var7, var10);
               var11.setTargets(var6);
            }
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "ConnectionFactory", var2).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: factoryName cannot be null or empty");
      }
   }

   public static void deleteConnectionFactory(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteConnectionFactory(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteConnectionFactory(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            JMSConnectionFactoryBean var4 = var3.lookupConnectionFactory(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ConnectionFactory", var2).getMessage());
            } else {
               var3.destroyConnectionFactory(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "ConnectionFactory", var2).getMessage(), var5);
         }
      }
   }

   public static void createQueue(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createQueue((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createQueue(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createQueue((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createQueue(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createQueue(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createQueue(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.equals("")) {
         try {
            JMSServerMBean var6 = var0.lookupJMSServer(var2);
            if (var6 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "JMSServer", var2).getMessage());
            } else {
               JMSSystemResourceMBean var7 = findJMSSystemResource(var0, var1);
               JMSBean var8 = var7.getJMSResource();
               QueueBean var9 = var8.createQueue(var3);
               var9.setJNDIName(var4);
               if (var5 != null) {
                  var5.modify(var9);
               }

               String var10 = var9.getSubDeploymentName();
               if (var10 == null) {
                  var10 = var3;
               }

               SubDeploymentMBean var11 = findOrCreateSubDeployment(var7, var10);
               var11.addTarget(var6);
            }
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "Queue", var3).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: queueName cannot be null or empty");
      }
   }

   public static void deleteQueue(Context var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "JMSQueue");
   }

   public static void deleteQueue(DomainMBean var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "JMSQueue");
   }

   public static void createTopic(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createTopic((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createTopic(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createTopic((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createTopic(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createTopic(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createTopic(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         try {
            JMSServerMBean var6 = var0.lookupJMSServer(var2);
            if (var6 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "JMSServer", var2).getMessage());
            } else {
               JMSSystemResourceMBean var7 = findJMSSystemResource(var0, var1);
               JMSBean var8 = var7.getJMSResource();
               TopicBean var9 = var8.createTopic(var3);
               var9.setJNDIName(var4);
               if (var5 != null) {
                  var5.modify(var9);
               }

               String var10 = var9.getSubDeploymentName();
               if (var10 == null) {
                  var10 = var3;
               }

               SubDeploymentMBean var11 = findOrCreateSubDeployment(var7, var10);
               var11.addTarget(var6);
            }
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "Topic", var3).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: topicName cannot be null or empty");
      }
   }

   public static void deleteTopic(Context var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "JMSTopic");
   }

   public static void deleteTopic(DomainMBean var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "JMSTopic");
   }

   public static void createDistributedQueue(Context var0, String var1, String var2, String var3) throws JMSException {
      createDistributedQueue((Context)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedQueue(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      createDistributedQueue((DomainMBean)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedQueue(Context var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      ConfigurationManagerMBean var5 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var6 = beginEditSession(var5);
      boolean var7 = false;

      try {
         createDistributedQueue(var6, var1, var2, var3, var4);
         var7 = true;
      } finally {
         endEditSession(var5, var2, var7);
      }

   }

   public static void createDistributedQueue(DomainMBean var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSSystemResourceMBean var5 = findJMSSystemResource(var0, var1);
            JMSBean var6 = var5.getJMSResource();
            DistributedQueueBean var7 = var6.createDistributedQueue(var2);
            var7.setJNDIName(var3);
            if (var4 != null) {
               var4.modify(var7);
            }

         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage(), var8);
         }
      } else {
         throw new JMSException("ERROR: distributedQueueName cannot be null or empty");
      }
   }

   public static void deleteDistributedQueue(Context var0, String var1, String var2, boolean var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteDistributedQueue(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

   }

   public static void deleteDistributedQueue(DomainMBean var0, String var1, String var2, boolean var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            DistributedQueueBean var5 = var4.lookupDistributedQueue(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage());
            } else {
               if (var3) {
                  DistributedDestinationMemberBean[] var6 = var5.getDistributedQueueMembers();
                  if (var6 != null) {
                     for(int var7 = 0; var7 < var6.length; ++var7) {
                        var5.destroyDistributedQueueMember(var6[var7]);
                     }
                  }
               }

               var4.destroyDistributedQueue(var5);
            }
         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage(), var8);
         }
      }
   }

   public static void createDistributedQueueMember(Context var0, String var1, String var2, String var3, int var4) throws JMSException {
      createDistributedQueueMember((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedQueueMember(DomainMBean var0, String var1, String var2, String var3, int var4) throws JMSException {
      createDistributedQueueMember((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedQueueMember(Context var0, String var1, String var2, String var3, int var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createDistributedQueueMember(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createDistributedQueueMember(DomainMBean var0, String var1, String var2, String var3, int var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         try {
            JMSBean var6 = getJMSBean(var0, var1);
            DistributedQueueBean var7 = var6.lookupDistributedQueue(var2);
            if (var7 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage());
            } else {
               DistributedDestinationMemberBean var8 = var7.createDistributedQueueMember(var3);
               var8.setWeight(var4);
               if (var5 != null) {
                  var5.modify(var8);
               }

            }
         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "DistributedQueueMember", var3).getMessage(), var9);
         }
      } else {
         throw new JMSException("ERROR: distributedQueueMemberName cannot be null or empty");
      }
   }

   public static void deleteDistributedQueueMember(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteDistributedQueueMember(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteDistributedQueueMember(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            DistributedQueueBean var5 = var4.lookupDistributedQueue(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage());
            } else {
               DistributedDestinationMemberBean var6 = var5.lookupDistributedQueueMember(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueueMember", var3).getMessage());
               } else {
                  var5.destroyDistributedQueueMember(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "DistributedQueueMember", var3).getMessage(), var7);
         }
      }
   }

   public static String[] getDistributedQueueMemberNames(Context var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         var3 = getDistributedQueueMemberNames(var5, var1, var1);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

      return var3;
   }

   public static String[] getDistributedQueueMemberNames(DomainMBean var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      JMSBean var4 = getJMSBean(var0, var1);
      DistributedQueueBean var5 = var4.lookupDistributedQueue(var2);
      if (var5 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage());
      } else {
         DistributedDestinationMemberBean[] var6 = var5.getDistributedQueueMembers();
         var3 = new String[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var3[var7] = var6[var7].getPhysicalDestinationName();
         }

         return var3;
      }
   }

   public static String[] getDistributedQueueMemberJndiNames(Context var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         var3 = getDistributedQueueMemberJndiNames(var5, var1, var2);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

      return var3;
   }

   public static String[] getDistributedQueueMemberJndiNames(DomainMBean var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      JMSBean var4 = getJMSBean(var0, var1);
      DistributedQueueBean var5 = var4.lookupDistributedQueue(var2);
      if (var5 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedQueue", var2).getMessage());
      } else {
         DistributedDestinationMemberBean[] var6 = var5.getDistributedQueueMembers();
         var3 = new String[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            QueueBean var8 = var4.lookupQueue(var6[var7].getPhysicalDestinationName());
            if (var8 != null) {
               var3[var7] = var8.getJNDIName();
            }
         }

         return var3;
      }
   }

   public static void createDistributedTopic(Context var0, String var1, String var2, String var3) throws JMSException {
      createDistributedTopic((Context)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedTopic(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      createDistributedTopic((DomainMBean)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedTopic(Context var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      ConfigurationManagerMBean var5 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var6 = beginEditSession(var5);
      boolean var7 = false;

      try {
         createDistributedTopic(var6, var1, var2, var3, var4);
         var7 = true;
      } finally {
         endEditSession(var5, var2, var7);
      }

   }

   public static void createDistributedTopic(DomainMBean var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSSystemResourceMBean var5 = findJMSSystemResource(var0, var1);
            JMSBean var6 = var5.getJMSResource();
            DistributedTopicBean var7 = var6.createDistributedTopic(var2);
            var7.setJNDIName(var3);
            if (var4 != null) {
               var4.modify(var7);
            }

         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage(), var8);
         }
      } else {
         throw new JMSException("ERROR: distributedTopicName cannot be null or empty");
      }
   }

   public static void deleteDistributedTopic(Context var0, String var1, String var2, boolean var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteDistributedTopic(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

   }

   public static void deleteDistributedTopic(DomainMBean var0, String var1, String var2, boolean var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            DistributedTopicBean var5 = var4.lookupDistributedTopic(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage());
            } else {
               if (var3) {
                  DistributedDestinationMemberBean[] var6 = var5.getDistributedTopicMembers();
                  if (var6 != null) {
                     for(int var7 = 0; var7 < var6.length; ++var7) {
                        var5.destroyDistributedTopicMember(var6[var7]);
                     }
                  }
               }

               var4.destroyDistributedTopic(var5);
            }
         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage(), var8);
         }
      }
   }

   public static void createDistributedTopicMember(Context var0, String var1, String var2, String var3, int var4) throws JMSException {
      createDistributedTopicMember((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedTopicMember(DomainMBean var0, String var1, String var2, String var3, int var4) throws JMSException {
      createDistributedTopicMember((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createDistributedTopicMember(Context var0, String var1, String var2, String var3, int var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createDistributedTopicMember(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createDistributedTopicMember(DomainMBean var0, String var1, String var2, String var3, int var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         try {
            JMSBean var6 = getJMSBean(var0, var1);
            DistributedTopicBean var7 = var6.lookupDistributedTopic(var2);
            if (var7 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage());
            } else {
               DistributedDestinationMemberBean var8 = var7.createDistributedTopicMember(var3);
               var8.setWeight(var4);
               if (var5 != null) {
                  var5.modify(var8);
               }

            }
         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "DistributedTopicMember", var3).getMessage(), var9);
         }
      } else {
         throw new JMSException("ERROR: distributedTopicMemberName cannot be null or empty");
      }
   }

   public static void deleteDistributedTopicMember(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteDistributedTopicMember(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteDistributedTopicMember(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            DistributedTopicBean var5 = var4.lookupDistributedTopic(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage());
            } else {
               DistributedDestinationMemberBean var6 = var5.lookupDistributedTopicMember(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopicMember", var3).getMessage());
               } else {
                  var5.destroyDistributedTopicMember(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "DistributedTopicMember", var3).getMessage(), var7);
         }
      }
   }

   public static String[] getDistributedTopicMemberNames(Context var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         var3 = getDistributedTopicMemberNames(var5, var1, var2);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

      return var3;
   }

   public static String[] getDistributedTopicMemberNames(DomainMBean var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      JMSBean var4 = getJMSBean(var0, var1);
      DistributedTopicBean var5 = var4.lookupDistributedTopic(var2);
      if (var5 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage());
      } else {
         DistributedDestinationMemberBean[] var6 = var5.getDistributedTopicMembers();
         var3 = new String[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var3[var7] = var6[var7].getPhysicalDestinationName();
         }

         return var3;
      }
   }

   public static void createUniformDistributedTopic(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createUniformDistributedTopic((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createUniformDistributedTopic(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createUniformDistributedTopic((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createUniformDistributedTopic(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createUniformDistributedTopic(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var2, var8);
      }

   }

   public static void createUniformDistributedTopic(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            TargetMBean[] var6 = targetNames2TargetMBeans(var0, var4);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var6[var7] instanceof MigratableTargetMBean) {
                  throw new JMSException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var1, var4, "MigratableTargetMBean", var2).getMessage());
               }
            }

            JMSSystemResourceMBean var13 = findJMSSystemResource(var0, var1);
            JMSBean var8 = var13.getJMSResource();
            UniformDistributedTopicBean var9 = var8.createUniformDistributedTopic(var2);
            var9.setJNDIName(var3);
            if (var5 != null) {
               var5.modify(var9);
            }

            String var10 = var9.getSubDeploymentName();
            if (var10 == null) {
               var10 = var2;
            }

            SubDeploymentMBean var11 = findOrCreateSubDeployment(var13, var10);
            var11.setTargets(var6);
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "UniformDistributedTopic", var2).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: uniformDistributedTopicName cannot be null or empty");
      }
   }

   public static void createUniformDistributedQueue(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createUniformDistributedQueue((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createUniformDistributedQueue(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createUniformDistributedQueue((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createUniformDistributedQueue(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createUniformDistributedQueue(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var2, var8);
      }

   }

   public static void createUniformDistributedQueue(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            TargetMBean[] var6 = targetNames2TargetMBeans(var0, var4);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var6[var7] instanceof MigratableTargetMBean) {
                  throw new JMSException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var1, var4, "MigratableTargetMBean", var2).getMessage());
               }
            }

            JMSSystemResourceMBean var13 = findJMSSystemResource(var0, var1);
            JMSBean var8 = var13.getJMSResource();
            UniformDistributedQueueBean var9 = var8.createUniformDistributedQueue(var2);
            var9.setJNDIName(var3);
            if (var5 != null) {
               var5.modify(var9);
            }

            String var10 = var9.getSubDeploymentName();
            if (var10 == null) {
               var10 = var2;
            }

            SubDeploymentMBean var11 = findOrCreateSubDeployment(var13, var10);
            var11.setTargets(var6);
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "UniformDistributedQueue", var2).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: uniformDistributedQueueName cannot be null or empty");
      }
   }

   public static void deleteUniformDistributedQueue(Context var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "UniformDistributedQueue");
   }

   public static void deleteUniformDistributedQueue(DomainMBean var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "UniformDistributedQueue");
   }

   public static void deleteUniformDistributedTopic(Context var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "UniformDistributedTopic");
   }

   public static void deleteUniformDistributedTopic(DomainMBean var0, String var1, String var2) throws JMSException {
      deleteDestination(var0, var1, var2, "UniformDistributedTopic");
   }

   public static String[] getDistributedTopicMemberJndiNames(Context var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         getDistributedTopicMemberJndiNames(var5, var1, var2);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

      return var3;
   }

   public static String[] getDistributedTopicMemberJndiNames(DomainMBean var0, String var1, String var2) throws JMSException {
      String[] var3 = new String[0];
      JMSBean var4 = getJMSBean(var0, var1);
      DistributedTopicBean var5 = var4.lookupDistributedTopic(var2);
      if (var5 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "DistributedTopic", var2).getMessage());
      } else {
         DistributedDestinationMemberBean[] var6 = var5.getDistributedTopicMembers();
         var3 = new String[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            TopicBean var8 = var4.lookupTopic(var6[var7].getPhysicalDestinationName());
            if (var8 != null) {
               var3[var7] = var8.getJNDIName();
            }
         }

         return var3;
      }
   }

   public static void createForeignServer(Context var0, String var1, String var2, String var3) throws JMSException {
      createForeignServer((Context)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createForeignServer(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      createForeignServer((DomainMBean)var0, var1, var2, var3, (JMSNamedEntityModifier)null);
   }

   public static void createForeignServer(Context var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      ConfigurationManagerMBean var5 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var6 = beginEditSession(var5);
      boolean var7 = false;

      try {
         createForeignServer(var6, var1, var2, var3, var4);
         var7 = true;
      } finally {
         endEditSession(var5, var2, var7);
      }

   }

   public static void createForeignServer(DomainMBean var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            TargetMBean[] var5 = targetNames2TargetMBeans(var0, var3);

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6] instanceof MigratableTargetMBean) {
                  throw new JMSException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var1, var3, "MigratableTargetMBean", var2).getMessage());
               }
            }

            JMSSystemResourceMBean var12 = findJMSSystemResource(var0, var1);
            JMSBean var7 = var12.getJMSResource();
            ForeignServerBean var8 = var7.createForeignServer(var2);
            if (var4 != null) {
               var4.modify(var8);
            }

            String var9 = var8.getSubDeploymentName();
            if (var9 == null) {
               var9 = var2;
            }

            SubDeploymentMBean var10 = findOrCreateSubDeployment(var12, var9);
            var10.setTargets(var5);
         } catch (Exception var11) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage(), var11);
         }
      } else {
         throw new JMSException("ERROR: foreignServerName cannot be null or empty");
      }
   }

   public static void deleteForeignServer(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteForeignServer(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteForeignServer(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            ForeignServerBean var4 = var3.lookupForeignServer(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage());
            } else {
               ForeignConnectionFactoryBean[] var5 = var4.getForeignConnectionFactories();
               if (var5 != null) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     var4.destroyForeignConnectionFactory(var5[var6]);
                  }
               }

               ForeignDestinationBean[] var9 = var4.getForeignDestinations();
               if (var9 != null) {
                  for(int var7 = 0; var7 < var9.length; ++var7) {
                     var4.destroyForeignDestination(var9[var7]);
                  }
               }

               var3.destroyForeignServer(var4);
            }
         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage(), var8);
         }
      }
   }

   public static void createForeignDestination(Context var0, String var1, String var2, String var3, String var4, String var5) throws JMSException {
      createForeignDestination((Context)var0, var1, var2, var3, var4, var5, (JMSNamedEntityModifier)null);
   }

   public static void createForeignDestination(DomainMBean var0, String var1, String var2, String var3, String var4, String var5) throws JMSException {
      createForeignDestination((DomainMBean)var0, var1, var2, var3, var4, var5, (JMSNamedEntityModifier)null);
   }

   public static void createForeignDestination(Context var0, String var1, String var2, String var3, String var4, String var5, JMSNamedEntityModifier var6) throws JMSException {
      ConfigurationManagerMBean var7 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var8 = beginEditSession(var7);
      boolean var9 = false;

      try {
         createForeignDestination(var8, var1, var2, var3, var4, var5, var6);
         var9 = true;
      } finally {
         endEditSession(var7, var3, var9);
      }

   }

   public static void createForeignDestination(DomainMBean var0, String var1, String var2, String var3, String var4, String var5, JMSNamedEntityModifier var6) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         try {
            JMSBean var7 = getJMSBean(var0, var1);
            ForeignServerBean var8 = var7.lookupForeignServer(var2);
            if (var8 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage());
            } else {
               ForeignDestinationBean var9 = var8.createForeignDestination(var3);
               var9.setLocalJNDIName(var4);
               var9.setRemoteJNDIName(var5);
               if (var6 != null) {
                  var6.modify(var9);
               }

            }
         } catch (Exception var10) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "ForeignDestination", var3).getMessage(), var10);
         }
      } else {
         throw new JMSException("ERROR: foreignDestinationName cannot be null or empty");
      }
   }

   public static void deleteForeignDestination(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteForeignDestination(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteForeignDestination(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            ForeignServerBean var5 = var4.lookupForeignServer(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage());
            } else {
               ForeignDestinationBean var6 = var5.lookupForeignDestination(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignDestination", var3).getMessage());
               } else {
                  var5.destroyForeignDestination(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "ForeignDestination", var3).getMessage(), var7);
         }
      }
   }

   public static void createForeignConnectionFactory(Context var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws JMSException {
      createForeignConnectionFactory((Context)var0, var1, var2, var3, var4, var5, var6, var7, (JMSNamedEntityModifier)null);
   }

   public static void createForeignConnectionFactory(DomainMBean var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws JMSException {
      createForeignConnectionFactory((DomainMBean)var0, var1, var2, var3, var4, var5, var6, var7, (JMSNamedEntityModifier)null);
   }

   public static void createForeignConnectionFactory(Context var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, JMSNamedEntityModifier var8) throws JMSException {
      ConfigurationManagerMBean var9 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var10 = beginEditSession(var9);
      boolean var11 = false;

      try {
         createForeignConnectionFactory(var10, var1, var2, var3, var4, var5, var6, var7, var8);
         var11 = true;
      } finally {
         endEditSession(var9, var3, var11);
      }

   }

   public static void createForeignConnectionFactory(DomainMBean var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, JMSNamedEntityModifier var8) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         try {
            JMSBean var9 = getJMSBean(var0, var1);
            ForeignServerBean var10 = var9.lookupForeignServer(var2);
            if (var10 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage());
            } else {
               ForeignConnectionFactoryBean var11 = var10.createForeignConnectionFactory(var3);
               var11.setLocalJNDIName(var4);
               var11.setRemoteJNDIName(var5);
               var11.setUsername(var6);
               var11.setPassword(var7);
               if (var8 != null) {
                  var8.modify(var11);
               }

            }
         } catch (Exception var12) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "ForeignConnectionFactory", var3).getMessage(), var12);
         }
      } else {
         throw new JMSException("ERROR: foreignConnectionFactoryName cannot be null or empty");
      }
   }

   public static void deleteForeignConnectionFactory(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteForeignConnectionFactory(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteForeignConnectionFactory(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            ForeignServerBean var5 = var4.lookupForeignServer(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignServer", var2).getMessage());
            } else {
               ForeignConnectionFactoryBean var6 = var5.lookupForeignConnectionFactory(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "ForeignConnectionFactory", var3).getMessage());
               } else {
                  var5.destroyForeignConnectionFactory(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "ForeignConnectionFactory", var3).getMessage(), var7);
         }
      }
   }

   public static void createSAFImportedDestinations(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFImportedDestinations((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFImportedDestinations(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFImportedDestinations((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFImportedDestinations(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createSAFImportedDestinations(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var2, var8);
      }

   }

   public static void createSAFImportedDestinations(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            TargetMBean[] var6 = targetNames2TargetMBeans(var0, var4);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var6[var7] instanceof MigratableTargetMBean || var6[var7] instanceof JMSServerMBean) {
                  throw new JMSException(JMSExceptionLogger.logInvalidSubDeploymentTargetLoggable(var1, var4, "MigratableTargetMBean", var2).getMessage());
               }
            }

            JMSSystemResourceMBean var14 = findJMSSystemResource(var0, var1);
            JMSBean var8 = var14.getJMSResource();
            SAFRemoteContextBean var9 = var8.lookupSAFRemoteContext(var3);
            if (var9 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFRemoteContext", var3).getMessage());
            } else {
               SAFImportedDestinationsBean var10 = var8.createSAFImportedDestinations(var2);
               var10.setSAFRemoteContext(var9);
               if (var5 != null) {
                  var5.modify(var10);
               }

               String var11 = var10.getSubDeploymentName();
               if (var11 == null) {
                  var11 = var2;
               }

               SubDeploymentMBean var12 = findOrCreateSubDeployment(var14, var11);
               var12.setTargets(var6);
            }
         } catch (Exception var13) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage(), var13);
         }
      } else {
         throw new JMSException("ERROR: safImportedDestinationsName cannot be null or empty");
      }
   }

   public static void deleteSAFImportedDestinations(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteSAFImportedDestinations(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteSAFImportedDestinations(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            SAFImportedDestinationsBean var4 = var3.lookupSAFImportedDestinations(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage());
            } else {
               SAFQueueBean[] var5 = var4.getSAFQueues();
               if (var5 != null) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     var4.destroySAFQueue(var5[var6]);
                  }
               }

               SAFTopicBean[] var9 = var4.getSAFTopics();
               if (var9 != null) {
                  for(int var7 = 0; var7 < var9.length; ++var7) {
                     var4.destroySAFTopic(var9[var7]);
                  }
               }

               var3.destroySAFImportedDestinations(var4);
            }
         } catch (Exception var8) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage(), var8);
         }
      }
   }

   public static void createSAFRemoteContext(Context var0, String var1, String var2) throws JMSException {
      createSAFRemoteContext((Context)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createSAFRemoteContext(DomainMBean var0, String var1, String var2) throws JMSException {
      createSAFRemoteContext((DomainMBean)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createSAFRemoteContext(Context var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         createSAFRemoteContext(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

   }

   public static void createSAFRemoteContext(DomainMBean var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSSystemResourceMBean var4 = findJMSSystemResource(var0, var1);
            JMSBean var5 = var4.getJMSResource();
            SAFRemoteContextBean var6 = var5.createSAFRemoteContext(var2);
            if (var3 != null) {
               var3.modify(var6);
            }

         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFRemoteContext", var2).getMessage(), var7);
         }
      } else {
         throw new JMSException("ERROR: SAFRemoteContextName cannot be null or empty");
      }
   }

   public static void deleteSAFRemoteContext(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteSAFRemoteContext(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteSAFRemoteContext(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            SAFRemoteContextBean var4 = var3.lookupSAFRemoteContext(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFRemoteContext", var2).getMessage());
            } else {
               var3.destroySAFRemoteContext(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFRemoteContext", var2).getMessage(), var5);
         }
      }
   }

   public static void createSAFErrorHandling(Context var0, String var1, String var2) throws JMSException {
      createSAFErrorHandling((Context)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createSAFErrorHandling(DomainMBean var0, String var1, String var2) throws JMSException {
      createSAFErrorHandling((DomainMBean)var0, var1, var2, (JMSNamedEntityModifier)null);
   }

   public static void createSAFErrorHandling(Context var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         createSAFErrorHandling(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

   }

   public static void createSAFErrorHandling(DomainMBean var0, String var1, String var2, JMSNamedEntityModifier var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var2 != null && !var2.trim().equals("")) {
         try {
            JMSSystemResourceMBean var4 = findJMSSystemResource(var0, var1);
            JMSBean var5 = var4.getJMSResource();
            SAFErrorHandlingBean var6 = var5.createSAFErrorHandling(var2);
            if (var3 != null) {
               var3.modify(var6);
            }

         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFErrorHandling", var2).getMessage(), var7);
         }
      } else {
         throw new JMSException("ERROR: SAFErrorHandlingName cannot be null or empty");
      }
   }

   public static void deleteSAFErrorHandling(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deleteSAFErrorHandling(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var2, var5);
      }

   }

   public static void deleteSAFErrorHandling(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var3 = getJMSBean(var0, var1);
            SAFErrorHandlingBean var4 = var3.lookupSAFErrorHandling(var2);
            if (var4 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFErrorHandling", var2).getMessage());
            } else {
               var3.destroySAFErrorHandling(var4);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFErrorHandling", var2).getMessage(), var5);
         }
      }
   }

   public static void createSAFQueue(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFQueue((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFQueue(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFQueue((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFQueue(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createSAFQueue(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createSAFQueue(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         if (var4 != null && !var4.trim().equals("")) {
            try {
               JMSBean var6 = getJMSBean(var0, var1);
               SAFImportedDestinationsBean var7 = var6.lookupSAFImportedDestinations(var2);
               if (var7 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage());
               } else {
                  SAFQueueBean var8 = var7.createSAFQueue(var3);
                  var8.setRemoteJNDIName(var4);
                  if (var5 != null) {
                     var5.modify(var8);
                  }

               }
            } catch (Exception var9) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFQueue", var3).getMessage(), var9);
            }
         } else {
            throw new JMSException("ERROR: RemoteJNDIName of a SAFQueue cannot be null or empty");
         }
      } else {
         throw new JMSException("ERROR: safQueueName cannot be null or empty");
      }
   }

   public static void deleteSAFQueue(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteSAFQueue(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteSAFQueue(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            SAFImportedDestinationsBean var5 = var4.lookupSAFImportedDestinations(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage());
            } else {
               SAFQueueBean var6 = var5.lookupSAFQueue(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFQueue", var3).getMessage());
               } else {
                  var5.destroySAFQueue(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "SAFQueue", var3).getMessage(), var7);
         }
      }
   }

   public static void createSAFTopic(Context var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFTopic((Context)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFTopic(DomainMBean var0, String var1, String var2, String var3, String var4) throws JMSException {
      createSAFTopic((DomainMBean)var0, var1, var2, var3, var4, (JMSNamedEntityModifier)null);
   }

   public static void createSAFTopic(Context var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      ConfigurationManagerMBean var6 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var7 = beginEditSession(var6);
      boolean var8 = false;

      try {
         createSAFTopic(var7, var1, var2, var3, var4, var5);
         var8 = true;
      } finally {
         endEditSession(var6, var3, var8);
      }

   }

   public static void createSAFTopic(DomainMBean var0, String var1, String var2, String var3, String var4, JMSNamedEntityModifier var5) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var3 != null && !var3.trim().equals("")) {
         if (var4 != null && !var4.trim().equals("")) {
            try {
               JMSBean var6 = getJMSBean(var0, var1);
               SAFImportedDestinationsBean var7 = var6.lookupSAFImportedDestinations(var2);
               if (var7 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage());
               } else {
                  SAFTopicBean var8 = var7.createSAFTopic(var3);
                  var8.setRemoteJNDIName(var4);
                  if (var5 != null) {
                     var5.modify(var8);
                  }

               }
            } catch (Exception var9) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInJMSSystemResourceLoggable(var1, "SAFTopic", var3).getMessage(), var9);
            }
         } else {
            throw new JMSException("ERROR: RemoteJNDIName of a SAFTopic cannot be null or empty");
         }
      } else {
         throw new JMSException("ERROR: topicName cannot be null or empty");
      }
   }

   public static void deleteSAFTopic(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteSAFTopic(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var3, var6);
      }

   }

   public static void deleteSAFTopic(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            SAFImportedDestinationsBean var5 = var4.lookupSAFImportedDestinations(var2);
            if (var5 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFImportedDestinations", var2).getMessage());
            } else {
               SAFTopicBean var6 = var5.lookupSAFTopic(var3);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, "SAFTopic", var3).getMessage());
               } else {
                  var5.destroySAFTopic(var6);
               }
            }
         } catch (Exception var7) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, "SAFTopic", var3).getMessage(), var7);
         }
      }
   }

   public static void findAndModifyEntity(Context var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var4 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logInvalidModuleEntityModifierLoggable(var1, var3, var2).getMessage());
      } else {
         ConfigurationManagerMBean var5 = JMSEditHelper.getConfigurationManager(var0);
         DomainMBean var6 = beginEditSession(var5);
         boolean var7 = false;

         try {
            findAndModifyEntity(var6, var1, var2, var3, var4);
            var7 = true;
         } finally {
            endEditSession(var5, var2, var7);
         }

      }
   }

   public static void findAndModifyEntity(DomainMBean var0, String var1, String var2, String var3, JMSNamedEntityModifier var4) throws JMSException {
      if (var4 == null) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logInvalidModuleEntityModifierLoggable(var1, var3, var2).getMessage());
      } else if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var1 != null && !var1.trim().equals("")) {
         if (var2 != null && !var2.trim().equals("")) {
            if (var3 != null && !var3.trim().equals("")) {
               try {
                  JMSBean var5 = getJMSBean(var0, var1);
                  Class var6 = var5.getClass();
                  String var7 = "lookup" + var3;
                  Method var8 = null;
                  Class[] var9 = new Class[]{String.class};
                  Object[] var10 = new Object[]{var2};
                  NamedEntityBean var11 = null;

                  try {
                     var8 = var6.getMethod(var7, var9);
                  } catch (Exception var14) {
                     throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotFindAndModifyEntityFromJMSSystemResourceLoggable(var1, var3, var2).getMessage(), var14);
                  }

                  try {
                     var11 = (NamedEntityBean)var8.invoke(var5, var10);
                  } catch (Exception var13) {
                     throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotFindAndModifyEntityFromJMSSystemResourceLoggable(var1, var3, var2).getMessage(), var13);
                  }

                  if (var11 == null) {
                     throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, var3, var2).getMessage());
                  } else {
                     var4.modify(var11);
                  }
               } catch (Exception var15) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotFindAndModifyEntityFromJMSSystemResourceLoggable(var1, var3, var2).getMessage(), var15);
               }
            } else {
               throw new JMSException("ERROR: entityType cannot be null or empty");
            }
         } else {
            throw new JMSException("ERROR: entityName cannot be null or empty");
         }
      } else {
         throw new JMSException("ERROR: resourceName cannot be null or empty");
      }
   }

   public static void createJMSServer(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         createJMSServer(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var1, var5);
      }

   }

   public static void createJMSServer(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var1 != null && !var1.trim().equals("")) {
         try {
            TargetMBean[] var3 = targetNames2TargetMBeans(var0, var2);
            if (var3.length != 1) {
               throw new JMSException("ERROR: A JMSServer cannot be targeted multiple targets");
            } else if (!(var3[0] instanceof ClusterMBean) && !(var3[0] instanceof SAFAgentMBean) && !(var3[0] instanceof JMSServerMBean)) {
               JMSServerMBean var4 = var0.createJMSServer(var1);
               var4.setTargets(var3);
            } else {
               throw new JMSException("ERROR: Cluster, SAFAgent and JMSServer cannot be set as target for JMSServer");
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInDomainLoggable(var0.getName(), "JMSServer", var1).getMessage(), var5);
         }
      } else {
         throw new JMSException("ERROR: jmsServerName cannot be null or empty");
      }
   }

   public static void deleteJMSServer(Context var0, String var1) throws JMSException {
      ConfigurationManagerMBean var2 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var3 = beginEditSession(var2);
      boolean var4 = false;

      try {
         deleteJMSServer(var3, var1);
         var4 = true;
      } finally {
         endEditSession(var2, var1, var4);
      }

   }

   public static void deleteJMSServer(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSServerMBean var2 = var0.lookupJMSServer(var1);
            if (var2 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "JMSServer", var1).getMessage());
            } else {
               TargetMBean var3 = var2.getTargets()[0];
               var2.removeTarget(var3);
               var0.destroyJMSServer(var2);
            }
         } catch (Exception var4) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromDomainLoggable(var0.getName(), "JMSServer", var1).getMessage(), var4);
         }
      }
   }

   public static void deployJMSServer(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deployJMSServer(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var1, var5);
      }

   }

   public static void deployJMSServer(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         JMSServerMBean var3 = var0.lookupJMSServer(var1);
         if (var3 == null) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "JMSServer", var1).getMessage());
         } else {
            try {
               TargetMBean[] var4 = targetNames2TargetMBeans(var0, var2);
               if (var4.length != 1) {
                  throw new JMSException("ERROR: A JMSServer cannot be targeted multiple targets");
               } else if (!(var4[0] instanceof ClusterMBean) && !(var4[0] instanceof SAFAgentMBean) && !(var4[0] instanceof JMSServerMBean)) {
                  var3.setTargets(var4);
               } else {
                  throw new JMSException("ERROR: Cluster, SAFAgent and JMSServer cannot be set as target for JMSServer");
               }
            } catch (Exception var5) {
               throw new weblogic.jms.common.JMSException("ERROR: Could not deploy JMSSerever " + var1 + " in the domain " + var0.getName(), var5);
            }
         }
      }
   }

   public static void undeployJMSServer(Context var0, String var1) throws JMSException {
      ConfigurationManagerMBean var2 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var3 = beginEditSession(var2);
      boolean var4 = false;

      try {
         undeployJMSServer(var3, var1);
         var4 = true;
      } finally {
         endEditSession(var2, var1, var4);
      }

   }

   public static void undeployJMSServer(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         JMSServerMBean var2 = var0.lookupJMSServer(var1);
         if (var2 == null) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var0.getName(), "JMSServer", var1).getMessage());
         } else {
            TargetMBean[] var3 = var2.getTargets();
            if (var3 != null && var3.length != 0) {
               TargetMBean var4 = var3[0];
               ServerMBean var5 = null;
               if (var4 == null) {
                  throw new JMSException("JMSServer " + var1 + " is not currently deployed");
               } else {
                  int var7;
                  if (var4 instanceof MigratableTargetMBean) {
                     MigratableTargetMBean[] var6 = var0.getMigratableTargets();

                     for(var7 = 0; var7 < var6.length; ++var7) {
                        if (var4.getName().equals(var6[var7].getName())) {
                           var5 = var6[var7].getUserPreferredServer();
                           break;
                        }
                     }
                  } else {
                     ServerMBean[] var9 = var0.getServers();

                     for(var7 = 0; var7 < var9.length; ++var7) {
                        if (var4.getName().equals(var9[var7].getName())) {
                           var5 = var9[var7];
                           break;
                        }
                     }
                  }

                  try {
                     if (var5 != null) {
                        var2.removeTarget(var4);
                     }

                  } catch (Exception var8) {
                     throw new weblogic.jms.common.JMSException("ERROR: Could not undeploy JMSServer " + var1 + " in the domain " + var0.getName(), var8);
                  }
               }
            } else {
               throw new weblogic.jms.common.JMSException("ERROR: Could not undeploy JMSServer " + var1 + " in the domain " + var0.getName() + ", it is not currently deployed");
            }
         }
      }
   }

   public static void createSAFAgent(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         createSAFAgent(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var1, var5);
      }

   }

   public static void createSAFAgent(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else if (var1 != null && !var1.trim().equals("")) {
         try {
            TargetMBean[] var3 = targetNames2TargetMBeans(var0, var2);

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4] instanceof MigratableTargetMBean || var3[var4] instanceof JMSServerMBean || var3[var4] instanceof SAFAgentMBean) {
                  throw new JMSException("ERROR: MigratableTarget, JMSServer and SAFAgent cannot be set as target for SAFAgent");
               }
            }

            SAFAgentMBean var6 = var0.createSAFAgent(var1);
            var6.setTargets(var3);
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotCreateEntityInDomainLoggable(var0.getName(), "SAFAgent", var1).getMessage(), var5);
         }
      } else {
         throw new JMSException("ERROR: safAgentName cannot be null or empty");
      }
   }

   public static void deleteSAFAgent(Context var0, String var1) throws JMSException {
      ConfigurationManagerMBean var2 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var3 = beginEditSession(var2);
      boolean var4 = false;

      try {
         deleteSAFAgent(var3, var1);
         var4 = true;
      } finally {
         endEditSession(var2, var1, var4);
      }

   }

   public static void deleteSAFAgent(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            SAFAgentMBean var2 = var0.lookupSAFAgent(var1);
            if (var2 == null) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "SAFAgent", var1).getMessage());
            } else {
               TargetMBean[] var3 = var2.getTargets();

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  var2.removeTarget(var3[var4]);
               }

               var0.destroySAFAgent(var2);
            }
         } catch (Exception var5) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromDomainLoggable(var0.getName(), "SAFAgent", var1).getMessage(), var5);
         }
      }
   }

   public static void deploySAFAgent(Context var0, String var1, String var2) throws JMSException {
      ConfigurationManagerMBean var3 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var4 = beginEditSession(var3);
      boolean var5 = false;

      try {
         deploySAFAgent(var4, var1, var2);
         var5 = true;
      } finally {
         endEditSession(var3, var1, var5);
      }

   }

   public static void deploySAFAgent(DomainMBean var0, String var1, String var2) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         SAFAgentMBean var3 = var0.lookupSAFAgent(var1);
         if (var3 == null) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInDomainLoggable(var0.getName(), "SAFAgent", var1).getMessage());
         } else {
            try {
               TargetMBean[] var4 = targetNames2TargetMBeans(var0, var2);

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (var4[var5] instanceof MigratableTargetMBean || var4[var5] instanceof JMSServerMBean || var4[var5] instanceof SAFAgentMBean) {
                     throw new JMSException("ERROR: MigratableTarget, JMSServer and SAFAgent cannot be set as target for SAFAgent");
                  }
               }

               var3.setTargets(var4);
            } catch (Exception var6) {
               throw new weblogic.jms.common.JMSException("ERROR: Could not deploy SAFAgent " + var1 + " in the domain " + var0.getName(), var6);
            }
         }
      }
   }

   public static void undeploySAFAgent(Context var0, String var1) throws JMSException {
      ConfigurationManagerMBean var2 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var3 = beginEditSession(var2);
      boolean var4 = false;

      try {
         undeploySAFAgent(var3, var1);
         var4 = true;
      } finally {
         endEditSession(var2, var1, var4);
      }

   }

   public static void undeploySAFAgent(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         SAFAgentMBean var2 = var0.lookupSAFAgent(var1);
         if (var2 == null) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var0.getName(), "SAFAgent", var1).getMessage());
         } else {
            TargetMBean[] var3 = var2.getTargets();
            if (var3 != null && var3.length != 0) {
               try {
                  for(int var4 = 0; var4 < var3.length; ++var4) {
                     var2.removeTarget(var3[var4]);
                  }

               } catch (Exception var5) {
                  throw new weblogic.jms.common.JMSException("ERROR: Could not deploy SAFAgent " + var1 + " in the domain " + var0.getName(), var5);
               }
            } else {
               throw new JMSException("SAFAgent " + var1 + " is not currently deployed");
            }
         }
      }
   }

   private static TargetMBean findMatchingTargetMBean(DomainMBean var0, String var1) throws JMSException {
      Object var2 = var0.lookupMigratableTarget(var1);
      if (var2 == null) {
         var2 = var0.lookupCluster(var1);
         if (var2 == null) {
            var2 = var0.lookupServer(var1);
            if (var2 == null) {
               var2 = var0.lookupJMSServer(var1);
               if (var2 == null) {
                  var2 = var0.lookupSAFAgent(var1);
                  if (var2 == null) {
                     throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var0.getName(), "MigratableTarget/Cluster/Server/JMSServer/SAFAgent", var1).getMessage());
                  }
               }
            }
         }
      }

      return (TargetMBean)var2;
   }

   private static TargetMBean[] targetNames2TargetMBeans(DomainMBean var0, String var1) throws JMSException {
      String[] var2 = var1.split(",");
      TargetMBean[] var3 = new TargetMBean[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3[var4] = findMatchingTargetMBean(var0, var2[var4]);
      }

      return var3;
   }

   private static void deleteDestination(Context var0, String var1, String var2, String var3) throws JMSException {
      ConfigurationManagerMBean var4 = JMSEditHelper.getConfigurationManager(var0);
      DomainMBean var5 = beginEditSession(var4);
      boolean var6 = false;

      try {
         deleteDestination(var5, var1, var2, var3);
         var6 = true;
      } finally {
         endEditSession(var4, var2, var6);
      }

   }

   private static void deleteDestination(DomainMBean var0, String var1, String var2, String var3) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: domain cannot be null");
      } else {
         try {
            JMSBean var4 = getJMSBean(var0, var1);
            if (var3.equals("JMSQueue")) {
               QueueBean var5 = var4.lookupQueue(var2);
               if (var5 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, var3, var2).getMessage());
               }

               var4.destroyQueue(var5);
            } else if (var3.equals("JMSTopic")) {
               TopicBean var7 = var4.lookupTopic(var2);
               if (var7 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, var3, var2).getMessage());
               }

               var4.destroyTopic(var7);
            } else if (var3.equals("UniformDistributedQueue")) {
               UniformDistributedQueueBean var8 = var4.lookupUniformDistributedQueue(var2);
               if (var8 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, var3, var2).getMessage());
               }

               var4.destroyUniformDistributedQueue(var8);
            } else if (var3.equals("UniformDistributedTopic")) {
               UniformDistributedTopicBean var9 = var4.lookupUniformDistributedTopic(var2);
               if (var9 == null) {
                  throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logEntityNotFoundInJMSSystemResourceLoggable(var1, var3, var2).getMessage());
               }

               var4.destroyUniformDistributedTopic(var9);
            }

         } catch (Exception var6) {
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logCannotDeleteEntityFromJMSSystemResourceLoggable(var1, var3, var2).getMessage(), var6);
         }
      }
   }

   private static void activateEdit(ConfigurationManagerMBean var0) throws JMSException {
      try {
         var0.save();
      } catch (NotEditorException var4) {
         throw new weblogic.jms.common.JMSException("ERROR: Not editor while saving edit", var4);
      } catch (ValidationException var5) {
         throw new weblogic.jms.common.JMSException("ERROR: Validation error while saving edit", var5);
      }

      ActivationTaskMBean var1;
      try {
         var1 = var0.activate(-1L);
      } catch (NotEditorException var3) {
         throw new weblogic.jms.common.JMSException("ERROR: Edit session activation failed", var3);
      }

      Exception var2 = var1.getError();
      if (var2 != null) {
         throw new weblogic.jms.common.JMSException("ERROR: Edit session activation failed. Reason:" + var2.getMessage(), var2);
      }
   }

   public static String uddMakeName(String var0, String var1) {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      int var3 = var1.indexOf("!");
      String var2;
      if (var3 != -1) {
         var2 = var1.substring(var3 + 1);
      } else {
         var2 = var1;
      }

      return var0 + "@" + var2;
   }

   public static String uddMemberName(String var0, String var1) {
      return uddMakeName(var0, var1);
   }

   public static String uddMemberJNDIName(String var0, String var1) {
      return uddMakeName(var0, var1);
   }

   private static void uddFillWithJMSServers(Map var0, DomainMBean var1, ServerMBean[] var2) {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            uddFillWithJMSServers(var0, var1, var2[var3]);
         }

      }
   }

   private static void uddFillWithJMSServers(Map var0, DomainMBean var1, ServerMBean var2) {
      if (var2 != null) {
         JMSServerMBean[] var3 = var1.getJMSServers();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            TargetMBean[] var5 = var3[var4].getTargets();
            if (var5 != null && var5.length != 0) {
               TargetMBean var6 = var5[0];
               if (var6.getName().equals(var2.getName())) {
                  if (var0.put(var3[var4].getName(), var3[var4].getName()) != null) {
                     throw new IllegalArgumentException("Targets of UDD overlap");
                  }
               } else {
                  ClusterMBean var7 = var2.getCluster();
                  if (var7 != null && var6 instanceof MigratableTargetMBean) {
                     ClusterMBean var8 = ((MigratableTargetMBean)var6).getCluster();
                     ServerMBean[] var9 = ((MigratableTargetMBean)var6).getAllCandidateServers();
                     if (var9 != null) {
                        if (var9.length == 0) {
                           if (var8 != null && var7.getName().equals(var8.getName()) && var0.get(var3[var4].getName()) == null) {
                              var0.put(var3[var4].getName(), var3[var4].getName());
                           }
                        } else {
                           for(int var10 = 0; var10 < var9.length; ++var10) {
                              if (var9[var10].getName().equals(var2.getName()) && var0.get(var3[var4].getName()) == null) {
                                 var0.put(var3[var4].getName(), var3[var4].getName());
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

   public static void uddFillWithMyTargets(Map var0, DomainMBean var1, SubDeploymentMBean var2) {
      uddFillWithMyTargets(var0, var1, var2.getTargets());
   }

   public static void uddFillWithMyTargets(Map var0, DomainMBean var1, TargetMBean[] var2) {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            TargetMBean var4 = var2[var3];
            if (var4 instanceof ClusterMBean) {
               ServerMBean[] var5 = ((ClusterMBean)var4).getServers();
               uddFillWithJMSServers(var0, var1, var5);
            } else if (var4 instanceof ServerMBean) {
               uddFillWithJMSServers(var0, var1, (ServerMBean)var4);
            } else {
               if (!(var4 instanceof JMSServerMBean)) {
                  throw new IllegalArgumentException("A UDD cannot be targeted to a migratable target");
               }

               if (var0.put(var4.getName(), var4.getName()) != null) {
                  throw new IllegalArgumentException("Targets of UDD overlap");
               }
            }
         }

         if (var0.size() != 1) {
            Iterator var10 = var0.values().iterator();
            String var11 = null;

            while(var10.hasNext()) {
               String var12 = (String)var10.next();
               JMSServerMBean var6 = var1.lookupJMSServer(var12);
               if (var6.getTargets().length != 0) {
                  TargetMBean var7 = var6.getTargets()[0];
                  ClusterMBean var8;
                  if (var7 instanceof MigratableTargetMBean) {
                     var8 = ((MigratableTargetMBean)var7).getCluster();
                  } else {
                     var8 = ((ServerMBean)var7).getCluster();
                  }

                  String var9;
                  if (var8 == null) {
                     var9 = "Stand Alone Server " + ((ServerMBean)var7).getName();
                  } else {
                     var9 = "Cluster " + var8.getName();
                  }

                  if (var11 == null) {
                     var11 = var9;
                  } else if (!var9.equals(var11)) {
                     throw new IllegalArgumentException("A UDD must be targeted to servers within a single cluster or a single stand-alone server, rather than " + var11 + " and " + var9);
                  }
               }
            }

         }
      }
   }

   public static String[] uddReturnJMSServers(DomainMBean var0, SubDeploymentMBean var1) {
      HashMap var2 = new HashMap();
      uddFillWithMyTargets(var2, var0, (SubDeploymentMBean)var1);
      return (String[])((String[])var2.values().toArray(new String[1]));
   }

   private static JMSSystemResourceMBean findJMSSystemResource(DomainMBean var0, String var1) throws JMSException {
      if (var0 == null) {
         throw new JMSException("ERROR: Invalid domain: DomainMBean cannot be null ");
      } else if (var1 != null && !var1.trim().equals("")) {
         JMSSystemResourceMBean var2 = var0.lookupJMSSystemResource(var1);
         if (var2 == null) {
            throw new JMSException("ERROR: Could not find JMSSystemResource " + var1 + " in the domain " + var0.getName());
         } else {
            return var2;
         }
      } else {
         throw new JMSException("ERROR: Invalid JMS System Resource Name: resource name cannot be null or empty");
      }
   }

   private static JMSBean getJMSBean(DomainMBean var0, String var1) throws JMSException {
      JMSSystemResourceMBean var2 = findJMSSystemResource(var0, var1);
      return var2.getJMSResource();
   }

   private static DomainMBean beginEditSession(ConfigurationManagerMBean var0) throws JMSException {
      DomainMBean var1 = null;

      try {
         var1 = var0.startEdit(-1, -1);
         return var1;
      } catch (EditTimedOutException var3) {
         throw new weblogic.jms.common.JMSException("ERROR: Unable to start the edit session", var3);
      }
   }

   private static void endEditSession(ConfigurationManagerMBean var0, String var1, boolean var2) throws JMSException {
      if (!var2) {
         var0.cancelEdit();
      } else {
         try {
            activateEdit(var0);
         } catch (JMSException var5) {
            var0.cancelEdit();
            Throwable var4 = var5.getCause();
            throw new weblogic.jms.common.JMSException("ERROR: Could not activate update for the entity " + var1 + ". The edit session was cancelled and the changes made " + "in this edit session were discarded. \nREASON: ", (Throwable)(var4 == null ? var5 : var4));
         }
      }

   }

   private static SubDeploymentMBean findOrCreateSubDeployment(JMSSystemResourceMBean var0, String var1) {
      SubDeploymentMBean var2 = var0.lookupSubDeployment(var1);
      if (var2 == null) {
         var2 = var0.createSubDeployment(var1);
      }

      return var2;
   }
}
