package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TransportBinding extends SecurityBinding {
   public static final String TRANSPORT_BINDING = "TransportBinding";

   public QName getName() {
      return new QName(this.getNamespace(), "TransportBinding", "sp");
   }

   public TransportToken getTransportToken() {
      return (TransportToken)this.getNestedAssertion(TransportToken.class);
   }
}
