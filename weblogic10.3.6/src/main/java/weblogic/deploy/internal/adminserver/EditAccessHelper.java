package weblogic.deploy.internal.adminserver;

import java.security.AccessController;
import java.security.PrivilegedAction;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.common.Debug;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditChangesValidationException;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.EditSaveChangesFailedException;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.StackTraceUtils;

public final class EditAccessHelper {
   private final EditAccess delegate;
   private static final int MAX_EDIT_SESSION_DURATION = 120000;
   private static final int MAX_WAIT_TIME_TO_ACQUIRE_EDIT_SESSION = 0;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private EditAccessHelper() {
      this.delegate = ManagementServiceRestricted.getEditAccess(kernelId);
   }

   public static EditAccessHelper getInstance(AuthenticatedSubject var0) {
      SecurityServiceManager.checkKernelIdentity(var0);
      return EditAccessHelper.Maker.HELPER;
   }

   public final boolean isEditorExclusive() {
      return this.delegate.isEditorExclusive();
   }

   public final long getEditorExpirationTime() {
      return this.delegate.getEditorExpirationTime();
   }

   public final boolean isPendingChange() {
      return this.delegate.isPendingChange();
   }

   public final DomainMBean startEditSession(boolean var1) throws ManagementException {
      try {
         DomainMBean var2 = this.delegate.startEdit(0, 120000, var1);
         return var2;
      } catch (EditWaitTimedOutException var4) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Failed to get an edit session lock: " + var4.toString());
         }

         throw var4;
      } catch (EditFailedException var5) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Failed to get an edit session: " + var5.toString());
         }

         throw var5;
      }
   }

   public final void saveEditSessionChanges() throws ManagementException {
      try {
         this.delegate.saveChanges();
      } catch (EditNotEditorException var2) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Tried to save an edit session when not holding the edit lock: " + var2.toString());
         }

         throw var2;
      } catch (EditSaveChangesFailedException var3) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Attempt to save changes of an edit session failed: " + var3.toString());
         }

         throw var3;
      } catch (EditChangesValidationException var4) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Validation when attempting to save edit session changes failed: " + var4.toString());
         }

         throw var4;
      }
   }

   public final void activateEditSessionChanges(long var1) throws ManagementException {
      try {
         this.delegate.activateChanges(var1);
      } catch (EditNotEditorException var4) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Tried to activate an edit session when not holding the edit lock: " + var4.toString());
         }

         throw var4;
      } catch (EditFailedException var5) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Attempt to activate changes of an edit session failed: " + var5.toString());
         }

         throw var5;
      }
   }

   public final void stopEditSession(AuthenticatedSubject var1) throws ManagementException {
      if (this.isCurrentEditor(var1)) {
         Object var2 = SecurityServiceManager.runAs(kernelId, var1, new PrivilegedAction() {
            public Object run() {
               Object var1 = null;

               try {
                  EditAccessHelper.this.delegate.stopEdit();
               } catch (EditNotEditorException var3) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Tried to stop an edit session when not holding the edit lock: " + var3.toString());
                  }

                  var1 = var3;
               } catch (EditFailedException var4) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Edit session failure when attempting to stop an edit session: " + var4.toString());
                  }

                  var1 = var4;
               }

               return var1;
            }
         });
         if (var2 instanceof ManagementException) {
            throw (ManagementException)var2;
         }
      }
   }

   public final void cancelActivateSession(AuthenticatedSubject var1) throws ManagementException {
      if (this.isCurrentEditor(var1)) {
         Object var2 = SecurityServiceManager.runAs(kernelId, var1, new PrivilegedAction() {
            public Object run() {
               ManagementException var1 = null;

               try {
                  EditAccessHelper.this.delegate.cancelActivate();
               } catch (Throwable var3) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Edit session failure when attempting to cancel activate session: " + var3.toString());
                  }

                  if (var3 instanceof ManagementException) {
                     var1 = (ManagementException)var3;
                  } else {
                     var1 = new ManagementException(var3.getMessage(), var3);
                  }
               }

               return var1;
            }
         });
         if (var2 instanceof ManagementException) {
            throw (ManagementException)var2;
         }
      }
   }

   public final void cancelEditSession(AuthenticatedSubject var1) {
      SecurityServiceManager.runAs(kernelId, var1, new PrivilegedAction() {
         public Object run() {
            Object var1 = null;

            try {
               EditAccessHelper.this.delegate.cancelEdit();
            } catch (EditFailedException var3) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Edit session failure when attempting  to cancel an edit session: " + var3.toString());
               }
            }

            return var1;
         }
      });
   }

   public final void undoUnactivatedChanges(AuthenticatedSubject var1) throws ManagementException {
      Object var2 = SecurityServiceManager.runAs(kernelId, var1, new PrivilegedAction() {
         public Object run() {
            Object var1 = null;

            try {
               EditAccessHelper.this.delegate.undoUnactivatedChanges();
            } catch (EditFailedException var3) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Edit session failure when attempting to undo unactivated changes of an edit session: " + var3.toString());
               }

               var1 = var3;
            } catch (EditNotEditorException var4) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Edit session failure when attempting to undo unactivated changes of an edit session: " + var4.toString());
               }

               var1 = var4;
            }

            return var1;
         }
      });
      if (var2 instanceof ManagementException) {
         throw (ManagementException)var2;
      }
   }

   public final boolean isCurrentEditor(AuthenticatedSubject var1) {
      String var2 = this.delegate.getEditor();
      if (var2 == null) {
         return false;
      } else {
         return var1 != null && var2.equals(SubjectUtils.getUsername(var1));
      }
   }

   private final DomainMBean getEditDomainBean() {
      try {
         return this.delegate.getDomainBean();
      } catch (EditNotEditorException var2) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Edit session failure when attempting to retrieve the domain mbean: " + var2.toString());
         }
      } catch (EditFailedException var3) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Edit session failure when attempting to retrieve the domain mbean: " + var3.toString());
         }
      }

      return null;
   }

   public final DomainMBean getEditDomainBean(AuthenticatedSubject var1) {
      return this.isCurrentEditor(var1) ? this.getEditDomainBean() : null;
   }

   public void rollback(final AppDeploymentMBean var1, final DeploymentBeanFactory var2, final AuthenticatedSubject var3) {
      SecurityServiceManager.runAs(kernelId, var3, new PrivilegedAction() {
         public Object run() {
            Object var1x = null;
            DomainMBean var2x = EditAccessHelper.this.getEditDomainBean(var3);
            if (var2x == null) {
               return var1x;
            } else {
               try {
                  var2.setEditableDomain(var2x, true);
                  var2.removeMBean(var1);
               } catch (ManagementException var8) {
                  Debug.deploymentLogger.debug("Failed to remove mbeans for failed deployment of : " + var1.getName() + " due to " + StackTraceUtils.throwable2StackTrace(var8));
               } finally {
                  var2.resetEditableDomain();
               }

               return var1x;
            }
         }
      });
   }

   // $FF: synthetic method
   EditAccessHelper(Object var1) {
      this();
   }

   static final class Maker {
      static final EditAccessHelper HELPER = new EditAccessHelper();
   }
}
