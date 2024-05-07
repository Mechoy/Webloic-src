package weblogic.wsee.security.saml;

import java.io.StringBufferInputStream;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.TrustSoapClient;
import weblogic.wsee.security.wst.framework.WSTContext;

public class SAMLSTSHelper {
   public static final SAMLToken performHandshake(SoapMessageContext var0) {
      NormalizedExpression var1 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveRequestPolicy");
      NormalizedExpression var2 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveResponsePolicy");

      try {
         if (var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx") == null) {
            WssPolicyContext var3 = new WssPolicyContext(KernelStatus.isServer());
            var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var3);
         }

         WSTContext var18 = WSTContext.getWSTContext(var0);
         String var4 = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID";
         var18.setTokenType(var4);
         var18.initEndpoints(var0);
         var18.setTrustVersion("http://schemas.xmlsoap.org/ws/2005/02/trust");
         var18.setAction(getAction("http://schemas.xmlsoap.org/ws/2005/02/trust"));
         var18.setWssp(false);
         var18.setBootstrapPolicy(getTrustBootStrapPolicy(false));
         TrustSoapClient var5 = new TrustSoapClient(var18);
         SAMLCredential var6 = SAMLTrustCredentialProvider.createCredential(var5, var18, new SAMLTokenHandler(), var4);
         var0.setProperty("weblogic.wsee.saml.credential", var6);
         StringBufferInputStream var7 = new StringBufferInputStream("<?xml version=\"1.0\"?>\n\n<wsp:Policy\n  xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"\n  xmlns:wssp=\"http://www.bea.com/wls90/security/policy\"\n  xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n  xmlns:wls=\"http://www.bea.com/wls90/security/policy/wsee#part\"\n  >\n\n  <wssp:Identity>\n    <wssp:SupportedTokens>\n <wssp:SecurityToken  TokenType=\"http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID\">\n        <wssp:Claims>\n          <wssp:ConfirmationMethod>sender-vouches</wssp:ConfirmationMethod>\n        </wssp:Claims>\n      </wssp:SecurityToken>    </wssp:SupportedTokens>\n  </wssp:Identity>\n</wsp:Policy>");
         NormalizedExpression var8 = PolicyFinder.readPolicyFromStream((PolicyServer)null, "SAMLPolicy.xml", var7, true).normalize();
         SAMLToken var9 = (SAMLToken)var5.generateTrustToken(var0, var8, var4);
         return var9;
      } catch (WSTFaultException var15) {
         var15.printStackTrace();
         WSTFaultUtil.raiseFault(var15);
      } catch (PolicyException var16) {
         WSTFaultUtil.raiseFault(new WSTFaultException(var16.getMessage()));
      } finally {
         if (var1 != null) {
            var0.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var1);
         }

         if (var2 != null) {
            var0.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", var2);
         }

      }

      return null;
   }

   public static String getAction(String var0) {
      return var0 + "/RST" + "/Issue";
   }

   private static String getRequestType(String var0) {
      return var0 + "/Issue";
   }

   public static NormalizedExpression getTrustBootStrapPolicy(boolean var0) {
      NormalizedExpression var1 = NormalizedExpression.createUnitializedExpression();
      if (!var0) {
         try {
            PolicyServer var2 = new PolicyServer();
            var1 = var2.getPolicy("SecurityTokenService.xml").normalize();
         } catch (PolicyException var3) {
         }
      }

      return var1;
   }
}
