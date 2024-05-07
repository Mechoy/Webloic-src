package weblogic.ejb.container.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EJBClassEnhancer {
   protected String debug = null;

   public EJBClassEnhancer() {
      try {
         this.debug = System.getProperty("weblogic.ejb.enhancement.debug");
      } catch (Exception var2) {
      }

   }

   protected void writeEnhancedClassBack(String var1, byte[] var2) {
      if (this.debug != null) {
         File var3 = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "enhanced_classes", var1.replace('.', File.separatorChar) + ".class");
         var3.getParentFile().mkdirs();
         FileOutputStream var4 = null;

         try {
            if (!var3.exists()) {
               var3.createNewFile();
            }

            var4 = new FileOutputStream(var3);
            var4.write(var2);
         } catch (FileNotFoundException var17) {
            var17.printStackTrace();
         } catch (IOException var18) {
            var18.printStackTrace();
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var16) {
                  var16.printStackTrace();
               }
            }

         }

      }
   }
}
