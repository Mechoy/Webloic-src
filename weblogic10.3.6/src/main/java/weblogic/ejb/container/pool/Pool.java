package weblogic.ejb.container.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.EnterpriseBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.PoolHelper;
import weblogic.ejb.container.monitoring.EJBPoolRuntimeMBeanImpl;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public abstract class Pool implements PoolIntf, TimerListener {
   protected static final DebugLogger debugLogger;
   public static final String USE_GENERIC_RESOURCE_POOL_PROP = "weblogic.ejb.pool.InstancePool.useGenericResourcePool";
   private static final boolean useGenericResourcePool;
   private BeanManager beanManager;
   protected final BeanInfo beanInfo;
   protected int initialSize;
   protected volatile int maximumSize;
   protected int idleTimeoutSeconds;
   protected Class beanClass;
   protected String ejbName;
   private NewMonitoredPool pool;
   private Timer timer;
   private Lock poolLock;
   private final EJBPoolRuntimeMBeanImpl mbean;
   private PoolHelper helper;
   private String dirName;
   private String fileName;
   private String segmentName;
   private AuthenticatedSubject fileDesc;
   private AuthenticatedSubject filePtr;
   private AuthenticatedSubject fileSegment;
   private boolean doneInitialCreation = false;

   public Pool(BeanManager var1, BeanInfo var2, EJBPoolRuntimeMBean var3) throws WLDeploymentException {
      this.mbean = (EJBPoolRuntimeMBeanImpl)var3;
      this.initialSize = var2.getCachingDescriptor().getInitialBeansInFreePool();
      this.maximumSize = var2.getCachingDescriptor().getMaxBeansInFreePool();
      if (this.initialSize > this.maximumSize) {
         this.maximumSize = this.initialSize;
      }

      this.beanInfo = var2;
      this.beanManager = var1;
      this.ejbName = var2.getEJBName();
      this.idleTimeoutSeconds = var2.getCachingDescriptor().getIdleTimeoutSecondsPool();
      this.createInitialPool();
      this.mbean.setPool(this);
      this.dirName = var2.getRunAsPrincipalName();
      this.fileName = var2.getCreateAsPrincipalName();
      this.segmentName = var2.getRemoveAsPrincipalName();
      if (this.dirName != null || this.fileName != null || this.segmentName != null) {
         this.helper = new PoolHelper(var2.getDeploymentInfo().getSecurityRealmName(), var2.getJACCPolicyConfig(), var2.getJACCPolicyContextId(), var2.getJACCCodeSource(), var2.getJACCRoleMapper());

         try {
            if (this.dirName != null) {
               this.fileDesc = this.helper.getFileDesc(this.dirName);
            }

            if (this.fileName != null) {
               this.filePtr = this.helper.getFileDesc(this.fileName);
            }

            if (this.segmentName != null) {
               this.fileSegment = this.helper.getFileDesc(this.segmentName);
            }
         } catch (Exception var5) {
            throw new WLDeploymentException(var5.toString());
         }

         this.helper = null;
      }

   }

   public String toString() {
      return super.toString() + " - ejb name: '" + this.beanInfo.getEJBName() + "'";
   }

   protected boolean setFile() {
      return PoolHelper.setFile(this.filePtr, this.fileDesc);
   }

   protected boolean setSegment() {
      return PoolHelper.setSegment(this.fileSegment, this.fileDesc);
   }

   protected void resetFile() {
      PoolHelper.resetFile();
   }

   protected void setDir() {
      PoolHelper.setDir();
   }

   protected void resetDir() {
      PoolHelper.resetDir();
   }

   protected void setFile2() {
      if (this.fileDesc != null) {
         PoolHelper.setFile2(this.fileDesc);
      }

   }

   protected void resetFile2() {
      if (this.fileDesc != null) {
         PoolHelper.resetFile2();
      }

   }

   protected EJBPoolRuntimeMBeanImpl getPoolRuntime() {
      return this.mbean;
   }

   private void createInitialPool() {
      this.poolLock = new ReentrantLock();
      if (useGenericResourcePool) {
         Debug.assertion(false, "GenericResourcePool not hooked up to EJB pools yet");
      } else {
         this.pool = new NewMonitoredPool(new NewShrinkablePool(this.maximumSize, this.initialSize));
      }

   }

   public int getInitialBeansInFreePool() {
      return this.initialSize;
   }

   public void setInitialBeansInFreePool(int var1) {
      this.initialSize = var1;
      if (debugLogger.isDebugEnabled()) {
         this.debug("setInitialBeansInFreePool(" + var1 + ")");
      }

      this.createInitialPool();
   }

   public int getMaxBeansInFreePool() {
      return this.maximumSize;
   }

   public void setMaxBeansInFreePool(int var1) {
      this.maximumSize = var1;
      if (debugLogger.isDebugEnabled()) {
         this.debug("setMaxBeansInFreePool(" + var1 + ")");
      }

   }

   public void createInitialBeans() throws WLDeploymentException {
      if (!this.doneInitialCreation) {
         if (debugLogger.isDebugEnabled()) {
            this.debug("Creating InitialBeans in pool: '" + this + "' initialSize: '" + this.initialSize + "', maximumSize: '" + this.maximumSize + "'");
         }

         try {
            for(int var1 = 0; var1 < this.initialSize; ++var1) {
               this.pool.add(this.createBean());
               this.incrementCurrentSize();
            }

            this.doneInitialCreation = true;
         } catch (InternalException var3) {
            if (var3.detail != null) {
               throw new WLDeploymentException(var3.detail.getMessage() + StackTraceUtils.throwable2StackTrace(var3.detail));
            }

            throw new WLDeploymentException(var3.getMessage() + StackTraceUtils.throwable2StackTrace(var3));
         }
      }

      try {
         this.startIdleTimeout(0L);
      } catch (Exception var2) {
         EJBLogger.logErrorStartingFreepoolTimer(this.ejbName, var2.getMessage());
      }

   }

   protected abstract EnterpriseBean createBean() throws InternalException;

   protected abstract void removeBean(EnterpriseBean var1);

   protected void incrementCurrentSize() {
   }

   public void destroyBean(EnterpriseBean var1) {
      this.mbean.decrementBeansInUseCount();
      this.mbean.incrementDestroyedTotalCount();
   }

   public EnterpriseBean getBean() throws InternalException {
      EnterpriseBean var1 = (EnterpriseBean)this.pool.remove();
      if (debugLogger.isDebugEnabled()) {
         this.debug("Returning bean from the pool: '" + var1 + "'");
      }

      return var1;
   }

   public void releaseBean(EnterpriseBean var1) {
      this.mbean.decrementBeansInUseCount();
      boolean var2 = false;
      boolean var3;
      if (this.beanInfo.isEJB30()) {
         var3 = EJBRuntimeUtils.beanEq(this.beanClass, var1, this.beanInfo.getDeploymentInfo().getPitchforkContext());
      } else {
         var3 = var1 != null && var1.getClass() == this.beanClass;
      }

      if (var3) {
         var2 = this.pool.add(var1);
      }

      if (!var2) {
         this.removeBean(var1);
      }

   }

   public void updateMaxBeansInFreePool(int var1) {
      Iterator var2 = this.resizePool(var1).iterator();

      while(var2.hasNext()) {
         EnterpriseBean var3 = (EnterpriseBean)var2.next();
         this.removeBean(var3);
      }

   }

   protected List resizePool(int var1) {
      try {
         this.stopIdleTimeout();
      } catch (Exception var11) {
         EJBLogger.logErrorStoppingFreepoolTimer(this.ejbName, var11.getMessage());
      }

      new ArrayList();
      this.maximumSize = var1;

      List var2;
      try {
         this.poolLock.lock();
         int var3 = this.pool.getCapacity();
         this.pool.setCapacity(this.maximumSize);
         var2 = (List)this.pool.trim(var3 - this.maximumSize);
      } finally {
         this.poolLock.unlock();
      }

      try {
         this.startIdleTimeout(0L);
      } catch (Exception var9) {
         EJBLogger.logErrorStartingFreepoolTimer(this.ejbName, var9.getMessage());
      }

      return var2;
   }

   public void cleanup() {
      try {
         this.stopIdleTimeout();
      } catch (Exception var9) {
         EJBLogger.logErrorStoppingFreepoolTimer(this.ejbName, var9.getMessage());
      }

      ArrayList var1 = new ArrayList(this.maximumSize);

      while(!this.pool.isEmpty()) {
         var1.add(this.pool.remove());
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         EnterpriseBean var3 = (EnterpriseBean)var2.next();

         try {
            this.pushEnvironment();
            this.removeBean(var3);
         } finally {
            this.popEnvironment();
         }
      }

      this.doneInitialCreation = false;
   }

   protected void pushEnvironment() {
      EJBRuntimeUtils.pushEnvironment(this.beanManager.getEnvironmentContext());
   }

   protected void popEnvironment() {
      EJBRuntimeUtils.popEnvironment();
   }

   private void startIdleTimeout(long var1) {
      if (debugLogger.isDebugEnabled()) {
         this.debug(this.ejbName + ", entered startIdleTimeout timer with idleTimeoutSeconds = " + this.idleTimeoutSeconds);
      }

      if (this.idleTimeoutSeconds > 0) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.ejbName + ", startIdleTimeout timer");
         }

         long var3 = (long)(this.idleTimeoutSeconds * 1000);
         TimerManagerFactory var5 = TimerManagerFactory.getTimerManagerFactory();
         TimerManager var6 = var5.getDefaultTimerManager();
         this.timer = var6.scheduleAtFixedRate(this, var1, var3);
      }
   }

   public void stopIdleTimeout() {
      if (this.idleTimeoutSeconds > 0) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.ejbName + " stopping IdleTimeout timer ");
         }

         if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
         }

      }
   }

   public void updateIdleTimeoutSeconds(int var1) {
      long var2 = 0L;
      if (this.timer != null) {
         long var4 = this.timer.getTimeout();
         this.stopIdleTimeout();
         var2 = var4 - System.currentTimeMillis();
         if (var2 < 0L) {
            var2 = 0L;
         }

         if ((long)(var1 * 1000) < var2) {
            var2 = (long)(var1 * 1000);
         }
      }

      this.idleTimeoutSeconds = var1;
      this.startIdleTimeout(var2);
   }

   public void timerExpired(Timer var1) {
      Collection var2 = null;

      try {
         this.poolLock.lock();
         var2 = this.pool.trim(true);
      } finally {
         this.poolLock.unlock();
      }

      if (var2 != null) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.ejbName + " timerExpired: about to call remove on " + var2.size() + " beans.");
         }

         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            EnterpriseBean var4 = (EnterpriseBean)var3.next();
            this.removeBean(var4);
         }
      } else if (debugLogger.isDebugEnabled()) {
         this.debug(this.ejbName + " timerExpired: no beans to remove ");
      }

   }

   public void reInitializePool() {
      Collection var1;
      try {
         this.poolLock.lock();
         var1 = this.pool.trim(false);
      } finally {
         this.poolLock.unlock();
      }

      if (var1 != null) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.ejbName + " reInitializePool: about to call remove on " + var1.size() + " beans.");
         }

         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            EnterpriseBean var3 = (EnterpriseBean)var2.next();
            this.removeBean(var3);
         }
      } else if (debugLogger.isDebugEnabled()) {
         this.debug(this.ejbName + " reInitializePool: no beans to remove ");
      }

   }

   public int getFreeCount() {
      return this.pool.getFreeCount();
   }

   public long getAccessCount() {
      return this.pool.getAccessCount();
   }

   public long getMissCount() {
      return this.pool.getMissCount();
   }

   private void debug(String var1) {
      debugLogger.debug("[Pool] " + this.ejbName + " - " + var1);
   }

   static {
      debugLogger = EJBDebugService.poolingLogger;
      useGenericResourcePool = System.getProperty("weblogic.ejb.pool.InstancePool.useGenericResourcePool") != null;
   }
}
