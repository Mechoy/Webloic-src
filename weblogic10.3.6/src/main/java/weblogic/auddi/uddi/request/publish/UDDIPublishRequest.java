package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AuthInfo;
import weblogic.auddi.uddi.request.UDDIRequest;

public abstract class UDDIPublishRequest extends UDDIRequest {
   protected AuthInfo m_authInfo = null;

   public void setAuthInfo(AuthInfo var1) throws UDDIException {
      this.m_authInfo = var1;
   }

   public AuthInfo getAuthInfo() {
      return this.m_authInfo;
   }
}
