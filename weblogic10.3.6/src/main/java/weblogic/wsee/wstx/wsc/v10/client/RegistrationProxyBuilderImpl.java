package weblogic.wsee.wstx.wsc.v10.client;

import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.io.Closeable;
import java.io.IOException;
import javax.xml.ws.BindingProvider;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.RegistrationIF;
import weblogic.wsee.wstx.wsc.common.client.RegistrationProxyBuilder;
import weblogic.wsee.wstx.wsc.v10.types.RegisterResponseType;
import weblogic.wsee.wstx.wsc.v10.types.RegisterType;
import weblogic.wsee.wstx.wsc.v10.types.RegistrationCoordinatorPortType;

public class RegistrationProxyBuilderImpl extends RegistrationProxyBuilder {
   private static final RegistrationServiceV10 service = new RegistrationServiceV10();

   public RegistrationProxyBuilderImpl() {
      this.feature(new MemberSubmissionAddressingFeature());
   }

   protected String getDefaultCallbackAddress() {
      return WSATHelper.V10.getRegistrationRequesterAddress();
   }

   protected EndpointReferenceBuilder getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.MemberSubmission();
   }

   public RegistrationIF<MemberSubmissionEndpointReference, RegisterType, RegisterResponseType> build() {
      super.build();
      return new RegistrationProxyImpl();
   }

   class RegistrationProxyImpl extends RegistrationProxyBuilder.RegistrationProxyF<MemberSubmissionEndpointReference, RegisterType, RegisterResponseType, RegistrationCoordinatorPortType> {
      private RegistrationCoordinatorPortType port;

      RegistrationProxyImpl() {
         super();
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(RegistrationProxyBuilderImpl.service.getWSDLDocumentLocation(), RegistrationProxyBuilderImpl.service.getServiceName(), RegistrationServiceV10.class);
         this.port = (RegistrationCoordinatorPortType)var3.getPort(RegistrationProxyBuilderImpl.this.to, RegistrationCoordinatorPortType.class, RegistrationProxyBuilderImpl.this.getEnabledFeatures());
         ((BindingProvider)this.port).getRequestContext().put("weblogic.wsee.security.wss.CredentialProviderList", RegistrationProxyBuilderImpl.this.credentialProviders);
      }

      public RegistrationCoordinatorPortType getDelegate() {
         return this.port;
      }

      public void asyncRegister(RegisterType var1) {
         this.port.registerOperation(var1);
         this.closePort();
      }

      private void closePort() {
         try {
            ((Closeable)this.port).close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }

      }

      public AddressingVersion getAddressingVersion() {
         return AddressingVersion.MEMBER;
      }
   }
}
