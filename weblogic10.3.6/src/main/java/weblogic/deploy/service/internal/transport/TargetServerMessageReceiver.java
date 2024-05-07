package weblogic.deploy.service.internal.transport;

interface TargetServerMessageReceiver {
   void receiveHeartbeatMsg(DeploymentServiceMessage var1);

   void receiveRequestPrepareMsg(DeploymentServiceMessage var1);

   void receiveRequestCommitMsg(DeploymentServiceMessage var1);

   void receiveRequestCancelMsg(DeploymentServiceMessage var1);

   void receiveGetDeploymentsResponse(DeploymentServiceMessage var1);
}
