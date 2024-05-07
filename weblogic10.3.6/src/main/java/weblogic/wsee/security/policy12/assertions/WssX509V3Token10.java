package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssX509V3Token10 extends QNameAssertion {
   public static final String WSS_X509V3_TOKEN10 = "WssX509V3Token10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssX509V3Token10", "sp");
   }
}
