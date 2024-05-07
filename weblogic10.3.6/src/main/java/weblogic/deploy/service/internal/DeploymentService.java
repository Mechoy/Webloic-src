package weblogic.deploy.service.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.ChangeDescriptorFactory;
import weblogic.deploy.service.DataTransferHandler;
import weblogic.deploy.service.DataTransferHandlerExistsException;
import weblogic.deploy.service.DataTransferHandlerManager;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentProvider;
import weblogic.deploy.service.DeploymentProviderManager;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.DeploymentReceiversCoordinator;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.DeploymentRequestFactory;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.DeploymentServiceOperations;
import weblogic.deploy.service.InvalidCreateChangeDescriptorException;
import weblogic.deploy.service.RegistrationException;
import weblogic.deploy.service.RegistrationExistsException;
import weblogic.deploy.service.RequiresTaskMediatedStartException;
import weblogic.deploy.service.StatusListener;
import weblogic.deploy.service.StatusListenerManager;
import weblogic.deploy.service.StatusRelayer;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.datatransferhandlers.DataHandlerManager;
import weblogic.deploy.service.internal.adminserver.AdminDeploymentService;
import weblogic.deploy.service.internal.targetserver.TargetDeploymentService;
import weblogic.deploy.service.internal.transport.CommonMessageReceiver;
import weblogic.deploy.service.internal.transport.CommonMessageSender;
import weblogic.management.ManagementException;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkManagerFactory;

public final class DeploymentService extends AbstractServerService implements DataTransferHandlerManager, DeploymentRequestFactory, DeploymentServiceOperations, DeploymentReceiversCoordinator, ChangeDescriptorFactory, DeploymentProviderManager, StatusListenerManager, StatusRelayer {
   private static DeploymentService singleton;
   private static final byte DEPLOYMENT_SERVICE_VERSION = 2;
   private final AdminDeploymentService adminDelegate;
   private final TargetDeploymentService targetDelegate;
   private CommonMessageSender messageSender = null;
   private CommonMessageReceiver messageReceiver = null;
   private final Map dataTransferHandlers = new HashMap();

   public DeploymentService() {
      singleton = this;
      this.adminDelegate = AdminDeploymentService.getDeploymentService();
      this.targetDelegate = TargetDeploymentService.getDeploymentService();
   }

   public static DeploymentService getDeploymentService() {
      return singleton;
   }

   public final CommonMessageReceiver getMessageReceiver() {
      return this.messageReceiver;
   }

   public final CommonMessageSender getMessageSender() {
      return this.messageSender;
   }

   public static final byte getVersionByte() {
      return 2;
   }

   private static final void debug(String var0) {
      Debug.serviceDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public final void registerDataTransferHandler(DataTransferHandler var1) throws DataTransferHandlerExistsException {
      String var2 = var1.getType();
      synchronized(this.dataTransferHandlers) {
         if (this.dataTransferHandlers.get(var2) != null) {
            throw new DataTransferHandlerExistsException(DeploymentServiceLogger.logDataHandlerExistsLoggable(var2).getMessage());
         } else {
            this.dataTransferHandlers.put(var2, var1);
         }
      }
   }

   public final DataTransferHandler getDataTransferHandler(String var1) {
      synchronized(this.dataTransferHandlers) {
         return (DataTransferHandler)this.dataTransferHandlers.get(var1);
      }
   }

   public final String[] getRegisteredDataTransferHandlerTypes() {
      synchronized(this.dataTransferHandlers) {
         String[] var2 = (String[])((String[])this.dataTransferHandlers.keySet().toArray());
         return var2;
      }
   }

   public final DeploymentRequest createDeploymentRequest() throws ManagementException {
      return this.adminDelegate.createDeploymentRequest();
   }

   public final void register(Version var1, DeploymentServiceCallbackHandler var2) throws RegistrationExistsException {
      this.adminDelegate.register(var1, var2);
   }

   public final DeploymentRequestTaskRuntimeMBean deploy(DeploymentRequest var1) throws RequiresTaskMediatedStartException {
      return this.adminDelegate.deploy(var1);
   }

   public final DeploymentRequestTaskRuntimeMBean startDeploy(DeploymentRequest var1) {
      return this.adminDelegate.startDeploy(var1);
   }

   public final void cancel(DeploymentRequest var1) {
      this.adminDelegate.cancel(var1);
   }

   public final void unregister(String var1) {
      this.adminDelegate.unregister(var1);
   }

   public final DeploymentContext registerHandler(Version var1, DeploymentReceiver var2) throws RegistrationException {
      return this.targetDelegate.registerHandler(var1, var2);
   }

   public final void unregisterHandler(String var1) {
      this.targetDelegate.unregisterHandler(var1);
   }

   public final void notifyContextUpdated(final long var1, final String var3) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyContextUpdated(var1, var3);
         }
      });
   }

   public final void notifyContextUpdateFailed(final long var1, final String var3, final Throwable var4) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyContextUpdateFailed(var1, var3, var4);
         }
      });
   }

   public final void notifyPrepareSuccess(final long var1, final String var3) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyPrepareSuccess(var1, var3);
         }
      });
   }

   public final void notifyPrepareFailure(final long var1, final String var3, final Throwable var4) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyPrepareFailure(var1, var3, var4);
         }
      });
   }

   public final void notifyCommitSuccess(final long var1, final String var3) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyCommitSuccess(var1, var3);
         }
      });
   }

   public final void notifyCommitFailure(final long var1, final String var3, final Throwable var4) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyCommitFailure(var1, var3, var4);
         }
      });
   }

   public final void notifyCancelSuccess(final long var1, final String var3) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyCancelSuccess(var1, var3);
         }
      });
   }

   public final void notifyCancelFailure(final long var1, final String var3, final Throwable var4) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyCancelFailure(var1, var3, var4);
         }
      });
   }

   public final void notifyStatusUpdate(final long var1, final String var3, final Serializable var4) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            DeploymentService.this.targetDelegate.notifyStatusUpdate(var1, var3, var4);
         }
      });
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4) throws InvalidCreateChangeDescriptorException {
      return this.adminDelegate.createChangeDescriptor(var1, var2, var3, var4);
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4, String var5) throws InvalidCreateChangeDescriptorException {
      return this.adminDelegate.createChangeDescriptor(var1, var2, var3, var4, var5);
   }

   public final ChangeDescriptor createChangeDescriptor(Serializable var1, Serializable var2) {
      return this.adminDelegate.createChangeDescriptor(var1, var2);
   }

   public final void registerDeploymentProvider(DeploymentProvider var1) {
      this.adminDelegate.registerDeploymentProvider(var1);
   }

   public final Set getRegisteredDeploymentProviders() {
      return this.adminDelegate.getRegisteredDeploymentProviders();
   }

   public void registerStatusListener(String var1, StatusListener var2) {
      this.adminDelegate.registerStatusListener(var1, var2);
   }

   public void unregisterStatusListener(String var1) {
      this.adminDelegate.unregisterStatusListener(var1);
   }

   public void relayStatus(String var1, Serializable var2) {
      this.targetDelegate.relayStatus(var1, var2);
   }

   public void relayStatus(long var1, String var3, Serializable var4) {
      this.targetDelegate.relayStatus(var1, var3, var4);
   }

   public final void start() throws ServiceFailureException {
      if (isDebugEnabled()) {
         debug("Starting DeploymentService");
      }

      this.messageSender = CommonMessageSender.getInstance();
      this.messageReceiver = CommonMessageReceiver.getInstance();
      this.messageSender.getDelegate().setLoopbackReceiver(this.messageReceiver.getDelegate());

      try {
         this.registerDataTransferHandler(DataHandlerManager.getInstance().getHttpDataTransferHandler());
      } catch (DataTransferHandlerExistsException var2) {
         if (Debug.isServiceDebugEnabled()) {
            Debug.serviceLogger.debug("Data transfer handler for Http already registered");
         }
      }

      if (this.adminDelegate != null) {
         this.adminDelegate.start();
      }

      if (this.targetDelegate != null) {
         this.targetDelegate.start();
      }

   }

   public final void stop() throws ServiceFailureException {
      if (isDebugEnabled()) {
         debug("Stopping DeploymentService");
      }

      if (this.adminDelegate != null) {
         this.adminDelegate.stop();
      }

      if (this.targetDelegate != null) {
         this.targetDelegate.stop();
      }

   }

   public final void halt() throws ServiceFailureException {
      if (isDebugEnabled()) {
         debug("Halting DeploymentService");
      }

      if (this.adminDelegate != null) {
         this.adminDelegate.halt();
      }

      if (this.targetDelegate != null) {
         this.targetDelegate.halt();
      }

   }
}
