package weblogic.jms.extensions;

import java.util.ArrayList;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import weblogic.jms.common.ConfigurationException;
import weblogic.jms.common.DestinationImpl;
import weblogic.management.MBeanHome;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.JMSDestinationMBean;
import weblogic.management.configuration.JMSDistributedDestinationMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedQueueMemberMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;

/** @deprecated */
public final class JMSHelper extends JMSRuntimeHelper {
   private static TargetMBean getDDTarget(String[] var0, MBeanHome var1, String var2) throws Exception {
      if (var0.length == 1) {
         return getJMSServerTarget(var0[0], var1, var2, true);
      } else {
         ClusterMBean var3 = null;

         for(int var4 = 0; var4 < var0.length; ++var4) {
            ClusterMBean var5;
            try {
               var5 = (ClusterMBean)getJMSServerTarget(var0[var4], var1, var2, true);
            } catch (ClassCastException var7) {
               throw new JMSException("Distributed destination has multiple members, but JMS server " + var0[var4] + " is not targeted to a cluster");
            }

            if (var3 == null) {
               var3 = var5;
            } else if (!var3.getName().equals(var5.getName())) {
               throw new JMSException("JMS servers for the distributed destination may not be members  of different clusters");
            }
         }

         return var3;
      }
   }

   private static TargetMBean getJMSServerTarget(String var0, MBeanHome var1, String var2) throws Exception {
      return getJMSServerTarget(var0, var1, var2, false);
   }

   private static TargetMBean getJMSServerTarget(String var0, MBeanHome var1, String var2, boolean var3) throws Exception {
      WebLogicObjectName var4 = new WebLogicObjectName(var0, "JMSServer", var2);
      JMSServerMBean var5 = (JMSServerMBean)var1.getMBean(var4);
      TargetMBean[] var6 = var5.getTargets();
      if (var6 != null && var6.length != 0) {
         if (var6.length > 1) {
            throw new JMSException("JMS server " + var0 + " has multiple targets");
         } else {
            try {
               Object var7 = null;
               if (var6[0] instanceof MigratableTargetMBean) {
                  var7 = ((MigratableTargetMBean)var6[0]).getCluster();
               } else {
                  var7 = ((ServerMBean)var6[0]).getCluster();
               }

               if (var7 == null && !var3) {
                  var7 = var6[0];
               }

               return (TargetMBean)var7;
            } catch (ClassCastException var8) {
               var8.printStackTrace();
               throw new JMSException("JMS server " + var0 + " is not targeted");
            }
         }
      } else {
         throw new JMSException("JMS server " + var0 + " has no targets");
      }
   }

   private static JMSDestinationMBean createPermanentDestination(Context var0, String var1, String var2, String var3, JMSTemplateMBean var4, String var5) throws JMSException {
      try {
         MBeanHome var6 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
         String var7 = var6.getActiveDomain().getName();
         JMSServerMBean var8 = (JMSServerMBean)var6.getAdminMBean(var1, "JMSServer", var7);
         JMSDestinationMBean var9 = (JMSDestinationMBean)var6.createAdminMBean(var2, var5, var7, (ConfigurationMBean)null);
         var9.setJNDIName(var3);
         if (var4 != null) {
            var9.setTemplate(var4);
         }

         var9.setParent(var8);
         return var9;
      } catch (Exception var10) {
         throw new weblogic.jms.common.JMSException(var10);
      }
   }

   public static void createPermanentQueueAsync(Context var0, String var1, String var2, String var3) throws JMSException {
      createPermanentDestination(var0, var1, var2, var3, (JMSTemplateMBean)null, "JMSQueue");
   }

   public static void createPermanentTopicAsync(Context var0, String var1, String var2, String var3) throws JMSException {
      createPermanentDestination(var0, var1, var2, var3, (JMSTemplateMBean)null, "JMSTopic");
   }

   private static void deletePermanentDestination(Context var0, String var1, String var2, String var3) throws ConfigurationException {
      try {
         MBeanHome var4 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
         String var10 = var4.getActiveDomain().getName();
         WebLogicObjectName var6 = new WebLogicObjectName(var1, "JMSServer", var10);
         WebLogicObjectName var7 = new WebLogicObjectName(var2, var3, var10, var6);
         JMSDestinationMBean var8 = (JMSDestinationMBean)var4.getMBean(var7);
         if (var8 == null) {
            throw new weblogic.jms.common.JMSException("Destination " + var2 + " not found");
         } else {
            var4.deleteMBean((WebLogicMBean)var8);
         }
      } catch (Exception var9) {
         if (var9 instanceof ConfigurationException) {
            throw (ConfigurationException)var9;
         } else {
            ConfigurationException var5 = new ConfigurationException(var9.toString());
            var5.setLinkedException(var9);
            throw var5;
         }
      }
   }

   public static void deletePermanentQueue(Context var0, String var1, String var2) throws ConfigurationException {
      deletePermanentDestination(var0, var1, var2, "JMSQueue");
   }

   public static void deletePermanentTopic(Context var0, String var1, String var2) throws ConfigurationException {
      deletePermanentDestination(var0, var1, var2, "JMSTopic");
   }

   private static JMSDistributedDestinationMBean createDistributedDest(String var0, String var1, String var2, MBeanHome var3, String var4) throws Exception {
      JMSTemplateMBean var5 = (JMSTemplateMBean)var3.createAdminMBean(var0, "JMSTemplate", var4, (ConfigurationMBean)null);
      JMSDistributedDestinationMBean var6 = (JMSDistributedDestinationMBean)var3.createAdminMBean(var0, var1, var4, (ConfigurationMBean)null);
      var6.setJNDIName(var2);
      var6.setTemplate(var5);
      return var6;
   }

   public static void createDistributedQueueAsync(Context var0, String var1, String var2, String[] var3) throws JMSException {
      if (var3 != null && var3.length != 0) {
         try {
            MBeanHome var4 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
            String var12 = var4.getActiveDomain().getName();
            TargetMBean var6 = getDDTarget(var3, var4, var12);
            JMSDistributedQueueMBean var7 = (JMSDistributedQueueMBean)createDistributedDest(var1, "JMSDistributedQueue", var2, var4, var12);

            for(int var8 = 0; var8 < var3.length; ++var8) {
               JMSQueueMBean var9 = (JMSQueueMBean)createPermanentDestination(var0, var3[var8], var1 + "@" + var3[var8], var2 + "@" + var3[var8], var7.getTemplate(), "JMSQueue");
               JMSDistributedQueueMemberMBean var10 = (JMSDistributedQueueMemberMBean)var4.createAdminMBean(var1 + "@" + var3[var8], "JMSDistributedQueueMember", var12, var7);
               var10.setJMSQueue(var9);
               var7.addMember(var10);
            }

            TargetMBean[] var13 = new TargetMBean[]{var6};
            var7.setTargets(var13);
         } catch (Exception var11) {
            weblogic.jms.common.JMSException var5 = new weblogic.jms.common.JMSException(var11.toString());
            var5.setLinkedException(var11);
            throw var5;
         }
      } else {
         throw new JMSException("jmsServerNames parameter may not be null");
      }
   }

   public static void createDistributedTopicAsync(Context var0, String var1, String var2, String[] var3) throws JMSException {
      if (var3 == null) {
         throw new JMSException("jmsServerNames parameter may not be null");
      } else {
         try {
            MBeanHome var4 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
            String var12 = var4.getActiveDomain().getName();
            TargetMBean var6 = getDDTarget(var3, var4, var12);
            JMSDistributedTopicMBean var7 = (JMSDistributedTopicMBean)createDistributedDest(var1, "JMSDistributedTopic", var2, var4, var12);

            for(int var8 = 0; var8 < var3.length; ++var8) {
               JMSTopicMBean var9 = (JMSTopicMBean)createPermanentDestination(var0, var3[var8], var1 + "@" + var3[var8], var2 + "@" + var3[var8], var7.getTemplate(), "JMSTopic");
               JMSDistributedTopicMemberMBean var10 = (JMSDistributedTopicMemberMBean)var4.createAdminMBean(var1 + "@" + var3[var8], "JMSDistributedTopicMember", var12, var7);
               var10.setJMSTopic(var9);
               var7.addMember(var10);
            }

            TargetMBean[] var13 = new TargetMBean[]{var6};
            var7.setTargets(var13);
         } catch (Exception var11) {
            weblogic.jms.common.JMSException var5 = new weblogic.jms.common.JMSException(var11.toString());
            var5.setLinkedException(var11);
            throw var5;
         }
      }
   }

   public static void deleteDistributedQueue(Context var0, String var1) throws JMSException {
      deleteDistributedDestination(var0, var1, "JMSDistributedQueue");
   }

   public static void deleteDistributedTopic(Context var0, String var1) throws JMSException {
      deleteDistributedDestination(var0, var1, "JMSDistributedTopic");
   }

   private static void deleteDistributedDestination(Context var0, String var1, String var2) throws JMSException {
      try {
         MBeanHome var3 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
         String var12 = var3.getActiveDomain().getName();
         WebLogicObjectName var5 = new WebLogicObjectName(var1, var2, var12);
         JMSDistributedDestinationMBean var6 = (JMSDistributedDestinationMBean)var3.getMBean(var5);
         ArrayList var7 = new ArrayList();
         int var9;
         if (var6 instanceof JMSDistributedQueueMBean) {
            JMSDistributedQueueMemberMBean[] var8 = ((JMSDistributedQueueMBean)var6).getMembers();

            for(var9 = 0; var9 < var8.length; ++var9) {
               JMSQueueMBean var10 = var8[var9].getJMSQueue();
               if (var10 != null) {
                  var7.add(var10);
               }

               var3.deleteMBean((WebLogicMBean)var8[var9]);
            }
         } else {
            if (!(var6 instanceof JMSDistributedTopicMBean)) {
               throw new JMSException("Invalid destination type");
            }

            JMSDistributedTopicMemberMBean[] var13 = ((JMSDistributedTopicMBean)var6).getMembers();

            for(var9 = 0; var9 < var13.length; ++var9) {
               JMSTopicMBean var15 = var13[var9].getJMSTopic();
               if (var15 != null) {
                  var7.add(var15);
               }

               var3.deleteMBean((WebLogicMBean)var13[var9]);
            }
         }

         Iterator var14 = var7.iterator();

         while(var14.hasNext()) {
            var3.deleteMBean((WebLogicMBean)((JMSDestinationMBean)var14.next()));
         }

         JMSTemplateMBean var16 = var6.getTemplate();
         var3.deleteMBean((WebLogicMBean)var6);
         if (var16 != null && var16.getName().equals(var1)) {
            var3.deleteMBean((WebLogicMBean)var16);
         }

      } catch (Exception var11) {
         weblogic.jms.common.JMSException var4 = new weblogic.jms.common.JMSException(var11.toString());
         var4.setLinkedException(var11);
         throw var4;
      }
   }

   public static JMSTemplateMBean getJMSTemplateConfigMBean(Context var0, String var1) throws JMSException {
      if (var1 != null && var1.length() != 0) {
         try {
            MBeanHome var2 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
            String var7 = var2.getActiveDomain().getName();
            WebLogicObjectName var4 = new WebLogicObjectName(var1, "JMSTemplate", var7);
            JMSTemplateMBean var5 = (JMSTemplateMBean)var2.getMBean(var4);
            if (var5 == null) {
               throw new JMSException("JMS template config mbean not found");
            } else {
               return var5;
            }
         } catch (Exception var6) {
            if (var6 instanceof JMSException) {
               throw (JMSException)var6;
            } else {
               weblogic.jms.common.JMSException var3 = new weblogic.jms.common.JMSException(var6.toString());
               var3.setLinkedException(var6);
               throw var3;
            }
         }
      } else {
         throw new JMSException("Null or empty template name");
      }
   }

   public static JMSTopicMBean getJMSTopicConfigMBean(Context var0, Topic var1) throws JMSException {
      if (!(var1 instanceof DestinationImpl)) {
         throw new JMSException("Unknown foreign destination");
      } else {
         DestinationImpl var2 = (DestinationImpl)var1;
         return (JMSTopicMBean)getDestinationMBean(var0, var2.getServerName(), var2.getName(), "JMSTopic");
      }
   }

   public static JMSTopicMBean getJMSTopicConfigMBean(Context var0, String var1, String var2) throws JMSException {
      return (JMSTopicMBean)getDestinationMBean(var0, var1, var2, "JMSTopic");
   }

   public static JMSQueueMBean getJMSQueueConfigMBean(Context var0, Queue var1) throws JMSException {
      if (!(var1 instanceof DestinationImpl)) {
         throw new JMSException("Unknown foreign destination");
      } else {
         DestinationImpl var2 = (DestinationImpl)var1;
         return (JMSQueueMBean)getDestinationMBean(var0, var2.getServerName(), var2.getName(), "JMSQueue");
      }
   }

   public static JMSQueueMBean getJMSQueueConfigMBean(Context var0, String var1, String var2) throws JMSException {
      return (JMSQueueMBean)getDestinationMBean(var0, var1, var2, "JMSQueue");
   }

   public static JMSServerMBean getJMSServerConfigMBean(Context var0, String var1) throws JMSException {
      try {
         MBeanHome var2 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
         String var6 = var2.getActiveDomain().getName();
         JMSServerMBean var4 = (JMSServerMBean)var2.getAdminMBean(var1, "JMSServer", var6);
         if (var4 == null) {
            throw new JMSException("JMS server config mbean not found");
         } else {
            return var4;
         }
      } catch (Exception var5) {
         if (var5 instanceof JMSException) {
            throw (JMSException)var5;
         } else {
            weblogic.jms.common.JMSException var3 = new weblogic.jms.common.JMSException(var5.toString());
            throw var3;
         }
      }
   }

   private static JMSDestinationMBean getDestinationMBean(Context var0, String var1, String var2, String var3) throws JMSException {
      try {
         MBeanHome var4 = (MBeanHome)var0.lookup("weblogic.management.adminhome");
         String var10 = var4.getActiveDomain().getName();
         WebLogicObjectName var6 = new WebLogicObjectName(var1, "JMSServer", var10);
         WebLogicObjectName var7 = new WebLogicObjectName(var2, var3, var10, var6);
         JMSDestinationMBean var8 = (JMSDestinationMBean)var4.getMBean(var7);
         if (var8 == null) {
            throw new weblogic.jms.common.JMSException("Destination " + var2 + " not found");
         } else {
            return var8;
         }
      } catch (Exception var9) {
         if (var9 instanceof JMSException) {
            throw (JMSException)var9;
         } else {
            weblogic.jms.common.JMSException var5 = new weblogic.jms.common.JMSException(var9.toString());
            var5.setLinkedException(var9);
            throw var5;
         }
      }
   }
}
