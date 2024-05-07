package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.datastructure.AuthInfo;

public class DiscardAuthTokenRequest extends UDDIPublishRequest {
   public DiscardAuthTokenRequest() {
   }

   public DiscardAuthTokenRequest(AuthInfo var1) {
      this.m_authInfo = var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<discard_authToken");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      var1.append("</discard_authToken>");
      return var1.toString();
   }
}
