package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class ScopePolicy15 extends QNameAssertion {
   public static final String SCOPE_POLICY15 = "ScopePolicy15";

   public QName getName() {
      return new QName(this.getNamespace(), "ScopePolicy15", "sp13");
   }
}
