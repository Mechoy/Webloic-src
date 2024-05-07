package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.util.PropertyManager;
import weblogic.auddi.util.Util;

public class AuthTokenResponse extends UDDIResponse {
   private AuthInfo authInfo;

   public AuthTokenResponse(AuthInfo var1) {
      this.authInfo = var1;
   }

   public AuthTokenResponse() {
   }

   public void setAuthInfo(AuthInfo var1) {
      this.authInfo = var1;
   }

   public AuthInfo getAuthInfo() {
      return this.authInfo;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AuthTokenResponse)) {
         return false;
      } else {
         AuthTokenResponse var2 = (AuthTokenResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.authInfo, (Object)var2.authInfo);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<authToken xmlns=\"urn:uddi-org:api_v2\" generic=\"2.0\" operator=\"" + PropertyManager.getRuntimeProperty("auddi.siteoperator") + "\">");
      if (this.authInfo != null) {
         var1.append(this.authInfo.toXML());
      }

      var1.append("</authToken>");
      return var1.toString();
   }
}
