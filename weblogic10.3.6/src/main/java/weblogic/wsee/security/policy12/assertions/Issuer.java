package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class Issuer extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = 3689353876029314719L;
   public static final String ISSUER = "Issuer";
   private Address address;
   private String uriText = null;

   public QName getName() {
      return new QName(this.getNamespace(), "Issuer", "sp");
   }

   public String getIssuerUri() {
      return null != this.address ? this.address.getEndpointAddress() : this.uriText;
   }

   void initAssertion(Element var1) throws PolicyException {
      this.setNamespace(var1.getNamespaceURI());

      try {
         Element var2 = DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.w3.org/2005/08/addressing", "Address");
         if (var2 == null) {
            var2 = DOMUtils.getOptionalElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address");
         }

         if (var2 != null) {
            this.address = new Address();
            this.address.initialize(var1);
         } else if (var1.hasChildNodes()) {
            this.uriText = var1.getFirstChild().getNodeValue();
         }

      } catch (DOMProcessingException var3) {
         throw new PolicyException(var3);
      }
   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      if (null == this.address && this.uriText == null) {
         return var2;
      } else {
         Element var3 = weblogic.wsee.policy.framework.DOMUtils.createElement(this.getName(), var1, var2.getPrefix());
         if (this.address != null) {
            Element var4 = this.address.serialize(var1);
            var3.appendChild(var4);
         } else if (this.uriText != null) {
            DOMUtils.addTextData(var3, this.uriText);
         }

         var2.appendChild(var3);
         return var2;
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      Object var2 = var1.readObject();
      if (var2 instanceof String) {
         this.uriText = (String)var2;
      } else {
         this.address = new Address();
         this.address.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.address != null) {
         this.address.writeExternal(var1);
      }

      if (this.uriText != null) {
         var1.writeUTF(this.uriText);
      }

   }
}
