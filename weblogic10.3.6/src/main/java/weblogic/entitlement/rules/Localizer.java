package weblogic.entitlement.rules;

import java.util.Locale;
import weblogic.i18ntools.L10nLookup;

public class Localizer {
   public static String getText(String var0, Locale var1) {
      weblogic.i18n.Localizer var2 = L10nLookup.getLocalizer(var1, "weblogic.entitlement.rules.PredicateTextLocalizer");
      return var2.get(var0);
   }
}
