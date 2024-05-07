package weblogic.wtc.jatmi;

import com.bea.core.jatmi.internal.TCTaskHelper;
import java.util.TimerTask;

class rdtimer extends TimerTask {
   private rdsession myRdsession;
   private gwatmi myGwatmi;
   private SessionAcallDescriptor myReqid;
   private ReplyQueue myRplyObj;

   public rdtimer(rdsession var1, gwatmi var2, SessionAcallDescriptor var3, ReplyQueue var4) {
      this.myRdsession = var1;
      this.myGwatmi = var2;
      this.myReqid = var3;
      this.myRplyObj = var4;
   }

   public void run() {
      TdomTcb var2 = new TdomTcb(3, this.myReqid.getCd(), 4194304, (String)null);
      var2.set_diagnostic(13);
      tfmh var1 = new tfmh(1);
      var1.tdom = new tcm((short)7, var2);
      Object[] var3;
      if ((var3 = this.myRdsession.removeReplyObj(this.myReqid)) != null) {
         GatewayTpacallAsyncReply var4 = (GatewayTpacallAsyncReply)var3[2];
         if (this.myReqid.hasCallBack() && var4 != null) {
            TpacallAsyncExecute var5 = new TpacallAsyncExecute(this.myGwatmi, var1, this.myReqid, var4);
            TCTaskHelper.schedule(var5);
         } else {
            this.myRplyObj.add_reply(this.myGwatmi, this.myReqid, var1);
         }
      }

   }
}
