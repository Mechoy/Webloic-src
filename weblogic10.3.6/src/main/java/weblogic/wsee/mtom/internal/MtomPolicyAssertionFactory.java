package weblogic.wsee.mtom.internal;

import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;

public class MtomPolicyAssertionFactory extends PolicyAssertionFactory {
   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      String var2 = var1.getNamespaceURI();
      return var2 != null && (var2.equals("http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization") || var2.equals("http://www.w3.org/ns/ws-policy/optimizedmimeserialization")) && var1.getLocalName().equals("OptimizedMimeSerialization") ? new OptimizedMimeSerialization() : null;
   }

   static {
      ExternalizationUtils.registerExternalizable(OptimizedMimeSerialization.MTOM_QNAME, OptimizedMimeSerialization.class.getName());
   }
}
