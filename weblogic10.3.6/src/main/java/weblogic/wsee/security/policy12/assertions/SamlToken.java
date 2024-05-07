package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SamlToken extends Token {
   public static final String SAML_TOKEN = "SamlToken";

   public QName getName() {
      return new QName(this.getNamespace(), "SamlToken", "sp");
   }

   public RequireKeyIdentifierReference getRequireKeyIdentifierReference() {
      return (RequireKeyIdentifierReference)this.getNestedAssertion(RequireKeyIdentifierReference.class);
   }

   public WssSamlV11Token10 getWssSamlV11Token10() {
      return (WssSamlV11Token10)this.getNestedAssertion(WssSamlV11Token10.class);
   }

   public WssSamlV11Token11 getWssSamlV11Token11() {
      return (WssSamlV11Token11)this.getNestedAssertion(WssSamlV11Token11.class);
   }

   public WssSamlV20Token11 getWssSamlV20Token11() {
      return (WssSamlV20Token11)this.getNestedAssertion(WssSamlV20Token11.class);
   }
}
