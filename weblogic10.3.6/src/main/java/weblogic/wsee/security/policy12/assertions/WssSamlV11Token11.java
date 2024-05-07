package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssSamlV11Token11 extends QNameAssertion {
   public static final String WSS_SAML_V11_TOKEN11 = "WssSamlV11Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssSamlV11Token11", "sp");
   }
}
