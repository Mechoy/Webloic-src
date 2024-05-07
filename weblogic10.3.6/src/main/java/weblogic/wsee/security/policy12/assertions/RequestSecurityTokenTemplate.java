package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public class RequestSecurityTokenTemplate extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = -3174843977306475189L;
   public static final String REQUET_SECURITY_TOKEN_TEMPLATE = "RequestSecurityTokenTemplate";
   Map templateMap = null;

   void initAssertion(Element var1) throws PolicyException {
      if (null != var1) {
         String var2 = PolicyHelper.getOptionalPolicyNamespaceUri(var1);
         if (var2 != null) {
            this.setPolicyNamespaceUri(var2);
            this.setOptional(true);
         }

         this.templateMap = new HashMap();

         for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Element) {
               Element var4 = (Element)var3;
               QName var5 = new QName(var4.getNamespaceURI(), var4.getLocalName());
               String var6 = DOMUtils.getTextContent(var4, true);
               this.templateMap.put(var5, var6);
            }
         }

      }
   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      if (this.templateMap != null && this.templateMap.size() != 0) {
         Element var3 = DOMUtils.createElement(this.getName(), var1, var2.getPrefix());
         Set var4 = this.templateMap.entrySet();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            Element var7 = DOMUtils.createElement((QName)var6.getKey(), var1);
            var7.appendChild(var1.createTextNode((String)var6.getValue()));
            var3.appendChild(var7);
         }

         var2.appendChild(var3);
         return var2;
      } else {
         return var2;
      }
   }

   public QName getName() {
      return new QName(this.getNamespace(), "RequestSecurityTokenTemplate", "sp");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.templateMap = new HashMap();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         Map.Entry var4 = (Map.Entry)var1.readObject();
         this.templateMap.put(var4.getKey(), var4.getValue());
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeInt(this.templateMap.size());
      Set var2 = this.templateMap.entrySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         var1.writeObject(var4);
      }

   }

   public Map getTemplateMap() {
      return this.templateMap;
   }
}
