package weblogic.wsee.reliability2.store;

import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.reliability2.api.ReliabilityErrorListener;
import weblogic.wsee.reliability2.api.WsrmClientInitFeature;
import weblogic.wsee.reliability2.tube.DispatchFactoryNotReadyException;
import weblogic.wsee.reliability2.tube.DispatchFactoryResolver;

public abstract class SenderDispatchFactory {
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
         if (var2 == null) {
            throw new DispatchFactoryNotReadyException("No client with client identity '" + this._id + "' has been registered yet");
         } else if (!var2.isInitialized()) {
            throw new DispatchFactoryNotReadyException("Client identity '" + this._id + "' has been registered, but is not yet fully initialized");
         } else {
            WsrmClientInitFeature var3 = (WsrmClientInitFeature)var2.getOriginalConfig().getFeatures().get(WsrmClientInitFeature.class);
            return var3 == null ? null : var3.getErrorListener();
         }
      }
   }

   public interface Key extends DispatchFactoryResolver.Key {
      ReliabilityErrorListener findErrorListener();
   }
}
