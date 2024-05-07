package weblogic.work;

import java.security.AccessController;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.wl.ApplicationAdminModeTriggerBean;
import weblogic.j2ee.descriptor.wl.CapacityBean;
import weblogic.j2ee.descriptor.wl.ContextCaseBean;
import weblogic.j2ee.descriptor.wl.ContextRequestClassBean;
import weblogic.j2ee.descriptor.wl.FairShareRequestClassBean;
import weblogic.j2ee.descriptor.wl.MaxThreadsConstraintBean;
import weblogic.j2ee.descriptor.wl.MinThreadsConstraintBean;
import weblogic.j2ee.descriptor.wl.ResponseTimeRequestClassBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.descriptor.wl.WorkManagerBean;
import weblogic.j2ee.descriptor.wl.WorkManagerShutdownTriggerBean;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SelfTuningMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.WorkManagerMBean;
import weblogic.management.configuration.WorkManagerShutdownTriggerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class WorkManagerCollection extends AbstractCollection implements BeanUpdateListener {
   public static final int SHUTDOWN = 0;
   public static final int STARTED = 1;
   private static final DebugCategory debugWMCollection = Debug.getCategory("weblogic.workmanagercollection");
   static final String DEFAULT_WM_NAME = "default";
   private static final String MODULE_DELIMITER = "@";
   private HashMap workManagers;
   private HashMap requestClassMap;
   private HashMap maxMap;
   private HashMap minMap;
   private HashMap overloadMap;
   private Map<String, WorkManagerRuntimeMBean> runtimeMBeans;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String applicationName;
   private WeblogicApplicationBean weblogicApplicationBean;
   private J2EEApplicationRuntimeMBeanImpl applicationRuntimeMBean;
   private boolean initialized;
   private static final boolean use81ExecuteQueues;
   private boolean internal;
   private ApplicationAdminModeAction adminModeAction;
   private int state;
   private static String thisCluster;
   private static String thisServer;

   public WorkManagerCollection(String var1) {
      this(var1, false);
   }

   public WorkManagerCollection(String var1, boolean var2) {
      this.workManagers = new HashMap();
      this.requestClassMap = new HashMap();
      this.maxMap = new HashMap();
      this.minMap = new HashMap();
      this.overloadMap = new HashMap();
      this.runtimeMBeans = new HashMap();
      this.state = 0;
      this.applicationName = var1;
      this.internal = var2;
      this.debug("creating a new collection for app: " + var1 + ", internal: " + var2);
   }

   public synchronized void initialize(WeblogicApplicationBean var1) throws DeploymentException {
      if (!this.initialized) {
         this.populateNonWorkManagerComponents(var1);
         SelfTuningMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSelfTuning();
         StuckThreadManager var3 = null;
         WorkManagerService var4 = null;
         if (var2 == null) {
            var3 = this.getStuckThreadManager();
            var4 = WorkManagerServiceImpl.createService("default", this.applicationName, (String)null, var3);
            if (this.internal) {
               var4.setInternal();
            }

            this.workManagers.put("default", var4);
            this.addWorkManagerRuntime(var4, this.applicationRuntimeMBean);
            this.debug("created default WorkManager for " + this.applicationName);
            this.initialized = true;
         } else {
            if (thisServer == null || thisCluster == null) {
               RuntimeAccess var5 = ManagementService.getRuntimeAccess(kernelId);
               thisServer = var5.getServerName();
               ClusterRuntimeMBean var6 = var5.getServerRuntime().getClusterRuntime();
               if (var6 != null) {
                  thisCluster = var6.getName();
               }
            }

            boolean var10 = false;
            WorkManagerMBean[] var11 = var2.getWorkManagers();
            if (var11 != null) {
               for(int var7 = 0; var7 < var11.length; ++var7) {
                  WorkManagerMBean var8 = var11[var7];
                  WorkManagerService var9 = this.configureWorkManagerService(var8);
                  if (var9 != null && var9.getName().equals("default")) {
                     var10 = true;
                  }
               }
            }

            var2.addBeanUpdateListener(this);
            if (!var10) {
               var3 = this.getStuckThreadManager();
               var4 = WorkManagerServiceImpl.createService("default", this.applicationName, (String)null, var3);
               if (this.internal) {
                  var4.setInternal();
               }

               this.workManagers.put("default", var4);
               this.addWorkManagerRuntime(var4, this.applicationRuntimeMBean);
               this.debug("created default WorkManager for " + this.applicationName);
            }

            this.populateWorkManagers(var1);
            this.initialized = true;
         }
      }
   }

   private WorkManagerService configureWorkManagerService(WorkManagerMBean var1) throws DeploymentException {
      WorkManagerService var2 = null;
      if ("default".equals(var1.getName())) {
         var2 = this.addWorkManager(var1);
      } else {
         TargetMBean[] var3 = var1.getTargets();
         TargetMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            TargetMBean var7 = var4[var6];
            String var8 = var7.getName();
            if (var8.equals(thisServer) || var8.equals(thisCluster)) {
               var2 = this.addWorkManager(var1);
               break;
            }
         }
      }

      return var2;
   }

   public void setApplicationRuntime(J2EEApplicationRuntimeMBeanImpl var1) {
      this.applicationRuntimeMBean = var1;
   }

   public synchronized Iterator iterator() {
      return this.workManagers.values().iterator();
   }

   public int size() {
      return this.workManagers.size();
   }

   private synchronized void populateNonWorkManagerComponents(WeblogicApplicationBean var1) throws DeploymentException {
      if (var1 != null) {
         this.weblogicApplicationBean = var1;
         this.populate(var1.getFairShareRequests());
         this.populate(var1.getResponseTimeRequests());
         this.populate(var1.getContextRequests());
         this.populate(var1.getMaxThreadsConstraints());
         this.populate(var1.getMinThreadsConstraints());
         this.populate(var1.getCapacities());
         this.populate(var1.getApplicationAdminModeTrigger());
      }
   }

   private synchronized void populateWorkManagers(WeblogicApplicationBean var1) throws DeploymentException {
      if (var1 != null) {
         this.weblogicApplicationBean = var1;
         this.populate((String)null, (WorkManagerBean[])var1.getWorkManagers());
      }
   }

   public synchronized void populate(String var1, WeblogicEjbJarBean var2) throws DeploymentException {
      this.populate(var1, var2.getWorkManagers());
   }

   public synchronized void populate(String var1, WeblogicWebAppBean var2) throws DeploymentException {
      this.populate(var1, var2.getWorkManagers());
   }

   private void populate(FairShareRequestClassBean[] var1) throws DeploymentException {
      if (var1 != null && !use81ExecuteQueues) {
         try {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               FairShareRequestClassBean var3 = var1[var2];
               FairShareRequestClass var4 = new FairShareRequestClass(var3.getName(), var3.getFairShare());
               ((DescriptorBean)var3).addBeanUpdateListener(new FairShareRequestClassBeanUpdateListener(var4));
               var4.setShared(true);
               this.requestClassMap.put(var3.getName(), var4);
               this.applicationRuntimeMBean.addRequestClass(new RequestClassRuntimeMBeanImpl(var4, this.applicationRuntimeMBean));
            }

         } catch (ManagementException var5) {
            throw new DeploymentException(var5);
         }
      }
   }

   private void populate(ResponseTimeRequestClassBean[] var1) throws DeploymentException {
      if (var1 != null && !use81ExecuteQueues) {
         try {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               ResponseTimeRequestClassBean var3 = var1[var2];
               ResponseTimeRequestClass var4 = new ResponseTimeRequestClass(var3.getName(), var3.getGoalMs());
               ((DescriptorBean)var3).addBeanUpdateListener(new ResponseTimeRequestClassBeanUpdateListener(var4));
               var4.setShared(true);
               this.requestClassMap.put(var3.getName(), var4);
               this.applicationRuntimeMBean.addRequestClass(new RequestClassRuntimeMBeanImpl(var4, this.applicationRuntimeMBean));
            }

         } catch (ManagementException var5) {
            throw new DeploymentException(var5);
         }
      }
   }

   private void populate(ContextRequestClassBean[] var1) throws DeploymentException {
      if (var1 != null && !use81ExecuteQueues) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            ContextRequestClassBean var3 = var1[var2];
            ContextCaseBean[] var4 = var3.getContextCases();
            ContextRequestClass var5 = new ContextRequestClass(var3.getName());
            if (var4 != null) {
               for(int var6 = 0; var6 < var4.length; ++var6) {
                  ContextCaseBean var7 = var4[var2];
                  RequestClass var8 = this.getRequestClass(var7.getRequestClassName());
                  if (var8 == null) {
                     throw new DeploymentException("request class " + var7.getRequestClassName() + " not found");
                  }

                  if (var7.getUserName() != null) {
                     var5.addUser(var7.getUserName(), var8);
                  } else if (var7.getGroupName() != null) {
                     var5.addGroup(var7.getGroupName(), var8);
                  }
               }
            }

            var5.setShared(true);
            this.requestClassMap.put(var3.getName(), var5);
         }

      }
   }

   private void populate(MaxThreadsConstraintBean[] var1) throws DeploymentException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            MaxThreadsConstraintBean var3 = var1[var2];

            try {
               Object var4;
               if (var3.getPoolName() != null) {
                  var4 = new PoolBasedMaxThreadsConstraint(var3.getName(), var3.getPoolName(), this.weblogicApplicationBean);
               } else {
                  var4 = new MaxThreadsConstraint(var3.getName(), var3.getCount());
                  ((DescriptorBean)var3).addBeanUpdateListener(new MaxThreadsConstraintBeanUpdateListener((MaxThreadsConstraint)var4));
               }

               this.maxMap.put(var3.getName(), var4);
               if (!use81ExecuteQueues) {
                  this.applicationRuntimeMBean.addMaxThreadsConstraint(new MaxThreadsConstraintRuntimeMBeanImpl((MaxThreadsConstraint)var4, this.applicationRuntimeMBean));
               }
            } catch (ManagementException var5) {
               throw new DeploymentException("unable to create runtime mbean for max constraint " + var3.getName(), var5);
            }
         }

      }
   }

   private void populate(MinThreadsConstraintBean[] var1) throws DeploymentException {
      try {
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               MinThreadsConstraintBean var3 = var1[var2];
               MinThreadsConstraint var4 = new MinThreadsConstraint(var3.getName(), var3.getCount());
               this.minMap.put(var3.getName(), var4);
               if (!use81ExecuteQueues) {
                  ((DescriptorBean)var3).addBeanUpdateListener(new MinThreadsConstraintBeanUpdateListener(var4));
                  this.applicationRuntimeMBean.addMinThreadsConstraint(new MinThreadsConstraintRuntimeMBeanImpl(var4, this.applicationRuntimeMBean));
               }
            }

         }
      } catch (ManagementException var5) {
         throw new DeploymentException("unable to create runtime mbean for min constraint", var5);
      }
   }

   private void populate(CapacityBean[] var1) {
      if (var1 != null && !use81ExecuteQueues) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            CapacityBean var3 = var1[var2];
            OverloadManager var4 = new OverloadManager(var3.getName(), var3.getCount());
            ((DescriptorBean)var3).addBeanUpdateListener(new OverloadManagerBeanUpdateListener(var4));
            this.overloadMap.put(var3.getName(), var4);
         }

      }
   }

   private void populate(ApplicationAdminModeTriggerBean var1) {
      if (var1 != null && !use81ExecuteQueues) {
         this.adminModeAction = new ApplicationAdminModeAction(var1, this.applicationName);
      }
   }

   private void populate(String var1, WorkManagerBean[] var2) throws DeploymentException {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            WorkManagerBean var4 = var2[var3];
            this.populate(var1, var4);
         }

      }
   }

   public synchronized WorkManagerService populate(String var1, WorkManagerBean var2) throws DeploymentException {
      RequestClass var3 = this.getPolicy(var2);
      MinThreadsConstraint var4 = this.getMinConstraint(var2);
      MaxThreadsConstraint var5 = this.getMaxConstraint(var2);
      OverloadManager var6 = this.getOverload(var2);
      WorkManagerShutdownAction var7 = this.getShutdownAction(var2);
      StuckThreadManager var8 = this.getStuckThreadManager(var2, var7);
      WorkManagerLogger.logCreatingWorkManagerService(var1, this.applicationName, var2.getName());
      WorkManagerService var9 = WorkManagerServiceImpl.createService(var2.getName(), this.applicationName, var1, var3, var5, var4, var6, var8);
      if (var7 != null) {
         var7.setWorkManagerService(var9);
      }

      if (this.internal) {
         var9.setInternal();
      }

      if (var1 != null) {
         this.workManagers.put(var1 + "@" + var2.getName(), var9);
      } else {
         this.workManagers.put(var2.getName(), var9);
      }

      if (!use81ExecuteQueues && var1 == null) {
         this.addWorkManagerRuntime(var9, this.applicationRuntimeMBean);
         return var9;
      } else {
         return var9;
      }
   }

   private WorkManagerShutdownAction getShutdownAction(WorkManagerMBean var1) {
      if (use81ExecuteQueues) {
         return null;
      } else if (var1 == null) {
         return null;
      } else if (var1.getIgnoreStuckThreads()) {
         return null;
      } else {
         WorkManagerShutdownTriggerMBean var2 = var1.getWorkManagerShutdownTrigger();
         if (var2 != null && var2.getStuckThreadCount() > 0) {
            this.debug("Found WorkManagerShutdownTriggerMBean with " + var2.getMaxStuckThreadTime() + " seconds and " + var2.getStuckThreadCount() + " count");
            return new WorkManagerShutdownAction(var2);
         } else {
            return null;
         }
      }
   }

   private StuckThreadManager getStuckThreadManager(WorkManagerMBean var1, WorkManagerShutdownAction var2) {
      if (use81ExecuteQueues) {
         return null;
      } else if (var1 != null && var1.getIgnoreStuckThreads()) {
         return new StuckThreadManager();
      } else if (var2 == null && this.adminModeAction == null && GlobalWorkManagerComponentsFactory.getInstance().getServerFailedAction() == null) {
         this.debug("NO global ServerFailureAction found. No stuck thread action !");
         return null;
      } else {
         this.debug("Global ServerFailureAction FOUND. Creating StuckThreadManager !");
         return new StuckThreadManager(var2, this.adminModeAction, GlobalWorkManagerComponentsFactory.getInstance().getServerFailedAction());
      }
   }

   private StuckThreadManager getStuckThreadManager() {
      return this.getStuckThreadManager((WorkManagerBean)((WorkManagerBean)null), (WorkManagerShutdownAction)null);
   }

   private WorkManagerShutdownAction getShutdownAction(WorkManagerBean var1) {
      if (use81ExecuteQueues) {
         return null;
      } else if (var1 == null) {
         return null;
      } else if (var1.getIgnoreStuckThreads()) {
         return null;
      } else {
         WorkManagerShutdownTriggerBean var2 = var1.getWorkManagerShutdownTrigger();
         if (var2 != null && var2.getStuckThreadCount() > 0) {
            this.debug("Found WorkManagerShutdownTriggerMBean with " + var2.getMaxStuckThreadTime() + " seconds and " + var2.getStuckThreadCount() + " count");
            return new WorkManagerShutdownAction(var2);
         } else {
            return null;
         }
      }
   }

   private StuckThreadManager getStuckThreadManager(WorkManagerBean var1, WorkManagerShutdownAction var2) {
      if (use81ExecuteQueues) {
         return null;
      } else if (var1 != null && var1.getIgnoreStuckThreads()) {
         return new StuckThreadManager();
      } else if (var2 == null && this.adminModeAction == null && GlobalWorkManagerComponentsFactory.getInstance().getServerFailedAction() == null) {
         this.debug("NO global ServerFailureAction found. No stuck thread action !");
         return null;
      } else {
         this.debug("Global ServerFailureAction FOUND. Creating StuckThreadManager !");
         return new StuckThreadManager(var2, this.adminModeAction, GlobalWorkManagerComponentsFactory.getInstance().getServerFailedAction());
      }
   }

   private OverloadManager getOverload(WorkManagerBean var1) {
      if (use81ExecuteQueues) {
         return null;
      } else {
         CapacityBean var2 = var1.getCapacity();
         if (var2 != null) {
            OverloadManager var4 = new OverloadManager(var2.getName(), var2.getCount());
            ((DescriptorBean)var2).addBeanUpdateListener(new OverloadManagerBeanUpdateListener(var4));
            return var4;
         } else {
            String var3 = var1.getCapacityName();
            return var3 != null ? this.getOverload(var3) : null;
         }
      }
   }

   private MaxThreadsConstraint getMaxConstraint(WorkManagerBean var1) throws DeploymentException {
      MaxThreadsConstraintBean var2 = var1.getMaxThreadsConstraint();
      String var3;
      if (var2 != null) {
         var3 = var2.getPoolName();
         if (var3 != null) {
            return new PoolBasedMaxThreadsConstraint(var2.getName(), var3, this.weblogicApplicationBean);
         } else {
            MaxThreadsConstraint var4 = new MaxThreadsConstraint(var2.getName(), var2.getCount());
            ((DescriptorBean)var2).addBeanUpdateListener(new MaxThreadsConstraintBeanUpdateListener(var4));
            return var4;
         }
      } else {
         var3 = var1.getMaxThreadsConstraintName();
         return var3 != null ? this.getMaxConstraint(var3) : null;
      }
   }

   private MinThreadsConstraint getMinConstraint(WorkManagerBean var1) {
      MinThreadsConstraintBean var2 = var1.getMinThreadsConstraint();
      if (var2 != null) {
         MinThreadsConstraint var4 = new MinThreadsConstraint(var2.getName(), var2.getCount());
         ((DescriptorBean)var2).addBeanUpdateListener(new MinThreadsConstraintBeanUpdateListener(var4));
         return var4;
      } else {
         String var3 = var1.getMinThreadsConstraintName();
         return var3 != null ? this.getMinConstraint(var3) : null;
      }
   }

   private RequestClass getPolicy(WorkManagerBean var1) {
      if (use81ExecuteQueues) {
         return null;
      } else {
         FairShareRequestClassBean var2 = var1.getFairShareRequestClass();
         if (var2 != null) {
            FairShareRequestClass var10 = new FairShareRequestClass(var2.getName(), var2.getFairShare());
            ((DescriptorBean)var2).addBeanUpdateListener(new FairShareRequestClassBeanUpdateListener(var10));
            return var10;
         } else {
            ResponseTimeRequestClassBean var3 = var1.getResponseTimeRequestClass();
            if (var3 != null) {
               ResponseTimeRequestClass var11 = new ResponseTimeRequestClass(var3.getName(), var3.getGoalMs());
               ((DescriptorBean)var3).addBeanUpdateListener(new ResponseTimeRequestClassBeanUpdateListener(var11));
               return var11;
            } else {
               ContextRequestClassBean var4 = var1.getContextRequestClass();
               if (var4 != null) {
                  ContextCaseBean[] var5 = var4.getContextCases();
                  if (var5 != null) {
                     ContextRequestClass var6 = new ContextRequestClass(var4.getName());

                     for(int var7 = 0; var7 < var5.length; ++var7) {
                        ContextCaseBean var8 = var5[var7];
                        RequestClass var9;
                        if (var8.getUserName() != null) {
                           var9 = this.getRequestClass(var8.getRequestClassName());
                           if (var9 != null) {
                              var6.addUser(var8.getUserName(), var9);
                           } else {
                              WorkManagerLogger.logMissingContextCaseRequestClass("user-name", var8.getUserName());
                           }
                        } else if (var8.getGroupName() != null) {
                           var9 = this.getRequestClass(var8.getRequestClassName());
                           if (var9 != null) {
                              var6.addGroup(var8.getGroupName(), var9);
                           } else {
                              WorkManagerLogger.logMissingContextCaseRequestClass("group-name", var8.getUserName());
                           }
                        }
                     }

                     return var6;
                  }
               }

               String var12 = var1.getRequestClassName();
               return var12 != null ? this.getRequestClass(var12) : null;
            }
         }
      }
   }

   private RequestClass getRequestClass(String var1) {
      RequestClass var2 = (RequestClass)this.requestClassMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         return use81ExecuteQueues ? null : GlobalWorkManagerComponentsFactory.getInstance().findRequestClass(var1);
      }
   }

   private MaxThreadsConstraint getMaxConstraint(String var1) {
      MaxThreadsConstraint var2 = (MaxThreadsConstraint)this.maxMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         return use81ExecuteQueues ? null : GlobalWorkManagerComponentsFactory.getInstance().findMaxThreadsConstraint(var1);
      }
   }

   private MinThreadsConstraint getMinConstraint(String var1) {
      MinThreadsConstraint var2 = (MinThreadsConstraint)this.minMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         return use81ExecuteQueues ? null : GlobalWorkManagerComponentsFactory.getInstance().findMinThreadsConstraint(var1);
      }
   }

   private OverloadManager getOverload(String var1) {
      OverloadManager var2 = (OverloadManager)this.overloadMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         return use81ExecuteQueues ? null : GlobalWorkManagerComponentsFactory.getInstance().findOverloadManager(var1);
      }
   }

   public synchronized WorkManager get(String var1, String var2) {
      return this.get(var1, var2, true);
   }

   synchronized WorkManager get(String var1, String var2, boolean var3) {
      WorkManager var4 = null;
      if (var1 != null) {
         String var5 = var1 + "@" + var2;
         var4 = (WorkManager)this.workManagers.get(var5);
         if (var4 != null) {
            this.debug("found WorkManager for module '" + var1 + "' app '" + this.applicationName + "' wmName '" + var2);
            return var4;
         }
      }

      var4 = (WorkManager)this.workManagers.get(var2);
      if (var4 != null) {
         this.debug("found WorkManager for app '" + this.applicationName + "' wmName '" + var2);
         return var4;
      } else {
         this.debug("No WorkManager for wmName '" + var2 + "' in app '" + this.applicationName + "', module '" + var1 + "'. returning default");
         if (var3) {
            this.logWorkManagerNotFound(var2);
         }

         return this.getDefault();
      }
   }

   private void logWorkManagerNotFound(String var1) {
      if (var1 != null && var1.toLowerCase(Locale.ENGLISH).indexOf("default") == -1) {
         WorkManagerLogger.logWorkManagerNotFound(var1, this.applicationName);
      }

   }

   public synchronized void removeModuleEntries(String var1) {
      Iterator var2 = this.workManagers.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         if (((String)var3.getKey()).startsWith(var1 + "@")) {
            ((WorkManagerService)var3.getValue()).cleanup();
            var2.remove();
         }
      }

   }

   public synchronized List getWorkManagers(String var1) {
      if (var1 == null) {
         return Collections.EMPTY_LIST;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.workManagers.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var4.startsWith(var1 + "@")) {
               var2.add(this.workManagers.get(var4));
            }
         }

         return var2;
      }
   }

   public WorkManager getDefault() {
      return (WorkManager)this.workManagers.get("default");
   }

   private void addWorkManagerRuntime(WorkManagerService var1, RuntimeMBean var2) throws DeploymentException {
      WorkManager var3 = var1.getDelegate();

      try {
         if (!use81ExecuteQueues) {
            WorkManagerRuntimeMBean var4 = WorkManagerRuntimeMBeanImpl.getWorkManagerRuntime(var3, this.applicationRuntimeMBean, var2);
            this.runtimeMBeans.put(var3.getName(), var4);
            this.applicationRuntimeMBean.addWorkManager(var4);
         }
      } catch (ManagementException var5) {
         throw new DeploymentException("unable to create WorkManagerRuntimeMBean for " + var3.getName(), var5);
      }
   }

   private void debug(String var1) {
      if (debugWMCollection.isEnabled()) {
         WorkManagerLogger.logDebug("<WMCollection>" + var1);
      }

   }

   private synchronized WorkManagerService addWorkManager(WorkManagerMBean var1) throws DeploymentException {
      WorkManagerLogger.logCreatingServiceFromMBean(this.applicationName, var1.getName());
      WorkManagerShutdownAction var2 = this.getShutdownAction(var1);
      StuckThreadManager var3 = this.getStuckThreadManager(var1, var2);
      WorkManagerService var4 = WorkManagerServiceImpl.createService(this.applicationName, (String)null, (WorkManagerMBean)var1, var3);
      if (var2 != null) {
         var2.setWorkManagerService(var4);
      }

      if (this.internal) {
         var4.setInternal();
      }

      this.workManagers.put(var1.getName(), var4);
      this.addWorkManagerRuntime(var4, this.applicationRuntimeMBean);
      return var4;
   }

   private synchronized void removeWorkManager(WorkManagerMBean var1) {
      WorkManagerRuntimeMBeanImpl var2 = (WorkManagerRuntimeMBeanImpl)this.runtimeMBeans.remove(var1.getName());
      if (var2 != null) {
         try {
            if (debugWMCollection.isEnabled()) {
               this.debug("Removing work manager instance for " + var1.getName() + " from " + this.applicationName + " runtime");
            }

            this.workManagers.remove(var1.getName());
            var2.unregister();
         } catch (ManagementException var4) {
            if (debugWMCollection.isEnabled()) {
               this.debug("Unable to unregister RuntimeMBean for WorkManager " + var1.getName());
            }
         }
      }

   }

   public void prepareUpdate(BeanUpdateEvent var1) {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      if (!this.internal) {
         if (var1.getSourceBean() instanceof SelfTuningMBean) {
            BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
            WorkManagerMBean var3 = null;
            if (var2 == null) {
               return;
            }

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (var2[var4].getAddedObject() instanceof WorkManagerMBean) {
                  var3 = (WorkManagerMBean)var2[var4].getAddedObject();

                  try {
                     WorkManagerService var5 = this.configureWorkManagerService(var3);
                     if (var5 != null) {
                        this.startWorkManagerIfRequired(var5);
                     }
                  } catch (DeploymentException var6) {
                     if (debugWMCollection.isEnabled()) {
                        this.debug("Unable to add WorkManagerMBean '" + var3.getName() + "' to application '" + this.applicationName + "'");
                     }
                  }
               } else if (var2[var4].getRemovedObject() instanceof WorkManagerMBean) {
                  var3 = (WorkManagerMBean)var2[var4].getRemovedObject();
                  this.removeWorkManager(var3);
               }
            }
         }

      }
   }

   private synchronized void startWorkManagerIfRequired(WorkManagerService var1) {
      if (this.state == 1) {
         var1.start();
      }

   }

   public synchronized int getState() {
      return this.state;
   }

   public synchronized void setState(int var1) {
      this.state = var1;
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   public synchronized void close() {
      SelfTuningMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSelfTuning();
      if (var1 != null) {
         var1.removeBeanUpdateListener(this);
      }

      Iterator var2 = this.workManagers.values().iterator();

      while(var2.hasNext()) {
         WorkManagerService var3 = (WorkManagerService)var2.next();
         var3.cleanup();
      }

      var2 = this.requestClassMap.values().iterator();

      while(var2.hasNext()) {
         RequestClass var4 = (RequestClass)var2.next();
         var4.cleanup();
      }

   }

   static {
      use81ExecuteQueues = ManagementService.getRuntimeAccess(kernelId).getServer().getUse81StyleExecuteQueues();
   }
}
