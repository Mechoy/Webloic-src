package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509Pkcs7Token11 extends QNameAssertion {
   public static final String WSS_X509_PKCS7_TOKEN11 = "WssX509Pkcs7Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509Pkcs7Token11", "sp");
   }
}
