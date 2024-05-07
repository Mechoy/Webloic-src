package weblogic.wsee.wstx.wsc.common.client;

import javax.xml.ws.EndpointReference;
import org.w3c.dom.Element;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;
import weblogic.wsee.wstx.wsc.common.WSCUtil;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterType;

public abstract class RegistrationMessageBuilder {
   protected boolean durable = true;
   protected Element txIdElement;
   protected Element routingElement;
   protected String participantAddress;
   protected String protocolIdentifier;

   public RegistrationMessageBuilder durable(boolean var1) {
      this.durable = var1;
      return this;
   }

   public RegistrationMessageBuilder txId(String var1) {
      this.txIdElement = WSCUtil.referenceElementTxId(var1);
      return this;
   }

   public RegistrationMessageBuilder routing() {
      this.routingElement = WSCUtil.referenceElementRoutingInfo();
      return this;
   }

   public RegistrationMessageBuilder participantAddress(String var1) {
      this.participantAddress = var1;
      return this;
   }

   public RegistrationMessageBuilder protocolIdentifier(String var1) {
      this.protocolIdentifier = var1;
      return this;
   }

   public BaseRegisterType build() {
      if (this.participantAddress == null) {
         this.participantAddress = this.getDefaultParticipantAddress();
      }

      BaseRegisterType var1 = this.newRegistrationRequest();
      var1.setParticipantProtocolService(this.getParticipantProtocolService());
      var1.setProtocolIdentifier(this.protocolIdentifier);
      return var1;
   }

   protected EndpointReference getParticipantProtocolService() {
      EndpointReferenceBuilder var1 = this.getEndpointReferenceBuilder();
      return var1.address(this.participantAddress).referenceParameter(this.txIdElement).referenceParameter(this.routingElement).build();
   }

   protected abstract BaseRegisterType newRegistrationRequest();

   protected abstract String getDefaultParticipantAddress();

   protected abstract BaseRegisterResponseType buildRegistrationResponse();

   protected abstract EndpointReferenceBuilder getEndpointReferenceBuilder();
}
