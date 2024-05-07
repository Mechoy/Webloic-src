package weblogic.application.ddconvert;

import java.io.File;
import java.io.IOException;
import weblogic.connector.utils.RarUtils;
import weblogic.utils.jars.VirtualJarFile;

public final class RarConverterFactory extends ConverterFactory {
   public Converter newConverter(VirtualJarFile var1) throws DDConvertException, IOException {
      File[] var2 = var1.getRootFiles();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (RarUtils.isRar(var2[var3])) {
               return new RarConverter();
            }
         }
      }

      return null;
   }
}
