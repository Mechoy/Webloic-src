package weblogic.wsee.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.xml.rpc.JAXRPCException;
import weblogic.diagnostics.debug.DebugLogger;

public class Verbose {
   private static boolean verboseOn = false;
   private static HashMap<String, Level> startsWith;
   private static HashMap<String, Level> equals;
   private static HashMap<String, Level> notStartsWith;
   private static HashMap<String, Level> notEquals;
   private static PrintStream out;
   private static DateFormat df;
   private static boolean allwaysOn;
   private static final String WEBSERVICE = "webservice";
   private static DebugLogger logger;
   private static final boolean timestamp;
   private static final boolean threadstamp;
   private static Handler handler;

   private static void init() {
      String var0 = System.getProperty("weblogic.wsee.verbose");
      String var1 = System.getProperty("weblogic.wsee.verbose.subcomponents");
      if (var0 == null && var1 == null) {
         verboseOn = checkDebugLogger();
         var0 = "*";
      } else {
         verboseOn = true;
      }

      if (verboseOn) {
         banner("Webservice running in Verbose mode " + (timestamp ? " with timestamps enabled" : ""));
         System.out.println("WSEE verbose timestamping = " + timestamp + " in class loader: " + Thread.currentThread().getContextClassLoader());
         System.out.println("WSEE verbose threadstamping = " + threadstamp + " in class loader: " + Thread.currentThread().getContextClassLoader());
         if (var0 != null) {
            parseProperty(var0);
         }

         if (var1 != null) {
            expandSubcomponentProperty(var1);
         }
      }

      setupLogging();
   }

   public static void setupLogging() {
      setLoggingLevelsAndAddHandler(handler);
   }

   public static void setDateFormat(DateFormat var0) {
      df = var0;
   }

   private static boolean checkDebugLogger() {
      logger = DebugLogger.getDebugLogger("webservice");
      return logger.isDebugEnabled();
   }

   public static void setVerbose(boolean var0) {
      allwaysOn = var0;
   }

   public static void log(Object var0, Throwable var1) {
      synchronized(out) {
         log(var0, 2);
         log(var1, 2);
         dumpThrowable(var1);
      }
   }

   public static void log(Object var0) {
      String var1 = expand(var0);
      log(var1, 2);
   }

   public static void logException(Throwable var0) {
      flush();
      synchronized(out) {
         if (var0 != null) {
            log(var0, 2);
            dumpThrowable(var0);
         }
      }
   }

   private static void dumpThrowable(Throwable var0) {
      var0.printStackTrace(out);
      out.flush();
      if (var0 instanceof JAXRPCException) {
         Throwable var1 = ((JAXRPCException)var0).getLinkedCause();
         if (var1 != null) {
            say("-- Caused by --");
            dumpThrowable(var1);
         }
      }

   }

   public static void logArgs(Object... var0) {
      flush();
      StackTraceElement[] var1 = Thread.currentThread().getStackTrace();
      StackTraceElement var2 = var1[3];
      String var3 = shortName(var2.getClassName());
      String var4 = var2.getMethodName();
      synchronized(out) {
         writeWSEEStart();
         writeTimestamp();
         out.print(var3);
         out.print(".");
         out.print(var4);
         out.println("-->");

         for(int var6 = 0; var6 < var0.length; ++var6) {
            StringBuffer var7 = new StringBuffer();
            var7.append("  ");
            var7.append(var0[var6]);
            ++var6;
            if (var6 < var0.length) {
               var7.append("= ");
               var7.append(var0[var6]);
               out.println(fitIn(var7.toString()));
            }
         }

         flush();
      }
   }

   private static String fitIn(Object var0) {
      String var1 = var0 == null ? "null" : var0.toString();
      if (var1.length() > 70) {
         var1 = var1.substring(0, 67) + "...";
      }

      return var1;
   }

   public static PrintStream getOut() {
      return out;
   }

   public static String expand(Object var0) {
      if (var0 == null) {
         return "null";
      } else if (!var0.getClass().isArray()) {
         return var0.toString();
      } else {
         int var1 = Array.getLength(var0);
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("[").append(var3).append("]");
            var2.append(Array.get(var0, var3));
         }

         return var2.toString();
      }
   }

   public static void banner(String var0) {
      flush();
      StringBuffer var1 = new StringBuffer();
      var1.append("<WSEE>");
      if (timestamp) {
         var1.append("[").append(df.format(new Date())).append("]");
      }

      if (threadstamp) {
         var1.append("[").append(Thread.currentThread().getName()).append("]");
      }

      var1.append(var0);
      var0 = var1.toString();
      if (var0.length() > 60) {
         var0 = var1.substring(0, 58) + "..";
      }

      synchronized(out) {
         out.print("  ");

         int var3;
         for(var3 = 0; var3 < var0.length() + 4; ++var3) {
            out.print("-");
         }

         out.println();
         out.print(" |  ");
         out.print(var0);
         out.println("  |");
         out.print("  ");

         for(var3 = 0; var3 < var0.length() + 4; ++var3) {
            out.print("-");
         }

         out.println();
      }
   }

   private static void log(Object var0, int var1) {
      flush();
      StackTraceElement[] var2 = (new Exception()).getStackTrace();
      synchronized(out) {
         writeWSEEStart();
         writeTimestamp();
         writeThreadstamp();
         out.print(var0);
         out.print("<");
         out.print(shortName(var2[var1].getClassName()));
         out.print(".");
         out.print(var2[var1].getMethodName());
         int var4 = var2[var1].getLineNumber();
         if (var4 > -1) {
            out.print(":");
            out.print(var4);
         }

         out.println(">");
         out.flush();
      }
   }

   private static void writeWSEEStart() {
      out.print("<WSEE:");
      out.print(Thread.currentThread().getId());
      out.print(">");
   }

   private static void writeTimestamp() {
      if (timestamp) {
         out.print("[");
         out.print(df.format(new Date()));
         out.print("]");
      }

   }

   private static void writeThreadstamp() {
      if (threadstamp) {
         out.print("[");
         out.print(Thread.currentThread().getName());
         out.print("]");
      }

   }

   private static void flush() {
      System.out.flush();
      System.err.flush();
      out.flush();
   }

   public static void say(String var0) {
      flush();
      synchronized(out) {
         writeTimestamp();
         writeThreadstamp();
         out.println(var0);
         flush();
      }
   }

   public static void log(LogRecord var0) {
      if (var0.getThrown() != null) {
         logException(var0.getThrown());
      } else {
         flush();
         synchronized(out) {
            writeTimestamp();
            writeThreadstamp();
            out.print(" ");
            out.print(var0.getLevel().getName());
            out.print(" ");
            out.print(var0.getLoggerName());
            out.print(" ");
            out.println(var0.getMessage());
            flush();
         }
      }
   }

   private static String shortName(String var0) {
      int var1 = var0.lastIndexOf(46);
      if (var1 != -1) {
         var0 = var0.substring(var1 + 1, var0.length());
      }

      return var0;
   }

   private static void parseProperty(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",");

      while(var1.hasMoreTokens()) {
         String var2 = var1.nextToken().trim();
         boolean var3 = false;
         if (var2.startsWith("!")) {
            var3 = true;
            var2 = var2.substring(1);
         }

         if (var2.startsWith("file=")) {
            setOutput(var2.substring("file=".length()));
         } else {
            String var4 = Level.ALL.getName();
            int var5 = var2.indexOf("=");
            if (var5 > 0) {
               var4 = var2.substring(var5 + 1, var2.length());
               var2 = var2.substring(0, var5);
            }

            Level var6 = Level.parse(var4);
            if (var2.endsWith("*")) {
               String var7 = var2.substring(0, var2.length() - 1);
               addStartsWith(var7, var3, var6);
            } else {
               addEquals(var2, var3, var6);
            }
         }
      }

   }

   private static void setOutput(String var0) {
      File var1 = new File(var0);

      try {
         boolean var2 = Boolean.parseBoolean(System.getProperty("weblogic.wsee.verbose.rollFile", "false"));
         if (var2 && var1.exists()) {
            String var3 = var1.getName();
            String var4 = var3;
            String var5 = "";
            int var6 = var3.lastIndexOf(".");
            if (var6 > 0 && var6 < var3.length() - 1) {
               var4 = var3.substring(0, var6);
               var5 = var3.substring(var6);
            } else {
               var6 = var3.length();
            }

            int var7;
            for(var7 = 1; rollFileExists(var1.getParentFile(), var4, var7, var5); ++var7) {
            }

            String var8 = "_" + var7;
            var1 = new File(var1.getParentFile(), var4 + var8 + var5);
            System.out.println("Calculated new WSEE output file with 'rolling' enabled: " + var1.getName());
         }

         FileOutputStream var10 = new FileOutputStream(var1);
         out = new PrintStream(var10, true);
         System.out.println("Set WSEE output to " + var1.getAbsoluteFile().getAbsolutePath());
      } catch (FileNotFoundException var9) {
         logException(var9);
         log((Object)"Log to System.out");
         out = System.out;
      }

   }

   private static boolean rollFileExists(File var0, String var1, int var2, String var3) {
      String var4 = "_" + var2;
      File var5 = new File(var0, var1 + var4 + var3);
      return var5.exists();
   }

   private static void addEquals(String var0, boolean var1, Level var2) {
      if (var1) {
         var2 = Level.OFF;
         if (notEquals == null) {
            notEquals = new HashMap();
         }

         notEquals.put(var0, var2);
      } else {
         if (equals == null) {
            equals = new HashMap();
         }

         equals.put(var0, var2);
      }

   }

   private static void addStartsWith(String var0, boolean var1, Level var2) {
      if (var1) {
         var2 = Level.OFF;
         if (notStartsWith == null) {
            notStartsWith = new HashMap();
         }

         notStartsWith.put(var0, var2);
      } else {
         if (startsWith == null) {
            startsWith = new HashMap();
         }

         if ("".equals(var0) && var2.intValue() < Level.INFO.intValue()) {
            var2 = Level.INFO;
         }

         startsWith.put(var0, var2);
      }

   }

   private static void expandSubcomponentProperty(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ",");

      while(true) {
         while(var1.hasMoreTokens()) {
            String var2 = var1.nextToken().trim();
            boolean var3 = false;
            if (var2.startsWith("!")) {
               var3 = true;
               var2 = var2.substring(1);
            }

            if (var2.startsWith("file=")) {
               setOutput(var2.substring("file=".length()));
            } else {
               String var4 = Level.ALL.getName();
               int var5 = var2.indexOf("=");
               if (var5 > 0) {
                  var4 = var2.substring(var5 + 1, var2.length());
                  var2 = var2.substring(0, var5);
               }

               Level var6 = Level.parse(var4);
               String[] var7 = VerboseRegistry.getInstance().getSubcomponent(var2);
               if (var7 != null) {
                  String[] var8 = var7;
                  int var9 = var7.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     String var11 = var8[var10];
                     if (var11.endsWith("*")) {
                        var11 = var11.substring(0, var11.length() - 1);
                        addStartsWith(var11, var3, var6);
                     } else {
                        addEquals(var11, var3, var6);
                     }
                  }
               }
            }
         }

         return;
      }
   }

   public static boolean isVerbose(Class var0) {
      if (allwaysOn) {
         return true;
      } else if (!verboseOn) {
         return false;
      } else {
         String var1 = var0.getName();
         return isVerbose(var1);
      }
   }

   public static boolean isVerboseOn() {
      return allwaysOn || verboseOn;
   }

   public static boolean isVerbose(String var0) {
      return getVerboseLevel(var0) != Level.OFF;
   }

   public static Level getVerboseLevel(String var0) {
      String var1 = null;
      Level var2 = Level.OFF;
      if (allwaysOn) {
         return Level.ALL;
      } else if (!verboseOn) {
         return Level.OFF;
      } else if (notEquals != null && notEquals.containsKey(var0)) {
         return (Level)notEquals.get(var0);
      } else {
         Iterator var3;
         if (notStartsWith != null) {
            var3 = notStartsWith.keySet().iterator();

            while(var3.hasNext()) {
               var1 = (String)var3.next();
               if (var0.startsWith(var1)) {
                  return (Level)notStartsWith.get(var1);
               }
            }
         }

         if (equals != null && equals.containsKey(var0)) {
            return (Level)equals.get(var0);
         } else {
            if (startsWith != null) {
               var3 = startsWith.keySet().iterator();

               while(var3.hasNext()) {
                  var1 = (String)var3.next();
                  if (var0.startsWith(var1)) {
                     return (Level)startsWith.get(var1);
                  }
               }
            }

            return var2;
         }
      }
   }

   public static void hookUpUserLogger(Logger var0) {
      addHandler(handler, var0);
   }

   public static void setLoggingLevelsAndAddHandler(Handler var0) {
      Iterator var1;
      String var2;
      Level var3;
      if (startsWith != null) {
         for(var1 = startsWith.keySet().iterator(); var1.hasNext(); setLogLevelForLogger(var0, var2, var3)) {
            var2 = (String)var1.next();
            var3 = (Level)startsWith.get(var2);
            if (var2.endsWith(".")) {
               var2 = var2.substring(0, var2.length() - 1);
            }
         }
      }

      if (equals != null) {
         var1 = equals.keySet().iterator();

         while(var1.hasNext()) {
            var2 = (String)var1.next();
            var3 = (Level)equals.get(var2);
            setLogLevelForLogger(var0, var2, var3);
         }
      }

      if (notStartsWith != null) {
         for(var1 = notStartsWith.keySet().iterator(); var1.hasNext(); setLogLevelForLogger((Handler)null, var2, Level.OFF)) {
            var2 = (String)var1.next();
            if (var2.endsWith(".")) {
               var2 = var2.substring(0, var2.length() - 1);
            }
         }
      }

      if (notEquals != null) {
         var1 = notEquals.keySet().iterator();

         while(var1.hasNext()) {
            var2 = (String)var1.next();
            setLogLevelForLogger((Handler)null, var2, Level.OFF);
         }
      }

   }

   private static void setLogLevelForLogger(Handler var0, String var1, Level var2) {
      if (verboseOn) {
         say("#!#! - Setting LogLevel for " + var1 + " to " + var2 + " and handler: " + var0);
      }

      Logger var3 = Logger.getLogger(var1);
      var3.setLevel(var2);
      if (var0 != null) {
         addHandler(var0, var3);
      }

   }

   private static void addHandler(Handler var0, Logger var1) {
      Handler[] var2 = var1.getHandlers();
      if (!Arrays.asList(var2).contains(var0)) {
         var1.addHandler(var0);
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public static void here() {
      log("Called ........", 2);
   }

   static {
      out = System.out;
      df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
      allwaysOn = false;
      timestamp = Boolean.getBoolean("weblogic.wsee.verbose.timestamp");
      threadstamp = Boolean.getBoolean("weblogic.wsee.verbose.threadstamp");
      handler = new Handler() {
         public void publish(LogRecord var1) {
            Verbose.log(var1);
         }

         public void flush() {
         }

         public void close() throws SecurityException {
         }
      };
      init();
   }
}
