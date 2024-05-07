package weblogic.entitlement.rules;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class DatePredicateArgument extends BasePredicateArgument {
   private long defaultTime;

   public DatePredicateArgument(long var1) {
      this("DatePredicateArgumentName", "DatePredicateArgumentDescription", new Date(), var1);
   }

   public DatePredicateArgument(String var1, String var2, Date var3, long var4) {
      super(var1, var2, Date.class, var3);
      this.defaultTime = 0L;
      long var6 = 86400000L;
      if (var4 >= 0L && var4 <= var6) {
         this.defaultTime = var4;
      } else {
         throw new IllegalArgumentException("Default time parameter must be > 0 and < " + var6);
      }
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      try {
         return this.parseValueOrig(var1, var2);
      } catch (IllegalPredicateArgumentException var6) {
         try {
            DateFormat var4 = DateFormat.getDateTimeInstance(3, 2, var2);
            var4.setTimeZone(TimeZone.getTimeZone("GMT"));
            return var4.parse(var1);
         } catch (ParseException var5) {
            throw var6;
         }
      }
   }

   private Object parseValueOrig(String var1, Locale var2) throws IllegalPredicateArgumentException {
      TimeZone var3 = TimeZone.getTimeZone("GMT");
      DateFormat var4 = DateFormat.getDateInstance(3, var2);
      var4.setLenient(false);
      var4.setTimeZone(var3);
      ParsePosition var5 = new ParsePosition(0);
      Date var6 = var4.parse(var1, var5);
      if (var6 == null) {
         String var12 = Localizer.getText("InvalidDateFormat", var2);
         throw new IllegalPredicateArgumentException(var12);
      } else {
         long var7;
         if (var1.substring(var5.getIndex()).trim().length() > 0) {
            DateFormat var9 = DateFormat.getTimeInstance(2, var2);
            var9.setLenient(false);
            var9.setTimeZone(var3);
            Date var10 = var9.parse(var1, var5);
            if (var10 == null) {
               String var11 = Localizer.getText("InvalidTimeFormat", var2);
               throw new IllegalPredicateArgumentException(var11);
            }

            var7 = var10.getTime();
         } else {
            var7 = this.defaultTime;
         }

         return new Date(var6.getTime() + var7);
      }
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      this.validateValue(var1, var2);
      DateFormat var3 = DateFormat.getDateTimeInstance(3, 2, var2);
      var3.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var3.format((Date)var1);
   }

   public Object parseExprValue(String var1) throws IllegalPredicateArgumentException {
      SimpleDateFormat var2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      var2.setTimeZone(TimeZone.getTimeZone("GMT"));
      var2.setLenient(false);

      try {
         return var2.parse(var1);
      } catch (ParseException var4) {
         throw new IllegalPredicateArgumentException(var4.getMessage());
      }
   }

   public String formatExprValue(Object var1) throws IllegalPredicateArgumentException {
      this.validateValue(var1, (Locale)null);
      SimpleDateFormat var2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      var2.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var2.format((Date)var1);
   }

   public static void main(String[] var0) throws Exception {
      test(new DatePredicateArgument(0L), var0[0]);
   }

   public String getDescription(Locale var1) {
      ArrayList var2 = new ArrayList();
      String var3 = this.getDatePattern(var1);
      var2.add(var3);
      GregorianCalendar var4 = new GregorianCalendar(var1);
      var4.set(1, 2006);
      var4.set(2, 3);
      var4.set(5, 25);
      Date var5 = new Date(var4.getTimeInMillis());
      var2.add(this.getFormatedDateString(var3, var1, var5));
      var2.add(this.getFormatedDateTimePattern(var1));
      GregorianCalendar var6 = new GregorianCalendar(var1);
      var6.set(1, 2006);
      var6.set(2, 3);
      var6.set(5, 25);
      var6.set(10, 20);
      var6.set(12, 45);
      var6.set(13, 35);
      var6.set(9, 0);
      Date var7 = new Date(var6.getTimeInMillis());
      var2.add(this.getFormatedDateString(this.getDateTimePattern(var1), var1, var7));
      StringBuffer var8 = new StringBuffer(Localizer.getText("DatePredicateArgumentDescription", var1));
      return this.parseMessage(var8, var2);
   }
}
