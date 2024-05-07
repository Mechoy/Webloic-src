package weblogic.wsee.security.policy12.assertions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public abstract class QNameAssertion extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = -6357191830568150589L;

   void initAssertion(Element var1) throws PolicyException {
      String var2 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
      if (var2 != null) {
         this.setPolicyNamespaceUri(var2);
         this.setOptional(true);
      }

   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      return var2;
   }
}
