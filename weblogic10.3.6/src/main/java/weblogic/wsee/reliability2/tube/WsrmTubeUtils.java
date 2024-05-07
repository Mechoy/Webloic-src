package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.addressing.WsaPropertyBag;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.model.wsdl.WSDLPortType;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.TransportBackChannel;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.message.StringHeader;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.kernel.KernelStatus;
import weblogic.management.runtime.WseeBasePortRuntimeMBean;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.jaxws.client.async.AsyncResponseEndpoint;
import weblogic.wsee.jaxws.client.async.AsyncTransportProviderPropertyBag;
import weblogic.wsee.jaxws.cluster.spi.PhysicalStoreNameHeader;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.persistence.PersistentRequestContext;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.monitoring.WseePortRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeWsrmRuntimeMBeanImpl;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.faults.SecurityMismatchFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.reliability.faults.SequenceTerminatedFaultMsg;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.faults.WSRMRequiredFaultMsg;
import weblogic.wsee.reliability.faults.WsrmFaultException;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceMsg;
import weblogic.wsee.reliability.handshake.SequenceOffer;
import weblogic.wsee.reliability.handshake.TerminateSequenceMsg;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSSLHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSTRHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.compat.Rpc2WsUtil;
import weblogic.wsee.reliability2.exception.WsrmAbortSendException;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.exception.WsrmUnsupportedConfigurationException;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.CreateSequencePostSecurityTokenCallback;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationOfferSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.Sequence;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownSequenceException;
import weblogic.wsee.reliability2.sequence.UnknownSourceSequenceException;
import weblogic.wsee.reliability2.store.SenderDispatchFactory;
import weblogic.wsee.reliability2.tube.processors.CloseSequenceResponseProcessor;
import weblogic.wsee.reliability2.tube.processors.MessageProcessor;
import weblogic.wsee.reliability2.tube.processors.MessageProcessorFactory;
import weblogic.wsee.reliability2.tube.processors.TerminateSequenceProcessor;
import weblogic.wsee.reliability2.tube.processors.TerminateSequenceResponseProcessor;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class WsrmTubeUtils {
   private static final Logger LOGGER = Logger.getLogger(WsrmTubeUtils.class.getName());
   private WSBinding _binding;
   private AddressingVersion _addrVersion;
   private SOAPVersion _soapVersion;
   private WSDLPort _wsdlPort;
   @Nullable
   private WsrmPolicyHelper _policyHelper;
   private DispatchFactory _dispatchFactory;
   private WSEndpointImpl _endpoint;
   @Nullable
   private ClientTubeAssemblerContext _clientContext;
   @Nullable
   private ServerTubeAssemblerContext _serverContext;
   private boolean _clientSide;
   private boolean _seqTerminatedFlag;
   private static BASE64Encoder _base64 = new BASE64Encoder();

   public WsrmTubeUtils(boolean var1, @NotNull WSBinding var2, @NotNull WSDLPort var3, @Nullable WsrmPolicyHelper var4, @NotNull DispatchFactory var5, @Nullable ClientTubeAssemblerContext var6, @Nullable ServerTubeAssemblerContext var7) {
      this._clientSide = var1;
      this._binding = var2;
      this._addrVersion = var2.getAddressingVersion();
      this._soapVersion = var2.getSOAPVersion();
      this._wsdlPort = var3;
      this._policyHelper = var4;
      this._dispatchFactory = var5;
      this._clientContext = var6;
      this._serverContext = var7;
      SequenceIDRoutingInfoFinder.registerIfNeeded();
   }

   @NotNull
   public WSBinding getBinding() {
      return this._binding;
   }

   @NotNull
   public WSDLPort getWsdlPort() {
      return this._wsdlPort;
   }

   @Nullable
   public WsrmPolicyHelper getPolicyHelper() {
      return this._policyHelper;
   }

   @Nullable
   public ServerTubeAssemblerContext getServerContext() {
      return this._serverContext;
   }

   @NotNull
   public DispatchFactory getDispatchFactory() {
      return this._dispatchFactory;
   }

   public void setEndpoint(WSEndpointImpl var1) {
      this._endpoint = var1;
   }

   public WSEndpointImpl getEndpoint() {
      return this._endpoint;
   }

   public static SequenceFaultMsg getRmFaultFromMessage(Packet var0) throws SOAPException {
      Message var1 = var0.getMessage();
      if (var1 != null && var1.isFault()) {
         SOAPMessage var3 = null;

         try {
            var3 = var1.readAsSOAPMessage(var0);
            SequenceFaultMsg var2 = SequenceFaultMsgFactory.getInstance().parseSoapFault(var3);
            var1.consume();
            return var2;
         } catch (SequenceFaultException var5) {
            if (var3 != null) {
               var1 = Messages.create(var3);
               var0.setMessage(var1);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public InboundMessageResult processInboundMessage(Packet var1) throws WsrmException, XMLStreamException, PolicyException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Incoming packet security: " + dumpSecuritySSLOnPacket(var1) + " " + dumpSecuritySCTOnPacket(var1));
      }

      InboundMessageResult var2 = new InboundMessageResult();
      var2.handled = false;
      var2.message = null;
      var2.seq = null;
      this._seqTerminatedFlag = false;
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      if (var3.getInternalReceive()) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Detected an 'internal receive' message. Ignoring it.");
         }

         return var2;
      } else {
         WsrmInvocationPropertyBag.flagPersistentPropsOnPacket(var1);
         WsrmPropertyBag.flagPersistentPropsOnPacket(var1);
         SourceSequence var4 = getOutboundSourceSequenceForPacket(var1);
         if (var4 != null) {
            this.updateSecurityPropertiesIntoSequence(var1, var4);
         }

         var2.seq = var4;
         Message var5 = var1.getMessage();
         if (var5 == null) {
            return var2;
         } else {
            AddressingVersion var6 = this._binding.getAddressingVersion();
            SOAPVersion var7 = this._binding.getSOAPVersion();
            String var8 = var5.getHeaders().getAction(var6, var7);
            if (var8 == null) {
               var8 = var1.soapAction;
               if (var8 == null) {
                  var8 = "";
               }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Processing inbound message with action: " + var8);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Checking inbound action '" + var8 + "' against all possible RM actions: " + WsrmConstants.Action.dumpAllActionsForAllRMVersions());
            }

            if (!WsrmConstants.Action.matchesAnyActionAndRMVersion(var8)) {
               WsrmFaultMsg var9;
               if ((var9 = handlePossibleRmFault(var1)) == null) {
                  if (!var8.equals(WsUtil.getSOAPFaultAction(this._binding.getAddressingVersion()))) {
                     var2 = this.processInboundSequenceMessage(var1);
                  }
               } else {
                  var2.handled = true;
                  var2.message = var5;
                  var2.rmFault = var9;
               }
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Processing inbound WS-RM action: " + var8);
               }

               WsrmConstants.Action.VersionInfo var11 = WsrmConstants.Action.getVersionInfo(var8);
               MessageProcessor var10 = MessageProcessorFactory.createProcessorForAction(var11.action, this);
               var10.handleInbound(var1, var11.rmVersion, var2);
               var2.handled = true;
            }

            if (!this._seqTerminatedFlag) {
               this.handleInboundPiggybackRmHeaders(var1, var2.needSuspendOnCurrentFiber);
            }

            return var2;
         }
      }
   }

   private static WsrmFaultMsg handlePossibleRmFault(Packet var0) throws WsrmException {
      SequenceFaultMsg var1;
      try {
         var1 = getRmFaultFromMessage(var0);
      } catch (Exception var3) {
         throw new WsrmException(var3.toString(), var3);
      }

      if (var1 != null) {
         SourceSequenceManager.getInstance().handleRmFault(var1);
         DestinationSequenceManager.getInstance().handleRmFault(var1);
      }

      return var1;
   }

   public void setOutboundSequence(Packet var1, Sequence var2) {
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      var3.setOutboundSequenceId(var2.getId());
   }

   public void setOutboundSequenceId(Packet var1, String var2) {
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      var3.setOutboundSequenceId(var2);
   }

   public void setInboundSequence(Packet var1, Sequence var2) {
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      var3.setInboundSequenceId(var2.getId());
   }

   public InboundMessageResult processInboundSequenceMessage(Packet var1) {
      InboundMessageResult var2 = new InboundMessageResult();
      var2.message = null;
      var2.handled = false;
      Message var3 = var1.getMessage();
      WsrmConfig.Destination var4 = WsrmConfig.getDestination(this._serverContext, var1, this._clientSide);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Inbound sequence message config:\n" + var4.toString());
      }

      Header var5 = null;
      WsrmConstants.RMVersion[] var6 = WsrmConstants.RMVersion.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         WsrmConstants.RMVersion var9 = var6[var8];
         QName var10 = WsrmHeader.getQName(SequenceHeader.class, var9);
         var5 = var3.getHeaders().get(var10, true);
         if (var5 != null) {
            break;
         }
      }

      if (var5 == null) {
         if (var4.isReliable() && !var4.getRmAssertion().isOptional()) {
            WsrmConstants.RMVersion var22 = var4.getRmVersion();
            if (var22 == WsrmConstants.RMVersion.RM_10) {
               throw new RuntimeException("This endpoint requires reliable messaging");
            }

            WSRMRequiredFaultMsg var24 = new WSRMRequiredFaultMsg(var22);

            try {
               var2.message = Rpc2WsUtil.createMessageFromFaultMessage(var24, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
               var2.handled = true;
            } catch (Exception var18) {
               WseeRmLogger.logUnexpectedException(var18.toString(), var18);
               throw new RuntimeException(var18.toString(), var18);
            }
         }

         return var2;
      } else {
         WsrmPropertyBag var21 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
         var21.capturePersistentPropsFromJaxWsRi(this._binding.getAddressingVersion());
         WSDLBoundOperation var23 = var1.getMessage().getOperation(this._wsdlPort);
         if (var23 != null) {
            var21.setInboundWsdlOperationName(var23.getName().getLocalPart());
         }

         String var25 = var3.getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         var21.setInboundMessageId(var25);
         boolean var26 = var1.getMessage().isOneWay(this.getWsdlPort());
         if (var26) {
            TransportBackChannel var27 = var1.keepTransportBackChannelOpen();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("RM is hijacking the TransportBackChannel (to allow response to oneway method) for msgId=" + var25);
            }

            var21.setTransportBackChannel(var27);
         }

         try {
            SequenceHeader var28 = (SequenceHeader)WsrmHeaderFactory.getInstance().createWsrmHeaderFromHeader(SequenceHeader.class, var5);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Handling inbound sequence message with msg id " + var25 + ", seq " + var28.getSequenceId() + " and msg num " + var28.getMessageNumber());
            }

            DestinationSequence var11 = DestinationSequenceManager.getInstance().getSequence(var28.getRmVersion(), var28.getSequenceId(), !this._clientSide);
            if (var11 == null) {
               return var2;
            }

            if (!var11.isNonBuffered()) {
               WSEndpointReference var12 = var1.getMessage().getHeaders().getReplyTo(var11.getAddressingVersion(), var11.getSoapVersion());
               if ((var12 == null || var12.isAnonymous()) && !this._clientSide) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("ERROR:  Terminate Sequence and throw WsrmUnsupportedConfigurationException after Error:  Attempt to execute a WS-RM Buffered Service with a message sent using Synchronous HTTP Transport with a WS-Addressing ReplyTo Header value of anonymous.  The WS-RM Buffered Service can only be invoked via messages sent using Asynchronous HTTP Transport with a non-anonymous WS-Addressing ReplyTo Header.");
                  }

                  WsrmConstants.RMVersion var30 = var4.getRmVersion();
                  TerminateSequenceProcessor.forceTerminateSequence(var11, var30, 1L);
                  this._seqTerminatedFlag = true;
                  throw new WsrmUnsupportedConfigurationException(var30, var28.getSequenceId(), this._clientSide, "Error:  Attempt to execute a WS-RM Buffered Service with a message sent using Synchronous HTTP Transport with a WS-Addressing ReplyTo Header value of anonymous.  The WS-RM Buffered Service can only be invoked via messages sent using Asynchronous HTTP Transport with a non-anonymous WS-Addressing ReplyTo Header.  If you are using a WebLogic client check to be sure that it is using the AsyncClientTransportFeature with Asynchronous Transport.");
               }
            }

            validateSecurityOnInboundPacket(var11, var1);
            String var29 = var11.getOffer() != null ? var11.getOffer().getId() : null;
            var21.setInboundSequenceId(var11.getId());
            var21.setOutboundSequenceId(var29);
            var21.setInboundEndpointAddress(var11.getHostEpr().getAddress());
            DestinationSequenceManager.HandleSequenceMessageResult var13 = DestinationSequenceManager.getInstance().handleSequenceMessage(var28, var1, this._binding.getAddressingVersion(), this._binding.getSOAPVersion(), var26);
            DestinationMessageInfo var14 = var13.msgInfo;
            var2.needSuspendOnCurrentFiber = var13.needSuspendOnCurrentFiber;
            var2.currentFiberSuspendingCallback = var13.currentFiberSuspendingCallback;
            if (var14 != null && var11.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var14.isEmptyLastMessage()) {
               var2.handled = true;
               WSEndpointReference var15 = var1.getMessage().getHeaders().getReplyTo(var11.getAddressingVersion(), var11.getSoapVersion());
               if (var11.getAcksToEpr() != null && !var11.getAcksToEpr().isAnonymous() || var15 != null && !var15.isAnonymous()) {
                  var2.message = null;
               } else {
                  var2.message = Messages.createEmpty(var11.getSoapVersion());
                  String var16 = WsrmConstants.Action.ACK.getActionURI(WsrmConstants.RMVersion.RM_10);
                  StringHeader var17 = new StringHeader(var11.getAddressingVersion().actionTag, var16);
                  var2.message.getHeaders().add(var17);
               }
            } else if (!var11.isNonBuffered()) {
               var2.message = null;
               var2.handled = true;
            } else if (var14 == null) {
               var2.message = null;
               var2.handled = true;
            }
         } catch (WsrmException var19) {
            var2.message = handleInboundException(var19, this._addrVersion, this._soapVersion);
         } catch (WsrmFaultException var20) {
            var2.message = handleInboundException(var20, this._addrVersion, this._soapVersion);
         }

         return var2;
      }
   }

   public static Message handleInboundException(Exception var0, AddressingVersion var1, SOAPVersion var2) {
      Message var3;
      if (var0 instanceof UnknownSequenceException) {
         UnknownSequenceException var4 = (UnknownSequenceException)var0;
         UnknownSequenceFaultMsg var5 = new UnknownSequenceFaultMsg(var4.getRmVersion());
         var5.setSequenceId(var4.getSequenceId());

         try {
            var3 = Rpc2WsUtil.createMessageFromFaultMessage(var5, var1, var2);
         } catch (Exception var10) {
            WseeRmLogger.logUnexpectedException(var0.toString(), var0);
            WseeRmLogger.logUnexpectedException(var10.toString(), var10);
            var3 = null;
         }
      } else if (var0 instanceof WsrmUnsupportedConfigurationException) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Process " + var0.getClass().getName() + " with message '" + var0.getMessage() + "'");
         }

         WsrmUnsupportedConfigurationException var11 = (WsrmUnsupportedConfigurationException)var0;
         SequenceTerminatedFaultMsg var13 = new SequenceTerminatedFaultMsg(var11.getRMVersion(), var11.isClient());
         var13.setReason(var11.getMessage());
         var13.setSequenceId(var11.getSequenceId());

         try {
            var3 = Rpc2WsUtil.createMessageFromFaultMessage(var13, var1, var2);
         } catch (Exception var9) {
            WseeRmLogger.logUnexpectedException(var0.toString(), var0);
            WseeRmLogger.logUnexpectedException(var9.toString(), var9);
            var3 = null;
         }
      } else if (var0 instanceof WsrmFaultException && ((WsrmFaultException)var0).getWsrmFaultMsg() != null) {
         WsrmFaultException var12 = (WsrmFaultException)var0;
         WsrmFaultMsg var14 = var12.getWsrmFaultMsg();

         try {
            var3 = Rpc2WsUtil.createMessageFromFaultMessage(var14, var1, var2);
         } catch (Exception var8) {
            WseeRmLogger.logUnexpectedException(var0.toString(), var0);
            WseeRmLogger.logUnexpectedException(var8.toString(), var8);
            var3 = null;
         }
      } else {
         try {
            var3 = WsUtil.createMessageFromThrowable(var0, var1, var2);
         } catch (Exception var7) {
            WseeRmLogger.logUnexpectedException(var0.toString(), var0);
            WseeRmLogger.logUnexpectedException(var7.toString(), var7);
            var3 = null;
         }
      }

      return var3;
   }

   Packet handleOutboundException(Packet var1, WsrmException var2) {
      for(Object var3 = var2; var3 != null; var3 = ((Throwable)var3).getCause()) {
         if (var3 instanceof WsrmException) {
            Packet var4 = this.internalHandleOutboundException(var1, (WsrmException)var3);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   private Packet internalHandleOutboundException(Packet var1, WsrmException var2) {
      Packet var3 = null;
      if (var2 instanceof UnknownSequenceException) {
         UnknownSequenceException var5 = (UnknownSequenceException)var2;
         UnknownSequenceFaultMsg var6 = new UnknownSequenceFaultMsg(var5.getRmVersion());
         var6.setSequenceId(var5.getSequenceId());

         try {
            Message var4 = Rpc2WsUtil.createMessageFromFaultMessage(var6, this._addrVersion, this._soapVersion);
            var3 = var1.createServerResponse(var4, this._addrVersion, this._soapVersion, WsUtil.getSOAPFaultAction(this._addrVersion));
         } catch (Exception var8) {
            WseeRmLogger.logUnexpectedException(var8.toString(), var8);
         }
      }

      return var3;
   }

   public String getMessageID(Message var1) throws WsrmException {
      String var2 = var1.getHeaders().getMessageID(this._addrVersion, this._soapVersion);
      if (var2 != null && var2.length() >= 1) {
         return var2;
      } else {
         throw new WsrmException(WseeRmLogger.logNullMessageIDLoggable().getMessage());
      }
   }

   public static Sequence doInitialSetupForAction(String var0, boolean var1, String var2, WsrmConstants.RMVersion var3, Packet var4) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINER)) {
         LOGGER.finer("Setting up for action '" + var0 + "' on " + (var1 ? "source" : "destination") + " side seq: " + var2);
      }

      Object var5;
      if (var1) {
         var5 = SourceSequenceManager.getInstance().getSequence(var3, var2, false);
      } else {
         var5 = DestinationSequenceManager.getInstance().getSequence(var3, var2, false);
      }

      if (var3 != ((Sequence)var5).getRmVersion()) {
         throw new IllegalRMVersionFaultException(var2, var3, ((Sequence)var5).getRmVersion());
      } else {
         validateSecurityOnInboundPacket((Sequence)var5, var4);
         return (Sequence)var5;
      }
   }

   public static void validateSecurityOnInboundPacket(Sequence var0, Packet var1) {
      WsrmSecurityContext var2 = var0.getSecurityContext();
      if (var1 != null) {
         if (var2.isSecureWithSSL() && KernelStatus.isServer()) {
            String var7 = null;
            if (LOGGER.isLoggable(Level.FINE)) {
               var7 = "Packet (" + dumpSecuritySSLOnPacket(var1) + ") against sequence (" + dumpSecuritySSL(var2.getSSLSessionId(), var2.getSSLCertChain()) + ")";
               LOGGER.fine("Validating SSL Security On " + var7);
            }

            byte[] var8 = getSSLSessionId(var1);
            if (!Arrays.equals(var8, var2.getSSLSessionId()) && !compareSSLProperties(var2, var1)) {
               if (LOGGER.isLoggable(Level.FINER)) {
                  LOGGER.fine("SSL Security Mismatch Detected!: " + var7);
               }

               throw new SecurityMismatchFaultException(var0.getId(), var0.getRmVersion(), true);
            }
         } else if (var2.isSecure()) {
            SCCredential var3 = getSCCredential(var1);
            String var4 = null;
            if (LOGGER.isLoggable(Level.FINE)) {
               var4 = "Packet (" + dumpSecuritySCTOnPacket(var1) + ") against sequence (" + dumpSecuritySCT(var2.getSCCredential()) + ")";
               LOGGER.fine("Validating SCT Security On " + var4);
            }

            boolean var5 = var1.getMessage() != null && var1.getMessage().getHeaders().getRelatesTo(var0.getAddressingVersion(), var0.getSoapVersion()) != null;
            boolean var6 = false;
            if (!var5 && (var3 == null || !var3.equals(var2.getSCCredential()))) {
               var6 = true;
            }

            if (var6 && LOGGER.isLoggable(Level.FINER)) {
               LOGGER.fine("SCT Security Mismatch Detected!: " + var4);
            }
         }
      }

   }

   private static boolean compareSSLProperties(WsrmSecurityContext var0, Packet var1) {
      X509Certificate[] var2 = var0.getSSLCertChain();
      X509Certificate[] var3 = getSSLCertChain(var1);
      if (var2 == null) {
         return true;
      } else if (var3 == null) {
         return false;
      } else if (var2.length == var3.length) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            X509Certificate var5 = var2[var4];
            X509Certificate var6 = var3[var4];
            if (!compareCerts(var5, var6)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean compareCerts(X509Certificate var0, X509Certificate var1) {
      return var0 == null && var1 == null || getCertKey(var0).equals(getCertKey(var1));
   }

   private static String getCertKey(X509Certificate var0) {
      return var0.getIssuerX500Principal().getName() + var0.getSerialNumber();
   }

   public WsrmSecurityContext getWsrmSecurityContext(PolicyAssertion var1, Packet var2) throws WsrmException {
      try {
         WsrmSecurityContext var3 = this._policyHelper.createSecurityContext(var1);
         var3.newInitializedMap();
         var3.update(var2);
         return var3;
      } catch (Exception var5) {
         throw new WsrmException(var5.toString(), var5);
      }
   }

   private void handleInboundPiggybackRmHeaders(Packet var1, boolean var2) throws WsrmException {
      if (var1 != null && var1.getMessage() != null) {
         Message var3 = var1.getMessage();
         String var4 = null;
         if (this._binding.getAddressingVersion() != null) {
            var4 = var3.getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         }

         WsrmConstants.RMVersion[] var5 = WsrmConstants.RMVersion.values();
         HeaderList var6 = var3.getHeaders();
         WsrmConstants.RMVersion[] var8 = var5;
         int var9 = var5.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            WsrmConstants.RMVersion var11 = var8[var10];
            QName var7 = WsrmConstants.Element.ACK.getQName(var11);
            Iterator var12 = var6.getHeaders(var7, true);

            while(var12.hasNext()) {
               AcknowledgementHeader var13 = (AcknowledgementHeader)WsrmHeaderFactory.getInstance().createWsrmHeaderFromHeader(AcknowledgementHeader.class, (Header)var12.next());
               this.handleAckHeader(var13, var2);
            }

            var7 = WsrmConstants.Element.ACK_REQUESTED.getQName(var11);
            Iterator var15 = var6.getHeaders(var7, true);

            while(var15.hasNext()) {
               AckRequestedHeader var14 = (AckRequestedHeader)WsrmHeaderFactory.getInstance().createWsrmHeaderFromHeader(AckRequestedHeader.class, (Header)var15.next());
               this.handleAckRequestedHeader(var14, var4);
            }
         }

      }
   }

   public void handleAckRequestedHeader(AckRequestedHeader var1, String var2) throws WsrmException {
      DestinationSequenceManager.getInstance().handleAckRequest(var1, var2);
   }

   public void handleAckHeader(AcknowledgementHeader var1, boolean var2) throws WsrmException {
      final SourceSequence var3 = SourceSequenceManager.getInstance().getSequence(var1.getRmVersion(), var1.getSequenceId(), true);
      if (var3 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Handling ack header: " + var1);
         }

         boolean var4 = var3.isComplete();
         SourceSequenceManager.getInstance().handleAck(var1, var3);
         if (!var4 && var3.isComplete()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Found final ack, and starting terminate process with source sequence " + var3.getId());
            }

            if (var2) {
               final Fiber.CompletionCallback var5 = Fiber.current().getCompletionCallback();
               Fiber.current().setCompletionCallback(new Fiber.CompletionCallback() {
                  public void onCompletion(@NotNull Packet var1) {
                     try {
                        WsrmTubeUtils.sendTerminateSequence(var3, WsrmTubeUtils.this._dispatchFactory);
                     } catch (WsrmException var7) {
                        if (WsrmTubeUtils.LOGGER.isLoggable(Level.SEVERE)) {
                           WsrmTubeUtils.LOGGER.log(Level.SEVERE, "sendTerminateSequence failed: " + var7.toString(), var7);
                        }

                        throw new RuntimeException(var7.toString(), var7);
                     } finally {
                        if (var5 != null) {
                           var5.onCompletion(var1);
                        }

                     }

                  }

                  public void onCompletion(@NotNull Throwable var1) {
                     try {
                        WsrmTubeUtils.sendTerminateSequence(var3, WsrmTubeUtils.this._dispatchFactory);
                     } catch (WsrmException var7) {
                        if (WsrmTubeUtils.LOGGER.isLoggable(Level.SEVERE)) {
                           WsrmTubeUtils.LOGGER.log(Level.SEVERE, "sendTerminateSequence failed: " + var7.toString(), var7);
                        }

                        throw new RuntimeException(var7.toString(), var7);
                     } finally {
                        if (var5 != null) {
                           var5.onCompletion(var1);
                        }

                     }

                  }
               });
            } else {
               sendTerminateSequence(var3, this._dispatchFactory);
            }
         }

      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** IGNORING ack header for unknown source sequence " + var1.getSequenceId());
         }

      }
   }

   public static void addPiggybackHeaders(Packet var0, String var1, AddressingVersion var2, SOAPVersion var3) throws WsrmException {
      List var4 = SourceSequenceManager.getInstance().getSequencesForPiggybackEndpoint(var1);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         SourceSequence var6 = (SourceSequence)var5.next();
         if (piggybackHeadersForSequenceOntoPacket(var6, var0, var2, var3)) {
            SourceSequenceManager.getInstance().updateSequence(var6);
         }
      }

      List var11 = DestinationSequenceManager.getInstance().getSequencesForPiggybackEndpoint(var1);
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         DestinationSequence var7 = (DestinationSequence)var12.next();
         if (piggybackHeadersForSequenceOntoPacket(var7, var0, var2, var3)) {
            DestinationSequenceManager.getInstance().updateSequence(var7);
         }
      }

      boolean var13 = false;
      AddressingVersion[] var14 = AddressingVersion.values();
      int var8 = var14.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         AddressingVersion var10 = var14[var9];
         if (var10.anonymousUri.equals(var1)) {
            var13 = true;
            break;
         }
      }

      if (var13) {
         Sequence var15 = getRelatedSequenceForAnonymousOutgoingPacket(var0);
         if (var15 != null && piggybackHeadersForSequenceOntoPacket(var15, var0, var2, var3)) {
            if (var15 instanceof SourceSequence) {
               SourceSequenceManager.getInstance().updateSequence((SourceSequence)var15);
            } else {
               DestinationSequenceManager.getInstance().updateSequence((DestinationSequence)var15);
            }
         }

      }
   }

   private static boolean piggybackHeadersForSequenceOntoPacket(Sequence var0, Packet var1, AddressingVersion var2, SOAPVersion var3) throws WsrmException {
      boolean var4 = false;
      List var5 = var0.getAndClearPiggybackHeaders();
      if (var5.size() > 0) {
         ensureMessageInPacket(var1, getFallbackActionForSequence(var0), var2, var3);
         ArrayList var6 = new ArrayList(var5);
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            WsrmHeader var8 = (WsrmHeader)var7.next();
            if (var1.getMessage().getHeaders().get(var8.getName(), false) == null) {
               var4 = true;
               var6.remove(var8);
               var1.getMessage().getHeaders().addOrReplace(var8);
            }
         }

         if (var6.size() > 0) {
            var0.putBackUnusedPiggybackHeaders(var6);
         }
      }

      return var4;
   }

   private static String getFallbackActionForSequence(Sequence var0) {
      return WsrmConstants.Action.ACK.getActionURI(var0.getRmVersion());
   }

   private static void ensureMessageInPacket(Packet var0, String var1, AddressingVersion var2, SOAPVersion var3) throws WsrmException {
      Message var4 = var0.getMessage();
      if (var4 == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("!! Created an empty body outbound message to hold piggyback headers. Action is: " + var1);
         }

         try {
            var4 = WsUtil.createEmptyMessage(var1, var2, var3);
            var0.setMessage(var4);
         } catch (Exception var6) {
            throw new WsrmException(var6.toString(), var6);
         }
      }

   }

   @Nullable
   private static Sequence getRelatedSequenceForAnonymousOutgoingPacket(Packet var0) throws UnknownSequenceException {
      Sequence var1 = getAssociatedSequenceForAnonymousOutgoingPacket(var0);
      if (var1 == null) {
         return null;
      } else {
         Sequence var2 = getRelatedAnonymousSequence(var1);
         return var2;
      }
   }

   @Nullable
   private static Sequence getRelatedAnonymousSequence(Sequence var0) {
      Object var1 = null;
      if (var0 instanceof SourceOfferSequence) {
         var1 = ((SourceOfferSequence)var0).getMainSequence();
      } else if (var0 instanceof SourceSequence) {
         var1 = ((SourceSequence)var0).getOffer();
      } else if (var0 instanceof DestinationSequence) {
         var1 = (DestinationSequence)var0;
      }

      return (Sequence)(var1 == null || ((DestinationSequence)var1).getAcksToEpr() != null && !((DestinationSequence)var1).getAcksToEpr().isAnonymous() ? null : var1);
   }

   @Nullable
   private static Sequence getAssociatedSequenceForAnonymousOutgoingPacket(Packet var0) throws UnknownSequenceException {
      WsrmPropertyBag var1 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var0);
      String var2 = var1.getOutboundSequenceId();
      if (var2 == null && var0.getMessage() == null) {
         var2 = var1.getInboundSequenceId();
      }

      if (var2 == null) {
         return null;
      } else {
         Object var3 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
         if (var3 == null) {
            var3 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
         }

         return (Sequence)var3;
      }
   }

   public static void sendTerminateSequence(SourceSequence var0, DispatchFactory var1) throws WsrmException {
      if (var0.getState().isValidTransition(SequenceState.TERMINATING)) {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Sending TerminateSequence on source sequence " + var0.getId());
            }

            TerminateSequenceMsg var2 = createTerminateSequenceMessage(var0);
            String var3 = Rpc2WsUtil.getSOAPActionFromHandshakeMessage(var2);
            Message var4 = Rpc2WsUtil.createMessageFromHandshakeMessage(var2, var0.getAddressingVersion(), var0.getSoapVersion());
            WsUtil.getOrSetMessageID(var4, var0.getAddressingVersion(), var0.getSoapVersion(), var0.getPhysicalStoreName());
            WSEndpointReference var5 = var0.getEndpointEpr();
            if (var5.isAnonymous()) {
               if (var0.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
                  if (var0.getOffer() != null) {
                     var0.getOffer().addFinalAckToMessage(var4);
                  }

                  var0.setState(SequenceState.TERMINATING);
                  TerminateSequenceResponseProcessor.forceAnonymousTerminateSequenceResponse((SourceOfferSequence)var0);
               }
            } else {
               WSEndpointReference var6 = calculateReplyToForSourceSequence(var0);
               Sender var7 = new Sender(var1);
               Map var8 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)var0, (Packet)null);
               var7.send(var4, var3, var5, var6, var8);
            }

         } catch (Exception var9) {
            throw new WsrmException(var9.toString(), var9);
         }
      }
   }

   public static WSEndpointReference calculateReplyToForSourceSequence(SourceSequence var0) {
      WSEndpointReference var1 = null;
      if (var0 instanceof SourceOfferSequence) {
         var1 = var0.getAcksToEpr();
      } else {
         ClientInstanceIdentity var2 = var0.getCreatingClientInstanceId();
         if (var2 != null) {
            var1 = getReplyToForClient(var2.getClientId(), var0.isUsingSsl());
         }

         if (var1 == null) {
            var1 = var0.getAcksToEpr();
         }
      }

      return var1;
   }

   private static WSEndpointReference getReplyToForClient(String var0, boolean var1) {
      ClientIdentityRegistry.ClientInfo var2 = ClientIdentityRegistry.getClientInfo(var0);
      if (var2 == null) {
         throw new IllegalArgumentException("No client with identity '" + var0 + "' has been registered");
      } else {
         WSEndpointReference var3 = null;
         OneWayFeature var4 = (OneWayFeature)var2.getOriginalConfig().getFeatures().get(OneWayFeature.class);
         if (var4 == null) {
            AsyncClientTransportFeature var5 = (AsyncClientTransportFeature)var2.getOriginalConfig().getFeatures().get(AsyncClientTransportFeature.class);
            if (var5 != null) {
               var3 = new WSEndpointReference(var5.getEndpointReference(W3CEndpointReference.class, new Element[0]));
            }
         } else {
            var3 = var4.getReplyTo(var1);
         }

         return var3;
      }
   }

   public static TerminateSequenceMsg createTerminateSequenceMessage(SourceSequence var0) throws WsrmException {
      if (var0.getDestinationId() == null) {
         String var2 = getCurrentMethod();
         throw new WsrmException(WseeRmLogger.logCannotInteractWithPendingSequenceLoggable(var2, var0.getId()).getMessage());
      } else {
         TerminateSequenceMsg var1 = new TerminateSequenceMsg(var0.getRmVersion(), var0.getDestinationId());
         var1.setLastMsgNumber(var0.getMaxMessageNum());
         var1.setMustUnderstand(true);
         return var1;
      }
   }

   public static void sendCloseSequence(SourceSequence var0, DispatchFactory var1) throws WsrmException {
      if (var0.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         if (var0.getState().isValidTransition(SequenceState.CLOSING)) {
            try {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** Sending CloseSequence on source sequence " + var0.getId());
               }

               CloseSequenceMsg var2 = createCloseSequenceMessage(var0);
               String var3 = Rpc2WsUtil.getSOAPActionFromHandshakeMessage(var2);
               Message var4 = Rpc2WsUtil.createMessageFromHandshakeMessage(var2, var0.getAddressingVersion(), var0.getSoapVersion());
               WsUtil.getOrSetMessageID(var4, var0.getAddressingVersion(), var0.getSoapVersion(), var0.getPhysicalStoreName());
               WSEndpointReference var5 = var0.getEndpointEpr();
               if (var5.isAnonymous()) {
                  CloseSequenceResponseProcessor.forceAnonymousCloseSequenceResponse((SourceOfferSequence)var0);
               } else {
                  Sender var6 = new Sender(var1);
                  Map var7 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)var0, (Packet)null);
                  WSEndpointReference var8 = calculateReplyToForSourceSequence(var0);
                  var6.send(var4, var3, var5, var8, var7);
               }

            } catch (Exception var9) {
               throw new WsrmException(var9.toString(), var9);
            }
         }
      }
   }

   public static CloseSequenceMsg createCloseSequenceMessage(SourceSequence var0) throws WsrmException {
      if (var0.getDestinationId() == null) {
         String var2 = getCurrentMethod();
         throw new WsrmException(WseeRmLogger.logCannotInteractWithPendingSequenceLoggable(var2, var0.getId()).getMessage());
      } else {
         CloseSequenceMsg var1 = new CloseSequenceMsg(var0.getRmVersion(), var0.getDestinationId());
         var1.setLastMsgNumber(var0.getMaxMessageNum());
         var1.setMustUnderstand(true);
         return var1;
      }
   }

   public static String getCurrentMethod() {
      StackTraceElement[] var0 = (new Exception()).getStackTrace();
      return var0[1].getMethodName();
   }

   public static void sendAcknowledgementRequest(SourceSequence var0, DispatchFactory var1) throws WsrmException {
      if (var0.getState() == SequenceState.CREATED || var0.getState() == SequenceState.CLOSING || var0.getState() == SequenceState.CLOSED) {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Sending AcknowledgementRequest on source sequence " + var0.getId());
            }

            AckRequestedHeader var2 = new AckRequestedHeader(var0.getRmVersion());
            WSEndpointReference var3 = var0.getEndpointEpr();
            if (var3.isAnonymous()) {
               var0.addPiggybackHeader(var2);
            } else {
               String var4 = WsrmConstants.Action.ACK_REQUESTED.getActionURI(var0.getRmVersion());
               Message var5 = WsUtil.createEmptyMessage(var4, var0.getAddressingVersion(), var0.getSoapVersion());
               var5.getHeaders().add(var2);
               WsUtil.getOrSetMessageID(var5, var0.getAddressingVersion(), var0.getSoapVersion(), var0.getPhysicalStoreName());
               Sender var6 = new Sender(var1);
               Map var7 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)var0, (Packet)null);
               var6.send(var5, var4, var3, var0.getAcksToEpr(), var7);
            }

         } catch (Exception var8) {
            throw new WsrmException(var8.toString(), var8);
         }
      }
   }

   public OutboundMessageResult processOutboundMessage(Packet var1, SequenceIdFactory var2, String var3) throws WsrmException, XMLStreamException, PolicyException {
      OutboundMessageResult var4 = null;
      this.getRmInvokeProps(var1);
      WsrmInvocationPropertyBag.flagPersistentPropsOnPacket(var1);
      WsrmPropertyBag.flagPersistentPropsOnPacket(var1);
      WsrmPropertyBag var5 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      Message var6 = var1.getMessage();
      if (var6 == null) {
         DestinationMessageInfo var7 = var5.getDestMessageInfoFromRequest();
         if (var7 != null) {
            DestinationSequenceManager.getInstance().handleNoResponseSequenceMessage(var7);
         }
      }

      addPiggybackHeaders(var1, var3, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      var6 = var1.getMessage();
      AddressingVersion var12 = this._binding.getAddressingVersion();
      SOAPVersion var8 = this._binding.getSOAPVersion();
      String var9 = var6 != null ? var6.getHeaders().getAction(var12, var8) : null;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Processing outbound action: " + var9);
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Checking outbound action '" + var9 + "' against all possible RM actions: " + WsrmConstants.Action.dumpAllActionsForAllRMVersions());
      }

      if (!WsrmConstants.Action.matchesAnyActionAndRMVersion(var9)) {
         if (handlePossibleRmFault(var1) == null) {
            if (var9 != null && !var9.equals(WsUtil.getSOAPFaultAction(this._binding.getAddressingVersion()))) {
               NormalizedExpression var10 = this.getEffectivePolicyForPacket(var1, !this._clientSide);
               if (var10 != null) {
                  var4 = this.processOutboundSequenceMessage(var1, var2, var3);
                  if (!var4.messageBuffered) {
                     if (!this._clientSide && var1.invocationProperties.get("weblogic.wsee.policy.effectiveResponsePolicy") == null) {
                        var1.invocationProperties.put("weblogic.wsee.policy.effectiveResponsePolicy", var10);
                     } else if (this._clientSide && var1.invocationProperties.get("weblogic.wsee.policy.effectiveRequestPolicy") == null) {
                        var1.invocationProperties.put("weblogic.wsee.policy.effectiveRequestPolicy", var10);
                     }
                  }
               } else if (LOGGER.isLoggable(Level.FINE)) {
                  String var11 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
                  LOGGER.fine("Failed to find policy for outbound message with msg id " + var11 + ". SKIPPING!");
               }
            } else if (!this._clientSide) {
               var5.restorePersistentPropsIntoJaxWsRi(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            }
         }
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Processing outbound WS-RM action: " + var9);
         }

         if (!this._clientSide) {
            WsaPropertyBag var13 = (WsaPropertyBag)var1.getSatellite(WsaPropertyBag.class);
            if (var13 == null) {
               var13 = new WsaPropertyBag(var12, var8, var1);
               var1.addSatellite(var13);
            }

            if (var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest") == null) {
               var1.put("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest", var1.invocationProperties.remove("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest"));
               var1.put("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest", var1.invocationProperties.remove("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest"));
            }
         }

         WsrmConstants.Action.VersionInfo var14 = WsrmConstants.Action.getVersionInfo(var9);
         MessageProcessor var15 = MessageProcessorFactory.createProcessorForAction(var14.action, this);
         if (!var15.processOutbound(var1, var14.rmVersion, var2, var3)) {
            var4 = new OutboundMessageResult();
            var4.messageAborted = true;
         }
      }

      if (var6 != null) {
         WsUtil.getOrSetMessageID(var6, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      }

      if (var4 == null) {
         var4 = new OutboundMessageResult();
         var4.messageBuffered = false;
      }

      if (!var4.messageBuffered && !var4.messageAborted) {
         SourceSequence var16 = getOutboundSourceSequenceForPacket(var1);
         if (var16 != null) {
            SourceSequenceManager.getInstance().setInvokePropertiesOntoPacket(var16, var1);
         }
      }

      if (var5.getTransportBackChannel() != null) {
         var1.transportBackChannel = var5.getTransportBackChannel();
         var5.setTransportBackChannel((TransportBackChannel)null);
      }

      return var4;
   }

   static void initializeInboundMessage(Packet var0) throws WsrmException {
      Sequence var1 = getSequenceForPacket(var0);
      if (var1 instanceof SourceSequence) {
         SourceSequenceManager.getInstance().setInvokePropertiesOntoPacket((SourceSequence)var1, var0);
      } else if (var1 instanceof DestinationSequence) {
         DestinationSequenceManager.getInstance().setInvokePropertiesOntoPacket((DestinationSequence)var1, var0);
      }

   }

   public static SCCredential getSCCredential(Packet var0) {
      SCCredential var1 = null;
      if (var0.proxy != null) {
         var1 = (SCCredential)var0.proxy.getRequestContext().get("weblogic.wsee.wssc.sct");
      }

      if (var1 == null) {
         var1 = (SCCredential)var0.invocationProperties.get("weblogic.wsee.wssc.sct");
      }

      return var1;
   }

   public static byte[] getSSLSessionId(Packet var0) {
      if (var0 != null && var0.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var1 = (HttpServletRequest)var0.get("javax.xml.ws.servlet.request");
         SSLSession var2 = (SSLSession)var1.getAttribute("weblogic.servlet.request.sslsession");
         return var2 != null ? var2.getId() : null;
      } else {
         return null;
      }
   }

   public static boolean isSSLRequest(Packet var0) {
      if (var0 != null && var0.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var1 = (HttpServletRequest)var0.get("javax.xml.ws.servlet.request");
         return var1.isSecure();
      } else {
         return false;
      }
   }

   public static X509Certificate[] getSSLCertChain(Packet var0) {
      if (var0 != null && var0.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var1 = (HttpServletRequest)var0.get("javax.xml.ws.servlet.request");
         return (X509Certificate[])((X509Certificate[])var1.getAttribute("javax.servlet.request.X509Certificate"));
      } else {
         return null;
      }
   }

   private static String dumpSecuritySSLOnPacket(Packet var0) {
      if (var0 != null && var0.supports("javax.xml.ws.servlet.request")) {
         HttpServletRequest var1 = (HttpServletRequest)var0.get("javax.xml.ws.servlet.request");
         SSLSession var2 = (SSLSession)var1.getAttribute("weblogic.servlet.request.sslsession");
         X509Certificate[] var3 = (X509Certificate[])((X509Certificate[])var1.getAttribute("javax.servlet.request.X509Certificate"));
         return dumpSecuritySSL(var2 != null ? var2.getId() : null, var3);
      } else {
         return null;
      }
   }

   public static String dumpSecuritySSL(byte[] var0, X509Certificate[] var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = var0 != null ? _base64.encodeBuffer(var0) : null;
      var2.append("SSLSessionID=").append(var3);
      var2.append(" ");
      var2.append("X509Certificates=");
      if (var1 == null) {
         var2.append("null");
      } else {
         var2.append("[");
         X509Certificate[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            X509Certificate var7 = var4[var6];
            var2.append(var7);
            var2.append(",");
         }

         var2.deleteCharAt(var2.length() - 1);
         var2.append("]");
      }

      return var2.toString();
   }

   public static String dumpSSLSessionId(byte[] var0) {
      return var0 != null ? _base64.encodeBuffer(var0) : null;
   }

   private static String dumpSecuritySCTOnPacket(Packet var0) {
      if (var0 == null) {
         return "<None>";
      } else {
         SCCredential var1 = getSCCredential(var0);
         return dumpSecuritySCT(var1);
      }
   }

   public static String dumpSecuritySCT(SCCredential var0) {
      return var0 == null ? "<None>" : "SCCredential=" + var0 + " ID=" + var0.getIdentifier();
   }

   static void initializeOutboundMessage(Packet var0) throws WsrmException {
      Sequence var1 = getSequenceForPacket(var0);
      if (var1 instanceof SourceSequence) {
         SourceSequenceManager.getInstance().setInvokePropertiesOntoPacket((SourceSequence)var1, var0);
      } else if (var1 instanceof DestinationSequence) {
         DestinationSequenceManager.getInstance().setInvokePropertiesOntoPacket((DestinationSequence)var1, var0);
      }

   }

   private NormalizedExpression getEffectivePolicyForPacket(Packet var1, boolean var2) throws PolicyException {
      WsrmPropertyBag var3 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      WSDLBoundOperation var5 = var1.getMessage().getOperation(this._wsdlPort);
      String var4;
      if (var5 != null) {
         var4 = var5.getName().getLocalPart();
      } else if (!this._clientSide) {
         if (var2) {
            var4 = var3.getInboundWsdlOperationName();
         } else {
            var4 = var3.getOutboundWsdlOperationName();
         }
      } else if (var2) {
         var4 = var3.getOutboundWsdlOperationName();
      } else {
         var4 = var3.getInboundWsdlOperationName();
      }

      if (var4 != null) {
         NormalizedExpression var6;
         if (var2) {
            var6 = this._policyHelper.getResponseEffectivePolicy(var4);
         } else {
            var6 = this._policyHelper.getRequestEffectivePolicy(var4);
         }

         return var6;
      } else {
         return null;
      }
   }

   private static SourceSequence getOutboundSourceSequenceForPacket(Packet var0) throws UnknownSourceSequenceException {
      WsrmPropertyBag var1 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var0);
      String var2 = var1.getOutboundSequenceId();
      if (var2 != null) {
         SourceSequence var3 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
         return var3;
      } else {
         return null;
      }
   }

   private static Sequence<?> getSequenceForPacket(Packet var0) throws UnknownSequenceException {
      WsrmPropertyBag var1 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var0);
      String var2 = var1.getOutboundSequenceId();
      if (var2 != null) {
         return SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
      } else {
         var2 = var1.getInboundSequenceId();
         if (var2 != null) {
            Object var3 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
            if (var3 == null) {
               var3 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var2, true);
            }

            return (Sequence)var3;
         } else {
            return null;
         }
      }
   }

   private OutboundMessageResult processOutboundSequenceMessage(Packet var1, SequenceIdFactory var2, String var3) throws WsrmException {
      OutboundMessageResult var4 = new OutboundMessageResult();
      var4.messageBuffered = false;

      try {
         WsrmPropertyBag var5 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
         WsrmConfig.Source var6 = WsrmConfig.getSource(this._serverContext, this._clientContext, var1, !this._clientSide);
         WsrmConstants.RMVersion var7 = var6.getRmVersion();
         if (var7 == null) {
            return var4;
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Outbound sequence message config:\n" + var6.toString());
            }

            var5.restorePersistentPropsIntoJaxWsRi(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            if (var1.soapAction == null) {
               var1.soapAction = var1.getMessage().getHeaders().getAction(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            }

            String var8 = var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            String var9 = var1.getMessage().getHeaders().getRelatesTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            boolean var10 = var9 != null;
            if (var5.getInternalSend()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("WsrmTubeUtils.processOutboundSequenceMessage detected an internally sent request. Ignoring it to allow it to pass through the rest of the tubeline. Msg id " + var8);
               }

               return var4;
            } else {
               WSDLBoundOperation var11 = var1.getMessage().getOperation(this._wsdlPort);
               boolean var12 = false;
               if (var11 != null) {
                  var5.setOutboundWsdlOperationName(var11.getName().getLocalPart());
                  var12 = var11.getOperation().isOneWay();
               }

               if (var12) {
                  WSEndpointReference var13 = var1.getMessage().getHeaders().getReplyTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
                  var1.expectReply = var13.isAnonymous();
               }

               WeakReference var22 = (WeakReference)var1.invocationProperties.get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
               ClientInstance var14 = var22 != null ? (ClientInstance)var22.get() : null;
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Processing outbound sequence message with action '" + var1.soapAction + "' msgId=" + var8 + " clientInstance=" + var14);
               }

               EstablishSequenceForRequestAction var15 = new EstablishSequenceForRequestAction(var1, var2, var3, var5, var6, var7, var8, var10, var14);
               if (var14 != null) {
                  synchronized(var14) {
                     var15 = var15.invoke();
                  }
               } else {
                  var15 = var15.invoke();
               }

               SourceSequence var16 = var15.getSeq();
               if (var16 == null) {
                  return var4;
               } else {
                  WsrmInvocationPropertyBag var17 = WsrmInvocationPropertyBag.getFromPacket(var1);
                  var17.setSequenceId(var16.getId());
                  if (var16 instanceof SourceOfferSequence) {
                     WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_BEFORE_RESPONSE_SEND);
                  }

                  String var18 = var16.getPhysicalStoreName();
                  WsUtil.getOrSetMessageID(var1.getMessage(), this._binding.getAddressingVersion(), this._binding.getSOAPVersion(), var18);
                  var4.messageInfo = this.addMessageToSourceSequence(var1, var16);
                  if (var16.isNonBuffered()) {
                     if (var16.getState() == SequenceState.CREATING || var16.getState() == SequenceState.NEW) {
                        throw new WsrmAbortSendException(WseeRmLogger.logSourceSequenceNotReadyToSendLoggable().getMessage());
                     }
                  } else {
                     var4.messageBuffered = true;
                  }

                  return var4;
               }
            }
         }
      } catch (WsrmException var20) {
         throw var20;
      } catch (Exception var21) {
         throw new WsrmException(var21.toString(), var21);
      }
   }

   private void registerCurrentSequenceWithMonitoring(Packet var1, SourceSequence var2) {
      try {
         WseeWsrmRuntimeMBeanImpl var3 = getWsrmRuntimeFromPacket(var1);
         if (var3 != null) {
            var3.addSequenceId(var2.getId());
            if (var2.getOffer() != null) {
               var3.addSequenceId(var2.getOffer().getId());
            }
         }
      } catch (Exception var4) {
         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, var4.toString(), var4);
         }

         WseeRmLogger.logUnexpectedException(var4.toString(), var4);
      }

   }

   public void removeClientCurrentSequenceId(ClientInstanceIdentity var1) {
      WsrmClientRuntimeFeature var2 = WsrmClientRuntimeFeature.getFromBinding(this._binding);
      if (var2 != null) {
         var2.removeClientCurrentSequenceId(var1);
      }

   }

   private SourceMessageInfo addMessageToSourceSequence(Packet var1, SourceSequence var2) throws WsrmException {
      WsrmInvocationPropertyBag var3 = (WsrmInvocationPropertyBag)var1.invocationProperties.get(WsrmInvocationPropertyBag.key);
      if (var3 == null) {
         var3 = new WsrmInvocationPropertyBag(var1);
         var1.invocationProperties.put(WsrmInvocationPropertyBag.key, var3);
      }

      WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
      var4.setInternalSend(true);
      WsrmConstants.RMVersion var5 = var2.getRmVersion();
      String var6 = var2.getId();
      SequenceHeader var7 = (SequenceHeader)WsrmHeaderFactory.getInstance().createEmptyWsrmHeader(SequenceHeader.class, var5);
      var7.setMustUnderstand(true);
      long var8 = var2.getNextMessageNum(var7);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Processing (stage 2) outbound sequence message with msg id " + var1.getMessage().getHeaders().getMessageID(this._binding.getAddressingVersion(), this._binding.getSOAPVersion()) + ", seq " + var6 + " and msg num " + var8);
      }

      String var10;
      if (var2.getState() != SequenceState.NEW && var2.getState() != SequenceState.CREATING) {
         var10 = var2.getDestinationId();
      } else {
         var4.setOutboundMsgNeedsDestSeqId(true);
         var10 = var6;
      }

      var7.setSequenceId(var10);
      var7.setMessageNumber(var8);
      var1.getMessage().getHeaders().add(var7);
      SourceMessageInfo var11 = SourceSequenceManager.getInstance().processSequenceMessage(var7, var1, this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
      var3.setMostRecentMsgNum(var8);
      return var11;
   }

   private WsrmInvocationPropertyBag getRmInvokeProps(Packet var1) {
      return WsrmInvocationPropertyBag.getFromPacket(var1);
   }

   private DestinationOfferSequence createOfferSequenceIfNeeded(SourceSequence var1, WsrmConfig.Destination var2, ClientInstance var3) throws WsrmException {
      if (!this.checkOfferNeeded(var1.getRmVersion())) {
         return null;
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Outbound sequence message destination config: " + var2);
         }

         boolean var4 = var2.isNonBufferedDestination();
         if (!var4) {
            if (var1.isNonBuffered()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Turning off response buffering (setting WsrmConfig.Destination.nonBufferedDestination=true) even though it was requested via config) for offer sequence related to source sequence because the source sequence turned off buffering too");
               }

               var4 = true;
            }

            if (var1.getAcksToEpr().isAnonymous()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Turning off response buffering (setting WsrmConfig.Destination.nonBufferedDestination=true) (even though it was requested via config) for offer sequence related to source sequence because the source sequence is 'anonymous' and will process only synchronous requests and responses");
               }

               var4 = true;
            }
         }

         if (!var4 && var3 != null && var3.getCreationInfo() != null) {
            WebServiceFeatureList var5 = var3.getCreationInfo().getFeatures();
            boolean var6 = false;
            Iterator var7 = var5.values().iterator();

            while(var7.hasNext()) {
               WebServiceFeature var8 = (WebServiceFeature)var7.next();
               if (var8.getID().equals(McFeature.getMcFeatureIDValue())) {
                  var6 = true;
                  break;
               }
            }

            if (var6) {
               var4 = true;
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Client has the Make Connection Feature enabled.  Therefore responses to this client will not be buffered by WS-RM.  The Offer Sequence Destination 'isNonBuffered' configuration value is being forced to 'true'");
               }
            }
         }

         DestinationOfferSequence var10 = new DestinationOfferSequence((String)null, var2.getPersistenceConfig().getLogicalStoreName(), var1.getRmVersion(), var1.getAddressingVersion(), var1.getSoapVersion(), var1.getSecurityContext(), var4);
         ClientIdentityFeature var11 = (ClientIdentityFeature)this._binding.getFeature(ClientIdentityFeature.class);
         if (var11 != null) {
            DispatchFactoryResolver.ClientSideKey var12 = new DispatchFactoryResolver.ClientSideKey(var11.getClientId());
            var10.setDispatchKey(var12);
         }

         var10.takeEprsFromSequence(var1);
         var10.setIncompleteSequenceBehavior(var1.getIncompleteSequenceBehavior());
         var10.setDeliveryAssurance(var1.getDeliveryAssurance());

         try {
            var10.setExpires(var1.getExpires());
            var10.setIdleTimeout(var1.getIdleTimeout());
            var10.setAckInterval(DatatypeFactory.newInstance().newDuration(var2.getAcknowledgementInterval()));
         } catch (Exception var9) {
            WseeRmLogger.logUnexpectedException(var9.toString(), var9);
         }

         return var10;
      }
   }

   protected boolean checkOfferNeeded(WsrmConstants.RMVersion var1) throws WsrmException {
      WSDLPortType var2 = this._wsdlPort.getBinding().getPortType();
      Iterator var3 = var2.getOperations().iterator();

      RM11Assertion var6;
      do {
         WSDLOperation var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (WSDLOperation)var3.next();
         } while(var4.getOutput() == null);

         if (var1 == WsrmConstants.RMVersion.RM_10) {
            return true;
         }

         NormalizedExpression var5;
         try {
            var5 = this._policyHelper.getResponseEffectivePolicy(var4.getName().getLocalPart());
         } catch (Exception var7) {
            throw new WsrmException(var7.toString(), var7);
         }

         var6 = (RM11Assertion)var5.getPolicyAssertion(RM11Assertion.class);
      } while(var6 != null && var6.isOptional());

      return true;
   }

   private boolean updateSecurityPropertiesIntoSequence(Packet var1, SourceSequence var2) throws WsrmException {
      WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_SECURITY_PROPS_BEFORE_SAVE);
      WsrmSecurityContext var3 = var2.getSecurityContext();
      boolean var4 = var3.update(var1);
      if (var4) {
         SourceSequenceManager.getInstance().updateSequence(var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Updated WsrmSecurityContext object for source sequence: " + var2.getId());
         }
      } else if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("No WsrmSecurityContext values require updating");
      }

      return var4;
   }

   public static void sendCreateSequenceMsg(SourceSequence var0, DispatchFactory var1, Sender.SendFailureCallback var2) throws SOAPException, WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Sending CreateSequence on seq " + var0.getId() + " to " + var0.getEndpointEpr());
      }

      CreateSequenceMsg var3 = new CreateSequenceMsg(var0.getRmVersion());
      EndpointReference var4 = WsUtil.createWseeEPRFromWsEPR(var0.getAcksToEpr());
      var3.setAcksTo(var4);
      var3.setExpires(var0.getExpires());
      var3.setSecurityTokenReference((SecurityTokenReference)null);
      if (var0.getOffer() != null) {
         DestinationOfferSequence var5 = var0.getOffer();
         SequenceOffer var6 = new SequenceOffer(var0.getRmVersion());
         var6.setEndpoint(var4);
         var6.setExpires(var5.getExpires().toString());
         var6.setIncompleteSequenceBehavior(var5.getIncompleteSequenceBehavior());
         var6.setSequenceId(var5.getId());
         var3.setOffer(var6);
      }

      Message var22 = Rpc2WsUtil.createMessageFromHandshakeMessage(var3, var0.getAddressingVersion(), var0.getSoapVersion());
      String var23 = Rpc2WsUtil.getSOAPActionFromHandshakeMessage(var3);
      if (var0.getCreateSequenceMsgId() == null) {
         WsUtil.getOrSetMessageID(var22, var0.getAddressingVersion(), var0.getSoapVersion(), var0.getPhysicalStoreName());
      } else {
         StringHeader var7 = new StringHeader(var0.getAddressingVersion().messageIDTag, var0.getCreateSequenceMsgId());
         var22.getHeaders().addOrReplace(var7);
      }

      WsrmSecurityContext var24 = var0.getSecurityContext();
      WsrmConstants.RMVersion var8 = var0.getRmVersion();
      if (var24.isSecureWithWssc()) {
         if (var8.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            var22.getHeaders().add(new UsesSequenceSTRHeader(var8));
         }
      } else if (var24.isSecureWithSSL() && var8.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var22.getHeaders().add(new UsesSequenceSSLHeader(var8));
      }

      var0.setState(SequenceState.CREATING);
      SourceSequenceManager.getInstance().updateSequence(var0);
      Sender var9 = new Sender(var1);
      WSEndpointReference var10 = var0.getEndpointEpr();
      WSEndpointReference var11 = calculateReplyToForSourceSequence(var0);
      boolean var12 = var0.isNonBuffered();
      Map var13 = null;
      WsrmSecurityContext var14 = var0.getSecurityContext();

      try {
         if (var14 != null) {
            var13 = var14.newInitializedMap();
            CreateSequencePostSecurityTokenCallback.addCallbackToMap(var14, var13);
            if (LOGGER.isLoggable(Level.FINER)) {
               LOGGER.finer("\n\n\n======= after setting, securityPropertyMap contains: ");
               Set var15 = var13.keySet();
               int var16 = var15.size();
               LOGGER.finer("====   " + var16 + " key-pairs:");
               Iterator var17 = var15.iterator();

               while(var17.hasNext()) {
                  Object var18 = var17.next();
                  Object var19 = var13.get(var18);
                  if (var19 != null) {
                     LOGGER.finer("====   key='" + var18.toString() + "',  val='" + var19.toString() + "'");
                  } else {
                     LOGGER.finer("====   key='" + var18.toString() + "',  val=NULL");
                  }
               }

               LOGGER.finer("======= DONE ======\n\n\n\n");
            }
         }
      } catch (PolicyException var21) {
         throw new WsrmException(var21.getMessage());
      }

      Map var25 = SourceSequenceManager.getInstance().getSenderInvokeProperties((SourceSequence)var0, (Packet)null);
      var25.putAll(var13);

      try {
         var9.send(var22, var23, var10, var11, var25, var12, false, var2);
      } catch (Exception var20) {
         throw new WsrmException(var20.toString(), var20);
      }
   }

   public static WSEndpointReference getEndpointReferenceFromIncomingPacket(@NotNull Packet var0, @NotNull WSEndpointImpl var1) {
      WSEndpointReference var2 = null;
      AsyncTransportProviderPropertyBag var3 = (AsyncTransportProviderPropertyBag)AsyncTransportProviderPropertyBag.propertySetRetriever.getFromPacket(var0);
      AsyncResponseEndpoint var4 = null;
      if (var3 != null) {
         var4 = var3.getResponseEndpoint();
      }

      if (var4 != null) {
         var2 = new WSEndpointReference(var4.getEndpointReference());
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("getEndpointReferenceFromIncomingPacket returning client-side async response endpoint EPR: " + var2.getAddress());
         }
      }

      if (var2 == null) {
         String var5 = var0.webServiceContextDelegate.getEPRAddress(var0, var1);
         String var6 = null;
         if (var1.getServiceDefinition() != null) {
            var6 = var0.webServiceContextDelegate.getWSDLAddress(var0, var1);
         }

         Class var7 = W3CEndpointReference.class;
         var2 = var1.getWSEndpointReference(var7, var5, var6, new Element[0]);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("getEndpointReferenceFromIncomingPacket returning service-side endpoint EPR: " + var2.getAddress());
         }
      }

      return var2;
   }

   public static Packet createPacketFromPersistentMessage(PersistentMessage var0, AddressingVersion var1, SOAPVersion var2) {
      Packet var3 = new Packet();
      PersistentMessageFactory.getInstance().setMessageIntoPacket(var0, var3);
      WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var3);
      var4.restorePersistentPropsIntoJaxWsRi(var1, var2);
      return var3;
   }

   @Nullable
   public static WseeWsrmRuntimeMBeanImpl getWsrmRuntimeFromPacket(Packet var0) {
      if (KernelStatus.isServer()) {
         WseeBasePortRuntimeMBean var1 = WseePortRuntimeMBeanImpl.getFromPacket(var0);
         WseeWsrmRuntimeMBeanImpl var2 = getWsrmRuntimeFromPortRuntime(var1);
         return var2;
      } else {
         return null;
      }
   }

   private static WseeWsrmRuntimeMBeanImpl getWsrmRuntimeFromPortRuntime(WseeBasePortRuntimeMBean var0) {
      WseeWsrmRuntimeMBeanImpl var1 = null;

      try {
         if (var0 != null) {
            var1 = (WseeWsrmRuntimeMBeanImpl)var0.getWsrm();
         }
      } catch (Exception var3) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var3.toString(), var3);
         }

         WseeRmLogger.logUnexpectedException(var3.toString(), var3);
      }

      return var1;
   }

   private boolean isOfferTurnedOff(WsrmClientRuntimeFeature var1) {
      boolean var2 = false;
      if (var1 != null) {
         var2 = var1.isTurnOffOffer();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("isOfferTurnedOff is returning: " + var2);
      }

      return var2;
   }

   private class EstablishSequenceForRequestAction {
      private Packet _packet;
      private SequenceIdFactory _seqIdFactory;
      private String _endpointAddress;
      private WsrmPropertyBag _rmProps;
      private WsrmConfig.Source _rmSourceConfig;
      private WsrmConstants.RMVersion _rmVersion;
      private String _msgId;
      private boolean _response;
      private ClientInstance _clientInstance;
      private SourceSequence _seq;

      public EstablishSequenceForRequestAction(Packet var2, SequenceIdFactory var3, String var4, WsrmPropertyBag var5, WsrmConfig.Source var6, WsrmConstants.RMVersion var7, String var8, boolean var9, ClientInstance var10) {
         this._packet = var2;
         this._seqIdFactory = var3;
         this._endpointAddress = var4;
         this._rmProps = var5;
         this._rmSourceConfig = var6;
         this._rmVersion = var7;
         this._msgId = var8;
         this._response = var9;
         this._clientInstance = var10;
      }

      public SourceSequence getSeq() {
         return this._seq;
      }

      public EstablishSequenceForRequestAction invoke() throws WsrmException, SOAPException {
         AssociateSequenceWithRequestAction var1 = (WsrmTubeUtils.this.new AssociateSequenceWithRequestAction(this._packet, this._seqIdFactory, this._endpointAddress, this._rmProps, this._rmSourceConfig, this._rmVersion, this._clientInstance)).invoke();
         this._seq = var1.getSeq();
         boolean var2 = var1.isPreExisting();
         String var3 = var1.getSeqId();
         if (this._seq == null) {
            if (var2) {
               if (WsrmTubeUtils.this._clientSide) {
                  throw new WsrmException(WseeRmLogger.logUsingStaleSequenceLoggable(var3).getMessage());
               }

               return this;
            }

            SetupNewSequenceAction var4 = (WsrmTubeUtils.this.new SetupNewSequenceAction(this._packet, this._rmProps, this._rmSourceConfig, this._msgId, this._response, this._clientInstance)).invoke();
            this._seq = var4.getSeq();
            if (this._seq == null) {
               return this;
            }
         }

         return this;
      }
   }

   private class AssociateSequenceWithRequestAction {
      private Packet _packet;
      private SequenceIdFactory _seqIdFactory;
      private String _endpointAddress;
      private WsrmPropertyBag _rmProps;
      private WsrmConfig.Source _rmSourceConfig;
      private WsrmConstants.RMVersion _rmVersion;
      private ClientInstance _clientInstance;
      private String _seqId;
      private boolean _preExisting;
      private SourceSequence _seq;

      public AssociateSequenceWithRequestAction(Packet var2, SequenceIdFactory var3, String var4, WsrmPropertyBag var5, WsrmConfig.Source var6, WsrmConstants.RMVersion var7, ClientInstance var8) {
         this._packet = var2;
         this._seqIdFactory = var3;
         this._endpointAddress = var4;
         this._rmProps = var5;
         this._rmSourceConfig = var6;
         this._rmVersion = var7;
         this._clientInstance = var8;
      }

      public String getSeqId() {
         return this._seqId;
      }

      public boolean isPreExisting() {
         return this._preExisting;
      }

      public SourceSequence getSeq() {
         return this._seq;
      }

      public AssociateSequenceWithRequestAction invoke() {
         SequenceIdFactory.Info var1 = this._seqIdFactory.getSequenceId(this._endpointAddress, this._rmSourceConfig.getRmAssertion().isOptional(), this._packet);
         this._seqId = var1.id;
         this._preExisting = var1.preExisting;
         this._seq = null;
         if (this._seqId == null && this._clientInstance != null) {
            this._seqId = this.loadClientCurrentSequenceId(this._clientInstance);
            if (this._seqId != null) {
               if (WsrmTubeUtils.LOGGER.isLoggable(Level.FINE)) {
                  WsrmTubeUtils.LOGGER.fine("Found EXISTING sequence ID on conversational client: " + this._seqId);
               }

               this._seq = (SourceSequence)SourceSequenceManager.getInstance().getSequence(this._rmVersion, this._seqId);
               if (this._seq != null) {
                  var1.id = this._seqId;
                  var1.preExisting = true;
                  WsrmTubeUtils.this.registerCurrentSequenceWithMonitoring(this._packet, this._seq);
               } else {
                  if (WsrmTubeUtils.LOGGER.isLoggable(Level.FINE)) {
                     WsrmTubeUtils.LOGGER.fine("Found sequence ID for NON-EXISTANT sequence on conversational client: " + this._seqId + ". This stored sequence will be ignored.");
                  }

                  WsrmTubeUtils.this.removeClientCurrentSequenceId(this._clientInstance.getId());
               }
            }
         }

         if (this._seqId != null && this._seq == null) {
            this._seq = (SourceSequence)SourceSequenceManager.getInstance().getSequence(this._rmVersion, this._seqId);
         }

         this._rmProps.setOutboundSequenceId(this._seqId);
         return this;
      }

      private String loadClientCurrentSequenceId(ClientInstance var1) {
         WsrmClientRuntimeFeature var2 = WsrmClientRuntimeFeature.getFromBinding(WsrmTubeUtils.this._binding);
         return var2 != null ? var2.loadClientCurrentSequenceId(var1) : null;
      }
   }

   private class SetupNewSequenceAction {
      private Packet _packet;
      private WsrmPropertyBag _rmProps;
      private WsrmConfig.Source _rmSourceConfig;
      private String _msgId;
      private boolean _isResponse;
      private ClientInstance _clientInstance;
      private SourceSequence _seq;

      public SetupNewSequenceAction(Packet var2, WsrmPropertyBag var3, WsrmConfig.Source var4, String var5, boolean var6, ClientInstance var7) {
         this._packet = var2;
         this._rmProps = var3;
         this._rmSourceConfig = var4;
         this._msgId = var5;
         this._isResponse = var6;
         this._clientInstance = var7;
      }

      public SourceSequence getSeq() {
         return this._seq;
      }

      public SetupNewSequenceAction invoke() throws WsrmException, SOAPException {
         if (this._rmSourceConfig.getRmAssertion().isOptional()) {
            if (WsrmTubeUtils.LOGGER.isLoggable(Level.FINE)) {
               String var4 = this._rmProps.getOutboundWsdlOperationName() != null ? " (" + this._rmProps.getOutboundWsdlOperationName() + ")" : "";
               WsrmTubeUtils.LOGGER.fine("Invoking an RM-optional operation" + var4 + ", and no reliable sequence has been established for this stub. Treating this as a non-reliable invoke");
            }

            return this;
         } else {
            WsrmConfig.Destination var2 = WsrmConfig.getDestination(WsrmTubeUtils.this._clientContext, this._packet, !WsrmTubeUtils.this._clientSide);
            this._seq = this.createSourceSequence(this._packet, this._isResponse, this._clientInstance, this._rmSourceConfig, var2);
            String var1 = this._seq.getId();
            WsrmInvocationPropertyBag var3 = WsrmTubeUtils.this.getRmInvokeProps(this._packet);
            var3.setSequenceId(var1);
            this._rmProps.setOutboundSequenceId(var1);
            if (this._clientInstance != null) {
               this._seq.setCreatingClientInstanceId(this._clientInstance.getId());
            }

            if (this._clientInstance != null) {
               this.storeClientCurrentSequenceId(this._clientInstance, var1);
            }

            WsrmTubeUtils.this.registerCurrentSequenceWithMonitoring(this._packet, this._seq);
            if (WsrmTubeUtils.LOGGER.isLoggable(Level.FINE)) {
               if (var1 != null) {
                  WsrmTubeUtils.LOGGER.fine("Processing (stage 1) outbound sequence message with msg id " + this._msgId + ", seq " + var1);
               } else {
                  WsrmTubeUtils.LOGGER.fine("Didn't calculate any outbound sequence ID for the message with msg id " + this._msgId + ". This may indicate we're sending non-reliably");
               }
            }

            if (this._seq.isNonBuffered()) {
               WsrmTubeUtils.sendCreateSequenceMsg(this._seq, WsrmTubeUtils.this._dispatchFactory, (Sender.SendFailureCallback)null);
            }

            return this;
         }
      }

      private void storeClientCurrentSequenceId(ClientInstance var1, String var2) {
         WsrmClientRuntimeFeature var3 = WsrmClientRuntimeFeature.getFromBinding(WsrmTubeUtils.this._binding);
         if (var3 != null) {
            var3.storeClientCurrentSequenceId(var1, var2);
         }

      }

      private SourceSequence createSourceSequence(Packet var1, boolean var2, ClientInstance var3, WsrmConfig.Source var4, WsrmConfig.Destination var5) throws WsrmException {
         WsrmInvocationPropertyBag var6 = WsrmTubeUtils.this.getRmInvokeProps(var1);
         WsrmSecurityContext var7 = WsrmTubeUtils.this.getWsrmSecurityContext(var4.getRmAssertion(), var1);
         boolean var8 = var4.isNonBufferedSource();
         if (!var8) {
         }

         PersistentRequestContext var9;
         if (var3 != null) {
            var9 = new PersistentRequestContext(((BindingProvider)var3.getInstance()).getRequestContext());
         } else {
            var9 = new PersistentRequestContext(var1.invocationProperties);
         }

         DestinationSequence var10 = null;
         Object var11;
         if (!var2) {
            var11 = new SourceSequence((String)null, var4.getPersistenceConfig().getLogicalStoreName(), var4.getRmVersion(), WsrmTubeUtils.this._binding.getAddressingVersion(), WsrmTubeUtils.this._binding.getSOAPVersion(), var7, var8, var9);
         } else {
            WsrmPropertyBag var12 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
            String var13 = var12.getInboundSequenceId();
            if (var13 != null) {
               var10 = (DestinationSequence)DestinationSequenceManager.getInstance().getSequence(var4.getRmVersion(), var13);
            }

            var11 = new SourceOfferSequence((String)null, var4.getPersistenceConfig().getLogicalStoreName(), var4.getRmVersion(), WsrmTubeUtils.this._binding.getAddressingVersion(), WsrmTubeUtils.this._binding.getSOAPVersion(), var7, var10, var8);
            ((SourceOfferSequence)var11).setHandshaked(true);
         }

         var6.setWsrmVersion(var4.getRmVersion());
         WsaPropertyBag var26 = (WsaPropertyBag)var1.getSatellite(WsaPropertyBag.class);
         WSEndpointReference var27;
         if (!var2) {
            URI var14 = var1.endpointAddress.getURI();
            var27 = new WSEndpointReference(var14, WsrmTubeUtils.this._binding.getAddressingVersion());
         } else {
            var27 = var26.getReplyToFromRequest();
         }

         ((SourceSequence)var11).setEndpointEpr(var27);
         WSEndpointReference var28;
         if (!var2) {
            var28 = var1.getMessage().getHeaders().getReplyTo(WsrmTubeUtils.this._binding.getAddressingVersion(), WsrmTubeUtils.this._binding.getSOAPVersion());
         } else {
            var28 = var10 != null ? var10.getHostEpr() : var1.getMessage().getHeaders().getReplyTo(WsrmTubeUtils.this._binding.getAddressingVersion(), WsrmTubeUtils.this._binding.getSOAPVersion());
         }

         ((SourceSequence)var11).setAcksToEpr(var28);
         Object var15;
         if (!var2) {
            ClientIdentityFeature var16 = (ClientIdentityFeature)WsrmTubeUtils.this._binding.getFeature(ClientIdentityFeature.class);
            var15 = new SenderDispatchFactory.ClientSideKey(var16.getClientId());
         } else {
            var15 = new SenderDispatchFactory.ServerSideKey(var1.endpoint.getEndpointId());
         }

         ((SourceSequence)var11).setSenderDispatchKey((SenderDispatchFactory.Key)var15);
         ((SourceSequence)var11).setDeliveryAssurance(var4.getDeliveryAssurance());

         try {
            ((SourceSequence)var11).setBaseRetransmissionInterval(DatatypeFactory.newInstance().newDuration(var4.getBaseRetransmissionInterval()));
            ((SourceSequence)var11).setExponentialBackoffEnabled(var4.getRetransmissionExponentialBackoff());
            ((SourceSequence)var11).setExpires(DatatypeFactory.newInstance().newDuration(var4.getSequenceExpiration()));
            ((SourceSequence)var11).setIdleTimeout(DatatypeFactory.newInstance().newDuration(var4.getInactivityTimeout()));
         } catch (Exception var22) {
            throw new WsrmException(var22.toString(), var22);
         }

         DestinationOfferSequence var29 = null;
         boolean var17 = false;
         boolean var18 = false;

         try {
            if (!var2 && !WsrmTubeUtils.this.isOfferTurnedOff(WsrmClientRuntimeFeature.getFromBinding(WsrmTubeUtils.this._binding))) {
               var29 = WsrmTubeUtils.this.createOfferSequenceIfNeeded((SourceSequence)var11, var5, var3);
            }

            if (var29 != null) {
               DestinationSequenceManager.getInstance().addSequence((DestinationSequence)var29);
               var17 = true;
               ((SourceSequence)var11).setOffer(var29);
            }

            SourceSequenceManager.getInstance().addSequence((Sequence)var11);
            var18 = true;
            if (var29 != null) {
               var29.setMainSequence((SourceSequence)var11);
               DestinationSequenceManager.getInstance().updateSequence((DestinationSequence)var29);
            }

            String var19 = ((SourceSequence)var11).getPhysicalStoreName();
            PhysicalStoreNameHeader var20 = new PhysicalStoreNameHeader(var19);
            HeaderList var21 = new HeaderList();
            var21.add(var20);
            ((SourceSequence)var11).setAcksToEpr(new WSEndpointReference(((SourceSequence)var11).getAcksToEpr(), var21));
            SourceSequenceManager.getInstance().updateSequence((SourceSequence)var11);
            if (var29 != null) {
               var29.takeEprsFromMainSequence();
               var29.takeFirstRequestContextFromMainSequence();
               DestinationSequenceManager.getInstance().updateSequence((DestinationSequence)var29);
            }

            return (SourceSequence)var11;
         } catch (WsrmException var25) {
            if (var17) {
               try {
                  DestinationSequenceManager.getInstance().removeSequence(var29);
               } catch (Exception var24) {
                  if (WsrmTubeUtils.LOGGER.isLoggable(Level.SEVERE)) {
                     WsrmTubeUtils.LOGGER.log(Level.SEVERE, var24.toString(), var24);
                  }
               }
            }

            if (var18) {
               try {
                  SourceSequenceManager.getInstance().removeSequence((SourceSequence)var11);
               } catch (Exception var23) {
                  if (WsrmTubeUtils.LOGGER.isLoggable(Level.SEVERE)) {
                     WsrmTubeUtils.LOGGER.log(Level.SEVERE, var23.toString(), var23);
                  }
               }
            }

            throw var25;
         }
      }
   }

   public static class OutboundMessageResult {
      public boolean messageBuffered;
      public boolean messageAborted;
      public SourceMessageInfo messageInfo;
   }

   public class InboundMessageResult {
      public boolean handled;
      public Message message;
      public WsrmFaultMsg rmFault;
      public Sequence<?> seq;
      public boolean needSuspendOnCurrentFiber;
      public Fiber.Listener currentFiberSuspendingCallback;
   }
}
