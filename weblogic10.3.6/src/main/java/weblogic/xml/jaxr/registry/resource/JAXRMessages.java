package weblogic.xml.jaxr.registry.resource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class JAXRMessages {
   private static ResourceBundle m_resourceBundle;

   public static String getMessage(String var0, Object[] var1) {
      if (m_resourceBundle == null) {
         loadResourceBundle();
      }

      String var2 = m_resourceBundle.getString(var0);
      if (var1 != null) {
         var2 = MessageFormat.format(var2, var1);
      }

      return var2;
   }

   public static String getMessage(String var0) {
      return getMessage(var0, (Object[])null);
   }

   private static void loadResourceBundle() {
      Locale var0 = Locale.getDefault();
      String var1 = JAXRMessages.class.getName();
      m_resourceBundle = ResourceBundle.getBundle(var1, var0);
   }
}
