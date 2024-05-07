package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509PkiPathV1Token11 extends QNameAssertion {
   public static final String WSS_X509_PKI_PATHV1_TOKEN11 = "WssX509PkiPathV1Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509PkiPathV1Token11", "sp");
   }
}
