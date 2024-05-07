package weblogic.deploy.internal.adminserver;

import java.beans.BeanInfo;
import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.ApplicationFileManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentOrder;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.service.ConfigurationContext;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.DomainTargetedMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.management.internal.PendingDirectoryManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.provider.internal.DescriptorInfoUtils;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.VirtualJarFile;

final class ConfigChangesHandler {
   private static final String TARGETS_PROP_NAME = "Targets";
   private static final String CANDIDATE_SERVERS_PROP_NAME = "ConstrainedCandidateServers";
   private static final String TASK_ID_PREFIX = "weblogic.deploy.configChangeTask.";
   private static int curTaskId = 0;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static AppRuntimeStateRuntimeMBean appRTMBean;
   private static DeploymentBeanFactory beanFactory = DeploymentServerService.getDeploymentBeanFactory();

   public static List[] configChanged(DeploymentRequest var0, ConfigurationContext var1) {
      boolean var2 = false;
      boolean var3 = false;
      Iterator var4 = getDescriptorDiff(var1);
      if (var4 == null) {
         return null;
      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.configChanged: deployRequest=" + var0.getId() + ", diffs=" + dumpDiff(var1));
         }

         OrderedDeployments var5 = new OrderedDeployments();
         ArrayList var6 = new ArrayList();
         ArrayList var7 = new ArrayList();
         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();

         while(var4.hasNext()) {
            BeanUpdateEvent var12 = (BeanUpdateEvent)var4.next();
            BeanUpdateEvent.PropertyUpdate[] var13 = var12.getUpdateList();
            DescriptorBean var14 = var12.getSourceBean();
            boolean var15 = false;

            for(int var16 = 0; var16 < var13.length; ++var16) {
               BeanUpdateEvent.PropertyUpdate var17 = var13[var16];
               Object var18;
               if (beanFactory.isDeployerInitiatedBeanUpdate(var14, var17)) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("ConfigChangesHandler.isDeployerInitiatedBeanUpdate " + var17);
                  }

                  if (var17.getUpdateType() == 2) {
                     var18 = var17.getAddedObject();
                     if (var18 instanceof AppDeploymentMBean) {
                        AppDeploymentMBean var23 = (AppDeploymentMBean)var18;
                        var11.add(var23);
                     }
                  }
               } else {
                  switch (var17.getUpdateType()) {
                     case 1:
                        if (isTargetsChanged(var14, var17) || isCandidateServersChanged(var14, var17)) {
                           var15 = true;
                        }
                        break;
                     case 2:
                        var18 = var17.getAddedObject();
                        if (var18 instanceof BasicDeploymentMBean) {
                           deploymentAdded(var5, (BasicDeploymentMBean)var18);
                           if (var18 instanceof SystemResourceMBean) {
                              if (var18 instanceof JMSSystemResourceMBean) {
                                 SubDeploymentMBean[] var22 = ((JMSSystemResourceMBean)var18).getSubDeployments();
                                 if (var22 != null && var22.length > 0) {
                                    var3 = true;
                                 }
                              }

                              var10.add(var18);
                           }
                        } else if (var18 instanceof SubDeploymentMBean) {
                           var6.add(var18);
                           if (var12.getProposedBean() instanceof JMSSystemResourceMBean) {
                              var3 = true;
                           }
                        } else if (isTargetsChanged(var14, var17)) {
                           var8.add(var18);
                        } else if (isCandidateServersChanged(var14, var17)) {
                           var15 = true;
                        }

                        if (var18 instanceof JMSServerMBean) {
                           var2 = true;
                        }
                        break;
                     case 3:
                        Object var19 = var17.getRemovedObject();
                        if (var19 instanceof BasicDeploymentMBean) {
                           deploymentRemoved(var5, (BasicDeploymentMBean)var19);
                           if (var19 instanceof SystemResourceMBean) {
                              var10.add(var19);
                           }
                        } else if (var19 instanceof SubDeploymentMBean) {
                           var7.add(var19);
                        } else if (isTargetsChanged(var14, var17)) {
                           var9.add(var19);
                        } else if (isCandidateServersChanged(var14, var17)) {
                           var15 = true;
                        }
                  }
               }
            }

            if (var15) {
               targetsChanged(var5, getTargets((WebLogicMBean)var14), (WebLogicMBean)var12.getProposedBean());
            }

            if (var9.size() > 0) {
               targetsRemoved(var5, (WebLogicMBean)var12.getProposedBean(), var9);
               var9.clear();
            }

            if (var8.size() > 0) {
               targetsAdded(var5, (WebLogicMBean)var12.getProposedBean(), Arrays.asList(getTargets((WebLogicMBean)var14)), var8);
               var8.clear();
            }

            if (var7.size() > 0) {
               subDepsRemoved(var5, (TargetInfoMBean)var12.getProposedBean(), var7);
               var7.clear();
            }

            if (var6.size() > 0) {
               subDepsAdded(var5, (TargetInfoMBean)var12.getProposedBean(), var6);
               var6.clear();
            }
         }

         Map var20 = getExternalDescritorDiffs(var1);
         handleExternalTreeChanges(var5, var20, var0.getId(), var10);
         var10.clear();
         boolean var21 = !var3 || !var2;
         return var5.getAllDeployments(var0, var21, var11);
      }
   }

   public static void restartSystemResource(SystemResourceMBean var0) throws ManagementException {
      TargetMBean[] var1 = getTargets(var0);
      if (var1 != null && var1.length != 0) {
         DeploymentData var2 = new DeploymentData();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.addTarget(var1[var3].getName(), (String[])null);
         }

         Deployment var5 = findOrCreateDeployment(9, var0, var2, false, false, true);
         DeploymentTaskRuntime var4 = ((weblogic.deploy.internal.Deployment)var5).getDeploymentTaskRuntime();
         var4.start();
      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.restartSystemResource returning immediately since SystemResource '" + var0.getName() + "' does not have any targets");
         }

      }
   }

   private static void deploymentAdded(OrderedDeployments var0, BasicDeploymentMBean var1) {
      TargetMBean[] var2 = getTargets(var1);
      if (var2 != null && var2.length != 0) {
         DeployInfo var3 = createDeploymentInfo(var1, Arrays.asList(var2), 1, true);
         var3.deployData.setNewApp(true);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.deploymentAdded for " + var1.getName() + ", deployData=" + var3.deployData);
         }

         var3.requireRestart = false;
         var0.addDeploymentInfo(var3);
      }
   }

   private static void deploymentRemoved(OrderedDeployments var0, BasicDeploymentMBean var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("ConfigChangesHandler.deploymentRemoved for " + var1.getName() + ", bean: " + var1);
         TargetMBean[] var2 = getTargets(var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Set var4 = var2[var3].getServerNames();
            String[] var5 = new String[0];
            String[] var6 = (String[])((String[])var4.toArray(var5));
            Debug.deploymentDebug("target[" + var3 + "] = " + var2[var3]);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               Debug.deploymentDebug("\t sub-target[" + var7 + "] = " + var6[var7]);
            }
         }
      }

      DeploymentData var8 = new DeploymentData();
      String[] var9 = new String[0];
      String[] var10 = (String[])((String[])TargetHelper.getAllTargetedServers(var1).toArray(var9));
      if (var10 != null && var10.length > 0) {
         var8.setGlobalTargets(var10);
         UndeployDeployInfo var11 = new UndeployDeployInfo(var1, var8, false);
         var11.requireRestart = false;
         var0.addDeploymentInfo(var11);
      }

   }

   private static void subDepsAdded(OrderedDeployments var0, TargetInfoMBean var1, List var2) {
      if (var2.size() != 0) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.subDepsAdded for " + var1 + ", subDeps=" + var2 + ", isJMSModule=" + isJMSModule(var1));
         }

         if (isJMSModule(var1)) {
            jmsSubDepsChanged(var0, var2, true);
         } else {
            appSubDepsChanged(var0, var1, var2, true);
         }

      }
   }

   private static void subDepsRemoved(OrderedDeployments var0, TargetInfoMBean var1, List var2) {
      if (var2.size() != 0) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.subDepsRemoved for " + var1 + ", subDeps=" + var2 + ", isJMSModule=" + isJMSModule(var1));
         }

         if (isJMSModule(var1)) {
            jmsSubDepsChanged(var0, var2, false);
         } else {
            appSubDepsChanged(var0, var1, var2, false);
         }

      }
   }

   private static void jmsSubDepsChanged(OrderedDeployments var0, List var1, boolean var2) {
      RedeployDeployInfo var3 = new RedeployDeployInfo((BasicDeploymentMBean)null, new DeploymentData(), var2);
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         SubDeploymentMBean var5 = (SubDeploymentMBean)var4.next();
         TargetMBean[] var6 = var5.getTargets();
         if (var6 != null && var6.length != 0) {
            populateDeploymentInfo(var3, var5, Arrays.asList(var6));
         }
      }

      var3.requireRestart = false;
      var0.addDeploymentInfo(var3);
   }

   private static void appSubDepsChanged(OrderedDeployments var0, TargetInfoMBean var1, List var2, boolean var3) {
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         SubDeploymentMBean var7 = (SubDeploymentMBean)var6.next();
         TargetMBean[] var4;
         TargetMBean[] var5;
         if (var3) {
            var4 = getTargets(var1);
            var5 = getTargets(var7);
         } else {
            var4 = getTargets(var7);
            var5 = getTargets(var1);
         }

         List var8 = getTargetsNotIn(var4, var5);
         List var9 = getTargetsNotIn(var5, var4);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.appSubDepsChanged('" + var7 + ")" + " undeployTargets=" + var8 + ", deployTargets=" + var9);
         }

         if (var8.size() > 0) {
            targetsRemoved(var0, var7, var8);
         }

         if (var9.size() > 0) {
            targetsAdded(var0, var7, Arrays.asList(var4), var9);
         }
      }

   }

   private static void targetsAdded(OrderedDeployments var0, WebLogicMBean var1, List var2, List var3) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("ConfigChangesHandler.targetsAdded for " + var1 + ", addedTargets=" + getTargetsString(var3));
      }

      if (var1 instanceof TargetInfoMBean) {
         deploymentTargetsAdded(var0, (TargetInfoMBean)var1, var2, var3);
      } else {
         Object var4 = var1;
         boolean var5 = var1 instanceof MigratableTargetMBean;
         if (var5) {
            var4 = getJMSServer((MigratableTargetMBean)var1);
         }

         if (var4 instanceof DeploymentMBean && var4 instanceof TargetMBean) {
            deploymentMBeanTargetChanged(var0, (DeploymentMBean)var4, Collections.EMPTY_LIST, var3, var5, 9);
         }
      }

   }

   private static void targetsRemoved(OrderedDeployments var0, WebLogicMBean var1, List var2) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("ConfigChangesHandler.targetsRemoved for " + var1 + ", removedTargets=" + getTargetsString(var2));
      }

      if (var1 instanceof TargetInfoMBean) {
         deploymentTargetsRemoved(var0, (TargetInfoMBean)var1, var2);
      } else {
         Object var3 = var1;
         boolean var4 = var1 instanceof MigratableTargetMBean;
         if (var4) {
            var3 = getJMSServer((MigratableTargetMBean)var1);
         }

         if (var3 instanceof DeploymentMBean && var3 instanceof TargetMBean) {
            deploymentMBeanTargetChanged(var0, (DeploymentMBean)var3, var2, Collections.EMPTY_LIST, var4, 12);
         }
      }

   }

   private static void targetsChanged(OrderedDeployments var0, TargetMBean[] var1, WebLogicMBean var2) {
      targetsChanged(var0, var1, getTargets(var2), var2);
   }

   private static void targetsChanged(OrderedDeployments var0, TargetMBean[] var1, TargetMBean[] var2, WebLogicMBean var3) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("ConfigChangesHandler.targetsChanged for " + var3 + ", oldTargets=" + getTargetsString(var1) + ", newTargets=" + getTargetsString(var2));
      }

      List var4 = getTargetsNotIn(var1, var2);
      List var5 = getTargetsNotIn(var2, var1);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("ConfigChangesHandler undeployTargets=" + var4 + ", deployTargets=" + var5);
      }

      if (var4.size() > 0) {
         targetsRemoved(var0, var3, var4);
      }

      if (var5.size() > 0) {
         targetsAdded(var0, var3, Arrays.asList(var1), var5);
      }

      if (var4.size() == 0 && var5.size() == 0) {
         targetsAdded(var0, var3, Arrays.asList(var1), Arrays.asList(var2));
      }

   }

   private static void deploymentTargetsAdded(OrderedDeployments var0, TargetInfoMBean var1, List var2, List var3) {
      if (var3.size() != 0) {
         ArrayList var4 = new ArrayList(var3);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.depTargetsAdded for " + var1 + ", targets=" + var4 + ", isJMSModule=" + isJMSModule(var1));
         }

         if (!(var1 instanceof SystemResourceMBean)) {
            Iterator var5 = var4.iterator();

            label57:
            while(true) {
               TargetMBean var6;
               ClusterMBean var8;
               do {
                  if (!var5.hasNext()) {
                     if (var4.size() == 0) {
                        return;
                     }
                     break label57;
                  }

                  var6 = (TargetMBean)var5.next();
                  String var7 = var6.getName();
                  var8 = TargetHelper.getTargetCluster(var7);
               } while(var8 == null);

               ClusterDeployInfo var9 = new ClusterDeployInfo((BasicDeploymentMBean)null, new DeploymentData(), 1, true, var8.getName());
               DeployInfo var10 = var9.delegate;
               ArrayList var11 = new ArrayList();
               var11.add(var6);
               populateDeploymentInfo(var10, var1, var11);
               var5.remove();
               if ((var10.hasModuleTargets() || var10.hasSubModuleTargets()) && (isJMSModule(var1) || isJMSModule((TargetInfoMBean)var1.getParent()))) {
                  var9.setOp(9);
               }

               if (isTargetedToCluster(var2, var8)) {
                  var9.setOp(9);
               }

               var9.setRequireRestart(false);
               var0.addDeploymentInfo(var9);
            }
         }

         DeployInfo var12 = createDeploymentInfo(var1, var4, 1, true);
         if ((var12.hasModuleTargets() || var12.hasSubModuleTargets()) && (isJMSModule(var1) || isJMSModule((TargetInfoMBean)var1.getParent()))) {
            var12.op = 9;
         }

         var12.requireRestart = false;
         var0.addDeploymentInfo(var12);
      }
   }

   private static void deploymentTargetsRemoved(OrderedDeployments var0, TargetInfoMBean var1, List var2) {
      if (var2.size() != 0) {
         ArrayList var3 = new ArrayList(var2);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.depTargetsRemoved for " + var1 + ", targets=" + var3 + ", isJMSModule=" + isJMSModule(var1));
         }

         if (!(var1 instanceof SystemResourceMBean)) {
            Iterator var4 = var3.iterator();

            label61:
            while(true) {
               TargetMBean var5;
               ClusterMBean var7;
               do {
                  if (!var4.hasNext()) {
                     if (var3.size() == 0) {
                        return;
                     }
                     break label61;
                  }

                  var5 = (TargetMBean)var4.next();
                  String var6 = var5.getName();
                  var7 = TargetHelper.getTargetCluster(var6);
               } while(var7 == null);

               ClusterDeployInfo var8 = new ClusterDeployInfo((BasicDeploymentMBean)null, new DeploymentData(), 4, false, var7.getName());
               DeployInfo var9 = var8.delegate;
               ArrayList var10 = new ArrayList();
               var10.add(var5);
               populateDeploymentInfo(var9, var1, var10);
               var4.remove();
               if ((var9.hasModuleTargets() || var9.hasSubModuleTargets()) && (isJMSModule(var1) || isJMSModule((TargetInfoMBean)var1.getParent()))) {
                  var8.setOp(9);
               }

               if (isTargetedToCluster(Arrays.asList(getTargets(var1)), var7)) {
                  var8.setOp(9);
               }

               var8.setRequireRestart(false);
               var0.addDeploymentInfo(var8);
            }
         }

         DeployInfo var11 = createDeploymentInfo(var1, var3, 4, false);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.depTargetsRemoved : created DeploymentInfo : " + var11);
         }

         if ((var11.hasModuleTargets() || var11.hasSubModuleTargets()) && (isJMSModule(var1) || isJMSModule((TargetInfoMBean)var1.getParent()))) {
            var11.op = 9;
         }

         var11.requireRestart = false;
         var0.addDeploymentInfo(var11);
      }
   }

   private static void createAndAddClusterDeploymentInfos(BasicDeploymentMBean var0, List var1, List var2) {
   }

   private static void deploymentMBeanTargetChanged(OrderedDeployments var0, DeploymentMBean var1, List var2, List var3, boolean var4, int var5) {
      DomainMBean var6 = getDomainBean(false);
      String var7 = var1.getName();
      DeploymentData var8 = null;
      DeploymentData var9 = null;
      JMSSystemResourceMBean[] var10 = var6.getJMSSystemResources();

      for(int var11 = 0; var11 < var10.length; ++var11) {
         JMSSystemResourceMBean var12 = var10[var11];
         if (var8 == null) {
            var8 = new DeploymentData();
         }

         if (var9 == null) {
            var9 = new DeploymentData();
         }

         if (isModuleTargeted((BasicDeploymentMBean)var12, var7, var2, var3, var9, var8, var4)) {
            createAndAddDeploymentInfos(var0, var12, var2, var3, var9, var8, var5);
            var8 = null;
            var9 = null;
         }
      }

      AppDeploymentMBean[] var17 = AppDeploymentHelper.getAppsAndLibs(var6);

      for(int var18 = 0; var18 < var17.length; ++var18) {
         AppDeploymentMBean var13 = var17[var18];
         if (!isEar(var13)) {
            if (var8 == null) {
               var8 = new DeploymentData();
            }

            if (var9 == null) {
               var9 = new DeploymentData();
            }

            if (isModuleTargeted((BasicDeploymentMBean)var13, var7, var2, var3, var9, var8, var4)) {
               createAndAddDeploymentInfos(var0, var13, var2, var3, var9, var8, var5);
               var8 = null;
               var9 = null;
            }
         } else {
            SubDeploymentMBean[] var14 = var13.getSubDeployments();

            for(int var15 = 0; var15 < var14.length; ++var15) {
               SubDeploymentMBean var16 = var14[var15];
               if (var8 == null) {
                  var8 = new DeploymentData();
               }

               if (var9 == null) {
                  var9 = new DeploymentData();
               }

               if (isModuleTargeted(var16, var7, var2, var3, var9, var8, var4)) {
                  createAndAddDeploymentInfos(var0, var13, var2, var3, var9, var8, var5);
                  var8 = null;
                  var9 = null;
               }
            }
         }
      }

   }

   private static boolean isModuleTargeted(BasicDeploymentMBean var0, String var1, List var2, List var3, DeploymentData var4, DeploymentData var5, boolean var6) {
      if (isTargetIn(var1, var0.getTargets())) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("isModuleTargeted addTarget " + var1 + " for " + var0.getName());
         }

         addTargetsToData(var1, var2, var3, var4, var5, var6);
         return true;
      } else {
         return isJMSModule(var0) && isSubModulesTargeted((String)null, var0.getSubDeployments(), var1, var2, var3, var4, var5, var6);
      }
   }

   private static boolean isModuleTargeted(SubDeploymentMBean var0, String var1, List var2, List var3, DeploymentData var4, DeploymentData var5, boolean var6) {
      if (isTargetIn(var1, var0.getTargets())) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("isModuleTargeted addModuleTarget(" + var0.getName() + "," + var1 + ")");
         }

         addModuleTargetsToData(var0.getName(), var1, var2, var3, var4, var5, var6);
         return true;
      } else {
         return isJMSModule(var0) && isSubModulesTargeted(var0.getName(), var0.getSubDeployments(), var1, var2, var3, var4, var5, var6);
      }
   }

   private static boolean isSubModulesTargeted(String var0, SubDeploymentMBean[] var1, String var2, List var3, List var4, DeploymentData var5, DeploymentData var6, boolean var7) {
      if (var1 != null && var1.length != 0) {
         boolean var8 = false;

         for(int var9 = 0; var9 < var1.length; ++var9) {
            SubDeploymentMBean var10 = var1[var9];
            if (isTargetIn(var2, var10.getTargets())) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("isSubModulesTargeted addSubModuleTarget(" + var0 + "," + var10.getName() + "," + var2 + ")");
               }

               addSubModuleTargetsToData(var0, var10.getName(), var2, var3, var4, var5, var6, var7);
               var8 = true;
            }
         }

         return var8;
      } else {
         return false;
      }
   }

   private static void handleExternalTreeChanges(OrderedDeployments var0, Map var1, long var2, List var4) {
      if (var1 != null && var1.size() > 0) {
         Iterator var5 = var1.keySet().iterator();

         while(var5.hasNext()) {
            DescriptorBean var6 = (DescriptorBean)var5.next();
            SystemResourceMBean var7 = (SystemResourceMBean)DescriptorInfoUtils.getDescriptorConfigExtension(var6.getDescriptor());
            if (var4 != null && !var4.contains(var7)) {
               handleGlobalDescriptorChanges(var0, var7, var1, var2);
            }
         }

      }
   }

   private static void handleGlobalDescriptorChanges(OrderedDeployments var0, SystemResourceMBean var1, Map var2, long var3) {
      PendingDirectoryManager var5 = PendingDirectoryManager.getInstance();
      String var6 = var1.getDescriptorFileName();
      if (var5.globalDescriptorExists(var6)) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("handleGlobalDescriptorChanges: bean=" + var1 + ", file=" + var6);
         }

         Object var7 = var2.get(var1.getResource());
         boolean var8 = false;
         if (var7 != null) {
            var8 = hasNonDynamicChanges(var1, (ArrayList)var7, var3);
            if (var8 && Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("bean=" + var1 + " has non dynamic changes");
            }
         }

         DeploymentData var9 = new DeploymentData();
         TargetMBean[] var10 = getTargets(var1);
         if (var10 == null || var10.length == 0) {
            return;
         }

         List var11 = Arrays.asList(var10);
         var9.addSubModuleTarget((String)null, var6, getTargetNames(var11));
         DeployInfo var12 = createNewDeploymentInfo(var1, var9, 9, true);
         var12.requireRestart = var8;
         var0.addDeploymentInfo(var12);
      }

   }

   private static BeanInfo getBeanInfo(DescriptorBean var0) {
      BeanInfoAccess var1 = ManagementService.getBeanInfoAccess();
      BeanInfo var2 = var1.getBeanInfoForInstance(var0, true, (String)null);
      return var2;
   }

   private static boolean hasNonDynamicChanges(SystemResourceMBean var0, ArrayList var1, long var2) {
      if (!var1.isEmpty()) {
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            BeanUpdateEvent var5 = (BeanUpdateEvent)var4.next();
            DescriptorBean var6 = var5.getProposedBean();
            if (var6 == null) {
               return false;
            }

            BeanInfo var7 = getBeanInfo(var6);
            if (var7 == null) {
               return false;
            }

            BeanUpdateEvent.PropertyUpdate[] var8 = var5.getUpdateList();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               if (!var8[var9].isDynamic()) {
                  DeployerRuntimeLogger.logNonDynamicPropertyChange(var2, var0.getName(), var8[var9].getPropertyName(), var5.getSourceBean().toString());
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static Iterator getDescriptorDiff(ConfigurationContext var0) {
      if (var0 == null) {
         return Collections.EMPTY_LIST.iterator();
      } else {
         DescriptorDiff var1 = (DescriptorDiff)var0.getContextComponent("beanUpdateDescriptorDiffId");
         return var1.iterator();
      }
   }

   private static Map getExternalDescritorDiffs(ConfigurationContext var0) {
      return var0 == null ? null : (Map)var0.getContextComponent("externalDescritorDiffId");
   }

   private static boolean isTargetsChanged(DescriptorBean var0, BeanUpdateEvent.PropertyUpdate var1) {
      return (var0 instanceof TargetInfoMBean || var0 instanceof DeploymentMBean && var0 instanceof TargetMBean) && var1.getPropertyName().equals("Targets");
   }

   private static boolean isCandidateServersChanged(DescriptorBean var0, BeanUpdateEvent.PropertyUpdate var1) {
      return var0 instanceof MigratableTargetMBean && var1.getPropertyName().equals("ConstrainedCandidateServers");
   }

   private static DeployInfo createDeploymentInfo(TargetInfoMBean var0, List var1, int var2, boolean var3) {
      DeployInfo var4 = createNewDeploymentInfo(var2, var3);
      populateDeploymentInfo(var4, var0, var1);
      return var4;
   }

   private static DeployInfo createNewDeploymentInfo(int var0, boolean var1) {
      return createNewDeploymentInfo((BasicDeploymentMBean)null, new DeploymentData(), var0, var1);
   }

   private static DeployInfo createNewDeploymentInfo(BasicDeploymentMBean var0, DeploymentData var1, int var2, boolean var3) {
      if (var2 == 1) {
         return new DeployDeployInfo(var0, var1, var3);
      } else {
         return (DeployInfo)(var2 == 9 ? new RedeployDeployInfo(var0, var1, var3) : new UndeployDeployInfo(var0, var1, var3));
      }
   }

   private static void populateDeploymentInfo(DeployInfo var0, TargetInfoMBean var1, List var2) {
      DeploymentData var3 = var0.deployData;
      BasicDeploymentMBean var4;
      if (var1 instanceof BasicDeploymentMBean) {
         var4 = (BasicDeploymentMBean)var1;
         if (var2 != null && !var2.isEmpty()) {
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               TargetMBean var6 = (TargetMBean)var5.next();
               var3.addTarget(var6.getName(), (String[])null);
            }
         }
      } else {
         WebLogicMBean var9 = var1.getParent();
         String var10;
         if (var9 instanceof BasicDeploymentMBean) {
            var4 = (BasicDeploymentMBean)var9;
            if (isEar(var4)) {
               var10 = var1.getName();
               if (var2 != null && !var2.isEmpty()) {
                  Iterator var7 = var2.iterator();

                  while(var7.hasNext()) {
                     TargetMBean var8 = (TargetMBean)var7.next();
                     var3.addModuleTarget(var10, var8.getName());
                  }
               }
            } else {
               var10 = var1.getName();
               var3.addSubModuleTarget((String)null, var10, getTargetNames(var2));
            }
         } else {
            var4 = (BasicDeploymentMBean)var9.getParent();
            var10 = var1.getName();
            String var11 = var9.getName();
            var3.addSubModuleTarget(var11, var10, getTargetNames(var2));
         }
      }

      var0.topLevelDepBean = var4;
   }

   private static String[] getTargetNames(List var0) {
      if (var0 != null && var0.size() != 0) {
         String[] var1 = new String[var0.size()];

         for(int var2 = 0; var2 < var0.size(); ++var2) {
            var1[var2] = ((TargetMBean)var0.get(var2)).getName();
         }

         return var1;
      } else {
         return null;
      }
   }

   private static Deployment findOrCreateDeployment(int var0, BasicDeploymentMBean var1, DeploymentData var2, boolean var3, boolean var4, boolean var5) {
      try {
         String var6 = var1.getSourcePath();
         String var7 = "weblogic.deploy.configChangeTask." + getTaskId();
         DeploymentTaskRuntime var8 = createDeploymentTask(var6, var1, var2, var7, var0, var3, var5);
         AuthenticatedSubject var9 = SecurityServiceManager.getCurrentSubject(kernelId);
         return DeploymentManager.getInstance(kernelId).createDeployment(var7, var2, var0, var8, getDomainBean(var3), true, var9, true, var5, var4);
      } catch (Throwable var10) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug(StackTraceUtils.throwable2StackTrace(var10));
         }

         return null;
      }
   }

   private static synchronized int getTaskId() {
      return curTaskId++;
   }

   private static DeploymentTaskRuntime createDeploymentTask(String var0, BasicDeploymentMBean var1, DeploymentData var2, String var3, int var4, boolean var5, boolean var6) throws ManagementException {
      DomainMBean var7 = getDomainBean(var5);
      DeploymentTaskRuntime var8 = new DeploymentTaskRuntime(var0, var1, var2, var3, var4, var7, var6, true);
      return var8;
   }

   private static TargetMBean[] getTargets(WebLogicMBean var0) {
      if (var0 instanceof TargetInfoMBean) {
         return (TargetMBean[])(var0 instanceof DomainTargetedMBean ? getDomainBean(true).getServers() : ((TargetInfoMBean)var0).getTargets());
      } else if (var0 instanceof DeploymentMBean && var0 instanceof TargetMBean) {
         return ((DeploymentMBean)var0).getTargets();
      } else {
         return var0 instanceof MigratableTargetMBean ? ((MigratableTargetMBean)var0).getAllCandidateServers() : null;
      }
   }

   private static DomainMBean getDomainBean(boolean var0) {
      if (var0) {
         try {
            return ManagementServiceRestricted.getEditAccess(kernelId).getDomainBean();
         } catch (Throwable var2) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Failed to get proposed DomainBean" + StackTraceUtils.throwable2StackTrace(var2));
            }
         }
      }

      return ManagementService.getRuntimeAccess(kernelId).getDomain();
   }

   private static List getTargetsNotIn(TargetMBean[] var0, TargetMBean[] var1) {
      if (var0 != null && var0.length != 0) {
         if (var1 != null && var1.length != 0) {
            HashSet var2 = new HashSet();
            HashSet var3 = new HashSet();

            for(int var4 = 0; var4 < var1.length; ++var4) {
               var2.add(var1[var4].getName());
               var3.addAll(var1[var4].getServerNames());
            }

            ArrayList var11 = new ArrayList();

            for(int var5 = 0; var5 < var0.length; ++var5) {
               TargetMBean var6 = var0[var5];
               if (isPhysicalTarget(var6)) {
                  if (!var3.contains(var6.getName())) {
                     var11.add(var6);
                  }
               } else {
                  Set var7 = var6.getServerNames();
                  Iterator var8 = var7.iterator();

                  while(var8.hasNext()) {
                     String var9 = (String)var8.next();
                     if (!var3.contains(var9)) {
                        TargetMBean var10 = getDomainBean(false).lookupTarget(var9);
                        if (var10 != null) {
                           var11.add(var10);
                        }
                     }
                  }
               }
            }

            return var11;
         } else {
            return Arrays.asList(var0);
         }
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   private static boolean isPhysicalTarget(TargetMBean var0) {
      return var0 instanceof ServerMBean;
   }

   private static boolean isEar(TargetInfoMBean var0) {
      if (!(var0 instanceof AppDeploymentMBean)) {
         return false;
      } else if (ModuleType.EAR.toString().equals(var0.getModuleType())) {
         return true;
      } else {
         String var1 = ((AppDeploymentMBean)var0).getAbsoluteSourcePath();
         if (var1 != null) {
            if (var1.endsWith(".ear")) {
               return true;
            }

            if ((new File(var1, "META-INF/application.xml")).exists()) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean isJMSModule(TargetInfoMBean var0) {
      if (var0 instanceof JMSSystemResourceMBean) {
         return true;
      } else if (!(var0 instanceof AppDeploymentMBean)) {
         if (var0 instanceof SubDeploymentMBean) {
            TargetInfoMBean var3 = (TargetInfoMBean)var0.getParent();
            if (var3 instanceof JMSSystemResourceMBean) {
               return true;
            }

            if (var3 instanceof AppDeploymentMBean) {
               if (isJMSModule(var3)) {
                  return true;
               }

               return isJMSModule(var3, var0);
            }

            if (var3 instanceof SubDeploymentMBean) {
               TargetInfoMBean var2 = (TargetInfoMBean)var3.getParent();
               return isJMSModule(var2, var3);
            }
         }

         return false;
      } else {
         String var1 = ((AppDeploymentMBean)var0).getAbsoluteSourcePath();
         return var1 != null && var1.endsWith("-jms.xml");
      }
   }

   private static boolean isJMSModule(TargetInfoMBean var0, TargetInfoMBean var1) {
      String var2 = getAppRuntimeState().getModuleType(var0.getName(), var1.getName());
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Module type for " + var1 + ": " + var2);
      }

      if (var2.equals(WebLogicModuleType.MODULETYPE_UNKNOWN) && var0 instanceof AppDeploymentMBean) {
         VirtualJarFile var3 = null;

         try {
            ApplicationFileManager var4 = ApplicationFileManager.newInstance(((AppDeploymentMBean)var0).getAbsoluteSourcePath());
            var3 = var4.getVirtualJarFile();
            Iterator var5 = var3.entries();

            while(var5.hasNext()) {
               JarEntry var6 = (JarEntry)var5.next();
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Entry: " + var6.getName());
               }

               if (var6.getName().endsWith("-jms.xml")) {
                  boolean var7 = true;
                  return var7;
               }
            }
         } catch (Throwable var18) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Failed to get module type", var18);
            }
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var17) {
               }
            }

         }
      }

      return false;
   }

   private static AppRuntimeStateRuntimeMBean getAppRuntimeState() {
      if (appRTMBean == null) {
         appRTMBean = ManagementService.getDomainAccess(kernelId).getAppRuntimeStateRuntime();
      }

      return appRTMBean;
   }

   private static JMSServerMBean getJMSServer(MigratableTargetMBean var0) {
      DomainMBean var1 = getDomainBean(true);
      JMSServerMBean[] var2 = var1.getJMSServers();
      if (var2 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            TargetMBean[] var4 = var2[var3].getTargets();
            if (var4.length == 1 && var4[0].equals(var0)) {
               return var2[var3];
            }
         }

         return null;
      }
   }

   private static void createAndAddDeploymentInfos(OrderedDeployments var0, BasicDeploymentMBean var1, List var2, List var3, DeploymentData var4, DeploymentData var5, int var6) {
      DeployInfo var7;
      if (var2.size() > 0) {
         var7 = createNewDeploymentInfo(var1, var4, var6, false);
         var7.requireRestart = false;
         var0.addDeploymentInfo(var7);
      }

      if (var3.size() > 0) {
         var7 = createNewDeploymentInfo(var1, var5, var6, true);
         var7.requireRestart = false;
         var0.addDeploymentInfo(var7);
      }

   }

   private static void addTargetsToData(String var0, List var1, List var2, DeploymentData var3, DeploymentData var4, boolean var5) {
      if (var5) {
         Iterator var6 = var1.iterator();

         TargetMBean var7;
         while(var6.hasNext()) {
            var7 = (TargetMBean)var6.next();
            var3.addTarget(var7.getName(), (String[])null);
         }

         var6 = var2.iterator();

         while(var6.hasNext()) {
            var7 = (TargetMBean)var6.next();
            var4.addTarget(var7.getName(), (String[])null);
         }
      } else {
         if (var1.size() > 0) {
            var3.addTarget(var0, (String[])null);
         }

         if (var2.size() > 0) {
            var4.addTarget(var0, (String[])null);
         }
      }

   }

   private static void addModuleTargetsToData(String var0, String var1, List var2, List var3, DeploymentData var4, DeploymentData var5, boolean var6) {
      if (var6) {
         Iterator var7 = var2.iterator();

         TargetMBean var8;
         while(var7.hasNext()) {
            var8 = (TargetMBean)var7.next();
            var4.addModuleTarget(var0, var8.getName());
         }

         var7 = var3.iterator();

         while(var7.hasNext()) {
            var8 = (TargetMBean)var7.next();
            var5.addModuleTarget(var0, var8.getName());
         }
      } else {
         if (var2.size() > 0) {
            var4.addModuleTarget(var0, var1);
         }

         if (var3.size() > 0) {
            var5.addModuleTarget(var0, var1);
         }
      }

   }

   private static void addSubModuleTargetsToData(String var0, String var1, String var2, List var3, List var4, DeploymentData var5, DeploymentData var6, boolean var7) {
      String[] var8 = var7 ? getTargetNames(var3) : new String[]{var2};
      String[] var9 = var7 ? getTargetNames(var4) : new String[]{var2};
      if (var3.size() > 0) {
         var5.addSubModuleTarget(var0, var1, var8);
      }

      if (var4.size() > 0) {
         var6.addSubModuleTarget(var0, var1, var9);
      }

   }

   private static boolean isTargetIn(String var0, TargetMBean[] var1) {
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].getName().equals(var0)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static String dumpDiff(ConfigurationContext var0) {
      Iterator var1 = getDescriptorDiff(var0);
      StringBuffer var2 = new StringBuffer("{");

      while(var1.hasNext()) {
         BeanUpdateEvent var3 = (BeanUpdateEvent)var1.next();
         BeanUpdateEvent.PropertyUpdate[] var4 = var3.getUpdateList();
         DescriptorBean var5 = var3.getSourceBean();
         var2.append("\n=> Event for ").append(var5).append("[updateID=").append(var3.getUpdateID()).append("]={");

         for(int var6 = 0; var6 < var4.length; ++var6) {
            BeanUpdateEvent.PropertyUpdate var7 = var4[var6];
            var2.append("\n[").append(var7).append("]");
            if (var6 < var4.length - 1) {
               var2.append(",");
            }
         }

         var2.append("} ");
      }

      var2.append("}");
      return var2.toString();
   }

   private static String dumpTargets(DeploymentData var0) {
      if (var0 == null) {
         return "{}";
      } else {
         StringBuffer var1 = new StringBuffer("{");
         String[] var2 = var0.getGlobalTargets();
         if (var2 != null && var2.length > 0) {
            var1.append("GlobalTargets=");

            for(int var3 = 0; var3 < var2.length; ++var3) {
               var1.append(var2[var3]);
               if (var3 < var2.length - 1) {
                  var1.append(",");
               }
            }
         }

         Map var12 = var0.getAllModuleTargets();
         if (var12 != null && var12.size() > 0) {
            Iterator var4 = var12.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               var1.append("\nModuleTargets(").append(var5.getKey()).append(")=");
               var2 = (String[])((String[])var5.getValue());

               for(int var6 = 0; var6 < var2.length; ++var6) {
                  var1.append(var2[var6]);
                  if (var6 < var2.length - 1) {
                     var1.append(",");
                  }
               }
            }
         }

         Map var13 = var0.getAllSubModuleTargets();
         if (var13 != null && var13.size() > 0) {
            Iterator var14 = var13.entrySet().iterator();

            while(var14.hasNext()) {
               Map.Entry var15 = (Map.Entry)var14.next();
               String var7 = (String)var15.getKey();
               Map var8 = (Map)var15.getValue();
               Iterator var9 = var8.entrySet().iterator();

               while(var9.hasNext()) {
                  Map.Entry var10 = (Map.Entry)var9.next();
                  var1.append("\nSubModuleTargets(").append(var7).append(",").append(var10.getKey()).append(")=");
                  var2 = (String[])((String[])var10.getValue());

                  for(int var11 = 0; var11 < var2.length; ++var11) {
                     var1.append(var2[var11]);
                     if (var11 < var2.length - 1) {
                        var1.append(",");
                     }
                  }
               }
            }
         }

         var1.append("}");
         return var1.toString();
      }
   }

   private static String getTargetsString(TargetMBean[] var0) {
      StringBuffer var1 = new StringBuffer("(");
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(var0[var2].getName());
            if (var2 < var0.length - 1) {
               var1.append(",");
            }
         }
      }

      var1.append(")");
      return var1.toString();
   }

   private static String getTargetsString(List var0) {
      StringBuffer var1 = new StringBuffer("(");
      if (var0 != null && !var0.isEmpty()) {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            TargetMBean var3 = (TargetMBean)var0.get(var2);
            if (var3 != null) {
               var1.append(var3.getName());
            }

            if (var2 < var0.size() - 1) {
               var1.append(",");
            }
         }
      }

      var1.append(")");
      return var1.toString();
   }

   private static String getTaskString(int var0) {
      switch (var0) {
         case 1:
            return "Activate";
         case 4:
            return "Remove";
         case 9:
            return "Redeploy";
         default:
            return "Unknown";
      }
   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   private static void debugSay(String var0) {
      Debug.deploymentDebug(var0);
   }

   private static boolean areTargetsSame(DeploymentData var0, DeploymentData var1) {
      List var2 = Arrays.asList(var0.getGlobalTargets());
      List var3 = Arrays.asList(var1.getGlobalTargets());
      boolean var4 = var2.equals(var3);
      if (isDebugEnabled()) {
         debugSay(" Global Targets are same : " + var4);
      }

      if (!var4) {
         return false;
      } else {
         var4 = var0.getAllModuleTargets().equals(var1.getAllModuleTargets());
         if (isDebugEnabled()) {
            debugSay(" Module Targets are same : " + var4);
         }

         if (!var4) {
            return false;
         } else {
            var4 = var0.getAllSubModuleTargets().equals(var1.getAllSubModuleTargets());
            if (isDebugEnabled()) {
               debugSay(" Submodule Targets are same : " + var4);
               debugSay(" Targets are same : " + var4);
            }

            return var4;
         }
      }
   }

   private static boolean haveCommonTargets(DeploymentData var0, DeploymentData var1) {
      if (haveCommonGlobalTargets(var0, var1)) {
         return true;
      } else if (haveCommonModuleTargets(var0, var1)) {
         return true;
      } else {
         return haveCommonSubModuleTargets(var0, var1);
      }
   }

   private static boolean haveCommonGlobalTargets(DeploymentData var0, DeploymentData var1) {
      String[] var2 = var0.getGlobalTargets();
      if (var2 != null && var2.length != 0) {
         ExtendedArrayList var3 = new ExtendedArrayList(var2);
         String[] var4 = var1.getGlobalTargets();
         if (var4 != null && var4.length != 0) {
            if (var3.containsOne(var4)) {
               return true;
            } else {
               HashSet var5 = new HashSet();
               var5.addAll(Arrays.asList(var2));
               HashSet var6 = new HashSet();
               var6.addAll(Arrays.asList(var4));

               Set var7;
               Set var8;
               try {
                  var7 = var0.getAllTargetedServers(var5);
                  var8 = var1.getAllTargetedServers(var6);
               } catch (InvalidTargetException var11) {
                  return false;
               }

               if (!var7.isEmpty() && !var8.isEmpty()) {
                  Iterator var9 = var7.iterator();

                  Object var10;
                  do {
                     if (!var9.hasNext()) {
                        return false;
                     }

                     var10 = var9.next();
                  } while(!var8.contains(var10));

                  var0.setGlobalTargets((String[])((String[])var7.toArray(new String[0])));
                  var1.setGlobalTargets((String[])((String[])var8.toArray(new String[0])));
                  return true;
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean haveCommonModuleTargets(DeploymentData var0, DeploymentData var1) {
      Map var2 = var0.getAllModuleTargets();
      Map var3 = var1.getAllModuleTargets();
      if (!var3.isEmpty() && !var2.isEmpty()) {
         Set var4 = var3.keySet();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var2.containsKey(var6)) {
               String[] var7 = (String[])((String[])var3.get(var6));
               ExtendedArrayList var8 = new ExtendedArrayList((String[])((String[])var2.get(var6)));
               if (var8.containsOne(var7)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean haveCommonSubModuleTargets(DeploymentData var0, DeploymentData var1) {
      Map var2 = var0.getAllSubModuleTargets();
      Map var3 = var1.getAllSubModuleTargets();
      if (!var3.isEmpty() && !var2.isEmpty()) {
         Set var4 = var3.keySet();
         Iterator var5 = var4.iterator();

         while(true) {
            Map var7;
            Map var8;
            do {
               do {
                  String var6;
                  do {
                     do {
                        do {
                           if (!var5.hasNext()) {
                              return false;
                           }

                           var6 = (String)var5.next();
                        } while(!var2.containsKey(var6));

                        var7 = (Map)var3.get(var6);
                     } while(var7 == null);
                  } while(var7.isEmpty());

                  var8 = (Map)var2.get(var6);
               } while(var8 == null);
            } while(var8.isEmpty());

            Iterator var9 = var7.keySet().iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (var8.containsKey(var10)) {
                  String[] var11 = (String[])((String[])var7.get(var10));
                  ExtendedArrayList var12 = new ExtendedArrayList((String[])((String[])var8.get(var10)));
                  if (var12.containsOne(var11)) {
                     return true;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   private static boolean isTargetedToCluster(List var0, ClusterMBean var1) {
      if (var0.isEmpty()) {
         return false;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            TargetMBean var4 = (TargetMBean)var3.next();
            var2.add(var4.getName());
         }

         if (var2.contains(var1.getName())) {
            return true;
         } else {
            ServerMBean[] var5 = var1.getServers();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var2.contains(var5[var6].getName())) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static class ExtendedArrayList extends ArrayList {
      ExtendedArrayList(Collection var1) {
         super(var1);
      }

      ExtendedArrayList(String[] var1) {
         this((Collection)(var1 != null ? Arrays.asList(var1) : Collections.EMPTY_LIST));
      }

      boolean containsOne(String[] var1) {
         if (this.isEmpty()) {
            return false;
         } else if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (this.contains(var1[var2])) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }

   private static final class OrderedDeployments {
      private static final Comparator LOCAL_COMPARATOR = new Comparator() {
         public int compare(Object var1, Object var2) {
            if (var1 == var2) {
               return 0;
            } else {
               int var3 = ConfigChangesHandler.OrderedDeployments.compareDeployment((DeployInfo)var1, (DeployInfo)var2);
               return var3 != 0 ? var3 : -1;
            }
         }
      };
      private final TreeSet preDeploymentHandlerDeployments;
      private final TreeSet postDeploymentHandlerDeployments;
      private final List tempInfoList;

      private OrderedDeployments() {
         this.preDeploymentHandlerDeployments = new TreeSet(LOCAL_COMPARATOR);
         this.postDeploymentHandlerDeployments = new TreeSet(LOCAL_COMPARATOR);
         this.tempInfoList = new ArrayList() {
            public boolean add(Object var1) {
               if (this.contains(var1)) {
                  return false;
               } else {
                  DeployInfo var2 = (DeployInfo)var1;
                  List var3 = this.getDeployInfosOnSameApp(var2);
                  if (ConfigChangesHandler.isDebugEnabled()) {
                     ConfigChangesHandler.debugSay(" MergedList.addDeployInfo() : sameInfos of kind '" + var2 + "' =========== " + var3);
                  }

                  if (var3.size() < 1) {
                     return super.add(var2);
                  } else {
                     Iterator var4 = var3.iterator();

                     boolean var6;
                     do {
                        if (!var4.hasNext()) {
                           return super.add(var2);
                        }

                        DeployInfo var5 = (DeployInfo)var4.next();
                        var6 = var2.mergeWithOtherOperation(var5, this);
                     } while(!var6);

                     return true;
                  }
               }
            }

            private List getDeployInfosOnSameApp(DeployInfo var1) {
               ArrayList var2 = new ArrayList();
               BasicDeploymentMBean var3 = var1.getTopLevelDepBean();
               if (var3 == null) {
                  return var2;
               } else if (this.isEmpty()) {
                  return var2;
               } else {
                  Iterator var4 = this.iterator();

                  while(var4.hasNext()) {
                     DeployInfo var5 = (DeployInfo)var4.next();
                     BasicDeploymentMBean var6 = var5.getTopLevelDepBean();
                     if (var6 != null && var6 == var3 && var1 != var5) {
                        var2.add(var5);
                     }
                  }

                  return var2;
               }
            }
         };
      }

      private boolean addDeploymentInfo(DeployInfo var1) {
         return this.tempInfoList.add(var1);
      }

      private void add(DeployInfo var1) {
         boolean var2 = DeploymentOrder.isBeforeDeploymentHandler(var1.getTopLevelDepBean());
         boolean var3 = var1.isDeploy();
         if (var2 && var3 || !var2 && !var3) {
            this.preDeploymentHandlerDeployments.add(var1);
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" ++ preDeploymentHandlerDeployments after add : " + this.preDeploymentHandlerDeployments);
            }

            weblogic.deploy.internal.Deployment var4 = (weblogic.deploy.internal.Deployment)var1.getDeployment();
            var4.setBeforeDeploymentHandler();
            if (var3) {
               var4.setIsDeploy();
            }
         } else {
            this.postDeploymentHandlerDeployments.add(var1);
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" ++ postDeploymentHandlerDeployments after add : " + this.postDeploymentHandlerDeployments);
            }
         }

      }

      private static int compareDeployment(DeployInfo var0, DeployInfo var1) {
         boolean var2 = var0.isDeploy();
         boolean var3 = var1.isDeploy();
         if (var2 && var3) {
            return compare(var0, var1);
         } else if (!var2 && !var3) {
            return compare(var0, var1) * -1;
         } else {
            return !var2 && var3 ? -1 : 1;
         }
      }

      private static int compare(DeployInfo var0, DeployInfo var1) {
         return DeploymentOrder.COMPARATOR.compare(var0.getTopLevelDepBean(), var1.getTopLevelDepBean());
      }

      private void processDeploymentInfos() {
         try {
            Iterator var1 = this.tempInfoList.iterator();

            while(var1.hasNext()) {
               DeployInfo var2 = (DeployInfo)var1.next();
               Deployment var3 = var2.createDeployment();
               if (var3 != null) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("ConfigChangesHandler.addDeployment for " + var2.getTopLevelDepBean().getName() + ", deployOp=" + ConfigChangesHandler.getTaskString(var2.getOp()) + ", isDeploy=" + var2.isDeploy() + ", deployData=" + var2.getDeployData() + ", targets=" + ConfigChangesHandler.dumpTargets(var2.getDeployData()) + ", deployment=" + var2.getDeployment());
                  }

                  this.add(var2);
               }
            }
         } finally {
            this.tempInfoList.clear();
         }

      }

      private void addDeployments(Collection var1, List var2) {
         if (var1 != null && var1.size() != 0) {
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               DeployInfo var4 = (DeployInfo)var3.next();
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("  MBean=" + var4.getTopLevelDepBean() + ", op=" + ConfigChangesHandler.getTaskString(var4.getOp()));
               }

               if (var4.getDeployment() != null) {
                  var2.add(var4.getDeployment());
               }
            }

         }
      }

      private List[] getAllDeployments(DeploymentRequest var1, boolean var2, List<AppDeploymentMBean> var3) {
         boolean var4 = Debug.isDeploymentDebugEnabled();
         this.processDeploymentInfos();
         ArrayList var5 = new ArrayList();
         ArrayList var6 = new ArrayList();
         if (var4) {
            Debug.deploymentDebug("ConfigChangesHandler PreDepHandlerDeployments:" + this.preDeploymentHandlerDeployments);
         }

         this.addDeployments(this.preDeploymentHandlerDeployments, var5);
         if (var2 && var5.size() > 0) {
            var1.setCallConfigurationProviderLast();
         }

         if (var4) {
            Debug.deploymentDebug("ConfigChangesHandler PostDepHandlerDeployments:" + this.postDeploymentHandlerDeployments);
         }

         this.addDeployments(this.postDeploymentHandlerDeployments, var6);
         Collection var7 = DeploymentManager.getInstance(ConfigChangesHandler.kernelId).getPendingDeploymentsForEditLockOwner();
         if (var7 != null && var7.size() > 0) {
            if (var4) {
               Debug.deploymentDebug("ConfigChangesHandler PostDepHandlerDeployments with no configuration side-effects:");
            }

            weblogic.deploy.internal.Deployment var9;
            label64:
            for(Iterator var8 = var7.iterator(); var8.hasNext(); var6.add(var9)) {
               var9 = (weblogic.deploy.internal.Deployment)var8.next();
               if (var4) {
                  Debug.deploymentDebug(var9.getDeploymentTaskRuntimeId());
               }

               Iterator var10 = var3.iterator();

               while(true) {
                  AppDeploymentMBean var11;
                  do {
                     if (!var10.hasNext()) {
                        continue label64;
                     }

                     var11 = (AppDeploymentMBean)var10.next();
                  } while(!var11.getName().equals(var9.getIdentity()));

                  TargetMBean[] var12 = var11.getTargets();
                  ArrayList var13 = new ArrayList();

                  for(int var14 = 0; var14 < var12.length; ++var14) {
                     Set var15 = var12[var14].getServerNames();
                     Iterator var16 = var15.iterator();

                     while(var16.hasNext()) {
                        String var17 = (String)var16.next();
                        var13.add(var17);
                     }
                  }

                  var9.setTargets(var13);
               }
            }
         }

         return new List[]{var5, var6};
      }

      // $FF: synthetic method
      OrderedDeployments(Object var1) {
         this();
      }
   }

   private abstract static class DeployInfo {
      protected BasicDeploymentMBean topLevelDepBean;
      protected final DeploymentData deployData;
      protected int op;
      protected boolean isDeploy;
      private Deployment deployment;
      protected boolean requireRestart;

      private DeployInfo() {
         this.isDeploy = true;
         this.topLevelDepBean = null;
         this.deployData = null;
         this.deployment = null;
      }

      private DeployInfo(BasicDeploymentMBean var1, DeploymentData var2, int var3, boolean var4) {
         this.isDeploy = true;
         this.topLevelDepBean = var1;
         this.deployData = var2 != null ? var2 : new DeploymentData();
         this.op = var3;
         this.isDeploy = var4;
      }

      public boolean isDeploy() {
         return this.isDeploy;
      }

      public DeploymentData getDeployData() {
         return this.deployData;
      }

      public void setRequireRestart(boolean var1) {
         this.requireRestart = var1;
      }

      public int getOp() {
         return this.op;
      }

      public void setOp(int var1) {
         this.op = var1;
      }

      public Deployment getDeployment() {
         return this.deployment;
      }

      public BasicDeploymentMBean getTopLevelDepBean() {
         return this.topLevelDepBean;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append("[");
         var1.append("topLevelBean=").append(this.topLevelDepBean).append(",deployData=").append(this.deployData).append(",op=").append(this.op).append(",isDeploy=").append(this.isDeploy).append(",hasTargetedServers=").append(this.hasTargetedServers(ConfigChangesHandler.getDomainBean(true))).append("]");
         return var1.toString();
      }

      private boolean hasModuleTargets() {
         return this.deployData != null && this.deployData.hasModuleTargets();
      }

      private boolean hasSubModuleTargets() {
         return this.deployData != null && this.deployData.hasSubModuleTargets();
      }

      private boolean hasTargetedServers(DomainMBean var1) {
         if (this.deployData == null) {
            return false;
         } else {
            boolean var2 = false;

            try {
               Set var3 = this.deployData.getAllTargetedServers(this.deployData.getAllLogicalTargets(), var1);
               var2 = var3 != null && var3.size() > 0;
            } catch (Throwable var4) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("getAllTargetedServers failed", var4);
               }
            }

            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("ConfigChangesHandler.hasTargets=" + var2);
            }

            return var2;
         }
      }

      protected boolean mergeWithRedeploy(DeployInfo var1, Collection var2) {
         if (!(var1 instanceof RedeployDeployInfo)) {
            throw new AssertionError("Other DeployInfo is not Redeploy info");
         } else {
            return false;
         }
      }

      protected boolean mergeWithUndeploy(DeployInfo var1, Collection var2) {
         if (!(var1 instanceof UndeployDeployInfo)) {
            throw new AssertionError("Other DeployInfo is not Undeploy info");
         } else {
            return false;
         }
      }

      protected boolean mergeWithDeploy(DeployInfo var1, Collection var2) {
         if (!(var1 instanceof DeployDeployInfo)) {
            throw new AssertionError("Other DeployInfo is not Deploy info");
         } else {
            return false;
         }
      }

      protected boolean mergeWithSameInfo(DeployInfo var1, Collection var2) {
         if (!this.isSameInfoType(var1)) {
            throw new AssertionError(" DeployInfo instances are different types");
         } else {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ DeployInfos are of same type. Merging their data and making one info...");
            }

            DeploymentData var3 = this.deployData;
            DeploymentData var4 = var1.deployData;
            DeploymentData var5 = var3.copy();
            var5.setDeploymentPlan(var4.getDeploymentPlan());
            var5.addGlobalTargets(var4.getGlobalTargets());
            if (var4.hasModuleTargets()) {
               var5.addOrUpdateModuleTargets(var4.getAllModuleTargets());
            }

            if (var4.hasSubModuleTargets()) {
               var5.addOrUpdateSubModuleTargets(var4.getAllSubModuleTargets());
            }

            var2.remove(var1);
            var2.remove(this);
            DeployInfo var6 = ConfigChangesHandler.createNewDeploymentInfo(this.topLevelDepBean, var5, this.op, this.isDeploy);
            var6.requireRestart = this.requireRestart;
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Merged into Info : " + var6);
            }

            var2.add(var6);
            return true;
         }
      }

      protected boolean mergeWithOtherOperation(DeployInfo var1, Collection var2) {
         if (ConfigChangesHandler.isDebugEnabled()) {
            ConfigChangesHandler.debugSay("DeployInfo.mergeWithOtherOperation() invoked on '" + this + "' ------ with parameter '" + var1);
         }

         if (var1 instanceof ClusterDeployInfo) {
            return false;
         } else if (var1 instanceof DeployDeployInfo) {
            return this.mergeWithDeploy(var1, var2);
         } else {
            return var1 instanceof RedeployDeployInfo ? this.mergeWithRedeploy(var1, var2) : this.mergeWithUndeploy(var1, var2);
         }
      }

      protected Deployment createDeployment() {
         if (this.deployData != null && this.topLevelDepBean != null && this.hasTargetedServers(ConfigChangesHandler.getDomainBean(this.isDeploy))) {
            this.deployment = ConfigChangesHandler.findOrCreateDeployment(this.op, this.topLevelDepBean, this.deployData, this.isDeploy, this.requireRestart, false);
         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("ConfigChangesHandler.addDeployment skipped, deployInfo=" + this);
            }

            this.deployment = null;
         }

         return this.deployment;
      }

      private boolean isSameInfoType(DeployInfo var1) {
         return this.getClass().equals(var1.getClass());
      }

      // $FF: synthetic method
      DeployInfo(Object var1) {
         this();
      }

      // $FF: synthetic method
      DeployInfo(BasicDeploymentMBean var1, DeploymentData var2, int var3, boolean var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   private static final class UndeployDeployInfo extends DeployInfo {
      private UndeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3) {
         super(var1, var2, 4, var3, null);
      }

      protected boolean mergeWithRedeploy(DeployInfo var1, Collection var2) {
         super.mergeWithRedeploy(var1, var2);
         return var1.mergeWithUndeploy(this, var2);
      }

      protected boolean mergeWithUndeploy(DeployInfo var1, Collection var2) {
         super.mergeWithUndeploy(var1, var2);
         return this.mergeWithSameInfo(var1, var2);
      }

      protected boolean mergeWithDeploy(DeployInfo var1, Collection var2) {
         super.mergeWithDeploy(var1, var2);
         return var1.mergeWithUndeploy(this, var2);
      }

      // $FF: synthetic method
      UndeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class RedeployDeployInfo extends DeployInfo {
      private RedeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3) {
         super(var1, var2, 9, var3, null);
      }

      protected boolean mergeWithRedeploy(DeployInfo var1, Collection var2) {
         super.mergeWithRedeploy(var1, var2);
         return this.mergeWithSameInfo(var1, var2);
      }

      protected boolean mergeWithUndeploy(DeployInfo var1, Collection var2) {
         super.mergeWithUndeploy(var1, var2);
         if (ConfigChangesHandler.areTargetsSame(this.deployData, var1.deployData)) {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Targets are same...");
               ConfigChangesHandler.debugSay(" +++ Removing from the list : " + var1);
            }

            var2.remove(var1);
            var2.remove(this);
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Adding to the list : " + this);
            }

            var2.add(this);
            return true;
         } else {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Targets are *NOT* same...");
            }

            if (ConfigChangesHandler.haveCommonTargets(this.deployData, var1.deployData)) {
               DeploymentData var3 = this.deployData;
               DeploymentData var4 = var1.deployData;
               var3.removeCommonTargets(var4, false);
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Have common targets...");
                  ConfigChangesHandler.debugSay(" +++ Removing info : " + var1);
                  ConfigChangesHandler.debugSay(" +++ Removing info : " + this);
               }

               var2.remove(var1);
               var2.remove(this);
               DeployInfo var5;
               if (!var4.hasNoTargets()) {
                  if (ConfigChangesHandler.isDebugEnabled()) {
                     ConfigChangesHandler.debugSay(" +++ Other data HAS targets after removing common targets...");
                  }

                  var5 = ConfigChangesHandler.createNewDeploymentInfo(var1.topLevelDepBean, var4, 4, var1.isDeploy);
                  var5.requireRestart = var1.requireRestart;
                  if (ConfigChangesHandler.isDebugEnabled()) {
                     ConfigChangesHandler.debugSay(" +++ Adding new UndeployInfo : " + var5);
                  }

                  var2.add(var5);
               }

               var5 = ConfigChangesHandler.createNewDeploymentInfo(this.topLevelDepBean, var3, 9, this.isDeploy);
               var5.requireRestart = this.requireRestart;
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Adding new RedeployInfo : " + var5);
               }

               var2.add(var5);
               return true;
            } else {
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Have *NO* common targets... So returning false");
               }

               return false;
            }
         }
      }

      protected boolean mergeWithDeploy(DeployInfo var1, Collection var2) {
         super.mergeWithDeploy(var1, var2);
         if (ConfigChangesHandler.areTargetsSame(this.deployData, var1.deployData)) {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Targets are same... Need not have to add this");
            }

            return true;
         } else {
            DeploymentData var3 = this.deployData;
            DeploymentData var4 = var1.deployData;
            var3.setDeploymentPlan(var4.getDeploymentPlan());
            var3.addGlobalTargets(var4.getGlobalTargets());
            if (var4.hasModuleTargets()) {
               var3.addOrUpdateModuleTargets(var4.getAllModuleTargets());
            }

            if (var4.hasSubModuleTargets()) {
               var3.addOrUpdateSubModuleTargets(var4.getAllSubModuleTargets());
            }

            var2.remove(var1);
            var2.remove(this);
            DeployInfo var5 = ConfigChangesHandler.createNewDeploymentInfo(this.topLevelDepBean, var3, this.op, this.isDeploy);
            var5.requireRestart = this.requireRestart;
            var2.add(var5);
            return true;
         }
      }

      // $FF: synthetic method
      RedeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class DeployDeployInfo extends DeployInfo {
      private DeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3) {
         super(var1, var2, 1, var3, null);
      }

      protected boolean mergeWithRedeploy(DeployInfo var1, Collection var2) {
         super.mergeWithRedeploy(var1, var2);
         return var1.mergeWithDeploy(this, var2);
      }

      protected boolean mergeWithUndeploy(DeployInfo var1, Collection var2) {
         super.mergeWithUndeploy(var1, var2);
         if (ConfigChangesHandler.areTargetsSame(this.deployData, var1.deployData)) {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Targets are same...");
               ConfigChangesHandler.debugSay(" +++ Removing from the list : " + var1);
            }

            var2.remove(var1);
            var2.remove(this);
            DeployInfo var6 = ConfigChangesHandler.createNewDeploymentInfo(this.topLevelDepBean, this.deployData, 9, this.isDeploy);
            var6.requireRestart = this.requireRestart;
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Adding to the list : " + var6);
            }

            var2.add(var6);
            return true;
         } else {
            if (ConfigChangesHandler.isDebugEnabled()) {
               ConfigChangesHandler.debugSay(" +++ Targets are *NOT* same...");
            }

            if (ConfigChangesHandler.haveCommonTargets(this.deployData, var1.deployData)) {
               DeploymentData var3 = this.deployData;
               DeploymentData var4 = var1.deployData;
               var3.removeCommonTargets(var4, false);
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Have common targets...");
                  ConfigChangesHandler.debugSay(" +++ Removing info : " + var1);
                  ConfigChangesHandler.debugSay(" +++ Removing info : " + this);
               }

               var2.remove(var1);
               var2.remove(this);
               DeployInfo var5;
               if (!var4.hasNoTargets()) {
                  if (ConfigChangesHandler.isDebugEnabled()) {
                     ConfigChangesHandler.debugSay(" +++ Other data HAS targets after removing common targets...");
                  }

                  var5 = ConfigChangesHandler.createNewDeploymentInfo(var1.topLevelDepBean, var4, 4, var1.isDeploy);
                  var5.requireRestart = var1.requireRestart;
                  if (ConfigChangesHandler.isDebugEnabled()) {
                     ConfigChangesHandler.debugSay(" +++ Adding new UndeployInfo : " + var5);
                  }

                  var2.add(var5);
               }

               var5 = ConfigChangesHandler.createNewDeploymentInfo(this.topLevelDepBean, var3, 9, this.isDeploy);
               var5.requireRestart = this.requireRestart;
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Adding new RedeployInfo : " + var5);
               }

               var2.add(var5);
               return true;
            } else {
               if (ConfigChangesHandler.isDebugEnabled()) {
                  ConfigChangesHandler.debugSay(" +++ Have *NO* common targets... So returning false");
               }

               return false;
            }
         }
      }

      protected boolean mergeWithDeploy(DeployInfo var1, Collection var2) {
         super.mergeWithDeploy(var1, var2);
         return this.mergeWithSameInfo(var1, var2);
      }

      // $FF: synthetic method
      DeployDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class ClusterDeployInfo extends DeployInfo {
      private DeployInfo delegate;
      String associatedClusterName;

      private ClusterDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, int var3, boolean var4, String var5) {
         super(null);
         if (var3 == 1) {
            this.delegate = new DeployDeployInfo(var1, var2, var4);
         } else if (var3 == 9) {
            this.delegate = new RedeployDeployInfo(var1, var2, var4);
         } else {
            this.delegate = new UndeployDeployInfo(var1, var2, var4);
         }

         this.associatedClusterName = var5;
      }

      public boolean isDeploy() {
         return this.delegate.isDeploy();
      }

      public DeploymentData getDeployData() {
         return this.delegate.getDeployData();
      }

      public int getOp() {
         return this.delegate.getOp();
      }

      public void setRequireRestart(boolean var1) {
         this.delegate.setRequireRestart(var1);
      }

      public void setOp(int var1) {
         this.delegate.setOp(var1);
      }

      public final String toString() {
         return "ClusterDeplyInfo for cluster [" + this.associatedClusterName + "] with delegate " + this.delegate;
      }

      protected final boolean mergeWithOtherOperation(DeployInfo var1, Collection var2) {
         if (ConfigChangesHandler.isDebugEnabled()) {
            ConfigChangesHandler.debugSay("ClusterDeployInfo.mergeWithOtherOperation() invoked on '" + this + "' ------ with otherInfo '" + var1);
         }

         if (!(var1 instanceof ClusterDeployInfo)) {
            return false;
         } else {
            ClusterDeployInfo var3 = (ClusterDeployInfo)var1;
            if (!this.associatedClusterName.equals(var3.associatedClusterName)) {
               return false;
            } else {
               DeployInfo var4 = var3.delegate;
               if (this.delegate instanceof DeployDeployInfo) {
                  if (var4 instanceof DeployDeployInfo) {
                     var3.mergeTargets(this.delegate);
                     var2.remove(this);
                     return true;
                  } else if (var4 instanceof UndeployDeployInfo) {
                     var2.remove(var3);
                     return false;
                  } else {
                     var3.mergeTargets(this.delegate);
                     var2.remove(this);
                     return true;
                  }
               } else if (this.delegate instanceof RedeployDeployInfo) {
                  if (var4 instanceof DeployDeployInfo) {
                     this.mergeTargets(var4);
                     var2.remove(var3);
                     return false;
                  } else if (var4 instanceof UndeployDeployInfo) {
                     var2.remove(var3);
                     return false;
                  } else {
                     var3.mergeTargets(this.delegate);
                     var2.remove(this);
                     return true;
                  }
               } else if (var4 instanceof DeployDeployInfo) {
                  var2.remove(this);
                  return true;
               } else if (var4 instanceof UndeployDeployInfo) {
                  var3.mergeTargets(this.delegate);
                  var2.remove(this);
                  return true;
               } else {
                  var2.remove(this);
                  return true;
               }
            }
         }
      }

      void mergeTargets(DeployInfo var1) {
         DeploymentData var2 = this.delegate.deployData;
         DeploymentData var3 = var1.deployData;
         var2.setDeploymentPlan(var3.getDeploymentPlan());
         var2.addGlobalTargets(var3.getGlobalTargets());
         if (var3.hasModuleTargets()) {
            var2.addOrUpdateModuleTargets(var3.getAllModuleTargets());
         }

         if (var3.hasSubModuleTargets()) {
            var2.addOrUpdateSubModuleTargets(var3.getAllSubModuleTargets());
         }

         this.delegate.requireRestart = this.delegate.requireRestart || var1.requireRestart;
         if (ConfigChangesHandler.isDebugEnabled()) {
            ConfigChangesHandler.debugSay(" +++ Merged into Info : " + this);
         }

      }

      protected Deployment createDeployment() {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("ConfigChangesHandler.ClusterDeployInfo.createDeployment --> " + this);
         }

         return this.delegate.createDeployment();
      }

      public Deployment getDeployment() {
         return this.delegate.getDeployment();
      }

      public BasicDeploymentMBean getTopLevelDepBean() {
         return this.delegate.getTopLevelDepBean();
      }

      // $FF: synthetic method
      ClusterDeployInfo(BasicDeploymentMBean var1, DeploymentData var2, int var3, boolean var4, String var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
