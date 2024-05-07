package weblogic.wsee.reliability;

import java.io.Externalizable;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFTransportException;
import weblogic.messaging.saf.common.SAFConversationHandleImpl;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.FaultToHeader;
import weblogic.wsee.addressing.FromHeader;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.addressing.ToHeader;
import weblogic.wsee.async.AsyncInvokeState;
import weblogic.wsee.async.AsyncInvokeStateObjectHandler;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.http.HTTPClientTransport;
import weblogic.wsee.connection.transport.https.HttpsTransportInfo;
import weblogic.wsee.connection.transport.https.SSLAdapter;
import weblogic.wsee.conversation.ConversationInvokeState;
import weblogic.wsee.conversation.ConversationInvokeStateObjectHandler;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderFactory;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.faults.CreateSequenceRefusedFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.reliability.handshake.CloseSequenceMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.HandshakeMsgException;
import weblogic.wsee.reliability.handshake.SequenceAccept;
import weblogic.wsee.reliability.handshake.SequenceOffer;
import weblogic.wsee.reliability.handshake.TerminateSequenceMsg;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSSLHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSTRHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyUtils;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.sct.SCTokenHandler;
import weblogic.wsee.security.wssc.v200502.sct.SCTHelper;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.ws.dispatch.client.MimeHeaderHandler;
import weblogic.wsee.ws.dispatch.server.ConnectionHandler;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WsrmSequenceSender {
   private static final boolean verbose = Verbose.isVerbose(WsrmSequenceSender.class);
   private static WsStorage storage = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

   public static Externalizable send(SAFConversationInfo var0, SAFRequest var1) throws SAFTransportException {
      long var2 = System.nanoTime();
      if (verbose) {
         Verbose.say(var2 + " Entering WsrmSequenceSender.send(SAFConversationInfo,SAFRequest)");
      }

      long var4 = var1.getSequenceNumber();
      WsrmSequenceContext var6 = (WsrmSequenceContext)var0.getContext();
      WsrmPayloadContext var7 = (WsrmPayloadContext)var1.getPayloadContext();
      EndpointReference var8 = getConversationEPR(var7);
      SOAPInvokeState var9 = (SOAPInvokeState)var1.getPayload();
      if (var9 == null) {
         throw new SAFTransportException("No payload found.");
      } else {
         SOAPMessage var10 = var9.getClonedSOAPMessage();
         AddressingProvider var11 = AddressingProviderFactory.getInstance().getAddressingProvider(var9.getMessageContextProperties());

         try {
            SOAPHeader var12 = var10.getSOAPHeader();
            if (var12 == null) {
               throw new SAFTransportException("No SOAPHeader found in SOAPMessage.");
            }

            if (var7 != null && var7.getStartConversation()) {
               setStartConversationHdrs(var12, var7, var11);
            }

            if (WSAVersion.MemberSubmission.equals(var11.getWSAVersion())) {
               removeFromHeader(var12);
            }

            applyConversationEPRToContinueMsg(var8, var12);
            adjustSequenceHeaders(var12, var4, var0);
         } catch (DOMProcessingException var15) {
            if (verbose) {
               Verbose.logException(var15);
            }

            throw new SAFTransportException("DOMProcessingException", var15);
         } catch (SOAPException var16) {
            if (verbose) {
               Verbose.logException(var16);
            }

            throw new SAFTransportException("SOAPException", var16);
         }

         SoapMessageContext var17 = createSendSoapContext(var6, var9, var10, var7);
         if (verbose) {
            Verbose.say(var2 + " Inside WsrmSequenceSender.send(SAFConversationInfo,SAFRequest) == 1");
         }

         boolean var13 = checkForAutoTerminateOnOfferSequence(var9, var17, var6, var0, var4);
         if (verbose) {
            Verbose.say(var2 + " Inside WsrmSequenceSender.send(SAFConversationInfo,SAFRequest) == 2");
         }

         SoapMessageContext var14 = sendMessageWithSubject(var17, var0.getDestinationURL(), var0, var6);
         if (verbose) {
            Verbose.say(var2 + " Inside WsrmSequenceSender.send(SAFConversationInfo,SAFRequest) == 3");
         }

         if (var14 != null) {
            handleSyncSendResponse(var14, var6, var0);
         }

         var13 |= updateRenewedToken(var17, var6) != null;
         if (var13) {
            if (verbose) {
               Verbose.say(var2 + " Inside WsrmSequenceSender.send(SAFConversationInfo,SAFRequest) == 4");
            }

            return var6;
         } else {
            return null;
         }
      }
   }

   private static boolean checkForAutoTerminateOnOfferSequence(SOAPInvokeState var0, SoapMessageContext var1, WsrmSequenceContext var2, SAFConversationInfo var3, long var4) throws SAFTransportException {
      boolean var6 = false;
      ActionHeader var7 = (ActionHeader)var1.getHeaders().getHeader(ActionHeader.TYPE);
      if (var7 != null && WsrmConstants.Action.LAST_MESSAGE.getActionURI(var2.getRmVersion()).equals(var7.getActionURI())) {
         if (verbose) {
            Verbose.log((Object)"WsrmSequenceSender processing empty LastMessage message. Check for auto-terminate bypassed, as we know the current sequence is now ready to be terminated.");
         }

         return var6;
      } else {
         if (var0.getMessageContextProperties().containsKey("weblogic.wsee.async.res")) {
            String var8 = (String)var0.getMessageContextProperties().get("weblogic.wsee.reliability.RequestMessageSeqNumber");
            long var9 = Long.parseLong(var8);
            if (var9 > 0L) {
               boolean var11 = false;
               if (var0.getMessageContextProperties().containsKey("weblogic.wsee.conversation.AsyncConvId")) {
                  var11 = true;
               }

               if (verbose) {
                  String var12 = var11 ? "conversation ID" : "actual response";
                  Verbose.say("*** Doing " + var12 + " mapping of requestSeqNum " + var9 + " to " + var4 + " on offer sequence " + getSequenceIdFromConversationInfo(var3));
               }

               var2.mapRequestSeqNumToResponseSeqNum(var9, var4);
               var6 = true;
               WsrmSAFReceivingManager var15 = WsrmSAFManagerFactory.getWsrmSAFReceivingManager();

               try {
                  EndpointReference var13 = (EndpointReference)var0.getMessageContextProperties().get("weblogic.wsee.addressing.ReplyTo");
                  if (var13 == null || AddressingHelper.isAnonymousReferenceURI(var13.getAddress())) {
                     var13 = new EndpointReference(var3.getDestinationURL());
                  }

                  var15.checkForAutoTerminateOnOfferSequence(getSequenceIdFromConversationInfo(var3), var2, var13);
               } catch (SAFException var14) {
                  throw new SAFTransportException(var14.toString(), var14);
               }
            }
         }

         return var6;
      }
   }

   private static SoapMessageContext createSendSoapContext(WsrmSequenceContext var0, SOAPMessage var1) {
      SoapMessageContext var2 = new SoapMessageContext(var0.isSoap12());
      if (var0.getFrom() != null) {
         var2.setProperty("weblogic.wsee.addressing.From", var0.getFrom());
      }

      var2.setMessage(var1);
      setWSAVersion(var2, var0.getWsaVersion());
      return var2;
   }

   private static SoapMessageContext createSendSoapContext(WsrmSequenceContext var0, SOAPInvokeState var1, SOAPMessage var2, WsrmPayloadContext var3) {
      SoapMessageContext var4 = new SoapMessageContext(var0.isSoap12());
      Iterator var5 = var1.getMessageContextProperties().keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         var4.setProperty(var6, var1.getMessageContextProperties().get(var6));
      }

      var4.setMessage(var2);
      if (var3 != null) {
         var4.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var3.getRequestPolicy());
         var4.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var3.getResponsePolicy());
         var4.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", WssPolicyUtils.getContext());
      }

      if (var0.getWsrmSecurityContext().isSecureWithWssc()) {
         var4.setProperty("weblogic.wsee.wssc.sct", var0.getWsrmSecurityContext().getSCCredential());
      }

      return var4;
   }

   private static void applyConversationEPRToContinueMsg(EndpointReference var0, SOAPHeader var1) throws DOMProcessingException {
      if (var0 != null && DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "ContinueHeader") == null) {
         Iterator var2 = var0.getReferenceProperties().listHeaders();

         MsgHeader var3;
         while(var2.hasNext()) {
            var3 = (MsgHeader)var2.next();
            var3.writeToParent(var1);
         }

         var2 = var0.getReferenceParameters().listHeaders();

         while(var2.hasNext()) {
            var3 = (MsgHeader)var2.next();
            var3.writeToParent(var1);
         }
      }

   }

   private static Externalizable updateRenewedToken(SoapMessageContext var0, WsrmSequenceContext var1) {
      if (!var1.getWsrmSecurityContext().isSecureWithWssc()) {
         return null;
      } else {
         SCCredential var2 = var1.getWsrmSecurityContext().getSCCredential();
         if (var2 == null) {
            throw new JAXRPCException("No credential found for RM sequence");
         } else {
            SCCredential var3 = (SCCredential)var0.getProperty("weblogic.wsee.wssc.sct");
            if (var3 == null) {
               throw new JAXRPCException("No credential found sending message");
            } else if (!var2.getExpires().equals(var3.getExpires())) {
               WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RENEW_RES_BEFORE_SAVE);
               var1.getWsrmSecurityContext().setSCCredential(var3);
               return var1;
            } else {
               return null;
            }
         }
      }
   }

   private static void saveMCForAsyncResponse(SoapMessageContext var0) {
      if (var0.getProperty("weblogic.wsee.async.res") == null && var0.getProperty("weblogic.wsee.async.invoke") != null) {
         MessageIdHeader var1 = (MessageIdHeader)var0.getHeaders().getHeader(MessageIdHeader.TYPE);

         assert var1 != null;

         String var2 = var1.getMessageId();

         try {
            AsyncInvokeState var3 = (AsyncInvokeState)storage.persistentGet(var2);
            if (var3 != null) {
               if (verbose) {
                  Verbose.log((Object)("Updating AsyncInvokeState MessageContext field for request " + var1.getMessageId() + " prior to sending it as a sequence message"));
               }

               var3.setMessageContext(var0);
               storage.persistentPut(var2, var3);
            }
         } catch (PersistentStoreException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new JAXRPCException(var4);
         }
      }

   }

   private static void handleSyncSendResponse(SoapMessageContext var0, WsrmSequenceContext var1, SAFConversationInfo var2) throws SAFTransportException {
      handleResponseSecurity(var0, var1, var2);
      SOAPMessage var3 = var0.getMessage();

      SOAPBody var4;
      try {
         var4 = var3.getSOAPBody();
      } catch (SOAPException var10) {
         if (verbose) {
            Verbose.logException(var10);
         }

         throw new SAFTransportException("SOAPException", true, var10);
      }

      if (var4 == null) {
         throw new SAFTransportException("Empty reply body");
      } else if (!var4.hasFault()) {
         MsgHeaders var12 = var0.getHeaders();

         assert var12 != null;

         AcknowledgementHeader var13 = (AcknowledgementHeader)var12.getHeader(AcknowledgementHeader.TYPE);
         if (var13 != null) {
            FaultToHeader var7 = (FaultToHeader)var12.getHeader(FaultToHeader.TYPE);
            if (var7 != null) {
               var0.setProperty("weblogic.wsee.addressing.FaultTo", var7.getReference());
            } else {
               ReplyToHeader var8 = (ReplyToHeader)var12.getHeader(ReplyToHeader.TYPE);
               if (var8 != null) {
                  var0.setProperty("weblogic.wsee.addressing.FaultTo", var8.getReference());
               } else {
                  FromHeader var9 = (FromHeader)var12.getHeader(FromHeader.TYPE);
                  if (var9 != null) {
                     var0.setProperty("weblogic.wsee.addressing.FaultTo", var9.getReference());
                  } else {
                     var0.setProperty("weblogic.wsee.addressing.FaultTo", AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var0).createAnonymousEndpointReference());
                  }
               }
            }

            WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleAck(var13, var0, true);
         } else {
            if (verbose) {
               Verbose.log((Object)"Got unknown response back from sending a reliable message");
            }

         }
      } else {
         try {
            SequenceFaultMsg var5 = SequenceFaultMsgFactory.getInstance().parseSoapFault(var3, var1.getRmVersion());
            if (verbose) {
               Verbose.log((Object)("Found fault: " + var5.getSubCodeQualifiedName()));
            }

            WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleFault(var5);
         } catch (SequenceFaultException var11) {
            if (verbose) {
               SOAPFault var6 = var4.getFault();
               Verbose.log((Object)("Received a fault message: " + var6.getFaultString()));
            }
         }

         throw new SAFTransportException("Send failed, received a fault message");
      }
   }

   private static boolean handleResponseSecurity(SoapMessageContext var0, WsrmSequenceContext var1, SAFConversationInfo var2) {
      if (var1 == null) {
         return false;
      } else if (!var1.isSecure()) {
         return true;
      } else {
         try {
            WsrmSecurityContext var3 = var1.getWsrmSecurityContext();
            var0.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var3.getSecurityPolicy());
            var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", WssPolicyUtils.getContext());
            if (var3.isSecureWithWssc()) {
               var0.setProperty("weblogic.wsee.wssc.sct", var3.getSCCredential());
            }
         } catch (PolicyException var6) {
            if (verbose) {
               Verbose.logException(var6);
            }

            throw new JAXRPCException("Policy exception", var6);
         }

         boolean var7 = AsyncUtil.getWssClientHandler(var1).handleResponse(var0);
         if (var7) {
            GenericHandler var4 = AsyncUtil.getWssClientPolicyHandler(var1);
            if (var4 != null) {
               var7 = var4.handleResponse(var0);
            }
         }

         if (!var7) {
            return false;
         } else {
            try {
               WsrmServerHandshakeHandler.validateCredential(var2, var0);
               return true;
            } catch (Throwable var5) {
               if (verbose) {
                  Verbose.logException(var5);
               }

               return false;
            }
         }
      }
   }

   private static void adjustSequenceHeaders(SOAPHeader var0, long var1, SAFConversationInfo var3) throws DOMProcessingException {
      WsrmSequenceContext var4 = (WsrmSequenceContext)var3.getContext();
      WsrmConstants.RMVersion var5 = var4.getRmVersion();
      Element var6 = DOMUtils.getElementByTagNameNS(var0, var5.getNamespaceUri(), WsrmConstants.Element.SEQUENCE.getElementName());
      Element var7 = DOMUtils.getElementByTagNameNS(var6, var5.getNamespaceUri(), WsrmConstants.Element.MESSAGE_NUMBER.getElementName());
      NodeList var8 = var7.getChildNodes();

      assert var8.getLength() == 1;

      byte var9 = 0;
      if (var9 < var8.getLength()) {
         Node var10 = var8.item(var9);
         if (var10.getNodeType() == 3 || var10.getNodeType() == 4) {
            var10.setNodeValue(Long.toString(var1));
         }
      }

      if (var3.isDynamic()) {
         Element var12 = DOMUtils.getElementByTagNameNS(var6, var5.getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         var8 = var12.getChildNodes();

         assert var8.getLength() == 1;

         byte var13 = 0;
         if (var13 < var8.getLength()) {
            Node var11 = var8.item(var13);
            if (var11.getNodeType() == 3 || var11.getNodeType() == 4) {
               if (verbose) {
                  Verbose.log((Object)("Map sender side conversation name " + var11.getNodeValue() + " to receiver side conversation name " + var3.getDynamicConversationName()));
               }

               var11.setNodeValue(var3.getDynamicConversationName());
            }
         }
      }

   }

   private static void setStartConversationHdrs(SOAPHeader var0, WsrmPayloadContext var1, AddressingProvider var2) throws DOMProcessingException {
      String var3 = var2.getNamespaceURI();
      Element var4 = DOMUtils.getElementByTagNameNS(var0, var3, "ReplyTo");
      Element var5 = DOMUtils.getElementByTagNameNS(var4, var3, "ReferenceParameters");
      if (DOMUtils.getOptionalElementByTagNameNS(var5, "http://www.bea.com/safserver", "SAFServer") == null) {
         SAFServerHeader var6 = new SAFServerHeader();
         var6.setServerName(LocalServerIdentity.getIdentity().getServerName());
         var6.setConversationKey(var1.getConversationKey());
         var6.writeToParent(var5);
      }

   }

   private static EndpointReference getConversationEPR(WsrmPayloadContext var0) throws SAFTransportException {
      if (var0 != null && var0.getWaitForConversationId()) {
         WsStorage var1 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());

         try {
            ConversationInvokeState var2 = (ConversationInvokeState)var1.persistentGet(var0.getConversationKey());
            EndpointReference var3;
            if (var2 != null && (var3 = var2.getEpr()) != null) {
               return var3;
            } else {
               throw new SAFTransportException("Conversational message can only be sent after conversation ID has been sent back from the server.  This exception is expected and should not be a cause for concern.");
            }
         } catch (PersistentStoreException var5) {
            if (verbose) {
               Verbose.logException(var5);
            }

            throw new JAXRPCException(var5);
         }
      } else {
         return null;
      }
   }

   public static AcknowledgementHeader ackRequest(SAFConversationInfo var0) throws SAFTransportException {
      WsrmSequenceContext var1 = (WsrmSequenceContext)var0.getContext();
      boolean var2 = var1.isSoap12();
      SOAPMessage var3 = createSOAPMessage(var2);
      SoapMessageContext var4 = new SoapMessageContext(var2);
      var4.setMessage(var3);
      if (var1.getAcksTo() != null) {
         setWSAVersion(var4, var1.getWsaVersion());
      }

      String var5 = getLifecycleEndpointAddress(var1, var0);
      setupAckRequestAddressingHeaders(var1, var5, var4, var0, var3);
      AckRequestedHeader var6 = new AckRequestedHeader(var1.getRmVersion());
      String var7 = getSequenceIdFromConversationInfo(var0);
      var6.setSequenceId(var7);
      var4.getHeaders().addHeader(var6);
      if (verbose) {
         Verbose.log((Object)("Send acknowledgement request to " + var5));
      }

      SoapMessageContext var8 = sendMessageWithSubject(var4, var5, var0, var1);
      if (var8 != null) {
         SOAPMessage var9 = var8.getMessage();

         SOAPBody var10;
         try {
            var10 = var9.getSOAPBody();
         } catch (SOAPException var13) {
            if (verbose) {
               Verbose.logException(var13);
            }

            throw new SAFTransportException("SOAPException", true, var13);
         }

         if (var10 == null) {
            throw new SAFTransportException("Empty reply body");
         } else if (!var10.hasFault()) {
            MsgHeaders var11 = var8.getHeaders();

            assert var11 != null;

            AcknowledgementHeader var12 = (AcknowledgementHeader)var11.getHeader(AcknowledgementHeader.TYPE);
            return var12;
         } else {
            throw new SAFTransportException("Ack request reply has fault");
         }
      } else {
         return null;
      }
   }

   private static void setupAckRequestAddressingHeaders(WsrmSequenceContext var0, String var1, SoapMessageContext var2, SAFConversationInfo var3, SOAPMessage var4) {
      AddressingProvider var5 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
      ToHeader var6 = var5.createToHeader(var1);
      String var7 = WsrmConstants.Action.ACK_REQUESTED.getActionURI(var0.getRmVersion());
      ActionHeader var8 = var5.createActionHeader(var7);
      var2.getHeaders().addHeader(var6);
      var2.getHeaders().addHeader(var8);
      var2.getHeaders().addHeader(var5.createMessageIdHeader(Guid.generateGuid()));
      var2.getHeaders().addHeader(var5.createFromHeader(var5.createEndpointReference(var3.getSourceURL())));
      if (!AsyncUtil.isSoap12(var2)) {
         var4.getMimeHeaders().setHeader("SOAPAction", var7);
      }

   }

   public static Externalizable createSecurityToken(SAFConversationInfo var0) throws SAFTransportException {
      try {
         WsrmSequenceContext var1 = (WsrmSequenceContext)var0.getContext();
         var1.setSequenceCreator(true);
         WsrmSecurityContext var2 = var1.getWsrmSecurityContext();
         if (var2.isSecureWithWssc()) {
            if (var2.getSCCredential() != null) {
               return null;
            } else {
               if (var2.isSecureWithWssp12Wssc13()) {
                  performWSSC13Handshake(var1, var0.getDestinationURL());
               } else {
                  performWSSCHandshake(var1, var0.getDestinationURL());
               }

               return var1;
            }
         } else {
            return null;
         }
      } catch (Exception var3) {
         if (verbose) {
            Verbose.logException(var3);
         }

         if (var3 instanceof SAFTransportException) {
            throw (SAFTransportException)var3;
         } else {
            throw (RuntimeException)var3;
         }
      }
   }

   public static SAFConversationHandle createSequence(SAFConversationInfo var0) throws SAFTransportException {
      try {
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RSTR_BEFORE_CREATE_SEQ);
         WsrmSequenceContext var1 = (WsrmSequenceContext)var0.getContext();
         boolean var2 = var1.isSoap12();
         SOAPMessage var3 = createSOAPMessage(var2);
         SoapMessageContext var4 = createSendSoapContext(var1, var3);
         if (var1.getAcksTo() != null) {
            setWSAVersion(var4, var1.getAcksTo().getNamespaceURI());
         }

         String var5 = var0.getDestinationURL();
         String var6 = var0.getCreateConversationMessageID();
         setupCreateSequenceAddressingHeaders(var1, var4, var5, var6, var3);
         CreateSequenceMsg var7 = new CreateSequenceMsg(var1.getRmVersion());
         var7.setAcksTo(var1.getAcksTo());
         setupCreateSequenceSecurity(var1, var7, var4);
         setupCreateSequenceExpires(var0, var7);
         SAFConversationInfo var8 = var0.getConversationOffer();
         AddressingProvider var9 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var4);
         if (var8 != null) {
            setupCreateSequenceOffer(var8, var7, var4);
         } else {
            var4.getHeaders().addHeader(var9.createReplyToHeader(var1.getAcksTo()));
         }

         if (verbose) {
            Verbose.log((Object)("Send create to " + var5));
         }

         try {
            var7.writeMsg(var3);
         } catch (HandshakeMsgException var11) {
            if (verbose) {
               Verbose.logException(var11);
            }

            throw new SAFTransportException("Failed to write Create Sequence message body", var11);
         }

         applySecurity(var4, var1);
         SoapMessageContext var10 = sendMessageWithSubject(var4, var5, var0, var1);
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_CREATE_SEQ_BEFORE_RES);
         return var10 == null ? null : handleSyncCreateSequenceResponse(var10, var1, var0, var8, var6);
      } catch (Exception var12) {
         if (verbose) {
            Verbose.logException(var12);
         }

         if (var12 instanceof SAFTransportException) {
            throw (SAFTransportException)var12;
         } else {
            throw (RuntimeException)var12;
         }
      }
   }

   private static SAFConversationHandle handleSyncCreateSequenceResponse(SoapMessageContext var0, WsrmSequenceContext var1, SAFConversationInfo var2, SAFConversationInfo var3, String var4) throws SAFTransportException {
      handleResponseSecurity(var0, var1, var2);
      SOAPMessage var5 = var0.getMessage();

      try {
         SOAPBody var6 = var5.getSOAPBody();
         if (var6 == null) {
            throw new SAFTransportException("Empty body for create sequence response message");
         } else {
            Element var7 = DOMUtils.getOptionalElementByTagNameNS(var6, var1.getRmVersion().getNamespaceUri(), WsrmConstants.Element.CREATE_SEQUENCE_RESPONSE.getElementName());
            if (var7 != null) {
               return handleCreateSequenceResponseElement(var7, var3, var2, var4, var1);
            } else {
               handleCreateSequenceResponseFault(var6, var1);
               return null;
            }
         }
      } catch (SOAPException var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new SAFTransportException("SOAPException", var8);
      } catch (DOMProcessingException var9) {
         if (verbose) {
            Verbose.logException(var9);
         }

         throw new SAFTransportException("DOMProcessingException", var9);
      }
   }

   private static void handleCreateSequenceResponseFault(SOAPBody var0, WsrmSequenceContext var1) {
      SOAPFault var2 = var0.getFault();
      if (var2 == null) {
         throw new JAXRPCException("Protocol failure in create sequence");
      } else if (var2.getFaultCodeAsQName().equals((new CreateSequenceRefusedFaultMsg(new Exception(), var1.getRmVersion())).getSubCodeQName())) {
         throw new JAXRPCException("Create sequence refused by the receiving side");
      } else {
         throw new JAXRPCException("Create sequence failed, received a fault message: " + var2.getFaultString());
      }
   }

   private static SAFConversationHandle handleCreateSequenceResponseElement(Element var0, SAFConversationInfo var1, SAFConversationInfo var2, String var3, WsrmSequenceContext var4) {
      CreateSequenceResponseMsg var5 = new CreateSequenceResponseMsg(var4.getRmVersion());
      var5.read(var0);
      String var6 = var5.getSequenceId();
      SequenceAccept var7 = var5.getAccept();
      if (var7 != null) {
         EndpointReference var8 = var7.getAcksTo();
         if (var1 != null) {
            WsrmSequenceContext var9 = new WsrmSequenceContext();
            var9.setRmVersion(var4.getRmVersion());
            var9.setWsaVersion(var4.getWsaVersion());
            var9.setFrom(var4.getFrom());
            var9.setAcksTo(var8);
            var1.setContext(var9);
         }
      }

      Duration var11 = var5.getExpires();
      long var12 = Long.MAX_VALUE;
      if (var11 != null) {
         var12 = var11.getTimeInMillis(new Date());
      }

      WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_CREATE_SEQ_RES_BEFORE_RM);
      return new SAFConversationHandleImpl(var6, var12, var2.getMaximumIdleTime(), var1, var3, var4);
   }

   private static void setupCreateSequenceOffer(SAFConversationInfo var0, CreateSequenceMsg var1, SoapMessageContext var2) {
      SequenceOffer var3 = new SequenceOffer(var1.getRmVersion());
      var3.setSequenceId(getSequenceIdFromConversationInfo(var0));
      long var4 = var0.getTimeToLive();
      if (var4 != Long.MAX_VALUE) {
         try {
            Duration var6 = DatatypeFactory.newInstance().newDuration(var4);
            var3.setExpires(var6.toString());
         } catch (DatatypeConfigurationException var9) {
            throw new JAXRPCException(var9);
         } catch (Throwable var10) {
            throw new JAXRPCException(var10);
         }
      }

      if (var1.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var3.setEndpoint(var1.getAcksTo());
         var3.setIncompleteSequenceBehavior(WsrmConstants.IncompleteSequenceBehavior.NoDiscard);
      }

      var1.setOffer(var3);
      AddressingProvider var11 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
      EndpointReference var7 = var11.createEndpointReference(var0.getDestinationURL());
      ServiceIdentityHeader var8 = new ServiceIdentityHeader();
      var8.setServerName(LocalServerIdentity.getIdentity().getServerName());
      var8.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
      var7.getReferenceParameters().addHeader(var8);
      var2.getHeaders().addHeader(var11.createReplyToHeader(var7));
   }

   private static void setupCreateSequenceSecurity(WsrmSequenceContext var0, CreateSequenceMsg var1, SoapMessageContext var2) {
      WsrmSecurityContext var3 = var0.getWsrmSecurityContext();
      if (var3.isSecureWithWssc()) {
         try {
            Object var6;
            if (var3.isSecureWithWssp12Wssc13()) {
               var6 = new SCTokenHandler();
            } else {
               var6 = new weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler();
            }

            SCTokenBase var4 = (SCTokenBase)((SecurityTokenHandler)var6).getSecurityToken("NO-VALUE-TYPE", var3.getSCCredential(), (ContextHandler)null);
            SecurityTokenReference var5 = ((SecurityTokenHandler)var6).getSTR(WSSConstants.REFERENCE_QNAME, var4.getValueType(), var4);
            var1.setSecurityTokenReference(var5);
            if (var1.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
               var2.getHeaders().addHeader(new UsesSequenceSTRHeader(var1.getRmVersion()));
            }
         } catch (WSSecurityException var7) {
            if (verbose) {
               Verbose.logException(var7);
            }
         }
      }

      if (var3.isSecureWithSSL() && var1.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var2.getHeaders().addHeader(new UsesSequenceSSLHeader(var1.getRmVersion()));
      }

   }

   private static void setupCreateSequenceExpires(SAFConversationInfo var0, CreateSequenceMsg var1) {
      long var2 = var0.getTimeToLive();
      if (var2 != Long.MAX_VALUE) {
         try {
            Duration var4 = DatatypeFactory.newInstance().newDuration(var2);
            var1.setExpires(var4);
         } catch (DatatypeConfigurationException var5) {
            throw new JAXRPCException(var5);
         } catch (Throwable var6) {
            throw new JAXRPCException(var6);
         }
      }

   }

   private static void setupCreateSequenceAddressingHeaders(WsrmSequenceContext var0, SoapMessageContext var1, String var2, String var3, SOAPMessage var4) {
      AddressingProvider var5 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      var1.getHeaders().addHeader(var5.createToHeader(var2));
      var1.getHeaders().addHeader(var5.createFaultToHeader(var5.createAnonymousEndpointReference()));
      var1.getHeaders().addHeader(var5.createMessageIdHeader(var3));
      String var6 = WsrmConstants.Action.CREATE_SEQUENCE.getActionURI(var0.getRmVersion());
      var1.getHeaders().addHeader(var5.createActionHeader(var6));
      if (!AsyncUtil.isSoap12(var1)) {
         var4.getMimeHeaders().setHeader("SOAPAction", var6);
      }

   }

   private static boolean applySecurity(SoapMessageContext var0, WsrmSequenceContext var1) throws SAFTransportException {
      if (var1 != null && var1.isSecure()) {
         try {
            WsrmSecurityContext var2 = var1.getWsrmSecurityContext();
            NormalizedExpression var3 = var2.getSecurityPolicy();
            var0.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var3);
            if (verbose) {
               Verbose.log((Object)var3);
            }

            var0.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var3);
            var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", WssPolicyUtils.getContext());
            var0.setProperty("weblogic.wsee.security.wss.CredentialProviderList", WssPolicyUtils.getCredentialProviders());
            if (var2.isSecureWithWssc()) {
               var0.setProperty("weblogic.wsee.wssc.sct", var2.getSCCredential());
            }

            return true;
         } catch (PolicyException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new SAFTransportException("Failed to set policy for create sequence", var4);
         } catch (WssConfigurationException var5) {
            if (verbose) {
               Verbose.logException(var5);
            }

            throw new SAFTransportException("Failed to set policy for create sequence", var5);
         }
      } else {
         return false;
      }
   }

   public static void closeSequence(String var0) throws SAFTransportException {
      SAFConversationInfo var1 = WsrmSAFManager.getConversationInfo(true, var0, false);
      WsrmSequenceContext var2 = (WsrmSequenceContext)var1.getContext();
      boolean var3 = var2.isSoap12();
      SOAPMessage var4 = createSOAPMessage(var3);
      SoapMessageContext var5 = new SoapMessageContext(var3);
      var5.setMessage(var4);
      setWSAVersion(var5, var2.getWsaVersion());
      String var6 = getLifecycleEndpointAddress(var2, var1);
      String var7 = WsrmConstants.Action.CLOSE_SEQUENCE.getActionURI(var2.getRmVersion());
      setupEndOfLifeAddressingHeaders(var7, var6, var5, var4, var2);
      if (verbose) {
         Verbose.log((Object)("Map sender side conversation name " + var1.getConversationName() + " to receiver side conversation name " + var1.getDynamicConversationName()));
      }

      CloseSequenceMsg var8 = new CloseSequenceMsg(var2.getRmVersion(), var1.getDynamicConversationName());
      if (var2.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         long var9;
         try {
            var9 = SAFManagerImpl.getManager().getLastAssignedSequenceValueOnSendingSide(var0);
         } catch (SAFException var12) {
            throw new JAXRPCException(var12.toString(), var12);
         }

         var8.setLastMsgNumber(var9);
      }

      try {
         var8.writeMsg(var4);
      } catch (HandshakeMsgException var13) {
         if (verbose) {
            Verbose.logException(var13);
         }

         throw new SAFTransportException("Failed to write CloseSequence message body", var13);
      }

      if (verbose) {
         Verbose.log((Object)("Close sequence " + var0 + " to " + var6));
      }

      applySecurity(var5, var2);
      var2.setClosed(true);

      try {
         SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var0, var2);
      } catch (SAFException var14) {
         if (verbose) {
            Verbose.logException(var14);
         }
      }

      SoapMessageContext var15 = sendMessageWithSubject(var5, var6, var1, var2);
      if (var2.getRmVersion().isBefore(WsrmConstants.RMVersion.RM_11)) {
         if (var15 != null && verbose) {
            Verbose.log((Object)"Got unexpected reply from close sequence message");
         }
      } else if (var15 != null) {
         handleSyncCloseSequenceResponse(var15, var0, var2, var1);
      }

   }

   private static void handleSyncCloseSequenceResponse(SoapMessageContext var0, String var1, WsrmSequenceContext var2, SAFConversationInfo var3) throws SAFTransportException {
      handleResponseSecurity(var0, var2, var3);
      SOAPMessage var4 = var0.getMessage();

      try {
         SOAPBody var5 = var4.getSOAPBody();
         if (var5 == null) {
            throw new SAFTransportException("Empty body for close sequence response message");
         } else {
            Element var6 = DOMUtils.getOptionalElementByTagNameNS(var5, var2.getRmVersion().getNamespaceUri(), WsrmConstants.Element.CLOSE_SEQUENCE_RESPONSE.getElementName());
            if (var6 != null) {
               CloseSequenceResponseMsg var7 = new CloseSequenceResponseMsg(var2.getRmVersion());
               var7.read(var6);
               WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleCloseSequenceResponse(var7);
            } else {
               var2.setClosed(false);

               try {
                  SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var1, var2);
               } catch (SAFException var8) {
                  if (verbose) {
                     Verbose.logException(var8);
                  }
               }

               handleCloseSequenceResponseFault(var5);
            }

         }
      } catch (SOAPException var9) {
         if (verbose) {
            Verbose.logException(var9);
         }

         throw new SAFTransportException("SOAPException", var9);
      } catch (DOMProcessingException var10) {
         if (verbose) {
            Verbose.logException(var10);
         }

         throw new SAFTransportException("DOMProcessingException", var10);
      }
   }

   private static void handleCloseSequenceResponseFault(SOAPBody var0) {
      SOAPFault var1 = var0.getFault();
      if (var1 == null) {
         throw new JAXRPCException("Protocol failure in close sequence");
      } else {
         throw new JAXRPCException("Close sequence failed, received a fault message: " + var1.getFaultString());
      }
   }

   public static void terminateSequence(SAFConversationInfo var0, boolean var1) throws SAFTransportException {
      if (!var1) {
         terminateAndDestroyConversationWithSAF(var0);
      } else {
         WsrmSequenceContext var2 = (WsrmSequenceContext)var0.getContext();
         boolean var3 = var2.isSoap12();
         SOAPMessage var4 = createSOAPMessage(var3);
         SoapMessageContext var5 = new SoapMessageContext(var3);
         var5.setMessage(var4);
         setWSAVersion(var5, var2.getWsaVersion());
         String var6 = getLifecycleEndpointAddress(var2, var0);
         String var7 = WsrmConstants.Action.TERMINATE_SEQUENCE.getActionURI(var2.getRmVersion());
         setupEndOfLifeAddressingHeaders(var7, var6, var5, var4, var2);
         String var8 = getSequenceIdFromConversationInfo(var0);
         if (var0.isDynamic() && var0.getDynamicConversationName() != null && verbose) {
            Verbose.log((Object)("Map sender side conversation name " + var0.getConversationName() + " to receiver side conversation name " + var0.getDynamicConversationName()));
         }

         TerminateSequenceMsg var9 = new TerminateSequenceMsg(var2.getRmVersion(), var8);
         if (var2.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            long var10;
            try {
               var10 = SAFManagerImpl.getManager().getLastAssignedSequenceValueOnSendingSide(var0.getConversationName());
            } catch (SAFException var13) {
               throw new JAXRPCException(var13.toString(), var13);
            }

            var9.setLastMsgNumber(var10);
         }

         try {
            var9.writeMsg(var4);
         } catch (HandshakeMsgException var14) {
            if (verbose) {
               Verbose.logException(var14);
            }

            throw new SAFTransportException("Failed to write Terminate Sequence message body", var14);
         }

         if (verbose) {
            Verbose.log((Object)("Terminate sequence " + var8 + " to " + var6));
         }

         applySecurity(var5, var2);
         SoapMessageContext var15 = sendMessageWithSubject(var5, var6, var0, var2);
         if (var2.getRmVersion().isBefore(WsrmConstants.RMVersion.RM_11)) {
            if (var15 != null && verbose) {
               Verbose.log((Object)"Got unexpected reply from terminate sequence message");
            }
         } else if (var15 != null) {
            handleSyncTerminateSequenceResponse(var15, var2, var0);
         }

      }
   }

   private static String getSequenceIdFromConversationInfo(SAFConversationInfo var0) {
      String var1;
      if (var0.isDynamic() && var0.getDynamicConversationName() != null) {
         var1 = var0.getDynamicConversationName();
         if (verbose) {
            Verbose.log((Object)("Map sender side conversation name " + var0.getConversationName() + " to receiver side conversation name " + var0.getDynamicConversationName()));
         }
      } else {
         var1 = var0.getConversationName();
      }

      return var1;
   }

   private static void terminateAndDestroyConversationWithSAF(SAFConversationInfo var0) {
      try {
         SAFManagerImpl.getManager().closeConversationOnSendingSide(var0.getConversationName(), true);
      } catch (SAFException var2) {
         throw new JAXRPCException(var2.toString(), var2);
      }
   }

   private static void handleSyncTerminateSequenceResponse(SoapMessageContext var0, WsrmSequenceContext var1, SAFConversationInfo var2) throws SAFTransportException {
      handleResponseSecurity(var0, var1, var2);
      SOAPMessage var3 = var0.getMessage();

      try {
         SOAPBody var4 = var3.getSOAPBody();
         if (var4 == null) {
            throw new SAFTransportException("Empty body for terminate sequence response message");
         } else {
            Element var5 = DOMUtils.getOptionalElementByTagNameNS(var4, var1.getRmVersion().getNamespaceUri(), WsrmConstants.Element.TERMINATE_SEQUENCE_RESPONSE.getElementName());
            if (var5 != null) {
               TerminateSequenceResponseMsg var6 = new TerminateSequenceResponseMsg(var1.getRmVersion());
               var6.read(var5);
               WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleTerminateSequenceResponse(var6);
            } else {
               handleTerminateSequenceResponseFault(var4);
            }

         }
      } catch (SOAPException var7) {
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new SAFTransportException("SOAPException", var7);
      } catch (DOMProcessingException var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new SAFTransportException("DOMProcessingException", var8);
      }
   }

   private static void handleTerminateSequenceResponseFault(SOAPBody var0) {
      SOAPFault var1 = var0.getFault();
      if (var1 == null) {
         throw new JAXRPCException("Protocol failure in terminate sequence");
      } else {
         throw new JAXRPCException("Terminate sequence failed, received a fault message: " + var1.getFaultString());
      }
   }

   private static String getLifecycleEndpointAddress(WsrmSequenceContext var0, SAFConversationInfo var1) {
      String var2;
      if (var0.getLifecycleEndpoint() != null) {
         var2 = var0.getLifecycleEndpoint().getAddress();
      } else {
         var2 = var1.getDestinationURL();
      }

      return var2;
   }

   private static void setupEndOfLifeAddressingHeaders(String var0, String var1, SoapMessageContext var2, SOAPMessage var3, WsrmSequenceContext var4) {
      AddressingProvider var5 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
      ToHeader var6 = var5.createToHeader(var1);
      ActionHeader var7 = var5.createActionHeader();
      var7.setActionURI(var0);
      var2.getHeaders().addHeader(var6);
      var2.getHeaders().addHeader(var7);
      var2.getHeaders().addHeader(var5.createMessageIdHeader(Guid.generateGuid()));
      var2.getHeaders().addHeader(var5.createFromHeader(var5.createAnonymousEndpointReference()));
      if (!AsyncUtil.isSoap12(var2)) {
         var3.getMimeHeaders().setHeader("SOAPAction", var0);
      }

      EndpointReference var8;
      if (var4.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var8 = var4.getMainAckTo();
         if (var8 == null) {
            var8 = var4.getAcksTo();
         }

         var2.getHeaders().addHeader(var5.createReplyToHeader(var8));
      }

      var8 = var4.getLifecycleEndpoint();
      if (var8 == null) {
         var8 = var4.getDestination();
      }

      if (var8 != null) {
         addReferenceHeaders(var8, var2);
      }

   }

   public static void acknowledge(SAFConversationInfo var0, List<Long> var1, EndpointReference var2, WsrmSequenceContext var3, boolean var4) throws SAFTransportException, SAFException {
      String var5 = getSequenceIdFromConversationInfo(var0);
      String var6 = var2.getAddress();
      if (AddressingHelper.isAnonymousReferenceURI(var6)) {
         if (verbose) {
            Verbose.log((Object)"Skip sending acknowledgement, encountered anonymous URI");
         }

      } else {
         SOAPMessage var7 = createSOAPMessage(var4);
         SoapMessageContext var8 = createSendSoapContext(var3, var7);
         setWSAVersion(var8, var2.getNamespaceURI());
         addReferenceHeaders(var2, var8);
         setupAckAddressingHeaders(var3, var5, var1, var8, var6, var7);
         if (verbose) {
            String var9 = null;

            long var11;
            long var13;
            for(Iterator var10 = var1.iterator(); var10.hasNext(); var9 = var9 + "< sequenceNumberLow=" + var11 + " sequenceNumberHigh=" + var13 + "> ") {
               var11 = (Long)var10.next();
               var13 = (Long)var10.next();
            }

            Verbose.log((Object)("Send acknowledge to " + var6 + " with sequence id = " + var5 + " sequence range: " + var9));
         }

         applySecurity(var8, var3);
         SoapMessageContext var17 = sendMessage(var8, var6, var0, var3);
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_AFTER_ACK);
         if (var17 != null) {
            SOAPMessage var18 = var17.getMessage();

            SOAPBody var19;
            try {
               var19 = var18.getSOAPBody();
            } catch (SOAPException var15) {
               if (verbose) {
                  Verbose.logException(var15);
               }

               throw new SAFTransportException("SOAPException", true, var15);
            }

            if (var19 == null) {
               throw new SAFTransportException("Empty reply body");
            }

            if (!var19.hasFault()) {
               throw new SAFTransportException("Got a message back that is not a fault message.");
            }

            try {
               SequenceFaultMsg var12 = SequenceFaultMsgFactory.getInstance().parseSoapFault(var18, var3.getRmVersion());
               if (verbose) {
                  Verbose.log((Object)("Found fault: " + var12.getSubCodeQualifiedName()));
               }

               WsrmSAFManagerFactory.getWsrmSAFReceivingManager().handleFault(var12);
            } catch (SequenceFaultException var16) {
               if (verbose) {
                  SOAPFault var20 = var19.getFault();
                  Verbose.log((Object)("Received a fault message: " + var20.getFaultString()));
               }
            }
         }

      }
   }

   private static void setupAckAddressingHeaders(WsrmSequenceContext var0, String var1, List var2, SoapMessageContext var3, String var4, SOAPMessage var5) {
      AcknowledgementHeader var6 = (AcknowledgementHeader)MsgHeaderFactory.getInstance().createMsgHeader(WsrmHeader.getQName(AcknowledgementHeader.class, var0.getRmVersion()));
      var6.setSequenceId(var1);
      if (var0.isClosed()) {
         var6.setFinal(true);
      }

      if (var2.isEmpty()) {
         var6.setNone(true);
      } else {
         Iterator var7 = var2.iterator();

         while(var7.hasNext()) {
            long var8 = (Long)var7.next();
            long var10 = (Long)var7.next();
            var6.acknowledgeMessages(var8, var10);
         }
      }

      AddressingProvider var12 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var3);
      var3.getHeaders().addHeader(var6);
      var3.getHeaders().addHeader(var12.createToHeader(var4));
      var3.getHeaders().addHeader(var12.createFromHeader(var12.createAnonymousEndpointReference()));
      var3.getHeaders().addHeader(var12.createFaultToHeader(var12.createAnonymousEndpointReference()));
      String var13 = WsrmConstants.Action.ACK.getActionURI(var0.getRmVersion());
      var3.getHeaders().addHeader(var12.createActionHeader(var13));
      if (!AsyncUtil.isSoap12(var3)) {
         var5.getMimeHeaders().setHeader("SOAPAction", var13);
      }

   }

   private static SoapMessageContext sendMessageWithSubject(final SOAPMessageContext var0, final String var1, final SAFConversationInfo var2, final WsrmSequenceContext var3) throws SAFTransportException {
      AuthenticatedSubject var4 = null;
      if (var3 != null) {
         var4 = var3.getSecuritySubject();
      }

      if (var4 == null) {
         return sendMessage(var0, var1, var2, var3);
      } else {
         try {
            return (SoapMessageContext)SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var4, new PrivilegedExceptionAction() {
               public Object run() throws SAFTransportException {
                  return WsrmSequenceSender.sendMessage(var0, var1, var2, var3);
               }
            });
         } catch (PrivilegedActionException var7) {
            Exception var6 = var7.getException();
            if (var6 instanceof SAFTransportException) {
               throw (SAFTransportException)var6;
            } else {
               throw new SAFTransportException(var6.getMessage());
            }
         }
      }
   }

   private static final void performWSSCHandshake(WsrmSequenceContext var0, String var1) throws SAFTransportException {
      WsrmSecurityContext var2 = var0.getWsrmSecurityContext();
      if (var2.isSecureWithWssc()) {
         boolean var3 = var0.isSoap12();
         SOAPMessage var4 = createSOAPMessage(var3);
         final SoapMessageContext var5 = createSendSoapContext(var0, var4);
         var5.setProperty("javax.xml.rpc.service.endpoint.address", var1);

         try {
            NormalizedExpression var6 = var2.getSecurityPolicy();
            var5.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var6);
            var5.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var6);
            X509Certificate var7 = var2.getServerEncryptCert();
            if (var7 != null) {
               var5.setProperty("weblogic.wsee.security.bst.serverEncryptCert", var7);
            }

            X509Certificate var8 = var2.getServerVerifyCert();
            if (var8 != null) {
               var5.setProperty("weblogic.wsee.security.bst.serverVerifyCert", var8);
            }

            SCTokenBase var9 = (SCTokenBase)SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var0.getSecuritySubject(), new PrivilegedExceptionAction() {
               public Object run() {
                  return SCTHelper.performWSSCHandshake(var5);
               }
            });
            var2.setSCCredential(var9.getCredential());
         } catch (PrivilegedActionException var10) {
            throw new HandshakeMsgException(var10.getMessage(), var10);
         } catch (PolicyException var11) {
            throw new HandshakeMsgException(var11.getMessage(), var11);
         }
      }

   }

   private static final void performWSSC13Handshake(WsrmSequenceContext var0, String var1) throws SAFTransportException {
      WsrmSecurityContext var2 = var0.getWsrmSecurityContext();
      if (var2.isSecureWithWssp12Wssc13()) {
         boolean var3 = var0.isSoap12();
         SOAPMessage var4 = createSOAPMessage(var3);
         final SoapMessageContext var5 = createSendSoapContext(var0, var4);
         var5.setProperty("javax.xml.rpc.service.endpoint.address", var1);

         try {
            NormalizedExpression var6 = var2.getSecurityPolicy();
            var5.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var6);
            var5.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var6);
            X509Certificate var7 = var2.getServerEncryptCert();
            if (var7 != null) {
               var5.setProperty("weblogic.wsee.security.bst.serverEncryptCert", var7);
            }

            X509Certificate var8 = var2.getServerVerifyCert();
            if (var8 != null) {
               var5.setProperty("weblogic.wsee.security.bst.serverVerifyCert", var8);
            }

            SCTokenBase var9 = (SCTokenBase)SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var0.getSecuritySubject(), new PrivilegedExceptionAction() {
               public Object run() {
                  return weblogic.wsee.security.wssc.v13.sct.SCTHelper.performWSSCHandshake(var5);
               }
            });
            var2.setSCCredential(var9.getCredential());
         } catch (PrivilegedActionException var10) {
            throw new HandshakeMsgException(var10.getMessage(), var10);
         } catch (PolicyException var11) {
            throw new HandshakeMsgException(var11.getMessage(), var11);
         }
      }

   }

   private static SoapMessageContext sendMessage(SOAPMessageContext var0, String var1, SAFConversationInfo var2, WsrmSequenceContext var3) throws SAFTransportException {
      Connection var4 = establishConnection(var1, ((SoapMessageContext)var0).isSoap12());

      assert var0.getMessage() != null;

      var0.setProperty("javax.xml.rpc.service.endpoint.address", var1);
      String var5 = getSequenceIdFromConversationInfo(var2);
      ActionHeader var6 = (ActionHeader)((SoapMessageContext)var0).getHeaders().getHeader(ActionHeader.TYPE);
      if (var6 != null && !AsyncUtil.isSoap12(var0)) {
         var0.getMessage().getMimeHeaders().setHeader("SOAPAction", var6.getActionURI());
      }

      HTTPClientTransport var7 = (HTTPClientTransport)var4.getTransport();
      String var8 = "Unknown";
      if (var0.containsProperty("weblogic.wsee.addressing.Action")) {
         var8 = (String)var0.getProperty("weblogic.wsee.addressing.Action");
      } else if (var6 != null) {
         var8 = var6.getActionURI();
      }

      SequenceHeader var9 = (SequenceHeader)((SoapMessageContext)var0).getHeaders().getHeader(SequenceHeader.TYPE);
      if (verbose) {
         if (var9 != null) {
            Verbose.log((Object)("** Sending sequence message with action " + var8 + " seq num " + var9.getMessageNumber() + " on sequence " + var2.getConversationName() + (var2.getDynamicConversationName() != null ? " receive side sequence " + var2.getDynamicConversationName() : "") + " to: " + var1));
         } else {
            Verbose.log((Object)("** Sending message with action " + var8 + " on sequence " + var2.getConversationName() + (var2.getDynamicConversationName() != null ? " receive side sequence " + var2.getDynamicConversationName() : "") + " to: " + var1));
         }
      }

      int var10 = 0;

      try {
         setTransportInfoIntoMessageContext(var3, var0);
         (new MimeHeaderHandler()).handleRequest(var0);
         WsrmSAFSendingManager.addTestSequenceSSLHeaderIfNeeded(var3, (WlMessageContext)var0);
         if (var3 != null && var3.isSecure()) {
            if (var0.getProperty("weblogic.wsee.async.res") == null && var3.isSequenceCreator()) {
               AsyncUtil.getWssClientHandler(var3).handleRequest(var0);
            } else {
               AsyncUtil.getWssServerHandler(var3).handleResponse(var0);
            }

            if (((SoapMessageContext)var0).getFault() != null) {
               Throwable var18 = ((SoapMessageContext)var0).getFault();
               if (verbose) {
                  Verbose.logException(var18);
               }

               throw new SAFTransportException("Send failed", var18);
            }

            saveMCForAsyncResponse((SoapMessageContext)var0);
         }

         WsrmSecurityContext var11 = null;
         if (var3 != null) {
            var11 = var3.getWsrmSecurityContext();
         }

         setTransportInfoIntoMessageContext(var3, var0);
         ConnectionHandler.verifySSLAdapterOnMessageContextIfNeeded(var0);
         var7.setConnectionTimeout(30000);
         var4.send(var0);
         var7.confirmOneway(true);
         if (var11 != null && var11.isSecureWithSSL() && var11.getSSLSessionId() == null) {
            byte[] var17 = getSSLSessionId(var4, true);
            if (verbose) {
               Verbose.say("");
               Verbose.say("%%%%%%%%%%%%%%%%%%%%% Setting SSL/WLS Session ID on sending-side sequence %%%%%%%%%%");
               WsrmSAFManager.dumpByteArray(var17);
               Verbose.say("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
               Verbose.say("");
            }

            var3.getWsrmSecurityContext().setSSLSessionId(var17);
         }

         var10 = var7.getResponseCode();
      } catch (IOException var15) {
         if (verbose) {
            if (var9 != null) {
               Verbose.say("Unable to send sequence message on sequence " + var9.getSequenceId() + " and message number " + var9.getMessageNumber() + " to destination " + var1 + ": " + var15.toString());
            } else {
               Verbose.say("Unable to send message on sequence " + var5 + " to dest " + var1 + ": " + var15.toString());
            }

            Verbose.logException(var15);
         }

         if (var15 instanceof SSLHandshakeException) {
            WsrmPermanentTransportException var12 = new WsrmPermanentTransportException(var15.toString(), var15);
            throw new SAFTransportException(var12.toString(), var12);
         }

         try {
            var10 = var7.getResponseCode();
         } catch (Exception var13) {
            if (verbose) {
               Verbose.logException(var13);
            }
         }

         if (var10 == 500) {
            throwSOAPFaultExceptionFromConnectionInfo(var10, var4, var0);
         }

         throw new SAFTransportException("Send failed", var15);
      }

      if (var10 != 200) {
         return null;
      } else {
         try {
            var4.receive(var0);
            SOAPMessage var16 = var0.getMessage();

            assert var16 != null;
         } catch (IOException var14) {
            if (verbose) {
               Verbose.logException(var14);
            }

            throw new SAFTransportException("Receive failed", true, var14);
         }

         if (verbose) {
            Verbose.getOut().println("\n\n");
            Verbose.log((Object)"Got sync message response in WsrmSequenceSender.sendMessage");
         }

         return (SoapMessageContext)var0;
      }
   }

   private static void setTransportInfoIntoMessageContext(WsrmSequenceContext var0, SOAPMessageContext var1) {
      if (var0 != null && var1.getProperty("weblogic.wsee.connection.transportinfo") == null) {
         TransportInfo var2 = var0.getTransportInfo();
         if (var2 != null) {
            var1.setProperty("weblogic.wsee.connection.transportinfo", var2);
            if (var1.getProperty("weblogic.wsee.client.ssladapter") == null && var2 instanceof HttpsTransportInfo) {
               SSLAdapter var3 = ((HttpsTransportInfo)var2).getSSLAdapter();
               if (var3 != null) {
                  var1.setProperty("weblogic.wsee.client.ssladapter", var3);
               }
            }
         }
      }

   }

   private static byte[] getSSLSessionId(Connection var0, boolean var1) {
      if (var0 == null) {
         throw new JAXRPCException("Target service has SSL/TLS security enabled, but client connection is null");
      } else {
         Transport var2 = var0.getTransport();
         if (!(var2 instanceof HTTPClientTransport)) {
            throw new JAXRPCException("Target service has SSL/TLS security enabled, but client connection doesn't support SSL: " + var2);
         } else {
            HTTPClientTransport var3 = (HTTPClientTransport)var2;
            byte[] var4 = var3.getSSLSessionId();
            if (var4 == null && var1) {
               throw new JAXRPCException("Target service has SSL/TLS security enabled, but client connection didn't have the SSLSession set");
            } else {
               return var4;
            }
         }
      }
   }

   private static void throwSOAPFaultExceptionFromConnectionInfo(int var0, Connection var1, SOAPMessageContext var2) throws SAFTransportException {
      if (verbose) {
         Verbose.say("Got HTTP " + var0 + " error, trying to read detailed WS-RM error information");
      }

      try {
         var1.receive(var2);
         SOAPMessage var3 = var2.getMessage();
         if (verbose) {
            Verbose.getOut().println("\n\n");
            Verbose.log((Object)("Got HTTP " + var0 + " message fault response in WsrmSequenceSender.sendMessage"));
         }

         try {
            SequenceFaultMsgFactory.getInstance().parseSoapFault(var3);
            SOAPFault var4 = var3.getSOAPBody().getFault();
            SOAPFaultException var5 = new SOAPFaultException(var4.getFaultCodeAsQName(), var4.getFaultString(), var4.getFaultActor(), var4.getDetail());
            throw new WsrmPermanentTransportException(var5.toString(), var5);
         } catch (SequenceFaultException var6) {
         }
      } catch (WsrmPermanentTransportException var7) {
         throw new SAFTransportException(var7.toString(), var7);
      } catch (Exception var8) {
         if (verbose) {
            Verbose.logException(var8);
            Verbose.say("Ignoring prior exception generated during an attempt to get specific error from HTTP, and throwing generic HTTP exception instead");
         }

      }
   }

   public static void sendFault(String var0, WsrmSequenceContext var1, EndpointReference var2, Exception var3, boolean var4) throws SAFTransportException {
      MessageFactory var5 = WLMessageFactory.getInstance().getMessageFactory(var4);
      SOAPMessage var6 = SOAPFaultUtil.exception2Fault(var5, var3);
      sendFault(var0, var1, var2, var6, var4);
   }

   public static void sendFault(String var0, WsrmSequenceContext var1, EndpointReference var2, SequenceFaultMsg var3, boolean var4) throws SAFTransportException {
      SOAPMessage var5 = createSOAPMessage(var4);
      var3.write(var5);
      sendFault(var0, var1, var2, var5, var4);
   }

   public static void sendFault(String var0, WsrmSequenceContext var1, EndpointReference var2, SOAPMessage var3, boolean var4) throws SAFTransportException {
      if (AddressingHelper.isAnonymousReferenceURI(var2.getAddress())) {
         if (verbose) {
            Verbose.log((Object)"Skip sending fault, encountered anonymous URI");
         }

      } else {
         SoapMessageContext var5 = new SoapMessageContext(var4);
         setWSAVersion(var5, var1.getWsaVersion());
         var5.setMessage(var3);
         addReferenceHeaders(var2, var5);
         String var6 = var2.getAddress();
         setupFaultAddressingHeaders(var5, var6, var3);
         if (var5.getProperty("weblogic.wsee.connection.transportinfo") == null) {
            TransportInfo var7 = var1.getTransportInfo();
            if (var7 != null) {
               var5.setProperty("weblogic.wsee.connection.transportinfo", var7);
               if (var5.getProperty("weblogic.wsee.client.ssladapter") == null && var7 instanceof HttpsTransportInfo) {
                  SSLAdapter var8 = ((HttpsTransportInfo)var7).getSSLAdapter();
                  if (var8 != null) {
                     var5.setProperty("weblogic.wsee.client.ssladapter", var8);
                  }
               }
            }
         }

         (new MimeHeaderHandler()).handleRequest(var5);
         applySecurity(var5, var1);
         if (var1.getWsrmSecurityContext().isSecureWithWssc() && !AsyncUtil.getWssClientHandler(var1).handleRequest(var5)) {
            throw new JAXRPCException(var5.getFault());
         } else {
            if (verbose) {
               Verbose.log((Object)("Send fault to " + var2.getAddress() + " with sequence id = " + var0));
            }

            Connection var10 = establishConnection(var6, var4);

            try {
               var10.send(var5);
               var10.getTransport().confirmOneway();
            } catch (IOException var9) {
               if (verbose) {
                  Verbose.logException(var9);
               }

               throw new SAFTransportException("Send failed", var9);
            }
         }
      }
   }

   private static void setupFaultAddressingHeaders(SoapMessageContext var0, String var1, SOAPMessage var2) {
      AddressingProvider var3 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var0);
      var0.getHeaders().addHeader(var3.createToHeader(var1));
      var0.getHeaders().addHeader(var3.createActionHeader(WSAddressingConstants.FAULT_ACTION_URI));
      var0.getHeaders().addHeader(var3.createFromHeader(var3.createAnonymousEndpointReference()));
      var0.getHeaders().addHeader(var3.createFaultToHeader(var3.createAnonymousEndpointReference()));
      if (!AsyncUtil.isSoap12(var0)) {
         var2.getMimeHeaders().setHeader("SOAPAction", WSAddressingConstants.FAULT_ACTION_URI);
      }

      var0.setProperty("javax.xml.rpc.service.endpoint.address", var1);
   }

   private static void addReferenceHeaders(EndpointReference var0, SoapMessageContext var1) {
      Iterator var2 = var0.getReferenceProperties().listHeaders();

      while(var2.hasNext()) {
         var1.getHeaders().addHeader((MsgHeader)var2.next());
      }

      var2 = var0.getReferenceParameters().listHeaders();

      while(var2.hasNext()) {
         var1.getHeaders().addHeader((MsgHeader)var2.next());
      }

   }

   private static Connection establishConnection(String var0, boolean var1) throws SAFTransportException {
      int var3 = var0.indexOf(58);
      String var2;
      if (var3 < 0) {
         var2 = "http";
      } else {
         var2 = var0.substring(0, var3);
      }

      try {
         return ConnectionFactory.instance().createClientConnection(var2, var1 ? "SOAP12" : "SOAP11");
      } catch (ConnectionException var5) {
         if (verbose) {
            Verbose.logException(var5);
         }

         throw new SAFTransportException("ConnectionException", var5);
      }
   }

   private static SOAPMessage createSOAPMessage(boolean var0) throws SAFTransportException {
      MessageFactory var1 = WLMessageFactory.getInstance().getMessageFactory(var0);

      try {
         return var1.createMessage();
      } catch (SOAPException var3) {
         if (verbose) {
            Verbose.logException(var3);
         }

         throw new SAFTransportException("Create SOAP Message failed", var3);
      }
   }

   private static void setWSAVersion(SoapMessageContext var0, String var1) {
      var0.setProperty("weblogic.wsee.addressing.version", AddressingHelper.getWSAVersion(var1));
   }

   private static void setWSAVersion(SoapMessageContext var0, WSAVersion var1) {
      var0.setProperty("weblogic.wsee.addressing.version", var1);
   }

   private static void removeFromHeader(SOAPHeader var0) throws DOMProcessingException {
      Element var1 = DOMUtils.getOptionalElementByTagNameNS(var0, "http://schemas.xmlsoap.org/ws/2004/08/addressing", "From");
      if (var1 != null) {
         var1.getParentNode().removeChild(var1);
      }
   }
}
