package weblogic.management.deploy.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class AppRuntimeStateRuntimeMBeanImpl extends DomainRuntimeMBeanDelegate implements AppRuntimeStateRuntimeMBean {
   public static final String NAME = "AppRuntimeStateRuntime";
   private static final long TARGET_STATE_CACHE_EXPIRY = 15000L;
   private static AppRuntimeStateRuntimeMBeanImpl singleton = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Map targetStateCache = new ConcurrentHashMap();
   private Map targetTimestampCache = new ConcurrentHashMap();

   private AppRuntimeStateRuntimeMBeanImpl() throws ManagementException {
      super("AppRuntimeStateRuntime");
   }

   public static final void initialize() throws ManagementException {
      if (singleton == null) {
         if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
            String var1 = DeploymentManagerLogger.logMBeanUnavailable();
            throw new ManagementException(var1);
         }

         singleton = new AppRuntimeStateRuntimeMBeanImpl();
         DomainAccess var0 = ManagementService.getDomainAccess(kernelId);
         var0.setAppRuntimeStateRuntime(singleton);
      }

   }

   public String[] getApplicationIds() {
      AppDeploymentMBean[] var1 = AppDeploymentHelper.getAppsAndLibs(ManagementService.getRuntimeAccess(kernelId).getDomain());
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         AppDeploymentMBean var4 = var1[var3];
         if (!var4.isInternalApp()) {
            var2.add(var4.getName());
         }
      }

      return (String[])((String[])var2.toArray(new String[0]));
   }

   public boolean isAdminMode(String var1, String var2) {
      return AppRuntimeStateManager.getManager().isAdminMode(var1, var2);
   }

   public boolean isActiveVersion(String var1) {
      return AppRuntimeStateManager.getManager().isActiveVersion(var1);
   }

   public long getRetireTimeMillis(String var1) {
      return AppRuntimeStateManager.getManager().getRetireTimeMillis(var1);
   }

   public int getRetireTimeoutSeconds(String var1) {
      return AppRuntimeStateManager.getManager().getRetireTimeoutSeconds(var1);
   }

   public String getIntendedState(String var1) {
      return AppRuntimeStateManager.getManager().getIntendedState(var1);
   }

   public String getIntendedState(String var1, String var2) {
      String var3 = AppRuntimeStateManager.getManager().getIntendedState(var1, var2);
      return this.fixState(var3, var2);
   }

   public String getCurrentState(String var1, String var2) {
      String var3 = AppRuntimeStateManager.getManager().getCurrentState(var1, var2);
      return this.fixState(var3, var2);
   }

   public String[] getModuleIds(String var1) {
      return AppRuntimeStateManager.getManager().getModuleIds(var1);
   }

   public String[] getSubmoduleIds(String var1, String var2) {
      return AppRuntimeStateManager.getManager().getSubmoduleIds(var1, var2);
   }

   public String getModuleType(String var1, String var2) {
      return AppRuntimeStateManager.getManager().getModuleType(var1, var2);
   }

   public String getCurrentState(String var1, String var2, String var3) {
      String var4 = AppRuntimeStateManager.getManager().getCurrentState(var1, var2, var3);
      return this.fixState(var4, var3);
   }

   public TargetModuleState[] getModuleStates(String var1, String var2, String var3) {
      return AppRuntimeStateManager.getManager().getModuleStates(var1, var2, var3);
   }

   public String[] getModuleTargets(String var1, String var2) {
      return AppRuntimeStateManager.getManager().getModuleTargets(var1, var2);
   }

   public String getCurrentState(String var1, String var2, String var3, String var4) {
      String var5 = AppRuntimeStateManager.getManager().getCurrentState(var1, var2, var3, var4);
      return this.fixState(var5, var4);
   }

   public String[] getModuleTargets(String var1, String var2, String var3) {
      return AppRuntimeStateManager.getManager().getModuleTargets(var1, var2, var3);
   }

   private String fixState(String var1, String var2) {
      if ("STATE_NEW".equals(var1)) {
         return var1;
      } else {
         return this.isTargetShutDown(var2) ? "STATE_NEW" : var1;
      }
   }

   private boolean isTargetShutDown(String var1) {
      if (this.targetTimestampCache.containsKey(var1)) {
         long var2 = (Long)this.targetTimestampCache.get(var1);
         if (System.currentTimeMillis() < var2 + 15000L) {
            return (Boolean)this.targetStateCache.get(var1);
         }
      }

      try {
         DomainMBean var15 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         Set var3 = null;
         TargetMBean var4 = var15.lookupTarget(var1);
         if (var4 != null) {
            var3 = var4.getServerNames();
         }

         if (var3 != null && !var3.isEmpty()) {
            DomainAccess var5 = ManagementService.getDomainAccess(kernelId);
            DomainRuntimeMBean var6 = var5.getDomainRuntime();
            DomainRuntimeServiceMBean var7 = var5.getDomainRuntimeService();
            ServerRuntimeMBean[] var8 = var7.getServerRuntimes();
            ArrayList var9 = new ArrayList(var8.length);
            if (var8 != null) {
               ServerRuntimeMBean[] var10 = var8;
               int var11 = var8.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  ServerRuntimeMBean var13 = var10[var12];
                  var9.add(var13.getName());
               }
            }

            Iterator var16 = var3.iterator();

            while(var16.hasNext()) {
               String var17 = (String)var16.next();
               boolean var18 = var9.contains(var17);
               if (var18) {
                  ServerLifeCycleRuntimeMBean var19 = var6.lookupServerLifeCycleRuntime(var17);
                  if (var19 != null && !"SHUTDOWN".equals(var19.getState())) {
                     this.targetStateCache.put(var1, false);
                     this.targetTimestampCache.put(var1, System.currentTimeMillis());
                     return false;
                  }
               }
            }

            this.targetStateCache.put(var1, true);
            this.targetTimestampCache.put(var1, System.currentTimeMillis());
            return true;
         }
      } catch (Exception var14) {
      }

      return false;
   }
}
