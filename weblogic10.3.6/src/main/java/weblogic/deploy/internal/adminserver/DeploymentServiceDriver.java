package weblogic.deploy.internal.adminserver;

import java.io.Serializable;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentException;
import weblogic.deploy.service.DeploymentProvider;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.InvalidCreateChangeDescriptorException;
import weblogic.deploy.service.RegistrationExistsException;
import weblogic.deploy.service.RequiresRestartFailureDescription;
import weblogic.deploy.service.RequiresTaskMediatedStartException;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;

public final class DeploymentServiceDriver implements DeploymentServiceCallbackHandler {
   private String identity;
   private final DeploymentService service;
   private DeploymentManager deploymentManager;

   private DeploymentServiceDriver() {
      this.service = DeploymentService.getDeploymentService();
   }

   public static DeploymentServiceDriver getInstance() {
      return DeploymentServiceDriver.Maker.DRIVER;
   }

   public void initialize(String var1, Version var2, DeploymentManager var3) throws ManagementException {
      try {
         this.identity = var1;
         this.deploymentManager = var3;
         this.service.register(var2, this);
         this.service.registerDeploymentProvider(var3);
      } catch (RegistrationExistsException var6) {
         String var5 = DeploymentServiceLogger.duplicateRegistration(var1);
         throw new ManagementException(var5);
      }
   }

   public void shutdown() {
      this.service.unregister(this.getHandlerIdentity());
   }

   public DeploymentRequestTaskRuntimeMBean deploy(DeploymentRequest var1) throws RequiresTaskMediatedStartException {
      return this.service.deploy(var1);
   }

   public DeploymentRequestTaskRuntimeMBean startDeploy(DeploymentRequest var1) {
      return this.service.startDeploy(var1);
   }

   public String getHandlerIdentity() {
      return this.identity;
   }

   public Deployment[] getDeployments(Version var1, Version var2, String var3) {
      return this.deploymentManager.getDeployments(var1, var2, var3);
   }

   public void deploySucceeded(long var1, FailureDescription[] var3) {
      String var4 = getFailureDescriptions(var3);
      if (var4 != null) {
         Debug.deploymentLogger.debug("Deployment id '" + var1 + "' succeeded - however the listed servers did not receive the " + "deployment for the provided reasons:" + var4 + " - these " + "servers will receive the deployment when they are reachable from " + "the admin server");
      }

      this.deploymentManager.deploymentRequestSucceeded(var1, var3);
   }

   public void deployFailed(long var1, DeploymentException var3) {
      Debug.deploymentLogger.debug("Deployment id '" + var1 + "' failed due to the following reason: " + var3.toString());
      FailureDescription[] var4 = var3.getFailures();
      this.deploymentManager.deploymentRequestFailed(var1, var3, var4);
   }

   public void commitFailed(long var1, FailureDescription[] var3) {
      String var4 = getFailureDescriptions(var3);
      if (var4 != null) {
         Debug.deploymentLogger.debug("Deployment id '" + var1 + "' succeeded - however the listed servers did not receive the " + "'commit' for the provided reasons:" + var4);
      }

      this.deploymentManager.deploymentRequestCommitFailed(var1, var3);
   }

   public void commitSucceeded(long var1) {
      this.deploymentManager.deploymentRequestCommitSucceeded(var1);
   }

   public void cancelSucceeded(long var1, FailureDescription[] var3) {
      String var4 = getFailureDescriptions(var3);
      Debug.deploymentLogger.debug("Deployment id '" + var1 + "' was successfully canceled - however the listed servers did not " + "receive the cancel due to the provided reasons:" + var4);
      this.deploymentManager.deploymentRequestCancelSucceeded(var1, var3);
   }

   public void cancelFailed(long var1, DeploymentException var3) {
      Debug.deploymentLogger.debug("Attempt to cancel deployment id '" + var1 + "' failed due to the following reason: " + var3.toString());
      FailureDescription[] var4 = var3.getFailures();
      this.deploymentManager.deploymentRequestCancelFailed(var1, var3, var4);
   }

   private static String getFailureDescriptions(FailureDescription[] var0) {
      String var1 = null;
      if (var0 != null && var0.length > 0) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2.append("Deployment to '");
            var2.append(var0[var3].getServer());
            if (var0[var3] instanceof RequiresRestartFailureDescription) {
               var2.append("' requires restart due to non-dynamic changes");
            } else {
               var2.append("' failed with the reason: '");
               var2.append(var0[var3].getReason().toString());
               var2.append("'");
            }
         }

         var1 = var2.toString();
      }

      return var1;
   }

   public void receivedStatusFrom(long var1, Serializable var3, String var4) {
      this.deploymentManager.handleReceivedStatus(var1, (DeploymentState)var3, var4);
   }

   public final void registerDeploymentProvider(DeploymentProvider var1) {
      this.service.registerDeploymentProvider(var1);
   }

   public Set getRegisteredDeploymentProviders() {
      return this.service.getRegisteredDeploymentProviders();
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4) throws InvalidCreateChangeDescriptorException {
      return this.service.createChangeDescriptor(var1, var2, var3, var4);
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4, String var5) throws InvalidCreateChangeDescriptorException {
      return this.service.createChangeDescriptor(var1, var2, var3, var4, var5);
   }

   public final ChangeDescriptor createChangeDescriptor(Serializable var1, Serializable var2) {
      return this.service.createChangeDescriptor(var1, var2);
   }

   public DeploymentRequest createDeploymentRequest() throws ManagementException {
      return this.service.createDeploymentRequest();
   }

   // $FF: synthetic method
   DeploymentServiceDriver(Object var1) {
      this();
   }

   static final class Maker {
      static final DeploymentServiceDriver DRIVER = new DeploymentServiceDriver();
   }
}
