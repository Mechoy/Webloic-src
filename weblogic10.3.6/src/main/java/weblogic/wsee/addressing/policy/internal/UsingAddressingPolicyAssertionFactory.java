package weblogic.wsee.addressing.policy.internal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class UsingAddressingPolicyAssertionFactory extends PolicyAssertionFactory {
   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      String var2 = var1.getNamespaceURI();
      if (var2 == null) {
         return null;
      } else {
         WSAUsingAddressingAssertion var3 = new WSAUsingAddressingAssertion();
         if (DOMUtils.equalsQName(var1, WSAddressingConstants.WSAW_QNAME)) {
            var3.setName(WSAddressingConstants.WSAW_QNAME);
         } else if (!DOMUtils.equalsQName(var1, WSAddressingConstants.WSAW_QNAME_10)) {
            return null;
         }

         Boolean var4 = DOMUtils.getAttributeValueAsBoolean((Element)var1, WSAddressingConstants.WSAW_ATT_QNAME);
         if (var4 != null && var4) {
            var3.setRequired(true);
         }

         return var3;
      }
   }

   static {
      ExternalizationUtils.registerExternalizable(WSAddressingConstants.WSAW_QNAME, WSAUsingAddressingAssertion.class.getName());
      ExternalizationUtils.registerExternalizable(WSAddressingConstants.WSAW_QNAME_10, WSAUsingAddressingAssertion.class.getName());
   }
}
