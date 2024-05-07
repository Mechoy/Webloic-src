package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssRelV20Token10 extends QNameAssertion {
   public static final String WSS_REL_V20_TOKEN10 = "WssRelV20Token10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssRelV20Token10", "sp");
   }
}
