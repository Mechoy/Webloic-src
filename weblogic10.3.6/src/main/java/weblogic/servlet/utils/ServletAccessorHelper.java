package weblogic.servlet.utils;

import java.util.HashSet;
import java.util.Set;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;

public class ServletAccessorHelper {
   public static final String WEBAPP_LOG = "WebAppLog";

   public static Set getLogicalNamesForWebApps(WebServerMBean var0) {
      String var1 = var0.getName();
      HttpServer var2 = WebService.getHttpServer(var1);
      HashSet var3 = new HashSet();
      WebAppServletContext[] var4 = var2 != null ? var2.getServletContextManager().getAllContexts() : null;
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebAppServletContext var6 = var4[var5];
            if (var6 != null) {
               WebAppModule var7 = var6.getWebAppModule();
               if (var7 != null) {
                  WeblogicWebAppBean var8 = var7.getWlWebAppBean();
                  if (var8 != null) {
                     LoggingBean var9 = (LoggingBean)DescriptorUtils.getFirstChildOrDefaultBean(var8, var8.getLoggings(), "Logging");
                     if (var9 != null && var9.getLogFilename() != null) {
                        String var10 = var6.getContextPath();
                        if (var10 != null) {
                           if (var10.length() > 0 && var10.charAt(0) == '/') {
                              var3.add("WebAppLog/" + var1 + var10);
                           } else {
                              var3.add("WebAppLog/" + var1 + '/' + var10);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public static String getLogFileName(String var0, String var1) {
      LoggingBean var2 = getLoggingBean(var0, var1);
      return var2 == null ? null : var2.getLogFilename();
   }

   public static String getLogFileRotationDir(String var0, String var1) {
      LoggingBean var2 = getLoggingBean(var0, var1);
      return var2 == null ? null : var2.getLogFileRotationDir();
   }

   private static LoggingBean getLoggingBean(String var0, String var1) {
      HttpServer var2 = null;
      if (var0 == null) {
         var2 = WebService.defaultHttpServer();
      } else {
         var2 = WebService.getHttpServer(var0);
      }

      if (var2 == null) {
         return null;
      } else {
         WebAppServletContext var3 = var2.getServletContextManager().getContextForContextPath(var1);
         if (var3 == null) {
            return null;
         } else {
            WebAppModule var4 = var3.getWebAppModule();
            if (var4 == null) {
               return null;
            } else {
               WeblogicWebAppBean var5 = var4.getWlWebAppBean();
               return var5 == null ? null : (LoggingBean)DescriptorUtils.getFirstChildOrDefaultBean(var5, var5.getLoggings(), "Logging");
            }
         }
      }
   }
}
