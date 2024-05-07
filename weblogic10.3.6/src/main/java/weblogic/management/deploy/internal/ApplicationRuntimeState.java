package weblogic.management.deploy.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentVersion;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;

public class ApplicationRuntimeState implements Serializable {
   private static final long serialVersionUID = 1L;
   private String appId;
   private int retireTimeoutSecs = -1;
   private long retireTimeMillis = -1L;
   private HashMap appTargetState = new HashMap();
   private DeploymentVersion deploymentVersion = null;
   private Map modules = new HashMap();

   public ApplicationRuntimeState() {
   }

   ApplicationRuntimeState(String var1) {
      this.appId = var1;
   }

   ApplicationRuntimeState(ApplicationRuntimeState var1) {
      this.appId = var1.getAppId();
      this.retireTimeoutSecs = var1.getRetireTimeoutSeconds();
      this.retireTimeMillis = var1.getRetireTimeMillis();
   }

   public String toString() {
      StringBuffer var1 = (new StringBuffer()).append("AppRTState");
      var1.append("[").append("appId=").append(this.appId).append(",retireTimeoutSecs=").append(this.retireTimeoutSecs).append(",retireTimeMillis=").append(this.retireTimeMillis).append(",\nModule(s) State:").append(this.modules).append(",\nAppTarget(s) State:").append(this.appTargetState);
      if (this.deploymentVersion != null) {
         var1.append(",\nDeploymentVersion=").append(this.deploymentVersion);
      }

      var1.append("]");
      return var1.toString();
   }

   public String getIntendedState(String var1) {
      AppTargetState var2 = this.getAppTargetState(var1);
      return var2 == null ? null : var2.getState();
   }

   public int getStagingState(String var1) {
      AppTargetState var2 = this.getAppTargetState(var1);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug(" AppTargetState for '" + var1 + "' is : " + var2);
      }

      return var2 == null ? -1 : var2.getStagingState();
   }

   public String getAppId() {
      return this.appId;
   }

   public boolean isAdminMode(String var1) {
      return "STATE_ADMIN".equals(this.getIntendedState(var1));
   }

   public boolean isActiveVersion() {
      return this.retireTimeMillis == 0L;
   }

   void setActiveVersion(boolean var1) {
      if (this.retireTimeMillis <= 0L || var1) {
         this.retireTimeMillis = 0L;
      }
   }

   public int getRetireTimeoutSeconds() {
      return this.retireTimeoutSecs;
   }

   void setRetireTimeoutSeconds(int var1) {
      this.retireTimeoutSecs = var1;
   }

   public long getRetireTimeMillis() {
      return this.retireTimeMillis;
   }

   void setRetireTimeMillis(long var1) {
      this.retireTimeMillis = var1;
   }

   public final DeploymentVersion getDeploymentVersion() {
      return this.deploymentVersion;
   }

   public final void setDeploymentVersion(DeploymentVersion var1) {
      this.deploymentVersion = var1;
   }

   public Map getModules() {
      return this.modules;
   }

   public Map getAppTargetState() {
      return this.appTargetState;
   }

   public AppTargetState getAppTargetState(String var1) {
      return (AppTargetState)this.appTargetState.get(var1);
   }

   public void updateAppTargetState(AppTargetState var1, String var2) {
      this.appTargetState.put(var2, var1);
   }

   public void removeAppTargetState(String var1) {
      this.appTargetState.remove(var1);
   }

   void updateState(DeploymentState var1) {
      TargetModuleState[] var2 = var1.getTargetModules();
      this.updateState(var2);
      var2 = new TargetModuleState[]{new TargetModuleState("ROOT_MODULE", "*", var1.getTarget(), "*", var1.getServerName())};
      var2[0].setCurrentState(var1.getCurrentState());
      this.updateState(var2);
   }

   public void updateState(TargetModuleState[] var1) {
      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         TargetModuleState var3 = var1[var2];
         if (!this.skip(var3)) {
            Object var4 = (Map)this.modules.get(var3.getModuleId());
            if (var4 == null) {
               var4 = new HashMap();
            }

            Object var5 = (Map)((Map)var4).get(var3.getTargetName());
            if (var5 == null) {
               var5 = new HashMap();
            }

            Object var6;
            if (var3.isLogicalTarget()) {
               var6 = ((Map)var5).get(var3.getServerName());
               if (var6 == null) {
                  var6 = new HashMap();
               }

               ((Map)var6).put(var3.getServerName(), var3);
            } else {
               var6 = var3;
            }

            ((Map)var5).put(var3.getServerName(), var6);
            ((Map)var4).put(var3.getTargetName(), var5);
            this.modules.put(var3.getModuleId(), var4);
         }
      }

   }

   private boolean skip(TargetModuleState var1) {
      return var1.getCurrentState() == null || var1.getModuleId() == null || var1.getTargetName() == null || var1.getServerName() == null;
   }

   void resetState(String var1) {
      Iterator var2 = this.modules.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = (Map)this.modules.get(var3);
         if (var4 == null) {
            var4 = new HashMap();
         }

         Iterator var5 = ((Map)var4).keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            Map var7 = (Map)((Map)var4).get(var6);
            if (var7 != null) {
               Object var8 = ((Map)var4).get(var6);
               if (var8 instanceof Map) {
                  Map var9 = (Map)var8;
                  Object var10 = var9.get(var1);
                  if (var10 instanceof Map) {
                     var10 = ((Map)var10).get(var1);
                  }

                  if (var10 != null) {
                     this.resetState((TargetModuleState)var10, var1);
                  }
               } else {
                  this.resetState((TargetModuleState)var8, var1);
               }
            }
         }
      }

   }

   private void resetState(TargetModuleState var1, String var2) {
      if (var1.getServerName().equals(var2) && !"STATE_RETIRED".equals(var1.getCurrentState())) {
         var1.setCurrentState("STATE_NEW");
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("reset: " + var1);
         }
      }

   }
}
