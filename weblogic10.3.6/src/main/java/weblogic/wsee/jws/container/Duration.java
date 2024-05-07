package weblogic.wsee.jws.container;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Duration {
   static final long serialVersionUID = 1L;
   private static final int VALUE_UNKNOWN = -1;
   private static String[] _durationNames = new String[]{"years", "months", "days", "hours", "minutes", "seconds"};
   private long years;
   private long months;
   private long days;
   private long hours;
   private long minutes;
   private long seconds;

   public Duration() {
   }

   public Duration(long var1) {
      this.seconds = var1;
   }

   public Duration(int var1) {
      this.seconds = (long)var1;
   }

   public Duration(long var1, long var3, long var5, long var7, long var9, long var11) {
      this.years = var1;
      this.months = var3;
      this.days = var5;
      this.hours = var7;
      this.minutes = var9;
      this.seconds = var11;
   }

   public Duration(String var1) throws IllegalArgumentException {
      this.setFromDescription(var1);
   }

   public long getYears() {
      return this.years;
   }

   public long getMonths() {
      return this.months;
   }

   public long getDays() {
      return this.days;
   }

   public long getHours() {
      return this.hours;
   }

   public long getMinutes() {
      return this.minutes;
   }

   public long getSeconds() {
      return this.seconds;
   }

   public boolean isZero() {
      return this.years == 0L && this.months == 0L && this.days == 0L && this.hours == 0L && this.minutes == 0L && this.seconds == 0L;
   }

   public String toString() {
      return this.years + "y" + this.months + "m" + this.days + "dt" + this.hours + "h" + this.minutes + "m" + this.seconds + "s";
   }

   public long computeDate(long var1) {
      Date var3 = var1 == -1L ? new Date() : new Date(var1);
      return this.computeDate(var3).getTime();
   }

   public Date computeDate(Date var1) {
      GregorianCalendar var2 = new GregorianCalendar();
      if (var1 != null) {
         var2.setTime(var1);
      }

      gregory(var2, 1, this.getYears());
      gregory(var2, 2, this.getMonths());
      gregory(var2, 5, this.getDays());
      gregory(var2, 10, this.getHours());
      gregory(var2, 12, this.getMinutes());
      gregory(var2, 13, this.getSeconds());
      return var2.getTime();
   }

   public long convertToSeconds(Date var1) {
      if (var1 == null) {
         var1 = new Date();
      }

      return (this.computeDate(var1).getTime() - var1.getTime() + 499L) / 1000L;
   }

   private static void gregory(GregorianCalendar var0, int var1, long var2) {
      while(var2 != 0L) {
         int var4;
         if (var2 > 2147483647L) {
            var4 = Integer.MAX_VALUE;
         } else if (var2 < -2147483648L) {
            var4 = Integer.MIN_VALUE;
         } else {
            var4 = (int)var2;
         }

         var0.add(var1, var4);
         var2 -= (long)var4;
      }

   }

   private void setFromDescription(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1.toLowerCase(Locale.ENGLISH);
         boolean var2 = false;
         int var3 = 0;
         if (var1.charAt(0) == 'p') {
            var2 = true;
            ++var3;
         }

         long[] var4 = new long[6];
         long var5 = -1L;
         int var7 = 0;
         boolean var8 = false;

         while(true) {
            while(var3 < var1.length()) {
               char var9 = var1.charAt(var3);
               int var10 = var3;
               if (var8) {
                  var7 = 3;
                  var8 = false;
               }

               if (Character.isWhitespace(var9)) {
                  if (var2) {
                     throw new IllegalArgumentException("Whitespace characters not allowed");
                  }

                  do {
                     ++var10;
                  } while(var10 < var1.length() && Character.isWhitespace(var1.charAt(var10)));
               } else if (Character.isDigit(var9)) {
                  if (var5 != -1L) {
                     throw new IllegalArgumentException("Invalid format: multiple numeric values without delimiter");
                  }

                  do {
                     ++var10;
                  } while(var10 < var1.length() && Character.isDigit(var1.charAt(var10)));

                  var5 = Long.parseLong(var1.substring(var3, var10));
               } else {
                  if (!Character.isLetter(var9)) {
                     throw new IllegalArgumentException("Invalid character: " + var9);
                  }

                  do {
                     ++var10;
                  } while(var10 < var1.length() && Character.isLetter(var1.charAt(var10)));

                  String var11 = var1.substring(var3, var10);
                  int var12 = 0;

                  int var13;
                  for(var13 = 1; var13 <= 4; ++var13) {
                     if (var11.endsWith("time".substring(0, var13))) {
                        var12 = var13;
                     }
                  }

                  if (var12 > 0) {
                     if (var2 && var12 != 1) {
                        throw new IllegalArgumentException("Single character duration type expected");
                     }

                     var8 = true;
                     if (var11.length() == var12) {
                        var3 += var12;
                        continue;
                     }

                     var11 = var11.substring(0, var11.length() - var12);
                  }

                  if (var5 == -1L) {
                     throw new IllegalArgumentException("Please specify a valid duration. A duration value may be expressed in seconds, minutes, hours, days, months, or years. Examples: \"1 day\", \"2 minutes 30 seconds\", \"2 min 30 s\".");
                  }

                  if (var2 && var11.length() > 1) {
                     throw new IllegalArgumentException("Single character duration type expected");
                  }

                  for(var13 = var7; var13 < _durationNames.length; ++var13) {
                     if (_durationNames[var13].startsWith(var11)) {
                        var4[var13] = var5;
                        var5 = -1L;
                        if (var2) {
                           var7 = var13 + 1;
                        }
                        break;
                     }
                  }

                  if (var13 == _durationNames.length) {
                     throw new IllegalArgumentException("Unexpected duration value: " + var11);
                  }
               }

               var3 = var10;
            }

            if (var5 != -1L) {
               throw new IllegalArgumentException("Please specify a valid duration. A duration value may be expressed in seconds, minutes, hours, days, months, or years. Examples: \"1 day\", \"2 minutes 30 seconds\", \"2 min 30 s\".");
            }

            this.years = var4[0];
            this.months = var4[1];
            this.days = var4[2];
            this.hours = var4[3];
            this.minutes = var4[4];
            this.seconds = var4[5];
            return;
         }
      }
   }
}
