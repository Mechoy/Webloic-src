package weblogic.wsee.reliability;

import java.security.AccessController;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFTransportException;
import weblogic.messaging.saf.common.SAFConversationHandleImpl;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.common.SAFRemoteContext;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.addressing.ToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultMsg;
import weblogic.wsee.reliability.faults.InvalidAckFaultMsg;
import weblogic.wsee.reliability.faults.LastMessageNumExceededFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgType;
import weblogic.wsee.reliability.faults.SequenceRefusedFaultMsg;
import weblogic.wsee.reliability.faults.SequenceTerminatedFaultMsg;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.CreateSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.SequenceAccept;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Verbose;

public final class WsrmSAFSendingManager extends WsrmSAFManager {
   private static final boolean verbose = Verbose.isVerbose(WsrmSAFSendingManager.class);

   WsrmSAFSendingManager() {
      if (verbose) {
         safManager.addConversationLifecycleListener(new SAFManager.ConversationLifecycleListener() {
            public void ack(SAFConversationInfo var1, long var2, long var4) {
               if (WsrmSAFSendingManager.verbose) {
                  Verbose.say("*** SAF got ACK on sequence with seq ID: " + var1.getConversationName() + " dyn seq ID: " + var1.getDynamicConversationName() + " low: " + var2 + " high: " + var4);

                  try {
                     long var6 = WsrmSAFManager.safManager.getLastAssignedSequenceValueOnSendingSide(var1.getConversationName());
                     Verbose.say("*** SAF has last sent for seq ID:        " + var1.getConversationName() + " dyn seq ID: " + var1.getDynamicConversationName() + " seq num: " + var6);
                  } catch (SAFException var8) {
                     Verbose.logException(var8);
                  }
               }

            }

            public void addToCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
               if (WsrmSAFSendingManager.verbose) {
                  Verbose.say("*** SAF adding (on " + (var1 ? "sending" : "receiving") + " side) for " + var2 + " conversation " + var4.getConversationName() + " under key " + var3);
                  Verbose.say("*** SAF currently tracking (on " + (var1 ? "sending" : "receiving") + " side): " + var5 + " conversations");
               }

            }

            public void preClose(boolean var1, boolean var2, SAFConversationInfo var3) {
               if (WsrmSAFSendingManager.verbose && var2) {
                  Verbose.say("*** SAF closing sequence (on " + (var1 ? "sending" : "receiving") + " side) with seq ID: " + var3.getConversationName() + " dyn seq ID: " + var3.getDynamicConversationName());
               }

            }

            public void removeFromCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
               if (WsrmSAFSendingManager.verbose) {
                  Verbose.say("*** SAF removing (on " + (var1 ? "sending" : "receiving") + " side) for " + var2 + " conversation " + var4.getConversationName() + " under key " + var3);
                  Verbose.say("*** SAF currently tracking (on " + (var1 ? "sending" : "receiving") + " side): " + var5 + " conversations");
               }

            }
         });
      }

   }

   private SAFConversationInfo createSAFConversationInfo(String var1, String var2, WlMessageContext var3) {
      SAFConversationInfoImpl var4 = new SAFConversationInfoImpl(2);
      var4.setDestinationType(2);
      var4.setDestinationURL(var2);
      var4.setDynamic(true);
      var4.setConversationName(var1);
      var4.setTransportType(2);
      var4.setErrorHandler(new WsrmSAFErrorHandler());
      var4.setCreateConversationMessageID(this.generateSequenceId());
      this.setupSAFConversationQOS(var3, var4);
      this.setupSAFConversationContext(var3, var2, var4);
      Duration var5 = (Duration)var3.getProperty("weblogic.wsee.wsrm.sequence.expiration");
      this.setupSAFConversationTTL(var5, var4);
      this.setupSAFConversationMaxIdelTime(var3, var4);
      this.setupSAFConversationRemoteContext(var3, var4);
      this.setupSAFConversationOffer(var3, var2, var4);
      return var4;
   }

   private void setupSAFConversationRemoteContext(WlMessageContext var1, SAFConversationInfo var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.wsrm.BaseRetransmissionInterval");
      Boolean var4 = (Boolean)var1.getProperty("weblogic.wsee.wsrm.RetransmissionExponentialBackoff");
      if (var3 != null) {
         try {
            Duration var5 = DatatypeFactory.newInstance().newDuration(var3);
            long var6 = System.currentTimeMillis();
            long var8 = var5.getTimeInMillis(new Date(var6));
            if (var4 != null && var4) {
               long var10 = var8 << 10;
               var2.setRemoteContext(new SAFRemoteContext(var8, var10, 2L));
            } else {
               var2.setRemoteContext(new SAFRemoteContext(var8, var8, 1L));
            }

            if (verbose) {
               Verbose.log((Object)("SAF BaseRetransmission is " + var3));
            }
         } catch (Exception var12) {
            throw new JAXRPCException(var12.toString(), var12);
         }
      }

   }

   private void setupSAFConversationContext(WlMessageContext var1, String var2, SAFConversationInfo var3) {
      WsrmSecurityContext var4 = new WsrmSecurityContext(var1);
      EndpointReference var5 = (EndpointReference)var1.getProperty("weblogic.wsee.acksto");
      AddressingProvider var6 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      if (var5 == null) {
         if ("true".equals(var1.getProperty("weblogic.wsee.ackstoanon"))) {
            var5 = var6.createAnonymousEndpointReference();
         } else {
            var5 = this.getAsyncResponseEPR(var1, var2, var6);
            if (var5.getReferenceParameters().getHeader(ServiceIdentityHeader.TYPE) == null) {
               ServiceIdentityHeader var7 = new ServiceIdentityHeader();
               var7.setServerName(LocalServerIdentity.getIdentity().getServerName());
               var7.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
               var5.getReferenceParameters().addHeader(var7);
            }
         }
      }

      WsrmSequenceContext var13 = new WsrmSequenceContext();
      WsrmConstants.RMVersion var8 = (WsrmConstants.RMVersion)var1.getProperty("weblogic.wsee.wsrm.RMVersion");
      if (var8 == null) {
         throw new JAXRPCException("No RMVersion set on request message context when sending first message of a sequence.");
      } else {
         var13.setRmVersion(var8);
         var13.setWsaVersion(AddressingHelper.getWSAVersion((MessageContext)var1));
         var13.setWsrmSecurityContext(var4);
         var13.setAcksTo(var5);
         if (AsyncUtil.isSoap12(var1)) {
            var13.setSoap12(true);
         }

         var13.setFailTo((EndpointReference)var1.getProperty("weblogic.wsee.failto"));
         var13.setSecuritySubject(ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())));
         var13.setTransportInfo((TransportInfo)var1.getProperty("weblogic.wsee.connection.transportinfo"));
         if (var1.containsProperty("weblogic.wsee.addressing.From")) {
            var13.setFrom((EndpointReference)var1.getProperty("weblogic.wsee.addressing.From"));
         }

         if (var1.containsProperty("weblogic.wsee.reliability.TestSequenceSSL")) {
            String var9 = (String)var1.getProperty("weblogic.wsee.reliability.TestSequenceSSL");

            byte[] var10;
            try {
               var10 = var9.getBytes("UTF-8");
            } catch (Exception var12) {
               throw new JAXRPCException(var12.toString(), var12);
            }

            if (verbose) {
               Verbose.say("");
               Verbose.say("%%%%%%%%%%%%%%%%%%%%% *FORCING* SSL/TLS Session ID on sending-side sequence %%%%%%%%%%");
               Verbose.say(WsrmSAFManager.dumpByteArray(var10));
               Verbose.say("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
               Verbose.say("");
            }

            var13.getWsrmSecurityContext().setSSLSessionId(var10);
            var13.getWsrmSecurityContext().setForcedSSLSessionId(true);
         }

         var3.setContext(var13);
         var3.setSourceURL(var5.getAddress());
      }
   }

   private EndpointReference getAsyncResponseEPR(WlMessageContext var1, String var2, AddressingProvider var3) {
      EndpointReference var4;
      if (var1.containsProperty("weblogic.wsee.async.res.epr")) {
         var4 = (EndpointReference)var1.getProperty("weblogic.wsee.async.res.epr");
         var4.setNamespaceURI(var3.getNamespaceURI());
      } else {
         var4 = var3.createEndpointReference(this.getAsyncAddress(var2, AsyncUtil.isSoap12(var1)));
      }

      return var4;
   }

   private void setupSAFConversationOffer(WlMessageContext var1, String var2, SAFConversationInfo var3) {
      WsrmConstants.RMVersion var4 = (WsrmConstants.RMVersion)var1.getProperty("weblogic.wsee.wsrm.RMVersion");
      if (var4 == null) {
         throw new JAXRPCException("No RMVersion set on request message context when sending first message of a sequence.");
      } else if (this.checkOfferNeeded(var1, var4)) {
         SAFConversationInfoImpl var5 = new SAFConversationInfoImpl(2);
         this.setupOfferDestination(var1, var3, var5);
         this.setupOfferName(var1, var5);
         var5.setSourceURL(var2);
         var5.setQOS(var3.getQOS());
         var5.setDynamic(false);
         var5.setInorder(var3.isInorder());
         var5.setDestinationType(2);
         var5.setTransportType(2);
         var5.setTimeoutPolicy(2);
         var5.setMaximumIdleTime(var3.getMaximumIdleTime());
         this.setupOfferTTL(var1, var5);
         var3.setConversationOffer(var5);
      }
   }

   private void setupOfferTTL(WlMessageContext var1, SAFConversationInfo var2) {
      Duration var3 = (Duration)var1.getProperty("weblogic.wsee.wsrm.offer.sequence.expiration");
      if (var3 != null) {
         this.setupSAFConversationTTL(var3, var2);
      } else {
         Duration var4 = this.getSequenceExpirationFromContext(var1);
         this.setupSAFConversationTTL(var4, var2);
      }

   }

   private void setupOfferName(WlMessageContext var1, SAFConversationInfo var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.offer.sequence.id");
      if (var3 == null || var3.equals("PendingOfferSeqId")) {
         var3 = this.generateSequenceId();
      }

      var2.setConversationName(var3);
   }

   private void setupOfferDestination(WlMessageContext var1, SAFConversationInfo var2, SAFConversationInfo var3) {
      ReplyToHeader var4 = (ReplyToHeader)var1.getHeaders().getHeader(ReplyToHeader.TYPE);

      assert var4 != null;

      EndpointReference var5 = var4.getReference();
      String var6 = var5.getAddress();
      AddressingProvider var7 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      if (var7.isAnonymousReferenceURI(var6)) {
         var6 = this.getAsyncResponseEPR(var1, var2.getDestinationURL(), var7).getAddress();
      }

      var3.setDestinationURL(var6);
   }

   private void verifyOrRegisterSAFConversationInfo(SequenceIdInfo var1, String var2, WlMessageContext var3, String var4) {
      SAFConversationInfo var5;
      try {
         var5 = safManager.getCachedConversationInfoOnSendingSide(var1.seqId);
      } catch (SAFException var9) {
         throw new JAXRPCException(var9);
      }

      if (var5 == null) {
         if (var1.preExisting) {
            SAFConversationNotAvailException var12 = new SAFConversationNotAvailException("Conversation " + var1.seqId + " was specified directly by the client, but has expired or has been terminated or destroyed. Please use a new conversation name.");
            var12.fillInStackTrace();
            throw new JAXRPCException(var12.toString(), var12);
         } else {
            if (verbose) {
               Verbose.log((Object)("** RM doing top-level register for SAF conversation (on sending side) in response to action '" + var4 + "' for sequence " + var1.seqId));
            }

            var5 = this.createSAFConversationInfo(var1.seqId, var2, var3);

            String var6;
            try {
               var6 = safManager.registerConversationOnSendingSide(var5);

               assert var1.seqId.equals(var6);
            } catch (SAFException var10) {
               if (verbose) {
                  Verbose.logException(var10);
               }

               throw new JAXRPCException("Failed to register sequence", var10);
            }

            if (var5.getConversationOffer() != null) {
               var6 = var5.getConversationOffer().getConversationName();
               Map var7 = (Map)var3.getProperty("weblogic.wsee.invoke_properties");
               var7.put("weblogic.wsee.offer.sequence.id", var6);
               if (verbose) {
                  MessageIdHeader var8 = (MessageIdHeader)var3.getHeaders().getHeader(MessageIdHeader.TYPE);
                  Verbose.log((Object)("Stored offer seq ID on invokeProperties for msg ID '" + var8.getMessageId() + "' as: " + var6));
               }
            } else {
               Map var11 = (Map)var3.getProperty("weblogic.wsee.invoke_properties");
               var11.put("weblogic.wsee.offer.sequence.id", (Object)null);
               if (verbose) {
                  MessageIdHeader var13 = (MessageIdHeader)var3.getHeaders().getHeader(MessageIdHeader.TYPE);
                  Verbose.log((Object)("Clearing pending offer seq ID from invokeProperties for msg ID '" + var13.getMessageId() + "'"));
               }
            }

         }
      }
   }

   void storeAndForward(WlMessageContext var1) {
      long var2 = System.nanoTime();
      if (verbose) {
         Verbose.say(var2 + " :Entering WsrmSAFSendingManager.storeAndForward(WlMessageContext)");
      }

      SOAPMessageContext var4 = (SOAPMessageContext)var1;
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 0");
      }

      MsgHeaders var5 = var1.getHeaders();
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 0.1");
      }

      ToHeader var6 = (ToHeader)var5.getHeader(ToHeader.TYPE);
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 0.2");
      }

      String var7 = var6.getAddress();
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 0.3");
      }

      MessageIdHeader var8 = (MessageIdHeader)var5.getHeader(MessageIdHeader.TYPE);
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 0.4");
      }

      String var9 = var8.getMessageId();
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 1");
      }

      Map var10 = this.getInvokeProperties(var1);
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 1.1");
      }

      SequenceIdInfo var11 = this.getSequenceId(var10, var1);
      String var12 = var11.seqId;
      String var13 = "Unknown";
      if (var1.containsProperty("weblogic.wsee.addressing.Action")) {
         var13 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 1.2");
         }
      } else {
         ActionHeader var14 = (ActionHeader)((SoapMessageContext)var4).getHeaders().getHeader(ActionHeader.TYPE);
         if (var14 != null) {
            var13 = var14.getActionURI();
         }

         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 1.3");
         }
      }

      SAFConversationInfo var15;
      String var25;
      if (var1.getProperty("weblogic.wsee.async.res") == null) {
         this.verifyOrRegisterSAFConversationInfo(var11, var7, var1, var13);
      } else {
         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 2");
         }

         var25 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqNumber");
         if (var25 != null) {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 3");
            }

            var15 = getConversationInfo(true, var11.seqId, false);
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 4");
            }

            if (var15 != null) {
               String var16 = var15.getConversationName();
               long var17 = Long.parseLong(var25);
               WsrmSequenceContext var19 = (WsrmSequenceContext)var15.getContext();
               if (verbose) {
                  String var20 = "normal";
                  if (var1.containsProperty("weblogic.wsee.conversation.AsyncConvId")) {
                     var20 = "conversation ID";
                  }

                  Verbose.say(var2 + " :*** Doing 'pre-SAF send' mapping of request to " + var7 + " action(" + var13 + ") with SeqNum " + var17 + " to " + var20 + " -1 on offer sequence " + var16);
               }

               var19.mapRequestSeqNumToResponseSeqNum(var17, -1L);

               try {
                  if (verbose) {
                     Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 5");
                  }

                  SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var16, var19);
                  if (verbose) {
                     Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 6");
                  }
               } catch (Exception var21) {
                  if (verbose) {
                     Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 6.1\n");
                     Verbose.logException(var21);
                  }

                  throw new JAXRPCException(var21.toString(), var21);
               }
            }
         }
      }

      if (verbose) {
         var25 = "Unknown";

         try {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 7");
            }

            var15 = this.getConversationInfo(true, var12);
            if (var15 != null && var15.getDynamicConversationName() != null) {
               var25 = var15.getDynamicConversationName();
            }

            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8");
            }
         } catch (Exception var24) {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.1");
            }

            Verbose.logException(var24);
         }

         Verbose.log((Object)(var2 + " :SendingManager sending reliable message with action '" + var13 + "' and " + (var11.preExisting ? "pre-existing" : "new") + " sender side sequence ID " + var12 + " receiver side sequence ID " + var25 + " to " + var7));
      }

      SequenceHeader var26 = this.createSequenceHeader(var12, var1, var5);
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.2");
      }

      var15 = getConversationInfo(true, var12, false);
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.3");
      }

      WsrmSequenceContext var27 = (WsrmSequenceContext)var15.getContext();
      if (verbose) {
         Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.4");
      }

      if (this.checkForSequenceClosed(var4, var12, var27)) {
         SOAPFault var29;
         try {
            SOAPBody var18 = var4.getMessage().getSOAPBody();
            var29 = var18.getFault();
         } catch (Exception var22) {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.5\n");
               Verbose.logException(var22);
            }

            throw new JAXRPCException("Cannot send messages on a closed sequence");
         }

         SOAPFaultException var30 = new SOAPFaultException(var29.getFaultCodeAsQName(), var29.getFaultString(), var29.getFaultActor(), var29.getDetail());
         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 8.6\n");
            Verbose.logException(var30);
         }

         throw var30;
      } else {
         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 9");
         }

         SAFRequest var28 = this.createSAFRequest(var26, var9, var4, var27);
         if (verbose) {
            Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 10");
         }

         this.checkConversationProtocolMsg(var4, var28, var10);

         try {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 11");
            }

            safManager.send(var28);
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 12");
            }

         } catch (SAFException var23) {
            if (verbose) {
               Verbose.say(var2 + " :Inside WsrmSAFSendingManager.storeAndForward(WlMessageContext) == 13\n");
               Verbose.logException(var23);
            }

            throw new JAXRPCException(var23.toString(), var23);
         }
      }
   }

   private void checkConversationProtocolMsg(SOAPMessageContext var1, SAFRequest var2, Map var3) {
      if (var1.getProperty("weblogic.wsee.conversation.waitid") != null) {
         ((WsrmPayloadContext)var2.getPayloadContext()).setWaitForConversationId(true);
      }

      if (var3.get("weblogic.wsee.conversation.key") != null) {
         ((WsrmPayloadContext)var2.getPayloadContext()).setConversationKey((String)var3.get("weblogic.wsee.conversation.key"));
         if (var1.getProperty("weblogic.wsee.conversation.ConversationPhase") == ConversationPhase.START) {
            ((WsrmPayloadContext)var2.getPayloadContext()).setStartConversation(true);
         }
      }

   }

   private SequenceHeader createSequenceHeader(String var1, WlMessageContext var2, MsgHeaders var3) {
      SAFConversationInfo var4 = getConversationInfo(true, var1, false);
      WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
      SequenceHeader var6 = new SequenceHeader(var5.getRmVersion());
      var6.setMessageNumber(-1L);
      var6.setSequenceId(var1);
      if (var5.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
         if ("true".equals(var2.getProperty("weblogic.wsee.lastmessage"))) {
            var6.setLastMessage(true);
         } else {
            var6.setLastMessage(false);
         }
      }

      var3.addHeader(var6);
      return var6;
   }

   private Map getInvokeProperties(WlMessageContext var1) {
      Object var2 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");
      if (var2 == null) {
         var2 = new ConcurrentHashMap();
         var1.setProperty("weblogic.wsee.invoke_properties", var2);
      }

      return (Map)var2;
   }

   private SequenceIdInfo getSequenceId(Map var1, WlMessageContext var2) {
      SequenceIdInfo var3 = new SequenceIdInfo();
      var3.preExisting = true;
      synchronized(var1) {
         var3.seqId = (String)var2.getProperty("weblogic.wsee.sequenceid");
         if (var3.seqId == null) {
            var3.seqId = (String)var1.get("weblogic.wsee.sequenceid");
         }

         if (var3.seqId == null || var3.seqId.equals("PendingSeqId")) {
            var3.seqId = this.generateSequenceId();
            var3.preExisting = false;
         }

         var1.put("weblogic.wsee.sequenceid", var3.seqId);
         if (verbose) {
            MessageIdHeader var5 = (MessageIdHeader)var2.getHeaders().getHeader(MessageIdHeader.TYPE);
            Verbose.say("Stored main sequence ID on invokeProperties for msg ID '" + var5.getMessageId() + "' as: " + var3.seqId);
         }

         return var3;
      }
   }

   public void handleAck(AcknowledgementHeader var1, SOAPMessageContext var2, boolean var3) {
      WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_VALIDATE_ACK_BEFORE_SAVE);
      String var4 = var1.getSequenceId();
      EndpointReference var5 = (EndpointReference)var2.getProperty("weblogic.wsee.addressing.FaultTo");

      assert var5 != null;

      String var6 = var5.getAddress();
      boolean var7 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2).isAnonymousReferenceURI(var6);
      if (!var3 && !var7) {
         AddressingUtil.confirmOneway((WlMessageContext)var2);
      }

      MessageRange var14;
      try {
         for(Iterator var8 = var1.listMessageRanges(); var8.hasNext(); safManager.acknowledge(var4, var14.lowerBounds, var14.upperBounds)) {
            var14 = (MessageRange)var8.next();
            if (verbose) {
               Verbose.log((Object)("Calling ack " + var14.lowerBounds + " - " + var14.upperBounds));
            }
         }
      } catch (SAFException var13) {
         if (verbose) {
            Verbose.logException(var13);
         }

         if (var3 && var7) {
            return;
         }

         try {
            SAFConversationInfo var9 = getConversationInfo(true, var4, false);
            WsrmSequenceContext var10 = (WsrmSequenceContext)var9.getContext();
            InvalidAckFaultMsg var11 = new InvalidAckFaultMsg(var10.getRmVersion());
            var11.setSequenceId(var4);
            var11.setAcknowledgementRanges(var1.getAcknowledgementRanges());
            if (!var3) {
               WsrmHelper.sendFault(var2, var11, var10.getAcksTo());
               return;
            }

            WsrmSequenceSender.sendFault(var4, (WsrmSequenceContext)var9.getContext(), var5, (SequenceFaultMsg)var11, AsyncUtil.isSoap12(var2));
         } catch (SAFException var12) {
            if (verbose) {
               Verbose.logException(var12);
            }
         }

         return;
      }

      if (!var3) {
         var2.setProperty("weblogic.wsee.reliable.oneway.msg", "true");
         if (var7) {
            AddressingUtil.confirmOneway((WlMessageContext)var2);
         }
      }

   }

   public void ackRequest(WlMessageContext var1) {
      String var2 = (String)var1.getProperty("weblogic.wsee.sequenceid");

      SAFConversationInfo var3;
      try {
         var3 = safManager.getCachedConversationInfoOnSendingSide(var2);
      } catch (SAFException var5) {
         throw new JAXRPCException(var5);
      }

      if (var3 == null) {
         if (verbose) {
            Verbose.log((Object)("Cannot find conversation info for sequence " + var2));
         }

      } else {
         try {
            AcknowledgementHeader var4 = WsrmSequenceSender.ackRequest(var3);
            if (verbose) {
               if (var4 != null) {
                  Verbose.log((Object)var4.toString());
               } else {
                  Verbose.log((Object)"Received null ack header, it will be sent back asynchronously");
               }
            }
         } catch (SAFTransportException var6) {
            if (verbose) {
               Verbose.logException(var6);
            }
         }

      }
   }

   public void handleAsyncFault(String var1, String var2, Exception var3) {
      SAFConversationInfo var4 = this.getConversationInfo(true, var1);
      if (var4 != null) {
         try {
            safManager.handleAsyncFault(var4.getConversationName(), var2, var3);
         } catch (SAFException var6) {
            if (verbose) {
               Verbose.logException(var6);
            }
         }
      }

   }

   public void handleCreateSequenceRefusedError(String var1) {
      if (verbose) {
         Verbose.log((Object)("CreateSequenceRefusedFault: receiver refuse to create a sequence for message " + var1));
      }

      try {
         SAFConversationInfo var2 = this.getConversationInfo(true, var1);
         if (var2 != null) {
            safManager.closeConversationOnSendingSide(var2.getConversationName(), true);
         }
      } catch (SAFException var3) {
         if (verbose) {
            Verbose.logException(var3);
         }
      }

   }

   public void handleFault(SequenceFaultMsg var1) {
      SequenceFaultMsgType var2 = var1.getType();

      try {
         if (UnknownSequenceFaultMsg.TYPE.equals(var2)) {
            if (verbose) {
               Verbose.log((Object)("UnknownSequenceFault: receiver received unknown sequence " + var1.getSequenceId()));
            }
         } else if (SequenceRefusedFaultMsg.TYPE.equals(var2)) {
            if (verbose) {
               Verbose.log((Object)("SequenceRefusedFault: sequence " + var1.getSequenceId() + " has been refused."));
            }

            safManager.closeConversationOnSendingSide(var1.getSequenceId(), true);
         } else if (SequenceTerminatedFaultMsg.TYPE.equals(var2)) {
            if (verbose) {
               Verbose.log((Object)("SequenceTerminatedFault: sequence " + var1.getSequenceId() + " has been terminated on the receiving side."));
            }

            safManager.closeConversationOnSendingSide(var1.getSequenceId(), true);
         } else if (LastMessageNumExceededFaultMsg.TYPE.equals(var2)) {
            if (verbose) {
               Verbose.log((Object)("LastMessageNumExceededFault: receiver received a message  that exceeds the last message number for sequence " + var1.getSequenceId()));
            }

            safManager.closeConversationOnSendingSide(var1.getSequenceId(), true);
         } else if (IllegalRMVersionFaultMsg.TYPE.equals(var2)) {
            String var3 = var1.getSequenceId();
            if (verbose) {
               Verbose.log((Object)("IllegalRMVersionFault: receiver received a message  with an unexpected RM version for sequence " + var3));
            }

            SAFConversationInfo var4 = this.getConversationInfo(true, var3);
            if (var4 != null) {
               safManager.closeConversationOnSendingSide(var4.getConversationName(), true);
            }
         } else if (verbose) {
            Verbose.log((Object)("SequenceFaultMsg: " + var1.getSubCodeQName() + " " + var1.getReason()));
         }
      } catch (SAFException var5) {
         if (verbose) {
            Verbose.logException(var5);
         }
      }

   }

   public void createSequenceResponse(SOAPMessageContext var1, CreateSequenceResponseMsg var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.addressing.RelatesTo");
      AddressingUtil.confirmOneway((WlMessageContext)var1);
      SAFConversationInfo var4 = getConversationInfo(true, var3, true);
      if (var4 == null) {
         throw new JAXRPCException("Unknown create sequence response message. Sequence " + var3);
      } else if (var4.getDynamicConversationName() != null) {
         if (verbose) {
            Verbose.log((Object)("Got duplicate create sequence response. Receiving side seq id is already set to " + var4.getDynamicConversationName() + ". Ignoring."));
         }

      } else {
         WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
         EndpointReference var6 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.From");
         if (var6 != null && !AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).isAnonymousReferenceURI(var6.getAddress())) {
            var5.setDestination(var6);
         }

         SAFConversationInfo var7 = var4.getConversationOffer();
         if (verbose) {
            Verbose.log((Object)("** Processing CreateSequenceResponse for sequence " + var4.getConversationName() + (var7 != null ? " offer sequence " + var7.getConversationName() : null) + " receiver side sequence " + var2.getSequenceId()));
         }

         String var8 = var2.getSequenceId();
         this.setOfferSequenceContext(var2, var7, var5, (WlMessageContext)var1);
         Duration var9 = var2.getExpires();
         long var10 = Long.MAX_VALUE;
         if (var9 != null) {
            var10 = var9.getTimeInMillis(new Date());
         }

         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_CREATE_SEQ_RES_BEFORE_RM);
         SAFConversationHandleImpl var12 = new SAFConversationHandleImpl(var8, var10, var4.getMaximumIdleTime(), var7, var3, var4.getContext());

         try {
            safManager.createConversationSucceeded(var12);
         } catch (SAFException var14) {
            throw new JAXRPCException(var14);
         }

         WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RM_BEFORE_SEND);
      }
   }

   private void setOfferSequenceContext(CreateSequenceResponseMsg var1, SAFConversationInfo var2, WsrmSequenceContext var3, WlMessageContext var4) {
      SequenceAccept var5 = var1.getAccept();
      if (var5 != null) {
         EndpointReference var6 = var5.getAcksTo();
         if (var2 != null) {
            WsrmSequenceContext var7 = new WsrmSequenceContext();
            var7.setWsaVersion(var3.getWsaVersion());
            var7.setRmVersion(var3.getRmVersion());
            var7.setSoap12(var3.isSoap12());
            var7.setFrom(var3.getFrom());
            var7.setAcksTo(var6);
            var7.setWsrmSecurityContext(new WsrmSecurityContext(var3.getWsrmSecurityContext()));
            var7.setTransportInfo(var3.getTransportInfo());
            if (var3.isSecureWithSSL()) {
               setSSLSessionIdFromContext(var4, var7.getWsrmSecurityContext(), true);
            }

            var2.setContext(var7);
            if (var3.getDestination() == null) {
               var3.setDestination(var6);
            }
         }
      }

   }

   public void handleCloseSequenceResponse(CloseSequenceResponseMsg var1) {
      super.handleCloseSequenceResponse(true, var1);
   }

   public void handleTerminateSequenceResponse(TerminateSequenceResponseMsg var1) {
      super.handleTerminateSequenceResponse(true, var1);
   }

   private class SequenceIdInfo {
      String seqId;
      boolean preExisting;

      private SequenceIdInfo() {
      }

      // $FF: synthetic method
      SequenceIdInfo(Object var2) {
         this();
      }
   }
}
