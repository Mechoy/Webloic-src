package weblogic.wsee.ws.dispatch.server;

import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import weblogic.wsee.addressing.RelatesToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmPermanentTransportException;
import weblogic.wsee.reliability.WsrmSAFManagerFactory;
import weblogic.wsee.reliability.faults.CreateSequenceRefusedFaultMsg;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.SequenceFaultMsgFactory;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.security.wssp.handlers.WssServerHandler;
import weblogic.wsee.util.Verbose;

public class SoapFaultHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(SoapFaultHandler.class);

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      if (var1.getProperty("weblogic.wsee.ignore.fault") != null) {
         RelatesToHeader var4;
         boolean var6;
         try {
            SOAPFault var2 = ((SoapMessageContext)var1).getMessage().getSOAPBody().getFault();

            assert var2 != null;

            List var3 = WsrmFaultMsg.getSubCodeQNames(CreateSequenceRefusedFaultMsg.class);
            if (var3.contains(var2.getFaultCodeAsQName())) {
               var4 = (RelatesToHeader)WlMessageContext.narrow(var1).getHeaders().getHeader(RelatesToHeader.TYPE);
               if (var4 == null) {
                  throw new JAXRPCException("No relates to header found");
               }

               String var12 = var4.getRelatedMessageId();
               var6 = WsrmServerHandshakeHandler.isRMSequenceSecure(true, var12);
               if (var6) {
                  if (!AsyncUtil.getWssServerHandler(WsrmServerHandshakeHandler.getRMSequenceContext(true, var12)).handleRequest(var1)) {
                     return true;
                  }

                  if (!this.validateSecurityPolicy(var1, true, var12)) {
                     return true;
                  }
               }

               if (verbose) {
                  Verbose.log((Object)("Create sequence refused with: " + var2.getFaultString()));
               }

               this.handlePermanentException(var1, var12, "-1");
               WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleCreateSequenceRefusedError(var12);
               return false;
            }
         } catch (SOAPException var9) {
            if (verbose) {
               Verbose.logException(var9);
            }
         }

         try {
            if (!(new WssServerHandler()).handleRequest(var1)) {
               return true;
            } else {
               SequenceFaultMsg var10 = SequenceFaultMsgFactory.getInstance().parseSoapFault(((SoapMessageContext)var1).getMessage());
               String var11 = var10.getSequenceId();
               var4 = (RelatesToHeader)WlMessageContext.narrow(var1).getHeaders().getHeader(RelatesToHeader.TYPE);
               boolean var5 = var10 instanceof IllegalRMVersionFaultMsg && "New".equals(var11);
               if (var5) {
                  if (var4 == null) {
                     throw new JAXRPCException("No relates to header found");
                  }

                  var11 = var4.getRelatedMessageId();
                  var10.setSequenceId(var11);
               }

               var6 = WsrmServerHandshakeHandler.isRMSequenceSecure(true, var11);
               if (var6) {
                  if (!this.validateSecurityPolicy(var1, true, var11)) {
                     return true;
                  }

                  WsrmServerHandshakeHandler.validateCredential(true, var11, (WsrmConstants.RMVersion)null, var1);
               }

               if (var4 != null) {
                  String var7 = var4.getRelatedMessageId();
                  if (var5) {
                     this.handlePermanentException(var1, var7, "-1");
                  } else {
                     this.handlePermanentException(var1, var11, var7);
                  }
               }

               WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleFault(var10);
               return false;
            }
         } catch (SequenceFaultException var8) {
            return true;
         }
      } else {
         return true;
      }
   }

   private void handlePermanentException(MessageContext var1, String var2, String var3) {
      try {
         SOAPFault var4 = ((SoapMessageContext)var1).getMessage().getSOAPBody().getFault();
         SOAPFaultException var5 = new SOAPFaultException(var4.getFaultCodeAsQName(), var4.getFaultString(), var4.getFaultActor(), var4.getDetail());
         WsrmPermanentTransportException var6 = new WsrmPermanentTransportException(var5.toString(), var5);
         WsrmSAFManagerFactory.getWsrmSAFSendingManager().handleAsyncFault(var2, var3, var6);
      } catch (SOAPException var7) {
      }

   }

   private boolean validateSecurityPolicy(MessageContext var1, boolean var2, String var3) {
      var1.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", WsrmServerHandshakeHandler.getAsyncResponseEndpointSecurityPolicy(var2, var3));
      return AsyncUtil.getWssServerPolicyHandler(WsrmServerHandshakeHandler.getRMSequenceContext(var2, var3)).handleRequest(var1);
   }
}
