package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Wss10 extends NestedSecurityPolicy12Assertion {
   public static final String WSS_10 = "Wss10";

   public QName getName() {
      return new QName(this.getNamespace(), "Wss10", "sp");
   }

   public MustSupportRefKeyIdentifier getMustSupportRefKeyIdentifier() {
      return (MustSupportRefKeyIdentifier)this.getNestedAssertion(MustSupportRefKeyIdentifier.class);
   }

   public MustSupportRefIssuerSerial getMustSupportRefIssuerSerial() {
      return (MustSupportRefIssuerSerial)this.getNestedAssertion(MustSupportRefIssuerSerial.class);
   }

   public MustSupportRefExternalURI getMustSupportRefExternalURI() {
      return (MustSupportRefExternalURI)this.getNestedAssertion(MustSupportRefExternalURI.class);
   }

   public MustSupportRefEmbeddedToken getMustSupportRefEmbeddedToken() {
      return (MustSupportRefEmbeddedToken)this.getNestedAssertion(MustSupportRefEmbeddedToken.class);
   }
}
