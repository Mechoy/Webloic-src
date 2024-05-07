package weblogic.wsee.jaxws.cluster;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.runtime.WebServicesRuntime;

public class ClusterInjectionTubeUtils {
   private static final Logger LOGGER = Logger.getLogger(ClusterInjectionTubeUtils.class.getName());
   private WSBinding _binding;
   private PersistenceConfig.Common _persistConfig;

   protected ClusterInjectionTubeUtils(WSBinding var1, PersistenceConfig.Common var2) {
      this._binding = var1;
      this._persistConfig = var2;
   }

   public Packet processOutboundMessage(Packet var1) {
      if (var1.getMessage() != null && this._binding.getAddressingVersion() != null) {
         String var2 = null;
         if (LOGGER.isLoggable(Level.FINE)) {
            var2 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            LOGGER.fine("ClusterInjectionTubeUtils outbound message ID: " + var2);
         }

         AddressingVersion var3 = this._binding.getAddressingVersion();
         SOAPVersion var4 = this._binding.getSOAPVersion();
         boolean var5 = false;
         String var6 = var1.getMessage().getHeaders().getRelatesTo(var3, var4);
         if (var6 == null) {
            WSEndpointReference var7 = var1.getMessage().getHeaders().getReplyTo(var3, var4);
            if (var7 != null && !var7.isAnonymous()) {
               var5 = true;
            }
         } else {
            var5 = true;
         }

         if (var5) {
            try {
               WebServiceLogicalStoreMBean var10 = this._persistConfig.getLogicalStoreMBean();
               boolean var8 = !var10.getPersistenceStrategy().equals("NETWORK_ACCESSIBLE");
               if (var8) {
                  this.injectRoutableMessageId(var1);
               }
            } catch (Exception var9) {
               if (LOGGER.isLoggable(Level.INFO)) {
                  LOGGER.info("Unable to inject routable message ID into outgoing message with ID '" + var2 + ": " + var9.toString());
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void injectRoutableMessageId(Packet var1) {
      if (var1.getMessage() != null && this._binding.getAddressingVersion() != null) {
         String var2 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         if (WsUtil.getStoreNameFromRoutableUUID(var2) == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClusterInjectionTubeUtils **INJECTING** message ID into message with old ID: " + var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion()));
            }

            List var3 = WebServicesRuntime.getInstance().getLocalPhysicalStoresForLogicalStore(this._persistConfig.getLogicalStoreName());
            String var4 = var3.size() > 0 ? (String)var3.get(0) : null;
            WsUtil.getOrSetMessageID(var1.getMessage(), this._binding.getAddressingVersion(), this._binding.getSOAPVersion(), var4);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClusterInjectionTubeUtils **INJECTED** message ID into message: " + var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion()));
            }

         }
      }
   }
}
