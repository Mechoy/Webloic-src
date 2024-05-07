package weblogic.wsee.util.jspgen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
   public static String fileToString(String var0) throws ScriptException {
      try {
         FileInputStream var1 = new FileInputStream(var0);
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();

         int var3;
         while((var3 = var1.read()) != -1) {
            var2.write(var3);
         }

         var1.close();
         return new String(var2.toByteArray(), "UTF-8");
      } catch (IOException var4) {
         throw new ScriptException("Failed to open include file " + var0);
      }
   }

   public static void stringToFile(String var0, String var1) throws ScriptException {
      try {
         File var2 = new File(var0);
         var2.getParentFile().mkdirs();
         FileOutputStream var3 = new FileOutputStream(var2);
         var3.write(var1.getBytes());
         var3.close();
      } catch (IOException var4) {
         throw new ScriptException("unable to write file:" + var0);
      }
   }
}
