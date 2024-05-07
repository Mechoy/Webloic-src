package weblogic.security.utils;

import java.util.Locale;

/** @deprecated */
public class SignaturePredicate extends weblogic.entitlement.rules.SignaturePredicate {
   public String getDescription(Locale var1) {
      return "Deprecated in WLS 9.0, replaced with weblogic.entitlement.rules.SignaturePredicate\n\n" + super.getDescription(var1);
   }

   public boolean isDeprecated() {
      return true;
   }
}
