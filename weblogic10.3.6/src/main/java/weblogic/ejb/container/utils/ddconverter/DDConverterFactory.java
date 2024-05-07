package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import weblogic.ejb.spi.EJBJarUtils;

public final class DDConverterFactory {
   public static DDConverterBase getDDConverter(String[] var0, String var1, String var2, ConvertLog var3) throws DDConverterException {
      Object var4 = null;
      boolean var5 = true;

      for(int var6 = 0; var6 < var0.length; ++var6) {
         try {
            if (!EJBJarUtils.isEJB(new File(var0[var6]))) {
               try {
                  new JarFile(var0[var6]);
                  EJBddcTextFormatter var8 = new EJBddcTextFormatter();
                  throw new DDConverterException(var8.invalidJarFile(var0[var6]));
               } catch (IOException var9) {
                  var5 = false;
               }
            }
         } catch (IOException var10) {
            throw new DDConverterException(var10);
         }
      }

      if (var5) {
         if (var2 != null && var2.equals("1.1")) {
            var4 = new DDConvertToLatest(var0, var1, var3, false);
         } else {
            var4 = new DDConvertToLatest(var0, var1, var3, true);
         }
      } else if (var2 == "2.1") {
         var4 = new DDConverter_1020(var0, var1, var3);
      } else {
         var4 = new DDConverter_1011(var0, var1, var3);
      }

      return (DDConverterBase)var4;
   }
}
