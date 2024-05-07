package weblogic.wsee.reliability2.saf;

import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.reliability2.api.ReliabilityErrorListener;
import weblogic.wsee.reliability2.api.WsrmClientInitFeature;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.DispatchFactoryResolver;

public abstract class WsrmSAFDispatchFactory {
   public static class ServerSideKey extends DispatchFactoryResolver.ServerSideKey implements Key {
      private static final long serialVersionUID = 1L;

      public ServerSideKey() {
      }

      public ServerSideKey(String var1) {
         super(var1);
      }

      public ReliabilityErrorListener findErrorListener() {
         return null;
      }
   }

   public static class ClientSideKey extends DispatchFactoryResolver.ClientSideKey implements Key {
      private static final long serialVersionUID = 1L;

      public ClientSideKey() {
      }

      public ClientSideKey(String var1) {
         super(var1);
      }

      public ReliabilityErrorListener findErrorListener() {
         String var1 = (String)this._id;
         ClientIdentityRegistry.ClientInfo var2 = ClientIdentityRegistry.getRequiredClientInfo(var1);
         WsrmClientInitFeature var3 = (WsrmClientInitFeature)var2.getOriginalConfig().getFeatures().get(WsrmClientInitFeature.class);
         if (var3 == null) {
            throw new DispatchFactoryNotReadyException("No WsrmClientInitFeature found for client with id: " + var1);
         } else {
            return var3.getErrorListener();
         }
      }
   }

   public interface Key extends DispatchFactoryResolver.Key {
      ReliabilityErrorListener findErrorListener();
   }
}
