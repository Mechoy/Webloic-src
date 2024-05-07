package weblogic.wsee.wstx.wsc.v10.client;

import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationMessageBuilder;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;
import weblogic.wsee.wstx.wsc.v10.XmlTypeAdapter;

public class RegistrationMessageBuilderImpl extends RegistrationMessageBuilder {
   public RegistrationMessageBuilder durable(boolean var1) {
      super.durable(var1);
      if (this.protocolIdentifier == null) {
         this.protocolIdentifier(var1 ? "http://schemas.xmlsoap.org/ws/2004/10/wsat/Durable2PC" : "http://schemas.xmlsoap.org/ws/2004/10/wsat/Volatile2PC");
      }

      return this;
   }

   protected BaseRegisterType newRegistrationRequest() {
      return XmlTypeAdapter.newRegisterType();
   }

   protected String getDefaultParticipantAddress() {
      return WSATHelper.V10.getParticipantAddress();
   }

   protected BaseRegisterResponseType buildRegistrationResponse() {
      return XmlTypeAdapter.newRegisterResponseType();
   }

   protected EndpointReferenceBuilder getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.MemberSubmission();
   }
}
