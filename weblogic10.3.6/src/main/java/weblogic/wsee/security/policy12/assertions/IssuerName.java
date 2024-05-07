package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.xml.dom.DOMUtils;

public class IssuerName extends SecurityPolicy12Assertion {
   public static final String ISSUER_NAME = "IssuerName";
   private String issuerName = null;

   public QName getName() {
      return new QName(this.getNamespace(), "IssuerName", "sp");
   }

   public String getIssuerName() {
      return this.issuerName;
   }

   void initAssertion(Element var1) throws PolicyException {
      this.issuerName = DOMUtils.getTextContent(var1, true);
   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      DOMUtils.addTextData(var2, this.issuerName);
      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.issuerName = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.issuerName);
   }
}
