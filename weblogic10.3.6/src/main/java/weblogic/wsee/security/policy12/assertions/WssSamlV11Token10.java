package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssSamlV11Token10 extends QNameAssertion {
   public static final String WSS_SAML_V11_TOKEN10 = "WssSamlV11Token10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssSamlV11Token10", "sp");
   }
}
