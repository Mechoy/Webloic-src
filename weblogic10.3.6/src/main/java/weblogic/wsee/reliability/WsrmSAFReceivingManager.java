package weblogic.wsee.reliability;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.jws.ReliabilityBuffer;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.protocol.LocalServerIdentity;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.RelatesToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.faults.InvalidAckFaultMsg;
import weblogic.wsee.reliability.faults.LastMessageNumExceededFaultMsg;
import weblogic.wsee.reliability.faults.MessageNumRolloverFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.reliability.faults.SequenceFaultMsgType;
import weblogic.wsee.reliability.faults.SequenceTerminatedFaultMsg;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.HandshakeMsgException;
import weblogic.wsee.reliability.handshake.SequenceAccept;
import weblogic.wsee.reliability.handshake.SequenceOffer;
import weblogic.wsee.reliability.handshake.TerminateSequenceMsg;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSSLHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSTRHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WsrmSAFReceivingManager extends WsrmSAFManager {
   private static final boolean verbose = Verbose.isVerbose(WsrmSAFReceivingManager.class);

   WsrmSAFReceivingManager() {
      safManager.addConversationLifecycleListener(new SAFManager.ConversationLifecycleListener() {
         public void ack(SAFConversationInfo var1, long var2, long var4) {
         }

         public void addToCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
         }

         public void preClose(boolean var1, boolean var2, SAFConversationInfo var3) {
            if (!var1 && var2) {
               SAFConversationInfo var4 = var3.getConversationOffer();
               if (var4 != null) {
                  WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
                  if (!var5.hasRequestSeqNumBeenMappedToResponseSeqNum(1L)) {
                     try {
                        if (WsrmSAFReceivingManager.verbose) {
                           Verbose.say("*** Auto-terminating unused offer sequence " + var4.getConversationName() + " for request sequence " + var3.getConversationName());
                        }

                        WsrmSAFManager.safManager.setSentLastMessageOnSendingSide(var4.getConversationName(), 0L);
                     } catch (Exception var9) {
                        if (WsrmSAFReceivingManager.verbose) {
                           Verbose.logException(var9);
                        }
                     }
                  } else {
                     try {
                        if (!var5.hasFinalRequestSeqNum() && WsrmSAFManager.safManager.hasReceivedLastMessageOnReceivingSide(var3.getConversationName())) {
                           long var6 = WsrmSAFManager.safManager.getLastMessageSequenceNumberOnReceivingSide(var3.getConversationName());
                           if (WsrmSAFReceivingManager.verbose) {
                              Verbose.say("*** Starting auto-terminate processing for offer sequence " + var4.getConversationName() + " since request sequence " + var3.getConversationName() + " apparently never received TerminateSequence. Final request seq num is " + var6);
                           }

                           var5.setFinalRequestSeqNum(var6);
                           WsrmSAFManager.safManager.storeConversationContextOnSendingSide(var4.getConversationName(), var5);
                           WsrmSAFReceivingManager.this.checkForAutoTerminateOnOfferSequence(var4.getConversationName(), var5, var5.getDestination());
                        }
                     } catch (Exception var8) {
                        if (WsrmSAFReceivingManager.verbose) {
                           Verbose.logException(var8);
                        } else {
                           var8.printStackTrace();
                        }
                     }
                  }
               }
            }

         }

         public void removeFromCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
         }
      });
   }

   private SAFConversationInfo createSAFConversationInfo(String var1, WlMessageContext var2, CreateSequenceMsg var3, EndpointReference var4) {
      SAFConversationInfoImpl var5 = new SAFConversationInfoImpl(2);
      EndpointReference var6 = var3.getAcksTo();
      if (var6 == null) {
         throw new JAXRPCException("No AcksTo header in create sequence message");
      } else {
         var5.setSourceURL(var6.getAddress());
         String var7 = (String)var2.getProperty("weblogic.wsee.addressing.To");
         if (var7 == null) {
            throw new JAXRPCException("No To header in create sequence message");
         } else {
            var5.setDestinationURL(var7);
            this.setupSAFConversationQOS(var2, var5);
            var5.setDestinationType(2);
            var5.setConversationName(var1);
            var5.setTransportType(2);
            WsrmSequenceContext var8 = this.setupSAFConversationContext(var2, var6, var5, var3.getRmVersion());
            String var9 = (String)var2.getProperty("weblogic.wsee.addressing.MessageId");
            if (var9 == null) {
               throw new JAXRPCException("No message ID found");
            } else {
               var5.setCreateConversationMessageID(var9);
               this.handleExpiration(var3, var5, var2);
               this.setupSAFConversationMaxIdelTime(var2, var5);
               this.setupSAFConversationOffer(var2, var3, var5, var4, var8);
               return var5;
            }
         }
      }
   }

   private void setupSAFConversationOffer(WlMessageContext var1, CreateSequenceMsg var2, SAFConversationInfo var3, EndpointReference var4, WsrmSequenceContext var5) {
      boolean var6 = this.checkOfferNeeded(var1, var5.getRmVersion());
      SequenceOffer var7 = var2.getOffer();
      if (var7 != null) {
         if (!var6 && var5.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
            throw new JAXRPCException("Offered sequence refused. There are no output messages defined on this endpoint, so no offer sequence is required or allowed.");
         }

         SAFConversationInfoImpl var8 = new SAFConversationInfoImpl(2);
         String var9 = var7.getSequenceId();
         var8.setDestinationURL(var4.getAddress());
         var8.setQOS(var3.getQOS());
         var8.setInorder(var3.isInorder());
         var8.setDynamic(false);
         var8.setDestinationType(2);
         var8.setConversationName(var9);
         var8.setTransportType(2);
         WsrmSequenceContext var10 = new WsrmSequenceContext();
         var10.setDestination(var4);
         var10.setRmVersion(var5.getRmVersion());
         var10.setWsaVersion(var5.getWsaVersion());
         var10.setSoap12(var5.isSoap12());
         var10.setFrom(var5.getFrom());
         var10.setAcksTo(var5.getAcksTo());
         var10.setWsrmSecurityContext(new WsrmSecurityContext(var5.getWsrmSecurityContext()));
         var8.setContext(var10);
         var8.setSourceURL(var5.getAcksTo().getAddress());
         this.handleOfferExpiration(var7, var8);
         if (var5.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            if (var7.getEndpoint() != null) {
               boolean var11 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).isAnonymousReferenceURI(var7.getEndpoint().getAddress());
               if (var11) {
                  throw new JAXRPCException("CreateSequence/Offer/Endpoint cannot be anonymous");
               }

               var10.setLifecycleEndpoint(var7.getEndpoint());
            }

            if (var5.getWsrmSecurityContext().isForcedSSLSessionId()) {
               try {
                  String var13 = new String(var5.getWsrmSecurityContext().getSSLSessionId(), "UTF-8");
                  var13 = var13 + "Offer";
                  var10.getWsrmSecurityContext().setSSLSessionId(var13.getBytes("UTF-8"));
                  var10.getWsrmSecurityContext().setForcedSSLSessionId(true);
               } catch (Exception var12) {
                  throw new JAXRPCException(var12.toString(), var12);
               }
            }
         }

         var3.setConversationOffer(var8);
      } else if (var6) {
         throw new JAXRPCException("Sequence offer required for endpoints with output messages");
      }

   }

   private WsrmSequenceContext setupSAFConversationContext(WlMessageContext var1, EndpointReference var2, SAFConversationInfo var3, WsrmConstants.RMVersion var4) {
      WsrmSequenceContext var5 = new WsrmSequenceContext();
      var5.setRmVersion(var4);
      var5.setWsaVersion(AddressingHelper.getWSAVersion((MessageContext)var1));
      var5.setSoap12(AsyncUtil.isSoap12(var1));
      var5.setWsrmSecurityContext((WsrmSecurityContext)var1.getProperty("weblogic.wsee.wsrm.security.context"));
      if (var1.containsProperty("weblogic.wsee.addressing.From")) {
         var5.setFrom((EndpointReference)var1.getProperty("weblogic.wsee.addressing.From"));
      }

      var5.setAcksTo(var2);
      var3.setContext(var5);
      return var5;
   }

   private void handleOfferExpiration(SequenceOffer var1, SAFConversationInfo var2) {
      String var3 = var1.getExpires();
      if (var3 != null) {
         Duration var4;
         try {
            var4 = DatatypeFactory.newInstance().newDuration(var3);
         } catch (DatatypeConfigurationException var6) {
            throw new JAXRPCException("Cannot construct DatatypeFactory " + var6.toString());
         } catch (Throwable var7) {
            throw new JAXRPCException("Cannot read expiration for the offered sequence, not a valid duration type: " + var3 + " " + var7.toString());
         }

         this.setupSAFConversationTTL(var4, var2);
      } else {
         this.setupSAFConversationTTL((Duration)null, var2);
      }

   }

   private void handleExpiration(CreateSequenceMsg var1, SAFConversationInfo var2, WlMessageContext var3) {
      Duration var4 = var1.getExpires();
      Date var5 = new Date();
      this.setupSAFConversationTTL(var4, var2);
      Duration var6 = this.getSequenceExpirationFromContext(var3);
      if (var6 != null) {
         long var7 = var6.getTimeInMillis(var5);
         if (var7 <= 0L) {
            throw new JAXRPCException("Invalid expiration value in policy: " + var6.toString());
         }

         if (verbose) {
            Verbose.log((Object)("=======Conversation timeout is: " + var7 + "========"));
         }

         var2.setConversationTimeout(var7);
      }

   }

   public void deliver(SequenceHeader var1, SOAPMessageContext var2) {
      WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_BEFORE_SAF);
      SOAPMessage var3 = var2.getMessage();
      Dispatcher var4 = ((WlMessageContext)var2).getDispatcher();
      this.checkAsyncReqRes(var4, var2);
      String var5 = var1.getSequenceId();
      if (verbose) {
         ActionHeader var6 = (ActionHeader)((SoapMessageContext)var2).getHeaders().getHeader(ActionHeader.TYPE);
         String var7 = "Unknown";
         if (var6 != null) {
            var7 = var6.getActionURI();
         }

         String var8;
         if (var1.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
            var8 = var1.isLastMessage() ? "(LAST)" : "";
         } else {
            var8 = "";
         }

         Verbose.log((Object)("** Deliver reliable message with action " + var7 + " seq num " + var1.getMessageNumber() + var8 + " on sequence " + var5 + " to end destination"));
      }

      try {
         SAFConversationInfo var19 = this.getConversationInfo(var5, var2);
         if (var19 == null) {
            return;
         }

         WsrmSequenceContext var20 = (WsrmSequenceContext)var19.getContext();

         assert var20 != null;

         if (this.checkForSequenceClosed(var2, var5, var20)) {
            return;
         }

         EndpointReference var21 = var20.getAcksTo();

         assert var21 != null;

         boolean var9 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2).isAnonymousReferenceURI(var21.getAddress());
         this.checkAndRemoveMustUnderstand(var1, true, var3);
         String var10 = (String)var2.getProperty("weblogic.wsee.addressing.MessageId");
         this.checkAsyncReqResOffer(var4, var2, var19);
         var2.setProperty("weblogic.wsee.enable.rm", "true");
         long var11 = var1.getMessageNumber();
         var2.setProperty("weblogic.wsee.reliability.RequestMessageSeqNumber", Long.toString(var11));
         var2.setProperty("weblogic.wsee.reliability.RequestMessageSeqID", var1.getSequenceId());
         String var13 = "Unknown";
         ActionHeader var14 = (ActionHeader)((SoapMessageContext)var2).getHeaders().getHeader(ActionHeader.TYPE);
         if (var14 != null) {
            var13 = var14.getActionURI();
         }

         var2.setProperty("weblogic.wsee.reliability.RequestMessageAction", var13);
         SAFConversationInfo var15 = var19.getConversationOffer();
         if (var15 != null) {
            if (verbose) {
               Verbose.say("*** WsrmSAFReceivingManager.deliver() storing request offer seq ID as " + var15.getConversationName() + " for request sequence " + var5 + " requestSeqNum " + var11 + " action " + var13);
            }

            var2.setProperty("weblogic.wsee.reliability.RequestMessageOfferSeqID", var15.getConversationName());
         } else if (verbose) {
            Verbose.say("*** WsrmSAFReceivingManager.deliver() didn't find an offer sequence related to request sequence " + var5 + " requestSeqNum " + var11 + " action " + var13);
         }

         SAFRequest var16 = this.createSAFRequest(var1, var10, var2, var20);
         this.setupSAFRequestPayloadContext(var2, var16);
         if (!var9) {
            if (verbose) {
               Verbose.log((Object)("*** WsrmSAFReceivingManager.deliver() - requesting asynchronous delivery of SAFRequest on sequence: " + var5 + " requestSeqNum " + var11 + " action " + var13));
            }

            safManager.deliver(var19, var16);
            if (verbose) {
               Verbose.log((Object)("*** WsrmSAFReceivingManager.deliver() - done requesting asynchronous delivery of SAFRequest on sequence: " + var5 + " requestSeqNum " + var11 + " action " + var13));
            }

            AddressingUtil.confirmOneway((WlMessageContext)var2);
            var2.setProperty("weblogic.wsee.reliable.oneway.msg", "true");
         } else {
            if (verbose) {
               Verbose.log((Object)("*** WsrmSAFReceivingManager.deliver() - requesting synchronous delivery of SAFRequest on sequence: " + var5 + " requestSeqNum " + var11 + " action " + var13));
            }

            SAFResult var17 = safManager.deliverSync(var19, var16);
            if (verbose) {
               Verbose.log((Object)("*** WsrmSAFReceivingManager.deliver() - done with synchronous delivery of SAFRequest on sequence: " + var5 + " requestSeqNum " + var11 + " action " + var13));
            }

            if (var17.isDuplicate()) {
               this.handleDuplicateSAFResult(var5, var1);
            }

            if (var17.isSuccessful()) {
               this.handleSuccessfulSAFResult(var5, var2, var21, var20);
            } else {
               this.handleFailedSAFResult(var5, var17, var2, var21);
            }
         }
      } catch (SAFException var18) {
         if (var18.getResultCode() == 19) {
            if (verbose) {
               Verbose.log((Object)var18.getMessage());
            }

            this.sendLastMessageNumExceededFault(var5, var2);
            return;
         }

         if (verbose) {
            Verbose.logException(var18);
         }

         throw new JAXRPCException("Delivery to SAF failed: " + var18.toString(), var18);
      }

      if (((WlMessageContext)var2).getHeaders().getHeader(RelatesToHeader.TYPE) != null) {
         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RES_TO_SAF_BEFORE_QUEUE);
      }

   }

   private void sendLastMessageNumExceededFault(String var1, SOAPMessageContext var2) {
      SAFConversationInfo var3 = this.getConversationInfo(var1, var2);
      if (var3 != null) {
         WsrmSequenceContext var4 = (WsrmSequenceContext)var3.getContext();
         LastMessageNumExceededFaultMsg var5 = new LastMessageNumExceededFaultMsg(var4.getRmVersion());
         var5.setSequenceId(var1);
         WsrmHelper.sendFault(var2, var5, ((WsrmSequenceContext)var3.getContext()).getAcksTo());
      }
   }

   private void handleFailedSAFResult(String var1, SAFResult var2, SOAPMessageContext var3, EndpointReference var4) {
      if (verbose) {
         Verbose.log((Object)("Deliver message with sequence ID " + var1 + " failed.  Result code is " + var2.getResultCode() + ", description is " + var2.getDescription()));
      }

      SequenceFaultMsg var5 = this.getDeliverFaultMsg(var2, var1, var3, var4);
      if (var5 == null) {
         throw new JAXRPCException("Deliver message with sequence ID " + var1 + " failed.  Result code is " + var2.getResultCode() + ", description is " + var2.getDescription());
      } else {
         WsrmHelper.sendFault(var3, var5, var4);
      }
   }

   private void handleSuccessfulSAFResult(String var1, SOAPMessageContext var2, EndpointReference var3, WsrmSequenceContext var4) {
      AcknowledgementHeader var5 = this.createAckHeader(var1, var4);
      this.sendAck(var2, var5, var3, var4);
   }

   private void handleDuplicateSAFResult(String var1, SequenceHeader var2) {
      if (verbose) {
         Verbose.log((Object)("Got duplicate message with sequence ID: " + var1 + " and message number: " + var2.getMessageNumber()));
      }

   }

   private void setupSAFRequestPayloadContext(SOAPMessageContext var1, SAFRequest var2) {
      WlMessageContext var3 = (WlMessageContext)var1;
      ReliabilityBufferConfig var4 = this.getReliabilityBufferConfig(var3);
      if (var4 != null) {
         int var5 = var4.getRetryCount();
         String var6 = var4.getRetryDelay();
         weblogic.wsee.jws.container.Duration var7 = new weblogic.wsee.jws.container.Duration(var6);
         long var8 = var7.convertToSeconds(new Date());
         if (verbose) {
            Verbose.log((Object)("Buffer retry count " + var5));
            Verbose.log((Object)("Buffer retry delay " + var8));
         }

         ((WsrmPayloadContext)var2.getPayloadContext()).setRetryCount(var5);
         ((WsrmPayloadContext)var2.getPayloadContext()).setRetryDelay(var8);
      }

   }

   private ReliabilityBufferConfig getReliabilityBufferConfig(WlMessageContext var1) {
      ReliabilityBufferConfig var2 = null;
      if (var1.getDispatcher().getWsMethod() != null) {
         Class var3 = var1.getDispatcher().getWsPort().getEndpoint().getJwsClass();
         if (var3 != null) {
            Method var4 = this.findMethod(var3, var1.getDispatcher().getWsMethod().getMethodName());
            if (var4 != null) {
               ReliabilityBuffer var5 = (ReliabilityBuffer)var4.getAnnotation(ReliabilityBuffer.class);
               if (var5 == null) {
                  var5 = (ReliabilityBuffer)var3.getAnnotation(ReliabilityBuffer.class);
               }

               if (var5 != null) {
                  var2 = new ReliabilityBufferConfig(var5);
               }
            }
         }
      }

      if (var2 == null && (var1.getProperty("weblogic.wsee.wsrm.RetryCount") != null || var1.getProperty("weblogic.wsee.wsrm.RetryDelay") != null)) {
         var2 = new ReliabilityBufferConfig();
         String var8;
         if (var1.getProperty("weblogic.wsee.wsrm.RetryCount") != null) {
            try {
               var8 = (String)var1.getProperty("weblogic.wsee.wsrm.RetryCount");
               var2.setRetryCount(Integer.parseInt(var8));
            } catch (Exception var7) {
               if (verbose) {
                  Verbose.logException(var7);
               }
            }
         }

         if (var1.getProperty("weblogic.wsee.wsrm.RetryDelay") != null) {
            try {
               var8 = (String)var1.getProperty("weblogic.wsee.wsrm.RetryDelay");
               var2.setRetryDelay(var8);
            } catch (Exception var6) {
               if (verbose) {
                  Verbose.logException(var6);
               }
            }
         }
      }

      return var2;
   }

   private Method findMethod(Class var1, String var2) {
      Method[] var3 = var1.getMethods();
      Method[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (var7.getName().equals(var2)) {
            return var7;
         }
      }

      return null;
   }

   private SAFConversationInfo getConversationInfo(String var1, SOAPMessageContext var2) {
      SAFConversationInfo var3;
      try {
         var3 = safManager.getConversationInfoOnReceivingSide(var1);
      } catch (Throwable var5) {
         if (verbose) {
            Verbose.logException(var5);
         }

         throw new JAXRPCException("Unable to retrieve reliable sequence information on the receiving side", var5);
      }

      if (var3 == null) {
         this.sendUnknownSequenceFault(var1, var2);
      }

      return var3;
   }

   private SequenceFaultMsg getDeliverFaultMsg(SAFResult var1, String var2, SOAPMessageContext var3, EndpointReference var4) {
      SequenceFaultMsgFactory var5 = SequenceFaultMsgFactory.getInstance();
      int var6 = var1.getResultCode();
      QName var7 = var5.getSAFResultCodeMapping(var6);
      if (var7 == null) {
         if (verbose) {
            Verbose.log((Object)("Unknown fault code " + var6 + " -- " + var1.getDescription()));
         }

         return null;
      } else {
         WsrmSequenceContext var8 = (WsrmSequenceContext)var1.getConversationInfo().getContext();
         SequenceFaultMsg var9 = var5.createSequenceFaultMsg(var7, var8.getRmVersion());
         if (var9 != null) {
            var9.setSequenceId(var2);
         }

         if (!(var9 instanceof UnknownSequenceFaultMsg)) {
            var3.setProperty("weblogic.wsee.faultto.override", var4);
         }

         return var9;
      }
   }

   private void checkAsyncReqRes(Dispatcher var1, SOAPMessageContext var2) {
      if (var1.getOperation() != null && (var1.getOperation().getType() == 0 || var1.getOperation().getType() == 2) && var2.getProperty("weblogic.wsee.reply.anonymous") != null) {
         throw new JAXRPCException("Reliable messaging can only work with oneway or asynchronous request/response MEP.  The reply-to endpoint should not be anonymous in request/response.");
      }
   }

   private void checkAsyncReqResOffer(Dispatcher var1, SOAPMessageContext var2, SAFConversationInfo var3) {
      if (var1.getOperation() != null) {
         String var4 = ((EndpointReference)var2.getProperty("weblogic.wsee.addressing.ReplyTo")).getAddress();
         SAFConversationInfo var5;
         if (var1.getOperation().getType() != 0 && var1.getOperation().getType() != 2) {
            var5 = var3.getConversationOffer();
            if (var5 != null && var4.equals(var5.getDestinationURL())) {
               var2.setProperty("weblogic.wsee.convid.sequence.id", var5.getConversationName());
            }
         } else {
            if (var2.getProperty("weblogic.wsee.reply.anonymous") != null) {
               throw new JAXRPCException("Reliable messaging can only work with oneway or asynchronous request/response MEP.  The reply-to endpoint should not be anonymous in request/response.");
            }

            var5 = var3.getConversationOffer();
            if (var5 == null) {
               WsrmSequenceContext var6 = (WsrmSequenceContext)var3.getContext();
               WsrmConstants.RMVersion var7 = var6.getRmVersion();
               if (var7 == WsrmConstants.RMVersion.RM_10) {
                  throw new JAXRPCException("offer is required for reliable request/response MEP");
               }
            } else if (var4.equals(var5.getDestinationURL())) {
               var2.setProperty("weblogic.wsee.async.res.sequence.id", var5.getConversationName());
            }
         }
      }

   }

   public void ackRequested(String var1, SOAPMessageContext var2) {
      if (verbose) {
         Verbose.log((Object)"received ack request");
      }

      try {
         SAFConversationInfo var3 = this.getConversationInfo(var1, var2);
         if (var3 != null) {
            WsrmSequenceContext var4 = (WsrmSequenceContext)var3.getContext();

            assert var4 != null;

            EndpointReference var5 = var4.getAcksTo();

            assert var5 != null;

            AcknowledgementHeader var6 = this.createAckHeader(var1, var4);
            boolean var7 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2).isAnonymousReferenceURI(var5.getAddress());
            WsrmServerPayloadContext var8 = new WsrmServerPayloadContext();
            var8.setRequestPolicy(PolicyContext.getRequestEffectivePolicy(var2));
            var8.setResponsePolicy(PolicyContext.getResponseEffectivePolicy(var2));
            if (var7) {
               this.sendAck(var2, var6, var5, var4);
            } else {
               List var9 = safManager.getAllSequenceNumberRangesOnReceivingSide(var1);
               WsrmSequenceSender.acknowledge(var3, var9, var5, var4, var4.isSoap12());
               AddressingUtil.confirmOneway((WlMessageContext)var2);
               var2.setProperty("weblogic.wsee.reliable.oneway.msg", "true");
            }

         }
      } catch (JAXRPCException var10) {
         throw var10;
      } catch (SAFException var11) {
         if (verbose) {
            Verbose.logException(var11);
         }

         throw new JAXRPCException("SAFException", var11);
      } catch (Throwable var12) {
         if (verbose) {
            Verbose.logException(var12);
         }

         throw new JAXRPCException("Throwable", var12);
      }
   }

   public void createSequence(SOAPMessageContext var1, CreateSequenceMsg var2) throws SAFException {
      SoapMessageContext var3 = (SoapMessageContext)var1;
      WsrmSecurityContext var4 = (WsrmSecurityContext)var3.getProperty("weblogic.wsee.wsrm.security.context");
      if (var4 == null) {
         throw new JAXRPCException("No WsrmSecurityContext found on message context");
      } else {
         if (var4.isSecureWithSSL()) {
            UsesSequenceSSLHeader var5 = (UsesSequenceSSLHeader)var3.getHeaders().getHeader(UsesSequenceSSLHeader.TYPE);
            if (var5 != null) {
               this.checkAndRemoveMustUnderstand(var5, false, var3.getMessage());
            }

            setSSLSessionIdFromContext(var3, var4, false);
         }

         if (var4.isSecure()) {
            UsesSequenceSTRHeader var13 = (UsesSequenceSTRHeader)var3.getHeaders().getHeader(UsesSequenceSTRHeader.TYPE);
            if (var13 != null) {
               this.checkAndRemoveMustUnderstand(var13, false, var3.getMessage());
            }
         }

         EndpointReference var14 = (EndpointReference)var3.getProperty("weblogic.wsee.addressing.ReplyTo");
         String var6 = var14.getAddress();
         String var7 = (String)var3.getProperty("weblogic.wsee.addressing.MessageId");

         assert var7 != null;

         if (!this.checkDuplicateCreateSequence(var7, var3, var2, var6)) {
            String var8 = this.generateSequenceId();
            if (verbose) {
               SequenceOffer var9 = var2.getOffer();
               Verbose.log((Object)("Create sequence with ID " + var8 + (var9 != null ? " offer seq id " + var9.getSequenceId() : "")));
            }

            SAFConversationInfo var15 = this.createSAFConversationInfo(var8, var3, var2, var14);
            SAFConversationHandle var10 = safManager.registerConversationOnReceivingSide(var15);
            long var11 = var10.getConversationTimeout();
            WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_CREATE_SEQ);
            this.sendCreateSequenceResp(var3, var8, var11, var2, var6);
         }
      }
   }

   private void checkAndRemoveMustUnderstand(WsrmHeader var1, boolean var2, SOAPMessage var3) throws SAFException {
      if (var1.isMustUnderstand()) {
         SOAPHeader var4;
         try {
            var4 = var3.getSOAPHeader();
         } catch (SOAPException var8) {
            throw new SAFException(var8.toString(), var8);
         }

         if (var4 == null) {
            throw new JAXRPCException("No SOAPHeader found in SOAPMessage.");
         }

         Element var5;
         try {
            var5 = DOMUtils.getElementByTagNameNS(var4, var1.getRmVersion().getNamespaceUri(), var1.getName().getLocalPart());
         } catch (DOMProcessingException var7) {
            throw new JAXRPCException(var7);
         }

         var4.removeChild(var5);
      } else if (var2) {
         throw new JAXRPCException(var1.getName() + " header must be marked mustUnderstand = \"1\"");
      }

   }

   private boolean checkDuplicateCreateSequence(String var1, SOAPMessageContext var2, CreateSequenceMsg var3, String var4) throws SAFException {
      SAFConversationInfo var5;
      try {
         var5 = safManager.getConversationInfoOnReceivingSide(var1);
      } catch (SAFConversationNotAvailException var7) {
         var5 = null;
      }

      if (var5 != null) {
         if (verbose) {
            Verbose.log((Object)"Received duplicate create sequence request");
         }

         this.sendCreateSequenceResp(var2, var5.getConversationName(), var5.getConversationTimeout() > var5.getTimeToLive() ? var5.getTimeToLive() : var5.getConversationTimeout(), var3, var4);
         return true;
      } else {
         return false;
      }
   }

   private void sendCreateSequenceResp(SOAPMessageContext var1, String var2, long var3, CreateSequenceMsg var5, String var6) {
      try {
         SAFConversationInfo var7 = this.getConversationInfo(var2, var1);
         WsrmSequenceContext var8 = (WsrmSequenceContext)var7.getContext();
         WsrmSequenceContext var9 = null;
         if (var7.getConversationOffer() != null) {
            var9 = (WsrmSequenceContext)var7.getConversationOffer().getContext();
         }

         SOAPMessage var10 = getMessageFactory(var1).createMessage();
         var1.setMessage(var10);
         this.setCreateSequenceResponseHeader(var1, var8);
         CreateSequenceResponseMsg var11 = this.setCreateSequenceResponseMsg(var2, var3, var8);
         this.setCreateSequenceResponseOffer(var5, var6, var1, var11);
         var11.writeMsg(var10);
         this.setCreateSequenceResponseSecurity(var1);
         if (var9 != null) {
            if (var9.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
               var9.setMainAckTo(var11.getAccept().getAcksTo());
            }

            addTestSequenceSSLHeaderIfNeeded(var9, (WlMessageContext)var1);
         }

         if (verbose) {
            SequenceOffer var12 = var5.getOffer();
            Verbose.say("Requesting send of CreateSequenceResponse for recv side seq id " + var11.getSequenceId() + (var12 != null ? " offer seq id " + var12.getSequenceId() : ""));
         }

      } catch (HandshakeMsgException var13) {
         if (verbose) {
            Verbose.logException(var13);
         }

         throw new JAXRPCException("HandshakeMsgException", var13);
      } catch (SOAPException var14) {
         if (verbose) {
            Verbose.logException(var14);
         }

         throw new JAXRPCException("SOAPException", var14);
      }
   }

   private void setCreateSequenceResponseSecurity(SOAPMessageContext var1) {
      WsrmSecurityContext var2 = (WsrmSecurityContext)var1.getProperty("weblogic.wsee.wsrm.security.context");
      if (var2.isSecure()) {
         try {
            var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var2.getSecurityPolicy());
         } catch (PolicyException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new JAXRPCException("Policy exception", var4);
         }
      }

   }

   private void setCreateSequenceResponseOffer(CreateSequenceMsg var1, String var2, SOAPMessageContext var3, CreateSequenceResponseMsg var4) {
      SequenceOffer var5 = var1.getOffer();
      if (var5 != null) {
         SequenceAccept var6 = new SequenceAccept(var1.getRmVersion());
         EndpointReference var7 = this.calculateAcksToEprForAcceptedOffer(var2, var3);
         if (var7.getReferenceParameters().getHeader(ServiceIdentityHeader.TYPE) == null) {
            ServiceIdentityHeader var8 = new ServiceIdentityHeader();
            var8.setServerName(LocalServerIdentity.getIdentity().getServerName());
            var8.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
            var7.getReferenceParameters().addHeader(var8);
         }

         var6.setAcksTo(var7);
         var4.setAccept(var6);
      }

   }

   private EndpointReference calculateAcksToEprForAcceptedOffer(String var1, SOAPMessageContext var2) {
      URI var3 = URI.create(var1);
      String var4 = var3.getScheme();
      EndpointReference var5 = (EndpointReference)var2.getProperty("weblogic.wsee.addressing.ServerEndpoint");
      String var6 = var5.getAddress();
      int var7 = var6.indexOf(58);
      String var8 = var6.substring(var7);
      var6 = var4 + var8;
      AddressingProvider var9 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
      EndpointReference var10 = var9.createEndpointReference(var6);
      var10.setServiceName(var5.getServiceName(), var5.getPortName());
      FreeStandingMsgHeaders var11 = new FreeStandingMsgHeaders();
      MsgHeaders var12 = var5.getReferenceParameters();
      Iterator var13 = var12.listHeaders();

      while(var13.hasNext()) {
         MsgHeader var14 = (MsgHeader)var13.next();
         var11.addHeader(var14);
      }

      return var10;
   }

   private CreateSequenceResponseMsg setCreateSequenceResponseMsg(String var1, long var2, WsrmSequenceContext var4) {
      CreateSequenceResponseMsg var5 = new CreateSequenceResponseMsg(var4.getRmVersion());
      var5.setSequenceId(var1);
      if (var2 != Long.MAX_VALUE) {
         try {
            Duration var6 = DatatypeFactory.newInstance().newDuration(var2);
            var5.setExpires(var6);
         } catch (DatatypeConfigurationException var7) {
            throw new JAXRPCException(var7);
         } catch (Throwable var8) {
            throw new JAXRPCException(var8);
         }
      }

      if (var4.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var5.setIncompleteSequenceBehavior(WsrmConstants.IncompleteSequenceBehavior.NoDiscard);
      }

      return var5;
   }

   private void setCreateSequenceResponseHeader(SOAPMessageContext var1, WsrmSequenceContext var2) {
      String var3 = WsrmConstants.Action.CREATE_SEQUENCE_RESPONSE.getActionURI(var2.getRmVersion());
      ((WlMessageContext)var1).getHeaders().addHeader(AddressingHelper.getAddressingProvider(var1).createActionHeader(var3));
      var1.getMessage().getMimeHeaders().setHeader("SOAPAction", var3);
   }

   public void closeSequence(WlMessageContext var1, CloseSequenceMsg var2) {
      SoapMessageContext var3 = (SoapMessageContext)var1;
      String var4 = var2.getSequenceId();
      if (verbose) {
         Verbose.log((Object)("Close sequence with ID " + var4));
      }

      try {
         SAFConversationInfo var5 = this.getConversationInfo(var4, (SOAPMessageContext)var1);
         if (var5 != null) {
            WsrmSequenceContext var6 = (WsrmSequenceContext)var5.getContext();
            var6.setClosed(true);
            SAFManagerImpl.getManager().storeConversationContextOnReceivingSide(var4, var6);
            this.sendCloseSequenceResp(var3, var2);
         }
      } catch (Throwable var7) {
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new JAXRPCException("Unable to terminate sequence " + var4, var7);
      }
   }

   private void sendCloseSequenceResp(SoapMessageContext var1, CloseSequenceMsg var2) {
      try {
         SAFConversationInfo var3 = this.getConversationInfo(var2.getSequenceId(), var1);
         WsrmSequenceContext var4 = (WsrmSequenceContext)var3.getContext();
         SOAPMessage var5 = getMessageFactory(var1).createMessage();
         var1.setMessage(var5);
         this.setCloseSequenceResponseHeader(var1, var3, var4);
         CloseSequenceResponseMsg var6 = this.setCloseSequenceResponseMsg(var2.getSequenceId(), var4);
         var6.writeMsg(var5);
         this.setCloseSequenceResponseSecurity(var1);
         addTestSequenceSSLHeaderIfNeeded(var4, var1);
      } catch (HandshakeMsgException var7) {
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new JAXRPCException("HandshakeMsgException", var7);
      } catch (SOAPException var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new JAXRPCException("SOAPException", var8);
      }
   }

   private void setCloseSequenceResponseSecurity(SOAPMessageContext var1) {
      WsrmSecurityContext var2 = (WsrmSecurityContext)var1.getProperty("weblogic.wsee.wsrm.security.context");
      if (var2 != null && var2.isSecure()) {
         try {
            var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var2.getSecurityPolicy());
         } catch (PolicyException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new JAXRPCException("Policy exception", var4);
         }
      }

   }

   private void setCloseSequenceResponseHeader(SoapMessageContext var1, SAFConversationInfo var2, WsrmSequenceContext var3) {
      String var4 = WsrmConstants.Action.CLOSE_SEQUENCE_RESPONSE.getActionURI(var3.getRmVersion());
      var1.setProperty("weblogic.wsee.addressing.version", var3.getWsaVersion());
      var1.getHeaders().addHeader(AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).createActionHeader(var4));
      if (!var3.isSoap12()) {
         var1.getMessage().getMimeHeaders().setHeader("SOAPAction", var4);
      }

      this.setAckHeaderOnMessage(var1, var2);
   }

   private void setAckHeaderOnMessage(SoapMessageContext var1, SAFConversationInfo var2) {
      WsrmSequenceContext var3 = (WsrmSequenceContext)var2.getContext();
      String var4 = var2.getConversationName();
      AcknowledgementHeader var5 = this.createAckHeader(var4, var3);
      var1.getHeaders().addHeader(var5);
      addTestSequenceSSLHeaderIfNeeded(var3, var1);
   }

   private AcknowledgementHeader createAckHeader(String var1, WsrmSequenceContext var2) {
      AcknowledgementHeader var3 = new AcknowledgementHeader(var2.getRmVersion());
      var3.setSequenceId(var1);
      if (var2.isClosed()) {
         var3.setFinal(true);
      }

      try {
         List var4 = safManager.getAllSequenceNumberRangesOnReceivingSide(var1);
         if (var4.size() == 0) {
            var3.setNone(true);
         } else {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               Long var6 = (Long)var4.get(var5);
               ++var5;
               Long var7 = (Long)var4.get(var5);
               var3.acknowledgeMessages(var6, var7);
            }
         }

         return var3;
      } catch (SAFException var8) {
         throw new JAXRPCException(var8.toString(), var8);
      }
   }

   private CloseSequenceResponseMsg setCloseSequenceResponseMsg(String var1, WsrmSequenceContext var2) {
      CloseSequenceResponseMsg var3 = new CloseSequenceResponseMsg(var2.getRmVersion());
      var3.setSequenceId(var1);
      return var3;
   }

   public void terminateSequence(WlMessageContext var1, TerminateSequenceMsg var2) {
      SOAPMessageContext var3 = (SOAPMessageContext)var1;
      String var4 = var2.getSequenceId();
      if (verbose) {
         Verbose.log((Object)("*** Terminate sequence with ID " + var4));
      }

      if (var2.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
         AddressingUtil.confirmOneway(var1);
         var1.setProperty("weblogic.wsee.reliable.oneway.msg", "true");
      }

      try {
         SAFConversationInfo var5 = this.getConversationInfo(var4, (SOAPMessageContext)var1);
         if (var5 != null) {
            SAFConversationInfo var6 = var5.getConversationOffer();
            WsrmSequenceContext var7;
            if (var6 != null) {
               var7 = (WsrmSequenceContext)var6.getContext();
               boolean var8;
               if (var7.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
                  var8 = safManager.hasReceivedLastMessageOnReceivingSide(var4);
               } else {
                  var8 = true;
               }

               if (var8) {
                  this.startAutoTerminateProcessingForOffer(var4, var6, var2);
               } else if (verbose) {
                  Verbose.say("** Got terminate sequence message, but do not yet have the last message for the sequence. Cannot auto-terminate");
               }
            }

            var7 = (WsrmSequenceContext)var5.getContext();

            assert var7 != null;

            if (var7.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
               this.sendTerminateSequenceResp(var3, var2, var5);
            }

            safManager.closeConversationOnReceivingSide(var5);
         }
      } catch (SAFException var9) {
         if (verbose) {
            Verbose.logException(var9);
         }

         throw new JAXRPCException("Unable to terminate sequence " + var4, var9);
      } catch (Throwable var10) {
         if (verbose) {
            Verbose.logException(var10);
         }

         throw new JAXRPCException("Unable to terminate sequence " + var4, var10);
      }
   }

   private void startAutoTerminateProcessingForOffer(String var1, SAFConversationInfo var2, TerminateSequenceMsg var3) throws SAFException {
      if (verbose) {
         Verbose.say("*** Starting auto-terminate processing for OFFER sequence: " + var2.getConversationName() + " related to sequence: " + var1 + ". However, the offer will not be terminated until we see all responses for the final set of requests.");
      }

      String var4 = var2.getConversationName();
      WsrmSequenceContext var5 = (WsrmSequenceContext)var2.getContext();
      long var6;
      if (var5.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
         var6 = safManager.getLastMessageSequenceNumberOnReceivingSide(var1);
      } else {
         var6 = var3.getLastMsgNumber();
      }

      if (verbose) {
         Verbose.say("*** Final message on request sequence " + var1 + " had sequence number: " + var6);
      }

      var5.setFinalRequestSeqNum(var6);
      long var8 = var5.getFinalResponseSeqNum();
      if (var8 >= 0L) {
         if (verbose) {
            Verbose.say("*** Final message on request sequence " + var1 + " with sequence number: " + var6 + " was mapped to offer sequence " + var4 + " sequence number " + var8 + ". Enabling auto-terminate.");
         }

         this.enableAutoTerminateForOfferSequence(var4, var8);
      } else if (verbose) {
         Verbose.say("*** Final message on request sequence " + var1 + " with sequence number: " + var6 + " could not be mapped to a sequence number on offer sequence " + var4);
      }

   }

   public void checkForAutoTerminateOnOfferSequence(String var1, WsrmSequenceContext var2, EndpointReference var3) throws SAFException {
      if (verbose) {
         String var4 = var2.dumpRequestSeqNumToResponseSeqNumMap();
         Verbose.say("*** Current request->response seq num map for offer sequence " + var1 + " is: " + var4);
      }

      long var6 = var2.getFinalResponseSeqNum();
      if (var6 >= 0L) {
         if (verbose) {
            Verbose.say("*** Found finalResponseSeqNum=" + var6 + " on offer sequence " + var1 + ". Enabling auto-terminate");
         }

         this.enableAutoTerminateForOfferSequence(var1, var6);
      }

   }

   private void enableAutoTerminateForOfferSequence(String var1, long var2) throws SAFException {
      if (verbose) {
         Verbose.say("*** In WsrmSAFReceivingManager.enableAutoTerminateForOfferSequence on offer seq " + var1 + " with final response seq " + var2);
      }

      SAFConversationInfo var4 = getConversationInfo(true, var1, false);
      WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
      synchronized(var5) {
         if (var5.isOfferSequenceAutoTerminating()) {
            if (verbose) {
               Verbose.say("*** WHOA, duplicate request to enable auto-terminate on offer sequence " + var1 + ". Ignoring.");
            }

            return;
         }

         var5.setOfferSequenceAutoTerminating(true);
      }

      if (var5.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var5.getAcksTo() != null) {
         HashMap var6 = new HashMap();
         var6.put("weblogic.wsee.sequenceid", var1);
         WSAVersion var7 = var5.getWsaVersion();
         if (var7 != null) {
            var6.put("weblogic.wsee.addressing.version", var7);
         }

         if (verbose) {
            Verbose.say("*** Asking for LastMessage to be sent on offer seq " + var1);
            Verbose.say("*** Done asking for LastMessage to be sent on offer seq " + var1);
         }

         WsrmProtocolUtils.sendEmptyLastMessage(var6, (EndpointReference)var5.getAcksTo());
      } else {
         if (verbose) {
            Verbose.say("*** Setting sentLastMessageOnSendingSide for offer sequence " + var1 + ", and 'final' seq num=" + var2);
         }

         safManager.setSentLastMessageOnSendingSide(var1, var2);
      }

   }

   private void sendTerminateSequenceResp(SOAPMessageContext var1, TerminateSequenceMsg var2, SAFConversationInfo var3) {
      try {
         WsrmSequenceContext var4 = (WsrmSequenceContext)var3.getContext();
         SOAPMessage var5 = getMessageFactory(var1).createMessage();
         var1.setMessage(var5);
         this.setTerminateSequenceResponseHeader((SoapMessageContext)WlMessageContext.narrow(var1), var4, var3);
         TerminateSequenceResponseMsg var6 = this.setTerminateSequenceResponseMsg(var2.getSequenceId(), var4);
         var6.writeMsg(var5);
         this.setTerminateSequenceResponseSecurity(var1);
         addTestSequenceSSLHeaderIfNeeded(var4, (WlMessageContext)var1);
      } catch (HandshakeMsgException var7) {
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new JAXRPCException("HandshakeMsgException", var7);
      } catch (SOAPException var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new JAXRPCException("SOAPException", var8);
      }
   }

   private void setTerminateSequenceResponseSecurity(SOAPMessageContext var1) {
      WsrmSecurityContext var2 = (WsrmSecurityContext)var1.getProperty("weblogic.wsee.wsrm.security.context");
      if (var2 != null && var2.isSecure()) {
         try {
            var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var2.getSecurityPolicy());
         } catch (PolicyException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new JAXRPCException("Policy exception", var4);
         }
      }

   }

   private void setTerminateSequenceResponseHeader(SoapMessageContext var1, WsrmSequenceContext var2, SAFConversationInfo var3) {
      String var4 = WsrmConstants.Action.TERMINATE_SEQUENCE_RESPONSE.getActionURI(var2.getRmVersion());
      var1.setProperty("weblogic.wsee.addressing.version", var2.getWsaVersion());
      var1.getHeaders().addHeader(AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).createActionHeader(var4));
      var1.getMessage().getMimeHeaders().setHeader("SOAPAction", var4);
      this.setAckHeaderOnMessage(var1, var3);
   }

   private TerminateSequenceResponseMsg setTerminateSequenceResponseMsg(String var1, WsrmSequenceContext var2) {
      TerminateSequenceResponseMsg var3 = new TerminateSequenceResponseMsg(var2.getRmVersion());
      var3.setSequenceId(var1);
      return var3;
   }

   public void handleFault(SequenceFaultMsg var1) {
      SequenceFaultMsgType var2 = var1.getType();
      SAFConversationInfoImpl var3 = new SAFConversationInfoImpl();
      if (UnknownSequenceFaultMsg.TYPE.equals(var2)) {
         if (verbose) {
            Verbose.log((Object)("UnknownSequenceFault: received unknown sequence " + var1.getSequenceId()));
         }
      } else if (SequenceTerminatedFaultMsg.TYPE.equals(var2)) {
         if (verbose) {
            Verbose.log((Object)("SequenceTerminatedFault: sequence " + var1.getSequenceId() + " has been terminated on the sending side."));
         }

         var3.setConversationName(var1.getSequenceId());

         try {
            safManager.closeConversationOnReceivingSide(var3);
         } catch (Exception var8) {
            Verbose.logException(var8);
         }
      } else if (MessageNumRolloverFaultMsg.TYPE.equals(var2)) {
         if (verbose) {
            Verbose.log((Object)("MessageNumRolloverFault: sequence " + var1.getSequenceId() + " has run out of message number."));
         }

         var3.setConversationName(var1.getSequenceId());

         try {
            safManager.closeConversationOnReceivingSide(var3);
         } catch (Exception var7) {
            Verbose.logException(var7);
         }
      } else if (InvalidAckFaultMsg.TYPE.equals(var2)) {
         if (verbose) {
            Verbose.log((Object)("InvalidAckFaultMsg: sequence " + var1.getSequenceId() + " has invalid acknowldgements: "));
            Iterator var4 = ((InvalidAckFaultMsg)var1).listMessageRanges();

            while(var4.hasNext()) {
               MessageRange var5 = (MessageRange)var4.next();
               Verbose.getOut().println(var5.lowerBounds + " -- " + var5.upperBounds);
            }
         }

         var3.setConversationName(var1.getSequenceId());

         try {
            safManager.closeConversationOnReceivingSide(var3);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      } else if (verbose) {
         Verbose.log((Object)("SequenceFaultMsg: " + var1.getSubCodeQualifiedName() + " " + var1.getReason()));
      }

   }

   private void sendAck(SOAPMessageContext var1, AcknowledgementHeader var2, EndpointReference var3, WsrmSequenceContext var4) {
      try {
         WlMessageContext var5 = (WlMessageContext)var1;
         SOAPMessage var6 = getMessageFactory(var1).createMessage();
         var1.setMessage(var6);
         MsgHeaders var7 = var5.getHeaders();
         Iterator var8 = var3.getReferenceProperties().listHeaders();

         while(var8.hasNext()) {
            var7.addHeader((MsgHeader)var8.next());
         }

         var8 = var3.getReferenceParameters().listHeaders();

         while(var8.hasNext()) {
            var7.addHeader((MsgHeader)var8.next());
         }

         AddressingProvider var9 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var5);
         var7.addHeader(var2);
         var7.addHeader(var9.createToHeader(var3.getAddress()));
         var7.addHeader(var9.createReplyToHeader(var9.createAnonymousEndpointReference()));
         String var10 = WsrmConstants.Action.ACK.getActionURI(var4.getRmVersion());
         var7.addHeader(var9.createActionHeader(var10));
         var7.addHeader(var9.createMessageIdHeader(Guid.generateGuid()));
         if (!AsyncUtil.isSoap12(var5)) {
            var6.getMimeHeaders().setHeader("SOAPAction", var10);
         }

         WsrmSecurityContext var11 = var4.getWsrmSecurityContext();
         if (var4.isSecure()) {
            try {
               var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var11.getSecurityPolicy());
            } catch (PolicyException var13) {
               if (verbose) {
                  Verbose.logException(var13);
               }

               throw new JAXRPCException(var13);
            }
         }

         var1.setProperty("weblogic.wsee.reliable.oneway.reply", "true");
         addTestSequenceSSLHeaderIfNeeded(var4, (WlMessageContext)var1);
      } catch (SOAPException var14) {
         if (verbose) {
            Verbose.logException(var14);
         }

         throw new JAXRPCException(var14);
      }
   }

   public class ReliabilityBufferConfig {
      private int retryCount;
      private String retryDelay;

      public ReliabilityBufferConfig() {
         this.retryCount = 3;
         this.retryDelay = "5 seconds";
      }

      public ReliabilityBufferConfig(int var2, String var3) {
         this.retryCount = var2;
         this.retryDelay = var3;
      }

      public ReliabilityBufferConfig(ReliabilityBuffer var2) {
         this.retryCount = var2.retryCount();
         this.retryDelay = var2.retryDelay();
      }

      public int getRetryCount() {
         return this.retryCount;
      }

      public void setRetryCount(int var1) {
         this.retryCount = var1;
      }

      public String getRetryDelay() {
         return this.retryDelay;
      }

      public void setRetryDelay(String var1) {
         this.retryDelay = var1;
      }
   }
}
