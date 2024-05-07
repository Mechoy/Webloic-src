package weblogic.wsee.policy.framework;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import weblogic.wsee.mc.internal.MCSupported;
import weblogic.wsee.policy.util.PolicyHelper;

public class NormalizedExpression implements Externalizable {
   private static final long serialVersionUID = 68366604685464881L;
   private static final boolean debug = false;
   protected String policyNamespaceUri = "http://schemas.xmlsoap.org/ws/2004/09/policy";
   private Set alts = null;

   public NormalizedExpression() {
   }

   public NormalizedExpression(String var1) {
      this.policyNamespaceUri = var1;
   }

   public String getPolicyNamespaceUri() {
      return this.policyNamespaceUri;
   }

   public void setPolicyNamespaceUri(String var1) {
      this.policyNamespaceUri = var1;
   }

   public static NormalizedExpression createFromPolicyAlternatives(Set var0) {
      assert var0 != null;

      NormalizedExpression var1 = new NormalizedExpression();
      var1.addAlternatives(var0);
      var1.setPolicyNamespaceUri(PolicyHelper.getPolicyNamespaceUri(var0));
      return var1;
   }

   public static NormalizedExpression createEmptyExpression() {
      return createFromPolicyAlternatives(new LinkedHashSet());
   }

   public static NormalizedExpression createUnitializedExpression() {
      return new NormalizedExpression();
   }

   static NormalizedExpression createFromPolicyExpression(PolicyExpression var0) throws PolicyException {
      NormalizedExpression var1 = createUnitializedExpression();
      NormalizedExpression var3;
      if (var0.assertion != null && NestedPolicyAssertion.class.isAssignableFrom(var0.assertion.getClass()) && var0.operator == OperatorType.TERMINAL) {
         NestedPolicyAssertion var8 = (NestedPolicyAssertion)var0.assertion;
         var3 = var8.nestedPolicy;
         if (var3 != null && var3.getPolicyAlternatives() != null && var3.getPolicyAlternatives().size() > 1) {
            Iterator var10 = var3.getPolicyAlternatives().iterator();

            while(var10.hasNext()) {
               LinkedHashSet var11 = new LinkedHashSet();
               var11.add(var10.next());
               NestedPolicyAssertion var6 = (NestedPolicyAssertion)var8.clone();
               var6.setNestedPolicy(createFromPolicyAlternatives(var11));
               var1.addAlternative(new PolicyAlternative(new PolicyAssertion[]{var6}));
            }
         } else {
            PolicyAlternative var9 = new PolicyAlternative(new PolicyAssertion[]{var0.assertion});
            var1.addAlternative(var9);
         }

         if (var0.assertion != null && var0.assertion.getOptional()) {
            var1.addAlternative(PolicyAlternative.EMPTY_ALTERNATIVE);
         }
      } else if (var0.operator == OperatorType.TERMINAL) {
         PolicyAlternative var2 = new PolicyAlternative(new PolicyAssertion[]{var0.assertion});
         var1.addAlternative(var2);
         if (var0.assertion != null && var0.assertion.getOptional() && !(var0.assertion instanceof MCSupported)) {
            var1.addAlternative(PolicyAlternative.EMPTY_ALTERNATIVE);
         }
      } else {
         Iterator var7 = var0.expressions.iterator();
         var3 = null;
         if (var0.operator == OperatorType.EXACTLY_ONE && !var7.hasNext()) {
            var1 = createEmptyExpression();
            var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
         } else {
            var3 = createUnitializedExpression();
            var3.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());

            while(var7.hasNext()) {
               PolicyExpression var4 = (PolicyExpression)var7.next();
               NormalizedExpression var5 = var4.normalize();
               if (var3 == null) {
                  var3 = var5;
               } else if (var0.operator == OperatorType.ALL) {
                  var3 = PolicyMath.all(var3, var5);
               } else if (var0.operator == OperatorType.EXACTLY_ONE) {
                  var3 = PolicyMath.exactlyOne(var3, var5);
               } else {
                  if (var0.operator != OperatorType.ONE_OR_MORE) {
                     throw new AssertionError("Unknown operator type: " + var0.operator);
                  }

                  var3 = PolicyMath.oneOrMore(var3, var5);
               }
            }

            var1 = var3;
         }
      }

      var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
      return var1;
   }

   static NormalizedExpression createFromPolicyExpressionInternal(PolicyExpression var0) throws PolicyException {
      NormalizedExpression var1 = createUnitializedExpression();
      if (var0.operator == OperatorType.TERMINAL) {
         PolicyAlternative var2 = new PolicyAlternative(new PolicyAssertion[]{var0.assertion});
         var1.addAlternative(var2);
      } else {
         Iterator var6 = var0.expressions.iterator();
         NormalizedExpression var3 = null;
         if (var0.operator == OperatorType.EXACTLY_ONE && !var6.hasNext()) {
            var1 = createEmptyExpression();
            var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
         } else {
            var3 = createUnitializedExpression();
            var3.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());

            while(var6.hasNext()) {
               PolicyExpression var4 = (PolicyExpression)var6.next();
               NormalizedExpression var5 = var4.internalNormalize();
               if (var3 == null) {
                  var3 = var5;
               } else if (var0.operator == OperatorType.ALL) {
                  var3 = PolicyMath.all(var3, var5);
               } else if (var0.operator == OperatorType.EXACTLY_ONE) {
                  var3 = PolicyMath.exactlyOne(var3, var5);
               } else {
                  if (var0.operator != OperatorType.ONE_OR_MORE) {
                     throw new AssertionError("Unknown operator type: " + var0.operator);
                  }

                  var3 = PolicyMath.oneOrMore(var3, var5);
               }
            }

            var1 = var3;
         }
      }

      var1.setPolicyNamespaceUri(var0.getPolicyNamespaceUri());
      return var1;
   }

   public void addAlternative(PolicyAlternative var1) {
      assert var1 != null;

      if (this.alts == null) {
         this.alts = new LinkedHashSet();
      }

      this.alts.add(var1);
   }

   public void addAlternatives(Set var1) {
      assert var1 != null;

      if (this.alts == null) {
         this.alts = new LinkedHashSet();
      }

      this.alts.addAll(var1);
   }

   public boolean isEmpty() {
      return this.alts != null && this.alts.size() == 0;
   }

   public boolean isUninitialized() {
      return this.alts == null;
   }

   public Set getPolicyAlternatives() {
      return this.alts == null ? null : new LinkedHashSet(this.alts);
   }

   public PolicyAlternative getPolicyAlternative() {
      return this.alts != null && this.alts.size() != 0 ? (PolicyAlternative)this.alts.iterator().next() : null;
   }

   public Set getPolicyAlternatives(PolicyAssertion var1) {
      if (this.alts == null) {
         return null;
      } else {
         LinkedHashSet var2 = new LinkedHashSet();
         Iterator var3 = this.alts.iterator();

         while(var3.hasNext()) {
            PolicyAlternative var4 = (PolicyAlternative)var3.next();
            if (var4.isTrue(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public Set getPolicyAlternatives(Class var1) {
      if (this.alts == null) {
         return null;
      } else {
         LinkedHashSet var2 = new LinkedHashSet();
         Iterator var3 = this.alts.iterator();

         while(var3.hasNext()) {
            PolicyAlternative var4 = (PolicyAlternative)var3.next();
            if (var4.getAssertions(var1).size() > 0) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public boolean containsPolicyAssertion(Class var1) {
      return !this.isUninitialized() && !this.isEmpty() && this.getPolicyAlternatives(var1).size() > 0;
   }

   public PolicyAssertion getPolicyAssertion(Class var1) {
      if (this.alts != null) {
         new LinkedHashSet();
         Iterator var3 = this.alts.iterator();

         while(var3.hasNext()) {
            PolicyAlternative var4 = (PolicyAlternative)var3.next();
            Set var5 = var4.getAssertions(var1);
            if (!var5.isEmpty()) {
               return (PolicyAssertion)var5.iterator().next();
            }
         }
      }

      return null;
   }

   public String toString() {
      return this.toCompactForm((String)null).toString();
   }

   public PolicyStatement toCompactForm(String var1) {
      PolicyStatement var2 = new PolicyStatement((String)null, this.policyNamespaceUri);
      PolicyExpression var3;
      if (this.alts == null) {
         var3 = PolicyExpression.createExpression(OperatorType.ALL);
         var3.setPolicyNamespaceUri(this.policyNamespaceUri);
         PolicyExpression var4 = PolicyExpression.createExpression(OperatorType.EXACTLY_ONE);
         var4.setPolicyNamespaceUri(this.policyNamespaceUri);
         var4.addExpression(var3);
         var2.addExpression(var4);
      } else {
         var3 = new PolicyExpression(OperatorType.EXACTLY_ONE);
         var3.setPolicyNamespaceUri(this.policyNamespaceUri);
         Iterator var9 = this.alts.iterator();

         while(var9.hasNext()) {
            PolicyAlternative var5 = (PolicyAlternative)var9.next();
            PolicyExpression var6 = new PolicyExpression(OperatorType.ALL);
            var6.setPolicyNamespaceUri(this.policyNamespaceUri);
            Iterator var7 = var5.getAssertions().iterator();

            while(var7.hasNext()) {
               PolicyAssertion var8 = (PolicyAssertion)var7.next();
               var6.addExpression(new PolicyExpression(OperatorType.TERMINAL, var8));
            }

            var3.addExpression(var6);
         }

         var2.addExpression(var3);
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && var1 instanceof NormalizedExpression) {
         NormalizedExpression var2 = (NormalizedExpression)var1;
         return this.alts == null && var2.alts == null || this.alts.equals(var2.alts);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.alts == null ? this.getClass().hashCode() : this.alts.hashCode();
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.alts = ExternalizationUtils.readAlternatives(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      Object var2 = this.alts;
      if (var2 == null) {
         var2 = new LinkedHashSet();
      }

      ExternalizationUtils.writeAlternatives((Set)var2, var1);
   }
}
