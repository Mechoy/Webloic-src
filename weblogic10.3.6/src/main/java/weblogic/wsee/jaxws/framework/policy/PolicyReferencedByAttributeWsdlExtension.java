package weblogic.wsee.jaxws.framework.policy;

import java.net.URISyntaxException;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyReferencedByAttributeWsdlExtension implements WsdlExtension {
   private PolicyURIs policyUri = null;

   public PolicyReferencedByAttributeWsdlExtension(String var1) throws URISyntaxException {
      this.policyUri = new PolicyURIs(var1);
   }

   public PolicyURIs getPolicyUri() {
      return this.policyUri;
   }

   public String getKey() {
      return "PolicyURIs";
   }

   public void write(Element var1, WsdlWriter var2) {
   }
}
