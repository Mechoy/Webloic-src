package weblogic.deploy.api.internal.utils;

import java.io.PrintStream;

public class Debug {
   public static final String DEBUG_PROP = "weblogic.deployer.debug";
   public static final String CONFIG = "config";
   public static final String DEPLOY = "deploy";
   public static final String STATUS = "status";
   public static final String FACTORY = "factory";
   public static final String MODEL = "model";
   public static final String UTILS = "utils";
   public static final String ALL = "all";
   public static final String INTERNAL = "internal";
   private static String flags;
   private static final PrintStream out;
   private static final boolean DEBUG = false;
   private static final boolean FORCE_DEBUG = false;
   private static final boolean CLASSINFO = true;
   private static final String DEBUG_DEBUG = "";

   public static boolean isDebug(String var0) {
      if (flags.indexOf("all") != -1 && !"internal".equals(var0)) {
         return true;
      } else {
         return flags.indexOf(var0) != -1;
      }
   }

   public static void say(String var0) {
      try {
         out.println((new StackTrace()).location(0).tag(0) + var0);
      } catch (Throwable var2) {
         out.println("[unknown]" + var0);
      }

   }

   static {
      out = System.err;
      flags = System.getProperty("weblogic.deployer.debug", "");
      if ("".equals(flags)) {
         flags = "";
      }

   }

   static final class StackTrace {
      private final Location[] stack;

      StackTrace() {
         StackTraceElement[] var1 = (new Exception()).getStackTrace();
         this.stack = new Location[var1.length - 2];

         for(int var2 = 2; var2 < var1.length; ++var2) {
            this.stack[var2 - 2] = new Location(var1[var2]);
         }

      }

      public Location location(int var1) throws ArrayIndexOutOfBoundsException {
         return this.stack[var1];
      }

      public void dump(PrintStream var1, String var2) {
         var1.println(var2);

         for(int var3 = 0; var3 < this.stack.length; ++var3) {
            var1.flush();
            var1.println("  " + this.location(var3).dump());
         }

      }
   }

   static final class Location {
      private static final String UNKNOWN = "<unknown>";
      private final String pkg;
      private final String clazz;
      private final String method;
      private final String linenum;
      private final String fullClass;
      private final String sourcefile;

      Location(StackTraceElement var1) {
         this.fullClass = var1.getClassName();
         this.method = var1.getMethodName();
         int var2 = this.fullClass.lastIndexOf(".");
         if (var2 == -1) {
            this.pkg = "<unknown>";
            this.clazz = this.fullClass;
         } else {
            this.pkg = this.fullClass.substring(0, var2);
            this.clazz = this.fullClass.substring(var2 + 1);
         }

         this.sourcefile = var1.getFileName();
         int var3 = var1.getLineNumber();
         this.linenum = var3 > 0 ? String.valueOf(var3) : "<unknown>";
      }

      public String tag(int var1) {
         return "[" + this.clazz + "." + this.method + "()" + ":" + this.linenum + "] " + (var1 != 0 ? "(" + var1 + ")" : "") + ": ";
      }

      public String dump() {
         return this.fullname() + '(' + this.sourcefile + ':' + this.linenum + ')';
      }

      public String caller() {
         return this.fullname() + "(), line " + this.linenum;
      }

      private String fullname() {
         return this.fullClass + '.' + this.method;
      }
   }
}
