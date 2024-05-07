package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssSamlV20Token11 extends QNameAssertion {
   public static final String WSS_SAML_V20_TOKEN11 = "WssSamlV20Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssSamlV20Token11", "sp");
   }
}
