package weblogic.wsee.util.cow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import weblogic.utils.FileUtils;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.Logger;

public class CowFile {
   private final File cowFile;

   public CowFile(File var1) {
      assert var1 != null;

      if (!var1.exists()) {
         throw new IllegalArgumentException("compiledWsdl file " + var1.getAbsolutePath() + " does not exist.");
      } else {
         this.cowFile = var1;
      }
   }

   public void extract(File var1, Logger var2) throws IOException {
      this.extract(var1, var2, false);
   }

   public void extract(File var1, Logger var2, boolean var3) throws IOException {
      JarFile var4 = null;

      try {
         var4 = new JarFile(this.cowFile);
         Enumeration var5 = var4.entries();

         while(true) {
            JarEntry var6;
            String var7;
            do {
               do {
                  if (!var5.hasMoreElements()) {
                     return;
                  }

                  var6 = (JarEntry)var5.nextElement();
                  var7 = var6.getName().replace('/', File.separatorChar);
               } while(var6.isDirectory());
            } while((var3 || !var7.endsWith(".java")) && !var7.startsWith("wsdls" + File.separatorChar) && (!var3 || var7.toUpperCase(Locale.ENGLISH).endsWith(".MF") || var7.endsWith(".java")));

            InputStream var8 = null;

            try {
               var8 = var4.getInputStream(var6);
               String var9 = "meta-inf" + File.separatorChar + "src" + File.separatorChar;
               if (var7.toLowerCase(Locale.ENGLISH).startsWith(var9) && var7.endsWith(".java")) {
                  var7 = var7.substring(var9.length());
               }

               File var10 = new File(var1, var7);
               FileUtils.writeToFile(var8, var10);
               var2.log(EventLevel.VERBOSE, "Extracted file to " + var10.getAbsolutePath() + " from compiledWsdl.");
            } finally {
               if (var8 != null) {
                  var8.close();
               }

            }
         }
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }
   }
}
