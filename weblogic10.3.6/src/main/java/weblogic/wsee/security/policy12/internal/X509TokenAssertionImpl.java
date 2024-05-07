package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.X509Token;
import weblogic.wsee.security.wssp.X509TokenAssertion;

public class X509TokenAssertionImpl extends TokenAssertionImpl implements X509TokenAssertion {
   private boolean isKeyIdentifierReferenceRequired;
   private boolean isIssuerSerialReferenceRequired;
   private boolean isEmbeddedTokenReferenceRequired;
   private boolean isThumbprintReferenceRequired;
   private boolean isThumbprintReferenceOptional;
   private X509TokenAssertion.TokenType tokenType;
   private String namespaceURI;
   private static final String DK_TOKEN_TYPE_V200502 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   private static final String DK_TOKEN_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   private static final String[] DK_TOKEN_TYPES = new String[]{"http://schemas.xmlsoap.org/ws/2005/02/sc/dk"};
   private static final String[] DK_TOKEN_TYPES_13 = new String[]{"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk", "http://schemas.xmlsoap.org/ws/2005/02/sc/dk"};

   X509TokenAssertionImpl(X509Token var1) {
      super(var1);
      this.tokenType = X509TokenAssertion.TokenType.WSS_X509_V3_TOKEN_10;
      this.namespaceURI = var1.getName().getNamespaceURI();
      this.isKeyIdentifierReferenceRequired = var1.getRequireKeyIdentifierReference() != null;
      this.isIssuerSerialReferenceRequired = var1.getRequireIssuerSerialReference() != null;
      this.isEmbeddedTokenReferenceRequired = var1.getRequireEmbeddedTokenReference() != null;
      if (var1.getRequireThumbprintReference() != null) {
         this.isThumbprintReferenceRequired = true;
         this.isThumbprintReferenceOptional = var1.getRequireThumbprintReference().isOptional();
      }

      this.initTokenType(var1);
   }

   private void initTokenType(X509Token var1) {
      if (var1.getWssX509V3Token10() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_V3_TOKEN_10;
      } else if (var1.getWssX509V3Token11() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_V3_TOKEN_11;
      } else if (var1.getWssX509Pkcs7Token10() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_PKCS7_TOKEN_10;
      } else if (var1.getWssX509Pkcs7Token11() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_PKCS7_TOKEN_11;
      } else if (var1.getWssX509PkiPathV1Token10() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_PKIPATH_V1_TOKEN_10;
      } else if (var1.getWssX509PkiPathV1Token11() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_PKIPATH_V1_TOKEN_11;
      } else if (var1.getWssX509V1Token11() != null) {
         this.tokenType = X509TokenAssertion.TokenType.WSS_X509_V1_TOKEN_11;
      }

   }

   public boolean isKeyIdentifierReferenceRequired() {
      return this.isKeyIdentifierReferenceRequired;
   }

   public boolean isIssuerSerialReferenceRequired() {
      return this.isIssuerSerialReferenceRequired;
   }

   public boolean isEmbeddedTokenReferenceRequired() {
      return this.isEmbeddedTokenReferenceRequired;
   }

   public boolean isThumbprintReferenceRequired() {
      return this.isThumbprintReferenceRequired;
   }

   public boolean isThumbprintReferenceOptional() {
      return this.isThumbprintReferenceOptional;
   }

   public X509TokenAssertion.TokenType getX509TokenType() {
      return this.tokenType;
   }

   public String[] getDerivedKeyTokenType(boolean var1) {
      if (var1) {
         return DK_TOKEN_TYPES_13;
      } else {
         return null != this.namespaceURI && "http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702".equals(this.namespaceURI) ? DK_TOKEN_TYPES_13 : DK_TOKEN_TYPES;
      }
   }
}
