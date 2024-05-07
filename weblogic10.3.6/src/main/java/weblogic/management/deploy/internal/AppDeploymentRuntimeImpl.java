package weblogic.management.deploy.internal;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.enterprise.deploy.shared.ModuleType;
import javax.management.MBeanException;
import javax.management.Notification;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.ModuleInfo;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AppDeploymentRuntimeMBean;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.DeploymentProgressObjectMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class AppDeploymentRuntimeImpl extends DomainRuntimeMBeanDelegate implements AppDeploymentRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static Map<String, NotificationGenerator> ngs = new HashMap();
   private final String appName;
   private final String appVersion;
   private final AppDeploymentMBean deployable;
   private NotificationGenerator notificationGenerator;
   private long notificationSequence = 0L;

   AppDeploymentRuntimeImpl(AppDeploymentMBean var1) throws ManagementException {
      super(var1.getName());
      this.appName = var1.getApplicationName();
      this.appVersion = var1.getVersionIdentifier();
      this.deployable = var1;
   }

   public String getApplicationName() {
      return this.appName;
   }

   public String getApplicationVersion() {
      return this.appVersion;
   }

   public DeploymentProgressObjectMBean start() throws RuntimeException {
      return this.doOperation(AppDeploymentRuntimeImpl.OperationType.START, true, (String[])null, (Properties)null);
   }

   public DeploymentProgressObjectMBean start(String[] var1, Properties var2) throws RuntimeException {
      return this.doOperation(AppDeploymentRuntimeImpl.OperationType.START, false, var1, var2);
   }

   public DeploymentProgressObjectMBean stop() throws RuntimeException {
      return this.doOperation(AppDeploymentRuntimeImpl.OperationType.STOP, true, (String[])null, (Properties)null);
   }

   public DeploymentProgressObjectMBean stop(String[] var1, Properties var2) throws RuntimeException {
      return this.doOperation(AppDeploymentRuntimeImpl.OperationType.STOP, false, var1, var2);
   }

   public String[] getModules() {
      ArrayList var1 = new ArrayList();
      AppDeploymentMBean var2 = ApplicationVersionUtils.getActiveAppDeployment(this.appName);
      if (var2 != null) {
         String var3 = var2.getSourcePath();
         File var4 = new File(var3);
         File var5 = new File(var4.getParent());
         String var6 = var2.getPlanPath();
         File var7 = null;
         if (var6 != null) {
            var7 = new File(var6);
         }

         try {
            WebLogicDeploymentManager var8 = SessionHelper.getDisconnectedDeploymentManager();
            SessionHelper var9 = SessionHelper.getInstance(var8);
            var9.setFullInit(false);
            var9.setApplication(var4);
            var9.setApplicationRoot(var5);
            if (var7 != null) {
               var9.setPlan(var7);
            }

            var9.initializeConfiguration();
            ModuleInfo var10 = var9.getModuleInfo();
            ModuleType var11 = var10.getType();
            int var14;
            int var15;
            if (var11 == ModuleType.EAR) {
               ModuleInfo[] var12 = var10.getSubModules();
               if (var12 != null) {
                  ModuleInfo[] var13 = var12;
                  var14 = var12.length;

                  for(var15 = 0; var15 < var14; ++var15) {
                     ModuleInfo var16 = var13[var15];
                     String var17 = var16.getName();
                     var11 = var16.getType();
                     if (var11 == ModuleType.WAR) {
                        String[] var18 = var16.getContextRoots();
                        if (var18 != null) {
                           String[] var19 = var18;
                           int var20 = var18.length;

                           for(int var21 = 0; var21 < var20; ++var21) {
                              String var22 = var19[var21];
                              if (var22 != null && var22.length() > 0) {
                                 var1.add(var22);
                              }
                           }
                        }
                     } else {
                        var1.add(var17);
                     }
                  }
               }
            } else if (var11 == ModuleType.WAR) {
               String[] var24 = var10.getContextRoots();
               if (var24 != null) {
                  String[] var25 = var24;
                  var14 = var24.length;

                  for(var15 = 0; var15 < var14; ++var15) {
                     String var26 = var25[var15];
                     if (var26 != null && var26.length() > 0) {
                        var1.add(var26);
                     }
                  }
               }
            } else {
               var1.add(var10.getName());
            }
         } catch (Exception var23) {
            throw new RuntimeException(var23);
         }
      }

      return (String[])var1.toArray(new String[0]);
   }

   public String getState(String var1) {
      AppRuntimeStateRuntimeMBean var2 = ManagementService.getDomainAccess(kernelId).getAppRuntimeStateRuntime();
      return var2.getCurrentState(this.getName(), var1);
   }

   void setNotificationGenerator(NotificationGenerator var1) {
      this.notificationGenerator = var1;
      ngs.put(this.appName, var1);
   }

   void sendNotification(DeploymentState var1) {
      if (this.notificationGenerator != null) {
         ++this.notificationSequence;
         Notification var2 = new Notification(DeploymentManagerImpl.translateState(var1.getCurrentState()), this.notificationGenerator.getObjectName(), this.notificationSequence);
         String var3 = var1.getTarget();
         var2.setUserData(var3);

         try {
            this.notificationGenerator.sendNotification(var2);
         } catch (MBeanException var5) {
            var5.printStackTrace();
         }
      }

   }

   private DeploymentData propertiesToDeploymentData(String[] var1, Properties var2) throws ManagementException {
      DeploymentOptions var3 = new DeploymentOptions();
      DeploymentData var4 = new DeploymentData();
      ArrayList var5 = new ArrayList();
      if (var2 != null) {
         String var6 = null;
         var6 = var2.getProperty("clusterDeploymentTimeout");
         int var7;
         if (var6 != null) {
            try {
               var7 = new Integer(var6);
               var3.setClusterDeploymentTimeout(var7);
            } catch (NumberFormatException var11) {
               throw new ManagementException(var11);
            }
         }

         var6 = var2.getProperty("gracefulIgnoreSessions");
         if (var6 != null) {
            var3.setGracefulIgnoreSessions(Boolean.parseBoolean(var6));
         }

         var6 = var2.getProperty("gracefulProductionToAdmin");
         if (var6 != null) {
            var3.setGracefulProductionToAdmin(Boolean.parseBoolean(var6));
         }

         var6 = var2.getProperty("retireGracefully");
         if (var6 != null) {
            var3.setRetireGracefully(Boolean.parseBoolean(var6));
         }

         var6 = var2.getProperty("retireTimeout");
         if (var6 != null) {
            try {
               var7 = new Integer(var6);
               var3.setRetireTime(var7);
            } catch (NumberFormatException var10) {
               throw new ManagementException(var10);
            }
         }

         var6 = var2.getProperty("adminMode");
         if (var6 != null) {
            var3.setAdminMode(Boolean.parseBoolean(var6));
         }

         var6 = var2.getProperty("timeout");
         if (var6 != null) {
            try {
               long var12 = (long)(new Long(var6)).intValue();
               var3.setTimeout(var12);
            } catch (NumberFormatException var9) {
               throw new ManagementException(var9);
            }
         }

         if (this.appVersion != null) {
            var3.setArchiveVersion(this.appVersion);
         }

         var4.setDeploymentOptions(var3);
      }

      if (var1 != null) {
         for(int var14 = 0; var14 < var1.length; ++var14) {
            if (var1[var14] != null) {
               if (var1[var14].contains("@")) {
                  String[] var13 = var1[var14].split("@");
                  if (var13 == null || var13.length != 2) {
                     throw new ManagementException("invalid target:  " + var1[var14]);
                  }

                  var4.addModuleTarget(var13[0].trim(), var13[1].trim());
               } else {
                  var5.add(var1[var14]);
               }
            }
         }
      }

      var4.addGlobalTargets((String[])var5.toArray(new String[0]));
      return var4;
   }

   private DeploymentProgressObjectMBean doOperation(OperationType var1, boolean var2, String[] var3, Properties var4) throws RuntimeException {
      DeploymentProgressObjectMBean var5 = null;
      DeploymentTaskRuntimeMBean var6 = null;

      try {
         DeploymentManagerMBean var7 = ManagementService.getDomainAccess(kernelId).getDeploymentManager();
         DeployerRuntimeMBean var11 = ManagementService.getDomainAccess(kernelId).getDeployerRuntime();
         DeploymentData var9 = null;
         if (var3 == null && var4 == null) {
            var9 = new DeploymentData();
         } else {
            var9 = this.propertiesToDeploymentData(var3, var4);
         }

         if (var1.equals(AppDeploymentRuntimeImpl.OperationType.START)) {
            var6 = var11.start(this.name, var9, (String)null, false);
         } else {
            var6 = var11.stop(this.name, var9, (String)null, false);
         }

         var5 = ((DeploymentManagerImpl)var7).allocateDeploymentProgressObject(this.getName(), var6, this.deployable);
         var6.start();
         if (var2) {
            var6.waitForTaskCompletion(-1L);
         }
      } catch (Throwable var10) {
         RuntimeException var8 = ExceptionTranslator.translateException(var10);
         if (var5 == null || var6 == null) {
            throw var8;
         }

         ((DeploymentProgressObjectImpl)var5).addException(var8);
      }

      return var5;
   }

   private static enum OperationType {
      START,
      STOP;
   }
}
