package weblogic.entitlement.rules;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class TimePredicateArgument extends BasePredicateArgument {
   public static String TimePredicateArgumentName = "TimePredicateArgumentName";
   public static String TimePredicateStartTimeArgumentName = "TimePredicateStartTimeArgumentName";
   public static String TimePredicateEndTimeArgumentName = "TimePredicateEndTimeArgumentName";

   public TimePredicateArgument() {
      this("TimePredicateArgumentName", "TimePredicateArgumentDescription", (TimeOfDay)null);
   }

   public TimePredicateArgument(String var1, String var2, TimeOfDay var3) {
      super(var1, var2, TimeOfDay.class, var3);
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      DateFormat var3 = DateFormat.getTimeInstance(2, var2);
      var3.setTimeZone(TimeZone.getTimeZone("GMT"));
      var3.setLenient(false);

      try {
         Date var4 = var3.parse(var1);
         return new TimeOfDay((int)var4.getTime());
      } catch (Exception var5) {
         throw new IllegalPredicateArgumentException(var5.getMessage());
      }
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      DateFormat var3 = DateFormat.getTimeInstance(2);
      var3.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var3.format(new Date((long)((TimeOfDay)var1).getTime()));
   }

   public Object parseExprValue(String var1) throws IllegalPredicateArgumentException {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      StringTokenizer var5 = new StringTokenizer(var1, ":", false);
      if (var5.hasMoreTokens()) {
         var2 = Integer.parseInt(var5.nextToken().trim());
         if (var5.hasMoreTokens()) {
            var3 = Integer.parseInt(var5.nextToken().trim());
            if (var5.hasMoreTokens()) {
               var4 = Integer.parseInt(var5.nextToken().trim());
               if (var5.hasMoreTokens()) {
                  throw new IllegalPredicateArgumentException("Unexpected time format");
               }
            }
         }
      }

      try {
         return new TimeOfDay(var2, var3, var4);
      } catch (IllegalArgumentException var7) {
         throw new IllegalPredicateArgumentException(var7.getMessage());
      }
   }

   public String formatExprValue(Object var1) throws IllegalPredicateArgumentException {
      this.validateValue(var1, (Locale)null);
      TimeOfDay var2 = (TimeOfDay)var1;
      return var2.getHours() + ":" + var2.getMinutes() + ":" + var2.getSeconds();
   }

   public static void main(String[] var0) throws Exception {
      test(new TimePredicateArgument(), var0[0]);
   }

   public String getDescription(Locale var1) {
      if (this.displayNameId.equals(TimePredicateArgumentName)) {
         return this.getDescription(Localizer.getText("TimePredicateArgumentDescription", var1), var1);
      } else {
         return this.displayNameId.equals(TimePredicateStartTimeArgumentName) ? this.getDescription(Localizer.getText("TimePredicateStartTimeArgumentDescription", var1), var1) : this.getDescription(Localizer.getText("TimePredicateEndTimeArgumentDescription", var1), var1);
      }
   }

   public String getDescription(String var1, Locale var2) {
      ArrayList var3 = new ArrayList();
      String var4 = this.getFormatedTimePattern(var2, 2);
      var3.add(var4);
      GregorianCalendar var5 = new GregorianCalendar(var2);
      var5.set(10, 20);
      var5.set(12, 45);
      var5.set(13, 35);
      var5.set(9, 0);
      Date var6 = new Date(var5.getTimeInMillis());
      var3.add(this.getFormatedDateString(this.getTimePattern(var2), var2, var6));
      return this.parseMessage(new StringBuffer(var1), var3);
   }
}
