package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXA;
import com.bea.core.jatmi.intf.TCTask;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import java.util.Timer;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import weblogic.wtc.WTCLogger;

class OatmialCommitter implements TCTask {
   private Xid myXid;
   private TuxedoXA myResource;
   private Timer myTimeService;
   private long myTimeLeft;
   private long myLastTime;
   private boolean myRetry;
   private OatmialCommitterTimer myCommitterTimer;
   private String myName;

   public OatmialCommitter(Xid var1, TuxedoXA var2, int var3, Timer var4) {
      this.myXid = var1;
      this.myResource = var2;
      this.myTimeService = var4;
      this.myTimeLeft = (long)(var3 * 1000);
      this.myLastTime = System.currentTimeMillis();
      this.myCommitterTimer = new OatmialCommitterTimer(this);
      this.myRetry = false;
   }

   public int execute() {
      boolean var1 = ntrace.isTraceEnabled(2);
      boolean var2 = false;
      if (var1) {
         ntrace.doTrace("[/OatmialCommitter/execute/" + Thread.currentThread());
      }

      TuxedoLoggable var3 = null;
      if (this.myXid != null && this.myResource != null) {
         var3 = TCTransactionHelper.createTuxedoLoggable(this.myXid, 2);

         try {
            this.myResource.recoveryCommit(this.myXid, false);
         } catch (XAException var6) {
            var2 = true;
            WTCLogger.logXAEcommitXid(var6);
         }

         var3.forget();
         if (var2) {
            long var4 = System.currentTimeMillis();
            this.myTimeLeft -= var4 - this.myLastTime;
            this.myLastTime = var4;
            if (this.myTimeLeft < 0L) {
               this.myCommitterTimer.cancel();
               if (var1) {
                  ntrace.doTrace("/OatmialCommitter/execute/cancelled timer commit retry timer");
               }
            }

            if (!this.myRetry) {
               this.myRetry = true;
               this.myTimeService.schedule(this.myCommitterTimer, 60000L, 60000L);
               if (var1) {
                  ntrace.doTrace("/OatmialCommitter/execute/scheduled next commit retry");
               }
            }
         } else {
            this.myCommitterTimer.cancel();
         }

         if (var1) {
            ntrace.doTrace("]/OatmialCommitter/execute/30");
         }

         return 0;
      } else {
         if (var1) {
            ntrace.doTrace("]/OatmialCommitter/execute/10");
         }

         return 0;
      }
   }

   public void setTaskName(String var1) {
      this.myName = new String("OatmialCommitter$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "OatmialCommitter$unknown" : this.myName;
   }
}
