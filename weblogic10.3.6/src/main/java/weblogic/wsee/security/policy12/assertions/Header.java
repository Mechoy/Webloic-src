package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public class Header extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = 5969917487655147126L;
   public static final String HEADER = "Header";
   private static final QName NAME = new QName("Name");
   private static final QName NAMESPACE = new QName("Namespace");
   private String headerName;
   private String namespaceUri;

   public String getHeaderName() {
      return this.headerName;
   }

   public String getHeaderNamespaceUri() {
      return this.namespaceUri;
   }

   public QName getName() {
      return new QName(this.getNamespace(), "Header", "sp");
   }

   void initAssertion(Element var1) throws PolicyException {
      this.headerName = DOMUtils.getAttributeValueAsString(var1, NAME);
      this.namespaceUri = DOMUtils.getAttributeValueAsString(var1, NAMESPACE);
      String var2 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
      if (null != var2) {
         this.setPolicyNamespaceUri(var2);
         this.setOptional(true);
      }

   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      if (this.headerName != null && this.headerName.length() > 0) {
         DOMUtils.addAttribute(var2, NAME, this.headerName);
      }

      DOMUtils.addAttribute(var2, NAMESPACE, this.namespaceUri);
      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      if (var1.readBoolean()) {
         this.headerName = var1.readUTF();
      }

      this.namespaceUri = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.headerName != null && this.headerName.length() > 0) {
         var1.writeBoolean(true);
         var1.writeUTF(this.headerName);
      } else {
         var1.writeBoolean(false);
      }

      var1.writeUTF(this.namespaceUri);
   }
}
