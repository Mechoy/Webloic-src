package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AuthInfo;

public class GetRegisteredInfoRequest extends UDDIPublishRequest {
   public GetRegisteredInfoRequest() {
   }

   public GetRegisteredInfoRequest(AuthInfo var1) throws UDDIException {
      this.setAuthInfo(var1);
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_registeredInfo");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      var1.append("</get_registeredInfo>");
      return var1.toString();
   }
}
