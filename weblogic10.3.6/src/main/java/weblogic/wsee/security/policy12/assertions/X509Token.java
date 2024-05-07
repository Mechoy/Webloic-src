package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class X509Token extends Token {
   public static final String X509_TOKEN = "X509Token";

   public QName getName() {
      return new QName(this.getNamespace(), "X509Token", "sp");
   }

   public RequireKeyIdentifierReference getRequireKeyIdentifierReference() {
      return (RequireKeyIdentifierReference)this.getNestedAssertion(RequireKeyIdentifierReference.class);
   }

   public RequireIssuerSerialReference getRequireIssuerSerialReference() {
      return (RequireIssuerSerialReference)this.getNestedAssertion(RequireIssuerSerialReference.class);
   }

   public RequireEmbeddedTokenReference getRequireEmbeddedTokenReference() {
      return (RequireEmbeddedTokenReference)this.getNestedAssertion(RequireEmbeddedTokenReference.class);
   }

   public RequireThumbprintReference getRequireThumbprintReference() {
      return (RequireThumbprintReference)this.getNestedAssertion(RequireThumbprintReference.class);
   }

   public WssX509V3Token10 getWssX509V3Token10() {
      return (WssX509V3Token10)this.getNestedAssertion(WssX509V3Token10.class);
   }

   public WssX509Pkcs7Token10 getWssX509Pkcs7Token10() {
      return (WssX509Pkcs7Token10)this.getNestedAssertion(WssX509Pkcs7Token10.class);
   }

   public WssX509PkiPathV1Token10 getWssX509PkiPathV1Token10() {
      return (WssX509PkiPathV1Token10)this.getNestedAssertion(WssX509PkiPathV1Token10.class);
   }

   public WssX509V1Token11 getWssX509V1Token11() {
      return (WssX509V1Token11)this.getNestedAssertion(WssX509V1Token11.class);
   }

   public WssX509V3Token11 getWssX509V3Token11() {
      return (WssX509V3Token11)this.getNestedAssertion(WssX509V3Token11.class);
   }

   public WssX509Pkcs7Token11 getWssX509Pkcs7Token11() {
      return (WssX509Pkcs7Token11)this.getNestedAssertion(WssX509Pkcs7Token11.class);
   }

   public WssX509PkiPathV1Token11 getWssX509PkiPathV1Token11() {
      return (WssX509PkiPathV1Token11)this.getNestedAssertion(WssX509PkiPathV1Token11.class);
   }
}
