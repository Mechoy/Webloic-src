package weblogic.wsee.security.wssp;

import java.util.Iterator;
import java.util.LinkedList;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;

public final class SecurityPolicyAssertionInfoFactory {
   private static final LinkedList<String> supportedSecurityInfo = new LinkedList();

   public static boolean hasSecurityPolicy(NormalizedExpression var0) {
      if (var0.getPolicyAlternatives() == null) {
         return false;
      } else {
         Iterator var1 = var0.getPolicyAlternatives().iterator();

         SecurityPolicyAssertionInfo var3;
         do {
            do {
               if (!var1.hasNext()) {
                  return false;
               }

               PolicyAlternative var2 = (PolicyAlternative)var1.next();
               var3 = getSecurityPolicyAssertionInfo(var2);
            } while(var3 == null);
         } while(!var3.isMessageSecurityEnabled() && var3.getTransportBindingInfo() == null);

         return true;
      }
   }

   public static boolean hasMessageSecurityPolicy(NormalizedExpression var0) {
      if (var0.getPolicyAlternatives() == null) {
         return false;
      } else {
         Iterator var1 = var0.getPolicyAlternatives().iterator();

         SecurityPolicyAssertionInfo var3;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            PolicyAlternative var2 = (PolicyAlternative)var1.next();
            var3 = getSecurityPolicyAssertionInfo(var2);
         } while(var3 == null || !var3.isMessageSecurityEnabled());

         return true;
      }
   }

   public static boolean hasTransportSecurityPolicy(NormalizedExpression var0) {
      if (var0.getPolicyAlternatives() == null) {
         return false;
      } else {
         Iterator var1 = var0.getPolicyAlternatives().iterator();

         SecurityPolicyAssertionInfo var3;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            PolicyAlternative var2 = (PolicyAlternative)var1.next();
            var3 = getSecurityPolicyAssertionInfo(var2);
         } while(var3 == null || var3.getTransportBindingInfo() == null);

         return true;
      }
   }

   /** @deprecated */
   public static final boolean hasWsTrustPolicy(NormalizedExpression var0) {
      if (var0 == null) {
         return false;
      } else if (var0.getPolicyAlternatives() == null) {
         return false;
      } else {
         Iterator var1 = var0.getPolicyAlternatives().iterator();

         SecurityPolicyAssertionInfo var3;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            PolicyAlternative var2 = (PolicyAlternative)var1.next();
            var3 = getSecurityPolicyAssertionInfo(var2);
         } while(var3 == null || var3.getWsTrustOptions() == null || var3.getWsTrustBootstrapPolicy() == null);

         return true;
      }
   }

   public static final SecurityPolicyAssertionInfo getSecurityPolicyAssertionInfo(PolicyAlternative var0) {
      Iterator var1 = supportedSecurityInfo.iterator();

      SecurityPolicyAssertionInfo var3;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         String var2 = (String)var1.next();
         var3 = newInstance(var2);
         var3.init(var0);
      } while(var3.getTransportBindingInfo() == null && !var3.isMessageSecurityEnabled());

      return var3;
   }

   private static final SecurityPolicyAssertionInfo newInstance(String var0) {
      try {
         return (SecurityPolicyAssertionInfo)Class.forName(var0).newInstance();
      } catch (InstantiationException var2) {
      } catch (IllegalAccessException var3) {
      } catch (ClassNotFoundException var4) {
      }

      return null;
   }

   static {
      supportedSecurityInfo.add("weblogic.wsee.security.policy12.internal.SecurityPolicyAssertionInfoImpl");
   }
}
