package weblogic.wsee.reliability2.policy;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import weblogic.wsee.jaxws.framework.policy.PolicyPropertyBag;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.reliability.policy.RMAssertion;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;

public class WsrmPolicySelectionHelper {
   static final int ANY = 0;
   static final int RM10 = 1;
   static final int RM11 = 2;
   static final int BOTH = 3;
   private PolicySelectionPreference preference = null;
   protected Set alternatives = null;
   private boolean forceWsrm10Policy = false;
   protected NormalizedExpression normalizedPolicy;

   public WsrmPolicySelectionHelper(NormalizedExpression var1) {
      if (null == var1) {
         throw new IllegalArgumentException("NullnormalizedPolicy found!");
      } else {
         this.preference = new PolicySelectionPreference();
         this.forceWsrm10Policy = false;
         this.normalizedPolicy = var1;
         this.alternatives = var1.getPolicyAlternatives();
      }
   }

   public WsrmPolicySelectionHelper(NormalizedExpression var1, PolicyPropertyBag var2, WsrmInvocationPropertyBag var3) {
      if (null == var1) {
         throw new IllegalArgumentException("Null normalizedPolicy found!");
      } else {
         this.preference = var2.getPolicySelectionPreference();
         this.forceWsrm10Policy = var3.getForceWsrm10Client();
         this.normalizedPolicy = var1;
         this.alternatives = var1.getPolicyAlternatives();
      }
   }

   public PolicySelectionPreference getPolicySelectionPreference() {
      return this.preference;
   }

   public void setPolicySelectionPreference(PolicySelectionPreference var1) {
      if (null == var1) {
         this.preference = new PolicySelectionPreference();
      } else {
         this.preference = var1;
      }

   }

   public boolean isForceWsrm10Policy() {
      return this.forceWsrm10Policy;
   }

   public void setForceWsrm10Policy(boolean var1) {
      this.forceWsrm10Policy = var1;
   }

   public int getNumberOfAlternatives() {
      return null != this.alternatives && !this.alternatives.isEmpty() ? this.alternatives.size() : 0;
   }

   public boolean hasReliabilityPolicyAssertion() {
      if (this.getNumberOfAlternatives() == 0) {
         return false;
      } else {
         Set var1 = this.getReliabilityPolicyAssertion(0);
         return !var1.isEmpty();
      }
   }

   public PolicyAssertion[] getReliabilityPolicyAssertions() {
      if (this.getNumberOfAlternatives() == 0) {
         return null;
      } else {
         Set var1;
         if (this.isForceWsrm10Policy()) {
            var1 = this.getReliabilityPolicyAssertion(1);
            if (var1.size() == 0) {
               var1.add(new RMAssertion());
            }
         } else if (this.preference.isDefaut()) {
            var1 = this.getReliabilityPolicyAssertion(0);
         } else if (this.preference.isInteropFirst()) {
            var1 = this.getReliabilityPolicyAssertion(1);
            var1.addAll(this.getReliabilityPolicyAssertion(2));
         } else {
            var1 = this.getReliabilityPolicyAssertion(2);
            var1.addAll(this.getReliabilityPolicyAssertion(1));
         }

         if (var1.size() == 0) {
            return null;
         } else {
            PolicyAssertion[] var2 = new PolicyAssertion[var1.size()];
            Iterator var3 = var1.iterator();

            for(int var4 = 0; var4 < var1.size(); ++var4) {
               var2[var4] = (PolicyAssertion)var3.next();
            }

            return var2;
         }
      }
   }

   private Set getReliabilityPolicyAssertion(int var1) {
      Iterator var2 = this.alternatives.iterator();
      LinkedHashSet var3 = new LinkedHashSet();

      while(true) {
         label28:
         while(var2.hasNext()) {
            PolicyAlternative var4 = (PolicyAlternative)var2.next();
            if (var1 == 0) {
               Set var5 = var4.getAssertions();
               Iterator var6 = var5.iterator();

               while(true) {
                  PolicyAssertion var8;
                  do {
                     if (!var6.hasNext()) {
                        continue label28;
                     }

                     Object var7 = var6.next();
                     var8 = (PolicyAssertion)var7;
                  } while(!(var8 instanceof RMAssertion) && !(var8 instanceof RM11Assertion));

                  var3.add(var8);
               }
            } else {
               if ((var1 & 1) > 0) {
                  var3.addAll(var4.getAssertions(RMAssertion.class));
               }

               if ((var1 & 2) > 0) {
                  var3.addAll(var4.getAssertions(RM11Assertion.class));
               }
            }
         }

         return var3;
      }
   }
}
