package weblogic.uddi.client.structures.response;

import weblogic.uddi.client.structures.datatypes.AuthInfo;

public class AuthToken extends Response {
   private AuthInfo authInfo = null;

   public AuthInfo getAuthInfo() {
      return this.authInfo;
   }

   public void setAuthInfo(AuthInfo var1) {
      this.authInfo = var1;
   }
}
