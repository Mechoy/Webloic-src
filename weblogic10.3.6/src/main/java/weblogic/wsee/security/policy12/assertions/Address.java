package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class Address extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = -3037820652713414785L;
   private QName name;
   private String address;

   public Address() {
      this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME_10;
      this.address = null;
   }

   void initAssertion(Element var1) throws PolicyException {
      if ("Address".equals(var1.getLocalName())) {
         this.address = DOMUtils.getTextContent(var1, true);
         if ("http://www.w3.org/2005/08/addressing".equals(var1.getNamespaceURI())) {
            this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME_10;
         } else if ("http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var1.getNamespaceURI())) {
            this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME;
         } else {
            this.name = new QName(var1.getNamespaceURI(), var1.getLocalName(), var1.getPrefix());
         }
      } else {
         try {
            Element var2 = DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.w3.org/2005/08/addressing", "Address");
            if (var2 != null) {
               this.address = DOMUtils.getTextContent(var2, true);
               this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME_10;
               return;
            }

            var2 = DOMUtils.getElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address");
            if (var2 != null) {
               this.address = DOMUtils.getTextContent(var2, true);
               this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME;
            }
         } catch (DOMProcessingException var3) {
            throw new PolicyException(var3);
         }
      }

      String var4 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
      if (null != var4) {
         this.setPolicyNamespaceUri(var4);
         this.setOptional(true);
      }

   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = weblogic.wsee.policy.framework.DOMUtils.createElement(this.name, var1, this.name.getPrefix());
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var2, this.getPolicyNamespaceUri());
      }

      return this.serializeAssertion(var1, var2);
   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      DOMUtils.addTextData(var2, this.address);
      return var2;
   }

   public QName getName() {
      return this.name;
   }

   public String getEndpointAddress() {
      return this.address;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.address = var1.readUTF();
      String var2 = var1.readUTF();
      if ("http://www.w3.org/2005/08/addressing".equals(var2)) {
         this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME_10;
      }

      if ("http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var2)) {
         this.name = WSAddressingConstants.ISSUE_ADDRESS_QNAME;
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.address);
      var1.writeUTF(this.name.getNamespaceURI());
   }
}
