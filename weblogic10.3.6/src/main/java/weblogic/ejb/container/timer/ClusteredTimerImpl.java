package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.TimerHandler;
import weblogic.ejb.container.interfaces.TimerIntf;
import weblogic.scheduler.ejb.EJBTimerListener;
import weblogic.transaction.TxHelper;

public final class ClusteredTimerImpl implements Timer, TimerIntf, EJBTimerListener {
   private static final DebugLogger debugLogger;
   private Object pk;
   private Serializable info;
   private boolean isTransactional;
   private String appName;
   private String jarName;
   private String ejbName;
   private transient weblogic.scheduler.Timer timer;

   public ClusteredTimerImpl(Object var1, Serializable var2, boolean var3, BeanInfo var4) {
      this.pk = var1;
      this.info = var2;
      this.isTransactional = var3;
      DeploymentInfo var5 = var4.getDeploymentInfo();
      this.appName = var5.getApplicationName();
      this.jarName = var5.getModuleURI();
      this.ejbName = var4.getEJBName();
   }

   public void initialize(weblogic.timers.Timer var1) {
      this.timer = (weblogic.scheduler.Timer)var1;
   }

   public void timerExpired(weblogic.timers.Timer var1) {
      this.initialize(var1);
      ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getApplicationContext(this.appName);
      Context var3 = var2.getEnvContext();

      try {
         TimerHandler var4 = (TimerHandler)var3.lookup("ejb/" + this.jarName + "#" + this.ejbName + "/timerHandler");
         var4.executeTimer(this);
      } catch (NamingException var7) {
         EJBLogger.logClusteredTimerFailedToLookupTimerHandler(this.ejbName, this.jarName, this.appName);
         if (this.isTransactional) {
            try {
               TxHelper.getTransaction().setRollbackOnly();
            } catch (Exception var6) {
               EJBLogger.logErrorMarkingRollback(var6);
            }
         }
      }

   }

   public void cancel() {
      this.timer.cancel();
   }

   public TimerHandle getHandle() {
      return new ClusteredTimerHandleImpl(this.timer);
   }

   public Serializable getInfo() {
      return this.info;
   }

   public Date getNextTimeout() {
      return new Date(this.timer.getTimeout());
   }

   public long getTimeRemaining() {
      return this.timer.getTimeout() - System.currentTimeMillis();
   }

   public Object getPrimaryKey() {
      return this.pk;
   }

   public String getGroupName() {
      return this.pk == null ? null : Integer.toString(this.pk.hashCode());
   }

   public boolean isTransactional() {
      return this.isTransactional;
   }

   public boolean exists() {
      return !this.timer.isCancelled();
   }

   static {
      debugLogger = EJBDebugService.timerLogger;
   }
}
