package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.EnterpriseBean;
import javax.ejb.Handle;
import javax.ejb.NoSuchEJBException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRolledbackException;
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
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.ejb20.interfaces.RemoteHome;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.kernel.ThreadLocalStack;
import weblogic.rmi.SupportsInterfaceBasedCallByReference;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.security.service.ContextHandler;
import weblogic.transaction.RollbackException;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public abstract class BaseRemoteObject implements SupportsInterfaceBasedCallByReference {
   protected static final DebugLogger debugLogger;
   protected BeanManager beanManager;
   protected ClientDrivenBeanInfo beanInfo;
   protected BaseEJBHome ejbHome;
   protected int txRetryCount;
   protected static final ThreadLocalStack currentInvocationWrapper;
   private boolean isImplementsRemote = true;
   private static ThreadLocalStack envStack;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -3749648514302390289L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.BaseRemoteObject");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public String toString() {
      return "[BaseRemoteObject] home: " + this.ejbHome;
   }

   public void setIsImplementsRemote(boolean var1) {
      this.isImplementsRemote = var1;
   }

   public boolean isImplementsRemote() {
      return this.isImplementsRemote;
   }

   public void setBeanManager(BeanManager var1) {
      this.beanManager = var1;
   }

   public BeanManager getBeanManager() {
      return this.beanManager;
   }

   public void setBeanInfo(BeanInfo var1) {
      this.beanInfo = (ClientDrivenBeanInfo)var1;
   }

   protected void checkMethodPermissions(MethodDescriptor var1, EJBContextHandler var2) throws AccessException {
      if (!this.isImplementsRemote) {
         var1.checkMethodPermissionsBusiness(var2);
      } else {
         var1.checkMethodPermissionsRemote(var2);
      }

   }

   void setEJBHome(BaseEJBHome var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Setting home to :" + var1 + " in eo:" + this);
      }

      this.ejbHome = var1;
   }

   protected EJBHome getEJBHome(MethodDescriptor var1) throws RemoteException {
      if (!$assertionsDisabled && this.ejbHome == null) {
         throw new AssertionError();
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("Getting home in eo:" + this);
         }

         this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
         return this.ejbHome;
      }
   }

   protected abstract Object getPrimaryKeyObject() throws RemoteException;

   protected abstract Handle getHandleObject() throws RemoteException;

   protected Object getPrimaryKey(MethodDescriptor var1) throws RemoteException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      return this.getPrimaryKeyObject();
   }

   protected Handle getHandle(MethodDescriptor var1) throws RemoteException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[0]));
      return this.getHandleObject();
   }

   protected boolean isIdentical(MethodDescriptor var1, EJBObject var2) throws RemoteException {
      this.checkMethodPermissions(var1, new EJBContextHandler(var1, new Object[]{var2}));
      if (var2 == null) {
         return false;
      } else {
         String var3 = this.ejbHome.getIsIdenticalKey();
         RemoteHome var4 = null;

         try {
            var4 = (RemoteHome)PortableRemoteObject.narrow(var2.getEJBHome(), RemoteHome.class);
         } catch (ClassCastException var6) {
            return false;
         }

         String var5 = var4.getIsIdenticalKey();
         return var3.equals(var5);
      }
   }

   protected abstract void remove(MethodDescriptor var1) throws RemoteException, RemoveException;

   protected abstract InvocationWrapper __WL_preInvoke(MethodDescriptor var1, ContextHandler var2) throws RemoteException;

   protected InvocationWrapper preInvoke(InvocationWrapper var1, ContextHandler var2) throws RemoteException {
      boolean var3 = false;
      MethodDescriptor var4 = var1.getMethodDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debug("preInvoke called with:" + var1 + " on:" + this);
      }

      this.pushEnvironment();
      MethodInvocationHelper.pushMethodObject(this.beanInfo);
      SecurityHelper.pushCallerPrincipal();
      Transaction var5 = var1.getInvokeTx();

      try {
         if (!$assertionsDisabled && this.beanManager == null) {
            throw new AssertionError();
         } else {
            EnterpriseBean var6 = null;
            this.pushInvocationWrapperInThreadLocal(var1);

            try {
               try {
                  var6 = this.beanManager.preInvoke(var1);
               } catch (InternalException var11) {
                  if (this.isImplementsRemote) {
                     if (var11.detail instanceof NoSuchEJBException) {
                        NoSuchObjectException var8 = new NoSuchObjectException(var11.getMessage());
                        EJBRuntimeUtils.throwInternalException(var11.getMessage(), var8);
                     }

                     if (var11.detail instanceof ConcurrentAccessException) {
                        Throwable var17 = var11.detail.getCause();
                        EJBRuntimeUtils.throwInternalException(var11.getMessage(), var17);
                     }
                  }

                  throw var11;
               }
            } catch (InternalException var12) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Manager's preInvoke threw " + var12);
               }

               this.handleSystemException(var1, var12);
            } catch (Throwable var13) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Manager's preInvoke threw " + var13);
               }

               this.handleSystemException(var1, var13);
            }

            var1.setBean(var6);
            this.setTxCreateAttributeOnBean(var1);
            ((EJBContextHandler)var2).setEjb(var6);
            this.checkMethodPermissions(var4, (EJBContextHandler)var2);
            var4.pushRunAsIdentity();
            var3 = true;
            this.initTxRetryCount(var1);
            if (debugLogger.isDebugEnabled()) {
               debug("Manager.preInvoke returned a bean:" + var6);
            }

            return var1;
         }
      } catch (EJBAccessException var14) {
         this.handleAccessExceptions(var14, var4, var3, var1);
         throw new weblogic.utils.AssertionError("Should never be reached");
      } catch (AccessException var15) {
         this.handleAccessExceptions(var15, var4, var3, var1);
         throw new weblogic.utils.AssertionError("Should never be reached");
      } catch (RemoteException var16) {
         this.popInvocationWrapperInThreadLocalOnError(var1);
         if (var3) {
            var4.popRunAsIdentity();
         }

         try {
            SecurityHelper.popCallerPrincipal();
         } catch (PrincipalNotFoundException var10) {
            EJBLogger.logErrorPoppingCallerPrincipal(var10);
         }

         MethodInvocationHelper.popMethodObject(this.beanInfo);
         this.popEnvironment();

         try {
            Transaction var7 = var1.getCallerTx();
            EJBRuntimeUtils.resumeCallersTransaction(var7, var5);
         } catch (InternalException var9) {
         }

         throw var16;
      }
   }

   private void handleAccessExceptions(Exception var1, MethodDescriptor var2, boolean var3, InvocationWrapper var4) throws RemoteException {
      if (debugLogger.isDebugEnabled()) {
         debug("Method permission check failed.  Do postInvoke sequence. \n" + var1);
      }

      if (!var3) {
         if (debugLogger.isDebugEnabled()) {
            debug("Method permission check failed. pushRunAsIdentity");
         }

         var2.pushRunAsIdentity();
      }

      Throwable var5 = null;

      try {
         this.postInvoke1(0, var4, var1);
      } catch (Throwable var8) {
         var5 = var8;
      }

      try {
         this.__WL_postInvokeCleanup(var4, var5);
      } catch (Exception var7) {
         EJBRuntimeUtils.throwRemoteException("EJB Exception after method permission failure: ", var7);
      }

   }

   protected abstract void pushInvocationWrapperInThreadLocal(InvocationWrapper var1);

   protected abstract void popInvocationWrapperInThreadLocalOnError(InvocationWrapper var1);

   protected abstract void notifyRemoteCallBegin();

   protected void setTxCreateAttributeOnBean(InvocationWrapper var1) {
   }

   private void retryPreInvoke(InvocationWrapper var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("retryPreInvoke entered \n");
      }

      if (!this.isImplementsRemote) {
         EJBRuntimeUtils.setWrapWithTxsForBus(var1);
      } else {
         EJBRuntimeUtils.setWrapWithTxs(var1);
      }

      EnterpriseBean var2 = null;

      try {
         try {
            var2 = this.beanManager.preInvoke(var1);
         } catch (InternalException var5) {
            if (this.isImplementsRemote) {
               if (var5.detail instanceof NoSuchEJBException) {
                  NoSuchObjectException var4 = new NoSuchObjectException(var5.getMessage());
                  EJBRuntimeUtils.throwInternalException(var5.getMessage(), var4);
               }

               if (var5.detail instanceof ConcurrentAccessException) {
                  Throwable var8 = var5.detail.getCause();
                  EJBRuntimeUtils.throwInternalException(var5.getMessage(), var8);
               }
            }

            throw var5;
         }
      } catch (InternalException var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("Manager's preInvoke threw " + var6);
         }

         this.handleSystemException(var1, var6);
         throw new weblogic.utils.AssertionError("Should never reach here");
      } catch (Throwable var7) {
         this.handleSystemException(var1, var7);
         throw new weblogic.utils.AssertionError("Should never reach here");
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
         debug("prePostInvoke called with txRetryCount = " + var1 + "\n" + " wrap:" + var2 + " Exception: " + var3 + " on: " + this);
         if (null != var3) {
            ((Throwable)var3).printStackTrace();
         }
      }

      Transaction var5 = var2.getInvokeTx();
      Transaction var6 = var2.getCallerTx();
      Method var7 = var4.getMethod();
      boolean var8 = false;
      if (var3 != null && !EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var7, (Throwable)var3) && !(var3 instanceof AccessException) && !(var3 instanceof EJBAccessException)) {
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
         if (RemoteException.class.equals(var3.getClass())) {
            var3 = new RemoteException("EJB Exception:", (Throwable)var3);
         }

         var2.setSystemExceptionOccured();
         this.beanManager.destroyInstance(var2, (Throwable)var3);
         this.handleSystemException(var2, (Throwable)var3);
         throw new weblogic.utils.AssertionError("Should never be reached");
      } else if (!(var3 instanceof AccessException) && !(var3 instanceof EJBAccessException)) {
         if (var2.getInvokeTx() == null) {
            try {
               this.getBeanManager().beforeCompletion(var2);
               this.getBeanManager().afterCompletion(var2);
            } catch (InternalException var16) {
               if (EJBRuntimeUtils.isAppException(var7, var16)) {
                  throw (Exception)var16.detail;
               }

               this.handleSystemException(var2, var16);
               throw new weblogic.utils.AssertionError("Should never have reached here");
            }
         } else if (this instanceof StatefulRemoteObject && this.beanInfo instanceof Ejb3SessionBeanInfo) {
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
               EJBRuntimeUtils.throwRemoteException("Transaction Rolledback.", var18);
            } else {
               try {
                  if (var3 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), (Throwable)var3)) {
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
                     debug("Committing tx: " + var5);
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

                  EJBRuntimeUtils.throwRemoteException(((weblogic.transaction.Transaction)var5).getXid().toString(), var17);
               }
            }
         } else if (EJBRuntimeUtils.runningInCallerTx(var2) && var3 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), (Throwable)var3)) {
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
         throw new weblogic.utils.AssertionError("Should never be reached");
      }
   }

   public void __WL_postInvokeCleanup(InvocationWrapper var1, Throwable var2) throws Exception {
      boolean var14;
      boolean var10000 = var14 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var15 = null;
      DiagnosticActionState[] var16 = null;
      Object var13 = null;
      if (var10000) {
         Object[] var9 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High.isArgumentsCaptureNeeded()) {
            var9 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var23 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High;
         DiagnosticAction[] var10002 = var15 = var10001.getActions();
         InstrumentationSupport.preProcess(var23, var10001, var10002, var16 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         MethodDescriptor var3 = var1.getMethodDescriptor();

         try {
            this.popEnvironment();
            var3.popRunAsIdentity();
            SecurityHelper.popCallerPrincipal();
            if (MethodInvocationHelper.popMethodObject(this.beanInfo)) {
               this.beanManager.handleUncommittedLocalTransaction(var1);
            }

            if (var2 != null) {
               Method var4 = var1.getMethodDescriptor().getMethod();
               if (EJBRuntimeUtils.isAppException(var4, var2)) {
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
            Transaction var7 = var1.getCallerTx();
            EJBRuntimeUtils.resumeCallersTransaction(var7, var1.getInvokeTx());
         }
      } finally {
         if (var14) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High, var15, var16);
         }

      }

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
      EnvInfo var1 = new EnvInfo();
      ClassLoader var2 = this.beanInfo.getClassLoader();
      if (var2 instanceof GenericClassLoader) {
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();
         if (var4 != var2) {
            var1.captureClassloader(var4);
            var3.setContextClassLoader(var2);
         }
      }

      envStack.push(var1);
      EJBRuntimeUtils.pushEnvironment(this.beanManager.getEnvironmentContext());
   }

   public void popEnvironment() {
      EnvInfo var1 = (EnvInfo)envStack.pop();
      if (var1.wasSet()) {
         Thread.currentThread().setContextClassLoader(var1.getClassLoader());
      }

      EJBRuntimeUtils.popEnvironment();
   }

   protected void operationsComplete() {
   }

   protected void handleSystemException(InvocationWrapper var1, Throwable var2) throws RemoteException {
      try {
         handleSystemException(var1, this.beanManager.usesBeanManagedTx(), var2);
      } catch (TransactionRolledbackException var5) {
         if (!this.isImplementsRemote) {
            EJBTransactionRolledbackException var4 = null;
            if (var5.detail instanceof Exception) {
               var4 = new EJBTransactionRolledbackException(var5.getMessage(), (Exception)var5.detail);
               var4.initCause(var5.detail);
            } else {
               var4 = new EJBTransactionRolledbackException(var5.getMessage(), var5);
               var4.initCause(var5);
            }

            EJBRuntimeUtils.throwRemoteException(var5.getMessage(), var4);
         }

         throw var5;
      }
   }

   public static void handleSystemException(InvocationWrapper var0, boolean var1, Throwable var2) throws RemoteException {
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

         EJBRuntimeUtils.throwRemoteException("EJB Exception: ", var2);
      } else if (var3 == null) {
         EJBRuntimeUtils.throwRemoteException("EJB Exception: ", var2);
      } else if (EJBRuntimeUtils.runningInOurTx(var0)) {
         try {
            var3.rollback();
         } catch (Exception var6) {
            EJBLogger.logErrorOnRollback(var6);
         }

         EJBRuntimeUtils.throwRemoteException("EJB Exception: ", var2);
      } else {
         try {
            if (var3 instanceof weblogic.transaction.Transaction) {
               ((weblogic.transaction.Transaction)var3).setRollbackOnly(var2);
            } else {
               var3.setRollbackOnly();
            }
         } catch (Exception var5) {
            EJBLogger.logErrorMarkingRollback(var5);
         }

         if (EJBRuntimeUtils.isSpecialSystemException(var2)) {
            EJBRuntimeUtils.throwRemoteException("called setRollbackOnly", var2);
         }

         EJBRuntimeUtils.throwTransactionRolledback("EJB Exception: ", var2);
      }

   }

   public Object getInstance() {
      return this.ejbHome;
   }

   protected String getImplementedBusinessInterfaceName() {
      return null;
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseRemoteObject] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Business_Method_Postinvoke_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Business_Method_Postinvoke_Before_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseRemoteObject.java", "weblogic.ejb.container.internal.BaseRemoteObject", "__WL_postInvokeTxRetry", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)Z", 444, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BaseRemoteObject.java", "weblogic.ejb.container.internal.BaseRemoteObject", "__WL_postInvokeCleanup", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)V", 697, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Business_Method_Postinvoke_Cleanup_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      $assertionsDisabled = !BaseRemoteObject.class.desiredAssertionStatus();
      debugLogger = EJBDebugService.invokeLogger;
      currentInvocationWrapper = new ThreadLocalStack();
      envStack = new ThreadLocalStack(true);
   }

   private static class EnvInfo {
      private boolean clWasSet;
      private ClassLoader classLoader;

      private EnvInfo() {
         this.clWasSet = false;
         this.classLoader = null;
      }

      public boolean wasSet() {
         return this.clWasSet;
      }

      public void captureClassloader(ClassLoader var1) {
         this.clWasSet = true;
         this.classLoader = var1;
      }

      public ClassLoader getClassLoader() {
         return this.classLoader;
      }

      // $FF: synthetic method
      EnvInfo(Object var1) {
         this();
      }
   }

   protected class ThreadLocalObject {
      public boolean isRemoteBean = false;
      public final BaseRemoteObject baseRemoteObject;

      public ThreadLocalObject(boolean var2, BaseRemoteObject var3) {
         this.isRemoteBean = var2;
         this.baseRemoteObject = var3;
      }

      public boolean isRemote() {
         return this.isRemoteBean;
      }

      public BaseRemoteObject getBaseRemoteObject() {
         return this.baseRemoteObject;
      }
   }
}
