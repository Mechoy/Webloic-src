package weblogic.wsee.wstx.wsat.policy;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.jws.Policy.Direction;
import weblogic.jws.jaxws.BasePolicyFeature;
import weblogic.jws.jaxws.PoliciesFeature;
import weblogic.jws.jaxws.PolicyFeature;
import weblogic.wsee.policy.framework.OperatorType;
import weblogic.wsee.policy.framework.PolicyExpression;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class ATAssertionBuilder {
   public static void buildPolicyMap(String var0, Map<String, PolicyStatement> var1, Map<QName, BasePolicyFeature> var2, TransactionalFeature var3) {
      Transactional.Version var4 = var3.getVersion();
      Map var5 = var3.getFlowTypeMap();
      Map var6 = var3.getEnabledMap();
      Set var7 = var5.keySet();
      Iterator var8 = var7.iterator();

      while(true) {
         String var9;
         boolean var10;
         Transactional.TransactionFlowType var11;
         do {
            do {
               if (!var8.hasNext()) {
                  return;
               }

               var9 = (String)var8.next();
            } while(!Boolean.TRUE.equals(var6.get(var9)));

            var10 = false;
            var11 = (Transactional.TransactionFlowType)var5.get(var9);
         } while(TransactionFlowType.NEVER == var11);

         if (TransactionFlowType.MANDATORY == var11) {
            var10 = true;
         }

         StringBuilder var12 = new StringBuilder();
         if (var4 != null && var4 != Version.DEFAULT) {
            var12.append(var4);
         } else {
            var12.append("defaultAT");
         }

         if (!var10) {
            var12.append("_optional");
         }

         String var13 = var12.toString();
         PolicyStatement var14 = (PolicyStatement)var1.get(var13);
         if (var14 == null) {
            var14 = buildPolicyStatement(var13.toString(), var4, var10);
            var1.put(var13, var14);
         }

         PoliciesFeature var15 = addATPolicyFeature(var0, var2, var9, var13);
         var2.put(new QName(var0, var9), var15);
      }
   }

   private static PoliciesFeature addATPolicyFeature(String var0, Map<QName, BasePolicyFeature> var1, String var2, String var3) {
      BasePolicyFeature var4 = (BasePolicyFeature)var1.get(new QName(var0, var2));
      PolicyFeature var5 = new PolicyFeature(var3, Direction.both);
      PoliciesFeature var6 = null;
      if (var4 == null) {
         var6 = new PoliciesFeature(new PolicyFeature[]{var5});
      } else if (var4 instanceof PoliciesFeature) {
         var6 = (PoliciesFeature)var4;
         var6.getPolicies().add(var5);
      } else if (var4 instanceof PolicyFeature) {
         var6 = new PoliciesFeature(new PolicyFeature[]{(PolicyFeature)var4, var5});
      }

      return var6;
   }

   private static PolicyStatement buildPolicyStatement(String var0, Transactional.Version var1, boolean var2) {
      PolicyExpression var3 = PolicyExpression.createExpression(OperatorType.EXACTLY_ONE);
      if (var1 != null && var1 != Version.DEFAULT) {
         var3.addExpression(buildOneATAssertion(var2, var1));
      } else {
         Transactional.Version[] var4 = Version.values();
         Transactional.Version[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Transactional.Version var8 = var5[var7];
            if (var8 != Version.DEFAULT && var8 != Version.WSAT11) {
               var3.addExpression(buildOneATAssertion(var2, var8));
            }
         }
      }

      PolicyStatement var9 = PolicyStatement.createPolicyStatement(var0);
      var9.addExpression(var3);
      return var9;
   }

   private static PolicyExpression buildOneATAssertion(boolean var0, Transactional.Version var1) {
      ATAssertion var2 = new ATAssertion();
      if (!var0) {
         var2.setOptional(true);
      }

      var2.setName(var1.getQName());
      PolicyExpression var3 = PolicyExpression.createExpression(OperatorType.ALL);
      PolicyExpression var4 = PolicyExpression.createExpression(OperatorType.TERMINAL);
      var4.setAssertion(var2);
      var3.addExpression(var4);
      return var3;
   }
}
