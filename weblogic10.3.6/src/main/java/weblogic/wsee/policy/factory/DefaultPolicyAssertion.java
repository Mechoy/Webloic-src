package weblogic.wsee.policy.factory;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;

public class DefaultPolicyAssertion extends PolicyAssertion {
   private static final long serialVersionUID = -6125084898592047613L;
   public static final QName NAME = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "UnkownPolicyAssertion");
   private Node n;

   public DefaultPolicyAssertion() {
   }

   public DefaultPolicyAssertion(Node var1) {
      this.n = var1.cloneNode(true);
   }

   public QName getName() {
      if (this.n != null && this.n.getNamespaceURI() != null) {
         try {
            return new QName(this.n.getNamespaceURI(), this.n.getLocalName());
         } catch (Exception var2) {
         }
      }

      return NAME;
   }

   public Element serialize(Document var1) throws PolicyException {
      Node var2 = var1.importNode(this.n, true);
      return (Element)var2;
   }

   public String toString() {
      String var1 = this.n.getNodeValue();
      return null == var1 ? this.getName().toString() : this.getName().toString() + " = " + var1;
   }
}
