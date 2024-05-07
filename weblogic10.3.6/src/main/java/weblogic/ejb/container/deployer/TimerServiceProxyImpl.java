package weblogic.ejb.container.deployer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJBException;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.WLTimerService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.logging.Loggable;

public class TimerServiceProxyImpl implements WLTimerService {
   private BeanInfo bi;
   private boolean isSessionBean;

   public TimerServiceProxyImpl(BeanInfo var1) {
      this.bi = var1;
      if (var1 instanceof SessionBeanInfo) {
         this.isSessionBean = true;
      }

   }

   private TimerService getDelegate() {
      if (this.bi.isTimerDriven()) {
         return this.isSessionBean ? EJBContextManager.getEJBContext().getTimerService() : ((MessageDrivenManagerIntf)this.bi.getBeanManager()).getMessageDrivenContext().getTimerService();
      } else {
         Loggable var1 = null;
         if (this.isSessionBean && ((SessionBeanInfo)this.bi).isStateful()) {
            var1 = EJBLogger.logStatefulSessionBeanAttemptToAccessTimerServiceLoggable();
         } else {
            var1 = EJBLogger.logIllegalAttemptToAccessTimerServiceLoggable();
         }

         throw new IllegalStateException(var1.getMessage());
      }
   }

   public Timer createTimer(Date var1, long var2, Serializable var4) {
      return this.getDelegate().createTimer(var1, var2, var4);
   }

   public Timer createTimer(Date var1, Serializable var2) {
      return this.getDelegate().createTimer(var1, var2);
   }

   public Timer createTimer(long var1, long var3, Serializable var5) {
      return this.getDelegate().createTimer(var1, var3, var5);
   }

   public Timer createTimer(long var1, Serializable var3) {
      return this.getDelegate().createTimer(var1, var3);
   }

   public Collection getTimers() {
      return this.getDelegate().getTimers();
   }

   public Timer createTimer(Date var1, long var2, Serializable var4, WLTimerInfo var5) throws IllegalArgumentException, IllegalStateException, EJBException {
      return ((WLTimerService)this.getDelegate()).createTimer(var1, var2, var4, var5);
   }

   public Timer createTimer(Date var1, Serializable var2, WLTimerInfo var3) throws IllegalArgumentException, IllegalStateException, EJBException {
      return ((WLTimerService)this.getDelegate()).createTimer(var1, var2, var3);
   }

   public Timer createTimer(long var1, long var3, Serializable var5, WLTimerInfo var6) throws IllegalArgumentException, IllegalStateException, EJBException {
      return ((WLTimerService)this.getDelegate()).createTimer(var1, var3, var5, var6);
   }

   public Timer createTimer(long var1, Serializable var3, WLTimerInfo var4) throws IllegalArgumentException, IllegalStateException, EJBException {
      return ((WLTimerService)this.getDelegate()).createTimer(var1, var3, var4);
   }
}
