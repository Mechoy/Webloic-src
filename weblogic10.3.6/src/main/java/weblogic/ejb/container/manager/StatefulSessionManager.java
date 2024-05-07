package weblogic.ejb.container.manager;

import com.oracle.pitchfork.interfaces.EnterpriseBeanProxy;
import com.oracle.pitchfork.interfaces.TargetWrapper;
import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import com.oracle.pitchfork.interfaces.intercept.__ProxyControl;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.ejb.NoSuchEJBException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.naming.Context;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionSynchronizationRegistry;
import weblogic.deployment.PersistenceUnitInfoImpl;
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
import weblogic.ejb.container.cache.BaseCache;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.cache.LRUCache;
import weblogic.ejb.container.cache.NRUCache;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingDescriptor;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.SingleInstanceCache;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb.container.interfaces.WLSessionEJBContext;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.BaseEJBContext;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.ExtendedPersistenceContextManager;
import weblogic.ejb.container.internal.ExtendedPersistenceContextWrapper;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.PoolHelper;
import weblogic.ejb.container.internal.SessionEJBContextImpl;
import weblogic.ejb.container.internal.StatefulEJBHome;
import weblogic.ejb.container.internal.StatefulEJBHomeImpl;
import weblogic.ejb.container.internal.StatefulEJBLocalHome;
import weblogic.ejb.container.internal.StatefulEJBLocalObject;
import weblogic.ejb.container.internal.StatefulEJBObject;
import weblogic.ejb.container.internal.StatefulRemoteObject;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.locks.ExclusiveLockManager;
import weblogic.ejb.container.locks.LockManager;
import weblogic.ejb.container.monitoring.EJBCacheRuntimeMBeanImpl;
import weblogic.ejb.container.monitoring.StatefulEJBRuntimeMBeanImpl;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.swap.DiskSwap;
import weblogic.ejb.container.swap.EJBSwap;
import weblogic.ejb.container.swap.ReplicatedMemorySwap;
import weblogic.ejb.container.utils.PartialOrderSet;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.ejb.spi.ScrubbedCache;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.ejb20.locks.LockTimedOutException;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.StatefulEJBRuntimeMBean;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TxHelper;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.collections.PartitionedStackPool;

public class StatefulSessionManager extends BaseEJBManager implements BeanManager, CachingManager {
   private static final boolean ENABLE_PROXY_POOL;
   private static final AuthenticatedSubject kernelId;
   private SingleInstanceCache cache;
   private LockManager lockManager;
   private boolean isInMemoryReplication = false;
   private boolean implementsSessionSynchronization = false;
   private int idleTimeoutSeconds;
   protected EJBSwap swapper;
   protected SessionBeanInfo beanInfo;
   private StatefulEJBHome remoteHome = null;
   private StatefulEJBLocalHome localHome = null;
   protected KeyGenerator keyGenerator;
   protected boolean serializeCalls;
   private int defaultTransactionTimeoutMS;
   private StatefulEJBRuntimeMBean runtimeMBean;
   private EJBCacheRuntimeMBeanImpl cacheRTMBean;
   private AuthenticatedSubject fileDesc;
   private AuthenticatedSubject fileSector;
   private String dirName;
   private String sectorName;
   private Map extendedPersistenceContextMap = null;
   private PartitionedStackPool proxyPool = null;
   private Object bmTxLockClient = new Object();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -6964093428947732392L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.ejb.container.manager.StatefulSessionManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;

   public StatefulSessionManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   protected final EJBCacheRuntimeMBeanImpl getCacheRuntime() {
      return this.cacheRTMBean;
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException {
      throw new AssertionError("BeanManager.setup() should never be called on StatefulSessionManager.");
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4);
      this.beanInfo = (SessionBeanInfo)var3;
      this.remoteHome = (StatefulEJBHome)var1;
      this.localHome = (StatefulEJBLocalHome)var2;
      this.beanClass = this.beanInfo.getGeneratedBeanClass();

      Loggable var7;
      try {
         this.runtimeMBean = new StatefulEJBRuntimeMBeanImpl(var3.getEJBName(), var3.getEJBName(), this.getEJBComponentRuntime());
         this.setEJBRuntimeMBean(this.runtimeMBean);
         this.addEJBRuntimeMBean(this.runtimeMBean);
      } catch (ManagementException var9) {
         var7 = EJBLogger.logFailedToCreateRuntimeMBeanLoggable(var9);
         throw new WLDeploymentException(var7.getMessage(), var9);
      }

      this.txManager = new TxManager(this);
      this.cacheRTMBean = (EJBCacheRuntimeMBeanImpl)this.runtimeMBean.getCacheRuntime();
      this.keyGenerator = new SimpleKeyGenerator();
      this.keyGenerator.setup(var3);
      this.swapper = new DiskSwap(new File(this.getSwapDirectoryName()), this.beanInfo.getIdleTimeoutMS(), this.beanInfo.getSessionTimeoutMS());
      this.swapper.setup(var3, this, var3.getClassLoader());
      this.initializeCache();
      if (ENABLE_PROXY_POOL) {
         this.proxyPool = new PartitionedStackPool(var3.getCachingDescriptor().getMaxBeansInCache(), 20);
      }

      this.implementsSessionSynchronization = this.beanInfo.implementsSessionSynchronization();
      this.serializeCalls = this.beanInfo.statefulSessionSerializesConcurrentCalls();
      this.defaultTransactionTimeoutMS = this.beanInfo.getTransactionTimeoutMS();
      this.lockManager = new ExclusiveLockManager(this.runtimeMBean.getLockingRuntime());
      this.lockManager.setup(var3);
      int var6 = this.beanInfo.getReplicationType();
      this.isInMemoryReplication = var6 == 2 && this.inCluster();
      this.sectorName = var3.getPassivateAsPrincipalName();
      this.dirName = var3.getRunAsPrincipalName();
      if (this.sectorName != null || this.dirName != null) {
         PoolHelper var11 = new PoolHelper(var3.getDeploymentInfo().getSecurityRealmName(), var3.getJACCPolicyConfig(), var3.getJACCPolicyContextId(), var3.getJACCCodeSource(), var3.getJACCRoleMapper());

         try {
            if (this.sectorName != null) {
               this.fileSector = var11.getFileDesc(this.sectorName);
            }

            if (this.dirName != null) {
               this.fileDesc = var11.getFileDesc(this.dirName);
            }
         } catch (Exception var10) {
            throw new WLDeploymentException(var10.toString());
         }

         var7 = null;
      }

   }

   private void initializeCache() throws WLDeploymentException {
      if (!$assertionsDisabled && this.cache != null) {
         throw new java.lang.AssertionError();
      } else {
         CachingDescriptor var1 = this.beanInfo.getCachingDescriptor();
         String var2 = System.getProperty("weblogic.ejb.stateful.cache");
         if (var2 != null) {
            try {
               Class var3 = Class.forName(var2);
               Constructor var4 = var3.getConstructor(String.class, Integer.TYPE);
               this.cache = (SingleInstanceCache)var4.newInstance(this.ejbHome.getDisplayName(), var1.getMaxBeansInCache());
            } catch (Exception var5) {
               throw new WLDeploymentException("Exception instantiating custom Stateful Session Bean cache: " + var2, var5);
            }
         } else if (var1.getCacheType().equalsIgnoreCase("LRU")) {
            this.cache = new LRUCache(this.ejbHome.getDisplayName(), var1.getMaxBeansInCache());
         } else {
            this.cache = new NRUCache(this.ejbHome.getDisplayName(), var1.getMaxBeansInCache());
         }

         this.cache.register(this);
         this.cacheRTMBean.setReInitializableCache((ReInitializableCache)this.cache);
         this.idleTimeoutSeconds = var1.getIdleTimeoutSecondsCache();
         ((ScrubbedCache)this.cache).setScrubInterval(this.idleTimeoutSeconds);
         ((ScrubbedCache)this.cache).startScrubber();
      }
   }

   public void setExtendedPersistenceContextMap(Map var1) {
      this.extendedPersistenceContextMap = var1;
   }

   private Object getLockClient(Object var1) {
      if (var1 == null) {
         return this.usesBeanManagedTx() ? this.bmTxLockClient : Thread.currentThread();
      } else {
         return var1;
      }
   }

   private int getLockTimeout(InvocationWrapper var1) {
      if (this.serializeCalls) {
         MethodDescriptor var2 = var1.getMethodDescriptor();
         return var2 != null ? var2.getTxTimeoutMS() : this.defaultTransactionTimeoutMS;
      } else {
         return 0;
      }
   }

   private boolean acquireLock(InvocationWrapper var1) throws InternalException {
      Object var2 = var1.getPrimaryKey();
      return this.acquireLock(var1, var2);
   }

   private boolean acquireLock(InvocationWrapper var1, Object var2) throws InternalException {
      Transaction var3 = var1.getInvokeTx();

      try {
         return this.lockManager.lock(var2, this.getLockClient(var3), this.getLockTimeout(var1));
      } catch (LockTimedOutException var6) {
         Loggable var5 = EJBLogger.logNoExecuteSFSBMethodInDifferentTxLoggable();
         throw new InternalException(var5.getMessage(), var6);
      }
   }

   protected EnterpriseBean getBean(Object var1) throws InternalException {
      CacheKey var2 = new CacheKey(var1, this);
      this.getCacheRuntime().incrementCacheAccessCount();
      EnterpriseBean var3 = this.cache.get(var2);
      if (var3 == null) {
         var3 = this.swapper.read(var1);
         if (var3 != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("Found key: " + var1 + " in swap.");
            }

            try {
               this.cache.put(var2, var3);
               this.getCacheRuntime().incrementCachedBeansCurrentCount();
            } catch (CacheFullException var6) {
               EJBRuntimeUtils.throwInternalException("Exception in remote create", var6);
            }

            try {
               ((SessionBean)var3).ejbActivate();
               this.getCacheRuntime().incrementActivationCount();
            } catch (Exception var5) {
               EJBLogger.logExcepInActivate(StackTraceUtils.throwable2StackTrace(var5));
               this.removeBean(var2, var3);
               EJBRuntimeUtils.throwInternalException("Exception in ejbActivate:", var5);
            }
         } else {
            EJBRuntimeUtils.throwInternalException("Error calling get bean.", new NoSuchEJBException("Stateful session bean has been deleted."));
         }
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("** Found key: " + var1 + " in the EJB Cache.");
         }

         this.getCacheRuntime().incrementCacheHitCount();
      }

      return var3;
   }

   public EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException {
      boolean var25 = false;

      EnterpriseBean var10000;
      DelegatingMonitor var10002;
      try {
         var25 = true;
         super.preInvoke();
         Object var2 = var1.getPrimaryKey();
         Transaction var3 = var1.getInvokeTx();
         EnterpriseBean var4 = null;
         boolean var5 = !this.acquireLock(var1);

         try {
            var4 = this.getBean(var2);
            if (this.extendedPersistenceContextMap != null && var3 != null && !this.usesBeanManagedTx()) {
               Set var6 = ((WLSessionBean)var4).getExtendedPersistenceContexts();
               Iterator var7 = var6.iterator();

               while(var7.hasNext()) {
                  ExtendedPersistenceContextWrapper var8 = (ExtendedPersistenceContextWrapper)var7.next();
                  String var9 = var8.getPersistenceUnitName();
                  TransactionSynchronizationRegistry var10 = (TransactionSynchronizationRegistry)TransactionHelper.getTransactionHelper().getTransactionManager();
                  Object var11 = var10.getResource(var9);
                  if (var11 != null) {
                     if (!var11.equals(var8.getEntityManager())) {
                        EJBRuntimeUtils.throwInternalException("Error invoking EJB:", new EJBException("Error, the EJB " + this.ejbHome.getDisplayName() + " has an Extended Persistence Context and was invoked " + " in the context of a transaction that is already " + " associated with a different Persistence Context"));
                     }
                  } else {
                     var10.putResource(var9, var8.getEntityManager());
                     var8.getEntityManager().joinTransaction();
                  }
               }
            }

            if (this.usesBeanManagedTx()) {
               if (var5) {
                  ((WLEnterpriseBean)var4).__WL_setBeanManagedTransaction((Transaction)null);
               } else {
                  Transaction var33 = ((WLEnterpriseBean)var4).__WL_getBeanManagedTransaction();
                  if (var33 != null) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("** Resuming transaction on key: " + var2);
                     }

                     try {
                        TxHelper.getTransactionManager().resume(var33);
                     } catch (InvalidTransactionException var29) {
                        ((WLEnterpriseBean)var4).__WL_setBeanManagedTransaction((Transaction)null);
                        this.cache.remove(new CacheKey(var2, this));
                        this.lockManager.unlock(var2, this.getLockClient((Object)null));
                        throw var29;
                     } catch (SystemException var30) {
                        SystemException var35 = var30;
                        EJBLogger.logExcepResumingTx(var30);
                        ((WLEnterpriseBean)var4).__WL_setBeanManagedTransaction((Transaction)null);
                        this.removeBean(new CacheKey(var2, this), var4);

                        try {
                           if (var3 instanceof weblogic.transaction.Transaction) {
                              ((weblogic.transaction.Transaction)var3).setRollbackOnly("Couldn't resume transaction " + var33, var35);
                           } else {
                              var3.setRollbackOnly();
                           }
                        } catch (SystemException var26) {
                        }

                        this.lockManager.unlock(var2, this.getLockClient((Object)null));
                        throw new InternalException("Exception trying to resume transaction", var30);
                     }
                  }
               }
            }

            if (var3 != null && var5) {
               try {
                  this.setupTxListener(var1);
               } catch (InternalException var28) {
                  this.removeBean(new CacheKey(var2, this), var4);
                  throw var28;
               }
            }

            if (var5 && this.implementsSessionSynchronization) {
               try {
                  if (var3 != null) {
                     ((SessionSynchronization)var4).afterBegin();
                  }
               } catch (Throwable var27) {
                  EJBLogger.logExcepInAfterBegin(StackTraceUtils.throwable2StackTrace(var27));
                  this.removeBean(new CacheKey(var2, this), var4);
                  EJBRuntimeUtils.throwInternalException("Exception in afterBegin", var27);
               }
            }
         } catch (Throwable var31) {
            if (var5) {
               this.lockManager.unlock(var2, this.getLockClient(var3));
            }

            EJBRuntimeUtils.throwInternalException("Stateful Session Exception:", var31);
         }

         if (((WLEnterpriseBean)var4).__WL_isBusy()) {
            Loggable var34 = EJBLogger.logIllegalMakeReentrantCallSFSBLoggable(this.beanInfo.getDisplayName());
            ConcurrentAccessException var36 = new ConcurrentAccessException(var34.getMessageText());
            var34 = EJBLogger.logIllegalMakeReentrantCallSFSBFromHomeLoggable(this.ejbHome.getDisplayName());
            throw new InternalException(var34.getMessageText(), var36);
         }

         ((WLEnterpriseBean)var4).__WL_setBusy(true);
         var10000 = var4;
         var25 = false;
      } finally {
         if (var25) {
            var10000 = null;
            if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium.isEnabledAndNotDyeFiltered()) {
               var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
               InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
            }

         }
      }

      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium.isEnabledAndNotDyeFiltered()) {
         var10002 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium;
         InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10002, var10002.getActions());
      }

      return var10000;
   }

   public void postInvoke(InvocationWrapper var1) throws InternalException {
      if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium.isEnabledAndNotDyeFiltered()) {
         Object[] var15 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium.isArgumentsCaptureNeeded()) {
            var15 = new Object[]{this, var1};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var15, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (!$assertionsDisabled && var1 == null) {
         throw new java.lang.AssertionError();
      } else if (!$assertionsDisabled && var1.getPrimaryKey() == null) {
         throw new java.lang.AssertionError();
      } else {
         Transaction var2 = var1.getInvokeTx();
         Object var3 = var1.getPrimaryKey();
         CacheKey var4 = new CacheKey(var3, this);
         WLEnterpriseBean var5 = (WLEnterpriseBean)var1.getBean();
         var5.__WL_setBusy(false);
         if (this.usesBeanManagedTx()) {
            weblogic.transaction.Transaction var31 = TxHelper.getTransaction();
            if (var31 == null) {
               if (debugLogger.isDebugEnabled()) {
                  debug("** releasing key because BM has no associated tx:" + var3);
               }

               var5.__WL_setBeanManagedTransaction((Transaction)null);
               this.replicateAndRelease(var4, (EnterpriseBean)var5);
               this.lockManager.unlock(var3, this.getLockClient((Object)null));
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debug("** keeping lock and associating tx for:" + var3);
               }

               try {
                  if (var31.getStatus() == 0) {
                     TxHelper.getTransactionManager().suspend();
                     var5.__WL_setBeanManagedTransaction(var31);
                  } else if (var31.getStatus() == 1 && var31 instanceof weblogic.transaction.Transaction) {
                     weblogic.transaction.Transaction var6 = (weblogic.transaction.Transaction)var31;
                     var6.setProperty("DISABLE_TX_STATUS_CHECK", "true");
                     TxHelper.getTransactionManager().suspend();
                     var5.__WL_setBeanManagedTransaction(var31);
                  } else {
                     var5.__WL_setBeanManagedTransaction((Transaction)null);
                  }
               } catch (SystemException var30) {
                  var5.__WL_setBeanManagedTransaction((Transaction)null);
               }
            }
         } else if (var2 == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("** releasing non BM tx: " + var3);
            }

            this.replicateAndRelease(var4, (EnterpriseBean)var5);
            this.lockManager.unlock(var3, this.getLockClient(var2));
         } else {
            synchronized(var5) {
               try {
                  if (var5.__WL_needsRemove() && this.implementsSessionSynchronization) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("*** postInvoke called after afterCompletion****");
                     }

                     SessionSynchronization var7 = (SessionSynchronization)this.cache.get(var4);

                     try {
                        this.ejbHome.pushEnvironment();
                        if (((WLSessionBean)var5).__WL_needsSessionSynchronization()) {
                           var7.afterCompletion(var2.getStatus() == 3);
                        }
                     } catch (Throwable var26) {
                        EJBLogger.logExcepInAfterCompletion(StackTraceUtils.throwable2StackTrace(var26));
                        this.removeBean(var4, var7);
                     } finally {
                        this.ejbHome.popEnvironment();
                     }
                  }
               } finally {
                  if (debugLogger.isDebugEnabled()) {
                     debug("****releasing the bean inside postInvoke****");
                  }

                  if (var5.__WL_needsRemove()) {
                     var5.__WL_setNeedsRemove(false);
                     this.replicateAndRelease(var4, (EnterpriseBean)var5);
                     this.lockManager.unlock(var3, this.getLockClient(var2));
                  }

               }
            }
         }

      }
   }

   public EnterpriseBean preHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("Stateful session beans cannot have home methods");
   }

   public void postHomeInvoke(InvocationWrapper var1) throws InternalException {
      throw new AssertionError("Stateful session beans cannot have home methods");
   }

   public void destroyPooledInstance(InvocationWrapper var1, Throwable var2) throws InternalException {
      throw new AssertionError("Stateful session beans cannot have home methods");
   }

   public void destroyInstance(InvocationWrapper var1, Throwable var2) {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      if (var10000) {
         Object[] var7 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High.isArgumentsCaptureNeeded()) {
            var7 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var7, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var17, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         Object var3 = var1.getPrimaryKey();
         CacheKey var4 = new CacheKey(var3, this);
         if (!$assertionsDisabled && var3 == null) {
            throw new java.lang.AssertionError();
         }

         WLEnterpriseBean var5 = (WLEnterpriseBean)var1.getBean();
         var5.__WL_setBusy(false);
         this.removeBean(var4, var5);
         if (var1.getInvokeTx() == null || var5.__WL_needsRemove()) {
            this.lockManager.unlock(var3, this.getLockClient((Object)null));
         }
      } finally {
         if (var12) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High, var13, var14);
         }

      }

   }

   private void removeBean(CacheKey var1, Object var2) {
      this.cache.remove(var1);
      if (var2 != null) {
         ((WLSessionBean)var2).closeExtendedPersistenceContexts();
      }

   }

   private void releaseProxy(Object var1) {
      if (ENABLE_PROXY_POOL && this.beanInfo.isEJB30()) {
         EnterpriseBeanProxy var2 = (EnterpriseBeanProxy)var1;
         TargetWrapper var3 = (TargetWrapper)var2.getTarget();
         var3.removeTarget();
         this.proxyPool.add(var2);
      }

   }

   public void beforeCompletion(InvocationWrapper var1) throws InternalException {
   }

   public void beforeCompletion(Object var1) throws InternalException {
   }

   public void beforeCompletion(Collection var1, Transaction var2) throws InternalException {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         CacheKey var5 = new CacheKey(var4, this);
         if (this.implementsSessionSynchronization) {
            SessionSynchronization var6 = (SessionSynchronization)this.cache.get(var5);
            if (var6 != null) {
               try {
                  this.ejbHome.pushEnvironment();
                  if (((WLSessionBean)var6).__WL_needsSessionSynchronization()) {
                     var6.beforeCompletion();
                  }
               } catch (Throwable var15) {
                  Throwable var7 = var15;
                  EJBLogger.logExcepInBeforeCompletion(StackTraceUtils.throwable2StackTrace(var15));
                  this.removeBean(var5, var6);

                  try {
                     if (var2 instanceof weblogic.transaction.Transaction) {
                        ((weblogic.transaction.Transaction)var2).setRollbackOnly("beforeCompletion() threw an exception", var7);
                     } else {
                        var2.setRollbackOnly();
                     }
                  } catch (SystemException var14) {
                     EJBLogger.logExcepDuringSetRollbackOnly(var14);
                  }

                  EJBRuntimeUtils.throwInternalException("Exception in beforeCompletion:", var15);
               } finally {
                  this.ejbHome.popEnvironment();
               }
            }
         }
      }

   }

   public void afterCompletion(InvocationWrapper var1) {
   }

   public void afterCompletion(Object var1) {
   }

   public void afterCompletion(Collection var1, Transaction var2, int var3, Object var4) {
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         CacheKey var7 = new CacheKey(var6, this);
         boolean var8 = true;
         WLEnterpriseBean var9 = null;

         try {
            EnterpriseBean var10 = this.cache.get(var7);
            var9 = (WLEnterpriseBean)var10;
            if (var9 != null) {
               synchronized(var9) {
                  if (!var9.__WL_isBusy()) {
                     if (this.implementsSessionSynchronization) {
                        SessionSynchronization var12 = (SessionSynchronization)var10;

                        try {
                           this.ejbHome.pushEnvironment();
                           if (((WLSessionBean)var12).__WL_needsSessionSynchronization()) {
                              var12.afterCompletion(var3 == 3);
                           }
                        } catch (Throwable var27) {
                           EJBLogger.logExcepInAfterCompletion(StackTraceUtils.throwable2StackTrace(var27));
                           this.removeBean(var7, var12);
                        } finally {
                           this.ejbHome.popEnvironment();
                        }
                     }
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debug("******afterCompletion called before postInvoke ******");
                     }

                     var8 = false;
                     var9.__WL_setNeedsRemove(true);
                  }
               }
            }
         } finally {
            if (var8) {
               this.replicateAndRelease(var7, (EnterpriseBean)var9);
               this.lockManager.unlock(var6, this.getLockClient(var2));
            }

         }
      }

   }

   public EJBContext allocateContext(EnterpriseBean var1, EJBObject var2, EJBLocalObject var3) {
      return new SessionEJBContextImpl(var1, this, this.remoteHome, this.localHome, var2, var3);
   }

   public EJBContext allocateContext(EnterpriseBean var1, Object var2) {
      EJBObject var3 = null;
      BaseEJBLocalObjectIntf var4 = null;
      if (this.beanInfo.hasDeclaredRemoteHome()) {
         var3 = this.remoteHome.allocateEO(var2);
      }

      if (this.beanInfo.hasDeclaredLocalHome()) {
         var4 = this.localHome.allocateELO(var2);
      }

      return this.allocateContext(var1, var3, var4);
   }

   protected EnterpriseBean createNewBeanInstance() throws IllegalAccessException, InstantiationException {
      if (ENABLE_PROXY_POOL && this.beanInfo.isEJB30()) {
         EnterpriseBeanProxy var1 = (EnterpriseBeanProxy)this.proxyPool.remove();
         if (var1 != null) {
            ClassLoader var2 = this.beanInfo.getClassLoader();
            Thread var3 = Thread.currentThread();
            ClassLoader var4 = var3.getContextClassLoader();

            EnterpriseBean var6;
            try {
               var3.setContextClassLoader(var2);
               Object var5 = this.ejbComponentCreator.getBean(this.beanInfo.getEJBName(), this.beanClass, false);
               ((TargetWrapper)var1.getTarget()).resetTarget(var5);
               var6 = (EnterpriseBean)var1;
            } finally {
               var3.setContextClassLoader(var4);
            }

            return var6;
         }
      }

      return super.createNewBeanInstance();
   }

   protected void create(EJBObject var1, EJBLocalObject var2, Object var3, InvocationWrapper var4, Method var5, Method var6, Object[] var7) throws InternalException {
      SessionBean var8 = null;
      HashSet var9 = null;
      HashSet var10 = null;
      if (this.extendedPersistenceContextMap != null) {
         var9 = new HashSet();
         var10 = new HashSet();
         Set var11 = this.extendedPersistenceContextMap.keySet();

         ExtendedPersistenceContextWrapper var16;
         for(Iterator var12 = var11.iterator(); var12.hasNext(); var10.add(var16)) {
            String var13 = (String)var12.next();
            PersistenceUnitInfoImpl var14 = (PersistenceUnitInfoImpl)this.extendedPersistenceContextMap.get(var13);
            String var15 = var14.getPersistenceUnitId();
            var16 = ExtendedPersistenceContextManager.getExtendedPersistenceContext(var15);
            if (var16 != null) {
               var16.incrementReferenceCount();
            } else {
               var16 = new ExtendedPersistenceContextWrapper(var14);
               ExtendedPersistenceContextManager.setExtendedPersistenceContext(var15, var16);
               var9.add(var15);
            }
         }
      }

      try {
         var8 = (SessionBean)this.createBean(var3, var1, var2, (EnterpriseBean)null);
      } finally {
         if (this.extendedPersistenceContextMap != null) {
            Set var19 = ((WLSessionBean)var8).getExtendedPersistenceContexts();
            var19.addAll(var10);
            Iterator var20 = var9.iterator();

            while(var20.hasNext()) {
               String var21 = (String)var20.next();
               ExtendedPersistenceContextManager.removeExtendedPersistenceContext(var21);
            }
         }

      }

      if (!$assertionsDisabled && var4.getInvokeTx() != null) {
         throw new java.lang.AssertionError();
      } else {
         this.acquireLock(var4, var3);
         CacheKey var50 = new CacheKey(var3, this);

         try {
            try {
               Debug.assertion(var8 != null);
               if (this.beanInfo.isEJB30() && var8 instanceof __ProxyControl) {
                  ClassLoader var51 = this.beanInfo.getClassLoader();
                  Thread var53 = Thread.currentThread();
                  ClassLoader var54 = var53.getContextClassLoader();

                  try {
                     this.ejbHome.pushEnvironment();
                     var53.setContextClassLoader(var51);
                     EJBContextManager.pushEjbContext(((WLSessionBean)var8).__WL_getEJBContext());
                     AllowedMethodsHelper.pushMethodInvocationState(new Integer(4));
                     ((__ProxyControl)var8).invokeLifecycleMethod(LifecycleEvent.POST_CONSTRUCT);
                  } finally {
                     AllowedMethodsHelper.popMethodInvocationState();
                     var53.setContextClassLoader(var54);
                     EJBContextManager.popEjbContext();
                     this.ejbHome.popEnvironment();
                  }
               }

               if (var5 != null) {
                  Debug.assertion(var7 != null);
                  var5.invoke(var8, var7);
               }
            } catch (IllegalAccessException var47) {
               throw new AssertionError(var47);
            } catch (InvocationTargetException var48) {
               Throwable var52 = var48.getTargetException();
               if (debugLogger.isDebugEnabled()) {
                  debug("Error during create: ", var52);
               }

               this.handleMethodException(var5, (Class[])null, var52);
            }

            try {
               this.cache.put(var50, var8);
               this.getCacheRuntime().incrementCachedBeansCurrentCount();
            } catch (CacheFullException var44) {
               EJBRuntimeUtils.throwInternalException("Exception in remote create", var44);
            }

            this.replicateAndRelease(var50, var8);
         } finally {
            this.lockManager.unlock(var3, this.getLockClient((Object)null));
         }

      }
   }

   private EnterpriseBean createBean(Object var1, EJBObject var2, EJBLocalObject var3, EnterpriseBean var4) throws InternalException {
      EnterpriseBean var30;
      try {
         this.ejbHome.pushEnvironment();
         EJBContext var5 = this.allocateContext(var4, var2, var3);
         if (this.beanInfo instanceof Ejb3SessionBeanInfo) {
            WLSessionEJBContext var6 = (WLSessionEJBContext)var5;
            var6.setPrimaryKey(var1);
         }

         try {
            EJBContextManager.pushEjbContext(var5);
            AllowedMethodsHelper.pushMethodInvocationState(new Integer(1));
            if (var4 == null) {
               var4 = this.allocateBean();
            }
         } finally {
            EJBContextManager.popEjbContext();
            AllowedMethodsHelper.popMethodInvocationState();
         }

         ((BaseEJBContext)var5).setBean(var4);

         try {
            AllowedMethodsHelper.pushMethodInvocationState(new Integer(1));
            ((SessionBean)var4).setSessionContext((SessionContext)var5);
         } catch (Exception var25) {
            Exception var29 = var25;
            throw new InternalException("Error during setXXXContext: ", var25);
         } finally {
            AllowedMethodsHelper.popMethodInvocationState();
         }

         ((WLEnterpriseBean)var4).__WL_setEJBContext(var5);
         var30 = var4;
      } finally {
         this.ejbHome.popEnvironment();
      }

      return var30;
   }

   public Object createBean() throws InternalException {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var6 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium.isArgumentsCaptureNeeded()) {
            var6 = InstrumentationSupport.toSensitive(1);
         }

         DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var6, (Object)null);
         var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var17, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var15 = false;

      Object var18;
      try {
         var15 = true;
         if (debugLogger.isDebugEnabled()) {
            debug("calling getReferent(...) in StatefulSessionManager");
         }

         Object var1 = this.keyGenerator.nextKey();
         InvocationWrapper var2 = EJBRuntimeUtils.createWrap();
         EJBObject var3 = null;
         BaseEJBLocalObjectIntf var4 = null;
         if (this.beanInfo.hasDeclaredRemoteHome()) {
            var3 = this.remoteHome.allocateEO(var1);
         }

         if (this.beanInfo.hasDeclaredLocalHome()) {
            var4 = this.localHome.allocateELO(var1);
         }

         this.create(var3, var4, var1, var2, (Method)null, (Method)null, (Object[])null);
         var18 = var1;
         var15 = false;
      } finally {
         if (var15) {
            var10001 = null;
            if (var11) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var12, var13);
            }

         }
      }

      if (var11) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium, var12, var13);
      }

      return var18;
   }

   public StatefulRemoteObject remoteCreateForBI(Object var1, Class var2, Activator var3, Class var4) throws InternalException {
      if (var3 == null) {
         throw new AssertionError();
      } else {
         if (var1 == null) {
            var1 = this.createBean();
         }

         return (StatefulRemoteObject)((StatefulEJBHomeImpl)this.remoteHome).allocateBI(var1, var2, var4, var3);
      }
   }

   public EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      Object var5 = this.keyGenerator.nextKey();
      EJBObject var6 = null;
      BaseEJBLocalObjectIntf var7 = null;
      if (this.remoteHome != null) {
         var6 = this.remoteHome.allocateEO(var5);
      }

      if (this.localHome != null && this.beanInfo.hasDeclaredLocalHome()) {
         var7 = this.localHome.allocateELO(var5);
      }

      this.create(var6, var7, var5, var1, var2, var3, var4);
      return var6;
   }

   public EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException {
      Object var5 = this.keyGenerator.nextKey();
      StatefulEJBObject var6 = null;
      StatefulEJBLocalObject var7 = null;
      if (this.remoteHome != null && this.beanInfo.hasDeclaredRemoteHome()) {
         var6 = (StatefulEJBObject)this.remoteHome.allocateEO(var5);
      }

      if (this.localHome != null) {
         var7 = (StatefulEJBLocalObject)this.localHome.allocateELO(var5);
      }

      this.create(var6, var7, var5, var1, var2, var3, var4);
      return var7;
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      boolean var18;
      boolean var10000 = var18 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var19 = null;
      DiagnosticActionState[] var20 = null;
      Object var17 = null;
      if (var10000) {
         Object[] var13 = null;
         if (_WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High.isArgumentsCaptureNeeded()) {
            var13 = new Object[]{this, var1};
         }

         DynamicJoinPoint var31 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var13, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High;
         DiagnosticAction[] var10002 = var19 = var10001.getActions();
         InstrumentationSupport.preProcess(var31, var10001, var10002, var20 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         if (!$assertionsDisabled && var1.getMethodDescriptor().getTXAttribute() != 2 && var1.getInvokeTx() != null) {
            throw new java.lang.AssertionError();
         }

         Object var2 = var1.getPrimaryKey();
         CacheKey var3 = new CacheKey(var2, this);
         if (!$assertionsDisabled && var2 == null) {
            throw new java.lang.AssertionError();
         }

         if (!this.beanInfo.isAllowRemoveDuringTx()) {
            Object var4 = this.lockManager.getOwner(var2);
            if (var4 != null && var4 instanceof Transaction) {
               throw new InternalException("", new RemoveException("Illegal attempt to remove a stateful session bean while it is participating in a transaction"));
            }
         }

         boolean var30 = false;
         SessionBean var5 = null;
         boolean var6 = false;
         Transaction var7 = var1.getInvokeTx();
         if (var7 != null && !this.ejbHome.usesBeanManagedTx()) {
            var6 = true;
         }

         boolean var8 = false;

         try {
            var5 = (SessionBean)this.preInvoke(var1);
            var30 = true;
            if (!$assertionsDisabled && var5 == null) {
               throw new java.lang.AssertionError();
            }

            this.removeBean(var3, var5);
            var5.ejbRemove();
         } catch (Throwable var27) {
            var8 = true;
            this.handleMethodException(var1.getMethodDescriptor().getMethod(), (Class[])null, var27);
            throw new AssertionError("Should not reach");
         } finally {
            if (var5 != null) {
               ((WLEnterpriseBean)var5).__WL_setBusy(false);
               if (!var8) {
                  this.releaseProxy(var5);
               }
            }

            if (var30 && !var6) {
               this.lockManager.unlock(var2, this.getLockClient((Object)null));
            }

         }
      } finally {
         if (var18) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High, var19, var20);
         }

      }

   }

   public void removeForRemoveAnnotation(InvocationWrapper var1) throws InternalException {
      Object var2 = var1.getPrimaryKey();
      CacheKey var3 = new CacheKey(var2, this);
      if (!$assertionsDisabled && var2 == null) {
         throw new java.lang.AssertionError();
      } else {
         SessionBean var4 = null;

         try {
            if (var1.getInvokeTx() == null || var1.getInvokeTx().getStatus() != 0 && var1.getInvokeTx().getStatus() != 1) {
               this.lockManager.lock(var2, this.getLockClient((Object)null), this.getLockTimeout(var1));
            }

            var4 = (SessionBean)this.getBean(var2);
            if (!$assertionsDisabled && var4 == null) {
               throw new java.lang.AssertionError();
            }

            this.removeBean(var3, var4);
            var4.ejbRemove();
            this.releaseProxy(var4);
         } catch (Throwable var15) {
            EJBRuntimeUtils.throwInternalException("EJB Exception:", var15);
         } finally {
            try {
               if (var1.getInvokeTx() == null || var1.getInvokeTx().getStatus() != 0 && var1.getInvokeTx().getStatus() != 1) {
                  this.lockManager.unlock(var2, this.getLockClient((Object)null));
               }

               if (this.remoteHome != null && (this.remoteHome.getIsNoObjectActivation() || this.remoteHome.getIsInMemoryReplication())) {
                  EJBObject var8 = this.remoteHome.getEJBObject(var2);
                  if (var8 != null) {
                     this.remoteHome.releaseEO(var2);
                  }

                  if (this.remoteHome instanceof StatefulEJBHomeImpl) {
                     ((StatefulEJBHomeImpl)this.remoteHome).releaseBOs(var2);
                  }
               }
            } catch (Throwable var14) {
               EJBRuntimeUtils.throwInternalException("EJB Exception:", var14);
            }

         }

      }
   }

   public int getBeanSize() {
      return 1;
   }

   public boolean isEntityManager() {
      return false;
   }

   public void enrollInTransaction(Transaction var1, CacheKey var2, EntityBean var3, RSInfo var4) throws InternalException {
      throw new AssertionError("method 'enrollInTransaction' not valid for StatefulSessionManager");
   }

   public void selectedForReplacement(CacheKey var1, EntityBean var2) {
      throw new AssertionError("method 'selectedForReplacement' not valid for StatefulSessionManager");
   }

   public void loadBeanFromRS(CacheKey var1, EntityBean var2, RSInfo var3) {
      throw new AssertionError("method 'copy' not valid for StatefulSessionManager");
   }

   public void removedFromCache(CacheKey var1, EnterpriseBean var2) {
      this.getCacheRuntime().decrementCachedBeansCurrentCount();
   }

   public void removedOnError(CacheKey var1, EnterpriseBean var2) {
      throw new AssertionError("removedOnError in StatefulSessionManager");
   }

   public void swapIn(CacheKey var1, EnterpriseBean var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("Activating key: " + var1);
      }

      if (!$assertionsDisabled && !(var2 instanceof SessionBean)) {
         throw new java.lang.AssertionError();
      } else {
         Object var3 = var1.getPrimaryKey();
         if (!(this.swapper instanceof ReplicatedMemorySwap)) {
            this.swapper.remove(var3);
         }

         try {
            ((SessionBean)var2).ejbActivate();
            this.getCacheRuntime().incrementActivationCount();
         } catch (Exception var5) {
            this.removeBean(var1, var2);
            this.getCacheRuntime().decrementCachedBeansCurrentCount();
            EJBLogger.logExceptionDuringEJBActivate(var5);
         }

      }
   }

   public void swapOut(CacheKey var1, EnterpriseBean var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("swapOut key: " + var1);
      }

      if (!$assertionsDisabled && !(var2 instanceof SessionBean)) {
         throw new java.lang.AssertionError();
      } else {
         Object var3 = var1.getPrimaryKey();
         boolean var4 = PoolHelper.setFile(this.fileSector, this.fileDesc);

         try {
            this.cacheRTMBean.incrementPassivationCount();
            ((SessionBean)var2).ejbPassivate();
            this.swapper.write(var3, var2);
         } catch (Exception var10) {
            EJBLogger.logErrorDuringPassivation(StackTraceUtils.throwable2StackTrace(var10));
         } finally {
            if (var4) {
               PoolHelper.resetFile();
            }

         }

      }
   }

   public void replicate(CacheKey var1, EnterpriseBean var2) {
      if (this.isInMemoryReplication) {
         try {
            if (this.beanInfo.getPassivateDuringReplication()) {
               ((SessionBean)var2).ejbPassivate();
            }

            ((ReplicatedMemorySwap)this.swapper).sendUpdate(var1.getPrimaryKey(), var2);
         } catch (Exception var5) {
            EJBLogger.logErrorDuringPassivation(StackTraceUtils.throwable2StackTrace(var5));
         }

         try {
            if (this.beanInfo.getPassivateDuringReplication()) {
               ((SessionBean)var2).ejbActivate();
            }
         } catch (RemoteException var4) {
            this.removeBean(var1, var2);
            EJBLogger.logExceptionDuringEJBActivate(var4);
         }
      }

   }

   public boolean needsRemoval(EnterpriseBean var1) {
      return false;
   }

   public EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   public Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   public EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   public EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   public Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   public Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException {
      throw new AssertionError("No finders for stateful session beans");
   }

   private String getServerRelativePath() {
      return DomainDir.getTempDirForServer(ManagementService.getRuntimeAccess(kernelId).getServerName());
   }

   public String getSwapDirectoryName() {
      return this.getServerRelativePath() + File.separatorChar + this.beanInfo.getSwapDirectoryName() + File.separatorChar + StringUtils.mangle(this.ejbHome.getIsIdenticalKey());
   }

   public void undeploy() {
      super.undeploy();
      this.cache.clear();
      this.cache.stopScrubber();
      this.swapper.cancelTrigger();
   }

   private boolean inCluster() {
      return ((ServerMBean)Kernel.getConfig()).getCluster() != null;
   }

   public synchronized void beanImplClassChangeNotification() {
      this.beanClass = this.beanInfo.getGeneratedBeanClass();
      this.swapper.updateClassLoader(this.beanInfo.getClassLoader());
   }

   public void updateMaxBeansInCache(int var1) {
      this.cache.updateMaxBeansInCache(var1);
   }

   public void updateIdleTimeoutSecondsCache(int var1) {
      this.swapper.updateIdleTimeoutMS((long)var1 * 1000L);
      ((BaseCache)this.cache).updateIdleTimeoutSeconds(var1);
   }

   public void releaseBean(InvocationWrapper var1) {
   }

   public boolean isInMemoryReplication() {
      return this.beanInfo.getReplicationType() == 2;
   }

   private void replicateAndRelease(CacheKey var1, EnterpriseBean var2) {
      if (var2 != null) {
         this.replicate(var1, var2);
      }

      this.cache.release(var1);
   }

   public int getIdleTimeoutSeconds() {
      return this.idleTimeoutSeconds;
   }

   public void reInitializeCacheAndPool() {
      this.cache.reInitializeCacheAndPools();
   }

   public void reInitializePool() {
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

   public boolean beanIsOpsComplete(Transaction var1, Object var2) {
      return false;
   }

   public int passivateModifiedBean(Transaction var1, Object var2, boolean var3) {
      return 0;
   }

   public int passivateUnModifiedBean(Transaction var1, Object var2) {
      return 0;
   }

   public int cachePassivateModifiedBean(Transaction var1, Object var2, boolean var3) {
      return 0;
   }

   public int cachePassivateUnModifiedBean(Transaction var1, Object var2) {
      return 0;
   }

   public boolean passivateLockedModifiedBean(Transaction var1, Object var2, boolean var3, EntityBean var4) {
      return false;
   }

   public boolean passivateLockedUnModifiedBean(Transaction var1, Object var2, EntityBean var3) {
      return false;
   }

   public void operationsComplete(Transaction var1, Object var2) {
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatefulSessionManager] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[StatefulSessionManager] " + var0, var1);
   }

   public Object assembleEJB3Proxy(Object var1, BeanInfo var2) {
      return var2 instanceof Ejb3SessionBeanInfo ? this.ejbComponentCreator.assembleEJB3Proxy(var1, var2.getEJBName()) : var1;
   }

   public void passivateAndBacktoPool(CacheKey var1, EntityBean var2) {
   }

   static {
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Remove_Around_High = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Remove_Around_High");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium");
      _WLDF$INST_FLD_EJB_Diagnostic_Pool_Manager_Create_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "EJB_Diagnostic_Pool_Manager_Create_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulSessionManager.java", "weblogic.ejb.container.manager.StatefulSessionManager", "preInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)Ljavax/ejb/EnterpriseBean;", 477, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Preinvoke_After_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulSessionManager.java", "weblogic.ejb.container.manager.StatefulSessionManager", "postInvoke", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 634, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Postinvoke_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulSessionManager.java", "weblogic.ejb.container.manager.StatefulSessionManager", "destroyInstance", "(Lweblogic/ejb/container/internal/InvocationWrapper;Ljava/lang/Throwable;)V", 783, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Remove_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulSessionManager.java", "weblogic.ejb.container.manager.StatefulSessionManager", "createBean", "()Ljava/lang/Object;", 1178, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "StatefulSessionManager.java", "weblogic.ejb.container.manager.StatefulSessionManager", "remove", "(Lweblogic/ejb/container/internal/InvocationWrapper;)V", 1269, InstrumentationSupport.makeMap(new String[]{"EJB_Diagnostic_Pool_Manager_Remove_Around_High"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("wrap", "weblogic.diagnostics.instrumentation.gathering.EJBInvocationWrapperRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !StatefulSessionManager.class.desiredAssertionStatus();
      ENABLE_PROXY_POOL = Boolean.getBoolean("weblogic.ejb30.enableproxypool");
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
