package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EncryptedParts extends QNameParts {
   public static final String ENCRYPTED_PARTS = "EncryptedParts";

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptedParts", "sp");
   }
}
