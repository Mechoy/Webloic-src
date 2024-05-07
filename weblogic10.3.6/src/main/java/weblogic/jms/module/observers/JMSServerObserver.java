package weblogic.jms.module.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.TargetableBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.provider.UpdateException;
import weblogic.utils.ArrayUtils;

public class JMSServerObserver implements PropertyChangeListener, ArrayUtils.DiffHandler {
   private static final String QUEUE_STRING = "JMSQueues";
   private static final String TOPIC_STRING = "JMSTopics";
   private static final String[] handledProperties = new String[]{"JMSQueues", "JMSTopics"};
   private static final int UNHANDLED = -1;
   private static final int QUEUE = 0;
   private static final int TOPIC = 1;
   private static final int MAX_PROPERTIES = 2;
   private JMSObserver domainObserver;
   private JMSServerMBean jmsServer;
   private int currentType = -1;

   public JMSServerObserver(JMSObserver var1, JMSServerMBean var2) {
      this.domainObserver = var1;
      this.jmsServer = var2;
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      this.currentType = this.getType(var1.getPropertyName());
      if (this.currentType != -1) {
         Object[] var2 = (Object[])((Object[])var1.getOldValue());
         Object[] var3 = (Object[])((Object[])var1.getNewValue());
         ArrayUtils.computeDiff(var2, var3, this, this.domainObserver);
         this.currentType = -1;
      }
   }

   public JMSServerMBean getJMSServer() {
      return this.jmsServer;
   }

   private int getType(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < 2; ++var2) {
            String var3 = handledProperties[var2];
            if (var3.equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   private void addQuota(JMSBean var1, DestinationBean var2) {
      if (var2.getQuota() == null) {
         String var3 = MBeanConverter.constructQuotaNameFromDestinationName(var2.getName());
         QuotaBean var4 = var1.lookupQuota(var3);
         if (var4 == null) {
            var4 = var1.createQuota(var3);
            long var5 = this.jmsServer.getBytesMaximum();
            if (var5 >= 0L) {
               var4.setBytesMaximum(var5);
            }

            var5 = this.jmsServer.getMessagesMaximum();
            if (var5 >= 0L) {
               var4.setMessagesMaximum(var5);
            }

            var4.setShared(false);
            var2.setQuota(var4);
         }
      }
   }

   private void removeQuota(JMSBean var1, DestinationBean var2) {
      String var3 = MBeanConverter.constructQuotaNameFromDestinationName(var2.getName());
      QuotaBean var4 = var1.lookupQuota(var3);
      if (var4 != null) {
         var1.destroyQuota(var4);
      }
   }

   private void addQueue(JMSQueueMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSSystemResourceMBean var3 = JMSBeanHelper.addInteropApplication(var2);
      JMSBean var4 = var3.getJMSResource();
      QueueBean var5 = null;

      try {
         var5 = MBeanConverter.addQueue(var4, var3, var1);
      } catch (UpdateException var7) {
         this.domainObserver.logUpdateException(var1.getName(), var7);
         return;
      }

      this.addQuota(var4, var5);
      var1.useDelegates(var2, var4, var5);
   }

   private boolean findTargetableMatch(TargetableBean[] var1, String var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         TargetableBean var4 = var1[var3];
         if (var2.equals(var4.getSubDeploymentName())) {
            return true;
         }
      }

      return false;
   }

   private void removeSubDeployment(TargetableBean var1, JMSSystemResourceMBean var2) {
      JMSBean var3 = var2.getJMSResource();
      String var4 = var1.getSubDeploymentName();
      SubDeploymentMBean var5 = JMSBeanHelper.findSubDeployment(var4, var2);
      if (var5 != null) {
         QueueBean[] var6 = var3.getQueues();
         TopicBean[] var7 = var3.getTopics();
         if (!this.findTargetableMatch(var6, var4) && !this.findTargetableMatch(var7, var4)) {
            var2.destroySubDeployment(var5);
         }
      }
   }

   private void removeQueue(JMSQueueMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSInteropModuleMBean var3 = JMSBeanHelper.getJMSInteropModule(var2);
      if (var3 != null) {
         JMSBean var4 = var3.getJMSResource();
         QueueBean var5 = var4.lookupQueue(var1.getName());
         if (var5 != null) {
            var4.destroyQueue(var5);
            this.removeQuota(var4, var5);
         }

         this.removeSubDeployment(var5, var3);
      }
   }

   private void addTopic(JMSTopicMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSSystemResourceMBean var3 = JMSBeanHelper.addInteropApplication(var2);
      JMSBean var4 = var3.getJMSResource();
      TopicBean var5 = null;

      try {
         var5 = MBeanConverter.addTopic(var4, var3, var1);
      } catch (UpdateException var7) {
         this.domainObserver.logUpdateException(var1.getName(), var7);
         return;
      }

      this.addQuota(var4, var5);
      var1.useDelegates(var2, var4, var5);
   }

   private void removeTopic(JMSTopicMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSInteropModuleMBean var3 = JMSBeanHelper.getJMSInteropModule(var2);
      if (var3 != null) {
         JMSBean var4 = var3.getJMSResource();
         TopicBean var5 = var4.lookupTopic(var1.getName());
         if (var5 != null) {
            var4.destroyTopic(var5);
            this.removeQuota(var4, var5);
         }

         this.removeSubDeployment(var5, var3);
      }
   }

   public void addObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.addQueue((JMSQueueMBean)var1);
            break;
         case 1:
            this.addTopic((JMSTopicMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   public void removeObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.removeQueue((JMSQueueMBean)var1);
            break;
         case 1:
            this.removeTopic((JMSTopicMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof JMSServerObserver) {
         JMSServerObserver var2 = (JMSServerObserver)var1;
         return this.jmsServer == var2.jmsServer;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.jmsServer.hashCode();
   }
}
