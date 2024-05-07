package weblogic.wsee.security.wss.plan.fact;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.XBeanUtils;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.wsee.security.saml.SAMLIssuedTokenHelper;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.security.wss.plan.helper.TokenTypeHelper;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssc.dk.DKClaims;
import weblogic.wsee.security.wssp.AlgorithmSuiteInfo;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wssp.KerberosTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;

public class SecurityTokenFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityTokenFactory.class);
   private static final boolean debug = false;
   public static final String KERBEROS_TOKEN_PROFILE_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1";
   public static final String KERBEROS_TOKEN_PROFILE_V5_AP_REQ_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1#Kerberosv5_AP_REQ";
   public static final String KERBEROS_TOKEN_PROFILE_GSS_V5_AP_REQ_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1#GSS_Kerberosv5_AP_REQ";

   public static SecurityToken makeSecurityToken(SecurityTokenType var0) {
      if (null == var0) {
         return null;
      } else {
         SecurityToken var1 = new SecurityToken(XBeanUtils.getElement(var0), (String)null, var0.getTokenType(), var0.getIncludeInMessage());
         var1.setDerivedFromTokenType(var0.getDerivedFromTokenType());
         return var1;
      }
   }

   private static void setIssuerAndOptional(SecurityToken var0, TokenAssertion var1) {
      if (var1.getIssuer() != null) {
         var0.setTokenIssuer(var1.getIssuer());
      } else {
         var0.setIssuerName(var1.getIssuerName());
      }

      var0.setOptional(var1.isOptional());
   }

   public static SecurityToken makeSecurityTokenForSignature(X509TokenAssertion var0, boolean var1, GeneralPolicy var2) throws SecurityPolicyArchitectureException {
      SecurityToken var3 = makeSecurityTokenInternal(var0, var1, var2);
      TokenReferenceTypeHelper var4 = new TokenReferenceTypeHelper(var2, var0);
      if (var1) {
         var3.setStrTypes(var4.getSTRTypeListForSignature(var3.getTokenTypeUri()));
      } else {
         var3.setStrTypes(var4.getSTRTypeList(var3.getTokenTypeUri()));
      }

      return var3;
   }

   public static SecurityToken makeSecurityTokenInternal(X509TokenAssertion var0, boolean var1, GeneralPolicy var2) throws SecurityPolicyArchitectureException {
      SecurityToken var3 = makeSecurityToken(SecurityTokenTypeFactory.makeSecurityTokenType(var0.getX509TokenType(), var1));
      setIssuerAndOptional(var3, var0);
      return var3;
   }

   public static SecurityToken makeSecurityToken(X509TokenAssertion var0, boolean var1, GeneralPolicy var2) throws SecurityPolicyArchitectureException {
      SecurityToken var3 = makeSecurityTokenInternal(var0, var1, var2);
      TokenReferenceTypeHelper var4 = new TokenReferenceTypeHelper(var2, var0);
      var3.setStrTypes(var4.getSTRTypeList(var3.getTokenTypeUri()));
      return var3;
   }

   public static SecurityToken makeSecurityTokenForSignatureResponse(X509TokenAssertion var0, boolean var1, GeneralPolicy var2, boolean var3) throws SecurityPolicyArchitectureException {
      SecurityToken var4 = makeSecurityTokenInternal(var0, var1, var2);
      TokenReferenceTypeHelper var5 = new TokenReferenceTypeHelper(var2, var0);
      if (var3) {
         var4.setStrTypes(var5.getSTRTypeListForValidation(var4.getTokenTypeUri()));
      } else if (var1) {
         var4.setStrTypes(var5.getSTRTypeListForSignature(var4.getTokenTypeUri()));
      } else {
         var4.setStrTypes(var5.getSTRTypeList(var4.getTokenTypeUri()));
      }

      return var4;
   }

   public static SecurityToken makeSecurityToken(UsernameTokenAssertion var0, boolean var1) throws SecurityPolicyArchitectureException {
      SecurityToken var2 = makeSecurityToken(SecurityTokenTypeFactory.makeSecurityTokenType(var0, var1));
      setIssuerAndOptional(var2, var0);
      return var2;
   }

   public static SecurityToken makeSecurityToken(SamlTokenAssertion var0, boolean var1, GeneralPolicy var2) throws SecurityPolicyArchitectureException {
      String var3 = TokenTypeHelper.getSamlConfirmationMethod(var0);
      SecurityToken var4 = makeSecurityToken(SecurityTokenTypeFactory.makeSecurityTokenType(var0.getSamlTokenType(), var1, var3));
      var4.setStrTypes(TokenReferenceTypeHelper.getSTRTypesForSAML(var0.getSamlTokenType()));
      setIssuerAndOptional(var4, var0);
      return var4;
   }

   public static SecurityToken makeSecurityToken(KerberosTokenAssertion var0, boolean var1, GeneralPolicy var2) throws SecurityPolicyArchitectureException {
      String var3 = null;
      if (var0.requireDerivedKey()) {
         if (var0.requireExplicitDerivedKey()) {
            var3 = "Explicit";
         } else {
            var3 = "Implicit";
         }
      }

      SecurityToken var4 = makeSecurityToken(SecurityTokenTypeFactory.makeSecurityTokenType("http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1", var3, var1));
      TokenReferenceTypeHelper var5 = new TokenReferenceTypeHelper(var2, var0);
      var4.setStrTypes(var5.getSTRTypeList(var4.getTokenTypeUri()));
      setIssuerAndOptional(var4, var0);
      return var4;
   }

   public static SecurityToken makeSecurityToken(SecureConversationTokenAssertion var0, boolean var1, GeneralPolicy var2, AlgorithmSuiteInfo var3) throws SecurityPolicyArchitectureException {
      SecurityToken var4 = new SecurityToken();
      var4.setBootstrapPolicy(var0.getNormalizedBootstrapPolicy());
      if (var0.requireDerivedKey()) {
         var4.setTokenTypeUri(var0.getDkTokenType());
         var4.setDerivedFromTokenType(var0.getSctTokenType());
         var4.setIncludeInMessage(true);
         var4.setIncludeDerivedFromInMessage(var1);
         Node var5 = DKClaims.makeDKClaimsNode(var2, (String)null, var3);
         var4.setClaims(var5);
      } else {
         var4.setTokenTypeUri(var0.getSctTokenType());
         var4.setIncludeInMessage(var1);
      }

      TokenReferenceTypeHelper var6 = new TokenReferenceTypeHelper(var2, var0);
      var4.setStrTypes(var6.getSTRTypeList(var4.getTokenTypeUri()));
      setIssuerAndOptional(var4, var0);
      return var4;
   }

   public static SecurityToken makeSecurityToken(IssuedTokenAssertion var0, boolean var1, GeneralPolicy var2, AlgorithmSuiteInfo var3, SamlTokenAssertion.ConfirmationMethod var4) throws SecurityPolicyArchitectureException {
      SecurityToken var5 = new SecurityToken();
      String var6 = SAMLIssuedTokenHelper.getTrustVersionFromPolicy(var2);
      String var7 = var4.toString();
      boolean var9 = var0.requireDerivedKey();
      Object var8;
      if (var9) {
         if (verbose) {
            Verbose.log((Object)("The IssuedToken requireDerivedKey() = " + var9 + " setting DerivedFromTokenType = " + var0.getIssuedTokenType()));
         }

         var5.setTokenTypeUri(var0.getDkTokenType());
         var5.setDerivedFromTokenType(var0.getIssuedTokenType());
         var5.setIncludeInMessage(true);
         var5.setIncludeDerivedFromInMessage(var1);
         var8 = DKClaims.makeDKClaimsNode(var2, (String)null, var3);
         var5.setStrTypesForDKBaseToken(var5.getStrTypes());
         var5.setStrTypes(TokenReferenceTypeHelper.getSTRTypesForDK(var5.getTokenTypeUri()));
      } else {
         var5.setStrTypes(TokenReferenceTypeHelper.getSTRTypesForSAMLIssuedToken(var0.getIssuedTokenType()));
         var5.setTokenTypeUri(var0.getIssuedTokenType());
         var5.setIncludeInMessage(var1);
         var8 = DKClaims.makeClaimsNode();
      }

      Element var10 = SAMLIssuedTokenHelper.makeIssuedTokenClaimElement(var0, (Node)var8, var6);
      ((Node)var8).appendChild(var10);
      var5.setClaims((Node)var8);
      if ("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer".equals(SAMLIssuedTokenHelper.getTrustKeyTypeFromIssuedTokenClaims((Node)var8)) && !SamlTokenAssertion.ConfirmationMethod.BEARER.toString().equals(var7)) {
         if (verbose) {
            Verbose.log((Object)("Changed the subject confirmation method from " + var7 + " to " + SamlTokenAssertion.ConfirmationMethod.BEARER.toString()));
         }

         var7 = SamlTokenAssertion.ConfirmationMethod.BEARER.toString();
      }

      Element var11 = DOMUtils.createAndAddElement((Element)var8, SAMLConstants.CONFIRMATION_METHOD_QNAME, ((Node)var8).getPrefix());
      DOMUtils.addText(var11, var7);
      setIssuerAndOptional(var5, var0);
      return var5;
   }
}
