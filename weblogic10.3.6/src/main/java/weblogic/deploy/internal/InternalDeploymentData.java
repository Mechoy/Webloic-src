package weblogic.deploy.internal;

import java.io.Serializable;
import weblogic.management.deploy.DeploymentData;

public class InternalDeploymentData implements Serializable {
   private int deploymentOperation;
   private String deploymentTaskRuntimeId;
   private int notificationLevel;
   private DeploymentData externalDeploymentData;
   private String deploymentName;

   public DeploymentData getExternalDeploymentData() {
      return this.externalDeploymentData;
   }

   public void setExternalDeploymentData(DeploymentData var1) {
      this.externalDeploymentData = var1;
   }

   public int getNotificationLevel() {
      return this.notificationLevel;
   }

   public void setNotificationLevel(int var1) {
      this.notificationLevel = var1;
   }

   public int getDeploymentOperation() {
      return this.deploymentOperation;
   }

   public void setDeploymentOperation(int var1) {
      this.deploymentOperation = var1;
   }

   public void setDeploymentTaskRuntimeId(String var1) {
      this.deploymentTaskRuntimeId = var1;
   }

   public String getDeploymentTaskRuntimeId() {
      return this.deploymentTaskRuntimeId;
   }

   public void setDeploymentName(String var1) {
      this.deploymentName = var1;
   }

   public String getDeploymentName() {
      return this.deploymentName;
   }
}
