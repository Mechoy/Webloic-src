package weblogic.wsee.wstx.wsat.v11.client;

import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.common.ParticipantIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.v11.types.Notification;
import weblogic.wsee.wstx.wsat.v11.types.ParticipantPortType;

public class ParticipantProxyBuilderImpl extends ParticipantProxyBuilder<Notification> {
   public ParticipantProxyBuilderImpl() {
      super(WSATVersion.v11);
   }

   public ParticipantIF<Notification> build() {
      return new ParticipantProxyImpl();
   }

   class ParticipantProxyImpl implements ParticipantIF<Notification> {
      ParticipantPortType port;
      WSAT11Service service = new WSAT11Service();

      ParticipantProxyImpl() {
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(this.service.getWSDLDocumentLocation(), this.service.getServiceName(), WSAT11Service.class);
         this.port = (ParticipantPortType)var3.getPort(ParticipantProxyBuilderImpl.this.to, ParticipantPortType.class, ParticipantProxyBuilderImpl.this.getEnabledFeatures());
      }

      public void prepare(Notification var1) {
         this.port.prepareOperation(var1);
      }

      public void commit(Notification var1) {
         this.port.commitOperation(var1);
      }

      public void rollback(Notification var1) {
         this.port.rollbackOperation(var1);
      }
   }
}
