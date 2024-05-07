package weblogic.wsee.wstx.wsat.common;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.v11.NotificationBuilderImpl;
import weblogic.wsee.wstx.wsat.v11.client.CoordinatorProxyBuilderImpl;
import weblogic.wsee.wstx.wsat.v11.client.ParticipantProxyBuilderImpl;
import weblogic.wsee.wstx.wsat.v11.types.Notification;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;

class WSATVersion11 extends WSATVersion<Notification> {
   WSATVersion11() {
      super(Version.WSAT11);
      this.addressingVersion = AddressingVersion.W3C;
      this.soapVersion = SOAPVersion.SOAP_11;
   }

   public WSATHelper getWSATHelper() {
      return WSATHelper.V11;
   }

   public CoordinatorProxyBuilder<Notification> newCoordinatorProxyBuilder() {
      return new CoordinatorProxyBuilderImpl();
   }

   public ParticipantProxyBuilder<Notification> newParticipantProxyBuilder() {
      return new ParticipantProxyBuilderImpl();
   }

   public NotificationBuilder<Notification> newNotificationBuilder() {
      return new NotificationBuilderImpl();
   }

   public EndpointReferenceBuilder newEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.W3C();
   }

   public WebServiceFeature newAddressingFeature() {
      return new AddressingFeature();
   }
}
