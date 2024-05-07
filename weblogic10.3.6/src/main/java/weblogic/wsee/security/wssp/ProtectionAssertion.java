package weblogic.wsee.security.wssp;

import java.util.List;
import weblogic.wsee.security.policy12.assertions.RequiredElements;
import weblogic.wsee.security.policy12.assertions.RequiredParts;

public interface ProtectionAssertion {
   List<String> getRequiredElements();

   RequiredElements getRequiredElementsPolicy();

   String getXPathVersion();

   List<QNameExpr> getRequiredParts();

   RequiredParts getRequiredPartsPolicy();

   boolean hasRequiredPartsPolicy();
}
