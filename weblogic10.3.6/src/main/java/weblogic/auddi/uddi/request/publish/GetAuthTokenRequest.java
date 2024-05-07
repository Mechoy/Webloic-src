package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetAuthTokenRequest extends UDDIRequest {
   private String userid = null;
   private String cred = null;

   public GetAuthTokenRequest(String var1, String var2) throws UDDIException {
      if (var1 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "userID"));
      } else if (var2 == null) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "cred"));
      } else {
         this.userid = var1;
         this.cred = var2;
      }
   }

   public GetAuthTokenRequest() {
   }

   public void setUserId(String var1) {
      this.userid = var1;
   }

   public void setCred(String var1) {
      this.cred = var1;
   }

   public String getUserId() {
      return this.userid;
   }

   public String getCred() {
      return this.cred;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_authToken");
      var1.append(super.toXML());
      var1.append(" userID=\"" + this.userid + "\"");
      var1.append(" cred=\"" + this.cred + "\">");
      var1.append("</get_authToken>");
      return var1.toString();
   }
}
