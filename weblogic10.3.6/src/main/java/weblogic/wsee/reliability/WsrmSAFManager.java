package weblogic.wsee.reliability;

import java.net.Socket;
import java.security.AccessController;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFConversationNotAvailException;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFServiceNotAvailException;
import weblogic.messaging.saf.common.SAFRequestImpl;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.servlet.HttpServerTransport;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.faults.SequenceClosedFaultMsg;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultMsg;
import weblogic.wsee.reliability.handshake.CloseSequenceResponseMsg;
import weblogic.wsee.reliability.handshake.TerminateSequenceResponseMsg;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.TestSequenceSSLHeader;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class WsrmSAFManager {
   private static final boolean verbose = Verbose.isVerbose(WsrmSAFManager.class);
   protected static SAFManager safManager = null;

   WsrmSAFManager() {
      if (safManager == null) {
         safManager = SAFManagerImpl.getManager();
      }

   }

   protected SAFRequest createSAFRequest(SequenceHeader var1, String var2, SOAPMessageContext var3, WsrmSequenceContext var4) {
      SAFRequestImpl var5 = new SAFRequestImpl();
      var5.setConversationName(var1.getSequenceId());
      var5.setSequenceNumber(var1.getMessageNumber());
      var5.setDeliveryMode(2);
      var5.setTimeToLive(0L);
      var5.setTimestamp(System.currentTimeMillis());
      boolean var6;
      if (var4.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var1.isLastMessage()) {
         var6 = true;
      } else {
         var6 = "true".equals(var3.getProperty("weblogic.wsee.lastmessage"));
      }

      if (var6 && verbose) {
         Verbose.say("*** Setting 'End of Conversation' flag on request with message number " + var1.getMessageNumber() + " and sequence " + var1.getSequenceId());
      }

      var5.setEndOfConversation(var6);
      if (var2 != null) {
         var5.setMessageId(var2);
      }

      this.setupSAFRequestPayload(var3, var5);
      this.setupSAFRequestPayloadContext(var1, var3, var5, var4);
      return var5;
   }

   private void setupSAFRequestPayload(SOAPMessageContext var1, SAFRequest var2) {
      SOAPInvokeState var3 = this.createSOAPInvokeState(var1);
      var2.setPayload(var3);
      var2.setPayloadSize(var3.getPayloadSize());
   }

   protected SOAPInvokeState createSOAPInvokeState(SOAPMessageContext var1) {
      SOAPInvokeState var2 = new SOAPInvokeState(var1, true);
      AuthenticatedSubject var3 = (AuthenticatedSubject)var1.getProperty("weblogic.wsee.wss.subject");
      if (var3 != null && var3.getPrincipals().size() != 0) {
         var2.setSubject(var3);
      } else {
         var2.setSubject(ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())));
      }

      return var2;
   }

   private void setupSAFRequestPayloadContext(SequenceHeader var1, SOAPMessageContext var2, SAFRequest var3, WsrmSequenceContext var4) {
      WsrmPayloadContext var5 = new WsrmPayloadContext();
      boolean var6 = "true".equals(var2.getProperty("weblogic.wsee.lastmessage")) || var4.getRmVersion() == WsrmConstants.RMVersion.RM_10 && var1.isLastMessage();
      if (var6 && WsrmConstants.Action.LAST_MESSAGE.getActionURI(var4.getRmVersion()).equals(((ActionHeader)((WlMessageContext)var2).getHeaders().getHeader(ActionHeader.TYPE)).getActionURI())) {
         var5.setEmptyLastMessage(true);
      }

      try {
         if (var5.isEmptyLastMessage()) {
            WsrmSecurityContext var7 = var4.getWsrmSecurityContext();

            assert var7 != null;

            var5.setRequestPolicy(var7.getSecurityPolicy());
            var5.setResponsePolicy(var7.getSecurityPolicy());
         } else {
            var5.setRequestPolicy(PolicyContext.getRequestEffectivePolicy(var2));
            var5.setResponsePolicy(PolicyContext.getResponseEffectivePolicy(var2));
         }
      } catch (PolicyException var8) {
         throw new JAXRPCException(var8.getMessage());
      }

      var3.setPayloadContext(var5);
   }

   protected void setupSAFConversationMaxIdelTime(WlMessageContext var1, SAFConversationInfo var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.wsrm.InactivityTimeout");
      if (var3 != null) {
         try {
            Duration var4 = DatatypeFactory.newInstance().newDuration(var3);
            long var5 = System.currentTimeMillis();
            long var7 = var4.getTimeInMillis(new Date(var5));
            var2.setMaximumIdleTime(var7);
            if (verbose) {
               Verbose.log((Object)("InactivityTimeout is " + var4 + " msec"));
            }
         } catch (Exception var9) {
            throw new JAXRPCException(var9.toString(), var9);
         }
      } else {
         var2.setMaximumIdleTime(0L);
      }

   }

   protected void setupSAFConversationTTL(Duration var1, SAFConversationInfo var2) {
      if (var1 != null) {
         long var3 = var1.getTimeInMillis(new Date());
         if (var3 <= 0L) {
            throw new JAXRPCException("Invalid expiration time: " + var1.toString());
         }

         if (verbose) {
            Verbose.log((Object)("Setting RM sequence timetolive to " + var3));
         }

         var2.setTimeToLive(var3);
      } else {
         var2.setTimeToLive(Long.MAX_VALUE);
      }

   }

   protected void setupSAFConversationQOS(WlMessageContext var1, SAFConversationInfo var2) {
      if (var1.getProperty("weblogic.wsee.user.defined.qos") == null) {
         var2.setQOS(1);
         var2.setInorder(true);
      } else {
         Integer var3 = (Integer)var1.getProperty("weblogic.wsee.qos.delivery");
         if (var3 == null) {
            var2.setQOS(1);
         } else {
            var2.setQOS(var3);
         }

         String var4 = (String)var1.getProperty("weblogic.wsee.qos.inorder");
         if (var4 == null) {
            var2.setInorder(false);
         } else {
            var2.setInorder(true);
         }
      }

      if (verbose) {
         Verbose.log((Object)("Setting QOS to " + var2.getQOS()));
         Verbose.log((Object)("Setting inorder to " + var2.isInorder()));
      }

   }

   protected boolean checkOfferNeeded(WlMessageContext var1, WsrmConstants.RMVersion var2) {
      WsdlService var3 = var1.getDispatcher().getWsdlPort().getService();
      Iterator var4 = var3.getPortTypes().iterator();

      label49:
      while(var4.hasNext()) {
         WsdlPortType var5 = (WsdlPortType)var4.next();
         Iterator var6 = var5.getOperations().values().iterator();

         while(true) {
            WsdlOperation var7;
            do {
               if (!var6.hasNext()) {
                  continue label49;
               }

               var7 = (WsdlOperation)var6.next();
            } while(var7.getOutput() == null);

            if (var2 == WsrmConstants.RMVersion.RM_10) {
               return true;
            }

            WsdlBindingOperation var8 = (WsdlBindingOperation)var1.getDispatcher().getWsdlPort().getBinding().getOperations().get(var7.getName());
            PolicyServer var9 = var1.getDispatcher().getWsPort().getEndpoint().getService().getPolicyServer();
            Map var10 = var9.getCachedPolicies();
            if (var8 != null && var10 != null && var10.size() >= 1) {
               NormalizedExpression var11;
               try {
                  var11 = PolicyContext.getResponseEffectivePolicy(var7, var8, var9, var10);
               } catch (Exception var13) {
                  throw new JAXRPCException(var13.toString(), var13);
               }

               RM11Assertion var12 = (RM11Assertion)var11.getPolicyAssertion(RM11Assertion.class);
               if (var12 != null && var12.isOptional()) {
                  continue;
               }

               return true;
            }

            return true;
         }
      }

      return false;
   }

   protected void sendUnknownSequenceFault(String var1, SOAPMessageContext var2) {
      WsrmConstants.RMVersion var3 = WsrmProtocolUtils.getRMVersionFromMessageContext(var2);
      UnknownSequenceFaultMsg var4 = new UnknownSequenceFaultMsg(var3);
      var4.setSequenceId(var1);
      WsrmHelper.sendFault(var2, var4, (EndpointReference)null);
   }

   protected String getAsyncAddress(String var1, boolean var2) {
      int var4 = var1.indexOf(58);
      String var3;
      if (var4 < 0) {
         var3 = "http";
      } else {
         var3 = var1.substring(0, var4);
      }

      return ServerUtil.getServerURL(var3) + AsyncUtil.getAsyncUri(var2, var3);
   }

   protected String generateSequenceId() {
      String var1 = Guid.generateGuid();
      String var2 = LocalServerIdentity.getIdentity().getServerName();
      return "uuid:" + var2 + ":" + var1.substring(5, var1.length());
   }

   protected static MessageFactory getMessageFactory(SOAPMessageContext var0) {
      return ((SoapMessageContext)var0).getMessageFactory();
   }

   protected Duration getSequenceExpirationFromContext(WlMessageContext var1) {
      String var2 = (String)var1.getProperty("weblogic.wsee.wsrm.SequenceExpiration");
      Duration var3 = null;
      if (var2 != null && !var2.equals("P0S")) {
         try {
            var3 = DatatypeFactory.newInstance().newDuration(var2);
         } catch (Exception var5) {
            throw new RuntimeException(var5.toString(), var5);
         }
      }

      return var3;
   }

   protected void handleCloseSequenceResponse(boolean var1, CloseSequenceResponseMsg var2) {
      String var3 = var2.getSequenceId();
      SAFConversationInfo var4 = this.getConversationInfo(var1, var3);
      WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
      var5.setClosed(true);

      try {
         if (var1) {
            SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var3, var5);
         } else {
            SAFManagerImpl.getManager().storeConversationContextOnReceivingSide(var3, var5);
         }

      } catch (SAFException var7) {
         throw new JAXRPCException(var7.toString(), var7);
      }
   }

   protected void handleTerminateSequenceResponse(boolean var1, TerminateSequenceResponseMsg var2) {
   }

   protected SAFConversationInfo getConversationInfo(boolean var1, String var2) {
      return getConversationInfo(var1, var2, true);
   }

   public static SAFConversationInfo getConversationInfo(boolean var0, String var1, boolean var2) {
      try {
         SAFConversationInfo var3;
         if (var0) {
            var3 = safManager.getConversationInfoOnSendingSide(var1);
         } else {
            var3 = safManager.getConversationInfoOnReceivingSide(var1);
         }

         if (var3 == null && !var2) {
            throw new JAXRPCException("Unable to retrieve reliable sequence information on the " + (var0 ? "sending" : "receiving") + " side for sequence: " + var1);
         } else {
            return var3;
         }
      } catch (SAFServiceNotAvailException var5) {
         throw new JAXRPCException(var5.toString(), var5);
      } catch (Throwable var6) {
         if (verbose) {
            Verbose.logException(var6);
         }

         if (var2) {
            return null;
         } else if (var6 instanceof SAFConversationNotAvailException) {
            throw new JAXRPCException("Unknown conversation: " + var1);
         } else {
            throw new JAXRPCException("Unable to retrieve reliable sequence information on the " + (var0 ? "sending" : "receiving") + " side for sequence: " + var1, var6);
         }
      }
   }

   protected boolean checkForSequenceClosed(SOAPMessageContext var1, String var2, WsrmSequenceContext var3) {
      if (var3.isClosed()) {
         try {
            SequenceClosedFaultMsg var4 = new SequenceClosedFaultMsg(var3.getRmVersion());
            var4.setSequenceId(var2);
            WsrmHelper.sendFault(var1, var4, var3.getAcksTo());
            return true;
         } catch (JAXRPCException var5) {
            throw var5;
         } catch (Exception var6) {
            throw new JAXRPCException(var6.toString(), var6);
         }
      } else {
         return false;
      }
   }

   public static void setSSLSessionIdFromContext(WlMessageContext var0, WsrmSecurityContext var1, boolean var2) {
      byte[] var3 = getForcedSSLSessionId(var0);
      boolean var4 = var3 != null;
      if (var3 == null) {
         var3 = getSSLSessionId(var0);
      }

      if (verbose) {
         Verbose.say("%%%%%%%%%%%%%%%%% SETTING " + (var4 ? "*Forced*" : "Real") + " SSL/TLS Session ID for " + (var2 ? "offer" : "regular") + " sequence %%%%%%%%%%%%%%");
         Verbose.say("    SessionID: " + dumpByteArray(var3));
         Verbose.say("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      }

      var1.setSSLSessionId(var3);
      var1.setForcedSSLSessionId(var4);
   }

   public static byte[] getForcedSSLSessionId(WlMessageContext var0) {
      TestSequenceSSLHeader var1 = (TestSequenceSSLHeader)var0.getHeaders().getHeader(TestSequenceSSLHeader.TYPE);
      if (var1 != null) {
         try {
            byte[] var2 = var1.getSSLSessionId().getBytes("UTF-8");
            return var2;
         } catch (Exception var3) {
            throw new JAXRPCException(var3.toString(), var3);
         }
      } else {
         return null;
      }
   }

   public static byte[] getSSLSessionId(WlMessageContext var0) {
      Transport var1 = var0.getDispatcher().getConnection().getTransport();
      if (!(var1 instanceof HttpServerTransport)) {
         throw new JAXRPCException("Reliable endpoint has SSL/TLS security enabled, but incoming connection does not support it");
      } else {
         HttpServerTransport var2 = (HttpServerTransport)var1;
         if (!(var2.getRequest() instanceof ServletRequestImpl)) {
            throw new JAXRPCException("Reliable endpoint has SSL/TLS enabled, but incoming connection has unknown HttpServletRequest type: " + var2.getRequest());
         } else {
            ServletRequestImpl var3 = (ServletRequestImpl)var2.getRequest();
            Socket var4 = var3.getConnection().getSocket();
            if (!(var4 instanceof SSLSocket)) {
               throw new JAXRPCException("Reliable endpoint has SSL/TLS enabled, but socket from HttpServletRequest is not an SSLSocket: " + var4);
            } else {
               SSLSocket var5 = (SSLSocket)var4;
               SSLSession var6 = var5.getSession();
               if (var6 == null) {
                  throw new JAXRPCException("Reliable endpoint has SSL/TLS enabled, but socket from HttpServletRequest has no SSLSession on it");
               } else {
                  byte[] var7 = var6.getId();
                  return var7;
               }
            }
         }
      }
   }

   public static String dumpByteArray(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();
         byte[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            StringBuffer var6 = new StringBuffer(Byte.toString(var5));

            while(var6.length() < 3) {
               var6.insert(0, " ");
            }

            var1.append("[").append(var6).append("] ");
         }

         return var1.toString();
      }
   }

   public static void addTestSequenceSSLHeaderIfNeeded(WsrmSequenceContext var0, WlMessageContext var1) {
      WsrmSecurityContext var2 = var0.getWsrmSecurityContext();
      if (var2.isForcedSSLSessionId() && var1.getHeaders().getHeader(TestSequenceSSLHeader.TYPE) == null) {
         TestSequenceSSLHeader var3 = new TestSequenceSSLHeader(var0.getRmVersion());

         String var4;
         try {
            var4 = new String(var2.getSSLSessionId(), "UTF-8");
         } catch (Exception var6) {
            throw new JAXRPCException(var6.toString(), var6);
         }

         var3.setSSLSessionId(var4);
         var1.getHeaders().addHeader(var3);
      }

   }

   static {
      safManager = SAFManagerImpl.getManager();
   }
}
