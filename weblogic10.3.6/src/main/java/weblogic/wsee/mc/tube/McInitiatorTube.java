package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.server.sei.CorrelationPropertySet;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentContextStore;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentityFeature;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.mc.api.McPolicyInfo;
import weblogic.wsee.mc.api.McPolicyInfoFactory;
import weblogic.wsee.mc.api.McPollingTimeoutException;
import weblogic.wsee.mc.exception.McException;
import weblogic.wsee.mc.headers.McHeaderFactory;
import weblogic.wsee.mc.headers.MessagePendingHeader;
import weblogic.wsee.mc.messages.McMsg;
import weblogic.wsee.mc.processor.McPending;
import weblogic.wsee.mc.processor.McPendingManager;
import weblogic.wsee.mc.processor.McPoll;
import weblogic.wsee.mc.processor.McPollManager;
import weblogic.wsee.mc.property.McPropertyBag;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.tube.WsrmClientRuntimeFeature;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssp.handlers.WssHandlerListener;
import weblogic.wsee.ws.WsPort;

public class McInitiatorTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(McInitiatorTube.class.getName());
   private ClientTubeAssemblerContext _context;
   private WSBinding _binding;
   private AddressingVersion _addrVersion;
   private SOAPVersion _soapVersion;
   private String _defaultFaultAction;
   private McFeature _mcFeature;
   private WSDLPort _wsdlPort;
   private WSService _service;
   private URL _url;
   private McPolicyInfo _policy;
   private ClientInstanceIdentity _clientInstanceIdentity;
   private boolean _configChecked;
   private Duration _intervalDuration;
   private Duration _expirationDuration;
   private boolean _exponentialBackoff;
   private static final Object _createPollLock = "CreatePollLock";
   private static final Object _createPendingLock = "CreatePendingLock";

   public McInitiatorTube(Tube var1, ClientTubeAssemblerContext var2) {
      super(var1);
      WSBinding var3 = var2.getBinding();
      WSDLPort var4 = var2.getWsdlModel();
      WSService var5 = var2.getService();
      this.commonConstructorCode(var2, var3, var4, var5);
      McPollManager.getInstance();
   }

   public McInitiatorTube(McInitiatorTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode(var1._context, var1._binding, var1._wsdlPort, var1._service);
   }

   private void commonConstructorCode(ClientTubeAssemblerContext var1, WSBinding var2, WSDLPort var3, WSService var4) {
      this._context = var1;
      this._binding = var2;
      this._addrVersion = var2.getAddressingVersion();
      this._soapVersion = var2.getSOAPVersion();
      this._defaultFaultAction = this._addrVersion.getDefaultFaultAction();
      this._mcFeature = (McFeature)var2.getFeature(McFeature.class);
      this._wsdlPort = var3;
      this._service = var4;
      this._configChecked = false;
      ClientInstanceIdentityFeature var5 = (ClientInstanceIdentityFeature)var2.getFeature(ClientInstanceIdentityFeature.class);
      this._clientInstanceIdentity = var5.getClientInstanceId();
      String var6 = this._clientInstanceIdentity.getId();
      this.setReplyTo(var6);
      this.setMcPolicy();
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      McPropertyBag var2 = (McPropertyBag)McPropertyBag.propertySetRetriever.getFromPacket(var1);
      McPropertyBag.flagPersistentPropsOnPacket(var1);
      Message var3 = var1.getMessage();
      if (var3 == null) {
         LOGGER.log(Level.FINE, "processRequest: passing null request message on");
         return this.doInvoke(this.next, var1);
      } else {
         HeaderList var4 = var3.getHeaders();
         String var5 = var4.getAction(this._addrVersion, this._soapVersion);
         var2.setRqstAction(var5);
         WeakReference var6 = (WeakReference)var1.invocationProperties.get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
         ClientInstance var7 = var6 != null ? (ClientInstance)var6.get() : null;
         ClientInstanceIdentity var8 = var7 != null ? var7.getId() : null;
         if (var8 == null) {
            var8 = this._clientInstanceIdentity;
         }

         String var9 = var8 != null ? var8.getId() : null;
         if (LOGGER.isLoggable(Level.FINE)) {
            if (var9 == null) {
               LOGGER.fine("processRequest: null client instance identity. Action = " + var5);
            } else {
               LOGGER.fine("processRequest: non-null outgoing request message. Action= " + var5 + " client instance " + var9);
            }
         }

         String var31;
         if (McConstants.Action.MC.matchesAnyMCVersion(var5)) {
            McMsg var28 = new McMsg();

            try {
               var28.readFromSOAPMsg(var3.readAsSOAPMessage());
            } catch (SOAPException var24) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.log(Level.FINE, "processRequest failed: exception reading SOAP message: " + var24);
               }

               WseeMCLogger.logUnexpectedException(var24.toString(), var24);
               return this.doThrow(var24);
            }

            String var30 = var28.getAddress();
            var31 = McTubeUtils.getUUID(var30);
            var2.setPollId(McProtocolUtils.decodeId(var31));
            var2.setIsPoll(true);
            McTubeUtils.setMessageID(var3, this._addrVersion, this._soapVersion);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Processing outbound MakeConnection message for poll " + var31);
            }

            return this.doInvoke(this.next, var1);
         } else {
            boolean var10 = McTubeUtils.isMcAnonURI(var1.endpointAddress);
            if (var10) {
               McPendingManager var29 = McPendingManager.getInstance();
               var31 = McTubeUtils.getUUID(var1.endpointAddress);
               McPending var32;
               synchronized(_createPendingLock) {
                  var32 = var29.getPending(var31);
                  if (var32 == null) {
                     PersistenceConfig.Client var34 = PersistenceConfig.getClientConfig(this._context);
                     String var16 = var34.getLogicalStoreName();
                     var32 = new McPending(var31, var16, this._addrVersion, this._soapVersion);
                     var32.setAddress(this._url.getPath());
                     var29.addPending(var32);
                  }
               }

               if (LOGGER.isLoggable(Level.FINE)) {
                  String var33 = var4.getRelatesTo(this._addrVersion, this._soapVersion);
                  LOGGER.log(Level.FINE, "processRequest: adding packet to pending list, target: " + var1.endpointAddress + " action = " + var5 + " relatesTo " + var33);
               }

               var32.addMessage(var1);
               var29.updatePending(var32);
               var1.setMessage((Message)null);
               return this.doReturnWith(var1);
            } else {
               WSEndpointReference var11 = var4.getReplyTo(this._addrVersion, this._soapVersion);
               WSEndpointReference var12 = var4.getFaultTo(this._addrVersion, this._soapVersion);
               if (var12 == null) {
                  var12 = var11;
               }

               boolean var13 = McTubeUtils.isMcAnonURI(var11);
               boolean var14 = McTubeUtils.isMcAnonURI(var12);
               if (this._policy.isMcOptional() || var13 && var14) {
                  McTubeUtils.setMessageID(var3, this._addrVersion, this._soapVersion);
                  var2.setPollId(var9);
                  var2.setIsPoll(false);
                  var2.setMsgId(var4.getMessageID(this._addrVersion, this._soapVersion));
                  McPollManager var15 = McPollManager.getInstance();
                  synchronized(_createPollLock) {
                     if (var1.expectReply && (var13 || var14)) {
                        if (!this._configChecked) {
                           this.checkMcConfig();
                        }

                        McPoll var17 = var15.getPoll(var9);
                        if (var17 == null) {
                           PersistenceConfig.Client var18 = PersistenceConfig.getClientConfig(this._context);
                           String var19 = var18.getLogicalStoreName();
                           var17 = new McPoll(var8, var19, this._addrVersion, this._soapVersion);
                           SCCredential var20 = null;
                           if (var1.proxy != null) {
                              var20 = (SCCredential)var1.proxy.getRequestContext().get("weblogic.wsee.wssc.sct");
                           }

                           if (var20 == null) {
                              var20 = (SCCredential)var1.invocationProperties.get("weblogic.wsee.wssc.sct");
                           }

                           if (var20 == null) {
                              SecurityHanlderListener var21 = new SecurityHanlderListener(var17);
                              var21.init(var1);
                           } else {
                              var17.setCredential(var20);
                           }

                           var17.setExpires(this._expirationDuration);
                           var17.setInterval(this._intervalDuration);
                           var17.setExponentialBackoff(this._exponentialBackoff);
                           WSEndpointReference var35 = new WSEndpointReference(var1.endpointAddress.getURI(), this._addrVersion);
                           var17.setEndpointReference(var35);
                           if (LOGGER.isLoggable(Level.FINE)) {
                              LOGGER.log(Level.FINE, "processRequest: adding new poll for " + var9);
                           }

                           var15.addPoll(var17, var7);
                           this.addRequest(var15, var1, var17, var5, var4, var7);
                        } else {
                           synchronized(var17) {
                              if (LOGGER.isLoggable(Level.FINE)) {
                                 LOGGER.log(Level.FINE, "processRequest: poll already exists for " + var9);
                              }

                              NextAction var10000;
                              switch (var17.getState()) {
                                 case EXPIRED:
                                    var10000 = this.doThrow(new McPollingTimeoutException());
                                    return var10000;
                                 case TERMINATED:
                                    var10000 = this.doThrow(new WebServiceException("MakeConnection polling already terminated for " + var9));
                                    return var10000;
                                 default:
                                    this.addRequest(var15, var1, var17, var5, var4, var7);
                                    return this.doInvoke(this.next, var1);
                              }
                           }
                        }
                     } else if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "processRequest: no fiber added to poll " + var9 + " as either a one-way message or non MC uri for replyTo/faultTo");
                     }
                  }

                  return this.doInvoke(this.next, var1);
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.log(Level.FINE, "processRequest failed: policy required mandatory MC anonymous URI(s)");
                  }

                  return this.doThrow(new McException(WseeMCLogger.logPolicyURIMismatchLoggable().getMessage()));
               }
            }
         }
      }
   }

   private void addRequest(McPollManager var1, Packet var2, McPoll var3, String var4, HeaderList var5, ClientInstance var6) {
      String var7 = var5.getMessageID(this._addrVersion, this._soapVersion);
      if (this._binding.isFeatureEnabled(AsyncClientHandlerFeature.class)) {
         this.savePersistentContext(var2, var5.getMessageID(this._addrVersion, this._soapVersion));
         var3.addPersistentRequest(var7, var4);
      } else if (var3.getFiber(var7) == null) {
         McPollManager.FiberBox var8 = new McPollManager.FiberBox(Fiber.current());
         var3.addFiber(var7, var8);
      }

      var1.updatePoll(var3, var6);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      McPropertyBag var2 = (McPropertyBag)McPropertyBag.propertySetRetriever.getFromPacket(var1);
      McPollManager var3 = McPollManager.getInstance();
      String var4 = var2.getPollId();
      boolean var5 = var2.getIsPoll();
      McPoll var6 = var3.getPoll(var4);

      try {
         return var5 ? this.handlePollResponse(var1, var4, var6) : this.handleNonPolledResponse(var1, var2, var4, var6);
      } catch (Exception var8) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "processResponse failed: " + var8.toString(), var8);
         }

         WseeMCLogger.logUnexpectedException(var8.toString(), var8);
         this.doThrow(var8);
         return this.doReturnWith(var1);
      }
   }

   private NextAction handleNonPolledResponse(Packet var1, McPropertyBag var2, String var3, McPoll var4) {
      HeaderList var5;
      String var6;
      if (var4 == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            if (var1.getMessage() != null) {
               var5 = var1.getMessage().getHeaders();
               var6 = var5.getAction(this._addrVersion, this._soapVersion);
               String var7 = var5.getMessageID(this._addrVersion, this._soapVersion);
               LOGGER.log(Level.FINE, "processResponse: poll " + var3 + " doesn't exist, passing through response with action " + var6 + " and message id " + var7);
            }

            LOGGER.log(Level.FINE, "processResponse: poll " + var3 + " doesn't exist, passing through response with no message");
         }

         return this.doReturnWith(var1);
      } else if (var1.getMessage() == null) {
         switch (var4.getState()) {
            case EXPIRED:
               return this.doThrow(new McPollingTimeoutException());
            case TERMINATED:
               return this.doReturnWith(var1);
            case ENABLED:
            case POLLING:
               McPollManager.FiberBox var8 = var4.getFiber(var2.getMsgId());
               if (var8 != null) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.log(Level.FINE, "processResponse: suspending fiber " + var2.getMsgId() + " for poll " + var3);
                  }

                  var1.addSatellite(new CorrelationPropertySet(var2.getRqstAction()));
                  var8.open();
                  var4.incrementSuspendedFiberCount();
                  return this.doSuspend();
               } else {
                  if (var4.containsPersistentRequest(var2.getMsgId())) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "processResponse: no response message received. Looking for response to persistent request msg id " + var2.getMsgId());
                     }

                     NextAction var9 = new NextAction();
                     var9.abortResponse(var1);
                     return var9;
                  }

                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.log(Level.FINE, "processResponse: no fiber box or persistent request found for poll " + var3 + " with id " + var2.getMsgId());
                  }

                  return this.doReturnWith(var1);
               }
            default:
               WseeMCLogger.logUnexpectedResponse(var4.getId(), var4.getState().toString());
               return this.doReturnWith(var1);
         }
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "processResponse: passing along response for poll " + var3);
         }

         var5 = var1.getMessage().getHeaders();
         var6 = var5.getAction(this._addrVersion, this._soapVersion);
         this.setupCorrelationPropertySetIfNeeded(var1, var2.getRqstAction(), var6);
         return this.doReturnWith(var1);
      }
   }

   private void setupCorrelationPropertySetIfNeeded(Packet var1, String var2, String var3) {
      if (this._defaultFaultAction.equals(var3)) {
         var1.addSatellite(new CorrelationPropertySet(var2));
      }

   }

   private void savePersistentContext(Packet var1, String var2) {
      PersistentContext var3 = PersistentMessageFactory.getInstance().createContextFromPacket(var2, var1);
      PersistenceConfig.Client var4 = PersistenceConfig.getClientConfig(this._context);
      PersistentContext.getStoreMap(var4.getLogicalStoreName()).put(var2, var3);
   }

   private void setupPersistentContext(Packet var1, String var2) {
      PersistenceConfig.Client var3 = PersistenceConfig.getClientConfig(this._context);
      PersistentContextStore var4 = PersistentContext.getStoreMap(var3.getLogicalStoreName());
      PersistentContext var5 = (PersistentContext)var4.get(var2);
      if (var5 != null) {
         String var6 = (String)var1.get("javax.xml.ws.soap.http.soapaction.uri");
         PersistentMessageFactory.getInstance().setContextIntoPacket(var5, var1);
         var1.put("javax.xml.ws.soap.http.soapaction.uri", var6);
         var5.setState(PersistentContext.State.UNUSED);
      }

   }

   private NextAction handlePollResponse(Packet var1, String var2, McPoll var3) throws InterruptedException {
      if (var3 == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "processResponse: no poll instance for poll " + var2);
         }

         var1.setMessage((Message)null);
         return this.doReturnWith(var1);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling poll response for poll " + var2);
         }

         switch (var3.getState()) {
            case EXPIRED:
               return this.doThrow(new McPollingTimeoutException());
            case TERMINATED:
               var1.setMessage((Message)null);
               return this.doReturnWith(var1);
            case ENABLED:
            default:
               WseeMCLogger.logUnexpectedResponse(var3.getId(), var3.getState().toString());
               var1.setMessage((Message)null);
               return this.doReturnWith(var1);
            case POLLING:
               if (var1.getMessage() == null) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Handled poll response with null message for poll " + var2);
                  }

                  return this.doReturnWith(var1);
               } else {
                  this.handlePolledResponseMsg(var1, var2, var3);
                  return this.doReturnWith(var1);
               }
         }
      }
   }

   private void handlePolledResponseMsg(Packet var1, String var2, McPoll var3) throws InterruptedException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Handling poll response message for poll " + var2);
      }

      boolean var4 = false;
      HeaderList var5 = var1.getMessage().getHeaders();
      Header var6 = var5.get(McConstants.Element.MESSAGE_PENDING.getQName(McConstants.McVersion.MC_11), false);
      if (var6 != null) {
         try {
            MessagePendingHeader var7 = (MessagePendingHeader)McHeaderFactory.getInstance().createMcHeaderFromHeader(MessagePendingHeader.class, var6);
            if (var7 != null && var7.getPending()) {
               var4 = true;
            }
         } catch (MsgHeaderException var11) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "processResponse failed: " + var11.toString(), var11);
            }
         }
      }

      var3.resetPollCount(var4);
      String var12 = var5.getRelatesTo(this._addrVersion, this._soapVersion);
      if (this._binding.isFeatureEnabled(AsyncClientHandlerFeature.class)) {
         String var8 = var1.getMessage().getHeaders().getAction(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         if (var1.soapAction == null) {
            var1.soapAction = "\"" + var8 + "\"";
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling poll response with AsyncClientHandlerFeature with RelatesTo " + var12 + " and action " + var8 + " for poll " + var2);
         }

         this.setupCorrelationPropertySetIfNeeded(var1, var3.getPersistentRequestAction(var12), var8);
         this.setupPersistentContext(var1, var12);
         var3.removePersistentRequest(var12);
         McPollManager.getInstance().updatePoll(var3, (ClientInstance)null);
      } else {
         McPollManager.FiberBox var13 = var3.getFiber(var12);
         if (var13 == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "processResponse: no suspended fiber for poll " + var2 + " with id " + var12);
            }
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "processResponse: suspended fiber found for poll " + var2 + " with id " + var12);
            }

            Fiber var9 = var13.get();
            var3.decrementSuspendedFiberCount();
            var3.removeFiber(var12);
            Packet var10 = var9.getPacket().createClientResponse(var1.getMessage());
            var10.wasTransportSecure = var1.wasTransportSecure;
            var9.resume(var10);
            var1.setMessage((Message)null);
         }
      }

   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return this.doThrow(var1);
   }

   private void setMcPolicy() {
      EnvironmentFactory var1 = JAXRPCEnvironmentFeature.getFactory(this._binding, this._service, this._wsdlPort);
      EnvironmentFactory.SingletonService var2 = var1.getService();
      QName var3 = var1.getPort().getName();
      WsPort var4 = var2.getPort(var3.getLocalPart());
      PolicyServer var5 = var2.getPolicyServer();

      try {
         NormalizedExpression var6 = var5.getEndpointPolicy(var4.getWsdlPort());
         this._policy = McPolicyInfoFactory.getInstance(var6);
      } catch (PolicyException var8) {
         WseeMCLogger.logUnexpectedException(var8.toString(), var8);
         throw new RuntimeException(var8.toString(), var8);
      }
   }

   private void setReplyTo(String var1) {
      WSEndpointReference var2;
      String var3;
      if (this._context.getAddress().toString().equals(this._addrVersion.anonymousUri)) {
         var3 = this._wsdlPort.getAddress().toString();
         var2 = new WSEndpointReference(var3, this._addrVersion);
         this._url = this._wsdlPort.getAddress().getURL();
      } else {
         var3 = McConstants.getAnonymousURITemplate(McConstants.McVersion.MC_11) + McProtocolUtils.encodeId(var1);
         var2 = new WSEndpointReference(var3, this._addrVersion);
         this._url = null;
      }

      OneWayFeature var4 = (OneWayFeature)this._binding.getFeature(OneWayFeature.class);
      var4.setReplyTo(var2);
      if (this._mcFeature.isUseMcWithSyncInvoke()) {
         var4.setUseAsyncWithSyncInvoke(true);
      }

   }

   private void checkMcConfig() {
      this._exponentialBackoff = this._mcFeature.isUseExponentialBackoff();

      try {
         DatatypeFactory var1 = DatatypeFactory.newInstance();
         this._intervalDuration = var1.newDuration(this._mcFeature.getInterval());
         this._expirationDuration = var1.newDuration(this._mcFeature.getExpires());
         if (this._binding.getFeatures().isEnabled(WsrmClientRuntimeFeature.class)) {
            WsrmConfig.Source var2 = WsrmConfig.getSource(this._context, (Packet)null, false);
            if (var2 != null) {
               String var3 = var2.getBaseRetransmissionInterval();
               Duration var4 = var1.newDuration(var3);
               if (!this._intervalDuration.isShorterThan(var4)) {
                  Duration var5 = var1.newDuration(3000L);
                  if (var4.isLongerThan(var5)) {
                     this._intervalDuration = var4.subtract(var5);
                  } else {
                     this._intervalDuration = var4;
                  }
               }

               boolean var7 = var2.getRetransmissionExponentialBackoff();
               if (!var7) {
                  this._exponentialBackoff = false;
               }
            }
         }
      } catch (DatatypeConfigurationException var6) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "setMcConfig failed: " + var6.toString(), var6);
         }

         WseeMCLogger.logUnexpectedException(var6.toString(), var6);
      }

      this._configChecked = true;
   }

   public void preDestroy() {
      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new McInitiatorTube(this, var1);
   }

   private class SecurityHanlderListener extends WssHandlerListener {
      private McPoll poll = null;

      public SecurityHanlderListener(McPoll var2) {
         this.poll = var2;
      }

      public void init(Packet var1) {
         registerToProperties(var1.invocationProperties, this);
      }

      public void postHandlingRequest(MessageContext var1) {
         if (this.poll != null) {
            try {
               this.processSCCredential(var1);
            } catch (Exception var6) {
               McInitiatorTube.LOGGER.log(Level.FINE, "SCCredential processing failed: " + var6.toString(), var6);
            } finally {
               if (this.poll.getCredential() != null) {
                  this.poll = null;
               }

            }

         }
      }

      private void processSCCredential(MessageContext var1) {
         Map var3 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");
         SCCredential var2;
         if (var3 != null) {
            var2 = (SCCredential)var3.get("weblogic.wsee.wssc.sct");
         } else {
            var2 = (SCCredential)var1.getProperty("weblogic.wsee.wssc.sct");
         }

         this.poll.setCredential(var2);
      }

      public boolean isDisposed() {
         return this.poll == null;
      }
   }
}
