package weblogic.wsee.wstx.wsat.v10.client;

import java.io.Closeable;
import java.io.IOException;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.common.ParticipantIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.v10.types.Notification;
import weblogic.wsee.wstx.wsat.v10.types.ParticipantPortType;

public class ParticipantProxyBuilderImpl extends ParticipantProxyBuilder<Notification> {
   static final WSAT10Service service = new WSAT10Service();

   public ParticipantProxyBuilderImpl() {
      super(WSATVersion.v10);
   }

   public ParticipantIF<Notification> build() {
      return new ParticipantProxyImpl();
   }

   class ParticipantProxyImpl implements ParticipantIF<Notification> {
      ParticipantPortType port;

      ParticipantProxyImpl() {
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(ParticipantProxyBuilderImpl.service.getWSDLDocumentLocation(), ParticipantProxyBuilderImpl.service.getServiceName(), WSAT10Service.class);
         this.port = (ParticipantPortType)var3.getPort(ParticipantProxyBuilderImpl.this.to, ParticipantPortType.class, ParticipantProxyBuilderImpl.this.getEnabledFeatures());
      }

      public String toString() {
         return this.getClass().getName() + " hashcode:" + this.hashCode() + " to(EndpointReference):" + ParticipantProxyBuilderImpl.this.to + " port:" + this.port;
      }

      public void prepare(Notification var1) {
         this.port.prepare(var1);
      }

      public void commit(Notification var1) {
         this.port.commit(var1);
         this.closePort();
      }

      public void rollback(Notification var1) {
         this.port.rollback(var1);
         this.closePort();
      }

      private void closePort() {
         try {
            ((Closeable)this.port).close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }

      }
   }
}
