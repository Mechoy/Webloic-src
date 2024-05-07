package weblogic.servlet.internal;

import java.util.HashMap;
import java.util.Locale;
import weblogic.j2ee.descriptor.LocaleEncodingMappingBean;
import weblogic.j2ee.descriptor.LocaleEncodingMappingListBean;

final class LocaleEncodingMap {
   private final HashMap mapping = new HashMap();

   String getJavaCharset(Locale var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = (String)this.mapping.get(var1.toString());
         if (var2 != null) {
            return var2;
         } else {
            var2 = (String)this.mapping.get(var1.getLanguage());
            return var2 != null ? var2 : weblogic.utils.CharsetMap.getJavaFromLocale(var1);
         }
      }
   }

   void registerLocaleEncodingMap(LocaleEncodingMappingListBean[] var1) {
      if (var1 != null && var1.length >= 1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.registerLocaleEncodingMap(var1[var2].getLocaleEncodingMappings());
         }

      }
   }

   private void registerLocaleEncodingMap(LocaleEncodingMappingBean[] var1) {
      if (var1 != null && var1.length >= 1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            LocaleEncodingMappingBean var3 = var1[var2];
            this.mapping.put(var3.getLocale(), var3.getEncoding());
         }

      }
   }
}
