package weblogic.wsee.server;

import weblogic.utils.io.FilenameEncoder;

public class URLUtil {
   public static final String constructAbsoluteURL(String var0, String var1) {
      String var2 = getProtocol(var1);
      String var3 = getAddress(var1);
      if (var3 == null) {
         var3 = ServerUtil.getServerURL(var2);
         String var4 = FilenameEncoder.resolveRelativeURIPath(var3 + "/" + getPath(var0, var1));
         return appendJMSQuery(var2, var4);
      } else {
         return appendJMSQuery(var2, var1);
      }
   }

   public static final String appendJMSQuery(String var0, String var1) {
      String var2 = new String(var1);
      if (var0.equalsIgnoreCase("jms")) {
         if (var1.indexOf("?URI") < 0) {
            var2 = var2 + "?URI=" + ServerUtil.getMessagingQueue();
         }

         if (var1.indexOf("&FACTORY=") < 0) {
            var2 = var2 + "&FACTORY=" + ServerUtil.getJmsConnectionFactory();
         }
      }

      return var2;
   }

   public static final String getProtocol(String var0) {
      int var1 = var0.indexOf(58);
      if (var1 > 0) {
         return var0.substring(0, var1);
      } else {
         throw new ServerURLNotFoundException("Protocol is not defined for " + var0);
      }
   }

   public static final String getAddress(String var0) {
      int var1 = var0.indexOf("://");
      if (var1 > 0) {
         String var2 = var0.substring(var1 + 3);
         int var3 = var2.indexOf(47);
         return var3 == 0 ? null : var2.substring(0, var3);
      } else {
         return null;
      }
   }

   public static final String getPath(String var0, String var1) throws ServerURLNotFoundException {
      int var2;
      if ((var2 = var1.indexOf(":///")) > -1) {
         return var1.substring(var2 + 4);
      } else if ((var2 = var1.indexOf(":/")) > -1) {
         return var1.substring(var2 + 1);
      } else if ((var2 = var1.indexOf(58)) > -1) {
         return var0.endsWith("/") ? var0 + var1.substring(var2 + 1) : var0 + "/" + var1.substring(var2 + 1);
      } else {
         throw new ServerURLNotFoundException("Unable to resolve uri path for " + var1);
      }
   }
}
