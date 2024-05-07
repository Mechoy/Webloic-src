package weblogic.entitlement.rules;

import java.util.Calendar;
import java.util.TimeZone;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;

public abstract class DayOfMonthPredicate extends BasePredicate {
   private static final String VERSION = "1.0";
   private static PredicateArgument DAY_OF_MONTH_ARG = new DayOfMonthPredicateArgument();
   private static PredicateArgument TIME_ZONE_ARG = new TimeZonePredicateArgument();
   private static PredicateArgument[] arguments;
   private int dayOfMonth = 0;
   private TimeZone timeZone = null;

   public DayOfMonthPredicate(String var1, String var2) {
      super(var1, var2);
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length != 0) {
         if (var1.length > 2) {
            throw new IllegalPredicateArgumentException("Maximum two arguments are expected");
         } else {
            this.dayOfMonth = this.parseDayOfMonth(var1[0]);
            this.timeZone = var1.length > 1 ? this.parseTimeZone(var1[1]) : null;
         }
      } else {
         throw new IllegalPredicateArgumentException("At least one argument is expected");
      }
   }

   protected int getDayOfMonthArgument() {
      return this.dayOfMonth;
   }

   protected TimeZone getTimeZoneArgument() {
      return this.timeZone;
   }

   protected Calendar getCurrentDate() {
      return Calendar.getInstance(this.timeZone == null ? TimeZone.getDefault() : this.timeZone);
   }

   protected int getDayOfMonth(Calendar var1) {
      int var2 = this.dayOfMonth;
      if (var2 < 0) {
         var2 += 1 + var1.getActualMaximum(5);
      }

      return var2;
   }

   private int parseDayOfMonth(String var1) throws IllegalPredicateArgumentException {
      return ((Number)DAY_OF_MONTH_ARG.parseExprValue(var1)).intValue();
   }

   private TimeZone parseTimeZone(String var1) throws IllegalPredicateArgumentException {
      return (TimeZone)TIME_ZONE_ARG.parseExprValue(var1);
   }

   public String getVersion() {
      return "1.0";
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }

   static {
      arguments = new PredicateArgument[]{DAY_OF_MONTH_ARG, TIME_ZONE_ARG};
   }
}
