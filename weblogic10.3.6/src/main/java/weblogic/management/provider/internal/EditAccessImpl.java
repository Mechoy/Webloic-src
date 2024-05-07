package weblogic.management.provider.internal;

import com.bea.xml.XmlValidationError;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.xml.stream.XMLStreamException;
import weblogic.version;
import weblogic.deploy.internal.Deployment;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.DeploymentException;
import weblogic.deploy.service.DeploymentProvider;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.DeploymentServiceCallbackHandlerV2;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.InvalidCreateChangeDescriptorException;
import weblogic.deploy.service.RequiresRestartFailureDescription;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.ManagementRuntimeException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.ConfigurationExtensionMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.internal.PendingDirectoryManager;
import weblogic.management.internal.Utils;
import weblogic.management.provider.ActivateTask;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditChangesValidationException;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.EditSaveChangesFailedException;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.management.upgrade.ConfigFileHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class EditAccessImpl implements EditAccess, DeploymentServiceCallbackHandlerV2 {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static final String SCHEMA_VALIDATION_ENABLED_PROP = "weblogic.configuration.schemaValidationEnabled";
   private static final boolean schemaValidationEnabled = getBooleanProperty("weblogic.configuration.schemaValidationEnabled", true);
   private static int CANCEL = 1;
   private static int DEPLOY = 2;
   private static int COMMIT = 3;
   private EditLockManager lockMgr;
   private PendingDirectoryManager pendingDirMgr = PendingDirectoryManager.getInstance();
   private Descriptor editTree;
   private DomainMBean editDomainMBean;
   private Descriptor currentTree;
   private DomainMBean currentDomainMBean;
   private static final AuthenticatedSubject kernelIdentity = obtainKernelIdentity();
   private Map activationTasksByRequest = Collections.synchronizedMap(new HashMap());
   private WeakHashMap oldActivationTasks = new WeakHashMap();
   private boolean preparing = false;
   private long preparingTimeout;
   private long preparingId;
   private String rootDirectoryPrefix;
   private boolean pendingChange = false;
   private WeakHashMap temporaryTrees = new WeakHashMap();

   private EditAccessImpl() {
      File[] var1 = this.pendingDirMgr.getAllFiles();
      if (var1 != null && var1.length > 0) {
         boolean var2 = DescriptorHelper.recoverPendingDirectory(var1);
         if (var2) {
            this.setPendingChange(true);
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Changes are pending in pending directory");
            }
         }
      }

      this.lockMgr = new EditLockManager();
      this.rootDirectoryPrefix = (new File(DomainDir.getRootDir())).getPath() + File.separator;
      if (ManagementService.getPropertyService(kernelIdentity).isAdminServer()) {
         try {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Registering with deployment service");
            }

            ConfigurationVersion var4 = new ConfigurationVersion(true);
            DeploymentService.getDeploymentService().register(var4, this);
            if (ManagementService.getPropertyService(kernelIdentity).isAdminServer()) {
               RuntimeAccessDeploymentReceiverService.getService().registerHandler();
            }
         } catch (Exception var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception occurred registering with deploy service", var3);
            }

            ManagementLogger.logDeploymentRegistrationFailed(var3);
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Created edit access impl " + this);
         }

      }
   }

   public static void initialize() {
      EditAccessImpl var0 = new EditAccessImpl();
      ManagementServiceRestricted.setEditAccess(var0);
   }

   public DomainMBean getDomainBean() throws EditNotEditorException, EditFailedException {
      this.checkEditLock();
      if (this.editDomainMBean == null) {
         this.ensureBeanTreeLoaded();
      }

      return this.editDomainMBean;
   }

   public DomainMBean getCurrentDomainBean() throws EditFailedException {
      if (this.currentDomainMBean == null) {
         this.ensureBeanTreeLoaded();
      }

      return this.currentDomainMBean;
   }

   private AuthenticatedSubject acquireLock(int var1, int var2, boolean var3) throws EditWaitTimedOutException, EditFailedException {
      AuthenticatedSubject var4 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Starting edit session for " + var4);
      }

      boolean var5 = this.lockMgr.getEditLock(var4, var1, var2, var3);
      if (var5 && this.editDomainMBean != null) {
         try {
            this.undoUnsavedChanges();
         } catch (EditNotEditorException var7) {
            throw new AssertionError("Should have edit lock");
         }
      }

      return var4;
   }

   public DomainMBean startEdit(int var1, int var2) throws EditWaitTimedOutException, EditFailedException {
      return this.startEdit(var1, var2, false);
   }

   public DomainMBean startEdit(int var1, int var2, boolean var3) throws EditWaitTimedOutException, EditFailedException {
      this.acquireLock(var1, var2, var3);
      if (this.editDomainMBean == null) {
         this.ensureBeanTreeLoaded();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Loaded bean tree");
         }

         String var4 = version.getReleaseBuildVersion();
         this.editDomainMBean.setConfigurationVersion(var4);
      }

      return this.editDomainMBean;
   }

   public void stopEdit() throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Stopping edit session for " + var1);
      }

      try {
         this.undoUnsavedChanges();
      } catch (EditNotEditorException var3) {
         throw new AssertionError("Should have edit lock");
      }

      this.lockMgr.releaseEditLock(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Stopped edit session");
      }

   }

   public synchronized void cancelEdit() throws EditFailedException {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Canceling edit session for " + var1);
      }

      if (this.isPreparing()) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Cancel outstanding requests");
         }

         ActivateTaskImpl var2 = this.lookupTask(this.preparingId);
         this.resetPreparingInfo();
         this.rollbackCurrent();
         if (var2 != null) {
            var2.setError(new EditFailedException("Request canceled by cancelEdit operation"));
            var2.setState(5);
         }
      }

      this.lockMgr.cancelEditLock(var1);

      try {
         this.undoUnsavedChanges();
      } catch (EditNotEditorException var3) {
         throw new AssertionError("Should have edit lock");
      } catch (EditFailedException var4) {
         this.lockMgr.releaseEditLock(var1);
         throw var4;
      }

      this.lockMgr.releaseEditLock(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Cancelled edit session");
      }

   }

   public synchronized Iterator getUnsavedChanges() throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Getting iterator of unsaved changes for " + var1);
      }

      if (this.editTree == null) {
         throw new AssertionError("Edit bean tree is null");
      } else {
         Descriptor var2 = null;

         try {
            ArrayList var3 = new ArrayList();
            boolean var4 = this.areAnyExternalTreesModified(this.editTree);
            if (!this.editTree.isModified() && !var4) {
               return var3.iterator();
            } else {
               var2 = this.loadBeanTreeFromPending();
               this.addTemporaryTree(var2, "getUnsavedChanges");
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Loaded pending tree " + var2 + " compute diff with edit tree " + this.editTree);
               }

               DescriptorDiff var5 = var2.computeDiff(this.editTree);
               Iterator var6;
               if (var5 != null) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Returning iterator of unsaved changes");
                  }

                  var6 = var5.iterator();

                  while(var6.hasNext()) {
                     Object var7 = var6.next();
                     var3.add(var7);
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Beanupdate event " + var7);
                     }
                  }
               }

               if (!var4) {
                  return var3.iterator();
               } else {
                  var6 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

                  while(var6 != null && var6.hasNext()) {
                     DescriptorInfo var14 = (DescriptorInfo)var6.next();
                     Descriptor var8 = var14.getDescriptor();
                     if (var8.isModified()) {
                        Descriptor var9 = this.loadExternalBeanTree(var14, true, false);
                        this.addTemporaryTree(var9, "getUnsavedChanges." + var14.getDescriptorClass());
                        DescriptorDiff var10 = var9.computeDiff(var8);
                        if (var10 != null) {
                           Iterator var11 = var10.iterator();

                           while(var11.hasNext()) {
                              var3.add(var11.next());
                           }
                        }
                     }
                  }

                  return var3.iterator();
               }
            }
         } catch (EditFailedException var12) {
            throw var12;
         } catch (Exception var13) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in computing diff: ", var13);
            }

            throw new EditFailedException(var13);
         }
      }
   }

   public synchronized void undoUnsavedChanges() throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Undoing unsaved changes for " + var1);
      }

      if (this.editTree != null) {
         Descriptor var2 = null;

         try {
            if (this.editTree.isModified()) {
               var2 = this.loadBeanTreeFromPending();
               this.addTemporaryTree(var2, "undoUnsavedChanges");
               this.editTree.prepareUpdate(var2, false);
               this.editTree.activateUpdate();
            }

            Iterator var3 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

            while(var3 != null && var3.hasNext()) {
               DescriptorInfo var4 = (DescriptorInfo)var3.next();
               Descriptor var5 = var4.getDescriptor();
               if (var5.isModified()) {
                  Descriptor var6 = this.loadExternalBeanTree(var4, true, true);
                  if (var6 != null) {
                     this.addTemporaryTree(var6, "undoUnsavedChanges." + var4.getDescriptorClass());
                     var5.prepareUpdate(var6, false);
                     var5.activateUpdate();
                  }
               }
            }

            DescriptorInfoUtils.removeAllDeletedDescriptorInfos(this.editTree);
         } catch (IOException var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception loading tree ", var7);
            }

            throw new EditFailedException(var7);
         } catch (EditFailedException var8) {
            throw var8;
         } catch (ManagementException var9) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception running processors ", var9);
            }

            throw new EditFailedException(var9);
         } catch (DescriptorUpdateRejectedException var10) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in prepare/activate: ", var10);
            }

            throw new EditFailedException(var10);
         } catch (DescriptorUpdateFailedException var11) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception activating in undo unsaved ", var11);
            }

            throw new EditFailedException(var11);
         }

         this.removePendingUpdateTasks();
      }
   }

   private void removePendingUpdateTasks() {
      DeploymentManager var1 = DeploymentManager.getInstance(kernelIdentity);
      Collection var2 = var1.getPendingDeploymentsForEditLockOwner();
      if (var2 != null && var2.size() > 0) {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Deployment var5 = (Deployment)var4.next();
            DeploymentTaskRuntime var6 = var5.getDeploymentTaskRuntime();
            if (var6 != null && var6.getState() == 0) {
               var3.add(var6.getId());
               var6.remove();
            }
         }

         var1.removeDeploymentsForTasks(var3);
      }

   }

   private void backupConfig() {
      try {
         ConfigBackup.saveVersioned();
      } catch (IOException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception backing up config ", var2);
         }
      }

   }

   public String getEditor() {
      Object var1 = this.lockMgr.getLockOwner();
      if (var1 == null) {
         return null;
      } else {
         return var1 instanceof AuthenticatedSubject ? SubjectUtils.getUsername((AuthenticatedSubject)var1) : var1.toString();
      }
   }

   public boolean isEditor() {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
      return this.lockMgr.isLockOwner(var1);
   }

   public long getEditorStartTime() {
      return this.lockMgr.getLockAcquisitionTime();
   }

   public long getEditorExpirationTime() {
      return this.lockMgr.getLockExpirationTime();
   }

   public boolean isEditorExclusive() {
      return this.lockMgr.isLockExclusive();
   }

   public synchronized void validateChanges() throws EditNotEditorException, EditChangesValidationException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Validating changes for " + var1);
      }

      if (this.editTree == null) {
         throw new AssertionError("Edit bean tree is null");
      } else {
         try {
            this.editTree.validate();
            Iterator var2 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

            while(var2 != null && var2.hasNext()) {
               DescriptorInfo var3 = (DescriptorInfo)var2.next();
               Descriptor var4 = var3.getDescriptor();
               if (var4 != this.editTree && var4.isModified()) {
                  var4.validate();
               }
            }

         } catch (DescriptorValidateException var5) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in validation: ", var5);
            }

            throw new EditChangesValidationException(var5);
         }
      }
   }

   public synchronized void reload() throws EditNotEditorException, EditChangesValidationException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Validating changes for " + var1);
      }

      if (this.editTree != null) {
         Descriptor var2 = null;

         try {
            var2 = this.loadBeanTreeFromPending();
            this.addTemporaryTree(var2, "reload");
            this.editTree.prepareUpdate(var2, false);
            this.editTree.activateUpdate();
            Iterator var3 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

            while(true) {
               if (var3 == null || !var3.hasNext()) {
                  DescriptorInfoUtils.removeAllDeletedDescriptorInfos(this.editTree);
                  break;
               }

               DescriptorInfo var4 = (DescriptorInfo)var3.next();
               Descriptor var5 = var4.getDescriptor();
               Descriptor var6 = this.loadExternalBeanTree(var4, true, true);
               if (var6 != null) {
                  this.addTemporaryTree(var6, "reload." + var4.getDescriptorClass());
                  var5.prepareUpdate(var6, false);
                  var5.activateUpdate();
               }
            }
         } catch (IOException var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception loading tree ", var7);
            }

            throw new EditChangesValidationException(var7);
         } catch (EditFailedException var8) {
            throw new EditChangesValidationException(var8);
         } catch (ManagementException var9) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception running processors ", var9);
            }

            throw new EditChangesValidationException(var9);
         } catch (DescriptorUpdateRejectedException var10) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in prepare/activate: ", var10);
            }

            throw new EditChangesValidationException(var10);
         } catch (DescriptorUpdateFailedException var11) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception activating in undo unsaved ", var11);
            }

            throw new EditChangesValidationException(var11);
         }

         this.removePendingUpdateTasks();
      }
   }

   public synchronized void saveChanges() throws EditNotEditorException, EditSaveChangesFailedException, EditChangesValidationException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Saving changes for " + var1);
      }

      if (this.editTree == null) {
         throw new AssertionError("Edit bean tree is null");
      } else {
         this.validateChanges();
         this.validatePreparingInfo();
         if (this.isPreparing()) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Attempted to save changes while preparing: ");
            }

            throw new EditSaveChangesFailedException("Can not save changes while prepare changes are still in progress");
         } else {
            try {
               boolean var2 = DescriptorHelper.saveDescriptorTree(this.editTree, true, (String)null);
               if (var2) {
                  this.setPendingChange(true);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Saved tree, changes are pending in pending directory");
                  }
               }

               Iterator var3 = DescriptorInfoUtils.getDeletedDescriptorInfos(this.editTree);

               while(var3 != null && var3.hasNext()) {
                  DescriptorInfo var4 = (DescriptorInfo)var3.next();
                  String var5 = var4.getConfigurationExtension().getDescriptorFileName();
                  this.pendingDirMgr.deleteFile(var5);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Deleting from pending directory - deleted descriptor file " + var5);
                  }
               }

               DescriptorInfoUtils.removeAllDeletedDescriptorInfos(this.editTree);
            } catch (IOException var6) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception in write: ", var6);
               }

               throw new EditSaveChangesFailedException(var6);
            }
         }
      }
   }

   public ActivateTask activateChanges(long var1) throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var3 = this.checkEditLock();
      long var4 = 0L;
      DeploymentRequestTaskRuntimeMBean var6 = null;
      synchronized(this) {
         this.validatePreparingInfo();
         if (this.isPreparing()) {
            throw new EditFailedException("Unable to start new activation while preparing");
         }

         if (var1 == Long.MAX_VALUE) {
            var4 = Long.MAX_VALUE;
         } else {
            var4 = System.currentTimeMillis() + var1;
         }

         this.backupConfig();
      }

      try {
         DeploymentRequest var7 = null;

         try {
            var7 = DeploymentService.getDeploymentService().createDeploymentRequest();
         } catch (ManagementException var18) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Failed to create deployment request with error: ", var18);
            }

            throw new EditFailedException(var18);
         }

         DescriptorDiff var8 = this.currentTree.prepareUpdateDiff(this.editTree, false);
         Iterator var9 = var8.iterator();
         ConfigurationDeployment var10 = this.getConfigDeploymentPending(var9);
         if (var10 != null) {
            var7.addDeployment(var10);
         }

         Set var11 = DeploymentService.getDeploymentService().getRegisteredDeploymentProviders();
         Iterator var12 = var11.iterator();
         ConfigurationContextImpl var13 = new ConfigurationContextImpl(var7);
         var13.addContextComponent("beanUpdateDescriptorDiffId", var8);
         Map var14 = this.getExternalDescriptorDiffMap(false);
         var13.addContextComponent("externalDescritorDiffId", var14);
         ArrayList var15 = new ArrayList();
         this.combineDiffs(var15, var8, var14);

         while(var12 != null && var12.hasNext()) {
            DeploymentProvider var16 = (DeploymentProvider)var12.next();
            var16.addDeploymentsTo(var7, var13);
         }

         var7.setInitiator(var3);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Deploying request : " + var7);
         }

         boolean var21 = false;
         if (var10 != null) {
            var21 = true;
         }

         ActivateTaskImpl var17 = this.createActivationTask(var7.getId(), var1, var4, var21, var15, var3, this.lockMgr, var10);
         var7.setTimeoutInterval(var1);
         if (var7.isStartControlEnabled()) {
            var7.registerFailureListener(this);
         } else {
            if (var10 == null && !var7.getDeployments().hasNext()) {
               ((AdminRequestImpl)var7).reset();
               this.lockMgr.releaseEditLock(var3);
               return var17;
            }

            var6 = var7.getTaskRuntime();
            var6.start();
            if (debugLogger.isDebugEnabled()) {
               if (var6 != null) {
                  debugLogger.debug("Started deployment request with id " + var6.getTaskId() + " isComplete " + var6.isComplete());
               } else {
                  debugLogger.debug("Started deployment request task runtime is null");
               }
            }
         }

         if (var6 != null) {
            var17.setDeploymentRequestTaskRuntimeMBean(var6);
         }

         return var17;
      } catch (Exception var19) {
         this.resetPreparingInfo();
         this.rollbackCurrent();
         throw new EditFailedException(var19);
      }
   }

   public ActivateTask activateChangesAndWaitForCompletion(long var1) throws EditNotEditorException, EditFailedException {
      ActivateTaskImpl var3 = (ActivateTaskImpl)this.activateChanges(var1);
      if (var3 != null) {
         var3.waitForTaskCompletion();
         if (var3.getState() == 5 && var3.getError() == null) {
            var3.setError(new EditFailedException("Activate failed with unknown exception"));
         }
      }

      return var3;
   }

   public synchronized void undoUnactivatedChanges() throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Undoing unactivated changes for " + var1);
      }

      if (this.editTree == null) {
         throw new AssertionError("Edit bean tree is null");
      } else {
         this.validatePreparingInfo();
         if (this.isPreparing()) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Attempted to undo activated changes while perpaaring: ");
            }

            throw new EditFailedException("Can not undo unactivated changes while an activate changes operation is still in progress");
         } else {
            try {
               if (!this.deletePendingDirectory()) {
                  throw new EditFailedException("Can not delete all the files in the pending directory");
               } else {
                  this.setPendingChange(false);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Undo unactivated tree, no changes are pending in pending directory");
                     debugLogger.debug("Computing diff of unactivated changes");
                     DescriptorDiff var2 = this.editTree.computeDiff(this.currentTree);
                     if (var2 != null) {
                        Iterator var3 = var2.iterator();

                        while(var3.hasNext()) {
                           debugLogger.debug("Beanupdate event " + var3.next());
                        }
                     }
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Preparing revert of edit tree");
                  }

                  this.editTree.prepareUpdate(this.currentTree, false);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Committing revert of edit tree");
                  }

                  this.editTree.activateUpdate();
                  Iterator var11 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

                  while(var11 != null && var11.hasNext()) {
                     DescriptorInfo var12 = (DescriptorInfo)var11.next();
                     Descriptor var4 = var12.getDescriptor();
                     Descriptor var5 = this.loadExternalBeanTree(var12, true, true);
                     if (var5 != null) {
                        this.addTemporaryTree(var5, "undoUnactivatedChanges." + var12.getDescriptorClass());
                        var4.prepareUpdate(var5, false);
                        var4.activateUpdate();
                     }
                  }

                  DescriptorInfoUtils.removeAllDeletedDescriptorInfos(this.editTree);
               }
            } catch (IOException var6) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception loading tree ", var6);
               }

               throw new EditFailedException(var6);
            } catch (EditFailedException var7) {
               throw var7;
            } catch (ManagementException var8) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception running processors ", var8);
               }

               throw new EditFailedException(var8);
            } catch (DescriptorUpdateRejectedException var9) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception in prepare/activate: ", var9);
               }

               throw new EditFailedException(var9);
            } catch (DescriptorUpdateFailedException var10) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception activating in undo unactivated ", var10);
               }

               throw new EditFailedException(var10);
            }
         }
      }
   }

   public synchronized Iterator getUnactivatedChanges() throws EditNotEditorException, EditFailedException {
      AuthenticatedSubject var1 = this.checkEditLock();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Getting unactivated changes for " + var1);
      }

      ArrayList var2 = new ArrayList();
      if (!this.isPendingChange() && !this.isModified()) {
         return var2.iterator();
      } else {
         DescriptorDiff var3 = this.getUnactivatedChangesDiff();

         try {
            Map var4 = this.getExternalDescriptorDiffMap(true);
            this.combineDiffs(var2, var3, var4);
            return var2.iterator();
         } catch (Exception var5) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in computing diff: ", var5);
            }

            throw new EditFailedException(var5);
         }
      }
   }

   private void combineDiffs(ArrayList var1, DescriptorDiff var2, Map var3) {
      Iterator var4;
      if (var2 != null) {
         var4 = var2.iterator();

         while(var4.hasNext()) {
            var1.add(var4.next());
         }
      }

      if (!var3.isEmpty()) {
         var4 = var3.values().iterator();

         while(var4.hasNext()) {
            Iterator var5 = ((ArrayList)var4.next()).iterator();

            while(var5.hasNext()) {
               var1.add(var5.next());
            }
         }
      }

   }

   private Map getExternalDescriptorDiffMap(boolean var1) throws EditFailedException, IOException {
      HashMap var2 = new HashMap();
      Iterator var3 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

      label51:
      while(var3 != null && var3.hasNext()) {
         DescriptorInfo var4 = (DescriptorInfo)var3.next();
         Descriptor var5 = var4.getDescriptor();
         Descriptor var6 = this.getExternalCurrentTree(var4);
         if (var6 == null) {
            var6 = this.loadExternalBeanTree(var4, false, !var1);
         }

         if (var6 != null) {
            DescriptorDiff var7 = var6.computeDiff(var5);
            if (var7 != null) {
               ArrayList var8 = new ArrayList();
               Iterator var9 = var7.iterator();

               while(true) {
                  BeanUpdateEvent var10;
                  do {
                     if (!var9.hasNext()) {
                        var2.put(var4.getDescriptorBean(), var8);
                        continue label51;
                     }

                     var10 = (BeanUpdateEvent)var9.next();
                     var8.add(var10);
                  } while(!debugLogger.isDebugEnabled());

                  debugLogger.debug("Added external diff for bean: " + var10.getSource() + " in external desc for: " + var4.getDescriptorBean());
                  BeanUpdateEvent.PropertyUpdate[] var11 = var10.getUpdateList();

                  for(int var12 = 0; var12 < var11.length; ++var12) {
                     debugLogger.debug("diff[" + var12 + "], updateType: " + var11[var12].getUpdateType() + ", propertyName: " + var11[var12].getPropertyName() + ", isDynamic: " + var11[var12].isDynamic());
                  }
               }
            }
         }
      }

      return var2;
   }

   public boolean isModified() {
      if (this.editTree == null) {
         return false;
      } else {
         return this.editTree.isModified() || this.areAnyExternalTreesModified(this.editTree);
      }
   }

   public boolean isPendingChange() {
      return this.pendingChange;
   }

   public synchronized void setPendingChange(boolean var1) {
      this.pendingChange = var1;
   }

   private synchronized DescriptorDiff getUnactivatedChangesDiff() throws EditFailedException {
      if (this.editTree == null) {
         throw new AssertionError("Edit bean tree is null");
      } else {
         try {
            DescriptorDiff var1 = this.currentTree.computeDiff(this.editTree);
            if (var1 == null) {
               return null;
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Returning iterator of unactivated changes");
                  Iterator var2 = var1.iterator();

                  while(var2.hasNext()) {
                     debugLogger.debug("Beanupdate event " + var2.next());
                  }
               }

               return var1;
            }
         } catch (Exception var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in diff: ", var3);
            }

            throw new EditFailedException(var3);
         }
      }
   }

   public DomainMBean getDomainBeanWithoutLock() throws EditFailedException {
      if (this.editDomainMBean == null) {
         this.ensureBeanTreeLoaded();
      }

      return this.editDomainMBean;
   }

   public synchronized boolean isDomainBeanTreeLoaded() {
      return this.editDomainMBean != null;
   }

   EditLockManager getEditLockManager() {
      return this.lockMgr;
   }

   public String getHandlerIdentity() {
      return "Configuration";
   }

   public weblogic.deploy.service.Deployment[] getDeployments(Version var1, Version var2, String var3) {
      Object var4 = null;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Get deployments for server " + var3 + " from version " + var1 + " to version " + var2);
      }

      if (var1 != null && var1.equals(var2)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Get deployments - from and to version are equal.");
         }

         weblogic.deploy.service.Deployment[] var7 = new weblogic.deploy.service.Deployment[0];
         return var7;
      } else {
         try {
            ConfigurationDeployment var5 = this.getConfigDeploymentCurrent(var1, var2);
            if (var5 == null) {
               var4 = new weblogic.deploy.service.Deployment[0];
            } else {
               var4 = new ConfigurationDeployment[]{var5};
            }

            return (weblogic.deploy.service.Deployment[])var4;
         } catch (InvalidCreateChangeDescriptorException var6) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception getting current config deployment ", var6);
            }

            throw new ManagementRuntimeException(var6);
         }
      }
   }

   public synchronized void deploySucceeded(long var1, FailureDescription[] var3) {
      try {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (var3 != null) {
            for(int var6 = 0; var6 < var3.length; ++var6) {
               FailureDescription var7 = var3[var6];
               if (var7 instanceof RequiresRestartFailureDescription) {
                  var4.add(var7);
               } else {
                  var5.add(var7);
               }
            }
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Edit Access deploy succeeded for identifier " + var1 + " with " + var5.size() + " deferred deployments and " + var4.size() + " requires restart deployments");
         }

         this.prepareCompleted(var1);
         ActivateTaskImpl var9 = this.lookupTask(var1);
         if (var9 == null) {
            return;
         }

         FailureDescription[] var10 = new FailureDescription[var5.size()];
         var10 = (FailureDescription[])((FailureDescription[])var5.toArray(var10));
         var9.deploySucceeded(var10);
      } catch (Exception var8) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Edit Access deploy succeeded failed with exception ", var8);
         }
      }

   }

   public synchronized void deployFailed(long var1, DeploymentException var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access deploy failed for identifier " + var1 + " with reason:" + var3);
      }

      this.failed(var1, var3 != null ? var3.getFailures() : null, DEPLOY);
   }

   public synchronized void commitFailed(long var1, FailureDescription[] var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access deploy encountered commit failures for identifier " + var1 + " to " + var3.length + " targets");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            debugLogger.debug("Edit Access commit failure " + var3[var4]);
         }
      }

      this.failed(var1, var3, COMMIT);
   }

   public synchronized void commitSucceeded(long var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access commit suceeded for identifier " + var1);
      }

      try {
         ActivateTaskImpl var3 = this.lookupTask(var1);
         if (var3 == null) {
            return;
         }

         var3.commitSucceeded();
      } catch (Exception var4) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Edit Access commit succeeded failed with exception ", var4);
         }
      }

   }

   public synchronized void cancelSucceeded(long var1, FailureDescription[] var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access cancel suceeded for identifier " + var1 + " with " + var3.length + " cancel delivery attempt failures");
      }

      this.failed(var1, var3, CANCEL);
   }

   public synchronized void cancelFailed(long var1, DeploymentException var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access cancel failed for identifier " + var1 + " with reason:" + var3);
      }

      this.failed(var1, var3 != null ? var3.getFailures() : null, CANCEL);
   }

   public synchronized void receivedStatusFrom(long var1, Serializable var3, String var4) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access received status for identifier " + var1 + " for server " + var4 + " with status " + var3);
      }

      String var5 = (String)var3;
      if (var5.equals("COMMIT_PENDING")) {
         this.handleRequestStatusUpdate(var1, var5, var4);
      }

   }

   public synchronized void requestStatusUpdated(long var1, String var3, String var4) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Edit Access received request status update for identifier " + var1 + " for server " + var4 + " with status " + var3);
      }

      this.handleRequestStatusUpdate(var1, var3, var4);
   }

   private void handleRequestStatusUpdate(long var1, String var3, String var4) {
      ActivateTaskImpl var5 = this.lookupTask(var1);
      if (var5 != null) {
         boolean var6 = false;
         if (var3.equals("PrepareSuccessReceived")) {
            var6 = var5.updateServerState(var4, 2);
         } else if (var3.equals("PrepareFailedReceived")) {
            var6 = var5.updateServerState(var4, 5);
         } else if (var3.equals("CommitSuccessReceived")) {
            var6 = var5.updateServerState(var4, 4);
         } else if (var3.equals("CommitFailedReceived")) {
            var6 = var5.updateServerState(var4, 7);
         } else if (var3.equals("CancelSuccessReceived")) {
            var6 = var5.updateServerState(var4, 6);
         } else if (var3.equals("CancelFailedReceived")) {
            var6 = var5.updateServerState(var4, 6);
         } else if (var3.equals("COMMIT_PENDING")) {
            var6 = var5.updateServerState(var4, 3);
         }

         if ((var5.getState() == 2 || var5.getState() == 4 || var6) && this.isPreparing() && var1 == this.preparingId) {
            this.prepareCompleted(var1);
         }

         if (var6) {
            var5.releaseAndSetCommitted();
         }

         if (!var5.isRunning()) {
            this.removeTask(var5);
         }

      }
   }

   public synchronized void cancelActivate() throws EditFailedException {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Canceling activate operation for " + var1);
      }

      if (this.isPreparing()) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Cancel outstanding requests");
         }

         ActivateTaskImpl var2 = this.lookupTask(this.preparingId);
         this.resetPreparingInfo();
         this.rollbackCurrent();
         if (var2 != null) {
            try {
               var2.cancel();
            } catch (Exception var4) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception canceling task ", var4);
               }

               throw new EditFailedException(var4);
            }
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Cancelled activate.");
      }

   }

   private void failed(long var1, FailureDescription[] var3, int var4) {
      if (this.isPreparing() && var1 == this.preparingId) {
         this.resetPreparingInfo();
         this.rollbackCurrent();
      }

      boolean var5 = false;
      ActivateTaskImpl var6 = this.lookupTask(var1);
      if (var6 == null) {
         var5 = true;
         var6 = this.lookupOldTask(var1);
         if (var6 == null) {
            return;
         }
      }

      for(int var7 = 0; var3 != null && var7 < var3.length; ++var7) {
         var6.addFailedServer(var3[var7].getServer(), var3[var7].getReason());
      }

      if (var6.isWaitingForEndFailureCallback() || var4 == COMMIT || var4 == DEPLOY && var6.getState() == 6) {
         var6.setState(5);
         if (!var5) {
            this.removeTask(var6);
         }
      } else {
         var6.setWaitingForEndFailureCallback(true);
      }

   }

   private AuthenticatedSubject checkEditLock() throws EditNotEditorException {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelIdentity);
      if (!this.lockMgr.isLockOwner(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Subject " + var1 + " does not have the edit lock");
         }

         throw new EditNotEditorException("Not edit lock owner");
      } else {
         return var1;
      }
   }

   private static AuthenticatedSubject obtainKernelIdentity() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return var0;
   }

   private synchronized ConfigurationDeployment getConfigDeploymentPending(Iterator var1) throws InvalidCreateChangeDescriptorException {
      File[] var2 = this.pendingDirMgr.getAllFiles();
      if (var2 != null && var2.length != 0) {
         ConfigurationVersion var3 = new ConfigurationVersion(true);
         ConfigurationDeployment var4 = new ConfigurationDeployment(this.getHandlerIdentity());
         var4.setProposedVersion(var3);
         boolean var5 = this.pendingDirMgr.configExists();

         int var6;
         String var7;
         for(var6 = 0; var6 < var2.length; ++var6) {
            var7 = var2[var6].getPath();
            if (var5 && this.pendingDirMgr.getConfigFile().getPath().equals(var7) && var6 < var2.length - 1) {
               File var8 = var2[var2.length - 1];
               var2[var2.length - 1] = var2[var6];
               var2[var6] = var8;
               break;
            }
         }

         for(var6 = 0; var6 < var2.length; ++var6) {
            var7 = var2[var6].getPath();
            String var12 = this.pendingDirMgr.removePendingDirectoryFromPath(var7);
            String var9 = "update";
            String var10 = "config";
            if (!var5 || !this.pendingDirMgr.getConfigFile().getPath().equals(var7)) {
               if (this.isWLSExternalFile(var12)) {
                  var10 = "external";
               } else {
                  var10 = "non-wls";
               }

               File var11 = new File(var12);
               if (!var11.exists()) {
                  var9 = "add";
               }
            }

            var3.addOrUpdateFile(var7, var12);
            var7 = this.removeRootDirectoryFromPath(var7);
            var12 = this.removeRootDirectoryFromPath(var12);
            this.addChangeDescriptor(var7, var12, var4, var9, var3, var10);
         }

         this.processBeanUpdateEvents(var1, var4, var3);
         this.addTargets(var4);
         return var4;
      } else {
         return null;
      }
   }

   private boolean isWLSExternalFile(String var1) {
      String var2 = this.removeRootDirectoryFromPath(var1);
      String var3 = DomainDir.getRootDirNonCanonical() + "/" + var2;
      SystemResourceMBean[] var4 = this.editDomainMBean.getSystemResources();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if ((new File(var4[var5].getSourcePath())).getPath().equals((new File(var3)).getPath())) {
            return true;
         }
      }

      return false;
   }

   private ConfigurationDeployment getConfigDeploymentCurrent(Version var1, Version var2) throws InvalidCreateChangeDescriptorException {
      ConfigurationVersion var3 = new ConfigurationVersion(true);
      if (var1 != null && var1.equals(var3)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("GetConfigDeploymentCurrent - from and source version are equal.");
         }

         return null;
      } else {
         ConfigurationDeployment var4 = new ConfigurationDeployment(this.getHandlerIdentity());
         var4.setProposedVersion(var3);
         Map var5 = var3.getVersionComponents();
         Map var6 = var1.getVersionComponents();
         String var7 = BootStrap.getConfigDirectoryConfigFile().getPath();
         Iterator var8 = var5.keySet().iterator();

         while(var8 != null && var8.hasNext()) {
            String var9 = (String)var8.next();
            String var10 = "external";
            if (var7.equals(var9)) {
               var10 = "config";
            }

            boolean var11 = true;
            String var12 = (String)var6.get(var9);
            if (var12 != null && var12.equals((String)var5.get(var9))) {
               var11 = false;
            }

            if (var11) {
               var9 = this.removeRootDirectoryFromPath(var9);
               this.addChangeDescriptor(var9, var9, var4, "update", var3, var10);
            }
         }

         this.addTargets(var4);
         return var4;
      }
   }

   private void addTargets(ConfigurationDeployment var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelIdentity).getDomain();
      ServerMBean[] var3 = var2.getServers();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         var1.addTarget(var3[var4].getName());
      }

   }

   private void addChangeDescriptor(String var1, String var2, ConfigurationDeployment var3, String var4, ConfigurationVersion var5, String var6) throws InvalidCreateChangeDescriptorException {
      String var7 = (new File(var1)).getPath();
      String var8 = (new File(var2)).getPath();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Create change descriptor with target path " + var8);
         debugLogger.debug("Create change descriptor with src path " + var7);
      }

      ChangeDescriptor var9 = DeploymentService.getDeploymentService().createChangeDescriptor(var4, var8, var7, var5.toString(), var6);
      var3.addChangeDescriptor(var9);
   }

   private void processBeanUpdateEvents(Iterator var1, ConfigurationDeployment var2, ConfigurationVersion var3) throws InvalidCreateChangeDescriptorException {
      label24:
      while(true) {
         if (var1 != null && var1.hasNext()) {
            BeanUpdateEvent var4 = (BeanUpdateEvent)var1.next();
            String[] var5 = ChangeUtils.getRestartRequiredServers(var4);
            var2.addServersToBeRestarted(var5);
            BeanUpdateEvent.PropertyUpdate[] var6 = var4.getUpdateList();
            int var7 = 0;

            while(true) {
               if (var7 >= var6.length) {
                  continue label24;
               }

               BeanUpdateEvent.PropertyUpdate var8 = var6[var7];
               if (var8.getUpdateType() == 3) {
                  Object var9 = var8.getRemovedObject();
                  if (var9 instanceof ConfigurationExtensionMBean) {
                     ConfigurationExtensionMBean var10 = (ConfigurationExtensionMBean)var9;
                     String var11 = DomainDir.getConfigDir() + File.separator + var10.getDescriptorFileName();
                     var3.removeFile(var11);
                     var11 = this.removeRootDirectoryFromPath(var11);
                     this.addChangeDescriptor(var11, var11, var2, "delete", var3, "external");
                  }
               }

               ++var7;
            }
         }

         return;
      }
   }

   public String removeRootDirectoryFromPath(String var1) {
      return !var1.startsWith(this.rootDirectoryPrefix) && !(new File(var1)).getPath().startsWith((new File(this.rootDirectoryPrefix)).getPath()) ? var1 : var1.substring(this.rootDirectoryPrefix.length(), var1.length());
   }

   private void ensureBeanTreeLoaded() throws EditFailedException {
      try {
         synchronized(this) {
            File var2;
            if (this.editDomainMBean == null) {
               var2 = BootStrap.getConfigDirectoryConfigFile();
               if (!var2.canWrite()) {
                  ManagementLogger.logConfigurationFileIsReadOnly(var2.getPath());
               }

               this.editTree = this.loadBeanTreeFromPending();
               this.editDomainMBean = (DomainMBean)this.editTree.getRootBean();
            }

            if (this.currentDomainMBean == null) {
               var2 = BootStrap.getConfigDirectoryConfigFile();
               this.currentTree = this.loadBeanTreeFromActive(var2);
               this.currentDomainMBean = (DomainMBean)this.currentTree.getRootBean();
            }

         }
      } catch (ManagementException var5) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception running processors ", var5);
         }

         throw new EditFailedException(var5);
      } catch (IOException var6) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception loading tree ", var6);
         }

         throw new EditFailedException(var6);
      }
   }

   private Descriptor loadBeanTreeFromActive(File var1) throws IOException {
      Descriptor var2 = null;
      FileInputStream var3 = new FileInputStream(var1);

      Descriptor var6;
      try {
         DescriptorManager var4 = DescriptorManagerHelper.getDescriptorManager(false);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Loading bean tree from stream");
         }

         ArrayList var12 = new ArrayList();
         var2 = var4.createDescriptor(new ConfigReader(var3), var12, true);
         checkErrors(var1.getAbsolutePath(), var12);
         this.setProductionModeInfo(var2);
         var6 = var2;
      } catch (XMLStreamException var10) {
         IOException var5 = new IOException("Error loading " + var1 + ": " + var10.getMessage());
         var5.initCause(var10);
         throw var5;
      } finally {
         var3.close();
      }

      return var6;
   }

   private Descriptor loadBeanTreeFromPending() throws EditFailedException, IOException, ManagementException {
      FileLock var1 = Utils.getConfigFileLock();
      if (var1 == null) {
         ManagementLogger.logCouldNotGetConfigFileLock();
      }

      Descriptor var4;
      try {
         InputStream var2 = this.pendingDirMgr.getConfigAsStream();
         Descriptor var3 = this.loadBeanTree(var2, BootStrap.getDefaultConfigFileName());
         ((DescriptorImpl)var3).setModified(false);
         this.setExternalTreesUnmodified(var3);
         var4 = var3;
      } finally {
         if (var1 != null) {
            Utils.releaseConfigFileLock();
         }

      }

      return var4;
   }

   private Descriptor loadExternalBeanTree(DescriptorInfo var1, boolean var2, boolean var3) throws EditFailedException, IOException {
      FileLock var4 = Utils.getConfigFileLock();
      if (var4 == null) {
         ManagementLogger.logCouldNotGetConfigFileLock();
      }

      String var5 = null;
      Object var6 = null;

      try {
         String var8;
         try {
            ConfigurationExtensionMBean var7 = var1.getConfigurationExtension();
            var8 = var7.getDescriptorFileName();
            DescriptorManager var9 = var1.getDescriptorManager();
            File var10 = new File(DomainDir.getPathRelativeConfigDir(var8));
            ArrayList var11;
            Descriptor var12;
            Descriptor var13;
            if ((!var2 || !this.pendingDirMgr.fileExists(var8)) && !var10.exists()) {
               if (!var3) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Creating external bean tree from class");
                  }

                  var11 = new ArrayList();
                  var12 = var9.createDescriptorRoot(var1.getDescriptorClass(), "UTF-8");
                  checkErrors(var10.getAbsolutePath(), var11);
                  this.setProductionModeInfo(var12);
                  var13 = var12;
                  return var13;
               } else {
                  var11 = null;
                  return var11;
               }
            } else {
               if (var2) {
                  var5 = var7.getDescriptorFileName();
                  var6 = this.pendingDirMgr.getFileAsStream(var7.getDescriptorFileName());
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Loading external bean tree from stream");
                  }
               } else {
                  var6 = new FileInputStream(var10);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Loading external bean tree from current file");
                  }
               }

               var11 = new ArrayList();
               var12 = var9.createDescriptor(new ExternalConfigReader((InputStream)var6), var11, true);
               checkErrors(var10.getAbsolutePath(), var11);
               this.setProductionModeInfo(var12);
               var13 = var12;
               return var13;
            }
         } catch (XMLStreamException var18) {
            throw new EditFailedException("Error loading " + var5, var18);
         } catch (IOException var19) {
            if (var19 instanceof FileNotFoundException) {
               var8 = null;
               return var8;
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception in load external edit tree: ", var19);
               }

               throw new EditFailedException("Error loading " + var5, var19);
            }
         }
      } finally {
         if (var6 != null) {
            ((InputStream)var6).close();
         }

         if (var4 != null) {
            Utils.releaseConfigFileLock();
         }

      }
   }

   private Descriptor loadBeanTree(InputStream var1, String var2) throws EditFailedException, IOException {
      Descriptor var6;
      try {
         EditableDescriptorManager var3 = (EditableDescriptorManager)DescriptorManagerHelper.getDescriptorManager(true);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Loading bean tree from stream");
         }

         ArrayList var4 = new ArrayList();
         Descriptor var5 = var3.createDescriptor(new ConfigReader(var1), var4, false);
         checkErrors(var2, var4);
         this.setProductionModeInfo(var5);
         var6 = var5;
      } catch (XMLStreamException var11) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception in load edit tree: ", var11);
         }

         throw new EditFailedException("Error loading " + var2, var11);
      } catch (IOException var12) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception in load edit tree: ", var12);
         }

         throw new EditFailedException(var12);
      } finally {
         var1.close();
      }

      return var6;
   }

   private boolean isPreparing() {
      if (!this.preparing) {
         return false;
      } else if (System.currentTimeMillis() >= this.preparingTimeout) {
         this.resetPreparingInfo();
         return false;
      } else {
         return true;
      }
   }

   private void setPreparing(boolean var1) {
      this.preparing = var1;
   }

   private void resetPreparingInfo() {
      this.setPreparing(false);
      this.setPreparingTimeout(0L);
      this.preparingId = 0L;
   }

   private void validatePreparingInfo() {
      if (this.isPreparing()) {
         if (this.preparingId == 0L) {
            this.resetPreparingInfo();
         } else {
            ActivateTaskImpl var1 = this.lookupTask(this.preparingId);
            if (var1 == null) {
               var1 = this.lookupOldTask(this.preparingId);
            }

            if (var1 == null || !var1.isRunning()) {
               this.resetPreparingInfo();
            }

         }
      }
   }

   private void prepareCompleted(long var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Prepare completed " + this.isPreparing() + " id " + var1 + " prepare id " + this.preparingId);
      }

      ActivateTaskImpl var3;
      if (this.isPreparing() && var1 == this.preparingId) {
         RuntimeAccessDeploymentReceiverService.getService().commitAnyPendingRequests();
         if (!this.deletePendingDirectory()) {
            var3 = this.lookupTask(var1);
            if (var3 != null) {
               var3.setError(new EditFailedException("Can not delete all the files in the pending directory"));
               var3.setState(5);
            }
         } else {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Prepare completed, no changes are pending in pending directory");
            }

            this.setPendingChange(false);
         }

         try {
            this.currentTree.activateUpdate();
            Iterator var14 = DescriptorInfoUtils.getDescriptorInfos(this.editTree);

            while(var14 != null && var14.hasNext()) {
               DescriptorInfo var4 = (DescriptorInfo)var14.next();
               Descriptor var5 = var4.getDescriptor();
               Descriptor var6 = this.getExternalCurrentTree(var4);
               if (var6 != null) {
                  var6.prepareUpdate(var5, false);
                  var6.activateUpdate();
               }
            }
         } catch (DescriptorUpdateFailedException var11) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in prepare/activate of external current tree: ", var11);
            }
         } catch (DescriptorUpdateRejectedException var12) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in prepare/activate of external current tree: ", var12);
            }
         } finally {
            this.resetPreparingInfo();
         }
      } else if (var1 != this.preparingId && this.preparingId != 0L) {
         var3 = this.lookupTask(var1);
         if (var3 == null) {
            var3 = this.lookupOldTask(var1);
         }

         if (var3 == null || var3.isRunning()) {
            ManagementLogger.logInvalidPrepareCallback("" + var1);
         }
      }

   }

   private boolean deletePendingDirectory() {
      FileLock var1 = Utils.getConfigFileLock();
      if (var1 == null) {
         ManagementLogger.logCouldNotGetConfigFileLock();
      }

      try {
         boolean var2 = false;

         for(int var3 = 0; var3 < 5 && !var2; ++var3) {
            var2 = this.pendingDirMgr.deleteAll();
            if (!var2) {
               try {
                  Thread.sleep(3000L);
               } catch (Exception var8) {
               }
            }
         }

         boolean var10 = var2;
         return var10;
      } finally {
         if (var1 != null) {
            Utils.releaseConfigFileLock();
         }

      }
   }

   private void setPreparingInfo(long var1, long var3) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Set preparing id = " + var3 + " timeouttime = " + var1);
      }

      this.setPreparing(true);
      this.setPreparingTimeout(var1);
      this.preparingId = var3;
   }

   private void setPreparingTimeout(long var1) {
      this.preparingTimeout = var1;
   }

   private final synchronized ActivateTaskImpl createActivationTask(long var1, long var3, long var5, boolean var7, ArrayList var8, AuthenticatedSubject var9, EditLockManager var10, ConfigurationDeployment var11) throws EditFailedException {
      ActivateTaskImpl var12;
      if (!var7) {
         try {
            var12 = new ActivateTaskImpl("Activate task with id: " + var1, var10, var7, var8, var9, var1, var3, (String[])null);
            return var12;
         } catch (ManagementException var13) {
            throw new EditFailedException(var13);
         }
      } else {
         this.validatePreparingInfo();
         if (this.isPreparing()) {
            throw new EditFailedException("Unable to start new Activation while preparing.");
         } else {
            try {
               this.setPreparingInfo(var5, var1);
               var12 = new ActivateTaskImpl("Activate task with id: " + var1, var10, var7, var8, var9, var1, var3, var11.getTargets());
               this.activationTasksByRequest.put(new Long(var1), var12);
               return var12;
            } catch (ManagementException var14) {
               throw new EditFailedException(var14);
            }
         }
      }
   }

   private ActivateTaskImpl lookupTask(long var1) {
      return (ActivateTaskImpl)this.activationTasksByRequest.get(new Long(var1));
   }

   private ActivateTaskImpl lookupOldTask(long var1) {
      try {
         synchronized(this.oldActivationTasks) {
            Set var4 = this.oldActivationTasks.keySet();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               ActivateTaskImpl var6 = (ActivateTaskImpl)var5.next();
               if (var6.getTaskId() == var1) {
                  return var6;
               }
            }
         }
      } catch (Exception var9) {
      }

      return null;
   }

   private void removeTask(ActivateTask var1) {
      this.activationTasksByRequest.remove(new Long(var1.getTaskId()));
      synchronized(this.oldActivationTasks) {
         try {
            this.oldActivationTasks.put(var1, (Object)null);
         } catch (Exception var5) {
         }

      }
   }

   private boolean areAnyExternalTreesModified(Descriptor var1) {
      boolean var2 = false;
      Iterator var3 = DescriptorInfoUtils.getDescriptorInfos(var1);

      while(var3 != null && var3.hasNext() && !var2) {
         DescriptorInfo var4 = (DescriptorInfo)var3.next();
         Descriptor var5 = var4.getDescriptor();
         if (var5.isModified()) {
            var2 = true;
         }
      }

      return var2;
   }

   private void setExternalTreesUnmodified(Descriptor var1) {
      Iterator var2 = DescriptorInfoUtils.getDescriptorInfos(var1);

      while(var2 != null && var2.hasNext()) {
         DescriptorInfo var3 = (DescriptorInfo)var2.next();
         Descriptor var4 = var3.getDescriptor();
         if (var4.isModified()) {
            ((DescriptorImpl)var4).setModified(false);
         }
      }

   }

   private void rollbackCurrent() {
      if (this.currentTree != null) {
         this.currentTree.rollbackUpdate();
      }

   }

   private Descriptor getExternalCurrentTree(DescriptorInfo var1) {
      Iterator var2 = DescriptorInfoUtils.getDescriptorInfos(this.currentTree);

      while(var2 != null && var2.hasNext()) {
         DescriptorInfo var3 = (DescriptorInfo)var2.next();
         ConfigurationExtensionMBean var4 = var3.getConfigurationExtension();
         ConfigurationExtensionMBean var5 = var1.getConfigurationExtension();
         if (var4 != null && var5 != null && var4.getName().equals(var5.getName()) && var4.getDescriptorFileName().equals(var5.getDescriptorFileName())) {
            return var3.getDescriptor();
         }
      }

      return null;
   }

   public BeanInfo getBeanInfo(DescriptorBean var1) {
      BeanInfoAccess var2 = ManagementService.getBeanInfoAccess();
      return var2.getBeanInfoForDescriptorBean(var1);
   }

   public PropertyDescriptor getPropertyDescriptor(BeanInfo var1, String var2) {
      BeanInfoAccess var3 = ManagementService.getBeanInfoAccess();
      return var3.getPropertyDescriptor(var1, var2);
   }

   public boolean getRestartValue(PropertyDescriptor var1) {
      return ChangeUtils.getRestartValue(var1);
   }

   public static void checkErrors(String var0, ArrayList var1) throws IOException {
      if (var1.size() > 0) {
         int var2 = var1.size();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof XmlValidationError) {
               XmlValidationError var5 = (XmlValidationError)var4;
               if (ConfigFileHelper.isAcceptableXmlValidationError(var5)) {
                  --var2;
               } else {
                  ManagementLogger.logConfigurationValidationProblem(var0, var5.getMessage());
               }
            } else {
               ManagementLogger.logConfigurationValidationProblem(var0, var4.toString());
            }
         }

         if (schemaValidationEnabled && var2 > 0) {
            String var6 = "-Dweblogic.configuration.schemaValidationEnabled=false";
            Loggable var7 = ManagementLogger.logConfigurationSchemaFailureLoggable(var0, var6);
            throw new IOException(var7.getMessage());
         }
      }

   }

   public static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }

   public synchronized void shutdown() {
      if (!this.temporaryTrees.isEmpty()) {
         System.gc();
         Iterator var1 = this.temporaryTrees.values().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            ManagementLogger.logTemporaryBeanTreeNotGarbageCollected(var2);
         }

      }
   }

   private void addTemporaryTree(Object var1, String var2) {
      this.temporaryTrees.put(var1, var2 + "(" + new Date() + ")");
   }

   private void setProductionModeInfo(Descriptor var1) {
      DescriptorBean var2 = var1.getRootBean();
      if (var2 instanceof DomainMBean && ((DomainMBean)var2).isProductionModeEnabled()) {
         DescriptorHelper.setDescriptorTreeProductionMode(var1, true);
      } else {
         DescriptorHelper.setDescriptorTreeProductionMode(var1, false);
      }

   }
}
