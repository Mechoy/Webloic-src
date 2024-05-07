package weblogic.wsee.reliability;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.addressing.RelatesToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.faults.UnknownSequenceFaultException;
import weblogic.wsee.reliability.faults.WSRMRequiredFaultMsg;
import weblogic.wsee.reliability.handshake.SAFConversationInfoNotFoundException;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.policy.WsrmPolicyServiceRuntimeHandler;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;

public class WsrmServerHandler extends WsrmHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmServerHandler.class);

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
               (new WsrmPolicyServiceRuntimeHandler()).processRequest(var2, this.getEffectivePolicy(var1));
            } catch (Exception var14) {
               throw new JAXRPCException(var14.toString(), var14);
            }

            boolean var5;
            try {
               var5 = this.handleSequenceMsg(var4, var2);
            } catch (SAFConversationInfoNotFoundException var12) {
               return false;
            } catch (Exception var13) {
               var2.setProperty("weblogic.wsee.reliable.oneway.reply", true);
               ((SoapMessageContext)var2).setFault(var13);
               return false;
            }

            boolean var6 = var3.getProperty("weblogic.wsee.rm.mandatory") == null;
            if (!var6 && !var5) {
               WsrmConstants.RMVersion var7 = (WsrmConstants.RMVersion)var2.getProperty("weblogic.wsee.wsrm.RMVersion");
               if (var7 == null) {
                  var7 = WsrmConstants.RMVersion.latest();
               }

               if (var7 == WsrmConstants.RMVersion.RM_10) {
                  throw new JAXRPCException("This endpoint requires reliable messaging");
               }

               WSRMRequiredFaultMsg var8 = new WSRMRequiredFaultMsg(var7);
               boolean var9 = AsyncUtil.isSoap12(var2);
               MessageFactory var10 = WLMessageFactory.getInstance().getMessageFactory(var9);

               try {
                  SOAPMessage var11 = var10.createMessage();
                  var2.setMessage(var11);
                  var8.write(var11);
               } catch (SOAPException var15) {
                  if (verbose) {
                     Verbose.logException(var15);
                  }

                  throw new JAXRPCException("Create SOAP Message failed", var15);
               }
            }

            if (var5) {
               var1.setProperty("weblogic.wsee.queued.invoke", "true");
               return false;
            } else {
               return true;
            }
         }
      }
   }

   private boolean handleSequenceMsg(MsgHeaders var1, SOAPMessageContext var2) {
      SequenceHeader var3 = (SequenceHeader)var1.getHeader(SequenceHeader.TYPE);
      if (var3 != null) {
         String var4 = var3.getSequenceId();
         if (verbose) {
            Verbose.log((Object)("Sequence received with ID: " + var4 + ", Message #: " + var3.getMessageNumber()));
         }

         NormalizedExpression var5 = this.getEffectivePolicy(var2);

         WsrmSequenceContext var6;
         try {
            var6 = WsrmServerHandshakeHandler.checkRMVersionMatchWithCreateSequence(false, var4, var3.getRmVersion(), var2, var5);
         } catch (IllegalRMVersionFaultException var8) {
            WsrmServerHandshakeHandler.sendIllegalRMVersionFault(var8, var2);
            return true;
         } catch (UnknownSequenceFaultException var9) {
            WsrmServerHandshakeHandler.sendUnknownSequenceFault(var9, var2);
            return true;
         }

         if (var6.isSecureWithSSL()) {
            WsrmServerHandshakeHandler.validateSSLSessionId(var6.getWsrmSecurityContext(), (WlMessageContext)var2);
         }

         if (var6.isSecure()) {
            WsrmServerHandshakeHandler.validateCredential(false, var4, var3.getRmVersion(), var2);
         }

         if (var1.getHeader(RelatesToHeader.TYPE) != null) {
            WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RES_WSRM_SEC_VALIDATION);
         }

         WsrmSAFManagerFactory.getWsrmSAFReceivingManager().deliver(var3, var2);
         if (!((WlMessageContext)var2).hasFault() && verbose) {
            Verbose.log((Object)"Sequence delivered");
         }

         return true;
      } else {
         return false;
      }
   }

   private NormalizedExpression getEffectivePolicy(MessageContext var1) {
      try {
         NormalizedExpression var2;
         if (var1.containsProperty("weblogic.wsee.async.res")) {
            var2 = PolicyContext.getResponseEffectivePolicy(var1);
         } else {
            var2 = PolicyContext.getRequestEffectivePolicy(var1);
         }

         return var2;
      } catch (Exception var4) {
         throw new JAXRPCException(var4.toString(), var4);
      }
   }
}
