package weblogic.entitlement.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import weblogic.security.providers.authorization.EnumeratedPredicateArgument;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class DayOfWeekPredicateArgument extends BasePredicateArgument implements EnumeratedPredicateArgument {
   private static final List DAYS;

   public DayOfWeekPredicateArgument() {
      this("DayOfWeekPredicateArgumentName", "DayOfWeekPredicateArgumentDescription", DayOfWeek.MONDAY);
   }

   public DayOfWeekPredicateArgument(String var1, String var2, DayOfWeek var3) {
      super(var1, var2, DayOfWeek.class, var3);
   }

   public List getAllowedValues() {
      return DAYS;
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      for(int var3 = 0; var3 < DAYS.size(); ++var3) {
         DayOfWeek var4 = (DayOfWeek)DAYS.get(var3);
         if (var4.getLocalizedName(var2).equals(var1)) {
            return var4;
         }
      }

      String var5 = (new PredicateTextFormatter(var2)).getInvalidDayOfWeekMessage(var1);
      throw new IllegalPredicateArgumentException(var5);
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      return ((DayOfWeek)var1).getLocalizedName(var2);
   }

   public Object parseExprValue(String var1) throws IllegalPredicateArgumentException {
      for(int var2 = 0; var2 < DAYS.size(); ++var2) {
         DayOfWeek var3 = (DayOfWeek)DAYS.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      throw new IllegalPredicateArgumentException("Unknown day of the week " + var1);
   }

   public String formatExprValue(Object var1) throws IllegalPredicateArgumentException {
      this.validateValue(var1, (Locale)null);
      return ((DayOfWeek)var1).getName();
   }

   public static void main(String[] var0) throws Exception {
      test(new DayOfWeekPredicateArgument(), var0[0]);
   }

   static {
      ArrayList var0 = new ArrayList(7);
      var0.add(DayOfWeek.SUNDAY);
      var0.add(DayOfWeek.MONDAY);
      var0.add(DayOfWeek.TUESDAY);
      var0.add(DayOfWeek.WEDNESDAY);
      var0.add(DayOfWeek.THURSDAY);
      var0.add(DayOfWeek.FRIDAY);
      var0.add(DayOfWeek.SATURDAY);
      DAYS = Collections.unmodifiableList(var0);
   }
}
