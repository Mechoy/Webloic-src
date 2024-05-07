package weblogic.wtc.jatmi;

import java.util.TimerTask;

class rdXtimer extends TimerTask {
   private rdsession myRdsession;
   private gwatmi myGwatmi;
   private Txid myTxid;
   private TuxXidRply myRplyObj;

   public rdXtimer(rdsession var1, gwatmi var2, Txid var3, TuxXidRply var4) {
      this.myRdsession = var1;
      this.myGwatmi = var2;
      this.myTxid = var3;
      this.myRplyObj = var4;
   }

   public void run() {
      TdomTcb var2 = new TdomTcb(12, 0, 0, (String)null);
      var2.set_diagnostic(13);
      TdomTranTcb var3 = new TdomTranTcb(this.myTxid);
      tfmh var1 = new tfmh(1);
      var1.tdom = new tcm((short)7, var2);
      var1.tdomtran = new tcm((short)10, var3);
      if (this.myRdsession.remove_rplyXidObj(this.myTxid)) {
         this.myRplyObj.add_reply(this.myGwatmi, this.myTxid, var1);
      }

   }
}
