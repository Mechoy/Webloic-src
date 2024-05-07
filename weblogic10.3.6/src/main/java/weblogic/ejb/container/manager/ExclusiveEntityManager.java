package weblogic.ejb.container.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
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
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.cache.NRUCache;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.WLEJBContext;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLEntityBean;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.EntityEJBContextImpl;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.locks.ExclusiveLockManager;
import weblogic.ejb.container.locks.LockManager;
import weblogic.ejb.container.monitoring.EJBCacheRuntimeMBeanImpl;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.swap.EntitySwap;
import weblogic.ejb.container.utils.PartialOrderSet;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.ScrubbedCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.RuntimeCheckerException;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.ejb20.locks.LockTimedOutException;
import weblogic.logging.Loggable;
import weblogic.management.runtime.EntityEJBRuntimeMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public class ExclusiveEntityManager extends BaseEntityManager implements BeanManager, CMPBeanManager, CachingManager {
   private static boolean staticChecks;
   private boolean cacheBetweenTransactions = false;
   private boolean delayUpdatedUntilEndOfTx = true;
   private LockManager lockManager;
   protected NRUCache cache;
   protected EntityBeanInfo info;
   private EJBCacheRuntimeMBeanImpl cacheRTMBean;
   private int beanSize;
   EntitySwap swapper = null;
   private int idleTimeoutSeconds;
   boolean scrubberStarted = false;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 598785125594735472L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.ExclusiveEntityManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public ExclusiveEntityManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      throw new AssertionError("BeanManager.setup() should never be called on ExclusiveEntityManager.");
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4);
      EntityEJBRuntimeMBean var6 = (EntityEJBRuntimeMBean)this.getEJBRuntimeMBean();
      this.cacheRTMBean = (EJBCacheRuntimeMBeanImpl)var6.getCacheRuntime();
      this.info = (EntityBeanInfo)var3;
      this.lockManager = new ExclusiveLockManager(var6.getLockingRuntime());
      this.lockManager.setup(this.info);
      this.swapper = new EntitySwap();
      this.swapper.setup(this.info, this, this.info.getClassLoader());
      this.idleTimeoutSeconds = this.info.getCachingDescriptor().getIdleTimeoutSecondsCache();
      if (var5 == null) {
         this.cache = new NRUCache(this.ejbHome.getDisplayName(), this.info.getCachingDescriptor().getMaxBeansInCache());
         this.beanSize = 1;
         this.cache.setScrubInterval(this.idleTimeoutSeconds);
         this.cache.startScrubber();
         this.scrubberStarted = true;
      } else {
         if (!(var5 instanceof NRUCache)) {
            Loggable var7 = EJBLogger.lognotAnExclusiveCacheLoggable(this.info.getEJBName(), this.info.getCacheName());
            throw new WLDeploymentException(var7.getMessage());
         }

         this.cache = (NRUCache)var5;
         if (var5.usesMaxBeansInCache()) {
            this.beanSize = 1;
         } else {
            this.beanSize = this.info.getEstimatedBeanSize();
         }

         this.cache.setScrubInterval(this.idleTimeoutSeconds);
      }

      this.cache.register(this);
      this.cacheRTMBean.setReInitializableCache(this.cache);
      this.cacheBetweenTransactions = this.info.getCacheBetweenTransactions();
      this.delayUpdatedUntilEndOfTx = this.info.getBoxCarUpdates();
   }

   private boolean acquireLock(InvocationWrapper var1, Object var2) throws InternalException {
      int var3 = var1.getMethodDescriptor().getTxTimeoutMS();
      Object var4 = var1.getInvokeTxOrThread();
      return this.acquireLock(var2, var4, var3);
   }

   private boolean acquireLock(Object var1, Object var2, int var3) throws InternalException {
      try {
         return this.lockManager.lock(var1, var2, var3);
      } catch (LockTimedOutException var5) {
         throw new InternalException(var5.getMessage(), var5);
      }
   }

   protected boolean shouldLoad(Object var1, boolean var2, boolean var3) {
      return !var2 && !var3;
   }

   public EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException {
      boolean var13 = false;

      EntityBean var10000;
      try {
         var13 = true;
         super.preInvoke();
         Object var2 = var1.getPrimaryKey();
         Object var3 = var1.getInvokeTxOrThread();
         int var4 = var1.getMethodDescriptor().getTxTimeoutMS();
         EntityBean var5 = this.getReadyBean(var2, var3, var4);
         this.checkForReentrant(var5, var2);
         ((WLEnterpriseBean)var5).__WL_setBusy(true);
         var10000 = var5;
         var13 = false;
      } finally {
         if (var13) {
            Object var10001 = null;
            if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium.isEnabledAndNotDyeFiltered()) {
               DelegatingMonitor var10003 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium;
               InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10003, var10003.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium.isEnabledAndNotDyeFiltered()) {
         DelegatingMonitor var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
      }

      return var10000;
   }

   private EntityBean getReadyBean(Object var1, Object var2, int var3) throws InternalException {
      boolean var4 = this.acquireLock(var1, var2, var3);
      boolean var5 = false;
      EntityBean var6 = null;
      if (!$assertionsDisabled && var4 && var2 == null) {
         throw new java.lang.AssertionError();
      } else {
         CacheKey var7 = new CacheKey(var1, this);

         try {
            this.cacheRTMBean.incrementCacheAccessCount();
            var6 = (EntityBean)this.cache.get(var7);
            if (var6 == null) {
               EJBObject var8 = null;
               BaseEJBLocalObjectIntf var9 = null;
               if (this.remoteHome != null) {
                  var8 = this.remoteHome.allocateEO(var1);
               }

               if (this.localHome != null) {
                  var9 = this.localHome.allocateELO(var1);
               }

               var6 = this.getBeanFromPool();
               WLEJBContext var10 = (WLEJBContext)((WLEnterpriseBean)var6).__WL_getEJBContext();
               ((EntityEJBContextImpl)var10).__WL_setPrimaryKey(var1);
               var10.setEJBObject(var8);
               var10.setEJBLocalObject(var9);
               this.cache.put(var7, var6);
               this.cacheRTMBean.incrementCachedBeansCurrentCount();
               var5 = true;

               try {
                  var6.ejbActivate();
                  this.cacheRTMBean.incrementActivationCount();
               } catch (Throwable var13) {
                  EJBLogger.logErrorDuringActivate(StackTraceUtils.throwable2StackTrace(var13));
                  this.destroyPooledBean(var6);
                  EJBRuntimeUtils.throwInternalException("Exception in ejbActivate:", var13);
                  throw new AssertionError("does not reach");
               }
            } else {
               this.cacheRTMBean.incrementCacheHitCount();
            }

            if (this.shouldLoad(var1, this.cacheBetweenTransactions, var4) || var5) {
               try {
                  var6.ejbLoad();
               } catch (Throwable var12) {
                  EJBLogger.logErrorFromLoad(var12);
                  EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var12);
                  throw new AssertionError("does not reach");
               }
            }

            if (!var4) {
               this.setupTxListenerAndTxUser(var1, var2, (WLEnterpriseBean)var6);
            }

            return var6;
         } catch (Throwable var14) {
            this.cache.removeOnError(var7);
            if (!var4) {
               this.lockManager.unlock(var1, var2);
            }

            EJBRuntimeUtils.throwInternalException("Exception during transition from pooled to ready:", var14);
            throw new AssertionError("does not reach");
         }
      }
   }

   protected boolean shouldStoreAfterMethod(InvocationWrapper var1) {
      return var1.getInvokeTx() == null || !this.delayUpdatedUntilEndOfTx;
   }

   public int getBeanSize() {
      return this.beanSize;
   }

   public boolean isEntityManager() {
      return true;
   }

   public void enrollInTransaction(Transaction var1, CacheKey var2, EntityBean var3, RSInfo var4) throws InternalException {
      throw new AssertionError("method 'enrollInTransaction' not valid for ExclusiveEntityManager");
   }

   public void selectedForReplacement(CacheKey var1, EntityBean var2) {
      throw new AssertionError("method 'selectedForReplacement' not valid for ExclusiveEntityManager");
   }

   public void loadBeanFromRS(CacheKey var1, EntityBean var2, RSInfo var3) {
      throw new AssertionError("method 'copy' not valid for ExclusiveEntityManager");
   }

   public void removedFromCache(CacheKey var1, EnterpriseBean var2) {
      ((WLEnterpriseBean)var2).__WL_setBusy(false);
      this.releaseBeanToPool((EntityBean)var2);
      this.cacheRTMBean.decrementCachedBeansCurrentCount();
   }

   public void removedOnError(CacheKey var1, EnterpriseBean var2) {
      this.cacheRTMBean.decrementCachedBeansCurrentCount();
      this.destroyPooledBean((EntityBean)var2);
   }

   public void swapIn(CacheKey var1, EnterpriseBean var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("Activating key: " + var1);
      }

      if (!$assertionsDisabled && !(var2 instanceof EntityBean)) {
         throw new java.lang.AssertionError();
      } else {
         Object var3 = var1.getPrimaryKey();
         this.swapper.remove(var3);

         try {
            ((EntityBean)var2).ejbActivate();
         } catch (Exception var5) {
            this.cache.removeOnError(var1);
            this.cacheRTMBean.decrementCachedBeansCurrentCount();
            EJBLogger.logExceptionDuringEJBActivate(var5);
         }

      }
   }

   public void swapOut(CacheKey var1, EnterpriseBean var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("Passivating key: " + var1);
      }

      if (!$assertionsDisabled && !(var2 instanceof EntityBean)) {
         throw new java.lang.AssertionError();
      } else {
         Object var3 = var1.getPrimaryKey();

         try {
            this.cacheRTMBean.incrementPassivationCount();
            ((EntityBean)var2).ejbPassivate();
            this.swapper.write(var3, var2);
         } catch (Exception var5) {
            EJBLogger.logErrorDuringPassivation(StackTraceUtils.throwable2StackTrace(var5));
         }

      }
   }

   public void replicate(CacheKey var1, EnterpriseBean var2) {
   }

   public void postInvoke(InvocationWrapper var1) throws InternalException {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium.isEnabledAndNotDyeFiltered()) {
         Object[] var10 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{this, var1};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var10, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      Transaction var2 = var1.getInvokeTx();
      Object var3 = var1.getPrimaryKey();
      CacheKey var4 = new CacheKey(var3, this);
      EntityBean var5 = (EntityBean)this.cache.get(var4);
      if (var5 != null) {
         ((WLEnterpriseBean)var5).__WL_setBusy(false);
      }

      try {
         if (this.shouldStoreAfterMethod(var1)) {
            try {
               if (var5 != null && this.shouldStore(var5)) {
                  var5.ejbStore();
               }
            } catch (Throwable var13) {
               EJBLogger.logExcepInStore(var13);
               this.cache.removeOnError(var4);
               EJBRuntimeUtils.throwInternalException("Exception in ejbStore:", var13);
            }
         }
      } finally {
         if (var5 != null && ((WLEnterpriseBean)var5).__WL_needsRemove()) {
            if (debugLogger.isDebugEnabled()) {
               debug("releasing the bean inside postInvoke");
            }

            ((WLEnterpriseBean)var5).__WL_setNeedsRemove(false);
            this.lockManager.unlock(var3, var1.getInvokeTxOrThread());
         }

      }

   }

   public EnterpriseBean preHomeInvoke(InvocationWrapper var1) throws InternalException {
      EntityBean var2 = this.getBeanFromPool();
      return var2;
   }

   public void postHomeInvoke(InvocationWrapper var1) throws InternalException {
      EnterpriseBean var2 = var1.getBean();
      if (var1.hasSystemExceptionOccured()) {
         this.destroyPooledBean((EntityBean)var2);
      } else {
         this.releaseBeanToPool((EntityBean)var2);
      }

   }

   public void destroyInstance(InvocationWrapper var1, Throwable var2) {
      Object var3 = var1.getPrimaryKey();
      if (!$assertionsDisabled && var3 == null) {
         throw new java.lang.AssertionError();
      } else {
         this.cache.removeOnError(new CacheKey(var3, this));
      }
   }

   protected void prepareVerificationForBatch(Collection var1, Transaction var2) throws InternalException {
   }

   protected List pkListToBeanList(Collection var1, Transaction var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         CacheKey var7 = new CacheKey(var6, this);
         EntityBean var8 = (EntityBean)this.cache.get(var7);
         if (var3) {
            if (var8 != null) {
               var4.add(var8);
            }
         } else if (var8 != null && !((CMPBean)var8).__WL_getIsRemoved()) {
            var4.add(var8);
         }
      }

      return var4;
   }

   protected Map pkListToPkBeanMap(Collection var1, Transaction var2, boolean var3) {
      HashMap var4 = new HashMap();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         CacheKey var7 = new CacheKey(var6, this);
         EntityBean var8 = (EntityBean)this.cache.get(var7);
         if (var3) {
            if (var8 != null) {
               var4.put(var6, var8);
            }
         } else if (var8 != null && !((CMPBean)var8).__WL_getIsRemoved()) {
            var4.put(var6, var8);
         }
      }

      return var4;
   }

   public void beforeCompletion(Collection var1, Transaction var2) throws InternalException {
      this.beforeCompletion(var1, (Object)var2);
   }

   public void beforeCompletion(Collection var1, Object var2) throws InternalException {
      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            CacheKey var5 = new CacheKey(var4, this);
            EntityBean var6 = (EntityBean)this.cache.get(var5);

            try {
               if (var6 != null && this.shouldStore(var6)) {
                  Transaction var7 = null;
                  if (var2 instanceof Transaction) {
                     var7 = (Transaction)var2;
                  }

                  if (this.orderDatabaseOperations && var7 != null) {
                     ((CMPBean)var6).__WL_superEjbStore();
                  } else {
                     var6.ejbStore();
                  }
               }
            } catch (Throwable var8) {
               EJBLogger.logExcepInStore1(var8);
               this.cache.removeOnError(var5);
               EJBRuntimeUtils.throwInternalException("Exception in ejbStore:", var8);
            }
         }

      }
   }

   public void flushModified(Collection var1, Transaction var2) throws InternalException {
      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         Iterator var3 = var1.iterator();

         while(true) {
            while(var3.hasNext()) {
               Object var4 = var3.next();
               CacheKey var5 = new CacheKey(var4, this);
               EntityBean var6 = (EntityBean)this.cache.get(var5);
               if (this.isBeanManagedPersistence) {
                  try {
                     if (var6 != null && this.shouldStore(var6)) {
                        var6.ejbStore();
                     }
                  } catch (Throwable var8) {
                     this.cache.removeOnError(var5);
                     EJBRuntimeUtils.throwInternalException("Error writing from flushModified", var8);
                  }
               } else {
                  try {
                     if (var6 != null && this.shouldStore(var6)) {
                        if (this.orderDatabaseOperations && var2 != null) {
                           ((CMPBean)var6).__WL_superEjbStore();
                        } else {
                           ((CMPBean)var6).__WL_store(false);
                        }
                     }
                  } catch (Throwable var9) {
                     EJBLogger.logExcepInStore1(var9);
                     this.cache.removeOnError(var5);
                     EJBRuntimeUtils.throwInternalException("Error calling ejbStore.", var9);
                  }
               }
            }

            return;
         }
      }
   }

   public void afterCompletion(Collection var1, Transaction var2, int var3, Object var4) {
      this.afterCompletion(var1, (Object)var2, var3, var4);
   }

   public void afterCompletion(Collection var1, Object var2, int var3, Object var4) {
      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else if (!$assertionsDisabled && var2 == null) {
         throw new java.lang.AssertionError();
      } else if (!$assertionsDisabled && var3 != 3 && var3 != 4) {
         throw new java.lang.AssertionError();
      } else {
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            CacheKey var7 = new CacheKey(var6, this);
            WLEnterpriseBean var8 = (WLEnterpriseBean)this.cache.get(var7);

            try {
               if (this.cacheBetweenTransactions && var3 == 4) {
                  EnterpriseBean var17 = this.cache.get(var7);
                  if (var17 != null) {
                     try {
                        this.cacheRTMBean.incrementPassivationCount();
                        ((EntityBean)var17).ejbPassivate();
                     } catch (Exception var15) {
                        EJBLogger.logErrorDuringPassivation(StackTraceUtils.throwable2StackTrace(var15));
                        this.cache.removeOnError(var7);
                     }

                     this.cache.remove(var7);
                  }
               } else if (this.remoteHome == null || var8 != null && !var8.__WL_isCreatorOfTx()) {
                  WLEntityBean var9 = (WLEntityBean)this.cache.get(var7);
                  if (var9 != null) {
                     var9.__WL_setOperationsComplete(false);
                  }

                  this.cache.release(var7);
               }
            } finally {
               if (var8 == null) {
                  this.lockManager.unlock(var6, var2);
               } else if (!var8.__WL_isCreatorOfTx()) {
                  if (!var8.__WL_isBusy()) {
                     this.lockManager.unlock(var6, var2);
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debug("afterCompletion called before postInvoke");
                     }

                     var8.__WL_setNeedsRemove(true);
                  }
               }

            }
         }

      }
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      return (EJBObject)this.create(var1, var2, var3, var4);
   }

   public EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      return (EJBLocalObject)this.create(var1, var2, var3, var4);
   }

   private Object create(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      boolean var22;
      boolean var10000 = var22 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var23 = null;
      DiagnosticActionState[] var24 = null;
      Object var21 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var17 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var17 = new Object[]{this, var1, var2, var3, var4};
         }

         DynamicJoinPoint var47 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var17, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
         DiagnosticAction[] var10002 = var23 = var10001.getActions();
         InstrumentationSupport.preProcess(var47, var10001, var10002, var24 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var35 = false;

      BaseEJBLocalObjectIntf var49;
      label264: {
         EJBObject var48;
         try {
            var35 = true;
            EntityBean var5 = this.getBeanFromPool();
            ((WLEnterpriseBean)var5).__WL_setBusy(true);
            boolean var6 = false;
            Object var7 = null;
            EJBObject var8 = null;
            BaseEJBLocalObjectIntf var9 = null;

            Throwable var11;
            try {
               var7 = var2.invoke(var5, var4);
            } catch (InvocationTargetException var42) {
               this.destroyPooledBean(var5);
               var11 = var42.getTargetException();
               EJBRuntimeUtils.throwInternalException("Exception in ejbCreate()", var11);
            } catch (Throwable var43) {
               this.destroyPooledBean(var5);
               this.handleMethodException(var2, (Class[])null, var43);
            }

            try {
               if (var7 == null) {
                  if (!(var5 instanceof CMPBean)) {
                     throw new RuntimeCheckerException("Your BMP ejbCreate should not be returning null.");
                  }

                  var3.invoke(var5, var4);
                  var6 = true;
                  var7 = ((CMPBean)var5).__WL_getPrimaryKey();
               }
            } catch (InvocationTargetException var40) {
               this.destroyPooledBean(var5);
               var11 = var40.getTargetException();
               EJBRuntimeUtils.throwInternalException("Exception in ejbCreate()", var11);
            } catch (Throwable var41) {
               this.destroyPooledBean(var5);
               this.handleMethodException(var3, this.extraPostCreateExceptions, var41);
            }

            EntityEJBContextImpl var10 = (EntityEJBContextImpl)((WLEnterpriseBean)var5).__WL_getEJBContext();
            var10.__WL_setPrimaryKey(var7);
            Object var46 = var1.getInvokeTxOrThread();
            CacheKey var12 = new CacheKey(var7, this);

            try {
               this.acquireLock(var1, var7);
            } catch (InternalException var39) {
               this.destroyPooledBean(var5);
               throw var39;
            }

            boolean var13 = false;
            if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
               CMPBean var14 = (CMPBean)this.cache.get(var12);
               if (var14 != null && var14.__WL_getIsRemoved()) {
                  var13 = true;
                  var14.__WL_setBusy(true);
                  var14.__WL_initialize(false);
                  var14.__WL_setIsRemoved(false);
                  if (debugLogger.isDebugEnabled()) {
                     debug("collision occurred, __WL_copyFrom, pk=" + var7 + ", txOrThread=" + var46);
                  }

                  var14.__WL_copyFrom((CMPBean)var5, false);
                  ((WLEnterpriseBean)var5).__WL_setBusy(false);
                  this.releaseBeanToPool(var5);
                  var5 = (EntityBean)var14;
               }
            }

            if (this.remoteHome != null) {
               var8 = this.remoteHome.allocateEO(var7);
            }

            if (this.localHome != null) {
               var9 = this.localHome.allocateELO(var7);
            }

            var10.setEJBObject(var8);
            var10.setEJBLocalObject(var9);
            if (!var13) {
               try {
                  this.cache.put(var12, var5);
                  this.cacheRTMBean.incrementCachedBeansCurrentCount();
               } catch (CacheFullException var38) {
                  this.lockManager.unlock(var7, var46);
                  this.destroyPooledBean(var5);
                  throw new InternalException("Error during create.", var38);
               }
            }

            try {
               if (var13) {
                  this.registerInsertDeletedBeanAndTxUser(var7, var1.getInvokeTx(), (WLEnterpriseBean)var5);
               } else if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
                  this.registerInsertBeanAndTxUser(var7, var1.getInvokeTx(), (WLEnterpriseBean)var5);
               } else {
                  this.setupTxListenerAndTxUser(var7, var46, (WLEnterpriseBean)var5);
               }
            } catch (InternalException var44) {
               this.cache.removeOnError(var12);
               this.lockManager.unlock(var7, var46);
               throw var44;
            }

            if (!var6) {
               try {
                  var3.invoke(var5, var4);
               } catch (IllegalAccessException var36) {
                  throw new AssertionError(var36);
               } catch (InvocationTargetException var37) {
                  this.cache.removeOnError(var12);
                  Throwable var15 = var37.getTargetException();
                  this.handleMethodException(var3, this.extraPostCreateExceptions, var15);
               }
            }

            ((WLEnterpriseBean)var5).__WL_setBusy(false);
            if (var1.isLocal()) {
               var49 = var9;
               var35 = false;
               break label264;
            }

            var48 = var8;
            var35 = false;
         } finally {
            if (var35) {
               var10001 = null;
               if (var22) {
                  InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var23, var24);
               }

            }
         }

         if (var22) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var23, var24);
         }

         return var48;
      }

      if (var22) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var23, var24);
      }

      return var49;
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      Transaction var2 = var1.getInvokeTx();
      Object var3 = var1.getPrimaryKey();
      CacheKey var4 = new CacheKey(var3, this);
      EntityBean var5 = null;
      Iterator var6 = null;

      try {
         try {
            var5 = (EntityBean)this.preInvoke(var1);
         } catch (Exception var8) {
            throw new NoSuchObjectException("Bean with key: " + var1.getPrimaryKey() + " could not be removed." + PlatformConstants.EOL + "The underlying exception was:" + StackTraceUtils.throwable2StackTrace(var8));
         }

         var6 = this.cascadeDeleteRemove(var1, var5);
         if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
            ((CMPBean)var5).__WL_superEjbRemove(false);
            ((CMPBean)var5).__WL_setIsRemoved(true);
            if (!this.registerDeleteBean(var3, var1.getInvokeTx())) {
               if (((CMPBean)var5).__WL_getIsRemoved()) {
                  ((CMPBean)var5).__WL_initialize();
                  ((CMPBean)var5).__WL_setIsRemoved(false);
               }

               this.cache.remove(var4);
            }
         } else {
            var5.ejbRemove();
            this.cache.remove(var4);
         }

         if (this.timerManager != null) {
            this.timerManager.removeTimersForPK(var3);
         }
      } catch (Throwable var9) {
         this.cache.removeOnError(var4);
         EJBRuntimeUtils.throwInternalException("Exception during remove.", var9);
      }

      this.cascadeDeleteRemove(var1, var5, var6);
   }

   public void remove(InvocationWrapper var1, EntityBean var2, boolean var3) throws InternalException {
      Transaction var4 = var1.getInvokeTx();
      Object var5 = ((CMPBean)var2).__WL_getPrimaryKey();
      CacheKey var6 = new CacheKey(var5, this);
      this.checkForReentrant(var2, var5);
      ((WLEnterpriseBean)var2).__WL_setBusy(true);

      try {
         if (var3) {
            ((CMPBean)var2).__WL_superEjbRemove(true);
            this.cache.remove(var6);
         } else if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
            ((CMPBean)var2).__WL_superEjbRemove(false);
            ((CMPBean)var2).__WL_setIsRemoved(true);
            if (!this.registerDeleteBean(var5, var1.getInvokeTx())) {
               if (((CMPBean)var2).__WL_getIsRemoved()) {
                  ((CMPBean)var2).__WL_initialize();
                  ((CMPBean)var2).__WL_setIsRemoved(false);
               }

               this.cache.remove(var6);
            }
         } else {
            var2.ejbRemove();
            this.cache.remove(var6);
         }

         if (this.timerManager != null) {
            this.timerManager.removeTimersForPK(var5);
         }
      } catch (Throwable var8) {
         this.cache.removeOnError(var6);
         EJBRuntimeUtils.throwInternalException("Exception in remove:", var8);
      }

   }

   public EnterpriseBean lookup(Object var1) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("lookup called(pk=" + var1 + ")");
      }

      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         EntityBean var5;
         try {
            this.ejbHome.pushEnvironment();
            Object var2 = EJBRuntimeUtils.getInvokeTxOrThread();
            short var3 = 5000;
            EntityBean var4 = this.getReadyBean(var1, var2, var3);
            var5 = var4;
         } finally {
            this.ejbHome.popEnvironment();
         }

         return var5;
      }
   }

   public EntityBean getBeanFromRS(Object var1, Object var2, RSInfo var3) throws InternalException {
      EntityBean var4 = null;
      var4 = this.getBeanFromCache(var2, var1, 0);
      if (var4 != null) {
         this.cacheRTMBean.incrementCacheHitCount();
      } else {
         var4 = this.getBeanFromPool();
         ((CMPBean)var4).__WL_initialize();
         this.persistence.loadBeanFromRS(var4, var3);
         if (!this.finderCacheInsert(var4)) {
            return null;
         }
      }

      return var4;
   }

   private EntityBean getBeanFromCache(Object var1, Object var2, int var3) throws InternalException {
      return null;
   }

   protected boolean finderCacheInsert(Object var1, Object var2, EJBObject var3, EJBLocalObject var4, EntityBean var5) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("called finderCacheInsert...");
      }

      if (!$assertionsDisabled && var3 == null && var4 == null) {
         throw new java.lang.AssertionError();
      } else {
         boolean var6 = false;

         try {
            if (debugLogger.isDebugEnabled()) {
               debug("\tprimary key=" + var2);
            }

            if (!$assertionsDisabled && var2 == null) {
               throw new java.lang.AssertionError();
            }

            var6 = this.acquireLock(var2, var1, 0);
            if (debugLogger.isDebugEnabled()) {
               debug("\tafter acquireLock: alreadyLoaded=" + (var6 ? "true" : "false"));
            }
         } catch (Exception var27) {
            this.releaseBeanToPool(var5);
            return false;
         }

         CacheKey var7 = new CacheKey(var2, this);

         try {
            boolean var9;
            try {
               this.ejbHome.pushEnvironment();
               EntityBean var8 = (EntityBean)this.cache.get(var7);
               if (var8 == null) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("beanFromCache == null, pk=" + var2 + ", txOrThread=" + var1);
                  }

                  EJBContext var28 = ((WLEnterpriseBean)var5).__WL_getEJBContext();
                  ((WLEJBContext)var28).setEJBObject(var3);
                  ((WLEJBContext)var28).setEJBLocalObject(var4);

                  try {
                     this.cache.put(var7, var5);
                  } catch (CacheFullException var24) {
                     this.releaseBeanToPool(var5);
                     this.lockManager.unlock(var2, var1);
                     boolean var11 = false;
                     return var11;
                  }

                  this.cacheRTMBean.incrementCachedBeansCurrentCount();
                  this.initLastRead(var2);

                  try {
                     var5.ejbActivate();
                     this.cacheRTMBean.incrementActivationCount();
                  } catch (Throwable var23) {
                     EJBLogger.logErrorDuringActivate(StackTraceUtils.throwable2StackTrace(var23));
                     this.destroyPooledBean(var5);
                     EJBRuntimeUtils.throwInternalException("Exception in ejbActivate:", var23);
                     throw new AssertionError("does not reach");
                  }

                  try {
                     ((CMPBean)var5).__WL_superEjbLoad();
                  } catch (Throwable var22) {
                     EJBLogger.logErrorFromLoad(var22);
                     this.destroyPooledBean(var5);
                     EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var22);
                     throw new AssertionError("does not reach");
                  }

                  if (!var6) {
                     this.setupTxListenerAndTxUser(var2, var1, (WLEnterpriseBean)var5);
                  }
               } else {
                  if (debugLogger.isDebugEnabled()) {
                     debug("__WL_copyFrom, pk=" + var2 + ", txOrThread=" + var1);
                  }

                  var9 = this.shouldLoad(var2, this.cacheBetweenTransactions, var6);
                  if (var9) {
                     ((CMPBean)var8).__WL_initialize();
                  }

                  if (this.uses20CMP || var9) {
                     ((CMPBean)var8).__WL_copyFrom((CMPBean)var5, true);
                  }

                  this.releaseBeanToPool(var5);
                  if (var9) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("\tloading cached bean");
                     }

                     try {
                        ((CMPBean)var8).__WL_superEjbLoad();
                     } catch (Throwable var21) {
                        EJBLogger.logErrorFromLoad(var21);
                        EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var21);
                        throw new AssertionError("does not reach");
                     }
                  }

                  if (!var6) {
                     this.setupTxListenerAndTxUser(var2, var1, (WLEnterpriseBean)var8);
                  }
               }

               var9 = var8 == null;
               return var9;
            } catch (Throwable var25) {
               if (debugLogger.isDebugEnabled()) {
                  debug("\texception thrown in finderCacheInsert: ", var25);
               }

               this.cache.removeOnError(var7);
               this.lockManager.unlock(var2, var1);
               var9 = false;
               return var9;
            }
         } finally {
            this.ejbHome.popEnvironment();
         }
      }
   }

   protected void cacheRemoveBean(Transaction var1, Object var2) {
      this.cache.remove(new CacheKey(var2, this));
   }

   protected void cacheRemoveBeanOnError(Transaction var1, Object var2) {
      this.cache.removeOnError(new CacheKey(var2, this));
   }

   protected EntityBean alreadyCached(Object var1, Object var2) throws InternalException {
      EntityBean var3 = null;
      Object var4 = this.lockManager.getOwner(var2);
      if (this.cacheBetweenTransactions || var4 == var1) {
         var3 = (EntityBean)this.cache.get(new CacheKey(var2, this), false);
         if (var3 != null && !this.isBeanManagedPersistence && ((CMPBean)var3).__WL_getIsRemoved()) {
            Loggable var5 = EJBLogger.lognoSuchEntityExceptionLoggable(var2.toString());
            EJBRuntimeUtils.throwInternalException("EJB Exception: ", new ObjectNotFoundException(var5.getMessage()));
            var3 = null;
         }
      }

      return var3;
   }

   protected void initLastRead(Object var1) {
   }

   public void beanImplClassChangeNotification() {
      super.beanImplClassChangeNotification();
      this.cache.beanImplClassChangeNotification();
   }

   public void updateMaxBeansInCache(int var1) {
      this.cache.updateMaxBeansInCache(var1);
   }

   public void updateIdleTimeoutSecondsCache(int var1) {
      this.cache.updateIdleTimeoutSeconds(var1);
   }

   public void releaseBean(InvocationWrapper var1) {
      Object var2 = var1.getPrimaryKey();
      CacheKey var3 = new CacheKey(var2, this);
      this.cache.release(var3);
      this.lockManager.unlock(var2, var1.getInvokeTxOrThread());
   }

   public int getIdleTimeoutSeconds() {
      return this.idleTimeoutSeconds;
   }

   public void reInitializeCacheAndPool() {
      this.cache.reInitializeCacheAndPools();
   }

   public boolean getVerifyReads() {
      return false;
   }

   public void passivateAndRelease(CacheKey var1, EntityBean var2) {
   }

   public boolean hasBeansEnrolledInTx(Transaction var1) {
      return false;
   }

   public PartialOrderSet getEnrolledInTxKeys(Transaction var1) {
      return null;
   }

   public boolean isFlushPending(Transaction var1, Object var2) {
      return false;
   }

   public boolean isPassivatible(Transaction var1, Object var2, boolean var3, boolean var4) {
      return false;
   }

   public int cachePassivateModifiedBean(Transaction var1, Object var2, boolean var3) {
      return 0;
   }

   public int cachePassivateUnModifiedBean(Transaction var1, Object var2) {
      return 0;
   }

   public boolean passivateModifiedBean(Transaction var1, Object var2, boolean var3, EntityBean var4) {
      return false;
   }

   public boolean passivateUnModifiedBean(Transaction var1, Object var2, EntityBean var3) {
      return false;
   }

   public void postFinderCleanup(Object var1, Collection var2, boolean var3, boolean var4) {
   }

   public void unpin(Object var1, Object var2) {
   }

   public void flushModified(Collection var1, Transaction var2, boolean var3, Collection var4) {
   }

   public void operationsComplete(Transaction var1, Object var2) {
      EntityBean var3 = null;

      try {
         var3 = this.getReadyBean(var2, var1, 0);
      } catch (InternalException var5) {
      }

      if (var3 != null) {
         ((WLEntityBean)var3).__WL_setOperationsComplete(true);
      }
   }

   public boolean beanIsOpsComplete(Transaction var1, Object var2) {
      EntityBean var3 = null;

      try {
         var3 = this.getReadyBean(var2, var1, 0);
      } catch (InternalException var5) {
      }

      return var3 == null ? true : ((WLEntityBean)var3).__WL_getOperationsComplete();
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExclusiveEntityManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[ExclusiveEntityManager] " + var0, var1);
   }

   public void undeploy() {
      super.undeploy();
      if (this.scrubberStarted) {
         Debug.assertion(this.cache instanceof ScrubbedCache, "expected ScrubbedCache");
         this.cache.stopScrubber();
      }

   }

   public void passivateAndBacktoPool(CacheKey var1, EntityBean var2) {
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Create_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ExclusiveEntityManager.java", "weblogic.ejb.container.manager.ExclusiveEntityManager", "preInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)Ljavax/ejb/EnterpriseBean;", 252, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ExclusiveEntityManager.java", "weblogic.ejb.container.manager.ExclusiveEntityManager", "postInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 482, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ExclusiveEntityManager.java", "weblogic.ejb.container.manager.ExclusiveEntityManager", "create", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 789, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null, null, null})}), (boolean)0);
      $assertionsDisabled = !ExclusiveEntityManager.class.desiredAssertionStatus();
      staticChecks = true;
   }
}
