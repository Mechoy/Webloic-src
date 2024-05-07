package weblogic.xml.crypto.wss;

public class SCTUtils {
   public static boolean isSCTokenTypeURI(String var0) {
      for(int var1 = 0; var1 < WSSConstants.SCTOKEN_TYPE_URI_NAMES.length; ++var1) {
         if (var0.equalsIgnoreCase(WSSConstants.SCTOKEN_TYPE_URI_NAMES[var1])) {
            return true;
         }
      }

      return false;
   }
}
