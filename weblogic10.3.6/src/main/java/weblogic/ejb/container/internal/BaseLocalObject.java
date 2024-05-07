package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import javax.ejb.AccessLocalException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.transaction.Transaction;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb20.interfaces.LocalHandle;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.transaction.RollbackException;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.StackTraceUtils;

public abstract class BaseLocalObject {
   protected static final DebugLogger debugLogger;
   protected BeanManager beanManager;
   protected BeanInfo beanInfo;
   protected BaseEJBLocalHome ejbLocalHome;
   protected int txRetryCount;
   private boolean isEJB30ClientView = true;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -4972824511795923466L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.BaseLocalObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public void setIsEJB30ClientView(boolean var1) {
      this.isEJB30ClientView = var1;
   }

   public boolean isEJB30ClientView() {
      return this.isEJB30ClientView;
   }

   public String toString() {
      return this.beanInfo != null ? "[BaseLocalObject]  for EJB: '" + this.beanInfo.getEJBName() + "'" : "[BaseLocalObject]  for EJB: ' '";
   }

   public void setBeanManager(BeanManager var1) {
      this.beanManager = var1;
   }

   public BeanManager getBeanManager() {
      return this.beanManager;
   }

   public void setBeanInfo(BeanInfo var1) {
      this.beanInfo = var1;
   }

   protected void checkMethodPermissions(MethodDescriptor var1, EJBContextHandler var2) {
      if (this.isEJB30ClientView) {
         var1.checkMethodPermissionsBusiness(var2);
      } else {
         var1.checkMethodPermissionsLocal(var2);
      }

   }

   void setEJBLocalHome(BaseEJBLocalHome var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Setting home to :" + var1 + " in elo:" + this);
      }

      this.ejbLocalHome = var1;
      this.beanManager = var1.getBeanManager();
   }

   protected EJBLocalHome getEJBLocalHome(MethodDescriptor var1) {
      if (!$assertionsDisabled && this.ejbLocalHome == null) {
         throw new AssertionError();
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("Getting home in elo:" + this);
         }

         this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
         return this.ejbLocalHome;
      }
   }

   protected Object getPrimaryKey(MethodDescriptor var1) throws EJBException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      return this.getPrimaryKeyObject();
   }

   protected boolean isIdentical(MethodDescriptor var1, EJBLocalObject var2) throws EJBException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[]{var2}));
      if (var2 == null) {
         return false;
      } else {
         String var3 = this.ejbLocalHome.getIsIdenticalKey();
         String var4 = ((BaseEJBLocalHomeIntf)var2.getEJBLocalHome()).getIsIdenticalKey();
         if (!$assertionsDisabled && var3 == null) {
            throw new AssertionError();
         } else if (!$assertionsDisabled && var4 == null) {
            throw new AssertionError();
         } else {
            return var3.equals(var4);
         }
      }
   }

   protected LocalHandle getLocalHandle(MethodDescriptor var1) throws EJBException {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in elo:" + this);
      }

      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      return this.getLocalHandleObject();
   }

   protected Object getPrimaryKeyObject() throws EJBException {
      throw new AssertionError("Method not supported by this object type");
   }

   public LocalHandle getLocalHandleObject() throws EJBException {
      throw new AssertionError("Method not supported by this object type");
   }

   protected abstract InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws EJBException;

   protected InvocationWrapper preInvokeLite(InvocationWrapper var1, ContextHandler var2) throws EJBException {
      return this.preInvoke(var1, var2, true, (AuthenticatedSubject)null);
   }

   protected InvocationWrapper preInvoke(InvocationWrapper var1, ContextHandler var2) throws EJBException {
      return this.preInvoke(var1, var2, false, (AuthenticatedSubject)null);
   }

   protected InvocationWrapper preInvoke(InvocationWrapper var1, ContextHandler var2, AuthenticatedSubject var3) throws EJBException {
      return this.preInvoke(var1, var2, false, var3);
   }

   protected InvocationWrapper preInvoke(InvocationWrapper var1, ContextHandler var2, boolean var3, AuthenticatedSubject var4) throws EJBException {
      boolean var5 = false;
      MethodDescriptor var6 = var1.getMethodDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debug("preInvoke called with:" + var1 + " on:" + this + "\n");
      }

      if (!var3) {
         this.pushEnvironment();
         SecurityHelper.pushCallerPrincipal();
      }

      MethodInvocationHelper.pushMethodObject(this.beanInfo);
      Transaction var7 = var1.getInvokeTx();

      AccessLocalException var8;
      Throwable var25;
      try {
         if (!$assertionsDisabled && this.beanManager == null) {
            throw new AssertionError();
         } else {
            var8 = null;

            EnterpriseBean var24;
            try {
               try {
                  var24 = this.beanManager.preInvoke(var1);
               } catch (InternalException var17) {
                  if (!this.isEJB30ClientView) {
                     if (var17.detail instanceof NoSuchEJBException) {
                        NoSuchObjectLocalException var10 = new NoSuchObjectLocalException(var17.getMessage());
                        EJBRuntimeUtils.throwInternalException(var17.getMessage(), var10);
                     }

                     if (var17.detail instanceof ConcurrentAccessException) {
                        throw new InternalException(var17.getMessage());
                     }
                  }

                  throw var17;
               }
            } catch (InternalException var18) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Manager's preInvoke threw " + var18);
               }

               this.handleSystemException(var1, var18);
               throw new AssertionError("Should never reach here");
            } catch (Throwable var19) {
               this.handleSystemException(var1, var19);
               throw new AssertionError("Should never reach here");
            }

            var1.setBean(var24);
            ((EJBContextHandler)var2).setEjb(var24);
            this.checkMethodPermissions(var6, (EJBContextHandler)var2);
            if (!var3) {
               var6.pushRunAsIdentity(var4);
               var5 = true;
            }

            this.initTxRetryCount(var1);
            if (debugLogger.isDebugEnabled()) {
               debug("Manager.preInvoke returned a bean:" + var24 + "\n");
            }

            return var1;
         }
      } catch (EJBAccessException var20) {
         EJBAccessException var23 = var20;
         if (debugLogger.isDebugEnabled()) {
            debug("Method permission has been denied.  Do postInvoke sequence. \n" + var20);
         }

         if (!var3 && !var5) {
            var6.pushRunAsIdentity();
         }

         var25 = null;

         try {
            this.postInvoke1(0, var1, var23);
         } catch (Throwable var14) {
            var25 = var14;
         }

         try {
            this.postInvokeCleanup(var1, var25, var3);
         } catch (Exception var16) {
            if (var16 instanceof EJBAccessException) {
               throw (EJBAccessException)var16;
            }

            if (var16 instanceof EJBException) {
               throw (EJBException)var16;
            }

            EJBRuntimeUtils.throwEJBException("EJB Exception after method permission failure: ", var16);
         }

         throw var20;
      } catch (AccessLocalException var21) {
         var8 = var21;
         if (debugLogger.isDebugEnabled()) {
            debug("Method permission check failed.  Do postInvoke sequence. \n" + var21);
         }

         if (!var3 && !var5) {
            var6.pushRunAsIdentity();
         }

         var25 = null;

         try {
            this.postInvoke1(0, var1, var8);
         } catch (Throwable var13) {
            var25 = var13;
         }

         try {
            this.postInvokeCleanup(var1, var25, var3);
         } catch (Exception var15) {
            if (var15 instanceof AccessLocalException) {
               throw (AccessLocalException)var15;
            }

            if (var15 instanceof EJBException) {
               throw (EJBException)var15;
            }

            EJBRuntimeUtils.throwEJBException("EJB Exception after method permission failure: ", var15);
         }

         throw var21;
      } catch (EJBException var22) {
         if (!var3) {
            this.popEnvironment();
            if (var5) {
               var6.popRunAsIdentity();
            }

            try {
               SecurityHelper.popCallerPrincipal();
            } catch (PrincipalNotFoundException var12) {
               EJBLogger.logErrorPoppingCallerPrincipal(var12);
            }
         }

         MethodInvocationHelper.popMethodObject(this.beanInfo);

         try {
            Transaction var9 = var1.getCallerTx();
            EJBRuntimeUtils.resumeCallersTransaction(var9, var7);
         } catch (InternalException var11) {
         }

         throw var22;
      }
   }

   private void retryPreInvoke(InvocationWrapper var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("retryPreInvoke entered \n");
      }

      if (this.isEJB30ClientView) {
         EJBRuntimeUtils.setWrapWithTxsForBus(var1);
      } else {
         EJBRuntimeUtils.setWrapWithTxs(var1);
      }

      EnterpriseBean var2 = null;

      try {
         try {
            var2 = this.beanManager.preInvoke(var1);
         } catch (InternalException var5) {
            if (!this.isEJB30ClientView) {
               if (var5.detail instanceof NoSuchEJBException) {
                  NoSuchObjectLocalException var4 = new NoSuchObjectLocalException(var5.getMessage());
                  EJBRuntimeUtils.throwInternalException(var5.getMessage(), var4);
               }

               if (var5.detail instanceof ConcurrentAccessException) {
                  throw new InternalException(var5.getMessage());
               }
            }

            throw var5;
         }
      } catch (InternalException var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("Manager's preInvoke threw " + var6);
         }

         this.handleSystemException(var1, var6);
         throw new AssertionError("Should never reach here");
      } catch (Throwable var7) {
         this.handleSystemException(var1, var7);
         throw new AssertionError("Should never reach here");
      }

      var1.setBean(var2);
   }

   protected boolean __WL_postInvokeTxRetry(InvocationWrapper var1, Throwable var2) throws Exception {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isEnabledAndNotDyeFiltered()) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var3 = this.postInvoke1(this.getNextTxRetryCount(), var1, var2);
      if (var3) {
         this.retryPreInvoke(var1);
         var2 = null;
      }

      if (debugLogger.isDebugEnabled()) {
         debug("__WL_postInvokeTxRetry returning with retry = " + var3 + "\n");
      }

      return var3;
   }

   public boolean postInvoke1(int var1, InvocationWrapper var2, Throwable var3) throws Exception {
      MethodDescriptor var4 = var2.getMethodDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debug("postInvoke1 called with txRetryCount = " + var1 + "\n" + "wrap:" + var2 + "\n Exception: " + var3 + " on: " + this + "\n");
         if (null != var3) {
            ((Throwable)var3).printStackTrace();
         }
      }

      Transaction var5 = var2.getInvokeTx();
      Transaction var6 = var2.getCallerTx();
      Method var7 = var4.getMethod();
      boolean var8 = false;
      if (var3 != null && !EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var7, (Throwable)var3) && !(var3 instanceof AccessLocalException) && !(var3 instanceof EJBAccessException)) {
         var8 = true;
      } else {
         try {
            this.beanManager.postInvoke(var2);
         } catch (InternalException var15) {
            var3 = var15;
            var8 = true;
         }
      }

      if (var8) {
         this.beanManager.destroyInstance(var2, (Throwable)var3);
         this.handleSystemException(var2, (Throwable)var3);
         throw new AssertionError("Should never be reached");
      } else if (!(var3 instanceof AccessLocalException) && !(var3 instanceof EJBAccessException)) {
         if (var2.getInvokeTx() == null) {
            try {
               this.getBeanManager().beforeCompletion(var2);
               this.getBeanManager().afterCompletion(var2);
            } catch (InternalException var16) {
               if (EJBRuntimeUtils.isAppException(var7, var16)) {
                  throw (Exception)var16.detail;
               }

               this.handleSystemException(var2, var16);
               throw new AssertionError("Should never have reached here");
            }
         } else if (this instanceof StatefulLocalObject && this.beanInfo instanceof Ejb3SessionBeanInfo) {
            WLSessionBean var9 = (WLSessionBean)var2.getBean();
            boolean var10 = ((Ejb3SessionBeanInfo)this.beanInfo).isRemoveMethod(var7);
            boolean var11 = ((Ejb3SessionBeanInfo)this.beanInfo).isRetainifException(var7);
            if (var10 && (var3 == null || EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var7, (Throwable)var3) && !var11)) {
               var9.__WL_setNeedsSessionSynchronization(false);
            }
         }

         if (EJBRuntimeUtils.runningInOurTx(var2)) {
            if (EJBRuntimeUtils.isRollback(var2)) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" our tx marked for Rollback, attempt to rollback ");
               }

               try {
                  var5.rollback();
                  if (debugLogger.isDebugEnabled()) {
                     debug(" Rollback succeeded. ");
                  }
               } catch (Exception var14) {
                  EJBLogger.logErrorDuringRollback1(var5.toString(), StackTraceUtils.throwable2StackTrace(var14));
               }

               if (!EJBRuntimeUtils.isSystemRollback(var2)) {
                  return this.isTxRetry(var1);
               }

               if (debugLogger.isDebugEnabled()) {
                  debug(" system Rollback, throw exception");
               }

               Throwable var18 = ((TransactionImpl)var5).getRollbackReason();
               EJBRuntimeUtils.throwEJBException("Transaction Rolledback.", var18);
            } else {
               try {
                  if (var3 != null && this.isEJB30ClientView && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), (Throwable)var3)) {
                     if (debugLogger.isDebugEnabled()) {
                        debug(var3.getClass().getName() + " is thrown, attempt to rollback ");
                     }

                     try {
                        var5.rollback();
                        if (debugLogger.isDebugEnabled()) {
                           debug(" Rollback succeeded. ");
                        }
                     } catch (Exception var13) {
                        EJBLogger.logErrorDuringRollback1(var5.toString(), StackTraceUtils.throwable2StackTrace(var13));
                     }

                     return this.isTxRetry(var1);
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debug("Committing our tx: " + var5 + "\n");
                  }

                  var5.commit();
                  if (debugLogger.isDebugEnabled()) {
                     debug("Committing our tx: SUCCESS\n");
                  }
               } catch (Exception var17) {
                  if (var17 instanceof RollbackException) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("Committing our tx: ROLLBACK\n");
                     }

                     RollbackException var19 = (RollbackException)var17;
                     if (!EJBRuntimeUtils.isOptimisticLockException(var19.getNested())) {
                        EJBLogger.logErrorDuringCommit(var5.toString(), StackTraceUtils.throwable2StackTrace(var17));
                     }

                     if (var1 > 0) {
                        return this.isTxRetry(var1);
                     }
                  }

                  EJBRuntimeUtils.throwEJBException("Error committing transaction:", var17);
               }
            }
         } else if (EJBRuntimeUtils.runningInCallerTx(var2) && var3 != null && this.isEJB30ClientView && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), (Throwable)var3)) {
            if (debugLogger.isDebugEnabled()) {
               debug(" caller tx marked for Rollback, attempt to rollback ");
            }

            try {
               var6.setRollbackOnly();
               if (debugLogger.isDebugEnabled()) {
                  debug(" SetRollbackOnly succeeded. ");
               }
            } catch (Exception var12) {
               EJBLogger.logExcepDuringSetRollbackOnly(var12);
            }
         }

         return false;
      } else {
         this.handleSystemException(var2, (Throwable)var3);
         throw new AssertionError("Should never be reached");
      }
   }

   public void __WL_postInvokeCleanup(InvocationWrapper var1, Throwable var2) throws Exception {
      this.postInvokeCleanup(var1, var2, false);
   }

   public void __WL_postInvokeCleanupLite(InvocationWrapper var1, Throwable var2) throws Exception {
      this.postInvokeCleanup(var1, var2, true);
   }

   public void postInvokeCleanup(InvocationWrapper var1, Throwable var2, boolean var3) throws Exception {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      if (var10000) {
         Object[] var10 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{this, var1, var2, InstrumentationSupport.convertToObject(var3)};
         }

         DynamicJoinPoint var24 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var10, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var24, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         MethodDescriptor var4 = var1.getMethodDescriptor();

         try {
            if (!var3) {
               this.popEnvironment();
               var4.popRunAsIdentity();
               SecurityHelper.popCallerPrincipal();
            }

            if (MethodInvocationHelper.popMethodObject(this.beanInfo)) {
               this.beanManager.handleUncommittedLocalTransaction(var1);
            }

            if (var2 != null) {
               Method var5 = var1.getMethodDescriptor().getMethod();
               if (EJBRuntimeUtils.isAppException(var5, var2)) {
                  EJBRuntimeUtils.throwException(var2);
               } else {
                  if (var2 instanceof Exception) {
                     throw (Exception)var2;
                  }

                  if (!$assertionsDisabled && !(var2 instanceof Exception)) {
                     throw new AssertionError();
                  }

                  EJBRuntimeUtils.throwEJBException("EJB encountered System Exception: ", var2);
               }
            }
         } finally {
            Transaction var8 = var1.getCallerTx();
            EJBRuntimeUtils.resumeCallersTransaction(var8, var1.getInvokeTx());
         }
      } finally {
         if (var15) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High, var16, var17);
         }

      }

   }

   public void postInvoke(InvocationWrapper var1, Throwable var2) throws Exception {
      Throwable var3 = var2;

      try {
         this.postInvoke1(0, var1, var3);
      } catch (Throwable var5) {
         var3 = var5;
      }

      this.__WL_postInvokeCleanup(var1, var3);
   }

   private boolean isTxRetry(int var1) {
      if (var1 > 0) {
         if (debugLogger.isDebugEnabled()) {
            debug(" txRetryCount " + var1 + " return retry = true");
         }

         return true;
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" txRetryCount " + var1 + " return retry = false");
         }

         return false;
      }
   }

   private void initTxRetryCount(InvocationWrapper var1) {
      this.txRetryCount = var1.getMethodDescriptor().getRetryOnRollbackCount();
   }

   private int getNextTxRetryCount() {
      if (--this.txRetryCount < 0) {
         this.txRetryCount = 0;
      }

      return this.txRetryCount;
   }

   public void pushEnvironment() {
      EJBRuntimeUtils.pushEnvironment(this.beanManager.getEnvironmentContext());
   }

   public void popEnvironment() {
      EJBRuntimeUtils.popEnvironment();
   }

   protected void operationsComplete() {
   }

   protected void handleSystemException(InvocationWrapper var1, Throwable var2) {
      try {
         handleSystemException(var1, this.beanManager.usesBeanManagedTx(), var2);
      } catch (TransactionRolledbackLocalException var5) {
         if (this.isEJB30ClientView) {
            EJBTransactionRolledbackException var4 = null;
            if (var5.getCause() instanceof Exception) {
               var4 = new EJBTransactionRolledbackException(var5.getMessage(), (Exception)var5.getCause());
               var4.initCause(var5.getCause());
            } else {
               var4 = new EJBTransactionRolledbackException(var5.getMessage(), var5);
               var4.initCause(var5);
            }

            throw var4;
         } else {
            throw var5;
         }
      }
   }

   public static void handleSystemException(InvocationWrapper var0, boolean var1, Throwable var2) throws EJBException {
      Transaction var3 = var0.getInvokeTx();
      if (var1 && TxHelper.getTransaction() != null) {
         try {
            weblogic.transaction.Transaction var4 = TxHelper.getTransaction();
            if (var4.getStatus() == 0) {
               var4.rollback();
            }
         } catch (Exception var7) {
            EJBLogger.logStackTrace(var7);
            EJBLogger.logErrorOnRollback(var7);
         }

         if (var2 instanceof AccessLocalException) {
            throw (AccessLocalException)var2;
         }

         EJBRuntimeUtils.throwEJBException("EJB Exception: ", var2);
      } else if (var3 == null) {
         if (var2 instanceof AccessLocalException) {
            throw (AccessLocalException)var2;
         }

         EJBRuntimeUtils.throwEJBException("EJB Exception: ", var2);
      } else if (EJBRuntimeUtils.runningInOurTx(var0)) {
         try {
            var3.rollback();
         } catch (Exception var6) {
            EJBLogger.logStackTrace(var6);
            EJBLogger.logErrorOnRollback(var6);
         }

         if (var2 instanceof AccessLocalException) {
            throw (AccessLocalException)var2;
         }

         EJBRuntimeUtils.throwEJBException("EJB Exception: ", var2);
      } else {
         try {
            var3.setRollbackOnly();
         } catch (Exception var5) {
            EJBLogger.logErrorMarkingRollback(var5);
         }

         if (EJBRuntimeUtils.isSpecialSystemException(var2)) {
            EJBRuntimeUtils.throwEJBException("called setRollbackOnly", var2);
         }

         EJBRuntimeUtils.throwTransactionRolledbackLocal("EJB Exception: ", var2);
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseLocalObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Before_Low");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseLocalObject.java", "weblogic.ejb.container.internal.BaseLocalObject", "__WL_postInvokeTxRetry", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)Z", 455, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseLocalObject.java", "weblogic.ejb.container.internal.BaseLocalObject", "postInvokeCleanup", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;Z)V", 706, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null, null})}), (boolean)0);
      $assertionsDisabled = !BaseLocalObject.class.desiredAssertionStatus();
      debugLogger = EJBDebugService.invokeLogger;
   }
}
