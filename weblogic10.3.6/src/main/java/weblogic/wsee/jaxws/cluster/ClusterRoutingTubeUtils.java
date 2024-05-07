package weblogic.wsee.jaxws.cluster;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.jaxws.tubeline.standard.WseeWsaPropertyBag;
import weblogic.wsee.monitoring.WseeClusterRoutingRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseePortRuntimeMBeanImpl;
import weblogic.wsee.reliability2.compat.SOAPActionHeader;

public class ClusterRoutingTubeUtils {
   private static final Logger LOGGER = Logger.getLogger(ClusterRoutingTubeUtils.class.getName());
   private WSBinding _binding;
   private WSEndpoint _endpoint;
   @Nullable
   private InPlaceSOAPRouter _router;

   protected ClusterRoutingTubeUtils(WSBinding var1, WSEndpoint var2) {
      this._binding = var1;
      this._router = null;
      this.setEndpoint(var2);
   }

   void setEndpoint(WSEndpoint var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Set endpoint " + var1 + " in ClusterRoutingTubeUtils " + this);
      }

      this._endpoint = var1;
   }

   private void ensureSOAPRouter() {
      if (this._router == null) {
         if (this._endpoint == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Warning!, ClusterRoutingTubeUtils " + this + " having to search for endpoint");
            }

            AsyncClientTransportFeature var1 = (AsyncClientTransportFeature)this._binding.getFeature(AsyncClientTransportFeature.class);
            if (var1 != null) {
               this._endpoint = ((WLSEndpoint)var1.getEndpoint()).getWSEndpoint();
            }
         }

         WseeClusterRoutingRuntimeMBeanImpl var2 = getClusterRoutingRuntimeFromEndpoint(this._endpoint);
         if (var2 == null) {
            throw new IllegalStateException("ClusterRoutingTubeUtils " + this + " not ready, no WseeClusterRoutingRuntimeMBean/WseeRuntimeMBean");
         }

         this._router = new InPlaceSOAPRouter(this._binding, this._endpoint);
         this._router.init(var2);
      }

   }

   public Packet handleInboundMessage(Packet var1) throws Exception {
      if (this._endpoint.getSPI(WseeV2RuntimeMBean.class) != null) {
         this.ensureSOAPRouter();
      }

      if (var1.getMessage() != null && this._router != null) {
         BaseSOAPRouter.BaseRoutables var2 = new BaseSOAPRouter.BaseRoutables(var1);
         BaseSOAPRouter.BaseRoutables var3 = this._router.route(var2);
         return var3 != null ? var3.packet : null;
      } else {
         return null;
      }
   }

   public void processOutboundMessage(Packet var1) {
      if (var1.outboundHttpHeaders != null && var1.outboundHttpHeaders.containsKey("X-weblogic-wsee-storetoserver-list")) {
         WseeWsaPropertyBag var2 = (WseeWsaPropertyBag)WseeWsaPropertyBag.propertySetRetriever.getFromPacket(var1);
         boolean var3 = var2.getResponsePacket() == null && var1.getMessage() == null;
         if (!var3 && this._binding.getAddressingVersion() != null) {
            String var4 = var1.getMessage().getHeaders().getTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            WSEndpointReference var5 = var4 != null ? new WSEndpointReference(var4, this._binding.getAddressingVersion()) : null;
            if (var5 == null && var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest")) {
               var5 = (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest");
            }

            if (var5 == null && var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest")) {
               var5 = (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest");
            }

            var3 = var5 != null && !var5.isAnonymous();
         }

         if (var3) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Forcing a secondary response onto the response packet because we have to deliver a Store-to-Server Map back to the front-end router");
            }

            try {
               Packet var10 = new Packet();
               SOAPMessage var11 = MessageFactory.newInstance("SOAP 1.1 Protocol").createMessage();
               Message var6 = Messages.create(var11);
               var10.setMessage(var6);
               String var7 = "http://www.oracle.com/wsee/jaxws/cluster/SecondaryResponse";
               if (this._binding != null && this._binding.getAddressingVersion() != null) {
                  SOAPActionHeader var8 = new SOAPActionHeader(this._binding.getAddressingVersion().actionTag, var7, this._binding.getSOAPVersion());
                  var8.setMustUnderstand(true);
                  var6.getHeaders().addOrReplace(var8);
               }

               var10.soapAction = var7;
               var10.outboundHttpHeaders = new HashMap(var1.outboundHttpHeaders);
               ArrayList var12 = new ArrayList();
               var12.add("true");
               var10.outboundHttpHeaders.put("X-weblogic-wsee-ignore-this-response", var12);
               var2.setResponsePacket(var10);
               var10.transportBackChannel = var1.keepTransportBackChannelOpen();
            } catch (Exception var9) {
               if (LOGGER.isLoggable(Level.WARNING)) {
                  LOGGER.warning("While forcing a secondary response onto the response packet: " + var9.toString());
               }

               WseeCoreLogger.logUnexpectedException(var9.toString(), var9);
            }
         }
      }

   }

   @Nullable
   private static WseeClusterRoutingRuntimeMBeanImpl getClusterRoutingRuntimeFromEndpoint(@Nullable WSEndpoint var0) {
      WseePortRuntimeMBean var1 = WseePortRuntimeMBeanImpl.getFromEndpoint(var0);
      WseeClusterRoutingRuntimeMBeanImpl var2 = null;

      try {
         if (var1 != null) {
            var2 = (WseeClusterRoutingRuntimeMBeanImpl)var1.getClusterRouting();
         }
      } catch (Exception var4) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var4.toString(), var4);
         }

         WseeCoreLogger.logUnexpectedException(var4.toString(), var4);
      }

      return var2;
   }
}
