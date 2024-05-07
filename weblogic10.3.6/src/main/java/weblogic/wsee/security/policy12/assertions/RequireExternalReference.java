package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireExternalReference extends QNameAssertion {
   private static final long serialVersionUID = -9024337014885296094L;
   public static final String REQUIRE_EXTERNAL_REFERENCE = "RequireExternalReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireExternalReference", "sp");
   }
}
