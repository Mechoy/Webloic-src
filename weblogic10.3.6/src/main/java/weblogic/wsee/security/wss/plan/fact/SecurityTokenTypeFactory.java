package weblogic.wsee.security.wss.plan.fact;

import java.io.StringReader;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.wsee.security.wss.plan.helper.TokenTypeHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SecurityPolicyBuilderConstants;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;

public class SecurityTokenTypeFactory {
   private static final boolean verbose = Verbose.isVerbose(SecurityTokenTypeFactory.class);
   private static final boolean debug = false;
   private static Stack<DocumentBuilder> pool = new Stack();
   /** @deprecated */
   public static final String DK_VALUE_TYPE_V2005 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   /** @deprecated */
   public static final String SCT_VALUE_TYPE_V2005 = "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   /** @deprecated */
   public static final String DK_VALUE_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   /** @deprecated */
   public static final String SCT_VALUE_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";

   public static SecurityTokenType makeSecurityTokenType() {
      return SecurityTokenType.Factory.newInstance();
   }

   public static SecurityTokenType makeSecurityTokenType(X509TokenAssertion.TokenType var0, boolean var1) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         return null;
      } else {
         SecurityTokenType var2 = makeSecurityTokenType();
         var2.setIncludeInMessage(var1);
         if (!var0.equals(X509TokenAssertion.TokenType.WSS_X509_V3_TOKEN_10) && !var0.equals(X509TokenAssertion.TokenType.WSS_X509_V3_TOKEN_11)) {
            if (var0.equals(X509TokenAssertion.TokenType.WSS_X509_V1_TOKEN_11)) {
               var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1");
            } else if (!var0.equals(X509TokenAssertion.TokenType.WSS_X509_PKIPATH_V1_TOKEN_10) && !var0.equals(X509TokenAssertion.TokenType.WSS_X509_PKIPATH_V1_TOKEN_11)) {
               if (!var0.equals(X509TokenAssertion.TokenType.WSS_X509_PKCS7_TOKEN_10) && !var0.equals(X509TokenAssertion.TokenType.WSS_X509_PKCS7_TOKEN_11)) {
                  throw new SecurityPolicyArchitectureException("Unsuported X509 Token Type found.");
               }

               var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7");
            } else {
               var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1");
            }
         } else {
            var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
         }

         return var2;
      }
   }

   public static SecurityTokenType makeSecurityTokenType(UsernameTokenAssertion var0, boolean var1) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         return null;
      } else {
         UsernameTokenAssertion.TokenType var2 = var0.getUsernameTokenType();
         SecurityTokenType var3 = makeSecurityTokenType(var2, var1);
         DocumentBuilder var4 = null;

         try {
            var4 = getParser();
            Document var5 = var4.parse(new InputSource(new StringReader(var3.toString())));
            Element var6 = var5.getDocumentElement();
            if (!var0.noPasswordRequried()) {
               Element var7 = DOMUtils.createAndAddElement(var6, SecurityPolicyBuilderConstants.POLICY_USE_PASSWD, SecurityPolicyBuilderConstants.POLICY_PASSWD_TYPE.getPrefix());
               if (var0.isHashPasswordRequired()) {
                  DOMUtils.addAttribute(var7, SecurityPolicyBuilderConstants.POLICY_PASSWD_TYPE, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
               } else {
                  DOMUtils.addAttribute(var7, SecurityPolicyBuilderConstants.POLICY_PASSWD_TYPE, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
               }

               var3 = SecurityTokenType.Factory.parse((Node)var6);
            }

            if (var0.requireDerivedKey()) {
               if (var0.requireExplicitDerivedKey()) {
                  var3.setDerivedFromTokenType("????");
               }

               if (var0.requireExplicitDerivedKey()) {
                  var3.setDerivedFromTokenType("????");
               }

               throw new SecurityPolicyArchitectureException("Username Token DerivedKey is not supported");
            }
         } catch (Exception var12) {
            throw new SecurityPolicyArchitectureException(var12);
         } finally {
            if (var4 != null) {
               returnParser(var4);
            }

         }

         return var3;
      }
   }

   private static SecurityTokenType makeSecurityTokenType(UsernameTokenAssertion.TokenType var0, boolean var1) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         return null;
      } else {
         SecurityTokenType var2 = makeSecurityTokenType();
         var2.setIncludeInMessage(var1);
         if (var0.equals(UsernameTokenAssertion.TokenType.WSS_UT_10)) {
            var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
         } else {
            if (!var0.equals(UsernameTokenAssertion.TokenType.WSS_UT_11)) {
               throw new SecurityPolicyArchitectureException("Unsuported Username Token Type found.");
            }

            var2.setTokenType("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
         }

         return var2;
      }
   }

   public static SecurityTokenType makeSecurityTokenType(SamlTokenAssertion.TokenType var0, boolean var1, String var2) throws SecurityPolicyArchitectureException {
      if (null == var0) {
         return null;
      } else {
         SecurityTokenType var3 = makeSecurityTokenType();
         var3.setIncludeInMessage(var1);
         var3.setTokenType(TokenTypeHelper.getSamlValueTokenType(var0));
         DocumentBuilder var4 = null;

         SecurityTokenType var9;
         try {
            var4 = getParser();
            Document var5 = var4.parse(new InputSource(new StringReader(var3.toString())));
            Element var6 = var5.getDocumentElement();
            Element var7 = DOMUtils.createAndAddElement(var6, new QName("http://www.bea.com/wls90/security/policy", "Claims"), "wssp");
            Element var8 = DOMUtils.createAndAddElement(var7, SAMLConstants.CONFIRMATION_METHOD_QNAME, var7.getPrefix());
            DOMUtils.addText(var8, var2);
            var3 = SecurityTokenType.Factory.parse((Node)var6);
            var9 = var3;
         } catch (Exception var14) {
            throw new SecurityPolicyArchitectureException(var14);
         } finally {
            if (var4 != null) {
               returnParser(var4);
            }

         }

         return var9;
      }
   }

   /** @deprecated */
   public static SecurityTokenType makeDerivedFromSecurityTokenType(boolean var0, int var1) throws SecurityPolicyArchitectureException {
      SecurityTokenType var2 = makeSecurityTokenType();
      var2.setTokenType("http://schemas.xmlsoap.org/ws/2005/02/sc/dk");
      var2.setIncludeInMessage(var0);
      var2.setDerivedFromTokenType("http://schemas.xmlsoap.org/ws/2005/02/sc/sct");
      DocumentBuilder var3 = null;

      SecurityTokenType var9;
      try {
         var3 = getParser();
         Document var4 = var3.parse(new InputSource(new StringReader(var2.toString())));
         Element var5 = var4.getDocumentElement();
         Element var6 = DOMUtils.createAndAddElement(var5, new QName("http://www.bea.com/wls90/security/policy", "Claims"), "wssp");
         Element var7 = DOMUtils.createAndAddElement(var6, new QName("http://www.bea.com/wls90/security/policy", "Label"), var6.getPrefix());
         DOMUtils.addText(var7, "WS-SecureConversationWS-SecureConversation");
         Element var8 = DOMUtils.createAndAddElement(var6, new QName("http://www.bea.com/wls90/security/policy", "Length"), var6.getPrefix());
         DOMUtils.addText(var8, "" + var1);
         var2 = SecurityTokenType.Factory.parse((Node)var5);
         var9 = var2;
      } catch (Exception var14) {
         throw new SecurityPolicyArchitectureException(var14);
      } finally {
         if (var3 != null) {
            returnParser(var3);
         }

      }

      return var9;
   }

   public static SecurityTokenType makeSecurityTokenType(String var0, String var1, boolean var2) {
      if (null == var0) {
         return null;
      } else {
         SecurityTokenType var3 = makeSecurityTokenType();
         var3.setIncludeInMessage(var2);
         var3.setTokenType(var0);
         if (null != var1) {
            var3.setDerivedFromTokenType(var1);
         }

         return var3;
      }
   }

   private static DocumentBuilder createNewParser() {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         return var0.newDocumentBuilder();
      } catch (FactoryConfigurationError var1) {
         throw new RuntimeException(var1);
      } catch (ParserConfigurationException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static DocumentBuilder getParser() {
      DocumentBuilder var0 = null;
      synchronized(pool) {
         if (pool.empty()) {
            var0 = createNewParser();
         } else {
            var0 = (DocumentBuilder)pool.pop();
         }

         return var0;
      }
   }

   private static void returnParser(DocumentBuilder var0) {
      synchronized(pool) {
         pool.push(var0);
      }
   }
}
