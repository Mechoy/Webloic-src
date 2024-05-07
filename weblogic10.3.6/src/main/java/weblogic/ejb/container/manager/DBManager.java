package weblogic.ejb.container.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import weblogic.cluster.ClusterService;
import weblogic.cluster.GroupMessage;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.ejb.OptimisticConcurrencyException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.cache.EntityCache;
import weblogic.ejb.container.cmp.rdbms.RDBMSPersistenceManager;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.WLEJBContext;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLEntityBean;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.EntityEJBContextImpl;
import weblogic.ejb.container.internal.EntityEJBLocalObject;
import weblogic.ejb.container.internal.EntityEJBObject;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.InvalidationMessage;
import weblogic.ejb.spi.ScrubbedCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.RuntimeCheckerException;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.ServerMBean;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public class DBManager extends BaseEntityManager implements BeanManager, CMPBeanManager, CachingManager, InvalidationBeanManager, RecoverListener {
   private boolean publicCacheOnly;
   protected EntityCache publicCache;
   private boolean scrubberStarted = false;
   private EntityBeanInfo info;
   private boolean delayUpdatesUntilEndOfTx = true;
   private int beanSize;
   private int idleTimeoutSeconds = 0;
   protected InvalidationBeanManager invalidationTargetBM = null;
   private boolean cacheBetweenTransactions;
   protected boolean clusterInvalidationDisabled;
   protected MulticastSession multicastSession;
   private boolean verifyReads = false;
   private static final Object DUMMY_PK;
   private static int VERIFY_THRESHHOLD;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 8801993973041449919L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.DBManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public DBManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      throw new AssertionError("BeanManager.setup() should never be called on DBManager.");
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4);
      this.info = (EntityBeanInfo)var3;
      this.publicCacheOnly = true;
      if (var5 == null) {
         this.publicCache = new EntityCache(this.info.getEJBName(), this.info.getCachingDescriptor().getMaxBeansInCache());
         this.beanSize = 1;
         this.idleTimeoutSeconds = this.info.getCachingDescriptor().getIdleTimeoutSecondsCache();
         this.publicCache.setScrubInterval(this.idleTimeoutSeconds);
         this.publicCache.startScrubber();
         this.scrubberStarted = true;
      } else {
         if (!(var5 instanceof EntityCache)) {
            Loggable var8 = EJBLogger.lognotAMultiVersionCacheLoggable(this.info.getEJBName(), this.info.getCacheName());
            throw new WLDeploymentException(var8.getMessage());
         }

         this.publicCache = (EntityCache)var5;
         if (this.publicCache.usesMaxBeansInCache()) {
            this.beanSize = 1;
         } else {
            this.beanSize = this.info.getEstimatedBeanSize();
         }

         this.idleTimeoutSeconds = this.info.getCachingDescriptor().getIdleTimeoutSecondsCache();
         this.publicCache.setScrubInterval(this.idleTimeoutSeconds);
      }

      this.publicCache.register(this);
      this.getEJBCacheRuntimeMBeanImpl().setReInitializableCache(this.publicCache);
      this.cacheBetweenTransactions = this.info.getCacheBetweenTransactions();
      String var6 = this.info.getCachingDescriptor().getConcurrencyStrategy();
      if (var6 == null || "Database".equalsIgnoreCase(var6)) {
         this.cacheBetweenTransactions = false;
      }

      if (!this.cacheBetweenTransactions && (this.info.getConcurrencyStrategy() == 2 || this.info.getConcurrencyStrategy() == 6)) {
         this.publicCache.setDisableReadyCache(this.info.getDisableReadyInstances());
      }

      this.delayUpdatesUntilEndOfTx = this.info.getBoxCarUpdates();
      this.invalidationTargetBM = this.info.getInvalidationTargetBeanManager();
      boolean var7 = ((ServerMBean)Kernel.getConfig()).getCluster() != null;
      if (!var7 || var6 == null || this.rdbmsPersistence != null && this.rdbmsPersistence.getRDBMSBean().isClusterInvalidationDisabled()) {
         this.clusterInvalidationDisabled = true;
      } else if ((!var6.equalsIgnoreCase("Optimistic") || !this.cacheBetweenTransactions) && !var6.equalsIgnoreCase("ReadOnly")) {
         this.clusterInvalidationDisabled = true;
      } else {
         this.multicastSession = ClusterService.getServices().createMulticastSession(this, -1);
         this.clusterInvalidationDisabled = false;
      }

      if (this.rdbmsPersistence != null) {
         this.verifyReads = this.rdbmsPersistence.getVerifyReads();
      }

   }

   protected EntityCache getCache() {
      return this.publicCache;
   }

   public EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException {
      boolean var12 = false;

      EntityBean var10000;
      try {
         var12 = true;
         super.preInvoke();
         Object var2 = var1.getInvokeTxOrThread();
         Object var3 = var1.getPrimaryKey();
         EntityBean var4 = this.getReadyBean(var2, var3, true);
         this.checkForReentrant(var4, var3);
         ((WLEnterpriseBean)var4).__WL_setBusy(true);
         var10000 = var4;
         var12 = false;
      } finally {
         if (var12) {
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

   private EntityBean getReadyBean(Object var1, Object var2, boolean var3) throws InternalException {
      EntityBean var4 = null;
      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         var4 = this.getCache().get(var1, new CacheKey(var2, this), var3);
         this.cacheRTMBean.incrementCacheAccessCount();
         if (var4 != null) {
            if (var1 instanceof Thread) {
               try {
                  this.loadBean(var2, var4, (RSInfo)null, false);
               } catch (Throwable var9) {
                  EJBLogger.logErrorFromLoad(var9);
                  this.getCache().removeOnError(var1, new CacheKey(var2, this));
                  EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var9);
                  throw new AssertionError("cannot reach");
               }
            }

            this.cacheRTMBean.incrementCacheHitCount();
            return var4;
         } else {
            var4 = this.getBeanFromPool();
            if (!$assertionsDisabled && ((WLEnterpriseBean)var4).__WL_isBusy()) {
               throw new java.lang.AssertionError();
            } else {
               EJBContext var5 = ((WLEnterpriseBean)var4).__WL_getEJBContext();
               ((EntityEJBContextImpl)var5).__WL_setPrimaryKey(var2);
               EJBObject var6 = null;
               BaseEJBLocalObjectIntf var7 = null;
               if (this.remoteHome != null) {
                  var6 = this.remoteHome.allocateEO(var2);
               }

               ((WLEJBContext)var5).setEJBObject(var6);
               if (this.localHome != null) {
                  var7 = this.localHome.allocateELO(var2);
               }

               ((WLEJBContext)var5).setEJBLocalObject(var7);

               try {
                  var4.ejbActivate();
                  this.cacheRTMBean.incrementActivationCount();
               } catch (Throwable var12) {
                  EJBLogger.logErrorDuringActivate(StackTraceUtils.throwable2StackTrace(var12));
                  this.destroyPooledBean(var4);
                  EJBRuntimeUtils.throwInternalException("Exception in ejbActivate", var12);
                  throw new AssertionError("will not reach");
               }

               if (!$assertionsDisabled && var4 == null) {
                  throw new java.lang.AssertionError();
               } else {
                  if (this.supportsCopy()) {
                     this.perhapsCopy(var2, var4);
                  }

                  try {
                     this.loadBean(var2, var4, (RSInfo)null, true);
                  } catch (Throwable var11) {
                     EJBLogger.logErrorFromLoad(var11);
                     this.destroyPooledBean(var4);
                     EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var11);
                     throw new AssertionError("cannot reach");
                  }

                  try {
                     this.setupTxListenerAndTxUser(var2, var1, (WLEnterpriseBean)var4);
                     this.getCache().put(var1, new CacheKey(var2, this), var4, this, var3);
                     this.cacheRTMBean.incrementCachedBeansCurrentCount();
                     return var4;
                  } catch (Throwable var10) {
                     EJBLogger.logErrorFromLoad(var10);
                     this.destroyPooledBean(var4);
                     EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var10);
                     throw new AssertionError("cannot reach");
                  }
               }
            }
         }
      }
   }

   public int getBeanSize() {
      return this.beanSize;
   }

   public boolean isEntityManager() {
      return true;
   }

   public void enrollInTransaction(Transaction var1, CacheKey var2, EntityBean var3, RSInfo var4) throws InternalException {
      try {
         Object var5 = var2.getPrimaryKey();
         this.loadBean(var5, var3, var4, false);
         this.setupTxListenerAndTxUser(var5, var1, (WLEnterpriseBean)var3);
      } catch (Throwable var6) {
         EJBLogger.logErrorFromLoad(var6);
         this.getCache().removeOnError(var1, var2);
         EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var6);
         throw new AssertionError("cannot reach");
      }
   }

   public int getIdleTimeoutSeconds() {
      return this.idleTimeoutSeconds;
   }

   public boolean getVerifyReads() {
      return this.verifyReads;
   }

   public void selectedForReplacement(CacheKey var1, EntityBean var2) {
      this.passivateAndRelease(var1, var2);
      this.cacheRTMBean.decrementCachedBeansCurrentCount();
   }

   public void passivateAndRelease(CacheKey var1, EntityBean var2) {
      try {
         var2.ejbPassivate();
         this.releaseBeanToPool(var2);
      } catch (Throwable var8) {
         EJBLogger.logErrorPassivating(StackTraceUtils.throwable2StackTrace(var8));
         this.destroyPooledBean(var2);
      } finally {
         this.cacheRTMBean.incrementPassivationCount();
      }

   }

   public void loadBeanFromRS(CacheKey var1, EntityBean var2, RSInfo var3) throws InternalException {
      if (this.uses20CMP) {
         ((RDBMSPersistenceManager)this.persistence).loadBeanFromRS(var2, var3);
      }

   }

   public void removedOnError(CacheKey var1, EnterpriseBean var2) {
      this.cacheRTMBean.decrementCachedBeansCurrentCount();
      this.destroyPooledBean((EntityBean)var2);
   }

   public void removedFromCache(CacheKey var1, EnterpriseBean var2) {
      ((WLEnterpriseBean)var2).__WL_setBusy(false);
      this.releaseBeanToPool((EntityBean)var2);
      this.cacheRTMBean.decrementCachedBeansCurrentCount();
   }

   public void swapIn(CacheKey var1, EnterpriseBean var2) {
      throw new AssertionError("method 'swapIn' not valid for DBManager");
   }

   public void swapOut(CacheKey var1, EnterpriseBean var2) {
      throw new AssertionError("method 'swapOut' not valid for DBManager");
   }

   public void replicate(CacheKey var1, EnterpriseBean var2) {
      throw new AssertionError("method 'replicate' not valid for DBManager");
   }

   protected void loadBean(Object var1, EntityBean var2, RSInfo var3, boolean var4) throws Throwable {
      if (var3 == null) {
         if (var4 || !this.cacheBetweenTransactions || !((CMPBean)var2).__WL_isBeanStateValid()) {
            var2.ejbLoad();
            if (!this.isBeanManagedPersistence && this.uses20CMP) {
               ((CMPBean)var2).__WL_setBeanStateValid(true);
            }
         }
      } else {
         CMPBean var5 = (CMPBean)var2;
         if (!this.cacheBetweenTransactions || !((CMPBean)var2).__WL_isBeanStateValid()) {
            var5.__WL_initialize();
            if (!this.isBeanManagedPersistence && this.uses20CMP) {
               var5.__WL_setBeanStateValid(true);
            }
         }

         this.persistence.loadBeanFromRS(var2, var3);
         var5.__WL_superEjbLoad();
      }

   }

   protected void storeBean(EntityBean var1, Object var2) throws Throwable {
      if (this.shouldStore(var1)) {
         var1.ejbStore();
      }

   }

   public void postInvoke(InvocationWrapper var1) throws InternalException {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium.isEnabledAndNotDyeFiltered()) {
         Object[] var12 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium.isArgumentsCaptureNeeded()) {
            var12 = new Object[]{this, var1};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var12, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      EntityBean var2 = (EntityBean)var1.getBean();
      WLEnterpriseBean var3 = (WLEnterpriseBean)var2;
      Object var4 = var1.getPrimaryKey();
      CacheKey var5 = new CacheKey(var4, this);
      Throwable var6 = null;
      boolean var7 = false;
      synchronized(var2) {
         var3.__WL_setBusy(false);
         if (var3.__WL_needsRemove()) {
            if (debugLogger.isDebugEnabled()) {
               debug("postInvoke: setNeedsRemove, txOrThread= " + var1.getInvokeTxOrThread() + ", ejb- " + this.info.getEJBName() + ", pk- " + var4);
            }

            var3.__WL_setLoadUser((Object)null);
            if (this.cacheBetweenTransactions) {
               try {
                  this.storeBean(var2, var4);
               } catch (Throwable var14) {
                  EJBLogger.logErrorFromStore(var14);
                  var6 = var14;
               }

               if (var6 == null) {
                  throw new AssertionError("storeBean was expected to fail!");
               }
            } else if (this.remoteHome == null || !var3.__WL_isCreatorOfTx() || var1.getInvokeTx() == null) {
               var7 = true;
            }
         } else if (!this.delayUpdatesUntilEndOfTx || var1.getInvokeTx() == null) {
            try {
               this.storeBean(var2, var4);
            } catch (Throwable var13) {
               EJBLogger.logErrorFromStore(var13);
               var6 = var13;
            }
         }
      }

      if (var7) {
         this.cacheReleaseBean((EntityBean)null, var1.getInvokeTxOrThread(), var5, this.getCache());
      } else {
         if (var6 != null) {
            this.getCache().removeOnError(var1.getInvokeTxOrThread(), var5);
            if (debugLogger.isDebugEnabled()) {
               debug("postInvoke: ejbStore failed, txOrThread= " + var1.getInvokeTxOrThread() + ", ejb- " + this.info.getEJBName() + ", pk- " + var4);
            }

            EJBRuntimeUtils.throwInternalException("Exception in ejbStore:", var6);
         }

         this.getCache().unpin(var1.getInvokeTxOrThread(), var5);
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
      Object var3 = var1.getInvokeTxOrThread();
      Object var4 = var1.getPrimaryKey();
      if (var4 != null) {
         if (!$assertionsDisabled && var3 == null) {
            throw new java.lang.AssertionError();
         } else {
            if (var1.shouldLogException()) {
               EJBLogger.logErrorDuringBeanInvocation(this.ejbHome.getDisplayName(), var4.toString(), var2);
            }

            EntityBean var5 = (EntityBean)var1.getBean();
            if (!$assertionsDisabled && var5 == null) {
               throw new java.lang.AssertionError();
            } else if (!$assertionsDisabled && var4 == null) {
               throw new java.lang.AssertionError();
            } else {
               this.getCache().removeOnError(var3, new CacheKey(var4, this));
            }
         }
      }
   }

   private void doVerificationForBatch(List var1, StringBuffer[] var2, int[] var3, Transaction var4, boolean var5) throws SQLException {
      Connection var6 = null;
      PreparedStatement[] var7 = null;
      ResultSet[] var8 = null;

      try {
         var6 = this.rdbmsPersistence.getConnection();
         var7 = this.rdbmsPersistence.prepareStatement(var6, var2, var3, var5);
         if (debugLogger.isDebugEnabled()) {
            for(int var9 = 0; var9 < var2.length; ++var9) {
               debug("verifySql[" + var9 + "]: " + var2[var9]);
            }
         }

         int[] var18 = this.rdbmsPersistence.getVerifyCur();
         Iterator var10 = var1.iterator();

         while(var10.hasNext()) {
            Object var11 = var10.next();
            new CacheKey(var11, this);
            CMPBean var13 = (CMPBean)this.getCache().getActive(var4, new CacheKey(var11, this), false);
            if (var13 == null) {
               throw new AssertionError("no bean found for pk: " + var11);
            }

            var13.__WL_setVerifyParamsForBatch(var6, var7, var18);
         }

         var8 = this.rdbmsPersistence.executeQuery(var7);
         this.rdbmsPersistence.checkResults(var8, var3);
      } finally {
         this.rdbmsPersistence.releaseArrayResources(var6, var7, var8);
      }

   }

   protected void prepareVerificationForBatch(Collection var1, Transaction var2) throws InternalException {
      if (this.rdbmsPersistence.needsBatchOperationsWorkaround()) {
         ArrayList var3 = new ArrayList();
         StringBuffer[] var4 = null;
         Integer var5 = (Integer)((TransactionImpl)var2).getProperty("ISOLATION LEVEL");
         boolean var6 = var5 == null || var5 != 4 && var5 != 8;
         if (debugLogger.isDebugEnabled()) {
            debug("Require exclusive lock for batch? " + var6);
         }

         var4 = this.rdbmsPersistence.getVerifySql(var6);
         int[] var7 = this.rdbmsPersistence.getVerifyCount();
         int var8 = 0;
         Iterator var14;
         if (debugLogger.isDebugEnabled()) {
            for(int var9 = 0; var9 < var4.length; ++var9) {
               debug("sql[" + var9 + "] = " + var4[var9]);
               debug("count[" + var9 + "] = " + var7[var9]);
               debug("verifyMax = " + var8);
            }

            var14 = var3.iterator();

            while(var14.hasNext()) {
               debug("pk- " + var14.next());
            }

            debug("*************************************************");
         }

         var14 = var1.iterator();

         try {
            while(true) {
               CMPBean var11;
               do {
                  if (!var14.hasNext()) {
                     if (var8 > 0) {
                        this.doVerificationForBatch(var3, var4, var7, var2, var6);
                     }

                     return;
                  }

                  Object var10 = var14.next();
                  var11 = (CMPBean)this.getCache().getActive(var2, new CacheKey(var10, this), false);
               } while(var11 == null);

               var8 = var11.__WL_appendVerifySqlForBatch(var3, var4, var7, var8);
               if (debugLogger.isDebugEnabled()) {
                  for(int var12 = 0; var12 < var4.length; ++var12) {
                     debug("sql[" + var12 + "] = " + var4[var12]);
                     debug("count[" + var12 + "] = " + var7[var12]);
                     debug("verifyMax = " + var8);
                  }

                  Iterator var15 = var3.iterator();

                  while(var15.hasNext()) {
                     debug("pk- " + var15.next());
                  }

                  debug("------------------------------------------");
               }

               if (var8 >= VERIFY_THRESHHOLD) {
                  this.doVerificationForBatch(var3, var4, var7, var2, var6);
                  var3.clear();
                  var4 = this.rdbmsPersistence.getVerifySql(var6);
                  var7 = this.rdbmsPersistence.getVerifyCount();
                  var8 = 0;
               }
            }
         } catch (Throwable var13) {
            if (!(var13 instanceof OptimisticConcurrencyException)) {
               EJBLogger.logExcepInBeforeCompletion(StackTraceUtils.throwable2StackTrace(var13));
            }

            EJBRuntimeUtils.throwInternalException("Exception during before completion:", var13);
            throw new AssertionError("cannot reach");
         }
      }
   }

   private void doVerification(List var1, StringBuffer[] var2, int[] var3, Transaction var4, boolean var5) throws SQLException {
      Connection var6 = null;

      try {
         var6 = this.rdbmsPersistence.getConnection();
         PreparedStatement[] var7 = null;
         var7 = this.rdbmsPersistence.prepareStatement(var6, var2, var3, var5);
         if (debugLogger.isDebugEnabled()) {
            for(int var8 = 0; var8 < var2.length; ++var8) {
               debug("verifySql[" + var8 + "]: " + var2[var8]);
            }
         }

         int[] var17 = this.rdbmsPersistence.getVerifyCur();
         Iterator var9 = var1.iterator();

         while(var9.hasNext()) {
            Object var10 = var9.next();
            CMPBean var11 = (CMPBean)this.getCache().getActive(var4, new CacheKey(var10, this), false);
            if (var11 == null) {
               throw new AssertionError("no bean found for pk: " + var10);
            }

            var11.__WL_setVerifyParams(var6, var7, var17);
         }

         ResultSet[] var16 = this.rdbmsPersistence.executeQuery(var7);
         this.rdbmsPersistence.checkResults(var16, var3);
      } finally {
         this.rdbmsPersistence.releaseResources(var6, (Statement)null, (ResultSet)null);
      }

   }

   protected List pkListToBeanList(Collection var1, Transaction var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         CacheKey var7 = new CacheKey(var6, this);
         EntityBean var8 = this.getCache().getActive(var2, var7, false);
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
         EntityBean var8 = this.getCache().getActive(var2, var7, false);
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
      if (!$assertionsDisabled && var2 == null) {
         throw new java.lang.AssertionError();
      } else if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         if (this.verifyReads) {
            Transaction var3 = (Transaction)var2;
            ArrayList var4 = new ArrayList();
            StringBuffer[] var5 = null;
            Integer var6 = (Integer)((TransactionImpl)var3).getProperty("ISOLATION LEVEL");
            boolean var7 = var6 == null || var6 != 4 && var6 != 8;
            if (debugLogger.isDebugEnabled()) {
               debug("require exclusive lock? " + var7);
            }

            var5 = this.rdbmsPersistence.getVerifySql(var7);
            int[] var8 = this.rdbmsPersistence.getVerifyCount();
            int var9 = 0;
            Iterator var22;
            if (debugLogger.isDebugEnabled()) {
               for(int var10 = 0; var10 < var5.length; ++var10) {
                  debug("sql[" + var10 + "] = " + var5[var10]);
                  debug("count[" + var10 + "] = " + var8[var10]);
                  debug("verifyMax = " + var9);
               }

               var22 = var4.iterator();

               while(var22.hasNext()) {
                  debug("pk- " + var22.next());
               }

               debug("************************************************");
            }

            var22 = var1.iterator();

            try {
               label130:
               while(true) {
                  CMPBean var23;
                  do {
                     if (!var22.hasNext()) {
                        if (var9 > 0) {
                           this.doVerification(var4, var5, var8, var3, var7);
                        }
                        break label130;
                     }

                     Object var11 = var22.next();
                     var23 = (CMPBean)this.getCache().getActive(var3, new CacheKey(var11, this), false);
                  } while(var23 == null);

                  var9 = var23.__WL_appendVerifySql(var4, var5, var8, var9);
                  if (debugLogger.isDebugEnabled()) {
                     for(int var24 = 0; var24 < var5.length; ++var24) {
                        debug("sql[" + var24 + "] = " + var5[var24]);
                        debug("count[" + var24 + "] = " + var8[var24]);
                        debug("verifyMax = " + var9);
                     }

                     Iterator var25 = var4.iterator();

                     while(var25.hasNext()) {
                        debug("pk- " + var25.next());
                     }

                     debug("------------------------------------------");
                  }

                  if (var9 >= VERIFY_THRESHHOLD) {
                     this.doVerification(var4, var5, var8, var3, var7);
                     var4.clear();
                     var5 = this.rdbmsPersistence.getVerifySql(var7);
                     var8 = this.rdbmsPersistence.getVerifyCount();
                     var9 = 0;
                  }
               }
            } catch (Throwable var16) {
               if (!(var16 instanceof OptimisticConcurrencyException)) {
                  EJBLogger.logExcepInBeforeCompletion(StackTraceUtils.throwable2StackTrace(var16));
               } else {
                  Iterator var12 = var4.iterator();

                  while(var12.hasNext()) {
                     CacheKey var13 = new CacheKey(var12.next(), this);
                     CMPBean var14 = (CMPBean)this.getCache().getActive(var2, var13, false);
                     var14.__WL_setBeanStateValid(false);
                  }
               }

               EJBRuntimeUtils.throwInternalException("Exception during before completion:", var16);
               throw new AssertionError("cannot reach");
            }
         }

         CacheKey var19;
         for(Iterator var17 = var1.iterator(); var17.hasNext(); this.getCache().unpin(var2, var19)) {
            Object var18 = var17.next();
            var19 = new CacheKey(var18, this);
            EntityBean var20 = this.getCache().getActive(var2, var19, true);

            try {
               if (var20 != null && this.shouldStore(var20)) {
                  Transaction var21 = null;
                  if (var2 instanceof Transaction) {
                     var21 = (Transaction)var2;
                  }

                  if (this.orderDatabaseOperations && var21 != null) {
                     ((CMPBean)var20).__WL_superEjbStore();
                  } else {
                     var20.ejbStore();
                  }
               }
            } catch (Throwable var15) {
               if (!(var15 instanceof OptimisticConcurrencyException)) {
                  EJBLogger.logExcepFromStore(var15);
               }

               this.getCache().removeOnError(var2, new CacheKey(var18, this));
               EJBRuntimeUtils.throwInternalException("Exception from ejbStore:", var15);
               throw new AssertionError("cannot reach");
            }
         }

      }
   }

   public void flushModified(Collection var1, Transaction var2, boolean var3, Collection var4) throws InternalException {
      if (!$assertionsDisabled && var2 == null) {
         throw new java.lang.AssertionError();
      } else if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         Iterator var5 = var1.iterator();

         while(true) {
            while(var5.hasNext()) {
               Object var6 = var5.next();
               EntityBean var7 = this.getCache().getActive(var2, new CacheKey(var6, this), false);
               if (this.isBeanManagedPersistence) {
                  try {
                     if (var7 != null && this.shouldStore(var7)) {
                        var7.ejbStore();
                        if (var3) {
                           var4.add(var6);
                        }
                     }
                  } catch (Throwable var9) {
                     this.getCache().removeOnError(var2, new CacheKey(var6, this));
                     EJBRuntimeUtils.throwInternalException("Error writing from flushModified", var9);
                  }
               } else {
                  try {
                     if (var7 != null && this.shouldStore(var7)) {
                        if (this.orderDatabaseOperations && var2 != null) {
                           ((CMPBean)var7).__WL_superEjbStore();
                        } else {
                           ((CMPBean)var7).__WL_store(false);
                           if (var3) {
                              var4.add(var6);
                           }
                        }
                     }
                  } catch (Throwable var10) {
                     EJBLogger.logExcepFromStore(var10);
                     this.getCache().removeOnError(var2, new CacheKey(var6, this));
                     EJBRuntimeUtils.throwInternalException("Error writing from flushModified", var10);
                  }
               }
            }

            return;
         }
      }
   }

   private void passivateAndRemove(Transaction var1, Object var2, CacheKey var3, EntityBean var4) {
      boolean var5 = false;

      try {
         var4.ejbPassivate();
      } catch (Throwable var11) {
         EJBLogger.logErrorPassivating(StackTraceUtils.throwable2StackTrace(var11));
         var5 = true;
      } finally {
         this.cacheRTMBean.incrementPassivationCount();
      }

      if (!var5) {
         if (debugLogger.isDebugEnabled()) {
            debug("afterCompletion: tx- " + var1 + ", ejb- " + this.info.getEJBName() + ", pk- " + var2 + " removed from cache.");
         }

         this.getCache().remove(var1, var3);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("afterCompletion: tx- " + var1 + ", ejb- " + this.info.getEJBName() + ", pk- " + var2 + " removedOnError from cache.");
         }

         this.getCache().removeOnError(var1, var3);
      }

   }

   public void afterCompletion(Collection var1, Transaction var2, int var3, Object var4) {
      this.afterCompletion(var1, (Object)var2, var3, var4);
   }

   public void afterCompletion(Collection var1, Object var2, int var3, Object var4) {
      Iterator var5 = var1.iterator();

      while(true) {
         Object var6;
         CacheKey var7;
         EntityCache var8;
         EntityBean var9;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = var5.next();
            var7 = new CacheKey(var6, this);
            var8 = var4 != null ? (EntityCache)var4 : this.publicCache;
            var9 = var8.getActive(var2, var7, true);
         } while(var9 == null);

         boolean var10 = false;
         synchronized(var9) {
            WLEntityBean var12 = (WLEntityBean)var9;
            if (!var12.__WL_isBusy()) {
               var12.__WL_setLoadUser((Object)null);
               if (this.cacheBetweenTransactions && var3 != 3) {
                  if (!this.isBeanManagedPersistence && this.uses20CMP) {
                     CMPBean var13 = (CMPBean)var9;
                     if (!this.isOptimistic) {
                        var13.__WL_setBeanStateValid(false);
                     } else if (var13.__WL_isModified()) {
                        var13.__WL_setBeanStateValid(false);
                     } else {
                        var13.__WL_clearCMRFields();
                     }
                  }

                  var10 = true;
               } else if (this.remoteHome == null || !var12.__WL_isCreatorOfTx()) {
                  var10 = true;
                  if (debugLogger.isDebugEnabled()) {
                     debug("afterCompletion: txOrThread- " + var2 + ", ejb- " + this.info.getEJBName() + ", pk- " + var6 + " released from cache.");
                  }
               }
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debug("afterCompletion: setNeedsRemove, txOrThread= " + var2 + ", ejb- " + this.info.getEJBName() + ", pk- " + var6);
               }

               var12.__WL_setNeedsRemove(true);
            }
         }

         if (var10) {
            this.cacheReleaseBean(var9, var2, var7, var8);
         } else {
            var8.unpin(var2, var7);
         }
      }
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      return (EJBObject)this.create(var1, var2, var3, var4);
   }

   public EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      return (EJBLocalObject)this.create(var1, var2, var3, var4);
   }

   public Object create(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      boolean var26;
      boolean var10000 = var26 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var27 = null;
      DiagnosticActionState[] var28 = null;
      Object var25 = null;
      DynamicJoinPoint var61;
      if (var10000) {
         Object[] var21 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var21 = new Object[]{this, var1, var2, var3, var4};
         }

         var61 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var21, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
         DiagnosticAction[] var10002 = var27 = var10001.getActions();
         InstrumentationSupport.preProcess(var61, var10001, var10002, var28 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var39 = false;

      Object var63;
      label581: {
         EJBObject var62;
         try {
            var39 = true;
            EntityBean var5 = this.getBeanFromPool();
            ((WLEnterpriseBean)var5).__WL_setBusy(true);
            boolean var6 = false;
            Object var7 = null;
            CacheKey var8 = null;
            EJBObject var9 = null;
            Object var10 = null;
            EJBContext var11 = ((WLEnterpriseBean)var5).__WL_getEJBContext();
            if (this.remoteHome != null) {
               var9 = this.remoteHome.allocateEO(DUMMY_PK);
            }

            ((WLEJBContext)var11).setEJBObject(var9);
            if (this.localHome != null) {
               var10 = this.localHome.allocateELO(DUMMY_PK);
            }

            ((WLEJBContext)var11).setEJBLocalObject((EJBLocalObject)var10);

            Throwable var13;
            try {
               var7 = var2.invoke(var5, var4);
            } catch (IllegalAccessException var54) {
               throw new AssertionError(var54);
            } catch (InvocationTargetException var55) {
               var13 = var55.getTargetException();
               if (debugLogger.isDebugEnabled()) {
                  debug("Error during create: ", var13);
               }

               this.destroyPooledBean(var5);
               this.handleMethodException(var2, (Class[])null, var13);
            }

            try {
               if (var7 == null) {
                  if (!(var5 instanceof CMPBean)) {
                     throw new InternalException("Error during create.", new RuntimeCheckerException("Your BMP ejbCreate should not be returning null."));
                  }

                  var3.invoke(var5, var4);
                  var6 = true;
                  var7 = ((CMPBean)var5).__WL_getPrimaryKey();
               }

               if (this.remoteHome != null) {
                  var9 = ((EntityEJBContextImpl)var11).__WL_getEJBObject();
                  ((EntityEJBObject)var9).setPrimaryKey(var7);
               }

               if (this.localHome != null) {
                  var10 = ((EntityEJBContextImpl)var11).__WL_getEJBLocalObject();
                  ((EntityEJBLocalObject)var10).setPrimaryKey(var7);
               }
            } catch (IllegalAccessException var56) {
               throw new AssertionError(var56);
            } catch (InvocationTargetException var57) {
               var13 = var57.getTargetException();
               if (debugLogger.isDebugEnabled()) {
                  debug("Error during postCreate: " + var13);
               }

               this.destroyPooledBean(var5);
               this.handleMethodException(var3, this.extraPostCreateExceptions, var13);
            }

            EntityEJBContextImpl var12 = (EntityEJBContextImpl)((WLEnterpriseBean)var5).__WL_getEJBContext();
            var12.__WL_setPrimaryKey(var7);
            var8 = new CacheKey(var7, this);
            Object var60 = var1.getInvokeTxOrThread();
            if (!$assertionsDisabled && var60 == null) {
               throw new java.lang.AssertionError();
            }

            boolean var14 = false;
            boolean var15 = false;
            if (!this.isBeanManagedPersistence) {
               CMPBean var16 = (CMPBean)this.getCache().getActive(var60, var8, true);
               if (var16 != null) {
                  var15 = true;
                  if (this.orderDatabaseOperations && var16.__WL_getIsRemoved()) {
                     var14 = true;
                     var16.__WL_setIsRemoved(false);
                  }

                  var16.__WL_setBusy(true);
                  var16.__WL_initialize(false);
                  if (debugLogger.isDebugEnabled()) {
                     debug("collision occurred, __WL_copyFrom, pk=" + var7 + ", txOrThread=" + var60);
                  }

                  var16.__WL_copyFrom((CMPBean)var5, false);
                  ((WLEnterpriseBean)var5).__WL_setBusy(false);
                  this.releaseBeanToPool(var5);
                  var5 = (EntityBean)var16;
                  ((WLEntityBean)var5).__WL_setOperationsComplete(false);
               }
            }

            if (!var15) {
               try {
                  this.getCache().put(var60, var8, var5, this, true);
                  this.cacheRTMBean.incrementCachedBeansCurrentCount();
                  this.initLastLoad(var7, var5);
               } catch (CacheFullException var52) {
                  this.destroyPooledBean(var5);
                  throw new InternalException("Error during create.", var52);
               }
            }

            try {
               if (var14) {
                  this.registerInsertDeletedBeanAndTxUser(var7, var1.getInvokeTx(), (WLEnterpriseBean)var5);
               } else if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
                  this.registerInsertBeanAndTxUser(var7, var1.getInvokeTx(), (WLEnterpriseBean)var5);
               } else {
                  this.setupTxListenerAndTxUser(var7, var60, (WLEnterpriseBean)var5);
               }
            } catch (InternalException var58) {
               this.getCache().removeOnError(var60, new CacheKey(var7, this));
               throw var58;
            }

            if (!var6) {
               try {
                  Debug.assertion(var3 != null);
                  Debug.assertion(var5 != null);
                  Debug.assertion(var4 != null);
                  if (var14) {
                     ((CMPBean)var5).__WL_setCreateAfterRemove(true);
                  }

                  var3.invoke(var5, var4);
               } catch (IllegalAccessException var50) {
                  throw new AssertionError(var50);
               } catch (InvocationTargetException var51) {
                  Throwable var17 = var51.getTargetException();
                  this.getCache().removeOnError(var60, var8);
                  this.handleMethodException(var3, this.extraPostCreateExceptions, var17);
               } finally {
                  if (var14) {
                     ((CMPBean)var5).__WL_setCreateAfterRemove(false);
                  }

               }
            }

            ((WLEnterpriseBean)var5).__WL_setBusy(false);
            this.getCache().unpin(var60, var8);
            if (var1.isLocal()) {
               var63 = var10;
               var39 = false;
               break label581;
            }

            var62 = var9;
            var39 = false;
         } finally {
            if (var39) {
               var61 = null;
               if (var26) {
                  InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var27, var28);
               }

            }
         }

         if (var26) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var27, var28);
         }

         return var62;
      }

      if (var26) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var27, var28);
      }

      return var63;
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      EntityBean var2 = (EntityBean)this.preInvoke(var1);
      Object var3 = var1.getInvokeTxOrThread();
      Object var4 = var1.getPrimaryKey();
      Iterator var5 = null;

      try {
         var5 = this.cascadeDeleteRemove(var1, var2);
         if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
            ((CMPBean)var2).__WL_superEjbRemove(false);
            ((CMPBean)var2).__WL_setIsRemoved(true);
            if (!this.registerDeleteBean(var4, var1.getInvokeTx())) {
               if (((CMPBean)var2).__WL_getIsRemoved()) {
                  ((CMPBean)var2).__WL_initialize();
                  ((CMPBean)var2).__WL_setIsRemoved(false);
               }

               this.getCache().remove(var3, new CacheKey(var4, this));
            }
         } else {
            var2.ejbRemove();
            this.getCache().remove(var3, new CacheKey(var4, this));
         }

         if (this.timerManager != null) {
            this.timerManager.removeTimersForPK(var4);
         }
      } catch (Throwable var7) {
         ((WLEnterpriseBean)var2).__WL_setBusy(false);
         this.getCache().removeOnError(var3, new CacheKey(var4, this));
         this.handleMethodException(var1.getMethodDescriptor().getMethod(), (Class[])null, var7);
      }

      this.cascadeDeleteRemove(var1, var2, var5);
   }

   public void remove(InvocationWrapper var1, EntityBean var2, boolean var3) throws InternalException {
      Object var4 = var1.getInvokeTxOrThread();
      CMPBean var5 = (CMPBean)var2;
      EntityEJBContextImpl var6 = (EntityEJBContextImpl)var5.__WL_getEntityContext();
      Object var7 = var6.__WL_getPrimaryKey();
      this.checkForReentrant(var2, var7);
      ((WLEnterpriseBean)var2).__WL_setBusy(true);

      try {
         if (var3) {
            ((CMPBean)var2).__WL_superEjbRemove(true);
            this.getCache().remove(var4, new CacheKey(var7, this));
         } else if (this.orderDatabaseOperations && var1.getInvokeTx() != null) {
            ((CMPBean)var2).__WL_superEjbRemove(false);
            ((CMPBean)var2).__WL_setIsRemoved(true);
            if (!this.registerDeleteBean(var7, var1.getInvokeTx())) {
               if (((CMPBean)var2).__WL_getIsRemoved()) {
                  ((CMPBean)var2).__WL_initialize();
                  ((CMPBean)var2).__WL_setIsRemoved(false);
               }

               this.getCache().remove(var4, new CacheKey(var7, this));
            }
         } else {
            var2.ejbRemove();
            this.getCache().remove(var4, new CacheKey(var7, this));
         }

         if (this.timerManager != null) {
            this.timerManager.removeTimersForPK(var7);
         }
      } catch (Throwable var9) {
         this.getCache().removeOnError(var4, new CacheKey(var7, this));
         this.handleMethodException(var1.getMethodDescriptor().getMethod(), (Class[])null, var9);
      }

   }

   public EnterpriseBean lookup(Object var1) throws InternalException {
      Object var2 = EJBRuntimeUtils.getInvokeTxOrThread();
      EntityBean var3 = this.getReadyBean(var2, var1, false);
      return var3;
   }

   public EntityBean getBeanFromRS(Object var1, Object var2, RSInfo var3) throws InternalException {
      EntityBean var4 = null;
      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else {
         var4 = this.getCache().get(var1, new CacheKey(var2, this), var3, true);
         this.cacheRTMBean.incrementCacheAccessCount();
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
   }

   protected boolean finderCacheInsert(Object var1, Object var2, EJBObject var3, EJBLocalObject var4, EntityBean var5) throws InternalException {
      if (!$assertionsDisabled && var3 == null && var4 == null) {
         throw new java.lang.AssertionError();
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("called finderCacheInsert...");
         }

         try {
            this.ejbHome.pushEnvironment();
            EJBContext var6 = ((WLEnterpriseBean)var5).__WL_getEJBContext();
            ((WLEJBContext)var6).setEJBObject(var3);
            ((WLEJBContext)var6).setEJBLocalObject(var4);

            try {
               var5.ejbActivate();
               this.cacheRTMBean.incrementActivationCount();
            } catch (Throwable var17) {
               EJBLogger.logErrorDuringActivate(StackTraceUtils.throwable2StackTrace(var17));
               this.destroyPooledBean(var5);
               EJBRuntimeUtils.throwInternalException("Exception in ejbActivate", var17);
               throw new AssertionError("will not reach");
            }

            try {
               ((CMPBean)var5).__WL_superEjbLoad();
            } catch (Throwable var16) {
               EJBLogger.logExcepFromSuperLoad(var16);
               this.destroyPooledBean(var5);
               EJBRuntimeUtils.throwInternalException("Exception in superEjbLoad:", var16);
               throw new AssertionError("cannot reach");
            }
         } finally {
            this.ejbHome.popEnvironment();
         }

         try {
            Transaction var20 = null;
            if (var1 instanceof Transaction) {
               var20 = (Transaction)var1;
            }

            this.setupTxListenerAndTxUser(var2, var20, (WLEnterpriseBean)var5);
         } catch (InternalException var19) {
            this.destroyPooledBean(var5);
            throw var19;
         }

         try {
            this.getCache().put(var1, new CacheKey(var2, this), var5, this, true);
            this.cacheRTMBean.incrementCachedBeansCurrentCount();
            this.initLastLoad(var2, var5);
            return true;
         } catch (CacheFullException var15) {
            this.passivateAndRelease(new CacheKey(var2, this), var5);
            return false;
         }
      }
   }

   public void postFinderCleanup(Object var1, Collection var2, boolean var3, boolean var4) {
      if (this.findersLoadBean) {
         Object var5 = null;
         var5 = TxHelper.getTransaction();
         if (var5 == null) {
            var5 = Thread.currentThread();
         }

         Object var6 = null;
         if (var1 != null) {
            if (var3) {
               var6 = var1;
            } else if (var4) {
               var6 = ((EJBLocalObject)var1).getPrimaryKey();
            } else {
               try {
                  var6 = ((EJBObject)var1).getPrimaryKey();
               } catch (RemoteException var11) {
               }
            }

            if (var6 != null) {
               this.getCache().unpin(var5, new CacheKey(var6, this));
            }
         } else if (var2 != null) {
            Iterator var7;
            if (var3) {
               var7 = var2.iterator();

               while(var7.hasNext()) {
                  var6 = var7.next();
                  if (var6 != null) {
                     this.getCache().unpin(var5, new CacheKey(var6, this));
                  }
               }
            } else {
               var7 = var2.iterator();

               while(var7.hasNext()) {
                  var6 = null;
                  if (var4) {
                     EJBLocalObject var8 = (EJBLocalObject)var7.next();
                     if (var8 != null) {
                        var6 = var8.getPrimaryKey();
                     }
                  } else {
                     EJBObject var12 = (EJBObject)var7.next();
                     if (var12 != null) {
                        try {
                           var6 = var12.getPrimaryKey();
                        } catch (RemoteException var10) {
                        }
                     }
                  }

                  if (var6 != null) {
                     this.getCache().unpin(var5, new CacheKey(var6, this));
                  }
               }
            }
         }

      }
   }

   public void unpin(Object var1, Object var2) {
      this.getCache().unpin(var1, new CacheKey(var2, this));
   }

   protected void initLastLoad(Object var1, EntityBean var2) {
   }

   protected boolean supportsCopy() {
      return false;
   }

   protected void perhapsCopy(Object var1, EntityBean var2) throws InternalException {
   }

   protected void cacheRemoveBean(Transaction var1, Object var2) {
      this.getCache().remove(var1, new CacheKey(var2, this));
   }

   protected void cacheRemoveBeanOnError(Transaction var1, Object var2) {
      this.getCache().removeOnError(var1, new CacheKey(var2, this));
   }

   protected EntityBean alreadyCached(Object var1, Object var2) throws InternalException {
      EntityBean var3 = null;
      if (this.cacheBetweenTransactions) {
         var3 = this.getCache().getValid(var1, new CacheKey(var2, this), false);
      } else {
         var3 = this.getCache().getActive(var1, new CacheKey(var2, this), false);
      }

      if (var3 != null && !this.isBeanManagedPersistence && ((CMPBean)var3).__WL_getIsRemoved()) {
         Loggable var4 = EJBLogger.lognoSuchEntityExceptionLoggable(var2.toString());
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", new ObjectNotFoundException(var4.getMessage()));
         var3 = null;
      }

      return var3;
   }

   public void setMaxBeansInCache(int var1) {
      this.getCache().setMaxBeansInCache(var1);
   }

   public GroupMessage createRecoverMessage() {
      return new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName());
   }

   protected void sendInvalidate(Object var1) throws InternalException {
      InvalidationMessage var2;
      if (var1 == null) {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName());
      } else if (var1 instanceof Collection) {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName(), (Collection)var1);
      } else {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName(), var1);
      }

      try {
         this.multicastSession.send(var2);
      } catch (IOException var5) {
         Loggable var4 = EJBLogger.logErrorWhileMulticastingInvalidationLoggable(this.ejbHome.getDisplayName(), var5);
         throw new InternalException(var4.getMessage(), var5);
      }
   }

   public void invalidate(Object var1, Object var2) throws InternalException {
      this.invalidateLocalServer(var1, var2);
      if (!this.clusterInvalidationDisabled) {
         this.sendInvalidate(var2);
      }

   }

   public void invalidate(Object var1, Collection var2) throws InternalException {
      this.invalidateLocalServer(var1, var2);
      if (!this.clusterInvalidationDisabled) {
         this.sendInvalidate(var2);
      }

      this.resetInvalidationFlag(var1, var2);
   }

   public void invalidateAll(Object var1) throws InternalException {
      this.invalidateAllLocalServer(var1);
      if (!this.clusterInvalidationDisabled) {
         this.sendInvalidate((Object)null);
      }

   }

   public void invalidateLocalServer(Object var1, Object var2) {
      this.getCache().invalidate(var1, new CacheKey(var2, this));
      if (this.invalidationTargetBM != null) {
         this.invalidationTargetBM.invalidateLocalServer(var1, var2);
      }

   }

   public void invalidateLocalServer(Object var1, Collection var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var3.add(new CacheKey(var5, this));
      }

      this.getCache().invalidate(var1, (Collection)var3);
      if (this.invalidationTargetBM != null) {
         this.invalidationTargetBM.invalidateLocalServer(var1, var2);
      }

   }

   private void resetInvalidationFlag(Object var1, Collection var2) {
      if (var1 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            EntityBean var4 = this.getCache().getActive(var1, new CacheKey(var3.next(), this), false);
            if (var4 != null) {
               ((CMPBean)var4).__WL_setInvalidatedBeanIsRegistered(false);
            }
         }

      }
   }

   public void invalidateAllLocalServer(Object var1) {
      this.getCache().invalidateAll(var1);
      if (this.invalidationTargetBM != null) {
         this.invalidationTargetBM.invalidateAllLocalServer(var1);
      }

   }

   public void beanImplClassChangeNotification() {
      super.beanImplClassChangeNotification();
      this.getCache().beanImplClassChangeNotification();
   }

   public void updateMaxBeansInCache(int var1) {
      this.getCache().updateMaxBeansInCache(var1);
   }

   public void releaseBean(InvocationWrapper var1) {
      try {
         Object var2 = var1.getInvokeTxOrThread();
         Object var3 = var1.getPrimaryKey();
         CacheKey var4 = new CacheKey(var3, this);
         this.cacheReleaseBean((EntityBean)null, var2, var4, this.getCache());
      } catch (Exception var5) {
      }

   }

   private void cacheReleaseBean(EntityBean var1, Object var2, CacheKey var3, EntityCache var4) {
      if (var1 == null) {
         var1 = var4.getActive(var2, var3, false);
         if (var1 == null) {
            return;
         }
      }

      ((WLEntityBean)var1).__WL_setOperationsComplete(false);
      if (var1 instanceof CMPBean) {
         ((CMPBean)var1).__WL_setNonFKHolderRelationChange(false);
         ((CMPBean)var1).__WL_setM2NInsert(false);
      }

      var4.release(var2, var3);
   }

   public void reInitializeCacheAndPool() {
      this.getCache().reInitializeCacheAndPools();
   }

   public int cachePassivateModifiedBean(Transaction var1, Object var2, boolean var3) {
      return this.getCache().passivateModifiedBean(var1, new CacheKey(var2, this), var3);
   }

   public int cachePassivateUnModifiedBean(Transaction var1, Object var2) {
      int var3 = this.getCache().passivateUnModifiedBean(var1, new CacheKey(var2, this));

      for(int var4 = 0; var4 < var3; ++var4) {
         this.cacheRTMBean.decrementCachedBeansCurrentCount();
      }

      return var3;
   }

   public void operationsComplete(Transaction var1, Object var2) {
      EntityBean var3 = this.getCache().getActive(var1, new CacheKey(var2, this), false);
      if (var3 != null) {
         ((WLEntityBean)var3).__WL_setOperationsComplete(true);
      }
   }

   public boolean beanIsOpsComplete(Transaction var1, Object var2) {
      EntityBean var3 = this.getCache().getActive(var1, new CacheKey(var2, this), false);
      return var3 == null ? true : ((WLEntityBean)var3).__WL_getOperationsComplete();
   }

   private static void debug(String var0) {
      debugLogger.debug("[DBManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[DBManager] " + var0, var1);
   }

   public void updateIdleTimeoutSecondsCache(int var1) {
      this.getCache().updateIdleTimeoutSeconds(var1);
   }

   public void undeploy() {
      super.undeploy();
      if (this.scrubberStarted) {
         Debug.assertion(this.publicCache instanceof ScrubbedCache, "expected ScrubbedCache");
         this.publicCache.stopScrubber();
      }

   }

   public void passivateAndBacktoPool(CacheKey var1, EntityBean var2) {
      try {
         var2.ejbPassivate();
         ((WLEnterpriseBean)var2).__WL_setBusy(false);
         this.releaseBeanToPool(var2);
      } catch (Throwable var8) {
         EJBLogger.logErrorPassivating(StackTraceUtils.throwable2StackTrace(var8));
         this.destroyPooledBean(var2);
      } finally {
         this.cacheRTMBean.incrementPassivationCount();
         this.cacheRTMBean.decrementCachedBeansCurrentCount();
      }

   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Create_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DBManager.java", "weblogic.ejb.container.manager.DBManager", "preInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)Ljavax/ejb/EnterpriseBean;", 303, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Preinvoke_Entity_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DBManager.java", "weblogic.ejb.container.manager.DBManager", "postInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 567, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Postinvoke_Entity_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DBManager.java", "weblogic.ejb.container.manager.DBManager", "create", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 1408, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Create_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null, null, null})}), (boolean)0);
      $assertionsDisabled = !DBManager.class.desiredAssertionStatus();
      DUMMY_PK = new Object();
      VERIFY_THRESHHOLD = 50;
   }
}
