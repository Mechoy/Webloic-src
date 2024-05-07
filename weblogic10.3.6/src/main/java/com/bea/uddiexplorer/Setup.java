package com.bea.uddiexplorer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class Setup {
   private String PUBLIC_UDDI_LINKS_FILE = "PUBLIC_UDDI_LINKS";
   private String PUBLIC_INQUIRY_REGISTRY_URLS = "publicinquiryurls";
   private String PRIVATE_INQUIRY_REGISTRY_URLS = "privateinquiryurls";
   private String PRIVATE_PUBLISH_REGISTRY_URLS = "privatepublishurls";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String serverName;
   private String defaultServerName;
   private String portNumber;
   private String DEFAULT_PRIVATE_INQUIRY_URL;
   private String DEFAULT_PRIVATE_PUBLISH_URL;

   public Setup() {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServer().getListenAddress();
      this.defaultServerName = this.serverName != null && !this.serverName.equals("") ? this.serverName : "localhost";
      this.portNumber = Integer.toString(ManagementService.getRuntimeAccess(kernelId).getServer().getListenPort());
      this.DEFAULT_PRIVATE_INQUIRY_URL = "http://" + this.defaultServerName + ":" + this.portNumber + "/uddi/uddilistener";
      this.DEFAULT_PRIVATE_PUBLISH_URL = "http://" + this.defaultServerName + ":" + this.portNumber + "/uddi/uddilistener";
   }

   public String getPrivateRegistryInquiryURL(HttpServletRequest var1) {
      return this.getPrivateRegistryURL(this.PRIVATE_INQUIRY_REGISTRY_URLS, var1);
   }

   public String getPrivateRegistryPublishURL(HttpServletRequest var1) {
      return this.getPrivateRegistryURL(this.PRIVATE_PUBLISH_REGISTRY_URLS, var1);
   }

   private String getPrivateRegistryURL(String var1, HttpServletRequest var2) {
      String var3 = null;
      Cookie var4 = this.getCookie(var1, var2.getCookies());
      var3 = var4 != null ? this.removeCrLf(var4.getValue()) : this.getDefaultPrivateRegistryURL(var1);
      return StringUtils.encodeXSS(var3);
   }

   public String getDefaultPrivateRegistryURL(String var1) {
      String var2 = "";
      if (StringUtils.equals(this.PRIVATE_INQUIRY_REGISTRY_URLS, var1)) {
         var2 = this.DEFAULT_PRIVATE_INQUIRY_URL;
      } else if (StringUtils.equals(this.PRIVATE_PUBLISH_REGISTRY_URLS, var1)) {
         var2 = this.DEFAULT_PRIVATE_PUBLISH_URL;
      }

      return var2;
   }

   public boolean setPrivateRegistryInquiryURL(String var1, HttpServletRequest var2, HttpServletResponse var3) {
      return this.setPrivateRegistryURL(var1, this.PRIVATE_INQUIRY_REGISTRY_URLS, var2, var3);
   }

   public boolean setPrivateRegistryPublishURL(String var1, HttpServletRequest var2, HttpServletResponse var3) {
      return this.setPrivateRegistryURL(var1, this.PRIVATE_PUBLISH_REGISTRY_URLS, var2, var3);
   }

   private boolean setPrivateRegistryURL(String var1, String var2, HttpServletRequest var3, HttpServletResponse var4) {
      Cookie var5 = this.getCookie(var2, var3.getCookies());
      if (var5 != null) {
         var5.setValue(this.removeCrLf(var1));
      } else {
         var5 = new Cookie(var2, this.removeCrLf(var1));
      }

      var5.setMaxAge(32000000);
      var4.addCookie(var5);
      return true;
   }

   public void addPublicRegistryURL(String var1, String var2, HttpServletRequest var3, HttpServletResponse var4) {
      Cookie var5 = this.getCookie(this.PUBLIC_INQUIRY_REGISTRY_URLS, var3.getCookies());
      if (var5 != null) {
         String var6 = this.removeCrLf(var5.getValue());
         var6 = var6 + this.removeCrLf(var1) + "!" + this.removeCrLf(var2) + "|";
         var5.setValue(var6);
      }

      var5.setMaxAge(32000000);
      var4.addCookie(var5);
   }

   public void removePublicRegistryURL(String var1, HttpServletRequest var2, HttpServletResponse var3) {
      Cookie var4 = this.getCookie(this.PUBLIC_INQUIRY_REGISTRY_URLS, var2.getCookies());
      if (var4 != null) {
         String var5 = this.removeCrLf(var4.getValue());
         StringBuffer var6 = new StringBuffer();
         StringTokenizer var7 = new StringTokenizer(var5, "|");

         while(var7.hasMoreTokens()) {
            String var8 = var7.nextToken();
            if (var8.indexOf(var1) == -1) {
               var6.append(var8 + "|");
            }
         }

         var4.setValue(this.removeCrLf(var6.toString()));
         var4.setMaxAge(32000000);
         var3.addCookie(var4);
      }

   }

   public String[][] getOperators(HttpServletRequest var1, HttpServletResponse var2) {
      String[][] var3;
      try {
         Cookie[] var4 = var1.getCookies();
         String var15 = null;

         for(int var16 = 0; var16 < var4.length; ++var16) {
            Cookie var18 = var4[var16];
            if (StringUtils.equals(var18.getName(), this.PUBLIC_INQUIRY_REGISTRY_URLS)) {
               var15 = this.removeCrLf(var18.getValue());
               break;
            }
         }

         StringTokenizer var17 = new StringTokenizer(var15, "|");
         if (var17.countTokens() <= 0) {
            throw new Exception();
         }

         var3 = new String[var17.countTokens()][];

         String var20;
         for(int var19 = 0; var17.hasMoreTokens(); var3[var19++][1] = StringUtils.encodeXSS(var20.substring(var20.indexOf("!") + 1, var20.length()))) {
            var20 = var17.nextToken();
            var3[var19] = new String[2];
            var3[var19][0] = StringUtils.encodeXSS(var20.substring(0, var20.indexOf("!")));
         }
      } catch (Exception var14) {
         try {
            InputStream var5 = null;
            URL var6 = this.getClass().getResource(this.PUBLIC_UDDI_LINKS_FILE);
            var5 = var6.openStream();
            ByteArrayOutputStream var7 = new ByteArrayOutputStream();

            int var8;
            while((var8 = var5.read()) != -1) {
               var7.write(var8);
            }

            String var9 = new String(var7.toByteArray());
            var5.close();
            var7.close();
            StringTokenizer var10 = new StringTokenizer(var9, "\n");
            if (var10.countTokens() <= 0) {
               throw new Exception();
            }

            var3 = new String[var10.countTokens()][];

            for(var8 = 0; var10.hasMoreTokens(); ++var8) {
               String var11 = var10.nextToken();
               int var12 = var11.indexOf("!");
               var3[var8] = new String[2];
               var3[var8][0] = StringUtils.encodeXSS(var11.substring(0, var12).trim());
               var3[var8][1] = StringUtils.encodeXSS(var11.substring(var12 + 1, var11.length()).trim());
            }
         } catch (Exception var13) {
            var3 = new String[][]{{"http://www-3.ibm.com/services/uddi/inquiryapi", "IBM"}, {"http://services.xmethods.net/glue/inquire/uddi", "XMethods"}, {"http://uddi.rte.microsoft.com/inquire", "Microsoft"}};
         }

         var2.addCookie(this.generateCookie(this.PUBLIC_INQUIRY_REGISTRY_URLS, var3, 32000000));
      }

      return var3;
   }

   public Cookie generateCookie(String var1, String[][] var2, int var3) {
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var4.append(var2[var5][0].trim() + "!" + var2[var5][1].trim() + "|");
      }

      Cookie var6 = new Cookie(var1, this.removeCrLf(var4.toString()));
      var6.setMaxAge(var3);
      return var6;
   }

   public Cookie getCookie(String var1, Cookie[] var2) {
      Cookie var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (StringUtils.equals(var2[var4].getName(), var1)) {
            var3 = var2[var4];
            break;
         }
      }

      return var3;
   }

   private String removeCrLf(String var1) {
      if (null != var1 && var1.length() >= 2 && (var1.indexOf(10) != -1 || var1.indexOf(12) != -1)) {
         char[] var2 = var1.toCharArray();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] == '\n' || var2[var3] == '\f') {
               var2[var3] = ' ';
            }
         }

         return new String(var2);
      } else {
         return var1;
      }
   }
}
