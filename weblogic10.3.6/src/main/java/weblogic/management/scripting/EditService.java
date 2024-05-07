package weblogic.management.scripting;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import javax.management.Descriptor;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.Change;
import weblogic.management.mbeanservers.edit.EditTimedOutException;
import weblogic.management.mbeanservers.edit.NotEditorException;
import weblogic.management.mbeanservers.edit.ValidationException;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.management.scripting.utils.WLSTUtil;

public class EditService implements Serializable {
   private WLScriptContext ctx = null;
   private static final String ACTIVATE = "activate";
   private static final String UNDO = "undo";
   private static final String SAVE = "save";
   private static final String STOP_EDIT = "stopEdit";
   private static final String VALIDATE = "validate";
   private static final String SHOW_CHANGES = "showChanges";
   private WLSTMsgTextFormatter txtFmt;

   public EditService(WLScriptContext ctx) {
      this.ctx = ctx;
      this.txtFmt = ctx.getWLSTMsgFormatter();
   }

   private void validateTree() throws ScriptException {
      WLScriptContext var10001 = this.ctx;
      if (!this.ctx.domainType.equals("ConfigEdit")) {
         this.ctx.throwWLSTException(this.txtFmt.getCantCallEditFunctions());
      }

   }

   private boolean validateCall(String call) throws ScriptException {
      if (this.ctx.newBrowseHandler.doesUserHasLock()) {
         this.ctx.isEditSessionInProgress = true;
         return true;
      } else if (this.ctx.isEditSessionInProgress && call.equals("stopEdit")) {
         return true;
      } else if (this.ctx.isEditSessionInProgress && !this.ctx.newBrowseHandler.doesUserHasLock()) {
         this.ctx.println(this.txtFmt.getEditSessionTerminated());
         this.cleanUp();
         return false;
      } else if (!this.ctx.isEditSessionInProgress) {
         this.ctx.throwWLSTException(this.txtFmt.getNeedEditSessionFor(call));
         return false;
      } else {
         return true;
      }
   }

   private void cleanUp() {
      this.ctx.resetEditSession();
      if (!WLSTUtil.runningWLSTAsModule()) {
         WLSTUtil.getWLSTInterpreter().exec("evaluatePrompt()");
      }

   }

   public DomainMBean startEdit(int waitTimeInMillis, int timeOutInMillis, String exclusive) throws ScriptException {
      this.ctx.commandType = "startEdit";
      this.validateTree();
      DomainMBean bean = null;
      this.ctx.println(this.txtFmt.getStartingEditSession());

      String user;
      try {
         bean = this.ctx.configurationManager.startEdit(waitTimeInMillis, timeOutInMillis, this.ctx.getBoolean(exclusive));
         String prompt = this.ctx.getPrompt();
         this.ctx.wlcmo = bean;
         this.ctx.browseHandler.cd(prompt);
         this.ctx.println(this.txtFmt.getStartedEditSession());
         if (this.ctx.getBoolean(exclusive)) {
            this.ctx.print(this.txtFmt.getExclusiveSession());
            this.ctx.isEditSessionExclusive = true;
         }

         this.ctx.isEditSessionInProgress = true;
      } catch (EditTimedOutException var7) {
         user = this.ctx.configurationManager.getCurrentEditor();
         this.ctx.throwWLSTException(this.txtFmt.getEditLockHeld(user), var7);
      } catch (NoAccessRuntimeException var8) {
         user = new String(this.ctx.username_bytes);
         this.ctx.throwWLSTException(this.txtFmt.getNoPermissionForEdit(user), var8);
      }

      return bean;
   }

   public void save() throws ScriptException {
      this.ctx.commandType = "save";
      this.validateTree();
      if (this.validateCall("save")) {
         this.ctx.println(this.txtFmt.getSavingChanges());

         try {
            this.ctx.configurationManager.save();
            this.ctx.println(this.txtFmt.getSavedChanges());
         } catch (NotEditorException var2) {
            this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var2);
         } catch (ValidationException var3) {
            this.ctx.throwWLSTException(this.txtFmt.getInvalidChanges(), var3);
         }

      }
   }

   public ActivationTaskMBean activate(long timeout, String block) throws ScriptException {
      this.ctx.commandType = "activate";
      if (WLSTUtil.runningWLSTAsModule()) {
         block = "true";
      }

      this.validateTree();
      if (!this.validateCall("activate")) {
         return null;
      } else {
         try {
            if (block.equalsIgnoreCase("true")) {
               this.ctx.println(this.txtFmt.getActivatingChanges());
               this.ctx.configurationManager.save();
               this.ctx.activationTask = this.ctx.configurationManager.activate(timeout);
               this.printServerRestartInfo(this.ctx.activationTask.getChanges());
               this.ctx.activationTask.waitForTaskCompletion(timeout);
               if (this.ctx.activationTask.getError() != null) {
                  throw this.ctx.activationTask.getError();
               }

               this.ctx.println(this.txtFmt.getActivationComplete());
               if (!WLSTUtil.runningWLSTAsModule()) {
                  WLSTUtil.getWLSTInterpreter().set("activationTask", this.ctx.activationTask);
               }
            } else {
               this.ctx.println(this.txtFmt.getActivatingChangesNonBlocking());
               this.ctx.configurationManager.save();
               this.ctx.activationTask = this.ctx.configurationManager.activate(timeout);
               this.printServerRestartInfo(this.ctx.activationTask.getChanges());
               WLSTUtil.getWLSTInterpreter().set("activationTask", this.ctx.activationTask);
               this.ctx.println(this.txtFmt.getActivationTaskCreated());
            }

            this.ctx.resetEditSession();
            return this.ctx.activationTask;
         } catch (NotEditorException var10) {
            this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var10);
         } catch (RuntimeException var11) {
            boolean threwError = false;
            if (var11.getCause() instanceof RemoteException) {
               RemoteException re = (RemoteException)var11.getCause();
               if (re.getCause() instanceof SecurityException) {
                  SecurityException se = (SecurityException)re.getCause();
                  if (se.getMessage().indexOf(this.txtFmt.getAdministratorRequiredString()) != -1) {
                     this.ctx.println(this.txtFmt.getReloginRequired());

                     try {
                        this.ctx.dc("true");
                     } catch (Throwable var9) {
                        this.ctx.throwWLSTException(this.txtFmt.getErrorDisconnecting(), var9);
                     }

                     threwError = true;
                  }
               }
            }

            if (!threwError) {
               this.cleanUp();
               this.ctx.throwWLSTException(this.txtFmt.getErrorActivating(), var11);
            }
         } catch (Throwable var12) {
            this.cleanUp();
            this.ctx.throwWLSTException(this.txtFmt.getErrorActivating(), var12);
         }

         this.ctx.resetEditSession();
         return this.ctx.activationTask;
      }
   }

   private void printServerRestartInfo(Change[] unActivatedChanges) throws NotEditorException {
      if (unActivatedChanges.length >= 1) {
         HashMap beans = new HashMap();

         for(int i = 0; i < unActivatedChanges.length; ++i) {
            Change chg = unActivatedChanges[i];
            if (chg.isRestartRequired()) {
               if (beans.isEmpty()) {
                  beans.put(chg.getBean(), chg.getAttributeName());
               } else {
                  String attrNames = (String)beans.get(chg.getBean());
                  if (attrNames == null) {
                     beans.put(chg.getBean(), chg.getAttributeName());
                  } else {
                     attrNames = attrNames + ", " + chg.getAttributeName();
                     beans.put(chg.getBean(), attrNames);
                  }
               }
            }
         }

         if (!beans.isEmpty()) {
            this.ctx.println(this.txtFmt.getNonDynamicAttributes());
            this.ctx.isRestartRequired = true;
            Iterator iter = beans.keySet().iterator();

            while(iter.hasNext()) {
               Object bean = iter.next();
               this.ctx.println(this.txtFmt.getMBeanChanged(this.asString(bean)));
               this.ctx.println(this.txtFmt.getAttributesChanged(this.asString(beans.get(bean))));
               this.ctx.println("");
            }

         }
      }
   }

   public void undo(String unactivatedChanges) throws ScriptException {
      this.ctx.commandType = "undo";
      this.validateTree();
      if (this.validateCall("undo")) {
         try {
            if (unactivatedChanges.equals("true")) {
               this.ctx.configurationManager.undoUnactivatedChanges();
               this.ctx.println(this.txtFmt.getDiscardedAllChanges());
            } else {
               this.ctx.configurationManager.undo();
               this.ctx.println(this.txtFmt.getDiscardedAllInMemoryChanges());
            }
         } catch (NotEditorException var3) {
            this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var3);
         }

      }
   }

   public void cancelEdit() throws ScriptException {
      this.ctx.commandType = "cancelEdit";
      this.validateTree();
      this.ctx.configurationManager.cancelEdit();
      this.ctx.resetEditSession();
   }

   public boolean isRestartRequired(String attrName) throws ScriptException {
      this.ctx.commandType = "isRestartRequired";

      try {
         boolean displayed = false;
         int i;
         if (attrName == null) {
            if (this.ctx.isEditSessionInProgress) {
               Change[] changes = this.ctx.configurationManager.getUnactivatedChanges();
               boolean restartRequired = false;

               for(i = 0; i < changes.length; ++i) {
                  if (changes[i].isRestartRequired()) {
                     if (!displayed) {
                        this.ctx.println(this.txtFmt.getRestartRequired());
                     }

                     restartRequired = true;
                     displayed = true;
                  }
               }

               if (restartRequired) {
                  this.printServerRestartInfo(changes);
                  return true;
               }

               this.ctx.println(this.txtFmt.getRestartNotRequired());
               return false;
            }

            return this.ctx.isRestartRequired;
         }

         this.validateTree();
         ModelMBeanInfo modelInfo = (ModelMBeanInfo)this.ctx.getMBeanInfo(this.ctx.wlcmo);
         ModelMBeanAttributeInfo[] attrInfos = (ModelMBeanAttributeInfo[])((ModelMBeanAttributeInfo[])modelInfo.getAttributes());

         for(i = 0; i < attrInfos.length; ++i) {
            ModelMBeanAttributeInfo info = attrInfos[i];
            Descriptor desc;
            if (info.getName().equals(attrName)) {
               desc = info.getDescriptor();
               Boolean dyn = (Boolean)desc.getFieldValue("com.bea.dynamic");
               if (dyn != null && dyn) {
                  this.ctx.println(this.txtFmt.getRestartNotRequiredFor(attrName));
                  return false;
               }

               this.ctx.println(this.txtFmt.getRestartRequiredFor(attrName));
               return true;
            }

            if (attrName.equals("*")) {
               displayed = true;
               desc = info.getDescriptor();
               String _attrName = info.getName();
               Boolean dyn = (Boolean)desc.getFieldValue("com.bea.dynamic");
               if (dyn != null && dyn) {
                  this.ctx.println(this.txtFmt.getRestartNotRequiredFor(_attrName));
               }

               this.ctx.println(this.txtFmt.getRestartRequiredFor(_attrName));
            }
         }

         if (!displayed) {
            this.ctx.println(this.txtFmt.getAttributeNotFound(attrName));
         }
      } catch (Throwable var10) {
         this.ctx.throwWLSTException(this.txtFmt.getErrorGettingRestartInfo(), var10);
      }

      return false;
   }

   public boolean validate() throws ScriptException {
      this.ctx.commandType = "validate";

      try {
         this.validateTree();
         if (!this.validateCall("validate")) {
            return false;
         }

         this.ctx.println(this.txtFmt.getValidatingChanges());
         this.ctx.configurationManager.validate();
         this.ctx.println(this.txtFmt.getValidationSuccess());
         return true;
      } catch (NotEditorException var2) {
         this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var2);
      } catch (ValidationException var3) {
         this.ctx.throwWLSTException(this.txtFmt.getValidationErrors(), var3);
      }

      return false;
   }

   public void stopEdit() throws ScriptException {
      this.ctx.commandType = "stopEdit";
      this.validateTree();
      if (this.validateCall("stopEdit")) {
         try {
            this.ctx.configurationManager.stopEdit();
            this.ctx.resetEditSession();
         } catch (NotEditorException var2) {
            this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var2);
         }

      }
   }

   public void showChanges(String onlyInMemory) throws ScriptException {
      this.ctx.commandType = "showChanges";
      this.validateTree();
      if (this.validateCall("showChanges")) {
         try {
            Change[] unSavedChanges = this.ctx.configurationManager.getChanges();
            if (onlyInMemory.equals("true")) {
               this.ctx.println(this.txtFmt.getUnsavedChangesAre());
               this.printChanges(unSavedChanges);
               return;
            }

            Change[] unActivatedChanges = this.ctx.configurationManager.getUnactivatedChanges();
            if (unActivatedChanges != null && unActivatedChanges.length > 0) {
               this.ctx.println("");
               this.ctx.println(this.txtFmt.getUnactivatedChangesAre());
               this.printChanges(unActivatedChanges);
            }
         } catch (NotEditorException var4) {
            this.ctx.throwWLSTException(this.txtFmt.getNoChangesYet(), var4);
         }

      }
   }

   private void printChanges(Change[] changes) {
      for(int i = 0; i < changes.length; ++i) {
         Change change = changes[i];
         this.ctx.println("");
         this.printChange(change);
         this.ctx.println("");
      }

   }

   private void printChange(Change change) {
      this.ctx.println(this.txtFmt.getMBeanChanged2(this.asString(change.getBean())));
      this.ctx.println(this.txtFmt.getOperationInvoked(this.asString(change.getOperation())));
      this.ctx.println(this.txtFmt.getAttributeModified(this.asString(change.getAttributeName())));
      this.ctx.println(this.txtFmt.getAttributesOldValue(this.asString(change.getOldValue())));
      this.ctx.println(this.txtFmt.getAttributesNewValue(this.asString(change.getNewValue())));
      this.ctx.println(this.txtFmt.getServerRestartRequired(this.asString(change.isRestartRequired())));
   }

   private String asString(boolean b) {
      return b ? "true" : "false";
   }

   private String asString(Object o) {
      return o == null ? "null" : o.toString();
   }

   public ActivationTaskMBean getActivationTask() throws ScriptException {
      this.ctx.commandType = "getActivationTask";
      this.validateTree();
      return this.ctx.activationTask;
   }
}
