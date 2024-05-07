package weblogic.jms.module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TargetableBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;

public abstract class DefaultingHelper {
   private static final int QUEUE_TYPE = 0;
   private static final int TOPIC_TYPE = 1;
   private static final int MAX_RESTRICTED_TYPE = 2;
   private static final int CONN_TYPE = 2;
   private static final int FOR_TYPE = 3;
   private static final int UDQ_TYPE = 4;
   private static final int UDT_TYPE = 5;
   private static final int SAF_TYPE = 6;
   private static final int MAX_TYPE = 7;

   private static JMSServerMBean getTheOneJMSServer(ClusterMBean var0, HashMap var1, HashMap var2, DomainMBean var3) {
      if (var2.size() > 1) {
         return null;
      } else {
         JMSServerMBean var4 = null;

         for(Iterator var5 = var2.values().iterator(); var5.hasNext(); var4 = (JMSServerMBean)var5.next()) {
         }

         if (var4 != null) {
            return var4;
         } else {
            JMSServerMBean[] var6 = var3.getJMSServers();
            if (var6 == null) {
               return null;
            } else {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  JMSServerMBean var8 = var6[var7];
                  TargetMBean[] var9 = var8.getTargets();
                  if (var9 != null && var9.length > 0) {
                     TargetMBean var10 = var9[0];
                     if (var10 != null && var10 instanceof ServerMBean) {
                        ServerMBean var11 = (ServerMBean)var10;
                        if (var0 != null) {
                           ClusterMBean var14 = var11.getCluster();
                           if (var14 != null && var14.getName().equals(var0.getName())) {
                              if (var4 != null) {
                                 return null;
                              }

                              var4 = var8;
                           }
                        } else {
                           Iterator var12 = var1.values().iterator();

                           while(var12.hasNext()) {
                              ServerMBean var13 = (ServerMBean)var12.next();
                              if (var13.getName().equals(var11.getName())) {
                                 if (var4 != null) {
                                    return null;
                                 }

                                 var4 = var8;
                                 break;
                              }
                           }
                        }
                     }
                  }
               }

               return var4;
            }
         }
      }
   }

   private static boolean verifyOnenessOfWLSServers(ClusterMBean var0, HashMap var1) {
      if (var1.size() <= 0) {
         return true;
      } else {
         ClusterMBean var4;
         if (var1.size() > 1) {
            Iterator var5 = var1.values().iterator();

            while(var5.hasNext()) {
               ServerMBean var6 = (ServerMBean)var5.next();
               var4 = var6.getCluster();
               if (var4 == null) {
                  return false;
               }

               if (var0 == null) {
                  var0 = var4;
               } else if (!var0.getName().equals(var4.getName())) {
                  return false;
               }
            }

            return true;
         } else if (var0 == null) {
            return true;
         } else {
            ServerMBean var2 = null;

            for(Iterator var3 = var1.values().iterator(); var3.hasNext(); var2 = (ServerMBean)var3.next()) {
            }

            var4 = var2.getCluster();
            if (var4 == null) {
               return true;
            } else {
               return var4.getName().equals(var0.getName());
            }
         }
      }
   }

   public static HashMap getJMSDefaultTargets(JMSBean var0, DomainMBean var1, TargetMBean[] var2) {
      HashMap var3 = new HashMap();
      if (var0 != null && var2 != null && var2.length > 0) {
         HashMap var4 = new HashMap();
         HashMap var5 = new HashMap();
         HashMap var6 = new HashMap();

         for(int var7 = 0; var7 < var2.length; ++var7) {
            Object var8 = var2[var7];
            if (var8 instanceof JMSServerMBean) {
               JMSServerMBean var9 = (JMSServerMBean)var8;
               var6.put(var9.getName(), var9);
               TargetMBean[] var10 = var9.getTargets();
               if (var10 != null && var10.length >= 1 && var10[0] instanceof ServerMBean) {
                  var8 = (ServerMBean)var10[0];
               }
            }

            if (var8 instanceof ServerMBean) {
               ServerMBean var18 = (ServerMBean)var8;
               var5.put(var18.getName(), var18);
               ClusterMBean var22 = var18.getCluster();
               if (var22 != null) {
                  var8 = var22;
               }
            }

            if (var8 instanceof ClusterMBean) {
               ClusterMBean var20 = (ClusterMBean)var8;
               var4.put(var20.getName(), var20);
            }
         }

         if (var4.size() > 1) {
            return var3;
         } else {
            ClusterMBean var17 = null;

            for(Iterator var19 = var4.values().iterator(); var19.hasNext(); var17 = (ClusterMBean)var19.next()) {
            }

            if (!verifyOnenessOfWLSServers(var17, var5)) {
               return var3;
            } else {
               JMSServerMBean[] var21 = new JMSServerMBean[]{getTheOneJMSServer(var17, var5, var6, var1)};
               boolean var23 = var21[0] != null;
               HashSet var11 = new HashSet();

               int var12;
               Object var13;
               int var14;
               Object var15;
               String var16;
               for(var12 = 0; var12 < 2; ++var12) {
                  var13 = null;
                  switch (var12) {
                     case 0:
                        var13 = var0.getQueues();
                        break;
                     case 1:
                        var13 = var0.getTopics();
                        break;
                     default:
                        continue;
                  }

                  for(var14 = 0; var14 < ((Object[])var13).length; ++var14) {
                     var15 = ((Object[])var13)[var14];
                     var16 = ((TargetableBean)var15).getSubDeploymentName();
                     var11.add(var16);
                  }
               }

               for(var12 = 0; var12 < 7; ++var12) {
                  var13 = null;
                  switch (var12) {
                     case 0:
                        var13 = var0.getQueues();
                        break;
                     case 1:
                        var13 = var0.getTopics();
                        break;
                     case 2:
                        var13 = var0.getConnectionFactories();
                        break;
                     case 3:
                        var13 = var0.getForeignServers();
                        break;
                     case 4:
                        var13 = var0.getUniformDistributedQueues();
                        break;
                     case 5:
                        var13 = var0.getUniformDistributedTopics();
                        break;
                     case 6:
                        var13 = var0.getSAFImportedDestinations();
                        break;
                     default:
                        continue;
                  }

                  for(var14 = 0; var14 < ((Object[])var13).length; ++var14) {
                     var15 = ((Object[])var13)[var14];
                     var16 = ((TargetableBean)var15).getSubDeploymentName();
                     if (var11.contains(var16)) {
                        if (var23) {
                           var3.put(var16, var21);
                        }
                     } else {
                        var3.put(var16, var2);
                     }
                  }
               }

               return var3;
            }
         }
      } else {
         return var3;
      }
   }

   public static String[] getSubDeploymentNames(JMSBean var0) {
      HashSet var1 = new HashSet(7);
      addTargetables(var1, var0.getConnectionFactories());
      addTargetables(var1, var0.getForeignServers());
      addTargetables(var1, var0.getQueues());
      addTargetables(var1, var0.getSAFImportedDestinations());
      addTargetables(var1, var0.getTopics());
      addTargetables(var1, var0.getUniformDistributedQueues());
      addTargetables(var1, var0.getUniformDistributedTopics());
      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   private static void addTargetables(Set var0, TargetableBean[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var0.add(var1[var2].getSubDeploymentName());
      }

   }

   public static void getJMSDefaultTargets(JMSBean var0, DomainMBean var1, BasicDeploymentMBean var2) {
      if (var2 != null) {
         SubDeploymentMBean[] var3 = var2.getSubDeployments();
         if (var3.length == 0) {
            TargetMBean[] var4 = var2.getTargets();
            if (var4 != null && var4.length > 0) {
               HashMap var5 = getJMSDefaultTargets(var0, var1, var4);
               Iterator var6 = var5.keySet().iterator();

               while(var6.hasNext()) {
                  String var7 = (String)var6.next();
                  TargetMBean[] var8 = (TargetMBean[])((TargetMBean[])var5.get(var7));
                  SubDeploymentMBean var9 = var2.createSubDeployment(var7);

                  try {
                     var9.setTargets(var8);
                  } catch (InvalidAttributeValueException var11) {
                  } catch (DistributedManagementException var12) {
                  }
               }

            }
         }
      }
   }
}
