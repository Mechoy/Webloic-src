package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCTask;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

class TpacallAsyncExecute implements TCTask {
   private gwatmi myAtmi;
   private tfmh myTmmsg;
   private CallDescriptor myCd;
   private GatewayTpacallAsyncReply callBack;
   private String myName;

   TpacallAsyncExecute(gwatmi var1, tfmh var2, CallDescriptor var3, GatewayTpacallAsyncReply var4) {
      this.myAtmi = var1;
      this.myTmmsg = var2;
      this.myCd = var3;
      this.callBack = var4;
   }

   public int execute() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/rdsession/TpacallAsyncExecute/");
      }

      TdomTcb var2 = (TdomTcb)this.myTmmsg.tdom.body;
      int var3 = var2.get_diagnostic();
      int var4 = var2.getTpurcode();
      int var5 = var2.get_errdetail();
      int var6 = var2.get_opcode();
      UserTcb var8 = null;
      if (this.myTmmsg != null && this.callBack != null) {
         Transaction var10 = this.callBack.getTransaction();
         TypedBuffer var7;
         if (this.myTmmsg.user == null) {
            var7 = null;
         } else {
            var8 = (UserTcb)this.myTmmsg.user.body;
            var7 = var8.user_data;
         }

         TuxedoReply var9 = new TuxedoReply(var7, var4, (CallDescriptor)null);
         if (var6 == 3 && var3 != 11 && var3 != 10) {
            if ((var3 == 18 || var3 == 13) && var10 != null) {
               try {
                  var10.setRollbackOnly();
               } catch (SystemException var13) {
                  if (var1) {
                     ntrace.doTrace("/rdsession/TpacallAsyncExecute/SystemException:" + var13);
                  }
               }
            }

            TPException var15 = new TPException(var3, 0, var4, var5, var9);
            this.callBack.failure(this.myAtmi, this.myCd, var15);
            if (var1) {
               ntrace.doTrace("]/rdsession/TpacallAsyncExecute/10/" + var15);
            }

            return 0;
         } else {
            if (var3 != 11 && var3 != 10) {
               var3 = 0;
            }

            if (var3 == 0) {
               this.myAtmi.restoreTfmhToCache(this.myTmmsg);
               this.callBack.success(this.myAtmi, this.myCd, var9);
               if (var1) {
                  ntrace.doTrace("]/rdsession/TpacallAsyncExecute/30/" + var9);
               }

               return 0;
            } else {
               if (var10 != null) {
                  try {
                     var10.setRollbackOnly();
                  } catch (SystemException var14) {
                     if (var1) {
                        ntrace.doTrace("/rdsession/TpacallAsyncExecute/SystemException:" + var14);
                     }
                  }
               }

               TPReplyException var11 = new TPReplyException(var3, 0, var4, var5, var9);
               this.callBack.failure(this.myAtmi, this.myCd, var11);
               if (var1) {
                  ntrace.doTrace("]/rdsession/TpacallAsyncExecute/20/" + var11);
               }

               return 0;
            }
         }
      } else {
         return 0;
      }
   }

   public void setTaskName(String var1) {
      this.myName = new String("TpacallAsyncExecute$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "TpacallAsyncExecute$unknown" : this.myName;
   }
}
