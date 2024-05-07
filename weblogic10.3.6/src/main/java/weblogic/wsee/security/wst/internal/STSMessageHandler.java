package weblogic.wsee.security.wst.internal;

import java.util.Locale;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.security.WssServerPolicyHandler;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponseCollection;
import weblogic.wsee.security.wst.faults.BadRequestException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.TrustProcessor;
import weblogic.wsee.security.wst.framework.TrustProcessorFactory;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.SOAPHelper;
import weblogic.wsee.util.Verbose;

public class STSMessageHandler extends WSTServerHandler {
   private static final boolean verbose = Verbose.isVerbose(STSMessageHandler.class);
   private static final String STS_POLICY_VALIDATION_OFF = "weblogic.wsee.security.wst.sts.policy.validation.off";

   public boolean handleTrustRequest(SOAPMessageContext var1, String var2) {
      if (verbose) {
         Verbose.log((Object)(" handleTrustRequest  action arg value='" + var2 + "'"));
      }

      if (!Boolean.getBoolean("weblogic.wsee.security.wst.sts.policy.validation.off")) {
         this.validatePolicy(var1);
      }

      if (var1.containsProperty("weblogic.wsee.security.wssp.handlers.wst_heuristic")) {
         var1.removeProperty("weblogic.wsee.security.wssp.handlers.wst_heuristic");
      }

      String var3 = amendRSTRAction(var2);
      if (verbose) {
         Verbose.log((Object)(" handleTrustRequest  amended action arg value='" + var3 + "'"));
      }

      try {
         WSTContext var4 = this.initWSTContext(var1, var3);
         boolean var5 = false;
         if (var3.toLowerCase(Locale.ENGLISH).startsWith("http://docs.oasis-open.org/ws-sx/ws-trust/200512".toLowerCase(Locale.ENGLISH))) {
            var5 = true;
         }

         Node var6 = SOAPHelper.getRSTBaseNode(var1.getMessage());
         TrustProcessor var7 = getTrustProcessorFromAction(var2);
         Node var8 = var7.processRequestSecurityToken(var6, var4);
         Node var9 = var8;
         if (var5) {
            RequestSecurityTokenResponseCollection var10 = BindingHelper.createEmptyRSTRC(var4);
            Node var11 = BindingHelper.marshalRST(var10, var4);
            var8 = var11.getOwnerDocument().importNode(var8, true);
            var11.appendChild(var8);
            var9 = var11;
         }

         SOAPMessage var16 = SOAPHelper.createRSTBaseMsgContext(var9, var4).getMessage();
         var1.setMessage(var16);
         AddressingProvider var17 = AddressingHelper.getAddressingProvider(var1);
         ((WlMessageContext)var1).getHeaders().addHeader(var17.createActionHeader(var3));
         boolean var12 = "true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"));
         if (var12) {
            var1.setProperty("javax.xml.rpc.soap.http.soapaction.uri", var3);
         }
      } catch (PolicyException var13) {
         WSTFaultUtil.raiseFault(new BadRequestException("Unable to read trust boot strap policy: " + var13.getMessage()));
      } catch (SOAPException var14) {
         WSTFaultUtil.raiseFault(new BadRequestException("SOAP Exception on reading trust boot strap policy: " + var14.getMessage()));
      } catch (WSTFaultException var15) {
         var15.printStackTrace();
         WSTFaultUtil.raiseFault(var15);
      }

      return false;
   }

   private void validatePolicy(SOAPMessageContext var1) {
      GenericHandler var2 = this.getPolicyHandler();
      boolean var3 = var2.handleRequest(var1);
      if (!var3) {
         String var4 = "";

         try {
            var4 = " " + DOMUtils.toXMLString(var1.getMessage().getSOAPBody().getFault());
         } catch (SOAPException var6) {
         }

         WSTFaultUtil.raiseFault(new WSTFaultException("Failed to validate trust request against policy." + var4));
      }

   }

   protected GenericHandler getPolicyHandler() {
      WssServerPolicyHandler var1 = new WssServerPolicyHandler();
      return var1;
   }

   private WSTContext initWSTContext(SOAPMessageContext var1, String var2) throws PolicyException, SOAPException {
      WSTContext var3 = WSTContext.getWSTContext(var1);
      var3.setAppliesTo((String)var1.getProperty("weblogic.wsee.connection.end_point_address"));
      var3.setAction(var2);
      boolean var4 = ReliabilityPolicyAssertionsFactory.hasRMPolicy(PolicyContext.getEndpointPolicy(var1));
      boolean var5 = "true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"));
      if (var4 || var5) {
         var3.setPersistSession(true);
      }

      determineTokenType(var3, var2);
      setTrustNamespace(var3, var2);
      return var3;
   }

   private static final String amendRSTRAction(String var0) {
      if ("http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue".equals(var0)) {
         return "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal";
      } else {
         int var1 = var0.indexOf("/trust/RST/");
         if (var1 > 0) {
            return var0.substring(0, var1) + "/trust/RSTR/" + var0.substring(var1 + "/trust/RST/".length(), var0.length());
         } else {
            var1 = var0.indexOf("ws-trust/200512/RST/");
            return var1 > 0 ? var0.substring(0, var1) + "ws-trust/200512/RSTR/" + var0.substring(var1 + "ws-trust/200512/RST/".length(), var0.length()) : var0;
         }
      }
   }

   private static TrustProcessor getTrustProcessorFromAction(String var0) throws RequestFailedException {
      TrustProcessorFactory var1 = TrustProcessorFactory.getInstance();
      int var2 = var0.lastIndexOf(47);
      String var3 = var0.substring(var2);
      TrustProcessor var4;
      if (var3.equalsIgnoreCase("/SCT")) {
         var4 = var1.getProcessor(var0);
      } else {
         var4 = var1.getProcessor(var3);
      }

      if (var4 == null) {
         throw new RequestFailedException("Can not find trust processor to handle action:" + var0);
      } else {
         return var4;
      }
   }

   private static void determineTokenType(WSTContext var0, String var1) throws SOAPException {
      if (verbose) {
         Verbose.log((Object)(" about to look up token type for action='" + var1.toLowerCase(Locale.ENGLISH) + "'"));
      }

      String var2 = var1.toLowerCase(Locale.ENGLISH);
      if (var2.indexOf("sct") > -1) {
         if (var2.indexOf("http://schemas.xmlsoap.org/ws/2005/02/trust") > -1) {
            var0.setTokenType("http://schemas.xmlsoap.org/ws/2005/02/sc/sct");
         } else {
            if (var2.indexOf("http://docs.oasis-open.org/ws-sx/ws-trust/200512") <= -1) {
               throw new SOAPException(" Error.  While attempting to determine STS token type.  Unexpected action URI='" + var1 + "'");
            }

            var0.setTokenType("sct_v13");
         }
      }

   }

   private static void setTrustNamespace(WSTContext var0, String var1) throws SOAPException {
      if (verbose) {
         Verbose.log((Object)(" about to set trust ns for action='" + var1.toLowerCase(Locale.ENGLISH) + "'"));
      }

      String var2 = var1.toLowerCase(Locale.ENGLISH);
      if (var2.indexOf("http://schemas.xmlsoap.org/ws/2005/02/trust") > -1) {
         var0.setWstNamespaceURI("http://schemas.xmlsoap.org/ws/2005/02/trust");
      } else {
         if (var2.indexOf("http://docs.oasis-open.org/ws-sx/ws-trust/200512") <= -1) {
            throw new SOAPException(" Error.  While attempting to determine WS-Trust namespace.  Unexpected action URI='" + var1 + "'");
         }

         var0.setWstNamespaceURI("http://docs.oasis-open.org/ws-sx/ws-trust/200512");
      }

   }
}
