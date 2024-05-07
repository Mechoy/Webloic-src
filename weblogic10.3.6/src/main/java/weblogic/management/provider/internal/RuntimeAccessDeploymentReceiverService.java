package weblogic.management.provider.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import javax.xml.stream.XMLStreamException;
import weblogic.deploy.internal.targetserver.datamanagement.ConfigData;
import weblogic.deploy.internal.targetserver.datamanagement.Data;
import weblogic.deploy.internal.targetserver.datamanagement.DataUpdateRequestInfo;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentReceiverV2;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.RegistrationException;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorCreationListener;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.SpecialPropertiesProcessor;
import weblogic.management.configuration.ConfigurationExtensionMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.internal.ProductionModeHelper;
import weblogic.management.provider.MSIService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.provider.UpdateException;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class RuntimeAccessDeploymentReceiverService extends AbstractServerService implements DeploymentReceiverV2 {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private final Data dataObject;
   private ServerRuntimeMBean serverBean;
   private long currRequestId;
   private long lastRequestId;
   private Descriptor currRequestTree;
   private Descriptor proposedConfigTree;
   private boolean prepareCalled;
   private HashMap proposedExternalTrees;
   private List currChangeDescriptors;
   private String callbackHandlerId;
   private static RuntimeAccessDeploymentReceiverService singleton;
   private DeploymentService deploymentService = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String localServerName;
   private ArrayList restartRequestList = new ArrayList();
   private WeakHashMap temporaryTrees = new WeakHashMap();
   private long pendingCommitRequestId = -1L;
   private Descriptor pendingCommitRequestTree;

   public RuntimeAccessDeploymentReceiverService() {
      if (singleton != null) {
         throw new AssertionError("RuntimeAccessDeploymentReceiverService already initialized");
      } else {
         this.dataObject = new ConfigData(DomainDir.getRootDir(), this.getLockFileName(), "stage");
         singleton = this;
      }
   }

   public void start() throws ServiceFailureException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Starting Runtime Access deployment receiver service " + this);
      }

      try {
         if (!ManagementService.getPropertyService(kernelId).isAdminServer()) {
            this.registerHandler();
         }

      } catch (RegistrationException var2) {
         MSIService.getMSIService().setAdminServerAvailable(false);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Error registering deployment receiver: ", var2);
         }

      }
   }

   public void stop() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Stopping Runtime Access deployment receiver service " + this);
      }

      DeploymentService.getDeploymentService().unregisterHandler(this.callbackHandlerId);
      if (!this.temporaryTrees.isEmpty()) {
         System.gc();
         Iterator var1 = this.temporaryTrees.values().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            ManagementLogger.logTemporaryBeanTreeNotGarbageCollected(var2);
         }

      }
   }

   public void halt() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Halting Runtime Access deployment receiver service " + this);
      }

      this.commitAnyPendingRequests();
   }

   public String getHandlerIdentity() {
      return this.callbackHandlerId;
   }

   public synchronized void prepare(DeploymentContext var1) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      Descriptor var3 = var2.getDomain().getDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Handling prepare of configuration deployment request for " + var1.getDeploymentRequest() + " with context " + var1);
      }

      DeploymentRequest var4 = var1.getDeploymentRequest();
      if (this.proposedConfigTree == null && this.proposedExternalTrees == null) {
         this.notifyPrepareSuccess(var4, false);
      } else {
         try {
            Iterator var5 = var4.getDeployments(this.callbackHandlerId);
            Deployment var10 = (Deployment)var5.next();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("RuntimeDeploymentReceiver: preparing deployment " + var10);
            }

            Iterator var7 = this.currChangeDescriptors.iterator();

            while(var7 != null && var7.hasNext()) {
               ChangeDescriptor var8 = (ChangeDescriptor)var7.next();
               if (!this.isConfigChange(var8) && !this.isNonWLSChange(var8)) {
                  this.handleExternalTreePrepare(var1, var8, var10);
               }
            }

            if (this.proposedConfigTree != null) {
               var3.prepareUpdate(this.proposedConfigTree, false);
               this.prepareCalled = true;
            }

            this.notifyPrepareSuccess(var4, var1.isRestartRequired());
         } catch (Throwable var9) {
            Loggable var6 = ManagementLogger.logPrepareConfigUpdateFailedLoggable(var9);
            var6.log();
            this.notifyPrepareFailure(var4, new UpdateException(var6.getMessageBody(), var9));
         }

      }
   }

   public synchronized void commit(DeploymentContext var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Handling commit of runtime configuration deployment for " + var1.getDeploymentRequest() + " with context " + var1);
      }

      DescriptorUpdateFailedException var2 = null;
      DeploymentRequest var3 = var1.getDeploymentRequest();
      if (this.currRequestId != var3.getId()) {
         if (var3.getId() != this.lastRequestId) {
            IllegalArgumentException var13 = new IllegalArgumentException("Commit request id " + var3.getId() + " does not match outstanding request id " + this.currRequestId);
            this.notifyCommitFailure(var3, var13);
            return;
         }

         this.notifyCommitSuccess(var3);
      }

      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("RuntimeDeploymentReceiver: committing changes");
         }

         Iterator var4 = null;
         if (this.currChangeDescriptors != null) {
            var4 = this.currChangeDescriptors.iterator();
         }

         while(true) {
            while(var4 != null && var4.hasNext()) {
               ChangeDescriptor var14 = (ChangeDescriptor)var4.next();
               if (var14.getChangeOperation().equals("delete")) {
                  this.dataObject.deleteFile(var14.getChangeTarget(), var3.getId());
               } else if (this.isUpdateChange(var14) && !this.isConfigChange(var14) && !this.isNonWLSChange(var14)) {
                  String var6 = var14.getChangeTarget();

                  try {
                     this.handleExternalTreeCommit(var6, var1);
                  } catch (RuntimeException var9) {
                     if (!(var9.getCause() instanceof DescriptorUpdateFailedException)) {
                        throw var9;
                     }

                     var2 = (DescriptorUpdateFailedException)var9.getCause();
                  }
               }
            }

            RuntimeAccess var15 = ManagementService.getRuntimeAccess(kernelId);
            final Descriptor var16 = var15.getDomain().getDescriptor();
            if (this.currRequestTree != null) {
               try {
                  AuthenticatedSubject var7 = var1.getDeploymentRequest().getInitiator();
                  if (var7 != null) {
                     SecurityServiceManager.runAs(kernelId, var7, new PrivilegedAction() {
                        public Object run() {
                           try {
                              var16.activateUpdate();
                              return null;
                           } catch (DescriptorUpdateFailedException var2) {
                              throw new RuntimeException(var2);
                           }
                        }
                     });
                  } else {
                     var16.activateUpdate();
                  }
               } catch (DescriptorUpdateFailedException var10) {
                  var2 = var10;
               } catch (RuntimeException var11) {
                  if (!(var11.getCause() instanceof DescriptorUpdateFailedException)) {
                     throw var11;
                  }

                  var2 = (DescriptorUpdateFailedException)var11.getCause();
               }
            }

            this.dataObject.closeDataUpdate(var3.getId(), true);
            DescriptorInfoUtils.removeAllDeletedDescriptorInfos(var16);
            if (var2 != null) {
               throw var2;
            }

            this.notifyCommitSuccess(var3);
            this.resetState();
            break;
         }
      } catch (Throwable var12) {
         Loggable var5 = ManagementLogger.logCommitConfigUpdateFailedLoggable(var12);
         var5.log();
         this.notifyCommitFailure(var3, new UpdateException(var5.getMessageBody(), var12));
      }

   }

   private final ServerRuntimeMBean getServerBean() {
      if (this.serverBean == null) {
         this.serverBean = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      }

      return this.serverBean;
   }

   private final String getLocalServerName() {
      if (localServerName == null) {
         localServerName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      }

      return localServerName;
   }

   private boolean containsLocalServer(String[] var1) {
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (this.getLocalServerName().equals(var1[var2])) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean requiresRestart(Deployment var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.getServerBean().isRestartRequired() ? true : this.containsLocalServer(var1.getServersToBeRestarted());
      }
   }

   private boolean isConfigChange(ChangeDescriptor var1) {
      return var1.getIdentity() != null && var1.getIdentity().equals("config");
   }

   private boolean isUpdateChange(ChangeDescriptor var1) {
      return var1.getChangeOperation().equals("update");
   }

   private boolean isNonWLSChange(ChangeDescriptor var1) {
      return var1.getIdentity() != null && var1.getIdentity().equals("non-wls");
   }

   public synchronized void cancel(DeploymentContext var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Handling cancel of runtime configuration deployment for " + var1.getDeploymentRequest() + " with context " + var1);
      }

      DeploymentRequest var2 = var1.getDeploymentRequest();

      try {
         this.dataObject.cancelDataUpdate(var2.getId());
         this.removeFromRestartList(var2);
         if (this.currRequestId == var2.getId() && this.currRequestTree != null && this.prepareCalled) {
            this.currRequestTree.rollbackUpdate();
         } else if (this.pendingCommitRequestId == var2.getId()) {
            if (this.pendingCommitRequestTree != null) {
               this.pendingCommitRequestTree.rollbackUpdate();
            }

            this.resetPendingCommitState();
         }

         Iterator var3 = var2.getDeployments(this.callbackHandlerId);
         if (var3.hasNext()) {
            Deployment var4 = (Deployment)var3.next();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("RuntimeDeploymentReceiver: handle cancel " + var4);
            }
         }

         if (this.currRequestId == var2.getId()) {
            this.resetState();
         }

         DeploymentService.getDeploymentService().notifyCancelSuccess(var2.getId(), this.callbackHandlerId);
      } catch (Exception var5) {
         DeploymentService.getDeploymentService().notifyCancelFailure(var2.getId(), this.callbackHandlerId, var5);
      }

   }

   private void removeFromRestartList(DeploymentRequest var1) {
      if (var1 != null) {
         if (this.restartRequestList.remove(new Long(var1.getId())) && this.restartRequestList.isEmpty()) {
            this.getServerBean().setRestartRequired(false);
         }

      }
   }

   public void prepareCompleted(DeploymentContext var1, String var2) {
   }

   public void commitCompleted(DeploymentContext var1, String var2) {
   }

   public void commitSkipped(DeploymentContext var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Handling commit skipped request for " + var1.getDeploymentRequest() + " with context " + var1);
      }

      this.commitAnyPendingRequests();
   }

   public synchronized void updateDeploymentContext(DeploymentContext var1) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      Descriptor var3 = var2.getDomain().getDescriptor();
      this.proposedConfigTree = null;
      this.prepareCalled = false;
      this.proposedExternalTrees = null;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Handling update deployment of configuration deployment request for " + var1.getDeploymentRequest() + " with context " + var1);
      }

      DeploymentRequest var4 = var1.getDeploymentRequest();
      if (this.currRequestId == var4.getId()) {
         this.notifyUpdateSuccess(var4);
      } else {
         this.commitAnyPendingRequests();

         try {
            Iterator var5 = var4.getDeployments(this.callbackHandlerId);
            if (var5 == null || !var5.hasNext()) {
               this.notifyUpdateSuccess(var4);
               return;
            }

            Deployment var16 = (Deployment)var5.next();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("RuntimeDeploymentReceiver: update deployment context " + var16);
            }

            if (!var5.hasNext()) {
               List var7 = var16.getChangeDescriptors();
               this.currChangeDescriptors = var7;
               if (var7 == null) {
                  this.notifyUpdateFailure(var4, new IllegalArgumentException("No changes found."));
                  return;
               }

               this.downloadFiles(var4.getId(), var16);
               Iterator var8 = var7.iterator();

               while(var8 != null && var8.hasNext()) {
                  ChangeDescriptor var9 = (ChangeDescriptor)var8.next();
                  if (!this.isConfigChange(var9)) {
                     if (this.proposedExternalTrees == null) {
                        this.proposedExternalTrees = new HashMap();
                     }

                     this.handleExternalTreeLoad(var4.getId(), var9);
                  } else {
                     this.proposedConfigTree = this.handleConfigTreeLoad(var1, var9, var16);
                     DomainMBean var10 = (DomainMBean)this.proposedConfigTree.getRootBean();
                     if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
                        if (ProductionModeHelper.isProductionModePropertySet()) {
                           var10.setProductionModeEnabled(ProductionModeHelper.getProductionModeProperty());
                        } else if (var10.isProductionModeEnabled()) {
                           DescriptorHelper.setDescriptorTreeProductionMode(this.proposedConfigTree, true);
                        }
                     } else if (ProductionModeHelper.isGlobalProductionModeSet()) {
                        var10.setProductionModeEnabled(ProductionModeHelper.getGlobalProductionMode());
                     } else if (var10.isProductionModeEnabled()) {
                        DescriptorHelper.setDescriptorTreeProductionMode(this.proposedConfigTree, true);
                     }
                  }
               }

               if (this.proposedConfigTree != null) {
                  this.addTemporaryTree(this.proposedConfigTree, "updateDeploymentContext");
                  DescriptorInfoUtils.setDescriptorLoadExtensions(this.proposedConfigTree, true);
                  DomainMBean var17 = (DomainMBean)this.proposedConfigTree.getRootBean();
                  SpecialPropertiesProcessor.updateConfiguration(var17);
                  this.currRequestTree = var3;
                  var1.addContextComponent("PROPOSED_CONFIGURATION", var17);
               }

               this.setCurrentRequestId(var4.getId());
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Setting request id to " + this.currRequestId);
               }

               if (this.requiresRestart(var16)) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("RuntimeDeploymentReceiver: setting restart required for request " + var4.getId());
                  }

                  var1.setRestartRequired(true);
                  this.getServerBean().setRestartRequired(true);
                  this.restartRequestList.add(new Long(var4.getId()));
               }

               this.dataObject.commitDataUpdate();
               this.notifyUpdateSuccess(var4);
               return;
            }

            this.notifyUpdateFailure(var4, new IllegalArgumentException("Only one configuration deployment expected."));
         } catch (Throwable var14) {
            Loggable var6 = ManagementLogger.logPrepareConfigUpdateFailedLoggable(var14);
            var6.log();
            this.removeFromRestartList(var4);
            this.dataObject.cancelDataUpdate(var4.getId());
            this.notifyUpdateFailure(var4, new UpdateException(var6.getMessageBody(), var14));
            return;
         } finally {
            this.dataObject.releaseLock(var4.getId());
         }

      }
   }

   public static RuntimeAccessDeploymentReceiverService getService() {
      return singleton;
   }

   public void registerHandler() throws RegistrationException, ServiceFailureException {
      this.callbackHandlerId = "Configuration";
      ConfigurationVersion var1 = new ConfigurationVersion(true);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Registering handler for configuration with version " + var1);
      }

      this.deploymentService = DeploymentService.getDeploymentService();
      DeploymentContext var2 = this.deploymentService.registerHandler(var1, this);
      this.handleRegistrationResponse(var2);
   }

   private void handleRegistrationResponse(DeploymentContext var1) throws ServiceFailureException {
      DeploymentRequest var2 = var1.getDeploymentRequest();

      try {
         Iterator var3 = var2.getDeployments(this.callbackHandlerId);
         if (var3 != null && var3.hasNext()) {
            Deployment var11 = (Deployment)var3.next();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("RuntimeDeploymentReceiver: handle registration " + var11);
            }

            List var5 = var11.getChangeDescriptors();
            if (var5 == null) {
               return;
            }

            this.updateFiles(var2.getId(), var11);
            return;
         }
      } catch (Throwable var9) {
         Loggable var4 = ManagementLogger.logRegisterConfigUpdateFailedLoggable(var9);
         var4.log();
         this.dataObject.cancelDataUpdate(var2.getId());
         throw new ServiceFailureException(var4.getMessageBody(), var9);
      } finally {
         this.dataObject.closeDataUpdate(var2.getId(), true);
      }

   }

   private void notifyUpdateSuccess(DeploymentRequest var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Update deployment context succeeded for " + var1);
      }

      this.deploymentService.notifyContextUpdated(var1.getId(), this.callbackHandlerId);
   }

   private void notifyUpdateFailure(DeploymentRequest var1, Exception var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Update deployment context failed for " + var1 + " with exception:", var2);
      }

      this.deploymentService.notifyContextUpdateFailed(var1.getId(), this.callbackHandlerId, var2);
   }

   private void notifyPrepareSuccess(DeploymentRequest var1, boolean var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Prepare succeeded for " + var1);
      }

      boolean var3 = false;
      if (var2) {
         Iterator var4 = var1.getDeployments();
         var4.next();
         if (var4.hasNext()) {
            this.savePendingCommitState();
            var3 = true;
         }
      }

      this.deploymentService.notifyPrepareSuccess(var1.getId(), this.callbackHandlerId);
      if (var2) {
         this.deploymentService.notifyStatusUpdate(var1.getId(), this.callbackHandlerId, "COMMIT_PENDING");
         if (!var3) {
            this.dataObject.closeDataUpdate(this.currRequestId, true);
         }

         this.resetState();
      }

   }

   private void notifyPrepareFailure(DeploymentRequest var1, Exception var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Prepare failed for " + var1 + " with exception:", var2);
      }

      this.removeFromRestartList(var1);
      this.deploymentService.notifyPrepareFailure(var1.getId(), this.callbackHandlerId, var2);
   }

   private void notifyCommitSuccess(DeploymentRequest var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Commit succeeded for " + var1);
      }

      this.deploymentService.notifyCommitSuccess(var1.getId(), this.callbackHandlerId);
   }

   private void notifyCommitFailure(DeploymentRequest var1, Exception var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Commit failed for " + var1 + " with exception:", var2);
      }

      this.deploymentService.notifyCommitFailure(var1.getId(), this.callbackHandlerId, var2);
   }

   private void resetState() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Resetting state for id: " + this.currRequestId);
      }

      this.lastRequestId = this.currRequestId;
      this.setCurrentRequestId(-1L);
      this.currRequestTree = null;
      this.currChangeDescriptors = null;
      this.proposedConfigTree = null;
      this.proposedExternalTrees = null;
      this.prepareCalled = false;
   }

   private void setCurrentRequestId(long var1) {
      this.currRequestId = var1;
   }

   private String getLockFileName() {
      return "config/config.lok";
   }

   private void updateFiles(long var1, Deployment var3) throws ManagementException {
      try {
         this.downloadFiles(var1, var3);
         this.dataObject.commitDataUpdate();
      } catch (Throwable var8) {
         this.dataObject.cancelDataUpdate(var1);
         if (var8 instanceof ManagementException) {
            throw (ManagementException)var8;
         }

         throw new ManagementException(var8);
      } finally {
         this.dataObject.closeDataUpdate(var1, true);
      }

   }

   private void downloadFiles(final long var1, Deployment var3) throws ManagementException {
      Iterator var4 = var3.getChangeDescriptors().iterator();
      if (var4 != null && var4.hasNext()) {
         final ArrayList var5 = new ArrayList();

         while(var4.hasNext()) {
            ChangeDescriptor var6 = (ChangeDescriptor)var4.next();
            String var7 = var6.getChangeSource();
            var5.add(var7);
         }

         this.dataObject.initDataUpdate(new DataUpdateRequestInfo() {
            public List getDeltaFiles() {
               return var5;
            }

            public long getRequestId() {
               return var1;
            }

            public boolean isStatic() {
               return false;
            }

            public boolean isDelete() {
               return false;
            }

            public boolean isPlanUpdate() {
               return false;
            }
         });
         this.dataObject.prepareDataUpdate(var3.getDataTransferHandlerType());
      }
   }

   private Descriptor handleConfigTreeLoad(DeploymentContext var1, ChangeDescriptor var2, Deployment var3) throws IOException, DescriptorUpdateRejectedException {
      Descriptor var4 = null;
      String var5 = var2.getChangeTarget();
      String var6 = var2.getChangeOperation();
      FileInputStream var7 = null;

      IOException var11;
      try {
         File var8 = this.dataObject.getFileFor(var1.getDeploymentRequest().getId(), var5);
         var7 = new FileInputStream(var8);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("RuntimeDeploymentReceiver: processing change, target: " + var5);
         }

         if (var6.equals("add") || var6.equals("update")) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("RuntimeDeploymentReceiver: loading tree from stream, uri: " + var5 + " deployment: " + var3);
            }

            DescriptorManager var30 = DescriptorManagerHelper.getDescriptorManager(false);
            var30.setDescriptorCreationListener(new RuntimeAccessDescriptorCreationListener());

            try {
               ArrayList var31 = new ArrayList();
               var4 = var30.createDescriptor(new ConfigReader(var7), var31, true);
               EditAccessImpl.checkErrors(var5, var31);
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("RuntimeDeploymentReceiver: created proposed tree from IS : ");
               }

               return var4;
            } catch (XMLStreamException var27) {
               var11 = new IOException(var27.getMessage());
               var11.initCause(var27);
               throw var11;
            } finally {
               var30.setDescriptorCreationListener((DescriptorCreationListener)null);
            }
         }

         DeploymentRequest var9 = var1.getDeploymentRequest();
         IllegalArgumentException var10 = new IllegalArgumentException("Only update or add operations supported.");
         this.notifyUpdateFailure(var9, var10);
         var11 = null;
      } finally {
         if (var7 != null) {
            try {
               var7.close();
            } catch (IOException var26) {
            }
         }

      }

      return var11;
   }

   private void handleExternalTreeLoad(long var1, ChangeDescriptor var3) throws IOException, DescriptorUpdateRejectedException {
      String var4 = var3.getChangeTarget();
      String var5 = var3.getChangeOperation();
      FileInputStream var6 = null;

      try {
         var6 = new FileInputStream(this.dataObject.getFileFor(var1, var4));
         String var7;
         if (debugLogger.isDebugEnabled()) {
            var7 = "RuntimeDeploymentReceiver: processing external tree load, target: " + var4 + " oper: " + var5;
            debugLogger.debug(var7);
         }

         var7 = null;
         RuntimeAccess var19 = ManagementService.getRuntimeAccess(kernelId);
         Descriptor var18 = var19.getDomain().getDescriptor();
         if (var5.equals("update")) {
            Descriptor var9 = null;
            DescriptorInfo var10 = this.findDescriptorInfoForFileName(var18, var4);
            if (var10 != null) {
               DescriptorManager var11 = var10.getDescriptorManager();
               ArrayList var12 = new ArrayList();
               var9 = var11.createDescriptor(new ConfigReader(var6));
               EditAccessImpl.checkErrors(var4, var12);
               this.proposedExternalTrees.put(var4, var9);
               this.addTemporaryTree(var9, "updateDeploymentContext." + var4);
               if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
                  if (ProductionModeHelper.isProductionModePropertySet() || var19.getDomain().isProductionModeEnabled()) {
                     DescriptorHelper.setDescriptorTreeProductionMode(var9, ProductionModeHelper.getProductionModeProperty());
                  }
               } else if (ProductionModeHelper.isGlobalProductionModeSet() || var19.getDomain().isProductionModeEnabled()) {
                  DescriptorHelper.setDescriptorTreeProductionMode(var9, ProductionModeHelper.getGlobalProductionMode());
               }
            }
         }
      } catch (XMLStreamException var16) {
         IOException var8 = new IOException(var16.getMessage());
         var8.initCause(var16);
         throw var8;
      } finally {
         if (var6 != null) {
            var6.close();
         }

      }

   }

   private void handleExternalTreePrepare(DeploymentContext var1, ChangeDescriptor var2, Deployment var3) throws IOException, DescriptorUpdateRejectedException {
      String var4 = var2.getChangeTarget();
      String var5 = var2.getChangeOperation();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("RuntimeDeploymentReceiver: processing external prepare, target: " + var4 + " oper: " + var5);
      }

      RuntimeAccess var6 = ManagementService.getRuntimeAccess(kernelId);
      Descriptor var7 = var6.getDomain().getDescriptor();
      if (var5.equals("update")) {
         DescriptorInfo var8 = this.findDescriptorInfoForFileName(var7, var4);
         if (var8 != null) {
            Descriptor var9 = (Descriptor)this.proposedExternalTrees.get(var4);
            if (var9 != null) {
               Descriptor var10 = var8.getDescriptor();
               var10.prepareUpdate(var9, false);
            }
         }
      } else if (var5.equals("add")) {
         DescriptorManager var18 = DescriptorManagerHelper.getDescriptorManager(false);
         FileInputStream var19 = new FileInputStream(new File(DomainDir.getRootDir(), var4));

         try {
            ArrayList var17 = new ArrayList();
            var18.createDescriptor(new ConfigReader(var19), var17, true);
            EditAccessImpl.checkErrors(var4, var17);
         } catch (XMLStreamException var15) {
            IOException var11 = new IOException(var15.getMessage());
            var11.initCause(var15);
            throw var11;
         } finally {
            if (var19 != null) {
               var19.close();
            }

         }
      }

   }

   private void handleExternalTreeCommit(String var1, DeploymentContext var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("RuntimeDeploymentReceiver: processing external commit, target: " + var1);
      }

      RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelId);
      Descriptor var4 = var3.getDomain().getDescriptor();
      AuthenticatedSubject var5 = var2.getDeploymentRequest().getInitiator();
      DescriptorInfo var6 = this.findDescriptorInfoForFileName(var4, var1);
      if (var6 != null) {
         final Descriptor var7 = var6.getDescriptor();

         try {
            if (var5 != null) {
               SecurityServiceManager.runAs(kernelId, var5, new PrivilegedAction() {
                  public Object run() {
                     try {
                        var7.activateUpdate();
                        return null;
                     } catch (DescriptorUpdateFailedException var2) {
                        throw new RuntimeException(var2);
                     }
                  }
               });
            } else {
               var7.activateUpdate();
            }
         } catch (DescriptorUpdateFailedException var9) {
            throw new RuntimeException(var9);
         }
      }

   }

   private DescriptorInfo findDescriptorInfoForFileName(Descriptor var1, String var2) {
      Iterator var3 = DescriptorInfoUtils.getDescriptorInfos(var1);

      while(var3 != null && var3.hasNext()) {
         DescriptorInfo var4 = (DescriptorInfo)var3.next();
         ConfigurationExtensionMBean var5 = var4.getConfigurationExtension();
         String var6 = (new File(var2)).getPath();
         String var7 = (new File(var5.getDescriptorFileName())).getPath();
         if (var6.endsWith(var7)) {
            return var4;
         }
      }

      return null;
   }

   private void addTemporaryTree(Object var1, String var2) {
      this.temporaryTrees.put(var1, var2 + "(" + new Date() + ")");
   }

   private void savePendingCommitState() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Saving pending commit state info: " + this.currRequestId);
      }

      this.pendingCommitRequestId = this.currRequestId;
      this.pendingCommitRequestTree = this.currRequestTree;
   }

   private void resetPendingCommitState() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Reset pending commit state info: " + this.pendingCommitRequestId);
      }

      this.pendingCommitRequestId = -1L;
      this.pendingCommitRequestTree = null;
   }

   public void commitAnyPendingRequests() {
      if (this.pendingCommitRequestId != -1L) {
         this.dataObject.closeDataUpdate(this.pendingCommitRequestId, true);
         this.resetPendingCommitState();
      }

   }

   private class RuntimeAccessDescriptorCreationListener implements DescriptorCreationListener {
      private RuntimeAccessDescriptorCreationListener() {
      }

      public void descriptorCreated(Descriptor var1) {
         DescriptorInfoUtils.setDescriptorLoadExtensions(var1, false);
      }

      // $FF: synthetic method
      RuntimeAccessDescriptorCreationListener(Object var2) {
         this();
      }
   }
}
