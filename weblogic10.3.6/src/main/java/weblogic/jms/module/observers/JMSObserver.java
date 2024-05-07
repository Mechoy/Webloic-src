package weblogic.jms.module.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSCompatibilityProcessor;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.JMSConnectionFactoryMBean;
import weblogic.management.configuration.JMSDestinationKeyMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.AccessCallback;
import weblogic.management.provider.UpdateException;
import weblogic.utils.ArrayUtils;

public class JMSObserver implements AccessCallback, PropertyChangeListener, ArrayUtils.DiffHandler, Comparator {
   private static final String CONNECTION_FACTORY_STRING = "JMSConnectionFactories";
   private static final String JMS_SERVER_STRING = "JMSServers";
   private static final String TEMPLATE_STRING = "JMSTemplates";
   private static final String FOREIGN_SERVER_STRING = "ForeignJMSServers";
   private static final String DISTRIBUTED_QUEUE_STRING = "JMSDistributedQueues";
   private static final String DISTRIBUTED_TOPIC_STRING = "JMSDistributedTopics";
   private static final String DESTINATION_KEY_STRING = "JMSDestinationKeys";
   private static final String[] handledProperties = new String[]{"JMSConnectionFactories", "JMSServers", "JMSTemplates", "ForeignJMSServers", "JMSDistributedQueues", "JMSDistributedTopics", "JMSDestinationKeys"};
   private static final int UNHANDLED = -1;
   private static final int CONNECTION_FACTORY = 0;
   private static final int JMS_SERVER = 1;
   private static final int TEMPLATE = 2;
   private static final int FOREIGN_SERVER = 3;
   private static final int DISTRIBUTED_QUEUE = 4;
   private static final int DISTRIBUTED_TOPIC = 5;
   private static final int DESTINATION_KEY = 6;
   private static final int MAX_PROPERTIES = 7;
   private DomainMBean root;
   private HashSet jmsServerObservers;
   private HashSet foreignJMSServerObservers;
   private HashSet distributedQueueObservers;
   private HashSet distributedTopicObservers;
   private int currentType = -1;

   public void shutdown() {
      this.root.removePropertyChangeListener(this);
      this.root = null;
   }

   public void accessed(DomainMBean var1) {
      this.root = var1;
      JMSCompatibilityProcessor.updateConfiguration(var1);
      var1.addPropertyChangeListener(this);
      this.jmsServerObservers = new HashSet();
      this.foreignJMSServerObservers = new HashSet();
      this.distributedQueueObservers = new HashSet();
      this.distributedTopicObservers = new HashSet();
      JMSServerMBean[] var2 = this.root.getJMSServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.addJMSServer(var2[var3]);
      }

      ForeignJMSServerMBean[] var9 = this.root.getForeignJMSServers();

      for(int var4 = 0; var4 < var9.length; ++var4) {
         ForeignJMSServerMBean var5 = var9[var4];
         JMSForeignObserver var6 = new JMSForeignObserver(this, var5);
         ((AbstractDescriptorBean)var5).addPropertyChangeListener(var6);
         this.foreignJMSServerObservers.add(var6);
      }

      JMSDistributedQueueMBean[] var10 = this.root.getJMSDistributedQueues();

      for(int var11 = 0; var11 < var10.length; ++var11) {
         JMSDistributedQueueMBean var13 = var10[var11];
         JMSDistributedQueueObserver var7 = new JMSDistributedQueueObserver(this, var13);
         ((AbstractDescriptorBean)var13).addPropertyChangeListener(var7);
         this.distributedQueueObservers.add(var7);
      }

      JMSDistributedTopicMBean[] var12 = this.root.getJMSDistributedTopics();

      for(int var14 = 0; var14 < var12.length; ++var14) {
         JMSDistributedTopicMBean var15 = var12[var14];
         JMSDistributedTopicObserver var8 = new JMSDistributedTopicObserver(this, var15);
         ((AbstractDescriptorBean)var15).addPropertyChangeListener(var8);
         this.distributedTopicObservers.add(var8);
      }

   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      this.currentType = this.getType(var1.getPropertyName());
      if (this.currentType != -1) {
         Object[] var2 = (Object[])((Object[])var1.getOldValue());
         Object[] var3 = (Object[])((Object[])var1.getNewValue());
         ArrayUtils.computeDiff(var2, var3, this, this);
         this.currentType = -1;
      }
   }

   private int getType(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < 7; ++var2) {
            String var3 = handledProperties[var2];
            if (var3.equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   private void addConnectionFactory(JMSConnectionFactoryMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();
      HashMap var4 = MBeanConverter.splitDeployment(var1);
      if (var4.size() > 1) {
         JMSLogger.logSplitDeployment("JSMConnectionFactory", var1.getName());
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            ArrayList var7 = (ArrayList)var4.get(var6);
            TargetMBean[] var8 = (TargetMBean[])((TargetMBean[])var7.toArray(new TargetMBean[0]));
            JMSConnectionFactoryBean var9 = MBeanConverter.addJMSConnectionFactory(var3, var2, var1, var6, var8);
            var1.useDelegates(var9, JMSBeanHelper.findSubDeployment(var6, var2));
         }
      } else {
         JMSConnectionFactoryBean var10 = MBeanConverter.addJMSConnectionFactory(var3, var2, var1, var1.getName(), var1.getTargets());
         var1.useDelegates(var10, JMSBeanHelper.findSubDeployment(var1.getName(), var2));
      }

   }

   private void removeConnectionFactory(JMSConnectionFactoryMBean var1) {
      JMSInteropModuleMBean var2 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var2 != null) {
         JMSBean var3 = var2.getJMSResource();
         JMSConnectionFactoryBean var4 = var3.lookupConnectionFactory(var1.getName());
         var3.destroyConnectionFactory(var4);
         SubDeploymentMBean var5 = JMSBeanHelper.findSubDeployment(var1.getName(), var2);
         var2.destroySubDeployment(var5);
      }
   }

   private void addJMSServer(JMSServerMBean var1) {
      JMSServerObserver var2 = new JMSServerObserver(this, var1);
      ((AbstractDescriptorBean)var1).addPropertyChangeListener(var2);
      this.jmsServerObservers.add(var2);
   }

   private void removeJMSServer(JMSServerMBean var1) {
      Iterator var2 = this.jmsServerObservers.iterator();

      JMSServerObserver var3;
      JMSServerMBean var4;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (JMSServerObserver)var2.next();
         var4 = var3.getJMSServer();
      } while(var1 != var4);

      ((AbstractDescriptorBean)var1).removePropertyChangeListener(var3);
      var2.remove();
   }

   void logUpdateException(String var1, UpdateException var2) {
      JMSLogger.logUnableToAddEntity(var1, var2.toString());
   }

   private void addTemplate(JMSTemplateMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();

      TemplateBean var4;
      try {
         var4 = MBeanConverter.addTemplate(var3, var1);
      } catch (UpdateException var6) {
         this.logUpdateException(var1.getName(), var6);
         return;
      }

      var1.useDelegates(this.root, var3, var4);
   }

   private void removeTemplate(JMSTemplateMBean var1) {
      JMSInteropModuleMBean var2 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var2 != null) {
         JMSBean var3 = var2.getJMSResource();
         TemplateBean var4 = var3.lookupTemplate(var1.getName());
         var3.destroyTemplate(var4);
      }
   }

   private void addForeignServer(ForeignJMSServerMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();
      HashMap var4 = MBeanConverter.splitDeployment(var1);
      if (var4.size() > 1) {
         JMSLogger.logSplitDeployment("ForeignJMSServer", var1.getName());
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            ArrayList var7 = (ArrayList)var4.get(var6);
            TargetMBean[] var8 = (TargetMBean[])((TargetMBean[])var7.toArray(new TargetMBean[0]));
            ForeignServerBean var9 = MBeanConverter.addForeignJMSServer(var3, var2, var1, var6, var8);
            var1.useDelegates(var9, JMSBeanHelper.findSubDeployment(var6, var2));
         }
      } else {
         ForeignServerBean var10 = MBeanConverter.addForeignJMSServer(var3, var2, var1, var1.getName(), var1.getTargets());
         var1.useDelegates(var10, JMSBeanHelper.findSubDeployment(var1.getName(), var2));
      }

      JMSForeignObserver var11 = new JMSForeignObserver(this, var1);
      ((AbstractDescriptorBean)var1).addPropertyChangeListener(var11);
      this.foreignJMSServerObservers.add(var11);
   }

   private void removeForeignServer(ForeignJMSServerMBean var1) {
      Iterator var2 = this.foreignJMSServerObservers.iterator();

      while(var2.hasNext()) {
         JMSForeignObserver var3 = (JMSForeignObserver)var2.next();
         ForeignJMSServerMBean var4 = var3.getForeignJMSServer();
         if (var1 == var4) {
            ((AbstractDescriptorBean)var1).removePropertyChangeListener(var3);
            var2.remove();
            break;
         }
      }

      JMSInteropModuleMBean var7 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var7 != null) {
         JMSBean var8 = var7.getJMSResource();
         ForeignServerBean var5 = var8.lookupForeignServer(var1.getName());
         var8.destroyForeignServer(var5);
         SubDeploymentMBean var6 = JMSBeanHelper.findSubDeployment(var1.getName(), var7);
         var7.destroySubDeployment(var6);
      }
   }

   private void addDistributedQueue(JMSDistributedQueueMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();
      DistributedQueueBean var4 = null;

      try {
         var4 = MBeanConverter.addDistributedQueue(var3, var2, var1);
      } catch (UpdateException var6) {
         this.logUpdateException(var1.getName(), var6);
         return;
      }

      var1.useDelegates(var4, JMSBeanHelper.findSubDeployment(var4.getName(), var2));
      JMSDistributedQueueObserver var5 = new JMSDistributedQueueObserver(this, var1);
      ((AbstractDescriptorBean)var1).addPropertyChangeListener(var5);
      this.distributedQueueObservers.add(var5);
   }

   private void removeDistributedQueue(JMSDistributedQueueMBean var1) {
      Iterator var2 = this.distributedQueueObservers.iterator();

      while(var2.hasNext()) {
         JMSDistributedQueueObserver var3 = (JMSDistributedQueueObserver)var2.next();
         JMSDistributedQueueMBean var4 = var3.getJMSDistributedQueue();
         if (var1 == var4) {
            ((AbstractDescriptorBean)var1).removePropertyChangeListener(var3);
            var2.remove();
            break;
         }
      }

      JMSInteropModuleMBean var7 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var7 != null) {
         JMSBean var8 = var7.getJMSResource();
         DistributedQueueBean var5 = var8.lookupDistributedQueue(var1.getName());
         var8.destroyDistributedQueue(var5);
         SubDeploymentMBean var6 = JMSBeanHelper.findSubDeployment(var1.getName(), var7);
         var7.destroySubDeployment(var6);
      }
   }

   private void addDistributedTopic(JMSDistributedTopicMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();
      DistributedTopicBean var4 = null;

      try {
         var4 = MBeanConverter.addDistributedTopic(var3, var2, var1);
      } catch (UpdateException var6) {
         this.logUpdateException(var1.getName(), var6);
         return;
      }

      var1.useDelegates(var4, JMSBeanHelper.findSubDeployment(var4.getName(), var2));
      JMSDistributedTopicObserver var5 = new JMSDistributedTopicObserver(this, var1);
      ((AbstractDescriptorBean)var1).addPropertyChangeListener(var5);
      this.distributedTopicObservers.add(var5);
   }

   private void removeDistributedTopic(JMSDistributedTopicMBean var1) {
      Iterator var2 = this.distributedTopicObservers.iterator();

      while(var2.hasNext()) {
         JMSDistributedTopicObserver var3 = (JMSDistributedTopicObserver)var2.next();
         JMSDistributedTopicMBean var4 = var3.getJMSDistributedTopic();
         if (var1 == var4) {
            ((AbstractDescriptorBean)var1).removePropertyChangeListener(var3);
            var2.remove();
            break;
         }
      }

      JMSInteropModuleMBean var7 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var7 != null) {
         JMSBean var8 = var7.getJMSResource();
         DistributedTopicBean var5 = var8.lookupDistributedTopic(var1.getName());
         var8.destroyDistributedTopic(var5);
         SubDeploymentMBean var6 = JMSBeanHelper.findSubDeployment(var1.getName(), var7);
         var7.destroySubDeployment(var6);
      }
   }

   private void addDestinationKey(JMSDestinationKeyMBean var1) {
      JMSSystemResourceMBean var2 = JMSBeanHelper.addInteropApplication(this.root);
      JMSBean var3 = var2.getJMSResource();
      DestinationKeyBean var4 = MBeanConverter.addDestinationKey(var3, var1);
      var1.useDelegates(var4);
   }

   private void removeDestinationKey(JMSDestinationKeyMBean var1) {
      JMSInteropModuleMBean var2 = JMSBeanHelper.getJMSInteropModule(this.root);
      if (var2 != null) {
         JMSBean var3 = var2.getJMSResource();
         DestinationKeyBean var4 = var3.lookupDestinationKey(var1.getName());
         var3.destroyDestinationKey(var4);
      }
   }

   public void addObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.addConnectionFactory((JMSConnectionFactoryMBean)var1);
            break;
         case 1:
            this.addJMSServer((JMSServerMBean)var1);
            break;
         case 2:
            this.addTemplate((JMSTemplateMBean)var1);
            break;
         case 3:
            this.addForeignServer((ForeignJMSServerMBean)var1);
            break;
         case 4:
            this.addDistributedQueue((JMSDistributedQueueMBean)var1);
            break;
         case 5:
            this.addDistributedTopic((JMSDistributedTopicMBean)var1);
            break;
         case 6:
            this.addDestinationKey((JMSDestinationKeyMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   public void removeObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.removeConnectionFactory((JMSConnectionFactoryMBean)var1);
            break;
         case 1:
            this.removeJMSServer((JMSServerMBean)var1);
            break;
         case 2:
            this.removeTemplate((JMSTemplateMBean)var1);
            break;
         case 3:
            this.removeForeignServer((ForeignJMSServerMBean)var1);
            break;
         case 4:
            this.removeDistributedQueue((JMSDistributedQueueMBean)var1);
            break;
         case 5:
            this.removeDistributedTopic((JMSDistributedTopicMBean)var1);
            break;
         case 6:
            this.removeDestinationKey((JMSDestinationKeyMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   DomainMBean getDomain() {
      return this.root;
   }

   private int compareMBean(WebLogicMBean var1, WebLogicMBean var2) {
      String var3 = var1.getName();
      String var4 = var2.getName();
      if (var3 == null) {
         return var4 == null ? 0 : -1;
      } else {
         return var4 == null ? 1 : var3.compareTo(var4);
      }
   }

   public int compare(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         if (var1 instanceof WebLogicMBean && var2 instanceof WebLogicMBean) {
            return this.compareMBean((WebLogicMBean)var1, (WebLogicMBean)var2);
         } else {
            throw new AssertionError("ERROR: Comparator got beans of unknown type: " + var1.getClass().getName() + "/" + var2.getClass().getName());
         }
      } else {
         throw new AssertionError("ERROR: Comparator should not get nulls: " + var1 + "/" + var2);
      }
   }
}
