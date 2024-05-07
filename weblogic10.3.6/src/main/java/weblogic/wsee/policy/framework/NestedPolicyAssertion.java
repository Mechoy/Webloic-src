package weblogic.wsee.policy.framework;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.xml.dom.DOMProcessingException;

public abstract class NestedPolicyAssertion extends PolicyAssertion implements Cloneable {
   private static final long serialVersionUID = 5657389624647138838L;
   protected NormalizedExpression nestedPolicy;

   public NormalizedExpression getNestedPolicy() {
      return this.nestedPolicy;
   }

   public void setNestedPolicy(NormalizedExpression var1) {
      this.nestedPolicy = var1;
   }

   protected void init(Element var1) throws PolicyException {
      try {
         Element var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2004/09/policy", "Policy");
         if (var2 == null) {
            var2 = weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var1, "http://www.w3.org/ns/ws-policy", "Policy");
            if (null != var2) {
               this.setPolicyNamespaceUri("http://www.w3.org/ns/ws-policy");
            }
         } else {
            this.setPolicyNamespaceUri("http://schemas.xmlsoap.org/ws/2004/09/policy");
         }

         if (var2 != null) {
            this.nestedPolicy = this.readPolicyStatement(var2).internalNormalize();
         }

      } catch (DOMProcessingException var3) {
         throw new PolicyException(var3);
      }
   }

   protected NormalizedExpression addNestedPolicy(Set<PolicyAssertion> var1) {
      PolicyAlternative var2 = new PolicyAlternative();
      var2.addAssertions(var1);
      NormalizedExpression var3 = new NormalizedExpression();
      var3.addAlternative(var2);
      return var3;
   }

   public abstract Set<PolicyAssertion> readNestedAssertion(Element var1) throws PolicyException;

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.nestedPolicy = ExternalizationUtils.readNormalizedExpression(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      ExternalizationUtils.writeNormalizedExpression(this.nestedPolicy, var1);
   }

   protected PolicyAssertion getNestedAssertion(Class var1) {
      return this.nestedPolicy != null ? this.nestedPolicy.getPolicyAssertion(var1) : null;
   }

   protected Set<PolicyAssertion> getNestedAssertions(Class var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      Set var3 = this.nestedPolicy.getPolicyAlternatives();
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Iterator var5 = ((PolicyAlternative)var4.next()).getAssertions(var1).iterator();

            while(var5.hasNext()) {
               var2.add((PolicyAssertion)var5.next());
            }
         }
      }

      return var2;
   }

   public Object clone() {
      Object var1 = null;

      try {
         var1 = super.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }

   private PolicyExpression readExpression(Element var1) throws PolicyException {
      Object var2 = null;
      OperatorType var3 = null;
      if (PolicyConstants.ALL.getLocalPart().equals(var1.getLocalName())) {
         var3 = OperatorType.ALL;
      } else if (PolicyConstants.ONE_OR_MORE.getLocalPart().equals(var1.getLocalName())) {
         var3 = OperatorType.ONE_OR_MORE;
      } else if (PolicyConstants.EXACTLY_ONE.getLocalPart().equals(var1.getLocalName())) {
         var3 = OperatorType.EXACTLY_ONE;
      }

      if (var3 != null) {
         var2 = this.readOperatorExpression(var1, PolicyExpression.createExpression(var3));
      } else if (!DOMUtils.equalsQName(var1, PolicyConstants.POLICY_STATEMENT_ELEMENT) && !DOMUtils.equalsQName(var1, PolicyConstants.POLICY_STATEMENT_ELEMENT_15)) {
         var2 = this.readAssertionExpression(var1);
      } else {
         var2 = this.readPolicyStatement(var1);
      }

      ((PolicyExpression)var2).setPolicyNamespaceUri(this.policyNamespaceUri);
      return (PolicyExpression)var2;
   }

   private PolicyStatement readPolicyStatement(Element var1) throws PolicyException {
      PolicyStatement var2 = PolicyStatement.createPolicyStatement((String)null);
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 1) {
            PolicyExpression var6 = this.readExpression((Element)var5);
            var2.addExpression(var6);
         }
      }

      var2.setPolicyNamespaceUri(this.policyNamespaceUri);
      return var2;
   }

   private PolicyExpression readOperatorExpression(Element var1, PolicyExpression var2) throws PolicyException {
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (var5.getNodeType() == 1) {
            var2.addExpression(this.readExpression((Element)var5));
         }
      }

      var2.setPolicyNamespaceUri(this.policyNamespaceUri);
      return var2;
   }

   private PolicyExpression readAssertionExpression(Element var1) throws PolicyException {
      Set var2 = this.readNestedAssertion(var1);
      PolicyExpression var3 = null;
      if (var2.size() > 1) {
         var3 = PolicyExpression.createExpression(OperatorType.EXACTLY_ONE);
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            PolicyAssertion var5 = (PolicyAssertion)var4.next();
            var3.addExpression(PolicyExpression.createTerminal(var5));
         }
      } else {
         PolicyAssertion var6 = (PolicyAssertion)var2.iterator().next();
         if (null != var6) {
            var6.setPolicyNamespaceUri(this.policyNamespaceUri);
         }

         var3 = PolicyExpression.createTerminal(var6);
      }

      var3.setPolicyNamespaceUri(this.policyNamespaceUri);
      return var3;
   }
}
