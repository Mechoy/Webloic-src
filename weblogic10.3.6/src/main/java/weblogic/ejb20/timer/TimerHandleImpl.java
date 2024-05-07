package weblogic.ejb20.timer;

import java.io.Serializable;
import javax.ejb.EJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.TimerHelper;
import weblogic.ejb.container.interfaces.TimerIntf;
import weblogic.logging.Loggable;

public final class TimerHandleImpl implements TimerHandle, Serializable {
   private static final long serialVersionUID = 3158846282971285273L;
   private transient TimerIntf timer;
   private Long timerID;
   private String jarName;
   private String ejbName;

   public TimerHandleImpl(TimerIntf var1, Long var2, String var3, String var4) {
      this.timer = var1;
      this.timerID = var2;
      this.jarName = var3;
      this.ejbName = var4;
   }

   public Timer getTimer() {
      if (this.timer != null) {
         if (!this.timer.exists()) {
            Loggable var5 = EJBLogger.logExpiredTimerHandleLoggable();
            throw new NoSuchObjectLocalException(var5.getMessage());
         } else {
            return this.timer;
         }
      } else {
         TimerHelper var1 = null;

         try {
            InitialContext var2 = new InitialContext();
            var1 = (TimerHelper)var2.lookup("java:app/ejb/" + this.jarName + "#" + this.ejbName + "/timerHelper");
         } catch (NamingException var4) {
            Loggable var3 = EJBLogger.logTimerHandleInvokedOutsideOriginalAppContextLoggable();
            throw new EJBException(var3.getMessage());
         }

         this.timer = (TimerIntf)var1.getTimer(this.timerID);
         if (this.timer != null) {
            return this.timer;
         } else {
            Loggable var6 = EJBLogger.logExpiredTimerHandleLoggable();
            throw new NoSuchObjectLocalException(var6.getMessage());
         }
      }
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.ejbName == null ? 0 : this.ejbName.hashCode());
      var2 = 31 * var2 + (this.jarName == null ? 0 : this.jarName.hashCode());
      var2 = 31 * var2 + (this.timerID == null ? 0 : this.timerID.hashCode());
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof TimerHandleImpl)) {
         return false;
      } else {
         TimerHandleImpl var2 = (TimerHandleImpl)var1;
         if (!this.ejbName.equals(var2.ejbName)) {
            return false;
         } else if (!this.jarName.equals(var2.jarName)) {
            return false;
         } else {
            return this.timerID.equals(var2.timerID);
         }
      }
   }

   public String toString() {
      return "[TimerHandle] timerID: " + this.timerID + " jarName: " + this.jarName + " ejbName: " + this.ejbName;
   }
}
