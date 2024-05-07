package weblogic.wsee.jaxws.cluster;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.jaxws.client.async.AsyncResponseEndpoint;
import weblogic.wsee.jaxws.client.async.AsyncTransportProviderPropertyBag;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.spi.WLSEndpoint;

public class ClusterRoutingClientTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(ClusterRoutingClientTube.class.getName());
   private static Map<String, AsyncResponseCompleteListener> _asyncResponseCompleteListenerMap = new HashMap();
   private ClusterRoutingTubeUtils _util;
   private ClientTubeAssemblerContext _context;
   private WSEndpoint _endpoint;

   public ClusterRoutingClientTube(Tube var1, ClientTubeAssemblerContext var2, WSEndpoint var3) {
      super(var1);
      this._context = var2;
      this._endpoint = var3;
      this.commonConstructorCode(var2, var3);
   }

   private void commonConstructorCode(ClientTubeAssemblerContext var1, WSEndpoint var2) {
      this._util = new ClusterRoutingTubeUtils(var1.getBinding(), var2);
   }

   public ClusterRoutingClientTube(ClusterRoutingClientTube var1, TubeCloner var2) {
      super(var1, var2);
      this._context = var1._context;
      this._endpoint = var1._endpoint;
      this.commonConstructorCode(this._context, this._endpoint);
   }

   void setEndpoint(WSEndpoint var1) {
      this._endpoint = var1;
      this._util.setEndpoint(var1);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new ClusterRoutingClientTube(this, var1);
   }

   public void preDestroy() {
      if (this._endpoint != null) {
         AsyncResponseCompleteListener var1 = (AsyncResponseCompleteListener)_asyncResponseCompleteListenerMap.remove(this._endpoint.getEndpointId());
         if (var1 != null && var1.getEndpoint() != null) {
            var1.getEndpoint().removeResponseProcessingCompletionListener(var1);
         }
      }

      if (this.next != null) {
         super.preDestroy();
      }

   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      return this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      boolean var2 = var1.getSatellite(AsyncTransportProviderPropertyBag.class) != null;
      return var2 ? this.processAsyncResponse(var1) : this.doReturnWith(var1);
   }

   private void ensureEndpoint() {
      if (this._endpoint == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Warning!, ClusterRoutingClientTube " + this + " having to search for endpoint");
         }

         AsyncClientTransportFeature var1 = (AsyncClientTransportFeature)this._context.getBinding().getFeature(AsyncClientTransportFeature.class);
         if (var1 != null) {
            WSEndpoint var2 = ((WLSEndpoint)var1.getEndpoint()).getWSEndpoint();
            if (var2 != null) {
               this.setEndpoint(var2);
            }
         }
      }

   }

   @NotNull
   public NextAction processAsyncResponse(@NotNull Packet var1) {
      AsyncTransportProviderPropertyBag var2 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var1);

      try {
         this.ensureEndpoint();
         AsyncResponseCompleteListener var3 = (AsyncResponseCompleteListener)_asyncResponseCompleteListenerMap.get(this._endpoint.getEndpointId());
         if (var3 == null) {
            var3 = new AsyncResponseCompleteListener(var2.getResponseEndpoint());
            _asyncResponseCompleteListenerMap.put(this._endpoint.getEndpointId(), var3);
            var2.getResponseEndpoint().addResponseProcessingCompletionListener(var3);
         }

         Packet var13 = this._util.handleInboundMessage(var1);
         if (var13 == null) {
            ClusterRoutingPropertyBag var5 = (ClusterRoutingPropertyBag)ClusterRoutingPropertyBag.propertySetRetriever.getFromPacket(var1);
            if (var5.getUncorrelatedAsyncResponse()) {
               String var6 = null;
               AddressingVersion var7 = this._context.getBinding().getAddressingVersion();
               SOAPVersion var8 = this._context.getBinding().getSOAPVersion();
               if (var7 != null) {
                  var6 = var1.getMessage().getHeaders().getMessageID(var7, var8);
               }

               Exception var9 = new Exception("Message cannot be processed: " + var6);
               var9.fillInStackTrace();
               var13 = var1.copy(false);
               Message var10 = WsUtil.createMessageFromThrowable(var9, var7, var8);
               var13.setMessage(var10);
            }
         }

         if (var13 != null) {
            if (var13.getMessage() != null) {
               Packet var14 = var13.copy(true);
               var2.setResponsePacket(var14);
            }

            NextAction var15 = new NextAction();
            var15.abortResponse(var13);
            return var15;
         } else {
            return this.doReturnWith(var1);
         }
      } catch (DeliveryException var11) {
         NextAction var4 = new NextAction();
         var4.throwExceptionAbortResponse(var11);
         return var4;
      } catch (Exception var12) {
         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, var12.toString(), var12);
         }

         return this.doThrow(var12);
      }
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }

   private class AsyncResponseCompleteListener implements AsyncResponseEndpoint.ResponseProcessingCompletionListener {
      private WeakReference<AsyncResponseEndpoint> _endpointRef;

      private AsyncResponseCompleteListener(AsyncResponseEndpoint var2) {
         this._endpointRef = new WeakReference(var2);
      }

      public AsyncResponseEndpoint getEndpoint() {
         return this._endpointRef != null ? (AsyncResponseEndpoint)this._endpointRef.get() : null;
      }

      public Packet responseProcessingComplete(Packet var1) {
         ClusterRoutingClientTube.this._util.processOutboundMessage(var1);
         return var1;
      }

      public Packet responseProcessingFailed(Packet var1, Throwable var2) {
         if (var1.outboundHttpHeaders != null && var1.outboundHttpHeaders.containsKey("X-weblogic-wsee-storetoserver-list")) {
            AddressingVersion var3 = ClusterRoutingClientTube.this._context.getBinding().getAddressingVersion();
            SOAPVersion var4 = ClusterRoutingClientTube.this._context.getBinding().getSOAPVersion();
            Message var5 = WsUtil.createMessageFromThrowable(var2, var3, var4);
            String var6 = var5.getHeaders().getAction(var3, var4);
            return var1.createServerResponse(var5, var3, var4, var6);
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      AsyncResponseCompleteListener(AsyncResponseEndpoint var2, Object var3) {
         this(var2);
      }
   }
}
