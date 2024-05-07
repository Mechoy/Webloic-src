package weblogic.ejb.container.internal;

import javax.ejb.AccessLocalException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.NoSuchEJBException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb20.interfaces.LocalHandle;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.internal.LocalHandleImpl;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.security.service.ContextHandler;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;

public abstract class EntityEJBLocalObject extends BaseLocalObject implements BaseEJBLocalObjectIntf {
   private Object primaryKey;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -1643248204666032083L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.EntityEJBLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   protected final boolean isIdentical(MethodDescriptor var1, EJBLocalObject var2) throws EJBException {
      if (super.isIdentical(var1, var2)) {
         if (!(var2 instanceof EntityEJBLocalObject)) {
            return false;
         } else {
            EntityEJBLocalObject var3 = (EntityEJBLocalObject)var2;
            return this.primaryKey.equals(var3.getPrimaryKey());
         }
      } else {
         return false;
      }
   }

   public void setPrimaryKey(Object var1) {
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         this.primaryKey = var1;
      }
   }

   protected final Object getPrimaryKeyObject() {
      return this.primaryKey;
   }

   protected InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      boolean var13 = false;

      InvocationWrapper var10000;
      try {
         var13 = true;
         if (!$assertionsDisabled && this.primaryKey == null) {
            throw new AssertionError();
         }

         InvocationWrapper var3 = null;

         try {
            var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
         } catch (InternalException var14) {
            EJBRuntimeUtils.throwEJBException(var14);
            throw new weblogic.utils.AssertionError("Should not reach.");
         }

         var10000 = super.preInvoke(var3, var2);
         var13 = false;
      } finally {
         if (var13) {
            Object var10001 = null;
            if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
               DelegatingMonitor var10003 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
               InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10003, var10003.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low.isEnabledAndNotDyeFiltered()) {
         DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
      }

      return var10000;
   }

   protected InvocationWrapper __WL_preInvokeLite(MethodDescriptor var1, ContextHandler var2) throws EJBException {
      if (!$assertionsDisabled && this.primaryKey == null) {
         throw new AssertionError();
      } else {
         InvocationWrapper var3 = null;

         try {
            var3 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
         } catch (InternalException var5) {
            EJBRuntimeUtils.throwEJBException(var5);
            throw new weblogic.utils.AssertionError("Should not reach.");
         }

         return super.preInvokeLite(var3, var2);
      }
   }

   protected void remove(MethodDescriptor var1) throws RemoveException {
      boolean var20;
      boolean var10000 = var20 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var21 = null;
      DiagnosticActionState[] var22 = null;
      Object var19 = null;
      if (var10000) {
         Object[] var15 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isArgumentsCaptureNeeded()) {
            var15 = new Object[]{this, var1};
         }

         DynamicJoinPoint var67 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var15, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
         DiagnosticAction[] var10002 = var21 = var10001.getActions();
         InstrumentationSupport.preProcess(var67, var10001, var10002, var22 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         BaseEJBLocalHome var2 = (BaseEJBLocalHome)this.getEJBLocalHome();
         Transaction var3 = null;
         Transaction var4 = null;
         InvocationWrapper var5 = null;
         RemoveException var6 = null;

         try {
            var2.pushEnvironment();
            if (!var1.checkMethodPermissionsLocal(EJBContextHandler.EMPTY)) {
               SecurityException var7 = new SecurityException("Security violation: insufficient permission to access method");
               throw new AccessLocalException(var7.getMessage(), var7);
            }

            try {
               MethodInvocationHelper.pushMethodObject(var2.getBeanInfo());
               SecurityHelper.pushCallerPrincipal();
               var1.pushRunAsIdentity();
               var5 = EJBRuntimeUtils.createWrapWithTxs(var1, this.primaryKey);
               var4 = var5.getInvokeTx();
               var3 = var5.getCallerTx();

               try {
                  var2.getBeanManager().remove(var5);
               } catch (InternalException var62) {
                  if (!(var62.getCause() instanceof NoSuchEJBException) && !(var62.getCause() instanceof ConcurrentAccessException)) {
                     throw var62;
                  }

                  throw new InternalException(var62.getMessage());
               }
            } catch (InternalException var63) {
               if (!(var63.detail instanceof RemoveException)) {
                  var2.handleSystemException(var5, var63);
                  throw new weblogic.utils.AssertionError("Should not reach here");
               }

               var6 = (RemoveException)var63.detail;
            } finally {
               var1.popRunAsIdentity();

               try {
                  SecurityHelper.popCallerPrincipal();
               } catch (PrincipalNotFoundException var59) {
                  EJBLogger.logErrorPoppingCallerPrincipal(var59);
               }

            }

            try {
               if (MethodInvocationHelper.popMethodObject(var2.getBeanInfo())) {
                  var2.getBeanManager().handleUncommittedLocalTransaction(var5);
               }
            } catch (InternalException var61) {
               EJBRuntimeUtils.throwEJBException(var61);
            }

            if (EJBRuntimeUtils.runningInOurTx(var5)) {
               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Committing tx: " + var4);
                  }

                  var4.commit();
               } catch (Exception var60) {
                  EJBLogger.logErrorDuringCommit2(var4.toString(), StackTraceUtils.throwable2StackTrace(var60));
                  EJBRuntimeUtils.throwEJBException("Exception during commit:", var60);
               }
            }

            if (var6 != null) {
               throw var6;
            }
         } finally {
            var2.popEnvironment();

            try {
               EJBRuntimeUtils.resumeCallersTransaction(var3, var4);
            } catch (InternalException var58) {
               EJBRuntimeUtils.throwEJBException(var58);
            }

         }
      } finally {
         if (var20) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High, var21, var22);
         }

      }

   }

   public LocalHandle getLocalHandleObject() {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in eo:" + this);
      }

      return new LocalHandleImpl(this, this.primaryKey);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof EntityEJBLocalObject ? this.isIdentical((EntityEJBLocalObject)var1) : false;
      }
   }

   public int hashCode() {
      return this.primaryKey.hashCode();
   }

   public void operationsComplete() {
      weblogic.transaction.Transaction var1 = TxHelper.getTransaction();
      ((CachingManager)this.beanManager).operationsComplete(var1, this.primaryKey);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityEJBLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Preinvoke_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Preinvoke_After_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalObject.java", "weblogic.ejb.container.internal.EntityEJBLocalObject", "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;)Lweblogic/ejb/container/internal/InvocationWrapper;", 61, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Preinvoke_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalObject.java", "weblogic.ejb.container.internal.EntityEJBLocalObject", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;)V", 97, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !EntityEJBLocalObject.class.desiredAssertionStatus();
   }
}
