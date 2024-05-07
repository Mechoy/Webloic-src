package weblogic.wsee.security.wss.plan.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wssp.KerberosTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss11.internal.STRType;

public class TokenReferenceTypeHelper {
   private static final boolean verbose = Verbose.isVerbose(TokenReferenceTypeHelper.class);
   private static final boolean debug = false;
   private static final STRType[] STR_TYPE_MAP;
   private int requiredType = 0;
   private int mustSupportedType = 0;
   static int flag;

   public TokenReferenceTypeHelper(GeneralPolicy var1, X509TokenAssertion var2) {
      this.mustSupportedType = var1.getMustSupportedTypeCode() & -17;
      this.setRequiredType(var2);
   }

   /** @deprecated */
   public TokenReferenceTypeHelper(GeneralPolicy var1, SamlTokenAssertion var2) {
      this.mustSupportedType = var1.getMustSupportedTypeCode() & -17;
      this.setRequiredType(var2);
   }

   /** @deprecated */
   public TokenReferenceTypeHelper(GeneralPolicy var1, IssuedTokenAssertion var2) {
      this.mustSupportedType = var1.getMustSupportedTypeCode() & -17;
      this.setRequiredType(var2);
   }

   public TokenReferenceTypeHelper(GeneralPolicy var1, KerberosTokenAssertion var2) {
      this.mustSupportedType = var1.getMustSupportedTypeCode();
      if (var2.isKeyIdentifierReferenceRequired()) {
         if (var2.isWssKerberosV5ApReqToken11()) {
            this.setRequired(64);
         } else {
            this.setRequired(128);
         }
      }

   }

   public TokenReferenceTypeHelper(GeneralPolicy var1, SecureConversationTokenAssertion var2) {
      this.mustSupportedType = var1.getMustSupportedTypeCode();
      this.setRequiredType(var2);
   }

   public static List getSTRTypeList(String var0, int var1) {
      return var1 == 0 ? null : buildSTRTypeList(var1, var0, false);
   }

   public static List getSTRTypesForDK(String var0) {
      ArrayList var1 = new ArrayList();
      if ("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var0)) {
         var1.add(new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk"));
      } else {
         var1.add(new STRType(WSSConstants.REFERENCE_QNAME, "http://schemas.xmlsoap.org/ws/2005/02/sc/dk"));
      }

      return var1;
   }

   public static List getSTRTypesForSAML(SamlTokenAssertion.TokenType var0) {
      ArrayList var1 = new ArrayList();
      if (SamlTokenAssertion.TokenType.WSS_SAML_V20_TOKEN_11 == var0) {
         var1.add(new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"));
      } else if (SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_11 == var0) {
         var1.add(new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1"));
      } else if (SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_10 == var0) {
         var1.add(new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID"));
      } else {
         var1 = null;
      }

      return var1;
   }

   public static List getSTRTypesForSAMLIssuedToken(String var0) {
      ArrayList var1 = new ArrayList();
      if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var0)) {
         var1.add(new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"));
      } else if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1".equals(var0)) {
         var1.add(new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1"));
      } else if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var0)) {
         var1.add(new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID"));
      } else {
         var1 = null;
      }

      return var1;
   }

   public static boolean isThrumbprintSTRType(STRType var0) {
      return null == var0 ? false : "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var0.getValueType());
   }

   public List getSTRTypeList(String var1) {
      if (this.mustSupportedType == 0 && this.requiredType == 0) {
         return null;
      } else {
         return this.requiredType != 0 ? buildSTRTypeList(this.requiredType, var1, false) : buildSTRTypeList(this.mustSupportedType | 32, var1, false);
      }
   }

   public List getSTRTypeListForValidation(String var1) {
      return buildSTRTypeList(this.requiredType | this.mustSupportedType | 32, var1, false);
   }

   public List getSTRTypeListForSignature(String var1) {
      if (this.mustSupportedType == 0 && this.requiredType == 0) {
         return null;
      } else {
         return this.requiredType != 0 ? buildSTRTypeList(this.requiredType, var1, true) : buildSTRTypeList(this.mustSupportedType | 32, var1, true);
      }
   }

   public static String getStrTypeInfo(List var0) {
      StringBuffer var1 = new StringBuffer("Actual KeyInfo:");
      var1.append(" StrTypes size=" + var0.size() + " :");
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         STRType var3 = (STRType)var2.next();
         var1.append(var3.getTopLevelElement().toString() + "|");
         var1.append("|" + var3.getValueType() + ", ");
      }

      return var1.toString();
   }

   private static List buildSTRTypeList(int var0, String var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      if (var2 && (var0 & 32) == 32) {
         var3.add(STR_TYPE_MAP[5]);
         var0 &= -33;
      }

      for(int var4 = 0; var4 < STR_TYPE_MAP.length; ++var4) {
         if ((var0 & 1) == 1) {
            STRType var5 = STR_TYPE_MAP[var4];
            var5.setTokenType(var1);
            var3.add(STR_TYPE_MAP[var4]);
         }

         var0 >>= 1;
      }

      return var3;
   }

   public void setRequiredType(X509TokenAssertion var1) {
      if (var1 != null) {
         if (var1.isThumbprintReferenceRequired()) {
            this.setRequired(1);
         }

         if (var1.isKeyIdentifierReferenceRequired()) {
            this.setRequired(2);
         }

         if (var1.isIssuerSerialReferenceRequired()) {
            this.setRequired(4);
         }

         if (var1.isEmbeddedTokenReferenceRequired()) {
            this.setRequired(8);
         }

      }
   }

   /** @deprecated */
   public void setRequiredType(IssuedTokenAssertion var1) {
      if (var1 != null) {
         if (var1.isRequireExternalReference()) {
            this.setRequired(16);
         }

      }
   }

   /** @deprecated */
   public void setRequiredType(SamlTokenAssertion var1) {
      if (var1 != null) {
         if (var1.isKeyIdentifierReferenceRequired()) {
            this.setRequired(2);
         }

      }
   }

   public void setRequiredType(SecureConversationTokenAssertion var1) {
      if (var1 != null) {
         if (var1.isSC200502SecurityContextToken()) {
            this.setRequired(32);
         } else {
            if (!var1.isWSSC13SecurityContextToken()) {
               throw new IllegalArgumentException(" Unknown SecureConversation version, it's neither 200502 nor 1.3");
            }

            this.setRequired(32);
         }

         if (var1.isSC200502SecurityContextToken()) {
            this.setRequired(16);
         } else if (var1.isWSSC13SecurityContextToken()) {
            this.setRequired(16);
         }
      }

   }

   public void setRequired(int var1) {
      this.requiredType |= var1;
   }

   public void setRequiredType(int var1) {
      this.requiredType = var1;
   }

   public int getMustSupportedType() {
      return this.mustSupportedType;
   }

   public void setMustSupportedType(int var1) {
      this.mustSupportedType = var1;
   }

   public boolean isMustSupportRefThumbprint() {
      return (this.mustSupportedType & 1) == 1;
   }

   public boolean isMustSupportRefKeyIdentifier() {
      return (this.mustSupportedType & 2) == 2;
   }

   public boolean isMustSupportRefIssuerSerial() {
      return (this.mustSupportedType & 4) == 4;
   }

   public boolean isMustSupportRefExternalURI() {
      return (this.mustSupportedType & 16) == 16;
   }

   public boolean isMustSupportRefEmbeddedToken() {
      return (this.mustSupportedType & 8) == 8;
   }

   static {
      STR_TYPE_MAP = new STRType[]{new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1"), new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier"), new STRType(DsigConstants.X509ISSUER_SERIAL_QNAME), new STRType(WSSConstants.EMBEDDED_QNAME), new STRType(WSSConstants.REFERENCE_QNAME, "http://schemas.xmlsoap.org/ws/2005/02/sc/dk"), new STRType(WSSConstants.REFERENCE_QNAME), new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1#Kerberosv5_AP_REQ"), new STRType(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1#GSS_Kerberosv5_AP_REQ"), new STRType(WSSConstants.REFERENCE_QNAME, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk")};
      flag = 0;
   }
}
