package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SignatureToken extends ProtectionToken {
   public static final String SIGNATURE_TOKEN = "SignatureToken";

   public QName getName() {
      return new QName(this.getNamespace(), "SignatureToken", "sp");
   }
}
