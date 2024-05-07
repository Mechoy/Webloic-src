package weblogic.management.mbeans.custom;

import java.util.HashSet;
import java.util.Set;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.utils.ArrayUtils;

public class JMSServer extends ConfigurationMBeanCustomizer {
   private static final String TT_PROP = "TemporaryTemplate";
   private static final String TR_PROP = "TemporaryTemplateResource";
   private static final String TN_PROP = "TemporaryTemplateName";
   private transient DomainMBean delegate;

   public JMSServer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1) {
      this.delegate = var1;
   }

   public Set getServerNames() {
      TargetMBean[] var1 = ((JMSServerMBean)this.getMbean()).getTargets();
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.addAll(var1[var3].getServerNames());
      }

      return var2;
   }

   public JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2) {
      JMSQueueMBean var3 = (JMSQueueMBean)this.getMbean().createChildCopyIncludingObsolete("JMSQueue", var2);
      return var3;
   }

   public JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2) {
      JMSTopicMBean var3 = (JMSTopicMBean)this.getMbean().createChildCopyIncludingObsolete("JMSTopic", var2);
      return var3;
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2) {
      JMSSessionPoolMBean var3 = (JMSSessionPoolMBean)this.getMbean().createChildCopyIncludingObsolete("JMSSessionPool", var2);
      return var3;
   }

   public void setSessionPools(JMSSessionPoolMBean[] var1) throws InvalidAttributeValueException {
      ArrayUtils.computeDiff(this.getSessionPools(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            try {
               JMSServer.this.addSessionPool((JMSSessionPoolMBean)var1);
            } catch (InvalidAttributeValueException var3) {
               throw new RuntimeException(var3);
            } catch (DistributedManagementException var4) {
               throw new RuntimeException(var4);
            }
         }

         public void removeObject(Object var1) {
            try {
               JMSServer.this.removeSessionPool((JMSSessionPoolMBean)var1);
            } catch (InvalidAttributeValueException var3) {
               throw new RuntimeException(var3);
            } catch (DistributedManagementException var4) {
               throw new RuntimeException(var4);
            }
         }
      });
   }

   public boolean addSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSServerMBean var2 = (JMSServerMBean)this.getMbean();
      if (var2.lookupJMSSessionPool(var1.getName()) != null) {
         return true;
      } else {
         this.getMbean().createChildCopyIncludingObsolete("JMSSessionPool", var1);
         DomainMBean var3 = (DomainMBean)var2.getParentBean();
         var3.destroyJMSSessionPool(var1);
         return true;
      }
   }

   public boolean removeSessionPool(JMSSessionPoolMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      JMSServerMBean var2 = (JMSServerMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var2.lookupJMSSessionPool(var1.getName()) == null) {
         return true;
      } else {
         var3.createJMSSessionPool(var1.getName(), var1);
         var2.destroyJMSSessionPool(var1);
         return true;
      }
   }

   public JMSSessionPoolMBean[] getSessionPools() {
      JMSServerMBean var1 = (JMSServerMBean)this.getMbean();
      return var1.getJMSSessionPools();
   }

   public JMSTemplateMBean getTemporaryTemplate() {
      if (this.delegate == null) {
         Object var7 = this.getValue("TemporaryTemplate");
         return var7 != null && var7 instanceof JMSTemplateMBean ? (JMSTemplateMBean)var7 : null;
      } else {
         JMSServerMBean var1 = (JMSServerMBean)this.getMbean();
         String var2 = var1.getTemporaryTemplateName();
         String var3 = var1.getTemporaryTemplateResource();
         if (var3 != null && var3.equals("interop-jms")) {
            JMSTemplateMBean[] var4 = this.delegate.getJMSTemplates();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  JMSTemplateMBean var6 = var4[var5];
                  if (var2.equals(var6.getName())) {
                     return var6;
                  }
               }
            }

            return null;
         } else {
            return null;
         }
      }
   }

   public void setTemporaryTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("TemporaryTemplate", var1);
      } else {
         JMSServerMBean var2 = (JMSServerMBean)this.getMbean();
         if (var1 == null) {
            var2.unSet("TemporaryTemplateResource");
            var2.unSet("TemporaryTemplateName");
         } else {
            var2.setTemporaryTemplateResource("interop-jms");
            var2.setTemporaryTemplateName(var1.getName());
         }
      }
   }

   public JMSDestinationMBean[] getDestinations() {
      return (JMSDestinationMBean[])((JMSDestinationMBean[])JMSServer.DESTINATIONAGGREGATOR.instance.getAll(this.getMbean()));
   }

   public JMSDestinationMBean lookupDestination(String var1) {
      return (JMSDestinationMBean)JMSServer.DESTINATIONAGGREGATOR.instance.lookup(this.getMbean(), var1);
   }

   public boolean addDestination(JMSDestinationMBean var1) {
      JMSServerMBean var2 = (JMSServerMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      JMSSystemResourceMBean var4 = JMSBeanHelper.addInteropApplication(var3);
      JMSBean var5 = var4.getJMSResource();
      if (JMSModuleHelper.findDestinationBean(var1.getName(), var5) != null) {
         return true;
      } else {
         if (var1 instanceof JMSQueueMBean) {
            var2.createChildCopyIncludingObsolete("JMSQueue", var1);
            var3.destroyJMSQueue((JMSQueueMBean)var1);
         } else {
            var2.createChildCopyIncludingObsolete("JMSTopic", var1);
            var3.destroyJMSTopic((JMSTopicMBean)var1);
         }

         return true;
      }
   }

   public boolean removeDestination(JMSDestinationMBean var1) {
      JMSServerMBean var2 = (JMSServerMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var1 instanceof JMSQueueMBean) {
         if (var2.lookupJMSQueue(var1.getName()) == null) {
            return true;
         }

         var3.createChildCopyIncludingObsolete("JMSQueue", var1);
         var2.destroyJMSQueue((JMSQueueMBean)var1);
      } else {
         if (var2.lookupJMSTopic(var1.getName()) == null) {
            return true;
         }

         var3.createChildCopyIncludingObsolete("JMSTopic", var1);
         var2.destroyJMSTopic((JMSTopicMBean)var1);
      }

      return true;
   }

   public void setDestinations(JMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      ArrayUtils.computeDiff(this.getDestinations(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            JMSServer.this.addDestination((JMSDestinationMBean)var1);
         }

         public void removeObject(Object var1) {
            JMSServer.this.removeDestination((JMSDestinationMBean)var1);
         }
      });
   }

   public void _preDestroy() {
      DomainMBean var1;
      if (this.delegate == null) {
         var1 = JMSBeanHelper.getDomain(this.getMbean());
      } else {
         var1 = this.delegate;
      }

      JMSInteropModuleMBean var2 = JMSBeanHelper.getJMSInteropModule(var1);
      if (var2 != null) {
         JMSBean var3 = var2.getJMSResource();
         String var4 = ((JMSServerMBean)this.getMbean()).getName();
         QueueBean[] var5 = var3.getQueues();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            QueueBean var7 = var5[var6];
            if (var4.equals(var7.getSubDeploymentName())) {
               var3.destroyQueue(var7);
            }
         }

         TopicBean[] var9 = var3.getTopics();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            TopicBean var8 = var9[var10];
            if (var4.equals(var8.getSubDeploymentName())) {
               var3.destroyTopic(var8);
            }
         }

         SubDeploymentMBean var11 = var2.lookupSubDeployment(var4);
         if (var11 != null) {
            var2.destroySubDeployment(var11);
         }

      }
   }

   private static class DESTINATIONAGGREGATOR {
      static AttributeAggregator instance = new AttributeAggregator("weblogic.management.configuration.JMSServerMBean", JMSDestinationMBean.class);
   }
}
