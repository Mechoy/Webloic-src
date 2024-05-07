package weblogic.entitlement.rules;

import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public class ExcludedPolicy extends BasePredicate {
   private static final String VERSION = "1.0";

   public ExcludedPolicy() {
      super("ExcludedPolicyName", "ExcludedPolicyDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      return false;
   }

   public String getVersion() {
      return "1.0";
   }
}
