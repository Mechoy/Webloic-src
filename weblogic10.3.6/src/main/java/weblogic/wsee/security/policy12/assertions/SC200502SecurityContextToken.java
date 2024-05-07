package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SC200502SecurityContextToken extends QNameAssertion {
   public static final String SC200502_SECURITY_CONTEXT_TOKEN = "SC200502SecurityContextToken";

   public QName getName() {
      return new QName(this.getNamespace(), "SC200502SecurityContextToken", "sp");
   }
}
