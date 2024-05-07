package weblogic.jms.extensions;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import weblogic.jms.JMSLogger;

public final class Schedule {
   private static final int MILLISECOND = 0;
   private static final int SECOND = 1;
   private static final int MINUTE = 2;
   private static final int HOUR_OF_DAY = 3;
   private static final int DAY_OF_MONTH = 4;
   private static final int MONTH = 5;
   private static final int DAY_OF_WEEK = 6;
   private static final int MILLIDIVISOR = 20;
   private static final String[] CALNAMES = new String[]{"ms", "sec", "min", "hr", "dom", "mth", "dow"};
   private static final int[] MINIMUMS = new int[]{0, 0, 0, 0, 1, 1, 1};
   private static final int[] MAXIMUMS = new int[]{49, 59, 59, 23, 31, 12, 7};
   private static final int[] CALCODES = new int[]{14, 13, 12, 11, 5, 2, 7};

   private static void handleParseError(char[] var0, int[] var1, String var2) throws ParseException {
      String var3 = "";

      for(int var4 = var1[0]; var4 > 0; --var4) {
         var3 = var3 + ' ';
      }

      throw new ParseException("Parse error at or before position " + var1[0] + " in scheduling string:\n   \"" + new String(var0) + "\"\n    " + var3 + "^ " + var2, var1[0]);
   }

   private static int parseInt(char[] var0, int[] var1, int var2, int var3, int var4) throws ParseException {
      if (var1[0] >= var0.length) {
         handleParseError(var0, var1, "Unexpected end of string. Expected '0'-'9', 'l', or 'last'.");
      }

      char var5 = var0[var1[0]];
      int var10002;
      if (var5 != 'l' && var5 != 'L') {
         if (var5 < '0' || var5 > '9') {
            handleParseError(var0, var1, "Expected '0'-'9', 'l', or 'last'.");
         }

         var10002 = var1[0]++;

         int var6;
         for(var6 = var5 - 48; var1[0] != var0.length && (var5 = var0[var1[0]]) >= '0' && var5 <= '9'; var6 = var6 * 10 + (var5 - 48)) {
            var10002 = var1[0]++;
         }

         if (var4 == 1) {
            if (var6 < var2 || var6 > var3) {
               handleParseError(var0, var1, "Value " + var6 + " out of range, expect " + var2 + " <= value <= " + var3);
            }
         } else {
            if (var6 < var2 || var6 >= (var3 + 1) * var4) {
               handleParseError(var0, var1, "Value " + var6 + " out of range, expect " + var2 + " <= value <= " + ((var3 + 1) * var4 - 1));
            }

            var6 /= var4;
         }

         return var6;
      } else {
         if (++var1[0] != var0.length && (var0[var1[0]] == 'a' || var0[var1[0]] == 'A') && ++var1[0] != var0.length && (var0[var1[0]] == 's' || var0[var1[0]] == 'S') && ++var1[0] != var0.length && (var0[var1[0]] == 't' || var0[var1[0]] == 'T')) {
            var10002 = var1[0]++;
         }

         return var3;
      }
   }

   private static long parseRange(char[] var0, int[] var1, int var2, int var3, int var4) throws ParseException {
      long var5 = 0L;
      if (var1[0] >= var0.length) {
         handleParseError(var0, var1, "Unexpected end of string.");
      }

      char var7 = var0[var1[0]];
      int var10002;
      if (var7 == '*') {
         var10002 = var1[0]++;
         return -1L;
      } else {
         int var8 = parseInt(var0, var1, var2, var3, var4);
         var5 = 1L << var8;
         if (var1[0] == var0.length) {
            return var5;
         } else {
            if (var0[var1[0]] == '-') {
               var10002 = var1[0]++;
               int var9 = parseInt(var0, var1, var2, var3, var4);
               if (var9 < var8) {
                  handleParseError(var0, var1, "Start of range (" + var8 + ") is greater than end of range (" + var9 + ").");
               }

               while(true) {
                  ++var8;
                  if (var8 > var9) {
                     break;
                  }

                  var5 |= 1L << var8;
               }
            }

            if (var1[0] == var0.length) {
               return var5;
            } else if (var0[var1[0]] == ',') {
               var10002 = var1[0]++;
               return var5 | parseRange(var0, var1, var2, var3, var4);
            } else {
               return var5;
            }
         }
      }
   }

   private static void skipWhiteSpace(char[] var0, int[] var1) {
      while(var0.length != var1[0] && (var0[var1[0]] == ' ' || var0[var1[0]] == '\t')) {
         int var10002 = var1[0]++;
      }

   }

   private static void parseCron(char[] var0, int[] var1, long[] var2) throws ParseException {
      skipWhiteSpace(var0, var1);

      for(int var3 = 0; var3 < MINIMUMS.length; ++var3) {
         var2[var3] |= parseRange(var0, var1, MINIMUMS[var3], MAXIMUMS[var3], var3 == 0 ? 20 : 1);
         if (var3 < MINIMUMS.length - 1) {
            if (var0.length == var1[0]) {
               handleParseError(var0, var1, "Unexpected end of string.");
            }

            int var4 = var1[0];
            skipWhiteSpace(var0, var1);
            if (var1[0] == var4) {
               handleParseError(var0, var1, "Expected white space character (tab or space).");
            }
         }
      }

      skipWhiteSpace(var0, var1);
   }

   private static long[][] parseCrons(String var0) throws ParseException {
      long[][] var1 = new long[1][MINIMUMS.length];
      long[] var2 = var1[0];
      int[] var3 = new int[1];
      if (var0 == null) {
         handleParseError("<null>".toCharArray(), var3, "Attempt to parse null string.");
      }

      char[] var4 = var0.toCharArray();

      try {
         parseCron(var4, var3, var2);

         while(var3[0] != var4.length) {
            if (var4[var3[0]] != ';') {
               handleParseError(var4, var3, "Unexpected character '" + var4[var3[0]] + "', expected ';' or end of string.");
            }

            int var10002 = var3[0]++;
            long[][] var5 = new long[var1.length + 1][];

            for(int var6 = 0; var6 < var1.length; ++var6) {
               var5[var6] = var1[var6];
            }

            var2 = var5[var1.length] = new long[MINIMUMS.length];
            var1 = var5;
            parseCron(var4, var3, var2);
         }

         for(int var8 = 0; var8 < var2.length; ++var8) {
            if (var2[var8] == 0L) {
               throw new Throwable("Programming error.");
            }
         }

         return var1;
      } catch (Throwable var7) {
         if (!(var7 instanceof ParseException)) {
            JMSLogger.logStackTrace(var7);
            handleParseError(var4, var3, "Unhandled exception. " + var7.toString());
         }

         throw (ParseException)var7;
      }
   }

   private static int minimumBitPosition(long var0, int var2) {
      int var3;
      for(var3 = var2; (var0 & 1L << var3) == 0L; ++var3) {
      }

      return var3;
   }

   private static int firstMatchingBitPos(long var0, int var2, int var3, int var4) {
      while(var2 <= var4) {
         if ((var0 & 1L << var2) != 0L) {
            return var2;
         }

         ++var2;
      }

      for(var2 = var3; (var0 & 1L << var2) == 0L; ++var2) {
      }

      return var2;
   }

   private static int addThis(long var0, int var2, int var3, int var4) {
      int var5;
      for(var5 = 0; var2 <= var4; ++var2) {
         if ((var0 & 1L << var2) != 0L) {
            return var5;
         }

         ++var5;
      }

      for(var2 = var3; (var0 & 1L << var2) == 0L; ++var2) {
         ++var5;
      }

      return var5;
   }

   private static void minimize(int var0, Calendar var1, long[] var2) {
      if (var0 != 0) {
         if (var0 == 6) {
            var0 = 4;
         }

         int var3 = minimumBitPosition(var2[0], MINIMUMS[0]);
         var1.set(14, var3 * 20);

         for(int var4 = 1; var4 < var0; ++var4) {
            int var5 = minimumBitPosition(var2[var4], MINIMUMS[var4]);
            if (var4 == 5) {
               --var5;
            } else if (var4 == 4) {
               int var6 = var1.getActualMaximum(5);
               if (var5 > var6) {
                  var5 = var6;
               }
            }

            var1.set(CALCODES[var4], var5);
         }

      }
   }

   private static void scheduleField(int var0, Calendar var1, long[] var2) {
      int var3 = var1.get(CALCODES[var0]);
      if (var0 == 5) {
         ++var3;
      }

      int var4;
      if (var0 == 0) {
         var4 = var3 % 20;
         if (var4 != 0) {
            var1.add(CALCODES[var0], 20 - var4);
            var3 = var1.get(CALCODES[var0]);
         }

         var3 /= 20;
      }

      var4 = addThis(var2[var0], var3, MINIMUMS[var0], MAXIMUMS[var0]);
      if (var4 != 0) {
         if (var0 == 0) {
            var4 *= 20;
         }

         if (var0 == 5) {
            var1.set(5, 1);
         }

         var1.add(CALCODES[var0], var4);
         minimize(var0, var1, var2);
      }

   }

   private static void nextScheduledTime(long[] var0, Calendar var1) {
      var1.setFirstDayOfWeek(1);
      var1.add(CALCODES[0], 1);
      if (var0[0] != -1L) {
         scheduleField(0, var1, var0);
      }

      if (var0[1] != -1L) {
         scheduleField(1, var1, var0);
      }

      if (var0[2] != -1L) {
         scheduleField(2, var1, var0);
      }

      if (var0[3] != -1L) {
         scheduleField(3, var1, var0);
      }

      int var2;
      int var3;
      int var4;
      if (var0[4] != -1L) {
         var2 = var1.get(5);
         var3 = firstMatchingBitPos(var0[4], var2, MINIMUMS[4], MAXIMUMS[4]);
         if (var2 != var3) {
            var4 = var1.getActualMaximum(5);
            if (var3 > var4) {
               var3 = var4;
            } else if (var3 < var2) {
               var1.set(5, 1);
               var1.add(2, 1);
            }

            var1.set(5, var3);
            minimize(4, var1, var0);
         }
      }

      if (var0[5] != -1L) {
         scheduleField(5, var1, var0);
      }

      if (var0[6] != -1L) {
         scheduleField(6, var1, var0);
         var2 = var1.get(2) + 1;
         if ((var0[5] & 1L << var2) == 0L) {
            nextScheduledTime(var0, var1);
         } else {
            var3 = var1.get(5);
            if ((var0[4] & 1L << var3) == 0L) {
               var4 = var1.getActualMaximum(5);
               int var5 = firstMatchingBitPos(var0[4], var3, MINIMUMS[4], MAXIMUMS[4]);
               if (var5 < var4 || var3 != var4) {
                  nextScheduledTime(var0, var1);
               }
            }
         }
      }

   }

   private static void nextScheduledTime(long[][] var0, Calendar var1) {
      Calendar var2 = null;
      if (var0.length > 1) {
         var2 = (Calendar)var1.clone();
      }

      nextScheduledTime(var0[0], var1);

      for(int var3 = 1; var3 < var0.length; ++var3) {
         Calendar var4 = null;
         if (var3 + 1 < var0.length) {
            var4 = (Calendar)var2.clone();
         }

         nextScheduledTime(var0[var3], var2);
         if (var2.getTime().getTime() < var1.getTime().getTime()) {
            var1.setTime(var2.getTime());
         }

         var2 = var4;
      }

   }

   private static String longAsBitsReversed(long var0) {
      int var2 = 64;
      String var3 = "";

      while(true) {
         --var2;
         if (var2 < 0) {
            return var3;
         }

         if ((var0 & 1L) != 0L) {
            var3 = var3 + '1';
         } else {
            var3 = var3 + '0';
         }

         var0 >>>= 1;
      }
   }

   private static String leftJustify5(String var0) {
      while(var0.length() < 5) {
         var0 = ' ' + var0;
      }

      return var0;
   }

   private static String leftJustify5(int var0) {
      return leftJustify5(var0 + "");
   }

   private static void printCalendar(Calendar var0, boolean var1) {
      String[] var2 = new String[]{"yr", "dow", "mth", "dom", "hr", "mn", "sec", "ms"};
      int[] var3 = new int[]{1, 7, 2, 5, 11, 12, 13, 14};
      int var4;
      if (var1) {
         for(var4 = var3.length - 1; var4 >= 0; --var4) {
            System.out.print(leftJustify5(var2[var4]));
         }

         System.out.println();
      }

      for(var4 = var3.length - 1; var4 >= 0; --var4) {
         System.out.print(leftJustify5(var0.get(var3[var4]) + (var3[var4] == 2 ? 1 : 0)));
      }

      System.out.println();
   }

   private static void printSchedule(long[][] var0) {
      System.out.print(leftJustify5("") + " ");

      int var1;
      for(var1 = 0; var1 < 6; ++var1) {
         System.out.print("" + var1 + "         ");
      }

      System.out.println();
      System.out.print(leftJustify5("") + " ");

      int var2;
      for(var1 = 0; var1 < 6; ++var1) {
         for(var2 = 0; var2 <= 9; ++var2) {
            System.out.print(var2);
         }
      }

      System.out.println();
      System.out.println();

      for(var1 = 0; var1 < var0.length; ++var1) {
         if (var1 > 0) {
            System.out.println();
         }

         for(var2 = 0; var2 < var0[var1].length; ++var2) {
            System.out.print(leftJustify5(CALNAMES[var2]) + " ");
            System.out.println(longAsBitsReversed(var0[var1][var2]));
         }
      }

   }

   private static void throwUsage() throws Exception {
      throw new Exception("\nUsage:  java weblogic.jms.extensions.Schedule \"schedule\" [msecs secs mins hrs day mnth yr]\nExample:  java weblogic.jms.extensions.Schedule \"* * * * * * 1\" 0 0 0 0 23 4 2001\n     (first Sunday after 23rd April 2001)");
   }

   private static void testEOM() throws ParseException {
      long[][] var0 = parseCrons("* * * * 31 * *");

      for(int var1 = 0; var1 < 367; ++var1) {
         GregorianCalendar var2 = new GregorianCalendar(2001, 0, 1, 0, 0, 0);
         var2.add(6, var1);
         printCalendar(var2, true);
         nextScheduledTime((long[][])var0, var2);
         printCalendar(var2, false);
         System.out.println();
      }

   }

   public static void parseSchedule(String var0) throws ParseException {
      parseCrons(var0);
   }

   public static Calendar nextScheduledTime(String var0, Calendar var1) throws ParseException {
      int var2 = var1.getFirstDayOfWeek();
      Calendar var3 = (Calendar)var1.clone();
      nextScheduledTime(parseCrons(var0), var3);
      var3.setFirstDayOfWeek(var2);
      return var3;
   }

   public static Calendar nextScheduledTime(String var0) throws ParseException {
      return nextScheduledTime((String)var0, new GregorianCalendar());
   }

   public static long nextScheduledTimeInMillis(String var0, long var1) throws ParseException {
      GregorianCalendar var3 = new GregorianCalendar();
      var3.setTime(new Date(var1));
      nextScheduledTime((long[][])parseCrons(var0), var3);
      return var3.getTime().getTime();
   }

   public static long nextScheduledTimeInMillis(String var0) throws ParseException {
      return nextScheduledTimeInMillis(var0, System.currentTimeMillis());
   }

   public static long nextScheduledTimeInMillisRelative(String var0, long var1) throws ParseException {
      return nextScheduledTimeInMillis(var0, var1) - var1;
   }

   public static long nextScheduledTimeInMillisRelative(String var0) throws ParseException {
      return nextScheduledTimeInMillisRelative(var0, System.currentTimeMillis());
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         throwUsage();
      }

      GregorianCalendar var1 = new GregorianCalendar();
      if (var0.length > 1) {
         if (var0.length != 8) {
            throwUsage();
         }

         int var2 = Integer.parseInt(var0[1]);
         int var3 = Integer.parseInt(var0[2]);
         int var4 = Integer.parseInt(var0[3]);
         int var5 = Integer.parseInt(var0[4]);
         int var6 = Integer.parseInt(var0[5]);
         int var7 = Integer.parseInt(var0[6]) - 1;
         int var8 = Integer.parseInt(var0[7]);
         var1 = new GregorianCalendar(var8, var7, var6, var5, var4, var3);
         var1.set(14, var2);
      }

      var1.setFirstDayOfWeek(1);
      long[][] var9 = parseCrons(var0[0]);
      printSchedule(var9);
      System.out.println();
      printCalendar(var1, true);
      nextScheduledTime((long[][])var9, var1);
      printCalendar(var1, false);
      System.out.println();
   }
}
