package weblogic.wsee.wstx.wsat.v11.client;

import javax.xml.ws.WebServiceException;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.v11.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v11.types.Notification;

public class CoordinatorProxyBuilderImpl extends CoordinatorProxyBuilder<Notification> {
   public CoordinatorProxyBuilderImpl() {
      super(WSATVersion.v11);
   }

   public CoordinatorIF<Notification> build() {
      return new CoordinatorProxyImpl();
   }

   class CoordinatorProxyImpl implements CoordinatorIF<Notification> {
      CoordinatorPortType port;
      WSAT11Service service = new WSAT11Service();

      CoordinatorProxyImpl() {
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(this.service.getWSDLDocumentLocation(), this.service.getServiceName(), WSAT11Service.class);
         this.port = (CoordinatorPortType)var3.getPort(CoordinatorProxyBuilderImpl.this.to, CoordinatorPortType.class, CoordinatorProxyBuilderImpl.this.getEnabledFeatures());
      }

      public void preparedOperation(Notification var1) {
         this.port.preparedOperation(var1);
      }

      public void abortedOperation(Notification var1) {
         this.port.abortedOperation(var1);
      }

      public void readOnlyOperation(Notification var1) {
         this.port.readOnlyOperation(var1);
      }

      public void committedOperation(Notification var1) {
         this.port.committedOperation(var1);
      }

      public void replayOperation(Notification var1) {
         throw new WebServiceException("replayOperation is not supported by WS-AT 1.1 and 1.2");
      }
   }
}
