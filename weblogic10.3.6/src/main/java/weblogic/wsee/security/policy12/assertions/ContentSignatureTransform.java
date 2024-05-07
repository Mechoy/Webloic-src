package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class ContentSignatureTransform extends QNameAssertion {
   public static final String CONTENT_SIGNATURE_TRANSFORM = "ContentSignatureTransform";

   public QName getName() {
      return new QName(this.getNamespace(), "ContentSignatureTransform", "sp13");
   }
}
