package weblogic.servlet.internal.session;

import java.util.Map;
import java.util.StringTokenizer;
import weblogic.protocol.ServerIdentity;

public final class RSID implements SessionConstants {
   public String id;
   private ServerIdentity primary;
   private ServerIdentity secondary;
   private static final boolean DEBUG = false;

   public RSID(String var1) {
      this(var1, (Map)null);
   }

   public RSID(String var1, Map var2) {
      this.id = null;
      if (var1 != null) {
         try {
            StringTokenizer var3 = new StringTokenizer(var1, "!");
            int var4 = var3.countTokens();
            if (var4 < 1) {
               return;
            }

            this.id = var3.nextToken();
            if (var2 == null || var2.isEmpty() || var4 < 2) {
               return;
            }

            String var6 = null;
            String var5;
            if (var4 <= 4) {
               var5 = var3.nextToken();
               if (var4 > 2) {
                  var6 = var3.nextToken();
               }
            } else {
               var5 = var3.nextToken();
               if (var4 > 5) {
                  var3.nextToken();
                  var3.nextToken();
                  var3.nextToken();
                  var6 = var3.nextToken();
               }
            }

            this.findPrimarySecondary(var5, var6, var2);
         } catch (Exception var7) {
            HTTPSessionLogger.logCookieFormatError(var1, var7);
         }

      }
   }

   private void findPrimarySecondary(String var1, String var2, Map var3) {
      boolean var4 = var2 == null || var2.equals("NONE");
      Integer var5 = new Integer(var1);
      this.primary = (ServerIdentity)var3.get(var5);
      if (!var4) {
         Integer var6 = new Integer(var2);
         this.secondary = (ServerIdentity)var3.get(var6);
      }

   }

   public ServerIdentity getPrimary() {
      return this.primary;
   }

   public ServerIdentity getSecondary() {
      return this.secondary;
   }

   public static String getID(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf("!");
         if (var1 != -1) {
            var0 = var0.substring(0, var1);
         }

         return var0;
      }
   }

   public String toString() {
      return "[ID: " + this.id + " Primary: " + this.primary + " Secondary: " + this.secondary + "]";
   }
}
