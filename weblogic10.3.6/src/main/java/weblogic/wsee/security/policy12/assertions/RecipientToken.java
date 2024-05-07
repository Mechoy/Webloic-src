package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RecipientToken extends AsymmetricToken {
   public static final String RECIPIENT_TOKEN = "RecipientToken";

   public QName getName() {
      return new QName(this.getNamespace(), "RecipientToken", "sp");
   }
}
