package weblogic.management.descriptors;

import java.util.Locale;
import weblogic.apache.xerces.util.EncodingMap;

public final class Encoding {
   public static String getIANA2JavaMapping(String var0) {
      return EncodingMap.getIANA2JavaMapping(var0.toUpperCase(Locale.ENGLISH));
   }

   public static String getJava2IANAMapping(String var0) {
      return EncodingMap.getJava2IANAMapping(var0.toUpperCase(Locale.ENGLISH));
   }
}
