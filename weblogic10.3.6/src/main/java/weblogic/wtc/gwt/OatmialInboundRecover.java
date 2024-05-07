package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import java.util.Timer;
import java.util.TimerTask;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.kernel.ExecuteThread;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TdomTranTcb;
import weblogic.wtc.jatmi.Txid;
import weblogic.wtc.jatmi.gwatmi;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

class OatmialInboundRecover extends TimerTask {
   private Xid myXid;
   private int myOperation;
   private TDMRemote myRemoteDomain;
   private Timer myTimeService;
   private OatmialServices tos;
   private TuxedoLoggable myTransactionLoggable;
   protected static final int RECOVERED_NOLOG = 0;
   protected static final int RECOVERED_PREPARED = 1;
   protected static final int RECOVERED_COMMITTING = 2;
   protected static final int NOT_RECOVERED_PREPARED = 3;
   protected static final int NOT_RECOVERED_COMMITTING = 4;

   public OatmialInboundRecover(Xid var1, int var2, TDMRemote var3, Timer var4, TuxedoLoggable var5) {
      this.myXid = var1;
      this.myOperation = var2;
      this.myRemoteDomain = var3;
      this.myTimeService = var4;
      this.myTransactionLoggable = var5;
      this.tos = WTCService.getOatmialServices();
   }

   public void execute(ExecuteThread var1) throws Exception {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/OatmialInboundRecover/execute/" + Thread.currentThread());
      }

      XAResource var3 = null;
      TuxedoLoggable var4 = null;
      byte var5 = 0;
      gwatmi var9 = null;
      boolean var10 = true;
      if ((var3 = TCTransactionHelper.getXAResource()) == null) {
         if (var2) {
            ntrace.doTrace("]/OatmialInboundRecover/execute/10");
         }

      } else {
         byte var15;
         switch (this.myOperation) {
            case 0:
               if (var2) {
                  ntrace.doTrace("RECOVERED_NOLOG");
               }

               try {
                  if (var2) {
                     ntrace.doTrace("myXid = " + this.myXid);
                  }

                  var3.rollback(this.myXid);
               } catch (XAException var13) {
                  WTCLogger.logWarningRecoverRollbackFailure(var13.toString());
               }

               if (var2) {
                  ntrace.doTrace("]/OatmialInboundRecover/execute/20");
               }

               return;
            case 1:
               if (var2) {
                  ntrace.doTrace("RECOVERED_PREPARED");
               }

               var15 = 8;
               break;
            case 2:
               if (var2) {
                  ntrace.doTrace("RECOVERED_COMMITTING");
               }

               var4 = this.myTransactionLoggable;

               try {
                  if (var2) {
                     ntrace.doTrace("myXid = " + this.myXid);
                  }

                  var3.commit(this.myXid, false);
               } catch (XAException var14) {
                  switch (var14.errorCode) {
                     case -7:
                     case -6:
                     case -5:
                     case -4:
                     case -3:
                     case -2:
                     case -1:
                     case 0:
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                     case 7:
                     case 8:
                     default:
                        var5 = 16;
                        break;
                     case 5:
                     case 6:
                        var5 = 8;
                  }
               }

               var15 = 10;
               break;
            case 3:
               if (var2) {
                  ntrace.doTrace("NOT_RECOVERED_PREPARED");
               }

               var4 = this.myTransactionLoggable;
               var4.forget();
               return;
            case 4:
               if (var2) {
                  ntrace.doTrace("NOT_RECOVERED_COMMITTING");
               }

               var4 = this.myTransactionLoggable;
               var15 = 10;
               break;
            default:
               if (var2) {
                  ntrace.doTrace("]/OatmialInboundRecover/execute/30/" + this.myOperation);
               }

               return;
         }

         if (this.myRemoteDomain == null) {
            if (var2) {
               ntrace.doTrace("]/OatmialInboundRecover/execute/40");
            }

         } else if ((var9 = this.myRemoteDomain.getTsession(true)) == null) {
            if (var4 != null) {
               var4.forget();
            }

            if (var15 == 10) {
               this.tos.deleteInboundRdomsAssociatedWithXid(this.myXid);
               if (var2) {
                  ntrace.doTrace("]/OatmialInboundRecover/execute/50");
               }

            } else {
               this.myTimeService.schedule(this, 300000L);
               if (var2) {
                  ntrace.doTrace("]/OatmialInboundRecover/execute/60/rescheduled for five minutes");
               }

            }
         } else {
            tfmh var6 = new tfmh(1);
            TdomTcb var7 = new TdomTcb(var15, 0, 0, (String)null);
            var7.set_info(32 | var5);
            var6.tdom = new tcm((short)7, var7);
            TdomTranTcb var8 = new TdomTranTcb(new Txid(this.myXid.getGlobalTransactionId()));
            var8.setNwtranidparent(new String(this.myXid.getBranchQualifier()));
            var6.tdomtran = new tcm((short)10, var8);

            try {
               var9.send_transaction_reply(var6);
               if (var2) {
                  ntrace.doTrace("reply for transaction recovery sent");
               }
            } catch (TPException var12) {
               WTCLogger.logTPEsendTran(var12);
            }

            if (var4 != null) {
               var4.forget();
            }

            if (var15 == 10) {
               this.tos.deleteInboundRdomsAssociatedWithXid(this.myXid);
            }

            if (var2) {
               ntrace.doTrace("]/OatmialInboundRecover/execute/70");
            }

         }
      }
   }

   public void run() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/OatmialInboundRecover/run/");
      }

      if (this.tos.getInboundRdomsAssociatedWithXid(this.myXid) == null) {
         if (var1) {
            ntrace.doTrace("]/OatmialInboundRecover/run/10");
         }

      } else {
         try {
            this.execute((ExecuteThread)null);
         } catch (Exception var3) {
            if (var1) {
               ntrace.doTrace("]/OatmialInboundRecover/run/20/" + var3);
            }

            return;
         }

         if (var1) {
            ntrace.doTrace("]/OatmialInboundRecover/run/30");
         }

      }
   }
}
