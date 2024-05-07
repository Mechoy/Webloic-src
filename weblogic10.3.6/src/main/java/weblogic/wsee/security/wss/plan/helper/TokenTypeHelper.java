package weblogic.wsee.security.wss.plan.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;
import weblogic.wsee.util.Verbose;

public class TokenTypeHelper {
   private static final boolean verbose = Verbose.isVerbose(TokenTypeHelper.class);
   private static final boolean debug = false;
   private static final Map samlTokenMap = new HashMap();
   private static final Map samlConfirmationMethodMap;

   public static String getUsernameTokenType(UsernameTokenAssertion var0) {
      if (var0.getUsernameTokenType() == UsernameTokenAssertion.TokenType.WSS_UT_10) {
         return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
      } else if (var0.getUsernameTokenType() == UsernameTokenAssertion.TokenType.WSS_UT_11) {
         return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
      } else {
         throw new UnsupportedOperationException("Unkown Username Token Type found.");
      }
   }

   public static String getSamlValueTokenType(SamlTokenAssertion var0) {
      SamlTokenAssertion.TokenType var1 = var0.getSamlTokenType();
      return getSamlValueTokenType(var1);
   }

   public static String getSamlValueTokenType(SamlTokenAssertion.TokenType var0) {
      return (String)samlTokenMap.get(var0);
   }

   public static String getSamlConfirmationMethod(SamlTokenAssertion var0) {
      SamlTokenAssertion.ConfirmationMethod var1 = var0.getSubjectConfirmationMethod();
      return (String)samlConfirmationMethodMap.get(var1);
   }

   public static boolean isSamlValueType(String var0) {
      HashSet var1 = new HashSet(samlTokenMap.values());
      return var1.contains(var0);
   }

   static {
      samlTokenMap.put(SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_10, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID");
      samlTokenMap.put(SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_11, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1");
      samlTokenMap.put(SamlTokenAssertion.TokenType.WSS_SAML_V20_TOKEN_11, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");
      samlConfirmationMethodMap = new HashMap();
      samlConfirmationMethodMap.put(SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES, "sender-vouches");
      samlConfirmationMethodMap.put(SamlTokenAssertion.ConfirmationMethod.HOLDER_OF_KEY, "holder-of-key");
      samlConfirmationMethodMap.put(SamlTokenAssertion.ConfirmationMethod.BEARER, "bearer");
   }
}
