package weblogic.protocol;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Locale;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.net.http.HttpsURLConnection;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.channels.RemoteChannelService;
import weblogic.server.channels.RemoteChannelServiceImpl;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.ChannelSSLSocketFactory;
import weblogic.socket.ChannelSocketFactory;

public final class URLManager {
   public static final String PREFIX_ADMIN = "admin";
   public static final String PREFIX_HTTP = "http";
   public static final String PREFIX_HTTPS = "https";
   public static final String PREFIX_LDAP = "ldap";
   public static final String PREFIX_LDAPS = "ldaps";
   private static final boolean DEBUG = false;
   private static final String ADMIN_HOST_PROP = "weblogic.management.server";
   private static final String OLD_ADMIN_HOST_PROP = "weblogic.admin.host";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static String findURL(String var0) throws UnknownHostException {
      ServerIdentity var1 = ServerIdentityManager.findServerIdentity(URLManager.DomainNameMaker.DOMAIN_NAME, var0);
      if (var1 == null) {
         throw new UnknownHostException("Could not discover URL for server '" + var0 + "'");
      } else {
         return findURL(var1);
      }
   }

   public static String findURL(String var0, Protocol var1) throws UnknownHostException {
      ServerIdentity var2 = ServerIdentityManager.findServerIdentity(URLManager.DomainNameMaker.DOMAIN_NAME, var0);
      if (var2 == null) {
         throw new UnknownHostException("Could not discover URL for server '" + var0 + "'");
      } else {
         return findURL(var2, var1);
      }
   }

   public static String findAdministrationURL(String var0) throws UnknownHostException {
      ServerIdentity var1 = ServerIdentityManager.findServerIdentity(URLManager.DomainNameMaker.DOMAIN_NAME, var0);
      if (var1 == null) {
         throw new UnknownHostException("Could not discover administration URL for server '" + var0 + "'");
      } else {
         return findAdministrationURL(var1);
      }
   }

   public static String findURL(ServerIdentity var0) {
      return ChannelHelper.createURL(ServerChannelManager.findServerChannel(var0));
   }

   public static String findURL(ServerIdentity var0, Protocol var1) {
      return ChannelHelper.createURL(ServerChannelManager.findServerChannel(var0, var1));
   }

   public static String findURL(ServerIdentity var0, String var1) {
      String var2 = ChannelHelper.createURL(ServerChannelManager.findServerChannel(var0, var1));
      if (var2 == null) {
         var2 = findURL(var0, ProtocolManager.getDefaultProtocol());
         if (var2 == null) {
            var2 = findURL(var0, ProtocolManager.getDefaultSecureProtocol());
         }
      }

      return var2;
   }

   public static String findIPv4URL(ServerIdentity var0, Protocol var1) {
      String var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv4ServerChannel(var0, var1));
      if (var2 == null) {
         var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv4ServerChannel(var0, ProtocolManager.getDefaultProtocol()));
         if (var2 == null) {
            var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv4ServerChannel(var0, ProtocolManager.getDefaultSecureProtocol()));
         }
      }

      return var2;
   }

   public static String findIPv6URL(ServerIdentity var0, Protocol var1) {
      String var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv6ServerChannel(var0, var1));
      if (var2 == null) {
         var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv6ServerChannel(var0, ProtocolManager.getDefaultProtocol()));
         if (var2 == null) {
            var2 = ChannelHelper.createURL(ServerChannelManager.getServerChannelManager().getIPv6ServerChannel(var0, ProtocolManager.getDefaultSecureProtocol()));
         }
      }

      return var2;
   }

   public static String findURL(ServerIdentity var0, String var1, boolean var2) {
      String var3 = ChannelHelper.createURL(ServerChannelManager.findServerChannel(var0, var1));
      if (var3 == null) {
         Protocol var4 = ProtocolManager.getDefaultProtocol();
         Protocol var5 = ProtocolManager.getDefaultSecureProtocol();
         if (var2) {
            var4 = ProtocolManager.getDefaultSecureProtocol();
            var5 = ProtocolManager.getDefaultProtocol();
         }

         var3 = findURL(var0, var4);
         if (var3 == null) {
            var3 = findURL(var0, var5);
         }
      }

      return var3;
   }

   public static String findAdministrationURL(ServerIdentity var0) {
      String var1 = findURL(var0, ProtocolHandlerAdmin.PROTOCOL_ADMIN);
      if (var1 == null) {
         var1 = findURL(var0, ProtocolManager.getDefaultProtocol());
         if (var1 == null) {
            var1 = findURL(var0, ProtocolManager.getDefaultSecureProtocol());
         }
      }

      return var1;
   }

   public static HttpURLConnection createAdminHttpConnection(URL var0) throws IOException {
      HttpURLConnection var1;
      if (KernelStatus.isServer()) {
         var1 = null;
         ServerChannel var4;
         if (!KernelStatus.isConfigured()) {
            var4 = ServerChannelImpl.createBootstrapChannel(var0.getProtocol());
            if (var4 == null) {
               return (HttpURLConnection)var0.openConnection();
            }
         } else if (kernelId.getQOS() == 103) {
            var4 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerAdmin.PROTOCOL_ADMIN);
         } else {
            try {
               var4 = ServerChannelManager.findOutboundServerChannel(ProtocolManager.findProtocol(var0.getProtocol()));
            } catch (UnknownProtocolException var3) {
               throw new AssertionError(var3);
            }
         }

         if (var4 != null && var4.isHttpEnabledForThisProtocol()) {
            if (var4.supportsTLS()) {
               HttpsURLConnection var5 = new HttpsURLConnection(var0);
               var5.setSSLSocketFactory((new ChannelSSLSocketFactory(var4)).initializeFromThread());
               return var5;
            } else {
               weblogic.net.http.HttpURLConnection var2 = new weblogic.net.http.HttpURLConnection(var0);
               var2.setSocketFactory(new ChannelSocketFactory(var4));
               return var2;
            }
         } else {
            throw new IOException("Admin channel does exist or does not have HTTP enabled");
         }
      } else {
         var1 = (HttpURLConnection)var0.openConnection();
         return var1;
      }
   }

   public static String normalizeToAdminProtocol(String var0) {
      int var1 = var0.indexOf("://");
      Protocol var3 = ProtocolManager.getDefaultAdminProtocol();
      boolean var4 = ChannelHelper.isLocalAdminChannelEnabled();
      String var2;
      if (var1 < 0) {
         if (var4) {
            var2 = var3.getAsURLPrefix() + "://" + var0;
         } else {
            var2 = ProtocolManager.getDefaultProtocol().getAsURLPrefix() + "://" + var0;
         }

         return var2;
      } else {
         String var5 = var0.substring(0, var1).toLowerCase(Locale.ENGLISH);
         var0 = var0.substring(var1);
         if (!var4 || !var5.equals("admin") && (!var5.equals("http") || var3.isSecure()) && (!var5.equals("https") || !var3.isSecure())) {
            if (!var5.equals("http") && !var5.equals("admin")) {
               if (var5.startsWith("https")) {
                  var2 = Kernel.getConfig().getDefaultSecureProtocol() + var0;
               } else {
                  var2 = var5 + var0;
               }
            } else {
               var2 = Kernel.getConfig().getDefaultProtocol() + var0;
            }
         } else {
            var2 = var3.getAsURLPrefix() + var0;
         }

         return var2;
      }
   }

   public static String normalizeToHttpProtocol(String var0) {
      int var1 = var0.indexOf("://");
      if (var1 < 0) {
         return "http://" + var0;
      } else {
         String var2 = var0.substring(0, var1).toLowerCase(Locale.ENGLISH);
         var0 = var0.substring(var1);
         if (var2.equals("admin")) {
            var2 = ProtocolManager.getDefaultAdminProtocol().getAsURLPrefix();
         }

         return var2.endsWith("s") ? "https" + var0 : "http" + var0;
      }
   }

   public static String normalizeToLDAPProtocol(String var0) {
      int var1 = var0.indexOf("://");
      if (var1 < 0) {
         return "ldap://" + var0;
      } else {
         String var2 = var0.substring(0, var1).toLowerCase(Locale.ENGLISH);
         var0 = var0.substring(var1);
         return var2.endsWith("s") ? "ldaps" + var0 : "ldap" + var0;
      }
   }

   public static String[] getConnectedServers() {
      return getConnectedServers(URLManager.DomainNameMaker.DOMAIN_NAME);
   }

   public static String[] getDomainConnectedServers() throws RemoteException {
      RemoteChannelService var0 = RemoteChannelServiceImpl.getDomainGateway();
      if (var0 == null) {
         throw new UnknownHostException("The admin server could not be found");
      } else {
         return var0.getConnectedServers();
      }
   }

   public static String[] getConnectedServers(String var0) {
      return ServerIdentityManager.getConnectedServers(var0);
   }

   public static boolean isConnected(String var0) {
      return isConnected(var0, URLManager.DomainNameMaker.DOMAIN_NAME);
   }

   public static boolean isConnected(String var0, String var1) {
      return ServerIdentityManager.isConnected(var0, var1);
   }

   private static final String findAdminServerURL(String var0) {
      if (!KernelStatus.isServer()) {
         return var0;
      } else {
         String var1 = null;
         String var2 = URLManager.AdminPropMaker.ADMIN_HOST;
         if (var2 == null) {
            return null;
         } else {
            int var3 = var2.indexOf("://");
            if (var3 != -1) {
               var1 = var2.substring(0, var3).toLowerCase(Locale.ENGLISH) + var2.substring(var3);
            } else {
               String var4 = var0 == null ? Kernel.getConfig().getDefaultProtocol() : var0.substring(0, var0.indexOf("://"));
               var1 = var4 + "://" + var2;
            }

            if (var1.indexOf(58) == var1.lastIndexOf(58)) {
               var1 = var1 + var0.substring(var0.lastIndexOf(58));
            }

            return var1;
         }
      }
   }

   private static final void p(String var0) {
      System.out.println("<URLManager>: " + var0);
   }

   public static String[] findAllAddressesForHost(String var0) {
      String var2 = null;
      String var3 = null;
      String var4 = var0;
      int var1;
      if ((var1 = var0.indexOf("://")) != -1) {
         var2 = var0.substring(0, var1);
         var0 = var0.substring(var1 + 3);
      }

      if ((var1 = var0.indexOf("]:")) != -1) {
         var3 = var0.substring(var1 + 2);
         var0 = var0.substring(0, var1 + 1);
      } else if ((var1 = var0.indexOf(":")) != -1) {
         var3 = var0.substring(var1 + 1);
         var0 = var0.substring(0, var1);
      }

      try {
         InetAddress[] var5 = InetAddress.getAllByName(var0);
         String[] var6 = new String[var5.length];

         for(int var7 = 0; var7 < var5.length; ++var7) {
            if (var5[var7] instanceof Inet6Address) {
               var6[var7] = "[" + var5[var7].getHostAddress() + "]";
            } else {
               var6[var7] = var5[var7].getHostAddress();
            }

            if (var2 != null && !"".equals(var2)) {
               var6[var7] = var2 + "://" + var6[var7];
            }

            if (var3 != null && !"".equals(var3)) {
               var6[var7] = var6[var7] + ":" + var3;
            }
         }

         return var6;
      } catch (Exception var8) {
         return var0.equalsIgnoreCase("localhost") ? new String[]{var4} : null;
      }
   }

   private static class DomainNameMaker {
      private static final String DOMAIN_NAME;

      static {
         DOMAIN_NAME = ManagementService.getRuntimeAccess(URLManager.kernelId).getDomainName();
      }
   }

   private static final class AdminPropMaker {
      private static final String ADMIN_HOST = getHostProp();

      private static final String getHostProp() {
         try {
            String var0 = System.getProperty("weblogic.management.server");
            if (var0 == null) {
               var0 = System.getProperty("weblogic.admin.host");
            }

            return var0;
         } catch (SecurityException var1) {
            return null;
         }
      }
   }
}
