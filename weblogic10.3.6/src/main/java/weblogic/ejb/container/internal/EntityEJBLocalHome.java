package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import javax.ejb.AccessLocalException;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEJBException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.transaction.Transaction;
import weblogic.dbeans.ConversationImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.PreparedQuery;
import weblogic.ejb.Query;
import weblogic.ejb.WLQueryProperties;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.LocalQueryHandler;
import weblogic.ejb.container.interfaces.QueryCache;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.transaction.RollbackException;
import weblogic.transaction.internal.AppSetRollbackOnlyException;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;

public abstract class EntityEJBLocalHome extends BaseEJBLocalHome implements LocalQueryHandler {
   public static final int SCALAR_FINDER = 1;
   public static final int ENUM_FINDER = 2;
   public static final int COLL_FINDER = 4;
   private static final DebugLogger runtimeLogger;
   private Method findByPrimaryKeyMethod;
   private BaseEntityManager entityManager = null;
   private BeanManager beanManager;
   public MethodDescriptor md_createQuery = null;
   public MethodDescriptor md_prepareQuery = null;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 7018298011361997830L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.EntityEJBLocalHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;

   public EntityEJBLocalHome(Class var1) {
      super(var1);
   }

   public boolean usesBeanManagedTx() {
      return false;
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      EntityBeanInfo var4 = (EntityBeanInfo)var1;
      this.beanManager = var3;
      super.setup(var1, var2, var3);
      if (var3 instanceof BaseEntityManager) {
         this.entityManager = (BaseEntityManager)var3;
      }

      Class var5 = var4.getGeneratedBeanInterface();
      Class var6 = var4.getPrimaryKeyClass();
      if (var4.isUnknownPrimaryKey() && !var4.getIsBeanManagedPersistence()) {
         CMPBeanDescriptor var7 = var4.getCMPInfo().getCMPBeanDescriptor(var4.getEJBName());
         var6 = var7.getPrimaryKeyClass();
      }

      try {
         this.findByPrimaryKeyMethod = var5.getMethod("ejbFindByPrimaryKey", var6);
      } catch (NoSuchMethodException var8) {
         throw new AssertionError(var8);
      }
   }

   public BaseEJBLocalObjectIntf allocateELO() {
      throw new AssertionError("Must have pk for entity beans");
   }

   public BaseEJBLocalObjectIntf allocateELO(Object var1) {
      EntityEJBLocalObject var2 = null;

      try {
         var2 = (EntityEJBLocalObject)this.eloClass.newInstance();
      } catch (InstantiationException var4) {
         throw new AssertionError(var4);
      } catch (IllegalAccessException var5) {
         throw new AssertionError(var5);
      }

      var2.setBeanInfo(this.getBeanInfo());
      var2.setEJBLocalHome(this);
      var2.setPrimaryKey(var1);
      var2.setBeanManager(this.getBeanManager());
      var2.setIsEJB30ClientView(false);
      return var2;
   }

   public void cleanup() {
   }

   protected EJBLocalObject create(MethodDescriptor var1, Method var2, Method var3, Object[] var4) throws Exception {
      Object var5 = null;
      return this.create(var1, var2, var3, var4, var5);
   }

   protected EJBLocalObject create(MethodDescriptor var1, Method var2, Method var3, Object[] var4, Object var5) throws Exception {
      boolean var20;
      boolean var10000 = var20 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var21 = null;
      DiagnosticActionState[] var22 = null;
      Object var19 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var15 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var15 = new Object[]{this, var1, var2, var3, var4, var5};
         }

         DynamicJoinPoint var48 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var15, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
         DiagnosticAction[] var10002 = var21 = var10001.getActions();
         InstrumentationSupport.preProcess(var48, var10001, var10002, var22 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var28 = false;

      EJBLocalObject var49;
      try {
         EJBLocalObject var47;
         try {
            var28 = true;
            this.pushEnvironment();
            if (debugLogger.isDebugEnabled()) {
               debug("[EntityEJBHome] Creating a bean from md: " + var1);
            }

            EJBLocalObject var6 = null;
            InvocationWrapper var7 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var4));
            Throwable var8 = null;

            try {
               if (!$assertionsDisabled && var7.getInvokeTx() == null) {
                  throw new java.lang.AssertionError();
               }

               BeanManager var46 = this.getBeanManager();
               var6 = var46.localCreate(var7, var2, var3, var4);
               var7.setPrimaryKey(var6.getPrimaryKey());
            } catch (InternalException var39) {
               InternalException var45 = var39;
               var8 = var39.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var2, var8)) {
                  throw (Exception)var8;
               }

               this.handleSystemException(var7, var39);
               throw new AssertionError("Should never have reached here");
            } catch (Throwable var40) {
               Throwable var9 = var40;
               var8 = var40;
               this.handleSystemException(var7, var40);
               throw new AssertionError("Should never have reached here");
            } finally {
               this.postHomeInvoke(var7, var8);
            }

            EnterpriseBean var44 = var7.getBean();
            var47 = var6;
         } finally {
            this.popEnvironment();
         }

         var49 = var47;
         var28 = false;
      } finally {
         if (var28) {
            var10001 = null;
            if (var20) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var21, var22);
            }

         }
      }

      if (var20) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var21, var22);
      }

      return var49;
   }

   public void remove(MethodDescriptor var1, Object var2) throws RemoveException, EJBException {
      boolean var17;
      boolean var10000 = var17 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var18 = null;
      DiagnosticActionState[] var19 = null;
      Object var16 = null;
      if (var10000) {
         Object[] var12 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var12 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var44 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var12, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var18 = var10001.getActions();
         InstrumentationSupport.preProcess(var44, var10001, var10002, var19 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         try {
            this.pushEnvironment();
            InvocationWrapper var3 = this.preHomeInvoke(var1, EJBContextHandler.EMPTY);
            Throwable var4 = null;

            try {
               var3.setPrimaryKey(var2);
               Object var5 = null;

               try {
                  this.getBeanManager().remove(var3);
               } catch (InternalException var38) {
                  if (!(var38.getCause() instanceof NoSuchEJBException) && !(var38.getCause() instanceof ConcurrentAccessException)) {
                     throw var38;
                  }

                  throw new InternalException(var38.getMessage());
               }
            } catch (InternalException var39) {
               var4 = var39.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var1.getMethod(), var4)) {
                  Exception var6 = (Exception)var4;
                  if (var6 instanceof EJBException) {
                     throw (EJBException)var6;
                  }

                  if (var6 instanceof RemoveException) {
                     throw (RemoveException)var6;
                  }

                  throw new AssertionError("Invalid Exception thrown from remove: " + StackTraceUtils.throwable2StackTrace(var6));
               }

               this.handleSystemException(var3, var39);
               throw new AssertionError("Should never have reached here");
            } catch (Throwable var40) {
               var4 = var40;
               this.handleSystemException(var3, var40);
               throw new AssertionError("Should never have reached here");
            } finally {
               this.postHomeInvoke(var3, var4);
            }
         } finally {
            this.popEnvironment();
         }
      } finally {
         if (var17) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var18, var19);
         }

      }

   }

   protected Object findByPrimaryKey(MethodDescriptor var1, Object var2) throws Exception {
      Object var3 = null;
      return this.findByPrimaryKey(var1, var2, var3);
   }

   protected Object findByPrimaryKey(MethodDescriptor var1, Object var2, Object var3) throws Exception {
      boolean var19;
      boolean var10000 = var19 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var20 = null;
      DiagnosticActionState[] var21 = null;
      Object var18 = null;
      DynamicJoinPoint var51;
      if (var10000) {
         Object[] var14 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var14 = new Object[]{this, var1, var2, var3};
         }

         var51 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var14, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var20 = var10001.getActions();
         InstrumentationSupport.preProcess(var51, var10001, var10002, var21 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var29 = false;

      Object var52;
      try {
         var29 = true;
         if (!$assertionsDisabled && var1 == null) {
            throw new java.lang.AssertionError();
         }

         if (var2 == null) {
            throw new ObjectNotFoundException("Primary key was null!");
         }

         Object var7;
         try {
            this.pushEnvironment();
            Object var4 = null;
            InvocationWrapper var5 = this.preHomeInvoke(var1, EJBContextHandler.EMPTY);
            Throwable var6 = null;

            try {
               var5.setPrimaryKey(var2);
               var4 = this.getBeanManager().localFindByPrimaryKey(var5, var2);
            } catch (InternalException var45) {
               var6 = var45.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)this.findByPrimaryKeyMethod, var6)) {
                  throw (Exception)var6;
               }

               this.handleSystemException(var5, var45);
               throw new AssertionError("Should never have reached here");
            } catch (Throwable var46) {
               var6 = var46;
               this.handleSystemException(var5, var46);
               throw new AssertionError("Should never have reached here");
            } finally {
               if (var5.getInvokeTx() == null && !((EntityBeanInfo)this.beanInfo).getIsBeanManagedPersistence()) {
                  try {
                     this.getBeanManager().beforeCompletion(var5);
                     this.getBeanManager().afterCompletion(var5);
                  } catch (InternalException var44) {
                     if (EJBRuntimeUtils.isAppException(var1.getMethod(), var44)) {
                        throw (RemoveException)var44.detail;
                     }

                     this.handleSystemException(var5, var44);
                     throw new AssertionError("Should never have reached here");
                  }
               }

               this.postHomeInvoke(var5, var6);
            }

            EnterpriseBean var50 = var5.getBean();
            var7 = var4;
         } finally {
            this.popEnvironment();
         }

         var52 = var7;
         var29 = false;
      } finally {
         if (var29) {
            var51 = null;
            if (var19) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
            }

         }
      }

      if (var19) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
      }

      return var52;
   }

   protected InvocationWrapper preEntityHomeInvoke(MethodDescriptor var1) throws EJBException {
      InvocationWrapper var2 = null;
      this.pushEnvironment();

      try {
         var2 = this.preHomeInvoke(var1, EJBContextHandler.EMPTY);
      } catch (EJBException var12) {
         this.popEnvironment();
         throw var12;
      }

      try {
         if (!$assertionsDisabled && var2.getInvokeTx() == null) {
            throw new java.lang.AssertionError();
         } else {
            BeanManager var13 = this.getBeanManager();
            if (!$assertionsDisabled && var13 == null) {
               throw new java.lang.AssertionError();
            } else {
               EnterpriseBean var4 = null;
               var4 = var13.preHomeInvoke(var2);
               var2.setBean(var4);
               if (debugLogger.isDebugEnabled()) {
                  debug("Manager.preHomeInvoke returned a bean:" + var4);
               }

               return var2;
            }
         }
      } catch (Throwable var11) {
         Throwable var3 = var11;

         try {
            if (debugLogger.isDebugEnabled()) {
               debug("Manager's preHomeInvoke threw " + var3);
            }

            this.handleSystemException(var2, var3);
            throw new AssertionError("Should never reach here");
         } finally {
            this.popEnvironment();
            this.postHomeInvokeCleanup(var2);
         }
      }
   }

   protected int postEntityHomeInvokeTxRetry(InvocationWrapper var1, Throwable var2, int var3) throws Exception {
      int var4 = this.getNextTxRetryCount(var3);
      boolean var5 = this.postEntityHomeInvoke1(var4, var1, var2);
      if (!var5) {
         var4 = -1;
      }

      if (var4 >= 0) {
         this.retryEntityHomePreInvoke(var1);
         var2 = null;
      }

      if (debugLogger.isDebugEnabled()) {
         debug("postEntityHomeInvokeTxRetry returning with nextTxRetryCount  = " + var4 + "\n");
      }

      return var4;
   }

   private boolean postEntityHomeInvoke1(int var1, InvocationWrapper var2, Throwable var3) throws Exception {
      MethodDescriptor var4 = var2.getMethodDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseEJBLocalHome] postHomeInvoke1 called with nextTxRetryCount = " + var1 + "\n" + " wrap:" + var2 + " Exception: " + var3 + " on: " + this);
         if (null != var3) {
            var3.printStackTrace();
         }
      }

      Transaction var5 = var2.getInvokeTx();
      Transaction var6 = var2.getCallerTx();
      Method var7 = var4.getMethod();
      if (var3 != null && !EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var7, var3)) {
         var2.setSystemExceptionOccured();

         try {
            this.beanManager.destroyInstance(var2, var3);
         } catch (InternalException var10) {
            this.handleSystemException(var2, var3);
            throw new AssertionError("Should never be reached");
         }

         this.handleSystemException(var2, var3);
         throw new AssertionError("Should never be reached");
      } else {
         if (var2.getInvokeTx() == null) {
            try {
               this.getBeanManager().beforeCompletion(var2);
               this.getBeanManager().afterCompletion(var2);
            } catch (InternalException var15) {
               if (!EJBRuntimeUtils.isAppException(var7, var15)) {
                  this.handleSystemException(var2, var15);
                  throw new AssertionError("Should never have reached here");
               }

               EJBRuntimeUtils.throwEJBException("Error during bean cleanup ", var15.detail);
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
               } catch (Exception var13) {
                  EJBLogger.logErrorDuringRollback1(var5.toString(), StackTraceUtils.throwable2StackTrace(var13));
               }

               if (!EJBRuntimeUtils.isSystemRollback(var2)) {
                  return this.isTxRetry(var1);
               }

               if (debugLogger.isDebugEnabled()) {
                  debug(" system Rollback, throw exception");
               }

               this.destroyInstanceAfterFailedCommitOrRollback(var2, var3, false);
               Throwable var8 = ((TransactionImpl)var5).getRollbackReason();
               EJBRuntimeUtils.throwEJBException("Transaction Rolledback.", var8);
            } else {
               try {
                  if (var3 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), var3)) {
                     if (debugLogger.isDebugEnabled()) {
                        debug(var3.getClass().getName() + " is thrown, attempt to rollback ");
                     }

                     try {
                        var5.rollback();
                        if (debugLogger.isDebugEnabled()) {
                           debug(" Rollback succeeded. ");
                        }
                     } catch (Exception var11) {
                        EJBLogger.logErrorDuringRollback1(var5.toString(), StackTraceUtils.throwable2StackTrace(var11));
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
               } catch (Exception var14) {
                  if (var14 instanceof RollbackException) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("Committing our tx: ROLLBACK\n");
                     }

                     RollbackException var9 = (RollbackException)var14;
                     if (EJBRuntimeUtils.isOptimisticLockException(var9.getNested()) || var9.getNested() != null && var9.getNested() instanceof AppSetRollbackOnlyException) {
                        return this.isTxRetry(var1);
                     }

                     this.destroyInstanceAfterFailedCommitOrRollback(var2, var14, true);
                     EJBRuntimeUtils.throwEJBException("Error committing transaction:", var14);
                     throw new AssertionError("Should never reach here");
                  }

                  this.destroyInstanceAfterFailedCommitOrRollback(var2, var14, true);
                  EJBRuntimeUtils.throwEJBException("Error committing transaction:", var14);
                  throw new AssertionError("Should never reach here");
               }
            }
         } else if (EJBRuntimeUtils.runningInCallerTx(var2) && var3 != null && this.beanInfo.isEJB30() && EJBRuntimeUtils.isAppExceptionNeedtoRollback(this.beanInfo.getDeploymentInfo(), var3)) {
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
      }
   }

   private boolean isTxRetry(int var1) {
      if (var1 >= 0) {
         if (debugLogger.isDebugEnabled()) {
            debug(" nextTxRetryCount " + var1 + " retry Tx");
         }

         return true;
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" nextTxRetryCount " + var1 + " do not retry Tx");
         }

         return false;
      }
   }

   private void destroyInstanceAfterFailedCommitOrRollback(InvocationWrapper var1, Throwable var2, boolean var3) {
      try {
         this.getBeanManager().destroyInstance(var1, var2);
      } catch (InternalException var6) {
         Transaction var5 = var1.getInvokeTx();
         if (var3) {
            EJBLogger.logErrorDuringCommit(var5.toString(), StackTraceUtils.throwable2StackTrace(var6));
         } else {
            EJBLogger.logErrorDuringRollback(var5.toString(), StackTraceUtils.throwable2StackTrace(var6));
         }
      }

   }

   private void retryEntityHomePreInvoke(InvocationWrapper var1) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug("retryEntityHomePreInvoke entered \n");
      }

      EnterpriseBean var2 = null;

      try {
         EJBRuntimeUtils.setWrapWithTxs(var1);
         var2 = this.beanManager.preHomeInvoke(var1);
      } catch (Throwable var4) {
         if (debugLogger.isDebugEnabled()) {
            debug("Could not retry preInvoke " + var4);
         }

         this.handleSystemException(var1, var4);
         throw new AssertionError("Should never reach here");
      }

      var1.setBean(var2);
   }

   public void postEntityHomeInvokeCleanup(InvocationWrapper var1, Throwable var2) throws Exception {
      try {
         if (var2 != null) {
            Method var3 = var1.getMethodDescriptor().getMethod();
            if (EJBRuntimeUtils.isAppException(var3, var2)) {
               EJBRuntimeUtils.throwException(var2);
            } else {
               if (var2 instanceof Exception) {
                  throw (Exception)var2;
               }

               if (!$assertionsDisabled && !(var2 instanceof Exception)) {
                  throw new java.lang.AssertionError();
               }

               EJBRuntimeUtils.throwEJBException("EJB encountered System Exception: ", var2);
            }
         }
      } finally {
         this.popEnvironment();
         super.postHomeInvokeCleanup(var1);

         try {
            this.getBeanManager().postHomeInvoke(var1);
         } catch (Exception var10) {
            EJBLogger.logExcepInMethod(var1.getMethodDescriptor().getMethod().getName(), var10);
         }

      }

   }

   public int getRetryOnRollbackCount(InvocationWrapper var1) {
      return var1.getMethodDescriptor().getRetryOnRollbackCount();
   }

   private int getNextTxRetryCount(int var1) {
      --var1;
      return var1;
   }

   public Query createQuery() throws EJBException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);
         return new LocalQueryImpl(this);
      }
   }

   public PreparedQuery prepareQuery(String var1) throws EJBException {
      return this.prepareQuery(var1, (Properties)null);
   }

   public PreparedQuery prepareQuery(String var1, Properties var2) throws EJBException {
      LocalPreparedQueryImpl var3 = null;
      this.md_prepareQuery = this.md_createQuery;
      if (this.md_prepareQuery == null) {
         Loggable var9 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var9.getMessage());
      } else {
         this.md_prepareQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);
         Method var4 = this.md_prepareQuery.getMethod();

         try {
            var3 = new LocalPreparedQueryImpl(var1, this, var2);
         } catch (FinderException var8) {
            Loggable var6 = EJBLogger.logErrorPrepareQueryLoggable(var8.getMessage());
            EJBException var7 = new EJBException(var6.getMessage(), var8);
            var7.initCause(var8);
            throw var7;
         }

         if (runtimeLogger.isDebugEnabled()) {
            runtimeLogger.debug("\nReturning PreparedQuery: " + var3);
         }

         return var3;
      }
   }

   public Query createSqlQuery() throws EJBException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);
         return new LocalQueryImpl(this, true);
      }
   }

   public String nativeQuery(String var1) throws EJBException {
      Loggable var2;
      if (this.entityManager == null) {
         var2 = EJBLogger.logExecuteNativeQueryUseNonWeblogicEntitymanagerLoggable(var1);
         throw new EJBException(var2.getMessage());
      } else if (this.md_createQuery == null) {
         var2 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var2.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);

         try {
            return this.entityManager.nativeQuery(var1);
         } catch (FinderException var5) {
            Loggable var3 = EJBLogger.logEntityErrorObtainNativeQueryLoggable(var5.getMessage());
            EJBException var4 = new EJBException(var3.getMessage(), var5);
            var4.initCause(var5);
            throw var4;
         }
      }
   }

   public String getDatabaseProductName() throws EJBException {
      Loggable var1;
      if (this.entityManager == null) {
         var1 = EJBLogger.logExecuteGetDatabaseProductnameUseNonWeblogicEntityManagerLoggable();
         throw new EJBException(var1.getMessage());
      } else if (this.md_createQuery == null) {
         var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);

         try {
            return this.entityManager.getDatabaseProductName();
         } catch (FinderException var4) {
            Loggable var2 = EJBLogger.logErrorCallGetdatabaseProductNameLoggable(var4.getMessage());
            EJBException var3 = new EJBException(var2.getMessage(), var4);
            var3.initCause(var4);
            throw var3;
         }
      }
   }

   public String getDatabaseProductVersion() throws EJBException {
      Loggable var1;
      if (this.entityManager == null) {
         var1 = EJBLogger.logExecuteGetDatabaseProductnameUseNonWeblogicEntityManagerLoggable();
         throw new EJBException(var1.getMessage());
      } else if (this.md_createQuery == null) {
         var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessLocalException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsLocal(EJBContextHandler.EMPTY);

         try {
            return this.entityManager.getDatabaseProductVersion();
         } catch (FinderException var4) {
            Loggable var2 = EJBLogger.logErrorCallGetdatabaseProductNameLoggable(var4.getMessage());
            EJBException var3 = new EJBException(var2.getMessage(), var4);
            var3.initCause(var4);
            throw var3;
         }
      }
   }

   public Object executeQuery(String var1, WLQueryProperties var2, boolean var3, boolean var4) throws FinderException, EJBException {
      boolean var18;
      boolean var10000 = var18 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var19 = null;
      DiagnosticActionState[] var20 = null;
      Object var17 = null;
      DynamicJoinPoint var35;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var13 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var13 = InstrumentationSupport.toSensitive(5);
         }

         var35 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var35, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var25 = false;

      label330: {
         Object var36;
         label331: {
            try {
               var25 = true;
               Loggable var34;
               if (this.entityManager == null) {
                  var34 = EJBLogger.logNonWeblogicEntityManagerExecuteQueryLoggable(var1);
                  throw new EJBException(var34.getMessage());
               }

               if (var2.getEnableQueryCaching() && !this.entityManager.isReadOnly()) {
                  var34 = EJBLogger.logQueryCacheNotSupportReadWriteBeanLoggable();
                  throw new FinderException(var34.getMessage());
               }

               MethodDescriptor var5 = (MethodDescriptor)this.md_createQuery.clone();
               var5.setTXAttribute(var2.getTransaction());
               var5.setupIsolationLevel(var2.getIsolationLevel());
               Object var6 = null;
               InvocationWrapper var7 = this.preHomeInvoke(var5, EJBContextHandler.EMPTY);
               Method var8 = var5.getMethod();

               label319: {
                  Object var9;
                  label318: {
                     try {
                        label315: {
                           if (var2.getEnableQueryCaching()) {
                              var6 = ((TTLManager)this.beanManager).getFromQueryCache(var1, var2.getMaxElements(), var7.isLocal());
                              if (var6 != null) {
                                 if (var6 == QueryCache.NULL_VALUE) {
                                    var9 = null;
                                    break label315;
                                 }

                                 var9 = var6;
                                 break label318;
                              }
                           }

                           if (var4) {
                              var6 = this.entityManager.dynamicSqlQuery(var1, (Object[])null, var2, var8, var7.isLocal(), var3 ? ResultSet.class : Collection.class, (ConversationImpl)null);
                           } else {
                              var6 = this.entityManager.dynamicQuery(var1, (Object[])null, var2, var8, var7.isLocal(), var3);
                           }
                           break label319;
                        }
                     } catch (InternalException var30) {
                        if (var30.detail instanceof FinderException) {
                           throw (FinderException)var30.detail;
                        }

                        this.handleSystemException(var7, var30);
                        break label319;
                     } catch (Throwable var31) {
                        this.handleSystemException(var7, var31);
                        throw new AssertionError("Should never reach here");
                     } finally {
                        this.postHomeInvoke(var7, (Throwable)null);
                     }

                     var35 = (DynamicJoinPoint)var9;
                     var25 = false;
                     break label330;
                  }

                  var36 = var9;
                  var25 = false;
                  break label331;
               }

               var36 = var6;
               var25 = false;
            } finally {
               if (var25) {
                  var10001 = null;
                  if (var18) {
                     InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
                  }

               }
            }

            if (var18) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
            }

            return var36;
         }

         if (var18) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
         }

         return var36;
      }

      if (var18) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
      }

      return var35;
   }

   public Object executePreparedQuery(String var1, PreparedQuery var2, Map var3, Map var4, boolean var5) throws FinderException, EJBException {
      boolean var19;
      boolean var10000 = var19 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var20 = null;
      DiagnosticActionState[] var21 = null;
      Object var18 = null;
      DynamicJoinPoint var35;
      if (var10000) {
         Object[] var14 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var14 = InstrumentationSupport.toSensitive(6);
         }

         var35 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var14, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var20 = var10001.getActions();
         InstrumentationSupport.preProcess(var35, var10001, var10002, var21 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var26 = false;

      Object var36;
      try {
         var26 = true;
         this.md_prepareQuery = this.md_createQuery;
         MethodDescriptor var6 = (MethodDescriptor)this.md_prepareQuery.clone();
         var6.setTXAttribute(var2.getTransaction());
         var6.setupIsolationLevel(((WLQueryProperties)var2).getIsolationLevel());
         Object var7 = null;
         InvocationWrapper var8 = this.preHomeInvoke(var6, EJBContextHandler.EMPTY);
         Method var9 = var6.getMethod();

         try {
            var7 = ((BaseEntityManager)this.beanManager).executePreparedQuery(var1, var8, var9, var5, var3, var4, var2);
         } catch (InternalException var31) {
            if (var31.detail instanceof FinderException) {
               throw (FinderException)var31.detail;
            }

            this.handleSystemException(var8, var31);
         } catch (Throwable var32) {
            this.handleSystemException(var8, var32);
            throw new AssertionError("Should never reach here");
         } finally {
            this.postHomeInvoke(var8, (Throwable)null);
         }

         var36 = var7;
         var26 = false;
      } finally {
         if (var26) {
            var35 = null;
            if (var19) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
            }

         }
      }

      if (var19) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
      }

      return var36;
   }

   protected Object finder(MethodDescriptor var1, Object[] var2, int var3) throws Exception {
      Object var8;
      try {
         if (!$assertionsDisabled && var1 == null) {
            throw new java.lang.AssertionError();
         }

         this.pushEnvironment();
         Object var4 = null;
         Method var5 = var1.getMethod();
         InvocationWrapper var6 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var2));
         Throwable var7 = null;

         try {
            if (var1.getMethodId() != null) {
               if (!$assertionsDisabled && !(this.beanManager instanceof TTLManager)) {
                  throw new java.lang.AssertionError();
               }

               if (!$assertionsDisabled && var3 == 2) {
                  throw new java.lang.AssertionError();
               }

               var4 = ((TTLManager)this.beanManager).getFromQueryCache(var1.getMethodId(), var2, var6.isLocal());
            }

            if (var4 != null) {
               if (var4 == QueryCache.NULL_VALUE) {
                  var4 = null;
               }
            } else if (var3 == 1) {
               var4 = this.getBeanManager().localScalarFinder(var6, var5, var2);
            } else if (var3 == 2) {
               var4 = this.getBeanManager().enumFinder(var6, var5, var2);
            } else {
               if (var3 != 4) {
                  throw new AssertionError("Unexpected value: " + var3);
               }

               var4 = this.getBeanManager().collectionFinder(var6, var5, var2);
            }
         } catch (InternalException var31) {
            var7 = var31.detail;
            if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var5, var7)) {
               throw (Exception)var7;
            }

            this.handleSystemException(var6, var31);
         } catch (Throwable var32) {
            var7 = var32;
            this.handleSystemException(var6, var32);
         } finally {
            if (var6.getInvokeTx() == null && !((EntityBeanInfo)this.beanInfo).getIsBeanManagedPersistence()) {
               try {
                  this.getBeanManager().beforeCompletion(var4);
                  this.getBeanManager().afterCompletion(var4);
               } catch (InternalException var30) {
                  if (EJBRuntimeUtils.isAppException(var1.getMethod(), var30)) {
                     throw (RemoveException)var30.detail;
                  }

                  this.handleSystemException(var6, var30);
                  throw new AssertionError("Should never have reached here");
               }
            }

            this.postHomeInvoke(var6, var7);
         }

         var8 = var4;
      } finally {
         this.popEnvironment();
      }

      return var8;
   }

   public void invalidate(Object var1) {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new EJBException(var2.getMessage());
      } else {
         try {
            ((InvalidationBeanManager)this.beanManager).invalidate((Object)null, (Object)var1);
         } catch (InternalException var3) {
            EJBRuntimeUtils.throwEJBException(var3);
         }

      }
   }

   public void invalidate(Collection var1) {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new EJBException(var2.getMessage());
      } else {
         try {
            ((InvalidationBeanManager)this.beanManager).invalidate((Object)null, (Collection)var1);
         } catch (InternalException var3) {
            EJBRuntimeUtils.throwEJBException(var3);
         }

      }
   }

   public void invalidateAll() {
      try {
         ((InvalidationBeanManager)this.beanManager).invalidateAll((Object)null);
      } catch (InternalException var2) {
         EJBRuntimeUtils.throwEJBException(var2);
      }

   }

   public void invalidateLocalServer(Object var1) {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new EJBException(var2.getMessage());
      } else {
         ((InvalidationBeanManager)this.beanManager).invalidateLocalServer((Object)null, (Object)var1);
      }
   }

   public void invalidateLocalServer(Collection var1) {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new EJBException(var2.getMessage());
      } else {
         ((InvalidationBeanManager)this.beanManager).invalidateLocalServer((Object)null, (Collection)var1);
      }
   }

   public void invalidateAllLocalServer() {
      ((InvalidationBeanManager)this.beanManager).invalidateAllLocalServer((Object)null);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityEJBLocalHome] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Create_Around_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Database_Access_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalHome.java", "weblogic.ejb.container.internal.EntityEJBLocalHome", "create", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;Ljava/lang/Object;)Ljavax/ejb/EJBLocalObject;", 189, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null, null, null, null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalHome.java", "weblogic.ejb.container.internal.EntityEJBLocalHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 241, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalHome.java", "weblogic.ejb.container.internal.EntityEJBLocalHome", "findByPrimaryKey", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", 307, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Database_Access_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null, null})}), (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalHome.java", "weblogic.ejb.container.internal.EntityEJBLocalHome", "executeQuery", "(Ljava/lang/String;Lweblogic/ejb/WLQueryProperties;ZZ)Ljava/lang/Object;", 946, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBLocalHome.java", "weblogic.ejb.container.internal.EntityEJBLocalHome", "executePreparedQuery", "(Ljava/lang/String;Lweblogic/ejb/PreparedQuery;Ljava/util/Map;Ljava/util/Map;Z)Ljava/lang/Object;", 1042, (Map)null, (boolean)0);
      $assertionsDisabled = !EntityEJBLocalHome.class.desiredAssertionStatus();
      runtimeLogger = EJBDebugService.cmpRuntimeLogger;
   }
}
