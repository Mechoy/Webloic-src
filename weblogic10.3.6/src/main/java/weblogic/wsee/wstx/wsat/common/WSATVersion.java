package weblogic.wsee.wstx.wsat.common;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.v10.types.Notification;
import weblogic.wsee.wstx.wsc.common.EndpointReferenceBuilder;

public abstract class WSATVersion<T> {
   public static final WSATVersion<Notification> v10 = new WSATVersion10();
   public static final WSATVersion<weblogic.wsee.wstx.wsat.v11.types.Notification> v11 = new WSATVersion11();
   private Transactional.Version version;
   protected AddressingVersion addressingVersion;
   protected SOAPVersion soapVersion;

   public static WSATVersion getInstance(Transactional.Version var0) {
      if (Version.WSAT10 != var0 && Version.DEFAULT != var0) {
         if (Version.WSAT11 != var0 && Version.WSAT12 != var0) {
            throw new IllegalArgumentException(var0 + "is not a supported ws-at version");
         } else {
            return v11;
         }
      } else {
         return v10;
      }
   }

   WSATVersion(Transactional.Version var1) {
      this.version = var1;
   }

   public abstract WSATHelper getWSATHelper();

   public AddressingVersion getAddressingVersion() {
      return this.addressingVersion;
   }

   public SOAPVersion getSOPAVersion() {
      return this.soapVersion;
   }

   public Transactional.Version getVersion() {
      return this.version;
   }

   public abstract CoordinatorProxyBuilder<T> newCoordinatorProxyBuilder();

   public abstract ParticipantProxyBuilder<T> newParticipantProxyBuilder();

   public abstract NotificationBuilder<T> newNotificationBuilder();

   public abstract EndpointReferenceBuilder newEndpointReferenceBuilder();

   public abstract WebServiceFeature newAddressingFeature();
}
