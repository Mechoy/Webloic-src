package weblogic.xml.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.utils.AssertionError;

public class Debug {
   private static Debug anInstance = new Debug();
   static int minDebugLevel = 0;
   static Vector DebugFacilityExtent = new Vector();

   public static Location location(int var0) {
      return (anInstance.new StackTrace()).location(var0);
   }

   public static void setDebugLevelAll(int var0) {
      Enumeration var1 = DebugFacilityExtent.elements();

      while(var1.hasMoreElements()) {
         DebugFacility var2 = (DebugFacility)var1.nextElement();
         if (var2.getLevel() < var0) {
            var2.setLevel(var0);
         }
      }

      minDebugLevel = var0;
   }

   public static DebugFacility makeDebugFacility(DebugSpec var0) {
      return anInstance.new DebugFacility(var0);
   }

   public static DebugSpec getDebugSpec() {
      return anInstance.new DebugSpec();
   }

   public static String getPackage() {
      String var0 = location(1).getPackage();
      return var0;
   }

   public static String getClassName() {
      return getClassName(2);
   }

   private static String getClassName(int var0) {
      String var1 = location(var0).getClassName();
      return var1;
   }

   public static String getFullClassName() {
      return getFullClassName(2);
   }

   private static String getFullClassName(int var0) {
      String var1 = location(var0).getFullClassName();
      return var1;
   }

   public static Formatter getFormatter(OutputStream var0) {
      return new Debug().new StreamFormatterImpl(var0);
   }

   public class StreamFormatterImpl implements Formatter {
      PrintWriter pw = null;

      public StreamFormatterImpl(OutputStream var2) {
         this.pw = new PrintWriter(var2);
      }

      public void println(String var1) {
         this.pw.println(var1);
      }

      public void print(String var1) {
         this.pw.print(var1);
      }

      public void flush() {
         this.pw.flush();
      }
   }

   public interface DebugFormatter extends Formatter {
      void println(int var1, String var2);

      void print(int var1, String var2);
   }

   public interface Formatter {
      void println(String var1);

      void print(String var1);

      void flush();
   }

   final class StackTrace {
      Vector stack = new Vector();

      StackTrace() {
         try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            PrintStream var3 = new PrintStream(var2);
            (new Exception()).printStackTrace(var3);
            byte[] var4 = var2.toByteArray();
            ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
            DataInputStream var6 = new DataInputStream(var5);
            var3.close();
            var6.readLine();
            var6.readLine();
            var6.readLine();

            String var7;
            while((var7 = var6.readLine()) != null) {
               this.stack.addElement(Debug.this.new Location(var7));
            }
         } catch (IOException var8) {
         }

      }

      public Location location(int var1) throws ArrayIndexOutOfBoundsException {
         return (Location)this.stack.elementAt(var1);
      }
   }

   public class Location {
      private String pkg;
      private String clazz;
      private String method;
      private String linenum;
      private String fullClass;
      private String sourcefile;

      public String getPackage() {
         return this.pkg;
      }

      public String getClassName() {
         return this.clazz;
      }

      public String getMethod() {
         return this.method;
      }

      public String getLineNumber() {
         return this.linenum;
      }

      public String getFullClassName() {
         return this.fullClass;
      }

      public String getSourceFile() {
         return this.sourcefile;
      }

      public String here() {
         return this.fullClass + "." + this.method + "(" + this.sourcefile + ":" + this.linenum + ")";
      }

      public String caller() {
         return this.fullname() + "(), line " + this.linenum;
      }

      private String fullname() {
         return this.fullClass.length() > 0 ? this.fullClass + "." + this.method : this.method;
      }

      private Location(String var2) {
         this.pkg = "<unknown>";
         this.clazz = "<unknown>";
         this.method = "<unknown>";
         this.linenum = "<unknown>";
         this.fullClass = "<unknown>";
         this.sourcefile = "<unknown>";
         int var4 = var2.indexOf("at ") + 3;
         int var3 = var2.indexOf("(");
         if (var4 != -1 && var3 != -1) {
            String var5 = var2.substring(var4, var3);
            var3 = var5.lastIndexOf(".");
            if (var3 != -1) {
               this.fullClass = var5.substring(0, var3);
               this.method = var5.substring(var3 + 1);
               var3 = this.fullClass.lastIndexOf(".");
               if (var3 == -1) {
                  this.pkg = "<default>";
                  this.clazz = this.fullClass;
               } else {
                  this.pkg = this.fullClass.substring(0, var3);
                  this.clazz = this.fullClass.substring(var3 + 1);
               }

               var4 = var2.indexOf("(") + 1;
               var3 = var2.indexOf(":");
               if (var4 != -1 && var3 != -1) {
                  this.sourcefile = var2.substring(var4, var3);
                  var4 = var3 + 1;
                  var3 = var2.lastIndexOf(")");
                  if (var3 != -1) {
                     this.linenum = var2.substring(var4, var3);
                  } else {
                     this.linenum = var2.substring(var4);
                  }

               }
            }
         }
      }

      // $FF: synthetic method
      Location(String var2, Object var3) {
         this(var2);
      }
   }

   public class DebugFacility implements DebugFormatter {
      PrintWriter wtr;
      DebugSpec spec;
      boolean initialized;

      private DebugFacility(DebugSpec var2) {
         this.wtr = null;
         this.spec = null;
         this.initialized = false;
         this.spec = var2;
         this.wtr = new PrintWriter(this.spec.outStream);
         if (this.spec.name != null && this.spec.shortName) {
            int var3 = this.spec.name.lastIndexOf(".");
            this.spec.name = this.spec.name.substring(var3 + 1);
         }

         Debug.DebugFacilityExtent.add(this);
      }

      public boolean areDebugging() {
         return this.spec.level > 0;
      }

      public boolean areDebuggingAt(int var1) {
         return var1 <= this.spec.level;
      }

      public void pl(String var1) {
         this.println(var1);
      }

      public void pl(int var1, String var2) {
         this.println(var2);
      }

      public void println(String var1) {
         this.print(2, var1 + "\n", true);
      }

      public void println(int var1, String var2) {
         this.print(var1, var2 + "\n", true);
      }

      public void pw(String var1) {
         this.printWarning(var1);
      }

      public void pe(String var1) {
         this.printError(var1);
      }

      public void printWarning(String var1) {
         this.println(1, "WARNING: " + var1);
      }

      public void printError(String var1) {
         this.println(1, "ERROR: " + var1);
      }

      public void print(String var1) {
         this.print(2, var1, true);
      }

      public void print(int var1, String var2) {
         this.print(var1, var2, true);
      }

      public int getLevel() {
         return this.spec.level;
      }

      public void setLevel(int var1) {
         this.spec.level = var1;
      }

      public void setName(String var1) {
         this.spec.name = var1;
      }

      public void setPrefix(String var1) {
         this.spec.prefix = var1;
      }

      public void setOutStream(OutputStream var1) {
         this.spec.outStream = var1;
      }

      public void setIncludeTime(boolean var1) {
         this.spec.includeTime = var1;
      }

      public void setIncludeName(boolean var1) {
         this.spec.includeName = var1;
      }

      public void setIncludeClass(boolean var1) {
         this.spec.includeClass = var1;
      }

      public void setIncludeLocation(boolean var1) {
         this.spec.includeLocation = var1;
      }

      public void setShortName(boolean var1) {
         this.spec.shortName = var1;
      }

      public void setShortClass(boolean var1) {
         this.spec.shortClass = var1;
      }

      public void setMBean(ServerDebugMBean var1) {
         if (this.spec.prefix != null) {
            int var2 = 0;
            String var3 = null;
            OutputStream var4 = null;
            boolean var5 = false;
            boolean var6 = false;
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            if (this.spec.prefix.equals("JAXP")) {
               try {
                  var2 = var1.getDebugJAXPDebugLevel();
                  var3 = var1.getDebugJAXPDebugName();
                  var4 = var1.getDebugJAXPOutputStream();
                  var5 = var1.getDebugJAXPIncludeTime();
                  var6 = var1.getDebugJAXPIncludeName();
                  var7 = var1.getDebugJAXPIncludeClass();
                  var8 = var1.getDebugJAXPIncludeLocation();
                  var9 = var1.getDebugJAXPUseShortClass();
               } catch (Exception var13) {
               }
            } else if (this.spec.prefix.equals("XMLRegistry")) {
               try {
                  var2 = var1.getDebugXMLRegistryDebugLevel();
                  var3 = var1.getDebugXMLRegistryDebugName();
                  var4 = var1.getDebugXMLRegistryOutputStream();
                  var5 = var1.getDebugXMLRegistryIncludeTime();
                  var6 = var1.getDebugXMLRegistryIncludeName();
                  var7 = var1.getDebugXMLRegistryIncludeClass();
                  var8 = var1.getDebugXMLRegistryIncludeLocation();
                  var9 = var1.getDebugXMLRegistryUseShortClass();
               } catch (Exception var12) {
               }
            } else {
               if (!this.spec.prefix.equals("XMLEntityCache")) {
                  throw new IllegalArgumentException(this.spec.prefix + " prefix not supported");
               }

               try {
                  var2 = var1.getDebugXMLEntityCacheDebugLevel();
                  var3 = var1.getDebugXMLEntityCacheDebugName();
                  var4 = var1.getDebugXMLEntityCacheOutputStream();
                  var5 = var1.getDebugXMLEntityCacheIncludeTime();
                  var6 = var1.getDebugXMLEntityCacheIncludeName();
                  var7 = var1.getDebugXMLEntityCacheIncludeClass();
                  var8 = var1.getDebugXMLEntityCacheIncludeLocation();
                  var9 = var1.getDebugXMLEntityCacheUseShortClass();
               } catch (Exception var11) {
               }
            }

            if (var2 > this.getLevel()) {
               this.setLevel(var2);
            }

            if (var3 != null) {
               this.setName(var3);
            }

            if (var4 != null) {
               this.setOutStream(var4);
            }

            this.setIncludeTime(var5);
            this.setIncludeName(var6);
            this.setIncludeClass(var7);
            this.setIncludeLocation(var8);
            this.setShortClass(var9);
         }
      }

      private synchronized void print(int var1, String var2, boolean var3) {
         if (!this.initialized) {
            this.initialize();
         }

         if (this.areDebuggingAt(var1)) {
            this.wtr.print("DBG:");
            if (this.spec.includeTime) {
               this.wtr.print(" " + new Date());
            }

            if (this.spec.includeName || this.spec.includeLocation) {
               this.wtr.print(" (");
               if (this.spec.includeName) {
                  this.wtr.print(this.spec.name);
               }

               if (this.spec.includeClass) {
                  if (this.spec.includeName) {
                     this.wtr.print(", ");
                  }

                  String var4 = Debug.getFullClassName(4);
                  if (var4 != null && this.spec.shortClass) {
                     int var5 = var4.lastIndexOf(".");
                     var4 = var4.substring(var5 + 1);
                  }

                  this.wtr.print(var4);
               }

               if (this.spec.includeLocation) {
                  if (this.spec.includeName || this.spec.includeClass) {
                     this.wtr.print(", ");
                  }

                  this.wtr.print(Debug.location(2).here());
               }

               this.wtr.print(")");
            }

            this.wtr.print(" " + var2);
            if (var3) {
               this.wtr.flush();
            }

         }
      }

      public PrintWriter getWriter() {
         return this.wtr;
      }

      public void flush() {
         this.wtr.flush();
      }

      public void setDebugLevel(int var1) {
         this.spec.level = var1;
      }

      private void initialize() {
         String var1 = this.spec.name;

         try {
            String var2 = System.getProperty("DebugSpec");
            if (var2 != null) {
               this.initializeFromPropertries(var2);
            }

            var2 = System.getProperty("DebugSpec." + var1);
            if (var2 != null) {
               this.initializeFromPropertries(var2);
            }

            Integer var3 = Integer.getInteger("DebugLevel." + var1);
            if (var3 != null) {
               this.setLevel(var3);
            } else {
               var3 = Integer.getInteger("DebugLevel");
               this.setLevel(var3);
            }
         } catch (SecurityException var4) {
         }

         this.initialized = true;
      }

      private void setLevel(Integer var1) {
         if (var1 != null && var1 > Debug.minDebugLevel) {
            this.spec.level = var1;
         }

      }

      private void initializeFromPropertries(String var1) {
         try {
            FileInputStream var2 = new FileInputStream(var1);
            Properties var3 = new Properties();
            var3.load(var2);
            String var4 = var3.getProperty("level");
            String var5 = var3.getProperty("includeTime");
            String var6 = var3.getProperty("includeLocation");
            String var7 = var3.getProperty("outputFile");
            String var8 = var3.getProperty("name");
            String var9 = var3.getProperty("includeName");
            String var10 = var3.getProperty("shortName");
            String var11 = var3.getProperty("includeClass");
            String var12 = var3.getProperty("shortClass");
            if (var4 != null) {
               try {
                  this.setLevel(Integer.valueOf(var4));
               } catch (Exception var21) {
               }
            }

            if (var5 != null) {
               try {
                  this.spec.includeTime = Boolean.valueOf(var5);
               } catch (Exception var20) {
               }
            }

            if (var6 != null) {
               try {
                  this.spec.includeLocation = Boolean.valueOf(var6);
               } catch (Exception var19) {
               }
            }

            if (var7 != null) {
               try {
                  this.spec.outStream = new FileOutputStream(var7);
               } catch (Exception var18) {
               }
            }

            if (var8 != null) {
               this.spec.name = var8;
            }

            if (var9 != null) {
               try {
                  this.spec.includeName = Boolean.valueOf(var9);
               } catch (Exception var17) {
               }
            }

            if (var10 != null) {
               try {
                  this.spec.shortName = Boolean.valueOf(var10);
               } catch (Exception var16) {
               }
            }

            if (var11 != null) {
               try {
                  this.spec.includeClass = Boolean.valueOf(var11);
               } catch (Exception var15) {
               }
            }

            if (var12 != null) {
               try {
                  this.spec.shortClass = Boolean.valueOf(var12);
               } catch (Exception var14) {
               }
            }
         } catch (IOException var22) {
         }

      }

      public void assertion(boolean var1) {
         this.assertion(1, var1, "");
      }

      public void assertion(boolean var1, String var2) {
         this.assertion(1, var1, var2);
      }

      public void assertion(int var1, boolean var2) {
         this.assertion(var1, var2, "");
      }

      public void assertion(int var1, boolean var2, String var3) {
         if (this.areDebuggingAt(var1) && !var2) {
            throw new AssertionError("Assertion violated");
         }
      }

      public void px(Throwable var1) {
         this.pxI(var1, "", 2, 2);
      }

      public void px(Throwable var1, String var2) {
         this.pxI(var1, var2, 2, 2);
      }

      public void px(Throwable var1, int var2) {
         this.pxI(var1, "", var2, var2);
      }

      public void px(Throwable var1, String var2, int var3) {
         this.pxI(var1, var2, var3, var3);
      }

      public void px(Throwable var1, int var2, int var3) {
         this.pxI(var1, "", var2, var3);
      }

      public void px(Throwable var1, String var2, int var3, int var4) {
         this.pxI(var1, var2, var3, var4);
      }

      private synchronized void pxI(Throwable var1, String var2, int var3, int var4) {
         if (var1 != null) {
            int var5 = var3;
            if (var4 < var3) {
               var5 = var4;
            }

            if (var5 <= this.spec.level) {
               this.println(var5, "---------------------------------------------------------------------");
               if (this.areDebuggingAt(var3)) {
                  this.println(var5, "*** Exception: " + var1);
                  this.println(var5, "*** Caught from: " + Debug.location(2).here());
                  this.println(var5, "*** Associated message: " + var2);
               }

               if (this.areDebuggingAt(var4)) {
                  this.println(var5, "*** Stack trace:");
                  var1.printStackTrace(this.wtr);
               }

               this.println(var5, "---------------------------------------------------------------------");
               this.wtr.flush();
            }
         }
      }

      // $FF: synthetic method
      DebugFacility(DebugSpec var2, Object var3) {
         this(var2);
      }

      public class DebugListener implements PropertyChangeListener {
         public void propertyChange(PropertyChangeEvent var1) {
            String var2 = var1.getPropertyName();
            if (("Debug" + DebugFacility.this.spec.prefix + "DebugLevel").equals(var2)) {
               DebugFacility.this.setLevel((Integer)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "DebugName").equals(var2)) {
               DebugFacility.this.setName((String)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "OutputStream").equals(var2)) {
               DebugFacility.this.setOutStream((OutputStream)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "IncludeTime").equals(var2)) {
               DebugFacility.this.setIncludeTime((Boolean)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "IncludeName").equals(var2)) {
               DebugFacility.this.setIncludeName((Boolean)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "IncludeClass").equals(var2)) {
               DebugFacility.this.setIncludeClass((Boolean)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "IncludeLocation").equals(var2)) {
               DebugFacility.this.setIncludeLocation((Boolean)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "UseShortName").equals(var2)) {
               DebugFacility.this.setShortName((Boolean)var1.getNewValue());
            } else if (("Debug" + DebugFacility.this.spec.prefix + "UseShortClass").equals(var2)) {
               DebugFacility.this.setShortClass((Boolean)var1.getNewValue());
            }

         }
      }
   }

   public class DebugSpec {
      public int level;
      public boolean includeTime;
      public boolean includeLocation;
      public OutputStream outStream;
      public String name;
      public String prefix;
      public boolean includeName;
      public boolean shortName;
      public boolean includeClass;
      public boolean shortClass;

      public DebugSpec() {
         this.level = Debug.minDebugLevel;
         this.includeTime = false;
         this.includeLocation = false;
         this.outStream = System.out;
         this.name = null;
         this.prefix = null;
         this.includeName = true;
         this.shortName = false;
         this.includeClass = false;
         this.shortClass = true;
      }

      public void dump(PrintStream var1) {
         var1.println("DUMP of debug spec:");
         var1.println("  level: " + this.level);
         var1.println("  includeTime: " + this.includeTime);
         var1.println("  includeLocation: " + this.includeLocation);
         var1.println("  outStream: " + this.outStream);
         var1.println("  name: " + this.name);
         var1.println("  prefix: " + this.prefix);
         var1.println("  includeName: " + this.includeName);
         var1.println("  shortName: " + this.shortName);
         var1.println("  includeClass: " + this.includeClass);
         var1.println("  shortClass: " + this.shortClass);
      }
   }
}
