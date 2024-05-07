package weblogic.wsee.security.policy.assertions;

import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyAssertion;

public abstract class SecurityPolicyAssertion extends PolicyAssertion {
   protected static final HashMap prefixesMap = new HashMap();

   public static Element getElement(XmlObject var0) {
      XmlOptions var1 = new XmlOptions();
      var1.setSaveSuggestedPrefixes(prefixesMap);
      Node var2 = var0.newDomNode(var1).getFirstChild();
      if (var2.getNodeType() != 1) {
         throw new AssertionError();
      } else {
         return (Element)var2;
      }
   }

   static {
      prefixesMap.put("http://schemas.xmlsoap.org/ws/2004/09/policy", "wsp");
      prefixesMap.put("http://www.bea.com/wls90/security/policy", "wssp");
      prefixesMap.put("http://www.w3.org/ns/ws-policy", "wsp15");
   }
}
