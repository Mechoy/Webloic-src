package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.FinderException;
import javax.ejb.Handle;
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
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.QueryCache;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.QueryHandler;
import weblogic.ejb20.internal.EJBMetaDataImpl;
import weblogic.ejb20.internal.PreparedQueryImpl;
import weblogic.ejb20.internal.QueryImpl;
import weblogic.logging.Loggable;
import weblogic.transaction.RollbackException;
import weblogic.transaction.internal.AppSetRollbackOnlyException;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.SoftHashMap;

public abstract class EntityEJBHome extends BaseEJBHome implements EJBHome, QueryHandler {
   public static final int SCALAR_FINDER = 1;
   public static final int ENUM_FINDER = 2;
   public static final int COLL_FINDER = 4;
   private static final DebugLogger runtimeLogger;
   private EJBActivator ejbActivator = new EJBActivator(this);
   private boolean isNoObjectActivation = false;
   private boolean clusterableBean;
   private Method findByPrimaryKeyMethod;
   public MethodDescriptor md_createQuery = null;
   public MethodDescriptor md_prepareQuery = null;
   private Map eoMap = Collections.synchronizedMap(new SoftHashMap());
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -2820836546056761702L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.internal.EntityEJBHome");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;
   public static final JoinPoint _WLDF$INST_JPFLD_5;
   public static final JoinPoint _WLDF$INST_JPFLD_6;

   public EntityEJBHome(Class var1) {
      super(var1);
      this.isNoObjectActivation = !EntityEJBObject_Activatable.class.isAssignableFrom(var1);
   }

   public boolean usesBeanManagedTx() {
      return false;
   }

   public void setup(BeanInfo var1, BaseEJBHomeIntf var2, BeanManager var3) throws WLDeploymentException {
      super.setup(var1, var2, var3);
      EntityBeanInfo var4 = (EntityBeanInfo)this.beanInfo;
      this.clusterableBean = var4.getClusteringDescriptor().getHomeIsClusterable();
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

   public EJBObject allocateEOInstance() {
      EntityEJBObject var1 = null;

      try {
         var1 = (EntityEJBObject)this.eoClass.newInstance();
         return var1;
      } catch (InstantiationException var3) {
         throw new AssertionError(var3);
      } catch (IllegalAccessException var4) {
         throw new AssertionError(var4);
      }
   }

   public EJBObject allocateEO() {
      throw new AssertionError("Cannot allocate an entity EJBObject without a primary key.");
   }

   public EJBObject allocateEO(Object var1) {
      if (this.getIsNoObjectActivation()) {
         EntityEJBObject var3 = (EntityEJBObject)this.eoMap.get(var1);
         if (var3 == null) {
            var3 = (EntityEJBObject)this.allocateEOInstance();
            var3.setEJBHome(this);
            var3.setPrimaryKey(var1);
            var3.setBeanManager(this.getBeanManager());
            var3.setBeanInfo(this.getBeanInfo());
            this.eoMap.put(var1, var3);
         }

         return var3;
      } else {
         EntityEJBObject_Activatable var2 = (EntityEJBObject_Activatable)this.allocateEOInstance();
         var2.setEJBHome(this);
         var2.setPrimaryKey(var1);
         var2.setBeanManager(this.getBeanManager());
         var2.setBeanInfo(this.getBeanInfo());
         var2.setActivator(this.ejbActivator);
         return var2;
      }
   }

   public void releaseEO(Object var1) {
      this.eoMap.remove(var1);
   }

   public void cleanup() {
      if (this.getIsNoObjectActivation()) {
         Collection var1 = this.eoMap.values();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            EJBObject var3 = (EJBObject)var2.next();
            this.unexportEO(var3);
         }
      }

      this.unexportEJBActivator(this.ejbActivator, this.eoClass);
   }

   public boolean getIsNoObjectActivation() {
      return this.isNoObjectActivation;
   }

   protected EJBMetaData getEJBMetaDataInstance() {
      EntityBeanInfo var1 = (EntityBeanInfo)this.beanInfo;
      Class var2 = var1.getPrimaryKeyClass();
      if (var1.isUnknownPrimaryKey() && !var1.getIsBeanManagedPersistence()) {
         CMPBeanDescriptor var3 = var1.getCMPInfo().getCMPBeanDescriptor(var1.getEJBName());
         var2 = var3.getPrimaryKeyClass();
      }

      return new EJBMetaDataImpl(this, var1.getHomeInterfaceClass(), var1.getRemoteInterfaceClass(), var2, false, false);
   }

   protected EJBObject create(MethodDescriptor var1, Method var2, Method var3, Object[] var4) throws Exception {
      boolean var19;
      boolean var10000 = var19 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var20 = null;
      DiagnosticActionState[] var21 = null;
      Object var18 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var14 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var14 = new Object[]{this, var1, var2, var3, var4};
         }

         DynamicJoinPoint var45 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var14, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium;
         DiagnosticAction[] var10002 = var20 = var10001.getActions();
         InstrumentationSupport.preProcess(var45, var10001, var10002, var21 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var27 = false;

      EJBObject var46;
      try {
         EJBObject var44;
         try {
            var27 = true;
            this.pushEnvironment();
            if (debugLogger.isDebugEnabled()) {
               debug("Creating a bean from md: " + var1);
            }

            InvocationWrapper var5 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var4));
            EJBObject var6 = null;
            Throwable var7 = null;

            try {
               if (!$assertionsDisabled && var5.getInvokeTx() == null) {
                  throw new java.lang.AssertionError();
               }

               var6 = this.beanManager.remoteCreate(var5, var2, var3, var4);
               var5.setPrimaryKey(var6.getPrimaryKey());
            } catch (InternalException var38) {
               InternalException var43 = var38;
               var7 = var38.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var2, var7)) {
                  throw (Exception)var7;
               }

               this.handleSystemException(var5, var38);
               throw new AssertionError("Should never have reached here");
            } catch (Throwable var39) {
               Throwable var8 = var39;
               var7 = var39;
               this.handleSystemException(var5, var39);
               throw new AssertionError("Should never reach here");
            } finally {
               this.postHomeInvoke(var5, var7);
            }

            var44 = var6;
         } finally {
            this.popEnvironment();
         }

         var46 = var44;
         var27 = false;
      } finally {
         if (var27) {
            var10001 = null;
            if (var19) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var20, var21);
            }

         }
      }

      if (var19) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium, var20, var21);
      }

      return var46;
   }

   public void remove(MethodDescriptor var1, Handle var2) throws RemoteException, RemoveException {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      if (var10000) {
         Object[] var6 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var6 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var6, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         this.validateHandleFromHome(var2);
         EJBObject var3 = var2.getEJBObject();
         Object var4 = var3.getPrimaryKey();
         this.remove(var1, var3, var4);
      } finally {
         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var12, var13);
         }

      }

   }

   public void remove(MethodDescriptor var1, Object var2) throws RemoteException, RemoveException {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      if (var10000) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var5, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         Object var3 = null;
         this.remove(var1, (EJBObject)var3, var2);
      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium, var11, var12);
         }

      }

   }

   private void remove(MethodDescriptor var1, EJBObject var2, Object var3) throws RemoteException, RemoveException {
      try {
         this.pushEnvironment();
         InvocationWrapper var4 = this.preHomeInvoke(var1, new EJBContextHandler(var1, new Object[]{var3}));
         Throwable var5 = null;

         try {
            var4.setPrimaryKey(var3);

            try {
               this.beanManager.remove(var4);
            } catch (InternalException var22) {
               if (!(var22.getCause() instanceof NoSuchEJBException) && !(var22.getCause() instanceof ConcurrentAccessException)) {
                  throw var22;
               }

               throw new InternalException(var22.getMessage(), var22.getCause().getCause());
            }
         } catch (InternalException var23) {
            var5 = var23.detail;
            if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var1.getMethod(), var5)) {
               Exception var7 = (Exception)var5;
               if (var7 instanceof RemoveException) {
                  throw (RemoveException)var7;
               }

               throw new AssertionError("Invalid Exception thrown from remove: " + StackTraceUtils.throwable2StackTrace(var7));
            }

            this.handleSystemException(var4, var23);
            throw new AssertionError("Should never have reached here");
         } catch (Throwable var24) {
            var5 = var24;
            this.handleSystemException(var4, var24);
            throw new AssertionError("Should never reach here");
         } finally {
            this.postHomeInvoke(var4, var5);
            if (this.getIsNoObjectActivation()) {
               this.unexportEO(var2, false);
            }

         }
      } finally {
         this.popEnvironment();
      }

   }

   protected EJBObject findByPrimaryKey(MethodDescriptor var1, Object var2) throws Exception {
      boolean var18;
      boolean var10000 = var18 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var19 = null;
      DiagnosticActionState[] var20 = null;
      Object var17 = null;
      DynamicJoinPoint var49;
      if (var10000) {
         Object[] var13 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1, var2};
         }

         var49 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var13, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var49, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var28 = false;

      EJBObject var50;
      try {
         var28 = true;
         if (!$assertionsDisabled && var1 == null) {
            throw new java.lang.AssertionError();
         }

         if (var2 == null) {
            throw new ObjectNotFoundException("Primary key was null!");
         }

         EJBObject var6;
         try {
            this.pushEnvironment();
            InvocationWrapper var3 = this.preHomeInvoke(var1, new EJBContextHandler(var1, new Object[]{var2}));
            EJBObject var4 = null;
            Throwable var5 = null;

            try {
               var3.setPrimaryKey(var2);
               var4 = this.beanManager.remoteFindByPrimaryKey(var3, var2);
            } catch (InternalException var44) {
               var5 = var44.detail;
               if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)this.findByPrimaryKeyMethod, var5)) {
                  throw (Exception)var5;
               }

               this.handleSystemException(var3, var44);
               throw new AssertionError("Should never have reached here");
            } catch (Throwable var45) {
               var5 = var45;
               this.handleSystemException(var3, var45);
               throw new AssertionError("Should never reach here");
            } finally {
               if (var3.getInvokeTx() == null && !((EntityBeanInfo)this.beanInfo).getIsBeanManagedPersistence()) {
                  try {
                     this.getBeanManager().beforeCompletion(var3);
                     this.getBeanManager().afterCompletion(var3);
                  } catch (InternalException var43) {
                     if (EJBRuntimeUtils.isAppException(var1.getMethod(), var43)) {
                        throw (RemoveException)var43.detail;
                     }

                     this.handleSystemException(var3, var43);
                     throw new AssertionError("Should never have reached here");
                  }
               }

               this.postHomeInvoke(var3, var5);
            }

            var6 = var4;
         } finally {
            this.popEnvironment();
         }

         var50 = var6;
         var28 = false;
      } finally {
         if (var28) {
            var49 = null;
            if (var18) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
            }

         }
      }

      if (var18) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
      }

      return var50;
   }

   protected InvocationWrapper preEntityHomeInvoke(MethodDescriptor var1) throws RemoteException {
      InvocationWrapper var2 = null;
      this.pushEnvironment();

      try {
         var2 = this.preHomeInvoke(var1, EJBContextHandler.EMPTY);
      } catch (RemoteException var11) {
         this.popEnvironment();
         throw var11;
      }

      Throwable var3;
      try {
         if (!$assertionsDisabled && var2.getInvokeTx() == null) {
            throw new java.lang.AssertionError();
         } else {
            var3 = null;
            EnterpriseBean var12 = this.beanManager.preHomeInvoke(var2);
            var2.setBean(var12);
            return var2;
         }
      } catch (Throwable var10) {
         var3 = var10;

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
         debug("postEntityHomeInvokeTxRetry returning with nextTxRetryCount = " + var4 + "\n");
      }

      return var4;
   }

   private boolean postEntityHomeInvoke1(int var1, InvocationWrapper var2, Throwable var3) throws Exception {
      MethodDescriptor var4 = var2.getMethodDescriptor();
      if (debugLogger.isDebugEnabled()) {
         debug("[BaseEJBHome] postHomeInvoke1 called with nextTxRetryCount = " + var1 + "\n" + " wrap:" + var2 + " Exception: " + var3 + " on: " + this);
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

               EJBRuntimeUtils.throwRemoteException("Error during bean cleanup", var15.detail);
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
               EJBRuntimeUtils.throwRemoteException("Transaction Rolledback.", var8);
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
                     EJBRuntimeUtils.throwRemoteException("Error committing transaction:", var14);
                     throw new AssertionError("Should never reach here");
                  }

                  this.destroyInstanceAfterFailedCommitOrRollback(var2, var14, true);
                  EJBRuntimeUtils.throwRemoteException("Error committing transaction:", var14);
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
            debug("Could not retry preInvoke because of exception: " + var4);
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

   public Query createQuery() throws RemoteException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);
         return new QueryImpl(this);
      }
   }

   public Query createSqlQuery() throws RemoteException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);
         return new QueryImpl(this, true);
      }
   }

   public PreparedQuery prepareQuery(String var1) throws RemoteException {
      return this.prepareQuery(var1, (Properties)null);
   }

   public PreparedQuery prepareQuery(String var1, Properties var2) throws RemoteException {
      PreparedQueryImpl var3 = null;
      this.md_prepareQuery = this.md_createQuery;
      if (this.md_prepareQuery == null) {
         Loggable var8 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var8.getMessage());
      } else {
         this.md_prepareQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);
         Method var4 = this.md_prepareQuery.getMethod();

         try {
            var3 = new PreparedQueryImpl(var1, this, var2);
         } catch (FinderException var7) {
            Loggable var6 = EJBLogger.logErrorPrepareQueryLoggable(var7.getMessage());
            throw new RemoteException(var6.getMessage(), var7);
         }

         if (runtimeLogger.isDebugEnabled()) {
            runtimeLogger.debug("\nReturning PreparedQuery: " + var3);
         }

         return var3;
      }
   }

   public String nativeQuery(String var1) throws RemoteException {
      if (this.md_createQuery == null) {
         Loggable var2 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var2.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);

         try {
            return ((BaseEntityManager)this.beanManager).nativeQuery(var1);
         } catch (FinderException var4) {
            Loggable var3 = EJBLogger.logErrorObtainNativeQueryLoggable(var4.getMessage(), var4.toString());
            throw new RemoteException(var3.getMessage(), var4);
         }
      }
   }

   public String getDatabaseProductName() throws RemoteException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);

         try {
            return ((BaseEntityManager)this.beanManager).getDatabaseProductName();
         } catch (FinderException var3) {
            Loggable var2 = EJBLogger.logErrorObtainNativeQueryLoggable(var3.getMessage(), var3.toString());
            throw new RemoteException(var2.getMessage(), var3);
         }
      }
   }

   public String getDatabaseProductVersion() throws RemoteException {
      if (this.md_createQuery == null) {
         Loggable var1 = EJBLogger.logdynamicQueriesNotEnabledLoggable();
         throw new AccessException(var1.getMessage());
      } else {
         this.md_createQuery.checkMethodPermissionsRemote(EJBContextHandler.EMPTY);

         try {
            return ((BaseEntityManager)this.beanManager).getDatabaseProductVersion();
         } catch (FinderException var3) {
            Loggable var2 = EJBLogger.logErrorObtainNativeQueryLoggable(var3.getMessage(), var3.toString());
            throw new RemoteException(var2.getMessage(), var3);
         }
      }
   }

   public Object executeQuery(String var1, WLQueryProperties var2, boolean var3) throws FinderException, RemoteException {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var5 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High.isArgumentsCaptureNeeded()) {
            var5 = InstrumentationSupport.toSensitive(4);
         }

         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var14 = false;

      Object var17;
      try {
         var14 = true;
         var17 = this.executeQuery(var1, var2, var3, false);
         var14 = false;
      } finally {
         if (var14) {
            var10001 = null;
            if (var10) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var11, var12);
            }

         }
      }

      if (var10) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var11, var12);
      }

      return var17;
   }

   public Object executeQuery(String var1, WLQueryProperties var2, boolean var3, boolean var4) throws FinderException, RemoteException {
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

         var35 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, var13, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var35, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var25 = false;

      label324: {
         Object var36;
         label325: {
            try {
               var25 = true;
               if (var2.getEnableQueryCaching() && !((BaseEntityManager)this.beanManager).isReadOnly()) {
                  Loggable var34 = EJBLogger.logQueryCacheNotSupportReadWriteBeanLoggable();
                  throw new FinderException(var34.getMessage());
               }

               MethodDescriptor var5 = (MethodDescriptor)this.md_createQuery.clone();
               var5.setTXAttribute(var2.getTransaction());
               var5.setupIsolationLevel(var2.getIsolationLevel());
               Object var6 = null;
               InvocationWrapper var7 = this.preHomeInvoke(var5, EJBContextHandler.EMPTY);
               Method var8 = var5.getMethod();

               label313: {
                  Object var9;
                  label312: {
                     try {
                        label309: {
                           if (var2.getEnableQueryCaching()) {
                              var6 = ((TTLManager)this.beanManager).getFromQueryCache(var1, var2.getMaxElements(), var7.isLocal());
                              if (var6 != null) {
                                 if (var6 == QueryCache.NULL_VALUE) {
                                    var9 = null;
                                    break label309;
                                 }

                                 var9 = var6;
                                 break label312;
                              }
                           }

                           if (var4) {
                              var6 = ((BaseEntityManager)this.beanManager).dynamicSqlQuery(var1, (Object[])null, var2, var8, var7.isLocal(), var3 ? ResultSet.class : Collection.class, (ConversationImpl)null);
                           } else {
                              var6 = ((BaseEntityManager)this.beanManager).dynamicQuery(var1, (Object[])null, var2, var8, var7.isLocal(), var3);
                           }
                           break label313;
                        }
                     } catch (InternalException var30) {
                        if (var30.detail instanceof FinderException) {
                           throw (FinderException)var30.detail;
                        }

                        this.handleSystemException(var7, var30);
                        break label313;
                     } catch (Throwable var31) {
                        this.handleSystemException(var7, var31);
                        throw new AssertionError("Should never reach here");
                     } finally {
                        this.postHomeInvoke(var7, (Throwable)null);
                     }

                     var35 = (DynamicJoinPoint)var9;
                     var25 = false;
                     break label324;
                  }

                  var36 = var9;
                  var25 = false;
                  break label325;
               }

               var36 = var6;
               var25 = false;
            } finally {
               if (var25) {
                  var10001 = null;
                  if (var18) {
                     InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_5, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
                  }

               }
            }

            if (var18) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_5, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
            }

            return var36;
         }

         if (var18) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_5, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
         }

         return var36;
      }

      if (var18) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_5, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var19, var20);
      }

      return var35;
   }

   public Object executePreparedQuery(String var1, PreparedQuery var2, Map var3, Map var4, boolean var5) throws FinderException, RemoteException {
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

         var35 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_6, var14, (Object)null);
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
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_6, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
            }

         }
      }

      if (var19) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_6, _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High, var20, var21);
      }

      return var36;
   }

   protected Object finder(MethodDescriptor var1, Object[] var2, int var3) throws Exception {
      Object var4 = null;

      try {
         if (!$assertionsDisabled && var1 == null) {
            throw new java.lang.AssertionError();
         }

         this.pushEnvironment();
         InvocationWrapper var5 = this.preHomeInvoke(var1, new EJBContextHandler(var1, var2));
         Method var6 = var1.getMethod();
         Throwable var7 = null;

         try {
            if (var1.getMethodId() != null) {
               if (!$assertionsDisabled && !(this.beanManager instanceof TTLManager)) {
                  throw new java.lang.AssertionError();
               }

               if (!$assertionsDisabled && var3 == 2) {
                  throw new java.lang.AssertionError();
               }

               var4 = ((TTLManager)this.beanManager).getFromQueryCache(var1.getMethodId(), var2, var5.isLocal());
            }

            if (var4 != null) {
               if (var4 == QueryCache.NULL_VALUE) {
                  var4 = null;
               }
            } else if (var3 == 1) {
               var4 = this.beanManager.remoteScalarFinder(var5, var6, var2);
            } else if (var3 == 2) {
               var4 = this.beanManager.enumFinder(var5, var6, var2);
            } else {
               if (var3 != 4) {
                  throw new AssertionError("Unexpected value: " + var3);
               }

               var4 = this.beanManager.collectionFinder(var5, var6, var2);
            }
         } catch (InternalException var29) {
            var7 = var29.detail;
            if (EJBRuntimeUtils.isAppException((BeanInfo)this.beanInfo, (Method)var6, var7)) {
               throw (Exception)var29.detail;
            }

            this.handleSystemException(var5, var29);
         } catch (Throwable var30) {
            var7 = var30;
            this.handleSystemException(var5, var30);
            throw new AssertionError("Should never reach here");
         } finally {
            if (var5.getInvokeTx() == null && !((EntityBeanInfo)this.beanInfo).getIsBeanManagedPersistence()) {
               try {
                  this.getBeanManager().beforeCompletion(var4);
                  this.getBeanManager().afterCompletion(var4);
               } catch (InternalException var28) {
                  if (EJBRuntimeUtils.isAppException(var1.getMethod(), var28)) {
                     throw (RemoveException)var28.detail;
                  }

                  this.handleSystemException(var5, var28);
                  throw new AssertionError("Should never have reached here");
               }
            }

            this.postHomeInvoke(var5, var7);
         }
      } finally {
         this.popEnvironment();
      }

      return var4;
   }

   private void handleInvalidationException(InternalException var1) throws RemoteException {
      if (var1.detail != null) {
         throw new RemoteException(var1.getMessage(), var1.detail);
      } else {
         throw new RemoteException(StackTraceUtils.throwable2StackTrace(var1));
      }
   }

   public void invalidate(Object var1) throws RemoteException {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new RemoteException(var2.getMessage());
      } else {
         try {
            ((InvalidationBeanManager)this.beanManager).invalidate((Object)null, (Object)var1);
         } catch (InternalException var3) {
            this.handleInvalidationException(var3);
         }

      }
   }

   public void invalidate(Collection var1) throws RemoteException {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new RemoteException(var2.getMessage());
      } else {
         try {
            ((InvalidationBeanManager)this.beanManager).invalidate((Object)null, (Collection)var1);
         } catch (InternalException var3) {
            this.handleInvalidationException(var3);
         }

      }
   }

   public void invalidateAll() throws RemoteException {
      try {
         ((InvalidationBeanManager)this.beanManager).invalidateAll((Object)null);
      } catch (InternalException var2) {
         this.handleInvalidationException(var2);
      }

   }

   public void invalidateLocalServer(Object var1) throws RemoteException {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new RemoteException(var2.getMessage());
      } else {
         ((InvalidationBeanManager)this.beanManager).invalidateLocalServer((Object)null, (Object)var1);
      }
   }

   public void invalidateLocalServer(Collection var1) throws RemoteException {
      if (var1 == null) {
         Loggable var2 = EJBLogger.logNullInvalidateParameterLoggable();
         throw new RemoteException(var2.getMessage());
      } else {
         ((InvalidationBeanManager)this.beanManager).invalidateLocalServer((Object)null, (Collection)var1);
      }
   }

   public void invalidateAllLocalServer() throws RemoteException {
      ((InvalidationBeanManager)this.beanManager).invalidateAllLocalServer((Object)null);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityEJBHome] " + var0);
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Database_Access_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Database_Access_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Create_Around_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Home_Remove_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Home_Remove_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "create", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljavax/ejb/EJBObject;", 256, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null, null, null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljavax/ejb/Handle;)V", 301, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "remove", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)V", 311, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Home_Remove_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "findByPrimaryKey", "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/Object;)Ljavax/ejb/EJBObject;", 368, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Database_Access_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("md", "weblogic.diagnostics.instrumentation.gathering.EJBMethodDescriptorRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "executeQuery", "(Ljava/lang/String;Lweblogic/ejb/WLQueryProperties;Z)Ljava/lang/Object;", 943, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_5 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "executeQuery", "(Ljava/lang/String;Lweblogic/ejb/WLQueryProperties;ZZ)Ljava/lang/Object;", 950, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_6 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "EntityEJBHome.java", "weblogic.ejb.container.internal.EntityEJBHome", "executePreparedQuery", "(Ljava/lang/String;Lweblogic/ejb/PreparedQuery;Ljava/util/Map;Ljava/util/Map;Z)Ljava/lang/Object;", 1025, (Map)null, (boolean)0);
      $assertionsDisabled = !EntityEJBHome.class.desiredAssertionStatus();
      runtimeLogger = EJBDebugService.cmpRuntimeLogger;
   }
}
