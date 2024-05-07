package weblogic.deploy.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.BaseDeploymentImpl;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;

public final class Deployment extends BaseDeploymentImpl {
   private static final long serialVersionUID = 6457624201160781380L;
   private InternalDeploymentData internalDeploymentData;
   private String taskRuntimeId;
   private boolean isBeforeDeploymentHandler;
   private boolean isDeploy;
   private boolean requiresRestart;
   private boolean isSyncWithAdmin;
   private Map syncWithAdminState = new HashMap();
   private DeploymentVersion proposedDeploymentVersion;
   private long deploymentRequestId;
   private transient DeploymentTaskRuntime taskRuntime;
   private transient AuthenticatedSubject initiator;
   private transient boolean isCallerLockOwner;
   private transient boolean isStaged;
   private transient boolean isAControlOperation;
   private transient boolean isAnAppDeployment;
   private transient int operation;

   public Deployment() {
   }

   public Deployment(String var1, DeploymentTaskRuntime var2, DeploymentData var3, AuthenticatedSubject var4, boolean var5, boolean var6, boolean var7) {
      super(var2 != null ? var2.getDeploymentMBean().getName() : null, var1, (Version)null, (List)null, (List)null, (String)null, (List)null);
      this.taskRuntime = var2;
      this.internalDeploymentData = new InternalDeploymentData();
      this.internalDeploymentData.setExternalDeploymentData(var3);
      if (var2 != null) {
         this.internalDeploymentData.setNotificationLevel(var2.getNotificationLevel());
         this.internalDeploymentData.setDeploymentOperation(var2.getTask());
         this.internalDeploymentData.setDeploymentName(var2.getApplicationId());
         this.taskRuntimeId = var2.getId();
         this.operation = var2.getTask();
         if (var2.getAppDeploymentMBean() != null) {
            this.isAnAppDeployment = true;
         }
      }

      this.initiator = var4;
      this.isCallerLockOwner = var5;
      this.isAControlOperation = var6;
      this.requiresRestart = var7;
   }

   public void setNotificationLevel(int var1) {
      this.internalDeploymentData.setNotificationLevel(var1);
   }

   public final int getOperation() {
      return this.operation;
   }

   public final DeploymentVersion getProposedDeploymentVersion() {
      return this.proposedDeploymentVersion;
   }

   public final void setProposedDeploymentVersion(DeploymentVersion var1) {
      this.proposedDeploymentVersion = var1;
   }

   public final boolean isCallerLockOwner() {
      return this.isCallerLockOwner;
   }

   public final AuthenticatedSubject getInitiator() {
      return this.initiator;
   }

   public final boolean isAControlOperation() {
      return this.isAControlOperation;
   }

   public final boolean requiresRestart() {
      return this.requiresRestart;
   }

   public final DeploymentTaskRuntime getDeploymentTaskRuntime() {
      return this.taskRuntime;
   }

   public final String getDeploymentTaskRuntimeId() {
      return this.taskRuntimeId;
   }

   public final boolean isBeforeDeploymentHandler() {
      return this.isBeforeDeploymentHandler;
   }

   public final void setBeforeDeploymentHandler() {
      this.isBeforeDeploymentHandler = true;
   }

   public final boolean isDeploy() {
      return this.isDeploy;
   }

   public final void setIsDeploy() {
      this.isDeploy = true;
   }

   public final InternalDeploymentData getInternalDeploymentData() {
      return this.internalDeploymentData;
   }

   public final void setInternalDeploymentData(InternalDeploymentData var1) {
      this.internalDeploymentData = var1;
   }

   public final void setDeploymentRequestIdentifier(long var1) {
      this.deploymentRequestId = var1;
   }

   public final long getDeploymentRequestId() {
      return this.deploymentRequestId;
   }

   public final boolean isAnAppDeployment() {
      return this.isAnAppDeployment;
   }

   public final void setStaged(String var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Setting staged mode of '" + this.getIdentity() + "' to '" + var1 + "'");
      }

      if (var1 == null || "stage".equals(var1)) {
         this.isStaged = true;
      }

   }

   public final boolean isStaged() {
      return this.isStaged;
   }

   public final boolean isSyncWithAdmin() {
      return this.isSyncWithAdmin;
   }

   public final void enableSyncWithAdmin(Map var1) {
      this.isSyncWithAdmin = true;
      this.syncWithAdminState = var1;
   }

   public final Map getSyncWithAdminState() {
      return this.syncWithAdminState;
   }

   protected String toPrettyString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toPrettyString());
      var1.append(", isBeforeDeploymentHandler: ");
      var1.append(this.isBeforeDeploymentHandler);
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeObject(this.internalDeploymentData);
      var1.writeObject(this.taskRuntimeId);
      var1.writeBoolean(this.isBeforeDeploymentHandler);
      var1.writeBoolean(this.isDeploy);
      var1.writeBoolean(this.requiresRestart);
      var1.writeBoolean(this.isSyncWithAdmin);
      var1.writeObject(this.syncWithAdminState);
      var1.writeObject(this.proposedDeploymentVersion);
      var1.writeLong(this.deploymentRequestId);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.internalDeploymentData = (InternalDeploymentData)var1.readObject();
      this.taskRuntimeId = (String)var1.readObject();
      this.isBeforeDeploymentHandler = var1.readBoolean();
      this.isDeploy = var1.readBoolean();
      this.requiresRestart = var1.readBoolean();
      this.isSyncWithAdmin = var1.readBoolean();
      this.syncWithAdminState = (Map)var1.readObject();
      this.proposedDeploymentVersion = (DeploymentVersion)var1.readObject();
      this.deploymentRequestId = var1.readLong();
   }
}
