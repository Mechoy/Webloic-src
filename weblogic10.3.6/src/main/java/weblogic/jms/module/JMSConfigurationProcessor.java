package weblogic.jms.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.JMSLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.JMSConnectionFactoryMBean;
import weblogic.management.configuration.JMSDestinationKeyMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class JMSConfigurationProcessor implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      boolean var2 = false;
      JMSDestinationKeyMBean[] var3 = var1.getJMSDestinationKeys();
      if (var3 != null && var3.length > 0) {
         var2 = true;
      }

      JMSConnectionFactoryMBean[] var4 = var1.getJMSConnectionFactories();
      if (var4 != null && var4.length > 0) {
         var2 = true;
      }

      ForeignJMSServerMBean[] var5 = var1.getForeignJMSServers();
      if (var5 != null && var5.length > 0) {
         var2 = true;
      }

      JMSServerMBean[] var6 = var1.getJMSServers();
      LinkedList var7 = new LinkedList();
      LinkedList var8 = new LinkedList();
      if (var6 != null) {
         for(int var9 = 0; var9 < var6.length; ++var9) {
            JMSQueueMBean[] var10 = var6[var9].getJMSQueues();
            if (var10 != null) {
               for(int var11 = 0; var11 < var10.length; ++var11) {
                  var2 = true;
                  var7.add(var10[var11]);
               }
            }

            JMSTopicMBean[] var25 = var6[var9].getJMSTopics();
            if (var25 != null) {
               for(int var12 = 0; var12 < var25.length; ++var12) {
                  var2 = true;
                  var8.add(var25[var12]);
               }
            }
         }
      }

      JMSQueueMBean[] var23 = null;
      if (var7.size() > 0) {
         var23 = new JMSQueueMBean[var7.size()];
         var23 = (JMSQueueMBean[])((JMSQueueMBean[])var7.toArray(var23));
      }

      JMSTopicMBean[] var24 = null;
      if (var8.size() > 0) {
         var24 = new JMSTopicMBean[var8.size()];
         var24 = (JMSTopicMBean[])((JMSTopicMBean[])var8.toArray(var24));
      }

      JMSDistributedQueueMBean[] var26 = var1.getJMSDistributedQueues();
      if (var26 != null && var26.length > 0) {
         var2 = true;
      }

      JMSDistributedTopicMBean[] var27 = var1.getJMSDistributedTopics();
      if (var27 != null && var27.length > 0) {
         var2 = true;
      }

      JMSTemplateMBean[] var13 = var1.getJMSTemplates();
      if (var13 != null && var13.length > 0) {
         var2 = true;
      }

      if (var2) {
         JMSSystemResourceMBean var14 = JMSBeanHelper.addInteropApplication(var1);
         JMSBean var15 = var14.getJMSResource();
         int var16;
         if (var3 != null) {
            for(var16 = 0; var16 < var3.length; ++var16) {
               MBeanConverter.addDestinationKey(var15, var3[var16]);
            }
         }

         HashMap var18;
         Iterator var19;
         String var20;
         ArrayList var21;
         TargetMBean[] var22;
         if (var4 != null) {
            for(var16 = 0; var16 < var4.length; ++var16) {
               JMSConnectionFactoryMBean var17 = var4[var16];
               var18 = MBeanConverter.splitDeployment(var17);
               if (var18.size() >= 1) {
                  JMSLogger.logSplitDeployment("JMSConnectionFactory", var17.getName());
                  var19 = var18.keySet().iterator();

                  while(var19.hasNext()) {
                     var20 = (String)var19.next();
                     var21 = (ArrayList)var18.get(var20);
                     var22 = (TargetMBean[])((TargetMBean[])var21.toArray(new TargetMBean[0]));
                     MBeanConverter.addJMSConnectionFactory(var15, var14, var17, var20, var22);
                  }
               } else {
                  MBeanConverter.addJMSConnectionFactory(var15, var14, var17, var17.getName(), var17.getTargets());
               }
            }
         }

         if (var5 != null) {
            for(var16 = 0; var16 < var5.length; ++var16) {
               ForeignJMSServerMBean var28 = var5[var16];
               var18 = MBeanConverter.splitDeployment(var28);
               if (var18.size() >= 1) {
                  JMSLogger.logSplitDeployment("ForeignJMSServer", var28.getName());
                  var19 = var18.keySet().iterator();

                  while(var19.hasNext()) {
                     var20 = (String)var19.next();
                     var21 = (ArrayList)var18.get(var20);
                     var22 = (TargetMBean[])((TargetMBean[])var21.toArray(new TargetMBean[0]));
                     MBeanConverter.addForeignJMSServer(var15, var14, var28, var20, var22);
                  }
               } else {
                  MBeanConverter.addForeignJMSServer(var15, var14, var28, var28.getName(), var28.getTargets());
               }
            }
         }

         if (var23 != null) {
            for(var16 = 0; var16 < var23.length; ++var16) {
               MBeanConverter.addQueue(var15, var14, var23[var16]);
            }
         }

         if (var24 != null) {
            for(var16 = 0; var16 < var24.length; ++var16) {
               MBeanConverter.addTopic(var15, var14, var24[var16]);
            }
         }

         if (var26 != null) {
            for(var16 = 0; var16 < var26.length; ++var16) {
               MBeanConverter.addDistributedQueue(var15, var14, var26[var16]);
            }
         }

         if (var27 != null) {
            for(var16 = 0; var16 < var27.length; ++var16) {
               MBeanConverter.addDistributedTopic(var15, var14, var27[var16]);
            }
         }

         if (var13 != null) {
            for(var16 = 0; var16 < var13.length; ++var16) {
               MBeanConverter.addTemplate(var15, var13[var16]);
            }
         }

      }
   }
}
