package weblogic.wsee.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class ToolsUtil {
   private static final boolean verbose = Verbose.isVerbose(ToolsUtil.class);

   private ToolsUtil() {
   }

   public static PrintStream createJavaSourceStream(File var0, String var1) throws WsBuildException {
      int var2 = var1.lastIndexOf(".");
      if (var2 == -1) {
         throw new WsBuildException("Package name not found in:" + var1);
      } else {
         String var3 = var1.substring(0, var2);
         String var4 = var1.substring(var2 + 1, var1.length());
         return createJavaSourceStream(var0, var3, var4);
      }
   }

   public static PrintStream createJavaSourceStream(File var0, String var1, String var2) throws WsBuildException {
      File var3 = new File(var0, var1.replace('.', File.separatorChar));
      var3.mkdirs();
      File var4 = new File(var3, var2 + ".java");
      if (verbose) {
         Verbose.log((Object)("Creating java source file at: " + var4));
      }

      PrintStream var5 = null;

      try {
         var5 = new PrintStream(new FileOutputStream(var4), true);
         return var5;
      } catch (IOException var7) {
         throw new WsBuildException("Failed to open file for ouput." + var7, var7);
      }
   }

   public static String getPackageName(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var1 == -1 ? null : var0.substring(0, var1);
   }

   public static void validateNonEmptyAttr(String var0, String var1) throws WsBuildException {
      validateNonEmptyAttr(var0, var1, (Logger)null);
   }

   public static void validateNonEmptyAttr(String var0, String var1, Logger var2) throws WsBuildException {
      if (StringUtil.isEmpty(var0)) {
         throwException("The empty string is not a valid attribute for: " + var1, var2);
      }

   }

   public static void throwException(String var0, Logger var1) throws WsBuildException {
      if (var1 != null) {
         var1.log(EventLevel.ERROR, var0);
      }

      throw new WsBuildException(var0);
   }

   public static void validateRequiredFile(File var0, String var1) throws WsBuildException {
      validateRequiredFile(var0, var1, (Logger)null);
   }

   public static void validateRequiredFile(File var0, String var1, Logger var2) throws WsBuildException {
      if (var0 == null) {
         throwException("The required attribute: " + var1 + " must be set.", var2);
      } else {
         validateNonEmptyAttr(var0.getName(), var1, var2);
      }

   }

   public static void createDir(File var0, String var1) throws WsBuildException {
      if (!var0.exists() && !var0.mkdirs()) {
         throw new WsBuildException("Unable to create the directory " + var1 + ":  " + var0.getName());
      }
   }

   public static void validateRequiredAttr(String var0, String var1) throws WsBuildException {
      validateRequiredAttr(var0, var1, (Logger)null);
   }

   public static void validateRequiredAttr(String var0, String var1, Logger var2) throws WsBuildException {
      if (var0 == null) {
         throwException("The required attribute: " + var1 + " must be set", var2);
      } else {
         validateNonEmptyAttr(var0, var1, var2);
      }

   }

   public static String normalize(String var0) {
      if (StringUtil.isEmpty(var0)) {
         throw new IllegalArgumentException(var0);
      } else {
         String var1 = var0.replaceAll("\\\\", "/");
         if (!var1.startsWith("/")) {
            var1 = "/" + var1;
         }

         return var1;
      }
   }
}
