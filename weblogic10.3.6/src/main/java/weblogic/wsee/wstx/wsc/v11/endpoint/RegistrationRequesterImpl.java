package weblogic.wsee.wstx.wsc.v11.endpoint;

import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.endpoint.RegistrationRequester;

public class RegistrationRequesterImpl extends RegistrationRequester {
   public RegistrationRequesterImpl(WebServiceContext var1) {
      super(var1);
   }

   protected WSATHelper getWSATHelper() {
      return WSATHelper.V11;
   }
}
