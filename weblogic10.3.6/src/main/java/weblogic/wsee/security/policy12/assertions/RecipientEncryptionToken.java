package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RecipientEncryptionToken extends AsymmetricToken {
   public static final String RECIPIENT_ENCRYPTION_TOKEN = "RecipientEncryptionToken";

   public QName getName() {
      return new QName(this.getNamespace(), "RecipientEncryptionToken", "sp");
   }
}
