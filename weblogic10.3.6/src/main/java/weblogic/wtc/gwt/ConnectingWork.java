package weblogic.wtc.gwt;

import com.bea.core.jatmi.intf.TCTask;

class ConnectingWork implements TCTask {
   private TDMRemoteTDomain rdom;
   private ScheduledReconnect myControl;
   private String myName;

   ConnectingWork(ScheduledReconnect var1, TDMRemoteTDomain var2) {
      this.rdom = var2;
      this.myControl = var1;
   }

   public int execute() {
      if (this.rdom.getTsession(true) != null) {
         this.myControl.connectingSuccess();
      } else {
         this.myControl.connectingFailure();
      }

      return 0;
   }

   public void setTaskName(String var1) {
      this.myName = new String("ConnectingWork$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "ConnectingWork$unknown" : this.myName;
   }
}
