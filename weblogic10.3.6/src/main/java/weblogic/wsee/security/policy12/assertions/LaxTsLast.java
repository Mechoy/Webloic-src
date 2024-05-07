package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class LaxTsLast extends QNameAssertion {
   public static final String LAX_TS_LAST = "LaxTsLast";

   public QName getName() {
      return new QName(this.getNamespace(), "LaxTsLast", "sp");
   }
}
