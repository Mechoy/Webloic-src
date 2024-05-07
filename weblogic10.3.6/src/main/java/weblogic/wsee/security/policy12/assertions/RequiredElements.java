package weblogic.wsee.security.policy12.assertions;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import weblogic.wsee.security.wss.plan.helper.XpathNodesHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;

public class RequiredElements extends XPathElements {
   public static final String REQUIRED_ELEMENTS = "RequiredElements";

   public boolean isRequired() {
      return true;
   }

   public QName getName() {
      return new QName(this.getNamespace(), "RequiredElements", "sp");
   }

   public static void isValidElement(List<Node> var0) throws SecurityPolicyArchitectureException {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Node var2 = (Node)var0.get(var1);
         isValidElement(var2);
      }

   }

   public static void isValidElement(Node var0) throws SecurityPolicyArchitectureException {
      String var1 = XpathNodesHelper.getElementPath(var0);
      if (var1 != null) {
         if (!var1.startsWith("/http://schemas.xmlsoap.org/soap/envelope/:Envelope/") && !var1.startsWith("/http://www.w3.org/2003/05/soap-envelope:Envelope/")) {
            throw new SecurityPolicyArchitectureException("Error validating RequiredElement assertion for element <" + var1 + ">: nodelist does not contain a SOAP Envelope element");
         }
      }
   }
}
