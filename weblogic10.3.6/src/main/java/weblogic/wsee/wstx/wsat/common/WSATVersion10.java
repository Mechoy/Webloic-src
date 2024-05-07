package weblogic.wsee.wstx.wsat.common;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.v10.NotificationBuilderImpl;
import weblogic.wsee.wstx.wsat.v10.client.CoordinatorProxyBuilderImpl;
import weblogic.wsee.wstx.wsat.v10.client.ParticipantProxyBuilderImpl;
import weblogic.wsee.wstx.wsat.v10.types.Notification;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;

class WSATVersion10 extends WSATVersion<Notification> {
   WSATVersion10() {
      super(Version.WSAT10);
      this.addressingVersion = AddressingVersion.MEMBER;
      this.soapVersion = SOAPVersion.SOAP_11;
   }

   public WSATHelper getWSATHelper() {
      return WSATHelper.V10;
   }

   public CoordinatorProxyBuilder<Notification> newCoordinatorProxyBuilder() {
      return new CoordinatorProxyBuilderImpl();
   }

   public ParticipantProxyBuilder<Notification> newParticipantProxyBuilder() {
      return new ParticipantProxyBuilderImpl();
   }

   public EndpointReferenceBuilder newEndpointReferenceBuilder() {
      return EndpointReferenceBuilder.MemberSubmission();
   }

   public WebServiceFeature newAddressingFeature() {
      return new MemberSubmissionAddressingFeature();
   }

   public NotificationBuilder newNotificationBuilder() {
      return new NotificationBuilderImpl();
   }
}
