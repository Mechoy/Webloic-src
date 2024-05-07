package weblogic.wsee.security.policy.assertions;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.xbeans.IdentityDocument;

public class IdentityAssertion extends SecurityPolicyAssertion {
   public static final QName NAME = new QName("http://www.bea.com/wls90/security/policy", "Identity");
   private IdentityDocument xbean;

   public IdentityAssertion() {
   }

   IdentityAssertion(IdentityDocument var1) {
      if (var1 == null) {
         throw new AssertionError();
      } else {
         this.xbean = var1;
      }
   }

   public IdentityDocument getXbean() {
      return this.xbean;
   }

   public void load(Node var1) throws XmlException {
      this.xbean = IdentityDocument.Factory.parse(var1);
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = getElement(this.xbean);
      return var1 == null ? var2 : (Element)var1.importNode(var2, true);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof IdentityAssertion)) {
         return false;
      } else {
         IdentityAssertion var2 = (IdentityAssertion)var1;
         return this.xbean.equals(var2.xbean);
      }
   }

   public int hashCode() {
      return this.xbean.hashCode();
   }

   public QName getName() {
      return NAME;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.xbean = (IdentityDocument)ExternalizationUtils.readXmlObject(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      ExternalizationUtils.writeXmlObject(this.xbean, var1);
   }
}
