package weblogic.wsee.jaxws.client.async;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.jws.jaxws.client.async.FiberBox;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;

public class AsyncClientTransportReconnectTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(AsyncClientTransportReconnectTube.class.getName());
   private AsyncClientTransportFeature actf;
   private boolean usingAsyncClientHandlerFeature;
   private ClientTubeAssemblerContext context;
   private WSBinding binding;
   private FiberBox fiberBox;

   public AsyncClientTransportReconnectTube(AsyncClientTransportFeature var1, ClientTubeAssemblerContext var2, Tube var3) {
      super(var3);
      this.actf = var1;
      this.context = var2;
      this.binding = var2.getBinding();
      this.usingAsyncClientHandlerFeature = this.binding.getFeature(AsyncClientHandlerFeature.class) != null || this.binding.getFeature(AsyncClientHandlerMarkerFeature.class) != null;
   }

   protected AsyncClientTransportReconnectTube(AsyncClientTransportReconnectTube var1, TubeCloner var2) {
      super(var1, var2);
      this.actf = var1.actf;
      this.binding = var1.binding;
      this.usingAsyncClientHandlerFeature = var1.usingAsyncClientHandlerFeature;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new AsyncClientTransportReconnectTube(this, var1);
   }

   @NotNull
   public NextAction processRequest(Packet var1) {
      if (var1 == null) {
         return super.processRequest(var1);
      } else {
         String var2;
         if (LOGGER.isLoggable(Level.FINE)) {
            var2 = null;
            if (this.binding.getAddressingVersion() != null) {
               var2 = var1.getMessage().getHeaders().getMessageID(this.binding.getAddressingVersion(), this.binding.getSOAPVersion());
            }

            LOGGER.fine("AsyncClientTransportReconnectTube.processRequest clearing fiberBox " + this.fiberBox + " before processing request with msgId " + var2);
         }

         this.fiberBox = null;
         if (Boolean.TRUE.equals(var1.isSynchronousMEP) && !this.actf.isUseAsyncWithSyncInvoke()) {
            return this.doInvoke(this.next, var1);
         } else {
            var2 = var1.getMessage().getID(this.binding.getAddressingVersion(), this.binding.getSOAPVersion());
            if (!Boolean.TRUE.equals(var1.expectReply)) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Request msg: " + var2 + " was flagged expectReply=false. We won't suspend any fiber or save any request context for this request");
               }

               return this.doInvoke(this.next, var1);
            } else {
               boolean var3 = Boolean.TRUE.equals(var1.invocationProperties.get("weblogic.wsee.jaxws.client.async.SendErrorOnlyAsyncHandler"));
               boolean var4 = !this.usingAsyncClientHandlerFeature || Boolean.TRUE.equals(var1.nonNullAsyncHandlerGiven) && !var3;
               if (var4) {
                  this.fiberBox = new FiberBox(Fiber.current());
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("For message ID '" + var2 + "' storing fiber '" + Fiber.current().toString() + "' and tube " + this + " in FiberBox " + this.fiberBox + " for later resumption (assumes this fiber will be suspended after the request is sent)");
                  }

                  this.actf.register(var2, this.fiberBox);
                  return this.doInvoke(this.next, var1);
               } else {
                  PersistentContext var5 = PersistentMessageFactory.getInstance().createContextFromPacket(var2, var1);
                  PersistenceConfig.Client var6 = PersistenceConfig.getClientConfig(this.context);
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Storing persistent context for async request msg: " + var2 + " into logical store: " + var6.getLogicalStoreName());
                  }

                  PersistentContext.getStoreMap(var6.getLogicalStoreName()).put(var2, var5);
                  AsyncClientPropertyBag var7 = (AsyncClientPropertyBag)AsyncClientPropertyBag.propertySetRetriever.getFromPacket(var1);
                  var7.setRequestMessageID(var2);
                  return this.doInvoke(this.next, var1);
               }
            }
         }
      }
   }

   @NotNull
   public NextAction processResponse(Packet var1) {
      final FiberBox var2;
      if (var1.getMessage() != null) {
         String var5;
         if (LOGGER.isLoggable(Level.FINE)) {
            var5 = null;
            if (this.binding.getAddressingVersion() != null) {
               var5 = var1.getMessage().getHeaders().getMessageID(this.binding.getAddressingVersion(), this.binding.getSOAPVersion());
            }

            LOGGER.fine("AsyncClientTransportReconnectTube.processResponse for fiber " + Fiber.current() + " and tube " + this + " found non-empty message. Returning with response packet msgId: " + var5);
         }

         if (this.fiberBox != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               var5 = null;
               if (this.binding.getAddressingVersion() != null) {
                  var5 = var1.getMessage().getHeaders().getMessageID(this.binding.getAddressingVersion(), this.binding.getSOAPVersion());
               }

               LOGGER.fine("AsyncClientTransportReconnectTube.processResponse for fiber " + Fiber.current() + " and tube " + this + " clearing/opening fiberBox " + this.fiberBox + ". Returning with response packet msgId: " + var5);
            }

            var2 = this.fiberBox;
            this.fiberBox = null;
            var2.open();
         } else {
            AsyncClientPropertyBag var6 = (AsyncClientPropertyBag)AsyncClientPropertyBag.propertySetRetriever.getFromPacket(var1);
            if (var6.getRequestMessageID() != null) {
               String var3 = var6.getRequestMessageID();
               PersistenceConfig.Client var4 = PersistenceConfig.getClientConfig(this.context);
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Removing persistent context for async request msg: " + var3 + " from logical store: " + var4.getLogicalStoreName() + " because the response for it was returned on the back-channel instead of the async response endpoint");
               }

               PersistentContext.getStoreMap(var4.getLogicalStoreName()).remove(var3);
            }
         }

         return this.doReturnWith(var1);
      } else if (this.fiberBox == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("AsyncClientTransportReconnectTube.processResponse for fiber " + Fiber.current() + " and tube " + this + " found empty response message but null fiberBox. Returning with empty response packet");
         }

         return this.doReturnWith(var1);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("AsyncClientTransportReconnectTube.processResponse for fiber " + Fiber.current() + " and tube " + this + " clearing FiberBox and returning doSuspend() to eventually suspend fiber");
         }

         var2 = this.fiberBox;
         this.fiberBox = null;
         Fiber.current().addListener(new Fiber.Listener() {
            public void fiberSuspended(Fiber var1) {
               if (AsyncClientTransportReconnectTube.LOGGER.isLoggable(Level.FINE)) {
                  AsyncClientTransportReconnectTube.LOGGER.fine("Detected fiber suspension, opening FiberBox to allow response thread to resume it. Fiber=" + var2);
               }

               var2.open();
               var1.removeListener(this);
            }

            public void fiberResumed(Fiber var1) {
               var1.removeListener(this);
            }
         });
         return this.doSuspend();
      }
   }

   public NextAction processException(Throwable var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("AsyncClientTransportReconnectTube.processException clearing fiberBox " + this.fiberBox + " because of exception: " + var1);
      }

      this.fiberBox = null;
      return this.doThrow(var1);
   }

   public void preDestroy() {
      this.actf.dispose();
      if (this.next != null) {
         this.next.preDestroy();
      }

   }
}
