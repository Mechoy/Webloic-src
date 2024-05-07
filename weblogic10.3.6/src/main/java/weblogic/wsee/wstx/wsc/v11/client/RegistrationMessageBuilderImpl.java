package weblogic.wsee.wstx.wsc.v11.client;

import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationMessageBuilder;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;
import weblogic.wsee.wstx.wsc.v11.XmlTypeAdapter;

public class RegistrationMessageBuilderImpl extends RegistrationMessageBuilder {
   public RegistrationMessageBuilder durable(boolean var1) {
      super.durable(var1);
      if (this.protocolIdentifier == null) {
         this.protocolIdentifier(var1 ? "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Durable2PC" : "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Volatile2PC");
      }

      return this;
   }

   protected BaseRegisterType newRegistrationRequest() {
      return XmlTypeAdapter.newRegisterType();
   }

   protected String getDefaultParticipantAddress() {
      return WSATHelper.V11.getParticipantAddress();
   }

   protected BaseRegisterResponseType buildRegistrationResponse() {
      return XmlTypeAdapter.newRegisterResponseType();
   }

   protected EndpointReferenceBuilder getEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.W3C();
   }
}
