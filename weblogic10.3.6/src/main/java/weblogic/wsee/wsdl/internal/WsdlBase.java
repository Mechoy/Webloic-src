package weblogic.wsee.wsdl.internal;

import java.net.URISyntaxException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.wsdl.WsdlDocumentation;
import weblogic.wsee.wsdl.WsdlElement;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;

public class WsdlBase implements WsdlElement {
   private WsdlDocumentationImpl info = null;

   public void setDocumentation(WsdlDocumentationImpl var1) {
      this.info = var1;
   }

   public WsdlDocumentation getDocumentation() {
      return this.info;
   }

   public void writeDocumentation(Element var1, WsdlWriter var2) {
      if (this.info != null) {
         this.info.write(var1, var2);
      }

   }

   public Element getFirstChildElement(Element var1) {
      Node var2;
      for(var2 = var1.getFirstChild(); var2 != null && var2.getNodeType() != 1; var2 = var2.getNextSibling()) {
      }

      return (Element)var2;
   }

   public void addDocumentation(Element var1) throws WsdlException {
      Element var2 = this.getFirstChildElement(var1);
      if (var2 != null && "documentation".equals(var2.getLocalName())) {
         WsdlDocumentationImpl var3 = new WsdlDocumentationImpl();
         var3.parse(var2, (String)null);
         this.setDocumentation(var3);
      } else {
         this.setDocumentation((WsdlDocumentationImpl)null);
      }

   }

   protected PolicyURIs getPolicyUri(Element var1) throws WsdlException {
      String var2 = WsdlReader.getAttribute(var1, "http://schemas.xmlsoap.org/ws/2004/09/policy", "PolicyURIs");
      if (null == var2) {
         var2 = WsdlReader.getAttribute(var1, "http://www.w3.org/ns/ws-policy", "PolicyURIs");
      }

      if (var2 != null) {
         try {
            return new PolicyURIs(var2);
         } catch (URISyntaxException var4) {
            throw new WsdlException(var4.getMessage());
         }
      } else {
         return null;
      }
   }
}
