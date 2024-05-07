package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public abstract class QNameParts extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = 8092996954978190735L;
   private Body body = null;
   private Set<Header> headers = new LinkedHashSet();
   private boolean bodyOptional = false;
   private List unknown = null;

   public Body getBody() {
      return this.body;
   }

   public Set<Header> getHeaders() {
      return this.headers;
   }

   public List getUnknown() {
      return this.unknown;
   }

   public boolean isBodyOptional() {
      return this.bodyOptional;
   }

   public void setBodyOptional(boolean var1) {
      this.bodyOptional = var1;
   }

   void initAssertion(Element var1) throws PolicyException {
      try {
         if (null == var1) {
            throw new PolicyException("Null element");
         } else {
            this.unknown = new ArrayList();

            for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
               if (var2 instanceof Element) {
                  Element var3 = (Element)var2;
                  if (this.getNamespace().equals(var3.getNamespaceURI()) && "Body".equals(var3.getLocalName())) {
                     this.body = new Body();
                     this.body.setNamespace(this.getNamespace());
                     String var6 = PolicyHelper.getOptionalPolicyNamespaceUri(var3);
                     if (null != var6) {
                        this.setPolicyNamespaceUri(var6);
                        this.optional = true;
                        this.bodyOptional = true;
                     }
                  } else if (this.getNamespace().equals(var3.getNamespaceURI()) && "Header".equals(var3.getLocalName())) {
                     Header var4 = new Header();
                     var4.initialize(var3);
                     this.headers.add(var4);
                  } else {
                     this.unknown.add(var3);
                  }
               }
            }

            if (this.unknown.isEmpty()) {
               this.unknown = null;
            }

         }
      } catch (Exception var5) {
         throw new PolicyException(var5);
      }
   }

   Element serializeAssertion(Document var1, Element var2) throws PolicyException {
      if (this.body != null) {
         Element var3 = this.body.serialize(var1);
         if (this.bodyOptional) {
            PolicyHelper.addOptionalAttribute(var3, this.getPolicyNamespaceUri());
         }

         var2.appendChild(var3);
      }

      Iterator var5 = this.headers.iterator();

      while(var5.hasNext()) {
         Header var4 = (Header)var5.next();
         var2.appendChild(var4.serialize(var1));
      }

      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      if (var1.readBoolean()) {
         this.body = new Body();
      }

      this.headers = new LinkedHashSet();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         Header var4 = new Header();
         var4.readExternal(var1);
         this.headers.add(var4);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeBoolean(this.body != null);
      var1.writeInt(this.headers.size());
      Iterator var2 = this.headers.iterator();

      while(var2.hasNext()) {
         Header var3 = (Header)var2.next();
         var3.writeExternal(var1);
      }

   }
}
