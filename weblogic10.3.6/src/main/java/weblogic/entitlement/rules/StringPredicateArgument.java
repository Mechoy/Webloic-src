package weblogic.entitlement.rules;

import java.util.Locale;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class StringPredicateArgument extends BasePredicateArgument {
   public StringPredicateArgument() {
      this("StringPredicateArgumentName", "StringPredicateArgumentDescription", (String)null);
   }

   public StringPredicateArgument(String var1, String var2, String var3) {
      super(var1, var2, String.class, var3);
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      return var1;
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      return (String)var1;
   }
}
