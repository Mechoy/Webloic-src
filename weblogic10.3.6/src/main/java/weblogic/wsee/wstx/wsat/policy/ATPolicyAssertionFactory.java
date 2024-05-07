package weblogic.wsee.wstx.wsat.policy;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public class ATPolicyAssertionFactory extends PolicyAssertionFactory {
   public PolicyAssertion createAssertion(Node var1) throws PolicyException {
      if (var1 == null) {
         return null;
      } else {
         assert var1.getNodeType() == 1;

         if (!"ATAssertion".equals(var1.getLocalName())) {
            return null;
         } else {
            String var2 = var1.getNamespaceURI();
            if (!"http://schemas.xmlsoap.org/ws/2004/10/wsat".equals(var2) && !"http://docs.oasis-open.org/ws-tx/wsat/2006/06".equals(var2)) {
               return null;
            } else {
               ATAssertion var3 = new ATAssertion();
               if ("http://docs.oasis-open.org/ws-tx/wsat/2006/06".equals(var2)) {
                  var3.setName(ATPolicyConstants.AT12_QNAME);
               }

               Boolean var4 = PolicyHelper.getOptionalBoolean((Element)var1);
               if (var4 != null) {
                  var3.setOptional(var4);
               }

               return var3;
            }
         }
      }
   }

   public static boolean isATPolicyAssertion(Node var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.getNamespaceURI();
         return ("http://schemas.xmlsoap.org/ws/2004/10/wsat".equals(var1) || "http://docs.oasis-open.org/ws-tx/wsat/2006/06".equals(var1)) && var0.getLocalName().equals("ATAssertion");
      }
   }

   static {
      registerAssertion(ATPolicyConstants.AT10_QNAME, ATPolicyConstants.class.getName());
      registerAssertion(ATPolicyConstants.AT12_QNAME, ATPolicyConstants.class.getName());
   }
}
