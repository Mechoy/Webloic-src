package weblogic.deploy.service.internal.adminserver;

import java.io.Serializable;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentException;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.DeploymentServiceCallbackHandlerV2;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.StatusListener;
import weblogic.deploy.service.Version;

final class DeploymentServiceCallbackDeliverer implements DeploymentServiceCallbackHandlerV2, StatusListener {
   private final DeploymentServiceCallbackHandler delegate;

   DeploymentServiceCallbackDeliverer(DeploymentServiceCallbackHandler var1) {
      this.delegate = var1;
      StatusDeliverer.getInstance().registerStatusListener(var1.getHandlerIdentity(), this);
   }

   private final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public final String getHandlerIdentity() {
      return this.delegate.getHandlerIdentity();
   }

   public final Deployment[] getDeployments(Version var1, Version var2, String var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'getDeployments' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  to sync from '" + var1 + "' to '" + var2 + "' for server '" + var3 + "'");
      }

      return this.delegate.getDeployments(var1, var2, var3);
   }

   public final void deploySucceeded(long var1, FailureDescription[] var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'deploySucceeded' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "'");
      }

      this.delegate.deploySucceeded(var1, var3);
   }

   public final void deployFailed(long var1, DeploymentException var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'deployFailed' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "' due to '" + var3 + "'");
      }

      this.delegate.deployFailed(var1, var3);
   }

   public void commitFailed(long var1, FailureDescription[] var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'commitDeliverFailed' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "'");
      }

      this.delegate.commitFailed(var1, var3);
   }

   public void commitSucceeded(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'commitSucceeded' on DeploymentServiceCallbackHandlerfor '" + this.getHandlerIdentity() + "' for id '" + var1 + "'");
      }

      this.delegate.commitSucceeded(var1);
   }

   public final void cancelSucceeded(long var1, FailureDescription[] var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'cancelSucceeded' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "'");
      }

      this.delegate.cancelSucceeded(var1, var3);
   }

   public final void cancelFailed(long var1, DeploymentException var3) {
      if (this.isDebugEnabled()) {
         this.debug("Calling 'cancelFailed' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "' due to '" + var3 + "'");
      }

      this.delegate.cancelFailed(var1, var3);
   }

   public final void statusReceived(Serializable var1, String var2) {
   }

   public final void statusReceived(long var1, Serializable var3, String var4) {
      if (Debug.serviceStatusLogger.isDebugEnabled()) {
         Debug.serviceStatusLogger.debug("Calling 'received status' on DeploymentServiceCallbackHandler for '" + this.getHandlerIdentity() + "'  for id '" + var1 + "' from server '" + var4 + "'");
      }

      this.delegate.receivedStatusFrom(var1, var3, var4);
   }

   public final void receivedStatusFrom(long var1, Serializable var3, String var4) {
   }

   public final void requestStatusUpdated(long var1, String var3, String var4) {
      if (this.delegate instanceof DeploymentServiceCallbackHandlerV2) {
         DeploymentServiceCallbackHandlerV2 var5 = (DeploymentServiceCallbackHandlerV2)this.delegate;
         var5.requestStatusUpdated(var1, var3, var4);
      }

   }
}
