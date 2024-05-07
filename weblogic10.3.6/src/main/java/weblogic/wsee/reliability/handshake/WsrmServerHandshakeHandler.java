package weblogic.wsee.reliability.handshake;

import java.util.Arrays;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmHandler;
import weblogic.wsee.reliability.WsrmHelper;
import weblogic.wsee.reliability.WsrmProtocolUtils;
import weblogic.wsee.reliability.WsrmSAFManager;
import weblogic.wsee.reliability.WsrmSAFManagerFactory;
import weblogic.wsee.reliability.WsrmSAFReceivingManager;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.WsrmSequenceContext;
import weblogic.wsee.reliability.faults.CreateSequenceRefusedFaultMsg;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.reliability.faults.SequenceFaultMsgType;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultException;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.policy.WsrmPolicyRuntimeHandler;
import weblogic.wsee.reliability.policy.WsrmPolicyServiceRuntimeHandler;
import weblogic.wsee.security.WssServerPolicyHandler;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.handlers.PostWssServerPolicyHandler;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class WsrmServerHandshakeHandler extends WsrmHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmServerHandshakeHandler.class);
   private NormalizedExpression cachedEndptPolicy = null;

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         WlMessageContext var3 = WlMessageContext.narrow(var1);
         MsgHeaders var4 = var3.getHeaders();
         if (var4 == null) {
            return true;
         } else {
            try {
               this.cachedEndptPolicy = PolicyContext.getEndpointPolicy(var1);
            } catch (PolicyException var6) {
               throw new JAXRPCException(var6.getMessage());
            }

            try {
               if (this.handleTerminateSequenceResponseMsg(var2)) {
                  AddressingUtil.confirmOneway((WlMessageContext)var2);
                  return false;
               } else {
                  boolean var5;
                  if (this.handleAckMsg(var4, var2)) {
                     var5 = this.checkIsPiggyback(var2, WsrmConstants.Action.ACK);
                     if (!var5) {
                        AddressingUtil.confirmOneway((WlMessageContext)var2);
                        return var5;
                     }
                  }

                  if (this.handleAckRequestedMsg(var4, var2)) {
                     var5 = this.checkIsPiggyback(var2, WsrmConstants.Action.ACK_REQUESTED);
                     if (!var5) {
                        AddressingUtil.confirmOneway((WlMessageContext)var2);
                     }

                     return var5;
                  } else if (this.handleCreateSequenceMsg(var2)) {
                     return false;
                  } else if (this.handleCreateSequenceResponseMsg(var2)) {
                     AddressingUtil.confirmOneway((WlMessageContext)var2);
                     return false;
                  } else if (this.handleCloseSequenceMsg(var2)) {
                     return false;
                  } else if (this.handleCloseSequenceResponseMsg(var2)) {
                     AddressingUtil.confirmOneway((WlMessageContext)var2);
                     return false;
                  } else if (this.handleTerminateSequenceMsg(var2)) {
                     return false;
                  } else if (this.handleEmptyLastMsg(var4, var2)) {
                     return false;
                  } else if (this.handleRMFault(var2)) {
                     AddressingUtil.confirmOneway((WlMessageContext)var2);
                     return false;
                  } else {
                     return true;
                  }
               }
            } catch (IllegalRMVersionFaultException var7) {
               if (verbose) {
                  Verbose.logException(var7);
               }

               sendIllegalRMVersionFault(var7, var2);
               return false;
            } catch (UnknownSequenceFaultException var8) {
               if (verbose) {
                  Verbose.logException(var8);
               }

               sendUnknownSequenceFault(var8, var2);
               return false;
            } catch (SAFConversationInfoNotFoundException var9) {
               if (verbose) {
                  Verbose.logException(var9);
               }

               return false;
            }
         }
      }
   }

   public static NormalizedExpression getAsyncResponseEndpointSecurityPolicy(boolean var0, String var1) {
      try {
         return getRMSequenceContext(var0, var1).getWsrmSecurityContext().getSecurityPolicy();
      } catch (PolicyException var3) {
         if (verbose) {
            Verbose.logException(var3);
         }

         throw new JAXRPCException(var3);
      }
   }

   private boolean checkIsPiggyback(SOAPMessageContext var1, WsrmConstants.Action var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      return WsrmProtocolUtils.getActionVersion(var2, var3) == null;
   }

   private boolean handleEmptyLastMsg(MsgHeaders var1, SOAPMessageContext var2) throws IllegalRMVersionFaultException {
      String var3 = (String)var2.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var4 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.LAST_MESSAGE, var3);
      if (var4 == null) {
         return false;
      } else {
         SequenceHeader var5 = (SequenceHeader)var1.getHeader(SequenceHeader.TYPE);
         if (var5 == null) {
            throw new JAXRPCException("No sequence header found for last message");
         } else {
            String var6 = var5.getSequenceId();
            WsrmSequenceContext var7 = checkRMVersionMatchWithCreateSequence(false, var6, var4, var2, this.cachedEndptPolicy);
            if (var7.isSecureWithSSL()) {
               validateSSLSessionId(var7.getWsrmSecurityContext(), (WlMessageContext)var2);
            }

            if (var7.isSecure()) {
               if (!this.validateSecurityPolicy(var2, false, var6)) {
                  return true;
               }

               validateCredential(false, var6, var7.getRmVersion(), var2);
            }

            WsrmSAFManagerFactory.getWsrmSAFReceivingManager().deliver(var5, var2);
            return true;
         }
      }
   }

   private boolean handleAckMsg(MsgHeaders var1, SOAPMessageContext var2) throws IllegalRMVersionFaultException {
      AcknowledgementHeader var3 = (AcknowledgementHeader)var1.getHeader(AcknowledgementHeader.TYPE);
      if (var3 != null) {
         WsrmConstants.RMVersion var4 = var3.getRmVersion();
         String var5 = var3.getSequenceId();
         if (verbose) {
            Verbose.log((Object)("Acknowledgement received with ID: " + var5));
         }

         WsrmSequenceContext var6 = checkRMVersionMatchWithCreateSequence(true, var5, var4, var2, this.cachedEndptPolicy);
         if (var6.isSecureWithSSL()) {
            validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var2);
         }

         if (var6.isSecure()) {
            if (!this.validateSecurityPolicy(var2, true, var5, false)) {
               return true;
            }

            validateCredential(true, var5, var6.getRmVersion(), var2);
         }

         WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleAck(var3, var2, false);
         return true;
      } else {
         return false;
      }
   }

   private boolean handleAckRequestedMsg(MsgHeaders var1, SOAPMessageContext var2) throws IllegalRMVersionFaultException {
      AckRequestedHeader var3 = (AckRequestedHeader)var1.getHeader(AckRequestedHeader.TYPE);
      if (var3 != null) {
         String var4 = var3.getSequenceId();
         if (verbose) {
            Verbose.log((Object)"Acknowledgement requested");
         }

         WsrmConstants.RMVersion var5 = var3.getRmVersion();
         WsrmSequenceContext var6 = checkRMVersionMatchWithCreateSequence(false, var4, var5, var2, this.cachedEndptPolicy);
         if (var6.isSecureWithSSL()) {
            validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var2);
         }

         if (var6.isSecure()) {
            if (!this.validateSecurityPolicy(var2, false, var4)) {
               return true;
            }

            validateCredential(false, var4, var6.getRmVersion(), var2);
         }

         WsrmSAFManagerFactory.getWsrmSAFReceivingManager().ackRequested(var4, var2);
         return true;
      } else {
         return false;
      }
   }

   private boolean handleCreateSequenceMsg(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.CREATE_SEQUENCE, var2);
      if (var3 == null) {
         return false;
      } else {
         this.checkAllowedRMVersionsForEndpoint("New", var3);

         try {
            (new WsrmPolicyRuntimeHandler()).processRequest(var1, this.cachedEndptPolicy, var3);
         } catch (PolicyException var12) {
            throw new JAXRPCException(var12.toString(), var12);
         }

         var1.setProperty("weblogic.wsee.wsrm.RMVersion", var3);
         boolean var4 = this.isMessageSecured();
         if (var4) {
            var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", this.cachedEndptPolicy);
            Object var5;
            if (this.isMessageSecuredWssp10()) {
               var5 = new WssServerPolicyHandler();
            } else {
               var5 = new PostWssServerPolicyHandler();
            }

            if (!((GenericHandler)var5).handleRequest(var1)) {
               if (verbose) {
                  this.printThrowableOnContext(var1);
               }

               return true;
            }
         }

         CreateSequenceMsg var13 = new CreateSequenceMsg(var3);

         try {
            var13.readMsg(var1.getMessage());
         } catch (HandshakeMsgException var11) {
            this.createSequenceRefused(var1, var3, var11);
            return true;
         }

         if (verbose) {
            Verbose.log((Object)"Sequence creation requested");
         }

         WsrmSecurityContext var6 = new WsrmSecurityContext(var1);

         try {
            var6.setSCCredential(this.getSCCredential(var13, var1));
         } catch (JAXRPCException var10) {
            this.createSequenceRefused(var1, var3, var10);
            return true;
         }

         try {
            WsrmSAFManagerFactory.getWsrmSAFReceivingManager().createSequence(var1, var13);
         } catch (JAXRPCException var8) {
            this.createSequenceRefused(var1, var3, var8);
         } catch (SAFException var9) {
            this.createSequenceRefused(var1, var3, var9);
         }

         return true;
      }
   }

   private void checkAllowedRMVersionsForEndpoint(String var1, WsrmConstants.RMVersion var2) throws IllegalRMVersionFaultException {
      WsrmProtocolUtils.checkRMVersion(var1, var2, this.cachedEndptPolicy);
   }

   private void createSequenceRefused(SOAPMessageContext var1, WsrmConstants.RMVersion var2, Exception var3) {
      if (verbose) {
         Verbose.logException(var3);
      }

      WsrmHelper.sendFault(var1, new CreateSequenceRefusedFaultMsg(var3, var2), (EndpointReference)null);
   }

   private boolean handleCreateSequenceResponseMsg(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.CREATE_SEQUENCE_RESPONSE, var2);
      if (var3 == null) {
         return false;
      } else {
         String var4 = (String)var1.getProperty("weblogic.wsee.addressing.RelatesTo");
         if (var4 == null) {
            throw new JAXRPCException("No related create sequence message found");
         } else {
            WsrmSequenceContext var5 = checkRMVersionMatchWithCreateSequence(true, var4, var3, var1, this.cachedEndptPolicy);
            if (var5.isSecure()) {
               NormalizedExpression var6 = getAsyncResponseEndpointSecurityPolicy(true, var4);
               var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var6);
               var1.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var6);
               var1.setProperty("weblogic.wsee.wssc.sct", var5.getWsrmSecurityContext().getSCCredential());
               if (!AsyncUtil.getWssClientHandler(var5).handleResponse(var1)) {
                  if (verbose) {
                     this.printThrowableOnContext(var1);
                  }

                  return true;
               }

               validateCredential(true, var4, var3, var1);
            }

            CreateSequenceResponseMsg var9 = new CreateSequenceResponseMsg(var3);

            try {
               var9.readMsg(var1.getMessage());
            } catch (HandshakeMsgException var8) {
               if (verbose) {
                  Verbose.logException(var8);
               }

               throw new JAXRPCException("HandshakeMsgException", var8);
            }

            if (verbose) {
               Verbose.log((Object)"Sequence creation response");
            }

            WsrmSAFManagerFactory.getWsrmSAFSendingManager().createSequenceResponse(var1, var9);
            return true;
         }
      }
   }

   public static WsrmSequenceContext checkRMVersionMatchWithCreateSequence(boolean var0, String var1, WsrmConstants.RMVersion var2, MessageContext var3, NormalizedExpression var4) throws IllegalRMVersionFaultException, UnknownSequenceFaultException {
      SAFConversationInfo var5 = WsrmSAFManager.getConversationInfo(var0, var1, true);
      if (var5 == null) {
         throw new UnknownSequenceFaultException(var1, var2);
      } else {
         WsrmSequenceContext var6 = (WsrmSequenceContext)var5.getContext();
         if (var2 != var6.getRmVersion()) {
            throw new IllegalRMVersionFaultException(var1, var2, Arrays.asList(var6.getRmVersion()));
         } else {
            var3.setProperty("weblogic.wsee.wsrm.RMVersion", var6.getRmVersion());

            try {
               (new WsrmPolicyServiceRuntimeHandler()).processRequest(var3, var4, var6.getRmVersion());
               return var6;
            } catch (PolicyException var8) {
               throw new JAXRPCException(var8.toString(), var8);
            }
         }
      }
   }

   public static void sendIllegalRMVersionFault(IllegalRMVersionFaultException var0, SOAPMessageContext var1) {
      IllegalRMVersionFaultMsg var2 = new IllegalRMVersionFaultMsg(var0.getActual(), var0.getAllowed());
      var2.setSequenceId(var0.getSequenceId());
      WsrmHelper.sendFault(var1, var2, (EndpointReference)null);
   }

   public static void sendUnknownSequenceFault(UnknownSequenceFaultException var0, SOAPMessageContext var1) {
      if (verbose) {
         Verbose.logException(var0);
      }

      UnknownSequenceFaultMsg var2 = new UnknownSequenceFaultMsg(var0.getRMVersion());
      var2.setSequenceId(var0.getSequenceId());
      WsrmHelper.sendFault(var1, var2, (EndpointReference)null);
   }

   public static boolean isRMSequenceSecure(boolean var0, String var1) {
      return getRMSequenceContext(var0, var1).getWsrmSecurityContext().isSecureWithWssc();
   }

   public static WsrmSequenceContext getRMSequenceContext(boolean var0, String var1) {
      SAFConversationInfo var2 = WsrmSAFManager.getConversationInfo(var0, var1, false);
      return (WsrmSequenceContext)var2.getContext();
   }

   private boolean handleCloseSequenceMsg(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.CLOSE_SEQUENCE, var2);
      if (var3 == null) {
         return false;
      } else {
         String var4;
         CloseSequenceMsg var5;
         try {
            var5 = new CloseSequenceMsg(var3);
            var5.readMsg(var1.getMessage());
            var4 = var5.getSequenceId();
         } catch (HandshakeMsgException var7) {
            if (verbose) {
               Verbose.logException(var7);
            }

            throw new JAXRPCException("HandshakeMsgException", var7);
         }

         if (verbose) {
            Verbose.log((Object)("Sequence " + var4 + " closure requested"));
         }

         WsrmSequenceContext var6 = checkRMVersionMatchWithCreateSequence(false, var4, var3, var1, this.cachedEndptPolicy);
         if (var6.isSecureWithSSL()) {
            validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var1);
         }

         if (var6.isSecure()) {
            if (!this.validateSecurityPolicy(var1, false, var4)) {
               return true;
            }

            validateCredential(false, var4, var6.getRmVersion(), var1);
         }

         WsrmSAFManagerFactory.getWsrmSAFReceivingManager().closeSequence((WlMessageContext)var1, var5);
         return true;
      }
   }

   private boolean handleCloseSequenceResponseMsg(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.CLOSE_SEQUENCE_RESPONSE, var2);
      if (var3 == null) {
         return false;
      } else {
         String var4;
         CloseSequenceResponseMsg var5;
         try {
            var5 = new CloseSequenceResponseMsg(var3);
            var5.readMsg(var1.getMessage());
            var4 = var5.getSequenceId();
         } catch (HandshakeMsgException var7) {
            if (verbose) {
               Verbose.logException(var7);
            }

            throw new JAXRPCException("HandshakeMsgException", var7);
         }

         if (verbose) {
            Verbose.log((Object)("Sequence " + var4 + " close response received"));
         }

         WsrmSequenceContext var6 = checkRMVersionMatchWithCreateSequence(true, var4, var3, var1, this.cachedEndptPolicy);
         if (var6.isSecureWithSSL()) {
            validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var1);
         }

         if (var6.isSecure()) {
            if (!this.validateSecurityPolicy(var1, true, var4)) {
               return true;
            }

            validateCredential(false, var4, var6.getRmVersion(), var1);
         }

         WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleCloseSequenceResponse(var5);
         return true;
      }
   }

   private boolean handleTerminateSequenceMsg(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.TERMINATE_SEQUENCE, var2);
      if (var3 == null) {
         return false;
      } else {
         String var4;
         TerminateSequenceMsg var5;
         try {
            var5 = new TerminateSequenceMsg(var3);
            var5.readMsg(var1.getMessage());
            var4 = var5.getSequenceId();
         } catch (HandshakeMsgException var7) {
            if (verbose) {
               Verbose.logException(var7);
            }

            throw new JAXRPCException("HandshakeMsgException", var7);
         }

         if (verbose) {
            Verbose.log((Object)("Sequence " + var4 + " termination requested"));
         }

         WsrmSequenceContext var6 = checkRMVersionMatchWithCreateSequence(false, var4, var3, var1, this.cachedEndptPolicy);
         if (var6.isSecureWithSSL()) {
            validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var1);
         }

         if (var6.isSecure()) {
            if (!this.validateSecurityPolicy(var1, false, var4)) {
               return true;
            }

            validateCredential(false, var4, var6.getRmVersion(), var1);
         }

         WsrmSAFManagerFactory.getWsrmSAFReceivingManager().terminateSequence((WlMessageContext)var1, var5);
         return true;
      }
   }

   private boolean handleTerminateSequenceResponseMsg(SOAPMessageContext var1) {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getActionVersion(WsrmConstants.Action.TERMINATE_SEQUENCE_RESPONSE, var2);
      if (var3 == null) {
         return false;
      } else {
         String var4;
         try {
            TerminateSequenceResponseMsg var5 = new TerminateSequenceResponseMsg(var3);
            var5.readMsg(var1.getMessage());
            var4 = var5.getSequenceId();
         } catch (HandshakeMsgException var7) {
            if (verbose) {
               Verbose.logException(var7);
            }

            throw new JAXRPCException("HandshakeMsgException", var7);
         }

         if (verbose) {
            Verbose.log((Object)("Sequence " + var4 + " termination response received"));
         }

         return true;
      }
   }

   private boolean handleRMFault(SOAPMessageContext var1) throws IllegalRMVersionFaultException {
      if (var1.getProperty("weblogic.wsee.ignore.fault") != null) {
         try {
            SequenceFaultMsg var2 = SequenceFaultMsgFactory.getInstance().parseSoapFault(var1.getMessage());
            SequenceFaultMsgType var3 = var2.getType();
            if (UnknownSequenceFaultMsg.TYPE.equals(var3)) {
               WsrmSAFManagerFactory.getWsrmSAFReceivingManager().handleFault(var2);
               return true;
            } else {
               String var4 = var2.getSequenceId();
               WsrmSequenceContext var5 = checkRMVersionMatchWithCreateSequence(false, var4, var2.getRmVersion(), var1, this.cachedEndptPolicy);
               if (var5.isSecureWithSSL()) {
                  validateSSLSessionId(var5.getWsrmSecurityContext(), (WlMessageContext)var1);
               }

               if (var5.isSecure()) {
                  if (!this.validateSecurityPolicy(var1, false, var4)) {
                     return true;
                  }

                  validateCredential(false, var4, var5.getRmVersion(), var1);
               }

               WsrmSAFManagerFactory.getWsrmSAFReceivingManager().handleFault(var2);
               return true;
            }
         } catch (SequenceFaultException var6) {
            return false;
         }
      } else {
         return false;
      }
   }

   private SCCredential getSCCredential(CreateSequenceMsg var1, MessageContext var2) {
      SecurityTokenReference var3 = var1.getSecurityTokenReference();
      if (var3 == null) {
         return null;
      } else {
         SecurityToken[] var4 = SecurityTokenHelper.findSecurityTokenByType(WSSecurityContext.getSecurityContext(var2), var3.getValueType());
         if (var4.length != 1) {
            throw new JAXRPCException("Can not find any security token of type: " + var3.getValueType() + " in CreateSequenceMessage");
         } else if (!(var4[0] instanceof SCTokenBase)) {
            throw new JAXRPCException("At least one SecurityToken was found in CreateSequenceMessage but none was of a recognized SCToken version.");
         } else {
            return ((SCTokenBase)var4[0]).getCredential();
         }
      }
   }

   public static void validateCredential(boolean var0, String var1, WsrmConstants.RMVersion var2, MessageContext var3) {
      SAFConversationInfo var4 = WsrmSAFManager.getConversationInfo(var0, var1, true);
      if (var4 == null && !var0) {
         UnknownSequenceFaultMsg var5 = new UnknownSequenceFaultMsg(var2);
         var5.setSequenceId(var1);
         SOAPMessageContext var6 = (SOAPMessageContext)var3;
         WsrmHelper.sendFault(var6, var5, (EndpointReference)null);
         throw new SAFConversationInfoNotFoundException("No sequence information found");
      } else {
         validateCredential(var4, var3);
      }
   }

   public static void validateCredential(SAFConversationInfo var0, MessageContext var1) {
      WsrmSequenceContext var2 = (WsrmSequenceContext)var0.getContext();
      WsrmSecurityContext var3 = var2.getWsrmSecurityContext();
      if (var3.isSecureWithWssc()) {
         checkSCCredential(var3, var1);
      }

   }

   private static void checkSCCredential(WsrmSecurityContext var0, MessageContext var1) {
      SCCredential var2 = (SCCredential)var1.getProperty("weblogic.wsee.wssc.sct");
      if (var2 != null) {
         if (var0 != null) {
            SCCredential var3 = var0.getSCCredential();
            if (var3 != null) {
               if (!var3.equals(var2)) {
                  throw new JAXRPCException("Incoming STR does not match reliable sequence's SCT, incoming: " + (var2 == null ? null : var2.getIdentifier()) + " stored: " + var3.getIdentifier());
               }
            }
         }
      }
   }

   private boolean validateSecurityPolicy(MessageContext var1, boolean var2, String var3) {
      return this.validateSecurityPolicy(var1, var2, var3, true);
   }

   private boolean validateSecurityPolicy(MessageContext var1, boolean var2, String var3, boolean var4) {
      if (this.isMessageSecured()) {
         var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", this.cachedEndptPolicy);
      } else {
         var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", getAsyncResponseEndpointSecurityPolicy(var2, var3));
      }

      boolean var5;
      if (var4) {
         var5 = AsyncUtil.getWssServerPolicyHandler(getRMSequenceContext(var2, var3)).handleRequest(var1);
      } else {
         var5 = AsyncUtil.getWssServerPolicyHandler(getRMSequenceContext(var2, var3)).handleResponse(var1);
      }

      if (!var5 && verbose) {
         this.printThrowableOnContext(var1);
      }

      return var5;
   }

   private boolean isMessageSecured() {
      return this.isMessageSecuredWssp10() || this.isMessageSecuredWssp12();
   }

   private boolean isMessageSecuredWssp10() {
      return SecurityPolicyAssertionFactory.isWSTEnabled(this.cachedEndptPolicy);
   }

   private boolean isMessageSecuredWssp12() {
      return SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(this.cachedEndptPolicy);
   }

   private void printThrowableOnContext(MessageContext var1) {
      Throwable var2 = WlMessageContext.narrow(var1).getFault();
      if (var2 != null) {
         Verbose.logException(var2);
      }

   }

   public static void validateSSLSessionId(WsrmSecurityContext var0, WlMessageContext var1) {
      if (var0 != null && var0.isSecureWithSSL()) {
         if (verbose) {
            Verbose.say("");
            Verbose.say("&&&&&&&&&&&&&&&&&& Validating SSL/TLS Session &&&&&&&&&&&&&&&");
         }

         byte[] var2 = WsrmSAFReceivingManager.getForcedSSLSessionId(var1);
         if (var2 == null) {
            var2 = WsrmSAFReceivingManager.getSSLSessionId(var1);
         }

         byte[] var3 = var0.getSSLSessionId();
         if (verbose) {
            Verbose.say("    Expected: " + dumpByteArray(var3));
            Verbose.say("    Actual:   " + dumpByteArray(var2));
            Verbose.say("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            Verbose.say("");
         }

         if (var2 == null || !Arrays.equals(var2, var3)) {
            throw new JAXRPCException("Improper SSL session ID found on incoming request. Expected " + dumpByteArray(var3) + " but got " + dumpByteArray(var2));
         }
      }

   }

   private static String dumpByteArray(byte[] var0) {
      return WsrmSAFReceivingManager.dumpByteArray(var0);
   }
}
