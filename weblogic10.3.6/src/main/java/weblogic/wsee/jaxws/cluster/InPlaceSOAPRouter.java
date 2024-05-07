package weblogic.wsee.jaxws.cluster;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.fastinfoset.FastInfosetFeature;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Codec;
import com.sun.xml.ws.api.pipe.Pipe;
import com.sun.xml.ws.api.pipe.helper.PipeAdapter;
import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.transport.http.WSHTTPConnection;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.cluster.spi.AffinityBasedRoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutableIDMapServiceRegistry;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.ServerNameMapService;
import weblogic.wsee.runtime.WebServicesRuntime;

public class InPlaceSOAPRouter extends BaseSOAPRouter<String, BaseSOAPRouter.BaseRoutables> {
   private static final Logger LOGGER = Logger.getLogger(InPlaceSOAPRouter.class.getName());
   private static final AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final RuntimeAccess _runtimeAccess;
   private WSBinding _binding;
   private WSEndpoint _endpoint;
   private MessageDigest _sha;
   private final BASE64Encoder _base64 = new BASE64Encoder();
   private Pipe _transportPipe;
   private static long _lastStoreToServerListDeliveryTime;

   public InPlaceSOAPRouter(WSBinding var1, WSEndpoint var2) {
      this._binding = var1;
      this._endpoint = var2;
      FastInfosetFeature var4 = new FastInfosetFeature(false);

      Codec var3;
      try {
         ((BindingImpl)var1).addFeature(var4);
         var3 = ((BindingImpl)var1).createCodec();
      } finally {
         var4 = new FastInfosetFeature(true);
         ((BindingImpl)var1).addFeature(var4);
      }

      this._transportPipe = PipeAdapter.adapt((new ClientTubeAssemblerContext((EndpointAddress)null, (WSDLPort)null, (WSService)null, this._binding, (Container)null, var3)).createTransportTube());
      this._binding = var1;
      this._endpoint = var2;

      try {
         this._sha = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException var11) {
         LOGGER.warning("MessageDigest error on getting SHA-256, changing to SHA-1 instead...");

         try {
            this._sha = MessageDigest.getInstance("SHA-1");
         } catch (Exception var10) {
            WseeCoreLogger.logUnexpectedException(var10.toString(), var10);
         }
      }

   }

   protected String getMessageId(Message var1) {
      return this._binding.getAddressingVersion() != null ? var1.getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion()) : super.getMessageId(var1);
   }

   protected String getRelatesTo(Message var1) {
      return this._binding.getAddressingVersion() != null ? var1.getHeaders().getRelatesTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion()) : super.getRelatesTo(var1);
   }

   public BaseSOAPRouter.BaseRoutables route(BaseSOAPRouter.BaseRoutables var1) throws Exception {
      Map var2 = this.getOutboundHttpHeaders(var1.packet);
      BaseSOAPRouter.BaseRoutables var3 = super.route(var1);
      Packet var4 = var3 != null ? var3.packet : null;
      if (var4 == null) {
         var4 = var1.packet;
      }

      if (var4 != null) {
         if (var4.outboundHttpHeaders == null) {
            var4.outboundHttpHeaders = var2;
         } else {
            var4.outboundHttpHeaders.putAll(var2);
         }
      }

      return var3;
   }

   protected String getTargetServerForRouting(@NotNull RoutingInfo var1) throws DeliveryException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(this.getClass().getSimpleName() + " getting target server name for routing info: " + var1);
      }

      String var2 = null;
      switch (var1.getType()) {
         case ABSTAIN:
            return null;
         case PHYSICAL_STORE_NAME:
            var2 = WebServicesRuntime.getInstance().getServerNameForPhysicalStore(var1.getName());
            if (var2 == null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Couldn't find server hosting persistent store '" + var1.getName() + "'");
               }

               throw new DeliveryException("Couldn't find server hosting persistent store: " + var1.getName());
            }
            break;
         case SERVER_NAME:
            var2 = var1.getName();
      }

      if (_runtimeAccess.getServerName().equals(var2)) {
         var2 = null;
      }

      return var2;
   }

   protected RoutingInfo getRoutingForTargetServer(String var1) {
      return new RoutingInfo(var1, RoutingInfo.Type.SERVER_NAME);
   }

   protected BaseSOAPRouter.BaseRoutables deliverMessageToTargetServer(BaseSOAPRouter.BaseRoutables var1, String var2) throws Exception {
      String var3 = "";
      if (this._binding.getAddressingVersion() != null) {
         var3 = "ID: " + var1.packet.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("ClusterRoutingTubeUtils **ROUTING** inbound message " + var3 + " to server " + var2);
      }

      String var4 = null;

      try {
         var4 = var1.packet.webServiceContextDelegate.getEPRAddress(var1.packet, this._endpoint);
      } catch (Exception var11) {
         Set var6 = this._endpoint.getBoundEndpoints();
         if (!var6.isEmpty()) {
            BoundEndpoint var7 = (BoundEndpoint)var6.iterator().next();
            URI var8 = var7.getAddress();
            var8 = new URI("http", var8.getUserInfo(), var8.getHost(), var8.getPort(), var8.getPath(), var8.getQuery(), var8.getFragment());
            var4 = var8.toString();
         }

         if (var4 == null) {
            throw new IllegalStateException("Couldn't determine the endpoint address for this endpoint");
         }
      }

      URL var5 = new URL(var4);
      Protocol var12 = ProtocolManager.findProtocol(var5.getProtocol());
      String var13;
      if (var12.isSecure() && this.isSslTerminationInUse(var1.packet)) {
         var13 = var12.getProtocolName();
         var13 = var13.substring(0, var13.length() - 1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("InPlaceSOAPRouter *downgrading* protocol from " + var12 + " to " + var13);
         }

         var12 = ProtocolManager.findProtocol(var13);
      }

      var13 = URLManager.findURL(var2, var12);
      URL var14 = new URL(var13);
      URL var9 = new URL(var14.getProtocol(), var14.getHost(), var14.getPort(), var5.getFile());
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(this.getClass().getSimpleName() + " **ROUTING** inbound message " + var3 + " to server " + var2 + " Target URL: " + var9.toExternalForm());
      }

      var1.packet.endpointAddress = new EndpointAddress(var9.toExternalForm());
      this.copySSLHeadersForForwarding(var1.packet);
      Packet var10 = this._transportPipe.process(var1.packet);
      return new BaseSOAPRouter.BaseRoutables(var10);
   }

   private boolean isSslTerminationInUse(Packet var1) {
      if (var1.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var2 = (HttpServletRequest)var1.get("javax.xml.ws.servlet.request");
         String var3 = var2.getHeader("WL-Proxy-SSL");
         return Boolean.valueOf(var3);
      } else {
         return false;
      }
   }

   private void copySSLHeadersForForwarding(Packet var1) {
      if (var1.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var2 = (HttpServletRequest)var1.get("javax.xml.ws.servlet.request");
         this.copyHeaderForForwarding("WL-Proxy-SSL", var2, var1);
         this.copyHeaderForForwarding("WL-Proxy-Client-Cert", var2, var1);
         this.copyHeaderForForwarding("WL-Proxy-Client-IP", var2, var1);
      }

   }

   private void copyHeaderForForwarding(String var1, HttpServletRequest var2, Packet var3) {
      Object var4 = (Map)var3.invocationProperties.get("javax.xml.ws.http.request.headers");
      if (var4 == null) {
         var4 = new HashMap();
         var3.invocationProperties.put("javax.xml.ws.http.request.headers", var4);
      }

      String var5 = var2.getHeader(var1);
      if (var5 != null) {
         ((Map)var4).put(var1, Arrays.asList(var5));
      }

   }

   protected BaseSOAPRouter.BaseRoutables setAbstainedFinders(BaseSOAPRouter.BaseRoutables var1, Map<AffinityBasedRoutingInfoFinder, RoutingInfo> var2) {
      return null;
   }

   private Map<String, List<String>> getOutboundHttpHeaders(Packet var1) {
      HashMap var2 = new HashMap();
      WSHTTPConnection var3 = (WSHTTPConnection)var1.getSatellite(WSHTTPConnection.class);
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      if (var3 != null) {
         var4 = RoutableIDMapServiceRegistry.getInstance().hasNewInfoSince(_lastStoreToServerListDeliveryTime);
         var5 = var3.getRequestHeader("X-weblogic-wsee-request-storetoserver-list") != null;
         var6 = var3.getRequestHeader("X-weblogic-wsee-storetoserver-accepted") != null;
      }

      boolean var7 = var3 != null && (var5 || var6 && var4);
      if (var7) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("InPlaceSOAPRouter placing output HTTP headers with Store->Server Map info. ListRequested=" + var5 + " ListAccepted=" + var6 + " NewListInfo=" + var4);
         }

         _lastStoreToServerListDeliveryTime = System.currentTimeMillis();
         ArrayList var8 = new ArrayList();
         Map var9 = RoutableIDMapServiceRegistry.getInstance().getCurrentRoutableIDToServerMap();
         if (var9.size() > 0) {
            StringBuffer var10 = new StringBuffer();
            Iterator var11 = var9.keySet().iterator();

            String var12;
            while(var11.hasNext()) {
               var12 = (String)var11.next();
               var10.append(var12);
               var10.append("/");
               String var13 = (String)var9.get(var12);
               ServerNameMapService.ServerAddress var14 = RoutableIDMapServiceRegistry.getInstance().getServerAddress(var13);
               String var15;
               if (var14 != null) {
                  var15 = var13 + ":" + var14.host + ":" + var14.port + ":" + var14.sslPort;
               } else {
                  var15 = var13;
               }

               var10.append(var15);
               var10.append("|");
            }

            if (var10.charAt(var10.length() - 1) == '|') {
               var10.setLength(var10.length() - 1);
            }

            String var16 = var10.toString();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Adding StoreToServerList string to outbound HTTP headers: " + var16);
            }

            var8.add(var16);
            var12 = this.calculateHash(var16);
            ArrayList var17 = new ArrayList();
            var17.add(var12);
            var2.put("X-weblogic-wsee-storetoserver-list", var8);
            var2.put("X-weblogic-wsee-storetoserver-hash", var17);
            if (var1.outboundHttpHeaders == null) {
               var1.outboundHttpHeaders = var2;
            } else {
               var1.outboundHttpHeaders.putAll(var2);
            }
         }
      }

      return var2;
   }

   private String calculateHash(String var1) {
      this._sha.reset();
      byte[] var2 = this._sha.digest(var1.getBytes());
      String var3 = this._base64.encodeBuffer(var2);
      return var3.substring(0, var3.length() - 1);
   }

   static {
      _runtimeAccess = ManagementService.getRuntimeAccess(_kernelId);
      _lastStoreToServerListDeliveryTime = 0L;
   }
}
