package weblogic.wsee.security.wssp;

public interface X509TokenAssertion extends TokenAssertion {
   boolean isKeyIdentifierReferenceRequired();

   boolean isIssuerSerialReferenceRequired();

   boolean isEmbeddedTokenReferenceRequired();

   boolean isThumbprintReferenceRequired();

   TokenType getX509TokenType();

   String[] getDerivedKeyTokenType(boolean var1);

   public static enum TokenType {
      WSS_X509_V3_TOKEN_10,
      WSS_X509_PKCS7_TOKEN_10,
      WSS_X509_PKIPATH_V1_TOKEN_10,
      WSS_X509_V1_TOKEN_11,
      WSS_X509_V3_TOKEN_11,
      WSS_X509_PKCS7_TOKEN_11,
      WSS_X509_PKIPATH_V1_TOKEN_11;
   }
}
