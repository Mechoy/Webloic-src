package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.NestedPolicyAssertion;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;

public abstract class NestedSecurityPolicy12Assertion extends NestedPolicyAssertion implements AbstractSecurityPolicyAssertion {
   private String namespace;

   void setNamespace(String var1) {
      this.namespace = var1;
   }

   String getNamespace() {
      return this.namespace;
   }

   public Set<PolicyAssertion> readNestedAssertion(Element var1) throws PolicyException {
      LinkedHashSet var2 = new LinkedHashSet();
      PolicyAssertion var3 = SecurityPolicy12AssertionFactory.getInstance().createAssertion(var1);
      if (var3 != null && NestedPolicyAssertion.class.isAssignableFrom(var3.getClass())) {
         NestedPolicyAssertion var4 = (NestedPolicyAssertion)var3;
         if (var4.getNestedPolicy() != null && var4.getNestedPolicy().getPolicyAlternatives() != null && var4.getNestedPolicy().getPolicyAlternatives().size() > 1) {
            Iterator var5 = var4.getNestedPolicy().getPolicyAlternatives().iterator();

            while(var5.hasNext()) {
               LinkedHashSet var6 = new LinkedHashSet();
               var6.add(var5.next());
               NestedPolicyAssertion var7 = (NestedPolicyAssertion)var4.clone();
               var7.setNestedPolicy(NormalizedExpression.createFromPolicyAlternatives(var6));
               var2.add(var7);
            }
         } else {
            var2.add(var3);
         }
      } else {
         var2.add(var3);
      }

      return var2;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(this.getName(), var1, "sp");
      if (this.nestedPolicy != null && !this.nestedPolicy.isEmpty()) {
         Element var3;
         if (!"http://www.w3.org/ns/ws-policy".equals(this.nestedPolicy.getPolicyNamespaceUri()) && !PolicyHelper.hasWsp15NamespaceUri(var2)) {
            var3 = DOMUtils.createElement(PolicyConstants.POLICY_STATEMENT_ELEMENT, var1, "wsp");
         } else {
            var3 = DOMUtils.createElement(PolicyConstants.POLICY_STATEMENT_ELEMENT_15, var1, "wsp15");
         }

         var2.appendChild(var3);
         Set var4 = this.nestedPolicy.getPolicyAlternatives();
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(true) {
               PolicyAlternative var6;
               do {
                  do {
                     if (!var5.hasNext()) {
                        return var2;
                     }

                     var6 = (PolicyAlternative)var5.next();
                  } while(var6 == null);
               } while(var6.isEmpty());

               Iterator var7 = var6.getAssertions().iterator();

               while(var7.hasNext()) {
                  PolicyAssertion var8 = (PolicyAssertion)var7.next();
                  if (var8 != null) {
                     var3.appendChild(var8.serialize(var1));
                  }
               }
            }
         }
      }

      return var2;
   }

   public void initialize(Element var1) throws PolicyException {
      this.setNamespace(var1.getNamespaceURI());
      this.init(var1);
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
