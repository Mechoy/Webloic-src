package weblogic.wsee.mtom.internal;

import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.mtom.api.MtomPolicyInfo;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;

public class MtomPolicyInfoImpl implements MtomPolicyInfo {
   private boolean isMtomDisable = true;
   private boolean isMtomOptional = true;

   public MtomPolicyInfoImpl() {
   }

   public MtomPolicyInfoImpl(NormalizedExpression var1) {
      Set var2 = var1.getPolicyAlternatives();
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(true) {
            Set var5;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               PolicyAlternative var4 = (PolicyAlternative)var3.next();
               var5 = var4.getAssertions(OptimizedMimeSerialization.class);
            } while(var5 == null);

            OptimizedMimeSerialization var7;
            for(Iterator var6 = var5.iterator(); var6.hasNext(); this.isMtomOptional = var7.getOptional()) {
               this.isMtomDisable = false;
               var7 = (OptimizedMimeSerialization)var6.next();
            }
         }
      }
   }

   public boolean isMtomDisable() {
      return this.isMtomDisable;
   }

   public boolean isMtomOptional() {
      return this.isMtomOptional;
   }
}
