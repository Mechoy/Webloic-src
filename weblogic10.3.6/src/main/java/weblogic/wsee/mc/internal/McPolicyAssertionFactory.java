package weblogic.wsee.mc.internal;

import org.w3c.dom.Node;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;

public class McPolicyAssertionFactory extends PolicyAssertionFactory {
   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      return isMcPolicyAssertion(var1) ? new MCSupported() : null;
   }

   public static boolean isMcPolicyAssertion(Node var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.getNamespaceURI();
         return var1 != null && var1.equals(McConstants.McVersion.MC_11.getNamespaceUri()) && var0.getLocalName().equals(McConstants.Element.MC_SUPPORTED.getElementName());
      }
   }

   static {
      ExternalizationUtils.registerExternalizable(MCSupported.MC_QNAME, MCSupported.class.getName());
   }
}
