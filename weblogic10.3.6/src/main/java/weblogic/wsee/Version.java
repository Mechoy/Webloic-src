package weblogic.wsee;

import java.util.HashMap;
import java.util.Iterator;

public final class Version {
   public static final String VERSION = "9.0";
   public static final String VERSION_92 = "9.2";
   public static final String VERSION_95 = "9.5";
   public static final String VERSION_100 = "10.0";
   public static final String VERSION_103 = "10.3";
   public static final String VERSION_103_PLUS = "10.3+";
   public static final String VERSION_1036 = "10.3.6";
   public static final String LATEST = "10.3.6";
   private static HashMap<String, Integer> _versionToOrdinalMap = new HashMap();

   public static boolean isLaterThanOrEqualTo(String var0, String var1) {
      int var2 = (Integer)_versionToOrdinalMap.get(var0);
      int var3 = (Integer)_versionToOrdinalMap.get(var1);
      return var2 >= var3;
   }

   public static boolean isKnownVersion(String var0) {
      return _versionToOrdinalMap.containsKey(var0);
   }

   public static void checkForKnownVersion(String var0) {
      if (!isKnownVersion(var0)) {
         throw new IllegalStateException("Wrong version, expected one of the following: " + getKnownVersionsString() + " actual: " + var0);
      }
   }

   public static String getKnownVersionsString() {
      StringBuffer var0 = new StringBuffer();
      Iterator var1 = _versionToOrdinalMap.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         var0.append(var2).append(",");
      }

      var0.deleteCharAt(var0.length() - 1);
      return var0.toString();
   }

   static {
      _versionToOrdinalMap.put("9.0", 0);
      _versionToOrdinalMap.put("9.2", 1);
      _versionToOrdinalMap.put("9.5", 2);
      _versionToOrdinalMap.put("10.0", 3);
      _versionToOrdinalMap.put("10.3", 4);
      _versionToOrdinalMap.put("10.3+", 5);
      _versionToOrdinalMap.put("10.3.6", 6);
   }
}
