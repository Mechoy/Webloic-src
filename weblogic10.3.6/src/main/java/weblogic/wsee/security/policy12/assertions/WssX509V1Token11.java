package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509V1Token11 extends QNameAssertion {
   public static final String WSS_X509V1_TOKEN11 = "WssX509V1Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509V1Token11", "sp");
   }
}
