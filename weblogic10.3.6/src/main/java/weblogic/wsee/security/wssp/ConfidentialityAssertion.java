package weblogic.wsee.security.wssp;

import java.util.List;
import weblogic.wsee.security.policy12.assertions.ContentEncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;

public interface ConfidentialityAssertion {
   boolean isEncryptedBodyRequired();

   boolean isEncryptedBodyOptional();

   boolean isEncryptedHeaderRequired();

   List<String> getEncryptingElements();

   String getXPathVersion();

   List<QNameExpr> getEncryptingParts();

   EncryptedElements getEncryptedElementsPolicy();

   ContentEncryptedElements getContentEncryptedElementsPolicy();
}
