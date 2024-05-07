package weblogic.ejb.container.pool;

import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import com.oracle.pitchfork.interfaces.intercept.__ProxyControl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.SessionBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.StatelessEJBHome;
import weblogic.ejb.container.internal.StatelessEJBLocalHome;
import weblogic.ejb.container.manager.StatelessManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.security.subject.SubjectManager;

public final class StatelessSessionPool extends Pool {
   private final StatelessEJBHome remoteHome;
   private final StatelessEJBLocalHome localHome;
   private final StatelessManager beanManager;
   private AtomicInteger currentSize = new AtomicInteger(0);
   private long timeouts;
   private final Lock sizeLock = new ReentrantLock();
   private final Condition notEmpty;

   public StatelessSessionPool(StatelessEJBHome var1, StatelessEJBLocalHome var2, StatelessManager var3, BeanInfo var4, EJBPoolRuntimeMBean var5) throws WLDeploymentException {
      super(var3, var4, var5);
      this.notEmpty = this.sizeLock.newCondition();
      this.beanClass = ((SessionBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.remoteHome = var1;
      this.localHome = var2;
      this.beanManager = var3;
      if (debugLogger.isDebugEnabled()) {
         debug("created: '" + this + "'");
      }

   }

   public EnterpriseBean getBean() {
      throw new AssertionError("StatelessSessionPool.getBean() must be called with mustWait=true");
   }

   public EnterpriseBean getBean(long var1) throws InternalException {
      EnterpriseBean var3 = null;
      if (this.getMaxBeansInFreePool() > 0) {
         var3 = super.getBean();
         if (null == var3) {
            try {
               this.sizeLock.lock();
               if (this.getMaxBeansInFreePool() <= this.currentSize.get()) {
                  var3 = super.getBean();
                  if (null == var3) {
                     var3 = this.waitForBean(var1);
                  }
               } else {
                  this.incrementCurrentSize();
               }
            } finally {
               this.sizeLock.unlock();
            }
         }
      } else {
         this.incrementCurrentSize();
      }

      if (null == var3) {
         try {
            var3 = this.createBean();
            if (debugLogger.isDebugEnabled()) {
               debug("allocate new: '" + var3 + "'");
            }
         } finally {
            if (var3 == null) {
               this.decrementCurrentSize();
            }

         }
      }

      this.getPoolRuntime().incrementBeansInUseCount();
      return var3;
   }

   protected void removeBean(EnterpriseBean var1) {
      this.decrementCurrentSize();
      boolean var2 = this.setSegment();

      try {
         ((SessionBean)var1).ejbRemove();
      } catch (RemoteException var8) {
         EJBLogger.logExceptionDuringEJBRemove(var8);
      } finally {
         if (var2) {
            this.resetFile();
         }

      }

   }

   protected EnterpriseBean createBean() throws InternalException {
      SessionBean var1 = null;
      EJBObject var2 = null;
      BaseEJBLocalObjectIntf var3 = null;
      Thread var4 = Thread.currentThread();
      ClassLoader var5 = var4.getContextClassLoader();
      MethodInvocationHelper.pushMethodObject(this.beanInfo);
      this.setDir();
      int var6 = SubjectManager.getSubjectManager().getSize();
      boolean var7 = this.setFile();

      try {
         if (this.remoteHome != null) {
            var2 = this.remoteHome.allocateEO();
         }

         if (this.localHome != null) {
            var3 = this.localHome.allocateELO();
         }

         this.pushEnvironment();
         ClassLoader var8 = this.beanInfo.getModuleClassLoader();
         var4.setContextClassLoader(var8);
         var1 = (SessionBean)this.beanManager.createBean(var2, var3);
         if (this.beanInfo.isEJB30() && var1 instanceof __ProxyControl) {
            try {
               EJBContextManager.pushEjbContext(((WLSessionBean)var1).__WL_getEJBContext());
               ((__ProxyControl)var1).invokeLifecycleMethod(LifecycleEvent.POST_CONSTRUCT);
            } finally {
               EJBContextManager.popEjbContext();
            }
         }

         Method var26 = this.beanManager.getCreateMethod();
         if (var26 != null) {
            var26.invoke(var1, (Object[])null);
         }
      } catch (IllegalAccessException var23) {
         throw new AssertionError("Stateless session bean threw IllegalAccessException when we invoked its ejbCreate()");
      } catch (InvocationTargetException var24) {
         Throwable var9 = var24.getTargetException();
         EJBRuntimeUtils.throwInternalException("Error in ejbCreate:", var9);
      } finally {
         this.popEnvironment();
         var4.setContextClassLoader(var5);
         if (var7) {
            int var13 = SubjectManager.getSubjectManager().getSize();

            while(var13-- > var6) {
               this.resetFile();
            }
         }

         this.resetDir();
         if (MethodInvocationHelper.popMethodObject(this.beanInfo)) {
            this.beanManager.handleUncommittedLocalTransaction("ejbCreate()", var1);
         }

      }

      return var1;
   }

   public void destroyBean(EnterpriseBean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Destroying a bean in: '" + this + "'");
      }

      this.decrementCurrentSize();
      super.destroyBean(var1);
   }

   public void releaseBean(EnterpriseBean var1) {
      try {
         this.sizeLock.lock();
         super.releaseBean(var1);
         this.notEmpty.signal();
      } finally {
         this.sizeLock.unlock();
      }

   }

   private EnterpriseBean waitForBean(long var1) throws InternalException {
      if (debugLogger.isDebugEnabled()) {
         debug("Waiting for an instance in pool: '" + this + "'");
      }

      TimeUnit var3 = TimeUnit.MILLISECONDS;
      this.getPoolRuntime().incrementWaiterCount();
      long var4 = System.currentTimeMillis() + var1;

      EnterpriseBean var6;
      do {
         try {
            this.notEmpty.await(var4 - System.currentTimeMillis(), var3);
         } catch (InterruptedException var7) {
         }

         if (System.currentTimeMillis() >= var4) {
            this.getPoolRuntime().decrementWaiterCount();
            this.getPoolRuntime().incrementTotalTimeoutCount();
            ++this.timeouts;
            throw new RuntimeException("An invocation of EJB " + this.beanInfo.getDisplayName() + " timed out while waiting to get an instance from the free pool.");
         }

         var6 = super.getBean();
      } while(null == var6);

      this.getPoolRuntime().decrementWaiterCount();
      return var6;
   }

   public void reset() {
      this.beanClass = ((SessionBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.cleanup();
   }

   public void updateMaxBeansInFreePool(int var1) {
      List var2 = super.resizePool(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         EnterpriseBean var4 = (EnterpriseBean)var3.next();
         this.removeBean(var4);
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessSessionPool] " + var0);
   }

   private void decrementCurrentSize() {
      this.currentSize.decrementAndGet();
   }

   protected void incrementCurrentSize() {
      this.currentSize.incrementAndGet();
   }
}
