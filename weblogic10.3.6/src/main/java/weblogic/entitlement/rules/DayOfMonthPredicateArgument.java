package weblogic.entitlement.rules;

import java.util.Locale;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.RangePredicateArgument;

public class DayOfMonthPredicateArgument extends NumberPredicateArgument implements RangePredicateArgument {
   private static final Integer MAX_VALUE = new Integer(31);
   private static final Integer MIN_VALUE = new Integer(-31);

   public DayOfMonthPredicateArgument() {
      this("DayOfMonthPredicateArgumentName", "DayOfMonthPredicateArgumentDescription", 1);
   }

   public DayOfMonthPredicateArgument(String var1, String var2, int var3) {
      super(var1, var2, Integer.class, new Integer(var3), true);
   }

   public Comparable getMinValue() {
      return MIN_VALUE;
   }

   public Comparable getMaxValue() {
      return MAX_VALUE;
   }

   public void validateValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      super.validateValue(var1, var2);
      int var3 = (Integer)var1;
      if (var3 < MIN_VALUE || var3 > MAX_VALUE) {
         String var4 = Localizer.getText("InvalidDayOfMonth", var2);
         throw new IllegalPredicateArgumentException(var4);
      }
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      Number var3 = (Number)super.parseValue(var1, var2);
      Integer var4 = new Integer(var3.intValue());
      this.validateValue(var4, var2);
      return var4;
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      return super.formatValue(var1, var2);
   }

   public static void main(String[] var0) throws Exception {
      test(new DayOfMonthPredicateArgument(), var0[0]);
   }
}
