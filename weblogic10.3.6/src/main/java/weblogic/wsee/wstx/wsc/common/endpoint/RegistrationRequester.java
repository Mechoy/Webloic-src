package weblogic.wsee.wstx.wsc.common.endpoint;

import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.PendingRequestManager;
import weblogic.wsee.wstx.wsc.common.RegistrationRequesterIF;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;

public abstract class RegistrationRequester implements RegistrationRequesterIF {
   private WebServiceContext m_context;

   public RegistrationRequester(WebServiceContext var1) {
      this.m_context = var1;
   }

   public void registerResponse(BaseRegisterResponseType var1) {
      String var2 = this.getWSATHelper().getWSATTidFromWebServiceContextHeaderList(this.m_context);
      PendingRequestManager.registryReponse(var2, var1);
   }

   protected abstract WSATHelper getWSATHelper();
}
