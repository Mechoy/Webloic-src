package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMRuntimeException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.jmx.modelmbean.WLSModelMBean;
import weblogic.management.provider.ActivateTask;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditChangesValidationException;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.EditSaveChangesFailedException;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.UpdateException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class EditServiceInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("CompatabilityMBeanServer");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private WLSMBeanServer wlsMBeanServer;
   private EditAccess editAccess;

   public EditServiceInterceptor(EditAccess var1, WLSMBeanServer var2) {
      this.editAccess = var1;
      this.wlsMBeanServer = var2;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      ObjectInstance var3 = null;
      boolean var4 = this.editRequired(var2);
      if (var4) {
         synchronized(this) {
            try {
               this.startEdit();
               var3 = super.createMBean(var1, var2);
               this.completeEdit((Method)null);
               var4 = false;
            } finally {
               if (var4) {
                  this.stopEdit();
               }

            }
         }
      } else {
         var3 = super.createMBean(var1, var2);
      }

      return var3;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      boolean var4 = this.editRequired(var2);
      ObjectInstance var5 = null;
      if (var4) {
         synchronized(this) {
            try {
               this.startEdit();
               var5 = super.createMBean(var1, var2, var3);
               this.completeEdit((Method)null);
               var4 = false;
            } finally {
               if (var4) {
                  this.stopEdit();
               }

            }
         }
      } else {
         var5 = super.createMBean(var1, var2, var3);
      }

      return var5;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      boolean var5 = this.editRequired(var2);
      ObjectInstance var6 = null;
      if (var5) {
         synchronized(this) {
            try {
               this.startEdit();
               var6 = super.createMBean(var1, var2, var3, var4);
               this.completeEdit((Method)null);
               var5 = false;
            } finally {
               if (var5) {
                  this.stopEdit();
               }

            }
         }
      } else {
         var6 = super.createMBean(var1, var2, var3, var4);
      }

      return var6;
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      boolean var6 = this.editRequired(var2);
      ObjectInstance var7 = null;
      if (var6) {
         synchronized(this) {
            try {
               this.startEdit();
               var7 = super.createMBean(var1, var2, var3, var4, var5);
               this.completeEdit((Method)null);
               var6 = false;
            } finally {
               if (var6) {
                  this.stopEdit();
               }

            }
         }
      } else {
         var7 = super.createMBean(var1, var2, var3, var4, var5);
      }

      return var7;
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      super.unregisterMBean(var1);
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      boolean var3 = this.editRequired(var1);
      this.checkForMSConfigModification();
      if (var2 != null) {
         Object var4 = var2.getValue();
         if (var4 != null && var4.toString().length() == 0 && this.wlsMBeanServer.isInstanceOf(var1, "weblogic.management.WebLogicMBean")) {
            return;
         }
      }

      Method var16 = null;
      WLSModelMBean var5 = this.lookupWLSModelMBean(var1);
      if (var5 != null) {
         var16 = var5.getSetterMethod(var2.getName());
      }

      try {
         if (var3) {
            synchronized(this) {
               try {
                  this.startEdit();
                  super.setAttribute(var1, var2);
                  this.completeEdit(var16);
                  var3 = false;
               } finally {
                  if (var3) {
                     this.stopEdit();
                  }

               }
            }
         } else {
            super.setAttribute(var1, var2);
         }

      } catch (MBeanException var15) {
         Throwable var7 = var15.getCause();
         Throwable var8;
         InvalidAttributeValueException var9;
         if (var7 != null && (var7 instanceof EditFailedException || var7 instanceof UpdateException)) {
            if (debug.isDebugEnabled()) {
               debug.debug("Got Edit failure - see if should be mapped to InvalidAttributeValueException: ", var7);
            }

            var8 = var7.getCause();
            if (var8 != null && var8 instanceof BeanUpdateRejectedException) {
               var9 = new InvalidAttributeValueException(var7.getMessage());
               var9.initCause(var8);
               throw var9;
            }
         }

         if (var7 != null && var7 instanceof ModuleException) {
            if (debug.isDebugEnabled()) {
               debug.debug("Got Edit failure - see if should be mapped to InvalidAttributeValueException: ", var7);
            }

            var8 = var7.getCause();
            if (var8 != null && var8 instanceof IllegalArgumentException) {
               var9 = new InvalidAttributeValueException(var7.getMessage());
               var9.initCause(var8);
               throw var9;
            }
         }

         InvalidAttributeValueException var17;
         if (var7 != null && var7 instanceof IllegalArgumentException) {
            if (debug.isDebugEnabled()) {
               debug.debug("Got IllegalArgumentException - mapping to InvalidAttributeValueException: ", var15);
            }

            var17 = new InvalidAttributeValueException(var7.getMessage());
            var17.initCause(var7);
            throw var17;
         } else if (var7 != null && var7 instanceof EditChangesValidationException) {
            if (debug.isDebugEnabled()) {
               debug.debug("Got EditChangesValidationException - mapping to InvalidAttributeValueException: ", var15);
            }

            var17 = new InvalidAttributeValueException(var7.getMessage());
            var17.initCause(var7);
            throw var17;
         } else if (var7 != null && var7 instanceof BeanUpdateRejectedException) {
            if (debug.isDebugEnabled()) {
               debug.debug("Got BeanUpdateRejectedException - mapping to InvalidAttributeValueException: ", var15);
            }

            var17 = new InvalidAttributeValueException(var7.getMessage());
            var17.initCause(var7);
            throw var17;
         } else {
            if (debug.isDebugEnabled()) {
               debug.debug("Got exception - could not map exception.", var15);
            }

            throw var15;
         }
      }
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      try {
         this.checkForMSConfigModification();
         boolean var3 = this.editRequired(var1);
         AttributeList var4 = null;
         if (var3) {
            synchronized(this) {
               try {
                  this.startEdit();
                  var4 = super.setAttributes(var1, var2);
                  this.completeEdit((Method)null);
                  var3 = false;
               } finally {
                  if (var3) {
                     this.stopEdit();
                  }

               }
            }
         } else {
            var4 = super.setAttributes(var1, var2);
         }

         return var4;
      } catch (MBeanException var13) {
         throw (JMRuntimeException)(new JMRuntimeException(var13.getMessage())).initCause(var13);
      }
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      boolean var5 = false;
      boolean var6 = false;
      if (!this.isAdminMBean(var1)) {
         return super.invoke(var1, var2, var3, var4);
      } else {
         WLSModelMBean var7 = this.lookupWLSModelMBean(var1);
         Method var8 = null;
         String var10;
         if (var7 != null) {
            String var9 = var7.getRole(var2, var3, var4);
            if (var9 != null) {
               if (!var9.equals("factory") && !var9.equals("collection")) {
                  if (var9.equals("operation")) {
                     var10 = var7.getImpact(var2, var3, var4);
                     if (var10 != null && !var10.equals("info")) {
                        var5 = true;
                     }

                     if (var10 == null) {
                        var6 = true;
                     }
                  }
               } else {
                  var5 = true;
               }
            } else {
               var6 = true;
            }

            var8 = var7.getMethod(var2, var4);
         }

         Object var19;
         if (!var5) {
            if (var6) {
               boolean var18 = this.editAccess.isModified();
               var19 = super.invoke(var1, var2, var3, var4);
               if (!var18 && this.editAccess.isModified()) {
                  ManagementLogger.logCompatibilityInvokeModifiedConfig("" + var1, var2);
               }

               return var19;
            } else {
               return super.invoke(var1, var2, var3, var4);
            }
         } else {
            synchronized(this) {
               var10 = null;
               this.startEdit();

               Object var11;
               try {
                  var19 = super.invoke(var1, var2, var3, var4);
                  this.completeEdit(var8);
                  var5 = false;
                  var11 = var19;
               } finally {
                  if (var5) {
                     this.stopEdit();
                  }

               }

               return var11;
            }
         }
      }
   }

   private WLSModelMBean lookupWLSModelMBean(ObjectName var1) {
      Object var2 = this.wlsMBeanServer.lookupObject(var1);
      if (var2 != null && var2 instanceof WLSModelMBean) {
         WLSModelMBean var3 = (WLSModelMBean)var2;
         return var3;
      } else {
         return null;
      }
   }

   private boolean editRequired(ObjectName var1) {
      return this.isAdminMBean(var1);
   }

   private void startEdit() throws MBeanException {
      try {
         this.editAccess.startEdit(-1, 300000);
         if (this.editAccess.isPendingChange()) {
            Loggable var1 = ManagementLogger.logCompatibilityWithPendingChangesLoggable();
            var1.log();

            try {
               this.editAccess.stopEdit();
            } catch (Exception var3) {
            }

            throw new EditFailedException(var1.getMessage());
         }
      } catch (EditWaitTimedOutException var4) {
         throw new MBeanException(var4, "Unable to modify the configuration");
      } catch (EditFailedException var5) {
         throw new MBeanException(var5, "Unable to modify the configuration");
      }
   }

   private void rethrowException(Exception var1, Method var2) throws MBeanException {
      if (debug.isDebugEnabled()) {
         debug.debug("About to rethrow exception.", var1);
      }

      if (var1 instanceof MBeanException) {
         throw (MBeanException)var1;
      } else if (var2 == null) {
         throw new MBeanException(var1);
      } else {
         Class[] var3 = var2.getExceptionTypes();
         if (var3 != null && var3.length != 0) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               Class var5 = var3[var4];
               if (var5.equals(DistributedManagementException.class)) {
                  ArrayList var6 = new ArrayList(1);
                  var6.add(var1);
                  DistributedManagementException var7 = new DistributedManagementException(var6);
                  throw new MBeanException(var7);
               }
            }

            throw new MBeanException(var1);
         } else {
            throw new MBeanException(var1);
         }
      }
   }

   private void completeEdit(Method var1) throws MBeanException {
      try {
         boolean var2 = true;

         try {
            this.editAccess.saveChanges();
            ActivateTask var3 = this.editAccess.activateChangesAndWaitForCompletion(180000L);
            var2 = false;
            if (var3 != null && var3.getError() != null) {
               this.undoEdit();
               this.rethrowException(var3.getError(), var1);
            }
         } finally {
            if (var2) {
               this.undoEdit();
            }

         }
      } catch (EditChangesValidationException var11) {
         this.rethrowException(var11, var1);
      } catch (EditSaveChangesFailedException var12) {
         this.rethrowException(var12, var1);
      } catch (EditFailedException var13) {
         this.rethrowException(var13, var1);
      } catch (EditNotEditorException var14) {
         this.rethrowException(var14, var1);
      }

   }

   private void stopEdit() {
      try {
         if (this.editAccess.isEditor()) {
            this.editAccess.stopEdit();
         }
      } catch (EditFailedException var2) {
      } catch (EditNotEditorException var3) {
      }

   }

   private void undoEdit() {
      try {
         if (this.editAccess.getEditor() != null && this.editAccess.isPendingChange()) {
            this.editAccess.undoUnactivatedChanges();
         }
      } catch (Throwable var2) {
         ManagementLogger.logErrorUndoCompatibility(var2);
      }

   }

   private boolean isAdminMBean(ObjectName var1) {
      return var1.getKeyProperty("Location") == null;
   }

   private void checkForMSConfigModification() throws MBeanException {
      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         ManagementException var1 = new ManagementException("Attempt to modify Managed Server Configuration");
         throw new MBeanException(var1);
      }
   }
}
