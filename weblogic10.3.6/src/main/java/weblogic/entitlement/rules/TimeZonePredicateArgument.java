package weblogic.entitlement.rules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class TimeZonePredicateArgument extends BasePredicateArgument {
   public TimeZonePredicateArgument() {
      this("TimeZonePredicateArgumentName", "TimeZonePredicateArgumentDescription", (String)null, true);
   }

   public TimeZonePredicateArgument(String var1, String var2, String var3, boolean var4) {
      super(var1, var2, TimeZone.class, var3, var4);
   }

   public Object parseValue(String var1, Locale var2) throws IllegalPredicateArgumentException {
      TimeZone var3 = TimeZone.getTimeZone(var1);
      if (var3.getRawOffset() == 0) {
         if (var1 == null || !var1.startsWith("GMT")) {
            String var9 = Localizer.getText("InvalidTimeZoneFormat", var2);
            throw new IllegalPredicateArgumentException(var9);
         }

         if (var1.length() > 3) {
            char var4 = var1.charAt(3);
            if (var4 != '-' && var4 != '+') {
               String var10 = Localizer.getText("InvalidTimeZoneFormat", var2);
               throw new IllegalPredicateArgumentException(var10);
            }

            int var5 = 0;

            for(int var6 = 4; var6 < var1.length(); ++var6) {
               char var7 = var1.charAt(var6);
               String var8;
               if (var7 != '0' && var7 != ':') {
                  var8 = Localizer.getText("InvalidTimeZoneFormat", var2);
                  throw new IllegalPredicateArgumentException(var8);
               }

               if (var7 == ':') {
                  ++var5;
                  if (var5 > 1) {
                     var8 = Localizer.getText("InvalidTimeZoneFormat", var2);
                     throw new IllegalPredicateArgumentException(var8);
                  }
               }
            }
         }
      }

      return var3;
   }

   public String formatValue(Object var1, Locale var2) throws IllegalPredicateArgumentException {
      super.validateValue(var1, var2);
      TimeZone var3 = (TimeZone)var1;
      return var3.getDisplayName(false, 0, var2);
   }

   public static void main(String[] var0) throws Exception {
      test(new TimeZonePredicateArgument(), var0[0]);
   }

   public String getDescription(Locale var1) {
      return this.getDescription(Localizer.getText("TimeZonePredicateArgumentDescription", var1), var1);
   }

   public String getDescription(String var1, Locale var2) {
      ArrayList var3 = new ArrayList();
      String var4 = this.getFormatedTimePattern(var2);
      var3.add(var4);
      var3.add(var4);
      GregorianCalendar var5 = new GregorianCalendar(var2);
      var5.set(10, 5);
      var5.set(12, 0);
      Date var6 = new Date(var5.getTimeInMillis());
      var3.add(this.getFormatedDateString(var4, var2, var6));
      return this.parseMessage(new StringBuffer(var1), var3);
   }

   public String getFormatedTimePattern(Locale var1) {
      DateFormat var2 = DateFormat.getTimeInstance(3, var1);
      SimpleDateFormat var3 = (SimpleDateFormat)var2;
      String var4 = var3.toPattern();
      StringBuffer var5 = new StringBuffer(var3.toPattern());
      int var6 = var5.indexOf("a");
      if (var6 > 0) {
         var5 = var5.delete(var6 - 1, var6 + 1);
      }

      return var5.toString();
   }
}
