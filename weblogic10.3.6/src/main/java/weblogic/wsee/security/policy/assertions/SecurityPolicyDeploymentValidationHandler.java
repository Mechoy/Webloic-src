package weblogic.wsee.security.policy.assertions;

import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.provider.PolicyValidationHandler;
import weblogic.wsee.security.policy.assertions.xbeans.IdentityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy.assertions.xbeans.SupportedTokensType;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.xml.crypto.wss.SecurityUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;

public class SecurityPolicyDeploymentValidationHandler implements PolicyValidationHandler {
   public boolean validate(String var1, PolicyStatement var2) throws PolicyException {
      PolicyAlternative var3 = var2.normalize().getPolicyAlternative();
      if (var3 != null && !var3.isEmpty()) {
         this.validateIdentityAssertion(var1, var3);
         this.validateIntegrityAssertion(var3);
         return true;
      } else {
         throw new PolicyException("\"" + var1 + "\" does not contain any assertions");
      }
   }

   private void validateIntegrityAssertion(PolicyAlternative var1) throws PolicyException {
      Set var2 = var1.getAssertions(IntegrityAssertion.class);
      Iterator var3 = var2.iterator();

      while(true) {
         SupportedTokensType var5;
         do {
            if (!var3.hasNext()) {
               return;
            }

            IntegrityAssertion var4 = (IntegrityAssertion)var3.next();
            var5 = var4.getXbean().getIntegrity().getSupportedTokens();
         } while(var5 == null);

         SecurityTokenType[] var6 = var5.getSecurityTokenArray();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7].getTokenType().equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID")) {
               validateSAMLTokenType(var6[var7]);
            }
         }
      }
   }

   private void validateIdentityAssertion(String var1, PolicyAlternative var2) throws PolicyException {
      Set var3 = var2.getAssertions(IdentityAssertion.class);
      Iterator var4 = var3.iterator();

      while(true) {
         SupportedTokensType var7;
         do {
            if (!var4.hasNext()) {
               return;
            }

            IdentityAssertion var5 = (IdentityAssertion)var4.next();
            IdentityDocument.Identity var6 = var5.getXbean().getIdentity();
            var7 = var6.getSupportedTokens();
         } while(var7 == null);

         SecurityTokenType[] var8 = var7.getSecurityTokenArray();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            if (var8[var9].getTokenType().equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID")) {
               validateSAMLTokenType(var8[var9]);
            } else if (var8[var9].getTokenType().equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken")) {
               validateUsernameTokenType(var1, var8[var9]);
            }
         }
      }
   }

   private static void validateUsernameTokenType(String var0, SecurityTokenType var1) throws PolicyException {
      if (KernelStatus.isServer()) {
         Element var2 = (Element)var1.newDomNode().getFirstChild();
         NodeList var3 = var2.getElementsByTagNameNS("http://www.bea.com/wls90/security/policy", WSSConstants.POLICY_USE_PASSWD_QNAME.getLocalPart());
         if (var3 != null && var3.getLength() > 0) {
            String var4 = DOMUtils.getAttributeValueAsString((Element)var3.item(0), WSSConstants.TYPE_QNAME);
            if (var4 == null || var4.length() == 0) {
               throw new PolicyException(var0 + " is not valid: " + "'Type' attribute of 'UsePassword' is not specified.");
            }

            if (var4.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest") && !SecurityUtils.isPasswordDigestSupported()) {
               throw new PolicyException(var0 + " is not valid: " + " server is not configured to support Password Digest. Specify 'PasswordText' instead.");
            }
         }

      }
   }

   private static void validateSAMLTokenType(SecurityTokenType var0) throws PolicyException {
      Element var1 = (Element)var0.newDomNode().getFirstChild();
      if (var1 == null) {
         throw new PolicyException("Claims of SAML token must not be null");
      } else {
         String var2 = ClaimsBuilder.getClaimFromElt(var1, (QName)SAMLConstants.CONFIRMATION_METHOD_QNAME);
         if (var2 == null) {
            throw new PolicyException("ConfirmationMethod of saml token is not specified.");
         } else if (!var2.equals("holder-of-key") && !var2.equals("sender-vouches")) {
            throw new PolicyException(var2 + " is not a valid subject confirmation method");
         }
      }
   }
}
