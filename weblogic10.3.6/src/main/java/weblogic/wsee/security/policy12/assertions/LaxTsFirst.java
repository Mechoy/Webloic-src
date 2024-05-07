package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class LaxTsFirst extends QNameAssertion {
   public static final String LAX_TS_FIRST = "LaxTsFirst";

   public QName getName() {
      return new QName(this.getNamespace(), "LaxTsFirst", "sp");
   }
}
