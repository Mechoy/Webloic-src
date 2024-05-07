package weblogic.diagnostics.debug;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class DebugCategoryParser {
   private static final String TAG_NAME = "oldDebugCategory";
   private static final String OUTPUT_FILE_OPTION = "-outputFile";
   private static final String BOOLEAN_TYPE_NAME = "boolean";
   private static boolean verbose = false;
   private static Properties props = new Properties();

   public static boolean start(RootDoc var0) throws IOException {
      ClassDoc[] var1 = var0.classes();
      if (var1 == null) {
         return false;
      } else {
         String[][] var2 = var0.options();
         String var3 = null;
         int var4;
         if (var2 != null) {
            for(var4 = 0; var4 < var2.length; ++var4) {
               if (var2[var4][0].equals("-outputFile")) {
                  var3 = var2[var4][1];
               } else if (var2[var4][0].equals("-verbose")) {
                  verbose = true;
               }
            }
         }

         var0.printNotice("Started building the DebugCategory mapping...");

         for(var4 = 0; var4 < var1.length; ++var4) {
            if (!processClass(var0, var1[var4])) {
               return false;
            }
         }

         var0.printNotice("The DebugCategory mapping will be written to " + var3);
         FileOutputStream var5 = new FileOutputStream(var3);
         props.store(var5, "Map of DebugLogger to DebugCategory names");
         var0.printNotice("DebugCategory mapping generated successfully");
         return true;
      }
   }

   public static int optionLength(String var0) {
      return var0.equals("-outputFile") ? 2 : 0;
   }

   private static boolean processClass(RootDoc var0, ClassDoc var1) {
      var0.printNotice("DebugScopesParser will process class " + var1.qualifiedTypeName());
      MethodDoc[] var2 = var1.methods();
      if (var2 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            MethodDoc var4 = var2[var3];
            String var5 = var4.name();
            if (verbose) {
               var0.printNotice("Processing method " + var5);
            }

            String var6;
            String var7;
            if (var5.startsWith("get")) {
               var6 = var5.substring(3);
               var7 = var4.returnType().typeName();
            } else if (var5.startsWith("is")) {
               var6 = var5.substring(2);
               var7 = var4.returnType().typeName();
            } else {
               if (!var5.startsWith("set")) {
                  if (verbose) {
                     var0.printNotice("The method " + var5 + " does not define an MBean attribute");
                  }
                  continue;
               }

               var6 = var5.substring(3);
               Parameter[] var8 = var4.parameters();
               if (var8 == null || var8.length == 0) {
                  continue;
               }

               Parameter var9 = var8[0];
               if (var9 == null) {
                  continue;
               }

               var7 = var9.typeName();
            }

            if (var7.equals("boolean")) {
               if (verbose) {
                  var0.printNotice("Processing attribute name " + var6 + " of type " + var7);
               }

               Tag[] var14 = var4.tags("oldDebugCategory");
               if (var14 != null && var14.length != 0) {
                  for(int var15 = 0; var15 < var14.length; ++var15) {
                     Tag var10 = var14[var15];
                     StringTokenizer var11 = new StringTokenizer(var10.text().trim());
                     if (var11.hasMoreTokens()) {
                        String var12 = var11.nextToken();
                        String var13 = props.getProperty(var6);
                        if (var13 == null) {
                           props.put(var6, var12);
                        } else {
                           props.put(var6, var13 + "," + var12);
                        }

                        if (verbose) {
                           var0.printNotice("Mapping attribute " + var6 + " to the Category " + var12);
                        }
                     }
                  }
               }
            }
         }

         return true;
      }
   }
}
