package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.xml.dom.DOMUtils;

public abstract class XPathElements extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = 2407340005141782846L;
   private static final QName XPATH_VERSION = new QName("XPathVersion");
   private Set<XPath> xpathExprs = new LinkedHashSet();
   private String xpathVersion;

   public Set<XPath> getXPathExpressions() {
      return this.xpathExprs;
   }

   public String getXPathVersion() {
      return this.xpathVersion;
   }

   public abstract boolean isRequired();

   void initAssertion(Element var1) throws PolicyException {
      String var2 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
      if (null != var2) {
         this.setPolicyNamespaceUri(var2);
         this.setOptional(true);
      }

      List var3 = DOMUtils.getOptionalElementsByTagNameNS(var1, this.getNamespace(), "XPath");
      List var4 = DOMUtils.getOptionalElementsByTagNameNS(var1, this.getNamespace(), "XPath2");
      this.xpathVersion = weblogic.wsee.policy.framework.DOMUtils.getAttributeValueAsString(var1, XPATH_VERSION);
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         XPath var6 = new XPath();
         var6.initialize((Element)var5.next());
         var6.setXPathVersion(this.xpathVersion);
         this.xpathExprs.add(var6);
      }

      var5 = var4.iterator();

      while(var5.hasNext()) {
         XPath2 var7 = new XPath2();
         var7.initialize((Element)var5.next());
         var7.setXPathVersion(this.xpathVersion);
         this.xpathExprs.add(var7);
      }

   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      Iterator var3 = this.xpathExprs.iterator();

      while(var3.hasNext()) {
         XPath var4 = (XPath)var3.next();
         var2.appendChild(var4.serialize(var1));
      }

      var3 = this.getElementAttrs().entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var7 = (Map.Entry)var3.next();
         boolean var5 = true;
         if (((String)var7.getKey()).startsWith("xmlns")) {
            String var6 = ((String)var7.getKey()).substring(6);
            var5 = !var2.getNodeName().equals(var6 + ":" + var2.getLocalName());
         }

         if (var5) {
            var2.setAttribute((String)var7.getKey(), (String)var7.getValue());
         }
      }

      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.xpathVersion = var1.readUTF();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         Object var4 = var1.readBoolean() ? new XPath2() : new XPath();
         ((XPath)var4).readExternal(var1);
         this.xpathExprs.add(var4);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.xpathVersion);
      var1.writeInt(this.xpathExprs.size());
      Iterator var2 = this.xpathExprs.iterator();

      while(var2.hasNext()) {
         XPath var3 = (XPath)var2.next();
         var1.writeBoolean(var3 instanceof XPath2);
         var3.writeExternal(var1);
      }

   }
}
