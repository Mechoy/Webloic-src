package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RecipientSignatureToken extends AsymmetricToken {
   public static final String RECIPIENT_SIGNATURE_TOKEN = "RecipientSignatureToken";

   public QName getName() {
      return new QName(this.getNamespace(), "RecipientSignatureToken", "sp");
   }
}
