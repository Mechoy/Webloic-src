package weblogic.wsee.security.wst.framework;

import java.util.List;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponseCollection;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.helpers.BindingHelper;
import weblogic.wsee.security.wst.helpers.SOAPHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public class WSTCredentialProviderHelper {
   private static final boolean verbose = Verbose.isVerbose(WSTCredentialProviderHelper.class);

   public static RequestSecurityTokenResponse createCredential(WSTContext var0, SecurityTokenHandler var1) {
      try {
         return createCredential(getSoapClient(var0), var1);
      } catch (WSTFaultException var3) {
         WSTFaultUtil.raiseFault(var3);
         return null;
      }
   }

   private static RequestSecurityTokenResponse createCredential(TrustSoapClient var0, SecurityTokenHandler var1) throws WSTFaultException {
      SOAPMessage var2 = var0.requestTrustToken();
      RequestSecurityTokenResponse var3 = getRSTRfromSOAPMessage(var2, var1, (String)null);
      return var3;
   }

   public static RequestSecurityTokenResponse renewCredential(WSTContext var0, Object var1, SecurityTokenHandler var2, String var3, String var4, String var5) {
      try {
         var0.setAction(var5);
         SoapMessageContext var6 = SOAPHelper.createEmptyRSTBaseMsgContext(((SoapMessageContext)var0.getMessageContext()).isSoap12());
         var6.setProperty("weblogic.wsee.wssc.sct", var1);
         SOAPMessage var7 = getSoapClient(var0).renewTrustToken(var6, var4, var3);
         RequestSecurityTokenResponse var8 = getRSTRfromSOAPMessage(var7, var2, var3);
         return var8;
      } catch (InvalidRequestException var9) {
         WSTFaultUtil.raiseFault(var9);
      } catch (WSTFaultException var10) {
         WSTFaultUtil.raiseFault(var10);
      } catch (SOAPException var11) {
         WSTFaultUtil.raiseFault(new InvalidRequestException(var11.getMessage()));
      }

      return null;
   }

   public static void cancelCredential(MessageContext var0, Object var1, SecurityTokenHandler var2, String var3, String var4, String var5) {
      if (var1 == null) {
         WSTFaultUtil.raiseFault(new InvalidRequestException("No credential to cancel token"));
      }

      WSTContext var6 = WSTContext.getWSTContext(var0);
      var6.setAction(var5);

      try {
         SoapMessageContext var7 = SOAPHelper.createEmptyRSTBaseMsgContext(((SoapMessageContext)var0).isSoap12());
         var7.setProperty("weblogic.wsee.wssc.sct", var1);
         SOAPMessage var8 = getSoapClient(var6).cancelTrustToken(var7, var4, var3);
         RequestSecurityTokenResponse var9 = getRSTRfromSOAPMessage(var8, var2, var3);
         if (var9 != null && var9.getRequestedTokenCancelled() != null) {
            return;
         }

         WSTFaultUtil.raiseFault(new InvalidRequestException("Unable to cancel token"));
      } catch (WSTFaultException var10) {
         WSTFaultUtil.raiseFault(var10);
      } catch (SOAPException var11) {
         WSTFaultUtil.raiseFault(new InvalidRequestException(var11.getMessage()));
      }

   }

   private static RequestSecurityTokenResponse getRSTRfromSOAPMessage(SOAPMessage var0, SecurityTokenHandler var1, String var2) throws WSTFaultException {
      Node var3 = SOAPHelper.getRSTBaseNode(var0);
      if (var3 == null) {
         return null;
      } else {
         RequestSecurityTokenResponse var4 = null;
         if (var3.getLocalName().equals("RequestSecurityTokenResponseCollection")) {
            RequestSecurityTokenResponseCollection var5 = BindingHelper.unmarshalRSTRCNode(var3, var1, var2);
            List var6 = var5.getRequestSecurityTokenResponseCollection();
            if (var6.size() <= 0) {
               throw new WSTFaultException(" SecureTokenService did not return a token in the returned RequestSecurityTokenResponseCollection ");
            } else {
               var4 = (RequestSecurityTokenResponse)var6.get(0);
               return var4;
            }
         } else {
            var4 = BindingHelper.unmarshalRSTRNode(var3, var1, var2);
            return var4;
         }
      }
   }

   private static TrustSoapClient getSoapClient(WSTContext var0) throws InvalidRequestException {
      TrustSoapClient var1 = new TrustSoapClient(var0);
      return var1;
   }

   private static String getSTSURIFromMBean(MessageContext var0, String var1) {
      String var2 = null;
      WssPolicyContext var3 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var3 != null) {
         var2 = var3.getWssConfiguration().getSTSURI(var1);
         if (var2 == null) {
            var2 = var3.getWssConfiguration().getDefaultSTSURI();
         }
      }

      return var2;
   }

   public static String getSTSURIFromConfig(SecurityTokenContextHandler var0, MessageContext var1, String var2) {
      String var3 = (String)var0.getValue("weblogic.wsee.security.issuer_endpoint_ref");
      if (var3 == null) {
         var3 = getSTSURIFromMBean(var1, var2);
      }

      return var3;
   }

   private static String getSTSPolicyFromMBean(MessageContext var0, String var1) {
      String var2 = null;
      WssPolicyContext var3 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var3 != null) {
         var2 = var3.getWssConfiguration().getSTSPolicy(var1);
      }

      return var2;
   }

   public static NormalizedExpression getSTSPolicyFromConfig(SecurityTokenContextHandler var0, MessageContext var1, String var2) throws PolicyException {
      NormalizedExpression var3 = null;
      String var4 = null;
      if (var0 != null) {
         var3 = (NormalizedExpression)var0.getValue("weblogic.wsee.security.wst_bootstrap_policy");
      }

      if (var3 == null) {
         var4 = getSTSPolicyFromMBean(var1, var2);
      }

      WssPolicyContext var5 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var5 != null && var4 != null) {
         var3 = var5.getPolicyServer().getPolicy(var4).normalize();
      }

      return var3;
   }
}
