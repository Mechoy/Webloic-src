package weblogic.wsee.wstx.wsc.common;

import javax.xml.ws.EndpointReference;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;

public interface RegistrationRequesterIF<T extends EndpointReference, P> {
   void registerResponse(BaseRegisterResponseType<T, P> var1);
}
