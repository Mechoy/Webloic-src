package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class IncludeTimestamp extends QNameAssertion {
   public static final String INCLUDE_TIMESTAMP = "IncludeTimestamp";

   public QName getName() {
      return new QName(this.getNamespace(), "IncludeTimestamp", "sp");
   }
}
