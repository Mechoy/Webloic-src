package weblogic.servlet.security;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.security.internal.WebAppSecurity;

public class Utils {
   private static final String[] xssStrings = new String[]{"&quot;", null, null, "&#37;", "&amp;", "&#39;", "&#40;", "&#41;", null, "&#43;", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&#59;", "&lt;", null, "&gt;"};
   private static final int BEGIN = 34;

   public static String encodeXSS(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         if (var1 < 1) {
            return var0;
         } else {
            boolean var2 = false;
            StringBuilder var3 = null;

            for(int var4 = 0; var4 < var1; ++var4) {
               char var5 = var0.charAt(var4);
               int var6 = var5 - 34;
               if (var6 > -1 && var6 < xssStrings.length && xssStrings[var6] != null) {
                  if (!var2) {
                     var3 = new StringBuilder(var1);
                     var3.append(var0.substring(0, var4));
                  }

                  var3.append(xssStrings[var6]);
                  var2 = true;
               } else if (var2) {
                  var3.append(var5);
               }
            }

            if (!var2) {
               return var0;
            } else {
               return var3.toString();
            }
         }
      }
   }

   public static String getConfiguredAuthMethod(ServletContext var0) {
      return ((WebAppServletContext)var0).getSecurityManager().getWebAppSecurity().getAuthMethod();
   }

   public static String getConfiguredAuthMethod(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      return var1.getContext().getSecurityManager().getWebAppSecurity().getAuthMethod();
   }

   public static boolean isSSLRequired(ServletContext var0, String var1, String var2) {
      return ((WebAppServletContext)var0).isSSLRequired(var1, var2);
   }

   public static boolean isSSLRequired(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      return var1.getContext().isSSLRequired(WebAppSecurity.getRelativeURI(var0), var0.getMethod());
   }
}
