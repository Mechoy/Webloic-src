package weblogic.management.provider.internal;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AccessController;
import javax.mail.internet.MimeUtility;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.logging.Loggable;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.internal.BootStrapStruct;
import weblogic.management.internal.ConfigLogger;
import weblogic.management.provider.MSIService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class BootStrapHelper {
   public static final String OAM_APPNAME = "bea_wls_management_internal2";
   private static BootStrapStruct bootStrapStruct;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String IGNORE_DEPLOYMENT_STATUS_ON_ADMIN_PROPERTY = "weblogic.ignoreDeploymentStatusOnAdmin";

   static BootStrapStruct getBootStrapStruct() throws ConfigurationException {
      if (bootStrapStruct != null) {
         return bootStrapStruct;
      } else {
         String var0 = ManagementService.getPropertyService(kernelId).getTimestamp1();
         String var1 = ManagementService.getPropertyService(kernelId).getTimestamp2();
         String var2 = ManagementService.getPropertyService(kernelId).getServerName();
         URL var3 = null;
         HttpURLConnection var4 = null;
         InputStream var5 = null;
         String var6 = null;

         BootStrapStruct var11;
         try {
            ManagementService.getPropertyService(kernelId);
            var6 = PropertyService.getAdminHttpUrl();
            var3 = new URL(var6 + "/" + "bea_wls_management_internal2" + "/Bootstrap");
            var4 = URLManager.createAdminHttpConnection(var3);
            var4.setRequestProperty("username", mimeEncode(var0));
            var4.setRequestProperty("password", mimeEncode(var1));
            var4.setRequestProperty("servername", mimeEncode(var2));
            var4.setRequestProperty("Version", buildVersionString());
            var4.setRequestProperty("action", "bootstrap");
            String var7 = System.getProperty("weblogic.ignoreDeploymentStatusOnAdmin");
            if (var7 != null && var7.equals("true")) {
               var4.setRequestProperty("ignoreDeploymentStatus", "true");
            } else {
               var4.setRequestProperty("ignoreDeploymentStatus", "false");
            }

            var4.setRequestProperty("action", "bootstrap");
            var4.setDoOutput(true);
            var4.setDoInput(true);
            boolean var27 = true;

            int var28;
            try {
               var4.connect();
               var4.getHeaderField(0);
               var28 = var4.getResponseCode();
            } catch (FileNotFoundException var24) {
               var28 = 404;
            }

            String var9;
            if (var28 == 404) {
               var9 = var4.getHeaderField("UnkSvrMsg");
               if (var9 != null) {
                  throw new UnknownServerException(var9);
               }

               String var32 = var4.getHeaderField("MatchMsg");
               if (var32 != null) {
                  throw new ConfigurationException(var32);
               }

               throw new ConfigurationException(var2 + " not found");
            }

            if (var28 == 401) {
               var9 = var4.getHeaderField("ErrorMsg");
               if (var9 == null) {
                  var9 = "";
               }

               Loggable var31 = ConfigLogger.logAuthenticationFailedWhileStartingManagedServerLoggable(var0, var9);
               var31.log();
               throw new ConfigurationException(var31.getMessage());
            }

            if (var28 == 500 || var28 == 409) {
               var9 = var4.getHeaderField("ErrorMsg");
               if (var9 == null) {
                  var9 = "";
               }

               var9 = var9 + " " + var4.getResponseMessage();
               throw new ConfigurationException(var9);
            }

            if (var28 == 503) {
               Loggable var29 = ConfigLogger.logErrorConnectingToAdminServerLoggable(var6);
               var29.log();
               throw new ConfigurationException(var29.getMessage());
            }

            var9 = var4.getHeaderField("DomainVersion");
            if (var9 != null) {
               VersionInfo var10 = new VersionInfo(var9);
               if (var10.laterThan(VersionInfo.theOne())) {
                  Loggable var33 = ManagementLogger.logDomainVersionNotSupportedLoggable(var9, buildVersionString(".", true));
                  var33.log();
                  throw new ConfigurationException(var33.getMessage());
               }
            }

            var5 = var4.getInputStream();
            WLObjectInputStream var30 = new WLObjectInputStream(new BufferedInputStream(var5));
            bootStrapStruct = (BootStrapStruct)var30.readObject();
            var11 = bootStrapStruct;
         } catch (Exception var25) {
            Loggable var8 = ConfigLogger.logErrorConnectionAdminServerForBootstrapLoggable(var3, var0, var25);
            if (MSIService.getMSIService().isAdminServerAvailable()) {
               var8.log();
            } else {
               ManagementLogger.logErrorConnectingToAdminServer(var6);
            }

            if (var25 instanceof ConfigurationException) {
               throw (ConfigurationException)var25;
            }

            throw new ConfigurationException(var8.getMessage());
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var23) {
               }
            }

            if (var4 != null) {
               try {
                  var4.disconnect();
               } catch (Exception var22) {
               }
            }

         }

         return var11;
      }
   }

   private static String buildVersionString() {
      return buildVersionString(",", false);
   }

   private static String buildVersionString(String var0, boolean var1) {
      if (var0 == null) {
         var0 = ",";
      }

      StringBuffer var2 = new StringBuffer();
      var2.append(new Integer(VersionInfo.theOne().getMajor()));
      var2.append(var0);
      var2.append(new Integer(VersionInfo.theOne().getMinor()));
      var2.append(var0);
      var2.append(new Integer(VersionInfo.theOne().getServicePack()));
      if (var1) {
         var2.append(var0);
         var2.append(new Integer(VersionInfo.theOne().getRollingPatch()));
      }

      return var2.toString();
   }

   private static String mimeEncode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.encodeText(var0, "UTF-8", (String)null);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   static class UnknownServerException extends ConfigurationException {
      public UnknownServerException(String var1) {
         super(var1);
      }
   }
}
