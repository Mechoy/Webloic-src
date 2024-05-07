package weblogic.j2ee.customizers;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import weblogic.logging.NonCatalogLogger;

public final class AnnotationLocalizer {
   private static final String DESCRIPTION_KEY_SUFFIX = ".shortDescription";
   private static NonCatalogLogger _logger = new NonCatalogLogger(AnnotationLocalizer.class.getName());

   static String getShortDescription(String var0, String var1) {
      assert var0 != null;

      assert var1 != null;

      String var2 = null;
      Locale var3 = Locale.getDefault();
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      ResourceBundle var5 = null;

      try {
         var5 = ResourceBundle.getBundle(var0, var3, var4);
      } catch (MissingResourceException var7) {
         var5 = null;
         _logger.debug("No resource bundle " + var0);
      }

      if (var5 != null) {
         var2 = var5.getString(var1 + ".shortDescription");
         _logger.debug("Obtained description from resource bundle, " + var2);
      }

      if (var2 == null) {
         var2 = var1;
         _logger.debug("No description defined, using default, " + var1);
      }

      return var2;
   }
}
