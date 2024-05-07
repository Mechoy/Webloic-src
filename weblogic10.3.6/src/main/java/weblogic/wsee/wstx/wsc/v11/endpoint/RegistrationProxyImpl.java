package weblogic.wsee.wstx.wsc.v11.endpoint;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.endpoint.BaseRegistration;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v11.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.types.RegisterType;

public class RegistrationProxyImpl extends BaseRegistration<W3CEndpointReference, RegisterType, RegisterResponseType> {
   public RegistrationProxyImpl(WebServiceContext var1) {
      super(var1, Version.WSAT11);
   }

   protected EndpointReferenceBuilder<W3CEndpointReference> getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.W3C();
   }

   protected BaseRegisterResponseType<W3CEndpointReference, RegisterResponseType> newRegisterResponseType() {
      return XmlTypeAdapter.adapt(new RegisterResponseType());
   }

   protected String getCoordinatorAddress() {
      return WSATHelper.V11.getCoordinatorAddress();
   }
}
