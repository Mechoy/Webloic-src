package weblogic.wsee.security.policy.assertions;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.xml.dom.NamespaceUtils;

public class ConfidentialityAssertion extends SecurityPolicyAssertion {
   public static final QName NAME = new QName("http://www.bea.com/wls90/security/policy", "Confidentiality");
   private ConfidentialityDocument xbean;
   private Map namespaceMap;

   public ConfidentialityAssertion() {
   }

   ConfidentialityAssertion(ConfidentialityDocument var1, Map var2) {
      if (var1 == null) {
         throw new AssertionError();
      } else {
         this.xbean = var1;
         this.namespaceMap = var2;
      }
   }

   public ConfidentialityDocument getXbean() {
      return this.xbean;
   }

   public void load(Node var1) throws XmlException {
      this.xbean = ConfidentialityDocument.Factory.parse(var1);
   }

   public Map getNamespaceMap() {
      return this.namespaceMap;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = getElement(this.xbean);
      if (var1 != null) {
         var2 = (Element)var1.importNode(var2, true);
      }

      Iterator var3 = this.namespaceMap.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4 != null && var4.length() != 0) {
            String var5 = (String)this.namespaceMap.get(var4);
            if (prefixesMap.get(var5) == null) {
               NamespaceUtils.defineNamespace(var2, var4, var5);
            }
         }
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ConfidentialityAssertion)) {
         return false;
      } else {
         ConfidentialityAssertion var2 = (ConfidentialityAssertion)var1;
         return this.xbean.equals(var2.xbean);
      }
   }

   public QName getName() {
      return NAME;
   }

   public int hashCode() {
      return this.xbean.hashCode();
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.xbean = (ConfidentialityDocument)ExternalizationUtils.readXmlObject(var1);
      this.namespaceMap = (HashMap)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      ExternalizationUtils.writeXmlObject(this.xbean, var1);
      var1.writeObject(this.namespaceMap);
   }
}
