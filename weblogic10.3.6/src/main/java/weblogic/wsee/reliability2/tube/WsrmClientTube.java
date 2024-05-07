package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.message.RelatesToHeader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.client.async.AsyncTransportProviderPropertyBag;
import weblogic.wsee.jaxws.spi.WLSServiceDelegate;
import weblogic.wsee.reliability.faults.WsrmFaultException;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmAbortSendException;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.saf.WsrmSAFDispatchFactory;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.ws.WsPort;

public class WsrmClientTube extends AbstractFilterTubeImpl implements SequenceIdFactory {
   private static final Logger LOGGER = Logger.getLogger(WsrmClientTube.class.getName());
   private ClientTubeAssemblerContext _context;
   private WsrmClientRuntimeFeature _wsrmClientFeature;
   private WSBinding _binding;
   private WSDLPort _wsdlPort;
   private WsrmPolicyHelper _policyHelper;
   @Nullable
   private WLSServiceDelegate _service;
   private WsrmClientDispatchFactory _dispatchFactory;
   private WsrmTubeUtils _tubeUtil;

   public WsrmClientTube(Tube var1, ClientTubeAssemblerContext var2) {
      super(var1);
      WSBinding var3 = var2.getBinding();
      WsPort var4 = WsrmTubelineDeploymentListener.getWsPort(var2);
      WsrmPolicyHelper var5 = new WsrmPolicyHelper(var4);
      WLSServiceDelegate var6 = null;
      if (var2.getService() instanceof WLSServiceDelegate) {
         var6 = (WLSServiceDelegate)var2.getService();
      }

      this.commonConstructorCode(var2, var3, var2.getWsdlModel(), var5, var6);
   }

   public WsrmClientTube(WsrmClientTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode(var1._context, var1._binding, var1._wsdlPort, var1._policyHelper, var1._service);
   }

   private void commonConstructorCode(ClientTubeAssemblerContext var1, WSBinding var2, WSDLPort var3, WsrmPolicyHelper var4, WLSServiceDelegate var5) {
      this._context = var1;
      this._binding = var2;
      this._wsrmClientFeature = (WsrmClientRuntimeFeature)this._binding.getFeature(WsrmClientRuntimeFeature.class);
      this._wsdlPort = var3;
      this._policyHelper = var4;
      this._service = var5;
      if (this._wsrmClientFeature == null) {
         throw new IllegalStateException("No WsrmClientRuntimeFeature");
      } else {
         this._dispatchFactory = new WsrmClientDispatchFactory(this._wsrmClientFeature.getClientId());
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WsrmClientTube created for port " + (var3 != null ? var3.getName() : null));
            WsrmConfig.Source var6 = WsrmConfig.getSource(this._context, (Packet)null, false);
            LOGGER.fine("WsrmClientTube: RM Source Config:\n" + var6);
         }

         this._tubeUtil = new WsrmTubeUtils(true, this._binding, this._wsdlPort, this._policyHelper, this._dispatchFactory, this._context, (ServerTubeAssemblerContext)null);
      }
   }

   public void preDestroy() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Destroying WsrmClientTube with client ID: " + (this._wsrmClientFeature != null ? this._wsrmClientFeature.getClientId() : null));
      }

      ClientIdentityFeature var1 = (ClientIdentityFeature)this._binding.getFeature(ClientIdentityFeature.class);
      DispatchFactoryResolver.unregisterClientDispatchFactory(var1.getClientId());
      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      try {
         WsrmTubeUtils.OutboundMessageResult var2 = this._tubeUtil.processOutboundMessage(var1, this, var1.endpointAddress.toString());
         if (var2.messageBuffered) {
            return var2.messageInfo.getClientInvokeInfo().impliesSuspendedRequestFiber() ? this.doSuspend() : this.doReturnWith(var1.createClientResponse((Message)null));
         } else if (var2.messageAborted) {
            return this.doReturnWith(var1.createClientResponse((Message)null));
         } else {
            NextAction var7 = new NextAction();
            var7.invokeAsync(this.next, var1);
            return var7;
         }
      } catch (WsrmAbortSendException var4) {
         var1.setMessage((Message)null);
         return this.doReturnWith(var1);
      } catch (WsrmException var5) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "WsrmClientTube processRequest failed: " + var5.toString(), var5);
         }

         var1 = this._tubeUtil.handleOutboundException(var1, var5);
         if (var1 != null) {
            return this.doReturnWith(var1);
         } else {
            throw new RuntimeException(var5.toString(), var5);
         }
      } catch (Exception var6) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmClientTube processRequest failed: " + var6.toString(), var6);
         }

         WsrmException var3 = new WsrmException(var6.toString(), var6);
         var3.fillInStackTrace();
         var1 = this._tubeUtil.handleOutboundException(var1, var3);
         if (var1 != null) {
            return this.doReturnWith(var1);
         } else if (var6 instanceof RuntimeException) {
            throw (RuntimeException)var6;
         } else {
            throw new RuntimeException(var6.toString(), var6);
         }
      }
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      NextAction var2 = this.doReturnWith(var1);

      try {
         if (var1.getMessage() != null) {
            WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_RESPONSE);
         }

         WsrmTubeUtils.InboundMessageResult var3 = this._tubeUtil.processInboundMessage(var1);
         if (var3.needSuspendOnCurrentFiber) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Suspending fiber at the request of inbound message processing");
            }

            var2 = this.doSuspend();
            Fiber.current().addListener(var3.currentFiberSuspendingCallback);
         } else {
            if (var3.message != null && var3.rmFault != null) {
               throw new WsrmFaultException(var3.rmFault);
            }

            if (var3.message != null) {
               var2 = this.handleResponseToInboundMessage(var1, var3);
            } else if (var3.handled) {
               var1.setMessage((Message)null);
               var2 = this.doReturnWith(var1);
            }
         }

         return var2;
      } catch (Exception var4) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmClientTube processResponse failed: " + var4.toString(), var4);
         }

         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new RuntimeException(var4.toString(), var4);
         }
      }
   }

   private NextAction handleResponseToInboundMessage(Packet var1, WsrmTubeUtils.InboundMessageResult var2) throws WsrmException, SOAPException, IOException {
      if (LOGGER.isLoggable(Level.FINE)) {
         String var3 = this.getMessageInfoString(var1.getMessage());
         String var4 = this.getMessageInfoString(var2.message);
         LOGGER.fine("WsrmClientTube sending response '" + var4 + "' to inbound message!: " + var3);
      }

      WSEndpointReference var10 = var1.getMessage().getHeaders().getReplyTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      Message var11 = var1.getMessage();
      if (var10 != null && !var10.isAnonymous()) {
         if (var1.transportBackChannel != null) {
            var1.transportBackChannel.close();
         }

         this.sendResponseToInboundMessage(var11, var2, var10);
      } else {
         String var5 = this.getMessageInfoString(var11);
         String var6 = this.getMessageInfoString(var2.message);
         if (var1.getSatellite(AsyncTransportProviderPropertyBag.class) != null) {
            Packet var7 = var1.createClientResponse(var2.message);
            AsyncTransportProviderPropertyBag var8 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var1);
            var8.setResponsePacket(var7);
            if (LOGGER.isLoggable(Level.FINE)) {
               String var9 = "Incoming Response: '" + var5 + "' - Outgoing Secondary Response: '" + var6 + "' - " + var2.message.toString();
               LOGGER.fine("Set secondary response packet: " + var9);
               LOGGER.fine("Incoming Response Contents: " + this.dumpMessage(var11));
               LOGGER.fine("Outgoing Secondary Response Contents: " + this.dumpMessage(var2.message));
            }
         } else {
            String var13 = "Incoming Response: '" + var5 + "' - Outgoing Secondary Response: '" + var6 + "' - " + var2.message.toString();
            if (LOGGER.isLoggable(Level.FINE)) {
               String var14 = WseeRmLogger.logAttemptedSecondaryClientSideResponseFailedLoggable(var13, "Anonymous ReplyTo").getMessage();
               LOGGER.fine(var14);
               LOGGER.fine("Incoming Response Contents: " + this.dumpMessage(var11));
               LOGGER.fine("Outgoing Secondary Response Contents: " + this.dumpMessage(var2.message));
            }

            WseeRmLogger.logAttemptedSecondaryClientSideResponseFailedLoggable(var13, "Anonymous ReplyTo").log();
         }
      }

      NextAction var12 = new NextAction();
      var12.abortResponse(var1);
      return var12;
   }

   private String dumpMessage(Message var1) throws SOAPException, IOException {
      if (var1 == null) {
         return "null";
      } else {
         SOAPMessage var2 = var1.readAsSOAPMessage();
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         var2.writeTo(var3);
         return new String(var3.toByteArray());
      }
   }

   private void sendResponseToInboundMessage(final Message var1, final WsrmTubeUtils.InboundMessageResult var2, final WSEndpointReference var3) throws WsrmException {
      final WSEndpointReference var4 = this._binding.getAddressingVersion().anonymousEpr;
      this.addRelatedIdToResponseMsg(var1, var2);
      Runnable var5 = new Runnable() {
         public void run() {
            Sender var1x = new Sender(WsrmClientTube.this._dispatchFactory);

            String var3x;
            String var4x;
            String var5;
            try {
               Map var2x = null;
               if (var2.seq instanceof SourceSequence) {
                  var2x = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)((SourceSequence)var2.seq), (Packet)null);
               } else if (var2.seq instanceof DestinationSequence) {
                  var2x = DestinationSequenceManager.getInstance().getSenderInvokeProperties((DestinationSequence)((DestinationSequence)var2.seq), (Packet)null);
               }

               if (WsrmClientTube.LOGGER.isLoggable(Level.FINE)) {
                  var3x = WsrmClientTube.this.getMessageInfoString(var1);
                  var4x = WsrmClientTube.this.getMessageInfoString(var2.message);
                  var5 = "Incoming Response: '" + var3x + "' - Outgoing Secondary Response: '" + var4x + "' - " + var2.message.toString();
                  WsrmClientTube.LOGGER.fine("Delivering response to inbound response message: " + var5);
                  WsrmClientTube.LOGGER.fine("Incoming Response Contents: " + WsrmClientTube.this.dumpMessage(var1));
                  WsrmClientTube.LOGGER.fine("Outgoing Secondary Response Contents: " + WsrmClientTube.this.dumpMessage(var2.message));
               }

               var1x.send(var2.message, (String)null, var3, var4, (Map)(var2x != null ? var2x : new HashMap()), false, true);
            } catch (Exception var6) {
               var3x = WsrmClientTube.this.getMessageInfoString(var1);
               var4x = WsrmClientTube.this.getMessageInfoString(var2.message);
               var5 = "Incoming Response: '" + var3x + "' - Outgoing Secondary Response: '" + var4x + "' - " + var2.message.toString();
               if (WsrmClientTube.LOGGER.isLoggable(Level.FINE)) {
                  WsrmClientTube.LOGGER.fine("%% Encountered exception while trying to send secondary response. Details of response/secondary-response and exception follow");
                  WsrmClientTube.LOGGER.fine("%% Response details: " + var5);
                  WsrmClientTube.LOGGER.log(Level.FINE, "%% Exception details: ", var6);
               }

               WseeRmLogger.logUnexpectedException(var5 + " - " + var6.toString(), var6);
               WseeRmLogger.logAttemptedSecondaryClientSideResponseFailedLoggable(var5, var6.toString()).log();
            }

         }
      };
      var5.run();
   }

   private void addRelatedIdToResponseMsg(Message var1, WsrmTubeUtils.InboundMessageResult var2) {
      AddressingVersion var3 = this._binding.getAddressingVersion();
      SOAPVersion var4 = this._binding.getSOAPVersion();
      if (var3 != null && var2.message.getHeaders().getRelatesTo(var3, var4) == null) {
         String var5 = var1.getHeaders().getMessageID(var3, var4);
         RelatesToHeader var6 = new RelatesToHeader(var3.relatesToTag, var5);
         var2.message.getHeaders().addOrReplace(var6);
      }

   }

   private String getMessageInfoString(Message var1) {
      String var2 = var1.getHeaders().getAction(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      String var3 = var1.getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      String var4 = var1.getHeaders().getRelatesTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      return "Action: " + var2 + " Msg ID: " + var3 + " RelatesTo: " + var4;
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }

   public AbstractFilterTubeImpl copy(TubeCloner var1) {
      return new WsrmClientTube(this, var1);
   }

   public SequenceIdFactory.Info getSequenceId(String var1, boolean var2, Packet var3) {
      SequenceIdFactory.Info var4 = new SequenceIdFactory.Info();
      WsrmInvocationPropertyBag var5 = this.getRmInvokeProps(var3);
      var4.id = var5.getSequenceId();
      var4.preExisting = var4.id != null;
      if (var4.id == null && !var2) {
      }

      WsrmClientRuntimeFeature var6 = WsrmClientRuntimeFeature.getFromBinding(this._binding);
      var4.safDispatchKey = new WsrmSAFDispatchFactory.ClientSideKey(var6.getClientId());
      return var4;
   }

   private WsrmInvocationPropertyBag getRmInvokeProps(Packet var1) {
      WsrmInvocationPropertyBag var2 = (WsrmInvocationPropertyBag)var1.invocationProperties.get(WsrmInvocationPropertyBag.key);
      if (var2 == null) {
         var2 = new WsrmInvocationPropertyBag();
         var1.invocationProperties.put(WsrmInvocationPropertyBag.key, var2);
      }

      return var2;
   }

   public static Map<String, Object> getInvokePropsForClient(String var0, @Nullable WLSServiceDelegate var1) {
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      } else {
         ServiceReferenceDescriptionBean var3 = var1.getServiceReferenceDescription();
         if (var3 != null) {
            PortInfoBean[] var4 = var3.getPortInfos();
            if (var4 != null) {
               PortInfoBean[] var5 = var4;
               int var6 = var4.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  PortInfoBean var8 = var5[var7];
                  if (var8.getPortName().equals(var0)) {
                     PropertyNamevalueBean[] var9 = var8.getStubProperties();
                     if (var9 != null) {
                        PropertyNamevalueBean[] var10 = var9;
                        int var11 = var9.length;

                        for(int var12 = 0; var12 < var11; ++var12) {
                           PropertyNamevalueBean var13 = var10[var12];
                           var2.put(var13.getName(), var13.getValue());
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }
}
