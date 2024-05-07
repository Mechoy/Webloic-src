package weblogic.deploy.service.internal.targetserver;

import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.DeploymentReceiverV2;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

final class DeploymentReceiverCallbackDeliverer implements DeploymentReceiverV2 {
   private final DeploymentReceiver delegate;

   DeploymentReceiverCallbackDeliverer(DeploymentReceiver var1) {
      this.delegate = var1;
   }

   public final String getHandlerIdentity() {
      return this.delegate.getHandlerIdentity();
   }

   public final void updateDeploymentContext(DeploymentContext var1) {
      this.doUpdateDeploymentContextCallback(var1);
   }

   public final void prepare(final DeploymentContext var1) {
      DeploymentRequest var3 = var1.getDeploymentRequest();
      if (this.delegate != null) {
         if ((!var3.isConfigurationProviderCalledLast() || this.delegate.getHandlerIdentity().equals("Configuration")) && (var3.isConfigurationProviderCalledLast() || !this.delegate.getHandlerIdentity().equals("Configuration"))) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  DeploymentReceiverCallbackDeliverer.this.doPrepareCallback(var1);
               }
            });
         } else {
            this.doPrepareCallback(var1);
         }
      }

   }

   public final void commit(final DeploymentContext var1) {
      DeploymentRequest var3 = var1.getDeploymentRequest();
      if (this.delegate != null) {
         if ((!var3.isConfigurationProviderCalledLast() || this.delegate.getHandlerIdentity().equals("Configuration")) && (var3.isConfigurationProviderCalledLast() || !this.delegate.getHandlerIdentity().equals("Configuration"))) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  DeploymentReceiverCallbackDeliverer.this.doCommitCallback(var1);
               }
            });
         } else {
            this.doCommitCallback(var1);
         }
      }

   }

   public final void cancel(final DeploymentContext var1) {
      if (this.delegate != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               DeploymentReceiverCallbackDeliverer.this.doCancelCallback(var1);
            }
         });
      }

   }

   public final void prepareCompleted(final DeploymentContext var1, final String var2) {
      if (this.delegate != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               DeploymentReceiverCallbackDeliverer.this.doPrepareCompletedCallback(var1, var2);
            }
         });
      }

   }

   public final void commitCompleted(final DeploymentContext var1, final String var2) {
      if (this.delegate != null) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               DeploymentReceiverCallbackDeliverer.this.doCommitCompletedCallback(var1, var2);
            }
         });
      }

   }

   public final void commitSkipped(final DeploymentContext var1) {
      if (this.delegate != null && this.delegate instanceof DeploymentReceiverV2) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               DeploymentReceiverCallbackDeliverer.this.doCommitSkippedCallback(var1);
            }
         });
      }

   }

   private final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   private final void doUpdateDeploymentContextCallback(DeploymentContext var1) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'updateDeploymentContext' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.updateDeploymentContext(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         DeploymentService.getDeploymentService().notifyContextUpdateFailed(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var3));
      }

   }

   private final void doPrepareCallback(DeploymentContext var1) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'prepare' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.prepare(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         DeploymentService.getDeploymentService().notifyPrepareFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var3));
      }

   }

   private final void doCommitCallback(DeploymentContext var1) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'commit' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.commit(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         DeploymentService.getDeploymentService().notifyCommitFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var3));
      }

   }

   private final void doCancelCallback(DeploymentContext var1) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'cancel' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.cancel(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         DeploymentService.getDeploymentService().notifyCancelFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var3));
      }

   }

   private final void doPrepareCompletedCallback(DeploymentContext var1, String var2) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'prepareCompleted' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.prepareCompleted(var1, var2);
      } catch (Throwable var4) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var4.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var4));
         }

         DeploymentService.getDeploymentService().notifyPrepareFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var4));
      }

   }

   private final void doCommitCompletedCallback(DeploymentContext var1, String var2) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'commitCompleted' on DeploymentReceiver for '" + this.getHandlerIdentity() + "' for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         this.delegate.commitCompleted(var1, var2);
      } catch (Throwable var4) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var4.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var4));
         }

         DeploymentService.getDeploymentService().notifyCommitFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var4));
      }

   }

   private final void doCommitSkippedCallback(DeploymentContext var1) {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling 'commitSkipped' on DeploymentReceiver for id '" + var1.getDeploymentRequest().getId() + "'");
         }

         DeploymentReceiverV2 var2 = (DeploymentReceiverV2)this.delegate;
         var2.commitSkipped(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         DeploymentService.getDeploymentService().notifyCommitFailure(var1.getDeploymentRequest().getId(), this.getHandlerIdentity(), new Exception(var3));
      }

   }
}
