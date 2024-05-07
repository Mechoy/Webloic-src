package weblogic.application.ddconvert;

import java.io.IOException;
import weblogic.utils.jars.VirtualJarFile;

abstract class ConverterFactory {
   private static final ConverterFactory[] factories = new ConverterFactory[]{new EJBConverterFactory(), new WarConverterFactory(), new EarConverterFactory(), new RarConverterFactory()};

   public static Converter findConverter(ConvertCtx var0, VirtualJarFile var1) throws DDConvertException, IOException {
      for(int var2 = 0; var2 < factories.length; ++var2) {
         if (var0.isVerbose()) {
            ConvertCtx.debug("Trying factory " + factories[var2].getClass().getName());
         }

         Converter var3 = factories[var2].newConverter(var1);
         if (var0.isVerbose()) {
            ConvertCtx.debug("Called Factory " + factories[var2].getClass().getName() + " recognized application: " + (var3 != null));
         }

         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   abstract Converter newConverter(VirtualJarFile var1) throws DDConvertException, IOException;
}
