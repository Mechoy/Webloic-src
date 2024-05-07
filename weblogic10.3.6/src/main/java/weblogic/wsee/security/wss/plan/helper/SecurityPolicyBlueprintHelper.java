package weblogic.wsee.security.wss.plan.helper;

import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.util.Verbose;

public class SecurityPolicyBlueprintHelper {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyBlueprintHelper.class);
   private static final boolean debug = false;

   public static boolean shouldIncludeToken(TokenAssertion.TokenInclusion var0, boolean var1) throws SecurityPolicyArchitectureException {
      if (null != var0 && !var0.equals(TokenAssertion.TokenInclusion.ALWAYS)) {
         if (var0.equals(TokenAssertion.TokenInclusion.NEVER)) {
            return false;
         } else if (var0.equals(TokenAssertion.TokenInclusion.TO_RECIPIENT_ONLY) && var1) {
            return true;
         } else if (var0.equals(TokenAssertion.TokenInclusion.TO_INITIATOR_ONLY) && !var1) {
            return true;
         } else if (var0.equals(TokenAssertion.TokenInclusion.ONCE)) {
            throw new SecurityPolicyArchitectureException("Once is not a supported Token Inclusion");
         } else {
            return false;
         }
      } else {
         return true;
      }
   }
}
