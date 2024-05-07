package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.AuthInfo;

public abstract class UpdateRequest extends Request {
   protected AuthInfo authInfo = null;

   public void setAuthInfo(AuthInfo var1) {
      this.authInfo = var1;
   }

   public AuthInfo getAuthInfo() {
      return this.authInfo;
   }
}
