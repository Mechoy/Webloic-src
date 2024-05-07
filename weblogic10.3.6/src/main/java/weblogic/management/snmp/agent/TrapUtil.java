package weblogic.management.snmp.agent;

import weblogic.diagnostics.snmp.server.ALSBTrapUtil;

public final class TrapUtil {
   private static final boolean DEBUG = true;
   private static String encoding = null;

   public static void sendALAlertTrap(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11) {
      try {
         ALSBTrapUtil.sendALSBAlert(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public static long convertTimeToRFC1213(long var0) {
      long var2 = (long)Math.round((float)(var0 / 10L));
      return var2;
   }

   private static String getEncoding() {
      if (encoding != null) {
         return encoding;
      } else {
         encoding = System.getProperty("weblogic.management.snmp.encoding");
         if (encoding == null) {
            encoding = System.getProperty("file.encoding");
         }

         return encoding;
      }
   }
}
