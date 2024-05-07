package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.datastructure.CompletionStatus;

public class GetAssertionStatusReportRequest extends UDDIPublishRequest {
   private CompletionStatus completionStatus = null;

   public void setCompletionStatus(CompletionStatus var1) {
      this.completionStatus = var1;
   }

   public CompletionStatus getCompletionStatus() {
      return this.completionStatus;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_assertionStatusReport");
      var1.append(super.toXML() + ">");
      if (super.m_authInfo != null) {
         var1.append(super.m_authInfo.toXML());
      }

      if (this.completionStatus != null) {
         var1.append("<completionStatus>");
         var1.append(this.completionStatus);
         var1.append("</completionStatus>");
      }

      var1.append("</get_assertionStatusReport>");
      return var1.toString();
   }
}
