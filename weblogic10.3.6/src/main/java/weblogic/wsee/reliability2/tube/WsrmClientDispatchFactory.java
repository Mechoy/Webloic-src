package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.client.WSServiceDelegate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import weblogic.wsee.jaxws.spi.AsyncHandlerAllowedInternalFeature;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;

public class WsrmClientDispatchFactory implements DispatchFactory {
   private static final Logger LOGGER = Logger.getLogger(WsrmClientDispatchFactory.class.getName());
   private String _clientId;

   public WsrmClientDispatchFactory(String var1) {
      this._clientId = var1;
   }

   public <T> Dispatch<T> createDispatch(@NotNull WSEndpointReference var1, @NotNull Class<T> var2) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Creating equivalent dispatch for client identity: " + this._clientId);
      }

      ClientIdentityRegistry.ClientInfo var3 = ClientIdentityRegistry.getRequiredClientInfo(this._clientId);
      if (!var3.isInitialized()) {
         throw new WsrmException("DispatchFactory cannot be used until client identity '" + this._clientId + "' has been initialized");
      } else {
         Service.Mode var5 = Mode.MESSAGE;
         WSServiceDelegate var6 = var3.getOriginalConfig().getService();
         Dispatch var4 = ClientIdentityRegistry.getEquivalentDispatch(this._clientId, var2, var5, var6, var1, new AsyncHandlerAllowedInternalFeature());
         return var4;
      }
   }
}
