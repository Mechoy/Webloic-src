package weblogic.wsee.wstx.wsc.common;

import javax.xml.ws.EndpointReference;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;

public interface RegistrationIF<T extends EndpointReference, K, P> {
   BaseRegisterResponseType<T, P> registerOperation(BaseRegisterType<T, K> var1);
}
