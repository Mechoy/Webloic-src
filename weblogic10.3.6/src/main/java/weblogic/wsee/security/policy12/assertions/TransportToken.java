package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TransportToken extends NestedSecurityPolicy12Assertion {
   public static final String TRANSPORT_TOKEN = "TransportToken";

   public QName getName() {
      return new QName(this.getNamespace(), "TransportToken", "sp");
   }

   public HttpsToken getHttpsToken() {
      return (HttpsToken)this.getNestedAssertion(HttpsToken.class);
   }
}
