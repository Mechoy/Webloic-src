package weblogic.wsee.wstx.wsc.v10.endpoint;

import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import javax.xml.ws.WebServiceContext;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.endpoint.BaseRegistration;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.XmlTypeAdapter;
import weblogic.wsee.wstx.wsc.v10.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.types.RegisterType;

public class RegistrationProxyImpl extends BaseRegistration<MemberSubmissionEndpointReference, RegisterType, RegisterResponseType> {
   public RegistrationProxyImpl(WebServiceContext var1) {
      super(var1, Version.WSAT10);
   }

   protected EndpointReferenceBuilder<MemberSubmissionEndpointReference> getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.MemberSubmission();
   }

   protected BaseRegisterResponseType<MemberSubmissionEndpointReference, RegisterResponseType> newRegisterResponseType() {
      return XmlTypeAdapter.adapt(new RegisterResponseType());
   }

   protected String getCoordinatorAddress() {
      return WSATHelper.V10.getCoordinatorAddress();
   }
}
