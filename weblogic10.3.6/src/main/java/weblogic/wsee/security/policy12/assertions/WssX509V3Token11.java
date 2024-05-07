package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509V3Token11 extends QNameAssertion {
   public static final String WSS_X509V3_TOKEN11 = "WssX509V3Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509V3Token11", "sp");
   }
}
