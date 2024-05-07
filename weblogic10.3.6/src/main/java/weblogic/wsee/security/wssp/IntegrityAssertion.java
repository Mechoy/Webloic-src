package weblogic.wsee.security.wssp;

import java.util.List;
import weblogic.wsee.security.policy12.assertions.SignedElements;

public interface IntegrityAssertion {
   boolean isSignedBodyRequired();

   boolean isSignedBodyOptional();

   List<String> getSigningElements();

   String getXPathVersion();

   List<QNameExpr> getSigningParts();

   boolean isSignedWsaHeadersRequired();

   SignedElements getSignedElementsPolicy();
}
