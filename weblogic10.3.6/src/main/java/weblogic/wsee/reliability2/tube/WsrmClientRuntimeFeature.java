package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature.Type;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.wsee.jaxws.client.async.AsyncClientHandlerMarkerFeature;
import weblogic.wsee.jaxws.persistence.ClientInstanceProperties;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.reliability2.api.WsrmClientInitFeature;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.ws.WsPort;

public class WsrmClientRuntimeFeature extends WsrmClientInitFeature {
   private static final Logger LOGGER = Logger.getLogger(WsrmClientRuntimeFeature.class.getName());
   private static String ID = "WS-RM Client Feature";
   private String _clientId;
   private ClientTubeAssemblerContext _context;

   public WsrmClientRuntimeFeature(String var1, ClientTubeAssemblerContext var2, WsrmClientInitFeature var3) {
      super(var3);
      this._clientId = var1;
      this._context = var2;
   }

   public WsrmClientRuntimeFeature(String var1, ClientTubeAssemblerContext var2) {
      super(true);
      this._clientId = var1;
      this._context = var2;
   }

   public String getID() {
      return ID;
   }

   ClientTubeAssemblerContext getContext() {
      return this._context;
   }

   public String getClientId() {
      return this._clientId;
   }

   public void setClientId(String var1) {
      this._clientId = var1;
   }

   public String loadClientCurrentSequenceId(@NotNull ClientInstance var1) {
      if (var1.getProps() == null) {
         return null;
      } else {
         synchronized(var1.getProps()) {
            return (String)var1.getProps().get("weblogic.wsee.reliability2.client.CurrentSequenceID");
         }
      }
   }

   public void storeClientCurrentSequenceId(@NotNull ClientInstance var1, String var2) {
      if (var1.getId() != null) {
         ClientIdentityFeature var3 = ClientIdentityRegistry.getClientIdentityFeature(var1.getId().getClientId());
         if (var3 != null && var3.getType() == Type.CUSTOMER) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Storing current sequence " + var2 + " into client props for client instance: " + var1.getId());
            }

            Map var4 = var1.getProps();
            synchronized(var4) {
               var4.put("weblogic.wsee.reliability2.client.CurrentSequenceID", var2);
            }

            var1.saveProps();
         }

      }
   }

   public void removeClientCurrentSequenceId(ClientInstanceIdentity var1) {
      if (var1 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Removing client-side knowledge of current sequence ID for client instance: " + var1);
         }

         PersistenceConfig.Client var2 = PersistenceConfig.getClientConfig(this._context);
         ClientInstanceProperties var3 = (ClientInstanceProperties)ClientInstanceProperties.getStoreMap(var2.getLogicalStoreName()).get(var1);
         if (var3 == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Client didn't know about a current sequence. Client was: " + var1);
            }

            return;
         }

         Map var4 = var3.getPropertyMap();
         synchronized(var4) {
            String var6 = (String)var4.remove("weblogic.wsee.reliability2.client.CurrentSequenceID");
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Client has stored seq ID " + var6 + ". Client was: " + var1);
            }

            if (var4.isEmpty()) {
               ClientInstanceProperties.getStoreMap(var2.getLogicalStoreName()).remove(var1);
            } else {
               ClientInstanceProperties.getStoreMap(var2.getLogicalStoreName()).put(var1, var3);
            }
         }
      }

   }

   public void setNonBufferedAfterInit(boolean var1) throws WsrmException {
      WSBinding var2 = this.getBinding();
      if (!var1 && !var2.isFeatureEnabled(AsyncClientHandlerFeature.class) && !var2.isFeatureEnabled(AsyncClientHandlerMarkerFeature.class)) {
         throw new WsrmException("Cannot enable RM buffering without first adding AsyncClientHandlerFeature to Port/Dispatch");
      } else {
         this.setNonBufferedSource(var1);
      }
   }

   WSService getService() {
      return this._context.getService();
   }

   public WSBinding getBinding() {
      return this._context.getBinding();
   }

   public WSDLPort getWsdlPort() {
      return this._context.getWsdlModel();
   }

   @Nullable
   public WsPort getPort() {
      return WsrmTubelineDeploymentListener.getWsPort(this._context);
   }

   public QName getPortName() {
      WsPort var1 = this.getPort();
      return var1 != null ? var1.getWsdlPort().getName() : null;
   }

   public static WsrmClientRuntimeFeature getFromBinding(WSBinding var0) {
      WsrmClientRuntimeFeature var1 = (WsrmClientRuntimeFeature)var0.getFeature(WsrmClientRuntimeFeature.class);
      return var1;
   }
}
