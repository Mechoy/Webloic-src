package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public abstract class SecurityPolicy12Assertion extends PolicyAssertion implements AbstractSecurityPolicyAssertion {
   private String namespace;
   private Map<String, String> elementAttrs;

   void setNamespace(String var1) {
      this.namespace = var1;
   }

   String getNamespace() {
      return this.namespace;
   }

   public Map<String, String> getElementAttrs() {
      return this.elementAttrs;
   }

   public void initialize(Element var1) throws PolicyException {
      this.setNamespace(var1.getNamespaceURI());
      this.elementAttrs = DOMUtils.getAttributeMap(var1);
      this.initAssertion(var1);
   }

   abstract void initAssertion(Element var1) throws PolicyException;

   abstract Element serializeAssertion(Document var1, Element var2) throws PolicyException;

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(this.getName(), var1, "sp");
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var2, this.getPolicyNamespaceUri());
      }

      return this.serializeAssertion(var1, var2);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.namespace = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.namespace);
   }
}
