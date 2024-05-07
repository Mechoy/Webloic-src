package weblogic.management.deploy.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentVersion;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.utils.StackTraceUtils;

public class AppRuntimeStateManager {
   private static final String DEPLOYMENT_STORE_CONN_NAME = "weblogic.deploy.store";
   private final Map appStates = new HashMap();
   private PersistentStore pStore;
   private PersistentMap psMap;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static AppRuntimeStateManager singleton;
   static final String ROOT = "ROOT_MODULE";
   private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

   private AppRuntimeStateManager() {
   }

   public static AppRuntimeStateManager getManager() {
      if (singleton == null) {
         singleton = new AppRuntimeStateManager();
      }

      return singleton;
   }

   public ApplicationRuntimeState get(String var1) {
      synchronized(this.appStates) {
         return (ApplicationRuntimeState)this.appStates.get(var1);
      }
   }

   protected Set getAppStates() {
      synchronized(this.appStates) {
         return this.appStates.entrySet();
      }
   }

   private void initStore() throws ManagementException {
      if (this.psMap == null) {
         try {
            this.pStore = (PersistentStoreXA)PersistentStoreManager.getManager().getDefaultStore();
            this.psMap = this.pStore.createPersistentMap("weblogic.deploy.store");
         } catch (Exception var3) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(StackTraceUtils.throwable2StackTrace(var3));
            }

            String var2 = DeploymentManagerLogger.storeCreateFailed(var3);
            throw new ManagementException(var2, var3);
         }
      }
   }

   public Map getDeploymentVersions() {
      HashMap var1 = new HashMap();
      if (this.appStates != null) {
         synchronized(this.appStates) {
            Set var3 = this.appStates.entrySet();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               String var6 = (String)var5.getKey();
               ApplicationRuntimeState var7 = (ApplicationRuntimeState)var5.getValue();
               DeploymentVersion var8 = var7.getDeploymentVersion();
               if (var8 != null) {
                  var1.put(var6, var8);
               }
            }
         }
      }

      return var1;
   }

   public Map getStartupStateForServer(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (var2.getAdminServerName().equals(var1)) {
         return null;
      } else {
         HashMap var3 = new HashMap();
         synchronized(this.appStates) {
            Iterator var5 = this.appStates.entrySet().iterator();

            label63:
            while(true) {
               String var7;
               ApplicationRuntimeState var8;
               do {
                  if (!var5.hasNext()) {
                     return var3;
                  }

                  Map.Entry var6 = (Map.Entry)var5.next();
                  var7 = (String)var6.getKey();
                  var8 = (ApplicationRuntimeState)var6.getValue();
               } while(var8 == null);

               ApplicationRuntimeState var9 = new ApplicationRuntimeState(var8);
               Map var10 = var8.getAppTargetState();
               Map var11 = var8.getModules();
               Iterator var12 = var10.entrySet().iterator();

               while(true) {
                  String var14;
                  TargetMBean var15;
                  do {
                     do {
                        if (!var12.hasNext()) {
                           continue label63;
                        }

                        Map.Entry var13 = (Map.Entry)var12.next();
                        var14 = (String)var13.getKey();
                     } while(var14 == null);

                     var15 = var2.lookupTarget(var14);
                  } while(var15 == null);

                  if (this.isInTarget(var15, var1)) {
                     var9.updateAppTargetState(var8.getAppTargetState(var14), var14);
                     Iterator var16 = var11.keySet().iterator();

                     while(var16.hasNext()) {
                        String var17 = (String)var16.next();
                        var9.updateState(this.getModuleStates(var7, var17, var14));
                     }

                     var3.put(var7, var9);
                  }

                  DeploymentVersion var20 = var8.getDeploymentVersion();
                  if (var20 != null) {
                     var9.setDeploymentVersion(var20);
                  }
               }
            }
         }
      }
   }

   private boolean isInTarget(TargetMBean var1, String var2) {
      return var1.getServerNames().contains(var2);
   }

   public void loadStartupState(Map var1) throws ManagementException {
      if (var1 == null) {
         this.readAppStatesFromStore();
      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Reading app states received from admin server ...");
         }

         this.initStore();
         HashSet var2 = new HashSet();
         synchronized(this.appStates) {
            Iterator var4 = this.appStates.keySet().iterator();

            while(true) {
               if (!var4.hasNext()) {
                  break;
               }

               String var5 = (String)var4.next();
               var2.add(var5);
            }
         }

         Iterator var3 = var2.iterator();

         String var10;
         while(var3.hasNext()) {
            var10 = (String)var3.next();
            this.remove(var10);
         }

         ApplicationRuntimeState var11;
         for(var3 = var1.keySet().iterator(); var3.hasNext(); this.save(var11)) {
            var10 = (String)var3.next();
            var11 = (ApplicationRuntimeState)var1.get(var10);
            synchronized(this.appStates) {
               this.appStates.put(var10, var11);
            }
         }

      }
   }

   public void readAppStatesFromStore() throws ManagementException {
      this.initStore();

      try {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Reading app states from store...");
         }

         ArrayList var1 = new ArrayList();
         DomainMBean var13 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         String var3 = ManagementService.getPropertyService(kernelId).getServerName();
         Iterator var4 = this.psMap.keySet().iterator();

         String var5;
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            if (AppDeploymentHelper.lookupAppOrLib(var5, var13) == null) {
               var1.add(var5);
            } else {
               try {
                  ApplicationRuntimeState var6 = (ApplicationRuntimeState)this.psMap.get(var5);
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("read from store: " + var6);
                  }

                  synchronized(this.appStates) {
                     this.appStates.put(var5, var6);
                  }
               } catch (PersistentStoreException var11) {
               }
            }
         }

         var4 = var1.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();

            try {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("remove from store: " + var5);
               }

               this.psMap.remove(var5);
            } catch (PersistentStoreException var9) {
            }
         }

      } catch (Exception var12) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug(StackTraceUtils.throwable2StackTrace(var12));
         }

         String var2 = DeploymentManagerLogger.cannotReadStore(var12);
         throw new ManagementException(var2, var12);
      }
   }

   private ApplicationRuntimeState getOrCreate(String var1) {
      synchronized(this.appStates) {
         ApplicationRuntimeState var3 = (ApplicationRuntimeState)this.appStates.get(var1);
         if (var3 == null) {
            var3 = new ApplicationRuntimeState(var1);
            this.appStates.put(var1, var3);
         }

         return var3;
      }
   }

   public void save(ApplicationRuntimeState var1) throws ManagementException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("saving: " + var1);
      }

      try {
         this.psMap.put(var1.getAppId(), var1);
      } catch (Exception var4) {
         if (!ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown()) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(StackTraceUtils.throwable2StackTrace(var4));
            }

            String var3 = DeploymentManagerLogger.cannotSaveStore(var1.toString(), var4);
            throw new ManagementException(var3, var4);
         }
      }
   }

   public void remove(String var1) throws ManagementException {
      ApplicationRuntimeState var2 = null;
      synchronized(this.appStates) {
         var2 = (ApplicationRuntimeState)this.appStates.remove(var1);
      }

      if (var2 != null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("deleting: " + var2);
         }

         try {
            this.psMap.remove(var2.getAppId());
         } catch (Exception var6) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(StackTraceUtils.throwable2StackTrace(var6));
            }

            String var4 = DeploymentManagerLogger.cannotDeleteStore(var2.toString(), var6);
            throw new ManagementException(var4, var6);
         }
      }

   }

   public void removeTargets(String var1, String[] var2) throws ManagementException {
      if (Debug.isDeploymentDebugEnabled()) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            Debug.deploymentDebug("deleting target: " + var2[var3] + " for app:  " + var1);
         }
      }

      synchronized(this.appStates) {
         ApplicationRuntimeState var4 = (ApplicationRuntimeState)this.appStates.get(var1);
         if (var4 != null) {
            int var5;
            for(var5 = 0; var5 < var2.length; ++var5) {
               var4.removeAppTargetState(var2[var5]);
            }

            if (var4.getAppTargetState().size() == 0) {
               this.remove(var1);
            } else {
               try {
                  var4 = (ApplicationRuntimeState)this.psMap.get(var1);

                  for(var5 = 0; var5 < var2.length; ++var5) {
                     var4.removeAppTargetState(var2[var5]);
                  }
               } catch (PersistentStoreException var7) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("psMap remove error:  " + var7.getMessage());
                  }
               }
            }
         }

      }
   }

   public String getIntendedState(String var1) {
      ApplicationRuntimeState var2 = getManager().get(var1);
      if (var2 == null) {
         return null;
      } else {
         Collection var3 = var2.getAppTargetState().values();
         String var4 = "STATE_NEW";
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            AppTargetState var6 = (AppTargetState)var5.next();
            if (DeployHelper.less(var4, var6.getState())) {
               var4 = var6.getState();
            }
         }

         return var4;
      }
   }

   public String getIntendedState(String var1, String var2) {
      ApplicationRuntimeState var3 = getManager().get(var1);
      return var3 == null ? null : var3.getIntendedState(var2);
   }

   public int getStagingState(String var1, String var2) {
      ApplicationRuntimeState var3 = getManager().get(var1);
      return var3 == null ? -1 : var3.getStagingState(var2);
   }

   public String getCurrentState(String var1, String var2) {
      String var3 = this.getCurrentState(var1, "ROOT_MODULE", var2);
      if (!var3.equals("STATE_NEW")) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Returning state for root module: " + var3);
         }

         return var3;
      } else {
         String[] var4 = this.getModuleIds(var1, false);
         if (var4 == null) {
            return null;
         } else {
            for(int var6 = 0; var6 < var4.length; ++var6) {
               String var7 = var4[var6];
               String var5 = this.getCurrentState(var1, var7, var2);
               if (DeployHelper.less(var3, var5)) {
                  var3 = var5;
               }
            }

            return var3;
         }
      }
   }

   public String getCurrentStateOnServer(String var1, String var2, String var3) {
      String[] var4 = this.getModuleIds(var1);
      if (var4 == null) {
         return null;
      } else {
         String var5 = "STATE_NEW";

         for(int var7 = 0; var7 < var4.length; ++var7) {
            String var8 = var4[var7];
            String var6 = this.getCurrentStateOnServer(var1, var8, var2, var3);
            if (DeployHelper.less(var5, var6)) {
               var5 = var6;
            }
         }

         return var5;
      }
   }

   public String[] getModuleIds(String var1) {
      return this.getModuleIds(var1, true);
   }

   private String[] getModuleIds(String var1, boolean var2) {
      ApplicationRuntimeState var3 = getManager().get(var1);
      if (var3 == null) {
         return null;
      } else {
         Map var4 = var3.getModules();
         if (var4 == null) {
            return new String[0];
         } else {
            Object var5;
            if (var2) {
               var5 = new HashSet();
               Iterator var6 = var4.keySet().iterator();

               while(var6.hasNext()) {
                  String var7 = (String)var6.next();
                  if (!"ROOT_MODULE".equals(var7)) {
                     ((Set)var5).add(var7);
                  }
               }
            } else {
               var5 = var4.keySet();
            }

            return (String[])((String[])((Set)var5).toArray(new String[0]));
         }
      }
   }

   public String getModuleType(String var1, String var2) {
      Map var3 = this.getTargetLevelMap(var1, var2);
      if (var3 != null && !var3.isEmpty()) {
         Object var4 = var3.values().iterator().next();
         TargetModuleState var5 = this.getAnyTMSFromTargetMap(var4);
         String var6 = var5.getType();
         if (var6 == null) {
            var6 = WebLogicModuleType.MODULETYPE_UNKNOWN;
         }

         return var6;
      } else {
         return WebLogicModuleType.MODULETYPE_UNKNOWN;
      }
   }

   private TargetModuleState getAnyTMSFromTargetMap(Object var1) {
      TargetModuleState var2;
      if (var1 instanceof TargetModuleState) {
         var2 = (TargetModuleState)var1;
      } else {
         Object var3 = ((Map)var1).values().iterator().next();
         if (var3 instanceof TargetModuleState) {
            var2 = (TargetModuleState)var3;
         } else {
            var2 = (TargetModuleState)((Map)var3).values().iterator().next();
         }
      }

      return var2;
   }

   private TargetModuleState[] getAllTMSFromTargetMap(Object var1) {
      if (var1 instanceof TargetModuleState) {
         return new TargetModuleState[]{(TargetModuleState)var1};
      } else {
         Iterator var2 = ((Map)var1).values().iterator();
         ArrayList var3 = new ArrayList();

         while(var2.hasNext()) {
            Object var4 = var2.next();
            if (var4 instanceof TargetModuleState) {
               var3.add(var4);
            } else {
               var3.add(((Map)var4).values().iterator().next());
            }
         }

         return (TargetModuleState[])((TargetModuleState[])var3.toArray(new TargetModuleState[0]));
      }
   }

   public String getCurrentState(String var1, String var2, String var3) {
      TargetModuleState[] var4 = this.getModuleStates(var1, var2, var3);
      String var5 = "STATE_NEW";

      for(int var6 = 0; var4 != null && var6 < var4.length; ++var6) {
         TargetModuleState var7 = var4[var6];
         if (DeployHelper.less(var5, var7.getCurrentState())) {
            var5 = var7.getCurrentState();
         }
      }

      return var5;
   }

   public String getCurrentStateOnServer(String var1, String var2, String var3, String var4) {
      TargetModuleState[] var5 = this.getModuleStates(var1, var2, var3);
      String var6 = "STATE_NEW";

      for(int var7 = 0; var5 != null && var7 < var5.length; ++var7) {
         TargetModuleState var8 = var5[var7];
         if (var8.getServerName().equals(var4) && DeployHelper.less(var6, var8.getCurrentState())) {
            var6 = var8.getCurrentState();
         }
      }

      return var6;
   }

   public String getCurrentState(String var1, String var2, String var3, String var4) {
      return this.getCurrentState(var1, TargetModuleState.createName(var2, var3), var4);
   }

   public TargetModuleState[] getModuleStates(String var1, String var2, String var3) {
      Map var4 = this.getTargetLevelMap(var1, var2);
      if (var4 == null) {
         return null;
      } else {
         Object var5 = var4.get(var3);
         if (var5 == null) {
            Iterator var6 = var4.values().iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               if (var7 instanceof Map) {
                  var5 = ((Map)var7).get(var3);
               }

               if (var5 != null) {
                  break;
               }
            }
         }

         return var5 == null ? null : this.getAllTMSFromTargetMap(var5);
      }
   }

   public String[] getModuleTargets(String var1, String var2) {
      Map var3 = this.getTargetLevelMap(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         Set var4 = var3.keySet();
         return (String[])((String[])var4.toArray(new String[0]));
      }
   }

   public String[] getModuleTargets(String var1, String var2, String var3) {
      return this.getModuleTargets(var1, TargetModuleState.createName(var2, var3));
   }

   private Map getTargetLevelMap(String var1, String var2) {
      ApplicationRuntimeState var3 = getManager().get(var1);
      if (var3 == null) {
         return null;
      } else {
         Map var4 = var3.getModules();
         if (var4 == null) {
            return null;
         } else {
            Map var5 = (Map)var4.get(var2);
            return var5;
         }
      }
   }

   public boolean isAdminMode(AppDeploymentMBean var1) {
      return this.isAdminMode(var1.getName());
   }

   public boolean isAdminMode(String var1) {
      String var2 = this.getIntendedState(var1);
      return "STATE_ADMIN".equals(var2);
   }

   public boolean isAdminMode(String var1, String var2) {
      AppTargetState var3 = this.getAppTargetState(var1, var2);
      return var3 == null ? false : "STATE_ADMIN".equals(var3.getState());
   }

   public AppTargetState getAppTargetState(String var1, String var2) {
      ApplicationRuntimeState var3 = this.get(var1);
      return var3 == null ? null : var3.getAppTargetState(var2);
   }

   public void setState(String var1, String[] var2, String var3) throws ManagementException {
      if (var2 != null && var3 != null) {
         ApplicationRuntimeState var4 = null;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            String var6 = var2[var5];
            var4 = this.updateIntendedState(var1, var6, var3);
         }

         if (var4 != null) {
            this.save(var4);
         }

      }
   }

   public void setStagingState(String var1, String[] var2, int var3, boolean var4) throws ManagementException {
      ApplicationRuntimeState var5 = null;

      for(int var6 = 0; var6 < var2.length; ++var6) {
         String var7 = var2[var6];
         var5 = this.updateStagingState(var1, var7, var3);
      }

      if (var5 != null && !var4) {
         this.save(var5);
      }

   }

   private ApplicationRuntimeState updateIntendedState(String var1, String var2, String var3) {
      if (var1 != null && var2 != null && var3 != null) {
         ApplicationRuntimeState var4 = this.getOrCreate(var1);
         AppTargetState var5 = this.getAppTargetState(var1, var2);
         if (var5 == null) {
            var5 = new AppTargetState(var1, var2);
         }

         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Updating intended state for target: " + var2 + " state - " + var3);
         }

         var5.setState(var3);
         var4.updateAppTargetState(var5, var2);
         return var4;
      } else {
         return null;
      }
   }

   private ApplicationRuntimeState updateStagingState(String var1, String var2, int var3) {
      ApplicationRuntimeState var4 = this.getOrCreate(var1);
      if (var2 == null) {
         return var4;
      } else {
         AppTargetState var5 = this.getAppTargetState(var1, var2);
         if (var5 == null) {
            var5 = new AppTargetState(var1, var2);
         }

         if (var3 > -1) {
            var5.setStagingState(var3);
         }

         var4.updateAppTargetState(var5, var2);
         return var4;
      }
   }

   public boolean isActiveVersion(AppDeploymentMBean var1) {
      return this.isActiveVersion(var1.getName());
   }

   public boolean isActiveVersion(String var1) {
      if (ApplicationVersionUtils.getVersionId(var1) == null) {
         return true;
      } else {
         ApplicationRuntimeState var2 = this.get(var1);
         return var2 != null && var2.isActiveVersion();
      }
   }

   public void setActiveVersion(String var1) throws ManagementException {
      this.setActiveVersion(var1, false);
   }

   public void setActiveVersion(String var1, boolean var2) throws ManagementException {
      if (ApplicationVersionUtils.getVersionId(var1) != null) {
         ApplicationRuntimeState var3 = this.getOrCreate(var1);
         var3.setActiveVersion(var2);
         this.save(var3);
      }
   }

   public boolean isRetiredVersion(AppDeploymentMBean var1) {
      return var1 != null && var1.getVersionIdentifier() != null ? "STATE_RETIRED".equals(this.getCurrentState(var1)) : false;
   }

   public boolean isFailedVersion(AppDeploymentMBean var1) {
      return var1 != null && var1.getVersionIdentifier() != null ? "STATE_FAILED".equals(this.getCurrentState(var1)) : false;
   }

   public String getCurrentState(AppDeploymentMBean var1) {
      TargetMBean[] var2 = var1.getTargets();
      if (var2 != null && var2.length != 0) {
         String var3 = var2[0].getName();
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("getCurrentState " + var1.getName() + " state " + this.getCurrentState(var1.getName(), var3));
         }

         return this.getCurrentState(var1.getName(), var3);
      } else {
         return null;
      }
   }

   public int getRetireTimeoutSeconds(String var1) {
      ApplicationRuntimeState var2 = this.get(var1);
      return var2 == null ? 0 : var2.getRetireTimeoutSeconds();
   }

   public void setRetireTimeoutSeconds(String var1, int var2) throws ManagementException {
      if (ApplicationVersionUtils.getVersionId(var1) != null) {
         ApplicationRuntimeState var3 = this.getOrCreate(var1);
         var3.setRetireTimeoutSeconds(var2);
         this.save(var3);
      }
   }

   public long getRetireTimeMillis(String var1) {
      ApplicationRuntimeState var2 = this.get(var1);
      return var2 == null ? 0L : var2.getRetireTimeMillis();
   }

   public void setRetireTimeMillis(String var1, long var2) throws ManagementException {
      if (ApplicationVersionUtils.getVersionId(var1) != null) {
         ApplicationRuntimeState var4 = this.getOrCreate(var1);
         var4.setRetireTimeMillis(var2);
         this.save(var4);
      }
   }

   public void addStateListener(PropertyChangeListener var1) {
      this.pcs.addPropertyChangeListener(var1);
   }

   public void removeStateListener(PropertyChangeListener var1) {
      this.pcs.removePropertyChangeListener(var1);
   }

   public void updateState(String var1, DeploymentVersion var2) {
      ApplicationRuntimeState var3 = this.getOrCreate(var1);
      var3.setDeploymentVersion(var2);

      try {
         this.save(var3);
      } catch (ManagementException var5) {
         DeploymentManagerLogger.logStatePersistenceFailed(var1, var5);
      }

   }

   public void updateState(String var1, DeploymentState var2) {
      if (var1 != null) {
         ApplicationRuntimeState var3 = this.getOrCreate(var1);
         String var5 = var3.getIntendedState(var2.getTarget());
         if ((var5 != null || !"__Lifecycle_taskid__".equals(var2.getTaskID()) || "STATE_ACTIVE".equals(var2.getIntendedState())) && (var5 == null || DeployHelper.less(var5, var2.getIntendedState()))) {
            this.updateIntendedState(var1, var2.getTarget(), var2.getIntendedState());
         }

         var3 = this.updateStagingState(var1, var2.getTarget(), var2.getStagingState());
         var3.updateState(var2);
         if (this.pcs.hasListeners((String)null) && var2.getCurrentState() != null) {
            this.pcs.firePropertyChange(new PropertyChangeEvent(this, "State", (Object)null, var2));
         }

         try {
            this.save(var3);
         } catch (ManagementException var7) {
            DeploymentManagerLogger.logStatePersistenceFailed(var1, var7);
         }

      }
   }

   private void resetState(TargetModuleState var1, String var2) {
      if (var1.getTargetName().equals(var2) && !"STATE_RETIRED".equals(var1.getCurrentState())) {
         var1.setCurrentState("STATE_NEW");
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("reset: " + var1);
         }
      }

   }

   public void resetState(String var1, String[] var2) throws ManagementException {
      if (var2 != null) {
         ApplicationRuntimeState var3 = null;

         label58:
         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4];
            var3 = this.updateIntendedState(var1, var5, "STATE_NEW");
            Map var6 = var3.getModules();
            Iterator var7 = var6.keySet().iterator();

            while(true) {
               Map var10;
               do {
                  Map var9;
                  do {
                     if (!var7.hasNext()) {
                        continue label58;
                     }

                     String var8 = (String)var7.next();
                     var9 = (Map)var6.get(var8);
                  } while(var9 == null);

                  var10 = (Map)var9.get(var5);
               } while(var10 == null);

               Iterator var11 = var10.keySet().iterator();

               while(var11.hasNext()) {
                  String var12 = (String)var11.next();
                  Object var13 = var10.get(var12);
                  if (var13 != null && var13 instanceof Map) {
                     var13 = ((Map)var13).get(var12);
                  }

                  if (var13 != null && var13 instanceof TargetModuleState) {
                     TargetModuleState var14 = (TargetModuleState)var13;
                     this.resetState(var14, var5);
                  }
               }
            }
         }

         if (var3 != null) {
            this.save(var3);
         }

      }
   }

   public void updateStateForRedeployOperationOnCluster(String var1, DeploymentState var2) {
      if (var1 != null) {
         ApplicationRuntimeState var3 = this.getOrCreate(var1);
         var3.getIntendedState(var2.getTarget());
         this.updateIntendedState(var1, var2.getTarget(), var2.getIntendedState());
         var3 = this.updateStagingState(var1, var2.getTarget(), var2.getStagingState());
         var3.updateState(var2);

         try {
            this.save(var3);
         } catch (ManagementException var6) {
            DeploymentManagerLogger.logStatePersistenceFailed(var1, var6);
         }

      }
   }

   public String[] getSubmoduleIds(String var1, String var2) {
      ApplicationRuntimeState var3 = getManager().get(var1);
      if (var3 == null) {
         return null;
      } else {
         Map var4 = var3.getModules();
         if (var4 == null) {
            return null;
         } else {
            ArrayList var5 = new ArrayList();
            Iterator var6 = var4.keySet().iterator();

            while(var6.hasNext()) {
               String var7 = TargetModuleState.extractSubmodule((String)var6.next());
               if (var7 != null) {
                  var5.add(var7);
               }
            }

            return (String[])((String[])var5.toArray(new String[0]));
         }
      }
   }

   public String[] getStoppedModuleIds(String var1, String var2) {
      AppRuntimeStateManager var3 = getManager();
      if ("STATE_NEW".equals(var3.getIntendedState(var1))) {
         return null;
      } else {
         String[] var4 = var3.getModuleIds(var1, true);
         if (var4 == null) {
            return null;
         } else {
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var4.length; ++var6) {
               String var7 = this.getCurrentState(var1, var4[var6], var2);
               if ("STATE_NEW".equals(var7)) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Application: " + var1 + " has stopped module " + var4[var6] + " on target server " + var2);
                  }

                  var5.add(var4[var6]);
               }
            }

            if (var4.length == var5.size()) {
               return null;
            } else if (var5.size() == 0) {
               return null;
            } else {
               return (String[])((String[])var5.toArray(new String[0]));
            }
         }
      }
   }
}
