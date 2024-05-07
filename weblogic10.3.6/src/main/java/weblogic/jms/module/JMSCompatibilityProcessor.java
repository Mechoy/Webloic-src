package weblogic.jms.module;

import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSConnectionFactoryMBean;
import weblogic.management.configuration.ForeignJMSDestinationMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.JMSConnectionFactoryMBean;
import weblogic.management.configuration.JMSDestinationKeyMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedQueueMemberMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.JMSTopicMBean;

public class JMSCompatibilityProcessor {
   public static void updateConfiguration(DomainMBean var0) {
      JMSInteropModuleMBean var1 = JMSBeanHelper.getJMSInteropModule(var0);
      if (var1 != null) {
         JMSBean var2 = var1.getJMSResource();
         if (var2 != null) {
            DestinationKeyBean[] var3 = var2.getDestinationKeys();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  DestinationKeyBean var5 = var3[var4];
                  JMSDestinationKeyMBean var6 = var0.createJMSDestinationKey(var5.getName());
                  var6.useDelegates(var5);
               }
            }

            TemplateBean[] var17 = var2.getTemplates();
            if (var17 != null) {
               for(int var18 = 0; var18 < var17.length; ++var18) {
                  TemplateBean var20 = var17[var18];
                  JMSTemplateMBean var7 = var0.createJMSTemplate(var20.getName());
                  var7.useDelegates(var0, var2, var20);
               }
            }

            JMSServerMBean[] var19 = var0.getJMSServers();
            int var10;
            int var23;
            int var25;
            if (var19 != null) {
               QueueBean[] var21 = var2.getQueues();
               if (var21 != null) {
                  for(var23 = 0; var23 < var21.length; ++var23) {
                     QueueBean var8 = var21[var23];
                     String var9 = var8.getSubDeploymentName();

                     for(var10 = 0; var10 < var19.length; ++var10) {
                        JMSServerMBean var11 = var19[var10];
                        if (var11.getName().equals(var9)) {
                           JMSQueueMBean var12 = var11.createJMSQueue(var8.getName());
                           var12.useDelegates(var0, var2, var8);
                           break;
                        }
                     }
                  }
               }

               TopicBean[] var24 = var2.getTopics();
               if (var24 != null) {
                  for(var25 = 0; var25 < var24.length; ++var25) {
                     TopicBean var28 = var24[var25];
                     String var33 = var28.getSubDeploymentName();

                     for(int var36 = 0; var36 < var19.length; ++var36) {
                        JMSServerMBean var40 = var19[var36];
                        if (var40.getName().equals(var33)) {
                           JMSTopicMBean var13 = var40.createJMSTopic(var28.getName());
                           var13.useDelegates(var0, var2, var28);
                           break;
                        }
                     }
                  }
               }
            }

            JMSConnectionFactoryBean[] var22 = var2.getConnectionFactories();
            if (var22 != null) {
               for(var23 = 0; var23 < var22.length; ++var23) {
                  JMSConnectionFactoryBean var27 = var22[var23];
                  JMSConnectionFactoryMBean var29 = var0.createJMSConnectionFactory(var27.getName());
                  var29.useDelegates(var27, JMSBeanHelper.findSubDeployment(var27.getName(), var1));
               }
            }

            ForeignServerBean[] var26 = var2.getForeignServers();
            int var46;
            if (var26 != null) {
               for(var25 = 0; var25 < var26.length; ++var25) {
                  ForeignServerBean var31 = var26[var25];
                  ForeignJMSServerMBean var35 = var0.createForeignJMSServer(var31.getName());
                  var35.useDelegates(var31, JMSBeanHelper.findSubDeployment(var31.getName(), var1));
                  ForeignDestinationBean[] var38 = var31.getForeignDestinations();
                  if (var38 != null) {
                     for(int var41 = 0; var41 < var38.length; ++var41) {
                        ForeignDestinationBean var44 = var38[var41];
                        ForeignJMSDestinationMBean var14 = var35.createForeignJMSDestination(var44.getName());
                        var14.useDelegates(var44);
                     }
                  }

                  ForeignConnectionFactoryBean[] var43 = var31.getForeignConnectionFactories();
                  if (var43 != null) {
                     for(var46 = 0; var46 < var43.length; ++var46) {
                        ForeignConnectionFactoryBean var48 = var43[var46];
                        ForeignJMSConnectionFactoryMBean var15 = var35.createForeignJMSConnectionFactory(var48.getName());
                        var15.useDelegates(var48);
                     }
                  }
               }
            }

            DistributedQueueBean[] var30 = var2.getDistributedQueues();
            if (var30 != null) {
               for(int var32 = 0; var32 < var30.length; ++var32) {
                  DistributedQueueBean var37 = var30[var32];
                  JMSDistributedQueueMBean var39 = var0.createJMSDistributedQueue(var37.getName());
                  var39.useDelegates(var37, JMSBeanHelper.findSubDeployment(var37.getName(), var1));
                  DistributedDestinationMemberBean[] var45 = var37.getDistributedQueueMembers();
                  if (var45 != null) {
                     for(var46 = 0; var46 < var45.length; ++var46) {
                        DistributedDestinationMemberBean var49 = var45[var46];
                        JMSDistributedQueueMemberMBean var52 = var39.createJMSDistributedQueueMember(var49.getName());
                        var52.useDelegates(var0, var49);
                     }
                  }
               }
            }

            DistributedTopicBean[] var34 = var2.getDistributedTopics();
            if (var34 != null) {
               for(var10 = 0; var10 < var34.length; ++var10) {
                  DistributedTopicBean var42 = var34[var10];
                  JMSDistributedTopicMBean var47 = var0.createJMSDistributedTopic(var42.getName());
                  var47.useDelegates(var42, JMSBeanHelper.findSubDeployment(var42.getName(), var1));
                  DistributedDestinationMemberBean[] var50 = var42.getDistributedTopicMembers();
                  if (var50 != null) {
                     for(int var51 = 0; var51 < var50.length; ++var51) {
                        DistributedDestinationMemberBean var53 = var50[var51];
                        JMSDistributedTopicMemberMBean var16 = var47.createJMSDistributedTopicMember(var53.getName());
                        var16.useDelegates(var0, var53);
                     }
                  }
               }
            }

         }
      }
   }
}
