package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509Pkcs7Token10 extends QNameAssertion {
   public static final String WSS_X509_PKCS7_TOKEN10 = "WssX509Pkcs7Token10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509Pkcs7Token10", "sp");
   }
}
