package weblogic.auddi.uddi.response;

import weblogic.auddi.util.Util;

public abstract class DispositionReportResponse extends UDDIResponse {
   private Results m_results;

   public DispositionReportResponse() {
   }

   public DispositionReportResponse(int var1) {
      this.addResult(new Result(var1));
   }

   public void addResult(Result var1) {
      if (this.m_results == null) {
         this.m_results = new Results();
      }

      this.m_results.add(var1);
   }

   public Results getResults() {
      return this.m_results;
   }

   public void setResults(Results var1) {
      this.m_results = var1;
   }

   public boolean resultIs(int var1) {
      Results var2 = this.getResults();
      if (var2 == null) {
         return false;
      } else {
         Result var3 = var2.getFirst();
         if (var3 == null) {
            return false;
         } else {
            while(var3 != null) {
               if (var3.getErrno() != var1) {
                  return false;
               }

               var3 = var2.getNext();
            }

            return true;
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DispositionReportResponse)) {
         return false;
      } else {
         DispositionReportResponse var2 = (DispositionReportResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_results, (Object)var2.m_results);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<").append("dispositionReport");
      var1.append(super.toXML());
      var1.append(">");
      if (this.m_results != null) {
         var1.append(this.m_results.toXML());
      }

      var1.append("</").append("dispositionReport").append(">");
      return var1.toString();
   }
}
