package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.client.WSServiceDelegate;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.mc.exception.McException;

public class McInitiatorDispatchFactory implements McDispatchFactory {
   private String _clientId;

   public McInitiatorDispatchFactory(String var1) {
      this._clientId = var1;
   }

   public <T> Dispatch<T> createDispatch(@NotNull WSEndpointReference var1, @NotNull Class<T> var2) throws Exception {
      ClientIdentityRegistry.ClientInfo var3 = ClientIdentityRegistry.getRequiredClientInfo(this._clientId);
      if (!var3.isInitialized()) {
         throw new McException("DispatchFactory cannot be used until client identity " + this._clientId + " has been initialized");
      } else {
         Service.Mode var4 = Mode.MESSAGE;
         WSServiceDelegate var5 = var3.getOriginalConfig().getService();
         return ClientIdentityRegistry.getEquivalentDispatch(this._clientId, var2, var4, var5, var1);
      }
   }
}
