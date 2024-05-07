package weblogic.wsee.policy.factory;

import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;

public class DefaultPolicyAssertionFactory extends PolicyAssertionFactory {
   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      return new DefaultPolicyAssertion(var1);
   }

   static {
      registerAssertion(DefaultPolicyAssertion.NAME, DefaultPolicyAssertion.class.getName());
   }
}
