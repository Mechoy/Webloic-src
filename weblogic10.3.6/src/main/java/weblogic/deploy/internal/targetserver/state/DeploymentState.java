package weblogic.deploy.internal.targetserver.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OptionalDataException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import weblogic.application.ModuleListenerCtx;
import weblogic.application.SubModuleListenerCtx;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.compatibility.NotificationBroadcaster;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DeploymentState implements Externalizable {
   private static final long serialVersionUID = 1L;
   private Map tms = new HashMap();
   private String appId;
   private String taskID;
   private int notifLevel;
   private String state;
   private int taskState;
   private Exception exception;
   private String target;
   private String intendedState;
   private int stagingState = -1;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private transient ArrayList xitions = new ArrayList();
   private String serverName;

   public DeploymentState() {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public DeploymentState(String var1, String var2, int var3) {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      this.appId = var1;
      this.taskID = var2;
      this.notifLevel = var3;
   }

   public String toString() {
      return super.toString() + "[" + "appid=" + this.appId + ",taskid=" + this.taskID + ",state=" + this.state + "]";
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getId() {
      return this.appId;
   }

   public String getCurrentState() {
      return this.state;
   }

   public void setCurrentState(String var1) {
      this.state = var1;
   }

   public void setCurrentState(String var1, boolean var2) {
      this.setCurrentState(var1);
      if (var2) {
         TargetModuleState[] var3 = this.getTargetModules();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4].setCurrentState(var1);
         }
      }

   }

   public String getIntendedState() {
      return this.intendedState;
   }

   public void setIntendedState(String var1) {
      this.intendedState = var1;
   }

   public int getStagingState() {
      return this.stagingState;
   }

   public void setStagingState(int var1) {
      this.stagingState = var1;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String var1) {
      this.target = var1;
   }

   private void setTarget(ModuleListenerCtx var1) {
      if (var1 instanceof SubModuleListenerCtx) {
         TargetMBean var2 = TargetUtils.findLocalServerTarget(((SubModuleListenerCtx)var1).getSubModuleTargets());
         if (var2 != null) {
            this.setTarget(var2.getName());
         }
      } else {
         this.setTarget(var1.getTarget().getName());
      }

   }

   public void addAppXition(String var1) {
      if (this.notifLevel >= 1) {
         this.xitions.add(new AppTransition(var1, (new Date()).getTime(), this.serverName));
      }

   }

   public Object[] getTransitions() {
      return this.xitions.toArray(new Object[this.xitions.size()]);
   }

   public TargetModuleState[] getTargetModules() {
      Collection var1 = this.tms.values();
      TargetModuleState[] var2 = new TargetModuleState[var1.size()];
      return (TargetModuleState[])((TargetModuleState[])var1.toArray(var2));
   }

   TargetModuleState addModuleTransition(ModuleListenerCtx var1, String var2, String var3, String var4, long var5) {
      TargetModuleState var7 = this.getOrCreateTargetModuleState(var1);
      if (var7 == null) {
         return null;
      } else {
         if (this.notifLevel >= 2 && NotificationBroadcaster.isRelevantToWLS81(var2, var3)) {
            this.xitions.add(new ModuleTransition(var2, var3, var4, var5, var7));
         }

         if (this.getTarget() == null) {
            this.setTarget(var1);
         }

         this.setIntendedState(var3);
         return var7;
      }
   }

   TargetModuleState getOrCreateTargetModuleState(ModuleListenerCtx var1) {
      if (var1 instanceof SubModuleListenerCtx) {
         return this.getOrCreateSubmoduleTargetState(var1);
      } else {
         TargetModuleState var2 = (TargetModuleState)this.tms.get(var1.getModuleUri());
         if (null == var2) {
            TargetMBean var3 = var1.getTarget();
            if (var3 == null) {
               return null;
            }

            var2 = new TargetModuleState(var1.getModuleUri(), var1.getType(), var3.getName(), var3.getType(), this.serverName);
            this.tms.put(var1.getModuleUri(), var2);
         }

         return var2;
      }
   }

   private TargetModuleState getOrCreateSubmoduleTargetState(ModuleListenerCtx var1) {
      SubModuleListenerCtx var2 = (SubModuleListenerCtx)var1;
      TargetMBean var3 = TargetUtils.findLocalServerTarget(var2.getSubModuleTargets());
      if (var3 == null) {
         return null;
      } else {
         TargetModuleState var4 = new TargetModuleState(var1.getModuleUri(), var2.getSubModuleName(), var1.getType(), var3.getName(), var3.getType(), this.serverName);
         TargetModuleState var5 = (TargetModuleState)this.tms.get(var4.getModuleId());
         if (var5 == null) {
            this.tms.put(var4.getModuleId(), var4);
            var5 = var4;
         }

         return var5;
      }
   }

   public String getTaskID() {
      return this.taskID;
   }

   public int getTaskState() {
      return this.taskState;
   }

   public void setTaskState(int var1) {
      this.taskState = var1;
   }

   public Exception getException() {
      return this.exception;
   }

   public void setException(Exception var1) {
      this.exception = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.tms);
      var1.writeObject(this.appId);
      var1.writeObject(this.taskID);
      var1.writeInt(this.notifLevel);
      var1.writeObject(this.state);
      var1.writeInt(this.taskState);
      var1.writeObject(this.exception);
      var1.writeObject(this.target);
      var1.writeObject(this.intendedState);
      var1.writeInt(this.stagingState);
      var1.writeObject(this.serverName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      Map var2 = (Map)var1.readObject();
      if (var2 != null) {
         this.tms.putAll(var2);
      }

      this.appId = (String)var1.readObject();
      this.taskID = (String)var1.readObject();
      this.notifLevel = var1.readInt();
      this.state = (String)var1.readObject();
      this.taskState = var1.readInt();
      this.exception = (Exception)var1.readObject();
      this.target = (String)var1.readObject();
      this.intendedState = (String)var1.readObject();
      this.stagingState = var1.readInt();

      try {
         this.serverName = (String)var1.readObject();
      } catch (OptionalDataException var4) {
      }

   }
}
