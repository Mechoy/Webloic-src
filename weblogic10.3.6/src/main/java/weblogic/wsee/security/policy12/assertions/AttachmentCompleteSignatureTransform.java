package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class AttachmentCompleteSignatureTransform extends QNameAssertion {
   public static final String ATTACHMENT_COMPLETE_SIGNATURE_TRANSFORM = "AttachmentCompleteSignatureTransform";

   public QName getName() {
      return new QName(this.getNamespace(), "AttachmentCompleteSignatureTransform", "sp13");
   }
}
