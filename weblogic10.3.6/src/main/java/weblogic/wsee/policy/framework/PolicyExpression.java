package weblogic.wsee.policy.framework;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyExpression implements Externalizable {
   private static final long serialVersionUID = -6972053942488988205L;
   private static final boolean verbose = Verbose.isVerbose(PolicyExpression.class);
   private static final boolean debug = false;
   protected OperatorType operator;
   protected List expressions = new ArrayList();
   protected PolicyAssertion assertion;
   protected String policyNamespaceUri = "http://schemas.xmlsoap.org/ws/2004/09/policy";

   public PolicyExpression() {
   }

   protected PolicyExpression(OperatorType var1) {
      assert var1 != null && var1 != OperatorType.TERMINAL;

      this.operator = var1;
   }

   protected PolicyExpression(OperatorType var1, PolicyAssertion var2) {
      assert var1 == OperatorType.TERMINAL;

      this.operator = var1;
      this.assertion = var2;
   }

   public PolicyAssertion getAssertion() {
      assert this.operator == OperatorType.TERMINAL;

      return this.assertion;
   }

   public String getPolicyNamespaceUri() {
      return this.policyNamespaceUri;
   }

   public void setPolicyNamespaceUri(String var1) {
      this.policyNamespaceUri = var1;
   }

   public void setAssertion(PolicyAssertion var1) {
      assert this.operator == OperatorType.TERMINAL;

      this.assertion = var1;
   }

   public List getExpressions() {
      assert this.operator != null && this.operator != OperatorType.TERMINAL;

      return this.expressions;
   }

   public void addExpression(PolicyExpression var1) {
      assert this.operator != null && this.operator != OperatorType.TERMINAL;

      this.expressions.add(var1);
   }

   public void addExpressions(Collection var1) {
      assert this.operator != null && this.operator != OperatorType.TERMINAL;

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.addExpression((PolicyExpression)var2.next());
      }

   }

   public NormalizedExpression normalize() throws PolicyException {
      return NormalizedExpression.createFromPolicyExpression(this);
   }

   NormalizedExpression internalNormalize() throws PolicyException {
      return NormalizedExpression.createFromPolicyExpressionInternal(this);
   }

   public void resetExpressions() {
      assert this.operator != null && this.operator != OperatorType.TERMINAL;

      this.expressions.clear();
   }

   protected Element toXML(Document var1) throws PolicyException {
      Element var2 = null;
      if (this.operator == OperatorType.TERMINAL) {
         var2 = this.assertion.serialize(var1);
         PolicyHelper.checkOptionNamespaceUri(var2, this.getPolicyNamespaceUri());
      } else {
         if ("http://www.w3.org/ns/ws-policy".equals(this.getPolicyNamespaceUri())) {
            if (this.operator == OperatorType.EXACTLY_ONE) {
               var2 = DOMUtils.createElement(PolicyConstants.EXACTLY_ONE_15, var1);
            } else if (this.operator == OperatorType.ONE_OR_MORE) {
               var2 = DOMUtils.createElement(PolicyConstants.ONE_OR_MORE_15, var1);
            } else {
               if (this.operator != OperatorType.ALL) {
                  throw new AssertionError("Unrecognized operator type: " + this.operator);
               }

               var2 = DOMUtils.createElement(PolicyConstants.ALL_15, var1);
            }
         } else if (this.operator == OperatorType.EXACTLY_ONE) {
            var2 = DOMUtils.createElement(PolicyConstants.EXACTLY_ONE, var1);
         } else if (this.operator == OperatorType.ONE_OR_MORE) {
            var2 = DOMUtils.createElement(PolicyConstants.ONE_OR_MORE, var1);
         } else {
            if (this.operator != OperatorType.ALL) {
               throw new AssertionError("Unrecognized operator type: " + this.operator);
            }

            var2 = DOMUtils.createElement(PolicyConstants.ALL, var1);
         }

         Iterator var3 = this.expressions.iterator();

         while(var3.hasNext()) {
            PolicyExpression var4 = (PolicyExpression)var3.next();
            var2.appendChild(var4.toXML(var1));
         }
      }

      if (var2 == null) {
         throw new AssertionError("toXML() incorrectly returned null: operator=" + this.operator + ", assertion=" + this.assertion);
      } else {
         return var2;
      }
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = null;
      if (this.operator == OperatorType.TERMINAL) {
         this.assertion.write(var1, var2);
      } else {
         if (this.operator == OperatorType.EXACTLY_ONE) {
            var3 = var2.addChild(var1, PolicyConstants.EXACTLY_ONE.getLocalPart(), this.policyNamespaceUri);
         } else if (this.operator == OperatorType.ONE_OR_MORE) {
            var3 = var2.addChild(var1, PolicyConstants.ONE_OR_MORE.getLocalPart(), this.policyNamespaceUri);
         } else {
            if (this.operator != OperatorType.ALL) {
               throw new AssertionError("Unrecognized operator type: " + this.operator);
            }

            var3 = var2.addChild(var1, PolicyConstants.ALL.getLocalPart(), this.policyNamespaceUri);
         }

         Iterator var4 = this.expressions.iterator();

         while(var4.hasNext()) {
            PolicyExpression var5 = (PolicyExpression)var4.next();
            var5.write(var3, var2);
         }
      }

   }

   public String toString() {
      return "EXPR[" + this.hashCode() + "]";
   }

   public static PolicyExpression createTerminal(PolicyAssertion var0) {
      return new PolicyExpression(OperatorType.TERMINAL, var0);
   }

   public static PolicyExpression createExpression(OperatorType var0) {
      return new PolicyExpression(var0);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.operator = (OperatorType)var1.readObject();
      this.expressions = (ArrayList)var1.readObject();
      this.assertion = (PolicyAssertion)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.operator);
      var1.writeObject(this.expressions);
      var1.writeObject(this.assertion);
   }
}
