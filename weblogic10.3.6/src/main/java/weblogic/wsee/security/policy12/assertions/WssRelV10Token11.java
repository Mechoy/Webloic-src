package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssRelV10Token11 extends QNameAssertion {
   public static final String WSS_REL_V10_TOKEN11 = "WssRelV10Token11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssRelV10Token11", "sp");
   }
}
