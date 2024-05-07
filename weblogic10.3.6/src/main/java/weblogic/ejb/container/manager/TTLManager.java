package weblogic.ejb.container.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.ejb.EntityBean;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.transaction.Transaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.cache.QueryCacheElement;
import weblogic.ejb.container.cache.QueryCacheKey;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.QueryCache;
import weblogic.ejb.container.interfaces.ReadOnlyManager;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLEntityBean;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.SecurityHelper;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.EJBCacheFactory;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.logging.Loggable;
import weblogic.management.runtime.EntityEJBRuntimeMBean;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class TTLManager extends DBManager implements ReadOnlyManager {
   private EntityBeanInfo info;
   private QueryCache queryCache;
   private int readTimeoutMillis;
   private Method findByCategoryMethod;
   private Random readRand = new Random();
   private WorkManager workManager;
   private Map<String, Method> eagerRefreshMethodMap;
   private Map<EagerRefreshInfo, Long> eagerRefreshJobs;
   private EagerRefreshListener eagerRefreshListener;
   private TimerManager timerManager;
   private final Map categories = Collections.synchronizedMap(new HashMap());

   public TTLManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5, QueryCache var6) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4, var5);
      this.info = (EntityBeanInfo)var3;
      int var7 = this.info.getCachingDescriptor().getReadTimeoutSeconds();
      if (this.isReadOnly()) {
         if (var6 == null) {
            int var8 = this.info.getMaxQueriesInCache();
            this.queryCache = (QueryCache)EJBCacheFactory.createQueryCache(this.info.getEJBName(), var8);
            this.queryCache.setRuntimeMBean(((EntityEJBRuntimeMBean)this.getEJBRuntimeMBean()).getQueryCacheRuntime());
         } else {
            this.queryCache = var6;
         }

         if (!this.isBeanManagedPersistence && this.info.getCategoryCmpField() != null) {
            Method[] var12 = this.info.getGeneratedBeanClass().getMethods();
            int var9 = var12.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Method var11 = var12[var10];
               if (var11.getName().equals("ejbFindByCategory__WL_")) {
                  this.findByCategoryMethod = var11;
                  break;
               }
            }

            if (this.findByCategoryMethod == null) {
               throw new AssertionError("null findByCategoryMethod");
            }

            TimerManagerFactory var13 = TimerManagerFactory.getTimerManagerFactory();
            this.timerManager = var13.getDefaultTimerManager();
         }
      }

      this.updateReadTimeoutSeconds(var7);
   }

   public boolean isCategoryEnabled() {
      return this.findByCategoryMethod != null;
   }

   public Object getCategoryValue(CMPBean var1) {
      return var1._WL__getCategoryValue();
   }

   public void registerCategoryTimer(Object var1, long var2) {
      if (!this.categories.containsKey(var1)) {
         this.categories.put(var1, var2);
         if (debugLogger.isDebugEnabled()) {
            debug("adding " + var1 + " to categories with lastLoadTime: " + var2);
         }

         CategoryTimerListener var4 = new CategoryTimerListener(this, var1);
         long var5 = var2 + (long)(0.9F * (float)this.readTimeoutMillis) - System.currentTimeMillis();
         if (debugLogger.isDebugEnabled()) {
            debug("scheduling timer: " + var1 + " delay: " + var5 + " lastLoadTime: " + var2);
         }

         if (var5 < 0L) {
            var5 = 0L;
         }

         this.timerManager.schedule(var4, var5);
      }

   }

   public void invokeFindByCategory(Object var1) throws InternalException {
      InvocationWrapper var2 = EJBRuntimeUtils.createWrap();
      var2.setIsLocal(this.info.hasLocalClientView());
      Object var3 = null;

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("Invoking findByCategory with categoryValue: " + var1);
         }

         EJBRuntimeUtils.pushEnvironment(this.getEnvironmentContext());
         SecurityHelper.pushCallerPrincipal();
         SecurityHelper.pushRunAsSubject(SecurityHelper.getAnonymousUser());
         this.collectionFinder(var2, this.findByCategoryMethod, new Object[]{var1});
      } finally {
         SecurityHelper.popRunAsSubject();

         try {
            SecurityHelper.popCallerPrincipal();
         } catch (PrincipalNotFoundException var10) {
            debug("PrincipalNotFoundException is thrown when invoking findByCategory: " + var10);
         }

         EJBRuntimeUtils.popEnvironment();
      }

   }

   public QueryCache getQueryCache() {
      return this.queryCache;
   }

   protected void loadBean(Object var1, EntityBean var2, RSInfo var3, boolean var4) throws Throwable {
      if (debugLogger.isDebugEnabled()) {
         debug("loadBean called, EJB= " + this.info.getEJBName() + ", pk= " + var1);
      }

      boolean var5 = ((WLEntityBean)var2).__WL_isBeanStateValid();
      if (var3 == null) {
         if (var4 || !var5) {
            synchronized(var2) {
               var2.ejbLoad();
            }
         }
      } else if (!var5) {
         synchronized(var2) {
            CMPBean var7 = (CMPBean)var2;
            var7.__WL_initialize();
            this.persistence.loadBeanFromRS(var2, var3);
            var7.__WL_superEjbLoad();
            var7.__WL_setLastLoadTime(System.currentTimeMillis());
         }
      }

   }

   public boolean supportsCopy() {
      return this.isReadOnly() && !this.isBeanManagedPersistence;
   }

   public void doCopy(CMPBean var1, CMPBean var2) throws InternalException {
      synchronized(var1) {
         try {
            var2.__WL_initialize();
            var2.__WL_copyFrom(var1, true);
            var2.__WL_superEjbLoad();
            var2.__WL_setLastLoadTime(var1.__WL_getLastLoadTime());
         } catch (Throwable var6) {
            EJBLogger.logErrorFromLoad(var6);
            EJBRuntimeUtils.throwInternalException("Exception in ejbLoad:", var6);
         }

      }
   }

   public void perhapsCopy(Object var1, EntityBean var2) throws InternalException {
      assert this.supportsCopy();

      CMPBean var3 = this.getCache().getLastLoadedValidInstance(new CacheKey(var1, this));
      if (var3 != null) {
         this.doCopy(var3, (CMPBean)var2);
      }

   }

   public void loadBeanFromRS(CacheKey var1, EntityBean var2, RSInfo var3) throws InternalException {
      if (this.uses20CMP) {
         synchronized(var2) {
            this.persistence.loadBeanFromRS(var2, var3);
         }
      }

   }

   protected boolean shouldStore(EntityBean var1) throws Throwable {
      return !this.isReadOnly();
   }

   protected void initLastLoad(Object var1, EntityBean var2) {
      long var3 = System.currentTimeMillis();
      ((WLEntityBean)var2).__WL_setLastLoadTime(var3);
   }

   protected EntityBean alreadyCached(Object var1, Object var2) throws InternalException {
      EntityBean var3 = this.getCache().getIfNotTimedOut(var1, new CacheKey(var2, this), false);
      if (var3 != null && !this.isBeanManagedPersistence && ((CMPBean)var3).__WL_getIsRemoved()) {
         Loggable var4 = EJBLogger.lognoSuchEntityExceptionLoggable(var2.toString());
         EJBRuntimeUtils.throwInternalException("EJB Exception: ", new ObjectNotFoundException(var4.getMessage()));
         var3 = null;
      }

      return var3;
   }

   public void enrollNotTimedOutBean(Transaction var1, CacheKey var2, EntityBean var3) throws InternalException {
      Object var4 = var2.getPrimaryKey();
      if (debugLogger.isDebugEnabled()) {
         debug("enrollNotTimedOut called, EJB= " + this.info.getEJBName() + ", pk= " + var4);
      }

      if (this.isReadOnly()) {
         this.setupTxListener(var4, var1);
      } else {
         this.setupTxListenerAndTxUser(var4, var1, (WLEnterpriseBean)var3);
      }

   }

   public void updateReadTimeoutSeconds(int var1) {
      this.readTimeoutMillis = var1 * 1000;
      Class var2 = this.info.getGeneratedBeanClass();

      try {
         Field var3 = var2.getField("__WL_readTimeoutMS");
         var3.setInt(var2, this.readTimeoutMillis);
      } catch (NoSuchFieldException var4) {
         throw new AssertionError(var4);
      } catch (IllegalAccessException var5) {
         throw new AssertionError(var5);
      }
   }

   public void beforeCompletion(Collection var1, Object var2) throws InternalException {
      if (!this.isReadOnly()) {
         super.beforeCompletion(var1, var2);
      }

   }

   public Object getFromQueryCache(String var1, int var2, boolean var3) throws InternalException {
      Object var4 = EJBRuntimeUtils.getInvokeTxOrThread();
      QueryCacheKey var5 = new QueryCacheKey(var1, var2, this, 0);
      Object var6 = this.queryCache.get(var4, var5, var3, false);
      if (debugLogger.isDebugEnabled() && var6 == null) {
         debug("Cache miss: " + var5);
      }

      return var6;
   }

   public Object getFromQueryCache(String var1, Object[] var2, boolean var3) throws InternalException {
      Object var4 = EJBRuntimeUtils.getInvokeTxOrThread();
      QueryCacheKey var5 = new QueryCacheKey(var1, var2, this, 0);
      Object var6 = this.queryCache.get(var4, var5, var3, false);
      if (debugLogger.isDebugEnabled() && var6 == null) {
         debug("Cache miss: " + var5);
      }

      return var6;
   }

   public Object getFromQueryCache(String var1, Object[] var2, boolean var3, boolean var4) throws InternalException {
      Object var5 = EJBRuntimeUtils.getInvokeTxOrThread();
      return this.queryCache.get(var5, new QueryCacheKey(var1, var2, this, 1), var3, var4);
   }

   public void putInQueryCache(QueryCacheKey var1, Collection var2) {
      var1.setTimeoutMillis(this.readTimeoutMillis);
      if (this.eagerRefreshMethodMap != null) {
         Method var3 = (Method)this.eagerRefreshMethodMap.get(var1.getMethodId());
         if (var3 != null) {
            Object[] var4 = var1.getArguments();
            long var5 = (long)((int)((this.readRand.nextDouble() * 0.25 + 0.75) * (double)this.readTimeoutMillis));
            this.registerEagerRefreshJob(var3, var4, var5);
         }
      }

      boolean var7 = this.queryCache.put(var1, var2);
      if (debugLogger.isDebugEnabled()) {
         debug("Cache put: " + var1 + ": " + var7);
      }

   }

   public void putInQueryCache(QueryCacheKey var1, QueryCacheElement var2) {
      var1.setTimeoutMillis(this.readTimeoutMillis);
      boolean var3 = this.queryCache.put(var1, var2);
      if (debugLogger.isDebugEnabled()) {
         debug("Cache put: " + var1 + ": " + var3);
      }

   }

   public void invalidateLocalServer(Object var1, Object var2) {
      if (this.isReadOnly()) {
         this.invalidateQueryCache(new CacheKey(var2, this));
      }

      super.invalidateLocalServer(var1, var2);
   }

   public void invalidateLocalServer(Object var1, Collection var2) {
      ArrayList var3 = new ArrayList();

      CacheKey var5;
      for(Iterator var4 = var2.iterator(); var4.hasNext(); var3.add(var5)) {
         var5 = new CacheKey(var4.next(), this);
         if (this.isReadOnly()) {
            this.invalidateQueryCache(var5);
         }
      }

      this.getCache().invalidate(var1, (Collection)var3);
      if (this.invalidationTargetBM != null) {
         this.invalidationTargetBM.invalidateLocalServer(var1, var2);
      }

   }

   public void invalidateAllLocalServer(Object var1) {
      if (this.isReadOnly()) {
         this.queryCache.invalidateAll();
      }

      super.invalidateAllLocalServer(var1);
   }

   public EntityBean enrollIfNotTimedOut(Object var1, CacheKey var2) throws InternalException {
      assert var2.getCallback() == this;

      EntityBean var3 = this.getCache().getIfNotTimedOut(var1, var2, false);
      return var3 != null && ((CMPBean)var3).__WL_getIsRemoved() ? null : var3;
   }

   public void selectedForReplacement(CacheKey var1, EntityBean var2) {
      if (this.isReadOnly()) {
         this.invalidateQueryCache(var1);
      }

      super.selectedForReplacement(var1, var2);
   }

   private void invalidateQueryCache(CacheKey var1) {
      this.queryCache.invalidate(var1);
   }

   private static void debug(String var0) {
      debugLogger.debug("[TTLManager] " + var0);
   }

   private QueryCache createQueryCache(int var1) {
      try {
         Class var2 = Class.forName("weblogic.ejb.container.cache.QueryCache");
         Constructor var3 = var2.getConstructor(Integer.TYPE);
         return (QueryCache)var3.newInstance(new Integer(var1));
      } catch (Exception var4) {
         throw new weblogic.utils.AssertionError("Error creating query cache", var4);
      }
   }

   public Map getCategories() {
      return this.categories;
   }

   public EntityBean getBeanFromRS(Object var1, Object var2, RSInfo var3) throws InternalException {
      EntityBean var4 = null;

      assert var1 != null;

      CacheKey var5 = new CacheKey(var2, this);
      var4 = this.getCache().get(var1, var5, var3, true);
      this.cacheRTMBean.incrementCacheAccessCount();
      if (var4 != null) {
         this.cacheRTMBean.incrementCacheHitCount();
         if ((this.isCategoryEnabled() || this.isEagerRefreshEnabled()) && var3 != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("persistence.loadBeanFromRS() called for " + var4);
            }

            ((CMPBean)var4).__WL_initialize();
            this.persistence.loadBeanFromRS(var4, var3);
            ((CMPBean)var4).__WL_setLastLoadTime(System.currentTimeMillis());
            ((WLEntityBean)var4).__WL_setOperationsComplete(false);
            if (var4 instanceof CMPBean) {
               ((CMPBean)var4).__WL_setNonFKHolderRelationChange(false);
               ((CMPBean)var4).__WL_setM2NInsert(false);
            }

            this.getCache().release(var1, var5);
         }
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("Didn't find the bean in entity cache");
         }

         var4 = this.getBeanFromPool();
         ((CMPBean)var4).__WL_initialize();
         this.persistence.loadBeanFromRS(var4, var3);
         if (!this.finderCacheInsert(var4)) {
            return null;
         }
      }

      return var4;
   }

   public void addEagerRefreshMethod(Method var1, String var2) {
      if (this.eagerRefreshMethodMap == null) {
         this.eagerRefreshMethodMap = new HashMap();
         this.eagerRefreshJobs = Collections.synchronizedMap(new HashMap());
         this.eagerRefreshListener = new EagerRefreshListener(this, this.eagerRefreshJobs);
         this.workManager = WorkManagerFactory.getInstance().getDefault();
         this.workManager.schedule(this.eagerRefreshListener);
      }

      this.eagerRefreshMethodMap.put(var2, var1);
   }

   private boolean isEagerRefreshEnabled() {
      return this.eagerRefreshListener != null;
   }

   private void registerEagerRefreshJob(Method var1, Object[] var2, long var3) {
      EagerRefreshInfo var5 = new EagerRefreshInfo(var1, var2);
      if (!this.eagerRefreshJobs.containsKey(var5)) {
         this.eagerRefreshJobs.put(var5, System.currentTimeMillis() + var3);
         if (debugLogger.isDebugEnabled()) {
            debug("adding " + var5 + " eager refresh job with timeoutDelay: " + var3);
         }
      } else if (debugLogger.isDebugEnabled()) {
         debug("eager refresh job " + var5 + " already scheduled - skipping new job registration");
      }

   }

   public void invokeEagerRefreshQuery(EagerRefreshInfo var1) throws InternalException {
      InvocationWrapper var2 = EJBRuntimeUtils.createWrap();
      var2.setIsLocal(this.info.hasLocalClientView());
      Object var3 = null;

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("Invoking eagerRefreshQuery: " + var1);
         }

         EJBRuntimeUtils.pushEnvironment(this.getEnvironmentContext());
         SecurityHelper.pushCallerPrincipal();
         SecurityHelper.pushRunAsSubject(SecurityHelper.getAnonymousUser());
         this.collectionFinder(var2, var1.getMethod(), var1.getArgs());
      } finally {
         SecurityHelper.popRunAsSubject();

         try {
            SecurityHelper.popCallerPrincipal();
         } catch (PrincipalNotFoundException var10) {
            debug("PrincipalNotFoundException thrown when invoking eager refresh query: " + var10);
         }

         EJBRuntimeUtils.popEnvironment();
      }

   }

   private class CategoryTimerListener implements TimerListener {
      final TTLManager beanManager;
      final Object categoryValue;

      public CategoryTimerListener(TTLManager var2, Object var3) {
         this.beanManager = var2;
         this.categoryValue = var3;
      }

      public void timerExpired(Timer var1) {
         try {
            this.beanManager.invokeFindByCategory(this.categoryValue);
            this.beanManager.getCategories().remove(this.categoryValue);
         } catch (InternalException var3) {
            if (BaseEJBManager.debugLogger.isDebugEnabled()) {
               TTLManager.debug("category finder invocation failed. later the data will be loaded from database.");
            }

            this.beanManager.getCategories().remove(this.categoryValue);
         }
      }
   }

   private class EagerRefreshListener implements Runnable {
      private final TTLManager beanManager;
      private final Map<EagerRefreshInfo, Long> eagerRefreshJobs;

      private EagerRefreshListener(TTLManager var2, Map<EagerRefreshInfo, Long> var3) {
         this.beanManager = var2;
         this.eagerRefreshJobs = var3;
      }

      public void run() {
         ArrayList var1 = new ArrayList();

         while(true) {
            long var2 = System.currentTimeMillis();
            var1.clear();
            synchronized(this.eagerRefreshJobs) {
               Iterator var5 = this.eagerRefreshJobs.keySet().iterator();

               while(true) {
                  if (!var5.hasNext()) {
                     break;
                  }

                  EagerRefreshInfo var6 = (EagerRefreshInfo)var5.next();
                  if ((Long)this.eagerRefreshJobs.get(var6) <= var2 + 10000L) {
                     var1.add(var6);
                  }
               }
            }

            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               EagerRefreshInfo var11 = (EagerRefreshInfo)var4.next();

               try {
                  this.beanManager.invokeEagerRefreshQuery(var11);
                  this.eagerRefreshJobs.remove(var11);
               } catch (InternalException var9) {
                  if (BaseEJBManager.debugLogger.isDebugEnabled()) {
                     TTLManager.debug("eager refresh query invoke failed: " + var9);
                  }
               }
            }

            try {
               Thread.sleep(10000L);
            } catch (Exception var10) {
               if (BaseEJBManager.debugLogger.isDebugEnabled()) {
                  TTLManager.debug("Thread sleep - threw exception.");
               }
            }
         }
      }

      // $FF: synthetic method
      EagerRefreshListener(TTLManager var2, Map var3, Object var4) {
         this(var2, var3);
      }
   }

   private class EagerRefreshInfo {
      private final Method method;
      private final Object[] args;

      EagerRefreshInfo(Method var2, Object[] var3) {
         this.method = var2;
         this.args = var3;
      }

      Method getMethod() {
         return this.method;
      }

      Object[] getArgs() {
         return this.args;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.method.getName());
         var1.append('(');

         for(int var2 = 0; var2 < this.args.length; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            var1.append(this.args[var2]);
         }

         var1.append(')');
         return var1.toString();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            EagerRefreshInfo var2 = (EagerRefreshInfo)var1;
            if (!Arrays.equals(this.args, var2.args)) {
               return false;
            } else {
               if (this.method != null) {
                  if (!this.method.equals(var2.method)) {
                     return false;
                  }
               } else if (var2.method != null) {
                  return false;
               }

               return true;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.method != null ? this.method.hashCode() : 0;
         var1 = 31 * var1 + (this.args != null ? Arrays.hashCode(this.args) : 0);
         return var1;
      }
   }
}
