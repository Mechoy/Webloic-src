package weblogic.wsee.wstx.wsat.v10.client;

import java.io.Closeable;
import java.io.IOException;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.v10.types.CoordinatorPortType;
import weblogic.wsee.wstx.wsat.v10.types.Notification;

public class CoordinatorProxyBuilderImpl extends CoordinatorProxyBuilder<Notification> {
   private static final WSAT10Service service = new WSAT10Service();

   public CoordinatorProxyBuilderImpl() {
      super(WSATVersion.v10);
   }

   public CoordinatorIF<Notification> build() {
      return new CoordinatorProxyImpl();
   }

   class CoordinatorProxyImpl implements CoordinatorIF<Notification> {
      CoordinatorPortType port;

      CoordinatorProxyImpl() {
         WLSProvider var2 = WLSProvider.getInstance();
         WLSProvider.ServiceDelegate var3 = var2.createServiceDelegate(CoordinatorProxyBuilderImpl.service.getWSDLDocumentLocation(), CoordinatorProxyBuilderImpl.service.getServiceName(), WSAT10Service.class);
         this.port = (CoordinatorPortType)var3.getPort(CoordinatorProxyBuilderImpl.this.to, CoordinatorPortType.class, CoordinatorProxyBuilderImpl.this.getEnabledFeatures());
      }

      public void preparedOperation(Notification var1) {
         this.port.preparedOperation(var1);
         this.closePort();
      }

      public void abortedOperation(Notification var1) {
         this.port.abortedOperation(var1);
         this.closePort();
      }

      public void readOnlyOperation(Notification var1) {
         this.port.readOnlyOperation(var1);
         this.closePort();
      }

      public void committedOperation(Notification var1) {
         this.port.committedOperation(var1);
         this.closePort();
      }

      public void replayOperation(Notification var1) {
         this.port.replayOperation(var1);
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
