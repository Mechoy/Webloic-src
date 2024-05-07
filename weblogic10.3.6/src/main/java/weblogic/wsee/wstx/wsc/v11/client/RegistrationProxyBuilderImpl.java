package weblogic.wsee.wstx.wsc.v11.client;

import com.sun.xml.ws.api.addressing.AddressingVersion;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.RegistrationIF;
import weblogic.wsee.wstx.wsc.common.client.RegistrationProxyBuilder;
import weblogic.wsee.wstx.wsc.v11.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v11.types.RegisterType;
import weblogic.wsee.wstx.wsc.v11.types.RegistrationCoordinatorPortType;

public class RegistrationProxyBuilderImpl extends RegistrationProxyBuilder {
   public RegistrationProxyBuilderImpl() {
      this.feature(new AddressingFeature());
   }

   public RegistrationIF<W3CEndpointReference, RegisterType, RegisterResponseType> build() {
      super.build();
      return new RegistrationProxyImpl();
   }

   protected String getDefaultCallbackAddress() {
      return WSATHelper.V11.getRegistrationRequesterAddress();
   }

   protected EndpointReferenceBuilder getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.W3C();
   }

   public class RegistrationProxyImpl extends RegistrationProxyBuilder.RegistrationProxyF<W3CEndpointReference, RegisterType, RegisterResponseType, RegistrationCoordinatorPortType> {
      private RegistrationServiceV11 service = new RegistrationServiceV11();
      private RegistrationCoordinatorPortType port;

      RegistrationProxyImpl() {
         super();
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(this.service.getWSDLDocumentLocation(), this.service.getServiceName(), RegistrationServiceV11.class);
         this.port = (RegistrationCoordinatorPortType)var3.getPort(RegistrationProxyBuilderImpl.this.to, RegistrationCoordinatorPortType.class, RegistrationProxyBuilderImpl.this.getEnabledFeatures());
         ((BindingProvider)this.port).getRequestContext().put("weblogic.wsee.security.wss.CredentialProviderList", RegistrationProxyBuilderImpl.this.credentialProviders);
      }

      public RegistrationCoordinatorPortType getDelegate() {
         return this.port;
      }

      public void asyncRegister(RegisterType var1) {
         this.port.registerOperation(var1);
      }

      public AddressingVersion getAddressingVersion() {
         return AddressingVersion.W3C;
      }
   }
}
