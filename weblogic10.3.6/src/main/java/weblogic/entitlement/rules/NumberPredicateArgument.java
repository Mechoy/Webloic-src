package weblogic.entitlement.rules;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class NumberPredicateArgument extends BasePredicateArgument {
   private boolean parseIntegerOnly;

   public NumberPredicateArgument() {
      this("NumberPredicateArgumentName", "NumberPredicateArgumentDescription", (Number)null);
   }

   public NumberPredicateArgument(String var1, String var2, Number var3) {
      super(var1, var2, Number.class, var3);
      this.parseIntegerOnly = false;
   }

   protected NumberPredicateArgument(String var1, String var2, Class var3, Number var4, boolean var5) {
      super(var1, var2, var3, var4);
      this.parseIntegerOnly = false;
      this.parseIntegerOnly = var5;
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      ParsePosition var3 = new ParsePosition(0);
      NumberFormat var4 = NumberFormat.getNumberInstance(var2);
      var4.setParseIntegerOnly(this.parseIntegerOnly);
      Number var5 = var4.parse(var1, var3);
      if (var5 != null && var3.getIndex() >= var1.length()) {
         return var5;
      } else {
         String var6 = Localizer.getText(this.parseIntegerOnly ? "NotIntegerValue" : "NotNumericValue", var2);
         throw new IllegalPredicateArgumentException(var6);
      }
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      super.validateValue(var1, var2);
      Number var3 = (Number)var1;
      NumberFormat var4 = NumberFormat.getInstance(var2);
      return this.parseIntegerOnly ? var4.format(var3.longValue()) : var4.format(var3.doubleValue());
   }
}
