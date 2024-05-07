package weblogic.wsee.policy.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class PolicyMath {
   private static final boolean debug = false;

   public static NormalizedExpression merge(NormalizedExpression var0, NormalizedExpression var1) {
      return all(var0, var1);
   }

   public static NormalizedExpression intersect(NormalizedExpression var0, NormalizedExpression var1) {
      NormalizedExpression var2 = NormalizedExpression.createUnitializedExpression();
      NormalizedExpression var3 = NormalizedExpression.createUnitializedExpression();
      Set var4 = var0.getPolicyAlternatives();
      Set var5 = var1.getPolicyAlternatives();
      if (var4 != null && var5 != null) {
         PolicyAlternative[] var6 = (PolicyAlternative[])((PolicyAlternative[])var4.toArray(new PolicyAlternative[0]));
         PolicyAlternative[] var7 = (PolicyAlternative[])((PolicyAlternative[])var5.toArray(new PolicyAlternative[0]));

         for(int var8 = 0; var8 < var6.length; ++var8) {
            for(int var9 = 0; var9 < var7.length; ++var9) {
               if (isSameVocabulary(var6[var8], var7[var9])) {
                  var2.addAlternative(var6[var8]);
                  var3.addAlternative(var7[var9]);
               }
            }
         }
      }

      return all(var2, var3);
   }

   public static boolean isSameVocabulary(PolicyAlternative var0, PolicyAlternative var1) {
      if (var0.getAssertions().size() != var1.getAssertions().size()) {
         return false;
      } else {
         PolicyAssertion[] var2 = (PolicyAssertion[])((PolicyAssertion[])var0.getAssertions().toArray(new PolicyAssertion[0]));
         PolicyAssertion[] var3 = (PolicyAssertion[])((PolicyAssertion[])var1.getAssertions().toArray(new PolicyAssertion[0]));
         HashMap var4 = new HashMap();

         int var5;
         for(var5 = 0; var5 < var2.length; ++var5) {
            if (var2[var5].getName() != null) {
               var4.put(var2[var5].getName(), var2[var5]);
            }
         }

         for(var5 = 0; var5 < var3.length; ++var5) {
            if (var4.get(var3[var5].getName()) == null) {
               return false;
            }
         }

         return true;
      }
   }

   public static NormalizedExpression all(NormalizedExpression var0, NormalizedExpression var1) {
      Object var2 = null;
      Set var3 = var0.getPolicyAlternatives();
      Set var4 = var1.getPolicyAlternatives();
      if (!var0.isEmpty() && !var1.isEmpty()) {
         if (var3 == null) {
            if (var4 == null) {
               return NormalizedExpression.createUnitializedExpression();
            }

            var2 = var4;
         } else if (var4 == null) {
            var2 = var3;
         } else {
            var2 = new LinkedHashSet();
            Iterator var5 = var3.iterator();

            while(var5.hasNext()) {
               PolicyAlternative var6 = (PolicyAlternative)var5.next();
               Iterator var7 = var4.iterator();

               while(var7.hasNext()) {
                  PolicyAlternative var8 = (PolicyAlternative)var7.next();
                  PolicyAlternative var9 = new PolicyAlternative();
                  var9.addAssertions(var6.getAssertions());
                  var9.addAssertions(var8.getAssertions());
                  ((Set)var2).add(var9);
               }
            }
         }

         return NormalizedExpression.createFromPolicyAlternatives((Set)var2);
      } else {
         return NormalizedExpression.createEmptyExpression();
      }
   }

   public static NormalizedExpression exactlyOne(NormalizedExpression var0, NormalizedExpression var1) {
      Set var2 = var0.getPolicyAlternatives();
      Set var3 = var1.getPolicyAlternatives();
      if (!var0.isEmpty() && !var1.isEmpty()) {
         if (var0.isUninitialized() && var1.isUninitialized()) {
            return NormalizedExpression.createUnitializedExpression();
         } else {
            LinkedHashSet var4 = new LinkedHashSet();
            if (var2 != null) {
               var4.addAll(var2);
            }

            if (var3 != null) {
               var4.addAll(var3);
            }

            return NormalizedExpression.createFromPolicyAlternatives(var4);
         }
      } else {
         return NormalizedExpression.createEmptyExpression();
      }
   }

   public static NormalizedExpression oneOrMore(NormalizedExpression var0, NormalizedExpression var1) {
      NormalizedExpression var2 = exactlyOne(var0, var1);
      NormalizedExpression var3 = all(var0, var1);
      return exactlyOne(var2, var3);
   }
}
