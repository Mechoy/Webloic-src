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
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.xml.dom.NamespaceUtils;

public class IntegrityAssertion extends SecurityPolicyAssertion {
   public static final QName NAME = new QName("http://www.bea.com/wls90/security/policy", "Integrity");
   private IntegrityDocument xbean;
   private Map namespaceMap;

   public IntegrityAssertion() {
   }

   IntegrityAssertion(IntegrityDocument var1, Map var2) {
      if (var1 == null) {
         throw new AssertionError();
      } else {
         this.xbean = var1;
         this.namespaceMap = var2;
      }
   }

   public IntegrityDocument getXbean() {
      return this.xbean;
   }

   public void load(Node var1) throws XmlException {
      this.xbean = IntegrityDocument.Factory.parse(var1);
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
      } else if (!(var1 instanceof IntegrityAssertion)) {
         return false;
      } else {
         IntegrityAssertion var2 = (IntegrityAssertion)var1;
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
      this.xbean = (IntegrityDocument)ExternalizationUtils.readXmlObject(var1);
      this.namespaceMap = (HashMap)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      ExternalizationUtils.writeXmlObject(this.xbean, var1);
      var1.writeObject(this.namespaceMap);
   }
}
