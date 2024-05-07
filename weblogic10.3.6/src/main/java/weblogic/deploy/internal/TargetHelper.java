package weblogic.deploy.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.ModuleListener;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.collections.ArraySet;

public class TargetHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static ServerMBean localServer;

   public static String[] getModulesForTarget(DeploymentData var0, DomainMBean var1) {
      if (var0.isTargetsFromConfig()) {
         return null;
      } else if (var0.hasGlobalTarget(localServer.getName())) {
         return null;
      } else {
         Map var2 = var0.getAllModuleTargets();
         Iterator var3 = var2.keySet().iterator();
         ArrayList var4 = new ArrayList();

         while(var3.hasNext()) {
            String var5 = (String)var3.next();
            String[] var6 = (String[])((String[])var2.get(var5));
            if (targetsContainLocalServer(var6, var1)) {
               var4.add(var5);
            }
         }

         if (var4.size() > 0) {
            return (String[])((String[])var4.toArray(new String[0]));
         } else {
            return null;
         }
      }
   }

   private static boolean targetsContainLocalServer(String[] var0, DomainMBean var1) {
      TargetMBean[] var2;
      try {
         DomainMBean var3 = var1;
         if (var1 == null) {
            var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         }

         var2 = lookupTargetMBeans(var3, var0);
      } catch (InvalidTargetException var4) {
         throw new AssertionError(var4);
      } catch (Exception var5) {
         throw new AssertionError(var5);
      }

      return TargetUtils.isDeployedLocally(var2);
   }

   public static boolean isAppTargetedToCurrentServer(BasicDeploymentMBean var0) {
      TargetMBean[] var1 = var0.getTargets();
      String var2 = localServer.getName();
      ArraySet var3 = new ArraySet();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3.addAll(var1[var4].getServerNames());
      }

      return var3.contains(var2) ? true : areSubDeploymentsTargetedToCurrentServer(var0);
   }

   private static boolean areSubDeploymentsTargetedToCurrentServer(BasicDeploymentMBean var0) {
      List var1 = getAllSubDeployments(var0);
      String var2 = localServer.getName();
      Iterator var3 = var1.iterator();

      while(true) {
         SubDeploymentMBean var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (SubDeploymentMBean)var3.next();
         } while(var4 == null);

         TargetMBean[] var5 = var4.getTargets();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            Set var7 = var5[var6].getServerNames();
            if (var7.contains(var2)) {
               return true;
            }
         }
      }
   }

   private static List getAllSubDeployments(BasicDeploymentMBean var0) {
      ArrayList var1 = new ArrayList();
      SubDeploymentMBean[] var2 = var0.getSubDeployments();
      if (var2 != null && var2.length != 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3]);
            List var4 = getAllSubDeployments(var2[var3]);
            var1.addAll(var4);
         }

         return var1;
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   private static List getAllSubDeployments(SubDeploymentMBean var0) {
      ArrayList var1 = new ArrayList();
      SubDeploymentMBean[] var2 = var0.getSubDeployments();
      if (var2 != null && var2.length != 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3]);
            List var4 = getAllSubDeployments(var2[var3]);
            var1.addAll(var4);
         }

         return var1;
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   public static TargetMBean[] lookupTargetMBeans(DomainMBean var0, String[] var1) throws InvalidTargetException {
      TargetMBean[] var2 = new TargetMBean[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         try {
            var2[var3] = var0.lookupTarget(var1[var3]);
            if (var2[var3] == null) {
               throw new InvalidTargetException(var1[var3]);
            }
         } catch (IllegalArgumentException var5) {
            throw new InvalidTargetException(var1[var3]);
         }
      }

      return var2;
   }

   public static String[] getNonGlobalModules(DeploymentData var0, BasicDeployment var1, DomainMBean var2) {
      if (var0.isTargetsFromConfig()) {
         return null;
      } else {
         TargetMBean[] var3 = var1.getDeploymentMBean().getTargets();
         if (TargetUtils.isDeployedLocally(var3)) {
            for(int var10 = 0; var10 < var3.length; ++var10) {
               TargetMBean var11 = var3[var10];
               if (var0.hasGlobalTarget(var11.getName())) {
                  return null;
               }
            }

            return getModulesForTarget(var0, var2);
         } else {
            DeploymentState var4 = var1.getState();
            if (null == var4) {
               return getModulesForTarget(var0, var2);
            } else {
               TargetModuleState[] var5 = var4.getTargetModules();
               boolean var6 = true;
               String[] var7 = getModulesForTarget(var0, var2);
               if (var7 != null && var5 != null) {
                  List var8 = Arrays.asList(var7);

                  for(int var9 = 0; var9 < var5.length; ++var9) {
                     if (!var5[var9].getCurrentState().equals(ModuleListener.STATE_NEW.toString()) && !var8.contains(var5[var9].getModuleId())) {
                        var6 = false;
                     }
                  }
               }

               return !var6 ? var7 : null;
            }
         }
      }
   }

   public static String[] getNonGlobalSubModules(DeploymentData var0, BasicDeployment var1) {
      if (var0.isTargetsFromConfig()) {
         return null;
      } else {
         DeploymentState var2 = var1.getState();
         if (var2 == null) {
            return null;
         } else {
            TargetModuleState[] var3 = var2.getTargetModules();
            ArrayList var4 = new ArrayList();

            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (var3[var5].isSubmodule() && !ModuleListener.STATE_NEW.toString().equals(var3[var5].getCurrentState())) {
                  var4.add(var3[var5].getSubmoduleId());
               }
            }

            Map var11 = var0.getAllSubModuleTargets();
            ArrayList var6 = new ArrayList();
            Iterator var7 = var11.keySet().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               HashMap var9 = (HashMap)var11.get(var8);
               Iterator var10 = var9.keySet().iterator();

               while(var10.hasNext()) {
                  var6.add((String)var10.next());
               }
            }

            if (var6.containsAll(var4)) {
               return null;
            } else {
               Object[] var12 = var6.toArray();
               if (0 == var12.length) {
                  return null;
               } else {
                  String[] var13 = new String[var12.length];

                  for(int var14 = 0; var14 < var12.length; ++var14) {
                     var13[var14] = (String)var12[var14];
                  }

                  return var13;
               }
            }
         }
      }
   }

   public static ClusterMBean getTargetCluster(String var0) {
      ClusterMBean var1 = null;
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ServerMBean var3 = var2.lookupServer(var0);
      if (var3 != null) {
         var1 = var3.getCluster();
      }

      if (var1 == null) {
         var1 = var2.lookupCluster(var0);
      }

      return var1;
   }

   public static Set getAllTargetedServers(BasicDeploymentMBean var0) {
      HashSet var1 = new HashSet();
      var1.addAll(resolveTargetMBeans(var0.getTargets()));
      SubDeploymentMBean[] var2 = var0.getSubDeployments();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.addAll(resolveTargetMBeans(var2[var3].getTargets()));
         var1.addAll(resolveTargetInfos(var2[var3].getSubDeployments()));
      }

      return var1;
   }

   private static Set resolveTargetMBeans(TargetMBean[] var0) {
      HashSet var1 = new HashSet();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.addAll(var0[var2].getServerNames());
         }
      }

      return var1;
   }

   private static Set resolveTargetInfos(TargetInfoMBean[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.addAll(resolveTargetMBeans(var0[var2].getTargets()));
      }

      return var1;
   }

   public static int getTypeForTarget(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (var1.lookupCluster(var0) != null) {
         return 1;
      } else if (var1.lookupServer(var0) != null) {
         return 2;
      } else if (var1.lookupVirtualHost(var0) != null) {
         return 3;
      } else if (var1.lookupJMSServer(var0) != null) {
         return 4;
      } else {
         return var1.lookupSAFAgent(var0) != null ? 5 : 0;
      }
   }

   public static boolean isTargetedLocaly(BasicDeploymentMBean var0) {
      Set var1 = getAllTargetedServers(var0);
      printTargetList(var0, var1);
      return var1.contains(ManagementService.getRuntimeAccess(kernelId).getServer().getName());
   }

   public static boolean isTargetedLocally(DeploymentMBean var0) {
      Set var1 = resolveTargetMBeans(var0.getTargets());
      return var1.contains(ManagementService.getRuntimeAccess(kernelId).getServer().getName());
   }

   public static boolean isPinnedToServerInCluster(BasicDeploymentMBean var0) {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      String var2 = var1.getName();
      ClusterMBean var3 = var1.getCluster();
      if (var3 == null) {
         return false;
      } else {
         Set var4 = var3.getServerNames();
         var4.remove(var2);
         Set var5 = getAllTargetedServers(var0);
         printTargetList(var0, var5);
         Iterator var6 = var4.iterator();

         do {
            if (!var6.hasNext()) {
               return false;
            }
         } while(!var5.contains(var6.next()));

         return true;
      }
   }

   private static void printTargetList(BasicDeploymentMBean var0, Set var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Targets for app, " + var0.getName());
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Debug.deploymentDebug("   " + var2.next());
         }
      }

   }

   public static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public static String[] getTargetNames(TargetMBean[] var0) {
      if (var0 == null) {
         return null;
      } else {
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2].getName();
         }

         return var1;
      }
   }

   static {
      localServer = ManagementService.getRuntimeAccess(kernelId).getServer();
   }
}
