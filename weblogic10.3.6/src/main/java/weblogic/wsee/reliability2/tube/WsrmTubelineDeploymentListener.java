package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.SyncStartForAsyncInvokeFeature;
import com.sun.xml.ws.transport.http.server.EndpointImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentityFeature;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.AbstractTubelineSpliceFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability2.api.WsrmClientInitFeature;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.saf.BufferUtil;
import weblogic.wsee.ws.WsPort;

public class WsrmTubelineDeploymentListener implements TubelineDeploymentListener {
   private static final Logger LOGGER = Logger.getLogger(WsrmTubelineDeploymentListener.class.getName());

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Creating WS-RM client tubeline items for " + var1.getService().getServiceName());
      }

      WsPort var3 = getWsPort(var1);
      WsrmPolicyHelper var4 = new WsrmPolicyHelper(var3);

      try {
         if (!var4.hasRMPolicy()) {
            return;
         }
      } catch (Exception var10) {
         WseeRmLogger.logPolicyProcessingFailedOnClient(var10);
         return;
      }

      AbstractWsrmTube.addKnownHeaders(var1.getBinding());
      AbstractTubelineSpliceFactory var5 = new AbstractTubelineSpliceFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            WSBinding var3 = var2.getBinding();
            WsrmTubelineDeploymentListener.this.enableClientAddressing(var3);
            WsrmTubelineDeploymentListener.this.enableRmClientFeature(var2);
            return new WsrmClientTube(var1, var2);
         }

         public void createSplice(TubelineSpliceFactory.ClientDispatchFactory var1, ClientTubeAssemblerContext var2) {
            super.createSplice(var1, var2);
            ClientIdentityFeature var3 = (ClientIdentityFeature)var2.getBinding().getFeature(ClientIdentityFeature.class);
            String var4 = var3.getClientId();
            AsyncClientTransportFeature var5 = (AsyncClientTransportFeature)var2.getBinding().getFeature(AsyncClientTransportFeature.class);
            if (var5 != null) {
               MyAsyncEndpointListener var6 = new MyAsyncEndpointListener(var2, var5, var1);
               var5.addAsyncEndpointListener(var6);
               if (var5.isEndpointPublished()) {
                  var6.endpointPublished(var5);
               }
            } else {
               DispatchFactoryResolver.registerClientDispatchFactory(var4, var1);
            }

         }
      };
      TubelineAssemblerItem var6 = new TubelineAssemblerItem("WsrmClient", var5);
      HashSet var7 = new HashSet();
      var7.add("WS_SECURITY_1.1");
      var7.add("PRE_WS_SECURITY_POLICY_1.2");
      var6.setGoAfter(var7);
      HashSet var8 = new HashSet();
      var8.add("ADDRESSING_HANDLER");
      var8.add("client");
      var6.setGoBefore(var8);
      var2.add(var6);
      TubelineAssemblerItem var9 = new TubelineAssemblerItem("WsrmClientProtocol", new AbstractTubeFactory() {
         public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
            return new WsrmClientProtocolTube(var1);
         }
      });
      var9.setGoBefore(Collections.singleton("WS_SECURITY_1.1"));
      var2.add(var9);
   }

   private void enableClientAddressing(WSBinding var1) {
      WebServiceFeatureList var2;
      if (!var1.getFeatures().isEnabled(AddressingFeature.class)) {
         var2 = (WebServiceFeatureList)var1.getFeatures();
         var2.add(new AddressingFeature(true));
      }

      if (!var1.getFeatures().isEnabled(OneWayFeature.class) && !var1.getFeatures().isEnabled(AsyncClientTransportFeature.class)) {
         var2 = (WebServiceFeatureList)var1.getFeatures();
         var2.add(new OneWayFeature(true));
      }

   }

   private void enableServerAddressing(WSBinding var1) {
      if (!var1.getFeatures().isEnabled(AddressingFeature.class)) {
         WebServiceFeatureList var2 = (WebServiceFeatureList)var1.getFeatures();
         var2.add(new AddressingFeature(true));
      }

   }

   private void enableRmClientFeature(ClientTubeAssemblerContext var1) {
      WSBinding var2 = var1.getBinding();
      ClientInstanceIdentityFeature var3 = (ClientInstanceIdentityFeature)var2.getFeature(ClientInstanceIdentityFeature.class);
      if (var3 == null) {
         throw new IllegalStateException("No ClientInstanceIdentityFeature on RM enabled tubeline");
      } else {
         ClientInstanceIdentity var4 = var3.getClientInstanceId();
         WsrmClientRuntimeFeature var5;
         if (!var2.getFeatures().isEnabled(WsrmClientInitFeature.class)) {
            if (!var2.getFeatures().isEnabled(WsrmClientRuntimeFeature.class)) {
               WebServiceFeatureList var6 = (WebServiceFeatureList)var2.getFeatures();
               var5 = new WsrmClientRuntimeFeature(var4.getClientId(), var1);
               var6.add(var5);
            } else {
               var5 = (WsrmClientRuntimeFeature)var2.getFeatures().get(WsrmClientRuntimeFeature.class);
            }
         } else if (!var2.getFeatures().isEnabled(WsrmClientRuntimeFeature.class)) {
            WsrmClientInitFeature var9 = (WsrmClientInitFeature)var2.getFeatures().get(WsrmClientInitFeature.class);
            WebServiceFeatureList var7 = (WebServiceFeatureList)var2.getFeatures();
            var5 = new WsrmClientRuntimeFeature(var4.getClientId(), var1, var9);
            var7.add(var5);
         } else {
            var5 = (WsrmClientRuntimeFeature)var2.getFeatures().get(WsrmClientRuntimeFeature.class);
         }

         ClientIdentityFeature var10 = (ClientIdentityFeature)var2.getFeatures().get(ClientIdentityFeature.class);
         if (var10 != null && var10.getRawClientId() != null) {
            if (!var2.isFeatureEnabled(WsrmClientRuntimeFeature.class)) {
               ((WebServiceFeatureList)var2.getFeatures()).add(var5);
            }

            WsrmConfig.Source var11 = WsrmConfig.getSource(var1, (Packet)null, false);
            if (var11.getDeliveryAssurance().isInOrder()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Enabling SyncStartForAsyncInvokeFeature for WS-RM client tube to support in-order delivery");
               }

               if (!var2.isFeatureEnabled(SyncStartForAsyncInvokeFeature.class)) {
                  SyncStartForAsyncInvokeFeature var8 = new SyncStartForAsyncInvokeFeature();
                  ((WebServiceFeatureList)var2.getFeatures()).add(var8);
               }
            }

         } else {
            throw new IllegalStateException(WseeRmLogger.logClientIdentityNotProvidedLoggable().getMessage());
         }
      }
   }

   @Nullable
   public static WsPort getWsPort(ClientTubeAssemblerContext var0) {
      EnvironmentFactory var1 = JAXRPCEnvironmentFeature.getFactory(var0);
      if (var1 == null) {
         return null;
      } else {
         WSDLPort var2 = var1.getPort();
         if (var2 == null) {
            return null;
         } else {
            QName var3 = var2.getName();
            EnvironmentFactory.SingletonService var4 = var1.getService();
            return var4 == null ? null : var4.getPort(var3.getLocalPart());
         }
      }
   }

   @Nullable
   public static WsPort getWsPort(ServerTubeAssemblerContext var0) {
      EnvironmentFactory var1 = JAXRPCEnvironmentFeature.getFactory(var0.getEndpoint());
      WSDLPort var2 = var1.getPort();
      if (var2 == null) {
         return null;
      } else {
         QName var3 = var2.getName();
         EnvironmentFactory.SingletonService var4 = var1.getService();
         return var4 == null ? null : var4.getPort(var3.getLocalPart());
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Creating WS-RM server tubeline items for " + var1.getEndpoint().getServiceName());
      }

      WsPort var3 = getWsPort(var1);
      if (var3 != null) {
         WsrmPolicyHelper var4 = new WsrmPolicyHelper(var3);

         try {
            if (!var4.hasRMPolicy()) {
               return;
            }
         } catch (Exception var10) {
            WseeRmLogger.logPolicyProcessingFailedOnServer(var10);
            return;
         }

         AbstractWsrmTube.addKnownHeaders(var1.getEndpoint().getBinding());
         AbstractTubelineSpliceFactory var5 = new AbstractTubelineSpliceFactory() {
            private TubelineSpliceFactory.DispatchFactory _dispatchFactoryFromSplice;

            public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
               WSBinding var3 = var2.getEndpoint().getBinding();
               WsrmTubelineDeploymentListener.this.enableServerAddressing(var3);
               WsPort var4 = WsrmTubelineDeploymentListener.getWsPort(var2);
               return (Tube)(var4 == null ? var1 : new WsrmServerTube(var1, var2, this._dispatchFactoryFromSplice));
            }

            public void createSplice(TubelineSpliceFactory.DispatchFactory var1, ServerTubeAssemblerContext var2) {
               super.createSplice(var1, var2);
               this._dispatchFactoryFromSplice = var1;
               WSEndpoint var3 = var2.getEndpoint();
               DispatchFactoryResolver.registerServerDispatchFactory(var3.getEndpointId(), var1);
               WsrmConfig.Destination var4 = WsrmConfig.getDestination((ServerTubeAssemblerContext)var2, (Packet)null, false);
               if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                  WsrmTubelineDeploymentListener.LOGGER.fine("Server side splice: " + var4.toString());
               }

               if (var4.isNonBufferedDestination()) {
                  if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                     WsrmTubelineDeploymentListener.LOGGER.fine("setupBufferingFeature:  RM Destination is non-buffered.  Skip BufferingSetup.");
                  }

               } else {
                  List var5 = BufferUtil.getTargetURIs(var3);
                  Iterator var6 = var5.iterator();

                  String var7;
                  do {
                     if (!var6.hasNext()) {
                        try {
                           BufferUtil.setupBufferingFeature(var2, var3, var1, false, var4);
                           return;
                        } catch (BufferingException var8) {
                           throw new RuntimeException(var8);
                        }
                     }

                     var7 = (String)var6.next();
                  } while(!BufferingFeature.isBufferingFeatureRegistered(var7));

                  if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                     WsrmTubelineDeploymentListener.LOGGER.fine("Already have a buffering feature registered for one of the target URIs for endpoint " + var3.getEndpointId() + ". Ignoring it. Target URI: " + var7);
                  }

               }
            }
         };
         TubelineAssemblerItem var6 = new TubelineAssemblerItem("WsrmServer", var5);
         HashSet var7 = new HashSet();
         var7.add("ADDRESSING_HANDLER");
         var7.add("WS_SECURITY_1.1");
         var6.setGoBefore(var7);
         HashSet var8 = new HashSet();
         var6.setGoAfter(var8);
         var2.add(var6);
         TubelineAssemblerItem var9 = new TubelineAssemblerItem("WsrmServerProtocol", new AbstractTubeFactory() {
            public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
               return new WsrmServerProtocolTube(var1);
            }
         });
         var9.setGoBefore(Collections.singleton("WS_SECURITY_1.1"));
         var2.add(var9);
      }
   }

   private static class MyAsyncEndpointListener implements AsyncClientTransportFeature.AsyncEndpointListener {
      ClientTubeAssemblerContext _context;
      AsyncClientTransportFeature _feature;
      TubelineSpliceFactory.ClientDispatchFactory _dispatchFactory;

      public MyAsyncEndpointListener(ClientTubeAssemblerContext var1, AsyncClientTransportFeature var2, TubelineSpliceFactory.ClientDispatchFactory var3) {
         this._context = var1;
         this._feature = var2;
         this._dispatchFactory = var3;
      }

      public void endpointSet(AsyncClientTransportFeature var1) {
         BindingImpl var2 = null;
         if (var1.getEndpoint() instanceof EndpointImpl) {
            EndpointImpl var3 = (EndpointImpl)var1.getEndpoint();
            var2 = (BindingImpl)var3.getBinding();
         } else if (var1.getEndpoint() instanceof WLSEndpoint) {
            WLSEndpoint var4 = (WLSEndpoint)var1.getEndpoint();
            var2 = (BindingImpl)var4.getBinding();
         }

         if (var2 != null) {
            Set var5 = var2.getKnownHeaders();
            if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
               WsrmTubelineDeploymentListener.LOGGER.fine("Published endpoint has " + var5.size() + " known headers");
            }
         }

         AbstractWsrmTube.addKnownHeaders(var2);
      }

      public void endpointPublished(AsyncClientTransportFeature var1) {
         ClientIdentityFeature var2 = (ClientIdentityFeature)this._context.getBinding().getFeature(ClientIdentityFeature.class);
         DispatchFactoryResolver.registerClientDispatchFactory(var2.getClientId(), this._dispatchFactory);
         WsrmConfig.Destination var3 = WsrmConfig.getDestination((ClientTubeAssemblerContext)this._context, (Packet)null, false);
         if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
            WsrmTubelineDeploymentListener.LOGGER.fine("Client side splice: " + var3.toString());
         }

         if (var3.isNonBufferedDestination()) {
            if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
               WsrmTubelineDeploymentListener.LOGGER.fine("setupBufferingFeature:  RM Destination is non-buffered.  Skip BufferingSetup.");
            }

         } else {
            Endpoint var4 = var1.getEndpoint();
            if (var4 instanceof WLSEndpoint) {
               WLSEndpoint var5 = (WLSEndpoint)var4;
               WSEndpoint var6 = var5.getWSEndpoint();
               if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                  WsrmTubelineDeploymentListener.LOGGER.fine("Setting up buffering for async response endpoint: " + var6.getEndpointId());
               }

               List var7 = BufferUtil.getTargetURIs(var6);
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  if (BufferingFeature.isBufferingFeatureRegistered(var9)) {
                     if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                        WsrmTubelineDeploymentListener.LOGGER.fine("Already have a buffering feature registered for one of the target URIs for endpoint " + var6.getEndpointId() + ". Ignoring it. Target URI: " + var9);
                     }

                     return;
                  }
               }

               try {
                  BufferUtil.setupBufferingFeature((ClientTubeAssemblerContext)this._context, (WLSEndpoint)var5, this._dispatchFactory, true, var3);
               } catch (BufferingException var10) {
                  throw new RuntimeException(var10);
               }
            }

         }
      }

      public void endpointDisposed(AsyncClientTransportFeature var1) {
         var1.removeAsyncEndpointListener(this);
      }
   }

   private static class WsrmServerProtocolTube extends AbstractFilterTubeImpl {
      public WsrmServerProtocolTube(Tube var1) {
         super(var1);
      }

      private WsrmServerProtocolTube(WsrmServerProtocolTube var1, TubeCloner var2) {
         super(var1, var2);
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return new WsrmServerProtocolTube(this, var1);
      }

      @NotNull
      public NextAction processResponse(@NotNull Packet var1) {
         try {
            WsrmTubeUtils.initializeOutboundMessage(var1);
         } catch (Throwable var3) {
            if (var3 instanceof RuntimeException) {
               throw (RuntimeException)var3;
            }

            throw new RuntimeException(var3.toString(), var3);
         }

         return this.doReturnWith(var1);
      }
   }

   private static class WsrmClientProtocolTube extends AbstractFilterTubeImpl {
      public WsrmClientProtocolTube(Tube var1) {
         super(var1);
      }

      private WsrmClientProtocolTube(WsrmClientProtocolTube var1, TubeCloner var2) {
         super(var1, var2);
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return new WsrmClientProtocolTube(this, var1);
      }

      @NotNull
      public NextAction processResponse(@NotNull Packet var1) {
         try {
            WsrmTubeUtils.initializeInboundMessage(var1);
         } catch (Throwable var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new RuntimeException(var5.toString(), var5);
         }

         try {
            SequenceFaultMsg var2 = WsrmTubeUtils.getRmFaultFromMessage(var1);
            if (var2 != null) {
               if (WsrmTubelineDeploymentListener.LOGGER.isLoggable(Level.FINE)) {
                  WsrmTubelineDeploymentListener.LOGGER.fine("Found RM Fault in response msg: " + var2.toString());
               }

               SequenceFaultException var7 = new SequenceFaultException(var2);
               NextAction var4 = new NextAction();
               var4.throwException(var7);
               return var4;
            }
         } catch (SOAPException var6) {
            NextAction var3 = new NextAction();
            var3.throwExceptionAbortResponse(var6);
            return var3;
         }

         return this.doReturnWith(var1);
      }
   }
}
