package weblogic.ejb.container.timer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBTimerRuntimeMBean;

public class MDBTimerManagerFactory {
   private HashSet<BeanManager> bms = new HashSet();
   private boolean tmsetupCalled = false;

   public synchronized TimerManager createEJBTimerManager(BeanManager var1) {
      if (this.bms.size() > 0) {
         return ((BeanManager)this.bms.iterator().next()).getTimerManager();
      } else {
         BeanManager var2 = (BeanManager)Proxy.newProxyInstance(var1.getClass().getClassLoader(), var1.getClass().getInterfaces(), new InterceptingInvocationHandlerImpl());
         Object var3 = null;
         this.bms.add(var1);
         if (var1.getBeanInfo().isClusteredTimers()) {
            var3 = new ClusteredEJBTimerManager(var2);
         } else {
            var3 = new EJBTimerManager(var2);
         }

         return (TimerManager)var3;
      }
   }

   public synchronized BeanManager getActiveBeanManager() {
      return this.bms.size() > 0 ? (BeanManager)this.bms.iterator().next() : null;
   }

   public synchronized void setup(BeanManager var1, EJBTimerRuntimeMBean var2) throws WLDeploymentException {
      if (!this.tmsetupCalled && this.bms.size() == 1 && var1.getTimerManager() != null) {
         var1.getTimerManager().setup(var2);
         this.tmsetupCalled = true;
      }

      this.bms.add(var1);
   }

   public synchronized void undeploy(BeanManager var1) {
      if (this.bms.size() == 1 && var1.getTimerManager() != null) {
         var1.getTimerManager().undeploy();
         this.tmsetupCalled = false;
      }

      this.bms.remove(var1);
   }

   class InterceptingInvocationHandlerImpl implements InvocationHandler {
      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         Object var4 = null;

         try {
            var4 = var2.invoke(MDBTimerManagerFactory.this.getActiveBeanManager(), var3);
            return var4;
         } catch (InvocationTargetException var6) {
            throw var6.getCause();
         }
      }
   }
}
