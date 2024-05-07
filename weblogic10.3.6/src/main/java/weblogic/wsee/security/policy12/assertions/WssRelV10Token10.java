package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssRelV10Token10 extends QNameAssertion {
   public static final String WSS_REL_V10_TOKEN10 = "WssRelV10Token10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssRelV10Token10", "sp");
   }
}
