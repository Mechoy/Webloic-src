package weblogic.wsee.jaxws.client.async;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSFeatureList;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ResponseOnlyTube;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.AsyncProviderCallback;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.binding.ImpliesWebServiceFeature;
import com.sun.xml.ws.client.Stub;
import com.sun.xml.ws.client.SyncStartForAsyncInvokeFeature;
import com.sun.xml.ws.client.WSServiceDelegate;
import com.sun.xml.ws.client.dispatch.PacketDispatch;
import com.sun.xml.ws.server.sei.CorrelationPropertySet;
import com.sun.xml.ws.util.Pool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Response;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.Service.Mode;
import org.w3c.dom.Element;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.jws.jaxws.client.async.FiberBox;
import weblogic.jws.jaxws.client.async.OnServerInfo;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.jaxws.InvokerOnlyFeature;
import weblogic.wsee.jaxws.cluster.ClusterRoutingPropertyBag;
import weblogic.wsee.jaxws.cluster.ClusterTubelineDeploymentListener;
import weblogic.wsee.jaxws.cluster.spi.PhysicalStoreNameHeader;
import weblogic.wsee.jaxws.persistence.ClientInstancePoolFeature;
import weblogic.wsee.jaxws.persistence.ConversationalClientInstanceFeature;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentContextStore;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.runtime.WebServicesRuntime;
import weblogic.wsee.util.Guid;

@WebServiceProvider
@ServiceMode(Mode.MESSAGE)
public class AsyncTransportProvider implements weblogic.jws.jaxws.client.async.AsyncTransportProvider, ImpliesWebServiceFeature, AsyncResponseEndpoint {
   private static final Logger LOGGER = Logger.getLogger(AsyncTransportProvider.class.getName());
   private AsyncClientTransportFeature actf;
   private AddressingVersion av;
   private SOAPVersion sv;
   private WSFeatureList featuresWithHandler;
   private Dispatch<Packet> responseDispatch;
   private PersistenceConfig.Client persistConfig;
   @Nullable
   private OnServerInfo onServerInfo;
   private ReentrantReadWriteLock _responseCompleteListenersLock = new ReentrantReadWriteLock(false);
   private List<AsyncResponseEndpoint.ResponseProcessingCompletionListener> _responseCompleteListeners = new ArrayList();

   public AsyncTransportProvider(AsyncClientTransportFeature var1) {
      this.actf = var1;
   }

   public void performValidations(WSBinding var1) {
      if (var1.getFeature(McFeature.class) != null) {
         throw new WebServiceException("Cannot concurrently use AsyncClientTransportFeature and McFeature. They are mutually exclusive.");
      } else {
         AddressingVersion var2 = var1.getAddressingVersion();
         SOAPVersion var3 = var1.getSOAPVersion();
         if (this.av != null && !this.av.equals(var2)) {
            throw new WebServiceException("Client callback endpoints sharing the same instance of AsyncClientTransportFeature must use the same addressing version.  Mismatched versions: " + this.av + " and " + var2);
         } else if (this.sv != null && !this.sv.equals(var3)) {
            throw new WebServiceException("Client callback endpoints sharing the same instance of AsyncClientTransportFeature must use the same SOAP version.  Mismatched versions: " + this.sv + " and " + var3);
         }
      }
   }

   public void init(Stub var1) {
      WSBinding var2 = var1.getBinding();
      AddressingVersion var3 = var2.getAddressingVersion();
      SOAPVersion var4 = var2.getSOAPVersion();
      this.performValidations(var2);
      this.av = var3;
      this.sv = var4;
      WSFeatureList var5 = var2.getFeatures();
      boolean var6 = false;
      AsyncClientHandlerFeature var7 = (AsyncClientHandlerFeature)var5.get(AsyncClientHandlerFeature.class);
      if (var7 != null) {
         if (this.featuresWithHandler == null) {
            var6 = true;
         } else if (!this.compareFeatureLists(var5, this.featuresWithHandler)) {
         }

         this.featuresWithHandler = var5;
      }

      if (var6 || this.responseDispatch == null) {
         Pool var8 = var1.getTubes();
         Tube var9 = (Tube)var8.take();

         try {
            ResponseOnlyTube var10 = new ResponseOnlyTube(this.createTubeEnd(TubeCloner.clone(var9), var7), true, false);
            WSDLPort var11 = var1.getWSDLPort();
            this.responseDispatch = new PacketDispatch(var11 != null ? var11.getName() : null, (WSServiceDelegate)var1.getService(), var10, (BindingImpl)var1.getBinding(), (WSEndpointReference)null, true);
            if (var2.isFeatureEnabled(SyncStartForAsyncInvokeFeature.class)) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("AsyncTransportProvider " + this + " defeating SyncStartForAsyncInvokeFeature found on client binding by setting PREVENT_SYNC_START_FOR_ASYNC_INVOKE on the dispatch used for responses");
               }

               this.responseDispatch.getRequestContext().put("com.sun.xml.ws.client.StubRequestSyncStartForAsyncInvoke", "true");
            }
         } finally {
            var8.recycle(var9);
         }
      }

      this.persistConfig = PersistenceConfig.getClientConfig(var1);
      if (KernelStatus.isServer()) {
         this.onServerInfo = new OnServerInfo();
         ServletContext var15 = (ServletContext)ContainerResolver.getInstance().getContainer().getSPI(ServletContext.class);
         this.onServerInfo.setContext(var15);
         String var16 = WLSEndpoint.getServerAddress();
         this.onServerInfo.setListenAddress(var16);
         String var17 = WLSEndpoint.getServerAddressForSSL();
         this.onServerInfo.setSslListenAddress(var17);
         String var18 = Guid.generateGuid();
         this.onServerInfo.setGuid(var18);
         this.onServerInfo.setReplyToRefParams(this.getReplyToRefParams());
      }

   }

   private boolean compareFeatureLists(WSFeatureList var1, WSFeatureList var2) {
      return var1.get(ClientInstancePoolFeature.class) != null || var1.get(ConversationalClientInstanceFeature.class) != null || var1.equals(var2);
   }

   private Tube createTubeEnd(Tube var1, AsyncClientHandlerFeature var2) {
      return (Tube)(var2 == null ? new ErrorTube(var1) : var1);
   }

   public void addResponseProcessingCompletionListener(AsyncResponseEndpoint.ResponseProcessingCompletionListener var1) {
      try {
         this._responseCompleteListenersLock.writeLock().lock();
         if (!this._responseCompleteListeners.contains(var1)) {
            this._responseCompleteListeners.add(var1);
         }
      } finally {
         this._responseCompleteListenersLock.writeLock().unlock();
      }

   }

   public void removeResponseProcessingCompletionListener(AsyncResponseEndpoint.ResponseProcessingCompletionListener var1) {
      try {
         this._responseCompleteListenersLock.writeLock().lock();
         this._responseCompleteListeners.remove(var1);
      } finally {
         this._responseCompleteListenersLock.writeLock().unlock();
      }

   }

   private AsyncResponseEndpoint.ResponseProcessingCompletionListener[] getResponseProcessingCompletionListeners() {
      AsyncResponseEndpoint.ResponseProcessingCompletionListener[] var1;
      try {
         this._responseCompleteListenersLock.readLock().lock();
         var1 = (AsyncResponseEndpoint.ResponseProcessingCompletionListener[])this._responseCompleteListeners.toArray(new AsyncResponseEndpoint.ResponseProcessingCompletionListener[this._responseCompleteListeners.size()]);
      } finally {
         this._responseCompleteListenersLock.readLock().unlock();
      }

      return var1;
   }

   public void invoke(Message var1, AsyncProviderCallback<Message> var2, WebServiceContext var3) {
      try {
         Packet var4 = ((WSWebServiceContext)var3).getRequestPacket();
         HeaderList var5 = var1.getHeaders();
         String var6 = var5.getMessageID(this.av, this.sv);
         String var7 = var5.getRelatesTo(this.av, this.sv);
         AsyncTransportProviderPropertyBag var8 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var4);
         var8.setResponseEndpoint(this);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Added AsyncTransportProviderPropertyBag for msgId " + var6 + " packet: " + var4.toShortString());
         }

         FiberBox var9 = var7 != null ? this.actf.retrieve(var7) : null;
         if (var9 == null) {
            if (var7 != null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Found async response with no suspended thread. Attempting to find saved persistent contex for async request msg: " + var7 + " into response msg: " + var6);
               }

               PersistentContextStore var10 = PersistentContext.getStoreMap(this.persistConfig.getLogicalStoreName());
               PersistentContext var11 = (PersistentContext)var10.get(var7);
               if (var11 != null) {
                  this.dispatchResponseWithAsyncRequestContext(var4, var6, var7, var8, var10, var2, var11);
               } else if (ClusterTubelineDeploymentListener.isClusterServer()) {
                  ClusterRoutingPropertyBag var12 = (ClusterRoutingPropertyBag)ClusterRoutingPropertyBag.propertySetRetriever.getFromPacket(var4);
                  var12.setUncorrelatedAsyncResponse(true);
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("DIDN'T FIND saved persistent contex for async request msg: " + var7 + " while processing response msg: " + var6 + ". ALLOWING it to be processed in order to allow cluster routing to kick in.");
                  }

                  this.dispatchResponseDownTubeline(var2, var4);
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("DIDN'T FIND saved persistent contex for async request msg: " + var7 + " while processing response msg: " + var6 + ". This message cannot be processed.");
                  }

                  Exception var14 = new Exception("Message cannot be processed: " + var6);
                  var14.fillInStackTrace();
                  var2.sendError(var14);
               }
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Found incoming async message with no related outgoing request. Assuming " + var6 + " is a protocol message");
               }

               this.dispatchResponseDownTubeline(var2, var4);
            }
         } else {
            this.dispatchResponseWithSuspendedFiber(var4, var6, var7, var9, var2);
         }
      } catch (Throwable var13) {
         var2.sendError(var13);
      }

   }

   private void dispatchResponseWithSuspendedFiber(final Packet var1, String var2, String var3, FiberBox var4, final AsyncProviderCallback<Message> var5) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Found suspended fiber in fiberBox " + var4 + " from async request msg: " + var3 + ". Will get a ref to it (may block till original fiber is suspended), resume it and process response msg: " + var2);
      }

      try {
         Fiber var6 = var4.get();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Obtained ref to suspended fiber from fiberBox " + var4 + " incoming RelatesTo msgId: " + var3 + ". Will resume it and process response msg: " + var2);
         }

         Packet var7 = var6.getPacket().createClientResponse(var1.getMessage());
         var7.wasTransportSecure = var1.wasTransportSecure;
         var1.copySatelliteInto(var7);
         final Fiber.CompletionCallback var8 = var6.getCompletionCallback();
         var6.setCompletionCallback(new Fiber.CompletionCallback() {
            public void onCompletion(Packet var1x) {
               Packet var2 = null;

               try {
                  if (var8 != null) {
                     var8.onCompletion(var1x);
                  }

                  var2 = AsyncTransportProvider.this.getFinalResponsePacket(var1, var1x);
               } finally {
                  var5.send(var2 != null ? var2.getMessage() : null);
               }

            }

            public void onCompletion(Throwable var1x) {
               try {
                  if (var8 != null) {
                     var8.onCompletion(var1x);
                  }

                  Packet var2 = AsyncTransportProvider.this.getFinalResponsePacket(var1, var1x);
                  if (var2 != null) {
                     var1x = null;
                     var5.send(var2.getMessage());
                  }
               } finally {
                  if (var1x != null) {
                     var5.sendError(var1x);
                  }

               }

            }
         });
         var6.resume(var7);
      } catch (InterruptedException var9) {
         var5.send((Object)null);
      }

   }

   private void dispatchResponseWithAsyncRequestContext(Packet var1, String var2, final String var3, AsyncTransportProviderPropertyBag var4, final PersistentContextStore var5, AsyncProviderCallback<Message> var6, PersistentContext var7) {
      synchronized(var7) {
         var7 = (PersistentContext)var5.get(var3);
         if (var7 != null) {
            String var9;
            if (LOGGER.isLoggable(Level.FINE)) {
               var9 = this.dumpPersistentContext(var7);
               LOGGER.fine("Restoring saved persistent contex for async request msg: " + var3 + " into response msg: " + var2 + " contents: " + var9);
            }

            var9 = (String)var1.get("javax.xml.ws.soap.http.soapaction.uri");
            PersistentMessageFactory.getInstance().setContextIntoPacket(var7, var1);
            var1.addSatellite(new CorrelationPropertySet((String)var1.get("javax.xml.ws.soap.http.soapaction.uri")));
            var1.put("javax.xml.ws.soap.http.soapaction.uri", var9);
            if (LOGGER.isLoggable(Level.FINE)) {
               String var10 = null;
               if (var1.persistentContext != null) {
                  var10 = dumpPersistentContextContextProps(var1.persistentContext);
               }

               LOGGER.fine("Beginning dispatch of async response for request " + var3 + " as response msg " + var2 + " with Packet.persistentContext: " + var10);
            }

            var7.setState(PersistentContext.State.IN_USE);
            RequestContextRemovalCallback var13 = new RequestContextRemovalCallback() {
               public void stopUsingAsyncRequestContext() {
                  PersistentContext var1 = (PersistentContext)var5.get(var3);
                  if (var1 != null) {
                     if (AsyncTransportProvider.LOGGER.isLoggable(Level.FINE)) {
                        AsyncTransportProvider.LOGGER.fine("RequestContextRemovalCallback is setting PersistentContext.State.UNUSED for async request msgId: " + var3);
                     }

                     var1.setState(PersistentContext.State.UNUSED);
                  }

               }

               public void removeAsyncRequestContext() {
                  if (AsyncTransportProvider.LOGGER.isLoggable(Level.FINE)) {
                     AsyncTransportProvider.LOGGER.fine("RequestContextRemovalCallback is removing PersistentContext for async request msgId: " + var3);
                  }

                  var5.remove(var3);
               }
            };
            var4.setRequestContextRemovalCallback(var13);
            this.dispatchResponseDownTubeline(var6, var1);
         } else {
            var6.sendError(new RuntimeException("persistCtx is null"));
         }

      }
   }

   private String dumpPersistentContext(PersistentContext var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("ContextPropCount=");
      if (var1.getContextPropertyMap() != null) {
         var2.append(dumpPersistentContextContextProps(var1.getContextPropertyMap()));
      } else {
         var2.append("0");
      }

      var2.append(" PropCount=").append(var1.getPropertyMap().size());
      var2.append(" InvokePropCount=").append(var1.getInvocationPropertyMap().size());
      return var2.toString();
   }

   public static String dumpPersistentContextContextProps(Map<String, ?> var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append(var0.size());
      var1.append(" (");
      Iterator var2 = var0.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append(var3).append("=");
         var1.append(var0.get(var3));
         var1.append(",");
      }

      var1.append(")");
      return var1.toString();
   }

   private void dispatchResponseDownTubeline(final AsyncProviderCallback<Message> var1, final Packet var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         String var3 = var2.getMessage().getHeaders().getMessageID(this.av, this.sv);
         String var4 = dumpPersistentContextContextProps(var2.persistentContext);
         LOGGER.fine("Putting persistentContext props onto RequestContext for msg " + var3 + " contents: " + var4);
      }

      this.responseDispatch.getRequestContext().put("weblogic.wsee.jaxws.async.PersistentContext", var2.persistentContext);
      this.responseDispatch.invokeAsync(var2, new AsyncHandler<Packet>() {
         public void handleResponse(Response<Packet> var1x) {
            boolean var8 = false;

            AsyncTransportProviderPropertyBag var11;
            label80: {
               try {
                  Packet var3;
                  try {
                     var8 = true;
                     Packet var2x = (Packet)var1x.get();
                     var3 = AsyncTransportProvider.this.getFinalResponsePacket(var2, var2x);
                     var1.send(var3.getMessage());
                     var8 = false;
                     break label80;
                  } catch (Throwable var9) {
                     var3 = AsyncTransportProvider.this.getFinalResponsePacket(var2, var9);
                     if (var3 != null) {
                        var1.send(var3.getMessage());
                        var8 = false;
                     } else {
                        var1.sendError(var9);
                        var8 = false;
                     }
                  }
               } finally {
                  if (var8) {
                     AsyncTransportProviderPropertyBag var5 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var2);
                     if (var5.getRequestContextRemovalCallback() != null) {
                        var5.getRequestContextRemovalCallback().removeAsyncRequestContext();
                     }

                  }
               }

               var11 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var2);
               if (var11.getRequestContextRemovalCallback() != null) {
                  var11.getRequestContextRemovalCallback().removeAsyncRequestContext();
               }

               return;
            }

            var11 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var2);
            if (var11.getRequestContextRemovalCallback() != null) {
               var11.getRequestContextRemovalCallback().removeAsyncRequestContext();
            }

         }
      });
   }

   private Packet getFinalResponsePacket(Packet var1, Packet var2) {
      Packet var3 = null;
      AsyncResponseEndpoint.ResponseProcessingCompletionListener[] var4 = this.getResponseProcessingCompletionListeners();
      AsyncResponseEndpoint.ResponseProcessingCompletionListener[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         AsyncResponseEndpoint.ResponseProcessingCompletionListener var8 = var5[var7];
         Packet var9 = var8.responseProcessingComplete(var2);
         if (var9 != var2) {
            var3 = var9;
         }
      }

      AsyncTransportProviderPropertyBag var10 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var1);
      var10.setPacket(var3 != null ? var3 : var2);
      Packet var11 = var10.getResponsePacket();
      if (var11 != null) {
         var3 = var11;
      }

      if (var3 == null) {
         var3 = var2.copy(false);
      }

      if (var3 != null) {
         if (var1.outboundHttpHeaders != null && var3.outboundHttpHeaders != null) {
            var1.outboundHttpHeaders.putAll(var3.outboundHttpHeaders);
         } else if (var1.outboundHttpHeaders == null) {
            var1.outboundHttpHeaders = var3.outboundHttpHeaders;
         }

         if (var1.transportBackChannel == null) {
            var1.transportBackChannel = var3.transportBackChannel;
         }
      }

      return var3;
   }

   private Packet getFinalResponsePacket(Packet var1, Throwable var2) {
      AsyncResponseEndpoint.ResponseProcessingCompletionListener[] var3 = this.getResponseProcessingCompletionListeners();
      Packet var4 = null;
      AsyncResponseEndpoint.ResponseProcessingCompletionListener[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         AsyncResponseEndpoint.ResponseProcessingCompletionListener var8 = var5[var7];
         var4 = var8.responseProcessingFailed(var1, var2);
         if (var4 != null) {
            var4 = this.getFinalResponsePacket(var1, var4);
            break;
         }
      }

      return var4;
   }

   public void implyFeatures(Map<Class<? extends WebServiceFeature>, WebServiceFeature> var1) {
      var1.put(InvokerOnlyFeature.class, new InvokerOnlyFeature());
   }

   public OnServerInfo getOnServerInfo() {
      return this.onServerInfo;
   }

   public EndpointReference getEndpointReference() {
      return this.actf.getEndpoint().getEndpointReference(new Element[0]);
   }

   private HeaderList getReplyToRefParams() {
      HeaderList var1 = null;
      String var2 = this.persistConfig.getLogicalStoreName();
      if (var2 != null) {
         List var3 = WebServicesRuntime.getInstance().getLocalPhysicalStoresForLogicalStore(var2);
         String var4 = var3 != null && var3.size() > 0 ? (String)var3.get(0) : null;
         if (var4 != null) {
            PhysicalStoreNameHeader var5 = new PhysicalStoreNameHeader(var4);
            var1 = new HeaderList();
            var1.add(var5);
         }
      }

      return var1;
   }

   private static class ErrorTube extends AbstractFilterTubeImpl {
      public ErrorTube(Tube var1) {
         super(var1);
      }

      public ErrorTube(ErrorTube var1, TubeCloner var2) {
         super(var1, var2);
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return new ErrorTube(this, var1);
      }

      public NextAction processRequest(Packet var1) {
         throw new IllegalStateException("ErrorTube's processRequest shouldn't be called.");
      }

      public NextAction processResponse(Packet var1) {
         if (var1.getMessage() == null) {
            return this.doReturnWith(var1);
         } else {
            throw new WebServiceException("No handler present and protocol message reached tubeline end");
         }
      }
   }

   public interface RequestContextRemovalCallback {
      void stopUsingAsyncRequestContext();

      void removeAsyncRequestContext();
   }
}
