package weblogic.wsee.security.policy12.assertions;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import weblogic.wsee.security.wss.plan.helper.XpathNodesHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;

public class EncryptedElements extends XPathElements {
   public static final String ENCRYPTED_ELEMENTS = "EncryptedElements";

   public boolean isRequired() {
      return false;
   }

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptedElements", "sp");
   }

   public static void isValidElement(List<Node> var0, boolean var1) throws SecurityPolicyArchitectureException {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Node var3 = (Node)var0.get(var2);
         isValidElement(var3, var1);
      }

   }

   public static void isValidElement(Node var0, boolean var1) throws SecurityPolicyArchitectureException {
      String var2 = XpathNodesHelper.getElementPath(var0);
      if (var2 != null) {
         if (!var2.startsWith("/http://schemas.xmlsoap.org/soap/envelope/:Envelope/") && !var2.startsWith("/http://www.w3.org/2003/05/soap-envelope:Envelope/")) {
            throw new SecurityPolicyArchitectureException("Error validating EncryptedElement assertion for element <" + var2 + ">: nodelist does not contain a SOAP Envelope element");
         } else if (!var1 && (var2.compareTo("/http://schemas.xmlsoap.org/soap/envelope/:Envelope/http://schemas.xmlsoap.org/soap/envelope/:Body") == 0 || var2.compareTo("/http://www.w3.org/2003/05/soap-envelope:Envelope/http://www.w3.org/2003/05/soap-envelope:Body") == 0)) {
            throw new SecurityPolicyArchitectureException("Error validating EncryptedElement assertion for element <" + var2 + ">: EncryptedElement assertion may not encrypt the entire SOAPBody element");
         } else if (var2.compareTo("/http://schemas.xmlsoap.org/soap/envelope/:Envelope/http://schemas.xmlsoap.org/soap/envelope/:Header") != 0 && var2.compareTo("/http://www.w3.org/2003/05/soap-envelope:Envelope/http://www.w3.org/2003/05/soap-envelope:Header") != 0) {
            if ((var2.startsWith("/http://schemas.xmlsoap.org/soap/envelope/:Envelope/http://schemas.xmlsoap.org/soap/envelope/:Header") || var2.startsWith("/http://www.w3.org/2003/05/soap-envelope:Envelope/http://www.w3.org/2003/05/soap-envelope:Header")) && var0.getLocalName().compareTo("Security") == 0 && var0.getNamespaceURI().compareTo("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd") == 0) {
               throw new SecurityPolicyArchitectureException("Error validating EncryptedElement assertion for element <" + var2 + ">: the wsse:Security header may not be encrypted");
            }
         } else {
            throw new SecurityPolicyArchitectureException("Error validating EncryptedElement assertion for element <" + var2 + ">: EncryptedElement assertion may not encrypt the entire SOAPHeader element");
         }
      }
   }
}
