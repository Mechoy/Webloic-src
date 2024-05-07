package weblogic.wsee.mc.internal;

import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.mc.api.McPolicyInfo;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;

public class McPolicyInfoImpl implements McPolicyInfo {
   private boolean isMcDisable = true;
   private boolean isMcOptional = true;

   public McPolicyInfoImpl() {
   }

   public McPolicyInfoImpl(NormalizedExpression var1) {
      Set var2 = var1.getPolicyAlternatives();
      if (var2 != null) {
         Iterator var3 = var2.iterator();
         boolean var4 = false;

         while(true) {
            Set var6;
            do {
               if (!var3.hasNext()) {
                  if (!this.isMcDisable) {
                     this.isMcOptional = var4;
                  }

                  return;
               }

               PolicyAlternative var5 = (PolicyAlternative)var3.next();
               var6 = var5.getAssertions(MCSupported.class);
            } while(var6 == null);

            MCSupported var8;
            for(Iterator var7 = var6.iterator(); var7.hasNext(); var4 |= var8.getOptional()) {
               this.isMcDisable = false;
               var8 = (MCSupported)var7.next();
            }
         }
      }
   }

   public boolean isMcDisable() {
      return this.isMcDisable;
   }

   public boolean isMcOptional() {
      return this.isMcOptional;
   }
}
