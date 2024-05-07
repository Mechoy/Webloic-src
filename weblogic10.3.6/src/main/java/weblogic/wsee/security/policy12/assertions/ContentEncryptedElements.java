package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class ContentEncryptedElements extends EncryptedElements {
   public static final String CONTENT_ENCRYPTED_ELEMENTS = "ContentEncryptedElements";

   public boolean isRequired() {
      return false;
   }

   public QName getName() {
      return new QName(this.getNamespace(), "ContentEncryptedElements", "sp");
   }
}
