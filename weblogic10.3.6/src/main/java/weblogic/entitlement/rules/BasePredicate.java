package weblogic.entitlement.rules;

import java.util.Locale;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.Predicate;
import weblogic.security.providers.authorization.PredicateArgument;

public abstract class BasePredicate implements Predicate {
   private String displayNameId;
   private String descriptionId;

   public BasePredicate(String var1, String var2) {
      this.displayNameId = var1;
      this.descriptionId = var2;
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length > 0) {
         throw new IllegalPredicateArgumentException("No arguments expected");
      }
   }

   public boolean isSupportedResource(String var1) {
      return true;
   }

   public String getDisplayName(Locale var1) {
      return Localizer.getText(this.displayNameId, var1);
   }

   public String getDescription(Locale var1) {
      return Localizer.getText(this.descriptionId, var1);
   }

   public int getArgumentCount() {
      return 0;
   }

   public PredicateArgument getArgument(int var1) {
      throw new IndexOutOfBoundsException("This predicate takes no arguments");
   }

   public boolean isDeprecated() {
      return false;
   }
}
