package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;

public class SuccessDispositionReportResponse extends DispositionReportResponse {
   private boolean hasDefaultResult = true;

   public SuccessDispositionReportResponse() throws UDDIException {
      Result var1 = new Result();
      var1.setErrno(0);
      var1.setErrInfo(new ErrInfo(0, (String)null));
      super.addResult(var1);
      this.hasDefaultResult = true;
   }

   public void addResult(Result var1) {
      if (this.hasDefaultResult) {
         Results var2 = new Results();
         this.setResults(var2);
         this.hasDefaultResult = false;
      }

      super.addResult(var1);
   }
}
