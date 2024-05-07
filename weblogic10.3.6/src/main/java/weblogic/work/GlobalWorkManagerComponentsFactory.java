package weblogic.work;

import java.security.AccessController;
import java.util.HashMap;
import weblogic.management.ManagementException;
import weblogic.management.configuration.CapacityMBean;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ContextCaseMBean;
import weblogic.management.configuration.ContextRequestClassMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.FairShareRequestClassMBean;
import weblogic.management.configuration.MaxThreadsConstraintMBean;
import weblogic.management.configuration.MinThreadsConstraintMBean;
import weblogic.management.configuration.ResponseTimeRequestClassMBean;
import weblogic.management.configuration.SelfTuningMBean;
import weblogic.management.configuration.ServerFailureTriggerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.WorkManagerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

final class GlobalWorkManagerComponentsFactory {
   private static final DebugCategory debugWM = Debug.getCategory("weblogic.globalworkmanagercomponents");
   private boolean initialized;
   private SelfTuningMBean stmb;
   private ServerRuntimeMBean serverRuntime;
   private final HashMap maxMap;
   private final HashMap minMap;
   private final HashMap overloadMap;
   private final HashMap requestClassMap;
   private final HashMap workManagerTemplateMap;
   private ServerFailureAction serverFailureAction;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static GlobalWorkManagerComponentsFactory getInstance() {
      return GlobalWorkManagerComponentsFactory.Factory.THE_ONE;
   }

   private GlobalWorkManagerComponentsFactory() {
      this.maxMap = new HashMap();
      this.minMap = new HashMap();
      this.overloadMap = new HashMap();
      this.requestClassMap = new HashMap();
      this.workManagerTemplateMap = new HashMap();
   }

   synchronized void initialize() throws ManagementException {
      if (!this.initialized) {
         DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         this.stmb = var1.getSelfTuning();
         this.serverRuntime = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
         IncrementAdvisor.setMinThreadPoolSize(var2.getSelfTuningThreadPoolSizeMin());
         IncrementAdvisor.setMaxThreadPoolSize(var2.getSelfTuningThreadPoolSizeMax());
         ServerFailureTriggerMBean var3 = var2.getOverloadProtection().getServerFailureTrigger();
         if (var3 != null && var3.getStuckThreadCount() > 0) {
            debug("creating ServerFailureAction with " + var3.getMaxStuckThreadTime() + " seconds and count of " + var3.getStuckThreadCount());
            this.serverFailureAction = new ServerFailureAction(var3);
         }

         this.create(this.stmb.getFairShareRequestClasses());
         this.create(this.stmb.getResponseTimeRequestClasses());
         this.create(this.stmb.getContextRequestClasses());
         this.create(this.stmb.getMaxThreadsConstraints());
         this.create(this.stmb.getMinThreadsConstraints());
         this.create(this.stmb.getCapacities());
         this.create(this.stmb.getWorkManagers());
         this.initialized = true;
      }
   }

   void create(WorkManagerMBean[] var1) throws ConfigurationException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            WorkManagerMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               String var4 = var3.getName();
               if (this.workManagerTemplateMap.get(var4) == null) {
                  WorkManagerTemplate var5 = new WorkManagerTemplate(var3);
                  this.workManagerTemplateMap.put(var4, var5);
               }
            }
         }

      }
   }

   void create(FairShareRequestClassMBean[] var1) throws ManagementException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            FairShareRequestClassMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               FairShareRequestClass var4 = (FairShareRequestClass)this.getRequestClass(var3.getName(), FairShareRequestClass.class);
               if (var4 == null) {
                  this.create(var3);
               }
            }
         }

      }
   }

   private RequestClass create(FairShareRequestClassMBean var1) throws ManagementException {
      FairShareRequestClass var2 = new FairShareRequestClass(var1.getName(), var1.getFairShare());
      debug("created global fair share RC '" + var1.getName() + "'");
      var1.addBeanUpdateListener(new FairShareRequestClassBeanUpdateListener(var2));
      var2.setShared(true);
      this.requestClassMap.put(var1.getName(), var2);
      this.serverRuntime.addRequestClassRuntime(new RequestClassRuntimeMBeanImpl(var2));
      return var2;
   }

   void create(ResponseTimeRequestClassMBean[] var1) throws ManagementException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            ResponseTimeRequestClassMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               ResponseTimeRequestClass var4 = (ResponseTimeRequestClass)this.getRequestClass(var3.getName(), ResponseTimeRequestClass.class);
               if (var4 == null) {
                  this.create(var3);
               }
            }
         }

      }
   }

   private RequestClass create(ResponseTimeRequestClassMBean var1) throws ManagementException {
      ResponseTimeRequestClass var2 = new ResponseTimeRequestClass(var1.getName(), var1.getGoalMs());
      debug("created global response time RC '" + var1.getName() + "'");
      var1.addBeanUpdateListener(new ResponseTimeRequestClassBeanUpdateListener(var2));
      var2.setShared(true);
      this.requestClassMap.put(var1.getName(), var2);
      this.serverRuntime.addRequestClassRuntime(new RequestClassRuntimeMBeanImpl(var2));
      return var2;
   }

   void create(ContextRequestClassMBean[] var1) throws ConfigurationException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            ContextRequestClassMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               ContextRequestClass var4 = (ContextRequestClass)this.getRequestClass(var3.getName(), ContextRequestClass.class);
               if (var4 == null) {
                  this.create(var3);
               }
            }
         }

      }
   }

   private ContextRequestClass create(ContextRequestClassMBean var1) throws ConfigurationException {
      ContextRequestClass var2 = new ContextRequestClass(var1.getName());
      ContextCaseMBean[] var3 = var1.getContextCases();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            ContextCaseMBean var5 = var3[var4];
            RequestClass var6 = this.findPrimitiveRequestClass(var5.getRequestClassName());
            if (var6 == null) {
               throw new ConfigurationException("request class for group " + var5.getGroupName() + " not found");
            }

            if (var5.getUserName() != null) {
               var2.addUser(var5.getUserName(), var6);
            } else if (var5.getGroupName() != null) {
               var2.addGroup(var5.getGroupName(), var6);
            }
         }
      }

      debug("created global context RC '" + var1.getName() + "'");
      var2.setShared(true);
      this.requestClassMap.put(var1.getName(), var2);
      return var2;
   }

   private Object getRequestClass(String var1, Class var2) throws ConfigurationException {
      Object var3 = this.requestClassMap.get(var1);
      if (var2.isInstance(var3)) {
         return var3;
      } else if (var3 != null) {
         throw new ConfigurationException(var1 + " request class cannot be " + "created. A request class of a different type already exists with " + "that name");
      } else {
         return null;
      }
   }

   void create(MaxThreadsConstraintMBean[] var1) throws ManagementException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            MaxThreadsConstraintMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               MaxThreadsConstraint var4 = (MaxThreadsConstraint)this.maxMap.get(var3.getName());
               if (var4 == null) {
                  String var5 = var3.getConnectionPoolName();
                  if (var5 == null || var5.trim().length() == 0) {
                     this.create(var3);
                  }
               }
            }
         }

      }
   }

   private MaxThreadsConstraint create(MaxThreadsConstraintMBean var1) throws ManagementException {
      String var2 = var1.getConnectionPoolName();
      Object var3;
      if (var2 != null && var2.trim().length() != 0) {
         var3 = new PoolBasedMaxThreadsConstraint(var1.getName(), var1.getConnectionPoolName());
      } else {
         var3 = new MaxThreadsConstraint(var1.getName(), var1.getCount());
         var1.addBeanUpdateListener(new MaxThreadsConstraintBeanUpdateListener((MaxThreadsConstraint)var3));
      }

      debug("created global max threads constraint '" + var1.getName() + "'");
      this.maxMap.put(var1.getName(), var3);
      this.serverRuntime.addMaxThreadsConstraintRuntime(new MaxThreadsConstraintRuntimeMBeanImpl((MaxThreadsConstraint)var3));
      return (MaxThreadsConstraint)var3;
   }

   void create(MinThreadsConstraintMBean[] var1) throws ManagementException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            MinThreadsConstraintMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3)) {
               MinThreadsConstraint var4 = (MinThreadsConstraint)this.minMap.get(var3.getName());
               if (var4 == null) {
                  this.create(var3);
               }
            }
         }

      }
   }

   private MinThreadsConstraint create(MinThreadsConstraintMBean var1) throws ManagementException {
      MinThreadsConstraint var2 = new MinThreadsConstraint(var1.getName(), var1.getCount());
      var1.addBeanUpdateListener(new MinThreadsConstraintBeanUpdateListener(var2));
      debug("created global min threads constraint '" + var1.getName() + "'");
      this.minMap.put(var1.getName(), var2);
      this.serverRuntime.addMinThreadsConstraintRuntime(new MinThreadsConstraintRuntimeMBeanImpl(var2));
      return var2;
   }

   void create(CapacityMBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            CapacityMBean var3 = var1[var2];
            if (this.isDeployedToThisServer(var3) && this.overloadMap.get(var3.getName()) == null) {
               this.create(var3);
            }
         }

      }
   }

   private OverloadManager create(CapacityMBean var1) {
      OverloadManager var2 = new OverloadManager(var1.getName(), var1.getCount());
      var1.addBeanUpdateListener(new OverloadManagerBeanUpdateListener(var2));
      this.overloadMap.put(var1.getName(), var2);
      debug("created global capacity " + var1.getName() + "' with count " + var1.getCount());
      return var2;
   }

   synchronized RequestClass findRequestClass(String var1) {
      RequestClass var2 = this.findPrimitiveRequestClass(var1);
      if (var2 != null) {
         return var2;
      } else {
         try {
            ContextRequestClassMBean var3 = this.stmb.lookupContextRequestClass(var1);
            return var3 != null && this.isDeployedToThisServer(var3) ? this.create(var3) : null;
         } catch (ManagementException var4) {
            throw new IllegalArgumentException("RequestClassMBean  has invalid attributes", var4);
         }
      }
   }

   private RequestClass findPrimitiveRequestClass(String var1) {
      RequestClass var2 = (RequestClass)this.requestClassMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         try {
            FairShareRequestClassMBean var3 = this.stmb.lookupFairShareRequestClass(var1);
            if (var3 != null && this.isDeployedToThisServer(var3)) {
               return this.create(var3);
            } else {
               ResponseTimeRequestClassMBean var4 = this.stmb.lookupResponseTimeRequestClass(var1);
               return var4 != null && this.isDeployedToThisServer(var4) ? this.create(var4) : null;
            }
         } catch (ManagementException var5) {
            throw new IllegalArgumentException("RequestClassMBean  has invalid attributes", var5);
         }
      }
   }

   synchronized MaxThreadsConstraint findMaxThreadsConstraint(String var1) {
      MaxThreadsConstraint var2 = (MaxThreadsConstraint)this.maxMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         MaxThreadsConstraintMBean var3 = this.stmb.lookupMaxThreadsConstraint(var1);
         if (var3 != null && this.isDeployedToThisServer(var3)) {
            try {
               return this.create(var3);
            } catch (ManagementException var5) {
               throw new IllegalArgumentException("MaxThreadsConstraintMBean '" + var3.getName() + "' has invalid attributes", var5);
            }
         } else {
            return null;
         }
      }
   }

   synchronized MinThreadsConstraint findMinThreadsConstraint(String var1) {
      MinThreadsConstraint var2 = (MinThreadsConstraint)this.minMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         MinThreadsConstraintMBean var3 = this.stmb.lookupMinThreadsConstraint(var1);
         if (var3 != null && this.isDeployedToThisServer(var3)) {
            try {
               return this.create(var3);
            } catch (ManagementException var5) {
               throw new IllegalArgumentException("MinThreadsConstraintMBean '" + var3.getName() + "' has invalid attributes", var5);
            }
         } else {
            return null;
         }
      }
   }

   synchronized OverloadManager findOverloadManager(String var1) {
      OverloadManager var2 = (OverloadManager)this.overloadMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         CapacityMBean var3 = this.stmb.lookupCapacity(var1);
         return var3 != null && this.isDeployedToThisServer(var3) ? this.create(var3) : null;
      }
   }

   synchronized WorkManagerTemplate removeWorkManagerTemplate(String var1) {
      return (WorkManagerTemplate)this.workManagerTemplateMap.remove(var1);
   }

   synchronized WorkManagerTemplate findWorkManagerTemplate(WorkManagerMBean var1) {
      if (var1 == null || !"default".equals(var1.getName()) && !this.isDeployedToThisServer(var1)) {
         return null;
      } else {
         WorkManagerTemplate var2 = (WorkManagerTemplate)this.workManagerTemplateMap.get(var1.getName());
         if (var2 != null) {
            return var2;
         } else {
            try {
               var2 = new WorkManagerTemplate(var1);
               this.workManagerTemplateMap.put(var1.getName(), var2);
               return var2;
            } catch (ConfigurationException var4) {
               throw new IllegalArgumentException("WorkManagerMBean '" + var1.getName() + "' has invalid attributes", var4);
            }
         }
      }
   }

   ServerFailureAction getServerFailedAction() {
      return this.serverFailureAction;
   }

   private boolean isDeployedToThisServer(DeploymentMBean var1) {
      if (var1 != null) {
         TargetMBean[] var2 = var1.getTargets();
         if (var2 == null || var2.length == 0) {
            return true;
         }

         TargetMBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TargetMBean var6 = var3[var5];
            if (var6.getServerNames().contains(this.serverRuntime.getName())) {
               return true;
            }
         }
      }

      return false;
   }

   private static void debug(String var0) {
      if (debugWM.isEnabled()) {
         WorkManagerLogger.logDebug("<GlobalWMComponents>" + var0);
      }

   }

   // $FF: synthetic method
   GlobalWorkManagerComponentsFactory(Object var1) {
      this();
   }

   final class WorkManagerTemplate {
      private RequestClass requestClass;
      private MaxThreadsConstraint maxThreadsConstraint;
      private MinThreadsConstraint minThreadsConstraint;
      private OverloadManager capacity;

      WorkManagerTemplate(WorkManagerMBean var2) throws ConfigurationException {
         this.initRequestClass(var2);
         this.initMinThreadsConstraint(var2);
         this.initMaxThreadsConstraint(var2);
         this.initCapacity(var2);
      }

      RequestClass getRequestClass() {
         return this.requestClass;
      }

      MaxThreadsConstraint getMaxThreadsConstraint() {
         return this.maxThreadsConstraint;
      }

      MinThreadsConstraint getMinThreadsConstraint() {
         return this.minThreadsConstraint;
      }

      OverloadManager getCapacity() {
         return this.capacity;
      }

      private void initRequestClass(WorkManagerMBean var1) {
         ResponseTimeRequestClassMBean var2 = var1.getResponseTimeRequestClass();
         if (var2 != null) {
            ResponseTimeRequestClass var6 = new ResponseTimeRequestClass(var2.getName(), var2.getGoalMs());
            var2.addBeanUpdateListener(new ResponseTimeRequestClassBeanUpdateListener(var6));
            GlobalWorkManagerComponentsFactory.debug("created response time RC '" + var2.getName() + "' for WorkManagerMBean '" + var1.getName() + "'");
            this.requestClass = GlobalWorkManagerComponentsFactory.this.findRequestClass(var2.getName());
         } else {
            ContextRequestClassMBean var3 = var1.getContextRequestClass();
            if (var3 != null) {
               GlobalWorkManagerComponentsFactory.debug("created context RC  for WorkManagerMBean '" + var1.getName() + "'");
               this.requestClass = GlobalWorkManagerComponentsFactory.this.findRequestClass(var3.getName());
            } else {
               FairShareRequestClassMBean var4 = var1.getFairShareRequestClass();
               if (var4 != null) {
                  FairShareRequestClass var5 = new FairShareRequestClass(var4.getName(), var4.getFairShare());
                  var4.addBeanUpdateListener(new FairShareRequestClassBeanUpdateListener(var5));
                  GlobalWorkManagerComponentsFactory.debug("created fair share RC '" + var4.getName() + "' for WorkManagerMBean '" + var1.getName() + "'");
                  this.requestClass = GlobalWorkManagerComponentsFactory.this.findRequestClass(var4.getName());
               }

            }
         }
      }

      private void initCapacity(WorkManagerMBean var1) {
         CapacityMBean var2 = var1.getCapacity();
         if (var2 != null) {
            GlobalWorkManagerComponentsFactory.debug("created capacity '" + var2.getName() + "' for WorkManagerMBean '" + var1.getName() + "'");
            this.capacity = GlobalWorkManagerComponentsFactory.this.findOverloadManager(var2.getName());
            var2.addBeanUpdateListener(new OverloadManagerBeanUpdateListener(this.capacity));
         }

      }

      private void initMaxThreadsConstraint(WorkManagerMBean var1) {
         MaxThreadsConstraintMBean var2 = var1.getMaxThreadsConstraint();
         if (var2 != null) {
            GlobalWorkManagerComponentsFactory.debug("created max threads constraint '" + var2.getName() + "' for WorkManagerMBean '" + var1.getName() + "'");
            this.maxThreadsConstraint = GlobalWorkManagerComponentsFactory.this.findMaxThreadsConstraint(var2.getName());
            var2.addBeanUpdateListener(new MaxThreadsConstraintBeanUpdateListener(this.maxThreadsConstraint));
         }

      }

      private void initMinThreadsConstraint(WorkManagerMBean var1) {
         MinThreadsConstraintMBean var2 = var1.getMinThreadsConstraint();
         if (var2 != null) {
            GlobalWorkManagerComponentsFactory.debug("created min threads constraint '" + var2.getName() + "' for WorkManagerMBean '" + var1.getName() + "'");
            this.minThreadsConstraint = GlobalWorkManagerComponentsFactory.this.findMinThreadsConstraint(var2.getName());
            var2.addBeanUpdateListener(new MinThreadsConstraintBeanUpdateListener(this.minThreadsConstraint));
         }

      }
   }

   private static final class Factory {
      static final GlobalWorkManagerComponentsFactory THE_ONE = new GlobalWorkManagerComponentsFactory();
   }
}
