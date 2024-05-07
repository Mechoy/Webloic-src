package weblogic.wsee.wstx.wsc.v10;

import weblogic.wsee.wstx.wsat.security.ClientPolicyFeatureBuilder;
import weblogic.wsee.wstx.wsat.security.IssuedTokenBuilder;
import weblogic.wsee.wstx.wsc.common.WSATCoordinationContextBuilder;
import weblogic.wsee.wstx.wsc.common.WSCBuilderFactory;
import weblogic.wsee.wstx.wsc.common.client.RegistrationMessageBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationProxyBuilder;
import weblogic.wsee.wstx.wsc.v10.client.RegistrationMessageBuilderImpl;
import weblogic.wsee.wstx.wsc.v10.client.RegistrationProxyBuilderImpl;

public class WSCBuilderFactoryImpl extends WSCBuilderFactory {
   public WSATCoordinationContextBuilder newWSATCoordinationContextBuilder() {
      return new WSATCoordinationContextBuilderImpl();
   }

   public RegistrationProxyBuilder newRegistrationProxyBuilder() {
      return new RegistrationProxyBuilderImpl();
   }

   public RegistrationMessageBuilder newWSATRegistrationRequestBuilder() {
      return new RegistrationMessageBuilderImpl();
   }

   public IssuedTokenBuilder newIssuedTokenBuilder() {
      return IssuedTokenBuilder.v12();
   }

   public ClientPolicyFeatureBuilder newWSATReqistrationClientPolicyFeatureBuilder() {
      return ClientPolicyFeatureBuilder.V10();
   }
}
