package weblogic.ejb.container.pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.EnterpriseBean;
import javax.ejb.MessageDrivenBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.manager.MessageDrivenManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.utils.AssertionError;

public final class MessageDrivenPool extends Pool {
   private final MessageDrivenManager beanManager;
   private AtomicInteger currentSize = new AtomicInteger(0);
   private long timeouts;
   private final Lock sizeLock = new ReentrantLock();
   private final Condition notEmpty;

   public MessageDrivenPool(MessageDrivenManager var1, BeanInfo var2, EJBPoolRuntimeMBean var3) throws WLDeploymentException {
      super(var1, var2, var3);
      this.notEmpty = this.sizeLock.newCondition();
      this.beanClass = this.beanInfo.getBeanClass();
      this.beanManager = var1;
      if (debugLogger.isDebugEnabled()) {
         debug("Created: " + this + " with initialSize: '" + this.initialSize + "', maximumSize: '" + this.maximumSize + "'");
      }

   }

   public EnterpriseBean getBean() throws InternalException {
      throw new AssertionError("MessageDrivenPool.getBean() must be called with mustWait=true");
   }

   public EnterpriseBean getBean(long var1) throws InternalException {
      EnterpriseBean var3 = null;
      if (this.getMaxBeansInFreePool() > 0) {
         var3 = super.getBean();
         if (null == var3) {
            try {
               this.sizeLock.lock();
               if (this.getMaxBeansInFreePool() <= this.currentSize.get()) {
                  var3 = this.waitForBean(var1);
               }
            } finally {
               this.sizeLock.unlock();
            }
         }
      }

      if (null == var3) {
         var3 = this.createBean();
         this.incrementCurrentSize();
         if (debugLogger.isDebugEnabled()) {
            debug("allocate new: '" + var3 + "'");
         }
      }

      this.getPoolRuntime().incrementBeansInUseCount();
      return var3;
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

   protected void removeBean(EnterpriseBean var1) {
      this.decrementCurrentSize();
      boolean var2 = this.setSegment();

      try {
         ((MessageDrivenBean)var1).ejbRemove();
      } catch (Exception var8) {
         EJBLogger.logExceptionDuringEJBRemove(var8);
      } finally {
         if (var2) {
            this.resetFile();
         }

      }

   }

   protected EnterpriseBean createBean() throws InternalException {
      MethodInvocationHelper.pushMethodObject(this.beanInfo);
      boolean var1 = this.setFile();

      MessageDrivenBean var3;
      try {
         this.pushEnvironment();
         MessageDrivenBean var2 = this.beanManager.createBean();
         var3 = var2;
      } finally {
         this.popEnvironment();
         if (var1) {
            this.resetFile();
         }

         MethodInvocationHelper.popMethodObject(this.beanInfo);
      }

      return var3;
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

   public void reset() {
      this.beanClass = this.beanInfo.getBeanClass();
      this.cleanup();
   }

   private static void debug(String var0) {
      debugLogger.debug("[MessageDrivenPool] " + var0);
   }

   private void decrementCurrentSize() {
      this.currentSize.decrementAndGet();
   }

   protected void incrementCurrentSize() {
      this.currentSize.incrementAndGet();
   }
}
