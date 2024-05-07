package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EncryptionToken extends ProtectionToken {
   public static final String ENCRYPTION_TOKEN = "EncryptionToken";

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptionToken", "sp");
   }
}
